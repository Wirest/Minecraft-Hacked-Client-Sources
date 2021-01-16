package me.razerboy420.weepcraft.module.modules.combat;

import darkmagician6.EventManager;
import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.module.Module.Category;
import me.razerboy420.weepcraft.module.Module.Mod;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.value.Value;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemSoup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;

@Mod(category = Category.COMBAT, description = "Eats soup when your health is low", key = 0, name = "AutoSoup")
public class AutoSoup extends Module
{
    private int delay;
    public static int soups;
    public static Value health;
    
    static {
        AutoSoup.health = new Value("autosoup_Health", 14.0f, 5.0f, 19.0f, 0.1f);
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onPre(final EventPreMotionUpdates pre) {
        this.setDisplayName("AutoSoup [" + this.getTotalSoups() + "]");
        this.getTotalSoups();
        if (this.shouldHeal()) {
            ++this.delay;
        }
        if (this.isSoupOnHotbar() && this.shouldHeal() && this.delay >= 5) {
            this.drinkSoup();
            this.delay = 0;
        }
        else if (!this.isSoupOnHotbar() && this.shouldHeal() && this.getTotalSoups() > 0) {
            this.getSoup();
        }
    }
    
    private boolean isStackSoup(final ItemStack stack) {
        return stack != null && stack.getItem() instanceof ItemSoup;
    }
    
    private boolean shouldHeal() {
        return Wrapper.getPlayer().getHealth() <= AutoSoup.health.value;
    }
    
    private boolean isSoupOnHotbar() {
        for (int index = 36; index < 45; ++index) {
            final ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
            if (stack != null && this.isStackSoup(stack)) {
                return true;
            }
        }
        return false;
    }
    
    private void getSoup() {
        if (!(Wrapper.mc().currentScreen instanceof GuiChest)) {
            for (int index = 9; index < 36; ++index) {
                final ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
                if (!this.isStackSoup(stack)) {
                    this.dropLastSlot();
                }
                if (stack != null && this.isStackSoup(stack)) {
                    Wrapper.mc().playerController.windowClick(0, index, 0, ClickType.SWAP, Wrapper.getPlayer());
                    break;
                }
            }
        }
    }
    
    private void dropLastSlot() {
        for (int i1 = 0; i1 < 45; ++i1) {
            final ItemStack itemStack = Wrapper.getPlayer().inventoryContainer.getSlot(i1).getStack();
            if (itemStack != null && i1 > 43 && i1 <= 44) {
                Wrapper.mc().playerController.windowClick(0, i1, 0, ClickType.THROW, Wrapper.getPlayer());
                Wrapper.mc().playerController.windowClick(0, 64537, 0, ClickType.THROW, Wrapper.getPlayer());
                break;
            }
        }
    }
    
    private void drinkSoup() {
        for (int slot = 36; slot < 45; ++slot) {
            final ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(slot).getStack();
            if (stack != null && this.isStackSoup(stack)) {
                final int lastSlot = Wrapper.getPlayer().inventory.currentItem;
                Wrapper.sendPacket(new CPacketHeldItemChange(slot - 36));
                Wrapper.mc().playerController.updateController();
                Wrapper.mc().rightClickMouse();
                Wrapper.mc().playerController.windowClick(0, slot, 0, ClickType.QUICK_MOVE, Wrapper.getPlayer());
                Wrapper.sendPacket(new CPacketHeldItemChange(lastSlot));
                break;
            }
        }
    }
    
    public int getTotalSoups() {
        AutoSoup.soups = 0;
        for (int index = 9; index < 45; ++index) {
            final ItemStack stack = Wrapper.getPlayer().inventoryContainer.getSlot(index).getStack();
            if (this.isStackSoup(stack)) {
                AutoSoup.soups += stack.stackSize;
            }
        }
        return AutoSoup.soups;
    }
}
