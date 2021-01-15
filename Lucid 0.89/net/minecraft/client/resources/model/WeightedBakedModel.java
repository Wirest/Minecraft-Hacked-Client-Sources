package net.minecraft.client.resources.model;

import java.util.Collections;
import java.util.List;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;

public class WeightedBakedModel implements IBakedModel
{
    private final int totalWeight;
    private final List models;
    private final IBakedModel baseModel;

    public WeightedBakedModel(List p_i46073_1_)
    {
        this.models = p_i46073_1_;
        this.totalWeight = WeightedRandom.getTotalWeight(p_i46073_1_);
        this.baseModel = ((WeightedBakedModel.MyWeighedRandomItem)p_i46073_1_.get(0)).model;
    }

    @Override
	public List getFaceQuads(EnumFacing p_177551_1_)
    {
        return this.baseModel.getFaceQuads(p_177551_1_);
    }

    @Override
	public List getGeneralQuads()
    {
        return this.baseModel.getGeneralQuads();
    }

    @Override
	public boolean isAmbientOcclusion()
    {
        return this.baseModel.isAmbientOcclusion();
    }

    @Override
	public boolean isGui3d()
    {
        return this.baseModel.isGui3d();
    }

    @Override
	public boolean isBuiltInRenderer()
    {
        return this.baseModel.isBuiltInRenderer();
    }

    @Override
	public TextureAtlasSprite getTexture()
    {
        return this.baseModel.getTexture();
    }

    @Override
	public ItemCameraTransforms getItemCameraTransforms()
    {
        return this.baseModel.getItemCameraTransforms();
    }

    public IBakedModel getAlternativeModel(long p_177564_1_)
    {
        return ((WeightedBakedModel.MyWeighedRandomItem)WeightedRandom.getRandomItem(this.models, Math.abs((int)p_177564_1_ >> 16) % this.totalWeight)).model;
    }

    public static class Builder
    {
        private List listItems = Lists.newArrayList();

        public WeightedBakedModel.Builder add(IBakedModel p_177677_1_, int p_177677_2_)
        {
            this.listItems.add(new WeightedBakedModel.MyWeighedRandomItem(p_177677_1_, p_177677_2_));
            return this;
        }

        public WeightedBakedModel build()
        {
            Collections.sort(this.listItems);
            return new WeightedBakedModel(this.listItems);
        }

        public IBakedModel first()
        {
            return ((WeightedBakedModel.MyWeighedRandomItem)this.listItems.get(0)).model;
        }
    }

    static class MyWeighedRandomItem extends WeightedRandom.Item implements Comparable
    {
        protected final IBakedModel model;

        public MyWeighedRandomItem(IBakedModel p_i46072_1_, int p_i46072_2_)
        {
            super(p_i46072_2_);
            this.model = p_i46072_1_;
        }

        public int compareToItem(WeightedBakedModel.MyWeighedRandomItem p_177634_1_)
        {
            return ComparisonChain.start().compare(p_177634_1_.itemWeight, this.itemWeight).compare(this.getCountQuads(), p_177634_1_.getCountQuads()).result();
        }

        protected int getCountQuads()
        {
            int var1 = this.model.getGeneralQuads().size();
            EnumFacing[] var2 = EnumFacing.values();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4)
            {
                EnumFacing var5 = var2[var4];
                var1 += this.model.getFaceQuads(var5).size();
            }

            return var1;
        }

        @Override
		public String toString()
        {
            return "MyWeighedRandomItem{weight=" + this.itemWeight + ", model=" + this.model + '}';
        }

        @Override
		public int compareTo(Object p_compareTo_1_)
        {
            return this.compareToItem((WeightedBakedModel.MyWeighedRandomItem)p_compareTo_1_);
        }
    }
}
