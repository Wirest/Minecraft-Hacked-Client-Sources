// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.StatList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;

public class ItemFood extends Item
{
    public final int itemUseDuration;
    private final int healAmount;
    private final float saturationModifier;
    private final boolean isWolfsFavoriteMeat;
    private boolean alwaysEdible;
    private int potionId;
    private int potionDuration;
    private int potionAmplifier;
    private float potionEffectProbability;
    
    public ItemFood(final int amount, final float saturation, final boolean isWolfFood) {
        this.itemUseDuration = 32;
        this.healAmount = amount;
        this.isWolfsFavoriteMeat = isWolfFood;
        this.saturationModifier = saturation;
        this.setCreativeTab(CreativeTabs.tabFood);
    }
    
    public ItemFood(final int amount, final boolean isWolfFood) {
        this(amount, 0.6f, isWolfFood);
    }
    
    @Override
    public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityPlayer playerIn) {
        --stack.stackSize;
        playerIn.getFoodStats().addStats(this, stack);
        worldIn.playSoundAtEntity(playerIn, "random.burp", 0.5f, worldIn.rand.nextFloat() * 0.1f + 0.9f);
        this.onFoodEaten(stack, worldIn, playerIn);
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
        return stack;
    }
    
    protected void onFoodEaten(final ItemStack stack, final World worldIn, final EntityPlayer player) {
        if (!worldIn.isRemote && this.potionId > 0 && worldIn.rand.nextFloat() < this.potionEffectProbability) {
            player.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
        }
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 32;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.EAT;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        if (playerIn.canEat(this.alwaysEdible)) {
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }
        return itemStackIn;
    }
    
    public int getHealAmount(final ItemStack stack) {
        return this.healAmount;
    }
    
    public float getSaturationModifier(final ItemStack stack) {
        return this.saturationModifier;
    }
    
    public boolean isWolfsFavoriteMeat() {
        return this.isWolfsFavoriteMeat;
    }
    
    public ItemFood setPotionEffect(final int id, final int duration, final int amplifier, final float probability) {
        this.potionId = id;
        this.potionDuration = duration;
        this.potionAmplifier = amplifier;
        this.potionEffectProbability = probability;
        return this;
    }
    
    public ItemFood setAlwaysEdible() {
        this.alwaysEdible = true;
        return this;
    }
}
