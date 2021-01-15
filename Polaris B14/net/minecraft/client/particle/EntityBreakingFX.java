/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityBreakingFX extends EntityFX
/*    */ {
/*    */   protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item p_i1195_8_)
/*    */   {
/* 15 */     this(worldIn, posXIn, posYIn, posZIn, p_i1195_8_, 0);
/*    */   }
/*    */   
/*    */   protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, Item p_i1197_14_, int p_i1197_15_)
/*    */   {
/* 20 */     this(worldIn, posXIn, posYIn, posZIn, p_i1197_14_, p_i1197_15_);
/* 21 */     this.motionX *= 0.10000000149011612D;
/* 22 */     this.motionY *= 0.10000000149011612D;
/* 23 */     this.motionZ *= 0.10000000149011612D;
/* 24 */     this.motionX += xSpeedIn;
/* 25 */     this.motionY += ySpeedIn;
/* 26 */     this.motionZ += zSpeedIn;
/*    */   }
/*    */   
/*    */   protected EntityBreakingFX(World worldIn, double posXIn, double posYIn, double posZIn, Item p_i1196_8_, int p_i1196_9_)
/*    */   {
/* 31 */     super(worldIn, posXIn, posYIn, posZIn, 0.0D, 0.0D, 0.0D);
/* 32 */     setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i1196_8_, p_i1196_9_));
/* 33 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);
/* 34 */     this.particleGravity = net.minecraft.init.Blocks.snow.blockParticleGravity;
/* 35 */     this.particleScale /= 2.0F;
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 40 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 48 */     float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
/* 49 */     float f1 = f + 0.015609375F;
/* 50 */     float f2 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
/* 51 */     float f3 = f2 + 0.015609375F;
/* 52 */     float f4 = 0.1F * this.particleScale;
/*    */     
/* 54 */     if (this.particleIcon != null)
/*    */     {
/* 56 */       f = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0F * 16.0F);
/* 57 */       f1 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
/* 58 */       f2 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0F * 16.0F);
/* 59 */       f3 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
/*    */     }
/*    */     
/* 62 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 63 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 64 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 65 */     int i = getBrightnessForRender(partialTicks);
/* 66 */     int j = i >> 16 & 0xFFFF;
/* 67 */     int k = i & 0xFFFF;
/* 68 */     worldRendererIn.pos(f5 - p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 - p_180434_6_ * f4 - p_180434_8_ * f4).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 69 */     worldRendererIn.pos(f5 - p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 - p_180434_6_ * f4 + p_180434_8_ * f4).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 70 */     worldRendererIn.pos(f5 + p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 + p_180434_6_ * f4 + p_180434_8_ * f4).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 71 */     worldRendererIn.pos(f5 + p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 + p_180434_6_ * f4 - p_180434_8_ * f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 78 */       int i = p_178902_15_.length > 1 ? p_178902_15_[1] : 0;
/* 79 */       return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Item.getItemById(p_178902_15_[0]), i);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SlimeFactory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 87 */       return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.slime_ball);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class SnowballFactory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 95 */       return new EntityBreakingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, Items.snowball);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityBreakingFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */