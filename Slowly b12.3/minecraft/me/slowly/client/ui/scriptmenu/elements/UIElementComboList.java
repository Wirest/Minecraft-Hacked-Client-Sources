package me.slowly.client.ui.scriptmenu.elements;

import java.util.ArrayList;
import me.slowly.client.Client;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.fontmanager.UnicodeFontRenderer;
import me.slowly.client.util.handler.MouseInputHandler;
import net.minecraft.client.gui.Gui;

public class UIElementComboList {
   private ArrayList options = new ArrayList();
   private int selected;
   private int hoverOption;
   private int x;
   private int y;
   private int width = 100;
   private int height = 11;
   private UnicodeFontRenderer font;
   private MouseInputHandler mouseHandler = new MouseInputHandler(0);

   public UIElementComboList() {
      this.font = Client.getInstance().getFontManager().consolas14;
   }

   public void draw(float xPosition, float yPosition, int mouseX, int mouseY) {
      this.x = (int)xPosition;
      this.y = (int)yPosition;
      this.hoverOption = -1;
      if (this.selected > this.options.size() - 1) {
         this.selected = this.options.size() - 1;
      }

      int yAxis = this.y;

      for(int i = 0; i < this.options.size(); ++i) {
         boolean hover = mouseX >= this.x && mouseX <= this.x + this.width && mouseY > yAxis && mouseY <= yAxis + this.height;
         if (i == this.selected) {
            Gui.drawRect(this.x, yAxis, this.x + this.width, yAxis + this.height, FlatColors.BLUE.c);
            this.font = Client.getInstance().getFontManager().consolasbold14;
         }

         if (hover) {
            this.hoverOption = i;
         }

         this.font.drawString((String)this.options.get(i), (float)(this.x + 1), (float)yAxis + (float)(this.height - this.font.FONT_HEIGHT) / 2.0F, -2894893);
         yAxis += this.height;
         this.font = Client.getInstance().getFontManager().consolas14;
      }

      if (this.mouseHandler.canExcecute() && this.hoverOption != -1) {
         this.selected = this.hoverOption;
      }

   }

   public void deleteSelected() {
      if (this.canDelete()) {
         this.options.remove(this.selected);
      }

   }

   public void add(String s) {
      this.options.add(s);
   }

   public void moveSelectedUp() {
      if (this.canMoveUp()) {
         String swap = (String)this.options.get(this.selected);
         this.options.set(this.selected, (String)this.options.get(this.selected - 1));
         this.options.set(this.selected - 1, swap);
         --this.selected;
      }

   }

   public void moveSelectedDown() {
      if (this.canMoveDown()) {
         String swap = (String)this.options.get(this.selected);
         this.options.set(this.selected, (String)this.options.get(this.selected + 1));
         this.options.set(this.selected + 1, swap);
         ++this.selected;
      }

   }

   public boolean canDelete() {
      return this.options.size() > 0 && this.selected >= 0 && this.selected <= this.options.size() - 1;
   }

   public boolean canMoveUp() {
      return this.options.size() > 1 && this.selected != 0 && this.selected >= 0 && this.selected <= this.options.size() - 1;
   }

   public boolean canMoveDown() {
      return this.options.size() > 1 && this.selected != this.options.size() - 1 && this.selected >= 0 && this.selected <= this.options.size() - 1;
   }

   public String getSelected() {
      return (String)this.getOptions().get(this.selected);
   }

   public ArrayList getOptions() {
      return this.options;
   }

   public void setWidth(int width) {
      this.width = width;
   }

   public void setHeight(int height) {
      this.height = height;
   }
}
