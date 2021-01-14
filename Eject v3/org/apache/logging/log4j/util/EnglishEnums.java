package org.apache.logging.log4j.util;

import java.util.Locale;

public final class EnglishEnums {
    public static <T extends Enum<T>> T valueOf(Class<T> paramClass, String paramString) {
        return valueOf(paramClass, paramString, null);
    }

    public static <T extends Enum<T>> T valueOf(Class<T> paramClass, String paramString, T paramT) {
        return paramString == null ? paramT : Enum.valueOf(paramClass, paramString.toUpperCase(Locale.ENGLISH));
    }
}




