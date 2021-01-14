package moonx.ohare.client.module.impl.combat;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.MoveUtil;
import moonx.ohare.client.utils.Printer;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * made by oHare for eclipse
 *
 * @since 8/30/2019
 **/
public class Velocity extends Module {
    private NumberValue<Integer> horizontalModifier = new NumberValue<>("Horizontal", 0, 0, 100, 1);
    private NumberValue<Integer> verticalModifier = new NumberValue<>("Vertical", 0, 0, 100, 1);
    private EnumValue<mode> Mode = new EnumValue<>("Mode", mode.NORMAL);

    public Velocity() {
        super("Velocity", Category.COMBAT, new Color(0x7E7E7E).getRGB());
        setRenderLabel("Velocity");
    }

    private enum mode {
        NORMAL, AAC, DEV
    }

    @Handler
    public void onPacket(PacketEvent event) {
        final int vertical = verticalModifier.getValue();
        final int horizontal = horizontalModifier.getValue();
        if (getMc().thePlayer == null || getMc().theWorld == null) return;
        switch (Mode.getValue()) {
            case NORMAL:
            if (!event.isSending()) {
                if (event.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                    if (packet.getEntityID() == getMc().thePlayer.getEntityId()) {
                        if (packet.getEntityID() != getMc().thePlayer.getEntityId()) return;
                        if (vertical != 0 || horizontal != 0) {
                            packet.setMotionX(horizontal * packet.getMotionX() / 100);
                            packet.setMotionY(vertical * packet.getMotionY() / 100);
                            packet.setMotionZ(horizontal * packet.getMotionZ() / 100);
                        } else event.setCanceled(true);
                    }
                }
                if (event.getPacket() instanceof S27PacketExplosion) {
                    S27PacketExplosion packet = (S27PacketExplosion) event.getPacket();
                    if (vertical != 0 || horizontal != 0) {
                        packet.setField_149152_f(horizontal * packet.getField_149152_f() / 100);
                        packet.setField_149153_g(vertical * packet.getField_149153_g() / 100);
                        packet.setField_149159_h(horizontal * packet.getField_149159_h() / 100);
                    } else event.setCanceled(true);
                }
            }
            break;
            case AAC:
                if (!event.isSending()) {
                    if (event.getPacket() instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                        if (packet.getEntityID() != getMc().thePlayer.getEntityId()) return;
                        //packet.setMotionX(120 * packet.getMotionX() / 100);
                        //packet.setMotionY(100 * packet.getMotionY() / 100);
                        //packet.setMotionZ(120 * packet.getMotionZ() / 100);
                    }
                }
                break;
            case DEV:
                /*if (!event.isSending()) {
                    if (event.getPacket() instanceof S12PacketEntityVelocity) {
                        S12PacketEntityVelocity packet = (S12PacketEntityVelocity) event.getPacket();
                        if (packet.getEntityID() == getMc().thePlayer.getEntityId()) {
                            event.setCanceled(true);
                            new Thread(() -> {
                                try {
                                    int maxDelay = 2000;
                                    Thread.sleep(maxDelay);
                                    //packet.setField_149153_g(getMc().thePlayer.onGround ? 0.42f : 0.5f);
                                    getMc().getNetHandler().handleEntityVelocity(packet);
                                    // getMc().getNetHandler().handleEntityVelocity(packet);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                return;
                            }).start();
                        }
                    }
                }*/
                break;
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        switch (Mode.getValue()) {
            case NORMAL:
                break;
            case AAC:
                if (event.isPre()) {
                    if (getMc().thePlayer.hurtResistantTime > 13 && getMc().thePlayer.hurtResistantTime < 20 && !getMc().thePlayer.onGround) {
                        if (getMc().thePlayer.hurtResistantTime == 19) {
                            getMc().thePlayer.motionX *= 0.85;
                            getMc().thePlayer.motionZ *= 0.85;
                        }
                        else {
                            //getMc().thePlayer.motionY -= 0.01499;
                            getMc().thePlayer.onGround = true;
                            double tick = Math.max(0, 16 - getMc().thePlayer.hurtResistantTime);
                            //MoveUtil.setSpeed(MoveUtil.getSpeed() * (0.1 + tick / 20));
                            double speed = Math.min(0.05 + (tick * 0.01), 0.3125);
                           // MoveUtil.setSpeed(speed);
                           // Printer.print("" + speed);
                            //MoveUtil.setSpeed(0.3);
                            //MoveUtil.setSpeed(MoveUtil.getSpeed() * 0.6);
                            // getMc().thePlayer.onGround = true;
                        }
                    }
                }
                break;
            case DEV:
                if (event.isPre()) {
                    if (getMc().thePlayer.hurtResistantTime == 16) {
                        double x = getMc().thePlayer.motionX * 0.95;
                        double z = getMc().thePlayer.motionZ * 0.95;
                        //Printer.print("" + x);
                        getMc().thePlayer.setVelocity(x, getMc().thePlayer.motionY, z);
                        //  getMc().thePlayer.onGround = true;
                    }
                    /*if (getMc().thePlayer.hurtResistantTime >= 16 && getMc().thePlayer.hurtResistantTime < 17 && getMc().thePlayer.ticksExisted % 3 == 0) {
                        double x = -getMc().thePlayer.motionX;
                        double z = -getMc().thePlayer.motionZ;
                        Printer.print("" + x);
                        getMc().thePlayer.setVelocity(x, getMc().thePlayer.motionY, z);
                        //  getMc().thePlayer.onGround = true;
                    }*/
                }
                break;
        }
    }
}
