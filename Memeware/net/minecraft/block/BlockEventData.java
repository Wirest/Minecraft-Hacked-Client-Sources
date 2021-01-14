package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData {
    private BlockPos field_180329_a;
    private Block field_151344_d;

    /**
     * Different for each blockID
     */
    private int eventID;
    private int eventParameter;
    private static final String __OBFID = "CL_00000131";

    public BlockEventData(BlockPos p_i45756_1_, Block p_i45756_2_, int p_i45756_3_, int p_i45756_4_) {
        this.field_180329_a = p_i45756_1_;
        this.eventID = p_i45756_3_;
        this.eventParameter = p_i45756_4_;
        this.field_151344_d = p_i45756_2_;
    }

    public BlockPos func_180328_a() {
        return this.field_180329_a;
    }

    /**
     * Get the Event ID (different for each BlockID)
     */
    public int getEventID() {
        return this.eventID;
    }

    public int getEventParameter() {
        return this.eventParameter;
    }

    public Block getBlock() {
        return this.field_151344_d;
    }

    public boolean equals(Object p_equals_1_) {
        if (!(p_equals_1_ instanceof BlockEventData)) {
            return false;
        } else {
            BlockEventData var2 = (BlockEventData) p_equals_1_;
            return this.field_180329_a.equals(var2.field_180329_a) && this.eventID == var2.eventID && this.eventParameter == var2.eventParameter && this.field_151344_d == var2.field_151344_d;
        }
    }

    public String toString() {
        return "TE(" + this.field_180329_a + ")," + this.eventID + "," + this.eventParameter + "," + this.field_151344_d;
    }
}
