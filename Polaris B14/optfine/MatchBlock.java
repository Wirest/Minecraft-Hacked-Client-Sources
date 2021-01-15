/*    */ package optfine;
/*    */ 
/*    */ import net.minecraft.block.state.BlockStateBase;
/*    */ 
/*    */ public class MatchBlock
/*    */ {
/*  7 */   private int blockId = -1;
/*  8 */   private int[] metadatas = null;
/*    */   
/*    */   public MatchBlock(int p_i40_1_)
/*    */   {
/* 12 */     this.blockId = p_i40_1_;
/*    */   }
/*    */   
/*    */   public MatchBlock(int p_i41_1_, int[] p_i41_2_)
/*    */   {
/* 17 */     this.blockId = p_i41_1_;
/* 18 */     this.metadatas = p_i41_2_;
/*    */   }
/*    */   
/*    */   public int getBlockId()
/*    */   {
/* 23 */     return this.blockId;
/*    */   }
/*    */   
/*    */   public int[] getMetadatas()
/*    */   {
/* 28 */     return this.metadatas;
/*    */   }
/*    */   
/*    */   public boolean matches(BlockStateBase p_matches_1_)
/*    */   {
/* 33 */     if (p_matches_1_.getBlockId() != this.blockId)
/*    */     {
/* 35 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 39 */     if (this.metadatas != null)
/*    */     {
/* 41 */       boolean flag = false;
/* 42 */       int i = p_matches_1_.getMetadata();
/*    */       
/* 44 */       for (int j = 0; j < this.metadatas.length; j++)
/*    */       {
/* 46 */         int k = this.metadatas[j];
/*    */         
/* 48 */         if (k == i)
/*    */         {
/* 50 */           flag = true;
/* 51 */           break;
/*    */         }
/*    */       }
/*    */       
/* 55 */       if (!flag)
/*    */       {
/* 57 */         return false;
/*    */       }
/*    */     }
/*    */     
/* 61 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\MatchBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */