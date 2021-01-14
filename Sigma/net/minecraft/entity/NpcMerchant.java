package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class NpcMerchant implements IMerchant {
    /**
     * Instance of Merchants Inventory.
     */
    private InventoryMerchant theMerchantInventory;

    /**
     * This merchant's current player customer.
     */
    private EntityPlayer customer;

    /**
     * The MerchantRecipeList instance.
     */
    private MerchantRecipeList recipeList;
    private IChatComponent field_175548_d;
    private static final String __OBFID = "CL_00001705";

    public NpcMerchant(EntityPlayer p_i45817_1_, IChatComponent p_i45817_2_) {
        customer = p_i45817_1_;
        field_175548_d = p_i45817_2_;
        theMerchantInventory = new InventoryMerchant(p_i45817_1_, this);
    }

    @Override
    public EntityPlayer getCustomer() {
        return customer;
    }

    @Override
    public void setCustomer(EntityPlayer p_70932_1_) {
    }

    @Override
    public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
        return recipeList;
    }

    @Override
    public void setRecipes(MerchantRecipeList p_70930_1_) {
        recipeList = p_70930_1_;
    }

    @Override
    public void useRecipe(MerchantRecipe p_70933_1_) {
        p_70933_1_.incrementToolUses();
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or
     * not. Usually, this is just a sound byte being played depending if the
     * suggested itemstack is not null.
     */
    @Override
    public void verifySellingItem(ItemStack p_110297_1_) {
    }

    @Override
    public IChatComponent getDisplayName() {
        return field_175548_d != null ? field_175548_d : new ChatComponentTranslation("entity.Villager.name", new Object[0]);
    }
}
