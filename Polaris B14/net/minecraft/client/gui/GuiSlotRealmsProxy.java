/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.realms.RealmsScrolledSelectionList;
/*    */ 
/*    */ public class GuiSlotRealmsProxy extends GuiSlot
/*    */ {
/*    */   private final RealmsScrolledSelectionList selectionList;
/*    */   
/*    */   public GuiSlotRealmsProxy(RealmsScrolledSelectionList selectionListIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
/* 10 */     super(net.minecraft.client.Minecraft.getMinecraft(), widthIn, heightIn, topIn, bottomIn, slotHeightIn);
/* 11 */     this.selectionList = selectionListIn;
/*    */   }
/*    */   
/*    */   protected int getSize() {
/* 15 */     return this.selectionList.getItemCount();
/*    */   }
/*    */   
/*    */   protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
/* 19 */     this.selectionList.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
/*    */   }
/*    */   
/*    */   protected boolean isSelected(int slotIndex) {
/* 23 */     return this.selectionList.isSelectedItem(slotIndex);
/*    */   }
/*    */   
/*    */   protected void drawBackground() {
/* 27 */     this.selectionList.renderBackground();
/*    */   }
/*    */   
/*    */   protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
/* 31 */     this.selectionList.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
/*    */   }
/*    */   
/*    */   public int func_154338_k() {
/* 35 */     return this.width;
/*    */   }
/*    */   
/*    */   public int func_154339_l() {
/* 39 */     return this.mouseY;
/*    */   }
/*    */   
/*    */   public int func_154337_m() {
/* 43 */     return this.mouseX;
/*    */   }
/*    */   
/*    */   protected int getContentHeight() {
/* 47 */     return this.selectionList.getMaxPosition();
/*    */   }
/*    */   
/*    */   protected int getScrollBarX() {
/* 51 */     return this.selectionList.getScrollbarPosition();
/*    */   }
/*    */   
/*    */   public void handleMouseInput() {
/* 55 */     super.handleMouseInput();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSlotRealmsProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */