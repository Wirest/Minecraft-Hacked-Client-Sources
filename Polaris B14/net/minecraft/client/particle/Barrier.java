/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class Barrier extends EntityFX
/*    */ {
/*    */   protected Barrier(World worldIn, double p_i46286_2_, double p_i46286_4_, double p_i46286_6_, Item p_i46286_8_)
/*    */   {
/* 14 */     super(worldIn, p_i46286_2_, p_i46286_4_, p_i46286_6_, 0.0D, 0.0D, 0.0D);
/* 15 */     setParticleIcon(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getParticleIcon(p_i46286_8_));
/* 16 */     this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);
/* 17 */     this.motionX = (this.motionY = this.motionZ = 0.0D);
/* 18 */     this.particleGravity = 0.0F;
/* 19 */     this.particleMaxAge = 80;
/*    */   }
/*    */   
/*    */   public int getFXLayer()
/*    */   {
/* 24 */     return 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*    */   {
/* 32 */     float f = this.particleIcon.getMinU();
/* 33 */     float f1 = this.particleIcon.getMaxU();
/* 34 */     float f2 = this.particleIcon.getMinV();
/* 35 */     float f3 = this.particleIcon.getMaxV();
/* 36 */     float f4 = 0.5F;
/* 37 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/* 38 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/* 39 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/* 40 */     int i = getBrightnessForRender(partialTicks);
/* 41 */     int j = i >> 16 & 0xFFFF;
/* 42 */     int k = i & 0xFFFF;
/* 43 */     worldRendererIn.pos(f5 - p_180434_4_ * 0.5F - p_180434_7_ * 0.5F, f6 - p_180434_5_ * 0.5F, f7 - p_180434_6_ * 0.5F - p_180434_8_ * 0.5F).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 44 */     worldRendererIn.pos(f5 - p_180434_4_ * 0.5F + p_180434_7_ * 0.5F, f6 + p_180434_5_ * 0.5F, f7 - p_180434_6_ * 0.5F + p_180434_8_ * 0.5F).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 45 */     worldRendererIn.pos(f5 + p_180434_4_ * 0.5F + p_180434_7_ * 0.5F, f6 + p_180434_5_ * 0.5F, f7 + p_180434_6_ * 0.5F + p_180434_8_ * 0.5F).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/* 46 */     worldRendererIn.pos(f5 + p_180434_4_ * 0.5F - p_180434_7_ * 0.5F, f6 - p_180434_5_ * 0.5F, f7 + p_180434_6_ * 0.5F - p_180434_8_ * 0.5F).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*    */   }
/*    */   
/*    */   public static class Factory implements IParticleFactory
/*    */   {
/*    */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*    */     {
/* 53 */       return new Barrier(worldIn, xCoordIn, yCoordIn, zCoordIn, Item.getItemFromBlock(net.minecraft.init.Blocks.barrier));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\Barrier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */