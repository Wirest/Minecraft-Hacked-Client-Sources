/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.misc;

import darkmagician6.EventTarget;
import darkmagician6.events.EventPacketRecieve;
import java.util.List;
import java.util.UUID;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketSpawnPlayer;

@Module.Mod(category=Module.Category.MISC, description="Removes the AAC entity above you", key=0, name="AntiAAC")
public class AntiAAC
extends Module {
    @EventTarget
    public void onPacket(EventPacketRecieve event) {
        if (event.getPacket() instanceof SPacketSpawnPlayer) {
            SPacketSpawnPlayer packet = (SPacketSpawnPlayer)event.getPacket();
            int x = (int)packet.getX();
            int y = (int)packet.getY();
            int z = (int)packet.getZ();
            if (this.isAround((int)Wrapper.getPlayer().posY + 5, y, 2) && this.isAround((int)Wrapper.getPlayer().posX, x, 4) && this.isAround((int)Wrapper.getPlayer().posZ, z, 4)) {
                event.setCancelled(true);
            }
        }
    }

    public boolean isAround(int first, int second, int inc) {
        int i = 0;
        while (i < inc) {
            if (first == second + i) {
                return true;
            }
            ++i;
        }
        i = - inc;
        while (i < 0) {
            if (first == second + i) {
                return true;
            }
            ++i;
        }
        return false;
    }

    public EntityPlayer getPlayerBy(UUID uuid) {
        for (Object o : Wrapper.getWorld().loadedEntityList) {
            EntityPlayer ep;
            if (!(o instanceof EntityPlayer) || !(ep = (EntityPlayer)o).getUniqueID().equals(uuid)) continue;
            return ep;
        }
        return null;
    }

    public void runCmd(String s) {
    }
}

