// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.sevenz;

import java.io.FilterInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Deflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.Inflater;
import java.io.FilterOutputStream;
import org.tukaani.xz.FinishableOutputStream;
import org.tukaani.xz.FinishableWrapperOutputStream;
import org.tukaani.xz.LZMAInputStream;
import org.tukaani.xz.SPARCOptions;
import org.tukaani.xz.ARMThumbOptions;
import org.tukaani.xz.ARMOptions;
import org.tukaani.xz.IA64Options;
import org.tukaani.xz.PowerPCOptions;
import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.X86Options;
import java.util.HashMap;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.io.InputStream;
import java.util.Map;

class Coders
{
    private static final Map<SevenZMethod, CoderBase> CODER_MAP;
    
    static CoderBase findByMethod(final SevenZMethod method) {
        return Coders.CODER_MAP.get(method);
    }
    
    static InputStream addDecoder(final InputStream is, final Coder coder, final byte[] password) throws IOException {
        final CoderBase cb = findByMethod(SevenZMethod.byId(coder.decompressionMethodId));
        if (cb == null) {
            throw new IOException("Unsupported compression method " + Arrays.toString(coder.decompressionMethodId));
        }
        return cb.decode(is, coder, password);
    }
    
    static OutputStream addEncoder(final OutputStream out, final SevenZMethod method, final Object options) throws IOException {
        final CoderBase cb = findByMethod(method);
        if (cb == null) {
            throw new IOException("Unsupported compression method " + method);
        }
        return cb.encode(out, options);
    }
    
    static {
        CODER_MAP = new HashMap<SevenZMethod, CoderBase>() {
            private static final long serialVersionUID = 1664829131806520867L;
            
            {
                ((HashMap<SevenZMethod, CopyDecoder>)this).put(SevenZMethod.COPY, new CopyDecoder());
                ((HashMap<SevenZMethod, LZMADecoder>)this).put(SevenZMethod.LZMA, new LZMADecoder());
                ((HashMap<SevenZMethod, LZMA2Decoder>)this).put(SevenZMethod.LZMA2, new LZMA2Decoder());
                ((HashMap<SevenZMethod, DeflateDecoder>)this).put(SevenZMethod.DEFLATE, new DeflateDecoder());
                ((HashMap<SevenZMethod, BZIP2Decoder>)this).put(SevenZMethod.BZIP2, new BZIP2Decoder());
                ((HashMap<SevenZMethod, AES256SHA256Decoder>)this).put(SevenZMethod.AES256SHA256, new AES256SHA256Decoder());
                ((HashMap<SevenZMethod, BCJDecoder>)this).put(SevenZMethod.BCJ_X86_FILTER, new BCJDecoder((FilterOptions)new X86Options()));
                ((HashMap<SevenZMethod, BCJDecoder>)this).put(SevenZMethod.BCJ_PPC_FILTER, new BCJDecoder((FilterOptions)new PowerPCOptions()));
                ((HashMap<SevenZMethod, BCJDecoder>)this).put(SevenZMethod.BCJ_IA64_FILTER, new BCJDecoder((FilterOptions)new IA64Options()));
                ((HashMap<SevenZMethod, BCJDecoder>)this).put(SevenZMethod.BCJ_ARM_FILTER, new BCJDecoder((FilterOptions)new ARMOptions()));
                ((HashMap<SevenZMethod, BCJDecoder>)this).put(SevenZMethod.BCJ_ARM_THUMB_FILTER, new BCJDecoder((FilterOptions)new ARMThumbOptions()));
                ((HashMap<SevenZMethod, BCJDecoder>)this).put(SevenZMethod.BCJ_SPARC_FILTER, new BCJDecoder((FilterOptions)new SPARCOptions()));
                ((HashMap<SevenZMethod, DeltaDecoder>)this).put(SevenZMethod.DELTA_FILTER, new DeltaDecoder());
            }
        };
    }
    
    static class CopyDecoder extends CoderBase
    {
        CopyDecoder() {
            super((Class<?>[])new Class[0]);
        }
        
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            return in;
        }
        
        @Override
        OutputStream encode(final OutputStream out, final Object options) {
            return out;
        }
    }
    
    static class LZMADecoder extends CoderBase
    {
        LZMADecoder() {
            super((Class<?>[])new Class[0]);
        }
        
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            final byte propsByte = coder.properties[0];
            long dictSize = coder.properties[1];
            for (int i = 1; i < 4; ++i) {
                dictSize |= ((long)coder.properties[i + 1] & 0xFFL) << 8 * i;
            }
            if (dictSize > 2147483632L) {
                throw new IOException("Dictionary larger than 4GiB maximum size");
            }
            return (InputStream)new LZMAInputStream(in, -1L, propsByte, (int)dictSize);
        }
    }
    
    static class BCJDecoder extends CoderBase
    {
        private final FilterOptions opts;
        
        BCJDecoder(final FilterOptions opts) {
            super((Class<?>[])new Class[0]);
            this.opts = opts;
        }
        
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            try {
                return this.opts.getInputStream(in);
            }
            catch (AssertionError e) {
                final IOException ex = new IOException("BCJ filter needs XZ for Java > 1.4 - see http://commons.apache.org/proper/commons-compress/limitations.html#7Z");
                ex.initCause(e);
                throw ex;
            }
        }
        
        @Override
        OutputStream encode(final OutputStream out, final Object options) {
            final FinishableOutputStream fo = this.opts.getOutputStream((FinishableOutputStream)new FinishableWrapperOutputStream(out));
            return new FilterOutputStream(fo) {
                @Override
                public void flush() {
                }
            };
        }
    }
    
    static class DeflateDecoder extends CoderBase
    {
        DeflateDecoder() {
            super((Class<?>[])new Class[] { Number.class });
        }
        
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            return new InflaterInputStream(new DummyByteAddingInputStream(in), new Inflater(true));
        }
        
        @Override
        OutputStream encode(final OutputStream out, final Object options) {
            final int level = CoderBase.numberOptionOrDefault(options, 9);
            return new DeflaterOutputStream(out, new Deflater(level, true));
        }
    }
    
    static class BZIP2Decoder extends CoderBase
    {
        BZIP2Decoder() {
            super((Class<?>[])new Class[] { Number.class });
        }
        
        @Override
        InputStream decode(final InputStream in, final Coder coder, final byte[] password) throws IOException {
            return new BZip2CompressorInputStream(in);
        }
        
        @Override
        OutputStream encode(final OutputStream out, final Object options) throws IOException {
            final int blockSize = CoderBase.numberOptionOrDefault(options, 9);
            return new BZip2CompressorOutputStream(out, blockSize);
        }
    }
    
    private static class DummyByteAddingInputStream extends FilterInputStream
    {
        private boolean addDummyByte;
        
        private DummyByteAddingInputStream(final InputStream in) {
            super(in);
            this.addDummyByte = true;
        }
        
        @Override
        public int read() throws IOException {
            int result = super.read();
            if (result == -1 && this.addDummyByte) {
                this.addDummyByte = false;
                result = 0;
            }
            return result;
        }
        
        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            final int result = super.read(b, off, len);
            if (result == -1 && this.addDummyByte) {
                this.addDummyByte = false;
                b[off] = 0;
                return 1;
            }
            return result;
        }
    }
}
