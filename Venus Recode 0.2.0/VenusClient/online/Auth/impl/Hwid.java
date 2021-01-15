package VenusClient.online.Auth.impl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hwid {
	
  public static String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String s = System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
    return s;
  }
  
  public static String GetPcName() {
    String s = System.getProperty("user.name").trim();
    return s;
  }
  
  public static String GetUserName() {
    String s = System.getProperty("user.name").trim();
    return s;
  }
  
  public static String getHWID2() throws NoSuchAlgorithmException, UnsupportedEncodingException {
    String s = "";
    String main = System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
    byte[] bytes = main.getBytes("UTF-8");
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] md5 = messageDigest.digest(bytes);
    int i = 0;
    for (byte b : md5) {
      s = s + Integer.toHexString(b & 0xFF | 0x300).substring(0, 3);
      i++;
    } 
    return s;
  }
}
