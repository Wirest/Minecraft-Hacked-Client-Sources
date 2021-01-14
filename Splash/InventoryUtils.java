package splash.utilities.player;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import splash.utilities.time.Stopwatch;

/**
 * Author: Ice
 * Created: 23:19, 13-Jun-20
 * Project: Client
 */
public class InventoryUtils {
	public static boolean busy;
    public static Minecraft mc = Minecraft.getMinecraft();

	public static boolean isHoldingSword() {
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() != null
                && Minecraft.getMinecraft().thePlayer.inventory.getCurrentItem().getItem() instanceof ItemSword) {
            return true;
        }
        return false;
    }
    
    public static boolean isContainerFull(Container container) {
        boolean full = true;
        for (Slot slot : container.inventorySlots) {
            if (!slot.getHasStack()) {
                full = false;
                break;
            }
        }
        return full;
    }

    public static boolean isContainerEmpty(Container container) {
        boolean empty = true;
        for (Slot slot : container.inventorySlots) {
            if (slot.getHasStack()) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public static float getSharpnessLevel(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0.0f;
        }
        return (float) EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack) * 1.25f;
    }

    public static float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }

    public static int getArmorItemsEquipSlot(ItemStack stack, boolean equipmentSlot) {
        if (stack.getUnlocalizedName().contains("helmet"))
            return equipmentSlot ? 4 : 5;
        if (stack.getUnlocalizedName().contains("chestplate"))
            return equipmentSlot ? 3 : 6;
        if (stack.getUnlocalizedName().contains("leggings"))
            return equipmentSlot ? 2 : 7;
        if (stack.getUnlocalizedName().contains("boots"))
            return equipmentSlot ? 1 : 8;
        return -1;
    }

    public static double getArmorProtection(ItemStack armorStack) {
        if (!(armorStack.getItem() instanceof ItemArmor))
            return 0.0;

        final ItemArmor armorItem = (ItemArmor) armorStack.getItem();
        final double protectionLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, armorStack);

        return armorItem.damageReduceAmount + ((6 + protectionLevel * protectionLevel) * 0.75 / 3);

    }
    
    
    public static  boolean equipArmor(Stopwatch stopwatch, int delay) {

        return false;
    }

    public static ItemStack compareProtection(ItemStack item1, ItemStack item2) {
        if (!(item1.getItem() instanceof ItemArmor) && !(item2.getItem() instanceof ItemArmor))
            return null;

        if (!(item1.getItem() instanceof ItemArmor))
            return item2;

        if (!(item2.getItem() instanceof ItemArmor))
            return item1;

        if (getArmorProtection(item1) > getArmorProtection(item2)) {
            return item1;
        } else if (getArmorProtection(item2) > getArmorProtection(item1)) {
            return item2;
        }

        return null;
    }
}
