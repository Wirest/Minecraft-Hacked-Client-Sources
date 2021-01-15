package saint.tabgui.items;

import java.util.ArrayList;
import saint.modstuff.Module;
import saint.tabgui.TabGui;
import saint.utilities.NahrFont;
import saint.utilities.RenderHelper;

public class Tab {
   private TabGui gui;
   public ArrayList hacks;
   public String tabName;
   public int selectedItem = 0;
   public int menuHeight = 0;
   public int menuWidth = 0;
   private int colour;

   public Tab(TabGui GUI, String TabName) {
      this.tabName = TabName;
      this.gui = GUI;
      this.hacks = new ArrayList();
   }

   public void countMenuSize() {
      int maxWidth = 0;

      for(int i = 0; i < this.hacks.size(); ++i) {
         if (RenderHelper.getNahrFont().getStringWidth(((Module)this.hacks.get(i)).getName() + 4) > (float)maxWidth) {
            maxWidth = (int)(RenderHelper.getNahrFont().getStringWidth(((Module)this.hacks.get(i)).getName()) + 7.0F);
         }
      }

      this.menuWidth = maxWidth;
      this.menuHeight = this.hacks.size() * this.gui.tabHeight - 1;
   }

   public void drawTabMenu(int x, int y) {
      this.countMenuSize();
      x += 2;
      y += 2;
      RenderHelper.drawRect((float)(x - 1), (float)(y - 1), (float)(x + this.menuWidth - 2), (float)(y + this.menuHeight - 1), -1879048192);
      RenderHelper.drawRect((float)(x - 2), (float)(y - 2), (float)(x - 1), (float)(y + this.menuHeight), -16777216);
      RenderHelper.drawRect((float)(x - 2), (float)(y + this.menuHeight - 1), (float)(x + this.menuWidth - 1), (float)(y + this.menuHeight), -16777216);
      RenderHelper.drawRect((float)(x - 2), (float)(y - 2), (float)(x + this.menuWidth - 1), (float)(y - 1), -16777216);
      RenderHelper.drawRect((float)(x + this.menuWidth - 2), (float)(y - 2), (float)(x + this.menuWidth - 1), (float)(y + this.menuHeight), -16777216);

      for(int i = 0; i < this.hacks.size(); ++i) {
         this.colour = -1;
         RenderHelper.drawRect((float)(x - 1), (float)(y + this.gui.tabHeight * i - 1), (float)(x + this.menuWidth - 2), (float)(y + this.gui.tabHeight * i + 10), i == TabGui.selectedItem ? -2145320961 : 0);
         RenderHelper.getNahrFont().drawString((((Module)this.hacks.get(i)).isEnabled() ? "§6" : this.gui.colorNormal) + ((Module)this.hacks.get(i)).getName(), (float)(x + 2), (float)(y + this.gui.tabHeight * i - 4), NahrFont.FontType.SHADOW_THIN, -1, -16777216);
      }

   }
}
