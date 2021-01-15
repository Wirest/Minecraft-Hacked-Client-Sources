package net.minecraft.client.model;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelBiped extends ModelBase
{
    public ModelRenderer bipedHead;
    
    /** The Biped's Headwear. Used for the outer layer of player skins. */
    public ModelRenderer bipedHeadwear;
    public ModelRenderer bipedBody;
    
    /** The Biped's Right Arm */
    public ModelRenderer bipedRightArm;
    
    /** The Biped's Left Arm */
    public ModelRenderer bipedLeftArm;
    
    /** The Biped's Right Leg */
    public ModelRenderer bipedRightLeg;
    
    /** The Biped's Left Leg */
    public ModelRenderer bipedLeftLeg;
    
    /**
     * Records whether the model should be rendered holding an item in the left hand, and if that item is a block.
     */
    public int heldItemLeft;
    
    /**
     * Records whether the model should be rendered holding an item in the right hand, and if that item is a block.
     */
    public int heldItemRight;
    public boolean isSneak;
    
    /** Records whether the model should be rendered aiming a bow. */
    public boolean aimedBow;
    
    public ModelBiped()
    {
	this(0.0F);
    }
    
    public ModelBiped(float modelSize)
    {
	this(modelSize, 0.0F, 64, 32);
    }
    
    public ModelBiped(float modelSize, float p_i1149_2_, int textureWidthIn, int textureHeightIn)
    {
	this.textureWidth = textureWidthIn;
	this.textureHeight = textureHeightIn;
	this.bipedHead = new ModelRenderer(this, 0, 0);
	this.bipedHead.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize);
	this.bipedHead.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	this.bipedHeadwear = new ModelRenderer(this, 32, 0);
	this.bipedHeadwear.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8, modelSize + 0.5F);
	this.bipedHeadwear.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	this.bipedBody = new ModelRenderer(this, 16, 16);
	this.bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, modelSize);
	this.bipedBody.setRotationPoint(0.0F, 0.0F + p_i1149_2_, 0.0F);
	this.bipedRightArm = new ModelRenderer(this, 40, 16);
	this.bipedRightArm.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
	this.bipedRightArm.setRotationPoint(-5.0F, 2.0F + p_i1149_2_, 0.0F);
	this.bipedLeftArm = new ModelRenderer(this, 40, 16);
	this.bipedLeftArm.mirror = true;
	this.bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4, modelSize);
	this.bipedLeftArm.setRotationPoint(5.0F, 2.0F + p_i1149_2_, 0.0F);
	this.bipedRightLeg = new ModelRenderer(this, 0, 16);
	this.bipedRightLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
	this.bipedRightLeg.setRotationPoint(-1.9F, 12.0F + p_i1149_2_, 0.0F);
	this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
	this.bipedLeftLeg.mirror = true;
	this.bipedLeftLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, modelSize);
	this.bipedLeftLeg.setRotationPoint(1.9F, 12.0F + p_i1149_2_, 0.0F);
    }
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale)
    {
	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale, entityIn);
	GlStateManager.pushMatrix();
	
	if (this.isChild)
	{
	    float var8 = 2.0F;
	    GlStateManager.scale(1.5F / var8, 1.5F / var8, 1.5F / var8);
	    GlStateManager.translate(0.0F, 16.0F * scale, 0.0F);
	    this.bipedHead.render(scale);
	    GlStateManager.popMatrix();
	    GlStateManager.pushMatrix();
	    GlStateManager.scale(1.0F / var8, 1.0F / var8, 1.0F / var8);
	    GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
	    this.bipedBody.render(scale);
	    this.bipedRightArm.render(scale);
	    this.bipedLeftArm.render(scale);
	    this.bipedRightLeg.render(scale);
	    this.bipedLeftLeg.render(scale);
	    this.bipedHeadwear.render(scale);
	}
	else
	{
	    if (entityIn.isSneaking())
	    {
		GlStateManager.translate(0.0F, 0.2F, 0.0F);
	    }
	    
	    this.bipedHead.render(scale);
	    this.bipedBody.render(scale);
	    this.bipedRightArm.render(scale);
	    this.bipedLeftArm.render(scale);
	    this.bipedRightLeg.render(scale);
	    this.bipedLeftLeg.render(scale);
	    this.bipedHeadwear.render(scale);
	}
	
	GlStateManager.popMatrix();
    }
    
    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
	this.bipedHead.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
	this.bipedHead.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
	this.bipedRightArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 2.0F * p_78087_2_ * 0.5F;
	this.bipedLeftArm.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 2.0F * p_78087_2_ * 0.5F;
	this.bipedRightArm.rotateAngleZ = 0.0F;
	this.bipedLeftArm.rotateAngleZ = 0.0F;
	this.bipedRightLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_;
	this.bipedLeftLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + (float) Math.PI) * 1.4F * p_78087_2_;
	this.bipedRightLeg.rotateAngleY = 0.0F;
	this.bipedLeftLeg.rotateAngleY = 0.0F;
	
	if (this.isRiding)
	{
	    this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
	    this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
	    this.bipedRightLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
	    this.bipedLeftLeg.rotateAngleX = -((float) Math.PI * 2F / 5F);
	    this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 10F);
	    this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 10F);
	}
	
	if (this.heldItemLeft != 0)
	{
	    this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * this.heldItemLeft;
	}
	
	this.bipedRightArm.rotateAngleY = 0.0F;
	this.bipedRightArm.rotateAngleZ = 0.0F;
	
	switch (this.heldItemRight)
	{
	case 0:
	case 2:
	default:
	    break;
	    
	case 1:
	    this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * this.heldItemRight;
	    break;
	    
	case 3:
	    this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F) * this.heldItemRight;
	    //Marker 2
	    //this.bipedRightArm.rotateAngleY = -0.5235988F;
	}
	
	this.bipedLeftArm.rotateAngleY = 0.0F;
	float var8;
	float var9;
	
	if (this.swingProgress > -9990.0F)
	{
	    var8 = this.swingProgress;
	    this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(var8) * (float) Math.PI * 2.0F) * 0.2F;
	    this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
	    this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
	    this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
	    this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
	    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
	    this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
	    this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
	    var8 = 1.0F - this.swingProgress;
	    var8 *= var8;
	    var8 *= var8;
	    var8 = 1.0F - var8;
	    var9 = MathHelper.sin(var8 * (float) Math.PI);
	    float var10 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;
	    this.bipedRightArm.rotateAngleX = (float) (this.bipedRightArm.rotateAngleX - (var9 * 1.2D + var10));
	    this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
	    this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
	}
	
	if (this.isSneak)
	{
	    this.bipedBody.rotateAngleX = 0.5F;
	    this.bipedRightArm.rotateAngleX += 0.4F;
	    this.bipedLeftArm.rotateAngleX += 0.4F;
	    this.bipedRightLeg.rotationPointZ = 4.0F;
	    this.bipedLeftLeg.rotationPointZ = 4.0F;
	    this.bipedRightLeg.rotationPointY = 9.0F;
	    this.bipedLeftLeg.rotationPointY = 9.0F;
	    this.bipedHead.rotationPointY = 1.0F;
	}
	else
	{
	    this.bipedBody.rotateAngleX = 0.0F;
	    this.bipedRightLeg.rotationPointZ = 0.1F;
	    this.bipedLeftLeg.rotationPointZ = 0.1F;
	    this.bipedRightLeg.rotationPointY = 12.0F;
	    this.bipedLeftLeg.rotationPointY = 12.0F;
	    this.bipedHead.rotationPointY = 0.0F;
	}
	
	this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
	this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
	this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
	this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
	
	if (this.aimedBow)
	{
	    var8 = 0.0F;
	    var9 = 0.0F;
	    this.bipedRightArm.rotateAngleZ = 0.0F;
	    this.bipedLeftArm.rotateAngleZ = 0.0F;
	    this.bipedRightArm.rotateAngleY = -(0.1F - var8 * 0.6F) + this.bipedHead.rotateAngleY;
	    this.bipedLeftArm.rotateAngleY = 0.1F - var8 * 0.6F + this.bipedHead.rotateAngleY + 0.4F;
	    this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
	    this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
	    this.bipedRightArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
	    this.bipedLeftArm.rotateAngleX -= var8 * 1.2F - var9 * 0.4F;
	    this.bipedRightArm.rotateAngleZ += MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
	    this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(p_78087_3_ * 0.09F) * 0.05F + 0.05F;
	    this.bipedRightArm.rotateAngleX += MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
	    this.bipedLeftArm.rotateAngleX -= MathHelper.sin(p_78087_3_ * 0.067F) * 0.05F;
	}
	
	copyModelAngles(this.bipedHead, this.bipedHeadwear);
    }
    
    @Override
    public void setModelAttributes(ModelBase model)
    {
	super.setModelAttributes(model);
	
	if (model instanceof ModelBiped)
	{
	    ModelBiped var2 = (ModelBiped) model;
	    this.heldItemLeft = var2.heldItemLeft;
	    this.heldItemRight = var2.heldItemRight;
	    this.isSneak = var2.isSneak;
	    this.aimedBow = var2.aimedBow;
	}
    }
    
    public void setInvisible(boolean invisible)
    {
	this.bipedHead.showModel = invisible;
	this.bipedHeadwear.showModel = invisible;
	this.bipedBody.showModel = invisible;
	this.bipedRightArm.showModel = invisible;
	this.bipedLeftArm.showModel = invisible;
	this.bipedRightLeg.showModel = invisible;
	this.bipedLeftLeg.showModel = invisible;
    }
    
    public void postRenderArm(float scale)
    {
	this.bipedRightArm.postRender(scale);
    }
}
