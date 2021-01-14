package de.iotacb.client.utilities.player;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class EntityUtil {

	private static final Minecraft MC = Minecraft.getMinecraft();

	public final Block getBlockBelowEntity(Entity entity, double offset) {
		final Vec3 below = entity.getPositionVector();
		return MC.theWorld.getBlockState(new BlockPos(below).add(0, -offset, 0)).getBlock();
	}

	public final Block getBlockBelowPlayer(double offset) {
		return getBlockBelowEntity(MC.thePlayer, offset);
	}

	public final Block getBlockBelowEntity(Entity entity) {
		return getBlockBelowEntity(entity, 1);
	}

	public final Block getBlockBelowPlayer() {
		return getBlockBelowEntity(MC.thePlayer);
	}

	public final boolean isTeamMate(EntityLivingBase entity) {
		if (!(entity instanceof EntityPlayer))
			return false;
		if (MC.thePlayer.getTeam() != null && entity.getTeam() != null) {
			if (MC.thePlayer.isOnSameTeam(entity)) {
				return true;
			}
		}

		if (MC.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
			final String playerName = MC.thePlayer.getDisplayName().getFormattedText().replace("§r", "");
			final String entityName = entity.getDisplayName().getFormattedText().replace("§r", "");
			if (playerName.isEmpty() || entityName.isEmpty())
				return false;
			return playerName.charAt(1) == entityName.charAt(1);
		}

		return false;
	}
	
}
