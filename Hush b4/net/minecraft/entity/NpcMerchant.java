// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;

public class NpcMerchant implements IMerchant
{
    private InventoryMerchant theMerchantInventory;
    private EntityPlayer customer;
    private MerchantRecipeList recipeList;
    private IChatComponent field_175548_d;
    
    public NpcMerchant(final EntityPlayer p_i45817_1_, final IChatComponent p_i45817_2_) {
        this.customer = p_i45817_1_;
        this.field_175548_d = p_i45817_2_;
        this.theMerchantInventory = new InventoryMerchant(p_i45817_1_, this);
    }
    
    @Override
    public EntityPlayer getCustomer() {
        return this.customer;
    }
    
    @Override
    public void setCustomer(final EntityPlayer p_70932_1_) {
    }
    
    @Override
    public MerchantRecipeList getRecipes(final EntityPlayer p_70934_1_) {
        return this.recipeList;
    }
    
    @Override
    public void setRecipes(final MerchantRecipeList recipeList) {
        this.recipeList = recipeList;
    }
    
    @Override
    public void useRecipe(final MerchantRecipe recipe) {
        recipe.incrementToolUses();
    }
    
    @Override
    public void verifySellingItem(final ItemStack stack) {
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return (this.field_175548_d != null) ? this.field_175548_d : new ChatComponentTranslation("entity.Villager.name", new Object[0]);
    }
}
