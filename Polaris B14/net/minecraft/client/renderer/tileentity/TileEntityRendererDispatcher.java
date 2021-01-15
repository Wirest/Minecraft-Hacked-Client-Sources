/*     */ package net.minecraft.client.renderer.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.tileentity.TileEntityBeacon;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.tileentity.TileEntityEnchantmentTable;
/*     */ import net.minecraft.tileentity.TileEntityEndPortal;
/*     */ import net.minecraft.tileentity.TileEntityEnderChest;
/*     */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*     */ import net.minecraft.tileentity.TileEntityPiston;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.tileentity.TileEntitySkull;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.ReportedException;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class TileEntityRendererDispatcher
/*     */ {
/*  29 */   private Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> mapSpecialRenderers = Maps.newHashMap();
/*  30 */   public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
/*     */   
/*     */   private FontRenderer fontRenderer;
/*     */   
/*     */   public static double staticPlayerX;
/*     */   
/*     */   public static double staticPlayerY;
/*     */   
/*     */   public static double staticPlayerZ;
/*     */   
/*     */   public TextureManager renderEngine;
/*     */   
/*     */   public World worldObj;
/*     */   public Entity entity;
/*     */   public float entityYaw;
/*     */   public float entityPitch;
/*     */   public double entityX;
/*     */   public double entityY;
/*     */   public double entityZ;
/*     */   
/*     */   private TileEntityRendererDispatcher()
/*     */   {
/*  52 */     this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
/*  53 */     this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
/*  54 */     this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
/*  55 */     this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
/*  56 */     this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
/*  57 */     this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
/*  58 */     this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
/*  59 */     this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
/*  60 */     this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
/*  61 */     this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
/*     */     
/*  63 */     for (TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values())
/*     */     {
/*  65 */       tileentityspecialrenderer.setRendererDispatcher(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class<? extends TileEntity> teClass)
/*     */   {
/*  71 */     TileEntitySpecialRenderer<? extends TileEntity> tileentityspecialrenderer = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(teClass);
/*     */     
/*  73 */     if ((tileentityspecialrenderer == null) && (teClass != TileEntity.class))
/*     */     {
/*  75 */       tileentityspecialrenderer = getSpecialRendererByClass(teClass.getSuperclass());
/*  76 */       this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
/*     */     }
/*     */     
/*  79 */     return tileentityspecialrenderer;
/*     */   }
/*     */   
/*     */   public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(TileEntity tileEntityIn)
/*     */   {
/*  84 */     return tileEntityIn == null ? null : getSpecialRendererByClass(tileEntityIn.getClass());
/*     */   }
/*     */   
/*     */   public void cacheActiveRenderInfo(World worldIn, TextureManager textureManagerIn, FontRenderer fontrendererIn, Entity entityIn, float partialTicks)
/*     */   {
/*  89 */     if (this.worldObj != worldIn)
/*     */     {
/*  91 */       setWorld(worldIn);
/*     */     }
/*     */     
/*  94 */     this.renderEngine = textureManagerIn;
/*  95 */     this.entity = entityIn;
/*  96 */     this.fontRenderer = fontrendererIn;
/*  97 */     this.entityYaw = (entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks);
/*  98 */     this.entityPitch = (entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks);
/*  99 */     this.entityX = (entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks);
/* 100 */     this.entityY = (entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks);
/* 101 */     this.entityZ = (entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks);
/*     */   }
/*     */   
/*     */   public void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage)
/*     */   {
/* 106 */     if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared())
/*     */     {
/* 108 */       int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
/* 109 */       int j = i % 65536;
/* 110 */       int k = i / 65536;
/* 111 */       OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/* 112 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 113 */       BlockPos blockpos = tileentityIn.getPos();
/* 114 */       renderTileEntityAt(tileentityIn, blockpos.getX() - staticPlayerX, blockpos.getY() - staticPlayerY, blockpos.getZ() - staticPlayerZ, partialTicks, destroyStage);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks)
/*     */   {
/* 123 */     renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, -1);
/*     */   }
/*     */   
/*     */   public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks, int destroyStage)
/*     */   {
/* 128 */     TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = getSpecialRenderer(tileEntityIn);
/*     */     
/* 130 */     if (tileentityspecialrenderer != null)
/*     */     {
/*     */       try
/*     */       {
/* 134 */         tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
/*     */       }
/*     */       catch (Throwable throwable)
/*     */       {
/* 138 */         CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
/* 139 */         CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
/* 140 */         tileEntityIn.addInfoToCrashReport(crashreportcategory);
/* 141 */         throw new ReportedException(crashreport);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setWorld(World worldIn)
/*     */   {
/* 148 */     this.worldObj = worldIn;
/*     */   }
/*     */   
/*     */   public FontRenderer getFontRenderer()
/*     */   {
/* 153 */     return this.fontRenderer;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\tileentity\TileEntityRendererDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */