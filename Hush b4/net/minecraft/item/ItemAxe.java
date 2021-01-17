// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.material.Material;
import com.google.common.collect.Sets;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import java.util.Set;

public class ItemAxe extends ItemTool
{
    private static final Set<Block> EFFECTIVE_ON;
    
    static {
        EFFECTIVE_ON = Sets.newHashSet(Blocks.planks, Blocks.bookshelf, Blocks.log, Blocks.log2, Blocks.chest, Blocks.pumpkin, Blocks.lit_pumpkin, Blocks.melon_block, Blocks.ladder);
    }
    
    protected ItemAxe(final ToolMaterial material) {
        super(3.0f, material, ItemAxe.EFFECTIVE_ON);
    }
    
    @Override
    public float getStrVsBlock(final ItemStack stack, final Block block) {
        return (block.getMaterial() != Material.wood && block.getMaterial() != Material.plants && block.getMaterial() != Material.vine) ? super.getStrVsBlock(stack, block) : this.efficiencyOnProperMaterial;
    }
}
