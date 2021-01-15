package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public interface IMerchant
{
    void setCustomer(EntityPlayer p_70932_1_);

    EntityPlayer getCustomer();

    MerchantRecipeList getRecipes(EntityPlayer p_70934_1_);

    void setRecipes(MerchantRecipeList p_70930_1_);

    void useRecipe(MerchantRecipe p_70933_1_);

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    void verifySellingItem(ItemStack p_110297_1_);

    IChatComponent getDisplayName();
}
