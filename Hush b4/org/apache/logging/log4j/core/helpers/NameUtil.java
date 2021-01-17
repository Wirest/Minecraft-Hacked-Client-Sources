// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import java.security.MessageDigest;

public final class NameUtil
{
    private static final int MASK = 255;
    
    private NameUtil() {
    }
    
    public static String getSubName(final String name) {
        if (name.isEmpty()) {
            return null;
        }
        final int i = name.lastIndexOf(46);
        return (i > 0) ? name.substring(0, i) : "";
    }
    
    public static String md5(final String string) {
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(string.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder md5 = new StringBuilder();
            for (final byte b : bytes) {
                final String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1) {
                    md5.append('0');
                }
                md5.append(hex);
            }
            return md5.toString();
        }
        catch (Exception ex) {
            return string;
        }
    }
}
