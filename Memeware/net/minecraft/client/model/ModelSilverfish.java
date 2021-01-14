package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelSilverfish extends ModelBase {
    /**
     * The body parts of the silverfish's model.
     */
    private ModelRenderer[] silverfishBodyParts = new ModelRenderer[7];

    /**
     * The wings (dust-looking sprites) on the silverfish's model.
     */
    private ModelRenderer[] silverfishWings;
    private float[] field_78170_c = new float[7];

    /**
     * The widths, heights, and lengths for the silverfish model boxes.
     */
    private static final int[][] silverfishBoxLength = new int[][]{{3, 2, 2}, {4, 3, 2}, {6, 4, 3}, {3, 3, 3}, {2, 2, 3}, {2, 1, 2}, {1, 1, 2}};

    /**
     * The texture positions for the silverfish's model's boxes.
     */
    private static final int[][] silverfishTexturePositions = new int[][]{{0, 0}, {0, 4}, {0, 9}, {0, 16}, {0, 22}, {11, 0}, {13, 4}};
    private static final String __OBFID = "CL_00000855";

    public ModelSilverfish() {
        float var1 = -3.5F;

        for (int var2 = 0; var2 < this.silverfishBodyParts.length; ++var2) {
            this.silverfishBodyParts[var2] = new ModelRenderer(this, silverfishTexturePositions[var2][0], silverfishTexturePositions[var2][1]);
            this.silverfishBodyParts[var2].addBox((float) silverfishBoxLength[var2][0] * -0.5F, 0.0F, (float) silverfishBoxLength[var2][2] * -0.5F, silverfishBoxLength[var2][0], silverfishBoxLength[var2][1], silverfishBoxLength[var2][2]);
            this.silverfishBodyParts[var2].setRotationPoint(0.0F, (float) (24 - silverfishBoxLength[var2][1]), var1);
            this.field_78170_c[var2] = var1;

            if (var2 < this.silverfishBodyParts.length - 1) {
                var1 += (float) (silverfishBoxLength[var2][2] + silverfishBoxLength[var2 + 1][2]) * 0.5F;
            }
        }

        this.silverfishWings = new ModelRenderer[3];
        this.silverfishWings[0] = new ModelRenderer(this, 20, 0);
        this.silverfishWings[0].addBox(-5.0F, 0.0F, (float) silverfishBoxLength[2][2] * -0.5F, 10, 8, silverfishBoxLength[2][2]);
        this.silverfishWings[0].setRotationPoint(0.0F, 16.0F, this.field_78170_c[2]);
        this.silverfishWings[1] = new ModelRenderer(this, 20, 11);
        this.silverfishWings[1].addBox(-3.0F, 0.0F, (float) silverfishBoxLength[4][2] * -0.5F, 6, 4, silverfishBoxLength[4][2]);
        this.silverfishWings[1].setRotationPoint(0.0F, 20.0F, this.field_78170_c[4]);
        this.silverfishWings[2] = new ModelRenderer(this, 20, 18);
        this.silverfishWings[2].addBox(-3.0F, 0.0F, (float) silverfishBoxLength[4][2] * -0.5F, 6, 5, silverfishBoxLength[1][2]);
        this.silverfishWings[2].setRotationPoint(0.0F, 19.0F, this.field_78170_c[1]);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        int var8;

        for (var8 = 0; var8 < this.silverfishBodyParts.length; ++var8) {
            this.silverfishBodyParts[var8].render(p_78088_7_);
        }

        for (var8 = 0; var8 < this.silverfishWings.length; ++var8) {
            this.silverfishWings[var8].render(p_78088_7_);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        for (int var8 = 0; var8 < this.silverfishBodyParts.length; ++var8) {
            this.silverfishBodyParts[var8].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9F + (float) var8 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.05F * (float) (1 + Math.abs(var8 - 2));
            this.silverfishBodyParts[var8].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9F + (float) var8 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.2F * (float) Math.abs(var8 - 2);
        }

        this.silverfishWings[0].rotateAngleY = this.silverfishBodyParts[2].rotateAngleY;
        this.silverfishWings[1].rotateAngleY = this.silverfishBodyParts[4].rotateAngleY;
        this.silverfishWings[1].rotationPointX = this.silverfishBodyParts[4].rotationPointX;
        this.silverfishWings[2].rotateAngleY = this.silverfishBodyParts[1].rotateAngleY;
        this.silverfishWings[2].rotationPointX = this.silverfishBodyParts[1].rotationPointX;
    }
}
