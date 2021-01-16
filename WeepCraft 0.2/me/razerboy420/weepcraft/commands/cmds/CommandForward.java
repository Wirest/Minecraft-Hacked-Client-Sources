/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import java.util.ArrayList;
import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;

public class CommandForward
extends Command {
    public CommandForward() {
        super(new String[]{"forward"}, "Teleport forward", ".forward");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        float moved = 0.4f;
        int count = 0;
        try {
            ArrayList<CPacketPlayer.Position> packets = new ArrayList<CPacketPlayer.Position>();
            int i = 0;
            while (i < 120) {
                double mx = Math.cos(Math.toRadians(Wrapper.getPlayer().rotationYaw - 90.0f));
                double mz = Math.sin(Math.toRadians(Wrapper.getPlayer().rotationYaw - 90.0f));
                double x1 = (- (double)moved) * mx - 0.0 * mz + Wrapper.getPlayer().posX;
                double z1 = (- (double)moved) * mz - 0.0 * mx + Wrapper.getPlayer().posZ;
                packets.add(new CPacketPlayer.Position(x1, Wrapper.getPlayer().posY + (count == 3 ? 0.3994 : 0.0), z1, count != 3));
                moved += 0.2f;
                count = count == 3 ? 0 : ++count;
                ++i;
            }
            for (Packet p : packets) {
                Wrapper.sendPacketDirect(p);
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}

