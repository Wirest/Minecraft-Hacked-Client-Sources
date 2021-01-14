package optifine;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils
{
    public static String getHashMd5(String data)
    {
        return getHash(data, "MD5");
    }

    public static String getHashSha1(String data)
    {
        return getHash(data, "SHA-1");
    }

    public static String getHashSha256(String data)
    {
        return getHash(data, "SHA-256");
    }

    public static String getHash(String data, String digest)
    {
        try
        {
            byte[] abyte = getHash(data.getBytes("UTF-8"), digest);
            return toHexString(abyte);
        }
        catch (Exception exception)
        {
            throw new RuntimeException(exception.getMessage(), exception);
        }
    }

    public static String toHexString(byte[] data)
    {
        StringBuffer stringbuffer = new StringBuffer();

        for (int i = 0; i < data.length; ++i)
        {
            stringbuffer.append(Integer.toHexString(data[i] & 255 | 256).substring(1, 3));
        }

        return stringbuffer.toString();
    }

    public static byte[] getHashMd5(byte[] data) throws NoSuchAlgorithmException
    {
        return getHash(data, "MD5");
    }

    public static byte[] getHashSha1(byte[] data) throws NoSuchAlgorithmException
    {
        return getHash(data, "SHA-1");
    }

    public static byte[] getHashSha256(byte[] data) throws NoSuchAlgorithmException
    {
        return getHash(data, "SHA-256");
    }

    public static byte[] getHash(byte[] data, String digest) throws NoSuchAlgorithmException
    {
        MessageDigest messagedigest = MessageDigest.getInstance(digest);
        byte[] abyte = messagedigest.digest(data);
        return abyte;
    }
}
