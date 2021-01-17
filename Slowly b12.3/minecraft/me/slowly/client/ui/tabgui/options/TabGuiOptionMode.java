package me.slowly.client.ui.tabgui.options;

import me.slowly.client.Client;
import me.slowly.client.ui.tabgui.TabGui;
import me.slowly.client.ui.tabgui.draw.UITabSlot;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.KeyInputHandler;
import me.slowly.client.value.Value;
import org.lwjgl.opengl.GL11;

public class TabGuiOptionMode extends TabGuiOption {
   private KeyInputHandler keyUp = new KeyInputHandler(200);
   private KeyInputHandler keyDown = new KeyInputHandler(208);
   private KeyInputHandler keyEnter = new KeyInputHandler(28);
   private int option = 0;

   public TabGuiOptionMode(Value value, int x, int y, int height) {
      super(value, x, y, height);
   }

   public void draw() {
      int newWidth = -1;

      for(int i = 0; i < this.value.mode.size(); ++i) {
         if (TabGui.font.getStringWidth((String)this.value.mode.get(i)) > newWidth) {
            newWidth = TabGui.font.getStringWidth((String)this.value.mode.get(i));
         }
      }

      GL11.glColor3f(0.0F, 0.0F, 0.0F);
      this.width = newWidth + 5;
      float add = 8.0F * RenderUtil.delta;
      if (this.animationX + add < 1.0F) {
         this.animationX += add;
      } else if (this.animationX < 1.0F) {
         this.animationX = 1.0F;
      }

      if (this.mc.currentScreen == null) {
         if (this.keyDown.canExcecute()) {
            ++this.option;
         }

         if (this.keyUp.canExcecute()) {
            --this.option;
         }

         if (this.option < 0) {
            this.option = this.value.mode.size() - 1;
         }

         if (this.option > this.value.mode.size() - 1) {
            this.option = 0;
         }

         if (this.keyEnter.canExcecute()) {
            this.value.setCurrentMode(this.option);
            Client.getInstance().getFileUtil().saveValues();
         }
      }

      float yAdd = (float)(this.height * this.option);
      GL11.glEnable(3089);
      RenderUtil.doGlScissor(this.x, this.y, (int)((float)this.width * this.animationX), this.height * this.value.mode.size());
      UITabSlot.drawTabGuiSlot((float)this.x, (float)this.y, (float)this.width, (float)(this.height * this.value.mode.size()));
      UITabSlot.drawTabGuiSelector((float)this.x, (float)this.y + yAdd, (float)(this.x + this.width), (float)(this.y + this.height) + yAdd);
      int yAxis = this.y;

      for(int i = 0; i < this.value.mode.size(); ++i) {
         UITabSlot.drawString((String)this.value.mode.get(i), (float)(this.x + 1), (float)(yAxis + (this.height - TabGui.font.FONT_HEIGHT) / 2), this.value.isCurrentMode(this.value.getModeAt(i)) ? -1 : -8618884, this.option == i);
         yAxis += this.height;
      }

      GL11.glDisable(3089);
   }
}
