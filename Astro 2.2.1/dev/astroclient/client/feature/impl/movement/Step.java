package dev.astroclient.client.feature.impl.movement;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.player.EventStep;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.Timer;
import net.minecraft.network.play.client.C03PacketPlayer;

import java.util.Arrays;
import java.util.List;

@Toggleable(label = "Step", category = Category.MOVEMENT)
public class Step extends ToggleableFeature {

    public NumberProperty<Float> height = new NumberProperty<>("Height", true, 2.5F, .1F, 1F, 2.5F);
    public NumberProperty<Integer> delay = new NumberProperty<>("Delay", true, 100, 1, 0, 600, Type.MILLISECONDS);

    public boolean stepping;

    private Timer timer = new Timer();

    @Subscribe
    public void onEvent(EventStep eventStep) {
        double realHeight = mc.thePlayer.getEntityBoundingBox().minY - mc.thePlayer.posY;
        boolean shouldStep = realHeight >= 1;
        if (mc.thePlayer.isCollidedVertically && mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isPressed() && timer.hasReached(delay.getValue()) && !mc.thePlayer.isInWater() && !mc.thePlayer.isInLava() && !Client.INSTANCE.featureManager.flight.getState()) {
            eventStep.setStepHeight(height.getValue());
            if (shouldStep) {
                stepping = true;
                mc.timer.timerSpeed = .5F;
                step(realHeight);
                Client.INSTANCE.featureManager.speed.stage = -4;

                new Thread(() -> {
                    try {
                        Thread.sleep((long) (100 * realHeight));
                    } catch (InterruptedException ignored) {
                        //
                    }
                    stepping = false;
                    mc.timer.timerSpeed = 1.0F;
                }).start();
            }
        } else if (!Client.INSTANCE.featureManager.flight.getState())
            eventStep.setStepHeight(.6);
    }

    // TODO: Not skidded NCP step
    private void step(double height) {
        List<Double> offset = Arrays.asList(0.42, 0.333, 0.248, 0.083, -0.078);
        double posX = mc.thePlayer.posX;
        double posZ = mc.thePlayer.posZ;
        double y = mc.thePlayer.posY;
        if (height < 1.1) {
            double first = 0.42;
            double second = 0.75;
            if (height != 1) {
                first *= height;
                second *= height;
                if (first > 0.425) {
                    first = 0.425;
                }
                if (second > 0.78) {
                    second = 0.78;
                }
                if (second < 0.49) {
                    second = 0.49;
                }
            }
            mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + first, posZ, false));
            if (y + second < y + height)
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + second, posZ, false));
            return;
        } else if (height < 1.6) {
            for (int i = 0; i < offset.size(); i++) {
                double off = offset.get(i);
                y += off;
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, y, posZ, false));
            }
        } else if (height < 2.1) {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869};
            for (double off : heights) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        } else {
            double[] heights = {0.425, 0.821, 0.699, 0.599, 1.022, 1.372, 1.652, 1.869, 2.019, 1.907};
            for (double off : heights) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(posX, y + off, posZ, false));
            }
        }
    }
}
