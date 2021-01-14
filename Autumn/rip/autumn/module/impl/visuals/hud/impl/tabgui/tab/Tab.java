package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab;

import java.util.Optional;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;

public interface Tab {
   void doInvocation();

   void renderTabBack();

   void renderTabFront();

   int getTabWidth();

   Object getStateObject();

   Optional findParent();

   Optional findChildren();

   TabBlock getContainerTabBlock();

   TabHandler getHandler();

   void addChild(Tab... var1);

   int getPosX();

   int getPosY();

   int getWidth();

   int getHeight();

   void setPosX(int var1);

   void setPosY(int var1);

   void setDimensionAndPos(int var1, int var2, int var3, int var4);
}
