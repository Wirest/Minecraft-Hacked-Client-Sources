/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.Random;
/*    */ 
/*    */ public class EnchantmentNameParts
/*    */ {
/*  7 */   private static final EnchantmentNameParts instance = new EnchantmentNameParts();
/*  8 */   private Random rand = new Random();
/*  9 */   private String[] namePartsArray = "the elder scrolls klaatu berata niktu xyzzy bless curse light darkness fire air earth water hot dry cold wet ignite snuff embiggen twist shorten stretch fiddle destroy imbue galvanize enchant free limited range of towards inside sphere cube self other ball mental physical grow shrink demon elemental spirit animal creature beast humanoid undead fresh stale ".split(" ");
/*    */   
/*    */   public static EnchantmentNameParts getInstance()
/*    */   {
/* 13 */     return instance;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String generateNewRandomName()
/*    */   {
/* 21 */     int i = this.rand.nextInt(2) + 3;
/* 22 */     String s = "";
/*    */     
/* 24 */     for (int j = 0; j < i; j++)
/*    */     {
/* 26 */       if (j > 0)
/*    */       {
/* 28 */         s = s + " ";
/*    */       }
/*    */       
/* 31 */       s = s + this.namePartsArray[this.rand.nextInt(this.namePartsArray.length)];
/*    */     }
/*    */     
/* 34 */     return s;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void reseedRandomGenerator(long seed)
/*    */   {
/* 42 */     this.rand.setSeed(seed);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EnchantmentNameParts.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */