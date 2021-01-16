package org.m0jang.crystal.GUI.click.component;

import java.awt.Color;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.MathUtils;
import org.m0jang.crystal.Values.ValueManager;

public class ModuleButton extends Button {
   public Module module;
   public boolean hasSettings;
   public boolean isHoveredMod;
   public boolean isHoveredSetting;
   public final int moduleIndex;

   public ModuleButton(Window window, int id, int offX, int offY, String title, String tooltip, Module module) {
      super(window, id, offX, offY, title);
      this.hasSettings = module.isHasSubModule() || !ValueManager.getValueByModName(module.getName()).isEmpty();
      this.width = Math.max(WolframGui.defaultWidth, window.width);
      this.height = WolframGui.buttonHeight;
      this.module = module;
      this.moduleIndex = Crystal.INSTANCE.getMods().getModList().indexOf(module);
      this.type = "ModuleButton";
   }

   public void render(int mouseX, int mouseY) {
      if (!this.hasSettings) {
         if (this.isHoveredMod && this.isHovered) {
            RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, 0);
         } else {
            RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, WolframGui.backgroundColor);
         }
      } else {
         if (this.isHoveredMod && this.isHovered) {
            RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - 20), (float)this.height, 0);
         } else {
            RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - 20), (float)this.height, WolframGui.backgroundColor);
         }

         if (this.isHoveredSetting && this.isHovered) {
            RenderUtils.drawRect((float)(this.x + (this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)) - 20), (float)this.y, 20.0F, (float)this.height, 0);
         } else {
            RenderUtils.drawRect((float)(this.x + (this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)) - 20), (float)this.y, 20.0F, (float)this.height, WolframGui.backgroundColor);
         }

         float width = (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0));
         RenderUtils.drawLine2D((double)((float)this.x + width - 20.0F + 6.0F), (double)(this.y + this.height / 2 - 3), (double)((float)this.x + width - 6.0F), (double)(this.y + this.height / 2 + -3), 0.5F, (new Color(255, 255, 255)).getRGB());
         RenderUtils.drawLine2D((double)((float)this.x + width - 20.0F + 6.0F), (double)(this.y + this.height / 2), (double)((float)this.x + width - 6.0F), (double)(this.y + this.height / 2), 0.5F, (new Color(255, 255, 255)).getRGB());
         RenderUtils.drawLine2D((double)((float)this.x + width - 20.0F + 6.0F), (double)(this.y + this.height / 2 + 3), (double)((float)this.x + width - 6.0F), (double)(this.y + this.height / 2 + 3), 0.5F, (new Color(255, 255, 255)).getRGB());
      }

      int color = this.isToggled ? GuiManager.getHexMainColor() : 16777215;
      Fonts.segoe16.drawString(this.title, this.x + 2, this.y + this.height / 2 - Fonts.segoe16.getHeight() / 2, color);
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
      if (this.module != null) {
         this.isToggled = this.module.isEnabled();
      } else {
         this.isToggled = false;
      }

   }

   protected void pressed() {
      if ((this.isHoveredMod || this.isHoveredSetting && !this.hasSettings) && this.module != null) {
         this.module.toggle();
      }

      if (this.isHoveredSetting && this.hasSettings) {
         Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).isEnabled = !Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).isEnabled;
         if (Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).isEnabled) {
            Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).x = this.x + this.width;
            Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).y = this.y - this.height - 3;
            Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).bringToFront();
            Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).isOpened = true;
         } else {
            Crystal.INSTANCE.guiManager.gui.getWindowByID(this.moduleIndex).isOpened = false;
         }
      }

   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      this.isHoveredMod = this.containsMod(mouseX, mouseY) && this.window.mouseOver(mouseX, mouseY) && this.isHovered;
      this.isHoveredSetting = this.containsSettings(mouseX, mouseY) && this.window.mouseOver(mouseX, mouseY) && this.isHovered;
      super.mouseUpdates(mouseX, mouseY, isPressed);
   }

   public boolean containsMod(int mouseX, int mouseY) {
      return MathUtils.contains((float)mouseX, (float)mouseY, (float)this.x, (float)this.y, (float)(this.x + this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - (this.hasSettings ? 20 : 0)), (float)(this.y + this.height));
   }

   public boolean containsSettings(int mouseX, int mouseY) {
      return MathUtils.contains((float)mouseX, (float)mouseY, (float)(this.x + (this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - 20)), (float)this.y, (float)(this.x + this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)(this.y + this.height));
   }
}
