/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.client.settings.GameSettings.Options;
/*     */ 
/*     */ public class GuiOptionsRowList extends GuiListExtended
/*     */ {
/*  10 */   private final List<Row> field_148184_k = com.google.common.collect.Lists.newArrayList();
/*     */   
/*     */   public GuiOptionsRowList(Minecraft mcIn, int p_i45015_2_, int p_i45015_3_, int p_i45015_4_, int p_i45015_5_, int p_i45015_6_, GameSettings.Options... p_i45015_7_)
/*     */   {
/*  14 */     super(mcIn, p_i45015_2_, p_i45015_3_, p_i45015_4_, p_i45015_5_, p_i45015_6_);
/*  15 */     this.field_148163_i = false;
/*     */     
/*  17 */     for (int i = 0; i < p_i45015_7_.length; i += 2)
/*     */     {
/*  19 */       GameSettings.Options gamesettings$options = p_i45015_7_[i];
/*  20 */       GameSettings.Options gamesettings$options1 = i < p_i45015_7_.length - 1 ? p_i45015_7_[(i + 1)] : null;
/*  21 */       GuiButton guibutton = func_148182_a(mcIn, p_i45015_2_ / 2 - 155, 0, gamesettings$options);
/*  22 */       GuiButton guibutton1 = func_148182_a(mcIn, p_i45015_2_ / 2 - 155 + 160, 0, gamesettings$options1);
/*  23 */       this.field_148184_k.add(new Row(guibutton, guibutton1));
/*     */     }
/*     */   }
/*     */   
/*     */   private GuiButton func_148182_a(Minecraft mcIn, int p_148182_2_, int p_148182_3_, GameSettings.Options p_148182_4_)
/*     */   {
/*  29 */     if (p_148182_4_ == null)
/*     */     {
/*  31 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  35 */     int i = p_148182_4_.returnEnumOrdinal();
/*  36 */     return p_148182_4_.getEnumFloat() ? new GuiOptionSlider(i, p_148182_2_, p_148182_3_, p_148182_4_) : new GuiOptionButton(i, p_148182_2_, p_148182_3_, p_148182_4_, mcIn.gameSettings.getKeyBinding(p_148182_4_));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Row getListEntry(int index)
/*     */   {
/*  45 */     return (Row)this.field_148184_k.get(index);
/*     */   }
/*     */   
/*     */   protected int getSize()
/*     */   {
/*  50 */     return this.field_148184_k.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getListWidth()
/*     */   {
/*  58 */     return 400;
/*     */   }
/*     */   
/*     */   protected int getScrollBarX()
/*     */   {
/*  63 */     return super.getScrollBarX() + 32;
/*     */   }
/*     */   
/*     */   public static class Row implements GuiListExtended.IGuiListEntry
/*     */   {
/*  68 */     private final Minecraft field_148325_a = Minecraft.getMinecraft();
/*     */     private final GuiButton field_148323_b;
/*     */     private final GuiButton field_148324_c;
/*     */     
/*     */     public Row(GuiButton p_i45014_1_, GuiButton p_i45014_2_)
/*     */     {
/*  74 */       this.field_148323_b = p_i45014_1_;
/*  75 */       this.field_148324_c = p_i45014_2_;
/*     */     }
/*     */     
/*     */     public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
/*     */     {
/*  80 */       if (this.field_148323_b != null)
/*     */       {
/*  82 */         this.field_148323_b.yPosition = y;
/*  83 */         this.field_148323_b.drawButton(this.field_148325_a, mouseX, mouseY);
/*     */       }
/*     */       
/*  86 */       if (this.field_148324_c != null)
/*     */       {
/*  88 */         this.field_148324_c.yPosition = y;
/*  89 */         this.field_148324_c.drawButton(this.field_148325_a, mouseX, mouseY);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean mousePressed(int slotIndex, int p_148278_2_, int p_148278_3_, int p_148278_4_, int p_148278_5_, int p_148278_6_)
/*     */     {
/*  95 */       if (this.field_148323_b.mousePressed(this.field_148325_a, p_148278_2_, p_148278_3_))
/*     */       {
/*  97 */         if ((this.field_148323_b instanceof GuiOptionButton))
/*     */         {
/*  99 */           this.field_148325_a.gameSettings.setOptionValue(((GuiOptionButton)this.field_148323_b).returnEnumOptions(), 1);
/* 100 */           this.field_148323_b.displayString = this.field_148325_a.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148323_b.id));
/*     */         }
/*     */         
/* 103 */         return true;
/*     */       }
/* 105 */       if ((this.field_148324_c != null) && (this.field_148324_c.mousePressed(this.field_148325_a, p_148278_2_, p_148278_3_)))
/*     */       {
/* 107 */         if ((this.field_148324_c instanceof GuiOptionButton))
/*     */         {
/* 109 */           this.field_148325_a.gameSettings.setOptionValue(((GuiOptionButton)this.field_148324_c).returnEnumOptions(), 1);
/* 110 */           this.field_148324_c.displayString = this.field_148325_a.gameSettings.getKeyBinding(GameSettings.Options.getEnumOptions(this.field_148324_c.id));
/*     */         }
/*     */         
/* 113 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 117 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY)
/*     */     {
/* 123 */       if (this.field_148323_b != null)
/*     */       {
/* 125 */         this.field_148323_b.mouseReleased(x, y);
/*     */       }
/*     */       
/* 128 */       if (this.field_148324_c != null)
/*     */       {
/* 130 */         this.field_148324_c.mouseReleased(x, y);
/*     */       }
/*     */     }
/*     */     
/*     */     public void setSelected(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiOptionsRowList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */