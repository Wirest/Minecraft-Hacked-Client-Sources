// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.entity.AbstractClientPlayer;

public class RenderPlayer extends RendererLivingEntity<AbstractClientPlayer>
{
    private boolean smallArms;
    
    public RenderPlayer(final RenderManager renderManager) {
        this(renderManager, false);
    }
    
    public RenderPlayer(final RenderManager renderManager, final boolean useSmallArms) {
        super(renderManager, new ModelPlayer(0.0f, useSmallArms), 0.5f);
        this.smallArms = useSmallArms;
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerBipedArmor(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerHeldItem(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerArrow(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        ((RendererLivingEntity<EntityLivingBase>)this).addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
    }
    
    private void addLayer(final LayerCape layerCape) {
    }
    
    @Override
    public ModelPlayer getMainModel() {
        return (ModelPlayer)super.getMainModel();
    }
    
    @Override
    public void doRender(final AbstractClientPlayer entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
        if (!entity.isUser() || this.renderManager.livingPlayer == entity) {
            double d0 = y;
            if (entity.isSneaking() && !(entity instanceof EntityPlayerSP)) {
                d0 = y - 0.125;
            }
            this.setModelVisibilities(entity);
            super.doRender(entity, x, d0, z, entityYaw, partialTicks);
        }
    }
    
    private void setModelVisibilities(final AbstractClientPlayer clientPlayer) {
        final ModelPlayer modelplayer = this.getMainModel();
        if (clientPlayer.isSpectator()) {
            modelplayer.setInvisible(false);
            modelplayer.bipedHead.showModel = true;
            modelplayer.bipedHeadwear.showModel = true;
        }
        else {
            final ItemStack itemstack = clientPlayer.inventory.getCurrentItem();
            modelplayer.setInvisible(true);
            modelplayer.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            modelplayer.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            modelplayer.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            modelplayer.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            modelplayer.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            modelplayer.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            modelplayer.heldItemLeft = 0;
            modelplayer.aimedBow = false;
            modelplayer.isSneak = clientPlayer.isSneaking();
            if (itemstack == null) {
                modelplayer.heldItemRight = 0;
            }
            else {
                modelplayer.heldItemRight = 1;
                if (clientPlayer.getItemInUseCount() > 0) {
                    final EnumAction enumaction = itemstack.getItemUseAction();
                    if (enumaction == EnumAction.BLOCK) {
                        modelplayer.heldItemRight = 3;
                    }
                    else if (enumaction == EnumAction.BOW) {
                        modelplayer.aimedBow = true;
                    }
                }
            }
        }
    }
    
    @Override
    protected ResourceLocation getEntityTexture(final AbstractClientPlayer entity) {
        return entity.getLocationSkin();
    }
    
    @Override
    public void transformHeldFull3DItemLayer() {
        GlStateManager.translate(0.0f, 0.1875f, 0.0f);
    }
    
    @Override
    protected void preRenderCallback(final AbstractClientPlayer entitylivingbaseIn, final float partialTickTime) {
        final float f = 0.9375f;
        GlStateManager.scale(f, f, f);
    }
    
    @Override
    protected void renderOffsetLivingLabel(final AbstractClientPlayer entityIn, final double x, double y, final double z, final String str, final float p_177069_9_, final double p_177069_10_) {
        if (p_177069_10_ < 100.0) {
            final Scoreboard scoreboard = entityIn.getWorldScoreboard();
            final ScoreObjective scoreobjective = scoreboard.getObjectiveInDisplaySlot(2);
            if (scoreobjective != null) {
                final Score score = scoreboard.getValueFromObjective(entityIn.getName(), scoreobjective);
                this.renderLivingLabel(entityIn, String.valueOf(score.getScorePoints()) + " " + scoreobjective.getDisplayName(), x, y, z, 64);
                y += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15f * p_177069_9_;
            }
        }
        super.renderOffsetLivingLabel(entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
    }
    
    public void renderRightArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(f, f, f);
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.swingProgress = 0.0f;
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.renderRightArm();
    }
    
    public void renderLeftArm(final AbstractClientPlayer clientPlayer) {
        final float f = 1.0f;
        GlStateManager.color(f, f, f);
        final ModelPlayer modelplayer = this.getMainModel();
        this.setModelVisibilities(clientPlayer);
        modelplayer.isSneak = false;
        modelplayer.setRotationAngles(modelplayer.swingProgress = 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0625f, clientPlayer);
        modelplayer.renderLeftArm();
    }
    
    @Override
    protected void renderLivingAt(final AbstractClientPlayer entityLivingBaseIn, final double x, final double y, final double z) {
        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping()) {
            super.renderLivingAt(entityLivingBaseIn, x + entityLivingBaseIn.renderOffsetX, y + entityLivingBaseIn.renderOffsetY, z + entityLivingBaseIn.renderOffsetZ);
        }
        else {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        }
    }
    
    @Override
    protected void rotateCorpse(final AbstractClientPlayer bat, final float p_77043_2_, final float p_77043_3_, final float partialTicks) {
        if (bat.isEntityAlive() && bat.isPlayerSleeping()) {
            GlStateManager.rotate(bat.getBedOrientationInDegrees(), 0.0f, 1.0f, 0.0f);
            GlStateManager.rotate(this.getDeathMaxRotation(bat), 0.0f, 0.0f, 1.0f);
            GlStateManager.rotate(270.0f, 0.0f, 1.0f, 0.0f);
        }
        else {
            super.rotateCorpse(bat, p_77043_2_, p_77043_3_, partialTicks);
        }
    }
}
