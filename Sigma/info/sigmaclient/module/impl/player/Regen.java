package info.sigmaclient.module.impl.player;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.module.Module;
import net.minecraft.block.material.Material;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * Created by Arithmo on 5/17/2017 at 11:01 PM.
 */
public class Regen extends Module {

    private final String HEALTHXD = "HEALTH";

    public Regen(ModuleData data) {
        super(data);
        settings.put(HEALTHXD, new Setting<>(HEALTHXD, 7, "Health to regen at.", 0.5, 1, 10));
    }

    @RegisterEvent(events = EventUpdate.class)
    public void onEvent(Event event) {
        EventUpdate em = (EventUpdate) event;
        if (em.isPre()) {
            double health = ((Number) settings.get(HEALTHXD).getValue()).doubleValue();
            if (mc.thePlayer.getHealth() < health * 2 && mc.thePlayer.getFoodStats().getFoodLevel() > 16 && !mc.thePlayer.isUsingItem() && mc.thePlayer.isCollidedVertically &&
                    mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.getIsKeyPressed() && (!mc.thePlayer.isInsideOfMaterial(Material.lava)) &&
                    !mc.thePlayer.isInWater()) {
                for (int i = 0; i < 40; i++) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
        }
    }
}
