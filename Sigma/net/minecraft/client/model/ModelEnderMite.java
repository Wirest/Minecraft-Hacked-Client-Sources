package net.minecraft.client.model;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelEnderMite extends ModelBase {
    private static final int[][] field_178716_a = new int[][]{{4, 3, 2}, {6, 4, 5}, {3, 3, 1}, {1, 2, 1}};
    private static final int[][] field_178714_b = new int[][]{{0, 0}, {0, 5}, {0, 14}, {0, 18}};
    private static final int field_178715_c = field_178716_a.length;
    private final ModelRenderer[] field_178713_d;
    private static final String __OBFID = "CL_00002629";

    public ModelEnderMite() {
        this.field_178713_d = new ModelRenderer[field_178715_c];
        float var1 = -3.5F;

        for (int var2 = 0; var2 < this.field_178713_d.length; ++var2) {
            this.field_178713_d[var2] = new ModelRenderer(this, field_178714_b[var2][0], field_178714_b[var2][1]);
            this.field_178713_d[var2].addBox((float) field_178716_a[var2][0] * -0.5F, 0.0F, (float) field_178716_a[var2][2] * -0.5F, field_178716_a[var2][0], field_178716_a[var2][1], field_178716_a[var2][2]);
            this.field_178713_d[var2].setRotationPoint(0.0F, (float) (24 - field_178716_a[var2][1]), var1);

            if (var2 < this.field_178713_d.length - 1) {
                var1 += (float) (field_178716_a[var2][2] + field_178716_a[var2 + 1][2]) * 0.5F;
            }
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_) {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

        for (int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
            this.field_178713_d[var8].render(p_78088_7_);
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
        for (int var8 = 0; var8 < this.field_178713_d.length; ++var8) {
            this.field_178713_d[var8].rotateAngleY = MathHelper.cos(p_78087_3_ * 0.9F + (float) var8 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.01F * (float) (1 + Math.abs(var8 - 2));
            this.field_178713_d[var8].rotationPointX = MathHelper.sin(p_78087_3_ * 0.9F + (float) var8 * 0.15F * (float) Math.PI) * (float) Math.PI * 0.1F * (float) Math.abs(var8 - 2);
        }
    }
}
