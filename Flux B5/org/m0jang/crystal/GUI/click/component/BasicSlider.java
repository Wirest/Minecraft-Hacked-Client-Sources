package org.m0jang.crystal.GUI.click.component;

import java.math.BigDecimal;
import org.lwjgl.input.Mouse;
import org.m0jang.crystal.Font.Fonts;
import org.m0jang.crystal.GUI.GuiManager;
import org.m0jang.crystal.GUI.click.RenderUtils;
import org.m0jang.crystal.GUI.click.WolframGui;
import org.m0jang.crystal.GUI.click.window.Window;
import org.m0jang.crystal.Utils.MathUtils;

public class BasicSlider extends Component {
   public float min;
   public float max;
   public float value;
   public float increment;
   protected boolean isDragging;
   public String customDisplayValue = null;

   public BasicSlider(Window window, int id, int offX, int offY, String title, float min, float max, float increment) {
      super(window, id, offX, offY, title);
      this.min = min;
      this.max = max;
      this.increment = increment;
      this.width = WolframGui.settingsWidth;
      this.height = WolframGui.defaultHeight;
      this.type = "BasicSlider";
   }

   public void render(int mouseX, int mouseY) {
      RenderUtils.drawRect((float)this.x, (float)this.y, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), (float)this.height, this.isHovered ? 0 : WolframGui.backgroundColor);
      String displayValue = this.customDisplayValue == null ? "" + round(this.value, 1) : this.customDisplayValue;
      Fonts.segoe16.drawString(displayValue, (float)(this.x + this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - Fonts.segoe16.getStringWidth(displayValue) - 2), (float)(this.y + 4), GuiManager.getHexMainColor());
      Fonts.segoe16.drawString(this.title, (float)(this.x + 2), (float)(this.y + 4), 16777215);
      RenderUtils.drawRect((float)(this.x + 2), (float)(this.y + this.height - 4), (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - 4), 1.0F, GuiManager.getHexMainColor() + 1342177280);
      RenderUtils.drawRect((float)(this.x + 2), (float)(this.y + this.height - 4), MathUtils.map(this.value, this.min, this.max, 0.0F, (float)(this.width - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0) - 4)), 1.0F, GuiManager.getHexMainColor());
   }

   public static BigDecimal round(float f, int decimalPlace) {
      BigDecimal bd = new BigDecimal(Float.toString(f));
      bd = bd.setScale(decimalPlace, 4);
      return bd;
   }

   public void update(int mouseX, int mouseY) {
      super.update(mouseX, mouseY);
      if (this.isDragging) {
         this.value = MathUtils.map((float)(mouseX - this.x), 2.0F, (float)(this.width - 2 - (this.window.scrollbarEnabled ? WolframGui.scrollbarWidth : 0)), this.min, this.max);
         this.value -= this.value % this.increment;
         if (this.value > this.max) {
            this.value = this.max;
         }

         if (this.value < this.min) {
            this.value = this.min;
         }

         this.window.action(this);
      }

      if (this.window.mouseOver(mouseX, mouseY)) {
         this.contains(mouseX, mouseY);
      }

   }

   public void mouseUpdates(int mouseX, int mouseY, boolean isPressed) {
      this.isHovered = this.contains(mouseX, mouseY) && this.window.mouseOver(mouseX, mouseY);
      if (isPressed && !this.wasMousePressed && this.isHovered) {
         this.isDragging = true;
      }

      if (!isPressed) {
         this.isDragging = false;
      }

      this.wasMousePressed = isPressed;
   }

   public void noMouseUpdates() {
      super.noMouseUpdates();
      if (!Mouse.isButtonDown(0)) {
         this.isDragging = false;
      }

   }
}
