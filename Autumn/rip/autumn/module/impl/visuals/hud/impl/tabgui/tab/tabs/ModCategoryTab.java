package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import rip.autumn.module.ModuleCategory;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.utils.font.FontRendererHook;
import rip.autumn.utils.font.FontUtils;

public class ModCategoryTab extends AbstractTab {
   private static final FontRenderer ICONS = new FontRendererHook(FontUtils.getFontFromTTF(new ResourceLocation("autumn/undefeated.ttf"), 14.0F, 0), true, true);
   private float hoverPosition;

   public ModCategoryTab(TabHandler handler, ModuleCategory stateObject, Tab parent, TabBlock children, TabBlock container) {
      super(handler, stateObject, parent, children, container);
   }

   public void doInvocation() {
      if (this.handler.canDescendTree()) {
         this.handler.descendTabTree();
      }

   }

   public void renderTabFront() {
      FontRenderer fr = this.mc.fontRenderer;
      if (this.handler.isInDirectTree(this)) {
         if (this.hoverPosition < 7.0F) {
            this.hoverPosition = Math.min(7.0F, this.hoverPosition + 0.5F);
         }
      } else if (this.handler.getCurrentTab() != this && this.hoverPosition != 0.0F) {
         this.hoverPosition = Math.max(0.0F, this.hoverPosition - 0.5F);
      }

      String character = "";
      switch((ModuleCategory)this.stateObject) {
      case COMBAT:
         character = "s";
         break;
      case PLAYER:
         character = "q";
         break;
      case MOVEMENT:
         character = "j";
         break;
      case VISUALS:
         character = "t";
         break;
      case WORLD:
         character = "v";
         break;
      case EXPLOIT:
         character = "m";
      }

      ICONS.drawStringWithShadow(character, (float)(this.getTabWidth() - 8), (float)this.getPosY() + 5.0F, -1);
      fr.drawStringWithShadow(((ModuleCategory)this.stateObject).toString(), (float)(this.getPosX() + 3) + this.hoverPosition, (float)this.getPosY() + 2.5F, 16777215);
   }

   public int getTabWidth() {
      return 65;
   }
}
