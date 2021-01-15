package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C02PacketUseEntity.Action;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Criticals extends Module {

	int slotBefore;
	int bestSlot;
	float eating;
	public static boolean disable;

	public Criticals() {
		super("Criticals", Keyboard.KEY_NONE, Category.COMBAT, "Tries to critical every hit.");
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

		super.onUpdate();
	}

	@Override
	public void onLeftClick() {

	}

	@Override
	public void onPacketSent(AbstractPacket packet) {
		// if(packet instanceof C03PacketPlayer
		// && !mc.thePlayer.isMovingXZ() && !((C03PacketPlayer)packet).rotating)
		// {
		// packet.cancel();
		// Jigsaw.chatMessage("Cancel(r)");
		// }
		if (packet.crit || disable) {
			return;
		}
		if (packet instanceof C02PacketUseEntity && ((C02PacketUseEntity) packet).getAction() == Action.ATTACK) {
			critical(((C02PacketUseEntity) packet).getEntityFromWorld(mc.theWorld));
		}
		
		super.onPacketSent(packet);
	}

	public void critical(Entity en) {
		if (!mc.thePlayer.onGround) {
			return;
		}
		if(en instanceof EntityLivingBase) {
			if(((EntityLivingBase) en).hurtTime != 0) {
				return;
			}
		}
		// Jigsaw.chatMessage("Crit");
		// Jigsaw.chatMessage("Crit");
		// Jigsaw.chatMessage(enn.hurtTime);
		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY;
		double z = mc.thePlayer.posZ;
		sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625D, z, true).setCrit(true));
		sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false).setCrit(true));
		sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5D, z, false).setCrit(true));
		sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false).setCrit(true));
	}

	public static void crit(double xx, double yy, double zz) {
		// if(true) {
		// return;
		// }
		if (!Jigsaw.getModuleByName("Criticals").isToggled()) {
			return;
		}
		if (!mc.thePlayer.onGround) {
			return;
		}
		double x = xx;
		double y = yy;
		double z = zz;
		// Jigsaw.chatMessage("crit");
		mc.getNetHandler().getNetworkManager().sendPacketFinal(
				new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.0625D, z, true).setCrit(true));
		mc.getNetHandler().getNetworkManager()
				.sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false).setCrit(true));
		mc.getNetHandler().getNetworkManager().sendPacketFinal(
				new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.1E-5D, z, false).setCrit(true));
		mc.getNetHandler().getNetworkManager()
				.sendPacketFinal(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false).setCrit(true));
	}

	public static void crit() {
		// if(true) {
		// return;
		// }
		// crit(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
	}

}
