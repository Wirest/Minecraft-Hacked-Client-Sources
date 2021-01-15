package VenusClient.online.Auth;

import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import VenusClient.online.Auth.impl.Hwid;

public class VenusAuthAPI extends Thread {
  public static String username;
  
  public static String password;
  
  public static String role;
  
  public static String HWID;
  
  public static String uid;
  
  public VenusAuthAPI(String username, String password) {
    super("Alt Login Thread");
  }
  
  public static boolean authUserPass(String username, String password) throws IOException, NoSuchAlgorithmException {
    URL url = new URL("https://venusdb.000webhostapp.com/VenusClient-DataBase/VenusClientUser_DataBase.txt");
    Scanner s = new Scanner(url.openStream());
    while (s.hasNext()) {
      String[] s2 = s.nextLine().split(":");
      if (username.equals(s2[0]) && password.equals(s2[1])) {
        HWID = s2[2];
        role = s2[3];
        VenusAuthAPI.username = username;
        VenusAuthAPI.password = s2[1];
        return true;
      } 
    } 
    return false;
  }
  
  public static boolean authAutoHWID() throws IOException, NoSuchAlgorithmException {
    URL url = new URL("https://venusdb.000webhostapp.com/VenusClient-DataBase/VenusClientUser_DataBase.txt");
    Scanner s = new Scanner(url.openStream());
    while (s.hasNext()) {
      String[] s2 = s.nextLine().split(":");
      if (Hwid.getHWID2().contains(s2[2])) {
        HWID = s2[2];
        role = s2[3];
        username = s2[0];
        password = s2[1];
        return true;
      } 
    } 
    return false;
  }
  
  public static boolean authManualHWID(String hwid) throws IOException, NoSuchAlgorithmException {
    URL url = new URL("https://venusdb.000webhostapp.com/VenusClient-DataBase/VenusClientUser_DataBase.txt");
    Scanner s = new Scanner(url.openStream());
    while (s.hasNext()) {
      String[] s2 = s.nextLine().split(":");
      if (hwid.contains(s2[2])) {
        HWID = s2[2];
        role = s2[3];
        username = s2[0];
        password = s2[1];
        return true;
      } 
    } 
    return false;
  }
  
  public static String getUserName() throws IOException {
    return username;
  }
  
  public static String getPassword() {
    return password;
  }
  
  public static String getRole() throws IOException {
    return role;
  }
  
  public static String getHWID() throws IOException {
    return HWID;
  }
  
  public static String getUid() {
    return uid;
  }
}
