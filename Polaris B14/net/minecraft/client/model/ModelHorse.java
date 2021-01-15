/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.util.MathHelper;
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
/*     */ public class ModelHorse
/*     */   extends ModelBase
/*     */ {
/*     */   private ModelRenderer head;
/*     */   private ModelRenderer field_178711_b;
/*     */   private ModelRenderer field_178712_c;
/*     */   private ModelRenderer horseLeftEar;
/*     */   private ModelRenderer horseRightEar;
/*     */   private ModelRenderer muleLeftEar;
/*     */   private ModelRenderer muleRightEar;
/*     */   private ModelRenderer neck;
/*     */   private ModelRenderer horseFaceRopes;
/*     */   private ModelRenderer mane;
/*     */   private ModelRenderer body;
/*     */   private ModelRenderer tailBase;
/*     */   private ModelRenderer tailMiddle;
/*     */   private ModelRenderer tailTip;
/*     */   private ModelRenderer backLeftLeg;
/*     */   private ModelRenderer backLeftShin;
/*     */   private ModelRenderer backLeftHoof;
/*     */   private ModelRenderer backRightLeg;
/*     */   private ModelRenderer backRightShin;
/*     */   private ModelRenderer backRightHoof;
/*     */   private ModelRenderer frontLeftLeg;
/*     */   private ModelRenderer frontLeftShin;
/*     */   private ModelRenderer frontLeftHoof;
/*     */   private ModelRenderer frontRightLeg;
/*     */   private ModelRenderer frontRightShin;
/*     */   private ModelRenderer frontRightHoof;
/*     */   private ModelRenderer muleLeftChest;
/*     */   private ModelRenderer muleRightChest;
/*     */   private ModelRenderer horseSaddleBottom;
/*     */   private ModelRenderer horseSaddleFront;
/*     */   private ModelRenderer horseSaddleBack;
/*     */   private ModelRenderer horseLeftSaddleRope;
/*     */   private ModelRenderer horseLeftSaddleMetal;
/*     */   private ModelRenderer horseRightSaddleRope;
/*     */   private ModelRenderer horseRightSaddleMetal;
/*     */   private ModelRenderer horseLeftFaceMetal;
/*     */   private ModelRenderer horseRightFaceMetal;
/*     */   private ModelRenderer horseLeftRein;
/*     */   private ModelRenderer horseRightRein;
/*     */   
/*     */   public ModelHorse()
/*     */   {
/*  67 */     this.textureWidth = 128;
/*  68 */     this.textureHeight = 128;
/*  69 */     this.body = new ModelRenderer(this, 0, 34);
/*  70 */     this.body.addBox(-5.0F, -8.0F, -19.0F, 10, 10, 24);
/*  71 */     this.body.setRotationPoint(0.0F, 11.0F, 9.0F);
/*  72 */     this.tailBase = new ModelRenderer(this, 44, 0);
/*  73 */     this.tailBase.addBox(-1.0F, -1.0F, 0.0F, 2, 2, 3);
/*  74 */     this.tailBase.setRotationPoint(0.0F, 3.0F, 14.0F);
/*  75 */     setBoxRotation(this.tailBase, -1.134464F, 0.0F, 0.0F);
/*  76 */     this.tailMiddle = new ModelRenderer(this, 38, 7);
/*  77 */     this.tailMiddle.addBox(-1.5F, -2.0F, 3.0F, 3, 4, 7);
/*  78 */     this.tailMiddle.setRotationPoint(0.0F, 3.0F, 14.0F);
/*  79 */     setBoxRotation(this.tailMiddle, -1.134464F, 0.0F, 0.0F);
/*  80 */     this.tailTip = new ModelRenderer(this, 24, 3);
/*  81 */     this.tailTip.addBox(-1.5F, -4.5F, 9.0F, 3, 4, 7);
/*  82 */     this.tailTip.setRotationPoint(0.0F, 3.0F, 14.0F);
/*  83 */     setBoxRotation(this.tailTip, -1.40215F, 0.0F, 0.0F);
/*  84 */     this.backLeftLeg = new ModelRenderer(this, 78, 29);
/*  85 */     this.backLeftLeg.addBox(-2.5F, -2.0F, -2.5F, 4, 9, 5);
/*  86 */     this.backLeftLeg.setRotationPoint(4.0F, 9.0F, 11.0F);
/*  87 */     this.backLeftShin = new ModelRenderer(this, 78, 43);
/*  88 */     this.backLeftShin.addBox(-2.0F, 0.0F, -1.5F, 3, 5, 3);
/*  89 */     this.backLeftShin.setRotationPoint(4.0F, 16.0F, 11.0F);
/*  90 */     this.backLeftHoof = new ModelRenderer(this, 78, 51);
/*  91 */     this.backLeftHoof.addBox(-2.5F, 5.1F, -2.0F, 4, 3, 4);
/*  92 */     this.backLeftHoof.setRotationPoint(4.0F, 16.0F, 11.0F);
/*  93 */     this.backRightLeg = new ModelRenderer(this, 96, 29);
/*  94 */     this.backRightLeg.addBox(-1.5F, -2.0F, -2.5F, 4, 9, 5);
/*  95 */     this.backRightLeg.setRotationPoint(-4.0F, 9.0F, 11.0F);
/*  96 */     this.backRightShin = new ModelRenderer(this, 96, 43);
/*  97 */     this.backRightShin.addBox(-1.0F, 0.0F, -1.5F, 3, 5, 3);
/*  98 */     this.backRightShin.setRotationPoint(-4.0F, 16.0F, 11.0F);
/*  99 */     this.backRightHoof = new ModelRenderer(this, 96, 51);
/* 100 */     this.backRightHoof.addBox(-1.5F, 5.1F, -2.0F, 4, 3, 4);
/* 101 */     this.backRightHoof.setRotationPoint(-4.0F, 16.0F, 11.0F);
/* 102 */     this.frontLeftLeg = new ModelRenderer(this, 44, 29);
/* 103 */     this.frontLeftLeg.addBox(-1.9F, -1.0F, -2.1F, 3, 8, 4);
/* 104 */     this.frontLeftLeg.setRotationPoint(4.0F, 9.0F, -8.0F);
/* 105 */     this.frontLeftShin = new ModelRenderer(this, 44, 41);
/* 106 */     this.frontLeftShin.addBox(-1.9F, 0.0F, -1.6F, 3, 5, 3);
/* 107 */     this.frontLeftShin.setRotationPoint(4.0F, 16.0F, -8.0F);
/* 108 */     this.frontLeftHoof = new ModelRenderer(this, 44, 51);
/* 109 */     this.frontLeftHoof.addBox(-2.4F, 5.1F, -2.1F, 4, 3, 4);
/* 110 */     this.frontLeftHoof.setRotationPoint(4.0F, 16.0F, -8.0F);
/* 111 */     this.frontRightLeg = new ModelRenderer(this, 60, 29);
/* 112 */     this.frontRightLeg.addBox(-1.1F, -1.0F, -2.1F, 3, 8, 4);
/* 113 */     this.frontRightLeg.setRotationPoint(-4.0F, 9.0F, -8.0F);
/* 114 */     this.frontRightShin = new ModelRenderer(this, 60, 41);
/* 115 */     this.frontRightShin.addBox(-1.1F, 0.0F, -1.6F, 3, 5, 3);
/* 116 */     this.frontRightShin.setRotationPoint(-4.0F, 16.0F, -8.0F);
/* 117 */     this.frontRightHoof = new ModelRenderer(this, 60, 51);
/* 118 */     this.frontRightHoof.addBox(-1.6F, 5.1F, -2.1F, 4, 3, 4);
/* 119 */     this.frontRightHoof.setRotationPoint(-4.0F, 16.0F, -8.0F);
/* 120 */     this.head = new ModelRenderer(this, 0, 0);
/* 121 */     this.head.addBox(-2.5F, -10.0F, -1.5F, 5, 5, 7);
/* 122 */     this.head.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 123 */     setBoxRotation(this.head, 0.5235988F, 0.0F, 0.0F);
/* 124 */     this.field_178711_b = new ModelRenderer(this, 24, 18);
/* 125 */     this.field_178711_b.addBox(-2.0F, -10.0F, -7.0F, 4, 3, 6);
/* 126 */     this.field_178711_b.setRotationPoint(0.0F, 3.95F, -10.0F);
/* 127 */     setBoxRotation(this.field_178711_b, 0.5235988F, 0.0F, 0.0F);
/* 128 */     this.field_178712_c = new ModelRenderer(this, 24, 27);
/* 129 */     this.field_178712_c.addBox(-2.0F, -7.0F, -6.5F, 4, 2, 5);
/* 130 */     this.field_178712_c.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 131 */     setBoxRotation(this.field_178712_c, 0.5235988F, 0.0F, 0.0F);
/* 132 */     this.head.addChild(this.field_178711_b);
/* 133 */     this.head.addChild(this.field_178712_c);
/* 134 */     this.horseLeftEar = new ModelRenderer(this, 0, 0);
/* 135 */     this.horseLeftEar.addBox(0.45F, -12.0F, 4.0F, 2, 3, 1);
/* 136 */     this.horseLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 137 */     setBoxRotation(this.horseLeftEar, 0.5235988F, 0.0F, 0.0F);
/* 138 */     this.horseRightEar = new ModelRenderer(this, 0, 0);
/* 139 */     this.horseRightEar.addBox(-2.45F, -12.0F, 4.0F, 2, 3, 1);
/* 140 */     this.horseRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 141 */     setBoxRotation(this.horseRightEar, 0.5235988F, 0.0F, 0.0F);
/* 142 */     this.muleLeftEar = new ModelRenderer(this, 0, 12);
/* 143 */     this.muleLeftEar.addBox(-2.0F, -16.0F, 4.0F, 2, 7, 1);
/* 144 */     this.muleLeftEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 145 */     setBoxRotation(this.muleLeftEar, 0.5235988F, 0.0F, 0.2617994F);
/* 146 */     this.muleRightEar = new ModelRenderer(this, 0, 12);
/* 147 */     this.muleRightEar.addBox(0.0F, -16.0F, 4.0F, 2, 7, 1);
/* 148 */     this.muleRightEar.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 149 */     setBoxRotation(this.muleRightEar, 0.5235988F, 0.0F, -0.2617994F);
/* 150 */     this.neck = new ModelRenderer(this, 0, 12);
/* 151 */     this.neck.addBox(-2.05F, -9.8F, -2.0F, 4, 14, 8);
/* 152 */     this.neck.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 153 */     setBoxRotation(this.neck, 0.5235988F, 0.0F, 0.0F);
/* 154 */     this.muleLeftChest = new ModelRenderer(this, 0, 34);
/* 155 */     this.muleLeftChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
/* 156 */     this.muleLeftChest.setRotationPoint(-7.5F, 3.0F, 10.0F);
/* 157 */     setBoxRotation(this.muleLeftChest, 0.0F, 1.5707964F, 0.0F);
/* 158 */     this.muleRightChest = new ModelRenderer(this, 0, 47);
/* 159 */     this.muleRightChest.addBox(-3.0F, 0.0F, 0.0F, 8, 8, 3);
/* 160 */     this.muleRightChest.setRotationPoint(4.5F, 3.0F, 10.0F);
/* 161 */     setBoxRotation(this.muleRightChest, 0.0F, 1.5707964F, 0.0F);
/* 162 */     this.horseSaddleBottom = new ModelRenderer(this, 80, 0);
/* 163 */     this.horseSaddleBottom.addBox(-5.0F, 0.0F, -3.0F, 10, 1, 8);
/* 164 */     this.horseSaddleBottom.setRotationPoint(0.0F, 2.0F, 2.0F);
/* 165 */     this.horseSaddleFront = new ModelRenderer(this, 106, 9);
/* 166 */     this.horseSaddleFront.addBox(-1.5F, -1.0F, -3.0F, 3, 1, 2);
/* 167 */     this.horseSaddleFront.setRotationPoint(0.0F, 2.0F, 2.0F);
/* 168 */     this.horseSaddleBack = new ModelRenderer(this, 80, 9);
/* 169 */     this.horseSaddleBack.addBox(-4.0F, -1.0F, 3.0F, 8, 1, 2);
/* 170 */     this.horseSaddleBack.setRotationPoint(0.0F, 2.0F, 2.0F);
/* 171 */     this.horseLeftSaddleMetal = new ModelRenderer(this, 74, 0);
/* 172 */     this.horseLeftSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
/* 173 */     this.horseLeftSaddleMetal.setRotationPoint(5.0F, 3.0F, 2.0F);
/* 174 */     this.horseLeftSaddleRope = new ModelRenderer(this, 70, 0);
/* 175 */     this.horseLeftSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
/* 176 */     this.horseLeftSaddleRope.setRotationPoint(5.0F, 3.0F, 2.0F);
/* 177 */     this.horseRightSaddleMetal = new ModelRenderer(this, 74, 4);
/* 178 */     this.horseRightSaddleMetal.addBox(-0.5F, 6.0F, -1.0F, 1, 2, 2);
/* 179 */     this.horseRightSaddleMetal.setRotationPoint(-5.0F, 3.0F, 2.0F);
/* 180 */     this.horseRightSaddleRope = new ModelRenderer(this, 80, 0);
/* 181 */     this.horseRightSaddleRope.addBox(-0.5F, 0.0F, -0.5F, 1, 6, 1);
/* 182 */     this.horseRightSaddleRope.setRotationPoint(-5.0F, 3.0F, 2.0F);
/* 183 */     this.horseLeftFaceMetal = new ModelRenderer(this, 74, 13);
/* 184 */     this.horseLeftFaceMetal.addBox(1.5F, -8.0F, -4.0F, 1, 2, 2);
/* 185 */     this.horseLeftFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 186 */     setBoxRotation(this.horseLeftFaceMetal, 0.5235988F, 0.0F, 0.0F);
/* 187 */     this.horseRightFaceMetal = new ModelRenderer(this, 74, 13);
/* 188 */     this.horseRightFaceMetal.addBox(-2.5F, -8.0F, -4.0F, 1, 2, 2);
/* 189 */     this.horseRightFaceMetal.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 190 */     setBoxRotation(this.horseRightFaceMetal, 0.5235988F, 0.0F, 0.0F);
/* 191 */     this.horseLeftRein = new ModelRenderer(this, 44, 10);
/* 192 */     this.horseLeftRein.addBox(2.6F, -6.0F, -6.0F, 0, 3, 16);
/* 193 */     this.horseLeftRein.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 194 */     this.horseRightRein = new ModelRenderer(this, 44, 5);
/* 195 */     this.horseRightRein.addBox(-2.6F, -6.0F, -6.0F, 0, 3, 16);
/* 196 */     this.horseRightRein.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 197 */     this.mane = new ModelRenderer(this, 58, 0);
/* 198 */     this.mane.addBox(-1.0F, -11.5F, 5.0F, 2, 16, 4);
/* 199 */     this.mane.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 200 */     setBoxRotation(this.mane, 0.5235988F, 0.0F, 0.0F);
/* 201 */     this.horseFaceRopes = new ModelRenderer(this, 80, 12);
/* 202 */     this.horseFaceRopes.addBox(-2.5F, -10.1F, -7.0F, 5, 5, 12, 0.2F);
/* 203 */     this.horseFaceRopes.setRotationPoint(0.0F, 4.0F, -10.0F);
/* 204 */     setBoxRotation(this.horseFaceRopes, 0.5235988F, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/* 212 */     EntityHorse entityhorse = (EntityHorse)entityIn;
/* 213 */     int i = entityhorse.getHorseType();
/* 214 */     float f = entityhorse.getGrassEatingAmount(0.0F);
/* 215 */     boolean flag = entityhorse.isAdultHorse();
/* 216 */     boolean flag1 = (flag) && (entityhorse.isHorseSaddled());
/* 217 */     boolean flag2 = (flag) && (entityhorse.isChested());
/* 218 */     boolean flag3 = (i == 1) || (i == 2);
/* 219 */     float f1 = entityhorse.getHorseSize();
/* 220 */     boolean flag4 = entityhorse.riddenByEntity != null;
/*     */     
/* 222 */     if (flag1)
/*     */     {
/* 224 */       this.horseFaceRopes.render(scale);
/* 225 */       this.horseSaddleBottom.render(scale);
/* 226 */       this.horseSaddleFront.render(scale);
/* 227 */       this.horseSaddleBack.render(scale);
/* 228 */       this.horseLeftSaddleRope.render(scale);
/* 229 */       this.horseLeftSaddleMetal.render(scale);
/* 230 */       this.horseRightSaddleRope.render(scale);
/* 231 */       this.horseRightSaddleMetal.render(scale);
/* 232 */       this.horseLeftFaceMetal.render(scale);
/* 233 */       this.horseRightFaceMetal.render(scale);
/*     */       
/* 235 */       if (flag4)
/*     */       {
/* 237 */         this.horseLeftRein.render(scale);
/* 238 */         this.horseRightRein.render(scale);
/*     */       }
/*     */     }
/*     */     
/* 242 */     if (!flag)
/*     */     {
/* 244 */       GlStateManager.pushMatrix();
/* 245 */       GlStateManager.scale(f1, 0.5F + f1 * 0.5F, f1);
/* 246 */       GlStateManager.translate(0.0F, 0.95F * (1.0F - f1), 0.0F);
/*     */     }
/*     */     
/* 249 */     this.backLeftLeg.render(scale);
/* 250 */     this.backLeftShin.render(scale);
/* 251 */     this.backLeftHoof.render(scale);
/* 252 */     this.backRightLeg.render(scale);
/* 253 */     this.backRightShin.render(scale);
/* 254 */     this.backRightHoof.render(scale);
/* 255 */     this.frontLeftLeg.render(scale);
/* 256 */     this.frontLeftShin.render(scale);
/* 257 */     this.frontLeftHoof.render(scale);
/* 258 */     this.frontRightLeg.render(scale);
/* 259 */     this.frontRightShin.render(scale);
/* 260 */     this.frontRightHoof.render(scale);
/*     */     
/* 262 */     if (!flag)
/*     */     {
/* 264 */       GlStateManager.popMatrix();
/* 265 */       GlStateManager.pushMatrix();
/* 266 */       GlStateManager.scale(f1, f1, f1);
/* 267 */       GlStateManager.translate(0.0F, 1.35F * (1.0F - f1), 0.0F);
/*     */     }
/*     */     
/* 270 */     this.body.render(scale);
/* 271 */     this.tailBase.render(scale);
/* 272 */     this.tailMiddle.render(scale);
/* 273 */     this.tailTip.render(scale);
/* 274 */     this.neck.render(scale);
/* 275 */     this.mane.render(scale);
/*     */     
/* 277 */     if (!flag)
/*     */     {
/* 279 */       GlStateManager.popMatrix();
/* 280 */       GlStateManager.pushMatrix();
/* 281 */       float f2 = 0.5F + f1 * f1 * 0.5F;
/* 282 */       GlStateManager.scale(f2, f2, f2);
/*     */       
/* 284 */       if (f <= 0.0F)
/*     */       {
/* 286 */         GlStateManager.translate(0.0F, 1.35F * (1.0F - f1), 0.0F);
/*     */       }
/*     */       else
/*     */       {
/* 290 */         GlStateManager.translate(0.0F, 0.9F * (1.0F - f1) * f + 1.35F * (1.0F - f1) * (1.0F - f), 0.15F * (1.0F - f1) * f);
/*     */       }
/*     */     }
/*     */     
/* 294 */     if (flag3)
/*     */     {
/* 296 */       this.muleLeftEar.render(scale);
/* 297 */       this.muleRightEar.render(scale);
/*     */     }
/*     */     else
/*     */     {
/* 301 */       this.horseLeftEar.render(scale);
/* 302 */       this.horseRightEar.render(scale);
/*     */     }
/*     */     
/* 305 */     this.head.render(scale);
/*     */     
/* 307 */     if (!flag)
/*     */     {
/* 309 */       GlStateManager.popMatrix();
/*     */     }
/*     */     
/* 312 */     if (flag2)
/*     */     {
/* 314 */       this.muleLeftChest.render(scale);
/* 315 */       this.muleRightChest.render(scale);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setBoxRotation(ModelRenderer p_110682_1_, float p_110682_2_, float p_110682_3_, float p_110682_4_)
/*     */   {
/* 324 */     p_110682_1_.rotateAngleX = p_110682_2_;
/* 325 */     p_110682_1_.rotateAngleY = p_110682_3_;
/* 326 */     p_110682_1_.rotateAngleZ = p_110682_4_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private float updateHorseRotation(float p_110683_1_, float p_110683_2_, float p_110683_3_)
/*     */   {
/* 336 */     for (float f = p_110683_2_ - p_110683_1_; f < -180.0F; f += 360.0F) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 341 */     while (f >= 180.0F)
/*     */     {
/* 343 */       f -= 360.0F;
/*     */     }
/*     */     
/* 346 */     return p_110683_1_ + p_110683_3_ * f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*     */   {
/* 355 */     super.setLivingAnimations(entitylivingbaseIn, p_78086_2_, p_78086_3_, partialTickTime);
/* 356 */     float f = updateHorseRotation(entitylivingbaseIn.prevRenderYawOffset, entitylivingbaseIn.renderYawOffset, partialTickTime);
/* 357 */     float f1 = updateHorseRotation(entitylivingbaseIn.prevRotationYawHead, entitylivingbaseIn.rotationYawHead, partialTickTime);
/* 358 */     float f2 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTickTime;
/* 359 */     float f3 = f1 - f;
/* 360 */     float f4 = f2 / 57.295776F;
/*     */     
/* 362 */     if (f3 > 20.0F)
/*     */     {
/* 364 */       f3 = 20.0F;
/*     */     }
/*     */     
/* 367 */     if (f3 < -20.0F)
/*     */     {
/* 369 */       f3 = -20.0F;
/*     */     }
/*     */     
/* 372 */     if (p_78086_3_ > 0.2F)
/*     */     {
/* 374 */       f4 += MathHelper.cos(p_78086_2_ * 0.4F) * 0.15F * p_78086_3_;
/*     */     }
/*     */     
/* 377 */     EntityHorse entityhorse = (EntityHorse)entitylivingbaseIn;
/* 378 */     float f5 = entityhorse.getGrassEatingAmount(partialTickTime);
/* 379 */     float f6 = entityhorse.getRearingAmount(partialTickTime);
/* 380 */     float f7 = 1.0F - f6;
/* 381 */     float f8 = entityhorse.getMouthOpennessAngle(partialTickTime);
/* 382 */     boolean flag = entityhorse.field_110278_bp != 0;
/* 383 */     boolean flag1 = entityhorse.isHorseSaddled();
/* 384 */     boolean flag2 = entityhorse.riddenByEntity != null;
/* 385 */     float f9 = entitylivingbaseIn.ticksExisted + partialTickTime;
/* 386 */     float f10 = MathHelper.cos(p_78086_2_ * 0.6662F + 3.1415927F);
/* 387 */     float f11 = f10 * 0.8F * p_78086_3_;
/* 388 */     this.head.rotationPointY = 4.0F;
/* 389 */     this.head.rotationPointZ = -10.0F;
/* 390 */     this.tailBase.rotationPointY = 3.0F;
/* 391 */     this.tailMiddle.rotationPointZ = 14.0F;
/* 392 */     this.muleRightChest.rotationPointY = 3.0F;
/* 393 */     this.muleRightChest.rotationPointZ = 10.0F;
/* 394 */     this.body.rotateAngleX = 0.0F;
/* 395 */     this.head.rotateAngleX = (0.5235988F + f4);
/* 396 */     this.head.rotateAngleY = (f3 / 57.295776F);
/* 397 */     this.head.rotateAngleX = (f6 * (0.2617994F + f4) + f5 * 2.18166F + (1.0F - Math.max(f6, f5)) * this.head.rotateAngleX);
/* 398 */     this.head.rotateAngleY = (f6 * f3 / 57.295776F + (1.0F - Math.max(f6, f5)) * this.head.rotateAngleY);
/* 399 */     this.head.rotationPointY = (f6 * -6.0F + f5 * 11.0F + (1.0F - Math.max(f6, f5)) * this.head.rotationPointY);
/* 400 */     this.head.rotationPointZ = (f6 * -1.0F + f5 * -10.0F + (1.0F - Math.max(f6, f5)) * this.head.rotationPointZ);
/* 401 */     this.tailBase.rotationPointY = (f6 * 9.0F + f7 * this.tailBase.rotationPointY);
/* 402 */     this.tailMiddle.rotationPointZ = (f6 * 18.0F + f7 * this.tailMiddle.rotationPointZ);
/* 403 */     this.muleRightChest.rotationPointY = (f6 * 5.5F + f7 * this.muleRightChest.rotationPointY);
/* 404 */     this.muleRightChest.rotationPointZ = (f6 * 15.0F + f7 * this.muleRightChest.rotationPointZ);
/* 405 */     this.body.rotateAngleX = (f6 * -45.0F / 57.295776F + f7 * this.body.rotateAngleX);
/* 406 */     this.horseLeftEar.rotationPointY = this.head.rotationPointY;
/* 407 */     this.horseRightEar.rotationPointY = this.head.rotationPointY;
/* 408 */     this.muleLeftEar.rotationPointY = this.head.rotationPointY;
/* 409 */     this.muleRightEar.rotationPointY = this.head.rotationPointY;
/* 410 */     this.neck.rotationPointY = this.head.rotationPointY;
/* 411 */     this.field_178711_b.rotationPointY = 0.02F;
/* 412 */     this.field_178712_c.rotationPointY = 0.0F;
/* 413 */     this.mane.rotationPointY = this.head.rotationPointY;
/* 414 */     this.horseLeftEar.rotationPointZ = this.head.rotationPointZ;
/* 415 */     this.horseRightEar.rotationPointZ = this.head.rotationPointZ;
/* 416 */     this.muleLeftEar.rotationPointZ = this.head.rotationPointZ;
/* 417 */     this.muleRightEar.rotationPointZ = this.head.rotationPointZ;
/* 418 */     this.neck.rotationPointZ = this.head.rotationPointZ;
/* 419 */     this.field_178711_b.rotationPointZ = (0.02F - f8 * 1.0F);
/* 420 */     this.field_178712_c.rotationPointZ = (0.0F + f8 * 1.0F);
/* 421 */     this.mane.rotationPointZ = this.head.rotationPointZ;
/* 422 */     this.horseLeftEar.rotateAngleX = this.head.rotateAngleX;
/* 423 */     this.horseRightEar.rotateAngleX = this.head.rotateAngleX;
/* 424 */     this.muleLeftEar.rotateAngleX = this.head.rotateAngleX;
/* 425 */     this.muleRightEar.rotateAngleX = this.head.rotateAngleX;
/* 426 */     this.neck.rotateAngleX = this.head.rotateAngleX;
/* 427 */     this.field_178711_b.rotateAngleX = (0.0F - 0.09424778F * f8);
/* 428 */     this.field_178712_c.rotateAngleX = (0.0F + 0.15707964F * f8);
/* 429 */     this.mane.rotateAngleX = this.head.rotateAngleX;
/* 430 */     this.horseLeftEar.rotateAngleY = this.head.rotateAngleY;
/* 431 */     this.horseRightEar.rotateAngleY = this.head.rotateAngleY;
/* 432 */     this.muleLeftEar.rotateAngleY = this.head.rotateAngleY;
/* 433 */     this.muleRightEar.rotateAngleY = this.head.rotateAngleY;
/* 434 */     this.neck.rotateAngleY = this.head.rotateAngleY;
/* 435 */     this.field_178711_b.rotateAngleY = 0.0F;
/* 436 */     this.field_178712_c.rotateAngleY = 0.0F;
/* 437 */     this.mane.rotateAngleY = this.head.rotateAngleY;
/* 438 */     this.muleLeftChest.rotateAngleX = (f11 / 5.0F);
/* 439 */     this.muleRightChest.rotateAngleX = (-f11 / 5.0F);
/* 440 */     float f12 = 1.5707964F;
/* 441 */     float f13 = 4.712389F;
/* 442 */     float f14 = -1.0471976F;
/* 443 */     float f15 = 0.2617994F * f6;
/* 444 */     float f16 = MathHelper.cos(f9 * 0.6F + 3.1415927F);
/* 445 */     this.frontLeftLeg.rotationPointY = (-2.0F * f6 + 9.0F * f7);
/* 446 */     this.frontLeftLeg.rotationPointZ = (-2.0F * f6 + -8.0F * f7);
/* 447 */     this.frontRightLeg.rotationPointY = this.frontLeftLeg.rotationPointY;
/* 448 */     this.frontRightLeg.rotationPointZ = this.frontLeftLeg.rotationPointZ;
/* 449 */     this.backLeftShin.rotationPointY = (this.backLeftLeg.rotationPointY + MathHelper.sin(1.5707964F + f15 + f7 * -f10 * 0.5F * p_78086_3_) * 7.0F);
/* 450 */     this.backLeftShin.rotationPointZ = (this.backLeftLeg.rotationPointZ + MathHelper.cos(4.712389F + f15 + f7 * -f10 * 0.5F * p_78086_3_) * 7.0F);
/* 451 */     this.backRightShin.rotationPointY = (this.backRightLeg.rotationPointY + MathHelper.sin(1.5707964F + f15 + f7 * f10 * 0.5F * p_78086_3_) * 7.0F);
/* 452 */     this.backRightShin.rotationPointZ = (this.backRightLeg.rotationPointZ + MathHelper.cos(4.712389F + f15 + f7 * f10 * 0.5F * p_78086_3_) * 7.0F);
/* 453 */     float f17 = (-1.0471976F + f16) * f6 + f11 * f7;
/* 454 */     float f18 = (-1.0471976F + -f16) * f6 + -f11 * f7;
/* 455 */     this.frontLeftShin.rotationPointY = (this.frontLeftLeg.rotationPointY + MathHelper.sin(1.5707964F + f17) * 7.0F);
/* 456 */     this.frontLeftShin.rotationPointZ = (this.frontLeftLeg.rotationPointZ + MathHelper.cos(4.712389F + f17) * 7.0F);
/* 457 */     this.frontRightShin.rotationPointY = (this.frontRightLeg.rotationPointY + MathHelper.sin(1.5707964F + f18) * 7.0F);
/* 458 */     this.frontRightShin.rotationPointZ = (this.frontRightLeg.rotationPointZ + MathHelper.cos(4.712389F + f18) * 7.0F);
/* 459 */     this.backLeftLeg.rotateAngleX = (f15 + -f10 * 0.5F * p_78086_3_ * f7);
/* 460 */     this.backLeftShin.rotateAngleX = (-0.08726646F * f6 + (-f10 * 0.5F * p_78086_3_ - Math.max(0.0F, f10 * 0.5F * p_78086_3_)) * f7);
/* 461 */     this.backLeftHoof.rotateAngleX = this.backLeftShin.rotateAngleX;
/* 462 */     this.backRightLeg.rotateAngleX = (f15 + f10 * 0.5F * p_78086_3_ * f7);
/* 463 */     this.backRightShin.rotateAngleX = (-0.08726646F * f6 + (f10 * 0.5F * p_78086_3_ - Math.max(0.0F, -f10 * 0.5F * p_78086_3_)) * f7);
/* 464 */     this.backRightHoof.rotateAngleX = this.backRightShin.rotateAngleX;
/* 465 */     this.frontLeftLeg.rotateAngleX = f17;
/* 466 */     this.frontLeftShin.rotateAngleX = ((this.frontLeftLeg.rotateAngleX + 3.1415927F * Math.max(0.0F, 0.2F + f16 * 0.2F)) * f6 + (f11 + Math.max(0.0F, f10 * 0.5F * p_78086_3_)) * f7);
/* 467 */     this.frontLeftHoof.rotateAngleX = this.frontLeftShin.rotateAngleX;
/* 468 */     this.frontRightLeg.rotateAngleX = f18;
/* 469 */     this.frontRightShin.rotateAngleX = ((this.frontRightLeg.rotateAngleX + 3.1415927F * Math.max(0.0F, 0.2F - f16 * 0.2F)) * f6 + (-f11 + Math.max(0.0F, -f10 * 0.5F * p_78086_3_)) * f7);
/* 470 */     this.frontRightHoof.rotateAngleX = this.frontRightShin.rotateAngleX;
/* 471 */     this.backLeftHoof.rotationPointY = this.backLeftShin.rotationPointY;
/* 472 */     this.backLeftHoof.rotationPointZ = this.backLeftShin.rotationPointZ;
/* 473 */     this.backRightHoof.rotationPointY = this.backRightShin.rotationPointY;
/* 474 */     this.backRightHoof.rotationPointZ = this.backRightShin.rotationPointZ;
/* 475 */     this.frontLeftHoof.rotationPointY = this.frontLeftShin.rotationPointY;
/* 476 */     this.frontLeftHoof.rotationPointZ = this.frontLeftShin.rotationPointZ;
/* 477 */     this.frontRightHoof.rotationPointY = this.frontRightShin.rotationPointY;
/* 478 */     this.frontRightHoof.rotationPointZ = this.frontRightShin.rotationPointZ;
/*     */     
/* 480 */     if (flag1)
/*     */     {
/* 482 */       this.horseSaddleBottom.rotationPointY = (f6 * 0.5F + f7 * 2.0F);
/* 483 */       this.horseSaddleBottom.rotationPointZ = (f6 * 11.0F + f7 * 2.0F);
/* 484 */       this.horseSaddleFront.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 485 */       this.horseSaddleBack.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 486 */       this.horseLeftSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 487 */       this.horseRightSaddleRope.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 488 */       this.horseLeftSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 489 */       this.horseRightSaddleMetal.rotationPointY = this.horseSaddleBottom.rotationPointY;
/* 490 */       this.muleLeftChest.rotationPointY = this.muleRightChest.rotationPointY;
/* 491 */       this.horseSaddleFront.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 492 */       this.horseSaddleBack.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 493 */       this.horseLeftSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 494 */       this.horseRightSaddleRope.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 495 */       this.horseLeftSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 496 */       this.horseRightSaddleMetal.rotationPointZ = this.horseSaddleBottom.rotationPointZ;
/* 497 */       this.muleLeftChest.rotationPointZ = this.muleRightChest.rotationPointZ;
/* 498 */       this.horseSaddleBottom.rotateAngleX = this.body.rotateAngleX;
/* 499 */       this.horseSaddleFront.rotateAngleX = this.body.rotateAngleX;
/* 500 */       this.horseSaddleBack.rotateAngleX = this.body.rotateAngleX;
/* 501 */       this.horseLeftRein.rotationPointY = this.head.rotationPointY;
/* 502 */       this.horseRightRein.rotationPointY = this.head.rotationPointY;
/* 503 */       this.horseFaceRopes.rotationPointY = this.head.rotationPointY;
/* 504 */       this.horseLeftFaceMetal.rotationPointY = this.head.rotationPointY;
/* 505 */       this.horseRightFaceMetal.rotationPointY = this.head.rotationPointY;
/* 506 */       this.horseLeftRein.rotationPointZ = this.head.rotationPointZ;
/* 507 */       this.horseRightRein.rotationPointZ = this.head.rotationPointZ;
/* 508 */       this.horseFaceRopes.rotationPointZ = this.head.rotationPointZ;
/* 509 */       this.horseLeftFaceMetal.rotationPointZ = this.head.rotationPointZ;
/* 510 */       this.horseRightFaceMetal.rotationPointZ = this.head.rotationPointZ;
/* 511 */       this.horseLeftRein.rotateAngleX = f4;
/* 512 */       this.horseRightRein.rotateAngleX = f4;
/* 513 */       this.horseFaceRopes.rotateAngleX = this.head.rotateAngleX;
/* 514 */       this.horseLeftFaceMetal.rotateAngleX = this.head.rotateAngleX;
/* 515 */       this.horseRightFaceMetal.rotateAngleX = this.head.rotateAngleX;
/* 516 */       this.horseFaceRopes.rotateAngleY = this.head.rotateAngleY;
/* 517 */       this.horseLeftFaceMetal.rotateAngleY = this.head.rotateAngleY;
/* 518 */       this.horseLeftRein.rotateAngleY = this.head.rotateAngleY;
/* 519 */       this.horseRightFaceMetal.rotateAngleY = this.head.rotateAngleY;
/* 520 */       this.horseRightRein.rotateAngleY = this.head.rotateAngleY;
/*     */       
/* 522 */       if (flag2)
/*     */       {
/* 524 */         this.horseLeftSaddleRope.rotateAngleX = -1.0471976F;
/* 525 */         this.horseLeftSaddleMetal.rotateAngleX = -1.0471976F;
/* 526 */         this.horseRightSaddleRope.rotateAngleX = -1.0471976F;
/* 527 */         this.horseRightSaddleMetal.rotateAngleX = -1.0471976F;
/* 528 */         this.horseLeftSaddleRope.rotateAngleZ = 0.0F;
/* 529 */         this.horseLeftSaddleMetal.rotateAngleZ = 0.0F;
/* 530 */         this.horseRightSaddleRope.rotateAngleZ = 0.0F;
/* 531 */         this.horseRightSaddleMetal.rotateAngleZ = 0.0F;
/*     */       }
/*     */       else
/*     */       {
/* 535 */         this.horseLeftSaddleRope.rotateAngleX = (f11 / 3.0F);
/* 536 */         this.horseLeftSaddleMetal.rotateAngleX = (f11 / 3.0F);
/* 537 */         this.horseRightSaddleRope.rotateAngleX = (f11 / 3.0F);
/* 538 */         this.horseRightSaddleMetal.rotateAngleX = (f11 / 3.0F);
/* 539 */         this.horseLeftSaddleRope.rotateAngleZ = (f11 / 5.0F);
/* 540 */         this.horseLeftSaddleMetal.rotateAngleZ = (f11 / 5.0F);
/* 541 */         this.horseRightSaddleRope.rotateAngleZ = (-f11 / 5.0F);
/* 542 */         this.horseRightSaddleMetal.rotateAngleZ = (-f11 / 5.0F);
/*     */       }
/*     */     }
/*     */     
/* 546 */     f12 = -1.3089F + p_78086_3_ * 1.5F;
/*     */     
/* 548 */     if (f12 > 0.0F)
/*     */     {
/* 550 */       f12 = 0.0F;
/*     */     }
/*     */     
/* 553 */     if (flag)
/*     */     {
/* 555 */       this.tailBase.rotateAngleY = MathHelper.cos(f9 * 0.7F);
/* 556 */       f12 = 0.0F;
/*     */     }
/*     */     else
/*     */     {
/* 560 */       this.tailBase.rotateAngleY = 0.0F;
/*     */     }
/*     */     
/* 563 */     this.tailMiddle.rotateAngleY = this.tailBase.rotateAngleY;
/* 564 */     this.tailTip.rotateAngleY = this.tailBase.rotateAngleY;
/* 565 */     this.tailMiddle.rotationPointY = this.tailBase.rotationPointY;
/* 566 */     this.tailTip.rotationPointY = this.tailBase.rotationPointY;
/* 567 */     this.tailMiddle.rotationPointZ = this.tailBase.rotationPointZ;
/* 568 */     this.tailTip.rotationPointZ = this.tailBase.rotationPointZ;
/* 569 */     this.tailBase.rotateAngleX = f12;
/* 570 */     this.tailMiddle.rotateAngleX = f12;
/* 571 */     this.tailTip.rotateAngleX = (-0.2618F + f12);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */