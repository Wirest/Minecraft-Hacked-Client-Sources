package joptsimple.internal;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public final class Strings {
    public static final String EMPTY = "";
    public static final String SINGLE_QUOTE = "'";
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private Strings() {
        throw new UnsupportedOperationException();
    }

    public static String repeat(char paramChar, int paramInt) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramInt; i++) {
            localStringBuilder.append(paramChar);
        }
        return localStringBuilder.toString();
    }

    public static boolean isNullOrEmpty(String paramString) {
        return (paramString == null) || ("".equals(paramString));
    }

    public static String surround(String paramString, char paramChar1, char paramChar2) {
        return paramChar1 + paramString + paramChar2;
    }

    public static String join(String[] paramArrayOfString, String paramString) {
        return join(Arrays.asList(paramArrayOfString), paramString);
    }

    public static String join(List<String> paramList, String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        Iterator localIterator = paramList.iterator();
        while (localIterator.hasNext()) {
            localStringBuilder.append((String) localIterator.next());
            if (localIterator.hasNext()) {
                localStringBuilder.append(paramString);
            }
        }
        return localStringBuilder.toString();
    }
}




