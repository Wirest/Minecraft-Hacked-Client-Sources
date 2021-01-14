package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import net.minecraft.item.ItemFood;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

public class AutoEat extends Module {

    private String FOODLEVEL = "LEVEL";

    public AutoEat(ModuleData data) {
        super(data);
        settings.put(FOODLEVEL, new Setting<>(FOODLEVEL, 8.5, "Food level to eat at.", 0.5, 0, 10));
    }

    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        int foodSlot = getFoodSlotInHotbar();
        int eatFood = ((Number) settings.get(FOODLEVEL).getValue()).intValue();
        if ((foodSlot != -1) && (mc.thePlayer.getFoodStats().getFoodLevel() < eatFood * 2.0D) && (mc.thePlayer.isCollidedVertically)) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(foodSlot));
            mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.mainInventory[foodSlot]));
            for (int i = 0; i < 32; i++) {
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(false));
            }
            mc.thePlayer.stopUsingItem();
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    private int getFoodSlotInHotbar() {
        for (int i = 0; i < 9; i++) {
            if ((mc.thePlayer.inventory.mainInventory[i] != null) && (mc.thePlayer.inventory.mainInventory[i].getItem() != null) && ((mc.thePlayer.inventory.mainInventory[i].getItem() instanceof ItemFood))) {
                return i;
            }
        }
        return -1;
    }
}
