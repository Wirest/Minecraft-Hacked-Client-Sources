package net.minecraft.client.renderer.tileentity;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.src.Reflector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.tileentity.TileEntityEndPortal;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityPiston;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.tileentity.TileEntityStructure;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class TileEntityRendererDispatcher
{
	public final Map<Class, TileEntitySpecialRenderer> mapSpecialRenderers = Maps.<Class, TileEntitySpecialRenderer>newHashMap();
    public static TileEntityRendererDispatcher instance = new TileEntityRendererDispatcher();
    public FontRenderer fontRenderer;

    /** The player's current X position (same as playerX) */
    public static double staticPlayerX;

    /** The player's current Y position (same as playerY) */
    public static double staticPlayerY;

    /** The player's current Z position (same as playerZ) */
    public static double staticPlayerZ;
    public TextureManager renderEngine;
    public World worldObj;
    public Entity entity;
    public float entityYaw;
    public float entityPitch;
    public RayTraceResult cameraHitResult;
    public double entityX;
    public double entityY;
    public double entityZ;
    public TileEntity tileEntityRendered;
    private Tessellator batchBuffer = new Tessellator(2097152);
    private boolean drawingBatch = false;

    private TileEntityRendererDispatcher()
    {
        this.mapSpecialRenderers.put(TileEntitySign.class, new TileEntitySignRenderer());
        this.mapSpecialRenderers.put(TileEntityMobSpawner.class, new TileEntityMobSpawnerRenderer());
        this.mapSpecialRenderers.put(TileEntityPiston.class, new TileEntityPistonRenderer());
        this.mapSpecialRenderers.put(TileEntityChest.class, new TileEntityChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnderChest.class, new TileEntityEnderChestRenderer());
        this.mapSpecialRenderers.put(TileEntityEnchantmentTable.class, new TileEntityEnchantmentTableRenderer());
        this.mapSpecialRenderers.put(TileEntityEndPortal.class, new TileEntityEndPortalRenderer());
        this.mapSpecialRenderers.put(TileEntityEndGateway.class, new TileEntityEndGatewayRenderer());
        this.mapSpecialRenderers.put(TileEntityBeacon.class, new TileEntityBeaconRenderer());
        this.mapSpecialRenderers.put(TileEntitySkull.class, new TileEntitySkullRenderer());
        this.mapSpecialRenderers.put(TileEntityBanner.class, new TileEntityBannerRenderer());
        this.mapSpecialRenderers.put(TileEntityStructure.class, new TileEntityStructureRenderer());
        this.mapSpecialRenderers.put(TileEntityShulkerBox.class, new TileEntityShulkerBoxRenderer(new ModelShulker()));
        this.mapSpecialRenderers.put(TileEntityBed.class, new TileEntityBedRenderer());

        for (TileEntitySpecialRenderer<?> tileentityspecialrenderer : this.mapSpecialRenderers.values())
        {
            tileentityspecialrenderer.setRendererDispatcher(this);
        }
    }

    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRendererByClass(Class <? extends TileEntity > teClass)
    {
        TileEntitySpecialRenderer<T> tileentityspecialrenderer = (TileEntitySpecialRenderer)this.mapSpecialRenderers.get(teClass);

        if (tileentityspecialrenderer == null && teClass != TileEntity.class)
        {
            tileentityspecialrenderer = this.getSpecialRendererByClass((Class<? extends TileEntity>) teClass.getSuperclass());
            this.mapSpecialRenderers.put(teClass, tileentityspecialrenderer);
        }

        return tileentityspecialrenderer;
    }

    @Nullable
    public <T extends TileEntity> TileEntitySpecialRenderer<T> getSpecialRenderer(@Nullable TileEntity tileEntityIn)
    {
        return tileEntityIn == null ? null : this.getSpecialRendererByClass(tileEntityIn.getClass());
    }

    public void prepare(World p_190056_1_, TextureManager p_190056_2_, FontRenderer p_190056_3_, Entity p_190056_4_, RayTraceResult p_190056_5_, float p_190056_6_)
    {
        if (this.worldObj != p_190056_1_)
        {
            this.setWorld(p_190056_1_);
        }

        this.renderEngine = p_190056_2_;
        this.entity = p_190056_4_;
        this.fontRenderer = p_190056_3_;
        this.cameraHitResult = p_190056_5_;
        this.entityYaw = p_190056_4_.prevRotationYaw + (p_190056_4_.rotationYaw - p_190056_4_.prevRotationYaw) * p_190056_6_;
        this.entityPitch = p_190056_4_.prevRotationPitch + (p_190056_4_.rotationPitch - p_190056_4_.prevRotationPitch) * p_190056_6_;
        this.entityX = p_190056_4_.lastTickPosX + (p_190056_4_.posX - p_190056_4_.lastTickPosX) * (double)p_190056_6_;
        this.entityY = p_190056_4_.lastTickPosY + (p_190056_4_.posY - p_190056_4_.lastTickPosY) * (double)p_190056_6_;
        this.entityZ = p_190056_4_.lastTickPosZ + (p_190056_4_.posZ - p_190056_4_.lastTickPosZ) * (double)p_190056_6_;
    }

    public void renderTileEntity(TileEntity tileentityIn, float partialTicks, int destroyStage)
    {
        if (tileentityIn.getDistanceSq(this.entityX, this.entityY, this.entityZ) < tileentityIn.getMaxRenderDistanceSquared())
        {
            RenderHelper.enableStandardItemLighting();
            boolean flag = true;

            if (Reflector.ForgeTileEntity_hasFastRenderer.exists())
            {
                flag = !this.drawingBatch || !Reflector.callBoolean(tileentityIn, Reflector.ForgeTileEntity_hasFastRenderer);
            }

            if (flag)
            {
                int i = this.worldObj.getCombinedLight(tileentityIn.getPos(), 0);
                int j = i % 65536;
                int k = i / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)j, (float)k);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            }

            BlockPos blockpos = tileentityIn.getPos();
            this.func_192854_a(tileentityIn, (double)blockpos.getX() - staticPlayerX, (double)blockpos.getY() - staticPlayerY, (double)blockpos.getZ() - staticPlayerZ, partialTicks, destroyStage, 1.0F);
        }
    }

    /**
     * Render this TileEntity at a given set of coordinates
     */
    public void renderTileEntityAt(TileEntity tileEntityIn, double x, double y, double z, float partialTicks)
    {
        this.func_192855_a(tileEntityIn, x, y, z, partialTicks, 1.0F);
    }

    public void func_192855_a(TileEntity p_192855_1_, double p_192855_2_, double p_192855_4_, double p_192855_6_, float p_192855_8_, float p_192855_9_)
    {
        this.func_192854_a(p_192855_1_, p_192855_2_, p_192855_4_, p_192855_6_, p_192855_8_, -1, p_192855_9_);
    }

    public void func_192854_a(TileEntity p_192854_1_, double p_192854_2_, double p_192854_4_, double p_192854_6_, float p_192854_8_, int p_192854_9_, float p_192854_10_)
    {
        TileEntitySpecialRenderer<TileEntity> tileentityspecialrenderer = this.<TileEntity>getSpecialRenderer(p_192854_1_);

        if (tileentityspecialrenderer != null)
        {
            try
            {
                this.tileEntityRendered = p_192854_1_;

                if (this.drawingBatch && Reflector.callBoolean(p_192854_1_, Reflector.ForgeTileEntity_hasFastRenderer))
                {
                    tileentityspecialrenderer.renderTileEntityFast(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_, this.batchBuffer.getBuffer());
                }
                else
                {
                    tileentityspecialrenderer.func_192841_a(p_192854_1_, p_192854_2_, p_192854_4_, p_192854_6_, p_192854_8_, p_192854_9_, p_192854_10_);
                }

                this.tileEntityRendered = null;
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Rendering Block Entity");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Block Entity Details");
                p_192854_1_.addInfoToCrashReport(crashreportcategory);
                throw new ReportedException(crashreport);
            }
        }
    }

    public void setWorld(@Nullable World worldIn)
    {
        this.worldObj = worldIn;

        if (worldIn == null)
        {
            this.entity = null;
        }
    }

    public FontRenderer getFontRenderer()
    {
        return this.fontRenderer;
    }

    public void preDrawBatch()
    {
        this.batchBuffer.getBuffer().begin(7, DefaultVertexFormats.BLOCK);
        this.drawingBatch = true;
    }

    public void drawBatch(int p_drawBatch_1_)
    {
        this.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableBlend();
        GlStateManager.disableCull();

        if (Minecraft.isAmbientOcclusionEnabled())
        {
            GlStateManager.shadeModel(7425);
        }
        else
        {
            GlStateManager.shadeModel(7424);
        }

        if (p_drawBatch_1_ > 0)
        {
            this.batchBuffer.getBuffer().sortVertexData((float)staticPlayerX, (float)staticPlayerY, (float)staticPlayerZ);
        }

        this.batchBuffer.draw();
        RenderHelper.enableStandardItemLighting();
        this.drawingBatch = false;
    }
}
