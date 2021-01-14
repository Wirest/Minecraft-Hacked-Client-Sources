package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs;

import java.awt.Color;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import rip.autumn.core.Autumn;
import rip.autumn.module.Module;
import rip.autumn.module.impl.visuals.hud.HUDMod;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.utils.render.Palette;

public class ModuleTab extends AbstractTab {
   private float hoverPosition;

   public ModuleTab(TabHandler handler, Module stateObject, Tab parent, TabBlock children, TabBlock container) {
      super(handler, stateObject, parent, children, container);
   }

   public void doInvocation() {
      ((Module)this.stateObject).toggle();
   }

   public void renderTabFront() {
      FontRenderer fr = this.mc.fontRenderer;
      Color c = (Color)((HUDMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(HUDMod.class)).color.getValue();
      int color = Palette.fade(c).getRGB();
      if (this.handler.isInDirectTree(this)) {
         if (this.hoverPosition < 7.0F) {
            this.hoverPosition = Math.min(7.0F, this.hoverPosition + 0.5F);
         }
      } else if (this.handler.getCurrentTab() != this && this.hoverPosition != 0.0F) {
         this.hoverPosition = Math.max(0.0F, this.hoverPosition - 0.5F);
      }

      if (((Module)this.stateObject).isEnabled()) {
         Gui.drawRect((double)((float)this.getPosX() - 0.5F), (double)this.getPosY(), (double)((float)this.getPosX() + 1.5F), (double)(this.getPosY() + this.getHeight()), color);
         Gui.drawRect((double)((float)this.getPosX() + 1.5F), (double)this.getPosY(), (double)((float)(this.getPosX() + this.getWidth()) - 1.5F), (double)(this.getPosY() + this.getHeight()), 436536581);
      }

      fr.drawStringWithShadow(((Module)this.stateObject).getLabel(), (float)(this.getPosX() + 3) + this.hoverPosition, (float)this.getPosY() + 2.5F, ((Module)this.stateObject).isEnabled() ? -1 : -5723992);
      if (((TabBlock)this.findChildren().get()).sizeOf() > 0) {
         fr.drawStringWithShadow("+", (float)(this.getPosX() + this.getWidth() - fr.getStringWidth("+") - 3), (float)(this.getPosY() + 2), ((Module)this.stateObject).isEnabled() ? -3355444 : -5723992);
      }

   }

   public int getTabWidth() {
      return this.mc.fontRenderer.getStringWidth(((Module)this.stateObject).getLabel() + (((TabBlock)this.findChildren().get()).sizeOf() > 0 ? " +" : "")) + 7 + 4;
   }
}
