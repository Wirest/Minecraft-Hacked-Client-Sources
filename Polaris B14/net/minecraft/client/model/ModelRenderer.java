/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GLAllocation;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import optfine.ModelSprite;
/*     */ import org.lwjgl.opengl.GL11;
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
/*     */ public class ModelRenderer
/*     */ {
/*     */   public float textureWidth;
/*     */   public float textureHeight;
/*     */   private int textureOffsetX;
/*     */   private int textureOffsetY;
/*     */   public float rotationPointX;
/*     */   public float rotationPointY;
/*     */   public float rotationPointZ;
/*     */   public float rotateAngleX;
/*     */   public float rotateAngleY;
/*     */   public float rotateAngleZ;
/*     */   private boolean compiled;
/*     */   private int displayList;
/*     */   public boolean mirror;
/*     */   public boolean showModel;
/*     */   public boolean isHidden;
/*     */   public List cubeList;
/*     */   public List childModels;
/*     */   public final String boxName;
/*     */   private ModelBase baseModel;
/*     */   public float offsetX;
/*     */   public float offsetY;
/*     */   public float offsetZ;
/*     */   private static final String __OBFID = "CL_00000874";
/*     */   public List spriteList;
/*     */   public boolean mirrorV;
/*     */   
/*     */   public ModelRenderer(ModelBase model, String boxNameIn)
/*     */   {
/*  55 */     this.spriteList = new ArrayList();
/*  56 */     this.mirrorV = false;
/*  57 */     this.textureWidth = 64.0F;
/*  58 */     this.textureHeight = 32.0F;
/*  59 */     this.showModel = true;
/*  60 */     this.cubeList = Lists.newArrayList();
/*  61 */     this.baseModel = model;
/*  62 */     model.boxList.add(this);
/*  63 */     this.boxName = boxNameIn;
/*  64 */     setTextureSize(model.textureWidth, model.textureHeight);
/*     */   }
/*     */   
/*     */   public ModelRenderer(ModelBase model)
/*     */   {
/*  69 */     this(model, null);
/*     */   }
/*     */   
/*     */   public ModelRenderer(ModelBase model, int texOffX, int texOffY)
/*     */   {
/*  74 */     this(model);
/*  75 */     setTextureOffset(texOffX, texOffY);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addChild(ModelRenderer renderer)
/*     */   {
/*  83 */     if (this.childModels == null)
/*     */     {
/*  85 */       this.childModels = Lists.newArrayList();
/*     */     }
/*     */     
/*  88 */     this.childModels.add(renderer);
/*     */   }
/*     */   
/*     */   public ModelRenderer setTextureOffset(int x, int y)
/*     */   {
/*  93 */     this.textureOffsetX = x;
/*  94 */     this.textureOffsetY = y;
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public ModelRenderer addBox(String partName, float offX, float offY, float offZ, int width, int height, int depth)
/*     */   {
/* 100 */     partName = this.boxName + "." + partName;
/* 101 */     TextureOffset textureoffset = this.baseModel.getTextureOffset(partName);
/* 102 */     setTextureOffset(textureoffset.textureOffsetX, textureoffset.textureOffsetY);
/* 103 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F).setBoxName(partName));
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public ModelRenderer addBox(float offX, float offY, float offZ, int width, int height, int depth)
/*     */   {
/* 109 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, offX, offY, offZ, width, height, depth, 0.0F));
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public ModelRenderer addBox(float p_178769_1_, float p_178769_2_, float p_178769_3_, int p_178769_4_, int p_178769_5_, int p_178769_6_, boolean p_178769_7_)
/*     */   {
/* 115 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_178769_1_, p_178769_2_, p_178769_3_, p_178769_4_, p_178769_5_, p_178769_6_, 0.0F, p_178769_7_));
/* 116 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBox(float p_78790_1_, float p_78790_2_, float p_78790_3_, int width, int height, int depth, float scaleFactor)
/*     */   {
/* 124 */     this.cubeList.add(new ModelBox(this, this.textureOffsetX, this.textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, width, height, depth, scaleFactor));
/*     */   }
/*     */   
/*     */   public void setRotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn)
/*     */   {
/* 129 */     this.rotationPointX = rotationPointXIn;
/* 130 */     this.rotationPointY = rotationPointYIn;
/* 131 */     this.rotationPointZ = rotationPointZIn;
/*     */   }
/*     */   
/*     */   public void render(float p_78785_1_)
/*     */   {
/* 136 */     if ((!this.isHidden) && (this.showModel))
/*     */     {
/* 138 */       if (!this.compiled)
/*     */       {
/* 140 */         compileDisplayList(p_78785_1_);
/*     */       }
/*     */       
/* 143 */       GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
/*     */       
/* 145 */       if ((this.rotateAngleX == 0.0F) && (this.rotateAngleY == 0.0F) && (this.rotateAngleZ == 0.0F))
/*     */       {
/* 147 */         if ((this.rotationPointX == 0.0F) && (this.rotationPointY == 0.0F) && (this.rotationPointZ == 0.0F))
/*     */         {
/* 149 */           GlStateManager.callList(this.displayList);
/*     */           
/* 151 */           if (this.childModels != null)
/*     */           {
/* 153 */             for (int k = 0; k < this.childModels.size(); k++)
/*     */             {
/* 155 */               ((ModelRenderer)this.childModels.get(k)).render(p_78785_1_);
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 161 */           GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
/* 162 */           GlStateManager.callList(this.displayList);
/*     */           
/* 164 */           if (this.childModels != null)
/*     */           {
/* 166 */             for (int j = 0; j < this.childModels.size(); j++)
/*     */             {
/* 168 */               ((ModelRenderer)this.childModels.get(j)).render(p_78785_1_);
/*     */             }
/*     */           }
/*     */           
/* 172 */           GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 177 */         GlStateManager.pushMatrix();
/* 178 */         GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
/*     */         
/* 180 */         if (this.rotateAngleZ != 0.0F)
/*     */         {
/* 182 */           GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */         }
/*     */         
/* 185 */         if (this.rotateAngleY != 0.0F)
/*     */         {
/* 187 */           GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 190 */         if (this.rotateAngleX != 0.0F)
/*     */         {
/* 192 */           GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */         }
/*     */         
/* 195 */         GlStateManager.callList(this.displayList);
/*     */         
/* 197 */         if (this.childModels != null)
/*     */         {
/* 199 */           for (int i = 0; i < this.childModels.size(); i++)
/*     */           {
/* 201 */             ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
/*     */           }
/*     */         }
/*     */         
/* 205 */         GlStateManager.popMatrix();
/*     */       }
/*     */       
/* 208 */       GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderWithRotation(float p_78791_1_)
/*     */   {
/* 214 */     if ((!this.isHidden) && (this.showModel))
/*     */     {
/* 216 */       if (!this.compiled)
/*     */       {
/* 218 */         compileDisplayList(p_78791_1_);
/*     */       }
/*     */       
/* 221 */       GlStateManager.pushMatrix();
/* 222 */       GlStateManager.translate(this.rotationPointX * p_78791_1_, this.rotationPointY * p_78791_1_, this.rotationPointZ * p_78791_1_);
/*     */       
/* 224 */       if (this.rotateAngleY != 0.0F)
/*     */       {
/* 226 */         GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */       }
/*     */       
/* 229 */       if (this.rotateAngleX != 0.0F)
/*     */       {
/* 231 */         GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */       }
/*     */       
/* 234 */       if (this.rotateAngleZ != 0.0F)
/*     */       {
/* 236 */         GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */       }
/*     */       
/* 239 */       GlStateManager.callList(this.displayList);
/* 240 */       GlStateManager.popMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void postRender(float scale)
/*     */   {
/* 249 */     if ((!this.isHidden) && (this.showModel))
/*     */     {
/* 251 */       if (!this.compiled)
/*     */       {
/* 253 */         compileDisplayList(scale);
/*     */       }
/*     */       
/* 256 */       if ((this.rotateAngleX == 0.0F) && (this.rotateAngleY == 0.0F) && (this.rotateAngleZ == 0.0F))
/*     */       {
/* 258 */         if ((this.rotationPointX != 0.0F) || (this.rotationPointY != 0.0F) || (this.rotationPointZ != 0.0F))
/*     */         {
/* 260 */           GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 265 */         GlStateManager.translate(this.rotationPointX * scale, this.rotationPointY * scale, this.rotationPointZ * scale);
/*     */         
/* 267 */         if (this.rotateAngleZ != 0.0F)
/*     */         {
/* 269 */           GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */         }
/*     */         
/* 272 */         if (this.rotateAngleY != 0.0F)
/*     */         {
/* 274 */           GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 277 */         if (this.rotateAngleX != 0.0F)
/*     */         {
/* 279 */           GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void compileDisplayList(float scale)
/*     */   {
/* 290 */     this.displayList = GLAllocation.generateDisplayLists(1);
/* 291 */     GL11.glNewList(this.displayList, 4864);
/* 292 */     WorldRenderer worldrenderer = Tessellator.getInstance().getWorldRenderer();
/*     */     
/* 294 */     for (int i = 0; i < this.cubeList.size(); i++)
/*     */     {
/* 296 */       ((ModelBox)this.cubeList.get(i)).render(worldrenderer, scale);
/*     */     }
/*     */     
/* 299 */     for (int j = 0; j < this.spriteList.size(); j++)
/*     */     {
/* 301 */       ModelSprite modelsprite = (ModelSprite)this.spriteList.get(j);
/* 302 */       modelsprite.render(Tessellator.getInstance(), scale);
/*     */     }
/*     */     
/* 305 */     GL11.glEndList();
/* 306 */     this.compiled = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ModelRenderer setTextureSize(int textureWidthIn, int textureHeightIn)
/*     */   {
/* 314 */     this.textureWidth = textureWidthIn;
/* 315 */     this.textureHeight = textureHeightIn;
/* 316 */     return this;
/*     */   }
/*     */   
/*     */   public void addSprite(float p_addSprite_1_, float p_addSprite_2_, float p_addSprite_3_, int p_addSprite_4_, int p_addSprite_5_, int p_addSprite_6_, float p_addSprite_7_)
/*     */   {
/* 321 */     this.spriteList.add(new ModelSprite(this, this.textureOffsetX, this.textureOffsetY, p_addSprite_1_, p_addSprite_2_, p_addSprite_3_, p_addSprite_4_, p_addSprite_5_, p_addSprite_6_, p_addSprite_7_));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */