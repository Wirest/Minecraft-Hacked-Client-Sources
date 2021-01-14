package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public interface IMerchant {
    void setCustomer(EntityPlayer var1);

    EntityPlayer getCustomer();

    MerchantRecipeList getRecipes(EntityPlayer var1);

    void setRecipes(MerchantRecipeList var1);

    void useRecipe(MerchantRecipe var1);

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    void verifySellingItem(ItemStack var1);

    IChatComponent getDisplayName();
}
