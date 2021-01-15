package net.minecraft.client.renderer.entity;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.modules.render.Tags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

public abstract class RendererLivingEntity extends Render
{
    private static final Logger logger = LogManager.getLogger();
    private static final DynamicTexture field_177096_e = new DynamicTexture(16, 16);
    protected ModelBase mainModel;
    protected FloatBuffer brightnessBuffer = GLAllocation.createDirectFloatBuffer(4);
    protected List layerRenderers = Lists.newArrayList();
    protected boolean renderOutlines = false;
    
    public RendererLivingEntity(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
	super(renderManagerIn);
	this.mainModel = modelBaseIn;
	this.shadowSize = shadowSizeIn;
    }
    
    protected boolean addLayer(LayerRenderer layer)
    {
	return this.layerRenderers.add(layer);
    }
    
    protected boolean removeLayer(LayerRenderer layer)
    {
	return this.layerRenderers.remove(layer);
    }
    
    public ModelBase getMainModel()
    {
	return this.mainModel;
    }
    
    /**
     * Returns a rotation angle that is inbetween two other rotation angles. par1 and par2 are the angles between which
     * to interpolate, par3 is probably a float between 0.0 and 1.0 that tells us where "between" the two angles we are.
     * Example: par1 = 30, par2 = 50, par3 = 0.5, then return = 40
     */
    protected float interpolateRotation(float par1, float par2, float par3)
    {
	float var4;
	
	for (var4 = par2 - par1; var4 < -180.0F; var4 += 360.0F)
	{
	    ;
	}
	
	while (var4 >= 180.0F)
	{
	    var4 -= 360.0F;
	}
	
	return par1 + par3 * var4;
    }
    
    public void transformHeldFull3DItemLayer()
    {
    }
    
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(EntityLivingBase entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
	GlStateManager.pushMatrix();
	GlStateManager.disableCull();
	this.mainModel.swingProgress = this.getSwingProgress(entity, partialTicks);
	this.mainModel.isRiding = entity.isRiding();
	this.mainModel.isChild = entity.isChild();
	
	try
	{
	    float var10 = this.interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, partialTicks);
	    float var11 = this.interpolateRotation(entity.prevRotationYawHead, entity.rotationYawHead, partialTicks);
	    float var12 = var11 - var10;
	    float var14;
	    
	    if (entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase)
	    {
		EntityLivingBase var13 = (EntityLivingBase) entity.ridingEntity;
		var10 = this.interpolateRotation(var13.prevRenderYawOffset, var13.renderYawOffset, partialTicks);
		var12 = var11 - var10;
		var14 = MathHelper.wrapAngleTo180_float(var12);
		
		if (var14 < -85.0F)
		{
		    var14 = -85.0F;
		}
		
		if (var14 >= 85.0F)
		{
		    var14 = 85.0F;
		}
		
		var10 = var11 - var14;
		
		if (var14 * var14 > 2500.0F)
		{
		    var10 += var14 * 0.2F;
		}
	    }
	    
	    float var20 = entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks;
	    this.renderLivingAt(entity, x, y, z);
	    var14 = this.handleRotationFloat(entity, partialTicks);
	    this.rotateCorpse(entity, var14, var10, partialTicks);
	    GlStateManager.enableRescaleNormal();
	    GlStateManager.scale(-1.0F, -1.0F, 1.0F);
	    this.preRenderCallback(entity, partialTicks);
	    GlStateManager.translate(0.0F, -1.5078125F, 0.0F);
	    float var16 = entity.prevLimbSwingAmount + (entity.limbSwingAmount - entity.prevLimbSwingAmount) * partialTicks;
	    float var17 = entity.limbSwing - entity.limbSwingAmount * (1.0F - partialTicks);
	    
	    if (entity.isChild())
	    {
		var17 *= 3.0F;
	    }
	    
	    if (var16 > 1.0F)
	    {
		var16 = 1.0F;
	    }
	    
	    GlStateManager.enableAlpha();
	    this.mainModel.setLivingAnimations(entity, var17, var16, partialTicks);
	    this.mainModel.setRotationAngles(var17, var16, var14, var12, var20, 0.0625F, entity);
	    boolean var18;
	    
	    if (this.renderOutlines)
	    {
		var18 = this.setScoreTeamColor(entity);
		this.renderModel(entity, var17, var16, var14, var12, var20, 0.0625F);
		
		if (var18)
		{
		    this.unsetScoreTeamColor();
		}
	    }
	    else
	    {
		var18 = this.setDoRenderBrightness(entity, partialTicks);
		this.renderModel(entity, var17, var16, var14, var12, var20, 0.0625F);
		
		if (var18)
		{
		    this.unsetBrightness();
		}
		
		GlStateManager.depthMask(true);
		
		if (!(entity instanceof EntityPlayer) || !((EntityPlayer) entity).isSpectator())
		{
		    this.renderLayers(entity, var17, var16, partialTicks, var14, var12, var20, 0.0625F);
		}
	    }
	    
	    GlStateManager.disableRescaleNormal();
	}
	catch (Exception var19)
	{
	    logger.error("Couldn\'t render entity", var19);
	}
	
	GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	GlStateManager.enableTexture2D();
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	GlStateManager.enableCull();
	GlStateManager.popMatrix();
	
	if (!this.renderOutlines)
	{
	    super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
    }
    
    protected boolean setScoreTeamColor(EntityLivingBase entityLivingBaseIn)
    {
	int var2 = 16777215;
	
	if (entityLivingBaseIn instanceof EntityPlayer)
	{
	    ScorePlayerTeam var3 = (ScorePlayerTeam) entityLivingBaseIn.getTeam();
	    
	    if (var3 != null)
	    {
		String var4 = FontRenderer.getFormatFromString(var3.getColorPrefix());
		
		if (var4.length() >= 2)
		{
		    var2 = this.getFontRendererFromRenderManager().getColorCode(var4.charAt(1));
		}
	    }
	}
	
	float var6 = (var2 >> 16 & 255) / 255.0F;
	float var7 = (var2 >> 8 & 255) / 255.0F;
	float var5 = (var2 & 255) / 255.0F;
	GlStateManager.disableLighting();
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	GlStateManager.color(var6, var7, var5, 1.0F);
	GlStateManager.disableTexture2D();
	GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	GlStateManager.disableTexture2D();
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	return true;
    }
    
    protected void unsetScoreTeamColor()
    {
	GlStateManager.enableLighting();
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	GlStateManager.enableTexture2D();
	GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	GlStateManager.enableTexture2D();
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    /**
     * Renders the model in RenderLiving
     */
    protected void renderModel(EntityLivingBase entitylivingbaseIn, float p_77036_2_, float p_77036_3_, float p_77036_4_, float p_77036_5_, float p_77036_6_, float p_77036_7_)
    {
	boolean var8 = !entitylivingbaseIn.isInvisible();
	boolean var9 = !var8 && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer);
	
	if (var8 || var9)
	{
	    if (!this.bindEntityTexture(entitylivingbaseIn))
	    {
		return;
	    }
	    
	    if (var9)
	    {
		GlStateManager.pushMatrix();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 0.15F);
		GlStateManager.depthMask(false);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.alphaFunc(516, 0.003921569F);
	    }
	    
	    this.mainModel.render(entitylivingbaseIn, p_77036_2_, p_77036_3_, p_77036_4_, p_77036_5_, p_77036_6_, p_77036_7_);
	    
	    if (var9)
	    {
		GlStateManager.disableBlend();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.popMatrix();
		GlStateManager.depthMask(true);
	    }
	}
    }
    
    protected boolean setDoRenderBrightness(EntityLivingBase entityLivingBaseIn, float partialTicks)
    {
	return this.setBrightness(entityLivingBaseIn, partialTicks, true);
    }
    
    protected boolean setBrightness(EntityLivingBase entitylivingbaseIn, float partialTicks, boolean combineTextures)
    {
	float var4 = entitylivingbaseIn.getBrightness(partialTicks);
	int var5 = this.getColorMultiplier(entitylivingbaseIn, var4, partialTicks);
	boolean var6 = (var5 >> 24 & 255) > 0;
	boolean var7 = entitylivingbaseIn.hurtTime > 0 || entitylivingbaseIn.deathTime > 0;
	
	if (!var6 && !var7)
	{
	    return false;
	}
	else if (!var6 && !combineTextures)
	{
	    return false;
	}
	else
	{
	    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	    GlStateManager.enableTexture2D();
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
	    GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	    GlStateManager.enableTexture2D();
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND2_RGB, GL11.GL_SRC_ALPHA);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
	    this.brightnessBuffer.position(0);
	    
	    if (var7)
	    {
		this.brightnessBuffer.put(1.0F);
		this.brightnessBuffer.put(0.0F);
		this.brightnessBuffer.put(0.0F);
		this.brightnessBuffer.put(0.3F);
	    }
	    else
	    {
		float var8 = (var5 >> 24 & 255) / 255.0F;
		float var9 = (var5 >> 16 & 255) / 255.0F;
		float var10 = (var5 >> 8 & 255) / 255.0F;
		float var11 = (var5 & 255) / 255.0F;
		this.brightnessBuffer.put(var9);
		this.brightnessBuffer.put(var10);
		this.brightnessBuffer.put(var11);
		this.brightnessBuffer.put(1.0F - var8);
	    }
	    
	    this.brightnessBuffer.flip();
	    GL11.glTexEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_COLOR, this.brightnessBuffer);
	    GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
	    GlStateManager.enableTexture2D();
	    GlStateManager.bindTexture(field_177096_e.getGlTextureId());
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
	    GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
	    GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	    return true;
	}
    }
    
    protected void unsetBrightness()
    {
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
	GlStateManager.enableTexture2D();
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_ALPHA, OpenGlHelper.GL_PRIMARY_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_ALPHA, GL11.GL_SRC_ALPHA);
	GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
	GlStateManager.disableTexture2D();
	GlStateManager.bindTexture(0);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, OpenGlHelper.GL_COMBINE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_RGB, GL11.GL_MODULATE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND1_RGB, GL11.GL_SRC_COLOR);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_RGB, GL11.GL_TEXTURE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_COMBINE_ALPHA, GL11.GL_MODULATE);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_OPERAND0_ALPHA, GL11.GL_SRC_ALPHA);
	GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, OpenGlHelper.GL_SOURCE0_ALPHA, GL11.GL_TEXTURE);
	GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    /**
     * Sets a simple glTranslate on a LivingEntity.
     */
    protected void renderLivingAt(EntityLivingBase entityLivingBaseIn, double x, double y, double z)
    {
	GlStateManager.translate((float) x, (float) y, (float) z);
    }
    
    protected void rotateCorpse(EntityLivingBase bat, float p_77043_2_, float p_77043_3_, float p_77043_4_)
    {
	GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
	
	if (bat.deathTime > 0)
	{
	    float var5 = (bat.deathTime + p_77043_4_ - 1.0F) / 20.0F * 1.6F;
	    var5 = MathHelper.sqrt_float(var5);
	    
	    if (var5 > 1.0F)
	    {
		var5 = 1.0F;
	    }
	    
	    GlStateManager.rotate(var5 * this.getDeathMaxRotation(bat), 0.0F, 0.0F, 1.0F);
	}
	else
	{
	    String var6 = EnumChatFormatting.getTextWithoutFormattingCodes(bat.getCommandSenderName());
	    
	    if (var6 != null && (var6.equals("Dinnerbone") || var6.equals("Grumm")) && (!(bat instanceof EntityPlayer) || ((EntityPlayer) bat).isWearing(EnumPlayerModelParts.CAPE)))
	    {
		GlStateManager.translate(0.0F, bat.height + 0.1F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
	    }
	}
    }
    
    /**
     * Returns where in the swing animation the living entity is (from 0 to 1).  Args : entity, partialTickTime
     */
    protected float getSwingProgress(EntityLivingBase livingBase, float partialTickTime)
    {
	return livingBase.getSwingProgress(partialTickTime);
    }
    
    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase livingBase, float p_77044_2_)
    {
	return livingBase.ticksExisted + p_77044_2_;
    }
    
    protected void renderLayers(EntityLivingBase entitylivingbaseIn, float p_177093_2_, float p_177093_3_, float p_177093_4_, float p_177093_5_, float p_177093_6_, float p_177093_7_, float p_177093_8_)
    {
	Iterator var9 = this.layerRenderers.iterator();
	
	while (var9.hasNext())
	{
	    LayerRenderer var10 = (LayerRenderer) var9.next();
	    boolean var11 = this.setBrightness(entitylivingbaseIn, p_177093_4_, var10.shouldCombineTextures());
	    var10.doRenderLayer(entitylivingbaseIn, p_177093_2_, p_177093_3_, p_177093_4_, p_177093_5_, p_177093_6_, p_177093_7_, p_177093_8_);
	    
	    if (var11)
	    {
		this.unsetBrightness();
	    }
	}
    }
    
    protected float getDeathMaxRotation(EntityLivingBase entityLivingBaseIn)
    {
	return 90.0F;
    }
    
    /**
     * Returns an ARGB int color back. Args: entityLiving, lightBrightness, partialTickTime
     */
    protected int getColorMultiplier(EntityLivingBase entitylivingbaseIn, float lightBrightness, float partialTickTime)
    {
	return 0;
    }
    
    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityLivingBase entitylivingbaseIn, float partialTickTime)
    {
    }
    
    /**
     * Passes the specialRender and renders it
     */
    public void passSpecialRender(EntityLivingBase entitylivingbaseIn, double x, double y, double z)
    {
      if (canRenderName(entitylivingbaseIn))
      {
        double var8 = entitylivingbaseIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
        float var10 = entitylivingbaseIn.isSneaking() ? 32.0F : 64.0F;
        if ((var8 < var10 * var10) || (ModuleManager.getModule(Tags.class).enabled))
        {
          String var11 = entitylivingbaseIn.getDisplayName().getFormattedText();
          GlStateManager.alphaFunc(516, 0.1F);
          if ((entitylivingbaseIn.isSneaking()) && (!ModuleManager.getModule(Tags.class).enabled))
          {
            FontRenderer var13 = getFontRendererFromRenderManager();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float)x, (float)y + entitylivingbaseIn.height + 0.5F - (entitylivingbaseIn.isChild() ? entitylivingbaseIn.height / 2.0F : 0.0F), (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.02666667F, -0.02666667F, 0.02666667F);
            GlStateManager.translate(0.0F, 9.374999F, 0.0F);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.enableBlend();
            GlStateManager.disableTexture2D();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator var14 = Tessellator.getInstance();
            WorldRenderer var15 = var14.getWorldRenderer();
            var15.startDrawingQuads();
            int var16 = var13.getStringWidth(var11) / 2;
            var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
            var15.addVertex(-var16 - 1, -1.0D, 0.0D);
            var15.addVertex(-var16 - 1, 8.0D, 0.0D);
            var15.addVertex(var16 + 1, 8.0D, 0.0D);
            var15.addVertex(var16 + 1, -1.0D, 0.0D);
            var14.draw();
            GlStateManager.enableTexture2D();
            GlStateManager.depthMask(true);
            var13.drawString(var11, -var13.getStringWidth(var11) / 2, 0, 553648127);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
          }
          else
          {
            renderOffsetLivingLabel(entitylivingbaseIn, x, y - (entitylivingbaseIn.isChild() ? entitylivingbaseIn.height / 2.0F : 0.0D), z, var11, 0.02666667F, var8);
          }
        }
      }
    }
    
    /**
     * Test if the entity name must be rendered
     */
    protected boolean canRenderName(EntityLivingBase targetEntity)
    {
	EntityPlayerSP var2 = Minecraft.getMinecraft().thePlayer;
	
	if (targetEntity instanceof EntityPlayer && targetEntity != var2)
	{
	    Team var3 = targetEntity.getTeam();
	    Team var4 = var2.getTeam();
	    
	    if (var3 != null)
	    {
		Team.EnumVisible var5 = var3.func_178770_i();
		
		switch (RendererLivingEntity.SwitchEnumVisible.field_178679_a[var5.ordinal()])
		{
		case 1:
		    return true;
		    
		case 2:
		    return false;
		    
		case 3:
		    return var4 == null || var3.isSameTeam(var4);
		    
		case 4:
		    return var4 == null || !var3.isSameTeam(var4);
		    
		default:
		    return true;
		}
	    }
	}
	
	return Minecraft.isGuiEnabled() && targetEntity != this.renderManager.livingPlayer && !targetEntity.isInvisibleToPlayer(var2) && targetEntity.riddenByEntity == null;
    }
    
    public void setRenderOutlines(boolean renderOutlinesIn)
    {
	this.renderOutlines = renderOutlinesIn;
    }
    
    @Override
    protected boolean canRenderName(Entity entity)
    {
	return this.canRenderName((EntityLivingBase) entity);
    }
    
    @Override
    public void renderName(Entity entity, double x, double y, double z)
    {
	this.passSpecialRender((EntityLivingBase) entity, x, y, z);
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
	this.doRender((EntityLivingBase) entity, x, y, z, entityYaw, partialTicks);
    }
    
    static
    {
	int[] var0 = field_177096_e.getTextureData();
	
	for (int var1 = 0; var1 < 256; ++var1)
	{
	    var0[var1] = -1;
	}
	
	field_177096_e.updateDynamicTexture();
    }
    
    static final class SwitchEnumVisible
    {
	static final int[] field_178679_a = new int[Team.EnumVisible.values().length];
	
	static
	{
	    try
	    {
		field_178679_a[Team.EnumVisible.ALWAYS.ordinal()] = 1;
	    }
	    catch (NoSuchFieldError var4)
	    {
		;
	    }
	    
	    try
	    {
		field_178679_a[Team.EnumVisible.NEVER.ordinal()] = 2;
	    }
	    catch (NoSuchFieldError var3)
	    {
		;
	    }
	    
	    try
	    {
		field_178679_a[Team.EnumVisible.HIDE_FOR_OTHER_TEAMS.ordinal()] = 3;
	    }
	    catch (NoSuchFieldError var2)
	    {
		;
	    }
	    
	    try
	    {
		field_178679_a[Team.EnumVisible.HIDE_FOR_OWN_TEAM.ordinal()] = 4;
	    }
	    catch (NoSuchFieldError var1)
	    {
		;
	    }
	}
    }
}
