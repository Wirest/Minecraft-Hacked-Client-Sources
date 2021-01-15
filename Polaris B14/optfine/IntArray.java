/*    */ package optfine;
/*    */ 
/*    */ public class IntArray
/*    */ {
/*  5 */   private int[] array = null;
/*  6 */   private int position = 0;
/*  7 */   private int limit = 0;
/*    */   
/*    */   public IntArray(int p_i39_1_)
/*    */   {
/* 11 */     this.array = new int[p_i39_1_];
/*    */   }
/*    */   
/*    */   public void put(int p_put_1_)
/*    */   {
/* 16 */     this.array[this.position] = p_put_1_;
/* 17 */     this.position += 1;
/*    */     
/* 19 */     if (this.limit < this.position)
/*    */     {
/* 21 */       this.limit = this.position;
/*    */     }
/*    */   }
/*    */   
/*    */   public void put(int p_put_1_, int p_put_2_)
/*    */   {
/* 27 */     this.array[p_put_1_] = p_put_2_;
/*    */     
/* 29 */     if (this.limit < p_put_1_)
/*    */     {
/* 31 */       this.limit = p_put_1_;
/*    */     }
/*    */   }
/*    */   
/*    */   public void position(int p_position_1_)
/*    */   {
/* 37 */     this.position = p_position_1_;
/*    */   }
/*    */   
/*    */   public void put(int[] p_put_1_)
/*    */   {
/* 42 */     int i = p_put_1_.length;
/*    */     
/* 44 */     for (int j = 0; j < i; j++)
/*    */     {
/* 46 */       this.array[this.position] = p_put_1_[j];
/* 47 */       this.position += 1;
/*    */     }
/*    */     
/* 50 */     if (this.limit < this.position)
/*    */     {
/* 52 */       this.limit = this.position;
/*    */     }
/*    */   }
/*    */   
/*    */   public int get(int p_get_1_)
/*    */   {
/* 58 */     return this.array[p_get_1_];
/*    */   }
/*    */   
/*    */   public int[] getArray()
/*    */   {
/* 63 */     return this.array;
/*    */   }
/*    */   
/*    */   public void clear()
/*    */   {
/* 68 */     this.position = 0;
/* 69 */     this.limit = 0;
/*    */   }
/*    */   
/*    */   public int getLimit()
/*    */   {
/* 74 */     return this.limit;
/*    */   }
/*    */   
/*    */   public int getPosition()
/*    */   {
/* 79 */     return this.position;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\IntArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */