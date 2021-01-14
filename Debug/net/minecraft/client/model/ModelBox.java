package net.minecraft.client.model;

import net.minecraft.client.renderer.WorldRenderer;

public class ModelBox
{
    /**
     * The (x,y,z) vertex positions and (u,v) texture coordinates for each of the 8 points on a cube
     */
    private PositionTextureVertex[] vertexPositions;

    /** An array of 6 TexturedQuads, one for each face of a cube */
    private TexturedQuad[] quadList;

    /** X vertex coordinate of lower box corner */
    public final float posX1;

    /** Y vertex coordinate of lower box corner */
    public final float posY1;

    /** Z vertex coordinate of lower box corner */
    public final float posZ1;

    /** X vertex coordinate of upper box corner */
    public final float posX2;

    /** Y vertex coordinate of upper box corner */
    public final float posY2;

    /** Z vertex coordinate of upper box corner */
    public final float posZ2;
    public String boxName;
    private static final String __OBFID = "CL_00000872";

    public ModelBox(ModelRenderer renderer, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, int p_i46359_7_, int p_i46359_8_, int p_i46359_9_, float p_i46359_10_)
    {
        this(renderer, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, renderer.mirror);
    }

    public ModelBox(ModelRenderer p_i3_1_, int[][] p_i3_2_, float p_i3_3_, float p_i3_4_, float p_i3_5_, float p_i3_6_, float p_i3_7_, float p_i3_8_, float p_i3_9_, boolean p_i3_10_)
    {
        this.posX1 = p_i3_3_;
        this.posY1 = p_i3_4_;
        this.posZ1 = p_i3_5_;
        this.posX2 = p_i3_3_ + p_i3_6_;
        this.posY2 = p_i3_4_ + p_i3_7_;
        this.posZ2 = p_i3_5_ + p_i3_8_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = p_i3_3_ + p_i3_6_;
        float f1 = p_i3_4_ + p_i3_7_;
        float f2 = p_i3_5_ + p_i3_8_;
        p_i3_3_ = p_i3_3_ - p_i3_9_;
        p_i3_4_ = p_i3_4_ - p_i3_9_;
        p_i3_5_ = p_i3_5_ - p_i3_9_;
        f = f + p_i3_9_;
        f1 = f1 + p_i3_9_;
        f2 = f2 + p_i3_9_;

        if (p_i3_10_)
        {
            float f3 = f;
            f = p_i3_3_;
            p_i3_3_ = f3;
        }

        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i3_3_, p_i3_4_, p_i3_5_, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, p_i3_4_, p_i3_5_, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, p_i3_5_, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i3_3_, f1, p_i3_5_, 8.0F, 0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i3_3_, p_i3_4_, f2, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, p_i3_4_, f2, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i3_3_, f1, f2, 8.0F, 0.0F);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex;
        this.vertexPositions[2] = positiontexturevertex1;
        this.vertexPositions[3] = positiontexturevertex2;
        this.vertexPositions[4] = positiontexturevertex3;
        this.vertexPositions[5] = positiontexturevertex4;
        this.vertexPositions[6] = positiontexturevertex5;
        this.vertexPositions[7] = positiontexturevertex6;
        this.quadList[0] = this.makeTexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, p_i3_2_[4], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[1] = this.makeTexturedQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, p_i3_2_[5], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[2] = this.makeTexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, p_i3_2_[1], true, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[3] = this.makeTexturedQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, p_i3_2_[0], true, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[4] = this.makeTexturedQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, p_i3_2_[2], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);
        this.quadList[5] = this.makeTexturedQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, p_i3_2_[3], false, p_i3_1_.textureWidth, p_i3_1_.textureHeight);

        if (p_i3_10_)
        {
            for (TexturedQuad texturedquad : this.quadList)
            {
                texturedquad.flipFace();
            }
        }
    }

    private TexturedQuad makeTexturedQuad(PositionTextureVertex[] p_makeTexturedQuad_1_, int[] p_makeTexturedQuad_2_, boolean p_makeTexturedQuad_3_, float p_makeTexturedQuad_4_, float p_makeTexturedQuad_5_)
    {
        return p_makeTexturedQuad_2_ == null ? null : (p_makeTexturedQuad_3_ ? new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_) : new TexturedQuad(p_makeTexturedQuad_1_, p_makeTexturedQuad_2_[0], p_makeTexturedQuad_2_[1], p_makeTexturedQuad_2_[2], p_makeTexturedQuad_2_[3], p_makeTexturedQuad_4_, p_makeTexturedQuad_5_));
    }

    public ModelBox(ModelRenderer renderer, int textureX, int textureY, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, int p_i46301_7_, int p_i46301_8_, int p_i46301_9_, float p_i46301_10_, boolean p_i46301_11_)
    {
        this.posX1 = p_i46301_4_;
        this.posY1 = p_i46301_5_;
        this.posZ1 = p_i46301_6_;
        this.posX2 = p_i46301_4_ + (float)p_i46301_7_;
        this.posY2 = p_i46301_5_ + (float)p_i46301_8_;
        this.posZ2 = p_i46301_6_ + (float)p_i46301_9_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = p_i46301_4_ + (float)p_i46301_7_;
        float f1 = p_i46301_5_ + (float)p_i46301_8_;
        float f2 = p_i46301_6_ + (float)p_i46301_9_;
        p_i46301_4_ = p_i46301_4_ - p_i46301_10_;
        p_i46301_5_ = p_i46301_5_ - p_i46301_10_;
        p_i46301_6_ = p_i46301_6_ - p_i46301_10_;
        f = f + p_i46301_10_;
        f1 = f1 + p_i46301_10_;
        f2 = f2 + p_i46301_10_;

        if (p_i46301_11_)
        {
            float f3 = f;
            f = p_i46301_4_;
            p_i46301_4_ = f3;
        }

        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, p_i46301_6_, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, p_i46301_5_, p_i46301_6_, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1, p_i46301_6_, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i46301_4_, f1, p_i46301_6_, 8.0F, 0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_, f2, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, p_i46301_5_, f2, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i46301_4_, f1, f2, 8.0F, 0.0F);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex;
        this.vertexPositions[2] = positiontexturevertex1;
        this.vertexPositions[3] = positiontexturevertex2;
        this.vertexPositions[4] = positiontexturevertex3;
        this.vertexPositions[5] = positiontexturevertex4;
        this.vertexPositions[6] = positiontexturevertex5;
        this.vertexPositions[7] = positiontexturevertex6;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, textureX, textureY + p_i46301_9_, textureX + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, textureX + p_i46301_9_, textureY, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, textureY, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, textureX + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);

        if (p_i46301_11_)
        {
            for (int i = 0; i < this.quadList.length; ++i)
            {
                this.quadList[i].flipFace();
            }
        }
    }

    public void render(WorldRenderer renderer, float scale)
    {
        for (int i = 0; i < this.quadList.length; ++i)
        {
            TexturedQuad texturedquad = this.quadList[i];

            if (texturedquad != null)
            {
                texturedquad.draw(renderer, scale);
            }
        }
    }

    public ModelBox setBoxName(String name)
    {
        this.boxName = name;
        return this;
    }
}
