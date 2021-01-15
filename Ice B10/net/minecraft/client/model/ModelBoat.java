package net.minecraft.client.model;

import net.minecraft.entity.Entity;

public class ModelBoat extends ModelBase
{
    public ModelRenderer[] boatSides = new ModelRenderer[5];
    private static final String __OBFID = "CL_00000832";

    public ModelBoat()
    {
        this.boatSides[0] = new ModelRenderer(this, 0, 8);
        this.boatSides[1] = new ModelRenderer(this, 0, 0);
        this.boatSides[2] = new ModelRenderer(this, 0, 0);
        this.boatSides[3] = new ModelRenderer(this, 0, 0);
        this.boatSides[4] = new ModelRenderer(this, 0, 0);
        byte var1 = 24;
        byte var2 = 6;
        byte var3 = 20;
        byte var4 = 4;
        this.boatSides[0].addBox((float)(-var1 / 2), (float)(-var3 / 2 + 2), -3.0F, var1, var3 - 4, 4, 0.0F);
        this.boatSides[0].setRotationPoint(0.0F, (float)var4, 0.0F);
        this.boatSides[1].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.boatSides[1].setRotationPoint((float)(-var1 / 2 + 1), (float)var4, 0.0F);
        this.boatSides[2].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.boatSides[2].setRotationPoint((float)(var1 / 2 - 1), (float)var4, 0.0F);
        this.boatSides[3].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.boatSides[3].setRotationPoint(0.0F, (float)var4, (float)(-var3 / 2 + 1));
        this.boatSides[4].addBox((float)(-var1 / 2 + 2), (float)(-var2 - 1), -1.0F, var1 - 4, var2, 2, 0.0F);
        this.boatSides[4].setRotationPoint(0.0F, (float)var4, (float)(var3 / 2 - 1));
        this.boatSides[0].rotateAngleX = ((float)Math.PI / 2F);
        this.boatSides[1].rotateAngleY = ((float)Math.PI * 3F / 2F);
        this.boatSides[2].rotateAngleY = ((float)Math.PI / 2F);
        this.boatSides[3].rotateAngleY = (float)Math.PI;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        for (int var8 = 0; var8 < 5; ++var8)
        {
            this.boatSides[var8].render(p_78088_7_);
        }
    }
}
