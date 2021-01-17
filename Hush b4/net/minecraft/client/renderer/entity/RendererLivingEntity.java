// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.scoreboard.Team;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import java.util.Iterator;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.EnumChatFormatting;
import shadersmod.client.Shaders;
import optifine.Config;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.Minecraft;
import me.nico.hush.modules.render.ESP;
import org.lwjgl.opengl.GL11;
import me.nico.hush.utils.Util;
import net.minecraft.entity.player.EntityPlayer;
import me.nico.hush.Client;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.GlStateManager;
import optifine.Reflector;
import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.entity.Entity;
import me.nico.hush.events.EventRender;
import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GLAllocation;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import java.util.List;
import java.nio.FloatBuffer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.apache.logging.log4j.Logger;
import net.minecraft.entity.EntityLivingBase;

public abstract class RendererLivingEntity<T extends EntityLivingBase> extends Render<T>
{
    private static final Logger logger;
    private static final DynamicTexture field_177096_e;
    protected ModelBase mainModel;
    protected FloatBuffer brightnessBuffer;
    protected List<LayerRenderer<T>> layerRenderers;
    protected boolean renderOutlines;
    private static final String __OBFID = "CL_00001012";
    public static float NAME_TAG_RANGE;
    public static float NAME_TAG_RANGE_SNEAK;
    
    static {
        logger = LogManager.getLogger();
        field_177096_e = new DynamicTexture(16, 16);
        RendererLivingEntity.NAME_TAG_RANGE = 64.0f;
        RendererLivingEntity.NAME_TAG_RANGE_SNEAK = 32.0f;
        final int[] aint = RendererLivingEntity.field_177096_e.getTextureData();
        for (int i = 0; i < 256; ++i) {
            aint[i] = -1;
        }
        RendererLivingEntity.field_177096_e.updateDynamicTexture();
    }
    
    public RendererLivingEntity(final RenderManager renderManagerIn, final ModelBase modelBaseIn, final float shadowSizeIn) {
        super(renderManagerIn);
        this.brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
        this.layerRenderers = (List<LayerRenderer<T>>)Lists.newArrayList();
        this.renderOutlines = false;
        this.mainModel = modelBaseIn;
        this.shadowSize = shadowSizeIn;
    }
    
    public <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean addLayer(final U layer) {
        return this.layerRenderers.add((LayerRenderer<T>)layer);
    }
    
    protected <V extends EntityLivingBase, U extends LayerRenderer<V>> boolean removeLayer(final U layer) {
        return this.layerRenderers.remove(layer);
    }
    
    public ModelBase getMainModel() {
        return this.mainModel;
    }
    
    protected float interpolateRotation(final float par1, final float par2, final float par3) {
        float f;
        for (f = par2 - par1; f < -180.0f; f += 360.0f) {}
        while (f >= 180.0f) {
            f -= 360.0f;
        }
        return par1 + par3 * f;
    }
    
    public void transformHeldFull3DItemLayer() {
    }
    
