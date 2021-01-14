package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.PlayerUtil;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * Created by cool1 on 2/4/2017.
 */
public class Effects extends Module {

    public Effects(ModuleData data) {
        super(data);
    }

    @Override
    @RegisterEvent(events = EventUpdate.class)
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        if (em.isPre()) {
            if (PlayerUtil.isOnLiquid() || PlayerUtil.isInLiquid()) {
                return;
            }
            if (mc.thePlayer.isPotionActive(Potion.blindness.getId())) {
                mc.thePlayer.removePotionEffect(Potion.blindness.getId());
            }
            if (mc.thePlayer.isPotionActive(Potion.confusion.getId())) {
                mc.thePlayer.removePotionEffect(Potion.confusion.getId());
            }
            if (mc.thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
                mc.thePlayer.removePotionEffect(Potion.digSlowdown.getId());
            }
            if (mc.thePlayer.isBurning() && mc.thePlayer.isCollidedVertically) {
                for (int i = 0; i < 12; ++i) {
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                }
            }
            Potion[] potionTypes;
            for (int length = (potionTypes = Potion.potionTypes).length, j = 0; j < length; ++j) {
                final Potion potion = potionTypes[j];
                if (potion != null && potion.isBadEffect() && mc.thePlayer.isPotionActive(potion)) {
                    final PotionEffect activePotionEffect = mc.thePlayer.getActivePotionEffect(potion);
                    for (int k = 0; k < activePotionEffect.getDuration() / 20; ++k) {
                        mc.getNetHandler().addToSendQueue(new C03PacketPlayer(true));
                    }
                }
            }
        }
    }

}
