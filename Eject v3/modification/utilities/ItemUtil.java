package modification.utilities;

import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.util.DamageSource;

public final class ItemUtil {
    public final int bestSwordSlot(Container paramContainer, int paramInt, boolean paramBoolean) {
        ItemStack localItemStack1 = paramContainer.getSlot(paramInt).getStack();
        for (int i = 0; i < paramContainer.getInventory().size(); i++) {
            ItemStack localItemStack2 = paramContainer.getSlot(i).getStack();
            if ((localItemStack2 != null) && ((localItemStack2.getItem() instanceof ItemSword))) {
                float f1 = -1.0F;
                if ((localItemStack1 != null) && ((localItemStack1.getItem() instanceof ItemSword))) {
                    f1 = ((ItemSword) localItemStack1.getItem()).getDamageVsEntity() + EnchantmentHelper.func_152377_a(localItemStack1, EnumCreatureAttribute.UNDEFINED);
                }
                float f2 = ((ItemSword) localItemStack2.getItem()).getDamageVsEntity() + EnchantmentHelper.func_152377_a(localItemStack2, EnumCreatureAttribute.UNDEFINED);
                if (paramBoolean ? f2 >= f1 : f2 > f1) {
                    return i;
                }
            }
        }
        return paramInt;
    }

    public final int findHotbarItem(Container paramContainer, Block paramBlock) {
        for (int i = 0; i < 9; i++) {
            ItemStack localItemStack = paramContainer.getSlot(i | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemTool)) && (localItemStack.getItem().getStrVsBlock(localItemStack, paramBlock) != 1.0F)) {
                return i;
            }
        }
        return -1;
    }

    public final int bestToolSlot(Container paramContainer, int paramInt) {
        ItemStack localItemStack1 = paramContainer.getSlot(paramInt).getStack();
        for (int i = 0; i < paramContainer.getInventory().size(); i++) {
            ItemStack localItemStack2 = paramContainer.getSlot(i).getStack();
            if ((localItemStack2 != null) && ((localItemStack2.getItem() instanceof ItemTool))) {
                float f = 0.0F;
                if ((localItemStack1 != null) && ((localItemStack1.getItem() instanceof ItemTool))) {
                    f = ((ItemTool) localItemStack1.getItem()).getDamageVsEntity() + EnchantmentHelper.func_152377_a(localItemStack1, EnumCreatureAttribute.UNDEFINED);
                }
                if (((ItemTool) localItemStack2.getItem()).getDamageVsEntity() + EnchantmentHelper.func_152377_a(localItemStack2, EnumCreatureAttribute.UNDEFINED) > f) {
                    return i;
                }
            }
        }
        return paramInt;
    }

    public final int bestArmorSlot(Container paramContainer, int paramInt1, int paramInt2, boolean paramBoolean) {
        ItemStack localItemStack1 = paramContainer.getSlot(paramInt1).getStack();
        for (int i = 0; i < paramContainer.getInventory().size(); i++) {
            ItemStack localItemStack2 = paramContainer.getSlot(i).getStack();
            if ((localItemStack2 != null) && ((localItemStack2.getItem() instanceof ItemArmor))) {
                ItemArmor localItemArmor1 = (ItemArmor) localItemStack2.getItem();
                if (localItemArmor1.armorType == paramInt2) {
                    float f1 = 0.0F;
                    if ((localItemStack1 != null) && ((localItemStack1.getItem() instanceof ItemArmor))) {
                        ItemArmor localItemArmor2 = (ItemArmor) localItemStack1.getItem();
                        if (localItemArmor2.armorType == paramInt2) {
                            f1 = localItemArmor2.getArmorMaterial().getDamageReductionAmount(paramInt2) | localItemArmor2.getArmorMaterial().getDurability(paramInt2) | EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{localItemStack1}, DamageSource.generic);
                        }
                    }
                    float f2 = localItemArmor1.getArmorMaterial().getDurability(paramInt2) | localItemArmor1.getArmorMaterial().getDamageReductionAmount(paramInt2) | EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{localItemStack2}, DamageSource.generic);
                    if (paramBoolean ? f2 >= f1 : f2 > f1) {
                        return i;
                    }
                }
            }
        }
        return paramInt1;
    }

    public final int bestSameToolSlot(Container paramContainer, int paramInt) {
        ItemStack localItemStack1 = paramContainer.getSlot(paramInt).getStack();
        for (int i = 9; i < paramContainer.getInventory().size(); i++) {
            ItemStack localItemStack2 = paramContainer.getSlot(i).getStack();
            if ((localItemStack2 != null) && ((localItemStack2.getItem() instanceof ItemTool))) {
                ItemTool localItemTool = (ItemTool) localItemStack2.getItem();
                float f = 0.0F;
                if ((localItemStack1 != null) && ((localItemStack1.getItem() instanceof ItemTool)) && (containsSameSlot(localItemTool, (ItemTool) localItemStack1.getItem()))) {
                    f = ((ItemTool) localItemStack1.getItem()).getDamageVsEntity();
                }
                if (localItemTool.getDamageVsEntity() >= f) {
                    return i;
                }
            }
        }
        return paramInt;
    }

    private boolean containsSameSlot(ItemTool paramItemTool1, ItemTool paramItemTool2) {
        return (((paramItemTool1 instanceof ItemSpade)) && ((paramItemTool2 instanceof ItemSpade))) || (((paramItemTool1 instanceof ItemAxe)) && ((paramItemTool2 instanceof ItemAxe))) || (((paramItemTool1 instanceof ItemPickaxe)) && ((paramItemTool2 instanceof ItemPickaxe)));
    }

    public final int sameItem(Container paramContainer, int paramInt) {
        ItemStack localItemStack1 = paramContainer.getSlot(paramInt).getStack();
        for (int i = 9; i < paramContainer.getInventory().size(); i++) {
            ItemStack localItemStack2 = paramContainer.getSlot(i).getStack();
            if ((localItemStack2 != null) && (localItemStack1 != null) && (localItemStack2 != localItemStack1) && (localItemStack2.getItem() == localItemStack1.getItem()) && (localItemStack2.getItem() != Items.brick) && (localItemStack2.getItem() != Items.iron_ingot) && (localItemStack2.getItem() != Items.gold_ingot) && (!(localItemStack2.getItem() instanceof ItemBlock)) && (localItemStack2.getItem() != Items.ender_pearl) && (localItemStack2.getItem() != Items.diamond) && (localItemStack2.getItem() != Items.golden_apple) && (localItemStack2.getItem() != Items.snowball) && (localItemStack2.getItem() != Items.egg) && (!(localItemStack2.getItem() instanceof ItemFood))) {
                return i;
            }
        }
        return paramInt;
    }
}




