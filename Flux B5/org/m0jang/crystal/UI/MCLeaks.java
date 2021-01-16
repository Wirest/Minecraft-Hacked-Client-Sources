package org.m0jang.crystal.UI;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class MCLeaks {
   public static Session savedSession = null;
   private static String mcLeaksSession;
   private static String mcName;

   public static boolean isAltActive() {
      return mcLeaksSession != null;
   }

   public static String getMCLeaksSession() {
      return mcLeaksSession;
   }

   public static String getMCName() {
      return mcName;
   }

   public static void refresh(String session, String name) {
      mcLeaksSession = session;
      mcName = name;
   }

   public static void remove() {
      mcLeaksSession = null;
      mcName = null;
   }

   public static String getStatus() {
      String status = "Â\2476No Token redeemed. Using Â\247e" + Minecraft.getMinecraft().getSession().getUsername() + "Â\2476 to login!";
      if (mcLeaksSession != null) {
         status = "Â\2472Token active. Using Â\2473" + mcName + "Â\2472 to login!";
      }

      return status;
   }
}
