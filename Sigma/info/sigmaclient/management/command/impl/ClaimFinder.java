package info.sigmaclient.management.command.impl;

import info.sigmaclient.event.Event;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.util.StringConversions;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;

/**
 * Created by Arithmo on 5/3/2017 at 5:48 PM.
 */
public class ClaimFinder extends Command {

    public ClaimFinder(String[] names, String description) {
        super(names, description);
    }

    @Override
    public void fire(String[] args) {
        if (args == null) {
            return;
        }
        assert args.length == 1 && StringConversions.isNumeric(args[0]);
        if (mc.thePlayer.isRiding() && mc.thePlayer.ridingEntity instanceof EntityBoat) {
            int chunks = Integer.parseInt(args[0]) * 8;
            double topLeftX = mc.thePlayer.posX - chunks;
            double topLeftZ = mc.thePlayer.posY - chunks;
            for (int i = 0; i < Integer.parseInt(args[0]); i++) {
                int chunk = 16 + (i * 16);
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(topLeftX + chunk, mc.thePlayer.posY, topLeftZ + chunk, false));
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(topLeftX + chunk, mc.thePlayer.posY, topLeftZ + chunk, true));
            }
            for (int i = 0; i < 17; i++) {
                mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C02PacketUseEntity(mc.thePlayer.ridingEntity, C02PacketUseEntity.Action.ATTACK));
            }

            return;
        } else {
            ChatUtil.printChat(chatPrefix + "ERROR: you are not on a boat.");
            return;
        }
    }

    @Override
    public String getUsage() {
        return "Invalid Chunk number!";
    }

    @Override
    public void onEvent(Event event) {

    }
}
