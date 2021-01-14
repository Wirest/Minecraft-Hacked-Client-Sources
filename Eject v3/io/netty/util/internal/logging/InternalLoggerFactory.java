package io.netty.util.internal.logging;

public abstract class InternalLoggerFactory {
    private static volatile InternalLoggerFactory defaultFactory = newDefaultFactory(InternalLoggerFactory.class.getName());

    private static InternalLoggerFactory newDefaultFactory(String paramString) {
        Object localObject;
        try {
            localObject = new Slf4JLoggerFactory(true);
            ((InternalLoggerFactory) localObject).newInstance(paramString).debug("Using SLF4J as the default logging framework");
        } catch (Throwable localThrowable1) {
            try {
                localObject = new Log4JLoggerFactory();
                ((InternalLoggerFactory) localObject).newInstance(paramString).debug("Using Log4J as the default logging framework");
            } catch (Throwable localThrowable2) {
                localObject = new JdkLoggerFactory();
                ((InternalLoggerFactory) localObject).newInstance(paramString).debug("Using java.util.logging as the default logging framework");
            }
        }
        return (InternalLoggerFactory) localObject;
    }

    public static InternalLoggerFactory getDefaultFactory() {
        return defaultFactory;
    }

    public static void setDefaultFactory(InternalLoggerFactory paramInternalLoggerFactory) {
        if (paramInternalLoggerFactory == null) {
            throw new NullPointerException("defaultFactory");
        }
        defaultFactory = paramInternalLoggerFactory;
    }

    public static InternalLogger getInstance(Class<?> paramClass) {
        return getInstance(paramClass.getName());
    }

    public static InternalLogger getInstance(String paramString) {
        return getDefaultFactory().newInstance(paramString);
    }

    protected abstract InternalLogger newInstance(String paramString);
}




