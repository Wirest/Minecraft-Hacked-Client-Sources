// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.init.Items;
import net.minecraft.world.World;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.creativetab.CreativeTabs;
import java.util.List;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class ItemArmor extends Item
{
    private static final int[] maxDamageArray;
    public static final String[] EMPTY_SLOT_NAMES;
    private static final IBehaviorDispenseItem dispenserBehavior;
    public final int armorType;
    public final int damageReduceAmount;
    public final int renderIndex;
    private final ArmorMaterial material;
    
    static {
        maxDamageArray = new int[] { 11, 16, 15, 13 };
        EMPTY_SLOT_NAMES = new String[] { "minecraft:items/empty_armor_slot_helmet", "minecraft:items/empty_armor_slot_chestplate", "minecraft:items/empty_armor_slot_leggings", "minecraft:items/empty_armor_slot_boots" };
        dispenserBehavior = new BehaviorDefaultDispenseItem() {
            @Override
            protected ItemStack dispenseStack(final IBlockSource source, final ItemStack stack) {
                final BlockPos blockpos = source.getBlockPos().offset(BlockDispenser.getFacing(source.getBlockMetadata()));
                final int i = blockpos.getX();
                final int j = blockpos.getY();
                final int k = blockpos.getZ();
                final AxisAlignedBB axisalignedbb = new AxisAlignedBB(i, j, k, i + 1, j + 1, k + 1);
                final List<EntityLivingBase> list = source.getWorld().getEntitiesWithinAABB((Class<? extends EntityLivingBase>)EntityLivingBase.class, axisalignedbb, Predicates.and((Predicate<? super EntityLivingBase>)EntitySelectors.NOT_SPECTATING, (Predicate<? super EntityLivingBase>)new EntitySelectors.ArmoredMob(stack)));
                if (list.size() > 0) {
                    final EntityLivingBase entitylivingbase = list.get(0);
                    final int l = (entitylivingbase instanceof EntityPlayer) ? 1 : 0;
                    final int i2 = EntityLiving.getArmorPosition(stack);
                    final ItemStack itemstack = stack.copy();
                    itemstack.stackSize = 1;
                    entitylivingbase.setCurrentItemOrArmor(i2 - l, itemstack);
                    if (entitylivingbase instanceof EntityLiving) {
                        ((EntityLiving)entitylivingbase).setEquipmentDropChance(i2, 2.0f);
                    }
                    --stack.stackSize;
                    return stack;
                }
                return super.dispenseStack(source, stack);
            }
        };
    }
    
    public ItemArmor(final ArmorMaterial material, final int renderIndex, final int armorType) {
        this.material = material;
        this.armorType = armorType;
        this.renderIndex = renderIndex;
        this.damageReduceAmount = material.getDamageReductionAmount(armorType);
        this.setMaxDamage(material.getDurability(armorType));
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabCombat);
        BlockDispenser.dispenseBehaviorRegistry.putObject(this, ItemArmor.dispenserBehavior);
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass > 0) {
            return 16777215;
        }
        int i = this.getColor(stack);
        if (i < 0) {
            i = 16777215;
        }
        return i;
    }
    
    @Override
    public int getItemEnchantability() {
        return this.material.getEnchantability();
    }
    
    public ArmorMaterial getArmorMaterial() {
        return this.material;
    }
    
    public boolean hasColor(final ItemStack stack) {
        return this.material == ArmorMaterial.LEATHER && stack.hasTagCompound() && stack.getTagCompound().hasKey("display", 10) && stack.getTagCompound().getCompoundTag("display").hasKey("color", 3);
    }
    
    public int getColor(final ItemStack stack) {
        if (this.material != ArmorMaterial.LEATHER) {
            return -1;
        }
        final NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound != null) {
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
            if (nbttagcompound2 != null && nbttagcompound2.hasKey("color", 3)) {
                return nbttagcompound2.getInteger("color");
            }
        }
        return 10511680;
    }
    
    public void removeColor(final ItemStack stack) {
        if (this.material == ArmorMaterial.LEATHER) {
            final NBTTagCompound nbttagcompound = stack.getTagCompound();
            if (nbttagcompound != null) {
                final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
                if (nbttagcompound2.hasKey("color")) {
                    nbttagcompound2.removeTag("color");
                }
            }
        }
    }
    
    public void setColor(final ItemStack stack, final int color) {
        if (this.material != ArmorMaterial.LEATHER) {
            throw new UnsupportedOperationException("Can't dye non-leather!");
        }
        NBTTagCompound nbttagcompound = stack.getTagCompound();
        if (nbttagcompound == null) {
            nbttagcompound = new NBTTagCompound();
            stack.setTagCompound(nbttagcompound);
        }
        final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("display");
        if (!nbttagcompound.hasKey("display", 10)) {
            nbttagcompound.setTag("display", nbttagcompound2);
        }
        nbttagcompound2.setInteger("color", color);
    }
    
    @Override
    public boolean getIsRepairable(final ItemStack toRepair, final ItemStack repair) {
        return this.material.getRepairItem() == repair.getItem() || super.getIsRepairable(toRepair, repair);
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final int i = EntityLiving.getArmorPosition(itemStackIn) - 1;
        final ItemStack itemstack = playerIn.getCurrentArmor(i);
        if (itemstack == null) {
            playerIn.setCurrentItemOrArmor(i, itemStackIn.copy());
            itemStackIn.stackSize = 0;
        }
        return itemStackIn;
    }
    
    public enum ArmorMaterial
    {
        LEATHER("LEATHER", 0, "leather", 5, new int[] { 1, 3, 2, 1 }, 15), 
        CHAIN("CHAIN", 1, "chainmail", 15, new int[] { 2, 5, 4, 1 }, 12), 
        IRON("IRON", 2, "iron", 15, new int[] { 2, 6, 5, 2 }, 9), 
        GOLD("GOLD", 3, "gold", 7, new int[] { 2, 5, 3, 1 }, 25), 
        DIAMOND("DIAMOND", 4, "diamond", 33, new int[] { 3, 8, 6, 3 }, 10);
        
        private final String name;
        private final int maxDamageFactor;
        private final int[] damageReductionAmountArray;
        private final int enchantability;
        
        private ArmorMaterial(final String name2, final int ordinal, final String name, final int maxDamage, final int[] reductionAmounts, final int enchantability) {
            this.name = name;
            this.maxDamageFactor = maxDamage;
            this.damageReductionAmountArray = reductionAmounts;
            this.enchantability = enchantability;
        }
        
        public int getDurability(final int armorType) {
            return ItemArmor.maxDamageArray[armorType] * this.maxDamageFactor;
        }
        
        public int getDamageReductionAmount(final int armorType) {
            return this.damageReductionAmountArray[armorType];
        }
        
        public int getEnchantability() {
            return this.enchantability;
        }
        
        public Item getRepairItem() {
            return (this == ArmorMaterial.LEATHER) ? Items.leather : ((this == ArmorMaterial.CHAIN) ? Items.iron_ingot : ((this == ArmorMaterial.GOLD) ? Items.gold_ingot : ((this == ArmorMaterial.IRON) ? Items.iron_ingot : ((this == ArmorMaterial.DIAMOND) ? Items.diamond : null))));
        }
        
        public String getName() {
            return this.name;
        }
    }
}
