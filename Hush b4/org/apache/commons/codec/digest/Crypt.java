// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.digest;

import org.apache.commons.codec.Charsets;

public class Crypt
{
    public static String crypt(final byte[] keyBytes) {
        return crypt(keyBytes, null);
    }
    
    public static String crypt(final byte[] keyBytes, final String salt) {
        if (salt == null) {
            return Sha2Crypt.sha512Crypt(keyBytes);
        }
        if (salt.startsWith("$6$")) {
            return Sha2Crypt.sha512Crypt(keyBytes, salt);
        }
        if (salt.startsWith("$5$")) {
            return Sha2Crypt.sha256Crypt(keyBytes, salt);
        }
        if (salt.startsWith("$1$")) {
            return Md5Crypt.md5Crypt(keyBytes, salt);
        }
        return UnixCrypt.crypt(keyBytes, salt);
    }
    
    public static String crypt(final String key) {
        return crypt(key, null);
    }
    
    public static String crypt(final String key, final String salt) {
        return crypt(key.getBytes(Charsets.UTF_8), salt);
    }
}
