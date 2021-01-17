// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

public abstract class InternalLoggerFactory
{
    private static volatile InternalLoggerFactory defaultFactory;
    
    private static InternalLoggerFactory newDefaultFactory(final String name) {
        InternalLoggerFactory f;
        try {
            f = new Slf4JLoggerFactory(true);
            f.newInstance(name).debug("Using SLF4J as the default logging framework");
        }
        catch (Throwable t1) {
            try {
                f = new Log4JLoggerFactory();
                f.newInstance(name).debug("Using Log4J as the default logging framework");
            }
            catch (Throwable t2) {
                f = new JdkLoggerFactory();
                f.newInstance(name).debug("Using java.util.logging as the default logging framework");
            }
        }
        return f;
    }
    
    public static InternalLoggerFactory getDefaultFactory() {
        return InternalLoggerFactory.defaultFactory;
    }
    
    public static void setDefaultFactory(final InternalLoggerFactory defaultFactory) {
        if (defaultFactory == null) {
            throw new NullPointerException("defaultFactory");
        }
        InternalLoggerFactory.defaultFactory = defaultFactory;
    }
    
    public static InternalLogger getInstance(final Class<?> clazz) {
        return getInstance(clazz.getName());
    }
    
    public static InternalLogger getInstance(final String name) {
        return getDefaultFactory().newInstance(name);
    }
    
    protected abstract InternalLogger newInstance(final String p0);
    
    static {
        InternalLoggerFactory.defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());
    }
}
