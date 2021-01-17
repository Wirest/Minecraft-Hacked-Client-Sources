// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySign;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.tileentity.TileEntity;
import java.util.Map;

public class TileEntityRendererDispatcher
{
    private Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>> mapSpecialRenderers;
    public static TileEntityRendererDispatcher instance;
    private FontRenderer fontRenderer;
    public static double staticPlayerX;
    public static double staticPlayerY;
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public double entityX;
    public double entityY;
    public double entityZ;
    
    static {
        TileEntityRendererDispatcher.instance = new TileEntityRendererDispatcher();
    }
    
    private TileEntityRendererDispatcher() {
        (this.mapSpecialRenderers = (Map<Class<? extends TileEntity>, TileEntitySpecialRenderer<? extends TileEntity>>)Maps.newHashMap()).put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        for (final TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values()) {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }
    
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(final Class<? extends TileEntity> teClass) {
        TileEntitySpecialRenderer<? extends TileEntity> tileentityspecialrenderer = this.mapSpecialRenderers.get(teClass);
        if (tileentityspecialrenderer == null && teClass != TileEntity.class) {
            tileentityspecialrenderer = this.getSpecialRendererByClass((Class<? extends TileEntity>)teClass.getSuperclass());
            this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
        }
        return (TileEntitySpecialRenderer<T>)tileentityspecialrenderer;
    }
    
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(final TileEntity tileEntityIn) {
        return (tileEntityIn == null) ? null : this.getSpecialRendererByClass(tileEntityIn.getClass());
    }
    
    public void cacheActiveRenderInfo(final World worldIn, final TextureManager textureManagerIn, final FontRenderer fontrendererIn, final Entity entityIn, final float partialTicks) {
        if (this.worldObj != worldIn) {
            this.setWorld(worldIn);
        }
        this.renderEngine = textureManagerIn;
        this.entity = entityIn;
        this.fontRenderer = fontrendererIn;
        this.entityYaw = entityIn.prevRotationYaw + (entityIn.rotationYaw - entityIn.prevRotationYaw) * partialTicks;
        this.entityPitch = entityIn.prevRotationPitch + (entityIn.rotationPitch - entityIn.prevRotationPitch) * partialTicks;
        this.entityX = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
        this.entityY = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
        this.entityZ = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
    }
    
    public void renderTileEntity(final TileEntity tileentityIn, final float partialTicks, final int destroyStage) {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared()) {
            final int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
            final int j = i % 65536;
            final int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0f, k / 1.0f);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final BlockPos blockpos = tileentityIn.getPos();
            this.renderTileEntityAt(tileentityIn, blockpos.getX() - TileEntityRendererDispatcher.staticPlayerX, blockpos.getY() - TileEntityRendererDispatcher.staticPlayerY, blockpos.getZ() - TileEntityRendererDispatcher.staticPlayerZ, partialTicks, destroyStage);
        }
    }
    
    public void renderTileEntityAt(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks) {
        this.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, -1);
    }
    
    public void renderTileEntityAt(final TileEntity tileEntityIn, final double x, final double y, final double z, final float partialTicks, final int destroyStage) {
        final TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.getSpecialRenderer(tileEntityIn);
        if (tileentityspecialrenderer != null) {
            try {
                tileentityspecialrenderer.renderTileEntityAt(tileEntityIn, x, y, z, partialTicks, destroyStage);
            }
            catch (Throwable throwable) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                tileEntityIn.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }
    
    public void setWorld(final World worldIn) {
        this.worldObj = worldIn;
    }
    
    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }
}
