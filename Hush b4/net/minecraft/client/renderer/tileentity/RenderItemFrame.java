// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.storage.MapData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemSkull;
import net.minecraft.util.MathHelper;
import shadersmod.client.ShadersTex;
import optifine.Config;
import net.minecraft.client.renderer.texture.TextureCompass;
import optifine.Reflector;
import net.minecraft.item.ItemMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.entity.Render;

public class RenderItemFrame extends Render
{
    private static final ResourceLocation mapBackgroundTextures;
    private final Minecraft mc;
    private final ModelResourceLocation itemFrameModel;
    private final ModelResourceLocation mapModel;
    private RenderItem itemRenderer;
    private static final String __OBFID = "CL_00001002";
    
    static {
        mapBackgroundTextures = new ResourceLocation("textures/map/map_background.png");
    }
    
    public RenderItemFrame(final RenderManager renderManagerIn, final RenderItem itemRendererIn) {
        super(renderManagerIn);
        this.mc = Minecraft.getMinecraft();
        this.itemFrameModel = new ModelResourceLocation("item_frame", "normal");
        this.mapModel = new ModelResourceLocation("item_frame", "map");
        this.itemRenderer = itemRendererIn;
    }
    
    public void doRender(final EntityItemFrame entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        GlStateManager.pushMatrix();
        final BlockPos blockpos = entity.getHangingPosition();
        final double d0 = blockpos.getX() - entity.posX + x;
        final double d2 = blockpos.getY() - entity.posY + y;
        final double d3 = blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5, d2 + 0.5, d3 + 0.5);
        GlStateManager.rotate(180.0f - entity.rotationYaw, 0.0f, 1.0f, 0.0f);
        this.renderManager.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        final BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        final ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;
        if (entity.getDisplayedItem() != null && entity.getDisplayedItem().getItem() == Items.filled_map) {
            ibakedmodel = modelmanager.getModel(this.mapModel);
        }
        else {
            ibakedmodel = modelmanager.getModel(this.itemFrameModel);
        }
        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5f, -0.5f, -0.5f);
        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.popMatrix();
        GlStateManager.translate(0.0f, 0.0f, 0.4375f);
        this.renderItem(entity);
        GlStateManager.popMatrix();
        this.renderName(entity, x + entity.facingDirection.getFrontOffsetX() * 0.3f, y - 0.25, z + entity.facingDirection.getFrontOffsetZ() * 0.3f);
    }
    
    protected ResourceLocation getEntityTexture(final EntityItemFrame entity) {
        return null;
    }
    
    private void renderItem(final EntityItemFrame itemFrame) {
        final ItemStack itemstack = itemFrame.getDisplayedItem();
        if (itemstack != null) {
            final EntityItem entityitem = new EntityItem(itemFrame.worldObj, 0.0, 0.0, 0.0, itemstack);
            final Item item = entityitem.getEntityItem().getItem();
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0f;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int i = itemFrame.getRotation();
            if (item instanceof ItemMap) {
                i = i % 4 * 2;
            }
            GlStateManager.rotate(i * 360.0f / 8.0f, 0.0f, 0.0f, 1.0f);
            if (!Reflector.postForgeBusEvent(Reflector.RenderItemInFrameEvent_Constructor, itemFrame, this)) {
                if (item instanceof ItemMap) {
                    this.renderManager.renderEngine.bindTexture(RenderItemFrame.mapBackgroundTextures);
                    GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
                    final float f = 0.0078125f;
                    GlStateManager.scale(f, f, f);
                    GlStateManager.translate(-64.0f, -64.0f, 0.0f);
                    final MapData mapdata = Items.filled_map.getMapData(entityitem.getEntityItem(), itemFrame.worldObj);
                    GlStateManager.translate(0.0f, 0.0f, -1.0f);
                    if (mapdata != null) {
                        this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
                    }
                }
                else {
                    TextureAtlasSprite textureatlassprite = null;
                    if (item == Items.compass) {
                        textureatlassprite = this.mc.getTextureMapBlocks().getAtlasSprite(TextureCompass.field_176608_l);
                        if (Config.isShaders()) {
                            ShadersTex.bindTextureMapForUpdateAndRender(this.mc.getTextureManager(), TextureMap.locationBlocksTexture);
                        }
                        else {
                            this.mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
                        }
                        if (textureatlassprite instanceof TextureCompass) {
                            final TextureCompass texturecompass = (TextureCompass)textureatlassprite;
                            final double d0 = texturecompass.currentAngle;
                            final double d2 = texturecompass.angleDelta;
                            texturecompass.currentAngle = 0.0;
                            texturecompass.angleDelta = 0.0;
                            texturecompass.updateCompass(itemFrame.worldObj, itemFrame.posX, itemFrame.posZ, MathHelper.wrapAngleTo180_float((float)(180 + itemFrame.facingDirection.getHorizontalIndex() * 90)), false, true);
                            texturecompass.currentAngle = d0;
                            texturecompass.angleDelta = d2;
                        }
                        else {
                            textureatlassprite = null;
                        }
                    }
                    GlStateManager.scale(0.5f, 0.5f, 0.5f);
                    if (!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()) || item instanceof ItemSkull) {
                        GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                    }
                    GlStateManager.pushAttrib();
                    RenderHelper.enableStandardItemLighting();
                    this.itemRenderer.func_181564_a(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
                    RenderHelper.disableStandardItemLighting();
                    GlStateManager.popAttrib();
                    if (textureatlassprite != null && textureatlassprite.getFrameCount() > 0) {
                        textureatlassprite.updateAnimation();
                    }
                }
            }
            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
    
    protected void renderName(final EntityItemFrame entity, final double x, final double y, final double z) {
        if (Minecraft.isGuiEnabled() && entity.getDisplayedItem() != null && entity.getDisplayedItem().hasDisplayName() && this.renderManager.pointedEntity == entity) {
            final float f = 1.6f;
            final float f2 = 0.016666668f * f;
            final double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
            final float f3 = entity.isSneaking() ? 32.0f : 64.0f;
            if (d0 < f3 * f3) {
                final String s = entity.getDisplayedItem().getDisplayName();
                if (entity.isSneaking()) {
                    final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                    GlStateManager.pushMatrix();
                    GlStateManager.translate((float)x + 0.0f, (float)y + entity.height + 0.5f, (float)z);
                    GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                    GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                    GlStateManager.scale(-f2, -f2, f2);
                    GlStateManager.disableLighting();
                    GlStateManager.translate(0.0f, 0.25f / f2, 0.0f);
                    GlStateManager.depthMask(false);
                    GlStateManager.enableBlend();
                    GlStateManager.blendFunc(770, 771);
                    final Tessellator tessellator = Tessellator.getInstance();
                    final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
                    final int i = fontrenderer.getStringWidth(s) / 2;
                    GlStateManager.disableTexture2D();
                    worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
                    worldrenderer.pos(-i - 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldrenderer.pos(-i - 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldrenderer.pos(i + 1, 8.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    worldrenderer.pos(i + 1, -1.0, 0.0).color(0.0f, 0.0f, 0.0f, 0.25f).endVertex();
                    tessellator.draw();
                    GlStateManager.enableTexture2D();
                    GlStateManager.depthMask(true);
                    fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0.0, 553648127);
                    GlStateManager.enableLighting();
                    GlStateManager.disableBlend();
                    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                    GlStateManager.popMatrix();
                }
                else {
                    this.renderLivingLabel(entity, s, x, y, z, 64);
                }
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final Entity entity) {
        return this.getEntityTexture((EntityItemFrame)entity);
    }
    
    @Override
    protected void renderName(final Entity entity, final double x, final double y, final double z) {
        this.renderName((EntityItemFrame)entity, x, y, z);
    }
    
    @Override
    public void doRender(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        this.doRender((EntityItemFrame)entity, x, y, z, entityYaw, partialTicks);
    }
}
