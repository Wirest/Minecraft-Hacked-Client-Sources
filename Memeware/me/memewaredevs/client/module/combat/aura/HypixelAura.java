
package me.memewaredevs.client.module.combat.aura;

import org.apache.commons.lang3.RandomUtils;

import me.memewaredevs.client.event.events.UpdateEvent;
import me.memewaredevs.client.module.combat.Aura;
import me.memewaredevs.client.util.combat.CombatUtil;
import me.memewaredevs.client.util.combat.RotationUtils;
import me.memewaredevs.client.util.misc.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import optifine.MathUtils;

public class HypixelAura {
	private static Timer timer = new Timer();
	private static boolean block, stopServerBlocking;

	public static void doUpdate(final Aura aura, final UpdateEvent e, final Minecraft mc) {
		boolean invisible = aura.getBool("Invisibles");
		double range = aura.getDouble("Range");
		boolean players = aura.getBool("Players");
		boolean monsters = aura.getBool("Monsters");
		boolean teams = false;
		final Entity entity = CombatUtil.getTarget(teams, monsters, players, range, invisible);
		final boolean autoBlock = aura.getBool("Auto Block");
		final long clickSpeed = aura.getDouble("Click Speed").longValue() <= 2
				? aura.getDouble("Click Speed").longValue()
				: ((long) MathUtils.getRandom(aura.getDouble("Click Speed").longValue() - 2,
						aura.getDouble("Click Speed").longValue()));
		boolean doBlock = mc.thePlayer.getHeldItem() != null
				&& mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock
				&& CombatUtil.canBlock(teams, false, true, range + 0.3, true);
		boolean canBlockSword = (mc.thePlayer.getHeldItem() != null) && ((mc.thePlayer.isBlocking())
				|| ((mc.thePlayer.isUsingItem())
						&& ((mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword)))
				|| (((mc.thePlayer.getHeldItem().getItem() instanceof ItemSword))
						&& (mc.gameSettings.keyBindUseItem.pressed))
				|| ((mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)));

		Aura.currentEntity = (EntityLivingBase) entity;

		if (entity == null && stopServerBlocking && autoBlock) {
			mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			Aura.isBlocking = false;
		}

		if (entity != null) {
			stopServerBlocking = true;
			float[] rotations = RotationUtils.getRotations((EntityLivingBase) entity);
			e.setYaw(rotations[0]);
			e.setPitch(rotations[1]);
			mc.thePlayer.rotationYawHead = rotations[0];
			mc.thePlayer.rotationPitchHead = rotations[1];
			mc.thePlayer.renderYawOffset = rotations[0];
			if (e.isPre()) {
				if (timer.delay(1000 / clickSpeed)) {
					if (autoBlock && Aura.isBlocking) {
						mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
								C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
						Aura.isBlocking = false;
					}
					mc.thePlayer.swingItem();
					mc.thePlayer.sendQueue
							.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
					timer.reset();
				}
			} else if (e.isPost()) {
				if (autoBlock && !Aura.isBlocking && canBlockSword) {
					mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(
							new BlockPos(RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE),
									RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE),
									RandomUtils.nextDouble(Double.MIN_VALUE, Double.MAX_VALUE)),
							255, Minecraft.getMinecraft().thePlayer.getHeldItem(), 0, 0, 0));
					Aura.isBlocking = true;
				}
			}
		}
	}
}
