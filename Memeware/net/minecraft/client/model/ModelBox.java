package net.minecraft.client.model;

import net.minecraft.client.renderer.WorldRenderer;

public class ModelBox {
    /**
     * The (x,y,z) vertex positions and (u,v) texture coordinates for each of the 8 points on a cube
     */
    private PositionTextureVertex[] vertexPositions;

    /**
     * An array of 6 TexturedQuads, one for each face of a cube
     */
    private TexturedQuad[] quadList;

    /**
     * X vertex coordinate of lower box corner
     */
    public final float posX1;

    /**
     * Y vertex coordinate of lower box corner
     */
    public final float posY1;

    /**
     * Z vertex coordinate of lower box corner
     */
    public final float posZ1;

    /**
     * X vertex coordinate of upper box corner
     */
    public final float posX2;

    /**
     * Y vertex coordinate of upper box corner
     */
    public final float posY2;

    /**
     * Z vertex coordinate of upper box corner
     */
    public final float posZ2;
    public String field_78247_g;
    private static final String __OBFID = "CL_00000872";

    public ModelBox(ModelRenderer p_i46359_1_, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, int p_i46359_7_, int p_i46359_8_, int p_i46359_9_, float p_i46359_10_) {
        this(p_i46359_1_, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, p_i46359_1_.mirror);
    }

    public ModelBox(ModelRenderer p_i46301_1_, int p_i46301_2_, int p_i46301_3_, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, int p_i46301_7_, int p_i46301_8_, int p_i46301_9_, float p_i46301_10_, boolean p_i46301_11_) {
        this.posX1 = p_i46301_4_;
        this.posY1 = p_i46301_5_;
        this.posZ1 = p_i46301_6_;
        this.posX2 = p_i46301_4_ + (float) p_i46301_7_;
        this.posY2 = p_i46301_5_ + (float) p_i46301_8_;
        this.posZ2 = p_i46301_6_ + (float) p_i46301_9_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float var12 = p_i46301_4_ + (float) p_i46301_7_;
        float var13 = p_i46301_5_ + (float) p_i46301_8_;
        float var14 = p_i46301_6_ + (float) p_i46301_9_;
        p_i46301_4_ -= p_i46301_10_;
        p_i46301_5_ -= p_i46301_10_;
        p_i46301_6_ -= p_i46301_10_;
        var12 += p_i46301_10_;
        var13 += p_i46301_10_;
        var14 += p_i46301_10_;

        if (p_i46301_11_) {
            float var15 = var12;
            var12 = p_i46301_4_;
            p_i46301_4_ = var15;
        }

        PositionTextureVertex var24 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0F, 0.0F);
        PositionTextureVertex var16 = new PositionTextureVertex(var12, p_i46301_5_, p_i46301_6_, 0.0F, 8.0F);
        PositionTextureVertex var17 = new PositionTextureVertex(var12, var13, p_i46301_6_, 8.0F, 8.0F);
        PositionTextureVertex var18 = new PositionTextureVertex(p_i46301_4_, var13, p_i46301_6_, 8.0F, 0.0F);
        PositionTextureVertex var19 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, var14, 0.0F, 0.0F);
        PositionTextureVertex var20 = new PositionTextureVertex(var12, p_i46301_5_, var14, 0.0F, 8.0F);
        PositionTextureVertex var21 = new PositionTextureVertex(var12, var13, var14, 8.0F, 8.0F);
        PositionTextureVertex var22 = new PositionTextureVertex(p_i46301_4_, var13, var14, 8.0F, 0.0F);
        this.vertexPositions[0] = var24;
        this.vertexPositions[1] = var16;
        this.vertexPositions[2] = var17;
        this.vertexPositions[3] = var18;
        this.vertexPositions[4] = var19;
        this.vertexPositions[5] = var20;
        this.vertexPositions[6] = var21;
        this.vertexPositions[7] = var22;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[]{var20, var16, var17, var21}, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[]{var24, var19, var22, var18}, p_i46301_2_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[]{var20, var19, var24, var16}, p_i46301_2_ + p_i46301_9_, p_i46301_3_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[]{var17, var18, var22, var21}, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, p_i46301_3_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[]{var16, var24, var18, var17}, p_i46301_2_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[]{var19, var20, var21, var22}, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, p_i46301_3_ + p_i46301_9_, p_i46301_2_ + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, p_i46301_3_ + p_i46301_9_ + p_i46301_8_, p_i46301_1_.textureWidth, p_i46301_1_.textureHeight);

        if (p_i46301_11_) {
            for (int var23 = 0; var23 < this.quadList.length; ++var23) {
                this.quadList[var23].flipFace();
            }
        }
    }

    public void render(WorldRenderer p_178780_1_, float p_178780_2_) {
        for (int var3 = 0; var3 < this.quadList.length; ++var3) {
            this.quadList[var3].func_178765_a(p_178780_1_, p_178780_2_);
        }
    }

    public ModelBox func_78244_a(String p_78244_1_) {
        this.field_78247_g = p_78244_1_;
        return this;
    }
}
