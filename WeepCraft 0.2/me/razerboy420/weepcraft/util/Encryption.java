/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import me.razerboy420.weepcraft.Weepcraft;
import net.minecraft.client.Minecraft;

public class Encryption {
    public static final String CHARSET = "UTF-8";
    private static final File rsaKeyDir = System.getProperty("user.home") != null ? new File(System.getProperty("user.home"), ".ssh") : null;
    private static final File privateFile = rsaKeyDir != null ? new File(rsaKeyDir, "weep_rsa") : null;
    private static final File publicFile = rsaKeyDir != null ? new File(rsaKeyDir, "weep_rsa.pub") : null;
    private static final File aesFile = new File(Weepcraft.weepcraftDir, "key");
    private static KeyPair keypair;
    private static SecretKey aesKey;
    private static final SecureRandom random;

    static {
        random = new SecureRandom();
    }

    public static String encrypt(String string) {
        Encryption.checkKeys();
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(1, (Key)aesKey, new IvParameterSpec(aesKey.getEncoded()));
            return Base64.getEncoder().encodeToString(cipher.doFinal(string.getBytes("UTF-8")));
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decrypt(String string) {
        Encryption.checkKeys();
        try {
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(2, (Key)aesKey, new IvParameterSpec(aesKey.getEncoded()));
            return new String(cipher.doFinal(Base64.getDecoder().decode(string)), "UTF-8");
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void checkKeys() {
        if (Encryption.hasKeys()) {
            return;
        }
        if (rsaKeyDir == null) {
            System.err.println("RSA key dir does not exist!");
            Minecraft.getMinecraft().shutdown();
            return;
        }
        if (Encryption.hasRsaKeyFiles() && Encryption.loadRsaKeys()) {
            if (Encryption.hasAesKeyFile() && Encryption.loadAesKey()) {
                return;
            }
            Encryption.regenerateAesKey();
        } else {
            Encryption.regenerateRsaKeys();
            Encryption.regenerateAesKey();
        }
    }

    private static boolean hasRsaKeyFiles() {
        if (privateFile.exists() && publicFile.exists()) {
            return true;
        }
        return false;
    }

    private static boolean hasAesKeyFile() {
        return aesFile.exists();
    }

    private static boolean hasKeys() {
        if (keypair != null && keypair.getPrivate().getEncoded() != null && keypair.getPublic().getEncoded() != null && aesKey != null && aesKey.getEncoded() != null) {
            return true;
        }
        return false;
    }

    private static void regenerateRsaKeys() {
        System.out.println("WARNING: Regenerating RSA keys!");
        try {
            KeyPairGenerator keypairgen = KeyPairGenerator.getInstance("RSA");
            keypairgen.initialize(1024);
            keypair = keypairgen.generateKeyPair();
            if (!publicFile.getParentFile().exists()) {
                publicFile.getParentFile().mkdirs();
            }
            ObjectOutputStream savePublic = new ObjectOutputStream(new FileOutputStream(publicFile));
            savePublic.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPublic(), RSAPublicKeySpec.class).getModulus());
            savePublic.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPublic(), RSAPublicKeySpec.class).getPublicExponent());
            savePublic.close();
            if (!privateFile.getParentFile().exists()) {
                privateFile.getParentFile().mkdirs();
            }
            ObjectOutputStream savePrivate = new ObjectOutputStream(new FileOutputStream(privateFile));
            savePrivate.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPrivate(), RSAPrivateKeySpec.class).getModulus());
            savePrivate.writeObject(KeyFactory.getInstance("RSA").getKeySpec(keypair.getPrivate(), RSAPrivateKeySpec.class).getPrivateExponent());
            savePrivate.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void regenerateAesKey() {
        System.out.println("WARNING: Regenerating AES key!");
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
            aesKey = keygen.generateKey();
            if (!aesFile.getParentFile().exists()) {
                aesFile.getParentFile().mkdirs();
            }
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(1, keypair.getPublic());
            Files.write(aesFile.toPath(), rsaCipher.doFinal(aesKey.getEncoded()), new OpenOption[0]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean loadRsaKeys() {
        try {
            ObjectInputStream publicLoad = new ObjectInputStream(new FileInputStream(publicFile));
            PublicKey loadedPublicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec((BigInteger)publicLoad.readObject(), (BigInteger)publicLoad.readObject()));
            publicLoad.close();
            ObjectInputStream privateLoad = new ObjectInputStream(new FileInputStream(privateFile));
            PrivateKey loadedPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new RSAPrivateKeySpec((BigInteger)privateLoad.readObject(), (BigInteger)privateLoad.readObject()));
            privateLoad.close();
            keypair = new KeyPair(loadedPublicKey, loadedPrivateKey);
            return true;
        }
        catch (Exception e) {
            System.err.println("Failed to load RSA keys!");
            e.printStackTrace();
            return false;
        }
    }

    private static boolean loadAesKey() {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(2, keypair.getPrivate());
            aesKey = new SecretKeySpec(rsaCipher.doFinal(Files.readAllBytes(aesFile.toPath())), "AES");
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String sha256(String s) {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA-256");
            byte[] shaByteArr = mDigest.digest(s.getBytes(Charset.forName("UTF-8")));
            StringBuilder hexStrBuilder = new StringBuilder();
            byte[] arrayOfByte1 = shaByteArr;
            int j = arrayOfByte1.length;
            int i = 0;
            while (i < j) {
                byte element = arrayOfByte1[i];
                hexStrBuilder.append(String.format("%02x", Byte.valueOf(element)));
                ++i;
            }
            return hexStrBuilder.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String nextRandomString() {
        return new BigInteger(130, random).toString(32);
    }

    public static final String md5(String toEncrypt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(toEncrypt.getBytes());
            byte[] bytes = digest.digest();
            StringBuilder sb = new StringBuilder();
            int i = 0;
            while (i < bytes.length) {
                sb.append(String.format("%02X", Byte.valueOf(bytes[i])));
                ++i;
            }
            return sb.toString().toLowerCase();
        }
        catch (Exception exc) {
            return "";
        }
    }
}

