package moonx.ohare.client.module.impl.combat;

import java.awt.Color;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.TimerUtil;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import org.lwjgl.input.Mouse;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;


public class AutoApple extends Module {
    private boolean eatingApple;
    private int switched = -1;
    public static boolean doingStuff = false;
    private final TimerUtil timer = new TimerUtil();
    private final BooleanValue eatHeads = new BooleanValue("Eat heads","Heal width Heads", true);
    private final BooleanValue eatApples = new BooleanValue("Eat apples","Heal width apples", true);
    private final NumberValue<Integer> health = new NumberValue<>("Health", 10, 1, 20, 1);
    private final NumberValue<Integer> delay = new NumberValue<>("Delay", 750, 100, 2000, 25);

    public AutoApple() {
        super("AutoApple", Category.COMBAT, new Color(255, 255, 0, 255).getRGB());
        setDescription("Automatically eats golden apples / heads when at low health");
        setRenderLabel("Auto Apple");
    }

    @Override
    public void onEnable() {
        eatingApple = doingStuff = false;
        switched = -1;
        timer.reset();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        doingStuff = false;
        if (eatingApple) {
            repairItemPress();
            repairItemSwitch();
        }
        super.onDisable();
    }

    private void repairItemPress() {
        if (getMc().gameSettings != null) {
            final KeyBinding keyBindUseItem = getMc().gameSettings.keyBindUseItem;
            if (keyBindUseItem != null) keyBindUseItem.pressed = false;
        }
    }


    @Handler
    public void onUpdate(UpdateEvent event) {
        if (getMc().thePlayer == null) return;
        final InventoryPlayer inventory = getMc().thePlayer.inventory;
        if (inventory == null) return;
        doingStuff = false;
        if (!Mouse.isButtonDown(0) && !Mouse.isButtonDown(1)) {
            final KeyBinding useItem = getMc().gameSettings.keyBindUseItem;

            if (!timer.reach(delay.getValue())) {
                eatingApple = false;
                repairItemPress();
                repairItemSwitch();
                return;
            }

            if (getMc().thePlayer.capabilities.isCreativeMode || getMc().thePlayer.isPotionActive(Potion.regeneration) ||getMc().thePlayer.getHealth() >= health.getValue()) {
                timer.reset();
                if (eatingApple) {
                    eatingApple = false;
                    repairItemPress();
                    repairItemSwitch();
                }
                return;
            }

            for (int i = 0; i < 2; i++) {
                final boolean doEatHeads = i != 0;

                if (doEatHeads) {
                    if (!eatHeads.isEnabled()) continue;
                } else {
                    if (!eatApples.isEnabled()) {
                        eatingApple = false;
                        repairItemPress();
                        repairItemSwitch();
                        continue;
                    }
                }

                int slot;

                if (doEatHeads) {
                    slot = getItemFromHotbar(397);
                } else {
                    slot = getItemFromHotbar(322);
                }

                if (slot == -1) continue;

                final int tempSlot = inventory.currentItem;

                doingStuff = true;
                if (doEatHeads) {
                    getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));
                    getMc().thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(inventory.getCurrentItem()));
                    getMc().thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(tempSlot));
                    timer.reset();
                } else {
                    inventory.currentItem = slot;
                    useItem.pressed = true;
                    if (eatingApple) continue; // no message spam
                    eatingApple = true;
                    switched = tempSlot;
                }
                Printer.print(String.format("Automatically ate a %s", doEatHeads ? "player head" : "golden apple"));
            }
        }
    }

    private void repairItemSwitch() {
        final EntityPlayerSP p = getMc().thePlayer;
        if (p == null) return;
        final InventoryPlayer inventory = p.inventory;
        if (inventory == null) return;
        int switched = this.switched;
        if (switched == -1) return;
        inventory.currentItem = switched;
        switched = -1;
        this.switched = switched;
    }

    private int getItemFromHotbar(final int id) {
        for (int i = 0; i < 9; i++) {
            if (getMc().thePlayer.inventory.mainInventory[i] != null) {
                final ItemStack is = getMc().thePlayer.inventory.mainInventory[i];
                final Item item = is.getItem();
                if (Item.getIdFromItem(item) == id) {
                    return i;
                }
            }
        }
        return -1;
    }
}
