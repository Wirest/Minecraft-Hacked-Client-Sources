package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public abstract class GuiListExtended extends GuiSlot {
   public GuiListExtended(Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
      super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
   }

   protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
   }

   protected boolean isSelected(int slotIndex) {
      return false;
   }

   protected void drawBackground() {
   }

   protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
      this.getListEntry(entryID).drawEntry(entryID, p_180791_2_, p_180791_3_, this.getListWidth(), p_180791_4_, mouseXIn, mouseYIn, this.getSlotIndexFromScreenCoords(mouseXIn, mouseYIn) == entryID);
   }

   protected void func_178040_a(int p_178040_1_, int p_178040_2_, int p_178040_3_) {
      this.getListEntry(p_178040_1_).setSelected(p_178040_1_, p_178040_2_, p_178040_3_);
   }

   public boolean mouseClicked(int mouseX, int mouseY, int mouseEvent) {
      if (this.isMouseYWithinSlotBounds(mouseY)) {
         int i = this.getSlotIndexFromScreenCoords(mouseX, mouseY);
         if (i >= 0) {
            int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
            int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
            int l = mouseX - j;
            int i1 = mouseY - k;
            if (this.getListEntry(i).mousePressed(i, mouseX, mouseY, mouseEvent, l, i1)) {
               this.setEnabled(false);
               return true;
            }
         }
      }

      return false;
   }

   public boolean mouseReleased(int p_148181_1_, int p_148181_2_, int p_148181_3_) {
      for(int i = 0; i < this.getSize(); ++i) {
         int j = this.left + this.width / 2 - this.getListWidth() / 2 + 2;
         int k = this.top + 4 - this.getAmountScrolled() + i * this.slotHeight + this.headerPadding;
         int l = p_148181_1_ - j;
         int i1 = p_148181_2_ - k;
         this.getListEntry(i).mouseReleased(i, p_148181_1_, p_148181_2_, p_148181_3_, l, i1);
      }

      this.setEnabled(true);
      return false;
   }

   public abstract GuiListExtended.IGuiListEntry getListEntry(int var1);

   public interface IGuiListEntry {
      void setSelected(int var1, int var2, int var3);

      void drawEntry(int var1, int var2, int var3, int var4, int var5, int var6, int var7, boolean var8);

      boolean mousePressed(int var1, int var2, int var3, int var4, int var5, int var6);

      void mouseReleased(int var1, int var2, int var3, int var4, int var5, int var6);
   }
}
