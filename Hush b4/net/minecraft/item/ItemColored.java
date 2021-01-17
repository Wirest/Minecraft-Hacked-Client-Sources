// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemColored extends ItemBlock
{
    private final Block coloredBlock;
    private String[] subtypeNames;
    
    public ItemColored(final Block block, final boolean hasSubtypes) {
        super(block);
        this.coloredBlock = block;
        if (hasSubtypes) {
            this.setMaxDamage(0);
            this.setHasSubtypes(true);
        }
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    public ItemColored setSubtypeNames(final String[] names) {
        this.subtypeNames = names;
        return this;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        if (this.subtypeNames == null) {
            return super.getUnlocalizedName(stack);
        }
        final int i = stack.getMetadata();
        return (i >= 0 && i < this.subtypeNames.length) ? (String.valueOf(super.getUnlocalizedName(stack)) + "." + this.subtypeNames[i]) : super.getUnlocalizedName(stack);
    }
}
