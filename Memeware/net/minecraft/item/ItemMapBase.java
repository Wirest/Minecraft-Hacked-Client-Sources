package net.minecraft.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.world.World;

public class ItemMapBase extends Item {
    private static final String __OBFID = "CL_00000004";

    /**
     * false for all Items except sub-classes of ItemMapBase
     */
    public boolean isMap() {
        return true;
    }

    public Packet createMapDataPacket(ItemStack p_150911_1_, World worldIn, EntityPlayer p_150911_3_) {
        return null;
    }
}
