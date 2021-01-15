/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityDiggingFX extends EntityFX
/*     */ {
/*     */   private IBlockState field_174847_a;
/*     */   private BlockPos field_181019_az;
/*     */   
/*     */   protected EntityDiggingFX(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, IBlockState state)
/*     */   {
/*  19 */     super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
/*  20 */     this.field_174847_a = state;
/*  21 */     setParticleIcon(Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state));
/*  22 */     this.particleGravity = state.getBlock().blockParticleGravity;
/*  23 */     this.particleRed = (this.particleGreen = this.particleBlue = 0.6F);
/*  24 */     this.particleScale /= 2.0F;
/*     */   }
/*     */   
/*     */   public EntityDiggingFX func_174846_a(BlockPos pos)
/*     */   {
/*  29 */     this.field_181019_az = pos;
/*     */     
/*  31 */     if (this.field_174847_a.getBlock() == net.minecraft.init.Blocks.grass)
/*     */     {
/*  33 */       return this;
/*     */     }
/*     */     
/*     */ 
/*  37 */     int i = this.field_174847_a.getBlock().colorMultiplier(this.worldObj, pos);
/*  38 */     this.particleRed *= (i >> 16 & 0xFF) / 255.0F;
/*  39 */     this.particleGreen *= (i >> 8 & 0xFF) / 255.0F;
/*  40 */     this.particleBlue *= (i & 0xFF) / 255.0F;
/*  41 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public EntityDiggingFX func_174845_l()
/*     */   {
/*  47 */     this.field_181019_az = new BlockPos(this.posX, this.posY, this.posZ);
/*  48 */     Block block = this.field_174847_a.getBlock();
/*     */     
/*  50 */     if (block == net.minecraft.init.Blocks.grass)
/*     */     {
/*  52 */       return this;
/*     */     }
/*     */     
/*     */ 
/*  56 */     int i = block.getRenderColor(this.field_174847_a);
/*  57 */     this.particleRed *= (i >> 16 & 0xFF) / 255.0F;
/*  58 */     this.particleGreen *= (i >> 8 & 0xFF) / 255.0F;
/*  59 */     this.particleBlue *= (i & 0xFF) / 255.0F;
/*  60 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getFXLayer()
/*     */   {
/*  66 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float p_180434_4_, float p_180434_5_, float p_180434_6_, float p_180434_7_, float p_180434_8_)
/*     */   {
/*  74 */     float f = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
/*  75 */     float f1 = f + 0.015609375F;
/*  76 */     float f2 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
/*  77 */     float f3 = f2 + 0.015609375F;
/*  78 */     float f4 = 0.1F * this.particleScale;
/*     */     
/*  80 */     if (this.particleIcon != null)
/*     */     {
/*  82 */       f = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0F * 16.0F);
/*  83 */       f1 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
/*  84 */       f2 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0F * 16.0F);
/*  85 */       f3 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
/*     */     }
/*     */     
/*  88 */     float f5 = (float)(this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
/*  89 */     float f6 = (float)(this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
/*  90 */     float f7 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);
/*  91 */     int i = getBrightnessForRender(partialTicks);
/*  92 */     int j = i >> 16 & 0xFFFF;
/*  93 */     int k = i & 0xFFFF;
/*  94 */     worldRendererIn.pos(f5 - p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 - p_180434_6_ * f4 - p_180434_8_ * f4).tex(f, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*  95 */     worldRendererIn.pos(f5 - p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 - p_180434_6_ * f4 + p_180434_8_ * f4).tex(f, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*  96 */     worldRendererIn.pos(f5 + p_180434_4_ * f4 + p_180434_7_ * f4, f6 + p_180434_5_ * f4, f7 + p_180434_6_ * f4 + p_180434_8_ * f4).tex(f1, f2).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*  97 */     worldRendererIn.pos(f5 + p_180434_4_ * f4 - p_180434_7_ * f4, f6 - p_180434_5_ * f4, f7 + p_180434_6_ * f4 - p_180434_8_ * f4).tex(f1, f3).color(this.particleRed, this.particleGreen, this.particleBlue, 1.0F).lightmap(j, k).endVertex();
/*     */   }
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks)
/*     */   {
/* 102 */     int i = super.getBrightnessForRender(partialTicks);
/* 103 */     int j = 0;
/*     */     
/* 105 */     if (this.worldObj.isBlockLoaded(this.field_181019_az))
/*     */     {
/* 107 */       j = this.worldObj.getCombinedLight(this.field_181019_az, 0);
/*     */     }
/*     */     
/* 110 */     return i == 0 ? j : i;
/*     */   }
/*     */   
/*     */   public static class Factory implements IParticleFactory
/*     */   {
/*     */     public EntityFX getEntityFX(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... p_178902_15_)
/*     */     {
/* 117 */       return new EntityDiggingFX(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, Block.getStateById(p_178902_15_[0])).func_174845_l();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EntityDiggingFX.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */