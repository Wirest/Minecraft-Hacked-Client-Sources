package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.AnimationTimer;
import org.m0jang.crystal.Values.Value;

public class ComboBox extends Component {
   public String current;
   public Value modeValue;
   public int HoveredIndex;
   public Module module;
   AnimationTimer anim;

   public ComboBox(Value modeValue, Window window, int id, int offX, int offY, Module mod) {
      super(window, id, offX, offY, "");
      this.anim = new AnimationTimer(20);
      this.width = WolframGui.settingsWidth;
      this.modeValue = modeValue;
      this.height = WolframGui.defaultHeight * modeValue.getOptions().length;
      this.type = "ComboBox";
      this.module = mod;
   }

   public ComboBox(Value modeValue, Window window, int id, int offX, int offY) {
      this(modeValue, window, id, offX, offY, (Module)null);
   }

   public ComboBox(Module module, Window window, int id, int offX, int offY) {
      this(module.getMode(), window, id, offX, offY, (Module)null);
   }

   public void render(int mouseX, int mouseY) {
      try {
         for(int i = 0; i < this.modeValue.getOptions().length; ++i) {
            float y = (float)(this.y + WolframGui.defaultHeight * i);
            RenderUtils.drawRect((float)this.x, y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)WolframGui.defaultHeight, this.HoveredIndex == i && this.isHovered ? 0 : WolframGui.backgroundColor);
            Fonts.segoe16.drawString(this.modeValue.getOptions()[i], (float)(this.x + 18), y + 5.5F, 16777215);
            RenderUtils.drawRect((float)(this.x + 5), y + 5.0F, 8.0F, 8.0F, GuiManager.getHexMainColor());
            if (this.modeValue.getSelectedOption().equals(this.modeValue.getOptions()[i])) {
               RenderUtils.drawRect((float)(this.x + 5) + 1.5F, y + 5.0F + 1.5F, 5.0F, 5.0F, 16777215);
            }
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      this.isHovered = this.contains(mouseX, mouseY) && this.window.mouseOver(mouseX, mouseY);
      if (!this.isHovered) {
         this.HoveredIndex = -1;
      } else {
         this.HoveredIndex = this.containsIndex(mouseX, mouseY);
         if (isPressed && !this.wasMousePressed && this.isHovered && this.HoveredIndex != -1) {
            this.modeValue.setSelectedOption(this.modeValue.getOptions()[this.HoveredIndex]);
            if (this.module != null && this.module.isHasSubModule()) {
               this.module.updateSubModule();
            }
         }

         this.wasMousePressed = isPressed;
      }
   }

   public int containsIndex(int mouseX, int mouseY) {
      int diff = mouseY - this.y;
      int result = 0;

      for(int i = diff; i > WolframGui.defaultHeight; i -= WolframGui.defaultHeight) {
         ++result;
      }

      return result;
   }
}
