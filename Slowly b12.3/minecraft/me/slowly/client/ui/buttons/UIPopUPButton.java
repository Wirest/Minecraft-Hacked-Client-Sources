package me.slowly.client.ui.buttons;

import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.irc.ui.UIIRCChat;
import me.slowly.client.ui.clickgui.menu.UIMenu;
import me.slowly.client.ui.designsettings.UIDesignSettings;
import me.slowly.client.ui.scriptmenu.UIScriptMenu;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.ClientEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class UIPopUPButton {
   private float x;
   private float y;
   private float currentRadius;
   private float minRadius;
   private float maxRadius;
   private float menuSizeRadius;
   private boolean open;
   private boolean animateUp;
   private boolean animateDown;
   private ClientEventHandler mouseClickedPopUpMenu;
   private ArrayList popUpButtons = new ArrayList();
   private UIPopUPChooseButton openButton = null;
   private UIMenu menu;
   private UIScriptMenu uiScriptMenu;
   private UIIRCChat uiircChat;
   private boolean now;

   public UIPopUPButton(float x, float y, float minRadius, float maxRadius) {
      this.x = x;
      this.y = y;
      this.minRadius = minRadius;
      this.maxRadius = maxRadius;
      this.currentRadius = maxRadius;
      this.mouseClickedPopUpMenu = new ClientEventHandler();
      this.popUpButtons.add(new UIPopUPChooseButton("ClickGUI", (int)x + (int)maxRadius, 0, "slowly/icon/menu_icon.png"));
      this.popUpButtons.add(new UIPopUPChooseButton("Scripts", (int)x + (int)maxRadius, 0, "slowly/icon/script_icon.png"));
      this.popUpButtons.add(new UIPopUPChooseButton("Design Settings", (int)x + (int)maxRadius, 0, "slowly/icon/design_icon.png"));
      this.popUpButtons.add(new UIPopUPChooseButton("Music", (int)x + (int)maxRadius, 0, "slowly/icon/music_icon.png"));
      this.popUpButtons.add(new UIPopUPChooseButton("IRC", (int)x + (int)maxRadius, 0, "slowly/icon/irc_icon.png"));
      int yAxis = (int)y - 60;

      for(Iterator var7 = this.popUpButtons.iterator(); var7.hasNext(); yAxis -= 40) {
         UIPopUPChooseButton button = (UIPopUPChooseButton)var7.next();
         button.setY((float)yAxis);
      }

      this.openButton = (UIPopUPChooseButton)this.popUpButtons.get(0);
      this.menu = new UIMenu();
      this.uiScriptMenu = new UIScriptMenu();
      this.uiircChat = new UIIRCChat();
   }

   public void draw(int mouseX, int mouseY) {
      ScaledResolution sc = new ScaledResolution(Minecraft.getMinecraft());
      UnicodeFontRenderer font = Client.getInstance().getFontManager().VERDANA30;
      boolean hovering = this.isHovering(mouseX, mouseY) && this.currentRadius == this.maxRadius;
      this.y = (float)(sc.getScaledHeight() - 10);
      int yAxis = (int)this.y - 50;

      for(Iterator var8 = this.popUpButtons.iterator(); var8.hasNext(); yAxis -= 30) {
         UIPopUPChooseButton button = (UIPopUPChooseButton)var8.next();
         button.setY((float)yAxis);
      }

      this.animate();
      GL11.glPushMatrix();
      float scale = this.currentRadius / this.maxRadius;
      float xMid = this.x + this.maxRadius - 0.5F;
      float yMid = (float)sc.getScaledHeight() - this.maxRadius - 10.0F;
      GL11.glTranslated((double)xMid, (double)yMid, 0.0D);
      GL11.glScalef(scale, scale, scale);
      GL11.glTranslated((double)(-xMid), (double)(-yMid), 0.0D);
      Gui.circle(this.maxRadius + 10.0F, (float)sc.getScaledHeight() - this.maxRadius - 10.0F, this.menuSizeRadius, ClientUtil.reAlpha(-1, 0.8F));
      Gui.circle(this.maxRadius + 10.0F, (float)sc.getScaledHeight() - this.maxRadius - 10.0F, this.maxRadius, UIDesignSettings.getColor());
      if (hovering) {
         Gui.drawFilledCircle(this.maxRadius + 10.0F, (float)sc.getScaledHeight() - this.maxRadius - 10.0F, this.maxRadius + 0.5F, ClientUtil.reAlpha(Colors.BLACK.c, 0.1F));
      }

      if (this.mouseClickedPopUpMenu.canExcecute(Mouse.isButtonDown(0)) && hovering) {
         this.animateDown = true;
      }

      if (this.currentRadius > this.maxRadius / 2.0F && !this.open) {
         font.drawString("+", this.x + this.maxRadius - 0.5F - (float)(font.getStringWidth("+") / 2), (float)sc.getScaledHeight() - this.maxRadius - 12.0F - (float)(font.FONT_HEIGHT / 2), -1);
      } else {
         RenderUtil.drawImage(new ResourceLocation("slowly/icon/settings_icon.png"), (int)(this.x + this.maxRadius - 0.5F - 9.0F) + 3, (int)((float)sc.getScaledHeight() - this.maxRadius - 12.0F - 8.0F) + 2, 15, 15);
      }

      GL11.glPopMatrix();
      int rad = 0;
      Iterator var12 = this.popUpButtons.iterator();

      while(var12.hasNext()) {
         UIPopUPChooseButton button = (UIPopUPChooseButton)var12.next();
         if (this.open) {
            button.draw(mouseX, mouseY);
            if (button.clicked(mouseX, mouseY)) {
               this.openButton = button;
            }
         } else {
            if (rad == 0) {
               rad = (int)(-button.maxRadius);
            }

            button.currentRadius = (float)rad;
            rad = (int)((float)rad - button.maxRadius);
         }
      }

      if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
         float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
         this.menu.draw(mouseX, mouseY);
      }

      if (this.openButton.name.equalsIgnoreCase("Scripts")) {
         this.uiScriptMenu.draw(mouseX, mouseY);
      }

      if (this.openButton.name.equals("Design Settings")) {
         Client.getInstance();
         Client.uiCustomizer.draw(mouseX, mouseY);
      }

      if (this.openButton.name.equals("IRC")) {
         this.uiircChat.drawScreen(mouseX, mouseY);
      }

   }

   public void mouseClicked(int mouseX, int mouseY, int button) {
      if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
         this.menu.mouseClick(mouseX, mouseY);
      }

      if (this.openButton.name.equalsIgnoreCase("Scripts")) {
         this.uiScriptMenu.mouseClicked(mouseX, mouseY);
      }

      if (this.openButton.name.equals("Design Settings")) {
         Client.getInstance();
         Client.uiCustomizer.mouseClick(mouseX, mouseY, button);
      }

   }

   public void mouseReleased(int mouseX, int mouseY) {
      if (this.openButton.name.equalsIgnoreCase("ClickGUI")) {
         this.menu.mouseRelease(mouseX, mouseY);
      }

      if (this.openButton.name.equals("Design Settings")) {
         Client.getInstance();
         Client.uiCustomizer.mouseRelease();
      }

      if (this.openButton.name.equals("Scripts")) {
         this.uiScriptMenu.mouseReleased(mouseX, mouseY);
      }

   }

   private void animate() {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      float add = RenderUtil.delta * 45.0F;
      float menuAdd = RenderUtil.delta * 1500.0F;
      int maxMenuSize = res.getScaledWidth() + (int)((float)res.getScaledWidth() * 0.25F);
      if (this.open && !this.animateDown) {
         if (this.menuSizeRadius + menuAdd > (float)maxMenuSize) {
            this.menuSizeRadius = (float)maxMenuSize;
         } else {
            this.menuSizeRadius += menuAdd;
         }
      } else {
         this.now = true;
         if (this.menuSizeRadius <= (float)maxMenuSize / 1.2631578F) {
            this.menuSizeRadius = 0.0F;
         } else {
            this.menuSizeRadius -= menuAdd;
         }
      }

      if (this.animateDown) {
         if (this.currentRadius - add > this.minRadius) {
            this.currentRadius -= add;
         } else {
            this.currentRadius = this.minRadius;
            this.animateDown = false;
            this.animateUp = true;
            this.open = !this.open;
         }
      } else if (this.animateUp) {
         if (this.currentRadius + add < this.maxRadius) {
            this.currentRadius += add;
         } else {
            this.currentRadius = this.maxRadius;
            this.animateUp = false;
         }
      }

   }

   private boolean isHovering(int mouseX, int mouseY) {
      return (float)mouseX >= this.x && (float)mouseX <= this.x + this.maxRadius * 2.0F && (float)mouseY >= this.y - this.maxRadius * 2.0F && (float)mouseY <= this.y;
   }
}
