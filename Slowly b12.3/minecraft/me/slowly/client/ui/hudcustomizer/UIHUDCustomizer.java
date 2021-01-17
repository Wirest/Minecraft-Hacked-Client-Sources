package me.slowly.client.ui.hudcustomizer;

import java.io.IOException;
import java.util.ArrayList;
import me.slowly.client.Client;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDHotbar;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDLogo;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDModList;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class UIHUDCustomizer {
   private int x;
   private int y;
   private int width;
   private int height;
   private Minecraft mc = Minecraft.getMinecraft();
   private MouseInputHandler mouseHandler = new MouseInputHandler(0);
   public static ArrayList customs = new ArrayList();
   private int currentCustomHUD = 0;
   private CustomHUDTabGui tabGuiTab = new CustomHUDTabGui();
   private CustomHUDModList hudModList = new CustomHUDModList();
   public static CustomHUDHotbar hotbarTab;
   private CustomHUDLogo logoTab;

   public UIHUDCustomizer() {
      hotbarTab = new CustomHUDHotbar();
      this.logoTab = new CustomHUDLogo();
      customs.add(new CustomHUD("TabGui", this.mouseHandler, CustomHUDTabGui.optionList));
      customs.add(new CustomHUD("Module List", this.mouseHandler, CustomHUDModList.optionList));
      customs.add(new CustomHUD("Hotbar", this.mouseHandler, CustomHUDHotbar.optionList));
      customs.add(new CustomHUD("Logo", this.mouseHandler, CustomHUDLogo.optionList));

      try {
         Client.getInstance().getFileUtil().loadCustomValues();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void draw(int mouseX, int mouseY) {
      ScaledResolution res = new ScaledResolution(this.mc);
      this.width = (int)((double)res.getScaledWidth() * 0.75D);
      this.height = (int)((double)res.getScaledHeight() * 0.75D);
      this.x = (res.getScaledWidth() - this.width) / 2;
      this.y = (res.getScaledHeight() - this.height) / 2;
      int STRIP_COLOR = FlatColors.ORANGE.c;
      int BACKGROUND_COLOR = FlatColors.DARK_ASPHALT.c;
      boolean SETTING_FIELD_Y = true;
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + this.height, BACKGROUND_COLOR);
      Gui.drawRect(this.x, this.y, this.x + this.width, this.y + 30, STRIP_COLOR);

      for(int i = 0; i < customs.size(); ++i) {
         CustomHUD custom = (CustomHUD)customs.get(i);
         int windowX = this.x;
         int windowY = this.y + 30;
         custom.windowWidth = this.width;
         custom.windowHeight = this.height - 30;
         custom.width = this.width / customs.size();
         custom.height = 30;
         custom.x = this.x + custom.width * i;
         custom.y = this.y;
         if (i == this.currentCustomHUD) {
            Gui.drawRect(custom.x, custom.y, custom.x + custom.width, custom.y + custom.height, ClientUtil.reAlpha(Colors.BLACK.c, 0.25F));
         }

         UnicodeFontRenderer font = Client.getInstance().getFontManager().simpleton25;
         font.drawString(custom.owner, (float)(custom.x + (custom.width - font.getStringWidth(custom.owner)) / 2), (float)(custom.y + (custom.height - font.FONT_HEIGHT) / 2), -1);
         boolean hover = mouseX >= custom.x && mouseX < custom.x + custom.width && mouseY >= custom.y && mouseY <= custom.y + custom.height;
         if (hover && this.mouseHandler.canExcecute()) {
            this.currentCustomHUD = i;
         }

         if (i == this.currentCustomHUD) {
            GL11.glPushMatrix();
            GL11.glEnable(3089);
            RenderUtil.doGlScissor(windowX, windowY, custom.windowWidth, custom.windowHeight);
            GL11.glTranslated((double)windowX, (double)windowY, 0.0D);
            custom.draw(mouseX - windowX, mouseY - windowY);
            GL11.glDisable(3089);
            GL11.glPopMatrix();
         }
      }

   }

   public void mouseClick(int mouseX, int mouseY, int button) {
      for(int i = 0; i < customs.size(); ++i) {
         if (i == this.currentCustomHUD) {
            CustomHUD custom = (CustomHUD)customs.get(i);
            custom.mouseClick(mouseX - this.x, mouseY - this.y - 30);
         }
      }

   }

   public void mouseRelease() {
      for(int i = 0; i < customs.size(); ++i) {
         if (i == this.currentCustomHUD) {
            CustomHUD custom = (CustomHUD)customs.get(i);
            custom.mouseRelease();
         }
      }

      Client.getInstance().getFileUtil().saveCustomValues();
   }
}
