// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.Block;

public class ItemCloth extends ItemBlock
{
    public ItemCloth(final Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
    }
}
