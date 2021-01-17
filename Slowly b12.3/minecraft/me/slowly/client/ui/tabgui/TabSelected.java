package me.slowly.client.ui.tabgui;

import java.util.ArrayList;
import java.util.Iterator;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.ui.hudcustomizer.customs.CustomHUDTabGui;
import me.slowly.client.ui.tabgui.draw.UITabSlot;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.handler.KeyInputHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TabSelected {
   private Mod.Category category;
   public int x;
   public int y;
   public int width;
   public int height;
   public boolean open;
   public ArrayList modList = new ArrayList();
   public Mod currentMod = null;
   public TabGuiValues openValues = null;
   public float animationX = 0.0F;
   public float animateY;
   public boolean activated = false;
   private int modCount = 0;
   protected Minecraft mc = Minecraft.getMinecraft();
   private KeyInputHandler enterKey;
   private KeyInputHandler arrowUp;
   private KeyInputHandler arrowDown;
   private KeyInputHandler arrowLeft;
   private KeyInputHandler arrowRight;
   private ArrayList tabs = new ArrayList();
   public int xAdd = 0;

   public TabSelected(Mod.Category category, int x, int y, int width, int height, KeyInputHandler arrowUp, KeyInputHandler arrowDown, KeyInputHandler arrowLeft, KeyInputHandler arrowRight) {
      this.category = category;
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
      this.animateY = (float)y;
      this.arrowUp = new KeyInputHandler(200);
      this.arrowDown = new KeyInputHandler(208);
      this.arrowLeft = new KeyInputHandler(203);
      this.arrowRight = arrowRight;
      this.enterKey = new KeyInputHandler(28);
      this.addMods();
      this.width = (int)this.getLongestModWidth() + 5;
      int yAxis = y;
      Iterator var12 = ModManager.getModList().iterator();

      while(var12.hasNext()) {
         Mod m = (Mod)var12.next();
         if (m.getCategory().equals(this.category)) {
            if (m.hasValues()) {
               this.tabs.add(new TabGuiValues(m, (float)x + this.getLongestModWidth(), (float)yAxis, (float)width, (float)height, this.enterKey, arrowUp, arrowDown, arrowLeft, arrowRight));
            }

            yAxis += height;
         }
      }

   }

   private void drawRectWithAnimation() {
      int lightBlue = -11830879;
      int darkBlue = -14139823;
      float add = 50.0F * RenderUtil.delta;
      int modY = this.y + this.modCount * this.height;
      boolean speedUp = this.modCount == 0 && this.animateY - add > (float)modY;
      boolean speedDown = this.modCount == this.modList.size() - 1 && this.animateY + add < (float)modY;
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

      UITabSlot.drawTabGuiSelector((float)(this.x + this.xAdd), this.animateY, (float)(this.x + this.xAdd) + (float)this.width * this.animationX, this.animateY + (float)this.height);
   }

   private void prepare() {
      this.width = (int)this.getLongestModWidth() + 2;
      int yAxis = this.y;
      float borderSize = ((Boolean)CustomHUDTabGui.enableBorder.getValueState()).booleanValue() ? ((Double)CustomHUDTabGui.borderSize.getValueState()).floatValue() : 0.0F;

      for(Iterator var4 = this.tabs.iterator(); var4.hasNext(); yAxis += this.height) {
         TabGuiValues selected = (TabGuiValues)var4.next();
         selected.y = (float)yAxis;
         selected.height = (float)this.height;
         selected.x = (float)(this.x + this.xAdd + this.width + 1) + borderSize * 2.0F + 1.0F;
      }

      if (this.modCount < 0) {
         this.modCount = 0;
      }

      if (this.modCount > this.modList.size() - 1) {
         this.modCount = this.modList.size() - 1;
      }

      this.currentMod = (Mod)this.modList.get(this.modCount);
   }

   private void handleKeyInput() {
      if (Minecraft.getMinecraft().currentScreen != null) {
         this.activated = false;
      } else {
         if (!this.activated) {
            this.activated = this.arrowRight.clicked = this.arrowDown.clicked = this.arrowUp.clicked = this.enterKey.clicked = true;
         }

         if (this.arrowUp.canExcecute() && this.openValues == null) {
            this.modCount -= this.modCount == 0 ? -this.modList.size() + 1 : 1;
         }

         if (this.arrowDown.canExcecute() && this.openValues == null) {
            this.modCount += this.modCount == this.modList.size() - 1 ? -this.modList.size() + 1 : 1;
         }

         if (this.enterKey.canExcecute() && this.openValues == null) {
            this.currentMod.set(!this.currentMod.isEnabled());
         }

         if (this.arrowRight.canExcecute()) {
            boolean b = false;
            if (this.openValues == null) {
               b = true;
            }

            this.openValues = this.getCurrentModValues();
            if (b) {
               this.openValues.arrowRight.canExcecute();
            }
         }

      }
   }

   public void draw(int xAdd) {
      this.prepare();
      this.handleKeyInput();
      float add = 2.0F * RenderUtil.delta;
      if (this.animationX + add < 1.0F) {
         this.animationX += add;
      } else if (this.animationX < 1.0F) {
         this.animationX = 1.0F;
      }

      this.xAdd = xAdd;
      float borderSize = ((Boolean)CustomHUDTabGui.enableBorder.getValueState()).booleanValue() ? ((Double)CustomHUDTabGui.borderSize.getValueState()).floatValue() : 0.0F;
      float borderProduct = (float)(((Boolean)CustomHUDTabGui.smoothBorder.getValueState()).booleanValue() ? 4 : 4);
      GL11.glPushMatrix();
      GL11.glEnable(3089);
      RenderUtil.doGlScissor((int)((float)(this.x + xAdd) - borderSize), (int)((float)this.y - borderSize), (int)((float)this.width * this.animationX + borderSize * borderProduct), (int)(((float)this.height + borderSize * 100.0F) * (float)this.modList.size()));
      GL11.glColor3f(0.0F, 0.0F, 0.0F);
      UITabSlot.drawTabGuiSlot((float)(this.x + xAdd), (float)this.y, (float)this.width * this.animationX, (float)(this.height * this.modList.size()));
      this.drawRectWithAnimation();
      int yAxis = this.y;

      int yCount;
      Mod m;
      for(yCount = 0; yCount < this.modList.size(); ++yCount) {
         m = (Mod)this.modList.get(yCount);
         UITabSlot.drawString(m.getName(), (float)(this.x + 1 + xAdd), (float)(yAxis + (this.height - TabGui.font.FONT_HEIGHT) / 2), ClientUtil.reAlpha(m.isEnabled() ? -1 : -8618884, this.animationX), yCount == this.modCount);
         int iconSize = 4;
         if (m.hasValues()) {
            int xMid = this.x + this.width + xAdd - iconSize / 2 - 2;
            int yMid = yAxis + this.height / 2;
            GL11.glPushMatrix();
            GL11.glTranslated((double)xMid, (double)yMid, 0.0D);
            GL11.glRotated(-90.0D, 0.0D, 0.0D, 1.0D);
            GL11.glTranslated((double)(-xMid), (double)(-yMid), 0.0D);
            RenderUtil.drawImage(new ResourceLocation("slowly/icon/arrow-down.png"), this.x + xAdd + this.width - iconSize - 2, yAxis + (this.height - iconSize) / 2, iconSize, iconSize);
            GL11.glPopMatrix();
         }

         GL11.glColor3f(0.0F, 0.0F, 0.0F);
         yAxis += this.height;
      }

      GL11.glDisable(3089);
      GL11.glPopMatrix();
      if (this.openValues != null) {
         this.openValues.draw();
      }

      yCount = this.y;

      for(Iterator var11 = this.modList.iterator(); var11.hasNext(); yCount += this.height) {
         m = (Mod)var11.next();
         this.openValues.y = (float)yCount;
      }

   }

   private TabGuiValues getCurrentModValues() {
      Iterator var2 = this.tabs.iterator();

      while(var2.hasNext()) {
         TabGuiValues tabs = (TabGuiValues)var2.next();
         if (tabs.mod == this.currentMod) {
            return tabs;
         }
      }

      return null;
   }

   public Mod.Category getCategory() {
      return this.category;
   }

   public int getY() {
      return this.y;
   }

   private float getLongestModWidth() {
      float longest = -1.0F;
      Iterator var3 = ModManager.getModList().iterator();

      while(var3.hasNext()) {
         Mod m = (Mod)var3.next();
         if (m.getCategory().equals(this.category) && (float)this.mc.fontRendererObj.getStringWidth(m.getName() + (m.hasValues() ? " >" : "")) > longest) {
            longest = (float)this.mc.fontRendererObj.getStringWidth(m.getName() + (m.hasValues() ? " >" : ""));
         }
      }

      return longest + (float)((Double)CustomHUDTabGui.scaleX.getValueState()).intValue();
   }

   private void addMods() {
      Iterator var2 = ModManager.getModList().iterator();

      while(var2.hasNext()) {
         Mod m = (Mod)var2.next();
         if (m.getCategory().equals(this.category)) {
            this.modList.add(m);
         }
      }

   }
}
