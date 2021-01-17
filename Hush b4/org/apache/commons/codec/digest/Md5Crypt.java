// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.digest;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.apache.commons.codec.Charsets;

public class Md5Crypt
{
    static final String APR1_PREFIX = "$apr1$";
    private static final int BLOCKSIZE = 16;
    static final String MD5_PREFIX = "$1$";
    private static final int ROUNDS = 1000;
    
    public static String apr1Crypt(final byte[] keyBytes) {
        return apr1Crypt(keyBytes, "$apr1$" + B64.getRandomSalt(8));
    }
    
    public static String apr1Crypt(final byte[] keyBytes, String salt) {
        if (salt != null && !salt.startsWith("$apr1$")) {
            salt = "$apr1$" + salt;
        }
        return md5Crypt(keyBytes, salt, "$apr1$");
    }
    
    public static String apr1Crypt(final String keyBytes) {
        return apr1Crypt(keyBytes.getBytes(Charsets.UTF_8));
    }
    
    public static String apr1Crypt(final String keyBytes, final String salt) {
        return apr1Crypt(keyBytes.getBytes(Charsets.UTF_8), salt);
    }
    
    public static String md5Crypt(final byte[] keyBytes) {
        return md5Crypt(keyBytes, "$1$" + B64.getRandomSalt(8));
    }
    
    public static String md5Crypt(final byte[] keyBytes, final String salt) {
        return md5Crypt(keyBytes, salt, "$1$");
    }
    
    public static String md5Crypt(final byte[] keyBytes, final String salt, final String prefix) {
        final int keyLen = keyBytes.length;
        String saltString;
        if (salt == null) {
            saltString = B64.getRandomSalt(8);
        }
        else {
            final Pattern p = Pattern.compile("^" + prefix.replace("$", "\\$") + "([\\.\\/a-zA-Z0-9]{1,8}).*");
            final Matcher m = p.matcher(salt);
            if (m == null || !m.find()) {
                throw new IllegalArgumentException("Invalid salt value: " + salt);
            }
            saltString = m.group(1);
        }
        final byte[] saltBytes = saltString.getBytes(Charsets.UTF_8);
        final MessageDigest ctx = DigestUtils.getMd5Digest();
        ctx.update(keyBytes);
        ctx.update(prefix.getBytes(Charsets.UTF_8));
        ctx.update(saltBytes);
        MessageDigest ctx2 = DigestUtils.getMd5Digest();
        ctx2.update(keyBytes);
        ctx2.update(saltBytes);
        ctx2.update(keyBytes);
        byte[] finalb = ctx2.digest();
        for (int ii = keyLen; ii > 0; ii -= 16) {
            ctx.update(finalb, 0, (ii > 16) ? 16 : ii);
        }
        Arrays.fill(finalb, (byte)0);
        int ii = keyLen;
        final int j = 0;
        while (ii > 0) {
            if ((ii & 0x1) == 0x1) {
                ctx.update(finalb[0]);
            }
            else {
                ctx.update(keyBytes[0]);
            }
            ii >>= 1;
        }
        final StringBuilder passwd = new StringBuilder(prefix + saltString + "$");
        finalb = ctx.digest();
        for (int i = 0; i < 1000; ++i) {
            ctx2 = DigestUtils.getMd5Digest();
            if ((i & 0x1) != 0x0) {
                ctx2.update(keyBytes);
            }
            else {
                ctx2.update(finalb, 0, 16);
            }
            if (i % 3 != 0) {
                ctx2.update(saltBytes);
            }
            if (i % 7 != 0) {
                ctx2.update(keyBytes);
            }
            if ((i & 0x1) != 0x0) {
                ctx2.update(finalb, 0, 16);
            }
            else {
                ctx2.update(keyBytes);
            }
            finalb = ctx2.digest();
        }
        B64.b64from24bit(finalb[0], finalb[6], finalb[12], 4, passwd);
        B64.b64from24bit(finalb[1], finalb[7], finalb[13], 4, passwd);
        B64.b64from24bit(finalb[2], finalb[8], finalb[14], 4, passwd);
        B64.b64from24bit(finalb[3], finalb[9], finalb[15], 4, passwd);
        B64.b64from24bit(finalb[4], finalb[10], finalb[5], 4, passwd);
        B64.b64from24bit((byte)0, (byte)0, finalb[11], 2, passwd);
        ctx.reset();
        ctx2.reset();
        Arrays.fill(keyBytes, (byte)0);
        Arrays.fill(saltBytes, (byte)0);
        Arrays.fill(finalb, (byte)0);
        return passwd.toString();
    }
}
