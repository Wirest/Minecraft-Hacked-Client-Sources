package splash.utilities.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import splash.Splash;
import splash.client.modules.combat.AntiBot;

/**
 * Author: Ice Created: 00:16, 14-Jun-20 Project: Client
 */
public class PlayerUtils {

	public static Minecraft mc = Minecraft.getMinecraft();

	public static void damageHypixel() {
		if (mc.thePlayer.onGround) {
			final double offset = 0.4122222218322211111111F;
			final NetHandlerPlayClient netHandler = mc.getNetHandler();
			final EntityPlayerSP player = mc.thePlayer;
			final double x = player.posX;
			final double y = player.posY;
			final double z = player.posZ;
			for (int i = 0; i < 9; i++) {
				netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
				netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.000002737272, z, false));
				netHandler.addToSendQueue(new C03PacketPlayer(false));
			}
			netHandler.addToSendQueue(new C03PacketPlayer(true));
		}
	}

	public static double getJumpBoostModifier(double baseJumpHeight) {
		if (mc.thePlayer.isPotionActive(Potion.jump)) {
			int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
			baseJumpHeight += (float) (amplifier + 1) * 0.1F;
		}

		return baseJumpHeight;
	}

	public static int getHealthColor(final EntityLivingBase player) {
		final float f = player.getHealth();
		final float f2 = player.getMaxHealth();
		final float f3 = Math.max(0.0f, Math.min(f, f2) / f2);
		return Color.HSBtoRGB(f3 / 3.0f, 1.0f, 0.75f) | 0xFF000000;
	}

	public static boolean canBlock(boolean mobs, boolean players, double range, boolean invis) {
		for (Object obj : mc.theWorld.loadedEntityList) {
			if (obj instanceof EntityLivingBase) {
				EntityLivingBase o = (EntityLivingBase) obj;
				if (o.getDistanceToEntity((Entity) mc.thePlayer) > range)
					continue;
				if (o.isInvisible() && !invis)
					continue;
				if (Splash.INSTANCE.getFriendManager().isFriend(o.getName()))
					continue;
				if (o.isDead)
					continue;
				if (o == mc.thePlayer)
					continue;
				if (AntiBot.bots.contains(o))
					continue;
				if (!(o instanceof net.minecraft.entity.player.EntityPlayer) && players)
					continue;
				return true;
			}
		}
		return false;
	}

	public static List<EntityLivingBase> getTargets(boolean teams, final int maxTargets, final boolean mobs,
			final boolean players, final double range, final boolean invis) {
		final ArrayList<EntityLivingBase> list = new ArrayList<>();
		for (final Object obj : mc.theWorld.loadedEntityList) {
			EntityLivingBase o;
			if (!(obj instanceof EntityLivingBase)
					|| (o = (EntityLivingBase) obj).getDistanceToEntity(
							mc.thePlayer) > (((EntityLivingBase) obj).posY > mc.thePlayer.posY + 1 ? range + 2 : range)
					|| o.isInvisible() && !invis || o.isDead || o.getHealth() == 0.0f || o == mc.thePlayer
					|| AntiBot.bots.contains(o))
				continue;
			if (!(obj instanceof EntityPlayer))
				continue;
			if (list.size() >= maxTargets)
				continue;
			list.add(o);
		}
		return list;
	}

	public static EntityLivingBase getTarget(boolean teams, final boolean mobs, final boolean players,
			final double range, final boolean invis) {
		for (final Object obj : mc.theWorld.loadedEntityList) {
			EntityLivingBase o;
			if (!(obj instanceof EntityLivingBase)
					|| (o = (EntityLivingBase) obj).getDistanceToEntity(mc.thePlayer) > range
					|| o.isInvisible() && !invis || o.isDead || o.getHealth() == 0.0f || o == mc.thePlayer
					|| AntiBot.bots.contains(o))
				continue;
			if (!(obj instanceof EntityPlayer))
				continue;
			return o;
		}
		return null;
	}
}
