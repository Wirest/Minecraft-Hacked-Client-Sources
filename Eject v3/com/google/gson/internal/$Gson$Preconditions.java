package com.google.gson.internal;

public final class $Gson$Preconditions {
    public static <T> T checkNotNull(T paramT) {
        if (paramT == null) {
            throw new NullPointerException();
        }
        return paramT;
    }

    public static void checkArgument(boolean paramBoolean) {
        if (!paramBoolean) {
            throw new IllegalArgumentException();
        }
    }
}




