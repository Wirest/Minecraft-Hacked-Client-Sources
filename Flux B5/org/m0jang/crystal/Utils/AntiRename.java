package org.m0jang.crystal.Utils;

import java.util.Base64;

public class AntiRename {
   public static String getClientName() {
      String settings = "Rmx1eA==";
      String proxy = new String(Base64.getDecoder().decode(settings));
      return proxy;
   }

   public static String getVersion() {
      String settings = "YjU=";
      String proxy = new String(Base64.getDecoder().decode(settings));
      return proxy;
   }

   public static String getCredit() {
      String settings = "bWFkZSBieSBBeWF0YWthIMKnY1RISVMgSVMgVEVTVCBWRVJTSU9OIQ==";
      String proxy = new String(Base64.getDecoder().decode(settings));
      return proxy;
   }
}
