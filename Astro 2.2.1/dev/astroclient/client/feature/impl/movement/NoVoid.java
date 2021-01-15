package dev.astroclient.client.feature.impl.movement;

import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;

@Toggleable(label = "AntiVoid", category = Category.MOVEMENT)
public class NoVoid extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", "Packet", new String[]{"Packet", ""});
    public NumberProperty<Float> distance = new NumberProperty<>("Distance", true, 6F, 1F, 1F, 20F);

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        if (eventMotion.getEventType() == EventType.PRE) {
            if (mc.thePlayer.fallDistance > distance.getValue() && !Client.INSTANCE.featureManager.flight.getState()) {
                if (!isBlockUnder()) {
                    mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 12, mc.thePlayer.posZ, false));
                    mc.thePlayer.fallDistance = 0;
                }
            }
        }
    }

    private boolean isBlockUnder() {
        if (mc.thePlayer.posY < 0)
            return false;
        for (int offset = 0; offset < (int) mc.thePlayer.posY + 2; offset += 2) {
            AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);
            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
