/**
 * Time: 12:16:54 AM
 * Date: Dec 26, 2016
 * Creator: cool1
 */
package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.Timer;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

/**
 * @author cool1
 */
public class AutoSoup extends Module {

    Timer timer = new Timer();
    public static String DELAY = "DELAY";
    public static String HEALTH = "HEALTH";
    public String DROP = "DROP";

    /**
     * @param data
     */
    public AutoSoup(ModuleData data) {
        super(data);
        settings.put(HEALTH, new Setting(HEALTH, 3, "Maximum health before healing."));
        settings.put(DELAY, new Setting(DELAY, 350, "Delay before healing again.", 50, 100, 1000));
        settings.put(DROP, new Setting(DROP, false, "Drop soup after use."));
    }

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                int soupSlot = getSoupFromInventory();
                if (soupSlot != -1 && mc.thePlayer.getHealth() < ((Number) settings.get(HEALTH).getValue()).floatValue()
                        && timer.delay(((Number) settings.get(DELAY).getValue()).floatValue())) {
                    swap(getSoupFromInventory(), 6);
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(6));
                    mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                }
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    public static int getSoupFromInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        int soup = -1;
        int counter = 0;
        for (int i = 1; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (Item.getIdFromItem(item) == 282) {
                    counter++;
                    soup = i;
                }
            }
        }
        return soup;
    }
}
