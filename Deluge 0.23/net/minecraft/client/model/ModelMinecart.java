package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelMinecart extends ModelBase
{
    public ModelRenderer[] sideModels = new ModelRenderer[7];
    private static final String __OBFID = "CL_00000844";

    public ModelMinecart()
    {
        this.sideModels[0] = new ModelRenderer(this, 0, 10);
        this.sideModels[1] = new ModelRenderer(this, 0, 0);
        this.sideModels[2] = new ModelRenderer(this, 0, 0);
        this.sideModels[3] = new ModelRenderer(this, 0, 0);
        this.sideModels[4] = new ModelRenderer(this, 0, 0);
        this.sideModels[5] = new ModelRenderer(this, 44, 10);
        byte var1 = 20;
        byte var2 = 8;
        byte var3 = 16;
        byte var4 = 4;
        this.sideModels[0].addBox((float)(-var1 / 2), (float)(-var3 / 2), -1.0F, var1, var3, 2, 0.0F);
        this.sideModels[0].setRotationPoint(0.0F, (float)var4, 0.0F);
        this.sideModels[5].addBox((float)(-var1 / 2 + 1), (float)(-var3 / 2 + 1), -1.0F, var1 - 2, var3 - 2, 1, 0.0F);
        this.sideModels[5].setRotationPoint(0.0F, (float)var4, 0.0F);
        this.sideModels[1].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.sideModels[1].setRotationPoint((float)(-var1 / 2 + 1), (float)var4, 0.0F);
        this.sideModels[2].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.sideModels[2].setRotationPoint((float)(var1 / 2 - 1), (float)var4, 0.0F);
        this.sideModels[3].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.sideModels[3].setRotationPoint(0.0F, (float)var4, (float)(-var3 / 2 + 1));
        this.sideModels[4].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.sideModels[4].setRotationPoint(0.0F, (float)var4, (float)(var3 / 2 - 1));
        this.sideModels[0].rotateAngleX = ((float)Math.PI / 2F);
        this.sideModels[1].rotateAngleY = ((float)Math.PI * 3F / 2F);
        this.sideModels[2].rotateAngleY = ((float)Math.PI / 2F);
        this.sideModels[3].rotateAngleY = (float)Math.PI;
        this.sideModels[5].rotateAngleX = -((float)Math.PI / 2F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.sideModels[5].rotationPointY = 4.0F - p_78088_4_;

        for (int var8 = 0; var8 < 6; ++var8)
        {
            this.sideModels[var8].render(p_78088_7_);
        }
    }
}
