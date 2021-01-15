/*    */ package net.minecraft.world.gen.structure;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Random;
/*    */ 
/*    */ public class MapGenMineshaft extends MapGenStructure
/*    */ {
/*  9 */   private double field_82673_e = 0.004D;
/*    */   
/*    */ 
/*    */   public MapGenMineshaft() {}
/*    */   
/*    */ 
/*    */   public String getStructureName()
/*    */   {
/* 17 */     return "Mineshaft";
/*    */   }
/*    */   
/*    */   public MapGenMineshaft(Map<String, String> p_i2034_1_)
/*    */   {
/* 22 */     for (Map.Entry<String, String> entry : p_i2034_1_.entrySet())
/*    */     {
/* 24 */       if (((String)entry.getKey()).equals("chance"))
/*    */       {
/* 26 */         this.field_82673_e = net.minecraft.util.MathHelper.parseDoubleWithDefault((String)entry.getValue(), this.field_82673_e);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
/*    */   {
/* 33 */     return (this.rand.nextDouble() < this.field_82673_e) && (this.rand.nextInt(80) < Math.max(Math.abs(chunkX), Math.abs(chunkZ)));
/*    */   }
/*    */   
/*    */   protected StructureStart getStructureStart(int chunkX, int chunkZ)
/*    */   {
/* 38 */     return new StructureMineshaftStart(this.worldObj, this.rand, chunkX, chunkZ);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\structure\MapGenMineshaft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */