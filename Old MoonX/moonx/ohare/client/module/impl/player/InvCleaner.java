package moonx.ohare.client.module.impl.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class InvCleaner extends Module {

    private NumberValue<Integer> delay = new NumberValue<>("Delay", 150, 50, 300, 10);
    private BooleanValue toggle = new BooleanValue("Toggle",true);
    private BooleanValue inventoryonly = new BooleanValue("Inventory Only",false);
    private BooleanValue tools = new BooleanValue("Keep Tools",true);
    private TimerUtil timer = new TimerUtil();

    public InvCleaner() {
        super("InvCleaner", Category.PLAYER, new Color(255, 0, 255, 255).getRGB());
        setRenderLabel("Inv Cleaner");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            if (!(getMc().currentScreen instanceof GuiInventory) && inventoryonly.isEnabled()) {
                return;
            }
            int random = (Math.round(MathUtils.getRandomInRange(1, 75) / 50) * 50);
            if (getMc().thePlayer != null && (getMc().currentScreen == null || getMc().currentScreen instanceof GuiInventory) && timer.sleep(delay.getValue() + random)) {
                for (int i = 9; i < 45; ++i) {
                    if (getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                        ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                        if (isBad(is) && is != getMc().thePlayer.getCurrentEquippedItem()) {
                            getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, i, 0, 0, getMc().thePlayer);
                            getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, -999, 0, 0, getMc().thePlayer);
                            timer.reset();
                            break;
                        }
                    }
                }
                if (!hasGoodies() && toggle.isEnabled()) {
                    toggle();
                }
            }
        }
    }

    private boolean hasGoodies() {
        for (int i = 9; i < 45; ++i) {
            if (getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (isBad(is) && is != getMc().thePlayer.getCurrentEquippedItem()) {
                    return true;
                }
            }
        }
        return false;
    }
    private ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0;
        for (int i = 9; i < 45; ++i) {
            if (getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    float swordD = getItemDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = is;
                    }
                }
            }
        }
        return best;
    }

    private boolean isBad(final ItemStack item) {
        return item != null && ((item.getItem().getUnlocalizedName().contains("tnt")) || (item.getItem().getUnlocalizedName().contains("stick")) || item.getItem() instanceof ItemEgg || (item.getItem().getUnlocalizedName().contains("string")) || (item.getItem().getUnlocalizedName().contains("flint")) || (item.getItem().getUnlocalizedName().contains("compass")) || (item.getItem().getUnlocalizedName().contains("feather")) || (item.getItem().getUnlocalizedName().contains("bucket")) || (item.getItem().getUnlocalizedName().equalsIgnoreCase("chest") && !item.getDisplayName().toLowerCase().contains("collect")) || (item.getItem().getUnlocalizedName().contains("snow")) || (item.getItem().getUnlocalizedName().contains("enchant")) || (item.getItem().getUnlocalizedName().contains("exp")) || (item.getItem().getUnlocalizedName().contains("shears")) || (item.getItem().getUnlocalizedName().contains("anvil")) || (item.getItem().getUnlocalizedName().contains("torch")) || (item.getItem().getUnlocalizedName().contains("skull")) || (item.getItem().getUnlocalizedName().contains("seeds")) || (item.getItem().getUnlocalizedName().contains("leather")) || (item.getItem().getUnlocalizedName().contains("boat")) || (item.getItem().getUnlocalizedName().contains("fishing")) || (item.getItem().getUnlocalizedName().contains("wheat")) || (item.getItem().getUnlocalizedName().contains("flower")) || (item.getItem().getUnlocalizedName().contains("record")) || (item.getItem().getUnlocalizedName().contains("note")) || (item.getItem().getUnlocalizedName().contains("sugar")) || (item.getItem().getUnlocalizedName().contains("wire")) || (item.getItem().getUnlocalizedName().contains("trip")) || (item.getItem().getUnlocalizedName().contains("slime")) || (item.getItem().getUnlocalizedName().contains("web")) || (!tools.isEnabled() && (item.getItem() instanceof ItemPickaxe)) || ((item.getItem() instanceof ItemGlassBottle)) || (!tools.isEnabled() && (item.getItem() instanceof ItemTool)) || ((item.getItem() instanceof ItemArmor) && !getBest().contains(item)) || ((item.getItem() instanceof ItemSword) && item != bestSword()) || (item.getItem().getUnlocalizedName().contains("piston")) || (item.getItem().getUnlocalizedName().contains("potion") && (isBadPotion(item))) || (item.getItem() instanceof ItemBow || item.getItem().getUnlocalizedName().contains("arrow")));
    }

    private List<ItemStack> getBest() {
        List<ItemStack> best = new ArrayList<>();
        for (int i = 0; i < 4; ++i) {
            ItemStack armorStack = null;
            for (ItemStack itemStack : getMc().thePlayer.inventory.armorInventory) {
                if (itemStack == null || !(itemStack.getItem() instanceof ItemArmor)) continue;
                ItemArmor stackArmor = (ItemArmor) itemStack.getItem();
                if (stackArmor.armorType != i) continue;
                armorStack = itemStack;
            }
            final double reduction = armorStack == null ? -1 : getArmorStrength(armorStack);
            ItemStack slotStack = findBestArmor(i);
            if (slotStack != null && getArmorStrength(slotStack) <= reduction) {
                slotStack = armorStack;
            }
            if (slotStack == null) continue;
            best.add(slotStack);
        }
        return best;
    }

    private ItemStack findBestArmor(final int itemSlot) {
        ItemStack i = null;
        double maxReduction = 0;
        for (int slot = 0; slot < 36; ++slot) {
            ItemStack itemStack = getMc().thePlayer.inventory.mainInventory[slot];
            if (itemStack == null) continue;
            double reduction = getArmorStrength(itemStack);
            if (reduction == -1) continue;
            ItemArmor itemArmor = (ItemArmor) itemStack.getItem();
            if (itemArmor.armorType != itemSlot) continue;
            if (reduction < maxReduction) continue;
            maxReduction = reduction;
            i = itemStack;
        }
        return i;
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map<Integer, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }

    private boolean isBadPotion(final ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
            if (ItemPotion.isSplash(stack.getItemDamage())) {
                for (final PotionEffect o : potion.getEffects(stack)) {
                    final PotionEffect effect = o;
                    if (effect.getPotionID() == Potion.poison.getId() || effect.getPotionID() == Potion.harm.getId() || effect.getPotionID() == Potion.moveSlowdown.getId() || effect.getPotionID() == Potion.weakness.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
}
