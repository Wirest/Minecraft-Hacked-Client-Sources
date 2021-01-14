
package me.memewaredevs.client.module.combat;

import java.util.function.Consumer;

import me.hippo.api.lwjeb.annotation.Handler;
import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.Module;
import me.memewaredevs.client.module.combat.aura.HypixelAura;
import me.memewaredevs.client.module.combat.aura.MultiAura;
import me.memewaredevs.client.module.combat.aura.SingleAura;
import me.memewaredevs.client.util.packet.PacketUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class Aura extends Module {

	public static boolean isBlocking;
	public static EntityLivingBase currentEntity;

	public Aura() {
		super("Kill Aura", 19, Module.Category.COMBAT);
		addModes("Single", "Multi", "Hypixel");
		addInt("Click Speed", 12.0, 1.0, 20.0);
		addInt("Block Chance", 60.0, 1.0, 100.0);
		addDouble("Range", 6.0, 1.0, 8.0);
		addBoolean("Auto Block", true);
		addBoolean("Keep Sprint", true);
		addBoolean("Players", true);
		addBoolean("Animals", true);
		addBoolean("Monsters", true);
		addBoolean("Invisibles", true);
		addBoolean("Teams", true);
	}

	@Handler
	public Consumer<UpdateEvent> onUpdate = (event) -> {
		if (isMode("Single")) {
			SingleAura.doUpdate(this, event, mc);
		}
		if (isMode("Multi")) {
			MultiAura.doUpdate(this, event, mc);
		}
		if (isMode("Hypixel")) {
			HypixelAura.doUpdate(this, event, mc);
		}
	};

	@Override
	public void onDisable() {
		isBlocking = false;
		currentEntity = null;
		if (SingleAura.unblock) {
			PacketUtil.sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
					new BlockPos(0.221, 0.221, 0.213), EnumFacing.UP));
		}
	}
}
