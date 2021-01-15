// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.AntiLeak.utils;

import java.util.Base64;
import java.security.Key;
import javax.crypto.Cipher;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.security.MessageDigest;
import javax.crypto.spec.SecretKeySpec;

public class Encryption
{
    private static SecretKeySpec secretKey;
    private static byte[] key;
    
    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            Encryption.key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            Encryption.key = sha.digest(Encryption.key);
            Encryption.key = Arrays.copyOf(Encryption.key, 16);
            Encryption.secretKey = new SecretKeySpec(Encryption.key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
    }
    
    public static String decrypt(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            final Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(2, Encryption.secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        }
        catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
            return null;
        }
    }
}
