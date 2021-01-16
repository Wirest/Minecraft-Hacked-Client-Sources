package org.m0jang.crystal.GUI.click.component;

import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.Utils.AnimationTimer;
import org.m0jang.crystal.Values.Value;

public class Checkbox extends Component {
   public boolean value;
   public String setting;
   public Value storage;
   AnimationTimer anim = new AnimationTimer(20);

   public Checkbox(Value storage, Window window, int id, int offX, int offY, String title, String setting) {
      super(window, id, offX, offY, title);
      this.width = WolframGui.settingsWidth;
      this.height = WolframGui.defaultHeight;
      this.storage = storage;
      this.setting = setting;
      this.type = "Checkbox";
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
      if (this.storage == null) {
         this.value = false;
      } else {
         this.value = this.storage.getBooleanValue();
      }

      this.anim.update(this.value);
      if (this.window.mouseOver(mouseX, mouseY)) {
         this.contains(mouseX, mouseY);
      }

   }

   public void render(int mouseX, int mouseY) {
      RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, this.isHovered ? 0 : WolframGui.backgroundColor);
      Fonts.segoe16.drawString(this.title, (float)(this.x + 18), (float)this.y + 5.5F, 16777215);
      RenderUtils.drawRect((float)(this.x + 5), (float)(this.y + 5), (float)(WolframGui.defaultHeight - 10), (float)(WolframGui.defaultHeight - 10), GuiManager.getHexMainColor());
      if (this.anim.getValue() > 0.0D) {
         RenderUtils.drawLine2D((double)(this.x + 7), (double)(this.y + 8), (double)(this.x + 7), (double)(this.y + 8) + (double)(this.height - 15) * Math.min(0.5D, this.anim.getValue()) * 2.0D, 1.5F, 16777215);
         if (this.anim.getValue() > 0.5D) {
            RenderUtils.drawLine2D((double)(this.x + 7), (double)(this.y + this.height - 7), (double)(this.x + 7) + (double)(this.height - 10) * (this.anim.getValue() - 0.5D) * 2.0D, (double)(this.y + this.height - 7) - (double)(this.height - 13) * (this.anim.getValue() - 0.5D) * 2.0D, 1.5F, 16777215);
         }
      }

   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      this.isHovered = this.contains(mouseX, mouseY) && this.window.mouseOver(mouseX, mouseY);
      if (isPressed && !this.wasMousePressed && this.isHovered && this.storage != null) {
         this.value = !this.value;
         this.storage.setBooleanValue(this.value);
      }

      this.wasMousePressed = isPressed;
   }
}
