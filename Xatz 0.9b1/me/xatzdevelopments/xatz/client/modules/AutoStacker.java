package me.xatzdevelopments.xatz.client.modules;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.util.Vec3;

public class AutoStacker extends Module {

	public AutoStacker() {
		super("AutoStacker", Keyboard.KEY_NONE, Category.FUN,
				"Automatically stacks" + " close players in the Mineplex lobby.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		ArrayList<EntityLivingBase> ens = Utils.getClosestEntities(7f);
		for (EntityLivingBase en : ens) {
			if (en.isRiding()) {
				continue;
			}
			if (!(en instanceof EntityPlayer)) {
				continue;
			}
			Vec3 vec3 = new Vec3(en.posX, en.posY, en.posZ);
			sendPacket(new C02PacketUseEntity(en, vec3));
			if (this.currentMode.equals("Throw")) {
				mc.thePlayer.swingItem();
				sendPacket(new C02PacketUseEntity(en, Action.ATTACK));
			}

		}
		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Glue", "Throw" };
	}

	public String getModeName() {
		return "Mode: ";
	}
}
