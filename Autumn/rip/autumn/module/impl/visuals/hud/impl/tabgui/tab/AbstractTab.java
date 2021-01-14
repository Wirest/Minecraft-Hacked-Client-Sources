package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab;

import com.google.common.base.Preconditions;
import java.awt.Color;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;

public abstract class AbstractTab implements Tab {
   protected final Minecraft mc = Minecraft.getMinecraft();
   protected final Object stateObject;
   protected final TabHandler handler;
   protected final Tab parent;
   protected final TabBlock container;
   protected final TabBlock children;
   private int posX = 0;
   private int posY = 0;
   private int height = -1;
   private int width = -1;

   public AbstractTab(TabHandler handler, Object stateObject, Tab parent, TabBlock children, TabBlock container) {
      this.handler = (TabHandler)Preconditions.checkNotNull(handler);
      this.stateObject = stateObject;
      this.container = container;
      this.parent = parent;
      this.children = children;
   }

   public void renderTabBack() {
      Gui.drawRect((double)this.posX, (double)this.posY, (double)(this.posX + this.width), (double)(this.posY + this.height), (new Color(13, 13, 13, 220)).getRGB());
   }

   public Object getStateObject() {
      return this.stateObject;
   }

   public Optional findParent() {
      return Optional.ofNullable(this.parent);
   }

   public Optional findChildren() {
      return Optional.ofNullable(this.children);
   }

   public TabBlock getContainerTabBlock() {
      return this.container;
   }

   public TabHandler getHandler() {
      return this.handler;
   }

   public void addChild(Tab... children) {
      Tab[] var2 = children;
      int var3 = children.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Tab tab = var2[var4];
         this.children.appendTab(tab);
      }

   }

   public int getPosX() {
      return this.posX;
   }

   public int getPosY() {
      return this.posY;
   }

   public int getWidth() {
      return this.width;
   }

   public int getHeight() {
      return this.height;
   }

   public void setPosX(int posX) {
      this.posX = posX;
   }

   public void setPosY(int posY) {
      this.posY = posY;
   }

   public void setDimensionAndPos(int renderPosX, int renderPosY, int width, int height) {
      this.posX = renderPosX;
      this.posY = renderPosY;
      this.width = width;
      this.height = height;
   }
}
