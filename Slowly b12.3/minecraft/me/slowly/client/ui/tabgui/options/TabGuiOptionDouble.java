package me.slowly.client.ui.tabgui.options;

import me.slowly.client.Client;
import me.slowly.client.ui.tabgui.TabGui;
import me.slowly.client.ui.tabgui.draw.UITabSlot;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.KeyInputHandler;
import me.slowly.client.value.Value;
import org.lwjgl.opengl.GL11;

public class TabGuiOptionDouble extends TabGuiOption {
   public KeyInputHandler keyUp = new KeyInputHandler(200);
   public KeyInputHandler keyDown = new KeyInputHandler(208);

   public TabGuiOptionDouble(Value value, int x, int y, int height) {
      super(value, x, y, height);
   }

   public void draw() {
      float add = 8.0F * RenderUtil.delta;
      if (this.animationX + add < 1.0F) {
         this.animationX += add;
      } else if (this.animationX < 1.0F) {
         this.animationX = 1.0F;
      }

      double currentValue = ((Double)this.value.getValueState()).doubleValue();
      double step = this.value.getSteps();
      double newValue = currentValue;
      GL11.glColor3f(0.0F, 0.0F, 0.0F);
      this.width = TabGui.font.getStringWidth(String.valueOf(currentValue)) + 6;
      if (this.keyUp.canExcecute() && this.mc.currentScreen == null) {
         newValue = currentValue + step;
      }

      if (this.keyDown.canExcecute() && this.mc.currentScreen == null) {
         newValue -= step;
      }

      newValue = (double)Math.round(newValue * (1.0D / step)) / (1.0D / step);
      this.value.setValueState(newValue);
      if (((Double)this.value.getValueState()).doubleValue() != currentValue) {
         Client.getInstance().getFileUtil().saveValues();
      }

      currentValue = ((Double)this.value.getValueState()).doubleValue();
      if (currentValue > ((Double)this.value.getValueMax()).doubleValue()) {
         this.value.setValueState(this.value.getValueMax());
      }

      if (currentValue < ((Double)this.value.getValueMin()).doubleValue()) {
         this.value.setValueState(this.value.getValueMin());
      }

      if (((Double)this.value.getValueState()).doubleValue() != currentValue) {
         Client.getInstance().getFileUtil().saveValues();
      }

      String valueName = String.valueOf(this.value.getValueState());
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(this.x, this.y, (int)((float)this.width * this.animationX), this.height);
      UITabSlot.drawTabGuiSlot((float)this.x, (float)this.y, (float)this.width, (float)this.height);
      UITabSlot.drawTabGuiSelector((float)this.x, (float)this.y, (float)(this.x + this.width), (float)(this.y + this.height));
      UITabSlot.drawString(valueName, (float)(this.x + 1), (float)(this.y + (this.height - TabGui.font.FONT_HEIGHT) / 2), ClientUtil.reAlpha(-1, this.animationX), true);
      GL11.glDisable(3089);
      GL11.glPopMatrix();
   }
}
