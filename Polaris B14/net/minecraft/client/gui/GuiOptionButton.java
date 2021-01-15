/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.settings.GameSettings.Options;
/*    */ 
/*    */ public class GuiOptionButton extends GuiButton
/*    */ {
/*    */   private final GameSettings.Options enumOptions;
/*    */   
/*    */   public GuiOptionButton(int p_i45011_1_, int p_i45011_2_, int p_i45011_3_, String p_i45011_4_)
/*    */   {
/* 11 */     this(p_i45011_1_, p_i45011_2_, p_i45011_3_, null, p_i45011_4_);
/*    */   }
/*    */   
/*    */   public GuiOptionButton(int p_i45012_1_, int p_i45012_2_, int p_i45012_3_, int p_i45012_4_, int p_i45012_5_, String p_i45012_6_)
/*    */   {
/* 16 */     super(p_i45012_1_, p_i45012_2_, p_i45012_3_, p_i45012_4_, p_i45012_5_, p_i45012_6_);
/* 17 */     this.enumOptions = null;
/*    */   }
/*    */   
/*    */   public GuiOptionButton(int p_i45013_1_, int p_i45013_2_, int p_i45013_3_, GameSettings.Options p_i45013_4_, String p_i45013_5_)
/*    */   {
/* 22 */     super(p_i45013_1_, p_i45013_2_, p_i45013_3_, 150, 20, p_i45013_5_);
/* 23 */     this.enumOptions = p_i45013_4_;
/*    */   }
/*    */   
/*    */   public GameSettings.Options returnEnumOptions()
/*    */   {
/* 28 */     return this.enumOptions;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiOptionButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */