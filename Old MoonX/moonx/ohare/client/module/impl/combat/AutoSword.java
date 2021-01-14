package moonx.ohare.client.module.impl.combat;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class AutoSword extends Module {
    private NumberValue<Integer> delay = new NumberValue<>("Delay", 100, 1, 2000, 1);
    private TimerUtil timer = new TimerUtil();
    public static NumberValue<Integer> slot = new NumberValue<>("Slot", 0, 0, 8, 1);
    private BooleanValue inventoryonly = new BooleanValue("Inventory Only",false);

    public AutoSword() {
        super("AutoSword", Category.COMBAT, new Color(153, 204, 255, 255).getRGB());
        setDescription("Automatically equips the strongest sword");
        setRenderLabel("Auto Sword");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre() || !timer.reach(delay.getValue()) || (getMc().currentScreen != null && !(getMc().currentScreen instanceof GuiInventory)) || !(getMc().currentScreen instanceof GuiInventory) && inventoryonly.isEnabled())
            return;
        int best = -1;
        float swordDamage = 0;
        for (int i = 9; i < 45; ++i) {
            if (getMc().thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = getMc().thePlayer.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    float swordD = getItemDamage(is);
                    if (swordD >= swordDamage) {
                        swordDamage = swordD;
                        best = i;
                    }
                }
            }
        }
        final ItemStack current = getMc().thePlayer.inventoryContainer.getSlot(36 + slot.getValue()).getStack();
        if (best != -1 || current == null || (current.getItem() instanceof ItemSword && swordDamage > getItemDamage(current))) {
            float dmg = current != null && current.getItem() != null && current.getItem() instanceof ItemSword ? getItemDamage(current) : -83474;
            if (best != 36 + slot.getValue() && best != -1 && swordDamage > dmg) {
                getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, best, slot.getValue(), 2, getMc().thePlayer);
                timer.reset();
            }
        }
    }

    private float getItemDamage(final ItemStack itemStack) {
        float damage = ((ItemSword) itemStack.getItem()).getDamageVsEntity();
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 1.25f;
        damage += EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 0.01f;
        return damage;
    }
}
