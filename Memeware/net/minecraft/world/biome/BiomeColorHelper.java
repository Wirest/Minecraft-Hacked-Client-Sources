package net.minecraft.world.biome;

import java.util.Iterator;

import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BiomeColorHelper {
    private static final BiomeColorHelper.ColorResolver field_180291_a = new BiomeColorHelper.ColorResolver() {
        private static final String __OBFID = "CL_00002148";

        public int func_180283_a(BiomeGenBase p_180283_1_, BlockPos p_180283_2_) {
            return p_180283_1_.func_180627_b(p_180283_2_);
        }
    };
    private static final BiomeColorHelper.ColorResolver field_180289_b = new BiomeColorHelper.ColorResolver() {
        private static final String __OBFID = "CL_00002147";

        public int func_180283_a(BiomeGenBase p_180283_1_, BlockPos p_180283_2_) {
            return p_180283_1_.func_180625_c(p_180283_2_);
        }
    };
    private static final BiomeColorHelper.ColorResolver field_180290_c = new BiomeColorHelper.ColorResolver() {
        private static final String __OBFID = "CL_00002146";

        public int func_180283_a(BiomeGenBase p_180283_1_, BlockPos p_180283_2_) {
            return p_180283_1_.waterColorMultiplier;
        }
    };
    private static final String __OBFID = "CL_00002149";

    private static int func_180285_a(IBlockAccess p_180285_0_, BlockPos p_180285_1_, BiomeColorHelper.ColorResolver p_180285_2_) {
        int var3 = 0;
        int var4 = 0;
        int var5 = 0;
        int var8;

        for (Iterator var6 = BlockPos.getAllInBoxMutable(p_180285_1_.add(-1, 0, -1), p_180285_1_.add(1, 0, 1)).iterator(); var6.hasNext(); var5 += var8 & 255) {
            BlockPos.MutableBlockPos var7 = (BlockPos.MutableBlockPos) var6.next();
            var8 = p_180285_2_.func_180283_a(p_180285_0_.getBiomeGenForCoords(var7), var7);
            var3 += (var8 & 16711680) >> 16;
            var4 += (var8 & 65280) >> 8;
        }

        return (var3 / 9 & 255) << 16 | (var4 / 9 & 255) << 8 | var5 / 9 & 255;
    }

    public static int func_180286_a(IBlockAccess p_180286_0_, BlockPos p_180286_1_) {
        return func_180285_a(p_180286_0_, p_180286_1_, field_180291_a);
    }

    public static int func_180287_b(IBlockAccess p_180287_0_, BlockPos p_180287_1_) {
        return func_180285_a(p_180287_0_, p_180287_1_, field_180289_b);
    }

    public static int func_180288_c(IBlockAccess p_180288_0_, BlockPos p_180288_1_) {
        return func_180285_a(p_180288_0_, p_180288_1_, field_180290_c);
    }

    interface ColorResolver {
        int func_180283_a(BiomeGenBase var1, BlockPos var2);
    }
}
