// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.chunk;

import optifine.IntegerCache;
import java.util.ArrayDeque;
import java.util.EnumSet;
import net.minecraft.util.EnumFacing;
import java.util.Set;
import net.minecraft.util.BlockPos;
import java.util.BitSet;

public class VisGraph
{
    private static final int field_178616_a;
    private static final int field_178614_b;
    private static final int field_178615_c;
    private final BitSet field_178612_d;
    private static final int[] field_178613_e;
    private int field_178611_f;
    private static final String __OBFID = "CL_00002450";
    
    static {
        field_178616_a = (int)Math.pow(16.0, 0.0);
        field_178614_b = (int)Math.pow(16.0, 1.0);
        field_178615_c = (int)Math.pow(16.0, 2.0);
        field_178613_e = new int[1352];
        final boolean flag = false;
        final boolean flag2 = true;
        int i = 0;
        for (int j = 0; j < 16; ++j) {
            for (int k = 0; k < 16; ++k) {
                for (int l = 0; l < 16; ++l) {
                    if (j == 0 || j == 15 || k == 0 || k == 15 || l == 0 || l == 15) {
                        VisGraph.field_178613_e[i++] = getIndex(j, k, l);
                    }
                }
            }
        }
    }
    
    public VisGraph() {
        this.field_178612_d = new BitSet(4096);
        this.field_178611_f = 4096;
    }
    
    public void func_178606_a(final BlockPos pos) {
        this.field_178612_d.set(getIndex(pos), true);
        --this.field_178611_f;
    }
    
    private static int getIndex(final BlockPos pos) {
        return getIndex(pos.getX() & 0xF, pos.getY() & 0xF, pos.getZ() & 0xF);
    }
    
    private static int getIndex(final int x, final int y, final int z) {
        return x << 0 | y << 8 | z << 4;
    }
    
    public SetVisibility computeVisibility() {
        final SetVisibility setvisibility = new SetVisibility();
        if (4096 - this.field_178611_f < 256) {
            setvisibility.setAllVisible(true);
        }
        else if (this.field_178611_f == 0) {
            setvisibility.setAllVisible(false);
        }
        else {
            int[] field_178613_e;
            for (int length = (field_178613_e = VisGraph.field_178613_e).length, j = 0; j < length; ++j) {
                final int i = field_178613_e[j];
                if (!this.field_178612_d.get(i)) {
                    setvisibility.setManyVisible(this.func_178604_a(i));
                }
            }
        }
        return setvisibility;
    }
    
    public Set func_178609_b(final BlockPos pos) {
        return this.func_178604_a(getIndex(pos));
    }
    
    private Set func_178604_a(final int p_178604_1_) {
        final EnumSet enumset = EnumSet.noneOf(EnumFacing.class);
        final ArrayDeque arraydeque = new ArrayDeque(384);
        arraydeque.add(IntegerCache.valueOf(p_178604_1_));
        this.field_178612_d.set(p_178604_1_, true);
        while (!arraydeque.isEmpty()) {
            final int i = arraydeque.poll();
            this.func_178610_a(i, enumset);
            EnumFacing[] values;
            for (int length = (values = EnumFacing.VALUES).length, k = 0; k < length; ++k) {
                final EnumFacing enumfacing = values[k];
                final int j = this.func_178603_a(i, enumfacing);
                if (j >= 0 && !this.field_178612_d.get(j)) {
                    this.field_178612_d.set(j, true);
                    arraydeque.add(IntegerCache.valueOf(j));
                }
            }
        }
        return enumset;
    }
    
    private void func_178610_a(final int p_178610_1_, final Set p_178610_2_) {
        final int i = p_178610_1_ >> 0 & 0xF;
        if (i == 0) {
            p_178610_2_.add(EnumFacing.WEST);
        }
        else if (i == 15) {
            p_178610_2_.add(EnumFacing.EAST);
        }
        final int j = p_178610_1_ >> 8 & 0xF;
        if (j == 0) {
            p_178610_2_.add(EnumFacing.DOWN);
        }
        else if (j == 15) {
            p_178610_2_.add(EnumFacing.UP);
        }
        final int k = p_178610_1_ >> 4 & 0xF;
        if (k == 0) {
            p_178610_2_.add(EnumFacing.NORTH);
        }
        else if (k == 15) {
            p_178610_2_.add(EnumFacing.SOUTH);
        }
    }
    
    private int func_178603_a(final int p_178603_1_, final EnumFacing p_178603_2_) {
        switch (VisGraph$1.field_178617_a[p_178603_2_.ordinal()]) {
            case 1: {
                if ((p_178603_1_ >> 8 & 0xF) == 0x0) {
                    return -1;
                }
                return p_178603_1_ - VisGraph.field_178615_c;
            }
            case 2: {
                if ((p_178603_1_ >> 8 & 0xF) == 0xF) {
                    return -1;
                }
                return p_178603_1_ + VisGraph.field_178615_c;
            }
            case 3: {
                if ((p_178603_1_ >> 4 & 0xF) == 0x0) {
                    return -1;
                }
                return p_178603_1_ - VisGraph.field_178614_b;
            }
            case 4: {
                if ((p_178603_1_ >> 4 & 0xF) == 0xF) {
                    return -1;
                }
                return p_178603_1_ + VisGraph.field_178614_b;
            }
            case 5: {
                if ((p_178603_1_ >> 0 & 0xF) == 0x0) {
                    return -1;
                }
                return p_178603_1_ - VisGraph.field_178616_a;
            }
            case 6: {
                if ((p_178603_1_ >> 0 & 0xF) == 0xF) {
                    return -1;
                }
                return p_178603_1_ + VisGraph.field_178616_a;
            }
            default: {
                return -1;
            }
        }
    }
    
    static final class VisGraph$1
    {
        static final int[] field_178617_a;
        private static final String __OBFID = "CL_00002449";
        
        static {
            field_178617_a = new int[EnumFacing.values().length];
            try {
                VisGraph$1.field_178617_a[EnumFacing.DOWN.ordinal()] = 1;
            }
            catch (NoSuchFieldError noSuchFieldError) {}
            try {
                VisGraph$1.field_178617_a[EnumFacing.UP.ordinal()] = 2;
            }
            catch (NoSuchFieldError noSuchFieldError2) {}
            try {
                VisGraph$1.field_178617_a[EnumFacing.NORTH.ordinal()] = 3;
            }
            catch (NoSuchFieldError noSuchFieldError3) {}
            try {
                VisGraph$1.field_178617_a[EnumFacing.SOUTH.ordinal()] = 4;
            }
            catch (NoSuchFieldError noSuchFieldError4) {}
            try {
                VisGraph$1.field_178617_a[EnumFacing.WEST.ordinal()] = 5;
            }
            catch (NoSuchFieldError noSuchFieldError5) {}
            try {
                VisGraph$1.field_178617_a[EnumFacing.EAST.ordinal()] = 6;
            }
            catch (NoSuchFieldError noSuchFieldError6) {}
        }
    }
}
