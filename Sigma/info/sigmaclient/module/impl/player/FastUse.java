
package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastUse extends Module {

    public FastUse(ModuleData data) {
        super(data);
        settings.put(MODE, new Setting<>(MODE, new Options("Use Mode", "Packet", new String[]{"Packet", "Timer"}), "Fast Use method."));
        settings.put(TICKS, new Setting<>(TICKS, 12, "Ticks reached to Fast Use.", 1, 1, 20));
    }

    private String MODE = "MODE";
    private String TICKS = "TICKS";

    @Override
    public void onEnable() {
        super.onEnable();
        mc.timer.timerSpeed = 1;
    }

    @Override
    public void onDisable() {
        super.onDisable();
        mc.timer.timerSpeed = 1;
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                String str = ((Options) settings.get(MODE).getValue()).getSelected();
                setSuffix(str);
                switch (str) {
                    case "Timer": {
                        if(mc.thePlayer.getItemInUseDuration() >= ((Number) settings.get(TICKS).getValue()).intValue() && canUseItem(mc.thePlayer.getItemInUse().getItem())) {
                            mc.timer.timerSpeed = 1.3555f;
                        } else if (mc.timer.timerSpeed == 1.3555f) {
                            mc.timer.timerSpeed = 1;
                        }
                        break;
                    }
                    case "Packet": {
                        if (mc.thePlayer.getItemInUseDuration() == ((Number) settings.get(TICKS).getValue()).intValue() && canUseItem(mc.thePlayer.getItemInUse().getItem())) {
                        	for (int i = 0; i < 30; i++) {
                                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                            }
                            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                            mc.thePlayer.stopUsingItem();
                        }
                        break;
                    }
                }
            }
        }
    }

    private boolean canUseItem(Item item) {
    	boolean result = !((item instanceof ItemSword) || (item instanceof ItemBow));
        return result;
    }

}
