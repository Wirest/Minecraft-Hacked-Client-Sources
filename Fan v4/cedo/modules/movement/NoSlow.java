package cedo.modules.movement;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.events.listeners.EventPacket;
import cedo.events.listeners.EventSlowDown;
import cedo.modules.Module;
import cedo.settings.impl.ModeSetting;
import cedo.util.movement.MovementUtil;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;

@SuppressWarnings("rawtypes")
public class NoSlow extends Module {

    public ModeSetting mode = new ModeSetting("Mode", "Hypixel", "Hypixel", "Vanilla");


    public NoSlow() {
        super("NoSlow", Keyboard.KEY_NONE, Category.MOVEMENT);
    }


    public void onEvent(Event e) {
        if (e instanceof EventSlowDown) {
            e.setCancelled(true);
        }
        if (e instanceof EventPacket && e.isPre() && e.isOutgoing()) {
            EventPacket event = (EventPacket) e;
            if (shouldNoSlow() && event.getPacket() instanceof C08PacketPlayerBlockPlacement && isAirClick(event.getPacket())) {
                if (mode.is("Hypixel")) {
                    //KillAura.blocking = true;
                    event.setPacket(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                }
            }
        }
        if (e instanceof EventMotion) {
            if (mode.is("Hypixel")) {
                if (mc.thePlayer.isBlocking() && MovementUtil.isMoving() && MovementUtil.isOnGround(0.42) && Fan.killaura.target == null) {
                    if (e.isPre()) {
                        mc.getNetHandler().getNetworkManager().sendPacket((
                                new C07PacketPlayerDigging(
                                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                                        new BlockPos(RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE), RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)),
                                        EnumFacing.DOWN)));
                    } else {
                        mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                    }
                }
            }
        }
    }

    /**
     * <p>shouldNoSlow.</p>
     *
     * @return a boolean.
     */
    public boolean shouldNoSlow() {
        return mc.thePlayer != null && mc.thePlayer.getHeldItem() != null && (mc.thePlayer.getHeldItem().getItem() instanceof ItemSword || mc.thePlayer.getHeldItem().getItem() == Items.bow || mc.thePlayer.getHeldItem().getItem() instanceof ItemFood);
    }

    /**
     * <p>isAirClick.</p>
     *
     * @param packet a {@link net.minecraft.network.play.client.C08PacketPlayerBlockPlacement} object.
     * @return a boolean.
     */
    public boolean isAirClick(C08PacketPlayerBlockPlacement packet) {
        BlockPos pos = packet.getPosition();
        return pos.getX() == -1 && pos.getY() == -1 && pos.getZ() == -1;
    }

}
