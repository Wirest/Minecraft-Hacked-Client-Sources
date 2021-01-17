// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import com.google.common.collect.Multimap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSword extends Item
{
    private float attackDamage;
    private final ToolMaterial material;
    
    public ItemSword(final ToolMaterial material) {
        this.material = material;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.attackDamage = 4.0f + material.getDamageVsEntity();
    }
    
    public float getDamageVsEntity() {
        return this.material.getDamageVsEntity();
    }
    
    @Override
    public float getStrVsBlock(final ItemStack stack, final Block block) {
        if (block == Blocks.web) {
            return 15.0f;
        }
        final Material material = block.getMaterial();
        return (material != Material.plants && material != Material.vine && material != Material.coral && material != Material.leaves && material != Material.gourd) ? 1.0f : 1.5f;
    }
    
    @Override
    public boolean hitEntity(final ItemStack stack, final EntityLivingBase target, final EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }
    
    @Override
    public boolean onBlockDestroyed(final ItemStack stack, final World worldIn, final Block blockIn, final BlockPos pos, final EntityLivingBase playerIn) {
        if (blockIn.getBlockHardness(worldIn, pos) != 0.0) {
            stack.damageItem(2, playerIn);
        }
        return true;
    }
    
    @Override
    public boolean isFull3D() {
        return true;
    }
    
    @Override
    public EnumAction getItemUseAction(final ItemStack stack) {
        return EnumAction.BLOCK;
    }
    
    @Override
    public int getMaxItemUseDuration(final ItemStack stack) {
        return 72000;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }
    
    @Override
    public boolean canHarvestBlock(final Block blockIn) {
        return blockIn == Blocks.web;
    }
    
    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }
    
    public String getToolMaterialName() {
        return this.material.toString();
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.material.getRepairItem() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers() {
        final Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(ItemSword.itemModifierUUID, "Weapon modifier", this.attackDamage, 0));
        return multimap;
    }
    
    public float getAttackDamage() {
        return this.attackDamage;
    }
}
