package info.sigmaclient.util.security;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.Key;
import java.util.Enumeration;

public class Crypto {

    public static String encrypt(Key key, String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        byte[] encryptedValue = Base64.encodeBase64(encrypted);
        return new String(encryptedValue);
    }

    public static SecretKeySpec getSecret() {
        byte[] secret = Crypto.getUserKey(16);
        return new SecretKeySpec(secret, 0, secret.length, "AES");
    }

    public static SecretKeySpec getSecretOLD() {
        byte[] secret = Crypto.getUserKeyOLD(16);
        return new SecretKeySpec(secret, 0, secret.length, "AES");
    }

    public static String decrypt(Key key, String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.decodeBase64(text.getBytes());
        byte[] original = cipher.doFinal(decodedBytes);
        return new String(original);
    }

    public static byte[] getUserKey(int size) {
        byte[] ret = new byte[size];
        for (int i = 0; i < size; i++) {
            ret[i] = (byte) ((getNetData().split("(?<=\\G.{4})"))[i].hashCode() % 256);
        }
        return ret;
    }

    public static byte[] getUserKeyOLD(int size) {
        byte[] ret = new byte[size];
        for (int i = 0; i < size; i++) {
            ret[i] = (byte) ((getNetDataOLD().split("(?<=\\G.{4})"))[i].hashCode() % 256);
        }
        return ret;
    }

    public static EnumOS getOSType() {
        String var0 = System.getProperty("os.name").toLowerCase();
        return var0.contains("win") ? EnumOS.WINDOWS : (var0.contains("mac") ? EnumOS.OSX : (var0.contains("solaris") ? EnumOS.SOLARIS : (var0.contains("sunos") ? EnumOS.SOLARIS : (var0.contains("linux") ? EnumOS.LINUX : (var0.contains("unix") ? EnumOS.LINUX : EnumOS.UNKNOWN)))));
    }

    public enum EnumOS {
        LINUX("LINUX", 0), SOLARIS("SOLARIS", 1), WINDOWS("WINDOWS", 2), OSX("OSX", 3), UNKNOWN("UNKNOWN", 4);

        EnumOS(String p_i1357_1_, int p_i1357_2_) {
        }
    }

    private static String netData = null;

    private static String getNetDataOLD() {
        String netData1;
        if (getOSType() != EnumOS.WINDOWS) {
            netData1 = "A43s1ASDa-asda32=2=3fsf24aSADAmOP+-aEzx1ASDMS+sasdda0-a9aujsd0a-sad09as_ASASD-ad0-afkasf-KF_a0As-0d_J__oop51w912";
            return netData1;
        }
        Enumeration<NetworkInterface> nis = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        if (nis == null) {
            netData1 = "A43s1ASDa-asda32=2=3fsf24aSADAmOP+-aEzx1ASDMS+sasdda0-a9aujsd0a-sad09as_ASASD-ad0-afkasf-KF_a0As-0d_J__oop51w912";
            return netData1;
        }
        StringBuilder data = new StringBuilder();
        while (nis.hasMoreElements()) {
            NetworkInterface ni = nis.nextElement();
            data.append(ni.getName() + " " + ni.getDisplayName());
        }
        netData1 = data.toString();
        return netData1;
    }

    private static String getNetData() {
        if (netData == null) {
            netData = "PO85tZAj3Lon4AhaGsl8CLycAMFW1FZIm1kp7qIPA6iFkMMc98OnARIY4bpE5LY5qJyAfx9umpeLR2SBGl3OmNhKBnv3AuMF";
        }
        return netData;
    }

}