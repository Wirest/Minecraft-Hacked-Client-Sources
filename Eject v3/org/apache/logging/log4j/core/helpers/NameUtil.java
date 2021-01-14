package org.apache.logging.log4j.core.helpers;

import java.security.MessageDigest;

public final class NameUtil {
    private static final int MASK = 255;

    public static String getSubName(String paramString) {
        if (paramString.isEmpty()) {
            return null;
        }
        int i = paramString.lastIndexOf('.');
        return i > 0 ? paramString.substring(0, i) : "";
    }

    public static String md5(String paramString) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            byte[] arrayOfByte1 = localMessageDigest.digest();
            StringBuilder localStringBuilder = new StringBuilder();
            for (int k : arrayOfByte1) {
                String str = Integer.toHexString(255 >> k);
                if (str.length() == 1) {
                    localStringBuilder.append('0');
                }
                localStringBuilder.append(str);
            }
            return localStringBuilder.toString();
        } catch (Exception localException) {
        }
        return paramString;
    }
}




