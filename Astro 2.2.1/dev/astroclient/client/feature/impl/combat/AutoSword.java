package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.ItemUtil;
import dev.astroclient.client.util.Timer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

@Toggleable(label = "AutoSword", category = Category.COMBAT)
public class AutoSword extends ToggleableFeature {

    private Timer timer = new Timer();

    public BooleanProperty swap = new BooleanProperty("Swap", true, false);

    public NumberProperty<Integer> swordSlot = new NumberProperty<>("Sword Slot", true, 1, 1, 1, 9);

    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 100, 1, 0, 500, Type.MILLISECONDS);

    @Subscribe
    public void onEvent(EventMotion event) {
        if (event.getEventType() == EventType.PRE)
            if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory) {
                Item item;
                if (this.timer.hasReached(delay.getValue()))
                    for (int i = 0; i < 45; ++i) {
                        if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                            item = mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem();
                            if (item instanceof ItemSword) {
                                float itemDamage = ItemUtil.getSwordDamage(mc.thePlayer.inventoryContainer.getSlot(i).getStack());
                                float currentDamage = 0;
                                if (mc.thePlayer.inventory.getStackInSlot(swordSlot.getValue() - 1) != null && mc.thePlayer.inventory.getStackInSlot(swordSlot.getValue() - 1).getItem() instanceof ItemSword)
                                    currentDamage = ItemUtil.getSwordDamage(mc.thePlayer.inventory.getStackInSlot(swordSlot.getValue() - 1));
                                if (itemDamage > currentDamage) {
                                    ItemUtil.drop(swordSlot.getValue() - 1);
                                    ItemUtil.swap(i, swordSlot.getValue() - 1);
                                    this.timer.reset();
                                    break;
                                }
                            }
                        }
                    }

                if (swap.getValue())
                    if (mc.thePlayer.inventoryContainer.getSlot(36 + swordSlot.getValue() - 1).getHasStack()) {
                        item = mc.thePlayer.inventoryContainer.getSlot(36 + swordSlot.getValue() - 1).getStack().getItem();
                        if (item instanceof ItemSword && Client.INSTANCE.featureManager.killAura.target != null) {
                            mc.thePlayer.inventory.currentItem = swordSlot.getValue() - 1;
                        }
                    }
            }
    }
}
