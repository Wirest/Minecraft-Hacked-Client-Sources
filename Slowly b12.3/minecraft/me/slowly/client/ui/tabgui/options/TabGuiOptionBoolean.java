package me.slowly.client.ui.tabgui.options;

import me.slowly.client.Client;
import me.slowly.client.ui.tabgui.TabGui;
import me.slowly.client.ui.tabgui.draw.UITabSlot;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.KeyInputHandler;
import me.slowly.client.value.Value;
import org.lwjgl.opengl.GL11;

public class TabGuiOptionBoolean extends TabGuiOption {
   private KeyInputHandler keyUp = new KeyInputHandler(200);
   private KeyInputHandler keyDown = new KeyInputHandler(208);
   private KeyInputHandler keyEnter = new KeyInputHandler(28);
   private int option = 0;

   public TabGuiOptionBoolean(Value value, int x, int y, int height) {
      super(value, x, y, height);
   }

   public void draw() {
      this.width = 20;
      float add = 8.0F * RenderUtil.delta;
      if (this.animationX + add < 1.0F) {
         this.animationX += add;
      } else if (this.animationX < 1.0F) {
         this.animationX = 1.0F;
      }

      GL11.glColor3f(0.0F, 0.0F, 0.0F);
      if (this.mc.currentScreen == null) {
         if (this.keyDown.canExcecute()) {
            this.option = this.option == 1 ? 0 : 1;
         }

         if (this.keyUp.canExcecute()) {
            this.option = this.option == 1 ? 0 : 1;
         }

         if (this.keyEnter.canExcecute()) {
            this.value.setValueState(this.option == 0);
            Client.getInstance().getFileUtil().saveValues();
         }
      }

      float yAdd = (float)(this.height * this.option);
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(this.x, this.y, (int)((float)this.width * this.animationX), this.height * 2);
      UITabSlot.drawTabGuiSlot((float)this.x, (float)this.y, (float)this.width, (float)(this.height * 2));
      UITabSlot.drawTabGuiSelector((float)this.x, (float)this.y + yAdd, (float)(this.x + this.width), (float)(this.y + this.height) + yAdd);
      UITabSlot.drawString("On", (float)(this.x + 1), (float)(this.y + (this.height - TabGui.font.FONT_HEIGHT) / 2), ClientUtil.reAlpha(!((Boolean)this.value.getValueState()).booleanValue() ? -8618884 : -1, this.animationX), this.option == 0);
      UITabSlot.drawString("Off", (float)(this.x + 1), (float)(this.y + (this.height * 3 - TabGui.font.FONT_HEIGHT) / 2), ClientUtil.reAlpha(((Boolean)this.value.getValueState()).booleanValue() ? -8618884 : -1, this.animationX), this.option != 0);
      GL11.glDisable(3089);
   }
}
