package cedo.modules.player;

import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.settings.impl.NumberSetting;
import cedo.util.server.PacketUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class AntiVoid extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "Jump", "Normal", "Jump");
    private final NumberSetting motionY = new NumberSetting("Jump motion", 2.5, 0.1, 5, 0.1);
    private final NumberSetting jumpTicks = new NumberSetting("Jump ticks", 20, 1, 100, 1);
    private double lastGroundY;

    public AntiVoid() {
        super("AntiVoid", Keyboard.KEY_NONE, Category.PLAYER);
        addSettings(mode, motionY, jumpTicks);
    }

    public static boolean isBlockUnder() {
        for (int offset = 0; offset < mc.thePlayer.posY + mc.thePlayer.getEyeHeight(); offset += 2) {
            AxisAlignedBB boundingBox = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

            if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, boundingBox).isEmpty())
                return true;
        }
        return false;
    }

    public void onEvent(Event e) {
        if (e instanceof EventMotion && e.isPre()) {
            if (mode.is("Normal")) {
                if (!isBlockUnder() && mc.thePlayer.fallDistance > 3 && mc.thePlayer.ticksExisted % 5 == 0) {
                    PacketUtil.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.nextFloat(6F, 10F), mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.ticksExisted % 2 == 0));
                }
            } else if (mode.is("Jump")) {
                if (mc.thePlayer.onGround) {
                    lastGroundY = mc.thePlayer.posY;
                }
                if (mc.thePlayer.ticksExisted % 3 == 0 && !isBlockUnder() && lastGroundY - mc.thePlayer.posY > 10) {
                    if (mc.thePlayer.hurtTime > 0) {
                        mc.thePlayer.motionY = motionY.getValue();
                        mc.thePlayer.fallDistance = 0;
                    } else {
                        if (mc.thePlayer.ticksExisted % jumpTicks.getValue() == 0) {
                            mc.thePlayer.fallDistance = 0;
                        }
                        mc.thePlayer.motionY = 0;

                    }
                }
            }
        }
    }
}