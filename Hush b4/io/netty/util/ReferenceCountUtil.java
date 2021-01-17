// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import io.netty.util.internal.StringUtil;
import io.netty.util.internal.logging.InternalLoggerFactory;
import io.netty.util.internal.logging.InternalLogger;

public final class ReferenceCountUtil
{
    private static final InternalLogger logger;
    
    public static <T> T retain(final T msg) {
        if (msg instanceof ReferenceCounted) {
            return (T)((ReferenceCounted)msg).retain();
        }
        return msg;
    }
    
    public static <T> T retain(final T msg, final int increment) {
        if (msg instanceof ReferenceCounted) {
            return (T)((ReferenceCounted)msg).retain(increment);
        }
        return msg;
    }
    
    public static boolean release(final Object msg) {
        return msg instanceof ReferenceCounted && ((ReferenceCounted)msg).release();
    }
    
    public static boolean release(final Object msg, final int decrement) {
        return msg instanceof ReferenceCounted && ((ReferenceCounted)msg).release(decrement);
    }
    
    public static void safeRelease(final Object msg) {
        try {
            release(msg);
        }
        catch (Throwable t) {
            ReferenceCountUtil.logger.warn("Failed to release a message: {}", msg, t);
        }
    }
    
    public static void safeRelease(final Object msg, final int decrement) {
        try {
            release(msg, decrement);
        }
        catch (Throwable t) {
            if (ReferenceCountUtil.logger.isWarnEnabled()) {
                ReferenceCountUtil.logger.warn("Failed to release a message: {} (decrement: {})", msg, decrement, t);
            }
        }
    }
    
    public static <T> T releaseLater(final T msg) {
        return releaseLater(msg, 1);
    }
    
    public static <T> T releaseLater(final T msg, final int decrement) {
        if (msg instanceof ReferenceCounted) {
            ThreadDeathWatcher.watch(Thread.currentThread(), new ReleasingTask((ReferenceCounted)msg, decrement));
        }
        return msg;
    }
    
    private ReferenceCountUtil() {
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ReferenceCountUtil.class);
    }
    
    private static final class ReleasingTask implements Runnable
    {
        private final ReferenceCounted obj;
        private final int decrement;
        
        ReleasingTask(final ReferenceCounted obj, final int decrement) {
            this.obj = obj;
            this.decrement = decrement;
        }
        
        @Override
        public void run() {
            try {
                if (!this.obj.release(this.decrement)) {
                    ReferenceCountUtil.logger.warn("Non-zero refCnt: {}", this);
                }
                else {
                    ReferenceCountUtil.logger.debug("Released: {}", this);
                }
            }
            catch (Exception ex) {
                ReferenceCountUtil.logger.warn("Failed to release an object: {}", this.obj, ex);
            }
        }
        
        @Override
        public String toString() {
            return StringUtil.simpleClassName(this.obj) + ".release(" + this.decrement + ") refCnt: " + this.obj.refCnt();
        }
    }
}
