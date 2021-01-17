// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.model;

import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.collect.ComparisonChain;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.EnumFacing;
import java.util.Collection;
import net.minecraft.util.WeightedRandom;
import java.util.List;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List<MyWeighedRandomItem> models;
    private final IBakedModel baseModel;
    
    public WeightedBakedModel(final List<MyWeighedRandomItem> p_i46073_1_) {
        this.models = p_i46073_1_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
        this.baseModel = p_i46073_1_.get(0).model;
    }
    
    @Override
    public List<BakedQuad> getFaceQuads(final EnumFacing p_177551_1_) {
        return this.baseModel.getFaceQuads(p_177551_1_);
    }
    
    @Override
    public List<BakedQuad> getGeneralQuads() {
        return this.baseModel.getGeneralQuads();
    }
    
    @Override
    public boolean isAmbientOcclusion() {
        return this.baseModel.isAmbientOcclusion();
    }
    
    @Override
    public boolean isGui3d() {
        return this.baseModel.isGui3d();
    }
    
    @Override
    public boolean isBuiltInRenderer() {
        return this.baseModel.isBuiltInRenderer();
    }
    
    @Override
    public TextureAtlasSprite getParticleTexture() {
        return this.baseModel.getParticleTexture();
    }
    
    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return this.baseModel.getItemCameraTransforms();
    }
    
    public IBakedModel getAlternativeModel(final long p_177564_1_) {
        return WeightedRandom.getRandomItem(this.models, Math.abs((int)p_177564_1_ >> 16) % this.totalWeight).model;
    }
    
    static class MyWeighedRandomItem extends WeightedRandom.Item implements Comparable<MyWeighedRandomItem>
    {
        protected final IBakedModel model;
        
        public MyWeighedRandomItem(final IBakedModel p_i46072_1_, final int p_i46072_2_) {
            super(p_i46072_2_);
            this.model = p_i46072_1_;
        }
        
        @Override
        public int compareTo(final MyWeighedRandomItem p_compareTo_1_) {
            return ComparisonChain.start().compare(p_compareTo_1_.itemWeight, this.itemWeight).compare(this.getCountQuads(), p_compareTo_1_.getCountQuads()).result();
        }
        
        protected int getCountQuads() {
            int i = this.model.getGeneralQuads().size();
            EnumFacing[] values;
            for (int length = (values = EnumFacing.values()).length, j = 0; j < length; ++j) {
                final EnumFacing enumfacing = values[j];
                i += this.model.getFaceQuads(enumfacing).size();
            }
            return i;
        }
        
        @Override
        public String toString() {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }
    }
    
    public static class Builder
    {
        private List<MyWeighedRandomItem> listItems;
        
        public Builder() {
            this.listItems = (List<MyWeighedRandomItem>)Lists.newArrayList();
        }
        
        public Builder add(final IBakedModel p_177677_1_, final int p_177677_2_) {
            this.listItems.add(new MyWeighedRandomItem(p_177677_1_, p_177677_2_));
            return this;
        }
        
        public WeightedBakedModel build() {
            Collections.sort(this.listItems);
            return new WeightedBakedModel(this.listItems);
        }
        
        public IBakedModel first() {
            return this.listItems.get(0).model;
        }
    }
}
