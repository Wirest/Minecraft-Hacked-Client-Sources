package com.sun.jna;

import java.util.Arrays;
import java.util.Collection;

public abstract interface Callback {
    public static final String METHOD_NAME = "callback";
    public static final Collection FORBIDDEN_NAMES = Arrays.asList(new String[]{"hashCode", "equals", "toString"});

    public static abstract interface UncaughtExceptionHandler {
        public abstract void uncaughtException(Callback paramCallback, Throwable paramThrowable);
    }
}




