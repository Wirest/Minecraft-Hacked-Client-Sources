package net.minecraft.client.resources.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class SimpleBakedModel implements IBakedModel
{
    protected final List generalQuads;
    protected final List faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;

    public SimpleBakedModel(List p_i46077_1_, List p_i46077_2_, boolean p_i46077_3_, boolean p_i46077_4_, TextureAtlasSprite p_i46077_5_, ItemCameraTransforms p_i46077_6_)
    {
        this.generalQuads = p_i46077_1_;
        this.faceQuads = p_i46077_2_;
        this.ambientOcclusion = p_i46077_3_;
        this.gui3d = p_i46077_4_;
        this.texture = p_i46077_5_;
        this.cameraTransforms = p_i46077_6_;
    }

    @Override
	public List getFaceQuads(EnumFacing p_177551_1_)
    {
        return (List)this.faceQuads.get(p_177551_1_.ordinal());
    }

    @Override
	public List getGeneralQuads()
    {
        return this.generalQuads;
    }

    @Override
	public boolean isAmbientOcclusion()
    {
        return this.ambientOcclusion;
    }

    @Override
	public boolean isGui3d()
    {
        return this.gui3d;
    }

    @Override
	public boolean isBuiltInRenderer()
    {
        return false;
    }

    @Override
	public TextureAtlasSprite getTexture()
    {
        return this.texture;
    }

    @Override
	public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.cameraTransforms;
    }

    public static class Builder
    {
        private final List builderGeneralQuads;
        private final List builderFaceQuads;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private boolean builderGui3d;
        private ItemCameraTransforms builderCameraTransforms;

        public Builder(ModelBlock p_i46074_1_)
        {
            this(p_i46074_1_.isAmbientOcclusion(), p_i46074_1_.isGui3d(), new ItemCameraTransforms(p_i46074_1_.getThirdPersonTransform(), p_i46074_1_.getFirstPersonTransform(), p_i46074_1_.getHeadTransform(), p_i46074_1_.getInGuiTransform()));
        }

        public Builder(IBakedModel p_i46075_1_, TextureAtlasSprite p_i46075_2_)
        {
            this(p_i46075_1_.isAmbientOcclusion(), p_i46075_1_.isGui3d(), p_i46075_1_.getItemCameraTransforms());
            this.builderTexture = p_i46075_1_.getTexture();
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5)
            {
                EnumFacing var6 = var3[var5];
                this.addFaceBreakingFours(p_i46075_1_, p_i46075_2_, var6);
            }

            this.addGeneralBreakingFours(p_i46075_1_, p_i46075_2_);
        }

        private void addFaceBreakingFours(IBakedModel p_177649_1_, TextureAtlasSprite p_177649_2_, EnumFacing p_177649_3_)
        {
            Iterator var4 = p_177649_1_.getFaceQuads(p_177649_3_).iterator();

            while (var4.hasNext())
            {
                BakedQuad var5 = (BakedQuad)var4.next();
                this.addFaceQuad(p_177649_3_, new BreakingFour(var5, p_177649_2_));
            }
        }

        private void addGeneralBreakingFours(IBakedModel p_177647_1_, TextureAtlasSprite p_177647_2_)
        {
            Iterator var3 = p_177647_1_.getGeneralQuads().iterator();

            while (var3.hasNext())
            {
                BakedQuad var4 = (BakedQuad)var3.next();
                this.addGeneralQuad(new BreakingFour(var4, p_177647_2_));
            }
        }

        private Builder(boolean p_i46076_1_, boolean p_i46076_2_, ItemCameraTransforms p_i46076_3_)
        {
            this.builderGeneralQuads = Lists.newArrayList();
            this.builderFaceQuads = Lists.newArrayListWithCapacity(6);
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                this.builderFaceQuads.add(Lists.newArrayList());
            }

            this.builderAmbientOcclusion = p_i46076_1_;
            this.builderGui3d = p_i46076_2_;
            this.builderCameraTransforms = p_i46076_3_;
        }

        public SimpleBakedModel.Builder addFaceQuad(EnumFacing p_177650_1_, BakedQuad p_177650_2_)
        {
            ((List)this.builderFaceQuads.get(p_177650_1_.ordinal())).add(p_177650_2_);
            return this;
        }

        public SimpleBakedModel.Builder addGeneralQuad(BakedQuad p_177648_1_)
        {
            this.builderGeneralQuads.add(p_177648_1_);
            return this;
        }

        public SimpleBakedModel.Builder setTexture(TextureAtlasSprite p_177646_1_)
        {
            this.builderTexture = p_177646_1_;
            return this;
        }

        public IBakedModel makeBakedModel()
        {
            if (this.builderTexture == null)
            {
                throw new RuntimeException("Missing particle!");
            }
            else
            {
                return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
            }
        }
    }
}
