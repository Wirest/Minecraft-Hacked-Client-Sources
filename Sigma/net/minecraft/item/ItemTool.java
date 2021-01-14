package net.minecraft.item;

import com.google.common.collect.Multimap;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ItemTool extends Item {
    private Set effectiveBlocksTool;
    protected float efficiencyOnProperMaterial = 4.0F;

    /**
     * Damage versus entities.
     */
    private float damageVsEntity;

    /**
     * The material this tool is made from.
     */
    protected Item.ToolMaterial toolMaterial;
    private static final String __OBFID = "CL_00000019";

    protected ItemTool(float p_i45333_1_, Item.ToolMaterial p_i45333_2_, Set p_i45333_3_) {
        toolMaterial = p_i45333_2_;
        effectiveBlocksTool = p_i45333_3_;
        maxStackSize = 1;
        setMaxDamage(p_i45333_2_.getMaxUses());
        efficiencyOnProperMaterial = p_i45333_2_.getEfficiencyOnProperMaterial();
        damageVsEntity = p_i45333_1_ + p_i45333_2_.getDamageVsEntity();
        setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, Block p_150893_2_) {
        return effectiveBlocksTool.contains(p_150893_2_) ? efficiencyOnProperMaterial : 1.0F;
    }

    /**
     * Current implementations of this method in child classes do not use the
     * entry argument beside ev. They just raise the damage on the stack.
     *
     * @param target   The Entity being hit
     * @param attacker the attacking entity
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(2, attacker);
        return true;
    }

    /**
     * Called when a Block is destroyed using this Item. Return true to trigger
     * the "Use Item" statistic.
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
        if (blockIn.getBlockHardness(worldIn, pos) != 0.0D) {
            stack.damageItem(1, playerIn);
        }

        return true;
    }

    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    @Override
    public boolean isFull3D() {
        return true;
    }

    public Item.ToolMaterial getToolMaterial() {
        return toolMaterial;
    }
    public float getDamage(){
    	return this.damageVsEntity;
    }
    /**
     * Return the enchantability factor of the item, most of the time is based
     * on material.
     */
    @Override
    public int getItemEnchantability() {
        return toolMaterial.getEnchantability();
    }

    /**
     * Return the name for this tool's material.
     */
    public String getToolMaterialName() {
        return toolMaterial.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     *
     * @param toRepair The ItemStack to be repaired
     * @param repair   The ItemStack that should repair this Item (leather for
     *                 leather armor, etc.)
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        return toolMaterial.getBaseItemForRepair() == repair.getItem() ? true : super.getIsRepairable(toRepair, repair);
    }

    /**
     * Gets a map of item attribute modifiers, used by ItemSword to increase hit
     * damage.
     */
    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap var1 = super.getItemAttributeModifiers();
        var1.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(Item.itemModifierUUID, "Tool modifier", damageVsEntity, 0));
        return var1;
    }
}
