/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
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
/*     */ public class ModelBiped
/*     */   extends ModelBase
/*     */ {
/*     */   public ModelRenderer bipedHead;
/*     */   public ModelRenderer bipedHeadwear;
/*     */   public ModelRenderer bipedBody;
/*     */   public ModelRenderer bipedRightArm;
/*     */   public ModelRenderer bipedLeftArm;
/*     */   public ModelRenderer bipedRightLeg;
/*     */   public ModelRenderer bipedLeftLeg;
/*     */   public int heldItemLeft;
/*     */   public int heldItemRight;
/*     */   public boolean isSneak;
/*     */   public boolean aimedBow;
/*     */   
/*     */   public ModelBiped()
/*     */   {
/*  47 */     this(0.0F);
/*     */   }
/*     */   
/*     */   public ModelBiped(float modelSize)
/*     */   {
/*  52 */     this(modelSize, 0.0F, 64, 32);
/*     */   }
/*     */   
/*     */   public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn)
/*     */   {
/*  57 */     this.textureWidth = textureWidthIn;
/*  58 */     this.textureHeight = textureHeightIn;
/*  59 */     this.bipedHead = new ModelRenderer(this, 0, 0);
/*  60 */     this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
/*  61 */     this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
/*  62 */     this.bipedHeadwear = new ModelRenderer(this, 32, 0);
/*  63 */     this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
/*  64 */     this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
/*  65 */     this.bipedBody = new ModelRenderer(this, 16, 16);
/*  66 */     this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
/*  67 */     this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
/*  68 */     this.bipedRightArm = new ModelRenderer(this, 40, 16);
/*  69 */     this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
/*  70 */     this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
/*  71 */     this.bipedLeftArm = new ModelRenderer(this, 40, 16);
/*  72 */     this.bipedLeftArm.mirror = true;
/*  73 */     this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
/*  74 */     this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
/*  75 */     this.bipedRightLeg = new ModelRenderer(this, 0, 16);
/*  76 */     this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
/*  77 */     this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
/*  78 */     this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
/*  79 */     this.bipedLeftLeg.mirror = true;
/*  80 */     this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
/*  81 */     this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
/*     */   {
/*  89 */     setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
/*  90 */     GlStateManager.pushMatrix();
/*     */     
/*  92 */     if (this.isChild)
/*     */     {
/*  94 */       float f = 2.0F;
/*  95 */       GlStateManager.scale(1.5F / f, 1.5F / f, 1.5F / f);
/*  96 */       GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
/*  97 */       this.bipedHead.render(scale);
/*  98 */       GlStateManager.popMatrix();
/*  99 */       GlStateManager.pushMatrix();
/* 100 */       GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
/* 101 */       GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
/* 102 */       this.bipedBody.render(scale);
/* 103 */       this.bipedRightArm.render(scale);
/* 104 */       this.bipedLeftArm.render(scale);
/* 105 */       this.bipedRightLeg.render(scale);
/* 106 */       this.bipedLeftLeg.render(scale);
/* 107 */       this.bipedHeadwear.render(scale);
/*     */     }
/*     */     else
/*     */     {
/* 111 */       if (entityIn.isSneaking())
/*     */       {
/* 113 */         GlStateManager.translate(0.0F, 0.2F, 0.0F);
/*     */       }
/*     */       
/* 116 */       this.bipedHead.render(scale);
/* 117 */       this.bipedBody.render(scale);
/* 118 */       this.bipedRightArm.render(scale);
/* 119 */       this.bipedLeftArm.render(scale);
/* 120 */       this.bipedRightLeg.render(scale);
/* 121 */       this.bipedLeftLeg.render(scale);
/* 122 */       this.bipedHeadwear.render(scale);
/*     */     }
/*     */     
/* 125 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity entityIn)
/*     */   {
/* 135 */     this.bipedHead.rotateAngleY = (p_78087_4_ / 57.295776F);
/* 136 */     this.bipedHead.rotateAngleX = (p_78087_5_ / 57.295776F);
/* 137 */     this.bipedRightArm.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 2.0F * p_78087_2_ * 0.5F);
/* 138 */     this.bipedLeftArm.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F);
/* 139 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/* 140 */     this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 141 */     this.bipedRightLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_);
/* 142 */     this.bipedLeftLeg.rotateAngleX = (MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_);
/* 143 */     this.bipedRightLeg.rotateAngleY = 0.0F;
/* 144 */     this.bipedLeftLeg.rotateAngleY = 0.0F;
/*     */     
/* 146 */     if (this.isRiding)
/*     */     {
/* 148 */       this.bipedRightArm.rotateAngleX += -0.62831855F;
/* 149 */       this.bipedLeftArm.rotateAngleX += -0.62831855F;
/* 150 */       this.bipedRightLeg.rotateAngleX = -1.2566371F;
/* 151 */       this.bipedLeftLeg.rotateAngleX = -1.2566371F;
/* 152 */       this.bipedRightLeg.rotateAngleY = 0.31415927F;
/* 153 */       this.bipedLeftLeg.rotateAngleY = -0.31415927F;
/*     */     }
/*     */     
/* 156 */     if (this.heldItemLeft != 0)
/*     */     {
/* 158 */       this.bipedLeftArm.rotateAngleX = (this.bipedLeftArm.rotateAngleX * 0.5F - 0.31415927F * this.heldItemLeft);
/*     */     }
/*     */     
/* 161 */     this.bipedRightArm.rotateAngleY = 0.0F;
/* 162 */     this.bipedRightArm.rotateAngleZ = 0.0F;
/*     */     
/* 164 */     switch (this.heldItemRight)
/*     */     {
/*     */     case 0: 
/*     */     case 2: 
/*     */     default: 
/*     */       break;
/*     */     
/*     */     case 1: 
/* 172 */       this.bipedRightArm.rotateAngleX = (this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * this.heldItemRight);
/* 173 */       break;
/*     */     
/*     */     case 3: 
/* 176 */       this.bipedRightArm.rotateAngleX = (this.bipedRightArm.rotateAngleX * 0.5F - 0.31415927F * this.heldItemRight);
/* 177 */       this.bipedRightArm.rotateAngleY = -0.5235988F;
/*     */     }
/*     */     
/* 180 */     this.bipedLeftArm.rotateAngleY = 0.0F;
/*     */     
/* 182 */     if ((Polaris.instance.moduleManager.getModuleByName("Dab").isToggled()) && (Minecraft.getMinecraft().gameSettings.thirdPersonView > 0)) {
/* 183 */       boolean pressed = Keyboard.isKeyDown(29);
/* 184 */       boolean pressed2 = Keyboard.isKeyDown(157);
/* 185 */       if (pressed) {
/* 186 */         this.bipedHead.rotateAngleX = 0.5F;
/* 187 */         this.bipedHead.rotateAngleY = 0.75F;
/* 188 */         this.bipedRightArm.rotateAngleX = 4.75F;
/* 189 */         this.bipedRightArm.rotateAngleY = -1.0F;
/* 190 */         this.bipedLeftArm.rotateAngleX = 4.5F;
/* 191 */         this.bipedLeftArm.rotateAngleY = -1.25F;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 197 */     if (this.swingProgress > -9990.0F)
/*     */     {
/* 199 */       float f = this.swingProgress;
/* 200 */       this.bipedBody.rotateAngleY = (MathHelper.sin(MathHelper.sqrt_float(f) * 3.1415927F * 2.0F) * 0.2F);
/* 201 */       this.bipedRightArm.rotationPointZ = (MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F);
/* 202 */       this.bipedRightArm.rotationPointX = (-MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F);
/* 203 */       this.bipedLeftArm.rotationPointZ = (-MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F);
/* 204 */       this.bipedLeftArm.rotationPointX = (MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F);
/* 205 */       this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
/* 206 */       this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
/* 207 */       this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
/* 208 */       f = 1.0F - this.swingProgress;
/* 209 */       f *= f;
/* 210 */       f *= f;
/* 211 */       f = 1.0F - f;
/* 212 */       float f1 = MathHelper.sin(f * 3.1415927F);
/* 213 */       float f2 = MathHelper.sin(this.swingProgress * 3.1415927F) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
/* 214 */       this.bipedRightArm.rotateAngleX = ((float)(this.bipedRightArm.rotateAngleX - (f1 * 1.2D + f2)));
/* 215 */       this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
/* 216 */       this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * 3.1415927F) * -0.4F;
/*     */     }
/*     */     
/* 219 */     if (this.isSneak)
/*     */     {
/* 221 */       this.bipedBody.rotateAngleX = 0.5F;
/* 222 */       this.bipedRightArm.rotateAngleX += 0.4F;
/* 223 */       this.bipedLeftArm.rotateAngleX += 0.4F;
/* 224 */       this.bipedRightLeg.rotationPointZ = 4.0F;
/* 225 */       this.bipedLeftLeg.rotationPointZ = 4.0F;
/* 226 */       this.bipedRightLeg.rotationPointY = 9.0F;
/* 227 */       this.bipedLeftLeg.rotationPointY = 9.0F;
/* 228 */       this.bipedHead.rotationPointY = 1.0F;
/*     */     }
/*     */     else
/*     */     {
/* 232 */       this.bipedBody.rotateAngleX = 0.0F;
/* 233 */       this.bipedRightLeg.rotationPointZ = 0.1F;
/* 234 */       this.bipedLeftLeg.rotationPointZ = 0.1F;
/* 235 */       this.bipedRightLeg.rotationPointY = 12.0F;
/* 236 */       this.bipedLeftLeg.rotationPointY = 12.0F;
/* 237 */       this.bipedHead.rotationPointY = 0.0F;
/*     */     }
/*     */     
/* 240 */     this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 241 */     this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 242 */     this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/* 243 */     this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/*     */     
/* 245 */     if (this.aimedBow)
/*     */     {
/* 247 */       float f3 = 0.0F;
/* 248 */       float f4 = 0.0F;
/* 249 */       this.bipedRightArm.rotateAngleZ = 0.0F;
/* 250 */       this.bipedLeftArm.rotateAngleZ = 0.0F;
/* 251 */       this.bipedRightArm.rotateAngleY = (-(0.1F - f3 * 0.6F) + this.bipedHead.rotateAngleY);
/* 252 */       this.bipedLeftArm.rotateAngleY = (0.1F - f3 * 0.6F + this.bipedHead.rotateAngleY + 0.4F);
/* 253 */       this.bipedRightArm.rotateAngleX = (-1.5707964F + this.bipedHead.rotateAngleX);
/* 254 */       this.bipedLeftArm.rotateAngleX = (-1.5707964F + this.bipedHead.rotateAngleX);
/* 255 */       this.bipedRightArm.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
/* 256 */       this.bipedLeftArm.rotateAngleX -= f3 * 1.2F - f4 * 0.4F;
/* 257 */       this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 258 */       this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
/* 259 */       this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/* 260 */       this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
/*     */     }
/*     */     
/* 263 */     copyModelAngles(this.bipedHead, this.bipedHeadwear);
/*     */   }
/*     */   
/*     */   public void setModelAttributes(ModelBase model)
/*     */   {
/* 268 */     super.setModelAttributes(model);
/*     */     
/* 270 */     if ((model instanceof ModelBiped))
/*     */     {
/* 272 */       ModelBiped modelbiped = (ModelBiped)model;
/* 273 */       this.heldItemLeft = modelbiped.heldItemLeft;
/* 274 */       this.heldItemRight = modelbiped.heldItemRight;
/* 275 */       this.isSneak = modelbiped.isSneak;
/* 276 */       this.aimedBow = modelbiped.aimedBow;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setInvisible(boolean invisible)
/*     */   {
/* 282 */     this.bipedHead.showModel = invisible;
/* 283 */     this.bipedHeadwear.showModel = invisible;
/* 284 */     this.bipedBody.showModel = invisible;
/* 285 */     this.bipedRightArm.showModel = invisible;
/* 286 */     this.bipedLeftArm.showModel = invisible;
/* 287 */     this.bipedRightLeg.showModel = invisible;
/* 288 */     this.bipedLeftLeg.showModel = invisible;
/*     */   }
/*     */   
/*     */   public void postRenderArm(float scale)
/*     */   {
/* 293 */     this.bipedRightArm.postRender(scale);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\model\ModelBiped.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */