package org.m0jang.crystal.UI.AltManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.m0jang.crystal.Font.Fonts;

public class GuiAltList extends GuiWurstSlot {
   private int selectedSlot;
   private Minecraft mc;
   public static ArrayList alts = new ArrayList();
   public static int premiumAlts;
   public static int crackedAlts;
   private GuiAlts prevMenu;

   public GuiAltList(Minecraft mc, GuiAlts prevMenu) {
      super(mc, prevMenu.width, prevMenu.height, 36, prevMenu.height - 56, 30);
      this.mc = mc;
      this.prevMenu = prevMenu;
   }

   public static void sortAlts() {
      Collections.sort(alts, new Comparator<Alt>() {
         public int compare(Alt o1, Alt o2) {
            return o1 != null && o2 != null ? o1.getNameOrEmail().compareToIgnoreCase(o2.getNameOrEmail()) : 0;
         }
      });
      ArrayList newAlts = new ArrayList();
      premiumAlts = 0;
      crackedAlts = 0;

      int i;
      for(i = 0; i < alts.size(); ++i) {
         if (((Alt)alts.get(i)).isStarred()) {
            newAlts.add((Alt)alts.get(i));
         }
      }

      for(i = 0; i < alts.size(); ++i) {
         if (!((Alt)alts.get(i)).isCracked() && !((Alt)alts.get(i)).isStarred()) {
            newAlts.add((Alt)alts.get(i));
         }
      }

      for(i = 0; i < alts.size(); ++i) {
         if (((Alt)alts.get(i)).isCracked() && !((Alt)alts.get(i)).isStarred()) {
            newAlts.add((Alt)alts.get(i));
         }
      }

      for(i = 0; i < newAlts.size(); ++i) {
         for(int i2 = 0; i2 < newAlts.size(); ++i2) {
            if (i != i2 && ((Alt)newAlts.get(i)).getEmail().equals(((Alt)newAlts.get(i2)).getEmail()) && ((Alt)newAlts.get(i)).isCracked() == ((Alt)newAlts.get(i2)).isCracked()) {
               newAlts.remove(i2);
            }
         }
      }

      for(i = 0; i < newAlts.size(); ++i) {
         if (((Alt)newAlts.get(i)).isCracked()) {
            ++crackedAlts;
         } else {
            ++premiumAlts;
         }
      }

      alts = newAlts;
   }

   protected boolean isSelected(int id) {
      return this.selectedSlot == id;
   }

   protected int getSelectedSlot() {
      if (this.selectedSlot > alts.size()) {
         this.selectedSlot = alts.size();
      }

      return this.selectedSlot;
   }

   protected int getSize() {
      return alts.size();
   }

   protected void elementClicked(int var1, boolean doubleClick, int var3, int var4) {
      this.selectedSlot = var1;
      if (doubleClick) {
         this.prevMenu.actionPerformed(new GuiButton(0, 0, 0, (String)null));
      }

   }

   protected void drawBackground() {
   }

   protected void drawSlot(int id, int x, int y, int var4, int var5, int var6) {
      Alt alt = (Alt)alts.get(id);
      GL11.glDisable(3553);
      GL11.glDisable(2884);
      GL11.glEnable(3042);
      if (Minecraft.getMinecraft().getSession().getUsername().equals(alt.getName())) {
         float opacity = 0.3F - MathHelper.abs(MathHelper.sin((float)(Minecraft.getSystemTime() % 10000L) / 10000.0F * 3.1415927F * 2.0F) * 0.15F);
         GL11.glColor4f(0.0F, 1.0F, 0.0F, opacity);
         GL11.glBegin(7);
         GL11.glVertex2d((double)(x - 2), (double)(y - 2));
         GL11.glVertex2d((double)(x - 2 + 220), (double)(y - 2));
         GL11.glVertex2d((double)(x - 2 + 220), (double)(y - 2 + 30));
         GL11.glVertex2d((double)(x - 2), (double)(y - 2 + 30));
         GL11.glEnd();
      }

      GL11.glEnable(3553);
      GL11.glEnable(2884);
      GL11.glDisable(3042);
      AltRenderer.drawAltFace(alt.getNameOrEmail(), x + 1, y + 1, 24, 24, GuiAlts.altList.isSelected(alts.indexOf(alt)));
      Fonts.segoe18.drawString("Name: " + alt.getNameOrEmail(), x + 31, y + 3, 10526880);
      String tags = alt.isCracked() ? "\2478Cracked" : "\2472Premium";
      if (alt.isStarred()) {
         tags = tags + "\247r, \247eStarred";
      }

      if (alt.isUnchecked()) {
         tags = tags + "\247r, \247cUnchecked";
      }

      Fonts.segoe18.drawString(tags, x + 31, y + 15, 10526880);
   }
}
