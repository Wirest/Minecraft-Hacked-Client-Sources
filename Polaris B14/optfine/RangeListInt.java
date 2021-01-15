/*    */ package optfine;
/*    */ 
/*    */ public class RangeListInt
/*    */ {
/*  5 */   private RangeInt[] ranges = new RangeInt[0];
/*    */   
/*    */   public void addRange(RangeInt p_addRange_1_)
/*    */   {
/*  9 */     this.ranges = ((RangeInt[])Config.addObjectToArray(this.ranges, p_addRange_1_));
/*    */   }
/*    */   
/*    */   public boolean isInRange(int p_isInRange_1_)
/*    */   {
/* 14 */     for (int i = 0; i < this.ranges.length; i++)
/*    */     {
/* 16 */       RangeInt rangeint = this.ranges[i];
/*    */       
/* 18 */       if (rangeint.isInRange(p_isInRange_1_))
/*    */       {
/* 20 */         return true;
/*    */       }
/*    */     }
/*    */     
/* 24 */     return false;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 29 */     StringBuffer stringbuffer = new StringBuffer();
/* 30 */     stringbuffer.append("[");
/*    */     
/* 32 */     for (int i = 0; i < this.ranges.length; i++)
/*    */     {
/* 34 */       RangeInt rangeint = this.ranges[i];
/*    */       
/* 36 */       if (i > 0)
/*    */       {
/* 38 */         stringbuffer.append(", ");
/*    */       }
/*    */       
/* 41 */       stringbuffer.append(rangeint.toString());
/*    */     }
/*    */     
/* 44 */     stringbuffer.append("]");
/* 45 */     return stringbuffer.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\RangeListInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */