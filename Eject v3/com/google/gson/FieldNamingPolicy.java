package com.google.gson;

public enum FieldNamingPolicy
        implements FieldNamingStrategy {
    IDENTITY, UPPER_CAMEL_CASE, UPPER_CAMEL_CASE_WITH_SPACES, LOWER_CASE_WITH_UNDERSCORES, LOWER_CASE_WITH_DASHES;

    private FieldNamingPolicy() {
    }

    private static String separateCamelCase(String paramString1, String paramString2) {
        StringBuilder localStringBuilder = new StringBuilder();
        for (int i = 0; i < paramString1.length(); i++) {
            char c = paramString1.charAt(i);
            if ((Character.isUpperCase(c)) && (localStringBuilder.length() != 0)) {
                localStringBuilder.append(paramString2);
            }
            localStringBuilder.append(c);
        }
        return localStringBuilder.toString();
    }

    private static String upperCaseFirstLetter(String paramString) {
        StringBuilder localStringBuilder = new StringBuilder();
        int i = 0;
        for (char c = paramString.charAt(i); (i < paramString.length() - 1) && (!Character.isLetter(c)); c = paramString.charAt(++i)) {
            localStringBuilder.append(c);
        }
        if (i == paramString.length()) {
            return localStringBuilder.toString();
        }
        if (!Character.isUpperCase(c)) {
            String str = modifyString(Character.toUpperCase(c), paramString, ++i);
            return str;
        }
        return paramString;
    }

    private static String modifyString(char paramChar, String paramString, int paramInt) {
        return paramInt < paramString.length() ? paramChar + paramString.substring(paramInt) : String.valueOf(paramChar);
    }
}




