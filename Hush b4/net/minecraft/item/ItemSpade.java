// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemSpade extends ItemTool
{
    private static final Set<Block> EFFECTIVE_ON;
    
    static {
        EFFECTIVE_ON = Sets.newHashSet(Blocks.clay, Blocks.dirt, Blocks.farmland, Blocks.grass, Blocks.gravel, Blocks.mycelium, Blocks.sand, Blocks.snow, Blocks.snow_layer, Blocks.soul_sand);
    }
    
    public ItemSpade(final ToolMaterial material) {
        super(1.0f, material, ItemSpade.EFFECTIVE_ON);
    }
    
    @Override
    public boolean canHarvestBlock(final Block blockIn) {
        return blockIn == Blocks.snow_layer || blockIn == Blocks.snow;
    }
}
