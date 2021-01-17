package me.slowly.client.ui.clickgui.menu;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import me.slowly.client.Client;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.other.Gui;
import me.slowly.client.ui.options.UIMode;
import me.slowly.client.ui.options.UISlider;
import me.slowly.client.ui.options.UIToggleButton;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class UIMenuMods {
   private ArrayList modList = new ArrayList();
   private MouseInputHandler handler;
   public boolean open;
   public int x;
   public int y;
   public int width;
   public int tab_height;
   private Mod.Category c;
   public double yPos;
   private boolean opened;
   private boolean closed;
   private HashMap sliderList = new HashMap();
   private HashMap valueModeList = new HashMap();
   private HashMap toggleButtonList = new HashMap();
   private int valueYAdd = 0;
   private float scrollY;
   private float scrollAmount;

   public UIMenuMods(Mod.Category c, MouseInputHandler handler) {
      this.c = c;
      this.handler = handler;
      this.addMods();
      this.addValues();
      this.yPos = (double)(-(this.y + this.tab_height + this.modList.size() * 20 + 10));
   }

   public void draw(int mouseX, int mouseY) {
      int MAX_HEIGHT = 248;
      if (mouseY > this.y + MAX_HEIGHT) {
         mouseY = Integer.MAX_VALUE;
      }

      UnicodeFontRenderer font = Client.getInstance().getFontManager().getFont("simpleton", 14.0F, true);
      String name = "Panel " + this.c.name().substring(0, 1) + this.c.name().toLowerCase().substring(1, this.c.name().length());
      if (this.opened) {
         this.yPos = (double)(this.y + this.tab_height - 2);
      }

      if (this.closed) {
         this.yPos = (double)(this.y - this.modList.size() * 20 - this.valueYAdd);
      }

      if (this.yPos > (double)(this.y + this.tab_height - 2)) {
         this.yPos = (double)(this.y + this.tab_height - 2);
      }

      if (this.open) {
         this.yPos = RenderUtil.getAnimationState(this.yPos, (double)(this.y + this.tab_height - 2), Math.max(50.0D, Math.abs(this.yPos - (double)(this.y + this.tab_height - 2)) * 5.0D));
         if (this.yPos == (double)(this.y + this.tab_height - 2)) {
            this.opened = true;
         }

         this.closed = false;
      } else {
         this.yPos = RenderUtil.getAnimationState(this.yPos, (double)(this.y - this.modList.size() * 20 - this.valueYAdd), Math.max(1.0D, Math.abs(this.yPos - (double)(this.y - this.modList.size() * 20 - this.valueYAdd) - 2.0D) * 4.0D));
         this.opened = false;
         if (this.yPos == (double)(this.y - this.modList.size() * 20 - this.valueYAdd)) {
            this.closed = true;
         }
      }

      int yAxis = (int)this.yPos;
      int height = 20;
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, res.getScaledHeight());
      float bottomY = (float)(this.modList.size() * height + yAxis + this.valueYAdd);
      if (this.yPos != (double)(this.y - this.modList.size() * 20)) {
         RenderUtil.drawRoundedRect((float)this.x, Math.min((float)(this.y + MAX_HEIGHT), bottomY) - 2.0F, (float)(this.x + this.width), Math.min((float)(this.y + MAX_HEIGHT), bottomY) + 10.0F, 1.5F, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY.c, 1.0F) : -8487037);
      }

      RenderUtil.doGlScissor(this.x, this.y + this.tab_height - 2, this.width, Math.min(MAX_HEIGHT - (this.tab_height - 2), this.modList.size() * height + this.valueYAdd));
      GL11.glTranslated(0.0D, (double)this.scrollY, 0.0D);
      mouseY = (int)((float)mouseY - this.scrollY);
      this.valueYAdd = 0;

      for(Iterator var11 = this.modList.iterator(); var11.hasNext(); yAxis += height) {
         Mod m = (Mod)var11.next();
         net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY.c, 0.55F) : -13223618);
         if (m.isEnabled()) {
            net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY4.c, 0.55F) : -13613216);
         }

         boolean arrowHover = this.yPos == (double)(this.y + this.tab_height - 2) && mouseX >= this.x + this.width - 11 && mouseX <= this.x + this.width - 2 && mouseY >= yAxis && mouseY < yAxis + height && (float)mouseY + this.scrollY >= (float)(this.y + this.tab_height);
         boolean hover = !arrowHover && this.yPos == (double)(this.y + this.tab_height - 2) && mouseX >= this.x && mouseX <= this.x + this.width - 12 && mouseY >= yAxis && mouseY < yAxis + height && (float)mouseY + this.scrollY >= (float)(this.y + this.tab_height);
         if (hover) {
            m.hoverOpacity = RenderUtil.getAnimationState(m.hoverOpacity, 0.25D, 1.0D);
         } else {
            m.hoverOpacity = RenderUtil.getAnimationState(m.hoverOpacity, 0.09D, 1.5D);
         }

         if (hover && this.handler.canExcecute()) {
            m.set(!m.isEnabled());
         }

         if (arrowHover && this.handler.canExcecute() && m.hasValues()) {
            m.openValues = !m.openValues;
         }

         double val;
         if (m.hasValues()) {
            m.arrowAnlge = RenderUtil.getAnimationState(m.arrowAnlge, (double)(m.openValues ? 0 : -90), 1000.0D);
            int size = 5;
            double xMid = (double)(this.x + this.width - 8 + 2);
            val = (double)(yAxis + (height - size) / 2 + 1 + 2);
            GL11.glPushMatrix();
            GL11.glTranslated(xMid, val, 0.0D);
            GL11.glRotated(m.arrowAnlge, 0.0D, 0.0D, 1.0D);
            GL11.glTranslated(-xMid, -val, 0.0D);
            if (Gui.mode.isCurrentMode("Slowly")) {
               if (arrowHover) {
                  RenderUtil.drawImage(new ResourceLocation("slowly/icon/arrow-down.png"), this.x + this.width - 8, yAxis + (height - size) / 2 + 1, size, size, new Color(0.7058824F, 0.7058824F, 0.7058824F));
               } else {
                  RenderUtil.drawImage(new ResourceLocation("slowly/icon/arrow-down.png"), this.x + this.width - 8, yAxis + (height - size) / 2 + 1, size, size);
               }
            } else {
               RenderUtil.drawImage(new ResourceLocation("slowly/icon/gear.png"), this.x + this.width - 8, yAxis + (height - size) / 2 + 1, size, size);
            }

            GL11.glPopMatrix();
         }

         net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, ClientUtil.reAlpha(Colors.BLACK.c, (float)m.hoverOpacity));
         font.drawString(m.getName(), (float)(this.x + (this.width - font.getStringWidth(name)) / 2), (float)(yAxis + (height - font.FONT_HEIGHT) / 2), -1);
         if (m.openValues) {
            Iterator var16 = this.sliderList.keySet().iterator();

            Value value;
            while(var16.hasNext()) {
               value = (Value)var16.next();
               if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName())) {
                  yAxis += height;
                  this.valueYAdd += height;
                  net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY.c, 0.2F) : -17791255);
                  val = ((Double)value.getValueState()).doubleValue();
                  UISlider slider = (UISlider)this.sliderList.get(value);
                  slider.width = this.width - 3;
                  double newVal = slider.draw((float)val, mouseX, mouseY, this.x + 1, yAxis + height / 2 - 5);
                  value.setValueState(newVal);
               }
            }

            var16 = this.valueModeList.keySet().iterator();

            while(var16.hasNext()) {
               value = (Value)var16.next();
               if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName())) {
                  yAxis += height;
                  this.valueYAdd += height;
                  net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY.c, 0.4F) : -13223618);
                  UIMode mode = (UIMode)this.valueModeList.get(value);
                  mode.width = this.width;
                  mode.draw(mouseX, mouseY, this.x, yAxis);
               }
            }

            var16 = this.toggleButtonList.keySet().iterator();

            while(var16.hasNext()) {
               value = (Value)var16.next();
               if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName())) {
                  yAxis += height;
                  this.valueYAdd += height;
                  net.minecraft.client.gui.Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + height, Gui.mode.isCurrentMode("Slowly") ? ClientUtil.reAlpha(Colors.SLOWLY.c, 0.5F) : -13223618);
                  UIToggleButton button = (UIToggleButton)this.toggleButtonList.get(value);
                  button.width = this.width;
                  button.draw(mouseX, mouseY, this.x, yAxis);
               }
            }

            net.minecraft.client.gui.Gui.drawRect(this.x, yAxis + height, this.x + this.width, yAxis + height + 1, ClientUtil.reAlpha(Colors.BLACK.c, 0.26F));
            net.minecraft.client.gui.Gui.drawRect(this.x, yAxis + height - 1, this.x + this.width, yAxis + height, ClientUtil.reAlpha(Colors.BLACK.c, 0.26F));
         }
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      if (mouseX >= this.x && mouseX <= this.x + this.width && (float)mouseY + this.scrollY >= (float)this.y && (float)mouseY + this.scrollY <= (float)yAxis) {
         float scroll = (float)Mouse.getDWheel();
         this.scrollY += scroll / 10.0F;
      }

      if (yAxis - height - this.tab_height >= MAX_HEIGHT) {
         double test = (double)((float)(yAxis - this.y) + this.scrollY);
         if (test < (double)MAX_HEIGHT) {
            this.scrollY = (float)MAX_HEIGHT - (float)yAxis + (float)this.y;
         }
      }

      if (this.scrollY > 0.0F || yAxis - height - this.tab_height < MAX_HEIGHT) {
         this.scrollY = 0.0F;
      }

   }

   public void mouseClick(int mouseX, int mouseY) {
      mouseY = (int)((float)mouseY - this.scrollY);
      Iterator var4 = this.modList.iterator();

      while(true) {
         Mod m;
         do {
            if (!var4.hasNext()) {
               return;
            }

            m = (Mod)var4.next();
         } while(!m.openValues);

         Iterator var6 = Value.list.iterator();

         while(var6.hasNext()) {
            Value value = (Value)var6.next();
            if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueDouble) {
               UISlider slider = (UISlider)this.sliderList.get(value);
               if (slider.mouseClick(mouseX, mouseY)) {
                  this.handler.clicked = true;
               }
            }
         }
      }
   }

   public void mouseRelease(int mouseX, int mouseY) {
      Iterator var4 = this.modList.iterator();

      while(true) {
         Mod m;
         do {
            if (!var4.hasNext()) {
               return;
            }

            m = (Mod)var4.next();
         } while(!m.openValues);

         Iterator var6 = Value.list.iterator();

         while(var6.hasNext()) {
            Value value = (Value)var6.next();
            if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueDouble) {
               UISlider slider = (UISlider)this.sliderList.get(value);
               slider.mouseRelease();
            }
         }
      }
   }

   private void addSliders() {
      Iterator var2 = this.modList.iterator();

      while(var2.hasNext()) {
         Mod m = (Mod)var2.next();
         Iterator var4 = Value.list.iterator();

         while(var4.hasNext()) {
            Value value = (Value)var4.next();
            if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueDouble) {
               UISlider slider = new UISlider(value.getValueName().split("_")[1], ((Double)value.getValueMin()).doubleValue(), ((Double)value.getValueMax()).doubleValue(), value.getSteps(), this.width - 3);
               this.sliderList.put(value, slider);
            }
         }
      }

   }

   private void addModes() {
      int height = 20;
      Iterator var3 = this.modList.iterator();

      while(var3.hasNext()) {
         Mod m = (Mod)var3.next();
         Iterator var5 = Value.list.iterator();

         while(var5.hasNext()) {
            Value value = (Value)var5.next();
            if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueMode) {
               UIMode mode = new UIMode(value, this.handler, this.width, height);
               this.valueModeList.put(value, mode);
            }
         }
      }

   }

   private void addToggleButtons() {
      int height = 20;
      Iterator var3 = this.modList.iterator();

      while(var3.hasNext()) {
         Mod m = (Mod)var3.next();
         Iterator var5 = Value.list.iterator();

         while(var5.hasNext()) {
            Value value = (Value)var5.next();
            if (value.getValueName().split("_")[0].equalsIgnoreCase(m.getName()) && value.isValueBoolean) {
               UIToggleButton button = new UIToggleButton(value, this.handler, this.width, height);
               this.toggleButtonList.put(value, button);
            }
         }
      }

   }

   private void addValues() {
      this.addSliders();
      this.addModes();
      this.addToggleButtons();
   }

   private void addMods() {
      Iterator var2 = ModManager.getModList().iterator();

      while(var2.hasNext()) {
         Mod m = (Mod)var2.next();
         if (m.getCategory() == this.c) {
            this.modList.add(m);
         }
      }

   }
}