    @Override
    public void doRender(final T entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        final EventRender eventRenderer = new EventRender(entity);
        EventManager.call(eventRenderer);
        if (!Reflector.RenderLivingEvent_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Pre_Constructor, entity, this, x, y, z)) {
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
            this.mainModel.isRiding = entity.isRiding();
            if (Reflector.ForgeEntity_shouldRiderSit.exists()) {
                this.mainModel.isRiding = (entity.isRiding() && entity.ridingEntity != null && Reflector.callBoolean(entity.ridingEntity, Reflector.ForgeEntity_shouldRiderSit, new Object[0]));
            }
            this.mainModel.isChild = entity.isChild();
            try {
                float f = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
                final float f2 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
                float f3 = f2 - f;
                if (this.mainModel.isRiding && entity.ridingEntity instanceof EntityLivingBase) {
                    final EntityLivingBase entitylivingbase = (EntityLivingBase)entity.ridingEntity;
                    f = this.interpolateRotation(entitylivingbase.prevRenderYawOffset, entitylivingbase.renderYawOffset, partialTicks);
                    f3 = f2 - f;
                    float f4 = MathHelper.wrapAngleTo180_float(f3);
                    if (f4 < -85.0f) {
                        f4 = -85.0f;
                    }
                    if (f4 >= 85.0f) {
                        f4 = 85.0f;
                    }
                    f = f2 - f4;
                    if (f4 * f4 > 2500.0f) {
                        f += f4 * 0.2f;
                    }
                }
                final float f5 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
                this.renderLivingAt(entity, x, y, z);
                final float f6 = this.handleRotationFloat(entity, partialTicks);
                this.rotateCorpse(entity, f6, f, partialTicks);
                GlStateManager.enableRescaleNormal();
                GlStateManager.scale(-1.0f, -1.0f, 1.0f);
                this.preRenderCallback(entity, partialTicks);
                final float f7 = 0.0625f;
                GlStateManager.translate(0.0f, -1.5078125f, 0.0f);
                float f8 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
                float f9 = entity.limbSwing - entity.limbSwingAmount * (1.0f - partialTicks);
                if (Client.instance.moduleManager.getModuleName("ESP").isEnabled() && Client.instance.settingManager.getSettingByName("Outline").getValBoolean() && Client.instance.settingManager.getSettingByName("Players").getValBoolean() && entity instanceof EntityPlayer) {
                    Util.checkSetupFBO();
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    GL11.glBlendFunc(770, 771);
                    ESP.renderOne();
                    GL11.glLineWidth(3.0f);
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(3042);
                    GL11.glEnable(2848);
                    GL11.glHint(3154, 4354);
                    Minecraft.getMinecraft();
                    final float distance = Minecraft.thePlayer.getDistanceToEntity(entity);
                    final float health = entity.getHealth();
                    GL11.glEnable(3024);
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    ESP.renderTwo();
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    ESP.renderThree();
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    ESP.renderFour();
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    ESP.renderFive();
                    GL11.glDisable(3024);
                }
                if (entity.isChild()) {
                    f9 *= 3.0f;
                }
                if (f8 > 1.0f) {
                    f8 = 1.0f;
                }
                GlStateManager.enableAlpha();
                this.mainModel.setLivingAnimations(entity, f9, f8, partialTicks);
                this.mainModel.setRotationAngles(f9, f8, f6, f3, f5, 0.0625f, entity);
                if (this.renderOutlines) {
                    final boolean flag1 = this.setScoreTeamColor(entity);
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    if (flag1) {
                        this.unsetScoreTeamColor();
                    }
                }
                else {
                    final boolean flag2 = this.setDoRenderBrightness(entity, partialTicks);
                    this.renderModel(entity, f9, f8, f6, f3, f5, 0.0625f);
                    if (flag2) {
                        this.unsetBrightness();
                    }
                    GlStateManager.depthMask(true);
                    if (!(entity instanceof EntityPlayer) || !((EntityPlayer)entity).isSpectator()) {
                        this.renderLayers(entity, f9, f8, partialTicks, f6, f3, f5, 0.0625f);
                    }
                }
                GlStateManager.disableRescaleNormal();
            }
            catch (Exception exception) {
                RendererLivingEntity.logger.error("Couldn't render entity", exception);
            }
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
            if (!this.renderOutlines) {
                super.doRender(entity, x, y, z, entityYaw, partialTicks);
            }
            if (!Reflector.RenderLivingEvent_Post_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Post_Constructor, entity, this, x, y, z)) {}
        }
    }
    
    protected boolean setScoreTeamColor(final EntityLivingBase entityLivingBaseIn) {
        int i = 16777215;
        if (entityLivingBaseIn instanceof EntityPlayer) {
            final ScorePlayerTeam scoreplayerteam = (ScorePlayerTeam)entityLivingBaseIn.getTeam();
            if (scoreplayerteam != null) {
                final String s = FontRenderer.getFormatFromString(scoreplayerteam.getColorPrefix());
                if (s.length() >= 2) {
                    i = this.getFontRendererFromRenderManager().getColorCode(s.charAt(1));
                }
            }
        }
        final float f1 = (i >> 16 & 0xFF) / 255.0f;
        final float f2 = (i >> 8 & 0xFF) / 255.0f;
        final float f3 = (i & 0xFF) / 255.0f;
        GlStateManager.disableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(f1, f2, f3, 1.0f);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void unsetScoreTeamColor() {
        GlStateManager.enableLighting();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    protected void renderModel(final T entitylivingbaseIn, final float p_77036_2_, final float p_77036_3_, final float p_77036_4_, final float p_77036_5_, final float p_77036_6_, final float p_77036_7_) {
        final boolean flag = !entitylivingbaseIn.isInvisible();
        boolean b = false;
        Label_0038: {
            if (!flag) {
                Minecraft.getMinecraft();
                if (!entitylivingbaseIn.isInvisibleToPlayer(Minecraft.thePlayer)) {
                    b = true;
                    break Label_0038;
                }
            }
            b = false;
        }
        final boolean flag2 = b;
        if (flag || flag2) {
            if (!this.bindEntityTexture(entitylivingbaseIn)) {
                return;
            }
            if (flag2) {
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 0.15f);
                GlStateManager.depthMask(false);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(770, 771);
                GlStateManager.alphaFunc(516, 0.003921569f);
            }
            this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
            if (flag2) {
                GlStateManager.disableBlend();
                GlStateManager.alphaFunc(516, 0.1f);
                GlStateManager.popMatrix();
                GlStateManager.depthMask(true);
            }
        }
    }
    
    protected boolean setDoRenderBrightness(final T entityLivingBaseIn, final float partialTicks) {
        return this.setBrightness(entityLivingBaseIn, partialTicks, true);
    }
    
    protected boolean setBrightness(final T entitylivingbaseIn, final float partialTicks, final boolean combineTextures) {
        final float f = entitylivingbaseIn.getBrightness(partialTicks);
        final int i = this.getColorMultiplier(entitylivingbaseIn, f, partialTicks);
        final boolean flag = (i >> 24 & 0xFF) > 0;
        final boolean flag2 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;
        if (!flag && !flag2) {
            return false;
        }
        if (!flag && !combineTextures) {
            return false;
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        this.brightnessBuffer.position(0);
        if (flag2) {
            this.brightnessBuffer.put(1.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.0f);
            this.brightnessBuffer.put(0.3f);
            if (Config.isShaders()) {
                Shaders.setEntityColor(1.0f, 0.0f, 0.0f, 0.3f);
            }
        }
        else {
            final float f2 = (i >> 24 & 0xFF) / 255.0f;
            final float f3 = (i >> 16 & 0xFF) / 255.0f;
            final float f4 = (i >> 8 & 0xFF) / 255.0f;
            final float f5 = (i & 0xFF) / 255.0f;
            this.brightnessBuffer.put(f3);
            this.brightnessBuffer.put(f4);
            this.brightnessBuffer.put(f5);
            this.brightnessBuffer.put(1.0f - f2);
            if (Config.isShaders()) {
                Shaders.setEntityColor(f3, f4, f5, 1.0f - f2);
            }
        }
        this.brightnessBuffer.flip();
        GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(RendererLivingEntity.field_177096_e.getGlTextureId());
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        return true;
    }
    
    protected void unsetBrightness() {
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableTexture2D();
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_ALPHA, 770);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
        GlStateManager.disableTexture2D();
        GlStateManager.bindTexture(0);
        GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, 5890);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 8448);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
        GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, 5890);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        if (Config.isShaders()) {
            Shaders.setEntityColor(0.0f, 0.0f, 0.0f, 0.0f);
        }
    }
    
    protected void renderLivingAt(final T entityLivingBaseIn, final double x, final double y, final double z) {
        GlStateManager.translate((float)x, (float)y, (float)z);
    }
    
    protected void rotateCorpse(final T bat, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        GlStateManager.rotate(180.0f - p_77043_3_, 0.0f, 1.0f, 0.0f);
        if (bat.deathTime > 0) {
            float f = (bat.deathTime + partialTicks - 1.0f) / 20.0f * 1.6f;
            f = MathHelper.sqrt_float(f);
            if (f > 1.0f) {
                f = 1.0f;
            }
            GlStateManager.rotate(f * this.getDeathMaxRotation(bat), 0.0f, 0.0f, 1.0f);
        }
        else {
            final String s = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getName());
            if (s != null && (s.equals("Dinnerbone") || s.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer)bat).isWearing(EnumPlayerModelParts.CAPE))) {
                GlStateManager.translate(0.0f, bat.height + 0.1f, 0.0f);
                GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
            }
        }
    }
    
    protected float getSwingProgress(final T livingBase, final float partialTickTime) {
        return livingBase.getSwingProgress(partialTickTime);
    }
    
    protected float handleRotationFloat(final T livingBase, final float partialTicks) {
        return livingBase.ticksExisted + partialTicks;
    }
    
    protected void renderLayers(final T entitylivingbaseIn, final float p_177093_2_, final float p_177093_3_, final float partialTicks, final float p_177093_5_, final float p_177093_6_, final float p_177093_7_, final float p_177093_8_) {
        for (final LayerRenderer<T> layerrenderer : this.layerRenderers) {
            final boolean flag = this.setBrightness(entitylivingbaseIn, partialTicks, layerrenderer.shouldCombineTextures());
            layerrenderer.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, partialTicks, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
            if (flag) {
                this.unsetBrightness();
            }
        }
    }
    
    protected float getDeathMaxRotation(final T entityLivingBaseIn) {
        return 90.0f;
    }
    
    protected int getColorMultiplier(final T entitylivingbaseIn, final float lightBrightness, final float partialTickTime) {
        return 0;
    }
    
    protected void preRenderCallback(final T entitylivingbaseIn, final float partialTickTime) {
    }
    
    public void renderName(final T entity, final double x, final double y, final double z) {
        if (!Reflector.RenderLivingEvent_Specials_Pre_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Pre_Constructor, entity, this, x, y, z)) {
            if (this.canRenderName(entity)) {
                final double d0 = entity.getDistanceSqToEntity(this.renderManager.livingPlayer);
                final float f = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;
                if (d0 < f * f) {
                    final String s = entity.getDisplayName().getFormattedText();
                    final float f2 = 0.02666667f;
                    GlStateManager.alphaFunc(516, 0.1f);
                    if (entity.isSneaking()) {
                        final FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
                        GlStateManager.pushMatrix();
                        GlStateManager.translate((float)x, (float)y + entity.height + 0.5f - (entity.isChild() ? (entity.height / 2.0f) : 0.0f), (float)z);
                        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
                        GlStateManager.rotate(-RenderManager.playerViewY, 0.0f, 1.0f, 0.0f);
                        GlStateManager.rotate(RenderManager.playerViewX, 1.0f, 0.0f, 0.0f);
                        GlStateManager.scale(-0.02666667f, -0.02666667f, 0.02666667f);
                        GlStateManager.translate(0.0f, 9.374999f, 0.0f);
                        GlStateManager.disableLighting();
                        GlStateManager.depthMask(false);
                        GlStateManager.enableBlend();
                        GlStateManager.disableTexture2D();
                        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
                        final int i = fontrenderer.getStringWidth(s) / 2;
                        final Tessellator tessellator = Tessellator.getInstance();
                        final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
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
                        this.renderOffsetLivingLabel(entity, x, y - (entity.isChild() ? (entity.height / 2.0f) : 0.0), z, s, 0.02666667f, d0);
                    }
                }
            }
            if (!Reflector.RenderLivingEvent_Specials_Post_Constructor.exists() || !Reflector.postForgeBusEvent(Reflector.RenderLivingEvent_Specials_Post_Constructor, entity, this, x, y, z)) {}
        }
    }
    
    @Override
    protected boolean canRenderName(final T entity) {
        Minecraft.getMinecraft();
        final EntityPlayerSP entityplayersp = Minecraft.thePlayer;
        if (entity instanceof EntityPlayer && entity != entityplayersp) {
            final Team team = entity.getTeam();
            final Team team2 = entityplayersp.getTeam();
            if (team != null) {
                final Team.EnumVisible team$enumvisible = team.getNameTagVisibility();
                switch (RendererLivingEntity$1.field_178679_a[team$enumvisible.ordinal()]) {
                    case 1: {
                        return true;
                    }
                    case 2: {
                        return false;
                    }
                    case 3: {
                        return team2 == null || team.isSameTeam(team2);
                    }
                    case 4: {
                        return team2 == null || !team.isSameTeam(team2);
                    }
                    default: {
                        return true;
                    }
                }
            }
        }
        return Minecraft.isGuiEnabled() && entity != this.renderManager.livingPlayer && !entity.isInvisibleToPlayer(entityplayersp) && entity.riddenByEntity == null;
    }
    
    public void setRenderOutlines(final boolean renderOutlinesIn) {
        this.renderOutlines = renderOutlinesIn;
    }
    
    static final class RendererLivingEntity$1
    {
        static final int[] field_178679_a;
        private static final String __OBFID = "CL_00002435";
        
        static {
            field_178679_a = new int[Team.EnumVisible.values().length];
            try {
                RendererLivingEntity$1.field_178679_a[Team.EnumVisible.ALWAYS.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                RendererLivingEntity$1.field_178679_a[Team.EnumVisible.NEVER.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                RendererLivingEntity$1.field_178679_a[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                RendererLivingEntity$1.field_178679_a[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
        }
    }
}
