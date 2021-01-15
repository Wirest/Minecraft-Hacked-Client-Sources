/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityCloudFX extends EntityFX
/*    */ {
/*    */   float field_70569_a;
/*    */   
/*    */   protected EntityCloudFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1221_8_, double p_i1221_10_, double p_i1221_12_)
/*    */   {
/* 15 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
/* 16 */     float f = 2.5F;
/* 17 */     this.motionX *= 0.10000000149011612D;
/* 18 */     this.motionY *= 0.10000000149011612D;
/* 19 */     this.motionZ *= 0.10000000149011612D;
/* 20 */     this.motionX += p_i1221_8_;
/* 21 */     this.motionY += p_i1221_10_;
/* 22 */     this.motionZ += p_i1221_12_;
/* 23 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.30000001192092896D));
/* 24 */     this.particleScale *= 0.75F;
/* 25 */     this.particleScale *= f;
/* 26 */     this.field_70569_a = this.particleScale;
/* 27 */     this.particleMaxAge = ((int)(8.0D / (Math.random() * 0.8D + 0.3D)));
/* 28 */     this.particleMaxAge = ((int)(this.particleMaxAge * f));
/* 29 */     this.noClip = false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 37 */     float f = (this.particleAge + partialTicks) / this.particleMaxAge * 32.0F;
/* 38 */     f = net.minecraft.util.MathHelper.clamp_float(f, 0.0F, 1.0F);
/* 39 */     this.particleScale = (this.field_70569_a * f);
/* 40 */     super.renderParticle(worldRendererIn, entityIn, partialTicks, p_180434_4_, p_180434_5_, p_180434_6_, p_180434_7_, p_180434_8_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onUpdate()
/*    */   {
/* 48 */     this.prevPosX = this.posX;
/* 49 */     this.prevPosY = this.posY;
/* 50 */     this.prevPosZ = this.posZ;
/*    */     
/* 52 */     if (this.particleAge++ >= this.particleMaxAge)
/*    */     {
/* 54 */       setDead();
/*    */     }
/*    */     
/* 57 */     setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
/* 58 */     moveEntity(this.motionX, this.motionY, this.motionZ);
/* 59 */     this.motionX *= 0.9599999785423279D;
/* 60 */     this.motionY *= 0.9599999785423279D;
/* 61 */     this.motionZ *= 0.9599999785423279D;
/* 62 */     EntityPlayer entityplayer = this.worldObj.getClosestPlayerToEntity(this, 2.0D);
/*    */     
/* 64 */     if ((entityplayer != null) && (this.posY > entityplayer.getEntityBoundingBox().minY))
/*    */     {
/* 66 */       this.posY += (entityplayer.getEntityBoundingBox().minY - this.posY) * 0.2D;
/* 67 */       this.motionY += (entityplayer.motionY - this.motionY) * 0.2D;
/* 68 */       setPosition(this.posX, this.posY, this.posZ);
/*    */     }
/*    */     
/* 71 */     if (this.onGround)
/*    */     {
/* 73 */       this.motionX *= 0.699999988079071D;
/* 74 */       this.motionZ *= 0.699999988079071D;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 82 */       return new EntityCloudFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityCloudFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */