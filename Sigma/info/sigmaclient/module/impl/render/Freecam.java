/**
 * Time: 10:24:50 PM
 * Date: Jan 1, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.render;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventBlockBounds;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventMove;
import info.sigmaclient.event.impl.EventPushBlock;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

/**
 * @author cool1
 */
public class Freecam extends Module {

    public static String SPEED = "SPEED";
    private EntityOtherPlayerMP freecamEntity;

    /**
     * @param data
     */
    public Freecam(ModuleData data) {
        super(data);
        settings.put(SPEED, new Setting<>(SPEED, 1.0, "Movement speed.", 0.5, 0.5, 10));
    }

    public void onDisable() {
        mc.thePlayer.setPositionAndRotation(this.freecamEntity.posX, this.freecamEntity.posY, this.freecamEntity.posZ, this.freecamEntity.rotationYaw, this.freecamEntity.rotationPitch);
        mc.theWorld.removeEntityFromWorld(this.freecamEntity.getEntityId());
        mc.renderGlobal.loadRenderers();
        mc.thePlayer.noClip = false;
        super.onDisable();
    }

    public void onEnable() {
        if (mc.thePlayer == null) {
            return;
        }
        this.freecamEntity = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(new UUID(69L, 96L), "XDDD"));
        this.freecamEntity.inventory = mc.thePlayer.inventory;
        this.freecamEntity.inventoryContainer = mc.thePlayer.inventoryContainer;
        this.freecamEntity.setPositionAndRotation(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
        this.freecamEntity.rotationYawHead = mc.thePlayer.rotationYawHead;
        mc.theWorld.addEntityToWorld(this.freecamEntity.getEntityId(), this.freecamEntity);
        mc.renderGlobal.loadRenderers();
        super.onEnable();
    }

    /*
     * (non-Javadoc)
     *
     * @see EventListener#onEvent(Event)
     */
    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873D;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= (1.0D + 0.2D * (amplifier + 1));
        }
        return baseSpeed;
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class, EventPacket.class, EventBlockBounds.class, EventMove.class,
            EventPushBlock.class})
    public void onEvent(Event event) {
        float speed = ((Number) settings.get(SPEED).getValue()).floatValue();
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            mc.thePlayer.noClip = true;
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isOutgoing()) {
                if (ep.getPacket() instanceof C03PacketPlayer) {
                    C03PacketPlayer packet = (C03PacketPlayer) ep.getPacket();
                    packet.yaw = freecamEntity.rotationYaw;
                    packet.pitch = freecamEntity.rotationPitch;
                    packet.x = freecamEntity.posX;
                    packet.y = freecamEntity.posY;
                    packet.z = freecamEntity.posZ;
                    packet.onGround = freecamEntity.onGround;
                } else if (ep.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                    C03PacketPlayer.C04PacketPlayerPosition packet = (C03PacketPlayer.C04PacketPlayerPosition) ep.getPacket();
                    packet.yaw = freecamEntity.rotationYaw;
                    packet.pitch = freecamEntity.rotationPitch;
                    packet.x = freecamEntity.posX;
                    packet.y = freecamEntity.posY;
                    packet.z = freecamEntity.posZ;
                    packet.onGround = freecamEntity.onGround;
                }
            }
        }
        if (event instanceof EventBlockBounds) {
            EventBlockBounds ebb = (EventBlockBounds) event;
            ebb.setCancelled(true);
        }
        if (event instanceof EventMove) {
            EventMove em = (EventMove) event;
            if (mc.thePlayer.movementInput.jump) {
                em.setY(mc.thePlayer.motionY = speed);
            } else if (mc.thePlayer.movementInput.sneak) {
                em.setY(mc.thePlayer.motionY = -speed);
            } else {
                em.setY(mc.thePlayer.motionY = 0.0D);
            }
            speed = (float) Math.max(speed, getBaseMoveSpeed());
            double forward = mc.thePlayer.movementInput.moveForward;
            double strafe = mc.thePlayer.movementInput.moveStrafe;
            float yaw = mc.thePlayer.rotationYaw;
            if ((forward == 0.0D) && (strafe == 0.0D)) {
                em.setX(0.0D);
                em.setZ(0.0D);
            } else {
                if (forward != 0.0D) {
                    if (strafe > 0.0D) {
                        strafe = 1;
                        yaw += (forward > 0.0D ? -45 : 45);
                    } else if (strafe < 0.0D) {
                        yaw += (forward > 0.0D ? 45 : -45);
                    }
                    strafe = 0.0D;
                    if (forward > 0.0D) {
                        forward = 1;
                    } else if (forward < 0.0D) {
                        forward = -1;
                    }
                }
                em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F))
                        + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
                em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F))
                        - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
            }
        }
        if (event instanceof EventPushBlock) {
            EventPushBlock ebp = (EventPushBlock) event;
            ebp.setCancelled(true);
        }

    }

}
