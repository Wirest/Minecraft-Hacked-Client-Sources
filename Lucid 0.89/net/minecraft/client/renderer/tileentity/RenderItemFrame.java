package net.minecraft.client.renderer.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureCompass;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.MapData;

public class RenderItemFrame extends Render
{
    private static final ResourceLocation mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("item_frame", "normal");
    private final ModelResourceLocation mapModel = new ModelResourceLocation("item_frame", "map");
    private RenderItem itemRenderer;

    public RenderItemFrame(RenderManager renderManagerIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        this.itemRenderer = itemRendererIn;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(EntityItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        BlockPos var10 = entity.getHangingPosition();
        double var11 = var10.getX() - entity.posX + x;
        double var13 = var10.getY() - entity.posY + y;
        double var15 = var10.getZ() - entity.posZ + z;
        GlStateManager.translate(var11 + 0.5D, var13 + 0.5D, var15 + 0.5D);
        GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        BlockRendererDispatcher var17 = this.mc.getBlockRendererDispatcher();
        ModelManager var18 = var17.getBlockModelShapes().getModelManager();
        IBakedModel var19;

        if (entity.getDisplayedItem() != null && entity.getDisplayedItem().getItem() == Items.filled_map)
        {
            var19 = var18.getModel(this.mapModel);
        }
        else
        {
            var19 = var18.getModel(this.itemFrameModel);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        var17.getBlockModelRenderer().renderModelBrightnessColor(var19, 1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.4375F);
        this.renderItem(entity);
        GlStateManager.popMatrix();
        this.func_147914_a(entity, x + entity.facingDirection.getFrontOffsetX() * 0.3F, y - 0.25D, z + entity.facingDirection.getFrontOffsetZ() * 0.3F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityItemFrame entity)
    {
        return null;
    }

    private void renderItem(EntityItemFrame itemFrame)
    {
        ItemStack var2 = itemFrame.getDisplayedItem();

        if (var2 != null)
        {
            EntityItem var3 = new EntityItem(itemFrame.worldObj, 0.0D, 0.0D, 0.0D, var2);
            Item var4 = var3.getEntityItem().getItem();
            var3.getEntityItem().stackSize = 1;
            var3.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int var5 = itemFrame.getRotation();

            if (var4 == Items.filled_map)
            {
                var5 = var5 % 4 * 2;
            }

            GlStateManager.rotate(var5 * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            if (var4 == Items.filled_map)
            {
                this.renderManager.renderEngine.bindTexture(mapBackgroundTextures);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                float var6 = 0.0078125F;
                GlStateManager.scale(var6, var6, var6);
                GlStateManager.translate(-64.0F, -64.0F, 0.0F);
                MapData var7 = Items.filled_map.getMapData(var3.getEntityItem(), itemFrame.worldObj);
                GlStateManager.translate(0.0F, 0.0F, -1.0F);

                if (var7 != null)
                {
                    this.mc.entityRenderer.getMapItemRenderer().renderMap(var7, true);
                }
            }
            else
            {
                TextureAtlasSprite var12 = null;

                if (var4 == Items.compass)
                {
                    var12 = this.mc.getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
                    this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

                    if (var12 instanceof TextureCompass)
                    {
                        TextureCompass var13 = (TextureCompass)var12;
                        double var8 = var13.currentAngle;
                        double var10 = var13.angleDelta;
                        var13.currentAngle = 0.0D;
                        var13.angleDelta = 0.0D;
                        var13.updateCompass(itemFrame.worldObj, itemFrame.posX, itemFrame.posZ, MathHelper.wrapAngleTo180_float(180 + itemFrame.facingDirection.getHorizontalIndex() * 90), false, true);
                        var13.currentAngle = var8;
                        var13.angleDelta = var10;
                    }
                    else
                    {
                        var12 = null;
                    }
                }

                GlStateManager.scale(0.5F, 0.5F, 0.5F);

                if (!this.itemRenderer.shouldRenderItemIn3D(var3.getEntityItem()) || var4 instanceof ItemSkull)
                {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.pushAttrib();
                RenderHelper.enableStandardItemLighting();
                this.itemRenderer.renderItemModel(var3.getEntityItem());
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();

                if (var12 != null && var12.getFrameCount() > 0)
                {
                    var12.updateAnimation();
                }
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }

    protected void func_147914_a(EntityItemFrame itemFrame, double p_147914_2_, double p_147914_4_, double p_147914_6_)
    {
        if (Minecraft.isGuiEnabled() && itemFrame.getDisplayedItem() != null && itemFrame.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == itemFrame)
        {
            float var8 = 1.6F;
            float var9 = 0.016666668F * var8;
            double var10 = itemFrame.getDistanceSqToEntity(this.renderManager.livingPlayer);
            float var12 = itemFrame.isSneaking() ? 32.0F : 64.0F;

            if (var10 < var12 * var12)
            {
                String var13 = itemFrame.getDisplayedItem().getDisplayName();

                if (itemFrame.isSneaking())
                {
                    FontRenderer var14 = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)p_147914_2_ + 0.0F, (float)p_147914_4_ + itemFrame.height + 0.5F, (float)p_147914_6_);
                    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
                    GlStateManager.scale(-var9, -var9, var9);
                    GlStateManager.disableLighting();
                    GlStateManager.translate(0.0F, 0.25F / var9, 0.0F);
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    Tessellator var15 = Tessellator.getInstance();
                    WorldRenderer var16 = var15.getWorldRenderer();
                    GlStateManager.disableTexture2D();
                    var16.startDrawingQuads();
                    int var17 = var14.getStringWidth(var13) / 2;
                    var16.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                    var16.addVertex(-var17 - 1, -1.0D, 0.0D);
                    var16.addVertex(-var17 - 1, 8.0D, 0.0D);
                    var16.addVertex(var17 + 1, 8.0D, 0.0D);
                    var16.addVertex(var17 + 1, -1.0D, 0.0D);
                    var15.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    var14.drawString(var13, -var14.getStringWidth(var13) / 2, 0, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                    GlStateManager.popMatrix();
                }
                else
                {
                    this.renderLivingLabel(itemFrame, var13, p_147914_2_, p_147914_4_, p_147914_6_, 64);
                }
            }
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityItemFrame)entity);
    }

    @Override
	protected void renderName(Entity entity, double x, double y, double z)
    {
        this.func_147914_a((EntityItemFrame)entity, x, y, z);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    @Override
	public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.doRender((EntityItemFrame)entity, x, y, z, entityYaw, partialTicks);
    }
}
