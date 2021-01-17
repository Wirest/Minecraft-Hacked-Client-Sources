// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;
import java.util.Iterator;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import java.util.List;

public class SimpleBakedModel implements IBakedModel
{
    protected final List<BakedQuad> generalQuads;
    protected final List<List<BakedQuad>> faceQuads;
    protected final boolean ambientOcclusion;
    protected final boolean gui3d;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms cameraTransforms;
    
    public SimpleBakedModel(final List<BakedQuad> p_i46077_1_, final List<List<BakedQuad>> p_i46077_2_, final boolean p_i46077_3_, final boolean p_i46077_4_, final TextureAtlasSprite p_i46077_5_, final ItemCameraTransforms p_i46077_6_) {
        this.generalQuads = p_i46077_1_;
        this.faceQuads = p_i46077_2_;
        this.ambientOcclusion = p_i46077_3_;
        this.gui3d = p_i46077_4_;
        this.texture = p_i46077_5_;
        this.cameraTransforms = p_i46077_6_;
    }
    
    @Override
    public List<BakedQuad> getFaceQuads(final EnumFacing p_177551_1_) {
        return this.faceQuads.get(p_177551_1_.ordinal());
    }
    
    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.generalQuads;
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return this.ambientOcclusion;
    }
    
    @Override
    public boolean isGui3d() {
        return this.gui3d;
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.texture;
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.cameraTransforms;
    }
    
    public static class Builder
    {
        private final List<BakedQuad> builderGeneralQuads;
        private final List<List<BakedQuad>> builderFaceQuads;
        private final boolean builderAmbientOcclusion;
        private TextureAtlasSprite builderTexture;
        private boolean builderGui3d;
        private ItemCameraTransforms builderCameraTransforms;
        
        public Builder(final ModelBlock p_i46074_1_) {
            this(p_i46074_1_.isAmbientOcclusion(), p_i46074_1_.isGui3d(), p_i46074_1_.func_181682_g());
        }
        
        public Builder(final IBakedModel p_i46075_1_, final TextureAtlasSprite p_i46075_2_) {
            this(p_i46075_1_.isAmbientOcclusion(), p_i46075_1_.isGui3d(), p_i46075_1_.getItemCameraTransforms());
            this.builderTexture = p_i46075_1_.getParticleTexture();
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                this.addFaceBreakingFours(p_i46075_1_, p_i46075_2_, enumfacing);
            }
            this.addGeneralBreakingFours(p_i46075_1_, p_i46075_2_);
        }
        
        private void addFaceBreakingFours(final IBakedModel p_177649_1_, final TextureAtlasSprite p_177649_2_, final EnumFacing p_177649_3_) {
            for (final BakedQuad bakedquad : p_177649_1_.getFaceQuads(p_177649_3_)) {
                this.addFaceQuad(p_177649_3_, new BreakingFour(bakedquad, p_177649_2_));
            }
        }
        
        private void addGeneralBreakingFours(final IBakedModel p_177647_1_, final TextureAtlasSprite p_177647_2_) {
            for (final BakedQuad bakedquad : p_177647_1_.getGeneralQuads()) {
                this.addGeneralQuad(new BreakingFour(bakedquad, p_177647_2_));
            }
        }
        
        private Builder(final boolean p_i46076_1_, final boolean p_i46076_2_, final ItemCameraTransforms p_i46076_3_) {
            this.builderGeneralQuads = (List<BakedQuad>)Lists.newArrayList();
            this.builderFaceQuads = (List<List<BakedQuad>>)Lists.newArrayListWithCapacity(6);
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
                final EnumFacing enumfacing = values[i];
                this.builderFaceQuads.add((List<BakedQuad>)Lists.newArrayList());
            }
            this.builderAmbientOcclusion = p_i46076_1_;
            this.builderGui3d = p_i46076_2_;
            this.builderCameraTransforms = p_i46076_3_;
        }
        
        public Builder addFaceQuad(final EnumFacing p_177650_1_, final BakedQuad p_177650_2_) {
            this.builderFaceQuads.get(p_177650_1_.ordinal()).add(p_177650_2_);
            return this;
        }
        
        public Builder addGeneralQuad(final BakedQuad p_177648_1_) {
            this.builderGeneralQuads.add(p_177648_1_);
            return this;
        }
        
        public Builder setTexture(final TextureAtlasSprite p_177646_1_) {
            this.builderTexture = p_177646_1_;
            return this;
        }
        
        public IBakedModel makeBakedModel() {
            if (this.builderTexture == null) {
                throw new RuntimeException("Missing particle!");
            }
            return new SimpleBakedModel(this.builderGeneralQuads, this.builderFaceQuads, this.builderAmbientOcclusion, this.builderGui3d, this.builderTexture, this.builderCameraTransforms);
        }
    }
}
