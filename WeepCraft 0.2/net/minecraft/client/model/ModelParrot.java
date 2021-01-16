package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityParrot;
import net.minecraft.util.math.MathHelper;

public class ModelParrot extends ModelBase
{
    ModelRenderer field_192764_a;
    ModelRenderer field_192765_b;
    ModelRenderer field_192766_c;
    ModelRenderer field_192767_d;
    ModelRenderer field_192768_e;
    ModelRenderer field_192769_f;
    ModelRenderer field_192770_g;
    ModelRenderer field_192771_h;
    ModelRenderer field_192772_i;
    ModelRenderer field_192773_j;
    ModelRenderer field_192774_k;
    private ModelParrot.State field_192775_l = ModelParrot.State.STANDING;

    public ModelParrot()
    {
        this.textureWidth = 32;
        this.textureHeight = 32;
        this.field_192764_a = new ModelRenderer(this, 2, 8);
        this.field_192764_a.addBox(-1.5F, 0.0F, -1.5F, 3, 6, 3);
        this.field_192764_a.setRotationPoint(0.0F, 16.5F, -3.0F);
        this.field_192765_b = new ModelRenderer(this, 22, 1);
        this.field_192765_b.addBox(-1.5F, -1.0F, -1.0F, 3, 4, 1);
        this.field_192765_b.setRotationPoint(0.0F, 21.07F, 1.16F);
        this.field_192766_c = new ModelRenderer(this, 19, 8);
        this.field_192766_c.addBox(-0.5F, 0.0F, -1.5F, 1, 5, 3);
        this.field_192766_c.setRotationPoint(1.5F, 16.94F, -2.76F);
        this.field_192767_d = new ModelRenderer(this, 19, 8);
        this.field_192767_d.addBox(-0.5F, 0.0F, -1.5F, 1, 5, 3);
        this.field_192767_d.setRotationPoint(-1.5F, 16.94F, -2.76F);
        this.field_192768_e = new ModelRenderer(this, 2, 2);
        this.field_192768_e.addBox(-1.0F, -1.5F, -1.0F, 2, 3, 2);
        this.field_192768_e.setRotationPoint(0.0F, 15.69F, -2.76F);
        this.field_192769_f = new ModelRenderer(this, 10, 0);
        this.field_192769_f.addBox(-1.0F, -0.5F, -2.0F, 2, 1, 4);
        this.field_192769_f.setRotationPoint(0.0F, -2.0F, -1.0F);
        this.field_192768_e.addChild(this.field_192769_f);
        this.field_192770_g = new ModelRenderer(this, 11, 7);
        this.field_192770_g.addBox(-0.5F, -1.0F, -0.5F, 1, 2, 1);
        this.field_192770_g.setRotationPoint(0.0F, -0.5F, -1.5F);
        this.field_192768_e.addChild(this.field_192770_g);
        this.field_192771_h = new ModelRenderer(this, 16, 7);
        this.field_192771_h.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        this.field_192771_h.setRotationPoint(0.0F, -1.75F, -2.45F);
        this.field_192768_e.addChild(this.field_192771_h);
        this.field_192772_i = new ModelRenderer(this, 2, 18);
        this.field_192772_i.addBox(0.0F, -4.0F, -2.0F, 0, 5, 4);
        this.field_192772_i.setRotationPoint(0.0F, -2.15F, 0.15F);
        this.field_192768_e.addChild(this.field_192772_i);
        this.field_192773_j = new ModelRenderer(this, 14, 18);
        this.field_192773_j.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        this.field_192773_j.setRotationPoint(1.0F, 22.0F, -1.05F);
        this.field_192774_k = new ModelRenderer(this, 14, 18);
        this.field_192774_k.addBox(-0.5F, 0.0F, -0.5F, 1, 2, 1);
        this.field_192774_k.setRotationPoint(-1.0F, 22.0F, -1.05F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        this.field_192764_a.render(scale);
        this.field_192766_c.render(scale);
        this.field_192767_d.render(scale);
        this.field_192765_b.render(scale);
        this.field_192768_e.render(scale);
        this.field_192773_j.render(scale);
        this.field_192774_k.render(scale);
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        float f = ageInTicks * 0.3F;
        this.field_192768_e.rotateAngleX = headPitch * 0.017453292F;
        this.field_192768_e.rotateAngleY = netHeadYaw * 0.017453292F;
        this.field_192768_e.rotateAngleZ = 0.0F;
        this.field_192768_e.rotationPointX = 0.0F;
        this.field_192764_a.rotationPointX = 0.0F;
        this.field_192765_b.rotationPointX = 0.0F;
        this.field_192767_d.rotationPointX = -1.5F;
        this.field_192766_c.rotationPointX = 1.5F;

        if (this.field_192775_l != ModelParrot.State.FLYING)
        {
            if (this.field_192775_l == ModelParrot.State.SITTING)
            {
                return;
            }

            if (this.field_192775_l == ModelParrot.State.PARTY)
            {
                float f1 = MathHelper.cos((float)entityIn.ticksExisted);
                float f2 = MathHelper.sin((float)entityIn.ticksExisted);
                this.field_192768_e.rotationPointX = f1;
                this.field_192768_e.rotationPointY = 15.69F + f2;
                this.field_192768_e.rotateAngleX = 0.0F;
                this.field_192768_e.rotateAngleY = 0.0F;
                this.field_192768_e.rotateAngleZ = MathHelper.sin((float)entityIn.ticksExisted) * 0.4F;
                this.field_192764_a.rotationPointX = f1;
                this.field_192764_a.rotationPointY = 16.5F + f2;
                this.field_192766_c.rotateAngleZ = -0.0873F - ageInTicks;
                this.field_192766_c.rotationPointX = 1.5F + f1;
                this.field_192766_c.rotationPointY = 16.94F + f2;
                this.field_192767_d.rotateAngleZ = 0.0873F + ageInTicks;
                this.field_192767_d.rotationPointX = -1.5F + f1;
                this.field_192767_d.rotationPointY = 16.94F + f2;
                this.field_192765_b.rotationPointX = f1;
                this.field_192765_b.rotationPointY = 21.07F + f2;
                return;
            }

            this.field_192773_j.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            this.field_192774_k.rotateAngleX += MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount;
        }

        this.field_192768_e.rotationPointY = 15.69F + f;
        this.field_192765_b.rotateAngleX = 1.015F + MathHelper.cos(limbSwing * 0.6662F) * 0.3F * limbSwingAmount;
        this.field_192765_b.rotationPointY = 21.07F + f;
        this.field_192764_a.rotationPointY = 16.5F + f;
        this.field_192766_c.rotateAngleZ = -0.0873F - ageInTicks;
        this.field_192766_c.rotationPointY = 16.94F + f;
        this.field_192767_d.rotateAngleZ = 0.0873F + ageInTicks;
        this.field_192767_d.rotationPointY = 16.94F + f;
        this.field_192773_j.rotationPointY = 22.0F + f;
        this.field_192774_k.rotationPointY = 22.0F + f;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
        this.field_192772_i.rotateAngleX = -0.2214F;
        this.field_192764_a.rotateAngleX = 0.4937F;
        this.field_192766_c.rotateAngleX = -((float)Math.PI * 2F / 9F);
        this.field_192766_c.rotateAngleY = -(float)Math.PI;
        this.field_192767_d.rotateAngleX = -((float)Math.PI * 2F / 9F);
        this.field_192767_d.rotateAngleY = -(float)Math.PI;
        this.field_192773_j.rotateAngleX = -0.0299F;
        this.field_192774_k.rotateAngleX = -0.0299F;
        this.field_192773_j.rotationPointY = 22.0F;
        this.field_192774_k.rotationPointY = 22.0F;

        if (entitylivingbaseIn instanceof EntityParrot)
        {
            EntityParrot entityparrot = (EntityParrot)entitylivingbaseIn;

            if (entityparrot.func_192004_dr())
            {
                this.field_192773_j.rotateAngleZ = -0.34906584F;
                this.field_192774_k.rotateAngleZ = 0.34906584F;
                this.field_192775_l = ModelParrot.State.PARTY;
                return;
            }

            if (entityparrot.isSitting())
            {
                float f = 1.9F;
                this.field_192768_e.rotationPointY = 17.59F;
                this.field_192765_b.rotateAngleX = 1.5388988F;
                this.field_192765_b.rotationPointY = 22.97F;
                this.field_192764_a.rotationPointY = 18.4F;
                this.field_192766_c.rotateAngleZ = -0.0873F;
                this.field_192766_c.rotationPointY = 18.84F;
                this.field_192767_d.rotateAngleZ = 0.0873F;
                this.field_192767_d.rotationPointY = 18.84F;
                ++this.field_192773_j.rotationPointY;
                ++this.field_192774_k.rotationPointY;
                ++this.field_192773_j.rotateAngleX;
                ++this.field_192774_k.rotateAngleX;
                this.field_192775_l = ModelParrot.State.SITTING;
            }
            else if (entityparrot.func_192002_a())
            {
                this.field_192773_j.rotateAngleX += ((float)Math.PI * 2F / 9F);
                this.field_192774_k.rotateAngleX += ((float)Math.PI * 2F / 9F);
                this.field_192775_l = ModelParrot.State.FLYING;
            }
            else
            {
                this.field_192775_l = ModelParrot.State.STANDING;
            }

            this.field_192773_j.rotateAngleZ = 0.0F;
            this.field_192774_k.rotateAngleZ = 0.0F;
        }
    }

    static enum State
    {
        FLYING,
        STANDING,
        SITTING,
        PARTY;
    }
}
