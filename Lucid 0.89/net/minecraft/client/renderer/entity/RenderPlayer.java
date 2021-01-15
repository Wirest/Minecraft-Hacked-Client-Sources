package net.minecraft.client.renderer.entity;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerCape;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerDeadmau5Head;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;

public class RenderPlayer extends RendererLivingEntity
{
    public RenderPlayer(RenderManager renderManager)
    {
        this(renderManager, false);
    }

    public RenderPlayer(RenderManager renderManager, boolean useSmallArms)
    {
        super(renderManager, new ModelPlayer(0.0F, useSmallArms), 0.5F);
        this.addLayer(new LayerBipedArmor(this));
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerArrow(this));
        this.addLayer(new LayerDeadmau5Head(this));
        this.addLayer(new LayerCape(this));
        this.addLayer(new LayerCustomHead(this.getPlayerModel().bipedHead));
    }

    /**
     * returns the more specialized type of the model as the player model.
     */
    public ModelPlayer getPlayerModel()
    {
        return (ModelPlayer)super.getMainModel();
    }

    public void doRender(AbstractClientPlayer clientPlayer, double p_180596_2_, double p_180596_4_, double p_180596_6_, float p_180596_8_, float p_180596_9_)
    {
        if (!clientPlayer.isUser() || this.renderManager.livingPlayer == clientPlayer)
        {
            double var10 = p_180596_4_;

            if (clientPlayer.isSneaking() && !(clientPlayer instanceof EntityPlayerSP))
            {
                var10 = p_180596_4_ - 0.125D;
            }

            this.setModelVisibilities(clientPlayer);
            super.doRender(clientPlayer, p_180596_2_, var10, p_180596_6_, p_180596_8_, p_180596_9_);
        }
    }

    private void setModelVisibilities(AbstractClientPlayer clientPlayer)
    {
        ModelPlayer var2 = this.getPlayerModel();

        if (clientPlayer.isSpectator())
        {
            var2.setInvisible(false);
            var2.bipedHead.showModel = true;
            var2.bipedHeadwear.showModel = true;
        }
        else
        {
            ItemStack var3 = clientPlayer.inventory.getCurrentItem();
            var2.setInvisible(true);
            var2.bipedHeadwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.HAT);
            var2.bipedBodyWear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
            var2.bipedLeftLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
            var2.bipedRightLegwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
            var2.bipedLeftArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
            var2.bipedRightArmwear.showModel = clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
            var2.heldItemLeft = 0;
            var2.aimedBow = false;
            var2.isSneak = clientPlayer.isSneaking();

            if (var3 == null)
            {
                var2.heldItemRight = 0;
            }
            else
            {
                var2.heldItemRight = 1;

                if (clientPlayer.getItemInUseCount() > 0)
                {
                    EnumAction var4 = var3.getItemUseAction();

                    if (var4 == EnumAction.BLOCK)
                    {
                        var2.heldItemRight = 3;
                    }
                    else if (var4 == EnumAction.BOW)
                    {
                        var2.aimedBow = true;
                    }
                }
            }
        }
    }

    protected ResourceLocation getEntityTexture(AbstractClientPlayer clientPlayer)
    {
        return clientPlayer.getLocationSkin();
    }

    @Override
	public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.0F, 0.1875F, 0.0F);
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(AbstractClientPlayer entitylivingbaseIn, float partialTickTime)
    {
        float var3 = 0.9375F;
        GlStateManager.scale(var3, var3, var3);
    }

    protected void renderOffsetLivingLabel(AbstractClientPlayer clientPlayer, double x, double y, double z, String str, float p_96449_9_, double p_96449_10_)
    {
        if (p_96449_10_ < 100.0D)
        {
            Scoreboard var12 = clientPlayer.getWorldScoreboard();
            ScoreObjective var13 = var12.getObjectiveInDisplaySlot(2);

            if (var13 != null)
            {
                Score var14 = var12.getValueFromObjective(clientPlayer.getCommandSenderName(), var13);
                this.renderLivingLabel(clientPlayer, var14.getScorePoints() + " " + var13.getDisplayName(), x, y, z, 64);
                y += this.getFontRendererFromRenderManager().FONT_HEIGHT * 1.15F * p_96449_9_;
            }
        }

        super.renderOffsetLivingLabel(clientPlayer, x, y, z, str, p_96449_9_, p_96449_10_);
    }

    public void renderRightArm(AbstractClientPlayer clientPlayer)
    {
        float var2 = 1.0F;
        GlStateManager.color(var2, var2, var2);
        ModelPlayer var3 = this.getPlayerModel();
        this.setModelVisibilities(clientPlayer);
        var3.swingProgress = 0.0F;
        var3.isSneak = false;
        var3.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        var3.renderRightArm();
    }

    public void renderLeftArm(AbstractClientPlayer clientPlayer)
    {
        float var2 = 1.0F;
        GlStateManager.color(var2, var2, var2);
        ModelPlayer var3 = this.getPlayerModel();
        this.setModelVisibilities(clientPlayer);
        var3.isSneak = false;
        var3.swingProgress = 0.0F;
        var3.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, clientPlayer);
        var3.renderLeftArm();
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(AbstractClientPlayer entityLivingBaseIn, double x, double y, double z)
    {
        if (entityLivingBaseIn.isEntityAlive() && entityLivingBaseIn.isPlayerSleeping())
        {
            super.renderLivingAt(entityLivingBaseIn, x + entityLivingBaseIn.renderOffsetX, y + entityLivingBaseIn.renderOffsetY, z + entityLivingBaseIn.renderOffsetZ);
        }
        else
        {
            super.renderLivingAt(entityLivingBaseIn, x, y, z);
        }
    }

    protected void rotateCorpse(AbstractClientPlayer clientPlayer, float p_180595_2_, float p_180595_3_, float p_180595_4_)
    {
        if (clientPlayer.isEntityAlive() && clientPlayer.isPlayerSleeping())
        {
            GlStateManager.rotate(clientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.getDeathMaxRotation(clientPlayer), 0.0F, 0.0F, 1.0F);
            GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
        }
        else
        {
            super.rotateCorpse(clientPlayer, p_180595_2_, p_180595_3_, p_180595_4_);
        }
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    @Override
	protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime)
    {
        this.preRenderCallback((AbstractClientPlayer)entitylivingbaseIn, partialTickTime);
    }

    @Override
	protected void rotateCorpse(EntityLivingBase bat, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
        this.rotateCorpse((AbstractClientPlayer)bat, p_77043_2_, p_77043_3_, p_77043_4_);
    }

    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    @Override
	protected void renderLivingAt(EntityLivingBase entityLivingBaseIn, double x, double y, double z)
    {
        this.renderLivingAt((AbstractClientPlayer)entityLivingBaseIn, x, y, z);
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
	public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        this.doRender((AbstractClientPlayer)entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
	public ModelBase getMainModel()
    {
        return this.getPlayerModel();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((AbstractClientPlayer)entity);
    }

    @Override
	protected void renderOffsetLivingLabel(Entity entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_)
    {
        this.renderOffsetLivingLabel((AbstractClientPlayer)entityIn, x, y, z, str, p_177069_9_, p_177069_10_);
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
        this.doRender((AbstractClientPlayer)entity, x, y, z, entityYaw, partialTicks);
    }
}
