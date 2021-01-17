package me.slowly.client.ui.tabgui;

import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.mod.Mod;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.ui.tabgui.draw.UITabSlot;
import me.slowly.client.ui.tabgui.options.TabGuiOption;
import me.slowly.client.ui.tabgui.options.TabGuiOptionBoolean;
import me.slowly.client.ui.tabgui.options.TabGuiOptionDouble;
import me.slowly.client.ui.tabgui.options.TabGuiOptionMode;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.KeyInputHandler;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class TabGuiValues {
   public Mod mod;
   private Value currentValue = null;
   public TabGuiOption openOption = null;
   public float x;
   public float y;
   public float width;
   public float height;
   protected Minecraft mc = Minecraft.getMinecraft();
   public float animationX = 0.0F;
   public float animateY;
   public KeyInputHandler enterKey;
   public KeyInputHandler arrowUp;
   public KeyInputHandler arrowDown;
   public KeyInputHandler arrowLeft;
   public KeyInputHandler arrowRight;
   private int valueCount = 0;
   private ArrayList valueList = new ArrayList();
   private ArrayList optionList = new ArrayList();
   public boolean activated = false;

   public TabGuiValues(Mod mod, float x, float y, float width, float height, KeyInputHandler enterKey, KeyInputHandler arrowUp, KeyInputHandler arrowDown, KeyInputHandler arrowLeft, KeyInputHandler arrowRight) {
      this.x = x;
      this.y = y;
      this.animateY = y;
      this.width = width;
      this.height = height;
      this.mod = mod;
      this.arrowUp = arrowUp;
      this.arrowDown = arrowDown;
      this.arrowLeft = arrowLeft;
      this.arrowRight = new KeyInputHandler(205);
      this.enterKey = enterKey;
      this.addValues();
   }

   private void drawRectWithAnimation() {
      int lightBlue = -11830879;
      int darkBlue = -14139823;
      float add = 50.0F * RenderUtil.delta;
      int modY = (int)(this.y + (float)this.valueCount * this.height);
      boolean speedUp = this.valueCount == 0 && this.animateY - add > (float)modY;
      boolean speedDown = this.valueCount == this.valueList.size() - 1 && this.animateY + add < (float)modY;
      if (this.animateY - add > (float)modY) {
         this.animateY -= add * (float)(speedUp ? 5 : 1);
      } else if (this.animateY > (float)modY) {
         this.animateY = (float)modY;
      }

      if (this.animateY + add < (float)modY) {
         this.animateY += add * (float)(speedDown ? 5 : 1);
      } else if (this.animateY < (float)modY) {
         this.animateY = (float)modY;
      }

      UITabSlot.drawTabGuiSelector(this.x, this.animateY, this.x + this.width * this.animationX, this.animateY + this.height);
   }

   private void prepare() {
      if (((Boolean)CustomHUDTabGui.enableBorder.getValueState()).booleanValue()) {
         ((Double)CustomHUDTabGui.borderSize.getValueState()).floatValue();
      } else {
         float var10000 = 0.0F;
      }

      if (this.valueCount < 0) {
         this.valueCount = 0;
      }

      if (this.valueCount > this.valueList.size() - 1) {
         this.valueCount = this.valueList.size() - 1;
      }

      this.currentValue = (Value)this.valueList.get(this.valueCount);
      this.width = (float)(this.getLongestValueWidth() + 4);
   }

   private int getLongestValueWidth() {
      float longest = -1.0F;
      Iterator var3 = this.valueList.iterator();

      while(var3.hasNext()) {
         Value val = (Value)var3.next();
         String name;
         if (val.isValueMode) {
            name = val.getModeTitle();
            if ((float)this.mc.fontRendererObj.getStringWidth(name) > longest) {
               longest = (float)(this.mc.fontRendererObj.getStringWidth(name) + 1);
            }
         } else {
            name = val.getValueName().split("_")[1];
            if ((float)this.mc.fontRendererObj.getStringWidth(name) > longest) {
               longest = (float)this.mc.fontRendererObj.getStringWidth(name);
            }
         }
      }

      return (int)longest + ((Double)CustomHUDTabGui.scaleX.getValueState()).intValue();
   }

   private void handleKeyInput() {
      if (Minecraft.getMinecraft().currentScreen != null) {
         this.activated = false;
      } else {
         int modY = (int)(this.y + (float)this.valueCount * this.height);
         if (this.activated && this.arrowRight.canExcecute() && this.openOption == null) {
            this.openOption = this.getCurrentOption();
            if (this.openOption instanceof TabGuiOptionDouble) {
               TabGuiOptionDouble op = (TabGuiOptionDouble)this.openOption;
               op.keyUp.clicked = true;
               op.keyDown.clicked = true;
            }
         }

         if (this.activated && this.arrowUp.canExcecute() && this.openOption == null) {
            this.valueCount -= this.valueCount == 0 ? -this.valueList.size() + 1 : 1;
         }

         if (this.activated && this.arrowDown.canExcecute() && this.openOption == null) {
            boolean jumpAnimation = this.valueCount == this.valueList.size() - 1;
            this.valueCount += this.valueCount == this.valueList.size() - 1 ? -this.valueList.size() + 1 : 1;
            if (jumpAnimation) {
               this.animateY = (float)modY;
            }
         }

         this.activated = true;
      }
   }

   public void draw() {
      this.prepare();
      this.handleKeyInput();
      float add = 2.0F * RenderUtil.delta;
      if (this.animationX + add < 1.0F) {
         this.animationX += add;
      } else if (this.animationX < 1.0F) {
         this.animationX = 1.0F;
      }

      float borderSize = ((Boolean)CustomHUDTabGui.enableBorder.getValueState()).booleanValue() ? ((Double)CustomHUDTabGui.borderSize.getValueState()).floatValue() : 0.0F;
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)(this.x - borderSize), (int)(this.y - borderSize), (int)(this.width * this.animationX + borderSize * 4.0F), (int)((float)this.valueList.size() * this.height + borderSize * 4.0F));
      UITabSlot.drawTabGuiSlot(this.x, this.y, this.width * this.animationX, this.height * (float)this.valueList.size());
      this.drawRectWithAnimation();
      float yAxis = this.y;

      int yCount;
      for(yCount = 0; yCount < this.valueList.size(); ++yCount) {
         Value value = (Value)this.valueList.get(yCount);
         String valueName = value.getValueName().split("_")[1];
         if (value.isValueMode) {
            valueName = value.getModeTitle();
         }

         UITabSlot.drawString(valueName, this.x + 1.0F, yAxis + (this.height - (float)TabGui.font.FONT_HEIGHT) / 2.0F, ClientUtil.reAlpha(-1, this.animationX), yCount == this.valueCount);
         yAxis += this.height;
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      if (this.openOption != null) {
         this.openOption.height = (int)this.height;
         this.openOption.draw();
         this.openOption.x = (int)((float)((int)(this.x + this.width) + 2) + borderSize * 2.0F);
      }

      yCount = (int)this.y;
      boolean count = false;
      Iterator var7 = this.optionList.iterator();

      while(var7.hasNext()) {
         TabGuiOption value = (TabGuiOption)var7.next();
         String name = value.value.getValueName().split("_")[0];
         if (name.equalsIgnoreCase(this.mod.getName())) {
            value.y = yCount;
            yCount = (int)((float)yCount + this.height);
         }
      }

   }

   private TabGuiOption getCurrentOption() {
      Iterator var2 = this.optionList.iterator();

      while(var2.hasNext()) {
         TabGuiOption tab = (TabGuiOption)var2.next();
         if (tab.value == this.currentValue) {
            return tab;
         }
      }

      return null;
   }

   private void addValues() {
      int yAxis = (int)this.y;
      Iterator var3 = Value.list.iterator();

      while(var3.hasNext()) {
         Value value = (Value)var3.next();
         String name = value.getValueName().split("_")[0];
         if (name.equalsIgnoreCase(this.mod.getName())) {
            this.valueList.add(value);
            if (value.isValueDouble) {
               this.optionList.add(new TabGuiOptionDouble(value, (int)(this.x + (float)this.getLongestValueWidth() + 3.0F), yAxis, (int)this.height));
            }

            if (value.isValueBoolean) {
               this.optionList.add(new TabGuiOptionBoolean(value, (int)(this.x + (float)this.getLongestValueWidth() + 3.0F), yAxis, (int)this.height));
            }

            if (value.isValueMode) {
               this.optionList.add(new TabGuiOptionMode(value, (int)(this.x + (float)this.getLongestValueWidth() + 3.0F), yAxis, (int)this.height));
            }

            yAxis = (int)((float)yAxis + this.height);
         }
      }

   }
}
