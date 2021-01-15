/*     */ package net.minecraft.client.renderer.entity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockBed;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.model.ModelChicken;
/*     */ import net.minecraft.client.model.ModelCow;
/*     */ import net.minecraft.client.model.ModelHorse;
/*     */ import net.minecraft.client.model.ModelOcelot;
/*     */ import net.minecraft.client.model.ModelPig;
/*     */ import net.minecraft.client.model.ModelRabbit;
/*     */ import net.minecraft.client.model.ModelSheep2;
/*     */ import net.minecraft.client.model.ModelSlime;
/*     */ import net.minecraft.client.model.ModelSquid;
/*     */ import net.minecraft.client.model.ModelWolf;
/*     */ import net.minecraft.client.model.ModelZombie;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.RenderGlobal;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.culling.ICamera;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.tileentity.RenderEnderCrystal;
/*     */ import net.minecraft.client.renderer.tileentity.RenderItemFrame;
/*     */ import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLeashKnot;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.ai.EntityMinecartMobSpawner;
/*     */ import net.minecraft.entity.boss.EntityDragon;
/*     */ import net.minecraft.entity.boss.EntityWither;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.entity.item.EntityArmorStand;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityEnderEye;
/*     */ import net.minecraft.entity.item.EntityEnderPearl;
/*     */ import net.minecraft.entity.item.EntityExpBottle;
/*     */ import net.minecraft.entity.item.EntityFallingBlock;
/*     */ import net.minecraft.entity.item.EntityFireworkRocket;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.entity.item.EntityMinecartTNT;
/*     */ import net.minecraft.entity.item.EntityPainting;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.monster.EntityBlaze;
/*     */ import net.minecraft.entity.monster.EntityCaveSpider;
/*     */ import net.minecraft.entity.monster.EntityCreeper;
/*     */ import net.minecraft.entity.monster.EntityEnderman;
/*     */ import net.minecraft.entity.monster.EntityEndermite;
/*     */ import net.minecraft.entity.monster.EntityGhast;
/*     */ import net.minecraft.entity.monster.EntityGiantZombie;
/*     */ import net.minecraft.entity.monster.EntityGuardian;
/*     */ import net.minecraft.entity.monster.EntityIronGolem;
/*     */ import net.minecraft.entity.monster.EntityMagmaCube;
/*     */ import net.minecraft.entity.monster.EntityPigZombie;
/*     */ import net.minecraft.entity.monster.EntitySilverfish;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.monster.EntitySlime;
/*     */ import net.minecraft.entity.monster.EntitySnowman;
/*     */ import net.minecraft.entity.monster.EntitySpider;
/*     */ import net.minecraft.entity.monster.EntityWitch;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.passive.EntityBat;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityCow;
/*     */ import net.minecraft.entity.passive.EntityHorse;
/*     */ import net.minecraft.entity.passive.EntityMooshroom;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.passive.EntityPig;
/*     */ import net.minecraft.entity.passive.EntityRabbit;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.passive.EntitySquid;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.entity.projectile.EntityEgg;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.entity.projectile.EntityLargeFireball;
/*     */ import net.minecraft.entity.projectile.EntityPotion;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RenderManager
/*     */ {
/* 106 */   private Map<Class<? extends Entity>, Render<? extends Entity>> entityRenderMap = Maps.newHashMap();
/* 107 */   private Map<String, RenderPlayer> skinMap = Maps.newHashMap();
/*     */   
/*     */   private RenderPlayer playerRenderer;
/*     */   
/*     */   private FontRenderer textRenderer;
/*     */   
/*     */   public static double renderPosX;
/*     */   
/*     */   public static double renderPosY;
/*     */   
/*     */   public static double renderPosZ;
/*     */   
/*     */   public TextureManager renderEngine;
/*     */   
/*     */   public World worldObj;
/*     */   
/*     */   public Entity livingPlayer;
/*     */   public Entity pointedEntity;
/*     */   public static float playerViewY;
/*     */   public static float playerViewX;
/*     */   public GameSettings options;
/*     */   public double viewerPosX;
/*     */   public double viewerPosY;
/*     */   public double viewerPosZ;
/* 131 */   private boolean renderOutlines = false;
/* 132 */   private boolean renderShadow = true;
/*     */   
/*     */ 
/* 135 */   private boolean debugBoundingBox = false;
/*     */   
/*     */   public RenderManager(TextureManager renderEngineIn, RenderItem itemRendererIn)
/*     */   {
/* 139 */     this.renderEngine = renderEngineIn;
/* 140 */     this.entityRenderMap.put(EntityCaveSpider.class, new RenderCaveSpider(this));
/* 141 */     this.entityRenderMap.put(EntitySpider.class, new RenderSpider(this));
/* 142 */     this.entityRenderMap.put(EntityPig.class, new RenderPig(this, new ModelPig(), 0.7F));
/* 143 */     this.entityRenderMap.put(EntitySheep.class, new RenderSheep(this, new ModelSheep2(), 0.7F));
/* 144 */     this.entityRenderMap.put(EntityCow.class, new RenderCow(this, new ModelCow(), 0.7F));
/* 145 */     this.entityRenderMap.put(EntityMooshroom.class, new RenderMooshroom(this, new ModelCow(), 0.7F));
/* 146 */     this.entityRenderMap.put(EntityWolf.class, new RenderWolf(this, new ModelWolf(), 0.5F));
/* 147 */     this.entityRenderMap.put(EntityChicken.class, new RenderChicken(this, new ModelChicken(), 0.3F));
/* 148 */     this.entityRenderMap.put(EntityOcelot.class, new RenderOcelot(this, new ModelOcelot(), 0.4F));
/* 149 */     this.entityRenderMap.put(EntityRabbit.class, new RenderRabbit(this, new ModelRabbit(), 0.3F));
/* 150 */     this.entityRenderMap.put(EntitySilverfish.class, new RenderSilverfish(this));
/* 151 */     this.entityRenderMap.put(EntityEndermite.class, new RenderEndermite(this));
/* 152 */     this.entityRenderMap.put(EntityCreeper.class, new RenderCreeper(this));
/* 153 */     this.entityRenderMap.put(EntityEnderman.class, new RenderEnderman(this));
/* 154 */     this.entityRenderMap.put(EntitySnowman.class, new RenderSnowMan(this));
/* 155 */     this.entityRenderMap.put(EntitySkeleton.class, new RenderSkeleton(this));
/* 156 */     this.entityRenderMap.put(EntityWitch.class, new RenderWitch(this));
/* 157 */     this.entityRenderMap.put(EntityBlaze.class, new RenderBlaze(this));
/* 158 */     this.entityRenderMap.put(EntityPigZombie.class, new RenderPigZombie(this));
/* 159 */     this.entityRenderMap.put(EntityZombie.class, new RenderZombie(this));
/* 160 */     this.entityRenderMap.put(EntitySlime.class, new RenderSlime(this, new ModelSlime(16), 0.25F));
/* 161 */     this.entityRenderMap.put(EntityMagmaCube.class, new RenderMagmaCube(this));
/* 162 */     this.entityRenderMap.put(EntityGiantZombie.class, new RenderGiantZombie(this, new ModelZombie(), 0.5F, 6.0F));
/* 163 */     this.entityRenderMap.put(EntityGhast.class, new RenderGhast(this));
/* 164 */     this.entityRenderMap.put(EntitySquid.class, new RenderSquid(this, new ModelSquid(), 0.7F));
/* 165 */     this.entityRenderMap.put(EntityVillager.class, new RenderVillager(this));
/* 166 */     this.entityRenderMap.put(EntityIronGolem.class, new RenderIronGolem(this));
/* 167 */     this.entityRenderMap.put(EntityBat.class, new RenderBat(this));
/* 168 */     this.entityRenderMap.put(EntityGuardian.class, new RenderGuardian(this));
/* 169 */     this.entityRenderMap.put(EntityDragon.class, new RenderDragon(this));
/* 170 */     this.entityRenderMap.put(EntityEnderCrystal.class, new RenderEnderCrystal(this));
/* 171 */     this.entityRenderMap.put(EntityWither.class, new RenderWither(this));
/* 172 */     this.entityRenderMap.put(Entity.class, new RenderEntity(this));
/* 173 */     this.entityRenderMap.put(EntityPainting.class, new RenderPainting(this));
/* 174 */     this.entityRenderMap.put(EntityItemFrame.class, new RenderItemFrame(this, itemRendererIn));
/* 175 */     this.entityRenderMap.put(EntityLeashKnot.class, new RenderLeashKnot(this));
/* 176 */     this.entityRenderMap.put(EntityArrow.class, new RenderArrow(this));
/* 177 */     this.entityRenderMap.put(EntitySnowball.class, new RenderSnowball(this, Items.snowball, itemRendererIn));
/* 178 */     this.entityRenderMap.put(EntityEnderPearl.class, new RenderSnowball(this, Items.ender_pearl, itemRendererIn));
/* 179 */     this.entityRenderMap.put(EntityEnderEye.class, new RenderSnowball(this, Items.ender_eye, itemRendererIn));
/* 180 */     this.entityRenderMap.put(EntityEgg.class, new RenderSnowball(this, Items.egg, itemRendererIn));
/* 181 */     this.entityRenderMap.put(EntityPotion.class, new RenderPotion(this, itemRendererIn));
/* 182 */     this.entityRenderMap.put(EntityExpBottle.class, new RenderSnowball(this, Items.experience_bottle, itemRendererIn));
/* 183 */     this.entityRenderMap.put(EntityFireworkRocket.class, new RenderSnowball(this, Items.fireworks, itemRendererIn));
/* 184 */     this.entityRenderMap.put(EntityLargeFireball.class, new RenderFireball(this, 2.0F));
/* 185 */     this.entityRenderMap.put(EntitySmallFireball.class, new RenderFireball(this, 0.5F));
/* 186 */     this.entityRenderMap.put(EntityWitherSkull.class, new RenderWitherSkull(this));
/* 187 */     this.entityRenderMap.put(EntityItem.class, new RenderEntityItem(this, itemRendererIn));
/* 188 */     this.entityRenderMap.put(EntityXPOrb.class, new RenderXPOrb(this));
/* 189 */     this.entityRenderMap.put(EntityTNTPrimed.class, new RenderTNTPrimed(this));
/* 190 */     this.entityRenderMap.put(EntityFallingBlock.class, new RenderFallingBlock(this));
/* 191 */     this.entityRenderMap.put(EntityArmorStand.class, new ArmorStandRenderer(this));
/* 192 */     this.entityRenderMap.put(EntityMinecartTNT.class, new RenderTntMinecart(this));
/* 193 */     this.entityRenderMap.put(EntityMinecartMobSpawner.class, new RenderMinecartMobSpawner(this));
/* 194 */     this.entityRenderMap.put(EntityMinecart.class, new RenderMinecart(this));
/* 195 */     this.entityRenderMap.put(EntityBoat.class, new RenderBoat(this));
/* 196 */     this.entityRenderMap.put(EntityFishHook.class, new RenderFish(this));
/* 197 */     this.entityRenderMap.put(EntityHorse.class, new RenderHorse(this, new ModelHorse(), 0.75F));
/* 198 */     this.entityRenderMap.put(EntityLightningBolt.class, new RenderLightningBolt(this));
/* 199 */     this.playerRenderer = new RenderPlayer(this);
/* 200 */     this.skinMap.put("default", this.playerRenderer);
/* 201 */     this.skinMap.put("slim", new RenderPlayer(this, true));
/*     */   }
/*     */   
/*     */   public void setRenderPosition(double renderPosXIn, double renderPosYIn, double renderPosZIn)
/*     */   {
/* 206 */     renderPosX = renderPosXIn;
/* 207 */     renderPosY = renderPosYIn;
/* 208 */     renderPosZ = renderPosZIn;
/*     */   }
/*     */   
/*     */   public <T extends Entity> Render<T> getEntityClassRenderObject(Class<? extends Entity> p_78715_1_)
/*     */   {
/* 213 */     Render<? extends Entity> render = (Render)this.entityRenderMap.get(p_78715_1_);
/*     */     
/* 215 */     if ((render == null) && (p_78715_1_ != Entity.class))
/*     */     {
/* 217 */       render = getEntityClassRenderObject(p_78715_1_.getSuperclass());
/* 218 */       this.entityRenderMap.put(p_78715_1_, render);
/*     */     }
/*     */     
/* 221 */     return render;
/*     */   }
/*     */   
/*     */   public <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn)
/*     */   {
/* 226 */     if ((entityIn instanceof AbstractClientPlayer))
/*     */     {
/* 228 */       String s = ((AbstractClientPlayer)entityIn).getSkinType();
/* 229 */       RenderPlayer renderplayer = (RenderPlayer)this.skinMap.get(s);
/* 230 */       return renderplayer != null ? renderplayer : this.playerRenderer;
/*     */     }
/*     */     
/*     */ 
/* 234 */     return getEntityClassRenderObject(entityIn.getClass());
/*     */   }
/*     */   
/*     */ 
/*     */   public void cacheActiveRenderInfo(World worldIn, FontRenderer textRendererIn, Entity livingPlayerIn, Entity pointedEntityIn, GameSettings optionsIn, float partialTicks)
/*     */   {
/* 240 */     this.worldObj = worldIn;
/* 241 */     this.options = optionsIn;
/* 242 */     this.livingPlayer = livingPlayerIn;
/* 243 */     this.pointedEntity = pointedEntityIn;
/* 244 */     this.textRenderer = textRendererIn;
/*     */     
/* 246 */     if (((livingPlayerIn instanceof EntityLivingBase)) && (((EntityLivingBase)livingPlayerIn).isPlayerSleeping()))
/*     */     {
/* 248 */       IBlockState iblockstate = worldIn.getBlockState(new BlockPos(livingPlayerIn));
/* 249 */       Block block = iblockstate.getBlock();
/*     */       
/* 251 */       if (block == Blocks.bed)
/*     */       {
/* 253 */         int i = ((EnumFacing)iblockstate.getValue(BlockBed.FACING)).getHorizontalIndex();
/* 254 */         playerViewY = i * 90 + 180;
/* 255 */         playerViewX = 0.0F;
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 260 */       playerViewY = livingPlayerIn.prevRotationYaw + (livingPlayerIn.rotationYaw - livingPlayerIn.prevRotationYaw) * partialTicks;
/* 261 */       playerViewX = livingPlayerIn.prevRotationPitch + (livingPlayerIn.rotationPitch - livingPlayerIn.prevRotationPitch) * partialTicks;
/*     */     }
/*     */     
/* 264 */     if (optionsIn.thirdPersonView == 2)
/*     */     {
/* 266 */       playerViewY += 180.0F;
/*     */     }
/*     */     
/* 269 */     this.viewerPosX = (livingPlayerIn.lastTickPosX + (livingPlayerIn.posX - livingPlayerIn.lastTickPosX) * partialTicks);
/* 270 */     this.viewerPosY = (livingPlayerIn.lastTickPosY + (livingPlayerIn.posY - livingPlayerIn.lastTickPosY) * partialTicks);
/* 271 */     this.viewerPosZ = (livingPlayerIn.lastTickPosZ + (livingPlayerIn.posZ - livingPlayerIn.lastTickPosZ) * partialTicks);
/*     */   }
/*     */   
/*     */   public void setPlayerViewY(float playerViewYIn)
/*     */   {
/* 276 */     playerViewY = playerViewYIn;
/*     */   }
/*     */   
/*     */   public boolean isRenderShadow()
/*     */   {
/* 281 */     return this.renderShadow;
/*     */   }
/*     */   
/*     */   public void setRenderShadow(boolean renderShadowIn)
/*     */   {
/* 286 */     this.renderShadow = renderShadowIn;
/*     */   }
/*     */   
/*     */   public void setDebugBoundingBox(boolean debugBoundingBoxIn)
/*     */   {
/* 291 */     this.debugBoundingBox = debugBoundingBoxIn;
/*     */   }
/*     */   
/*     */   public boolean isDebugBoundingBox()
/*     */   {
/* 296 */     return this.debugBoundingBox;
/*     */   }
/*     */   
/*     */   public boolean renderEntitySimple(Entity entityIn, float partialTicks)
/*     */   {
/* 301 */     return renderEntityStatic(entityIn, partialTicks, false);
/*     */   }
/*     */   
/*     */   public boolean shouldRender(Entity entityIn, ICamera camera, double camX, double camY, double camZ)
/*     */   {
/* 306 */     Render<Entity> render = getEntityRenderObject(entityIn);
/* 307 */     return (render != null) && (render.shouldRender(entityIn, camera, camX, camY, camZ));
/*     */   }
/*     */   
/*     */   public boolean renderEntityStatic(Entity entity, float partialTicks, boolean p_147936_3_)
/*     */   {
/* 312 */     if (entity.ticksExisted == 0)
/*     */     {
/* 314 */       entity.lastTickPosX = entity.posX;
/* 315 */       entity.lastTickPosY = entity.posY;
/* 316 */       entity.lastTickPosZ = entity.posZ;
/*     */     }
/*     */     
/* 319 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 320 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 321 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 322 */     float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
/* 323 */     int i = entity.getBrightnessForRender(partialTicks);
/*     */     
/* 325 */     if (entity.isBurning())
/*     */     {
/* 327 */       i = 15728880;
/*     */     }
/*     */     
/* 330 */     int j = i % 65536;
/* 331 */     int k = i / 65536;
/* 332 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 333 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 334 */     return doRenderEntity(entity, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ, f, partialTicks, p_147936_3_);
/*     */   }
/*     */   
/*     */   public void renderWitherSkull(Entity entityIn, float partialTicks)
/*     */   {
/* 339 */     double d0 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
/* 340 */     double d1 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
/* 341 */     double d2 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
/* 342 */     Render<Entity> render = getEntityRenderObject(entityIn);
/*     */     
/* 344 */     if ((render != null) && (this.renderEngine != null))
/*     */     {
/* 346 */       int i = entityIn.getBrightnessForRender(partialTicks);
/* 347 */       int j = i % 65536;
/* 348 */       int k = i / 65536;
/* 349 */       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 350 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 351 */       render.renderName(entityIn, d0 - renderPosX, d1 - renderPosY, d2 - renderPosZ);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean renderEntityWithPosYaw(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks)
/*     */   {
/* 357 */     return doRenderEntity(entityIn, x, y, z, entityYaw, partialTicks, false);
/*     */   }
/*     */   
/*     */   public boolean doRenderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean p_147939_10_)
/*     */   {
/* 362 */     Render<Entity> render = null;
/*     */     
/*     */     try
/*     */     {
/* 366 */       render = getEntityRenderObject(entity);
/*     */       
/* 368 */       if ((render != null) && (this.renderEngine != null))
/*     */       {
/*     */         try
/*     */         {
/* 372 */           if ((render instanceof RendererLivingEntity))
/*     */           {
/* 374 */             ((RendererLivingEntity)render).setRenderOutlines(this.renderOutlines);
/*     */           }
/*     */           
/* 377 */           render.doRender(entity, x, y, z, entityYaw, partialTicks);
/*     */         }
/*     */         catch (Throwable throwable2)
/*     */         {
/* 381 */           throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
/*     */         }
/*     */         
/*     */         try
/*     */         {
/* 386 */           if (!this.renderOutlines)
/*     */           {
/* 388 */             render.doRenderShadowAndFire(entity, x, y, z, entityYaw, partialTicks);
/*     */           }
/*     */         }
/*     */         catch (Throwable throwable1)
/*     */         {
/* 393 */           throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
/*     */         }
/*     */         
/* 396 */         if ((this.debugBoundingBox) && (!entity.isInvisible()) && (!p_147939_10_))
/*     */         {
/*     */           try
/*     */           {
/* 400 */             renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
/*     */           }
/*     */           catch (Throwable throwable)
/*     */           {
/* 404 */             throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
/*     */           }
/*     */         }
/*     */       }
/* 408 */       else if (this.renderEngine != null)
/*     */       {
/* 410 */         return false;
/*     */       }
/*     */       
/* 413 */       return true;
/*     */     }
/*     */     catch (Throwable throwable3)
/*     */     {
/* 417 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
/* 418 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
/* 419 */       entity.addEntityCrashInfo(crashreportcategory);
/* 420 */       CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
/* 421 */       crashreportcategory1.addCrashSection("Assigned renderer", render);
/* 422 */       crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
/* 423 */       crashreportcategory1.addCrashSection("Rotation", Float.valueOf(entityYaw));
/* 424 */       crashreportcategory1.addCrashSection("Delta", Float.valueOf(partialTicks));
/* 425 */       throw new ReportedException(crashreport);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void renderDebugBoundingBox(Entity entityIn, double p_85094_2_, double p_85094_4_, double p_85094_6_, float p_85094_8_, float p_85094_9_)
/*     */   {
/* 434 */     GlStateManager.depthMask(false);
/* 435 */     GlStateManager.disableTexture2D();
/* 436 */     GlStateManager.disableLighting();
/* 437 */     GlStateManager.disableCull();
/* 438 */     GlStateManager.disableBlend();
/* 439 */     float f = entityIn.width / 2.0F;
/* 440 */     AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
/* 441 */     AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entityIn.posX + p_85094_2_, axisalignedbb.minY - entityIn.posY + p_85094_4_, axisalignedbb.minZ - entityIn.posZ + p_85094_6_, axisalignedbb.maxX - entityIn.posX + p_85094_2_, axisalignedbb.maxY - entityIn.posY + p_85094_4_, axisalignedbb.maxZ - entityIn.posZ + p_85094_6_);
/* 442 */     RenderGlobal.func_181563_a(axisalignedbb1, 255, 255, 255, 255);
/*     */     
/* 444 */     if ((entityIn instanceof EntityLivingBase))
/*     */     {
/* 446 */       float f1 = 0.01F;
/* 447 */       RenderGlobal.func_181563_a(new AxisAlignedBB(p_85094_2_ - f, p_85094_4_ + entityIn.getEyeHeight() - 0.009999999776482582D, p_85094_6_ - f, p_85094_2_ + f, p_85094_4_ + entityIn.getEyeHeight() + 0.009999999776482582D, p_85094_6_ + f), 255, 0, 0, 255);
/*     */     }
/*     */     
/* 450 */     Tessellator tessellator = Tessellator.getInstance();
/* 451 */     WorldRenderer worldrenderer = tessellator.getWorldRenderer();
/* 452 */     Vec3 vec3 = entityIn.getLook(p_85094_9_);
/* 453 */     worldrenderer.begin(3, DefaultVertexFormats.POSITION_COLOR);
/* 454 */     worldrenderer.pos(p_85094_2_, p_85094_4_ + entityIn.getEyeHeight(), p_85094_6_).color(0, 0, 255, 255).endVertex();
/* 455 */     worldrenderer.pos(p_85094_2_ + vec3.xCoord * 2.0D, p_85094_4_ + entityIn.getEyeHeight() + vec3.yCoord * 2.0D, p_85094_6_ + vec3.zCoord * 2.0D).color(0, 0, 255, 255).endVertex();
/* 456 */     tessellator.draw();
/* 457 */     GlStateManager.enableTexture2D();
/* 458 */     GlStateManager.enableLighting();
/* 459 */     GlStateManager.enableCull();
/* 460 */     GlStateManager.disableBlend();
/* 461 */     GlStateManager.depthMask(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(World worldIn)
/*     */   {
/* 469 */     this.worldObj = worldIn;
/*     */   }
/*     */   
/*     */   public double getDistanceToCamera(double p_78714_1_, double p_78714_3_, double p_78714_5_)
/*     */   {
/* 474 */     double d0 = p_78714_1_ - this.viewerPosX;
/* 475 */     double d1 = p_78714_3_ - this.viewerPosY;
/* 476 */     double d2 = p_78714_5_ - this.viewerPosZ;
/* 477 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FontRenderer getFontRenderer()
/*     */   {
/* 485 */     return this.textRenderer;
/*     */   }
/*     */   
/*     */   public void setRenderOutlines(boolean renderOutlinesIn)
/*     */   {
/* 490 */     this.renderOutlines = renderOutlinesIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\entity\RenderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */