/*     */ package optfine;
/*     */ 
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.block.model.BakedQuad;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ 
/*     */ public class NaturalProperties
/*     */ {
/*  11 */   public int rotation = 1;
/*  12 */   public boolean flip = false;
/*  13 */   private Map[] quadMaps = new Map[8];
/*     */   
/*     */   public NaturalProperties(String p_i44_1_)
/*     */   {
/*  17 */     if (p_i44_1_.equals("4"))
/*     */     {
/*  19 */       this.rotation = 4;
/*     */     }
/*  21 */     else if (p_i44_1_.equals("2"))
/*     */     {
/*  23 */       this.rotation = 2;
/*     */     }
/*  25 */     else if (p_i44_1_.equals("F"))
/*     */     {
/*  27 */       this.flip = true;
/*     */     }
/*  29 */     else if (p_i44_1_.equals("4F"))
/*     */     {
/*  31 */       this.rotation = 4;
/*  32 */       this.flip = true;
/*     */     }
/*  34 */     else if (p_i44_1_.equals("2F"))
/*     */     {
/*  36 */       this.rotation = 2;
/*  37 */       this.flip = true;
/*     */     }
/*     */     else
/*     */     {
/*  41 */       Config.warn("NaturalTextures: Unknown type: " + p_i44_1_);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isValid()
/*     */   {
/*  47 */     return (this.rotation != 2) && (this.rotation != 4) ? this.flip : true;
/*     */   }
/*     */   
/*     */   public synchronized BakedQuad getQuad(BakedQuad p_getQuad_1_, int p_getQuad_2_, boolean p_getQuad_3_)
/*     */   {
/*  52 */     int i = p_getQuad_2_;
/*     */     
/*  54 */     if (p_getQuad_3_)
/*     */     {
/*  56 */       i = p_getQuad_2_ | 0x4;
/*     */     }
/*     */     
/*  59 */     if ((i > 0) && (i < this.quadMaps.length))
/*     */     {
/*  61 */       Map map = this.quadMaps[i];
/*     */       
/*  63 */       if (map == null)
/*     */       {
/*  65 */         map = new IdentityHashMap(1);
/*  66 */         this.quadMaps[i] = map;
/*     */       }
/*     */       
/*  69 */       BakedQuad bakedquad = (BakedQuad)map.get(p_getQuad_1_);
/*     */       
/*  71 */       if (bakedquad == null)
/*     */       {
/*  73 */         bakedquad = makeQuad(p_getQuad_1_, p_getQuad_2_, p_getQuad_3_);
/*  74 */         map.put(p_getQuad_1_, bakedquad);
/*     */       }
/*     */       
/*  77 */       return bakedquad;
/*     */     }
/*     */     
/*     */ 
/*  81 */     return p_getQuad_1_;
/*     */   }
/*     */   
/*     */ 
/*     */   private BakedQuad makeQuad(BakedQuad p_makeQuad_1_, int p_makeQuad_2_, boolean p_makeQuad_3_)
/*     */   {
/*  87 */     int[] aint = p_makeQuad_1_.getVertexData();
/*  88 */     int i = p_makeQuad_1_.getTintIndex();
/*  89 */     EnumFacing enumfacing = p_makeQuad_1_.getFace();
/*  90 */     TextureAtlasSprite textureatlassprite = p_makeQuad_1_.getSprite();
/*  91 */     aint = fixVertexData(aint, p_makeQuad_2_, p_makeQuad_3_);
/*  92 */     BakedQuad bakedquad = new BakedQuad(aint, i, enumfacing, textureatlassprite);
/*  93 */     return bakedquad;
/*     */   }
/*     */   
/*     */   private int[] fixVertexData(int[] p_fixVertexData_1_, int p_fixVertexData_2_, boolean p_fixVertexData_3_)
/*     */   {
/*  98 */     int[] aint = new int[p_fixVertexData_1_.length];
/*     */     
/* 100 */     for (int i = 0; i < p_fixVertexData_1_.length; i++)
/*     */     {
/* 102 */       aint[i] = p_fixVertexData_1_[i];
/*     */     }
/*     */     
/* 105 */     int i1 = 4 - p_fixVertexData_2_;
/*     */     
/* 107 */     if (p_fixVertexData_3_)
/*     */     {
/* 109 */       i1 += 3;
/*     */     }
/*     */     
/* 112 */     i1 %= 4;
/*     */     
/* 114 */     for (int j = 0; j < 4; j++)
/*     */     {
/* 116 */       int k = j * 7;
/* 117 */       int l = i1 * 7;
/* 118 */       aint[(l + 4)] = p_fixVertexData_1_[(k + 4)];
/* 119 */       aint[(l + 4 + 1)] = p_fixVertexData_1_[(k + 4 + 1)];
/*     */       
/* 121 */       if (p_fixVertexData_3_)
/*     */       {
/* 123 */         i1--;
/*     */         
/* 125 */         if (i1 < 0)
/*     */         {
/* 127 */           i1 = 3;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 132 */         i1++;
/*     */         
/* 134 */         if (i1 > 3)
/*     */         {
/* 136 */           i1 = 0;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 141 */     return aint;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\NaturalProperties.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */