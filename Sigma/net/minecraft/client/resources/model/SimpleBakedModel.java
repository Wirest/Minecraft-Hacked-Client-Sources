package net.minecraft.client.resources.model;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BreakingFour;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;

public class SimpleBakedModel implements IBakedModel {
    protected final List field_177563_a;
    protected final List field_177561_b;
    protected final boolean field_177562_c;
    protected final boolean ambientOcclusion;
    protected final TextureAtlasSprite texture;
    protected final ItemCameraTransforms field_177558_f;
    private static final String __OBFID = "CL_00002386";

    public SimpleBakedModel(List p_i46077_1_, List p_i46077_2_, boolean p_i46077_3_, boolean p_i46077_4_, TextureAtlasSprite p_i46077_5_, ItemCameraTransforms p_i46077_6_) {
        this.field_177563_a = p_i46077_1_;
        this.field_177561_b = p_i46077_2_;
        this.field_177562_c = p_i46077_3_;
        this.ambientOcclusion = p_i46077_4_;
        this.texture = p_i46077_5_;
        this.field_177558_f = p_i46077_6_;
    }

    public List func_177551_a(EnumFacing p_177551_1_) {
        return (List) this.field_177561_b.get(p_177551_1_.ordinal());
    }

    public List func_177550_a() {
        return this.field_177563_a;
    }

    public boolean isGui3d() {
        return this.field_177562_c;
    }

    public boolean isAmbientOcclusionEnabled() {
        return this.ambientOcclusion;
    }

    public boolean isBuiltInRenderer() {
        return false;
    }

    public TextureAtlasSprite getTexture() {
        return this.texture;
    }

    public ItemCameraTransforms getItemCameraTransforms() {
        return this.field_177558_f;
    }

    public static class Builder {
        private final List field_177656_a;
        private final List field_177654_b;
        private final boolean field_177655_c;
        private TextureAtlasSprite field_177652_d;
        private boolean field_177653_e;
        private ItemCameraTransforms field_177651_f;
        private static final String __OBFID = "CL_00002385";

        public Builder(ModelBlock p_i46074_1_) {
            this(p_i46074_1_.func_178309_b(), p_i46074_1_.isAmbientOcclusionEnabled(), new ItemCameraTransforms(p_i46074_1_.getThirdPersonTransform(), p_i46074_1_.getFirstPersonTransform(), p_i46074_1_.getHeadTransform(), p_i46074_1_.getInGuiTransform()));
        }

        public Builder(IBakedModel p_i46075_1_, TextureAtlasSprite p_i46075_2_) {
            this(p_i46075_1_.isGui3d(), p_i46075_1_.isAmbientOcclusionEnabled(), p_i46075_1_.getItemCameraTransforms());
            this.field_177652_d = p_i46075_1_.getTexture();
            EnumFacing[] var3 = EnumFacing.values();
            int var4 = var3.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                EnumFacing var6 = var3[var5];
                this.func_177649_a(p_i46075_1_, p_i46075_2_, var6);
            }

            this.func_177647_a(p_i46075_1_, p_i46075_2_);
        }

        private void func_177649_a(IBakedModel p_177649_1_, TextureAtlasSprite p_177649_2_, EnumFacing p_177649_3_) {
            Iterator var4 = p_177649_1_.func_177551_a(p_177649_3_).iterator();

            while (var4.hasNext()) {
                BakedQuad var5 = (BakedQuad) var4.next();
                this.func_177650_a(p_177649_3_, new BreakingFour(var5, p_177649_2_));
            }
        }

        private void func_177647_a(IBakedModel p_177647_1_, TextureAtlasSprite p_177647_2_) {
            Iterator var3 = p_177647_1_.func_177550_a().iterator();

            while (var3.hasNext()) {
                BakedQuad var4 = (BakedQuad) var3.next();
                this.func_177648_a(new BreakingFour(var4, p_177647_2_));
            }
        }

        private Builder(boolean p_i46076_1_, boolean p_i46076_2_, ItemCameraTransforms p_i46076_3_) {
            this.field_177656_a = Lists.newArrayList();
            this.field_177654_b = Lists.newArrayListWithCapacity(6);
            EnumFacing[] var4 = EnumFacing.values();
            int var5 = var4.length;

            for (int var6 = 0; var6 < var5; ++var6) {
                EnumFacing var10000 = var4[var6];
                this.field_177654_b.add(Lists.newArrayList());
            }

            this.field_177655_c = p_i46076_1_;
            this.field_177653_e = p_i46076_2_;
            this.field_177651_f = p_i46076_3_;
        }

        public SimpleBakedModel.Builder func_177650_a(EnumFacing p_177650_1_, BakedQuad p_177650_2_) {
            ((List) this.field_177654_b.get(p_177650_1_.ordinal())).add(p_177650_2_);
            return this;
        }

        public SimpleBakedModel.Builder func_177648_a(BakedQuad p_177648_1_) {
            this.field_177656_a.add(p_177648_1_);
            return this;
        }

        public SimpleBakedModel.Builder func_177646_a(TextureAtlasSprite p_177646_1_) {
            this.field_177652_d = p_177646_1_;
            return this;
        }

        public IBakedModel func_177645_b() {
            if (this.field_177652_d == null) {
                throw new RuntimeException("Missing particle!");
            } else {
                return new SimpleBakedModel(this.field_177656_a, this.field_177654_b, this.field_177655_c, this.field_177653_e, this.field_177652_d, this.field_177651_f);
            }
        }
    }
}
