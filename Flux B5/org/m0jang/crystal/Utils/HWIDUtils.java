package org.m0jang.crystal.Utils;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;

public class HWIDUtils {
   protected static String getHashedHWID() {
      OS os = getOS();
      if (os == OS.WINDOWS) {
         return getHWIDForWindows();
      } else if (os == OS.LINUX) {
         return getHWIDForLinux();
      } else if (os == OS.MAC) {
         return getHWIDForOSX();
      } else {
         String str = "Invaild OS: " + System.getProperty("os.name");
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         StringSelection selection = new StringSelection(str);
         clipboard.setContents(selection, (ClipboardOwner)null);
         return null;
      }
   }

   protected static OS getOS() {
      String osname = System.getProperty("os.name");
      if (osname.contains("Windows")) {
         return OS.WINDOWS;
      } else if (osname.contains("Linux")) {
         return OS.LINUX;
      } else {
         return osname.contains("Mac") ? OS.MAC : OS.LINUX;
      }
   }

   protected static String getHWIDForWindows() {
      try {
         ProcessBuilder ps = new ProcessBuilder(new String[]{"wmic", "DISKDRIVE", "GET", "SerialNumber"});
         ps.redirectErrorStream(true);
         Process pr = ps.start();
         BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

         String line;
         String hwid;
         for(hwid = ""; (line = in.readLine()) != null; hwid = hwid + line) {
            ;
         }

         pr.waitFor();
         in.close();
         if (!hwid.equals("") && hwid.contains("SerialNumber")) {
            String str = "RAW:" + hwid + "\n" + "Hashed:" + DigestUtils.sha256Hex(hwid);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(str);
            clipboard.setContents(selection, (ClipboardOwner)null);
            System.out.println("Your RAW HWID is " + hwid);
            System.out.println("Your WINDOWS HWID is " + DigestUtils.sha256Hex(hwid));
            return DigestUtils.sha256Hex(hwid);
         } else {
            return null;
         }
      } catch (Exception var8) {
         return null;
      }
   }

   protected static String getHWIDForLinux() {
      try {
         ProcessBuilder ps = new ProcessBuilder(new String[]{"hdparm", "-I", "/dev/sd?", "|", "grep", "'Serial\\ Number'"});
         ps.redirectErrorStream(true);
         Process pr = ps.start();
         BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

         String line;
         String hwid;
         for(hwid = ""; (line = in.readLine()) != null; hwid = hwid + line) {
            ;
         }

         pr.waitFor();
         in.close();
         if (!hwid.equals("") && hwid.contains("Serial Number")) {
            String str = "RAW:" + hwid + "\n" + "Hashed:" + DigestUtils.sha256Hex(hwid);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(str);
            clipboard.setContents(selection, (ClipboardOwner)null);
            System.out.println("Your RAW HWID is " + hwid);
            System.out.println("Your LINUX HWID is " + DigestUtils.sha256Hex(hwid));
            return DigestUtils.sha256Hex(hwid);
         } else {
            return null;
         }
      } catch (Exception var8) {
         return null;
      }
   }

   protected static String getHWIDForOSX() {
      try {
         ProcessBuilder ps = new ProcessBuilder(new String[]{"system_profiler", "SPSerialATADataType", "|", "grep", "\"Serial\\ Number\""});
         ps.redirectErrorStream(true);
         Process pr = ps.start();
         BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

         String line;
         String hwid;
         for(hwid = ""; (line = in.readLine()) != null; hwid = hwid + line) {
            ;
         }

         pr.waitFor();
         in.close();
         if (!hwid.equals("") && hwid.contains("Serial Number")) {
            System.out.println("Your MACOX HWID is " + DigestUtils.sha256Hex(hwid));
            return DigestUtils.sha256Hex(hwid);
         } else {
            return null;
         }
      } catch (Exception var5) {
         return null;
      }
   }

   protected static String executePost(String Surl, ArrayList params) {
      String urlString = Surl;

      try {
         URL url = new URL(urlString);
         URLConnection uc = url.openConnection();
         uc.setDoOutput(true);
         OutputStream os = uc.getOutputStream();
         String postStr = "";

         NameValuePair pair;
         for(Iterator var8 = params.iterator(); var8.hasNext(); postStr = postStr + pair.getName() + "=" + pair.getValue() + "&") {
            pair = (NameValuePair)var8.next();
         }

         postStr.substring(0, postStr.length() - 1);
         PrintStream ps = new PrintStream(os);
         ps.print(postStr);
         ps.close();
         InputStream is = uc.getInputStream();
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));

         String s;
         String result;
         for(result = ""; (s = reader.readLine()) != null; result = result + s) {
            ;
         }

         reader.close();
         return result;
      } catch (IOException var12) {
         System.err.println("Can't connect to Server");
         System.exit(-1);
      } catch (Exception var13) {
         System.exit(-1);
      }

      return null;
   }
}
