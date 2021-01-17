package me.slowly.client.util;

import java.awt.Color;
import java.util.ArrayList;
import me.slowly.client.ui.notifiactions.ClientNotification;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.BlockPos;

public class ClientUtil {
   protected static Minecraft mc = Minecraft.getMinecraft();
   private static final String CLIENT_PREFIX = "¡ì2Slowly¡ìf";
   private static ArrayList notifications = new ArrayList();
   private static final ClientFont clientFont = new ClientFont("Verdana", 18.0F);
   private static final ClientFont smallFont = new ClientFont("Verdana", 18.0F);
   public static double addY;

   public static void drawNotifications() {
      ScaledResolution res = new ScaledResolution(mc);
      double startY = (double)(res.getScaledHeight() - 25);
      double lastY = startY;

      for(int i = 0; i < notifications.size(); ++i) {
         ClientNotification not = (ClientNotification)notifications.get(i);
         if (not.shouldDelete()) {
            notifications.remove(i);
         }

         not.draw(startY, lastY);
         startY -= not.getHeight() + 1.0D;
      }

   }

   public static ClientFont getSmallfont() {
      return smallFont;
   }

   public static void sendClientMessage(String message, ClientNotification.Type type) {
      notifications.add(new ClientNotification(message, type));
   }

   public static BlockPos getBlockCorner(BlockPos start, BlockPos end) {
      for(int x = 0; x <= 1; ++x) {
         for(int y = 0; y <= 1; ++y) {
            for(int z = 0; z <= 1; ++z) {
               BlockPos pos = new BlockPos(end.getX() + x, end.getY() + y, end.getZ() + z);
               if (!isBlockBetween(start, pos)) {
                  return pos;
               }
            }
         }
      }

      return null;
   }

   public static String removeColorCode(String text) {
      String finalText = text;
      if (text.contains("¡ì")) {
         for(int i = 0; i < finalText.length(); ++i) {
            if (Character.toString(finalText.charAt(i)).equals("¡ì")) {
               try {
                  String part1 = finalText.substring(0, i);
                  String part2 = finalText.substring(Math.min(i + 2, finalText.length()), finalText.length());
                  finalText = part1 + part2;
               } catch (Exception var5) {
                  ;
               }
            }
         }
      }

      return finalText;
   }

   public static boolean isBlockBetween(BlockPos start, BlockPos end) {
      int startX = start.getX();
      int startY = start.getY();
      int startZ = start.getZ();
      int endX = end.getX();
      int endY = end.getY();
      int endZ = end.getZ();
      double diffX = (double)(endX - startX);
      double diffY = (double)(endY - startY);
      double diffZ = (double)(endZ - startZ);
      double x = (double)startX;
      double y = (double)startY;
      double z = (double)startZ;
      double STEP = 0.1D;
      int STEPS = (int)Math.max(Math.abs(diffX), Math.max(Math.abs(diffY), Math.abs(diffZ))) * 4;

      for(int i = 0; i < STEPS - 1; ++i) {
         x += diffX / (double)STEPS;
         y += diffY / (double)STEPS;
         z += diffZ / (double)STEPS;
         if (x != (double)endX || y != (double)endY || z != (double)endZ) {
            BlockPos pos = new BlockPos(x, y, z);
            Block block = mc.theWorld.getBlockState(pos).getBlock();
            if (block.getMaterial() != Material.air && block.getMaterial() != Material.water && !(block instanceof BlockVine) && !(block instanceof BlockLadder)) {
               return true;
            }
         }
      }

      return false;
   }

   public static ClientFont getClientfont() {
      return clientFont;
   }

   public static int reAlpha(int color, float alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, alpha)).getRGB();
   }
}
