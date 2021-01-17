package me.slowly.client.ui.clickgui.menu;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class UIMenu {
   private ArrayList categories;
   public static int WIDTH = 50;
   public static int TAB_HEIGHT = 25;
   private MouseInputHandler handler = new MouseInputHandler(0);
   private Minecraft mc = Minecraft.getMinecraft();
   private String fileDir;

   public UIMenu() {
      this.fileDir = this.mc.mcDataDir.getAbsolutePath() + "/" + "Slowly";
      this.categories = new ArrayList();
      new ScaledResolution(Minecraft.getMinecraft());
      UnicodeFontRenderer font = Client.getInstance().getFontManager().arialBold18;
      this.addCategorys();

      try {
         this.loadClickGui();
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }

   public void draw(int mouseX, int mouseY) {
      Iterator var4 = this.categories.iterator();

      while(var4.hasNext()) {
         UIMenuCategory c = (UIMenuCategory)var4.next();
         c.draw(mouseX, mouseY);
      }

   }

   private void addCategorys() {
      int xAxis = 10;
      Mod.Category[] var5;
      int var4 = (var5 = Mod.Category.values()).length;

      for(int var3 = 0; var3 < var4; ++var3) {
         Mod.Category c = var5[var3];
         if (c != Mod.Category.NONE && c != Mod.Category.SETTINGS) {
            this.categories.add(new UIMenuCategory(c, xAxis, 100, WIDTH, TAB_HEIGHT, this.handler));
            xAxis += 115;
         }
      }

   }

   public void mouseClick(int mouseX, int mouseY) {
      Iterator var4 = this.categories.iterator();

      while(var4.hasNext()) {
         UIMenuCategory cat = (UIMenuCategory)var4.next();
         cat.mouseClick(mouseX, mouseY);
      }

   }

   public void mouseRelease(int mouseX, int mouseY) {
      Iterator var4 = this.categories.iterator();

      while(var4.hasNext()) {
         UIMenuCategory cat = (UIMenuCategory)var4.next();
         cat.mouseRelease(mouseX, mouseY);
      }

      Client.getInstance().getFileUtil().saveValues();
      this.saveClickGui();
   }

   public ArrayList getCategories() {
      return this.categories;
   }

   public void saveClickGui() {
      File f = new File(this.fileDir + "/gui.txt");

      try {
         if (!f.exists()) {
            f.createNewFile();
         }

         PrintWriter pw = new PrintWriter(f);
         Iterator var4 = this.getCategories().iterator();

         while(var4.hasNext()) {
            UIMenuCategory cat = (UIMenuCategory)var4.next();
            String name = cat.c.name();
            String x = String.valueOf(cat.x);
            String y = String.valueOf(cat.y);
            String open = String.valueOf(cat.uiMenuMods.open);
            pw.print(name + ":" + x + ":" + y + ":" + open + "\n");
         }

         pw.close();
      } catch (Exception var9) {
         var9.printStackTrace();
      }

   }

   public void loadClickGui() throws IOException {
      File f = new File(this.fileDir + "/gui.txt");
      if (!f.exists()) {
         f.createNewFile();
      } else {
         @SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(f));

         String line;
         while((line = br.readLine()) != null) {
            try {
               String[] s = line.split(":");
               if (s.length >= 4) {
                  String name = s[0];
                  int x = Integer.valueOf(s[1]).intValue();
                  int y = Integer.valueOf(s[2]).intValue();
                  boolean open = Boolean.valueOf(s[3]).booleanValue();
                  Iterator var10 = this.getCategories().iterator();

                  while(var10.hasNext()) {
                     UIMenuCategory cat = (UIMenuCategory)var10.next();
                     String name2 = cat.c.name();
                     if (name2.equals(name)) {
                        cat.x = x;
                        cat.y = y;
                        cat.uiMenuMods.open = open;
                     }
                  }
               }
            } catch (Exception var12) {
               ;
            }
         }
      }

   }
}
