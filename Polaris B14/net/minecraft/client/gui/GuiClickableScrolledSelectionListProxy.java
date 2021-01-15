/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.realms.RealmsClickableScrolledSelectionList;
/*     */ import net.minecraft.realms.Tezzelator;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class GuiClickableScrolledSelectionListProxy extends GuiSlot
/*     */ {
/*     */   private final RealmsClickableScrolledSelectionList field_178046_u;
/*     */   
/*     */   public GuiClickableScrolledSelectionListProxy(RealmsClickableScrolledSelectionList selectionList, int p_i45526_2_, int p_i45526_3_, int p_i45526_4_, int p_i45526_5_, int p_i45526_6_)
/*     */   {
/*  14 */     super(Minecraft.getMinecraft(), p_i45526_2_, p_i45526_3_, p_i45526_4_, p_i45526_5_, p_i45526_6_);
/*  15 */     this.field_178046_u = selectionList;
/*     */   }
/*     */   
/*     */   protected int getSize()
/*     */   {
/*  20 */     return this.field_178046_u.getItemCount();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY)
/*     */   {
/*  28 */     this.field_178046_u.selectItem(slotIndex, isDoubleClick, mouseX, mouseY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isSelected(int slotIndex)
/*     */   {
/*  36 */     return this.field_178046_u.isSelectedItem(slotIndex);
/*     */   }
/*     */   
/*     */   protected void drawBackground()
/*     */   {
/*  41 */     this.field_178046_u.renderBackground();
/*     */   }
/*     */   
/*     */   protected void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn)
/*     */   {
/*  46 */     this.field_178046_u.renderItem(entryID, p_180791_2_, p_180791_3_, p_180791_4_, mouseXIn, mouseYIn);
/*     */   }
/*     */   
/*     */   public int func_178044_e()
/*     */   {
/*  51 */     return this.width;
/*     */   }
/*     */   
/*     */   public int func_178042_f()
/*     */   {
/*  56 */     return this.mouseY;
/*     */   }
/*     */   
/*     */   public int func_178045_g()
/*     */   {
/*  61 */     return this.mouseX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getContentHeight()
/*     */   {
/*  69 */     return this.field_178046_u.getMaxPosition();
/*     */   }
/*     */   
/*     */   protected int getScrollBarX()
/*     */   {
/*  74 */     return this.field_178046_u.getScrollbarPosition();
/*     */   }
/*     */   
/*     */   public void handleMouseInput()
/*     */   {
/*  79 */     super.handleMouseInput();
/*     */     
/*  81 */     if ((this.scrollMultiplier > 0.0F) && (Mouse.getEventButtonState()))
/*     */     {
/*  83 */       this.field_178046_u.customMouseEvent(this.top, this.bottom, this.headerPadding, this.amountScrolled, this.slotHeight);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_178043_a(int p_178043_1_, int p_178043_2_, int p_178043_3_, Tezzelator p_178043_4_)
/*     */   {
/*  89 */     this.field_178046_u.renderSelected(p_178043_1_, p_178043_2_, p_178043_3_, p_178043_4_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void drawSelectionBox(int p_148120_1_, int p_148120_2_, int mouseXIn, int mouseYIn)
/*     */   {
/*  97 */     int i = getSize();
/*     */     
/*  99 */     for (int j = 0; j < i; j++)
/*     */     {
/* 101 */       int k = p_148120_2_ + j * this.slotHeight + this.headerPadding;
/* 102 */       int l = this.slotHeight - 4;
/*     */       
/* 104 */       if ((k > this.bottom) || (k + l < this.top))
/*     */       {
/* 106 */         func_178040_a(j, p_148120_1_, k);
/*     */       }
/*     */       
/* 109 */       if ((this.showSelectionBox) && (isSelected(j)))
/*     */       {
/* 111 */         func_178043_a(this.width, k, l, Tezzelator.instance);
/*     */       }
/*     */       
/* 114 */       drawSlot(j, p_148120_1_, k, l, mouseXIn, mouseYIn);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiClickableScrolledSelectionListProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */