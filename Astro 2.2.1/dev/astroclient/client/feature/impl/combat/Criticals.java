package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.asdw.EventType;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.packet.EventSendPacket;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.player.EventTick;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.feature.impl.movement.Speed;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.BlockUtil;
import dev.astroclient.client.util.Timer;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;

@Toggleable(label = "Criticals", category = Category.COMBAT)
public class Criticals extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", true, "Packet", new String[]{"", "Packet"});

    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 400, 1, 0, 1000, Type.MILLISECONDS);

    private int groundTicks;

    private Timer timer = new Timer();

    private double[] offsets = new double[]{0.051, 0.011511, 0.001, 0.001};

    @Subscribe
    public void onEvent(EventMotion event) {
        this.setSuffix(mode.getValue());
        if (event.getEventType() == EventType.PRE)
        if (mc.thePlayer.onGround)
            groundTicks++;
        else
            groundTicks = 0;
    }

    @Subscribe
    public void onEvent(EventSendPacket eventSendPacket) {
        if (eventSendPacket.getPacket() instanceof C0APacketAnimation) {
            boolean canCrit = !Client.INSTANCE.featureManager.step.stepping && !((ToggleableFeature) Client.INSTANCE.featureManager.get(Speed.class)).getState() &&
                    !Client.INSTANCE.featureManager.flight.getState() && mc.thePlayer.onGround &&
                    !mc.gameSettings.keyBindJump.isKeyDown() && !mc.thePlayer.isInWater() &&
                    !mc.thePlayer.isOnLadder() && BlockUtil.getBlockAtPos(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY + 1, mc.thePlayer.posZ)) instanceof BlockAir;
            if (canCrit)
                if (Client.INSTANCE.featureManager.killAura.target != null || mc.objectMouseOver.entityHit != null)
                    this.crit();
        }
    }

    private void crit() {
        if (timer.hasReached(delay.getValue()) && groundTicks > 1) {
            for (double offset : offsets)
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
            timer.reset();
        }
    }
}
