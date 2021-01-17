// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.util.internal.SystemPropertyUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.PlatformDependent;
import io.netty.util.internal.logging.InternalLogger;

public final class ZlibCodecFactory
{
    private static final InternalLogger logger;
    private static final boolean noJdkZlibDecoder;
    
    public static ZlibEncoder newZlibEncoder(final int compressionLevel) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(compressionLevel);
        }
        return new JdkZlibEncoder(compressionLevel);
    }
    
    public static ZlibEncoder newZlibEncoder(final ZlibWrapper wrapper) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(wrapper);
        }
        return new JdkZlibEncoder(wrapper);
    }
    
    public static ZlibEncoder newZlibEncoder(final ZlibWrapper wrapper, final int compressionLevel) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(wrapper, compressionLevel);
        }
        return new JdkZlibEncoder(wrapper, compressionLevel);
    }
    
    public static ZlibEncoder newZlibEncoder(final ZlibWrapper wrapper, final int compressionLevel, final int windowBits, final int memLevel) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(wrapper, compressionLevel, windowBits, memLevel);
        }
        return new JdkZlibEncoder(wrapper, compressionLevel);
    }
    
    public static ZlibEncoder newZlibEncoder(final byte[] dictionary) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(dictionary);
        }
        return new JdkZlibEncoder(dictionary);
    }
    
    public static ZlibEncoder newZlibEncoder(final int compressionLevel, final byte[] dictionary) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(compressionLevel, dictionary);
        }
        return new JdkZlibEncoder(compressionLevel, dictionary);
    }
    
    public static ZlibEncoder newZlibEncoder(final int compressionLevel, final int windowBits, final int memLevel, final byte[] dictionary) {
        if (PlatformDependent.javaVersion() < 7) {
            return new JZlibEncoder(compressionLevel, windowBits, memLevel, dictionary);
        }
        return new JdkZlibEncoder(compressionLevel, dictionary);
    }
    
    public static ZlibDecoder newZlibDecoder() {
        if (PlatformDependent.javaVersion() < 7 || ZlibCodecFactory.noJdkZlibDecoder) {
            return new JZlibDecoder();
        }
        return new JdkZlibDecoder();
    }
    
    public static ZlibDecoder newZlibDecoder(final ZlibWrapper wrapper) {
        if (PlatformDependent.javaVersion() < 7 || ZlibCodecFactory.noJdkZlibDecoder) {
            return new JZlibDecoder(wrapper);
        }
        return new JdkZlibDecoder(wrapper);
    }
    
    public static ZlibDecoder newZlibDecoder(final byte[] dictionary) {
        if (PlatformDependent.javaVersion() < 7 || ZlibCodecFactory.noJdkZlibDecoder) {
            return new JZlibDecoder(dictionary);
        }
        return new JdkZlibDecoder(dictionary);
    }
    
    private ZlibCodecFactory() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ZlibCodecFactory.class);
        noJdkZlibDecoder = SystemPropertyUtil.getBoolean("io.netty.noJdkZlibDecoder", true);
        ZlibCodecFactory.logger.debug("-Dio.netty.noJdkZlibDecoder: {}", (Object)ZlibCodecFactory.noJdkZlibDecoder);
    }
}
