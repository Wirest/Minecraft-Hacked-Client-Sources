package info.sigmaclient.module.impl.combat;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoGApple extends Module {

    private Timer timer = new Timer();
    private Timer eatTimer = new Timer();
    public static String DELAY = "DELAY";
    public static String HEALTH = "HEALTH";
    private int prevSlot = -1;

    public AutoGApple(ModuleData data) {
        super(data);
        settings.put(HEALTH, new Setting(HEALTH, 3, "Maximum health before eating."));
        settings.put(DELAY, new Setting(DELAY, 350, "Delay before eating again.", 50, 100, 1000));
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
                int appleSlot = getGAppleFromInventory();
                if (appleSlot != -1 && prevSlot < 0
                        && mc.thePlayer.getHealth() < ((Number) settings.get(HEALTH).getValue()).floatValue()
                        && !mc.thePlayer.isPotionActive(Potion.regeneration.id)
                        && timer.delay(((Number) settings.get(DELAY).getValue()).floatValue())) {
                    swap(getGAppleFromInventory(), 6);

                    prevSlot = mc.thePlayer.inventory.currentItem;
                    mc.thePlayer.inventory.currentItem = 6;

                    //mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
                    //mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
                    mc.rightClickMouse();
                    //ChatUtil.printChat("Start eating");
                    eatTimer.reset();
                    timer.reset();
                } else if (prevSlot >= 0 && eatTimer.getDifference() > 35 * 50) {
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);

                    mc.thePlayer.inventory.currentItem = prevSlot;
                    prevSlot = -1;
                    /*mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    mc.thePlayer.stopUsingItem();*/
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    //ChatUtil.printChat("Stop eating");
                    timer.reset();
                }
            }
        }
    }

    protected void swap(int slot, int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    public static int getGAppleFromInventory() {
        Minecraft mc = Minecraft.getMinecraft();
        int apple = -1;
        int counter = 0;
        for (int i = 1; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (Item.getIdFromItem(item) == 322) {
                    counter++;
                    apple = i;
                }
            }
        }
        return apple;
    }

    public boolean isEating() {
        return prevSlot >= 0;
    }
}
