// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;

public class ItemLeaves extends ItemBlock
{
    private final BlockLeaves leaves;
    
    public ItemLeaves(final BlockLeaves block) {
        super(block);
        this.leaves = block;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage | 0x4;
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + this.leaves.getWoodType(stack.getMetadata()).getUnlocalizedName();
    }
}
