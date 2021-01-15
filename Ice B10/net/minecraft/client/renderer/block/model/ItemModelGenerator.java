package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

public class ItemModelGenerator
{
    public static final List LAYERS = Lists.newArrayList(new String[] {"layer0", "layer1", "layer2", "layer3", "layer4"});
    private static final String __OBFID = "CL_00002488";

    public ModelBlock func_178392_a(TextureMap p_178392_1_, ModelBlock p_178392_2_)
    {
        HashMap var3 = Maps.newHashMap();
        ArrayList var4 = Lists.newArrayList();

        for (int var5 = 0; var5 < LAYERS.size(); ++var5)
        {
            String var6 = (String)LAYERS.get(var5);

            if (!p_178392_2_.isTexturePresent(var6))
            {
                break;
            }

            String var7 = p_178392_2_.resolveTextureName(var6);
            var3.put(var6, var7);
            TextureAtlasSprite var8 = p_178392_1_.getAtlasSprite((new ResourceLocation(var7)).toString());
            var4.addAll(this.func_178394_a(var5, var6, var8));
        }

        if (var4.isEmpty())
        {
            return null;
        }
        else
        {
            var3.put("particle", p_178392_2_.isTexturePresent("particle") ? p_178392_2_.resolveTextureName("particle") : (String)var3.get("layer0"));
            return new ModelBlock(var4, var3, false, false, new ItemCameraTransforms(p_178392_2_.getThirdPersonTransform(), p_178392_2_.getFirstPersonTransform(), p_178392_2_.getHeadTransform(), p_178392_2_.getInGuiTransform()));
        }
    }

    private List func_178394_a(int p_178394_1_, String p_178394_2_, TextureAtlasSprite p_178394_3_)
    {
        HashMap var4 = Maps.newHashMap();
        var4.put(EnumFacing.SOUTH, new BlockPartFace((EnumFacing)null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[] {0.0F, 0.0F, 16.0F, 16.0F}, 0)));
        var4.put(EnumFacing.NORTH, new BlockPartFace((EnumFacing)null, p_178394_1_, p_178394_2_, new BlockFaceUV(new float[] {16.0F, 0.0F, 0.0F, 16.0F}, 0)));
        ArrayList var5 = Lists.newArrayList();
        var5.add(new BlockPart(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), var4, (BlockPartRotation)null, true));
        var5.addAll(this.func_178397_a(p_178394_3_, p_178394_2_, p_178394_1_));
        return var5;
    }

    private List func_178397_a(TextureAtlasSprite p_178397_1_, String p_178397_2_, int p_178397_3_)
    {
        float var4 = (float)p_178397_1_.getIconWidth();
        float var5 = (float)p_178397_1_.getIconHeight();
        ArrayList var6 = Lists.newArrayList();
        Iterator var7 = this.func_178393_a(p_178397_1_).iterator();

        while (var7.hasNext())
        {
            ItemModelGenerator.Span var8 = (ItemModelGenerator.Span)var7.next();
            float var9 = 0.0F;
            float var10 = 0.0F;
            float var11 = 0.0F;
            float var12 = 0.0F;
            float var13 = 0.0F;
            float var14 = 0.0F;
            float var15 = 0.0F;
            float var16 = 0.0F;
            float var17 = 0.0F;
            float var18 = 0.0F;
            float var19 = (float)var8.func_178385_b();
            float var20 = (float)var8.func_178384_c();
            float var21 = (float)var8.func_178381_d();
            ItemModelGenerator.SpanFacing var22 = var8.func_178383_a();

            switch (ItemModelGenerator.SwitchSpanFacing.field_178390_a[var22.ordinal()])
            {
                case 1:
                    var13 = var19;
                    var9 = var19;
                    var11 = var14 = var20 + 1.0F;
                    var15 = var21;
                    var10 = var21;
                    var16 = var21;
                    var12 = var21;
                    var17 = 16.0F / var4;
                    var18 = 16.0F / (var5 - 1.0F);
                    break;

                case 2:
                    var16 = var21;
                    var15 = var21;
                    var13 = var19;
                    var9 = var19;
                    var11 = var14 = var20 + 1.0F;
                    var10 = var21 + 1.0F;
                    var12 = var21 + 1.0F;
                    var17 = 16.0F / var4;
                    var18 = 16.0F / (var5 - 1.0F);
                    break;

                case 3:
                    var13 = var21;
                    var9 = var21;
                    var14 = var21;
                    var11 = var21;
                    var16 = var19;
                    var10 = var19;
                    var12 = var15 = var20 + 1.0F;
                    var17 = 16.0F / (var4 - 1.0F);
                    var18 = 16.0F / var5;
                    break;

                case 4:
                    var14 = var21;
                    var13 = var21;
                    var9 = var21 + 1.0F;
                    var11 = var21 + 1.0F;
                    var16 = var19;
                    var10 = var19;
                    var12 = var15 = var20 + 1.0F;
                    var17 = 16.0F / (var4 - 1.0F);
                    var18 = 16.0F / var5;
            }

            float var23 = 16.0F / var4;
            float var24 = 16.0F / var5;
            var9 *= var23;
            var11 *= var23;
            var10 *= var24;
            var12 *= var24;
            var10 = 16.0F - var10;
            var12 = 16.0F - var12;
            var13 *= var17;
            var14 *= var17;
            var15 *= var18;
            var16 *= var18;
            HashMap var25 = Maps.newHashMap();
            var25.put(var22.func_178367_a(), new BlockPartFace((EnumFacing)null, p_178397_3_, p_178397_2_, new BlockFaceUV(new float[] {var13, var15, var14, var16}, 0)));

            switch (ItemModelGenerator.SwitchSpanFacing.field_178390_a[var22.ordinal()])
            {
                case 1:
                    var6.add(new BlockPart(new Vector3f(var9, var10, 7.5F), new Vector3f(var11, var10, 8.5F), var25, (BlockPartRotation)null, true));
                    break;

                case 2:
                    var6.add(new BlockPart(new Vector3f(var9, var12, 7.5F), new Vector3f(var11, var12, 8.5F), var25, (BlockPartRotation)null, true));
                    break;

                case 3:
                    var6.add(new BlockPart(new Vector3f(var9, var10, 7.5F), new Vector3f(var9, var12, 8.5F), var25, (BlockPartRotation)null, true));
                    break;

                case 4:
                    var6.add(new BlockPart(new Vector3f(var11, var10, 7.5F), new Vector3f(var11, var12, 8.5F), var25, (BlockPartRotation)null, true));
            }
        }

        return var6;
    }

    private List func_178393_a(TextureAtlasSprite p_178393_1_)
    {
        int var2 = p_178393_1_.getIconWidth();
        int var3 = p_178393_1_.getIconHeight();
        ArrayList var4 = Lists.newArrayList();

        for (int var5 = 0; var5 < p_178393_1_.getFrameCount(); ++var5)
        {
            int[] var6 = p_178393_1_.getFrameTextureData(var5)[0];

            for (int var7 = 0; var7 < var3; ++var7)
            {
                for (int var8 = 0; var8 < var2; ++var8)
                {
                    boolean var9 = !this.func_178391_a(var6, var8, var7, var2, var3);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.UP, var4, var6, var8, var7, var2, var3, var9);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.DOWN, var4, var6, var8, var7, var2, var3, var9);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.LEFT, var4, var6, var8, var7, var2, var3, var9);
                    this.func_178396_a(ItemModelGenerator.SpanFacing.RIGHT, var4, var6, var8, var7, var2, var3, var9);
                }
            }
        }

        return var4;
    }

    private void func_178396_a(ItemModelGenerator.SpanFacing p_178396_1_, List p_178396_2_, int[] p_178396_3_, int p_178396_4_, int p_178396_5_, int p_178396_6_, int p_178396_7_, boolean p_178396_8_)
    {
        boolean var9 = this.func_178391_a(p_178396_3_, p_178396_4_ + p_178396_1_.func_178372_b(), p_178396_5_ + p_178396_1_.func_178371_c(), p_178396_6_, p_178396_7_) && p_178396_8_;

        if (var9)
        {
            this.func_178395_a(p_178396_2_, p_178396_1_, p_178396_4_, p_178396_5_);
        }
    }

    private void func_178395_a(List p_178395_1_, ItemModelGenerator.SpanFacing p_178395_2_, int p_178395_3_, int p_178395_4_)
    {
        ItemModelGenerator.Span var5 = null;
        Iterator var6 = p_178395_1_.iterator();

        while (var6.hasNext())
        {
            ItemModelGenerator.Span var7 = (ItemModelGenerator.Span)var6.next();

            if (var7.func_178383_a() == p_178395_2_)
            {
                int var8 = p_178395_2_.func_178369_d() ? p_178395_4_ : p_178395_3_;

                if (var7.func_178381_d() == var8)
                {
                    var5 = var7;
                    break;
                }
            }
        }

        int var9 = p_178395_2_.func_178369_d() ? p_178395_4_ : p_178395_3_;
        int var10 = p_178395_2_.func_178369_d() ? p_178395_3_ : p_178395_4_;

        if (var5 == null)
        {
            p_178395_1_.add(new ItemModelGenerator.Span(p_178395_2_, var10, var9));
        }
        else
        {
            var5.func_178382_a(var10);
        }
    }

    private boolean func_178391_a(int[] p_178391_1_, int p_178391_2_, int p_178391_3_, int p_178391_4_, int p_178391_5_)
    {
        return p_178391_2_ >= 0 && p_178391_3_ >= 0 && p_178391_2_ < p_178391_4_ && p_178391_3_ < p_178391_5_ ? (p_178391_1_[p_178391_3_ * p_178391_4_ + p_178391_2_] >> 24 & 255) == 0 : true;
    }

    static class Span
    {
        private final ItemModelGenerator.SpanFacing field_178389_a;
        private int field_178387_b;
        private int field_178388_c;
        private final int field_178386_d;
        private static final String __OBFID = "CL_00002486";

        public Span(ItemModelGenerator.SpanFacing p_i46216_1_, int p_i46216_2_, int p_i46216_3_)
        {
            this.field_178389_a = p_i46216_1_;
            this.field_178387_b = p_i46216_2_;
            this.field_178388_c = p_i46216_2_;
            this.field_178386_d = p_i46216_3_;
        }

        public void func_178382_a(int p_178382_1_)
        {
            if (p_178382_1_ < this.field_178387_b)
            {
                this.field_178387_b = p_178382_1_;
            }
            else if (p_178382_1_ > this.field_178388_c)
            {
                this.field_178388_c = p_178382_1_;
            }
        }

        public ItemModelGenerator.SpanFacing func_178383_a()
        {
            return this.field_178389_a;
        }

        public int func_178385_b()
        {
            return this.field_178387_b;
        }

        public int func_178384_c()
        {
            return this.field_178388_c;
        }

        public int func_178381_d()
        {
            return this.field_178386_d;
        }
    }

    static enum SpanFacing
    {
        UP("UP", 0, EnumFacing.UP, 0, -1),
        DOWN("DOWN", 1, EnumFacing.DOWN, 0, 1),
        LEFT("LEFT", 2, EnumFacing.EAST, -1, 0),
        RIGHT("RIGHT", 3, EnumFacing.WEST, 1, 0);
        private final EnumFacing field_178376_e;
        private final int field_178373_f;
        private final int field_178374_g;

        private static final ItemModelGenerator.SpanFacing[] $VALUES = new ItemModelGenerator.SpanFacing[]{UP, DOWN, LEFT, RIGHT};
        private static final String __OBFID = "CL_00002485";

        private SpanFacing(String p_i46215_1_, int p_i46215_2_, EnumFacing p_i46215_3_, int p_i46215_4_, int p_i46215_5_)
        {
            this.field_178376_e = p_i46215_3_;
            this.field_178373_f = p_i46215_4_;
            this.field_178374_g = p_i46215_5_;
        }

        public EnumFacing func_178367_a()
        {
            return this.field_178376_e;
        }

        public int func_178372_b()
        {
            return this.field_178373_f;
        }

        public int func_178371_c()
        {
            return this.field_178374_g;
        }

        private boolean func_178369_d()
        {
            return this == DOWN || this == UP;
        }
    }

    static final class SwitchSpanFacing
    {
        static final int[] field_178390_a = new int[ItemModelGenerator.SpanFacing.values().length];
        private static final String __OBFID = "CL_00002487";

        static
        {
            try
            {
                field_178390_a[ItemModelGenerator.SpanFacing.UP.ordinal()] = 1;
            }
            catch (NoSuchFieldError var4)
            {
                ;
            }

            try
            {
                field_178390_a[ItemModelGenerator.SpanFacing.DOWN.ordinal()] = 2;
            }
            catch (NoSuchFieldError var3)
            {
                ;
            }

            try
            {
                field_178390_a[ItemModelGenerator.SpanFacing.LEFT.ordinal()] = 3;
            }
            catch (NoSuchFieldError var2)
            {
                ;
            }

            try
            {
                field_178390_a[ItemModelGenerator.SpanFacing.RIGHT.ordinal()] = 4;
            }
            catch (NoSuchFieldError var1)
            {
                ;
            }
        }
    }
}
