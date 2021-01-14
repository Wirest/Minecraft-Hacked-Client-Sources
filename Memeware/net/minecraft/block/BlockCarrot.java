package net.minecraft.block;

import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockCarrot extends BlockCrops {
    private static final String __OBFID = "CL_00000212";

    protected Item getSeed() {
        return Items.carrot;
    }

    protected Item getCrop() {
        return Items.carrot;
    }
}
