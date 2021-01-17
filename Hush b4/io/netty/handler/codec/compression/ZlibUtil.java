// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import com.jcraft.jzlib.JZlib;
import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.Inflater;

final class ZlibUtil
{
    static void fail(final Inflater z, final String message, final int resultCode) {
        throw inflaterException(z, message, resultCode);
    }
    
    static void fail(final Deflater z, final String message, final int resultCode) {
        throw deflaterException(z, message, resultCode);
    }
    
    static DecompressionException inflaterException(final Inflater z, final String message, final int resultCode) {
        return new DecompressionException(message + " (" + resultCode + ')' + ((z.msg != null) ? (": " + z.msg) : ""));
    }
    
    static CompressionException deflaterException(final Deflater z, final String message, final int resultCode) {
        return new CompressionException(message + " (" + resultCode + ')' + ((z.msg != null) ? (": " + z.msg) : ""));
    }
    
    static JZlib.WrapperType convertWrapperType(final ZlibWrapper wrapper) {
        JZlib.WrapperType convertedWrapperType = null;
        switch (wrapper) {
            case NONE: {
                convertedWrapperType = JZlib.W_NONE;
                break;
            }
            case ZLIB: {
                convertedWrapperType = JZlib.W_ZLIB;
                break;
            }
            case GZIP: {
                convertedWrapperType = JZlib.W_GZIP;
                break;
            }
            case ZLIB_OR_NONE: {
                convertedWrapperType = JZlib.W_ANY;
                break;
            }
            default: {
                throw new Error();
            }
        }
        return convertedWrapperType;
    }
    
    static int wrapperOverhead(final ZlibWrapper wrapper) {
        int overhead = 0;
        switch (wrapper) {
            case NONE: {
                overhead = 0;
                break;
            }
            case ZLIB:
            case ZLIB_OR_NONE: {
                overhead = 2;
                break;
            }
            case GZIP: {
                overhead = 10;
                break;
            }
            default: {
                throw new Error();
            }
        }
        return overhead;
    }
    
    private ZlibUtil() {
    }
}
