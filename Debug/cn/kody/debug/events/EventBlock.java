package cn.kody.debug.events;

import com.darkmagician6.eventapi.events.Event;

import net.minecraft.block.Block;

public class EventBlock implements Event
{
    public int x;
    public int y;
    public int z;
    public Block block;
    
    public EventBlock(final int p_i518_1_, final int p_i518_2_, final int p_i518_3_, final Block p_i518_4_) {
        super();
        this.x = p_i518_1_;
        this.y = p_i518_2_;
        this.z = p_i518_3_;
        this.block = p_i518_4_;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Block getBlock() {
        return this.block;
    }
}
