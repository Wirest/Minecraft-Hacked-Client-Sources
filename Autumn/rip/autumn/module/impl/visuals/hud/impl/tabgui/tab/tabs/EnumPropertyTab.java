package rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.tabs;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.TabHandler;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.block.TabBlock;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.AbstractTab;
import rip.autumn.module.impl.visuals.hud.impl.tabgui.tab.Tab;
import rip.autumn.module.option.impl.EnumOption;

public class EnumPropertyTab extends AbstractTab {
   private boolean selected;

   public EnumPropertyTab(TabHandler handler, EnumOption stateObject, Tab parent, TabBlock children, TabBlock container) {
      super(handler, stateObject, parent, children, container);
   }

   public void doInvocation() {
      Enum[] values = ((EnumOption)this.stateObject).getValues();
      this.selected = true;
      this.handler.disableDefaultKeyListening();
      this.handler.subscribeActionToKey(this, 203, (key, tab) -> {
         for(int i = 0; i < values.length; ++i) {
            if (values[i] == ((EnumOption)this.stateObject).getValue()) {
               ((EnumOption)this.stateObject).setValue(i == 0 ? values[values.length - 1] : values[i - 1]);
               break;
            }
         }

      });
      this.handler.subscribeActionToKey(this, 205, (key, tab) -> {
         for(int i = 0; i < values.length; ++i) {
            if (values[i] == ((EnumOption)this.stateObject).getValue()) {
               ((EnumOption)this.stateObject).setValue(i == values.length - 1 ? values[0] : values[i + 1]);
               break;
            }
         }

      });
      this.handler.subscribeActionToKey(this, 28, (key, tab) -> {
         this.selected = false;
         this.handler.unsubscribeSpecificActions(this);
         this.handler.enableDefaultKeyListening();
      });
   }

   public void renderTabFront() {
      FontRenderer fr = this.mc.fontRenderer;
      int valueColor;
      if (this.selected) {
         Gui.drawRect((double)((float)this.getPosX() + 1.0F), (double)this.getPosY(), (double)((float)(this.getPosX() + this.getWidth()) - 1.0F), (double)(this.getPosY() + this.getHeight()), -16448251);
         valueColor = -1;
      } else {
         valueColor = -5723992;
      }

      fr.drawStringWithShadow(((EnumOption)this.stateObject).getLabel() + ": ", (float)this.getPosX() + 2.5F, (float)this.getPosY() + 2.5F, -3355444);
      String enumValue = ((Enum)((EnumOption)this.stateObject).getValue()).toString();
      fr.drawStringWithShadow(enumValue, (float)(this.getPosX() + this.getWidth() - fr.getStringWidth(enumValue) - 3), (float)this.getPosY() + 2.5F, valueColor);
   }

   public int getTabWidth() {
      FontRenderer fr = this.mc.fontRenderer;
      return fr.getStringWidth(((EnumOption)this.stateObject).getLabel() + ": ") + this.getWidestEnumName(fr, ((EnumOption)this.stateObject).getValues()) + 4;
   }

   private int getWidestEnumName(FontRenderer fr, Enum[] enums) {
      int widestName = 0;
      Enum[] var4 = enums;
      int var5 = enums.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Enum e = var4[var6];
         int width = fr.getStringWidth(e.name());
         if (width > widestName) {
            widestName = width;
         }
      }

      return widestName;
   }
}
