/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelBox
/*     */ {
/*     */   private PositionTextureVertex[] vertexPositions;
/*     */   private TexturedQuad[] quadList;
/*     */   public final float posX1;
/*     */   public final float posY1;
/*     */   public final float posZ1;
/*     */   public final float posX2;
/*     */   public final float posY2;
/*     */   public final float posZ2;
/*     */   public String boxName;
/*     */   
/*     */   public ModelBox(ModelRenderer renderer, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, int p_i46359_7_, int p_i46359_8_, int p_i46359_9_, float p_i46359_10_)
/*     */   {
/*  36 */     this(renderer, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, renderer.mirror);
/*     */   }
/*     */   
/*     */   public ModelBox(ModelRenderer renderer, int textureX, int textureY, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, int p_i46301_7_, int p_i46301_8_, int p_i46301_9_, float p_i46301_10_, boolean p_i46301_11_)
/*     */   {
/*  41 */     this.posX1 = p_i46301_4_;
/*  42 */     this.posY1 = p_i46301_5_;
/*  43 */     this.posZ1 = p_i46301_6_;
/*  44 */     this.posX2 = (p_i46301_4_ + p_i46301_7_);
/*  45 */     this.posY2 = (p_i46301_5_ + p_i46301_8_);
/*  46 */     this.posZ2 = (p_i46301_6_ + p_i46301_9_);
/*  47 */     this.vertexPositions = new PositionTextureVertex[8];
/*  48 */     this.quadList = new TexturedQuad[6];
/*  49 */     float f = p_i46301_4_ + p_i46301_7_;
/*  50 */     float f1 = p_i46301_5_ + p_i46301_8_;
/*  51 */     float f2 = p_i46301_6_ + p_i46301_9_;
/*  52 */     p_i46301_4_ -= p_i46301_10_;
/*  53 */     p_i46301_5_ -= p_i46301_10_;
/*  54 */     p_i46301_6_ -= p_i46301_10_;
/*  55 */     f += p_i46301_10_;
/*  56 */     f1 += p_i46301_10_;
/*  57 */     f2 += p_i46301_10_;
/*     */     
/*  59 */     if (p_i46301_11_)
/*     */     {
/*  61 */       float f3 = f;
/*  62 */       f = p_i46301_4_;
/*  63 */       p_i46301_4_ = f3;
/*     */     }
/*     */     
/*  66 */     PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0F, 0.0F);
/*  67 */     PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, p_i46301_5_, p_i46301_6_, 0.0F, 8.0F);
/*  68 */     PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, p_i46301_6_, 8.0F, 8.0F);
/*  69 */     PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i46301_4_, f1, p_i46301_6_, 8.0F, 0.0F);
/*  70 */     PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, f2, 0.0F, 0.0F);
/*  71 */     PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, p_i46301_5_, f2, 0.0F, 8.0F);
/*  72 */     PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
/*  73 */     PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i46301_4_, f1, f2, 8.0F, 0.0F);
/*  74 */     this.vertexPositions[0] = positiontexturevertex7;
/*  75 */     this.vertexPositions[1] = positiontexturevertex;
/*  76 */     this.vertexPositions[2] = positiontexturevertex1;
/*  77 */     this.vertexPositions[3] = positiontexturevertex2;
/*  78 */     this.vertexPositions[4] = positiontexturevertex3;
/*  79 */     this.vertexPositions[5] = positiontexturevertex4;
/*  80 */     this.vertexPositions[6] = positiontexturevertex5;
/*  81 */     this.vertexPositions[7] = positiontexturevertex6;
/*  82 */     this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5 }, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/*  83 */     this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2 }, textureX, textureY + p_i46301_9_, textureX + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/*  84 */     this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex }, textureX + p_i46301_9_, textureY, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, renderer.textureWidth, renderer.textureHeight);
/*  85 */     this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5 }, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, textureY, renderer.textureWidth, renderer.textureHeight);
/*  86 */     this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1 }, textureX + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/*  87 */     this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] { positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6 }, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
/*     */     
/*  89 */     if (p_i46301_11_)
/*     */     {
/*  91 */       for (int i = 0; i < this.quadList.length; i++)
/*     */       {
/*  93 */         this.quadList[i].flipFace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void render(WorldRenderer renderer, float scale)
/*     */   {
/* 100 */     for (int i = 0; i < this.quadList.length; i++)
/*     */     {
/* 102 */       this.quadList[i].draw(renderer, scale);
/*     */     }
/*     */   }
/*     */   
/*     */   public ModelBox setBoxName(String name)
/*     */   {
/* 108 */     this.boxName = name;
/* 109 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */