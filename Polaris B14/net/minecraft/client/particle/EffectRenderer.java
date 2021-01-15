/*     */ package net.minecraft.client.particle;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.concurrent.Callable;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.renderer.ActiveRenderInfo;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.texture.TextureMap;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import optfine.Reflector;
/*     */ import optfine.ReflectorMethod;
/*     */ 
/*     */ public class EffectRenderer
/*     */ {
/*  36 */   private static final ResourceLocation particleTextures = new ResourceLocation("textures/particle/particles.png");
/*     */   
/*     */   protected World worldObj;
/*     */   
/*  40 */   private List[][] fxLayers = new List[4][];
/*  41 */   private List particleEmitters = Lists.newArrayList();
/*     */   
/*     */   private TextureManager renderer;
/*     */   
/*  45 */   private Random rand = new Random();
/*  46 */   private Map particleTypes = Maps.newHashMap();
/*     */   private static final String __OBFID = "CL_00000915";
/*     */   
/*     */   public EffectRenderer(World worldIn, TextureManager rendererIn)
/*     */   {
/*  51 */     this.worldObj = worldIn;
/*  52 */     this.renderer = rendererIn;
/*     */     
/*  54 */     for (int i = 0; i < 4; i++)
/*     */     {
/*  56 */       this.fxLayers[i] = new List[2];
/*     */       
/*  58 */       for (int j = 0; j < 2; j++)
/*     */       {
/*  60 */         this.fxLayers[i][j] = Lists.newArrayList();
/*     */       }
/*     */     }
/*     */     
/*  64 */     registerVanillaParticles();
/*     */   }
/*     */   
/*     */   private void registerVanillaParticles()
/*     */   {
/*  69 */     registerParticle(EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(), new EntityExplodeFX.Factory());
/*  70 */     registerParticle(EnumParticleTypes.WATER_BUBBLE.getParticleID(), new EntityBubbleFX.Factory());
/*  71 */     registerParticle(EnumParticleTypes.WATER_SPLASH.getParticleID(), new EntitySplashFX.Factory());
/*  72 */     registerParticle(EnumParticleTypes.WATER_WAKE.getParticleID(), new EntityFishWakeFX.Factory());
/*  73 */     registerParticle(EnumParticleTypes.WATER_DROP.getParticleID(), new EntityRainFX.Factory());
/*  74 */     registerParticle(EnumParticleTypes.SUSPENDED.getParticleID(), new EntitySuspendFX.Factory());
/*  75 */     registerParticle(EnumParticleTypes.SUSPENDED_DEPTH.getParticleID(), new EntityAuraFX.Factory());
/*  76 */     registerParticle(EnumParticleTypes.CRIT.getParticleID(), new EntityCrit2FX.Factory());
/*  77 */     registerParticle(EnumParticleTypes.CRIT_MAGIC.getParticleID(), new EntityCrit2FX.MagicFactory());
/*  78 */     registerParticle(EnumParticleTypes.SMOKE_NORMAL.getParticleID(), new EntitySmokeFX.Factory());
/*  79 */     registerParticle(EnumParticleTypes.SMOKE_LARGE.getParticleID(), new EntityCritFX.Factory());
/*  80 */     registerParticle(EnumParticleTypes.SPELL.getParticleID(), new EntitySpellParticleFX.Factory());
/*  81 */     registerParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), new EntitySpellParticleFX.InstantFactory());
/*  82 */     registerParticle(EnumParticleTypes.SPELL_MOB.getParticleID(), new EntitySpellParticleFX.MobFactory());
/*  83 */     registerParticle(EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(), new EntitySpellParticleFX.AmbientMobFactory());
/*  84 */     registerParticle(EnumParticleTypes.SPELL_WITCH.getParticleID(), new EntitySpellParticleFX.WitchFactory());
/*  85 */     registerParticle(EnumParticleTypes.DRIP_WATER.getParticleID(), new EntityDropParticleFX.WaterFactory());
/*  86 */     registerParticle(EnumParticleTypes.DRIP_LAVA.getParticleID(), new EntityDropParticleFX.LavaFactory());
/*  87 */     registerParticle(EnumParticleTypes.VILLAGER_ANGRY.getParticleID(), new EntityHeartFX.AngryVillagerFactory());
/*  88 */     registerParticle(EnumParticleTypes.VILLAGER_HAPPY.getParticleID(), new EntityAuraFX.HappyVillagerFactory());
/*  89 */     registerParticle(EnumParticleTypes.TOWN_AURA.getParticleID(), new EntityAuraFX.Factory());
/*  90 */     registerParticle(EnumParticleTypes.NOTE.getParticleID(), new EntityNoteFX.Factory());
/*  91 */     registerParticle(EnumParticleTypes.PORTAL.getParticleID(), new EntityPortalFX.Factory());
/*  92 */     registerParticle(EnumParticleTypes.ENCHANTMENT_TABLE.getParticleID(), new EntityEnchantmentTableParticleFX.EnchantmentTable());
/*  93 */     registerParticle(EnumParticleTypes.FLAME.getParticleID(), new EntityFlameFX.Factory());
/*  94 */     registerParticle(EnumParticleTypes.LAVA.getParticleID(), new EntityLavaFX.Factory());
/*  95 */     registerParticle(EnumParticleTypes.FOOTSTEP.getParticleID(), new EntityFootStepFX.Factory());
/*  96 */     registerParticle(EnumParticleTypes.CLOUD.getParticleID(), new EntityCloudFX.Factory());
/*  97 */     registerParticle(EnumParticleTypes.REDSTONE.getParticleID(), new EntityReddustFX.Factory());
/*  98 */     registerParticle(EnumParticleTypes.SNOWBALL.getParticleID(), new EntityBreakingFX.SnowballFactory());
/*  99 */     registerParticle(EnumParticleTypes.SNOW_SHOVEL.getParticleID(), new EntitySnowShovelFX.Factory());
/* 100 */     registerParticle(EnumParticleTypes.SLIME.getParticleID(), new EntityBreakingFX.SlimeFactory());
/* 101 */     registerParticle(EnumParticleTypes.HEART.getParticleID(), new EntityHeartFX.Factory());
/* 102 */     registerParticle(EnumParticleTypes.BARRIER.getParticleID(), new Barrier.Factory());
/* 103 */     registerParticle(EnumParticleTypes.ITEM_CRACK.getParticleID(), new EntityBreakingFX.Factory());
/* 104 */     registerParticle(EnumParticleTypes.BLOCK_CRACK.getParticleID(), new EntityDiggingFX.Factory());
/* 105 */     registerParticle(EnumParticleTypes.BLOCK_DUST.getParticleID(), new EntityBlockDustFX.Factory());
/* 106 */     registerParticle(EnumParticleTypes.EXPLOSION_HUGE.getParticleID(), new EntityHugeExplodeFX.Factory());
/* 107 */     registerParticle(EnumParticleTypes.EXPLOSION_LARGE.getParticleID(), new EntityLargeExplodeFX.Factory());
/* 108 */     registerParticle(EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), new EntityFirework.Factory());
/* 109 */     registerParticle(EnumParticleTypes.MOB_APPEARANCE.getParticleID(), new MobAppearance.Factory());
/*     */   }
/*     */   
/*     */   public void registerParticle(int id, IParticleFactory particleFactory)
/*     */   {
/* 114 */     this.particleTypes.put(Integer.valueOf(id), particleFactory);
/*     */   }
/*     */   
/*     */   public void emitParticleAtEntity(Entity entityIn, EnumParticleTypes particleTypes)
/*     */   {
/* 119 */     this.particleEmitters.add(new EntityParticleEmitter(this.worldObj, entityIn, particleTypes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityFX spawnEffectParticle(int particleId, double p_178927_2_, double p_178927_4_, double p_178927_6_, double p_178927_8_, double p_178927_10_, double p_178927_12_, int... p_178927_14_)
/*     */   {
/* 127 */     IParticleFactory iparticlefactory = (IParticleFactory)this.particleTypes.get(Integer.valueOf(particleId));
/*     */     
/* 129 */     if (iparticlefactory != null)
/*     */     {
/* 131 */       EntityFX entityfx = iparticlefactory.getEntityFX(particleId, this.worldObj, p_178927_2_, p_178927_4_, p_178927_6_, p_178927_8_, p_178927_10_, p_178927_12_, p_178927_14_);
/*     */       
/* 133 */       if (entityfx != null)
/*     */       {
/* 135 */         addEffect(entityfx);
/* 136 */         return entityfx;
/*     */       }
/*     */     }
/*     */     
/* 140 */     return null;
/*     */   }
/*     */   
/*     */   public void addEffect(EntityFX effect)
/*     */   {
/* 145 */     if (effect != null)
/*     */     {
/* 147 */       if ((!(effect instanceof EntityFirework.SparkFX)) || (optfine.Config.isFireworkParticles()))
/*     */       {
/* 149 */         int i = effect.getFXLayer();
/* 150 */         int j = effect.getAlpha() != 1.0F ? 0 : 1;
/*     */         
/* 152 */         if (this.fxLayers[i][j].size() >= 4000)
/*     */         {
/* 154 */           this.fxLayers[i][j].remove(0);
/*     */         }
/*     */         
/* 157 */         this.fxLayers[i][j].add(effect);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateEffects()
/*     */   {
/* 164 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 166 */       updateEffectLayer(i);
/*     */     }
/*     */     
/* 169 */     ArrayList arraylist = Lists.newArrayList();
/*     */     
/* 171 */     for (Object entityparticleemitter0 : this.particleEmitters)
/*     */     {
/* 173 */       EntityParticleEmitter entityparticleemitter = (EntityParticleEmitter)entityparticleemitter0;
/* 174 */       entityparticleemitter.onUpdate();
/*     */       
/* 176 */       if (entityparticleemitter.isDead)
/*     */       {
/* 178 */         arraylist.add(entityparticleemitter);
/*     */       }
/*     */     }
/*     */     
/* 182 */     this.particleEmitters.removeAll(arraylist);
/*     */   }
/*     */   
/*     */   private void updateEffectLayer(int p_178922_1_)
/*     */   {
/* 187 */     for (int i = 0; i < 2; i++)
/*     */     {
/* 189 */       updateEffectAlphaLayer(this.fxLayers[p_178922_1_][i]);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateEffectAlphaLayer(List p_178925_1_)
/*     */   {
/* 195 */     ArrayList arraylist = Lists.newArrayList();
/*     */     
/* 197 */     for (int i = 0; i < p_178925_1_.size(); i++)
/*     */     {
/* 199 */       EntityFX entityfx = (EntityFX)p_178925_1_.get(i);
/* 200 */       tickParticle(entityfx);
/*     */       
/* 202 */       if (entityfx.isDead)
/*     */       {
/* 204 */         arraylist.add(entityfx);
/*     */       }
/*     */     }
/*     */     
/* 208 */     p_178925_1_.removeAll(arraylist);
/*     */   }
/*     */   
/*     */   private void tickParticle(final EntityFX p_178923_1_)
/*     */   {
/*     */     try
/*     */     {
/* 215 */       p_178923_1_.onUpdate();
/*     */     }
/*     */     catch (Throwable throwable)
/*     */     {
/* 219 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking Particle");
/* 220 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being ticked");
/* 221 */       final int i = p_178923_1_.getFXLayer();
/* 222 */       crashreportcategory.addCrashSectionCallable("Particle", new Callable()
/*     */       {
/*     */         private static final String __OBFID = "CL_00000916";
/*     */         
/*     */         public String call() throws Exception {
/* 227 */           return p_178923_1_.toString();
/*     */         }
/* 229 */       });
/* 230 */       crashreportcategory.addCrashSectionCallable("Particle Type", new Callable()
/*     */       {
/*     */         private static final String __OBFID = "CL_00000917";
/*     */         
/*     */         public String call() throws Exception {
/* 235 */           return "Unknown - " + i;
/*     */         }
/* 237 */       });
/* 238 */       throw new ReportedException(crashreport);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderParticles(Entity entityIn, float partialTicks)
/*     */   {
/* 247 */     float f = ActiveRenderInfo.getRotationX();
/* 248 */     float f1 = ActiveRenderInfo.getRotationZ();
/* 249 */     float f2 = ActiveRenderInfo.getRotationYZ();
/* 250 */     float f3 = ActiveRenderInfo.getRotationXY();
/* 251 */     float f4 = ActiveRenderInfo.getRotationXZ();
/* 252 */     EntityFX.interpPosX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 253 */     EntityFX.interpPosY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 254 */     EntityFX.interpPosZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 255 */     GlStateManager.enableBlend();
/* 256 */     GlStateManager.blendFunc(770, 771);
/* 257 */     GlStateManager.alphaFunc(516, 0.003921569F);
/*     */     
/* 259 */     for (int i = 0; i < 3; i++)
/*     */     {
/* 261 */       final int j = i;
/*     */       
/* 263 */       for (int k = 0; k < 2; k++)
/*     */       {
/* 265 */         if (!this.fxLayers[j][k].isEmpty())
/*     */         {
/* 267 */           switch (k)
/*     */           {
/*     */           case 0: 
/* 270 */             GlStateManager.depthMask(false);
/* 271 */             break;
/*     */           
/*     */           case 1: 
/* 274 */             GlStateManager.depthMask(true);
/*     */           }
/*     */           
/* 277 */           switch (j)
/*     */           {
/*     */           case 0: 
/*     */           default: 
/* 281 */             this.renderer.bindTexture(particleTextures);
/* 282 */             break;
/*     */           
/*     */           case 1: 
/* 285 */             this.renderer.bindTexture(TextureMap.locationBlocksTexture);
/*     */           }
/*     */           
/* 288 */           GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 289 */           Tessellator tessellator = Tessellator.getInstance();
/* 290 */           WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 291 */           worldrenderer.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
/*     */           
/* 293 */           for (int l = 0; l < this.fxLayers[j][k].size(); l++)
/*     */           {
/* 295 */             final EntityFX entityfx = (EntityFX)this.fxLayers[j][k].get(l);
/*     */             
/*     */             try
/*     */             {
/* 299 */               entityfx.renderParticle(worldrenderer, entityIn, partialTicks, f, f4, f1, f2, f3);
/*     */             }
/*     */             catch (Throwable throwable)
/*     */             {
/* 303 */               CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Particle");
/* 304 */               CrashReportCategory crashreportcategory = crashreport.makeCategory("Particle being rendered");
/* 305 */               crashreportcategory.addCrashSectionCallable("Particle", new Callable()
/*     */               {
/*     */                 private static final String __OBFID = "CL_00000918";
/*     */                 
/*     */                 public String call() throws Exception {
/* 310 */                   return entityfx.toString();
/*     */                 }
/* 312 */               });
/* 313 */               crashreportcategory.addCrashSectionCallable("Particle Type", new Callable()
/*     */               {
/*     */                 private static final String __OBFID = "CL_00000919";
/*     */                 
/*     */                 public String call() throws Exception {
/* 318 */                   return "Unknown - " + j;
/*     */                 }
/* 320 */               });
/* 321 */               throw new ReportedException(crashreport);
/*     */             }
/*     */           }
/*     */           
/* 325 */           tessellator.draw();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 330 */     GlStateManager.depthMask(true);
/* 331 */     GlStateManager.disableBlend();
/* 332 */     GlStateManager.alphaFunc(516, 0.1F);
/*     */   }
/*     */   
/*     */   public void renderLitParticles(Entity entityIn, float p_78872_2_)
/*     */   {
/* 337 */     float f = 0.017453292F;
/* 338 */     float f1 = MathHelper.cos(entityIn.rotationYaw * 0.017453292F);
/* 339 */     float f2 = MathHelper.sin(entityIn.rotationYaw * 0.017453292F);
/* 340 */     float f3 = -f2 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
/* 341 */     float f4 = f1 * MathHelper.sin(entityIn.rotationPitch * 0.017453292F);
/* 342 */     float f5 = MathHelper.cos(entityIn.rotationPitch * 0.017453292F);
/*     */     
/* 344 */     for (int i = 0; i < 2; i++)
/*     */     {
/* 346 */       List list = this.fxLayers[3][i];
/*     */       
/* 348 */       if (!list.isEmpty())
/*     */       {
/* 350 */         Tessellator tessellator = Tessellator.getInstance();
/* 351 */         WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/*     */         
/* 353 */         for (int j = 0; j < list.size(); j++)
/*     */         {
/* 355 */           EntityFX entityfx = (EntityFX)list.get(j);
/* 356 */           entityfx.renderParticle(worldrenderer, entityIn, p_78872_2_, f1, f5, f2, f3, f4);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clearEffects(World worldIn)
/*     */   {
/* 364 */     this.worldObj = worldIn;
/*     */     
/* 366 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 368 */       for (int j = 0; j < 2; j++)
/*     */       {
/* 370 */         this.fxLayers[i][j].clear();
/*     */       }
/*     */     }
/*     */     
/* 374 */     this.particleEmitters.clear();
/*     */   }
/*     */   
/*     */   public void addBlockDestroyEffects(BlockPos pos, IBlockState state)
/*     */   {
/*     */     boolean flag;
/*     */     boolean flag;
/* 381 */     if ((Reflector.ForgeBlock_addDestroyEffects.exists()) && (Reflector.ForgeBlock_isAir.exists()))
/*     */     {
/* 383 */       Block block = state.getBlock();
/* 384 */       Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, new Object[] { this.worldObj, pos });
/* 385 */       if (!Reflector.callBoolean(block, Reflector.ForgeBlock_isAir, new Object[] { this.worldObj, pos })) {} flag = !Reflector.callBoolean(block, Reflector.ForgeBlock_addDestroyEffects, new Object[] { this.worldObj, pos, this });
/*     */     }
/*     */     else
/*     */     {
/* 389 */       flag = state.getBlock().getMaterial() != Material.air;
/*     */     }
/*     */     
/* 392 */     if (flag)
/*     */     {
/* 394 */       state = state.getBlock().getActualState(state, this.worldObj, pos);
/* 395 */       byte b0 = 4;
/*     */       
/* 397 */       for (int i = 0; i < b0; i++)
/*     */       {
/* 399 */         for (int j = 0; j < b0; j++)
/*     */         {
/* 401 */           for (int k = 0; k < b0; k++)
/*     */           {
/* 403 */             double d0 = pos.getX() + (i + 0.5D) / b0;
/* 404 */             double d1 = pos.getY() + (j + 0.5D) / b0;
/* 405 */             double d2 = pos.getZ() + (k + 0.5D) / b0;
/* 406 */             addEffect(new EntityDiggingFX(this.worldObj, d0, d1, d2, d0 - pos.getX() - 0.5D, d1 - pos.getY() - 0.5D, d2 - pos.getZ() - 0.5D, state).func_174846_a(pos));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBlockHitEffects(BlockPos pos, EnumFacing side)
/*     */   {
/* 418 */     IBlockState iblockstate = this.worldObj.getBlockState(pos);
/* 419 */     Block block = iblockstate.getBlock();
/*     */     
/* 421 */     if (block.getRenderType() != -1)
/*     */     {
/* 423 */       int i = pos.getX();
/* 424 */       int j = pos.getY();
/* 425 */       int k = pos.getZ();
/* 426 */       float f = 0.1F;
/* 427 */       double d0 = i + this.rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - f * 2.0F) + f + block.getBlockBoundsMinX();
/* 428 */       double d1 = j + this.rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - f * 2.0F) + f + block.getBlockBoundsMinY();
/* 429 */       double d2 = k + this.rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - f * 2.0F) + f + block.getBlockBoundsMinZ();
/*     */       
/* 431 */       if (side == EnumFacing.DOWN)
/*     */       {
/* 433 */         d1 = j + block.getBlockBoundsMinY() - f;
/*     */       }
/*     */       
/* 436 */       if (side == EnumFacing.UP)
/*     */       {
/* 438 */         d1 = j + block.getBlockBoundsMaxY() + f;
/*     */       }
/*     */       
/* 441 */       if (side == EnumFacing.NORTH)
/*     */       {
/* 443 */         d2 = k + block.getBlockBoundsMinZ() - f;
/*     */       }
/*     */       
/* 446 */       if (side == EnumFacing.SOUTH)
/*     */       {
/* 448 */         d2 = k + block.getBlockBoundsMaxZ() + f;
/*     */       }
/*     */       
/* 451 */       if (side == EnumFacing.WEST)
/*     */       {
/* 453 */         d0 = i + block.getBlockBoundsMinX() - f;
/*     */       }
/*     */       
/* 456 */       if (side == EnumFacing.EAST)
/*     */       {
/* 458 */         d0 = i + block.getBlockBoundsMaxX() + f;
/*     */       }
/*     */       
/* 461 */       addEffect(new EntityDiggingFX(this.worldObj, d0, d1, d2, 0.0D, 0.0D, 0.0D, iblockstate).func_174846_a(pos).multiplyVelocity(0.2F).multipleParticleScaleBy(0.6F));
/*     */     }
/*     */   }
/*     */   
/*     */   public void moveToAlphaLayer(EntityFX effect)
/*     */   {
/* 467 */     moveToLayer(effect, 1, 0);
/*     */   }
/*     */   
/*     */   public void moveToNoAlphaLayer(EntityFX effect)
/*     */   {
/* 472 */     moveToLayer(effect, 0, 1);
/*     */   }
/*     */   
/*     */   private void moveToLayer(EntityFX effect, int p_178924_2_, int p_178924_3_)
/*     */   {
/* 477 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 479 */       if (this.fxLayers[i][p_178924_2_].contains(effect))
/*     */       {
/* 481 */         this.fxLayers[i][p_178924_2_].remove(effect);
/* 482 */         this.fxLayers[i][p_178924_3_].add(effect);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String getStatistics()
/*     */   {
/* 489 */     int i = 0;
/*     */     
/* 491 */     for (int j = 0; j < 4; j++)
/*     */     {
/* 493 */       for (int k = 0; k < 2; k++)
/*     */       {
/* 495 */         i += this.fxLayers[j][k].size();
/*     */       }
/*     */     }
/*     */     
/* 499 */     return i;
/*     */   }
/*     */   
/*     */   public void addBlockHitEffects(BlockPos p_addBlockHitEffects_1_, MovingObjectPosition p_addBlockHitEffects_2_)
/*     */   {
/* 504 */     Block block = this.worldObj.getBlockState(p_addBlockHitEffects_1_).getBlock();
/* 505 */     boolean flag = Reflector.callBoolean(block, Reflector.ForgeBlock_addHitEffects, new Object[] { this.worldObj, p_addBlockHitEffects_2_, this });
/*     */     
/* 507 */     if ((block != null) && (!flag))
/*     */     {
/* 509 */       addBlockHitEffects(p_addBlockHitEffects_1_, p_addBlockHitEffects_2_.sideHit);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\particle\EffectRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */