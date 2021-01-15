/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.boss.EntityDragon;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModelDragon
/*     */   extends ModelBase
/*     */ {
/*     */   private ModelRenderer head;
/*     */   private ModelRenderer spine;
/*     */   private ModelRenderer jaw;
/*     */   private ModelRenderer body;
/*     */   private ModelRenderer rearLeg;
/*     */   private ModelRenderer frontLeg;
/*     */   private ModelRenderer rearLegTip;
/*     */   private ModelRenderer frontLegTip;
/*     */   private ModelRenderer rearFoot;
/*     */   private ModelRenderer frontFoot;
/*     */   private ModelRenderer wing;
/*     */   private ModelRenderer wingTip;
/*     */   private float partialTicks;
/*     */   
/*     */   public ModelDragon(float p_i46360_1_)
/*     */   {
/*  49 */     this.textureWidth = 256;
/*  50 */     this.textureHeight = 256;
/*  51 */     setTextureOffset("body.body", 0, 0);
/*  52 */     setTextureOffset("wing.skin", -56, 88);
/*  53 */     setTextureOffset("wingtip.skin", -56, 144);
/*  54 */     setTextureOffset("rearleg.main", 0, 0);
/*  55 */     setTextureOffset("rearfoot.main", 112, 0);
/*  56 */     setTextureOffset("rearlegtip.main", 196, 0);
/*  57 */     setTextureOffset("head.upperhead", 112, 30);
/*  58 */     setTextureOffset("wing.bone", 112, 88);
/*  59 */     setTextureOffset("head.upperlip", 176, 44);
/*  60 */     setTextureOffset("jaw.jaw", 176, 65);
/*  61 */     setTextureOffset("frontleg.main", 112, 104);
/*  62 */     setTextureOffset("wingtip.bone", 112, 136);
/*  63 */     setTextureOffset("frontfoot.main", 144, 104);
/*  64 */     setTextureOffset("neck.box", 192, 104);
/*  65 */     setTextureOffset("frontlegtip.main", 226, 138);
/*  66 */     setTextureOffset("body.scale", 220, 53);
/*  67 */     setTextureOffset("head.scale", 0, 0);
/*  68 */     setTextureOffset("neck.scale", 48, 0);
/*  69 */     setTextureOffset("head.nostril", 112, 0);
/*  70 */     float f = -16.0F;
/*  71 */     this.head = new ModelRenderer(this, "head");
/*  72 */     this.head.addBox("upperlip", -6.0F, -1.0F, -8.0F + f, 12, 5, 16);
/*  73 */     this.head.addBox("upperhead", -8.0F, -8.0F, 6.0F + f, 16, 16, 16);
/*  74 */     this.head.mirror = true;
/*  75 */     this.head.addBox("scale", -5.0F, -12.0F, 12.0F + f, 2, 4, 6);
/*  76 */     this.head.addBox("nostril", -5.0F, -3.0F, -6.0F + f, 2, 2, 4);
/*  77 */     this.head.mirror = false;
/*  78 */     this.head.addBox("scale", 3.0F, -12.0F, 12.0F + f, 2, 4, 6);
/*  79 */     this.head.addBox("nostril", 3.0F, -3.0F, -6.0F + f, 2, 2, 4);
/*  80 */     this.jaw = new ModelRenderer(this, "jaw");
/*  81 */     this.jaw.setRotationPoint(0.0F, 4.0F, 8.0F + f);
/*  82 */     this.jaw.addBox("jaw", -6.0F, 0.0F, -16.0F, 12, 4, 16);
/*  83 */     this.head.addChild(this.jaw);
/*  84 */     this.spine = new ModelRenderer(this, "neck");
/*  85 */     this.spine.addBox("box", -5.0F, -5.0F, -5.0F, 10, 10, 10);
/*  86 */     this.spine.addBox("scale", -1.0F, -9.0F, -3.0F, 2, 4, 6);
/*  87 */     this.body = new ModelRenderer(this, "body");
/*  88 */     this.body.setRotationPoint(0.0F, 4.0F, 8.0F);
/*  89 */     this.body.addBox("body", -12.0F, 0.0F, -16.0F, 24, 24, 64);
/*  90 */     this.body.addBox("scale", -1.0F, -6.0F, -10.0F, 2, 6, 12);
/*  91 */     this.body.addBox("scale", -1.0F, -6.0F, 10.0F, 2, 6, 12);
/*  92 */     this.body.addBox("scale", -1.0F, -6.0F, 30.0F, 2, 6, 12);
/*  93 */     this.wing = new ModelRenderer(this, "wing");
/*  94 */     this.wing.setRotationPoint(-12.0F, 5.0F, 2.0F);
/*  95 */     this.wing.addBox("bone", -56.0F, -4.0F, -4.0F, 56, 8, 8);
/*  96 */     this.wing.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
/*  97 */     this.wingTip = new ModelRenderer(this, "wingtip");
/*  98 */     this.wingTip.setRotationPoint(-56.0F, 0.0F, 0.0F);
/*  99 */     this.wingTip.addBox("bone", -56.0F, -2.0F, -2.0F, 56, 4, 4);
/* 100 */     this.wingTip.addBox("skin", -56.0F, 0.0F, 2.0F, 56, 0, 56);
/* 101 */     this.wing.addChild(this.wingTip);
/* 102 */     this.frontLeg = new ModelRenderer(this, "frontleg");
/* 103 */     this.frontLeg.setRotationPoint(-12.0F, 20.0F, 2.0F);
/* 104 */     this.frontLeg.addBox("main", -4.0F, -4.0F, -4.0F, 8, 24, 8);
/* 105 */     this.frontLegTip = new ModelRenderer(this, "frontlegtip");
/* 106 */     this.frontLegTip.setRotationPoint(0.0F, 20.0F, -1.0F);
/* 107 */     this.frontLegTip.addBox("main", -3.0F, -1.0F, -3.0F, 6, 24, 6);
/* 108 */     this.frontLeg.addChild(this.frontLegTip);
/* 109 */     this.frontFoot = new ModelRenderer(this, "frontfoot");
/* 110 */     this.frontFoot.setRotationPoint(0.0F, 23.0F, 0.0F);
/* 111 */     this.frontFoot.addBox("main", -4.0F, 0.0F, -12.0F, 8, 4, 16);
/* 112 */     this.frontLegTip.addChild(this.frontFoot);
/* 113 */     this.rearLeg = new ModelRenderer(this, "rearleg");
/* 114 */     this.rearLeg.setRotationPoint(-16.0F, 16.0F, 42.0F);
/* 115 */     this.rearLeg.addBox("main", -8.0F, -4.0F, -8.0F, 16, 32, 16);
/* 116 */     this.rearLegTip = new ModelRenderer(this, "rearlegtip");
/* 117 */     this.rearLegTip.setRotationPoint(0.0F, 32.0F, -4.0F);
/* 118 */     this.rearLegTip.addBox("main", -6.0F, -2.0F, 0.0F, 12, 32, 12);
/* 119 */     this.rearLeg.addChild(this.rearLegTip);
/* 120 */     this.rearFoot = new ModelRenderer(this, "rearfoot");
/* 121 */     this.rearFoot.setRotationPoint(0.0F, 31.0F, 4.0F);
/* 122 */     this.rearFoot.addBox("main", -9.0F, 0.0F, -20.0F, 18, 6, 24);
/* 123 */     this.rearLegTip.addChild(this.rearFoot);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
/*     */   {
/* 132 */     this.partialTicks = partialTickTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/* 140 */     GlStateManager.pushMatrix();
/* 141 */     EntityDragon entitydragon = (EntityDragon)entityIn;
/* 142 */     float f = entitydragon.prevAnimTime + (entitydragon.animTime - entitydragon.prevAnimTime) * this.partialTicks;
/* 143 */     this.jaw.rotateAngleX = ((float)(Math.sin(f * 3.1415927F * 2.0F) + 1.0D) * 0.2F);
/* 144 */     float f1 = (float)(Math.sin(f * 3.1415927F * 2.0F - 1.0F) + 1.0D);
/* 145 */     f1 = (f1 * f1 * 1.0F + f1 * 2.0F) * 0.05F;
/* 146 */     GlStateManager.translate(0.0F, f1 - 2.0F, -3.0F);
/* 147 */     GlStateManager.rotate(f1 * 2.0F, 1.0F, 0.0F, 0.0F);
/* 148 */     float f2 = -30.0F;
/* 149 */     float f4 = 0.0F;
/* 150 */     float f5 = 1.5F;
/* 151 */     double[] adouble = entitydragon.getMovementOffsets(6, this.partialTicks);
/* 152 */     float f6 = updateRotations(entitydragon.getMovementOffsets(5, this.partialTicks)[0] - entitydragon.getMovementOffsets(10, this.partialTicks)[0]);
/* 153 */     float f7 = updateRotations(entitydragon.getMovementOffsets(5, this.partialTicks)[0] + f6 / 2.0F);
/* 154 */     f2 += 2.0F;
/* 155 */     float f8 = f * 3.1415927F * 2.0F;
/* 156 */     f2 = 20.0F;
/* 157 */     float f3 = -12.0F;
/*     */     
/* 159 */     for (int i = 0; i < 5; i++)
/*     */     {
/* 161 */       double[] adouble1 = entitydragon.getMovementOffsets(5 - i, this.partialTicks);
/* 162 */       float f9 = (float)Math.cos(i * 0.45F + f8) * 0.15F;
/* 163 */       this.spine.rotateAngleY = (updateRotations(adouble1[0] - adouble[0]) * 3.1415927F / 180.0F * f5);
/* 164 */       this.spine.rotateAngleX = (f9 + (float)(adouble1[1] - adouble[1]) * 3.1415927F / 180.0F * f5 * 5.0F);
/* 165 */       this.spine.rotateAngleZ = (-updateRotations(adouble1[0] - f7) * 3.1415927F / 180.0F * f5);
/* 166 */       this.spine.rotationPointY = f2;
/* 167 */       this.spine.rotationPointZ = f3;
/* 168 */       this.spine.rotationPointX = f4;
/* 169 */       f2 = (float)(f2 + Math.sin(this.spine.rotateAngleX) * 10.0D);
/* 170 */       f3 = (float)(f3 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 171 */       f4 = (float)(f4 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 172 */       this.spine.render(scale);
/*     */     }
/*     */     
/* 175 */     this.head.rotationPointY = f2;
/* 176 */     this.head.rotationPointZ = f3;
/* 177 */     this.head.rotationPointX = f4;
/* 178 */     double[] adouble2 = entitydragon.getMovementOffsets(0, this.partialTicks);
/* 179 */     this.head.rotateAngleY = (updateRotations(adouble2[0] - adouble[0]) * 3.1415927F / 180.0F * 1.0F);
/* 180 */     this.head.rotateAngleZ = (-updateRotations(adouble2[0] - f7) * 3.1415927F / 180.0F * 1.0F);
/* 181 */     this.head.render(scale);
/* 182 */     GlStateManager.pushMatrix();
/* 183 */     GlStateManager.translate(0.0F, 1.0F, 0.0F);
/* 184 */     GlStateManager.rotate(-f6 * f5 * 1.0F, 0.0F, 0.0F, 1.0F);
/* 185 */     GlStateManager.translate(0.0F, -1.0F, 0.0F);
/* 186 */     this.body.rotateAngleZ = 0.0F;
/* 187 */     this.body.render(scale);
/*     */     
/* 189 */     for (int j = 0; j < 2; j++)
/*     */     {
/* 191 */       GlStateManager.enableCull();
/* 192 */       float f11 = f * 3.1415927F * 2.0F;
/* 193 */       this.wing.rotateAngleX = (0.125F - (float)Math.cos(f11) * 0.2F);
/* 194 */       this.wing.rotateAngleY = 0.25F;
/* 195 */       this.wing.rotateAngleZ = ((float)(Math.sin(f11) + 0.125D) * 0.8F);
/* 196 */       this.wingTip.rotateAngleZ = (-(float)(Math.sin(f11 + 2.0F) + 0.5D) * 0.75F);
/* 197 */       this.rearLeg.rotateAngleX = (1.0F + f1 * 0.1F);
/* 198 */       this.rearLegTip.rotateAngleX = (0.5F + f1 * 0.1F);
/* 199 */       this.rearFoot.rotateAngleX = (0.75F + f1 * 0.1F);
/* 200 */       this.frontLeg.rotateAngleX = (1.3F + f1 * 0.1F);
/* 201 */       this.frontLegTip.rotateAngleX = (-0.5F - f1 * 0.1F);
/* 202 */       this.frontFoot.rotateAngleX = (0.75F + f1 * 0.1F);
/* 203 */       this.wing.render(scale);
/* 204 */       this.frontLeg.render(scale);
/* 205 */       this.rearLeg.render(scale);
/* 206 */       GlStateManager.scale(-1.0F, 1.0F, 1.0F);
/*     */       
/* 208 */       if (j == 0)
/*     */       {
/* 210 */         GlStateManager.cullFace(1028);
/*     */       }
/*     */     }
/*     */     
/* 214 */     GlStateManager.popMatrix();
/* 215 */     GlStateManager.cullFace(1029);
/* 216 */     GlStateManager.disableCull();
/* 217 */     float f10 = -(float)Math.sin(f * 3.1415927F * 2.0F) * 0.0F;
/* 218 */     f8 = f * 3.1415927F * 2.0F;
/* 219 */     f2 = 10.0F;
/* 220 */     f3 = 60.0F;
/* 221 */     f4 = 0.0F;
/* 222 */     adouble = entitydragon.getMovementOffsets(11, this.partialTicks);
/*     */     
/* 224 */     for (int k = 0; k < 12; k++)
/*     */     {
/* 226 */       adouble2 = entitydragon.getMovementOffsets(12 + k, this.partialTicks);
/* 227 */       f10 = (float)(f10 + Math.sin(k * 0.45F + f8) * 0.05000000074505806D);
/* 228 */       this.spine.rotateAngleY = ((updateRotations(adouble2[0] - adouble[0]) * f5 + 180.0F) * 3.1415927F / 180.0F);
/* 229 */       this.spine.rotateAngleX = (f10 + (float)(adouble2[1] - adouble[1]) * 3.1415927F / 180.0F * f5 * 5.0F);
/* 230 */       this.spine.rotateAngleZ = (updateRotations(adouble2[0] - f7) * 3.1415927F / 180.0F * f5);
/* 231 */       this.spine.rotationPointY = f2;
/* 232 */       this.spine.rotationPointZ = f3;
/* 233 */       this.spine.rotationPointX = f4;
/* 234 */       f2 = (float)(f2 + Math.sin(this.spine.rotateAngleX) * 10.0D);
/* 235 */       f3 = (float)(f3 - Math.cos(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 236 */       f4 = (float)(f4 - Math.sin(this.spine.rotateAngleY) * Math.cos(this.spine.rotateAngleX) * 10.0D);
/* 237 */       this.spine.render(scale);
/*     */     }
/*     */     
/* 240 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private float updateRotations(double p_78214_1_)
/*     */   {
/* 250 */     while (p_78214_1_ >= 180.0D)
/*     */     {
/* 252 */       p_78214_1_ -= 360.0D;
/*     */     }
/*     */     
/* 255 */     while (p_78214_1_ < -180.0D)
/*     */     {
/* 257 */       p_78214_1_ += 360.0D;
/*     */     }
/*     */     
/* 260 */     return (float)p_78214_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */