/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BakedQuad
/*    */ {
/*    */   protected final int[] vertexData;
/*    */   protected final int tintIndex;
/*    */   protected final EnumFacing face;
/*    */   private static final String __OBFID = "CL_00002512";
/* 16 */   private TextureAtlasSprite sprite = null;
/* 17 */   private int[] vertexDataSingle = null;
/*    */   
/*    */   public BakedQuad(int[] p_i8_1_, int p_i8_2_, EnumFacing p_i8_3_, TextureAtlasSprite p_i8_4_)
/*    */   {
/* 21 */     this.vertexData = p_i8_1_;
/* 22 */     this.tintIndex = p_i8_2_;
/* 23 */     this.face = p_i8_3_;
/* 24 */     this.sprite = p_i8_4_;
/*    */   }
/*    */   
/*    */   public TextureAtlasSprite getSprite()
/*    */   {
/* 29 */     return this.sprite;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 34 */     return "vertex: " + this.vertexData.length / 7 + ", tint: " + this.tintIndex + ", facing: " + this.face + ", sprite: " + this.sprite;
/*    */   }
/*    */   
/*    */   public BakedQuad(int[] vertexDataIn, int tintIndexIn, EnumFacing faceIn)
/*    */   {
/* 39 */     this.vertexData = vertexDataIn;
/* 40 */     this.tintIndex = tintIndexIn;
/* 41 */     this.face = faceIn;
/*    */   }
/*    */   
/*    */   public int[] getVertexData()
/*    */   {
/* 46 */     return this.vertexData;
/*    */   }
/*    */   
/*    */   public boolean hasTintIndex()
/*    */   {
/* 51 */     return this.tintIndex != -1;
/*    */   }
/*    */   
/*    */   public int getTintIndex()
/*    */   {
/* 56 */     return this.tintIndex;
/*    */   }
/*    */   
/*    */   public EnumFacing getFace()
/*    */   {
/* 61 */     return this.face;
/*    */   }
/*    */   
/*    */   public int[] getVertexDataSingle()
/*    */   {
/* 66 */     if (this.vertexDataSingle == null)
/*    */     {
/* 68 */       this.vertexDataSingle = makeVertexDataSingle(this.vertexData, this.sprite);
/*    */     }
/*    */     
/* 71 */     return this.vertexDataSingle;
/*    */   }
/*    */   
/*    */   private static int[] makeVertexDataSingle(int[] p_makeVertexDataSingle_0_, TextureAtlasSprite p_makeVertexDataSingle_1_)
/*    */   {
/* 76 */     int[] aint = new int[p_makeVertexDataSingle_0_.length];
/*    */     
/* 78 */     for (int i = 0; i < aint.length; i++)
/*    */     {
/* 80 */       aint[i] = p_makeVertexDataSingle_0_[i];
/*    */     }
/*    */     
/* 83 */     int i1 = p_makeVertexDataSingle_1_.sheetWidth / p_makeVertexDataSingle_1_.getIconWidth();
/* 84 */     int j = p_makeVertexDataSingle_1_.sheetHeight / p_makeVertexDataSingle_1_.getIconHeight();
/*    */     
/* 86 */     for (int k = 0; k < 4; k++)
/*    */     {
/* 88 */       int l = k * 7;
/* 89 */       float f = Float.intBitsToFloat(aint[(l + 4)]);
/* 90 */       float f1 = Float.intBitsToFloat(aint[(l + 4 + 1)]);
/* 91 */       float f2 = p_makeVertexDataSingle_1_.toSingleU(f);
/* 92 */       float f3 = p_makeVertexDataSingle_1_.toSingleV(f1);
/* 93 */       aint[(l + 4)] = Float.floatToRawIntBits(f2);
/* 94 */       aint[(l + 4 + 1)] = Float.floatToRawIntBits(f3);
/*    */     }
/*    */     
/* 97 */     return aint;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\block\model\BakedQuad.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */