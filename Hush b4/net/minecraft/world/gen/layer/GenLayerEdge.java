// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.layer;

public class GenLayerEdge extends GenLayer
{
    private final Mode field_151627_c;
    
    public GenLayerEdge(final long p_i45474_1_, final GenLayer p_i45474_3_, final Mode p_i45474_4_) {
        super(p_i45474_1_);
        this.parent = p_i45474_3_;
        this.field_151627_c = p_i45474_4_;
    }
    
    @Override
    public int[] getInts(final int areaX, final int areaY, final int areaWidth, final int areaHeight) {
        switch (this.field_151627_c) {
            default: {
                return this.getIntsCoolWarm(areaX, areaY, areaWidth, areaHeight);
            }
            case HEAT_ICE: {
                return this.getIntsHeatIce(areaX, areaY, areaWidth, areaHeight);
            }
            case SPECIAL: {
                return this.getIntsSpecial(areaX, areaY, areaWidth, areaHeight);
            }
        }
    }
    
    private int[] getIntsCoolWarm(final int p_151626_1_, final int p_151626_2_, final int p_151626_3_, final int p_151626_4_) {
        final int i = p_151626_1_ - 1;
        final int j = p_151626_2_ - 1;
        final int k = 1 + p_151626_3_ + 1;
        final int l = 1 + p_151626_4_ + 1;
        final int[] aint = this.parent.getInts(i, j, k, l);
        final int[] aint2 = IntCache.getIntCache(p_151626_3_ * p_151626_4_);
        for (int i2 = 0; i2 < p_151626_4_; ++i2) {
            for (int j2 = 0; j2 < p_151626_3_; ++j2) {
                this.initChunkSeed(j2 + p_151626_1_, i2 + p_151626_2_);
                int k2 = aint[j2 + 1 + (i2 + 1) * k];
                if (k2 == 1) {
                    final int l2 = aint[j2 + 1 + (i2 + 1 - 1) * k];
                    final int i3 = aint[j2 + 1 + 1 + (i2 + 1) * k];
                    final int j3 = aint[j2 + 1 - 1 + (i2 + 1) * k];
                    final int k3 = aint[j2 + 1 + (i2 + 1 + 1) * k];
                    final boolean flag = l2 == 3 || i3 == 3 || j3 == 3 || k3 == 3;
                    final boolean flag2 = l2 == 4 || i3 == 4 || j3 == 4 || k3 == 4;
                    if (flag || flag2) {
                        k2 = 2;
                    }
                }
                aint2[j2 + i2 * p_151626_3_] = k2;
            }
        }
        return aint2;
    }
    
    private int[] getIntsHeatIce(final int p_151624_1_, final int p_151624_2_, final int p_151624_3_, final int p_151624_4_) {
        final int i = p_151624_1_ - 1;
        final int j = p_151624_2_ - 1;
        final int k = 1 + p_151624_3_ + 1;
        final int l = 1 + p_151624_4_ + 1;
        final int[] aint = this.parent.getInts(i, j, k, l);
        final int[] aint2 = IntCache.getIntCache(p_151624_3_ * p_151624_4_);
        for (int i2 = 0; i2 < p_151624_4_; ++i2) {
            for (int j2 = 0; j2 < p_151624_3_; ++j2) {
                int k2 = aint[j2 + 1 + (i2 + 1) * k];
                if (k2 == 4) {
                    final int l2 = aint[j2 + 1 + (i2 + 1 - 1) * k];
                    final int i3 = aint[j2 + 1 + 1 + (i2 + 1) * k];
                    final int j3 = aint[j2 + 1 - 1 + (i2 + 1) * k];
                    final int k3 = aint[j2 + 1 + (i2 + 1 + 1) * k];
                    final boolean flag = l2 == 2 || i3 == 2 || j3 == 2 || k3 == 2;
                    final boolean flag2 = l2 == 1 || i3 == 1 || j3 == 1 || k3 == 1;
                    if (flag2 || flag) {
                        k2 = 3;
                    }
                }
                aint2[j2 + i2 * p_151624_3_] = k2;
            }
        }
        return aint2;
    }
    
    private int[] getIntsSpecial(final int p_151625_1_, final int p_151625_2_, final int p_151625_3_, final int p_151625_4_) {
        final int[] aint = this.parent.getInts(p_151625_1_, p_151625_2_, p_151625_3_, p_151625_4_);
        final int[] aint2 = IntCache.getIntCache(p_151625_3_ * p_151625_4_);
        for (int i = 0; i < p_151625_4_; ++i) {
            for (int j = 0; j < p_151625_3_; ++j) {
                this.initChunkSeed(j + p_151625_1_, i + p_151625_2_);
                int k = aint[j + i * p_151625_3_];
                if (k != 0 && this.nextInt(13) == 0) {
                    k |= (1 + this.nextInt(15) << 8 & 0xF00);
                }
                aint2[j + i * p_151625_3_] = k;
            }
        }
        return aint2;
    }
    
    public enum Mode
    {
        COOL_WARM("COOL_WARM", 0), 
        HEAT_ICE("HEAT_ICE", 1), 
        SPECIAL("SPECIAL", 2);
        
        private Mode(final String name, final int ordinal) {
        }
    }
}
