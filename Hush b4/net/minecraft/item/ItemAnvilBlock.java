// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemAnvilBlock extends ItemMultiTexture
{
    public ItemAnvilBlock(final Block block) {
        super(block, block, new String[] { "intact", "slightlyDamaged", "veryDamaged" });
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage << 2;
    }
}
