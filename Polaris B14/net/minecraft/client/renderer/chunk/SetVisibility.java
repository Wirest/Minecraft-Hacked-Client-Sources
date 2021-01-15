/*    */ package net.minecraft.client.renderer.chunk;
/*    */ 
/*    */ import java.util.BitSet;
/*    */ import java.util.Iterator;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ public class SetVisibility
/*    */ {
/*  9 */   private static final int COUNT_FACES = EnumFacing.values().length;
/*    */   private final BitSet bitSet;
/*    */   
/*    */   public SetVisibility()
/*    */   {
/* 14 */     this.bitSet = new BitSet(COUNT_FACES * COUNT_FACES);
/*    */   }
/*    */   
/*    */   public void setManyVisible(java.util.Set<EnumFacing> p_178620_1_) {
/*    */     Iterator localIterator2;
/* 19 */     for (Iterator localIterator1 = p_178620_1_.iterator(); localIterator1.hasNext(); 
/*    */         
/* 21 */         localIterator2.hasNext())
/*    */     {
/* 19 */       EnumFacing enumfacing = (EnumFacing)localIterator1.next();
/*    */       
/* 21 */       localIterator2 = p_178620_1_.iterator(); continue;EnumFacing enumfacing1 = (EnumFacing)localIterator2.next();
/*    */       
/* 23 */       setVisible(enumfacing, enumfacing1, true);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void setVisible(EnumFacing facing, EnumFacing facing2, boolean p_178619_3_)
/*    */   {
/* 30 */     this.bitSet.set(facing.ordinal() + facing2.ordinal() * COUNT_FACES, p_178619_3_);
/* 31 */     this.bitSet.set(facing2.ordinal() + facing.ordinal() * COUNT_FACES, p_178619_3_);
/*    */   }
/*    */   
/*    */   public void setAllVisible(boolean visible)
/*    */   {
/* 36 */     this.bitSet.set(0, this.bitSet.size(), visible);
/*    */   }
/*    */   
/*    */   public boolean isVisible(EnumFacing facing, EnumFacing facing2)
/*    */   {
/* 41 */     return this.bitSet.get(facing.ordinal() + facing2.ordinal() * COUNT_FACES);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 46 */     StringBuilder stringbuilder = new StringBuilder();
/* 47 */     stringbuilder.append(' ');
/*    */     EnumFacing[] arrayOfEnumFacing1;
/* 49 */     int j = (arrayOfEnumFacing1 = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing1[i];
/*    */       
/* 51 */       stringbuilder.append(' ').append(enumfacing.toString().toUpperCase().charAt(0));
/*    */     }
/*    */     
/* 54 */     stringbuilder.append('\n');
/*    */     
/* 56 */     j = (arrayOfEnumFacing1 = EnumFacing.values()).length; for (i = 0; i < j; i++) { EnumFacing enumfacing2 = arrayOfEnumFacing1[i];
/*    */       
/* 58 */       stringbuilder.append(enumfacing2.toString().toUpperCase().charAt(0));
/*    */       EnumFacing[] arrayOfEnumFacing2;
/* 60 */       int m = (arrayOfEnumFacing2 = EnumFacing.values()).length; for (int k = 0; k < m; k++) { EnumFacing enumfacing1 = arrayOfEnumFacing2[k];
/*    */         
/* 62 */         if (enumfacing2 == enumfacing1)
/*    */         {
/* 64 */           stringbuilder.append("  ");
/*    */         }
/*    */         else
/*    */         {
/* 68 */           boolean flag = isVisible(enumfacing2, enumfacing1);
/* 69 */           stringbuilder.append(' ').append(flag ? 'Y' : 'n');
/*    */         }
/*    */       }
/*    */       
/* 73 */       stringbuilder.append('\n');
/*    */     }
/*    */     
/* 76 */     return stringbuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\chunk\SetVisibility.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */