package skyline.specc.mods.combat.antibot;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.MoveEvents.UpdateEvent;
import net.minecraft.client.Mineman;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.BlockPos;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventMotion;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.EventPlayerUpdate;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventTick;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventListener;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.main.EventType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModData;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModMode;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.ModType;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.mod.Module;
import skyline.specc.SkyLine;
import skyline.specc.extras.chat.ChatBuilder;
import skyline.specc.extras.chat.ChatColor;
import skyline.specc.mods.combat.AntiBot;
import skyline.specc.mods.combat.KillAuraMod;
import skyline.specc.utils.TimerUtils;
import skyline.specc.utils.Wrapper;

import static net.minecraft.client.Mineman.thePlayer;
import static net.minecraft.client.Mineman.theWorld;

public class Watchdodge extends ModMode<AntiBot>
{
public Watchdodge(AntiBot parent, String name) {
super(parent, name);
}

public static ArrayList<Entity> bots = new ArrayList<>();
private long time;
private TimerUtils timer = new TimerUtils();

@EventListener
public void onTick2(EventTick event) {
	if (mc.theWorld == null || mc.thePlayer == null || mc.theWorld.playerEntities.size() < 1) return;
	for (Object object : mc.theWorld.playerEntities) {
		EntityPlayer entityPlayer = (EntityPlayer) object;
		if (entityPlayer != null && entityPlayer != mc.thePlayer) {
			if (entityPlayer.getDisplayName().getFormattedText().equalsIgnoreCase(entityPlayer.getName() + "\247r") && !mc.thePlayer.getDisplayName().getFormattedText().equalsIgnoreCase(mc.thePlayer.getName() + "\247r") && entityPlayer.ticksExisted > 80) {
				mc.theWorld.removeEntity(entityPlayer);
				parent.addChat("Removed Bot with property: IllegalIdentifier");
			}
		}
	}
}

@EventListener
public void onPacket(EventPacket e) {
	if (e.getPacket() instanceof S0CPacketSpawnPlayer) {
		S0CPacketSpawnPlayer packet = (S0CPacketSpawnPlayer)e.getPacket();
		double posX = (double)packet.func_148942_f() / 32.0D;
		double posY = (double)packet.func_148949_g() / 32.0D;
		double posZ = (double)packet.func_148946_h() / 32.0D;
		double difX = Mineman.thePlayer.posX - posX;
		double difY = Mineman.thePlayer.posY - posY;
		double difZ = Mineman.thePlayer.posZ - posZ;
		double dist = Math.sqrt(difX * difX + difY * difY + difZ * difZ);
		if (dist <= 15.0D && Mineman.thePlayer.ticksExisted < 100) {
			if (posX != Mineman.thePlayer.posX) {
				if (posY != Mineman.thePlayer.posY) {
					if (posZ != Mineman.thePlayer.posZ) {
						e.setCancelled(true);
						parent.addChat("Removed Bot with property: InvalidSpawnPos");
					}
				 }
			  }
		   }
	    }
    }
public static List<EntityPlayer> getTabPlayerList() {
    final NetHandlerPlayClient var4 = AntiBot.mc.thePlayer.sendQueue;
    final List<EntityPlayer> list = new ArrayList<EntityPlayer>();
    final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy((Iterable)var4.func_175106_d());
    for (final Object o : players) {
        final NetworkPlayerInfo info = (NetworkPlayerInfo)o;
        if (info == null) {
            continue;
        }
        list.add(AntiBot.mc.theWorld.getPlayerEntityByName(info.func_178845_a().getName()));
    }
    return list;
	}
}
