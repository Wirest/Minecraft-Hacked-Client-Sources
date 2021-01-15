/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityPickupFX extends EntityFX
/*    */ {
/*    */   private Entity field_174840_a;
/*    */   private Entity field_174843_ax;
/*    */   private int age;
/*    */   private int maxAge;
/*    */   private float field_174841_aA;
/* 18 */   private RenderManager field_174842_aB = Minecraft.getMinecraft().getRenderManager();
/*    */   
/*    */   public EntityPickupFX(World worldIn, Entity p_i1233_2_, Entity p_i1233_3_, float p_i1233_4_)
/*    */   {
/* 22 */     super(worldIn, p_i1233_2_.posX, p_i1233_2_.posY, p_i1233_2_.posZ, p_i1233_2_.motionX, p_i1233_2_.motionY, p_i1233_2_.motionZ);
/* 23 */     this.field_174840_a = p_i1233_2_;
/* 24 */     this.field_174843_ax = p_i1233_3_;
/* 25 */     this.maxAge = 3;
/* 26 */     this.field_174841_aA = p_i1233_4_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 34 */     float f = (this.age + partialTicks) / this.maxAge;
/* 35 */     f *= f;
/* 36 */     double d0 = this.field_174840_a.posX;
/* 37 */     double d1 = this.field_174840_a.posY;
/* 38 */     double d2 = this.field_174840_a.posZ;
/* 39 */     double d3 = this.field_174843_ax.lastTickPosX + (this.field_174843_ax.posX - this.field_174843_ax.lastTickPosX) * partialTicks;
/* 40 */     double d4 = this.field_174843_ax.lastTickPosY + (this.field_174843_ax.posY - this.field_174843_ax.lastTickPosY) * partialTicks + this.field_174841_aA;
/* 41 */     double d5 = this.field_174843_ax.lastTickPosZ + (this.field_174843_ax.posZ - this.field_174843_ax.lastTickPosZ) * partialTicks;
/* 42 */     double d6 = d0 + (d3 - d0) * f;
/* 43 */     double d7 = d1 + (d4 - d1) * f;
/* 44 */     double d8 = d2 + (d5 - d2) * f;
/* 45 */     int i = getBrightnessForRender(partialTicks);
/* 46 */     int j = i % 65536;
/* 47 */     int k = i / 65536;
/* 48 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 49 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 50 */     d6 -= interpPosX;
/* 51 */     d7 -= interpPosY;
/* 52 */     d8 -= interpPosZ;
/* 53 */     this.field_174842_aB.renderEntityWithPosYaw(this.field_174840_a, (float)d6, (float)d7, (float)d8, this.field_174840_a.rotationYaw, partialTicks);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 61 */     this.age += 1;
/*    */     
/* 63 */     if (this.age == this.maxAge)
/*    */     {
/* 65 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 71 */     return 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityPickupFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */