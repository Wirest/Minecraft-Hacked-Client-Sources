package net.minecraft.client.renderer.entity;

import java.util.Random;

import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.modules.render.NoRender;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class RenderEntityItem extends Render
{
    private final RenderItem itemRenderer;
    private Random field_177079_e = new Random();
    
    public RenderEntityItem(RenderManager renderManagerIn, RenderItem p_i46167_2_)
    {
	super(renderManagerIn);
	this.itemRenderer = p_i46167_2_;
	this.shadowSize = 0.15F;
	this.shadowOpaque = 0.75F;
    }
    
    private int func_177077_a(EntityItem itemIn, double p_177077_2_, double p_177077_4_, double p_177077_6_, float p_177077_8_, IBakedModel p_177077_9_)
    {
	ItemStack var10 = itemIn.getEntityItem();
	Item var11 = var10.getItem();
	
	if (var11 == null)
	{
	    return 0;
	}
	else
	{
	    boolean var12 = p_177077_9_.isGui3d();
	    int var13 = this.func_177078_a(var10);
	    float var15 = MathHelper.sin((itemIn.getAge() + p_177077_8_) / 10.0F + itemIn.hoverStart) * 0.1F + 0.1F;
	    GlStateManager.translate((float) p_177077_2_, (float) p_177077_4_ + var15 + 0.25F, (float) p_177077_6_);
	    float var16;
	    
	    if (var12 || this.renderManager.options != null && this.renderManager.options.fancyGraphics)
	    {
		var16 = ((itemIn.getAge() + p_177077_8_) / 20.0F + itemIn.hoverStart) * (180F / (float) Math.PI);
		GlStateManager.rotate(var16, 0.0F, 1.0F, 0.0F);
	    }
	    
	    if (!var12)
	    {
		var16 = -0.0F * (var13 - 1) * 0.5F;
		float var17 = -0.0F * (var13 - 1) * 0.5F;
		float var18 = -0.046875F * (var13 - 1) * 0.5F;
		GlStateManager.translate(var16, var17, var18);
	    }
	    
	    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	    return var13;
	}
    }
    
    private int func_177078_a(ItemStack stack)
    {
	byte var2 = 1;
	
	if (stack.stackSize > 48)
	{
	    var2 = 5;
	}
	else if (stack.stackSize > 32)
	{
	    var2 = 4;
	}
	else if (stack.stackSize > 16)
	{
	    var2 = 3;
	}
	else if (stack.stackSize > 1)
	{
	    var2 = 2;
	}
	
	return var2;
    }
    
    public void func_177075_a(EntityItem itemIn, double p_177075_2_, double p_177075_4_, double p_177075_6_, float p_177075_8_, float p_177075_9_)
    {
	ItemStack var10 = itemIn.getEntityItem();
	this.field_177079_e.setSeed(187L);
	boolean var11 = false;
	
	if (this.bindEntityTexture(itemIn))
	{
	    this.renderManager.renderEngine.getTexture(this.func_177076_a(itemIn)).setBlurMipmap(false, false);
	    var11 = true;
	}
	
	GlStateManager.enableRescaleNormal();
	GlStateManager.alphaFunc(516, 0.1F);
	GlStateManager.enableBlend();
	GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	GlStateManager.pushMatrix();
	IBakedModel var12 = this.itemRenderer.getItemModelMesher().getItemModel(var10);
	int var13 = this.func_177077_a(itemIn, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_9_, var12);
	
	for (int var14 = 0; var14 < var13; ++var14)
	{
	    if (var12.isGui3d())
	    {
		GlStateManager.pushMatrix();
		
		if (var14 > 0)
		{
		    float var15 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
		    float var16 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
		    float var17 = (this.field_177079_e.nextFloat() * 2.0F - 1.0F) * 0.15F;
		    GlStateManager.translate(var15, var16, var17);
		}
		
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		this.itemRenderer.renderItem(var10, var12);
		GlStateManager.popMatrix();
	    }
	    else
	    {
		this.itemRenderer.renderItem(var10, var12);
		GlStateManager.translate(0.0F, 0.0F, 0.046875F);
	    }
	}
	
	GlStateManager.popMatrix();
	GlStateManager.disableRescaleNormal();
	GlStateManager.disableBlend();
	this.bindEntityTexture(itemIn);
	
	if (var11)
	{
	    this.renderManager.renderEngine.getTexture(this.func_177076_a(itemIn)).restoreLastBlurMipmap();
	}
	
	super.doRender(itemIn, p_177075_2_, p_177075_4_, p_177075_6_, p_177075_8_, p_177075_9_);
    }
    
    protected ResourceLocation func_177076_a(EntityItem itemIn)
    {
	return TextureMap.locationBlocksTexture;
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity entity)
    {
	return this.func_177076_a((EntityItem) entity);
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
      if (!ModuleManager.getModule(NoRender.class).enabled) {
        func_177075_a((EntityItem)entity, x, y, z, entityYaw, partialTicks);
      }
    }
}