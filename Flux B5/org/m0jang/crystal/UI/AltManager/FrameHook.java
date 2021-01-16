package org.m0jang.crystal.UI.AltManager;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;

public class FrameHook {
   private static JFrame frame;

   public static void createFrame(DefaultResourcePack mcDefaultResourcePack, Logger logger) throws LWJGLException {
      if (isAutoMaximize()) {
         frame = new JFrame("Minecraft 1.8");
         Canvas canvas = new Canvas();
         canvas.setBackground(new Color(16, 16, 16));
         Display.setParent(canvas);
         Minecraft mc = Minecraft.getMinecraft();
         canvas.setSize(Minecraft.displayWidth, Minecraft.displayHeight);
         frame.add(canvas);
         frame.setDefaultCloseOperation(3);
         frame.pack();
         frame.setLocationRelativeTo((Component)null);
         InputStream icon16 = null;
         InputStream icon32 = null;

         try {
            icon16 = mcDefaultResourcePack.func_152780_c(new ResourceLocation("icons/icon_16x16.png"));
            icon32 = mcDefaultResourcePack.func_152780_c(new ResourceLocation("icons/icon_32x32.png"));
            ArrayList icons = new ArrayList();
            icons.add(ImageIO.read(icon16));
            icons.add(ImageIO.read(icon32));
            frame.setIconImages(icons);
         } catch (Exception var10) {
            logger.error((String)"Couldn't set icon", (Throwable)var10);
         } finally {
            IOUtils.closeQuietly(icon16);
            IOUtils.closeQuietly(icon32);
         }

         frame.setVisible(true);
      }
   }

   private static boolean isAutoMaximize() {
      File autoMaximizeFile = new File(Minecraft.getMinecraft().mcDataDir + "/wurst/automaximize.json");
      boolean autoMaximizeEnabled = false;
      if (!autoMaximizeFile.exists()) {
         createAutoMaximizeFile(autoMaximizeFile);
      }

      try {
         BufferedReader load = new BufferedReader(new FileReader(autoMaximizeFile));
         String line = load.readLine();
         load.close();
         Minecraft.getMinecraft();
         autoMaximizeEnabled = line.equals("true") && !Minecraft.isRunningOnMac;
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      return autoMaximizeEnabled;
   }

   private static void createAutoMaximizeFile(File autoMaximizeFile) {
      try {
         if (!autoMaximizeFile.getParentFile().exists()) {
            autoMaximizeFile.getParentFile().mkdirs();
         }

         PrintWriter save = new PrintWriter(new FileWriter(autoMaximizeFile));
         save.println(Boolean.toString(!Minecraft.isRunningOnMac));
         save.close();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void maximize() {
      if (frame != null) {
         frame.setExtendedState(6);
      }

   }

   public static JFrame getFrame() {
      return frame;
   }
}
