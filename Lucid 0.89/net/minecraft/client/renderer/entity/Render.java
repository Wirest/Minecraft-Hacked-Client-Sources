package net.minecraft.client.renderer.entity;

import java.util.Iterator;

import org.lwjgl.opengl.GL11;

import me.aristhena.lucid.eventapi.events.NametagRenderEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class Render
{
    private static final ResourceLocation shadowTextures = new ResourceLocation("textures/misc/shadow.png");
    protected final RenderManager renderManager;
    protected float shadowSize;
    
    /**
     * Determines the darkness of the object's shadow. Higher value makes a darker shadow.
     */
    protected float shadowOpaque = 1.0F;
    
    protected Render(RenderManager renderManager)
    {
	this.renderManager = renderManager;
    }
    
    public boolean shouldRender(Entity entity, ICamera camera, double camX, double camY, double camZ)
    {
	return entity.isInRangeToRender3d(camX, camY, camZ) && (entity.ignoreFrustumCheck || camera.isBoundingBoxInFrustum(entity.getEntityBoundingBox()));
    }
    
    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void doRender(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     *  
     * @param entityYaw The yaw rotation of the passed entity
     */
    public void doRender(Entity entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
	this.renderName(entity, x, y, z);
    }
    
    protected void renderName(Entity entity, double x, double y, double z)
    {
	if (this.canRenderName(entity))
	{
	    this.renderLivingLabel(entity, entity.getDisplayName().getFormattedText(), x, y, z, 64);
	}
    }
    
    protected boolean canRenderName(Entity entity)
    {
	return entity.getAlwaysRenderNameTagForRender() && entity.hasCustomName();
    }
    
    protected void renderOffsetLivingLabel(Entity entityIn, double x, double y, double z, String str, float p_177069_9_, double p_177069_10_)
    {
	this.renderLivingLabel(entityIn, str, x, y, z, 64);
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected abstract ResourceLocation getEntityTexture(Entity var1);
    
    protected boolean bindEntityTexture(Entity entity)
    {
	ResourceLocation var2 = this.getEntityTexture(entity);
	
	if (var2 == null)
	{
	    return false;
	}
	else
	{
	    this.bindTexture(var2);
	    return true;
	}
    }
    
    public void bindTexture(ResourceLocation location)
    {
	this.renderManager.renderEngine.bindTexture(location);
    }
    
    /**
     * Renders fire on top of the entity. Args: entity, x, y, z, partialTickTime
     */
    private void renderEntityOnFire(Entity entity, double x, double y, double z, float partialTicks)
    {
	GlStateManager.disableLighting();
	TextureMap var9 = Minecraft.getMinecraft().getTextureMapBlocks();
	TextureAtlasSprite var10 = var9.getAtlasSprite("minecraft:blocks/fire_layer_0");
	TextureAtlasSprite var11 = var9.getAtlasSprite("minecraft:blocks/fire_layer_1");
	GlStateManager.pushMatrix();
	GlStateManager.translate((float) x, (float) y, (float) z);
	float var12 = entity.width * 1.4F;
	GlStateManager.scale(var12, var12, var12);
	Tessellator var13 = Tessellator.getInstance();
	WorldRenderer var14 = var13.getWorldRenderer();
	float var15 = 0.5F;
	float var16 = 0.0F;
	float var17 = entity.height / var12;
	float var18 = (float) (entity.posY - entity.getEntityBoundingBox().minY);
	GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	GlStateManager.translate(0.0F, 0.0F, -0.3F + ((int) var17) * 0.02F);
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	float var19 = 0.0F;
	int var20 = 0;
	var14.startDrawingQuads();
	
	while (var17 > 0.0F)
	{
	    TextureAtlasSprite var21 = var20 % 2 == 0 ? var10 : var11;
	    this.bindTexture(TextureMap.locationBlocksTexture);
	    float var22 = var21.getMinU();
	    float var23 = var21.getMinV();
	    float var24 = var21.getMaxU();
	    float var25 = var21.getMaxV();
	    
	    if (var20 / 2 % 2 == 0)
	    {
		float var26 = var24;
		var24 = var22;
		var22 = var26;
	    }
	    
	    var14.addVertexWithUV(var15 - var16, 0.0F - var18, var19, var24, var25);
	    var14.addVertexWithUV(-var15 - var16, 0.0F - var18, var19, var22, var25);
	    var14.addVertexWithUV(-var15 - var16, 1.4F - var18, var19, var22, var23);
	    var14.addVertexWithUV(var15 - var16, 1.4F - var18, var19, var24, var23);
	    var17 -= 0.45F;
	    var18 -= 0.45F;
	    var15 *= 0.9F;
	    var19 += 0.03F;
	    ++var20;
	}
	
	var13.draw();
	GlStateManager.popMatrix();
	GlStateManager.enableLighting();
    }
    
    /**
     * Renders the entity shadows at the position, shadow alpha and partialTickTime. Args: entity, x, y, z, shadowAlpha,
     * partialTickTime
     */
    private void renderShadow(Entity entityIn, double x, double y, double z, float shadowAlpha, float partialTicks)
    {
	GlStateManager.enableBlend();
	GlStateManager.blendFunc(770, 771);
	this.renderManager.renderEngine.bindTexture(shadowTextures);
	World var10 = this.getWorldFromRenderManager();
	GlStateManager.depthMask(false);
	float var11 = this.shadowSize;
	
	if (entityIn instanceof EntityLiving)
	{
	    EntityLiving var12 = (EntityLiving) entityIn;
	    var11 *= var12.getRenderSizeModifier();
	    
	    if (var12.isChild())
	    {
		var11 *= 0.5F;
	    }
	}
	
	double var35 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * partialTicks;
	double var14 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * partialTicks;
	double var16 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * partialTicks;
	int var18 = MathHelper.floor_double(var35 - var11);
	int var19 = MathHelper.floor_double(var35 + var11);
	int var20 = MathHelper.floor_double(var14 - var11);
	int var21 = MathHelper.floor_double(var14);
	int var22 = MathHelper.floor_double(var16 - var11);
	int var23 = MathHelper.floor_double(var16 + var11);
	double var24 = x - var35;
	double var26 = y - var14;
	double var28 = z - var16;
	Tessellator var30 = Tessellator.getInstance();
	WorldRenderer var31 = var30.getWorldRenderer();
	var31.startDrawingQuads();
	Iterator var32 = BlockPos.getAllInBox(new BlockPos(var18, var20, var22), new BlockPos(var19, var21, var23)).iterator();
	
	while (var32.hasNext())
	{
	    BlockPos var33 = (BlockPos) var32.next();
	    Block var34 = var10.getBlockState(var33.down()).getBlock();
	    
	    if (var34.getRenderType() != -1 && var10.getLightFromNeighbors(var33) > 3)
	    {
		this.func_180549_a(var34, x, y, z, var33, shadowAlpha, var11, var24, var26, var28);
	    }
	}
	
	var30.draw();
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	GlStateManager.disableBlend();
	GlStateManager.depthMask(true);
    }
    
    /**
     * Returns the render manager's world object
     */
    private World getWorldFromRenderManager()
    {
	return this.renderManager.worldObj;
    }
    
    private void func_180549_a(Block blockIn, double p_180549_2_, double p_180549_4_, double p_180549_6_, BlockPos pos, float p_180549_9_, float p_180549_10_, double p_180549_11_, double p_180549_13_, double p_180549_15_)
    {
	if (blockIn.isFullCube())
	{
	    Tessellator var17 = Tessellator.getInstance();
	    WorldRenderer var18 = var17.getWorldRenderer();
	    double var19 = (p_180549_9_ - (p_180549_4_ - (pos.getY() + p_180549_13_)) / 2.0D) * 0.5D * this.getWorldFromRenderManager().getLightBrightness(pos);
	    
	    if (var19 >= 0.0D)
	    {
		if (var19 > 1.0D)
		{
		    var19 = 1.0D;
		}
		
		var18.setColorRGBA_F(1.0F, 1.0F, 1.0F, (float) var19);
		double var21 = pos.getX() + blockIn.getBlockBoundsMinX() + p_180549_11_;
		double var23 = pos.getX() + blockIn.getBlockBoundsMaxX() + p_180549_11_;
		double var25 = pos.getY() + blockIn.getBlockBoundsMinY() + p_180549_13_ + 0.015625D;
		double var27 = pos.getZ() + blockIn.getBlockBoundsMinZ() + p_180549_15_;
		double var29 = pos.getZ() + blockIn.getBlockBoundsMaxZ() + p_180549_15_;
		float var31 = (float) ((p_180549_2_ - var21) / 2.0D / p_180549_10_ + 0.5D);
		float var32 = (float) ((p_180549_2_ - var23) / 2.0D / p_180549_10_ + 0.5D);
		float var33 = (float) ((p_180549_6_ - var27) / 2.0D / p_180549_10_ + 0.5D);
		float var34 = (float) ((p_180549_6_ - var29) / 2.0D / p_180549_10_ + 0.5D);
		var18.addVertexWithUV(var21, var25, var27, var31, var33);
		var18.addVertexWithUV(var21, var25, var29, var31, var34);
		var18.addVertexWithUV(var23, var25, var29, var32, var34);
		var18.addVertexWithUV(var23, var25, var27, var32, var33);
	    }
	}
    }
    
    /**
     * Renders a white box with the bounds of the AABB translated by the offset. Args: aabb, x, y, z
     */
    public static void renderOffsetAABB(AxisAlignedBB boundingBox, double x, double y, double z)
    {
	GlStateManager.disableTexture2D();
	Tessellator var7 = Tessellator.getInstance();
	WorldRenderer var8 = var7.getWorldRenderer();
	GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	var8.startDrawingQuads();
	var8.setTranslation(x, y, z);
	var8.setNormal(0.0F, 0.0F, -1.0F);
	var8.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var8.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var8.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var8.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var8.setNormal(0.0F, 0.0F, 1.0F);
	var8.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var8.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var8.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var8.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var8.setNormal(0.0F, -1.0F, 0.0F);
	var8.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var8.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var8.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var8.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var8.setNormal(0.0F, 1.0F, 0.0F);
	var8.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var8.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var8.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var8.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var8.setNormal(-1.0F, 0.0F, 0.0F);
	var8.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.maxZ);
	var8.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ);
	var8.addVertex(boundingBox.minX, boundingBox.maxY, boundingBox.minZ);
	var8.addVertex(boundingBox.minX, boundingBox.minY, boundingBox.minZ);
	var8.setNormal(1.0F, 0.0F, 0.0F);
	var8.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.minZ);
	var8.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ);
	var8.addVertex(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ);
	var8.addVertex(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ);
	var8.setTranslation(0.0D, 0.0D, 0.0D);
	var7.draw();
	GlStateManager.enableTexture2D();
    }
    
    /**
     * Renders the entity's shadow and fire (if its on fire). Args: entity, x, y, z, yaw, partialTickTime
     */
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks)
    {
	if (this.renderManager.options != null)
	{
	    if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow())
	    {
		double var10 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
		float var12 = (float) ((1.0D - var10 / 256.0D) * this.shadowOpaque);
		
		if (var12 > 0.0F)
		{
		    this.renderShadow(entityIn, x, y, z, var12, partialTicks);
		}
	    }
	    
	    if (entityIn.canRenderOnFire() && (!(entityIn instanceof EntityPlayer) || !((EntityPlayer) entityIn).isSpectator()))
	    {
		this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
	    }
	}
    }
    
    /**
     * Returns the font renderer from the set render manager
     */
    public FontRenderer getFontRendererFromRenderManager()
    {
	return this.renderManager.getFontRenderer();
    }
    
    /**
     * Renders an entity's name above its head
     */
    protected void renderLivingLabel(Entity entityIn, String str, double x, double y, double z, int maxDistance)
    {
	if (entityIn instanceof EntityPlayer)
	{
	    NametagRenderEvent event = new NametagRenderEvent(entityIn, str, x, y, z);
	    event.call();
	    
	    if (event.isCancelled()) return;
	}
	
	double var10 = entityIn.getDistanceSqToEntity(this.renderManager.livingPlayer);
	
	if (var10 <= maxDistance * maxDistance)
	{
	    FontRenderer var12 = this.getFontRendererFromRenderManager();
	    float var13 = 1.6F;
	    float var14 = 0.016666668F * var13;
	    GlStateManager.pushMatrix();
	    GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
	    GL11.glNormal3f(0.0F, 1.0F, 0.0F);
	    GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
	    GlStateManager.rotate(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
	    GlStateManager.scale(-var14, -var14, var14);
	    GlStateManager.disableLighting();
	    GlStateManager.depthMask(false);
	    GlStateManager.disableDepth();
	    GlStateManager.enableBlend();
	    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	    Tessellator var15 = Tessellator.getInstance();
	    WorldRenderer var16 = var15.getWorldRenderer();
	    byte var17 = 0;
	    
	    if (str.equals("deadmau5"))
	    {
		var17 = -10;
	    }
	    
	    GlStateManager.disableTexture2D();
	    var16.startDrawingQuads();
	    int var18 = var12.getStringWidth(str) / 2;
	    var16.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
	    var16.addVertex(-var18 - 1, -1 + var17, 0.0D);
	    var16.addVertex(-var18 - 1, 8 + var17, 0.0D);
	    var16.addVertex(var18 + 1, 8 + var17, 0.0D);
	    var16.addVertex(var18 + 1, -1 + var17, 0.0D);
	    var15.draw();
	    GlStateManager.enableTexture2D();
	    var12.drawString(str, -var12.getStringWidth(str) / 2, var17, 553648127);
	    GlStateManager.enableDepth();
	    GlStateManager.depthMask(true);
	    var12.drawString(str, -var12.getStringWidth(str) / 2, var17, -1);
	    GlStateManager.enableLighting();
	    GlStateManager.disableBlend();
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    GlStateManager.popMatrix();
	}
    }
    
    public RenderManager getRenderManager()
    {
	return this.renderManager;
    }
}
