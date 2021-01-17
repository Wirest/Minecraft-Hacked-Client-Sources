// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.stats;

import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.util.IChatComponent;
import net.minecraft.item.Item;

public class StatCrafting extends StatBase
{
    private final Item field_150960_a;
    
    public StatCrafting(final String p_i45910_1_, final String p_i45910_2_, final IChatComponent statNameIn, final Item p_i45910_4_) {
        super(String.valueOf(p_i45910_1_) + p_i45910_2_, statNameIn);
        this.field_150960_a = p_i45910_4_;
        final int i = Item.getIdFromItem(p_i45910_4_);
        if (i != 0) {
            IScoreObjectiveCriteria.INSTANCES.put(String.valueOf(p_i45910_1_) + i, this.func_150952_k());
        }
    }
    
    public Item func_150959_a() {
        return this.field_150960_a;
    }
}
