// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.util.BlockPos;

public class BlockEventData
{
    private BlockPos position;
    private Block blockType;
    private int eventID;
    private int eventParameter;
    
    public BlockEventData(final BlockPos pos, final Block blockType, final int eventId, final int p_i45756_4_) {
        this.position = pos;
        this.eventID = eventId;
        this.eventParameter = p_i45756_4_;
        this.blockType = blockType;
    }
    
    public BlockPos getPosition() {
        return this.position;
    }
    
    public int getEventID() {
        return this.eventID;
    }
    
    public int getEventParameter() {
        return this.eventParameter;
    }
    
    public Block getBlock() {
        return this.blockType;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (!(p_equals_1_ instanceof BlockEventData)) {
            return false;
        }
        final BlockEventData blockeventdata = (BlockEventData)p_equals_1_;
        return this.position.equals(blockeventdata.position) && this.eventID == blockeventdata.eventID && this.eventParameter == blockeventdata.eventParameter && this.blockType == blockeventdata.blockType;
    }
    
    @Override
    public String toString() {
        return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
    }
}
