package me.existdev.exist.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.Iterator;
import me.existdev.exist.Exist;
import me.existdev.exist.gui.TabGUI;
import me.existdev.exist.module.Module;
import me.existdev.exist.module.modules.render.HUD;
import me.existdev.exist.ttf.CustomFontManager;
import me.existdev.exist.utils.ColorUtils;
import me.existdev.exist.utils.PictureUtils;
import me.existdev.exist.utils.helper.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class IngameGUI extends GuiIngame {
   // $FF: synthetic field
   Minecraft mc = Minecraft.getMinecraft();
   // $FF: synthetic field
   private int item = 0;
   // $FF: synthetic field
   private float xPos = 0.0F;
   // $FF: synthetic field
   private static float xPos1 = 0.0F;
   // $FF: synthetic field
   private double xOffset = 0.0D;
   // $FF: synthetic field
   private double xOffset1 = 0.0D;
   // $FF: synthetic field
   private int yOffset = 0;
   // $FF: synthetic field
   private int min = 100;
   // $FF: synthetic field
   private int max = 255;
   // $FF: synthetic field
   private int cur = 100;
   // $FF: synthetic field
   public boolean increasing = true;

   // $FF: synthetic method
   public IngameGUI(Minecraft mcIn) {
      super(mcIn);
   }

   // $FF: synthetic method
   public void func_175180_a(float p_175180_1_) {
      super.func_175180_a(p_175180_1_);
      if(Exist.moduleManager.getModule(HUD.class).isToggled()) {
         if(Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Name").getBooleanValue()) {
            this.renderName();
         }

         if(Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "ArrayList").getBooleanValue()) {
            this.renderArraylist();
         }

         if(Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Info").getBooleanValue()) {
            this.renderInfo();
         }

         if(Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "TabGUI").getBooleanValue()) {
            TabGUI.renderTabGUI();
            TabGUI.init();
         }

      }
   }

   // $FF: synthetic method
   private void renderName() {
      if(Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Icon").getBooleanValue()) {
         PictureUtils p = new PictureUtils(new ResourceLocation("Exist/Icon.png"), -2, 7, 67, 52);
         p.draw();
      } else {
         RenderHelper.drawRect(0.0F, 5.0F, 2.0F, 30.0F, ColorUtils.getClientColor().hashCode());
         RenderHelper.drawRect(2.0F, 5.0F, 70.0F, 29.0F, Integer.MIN_VALUE);
         CustomFontManager.font90.drawStringWithShadow(ChatFormatting.WHITE + "E" + ChatFormatting.RESET + "xist", 4.0F, 1.0F, ColorUtils.getClientColor().hashCode());
         CustomFontManager.font40.drawStringWithShadow("" + Exist.getVersion(), 50.0F, 5.0F, ColorUtils.getClientColor().hashCode());
      }

   }

   // $FF: synthetic method
   private void renderInfo() {
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
      int y = sr.getScaledHeight() - 1;
      byte x = 40;
      byte x1 = 3;
      int y2 = y - 49;
      RenderHelper.drawBorderedRect((float)(x + 8), (float)(y - 1), (float)(x1 - 2), (float)y2, 3.0F, 1342177280, Integer.MIN_VALUE);
      EnumFacing dir = EnumFacing.getHorizontal(MathHelper.floor_double((double)(Minecraft.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3);
      String direction = dir.getName().substring(0, 1).toUpperCase() + dir.getName().substring(1);
      CustomFontManager.font30.drawStringWithShadow("Facing: " + EnumChatFormatting.WHITE + direction, (float)x1, (float)y2, ColorUtils.getClientColor().hashCode());
      CustomFontManager.font30.drawStringWithShadow("FPS: " + EnumChatFormatting.WHITE + Minecraft.debugFPS, (float)x1, (float)(y2 + 9), ColorUtils.getClientColor().hashCode());
      DecimalFormat numberFormat = new DecimalFormat("#.0");
      String xs = "X: " + EnumChatFormatting.WHITE + numberFormat.format(Minecraft.thePlayer.posX);
      String ys = "Y: " + EnumChatFormatting.WHITE + numberFormat.format(Minecraft.thePlayer.posY);
      String zs = "Z: " + EnumChatFormatting.WHITE + numberFormat.format(Minecraft.thePlayer.posZ);
      CustomFontManager.font30.drawStringWithShadow(xs, (float)x1, (float)(y2 + 18), ColorUtils.getClientColor().hashCode());
      CustomFontManager.font30.drawStringWithShadow(ys, (float)x1, (float)(y2 + 28), ColorUtils.getClientColor().hashCode());
      CustomFontManager.font30.drawStringWithShadow(zs, (float)x1, (float)(y2 + 38), ColorUtils.getClientColor().hashCode());
   }

   // $FF: synthetic method
   private void renderArraylist() {
      Gui.drawRect(0, 0, 0, 0, 0);
      ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
      this.getCurrentFade();
      int yCount = 2;
      int curMod = this.cur;
      boolean incMod = this.increasing;
      Exist.moduleManager.getModules().sort((m1, m2) -> {
         String name1 = m1.getDisplayName();
         String name2 = m2.getDisplayName();
         int mod1 = CustomFontManager.fontArrayList.getStringWidth(name1);
         int mod2 = CustomFontManager.fontArrayList.getStringWidth(name2);
         return mod2 - mod1;
      });
      Iterator var6 = Exist.moduleManager.getModules().iterator();

      while(var6.hasNext()) {
         Module m = (Module)var6.next();
         if(m.isToggled()) {
            String name = m.getDisplayName();
            int posX = sr.getScaledWidth() - CustomFontManager.fontArrayList.getStringWidth(name);
            Color k = new Color(ColorUtils.getClientColor().hashCode());
            int r = k.getRed() + this.min - curMod + 10;
            if(r < 50) {
               r = 50;
            }

            if(r > 255) {
               r = 255;
            }

            int g = k.getGreen() + this.min - curMod + 10;
            if(g < 50) {
               g = 50;
            }

            if(g > 255) {
               g = 255;
            }

            int b = k.getBlue() + this.min - curMod + 10;
            if(b < 50) {
               b = 50;
            }

            if(b > 255) {
               b = 255;
            }

            int cc = (new Color(r, g, b)).getRGB();
            RenderHelper.drawRect((float)(posX - 4), (float)(yCount + 1), (float)sr.getScaledWidth(), (float)(yCount + CustomFontManager.fontArrayList.FONT_HEIGHT + 2), -1879048192);
            if(!Exist.settingManager.getSetting(Exist.moduleManager.getModule(HUD.class), "Rainbow").getBooleanValue()) {
               CustomFontManager.fontArrayList.drawStringWithShadow(name, (float)(posX - 2), (float)yCount, transparency(cc, 1.0D));
            } else {
               CustomFontManager.fontArrayList.drawStringWithShadow(name, (float)(posX - 2), (float)yCount, ColorUtils.getRainbow(6000, yCount * 12));
            }

            curMod += incMod?17:-17;
            if(curMod > this.max) {
               incMod = false;
               curMod = this.max;
            }

            if(curMod < this.min) {
               incMod = true;
               curMod = this.min;
            }

            yCount = (int)((double)yCount + 11.5D);
         }
      }

   }

   // $FF: synthetic method
   private static int transparency(int color, double alpha) {
      Color c = new Color(color);
      float r = 0.003921569F * (float)c.getRed();
      float g = 0.003921569F * (float)c.getGreen();
      float b = 0.003921569F * (float)c.getBlue();
      return (new Color(r, g, b, (float)alpha)).getRGB();
   }

   // $FF: synthetic method
   private int getCurrentFade() {
      this.cur += this.increasing?1:-1;
      if(this.cur > this.max) {
         this.increasing = false;
         this.cur = this.max;
      }

      if(this.cur < this.min) {
         this.increasing = true;
         this.cur = this.min;
      }

      return (new Color(0, 120, this.cur)).getRGB();
   }
}
