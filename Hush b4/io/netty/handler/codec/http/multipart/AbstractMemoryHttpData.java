// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import java.io.FileOutputStream;
import io.netty.handler.codec.http.HttpConstants;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.io.FileInputStream;
import java.io.File;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import io.netty.buffer.ByteBuf;

public abstract class AbstractMemoryHttpData extends AbstractHttpData
{
    private ByteBuf byteBuf;
    private int chunkPosition;
    protected boolean isRenamed;
    
    protected AbstractMemoryHttpData(final String name, final Charset charset, final long size) {
        super(name, charset, size);
    }
    
    @Override
    public void setContent(final ByteBuf buffer) throws IOException {
        if (buffer == null) {
            throw new NullPointerException("buffer");
        }
        final long localsize = buffer.readableBytes();
        if (this.definedSize > 0L && this.definedSize < localsize) {
            throw new IOException("Out of size: " + localsize + " > " + this.definedSize);
        }
        if (this.byteBuf != null) {
            this.byteBuf.release();
        }
        this.byteBuf = buffer;
        this.size = localsize;
        this.completed = true;
    }
    
    @Override
    public void setContent(final InputStream inputStream) throws IOException {
        if (inputStream == null) {
            throw new NullPointerException("inputStream");
        }
        final ByteBuf buffer = Unpooled.buffer();
        final byte[] bytes = new byte[16384];
        int read = inputStream.read(bytes);
        int written = 0;
        while (read > 0) {
            buffer.writeBytes(bytes, 0, read);
            written += read;
            read = inputStream.read(bytes);
        }
        this.size = written;
        if (this.definedSize > 0L && this.definedSize < this.size) {
            throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
        }
        if (this.byteBuf != null) {
            this.byteBuf.release();
        }
        this.byteBuf = buffer;
        this.completed = true;
    }
    
    @Override
    public void addContent(final ByteBuf buffer, final boolean last) throws IOException {
        if (buffer != null) {
            final long localsize = buffer.readableBytes();
            if (this.definedSize > 0L && this.definedSize < this.size + localsize) {
                throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
            }
            this.size += localsize;
            if (this.byteBuf == null) {
                this.byteBuf = buffer;
            }
            else if (this.byteBuf instanceof CompositeByteBuf) {
                final CompositeByteBuf cbb = (CompositeByteBuf)this.byteBuf;
                cbb.addComponent(buffer);
                cbb.writerIndex(cbb.writerIndex() + buffer.readableBytes());
            }
            else {
                final CompositeByteBuf cbb = Unpooled.compositeBuffer(Integer.MAX_VALUE);
                cbb.addComponents(this.byteBuf, buffer);
                cbb.writerIndex(this.byteBuf.readableBytes() + buffer.readableBytes());
                this.byteBuf = cbb;
            }
        }
        if (last) {
            this.completed = true;
        }
        else if (buffer == null) {
            throw new NullPointerException("buffer");
        }
    }
    
    @Override
    public void setContent(final File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("file");
        }
        final long newsize = file.length();
        if (newsize > 2147483647L) {
            throw new IllegalArgumentException("File too big to be loaded in memory");
        }
        final FileInputStream inputStream = new FileInputStream(file);
        final FileChannel fileChannel = inputStream.getChannel();
        final byte[] array = new byte[(int)newsize];
        final ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        for (int read = 0; read < newsize; read += fileChannel.read(byteBuffer)) {}
        fileChannel.close();
        inputStream.close();
        byteBuffer.flip();
        if (this.byteBuf != null) {
            this.byteBuf.release();
        }
        this.byteBuf = Unpooled.wrappedBuffer(Integer.MAX_VALUE, byteBuffer);
        this.size = newsize;
        this.completed = true;
    }
    
    @Override
    public void delete() {
        if (this.byteBuf != null) {
            this.byteBuf.release();
            this.byteBuf = null;
        }
    }
    
    @Override
    public byte[] get() {
        if (this.byteBuf == null) {
            return Unpooled.EMPTY_BUFFER.array();
        }
        final byte[] array = new byte[this.byteBuf.readableBytes()];
        this.byteBuf.getBytes(this.byteBuf.readerIndex(), array);
        return array;
    }
    
    @Override
    public String getString() {
        return this.getString(HttpConstants.DEFAULT_CHARSET);
    }
    
    @Override
    public String getString(Charset encoding) {
        if (this.byteBuf == null) {
            return "";
        }
        if (encoding == null) {
            encoding = HttpConstants.DEFAULT_CHARSET;
        }
        return this.byteBuf.toString(encoding);
    }
    
    @Override
    public ByteBuf getByteBuf() {
        return this.byteBuf;
    }
    
    @Override
    public ByteBuf getChunk(final int length) throws IOException {
        if (this.byteBuf == null || length == 0 || this.byteBuf.readableBytes() == 0) {
            this.chunkPosition = 0;
            return Unpooled.EMPTY_BUFFER;
        }
        final int sizeLeft = this.byteBuf.readableBytes() - this.chunkPosition;
        if (sizeLeft == 0) {
            this.chunkPosition = 0;
            return Unpooled.EMPTY_BUFFER;
        }
        int sliceLength;
        if (sizeLeft < (sliceLength = length)) {
            sliceLength = sizeLeft;
        }
        final ByteBuf chunk = this.byteBuf.slice(this.chunkPosition, sliceLength).retain();
        this.chunkPosition += sliceLength;
        return chunk;
    }
    
    @Override
    public boolean isInMemory() {
        return true;
    }
    
    @Override
    public boolean renameTo(final File dest) throws IOException {
        if (dest == null) {
            throw new NullPointerException("dest");
        }
        if (this.byteBuf == null) {
            dest.createNewFile();
            return this.isRenamed = true;
        }
        final int length = this.byteBuf.readableBytes();
        final FileOutputStream outputStream = new FileOutputStream(dest);
        final FileChannel fileChannel = outputStream.getChannel();
        int written = 0;
        if (this.byteBuf.nioBufferCount() == 1) {
            for (ByteBuffer byteBuffer = this.byteBuf.nioBuffer(); written < length; written += fileChannel.write(byteBuffer)) {}
        }
        else {
            for (ByteBuffer[] byteBuffers = this.byteBuf.nioBuffers(); written < length; written += (int)fileChannel.write(byteBuffers)) {}
        }
        fileChannel.force(false);
        fileChannel.close();
        outputStream.close();
        this.isRenamed = true;
        return written == length;
    }
    
    @Override
    public File getFile() throws IOException {
        throw new IOException("Not represented by a file");
    }
}
