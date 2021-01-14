package cedo.modules.player;

import cedo.Fan;
import cedo.events.Event;
import cedo.events.listeners.EventMotion;
import cedo.modules.Module;
import cedo.modules.movement.Scaffold;
import net.minecraft.block.BlockBed;
import net.minecraft.network.play.client.C07PacketPlayerDigging.Action;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class BedBreaker extends Module {

    public BedBreaker() {
        super("BedBreaker", 0, Category.PLAYER);
    }

    @Override
    public void onEvent(Event e) {
        if (e instanceof EventMotion) {
            if (e.isPre()) {
                for(int x = -3; x < 3; x++) {
                    for(int y = -3; y < 3; y++) {
                        for(int z = -3; z < 3; z++) {
                            BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);

                            if (pos.getBlock() instanceof BlockBed) {
                                float[] rot = Fan.scaffold.getRotationsHypixel(pos, EnumFacing.NORTH);

                                ((EventMotion) e).setYaw(rot[0]);
                                ((EventMotion) e).setPitch(rot[1]);

                                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(Action.START_DESTROY_BLOCK, pos, EnumFacing.NORTH));
                                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(Action.STOP_DESTROY_BLOCK, pos, EnumFacing.NORTH));
                                mc.getNetHandler().addToSendQueue(new C0APacketAnimation());
                            }
                        }
                    }
                }
            }
        }
    }
}
