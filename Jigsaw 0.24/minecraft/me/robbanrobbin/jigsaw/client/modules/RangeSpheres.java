package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.entity.player.EntityPlayer;

public class RangeSpheres extends Module {

	public RangeSpheres() {
		super("RangeSpheres", Keyboard.KEY_NONE, Category.RENDER, "Enables you to see the range of other entities");
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
	public void onRender() {
		for (EntityPlayer en : mc.theWorld.playerEntities) {
			if (en.isEntityEqual(mc.thePlayer)) {
				continue;
			}
			int lines = 600 / Math.round(Math.max((mc.thePlayer.getDistanceToEntity(en)), 1));
			lines = Math.min(lines, 25);
			double xPos = (en.lastTickPosX + (en.posX - en.lastTickPosX) * mc.timer.renderPartialTicks)
					- mc.getRenderManager().renderPosX;
			double yPos = en.getEyeHeight()
					+ (en.lastTickPosY + (en.posY - en.lastTickPosY) * mc.timer.renderPartialTicks)
					- mc.getRenderManager().renderPosY;
			double zPos = (en.lastTickPosZ + (en.posZ - en.lastTickPosZ) * mc.timer.renderPartialTicks)
					- mc.getRenderManager().renderPosZ;
			float range = 3.5f;
			if (Jigsaw.getFriendsMananger().isFriend(en)) {
				RenderTools.drawSphere(0.0, 1, 1, 0.5, xPos, yPos, zPos, range, lines, lines, 2);
				continue;
			}
			if (mc.thePlayer.getDistanceToEntity(en) >= range) {
				if (mc.thePlayer.isOnSameTeam(en)) {
					RenderTools.drawSphere(0.5, 1, 0.5, 0.5, xPos, yPos, zPos, range, lines, lines, 2);
				} else {
					RenderTools.drawSphere(1, 0.8, 0.4, 0.5, xPos, yPos, zPos, range, lines, lines, 2);
				}

			} else {
				if (mc.thePlayer.isOnSameTeam(en)) {
					RenderTools.drawSphere(1, 0.4, 0.6, 0.7, xPos, yPos, zPos, range, lines, lines, 2);
				} else {
					RenderTools.drawSphere(1, 0.6, 0.4, 0.7, xPos, yPos, zPos, range, lines, lines, 2);
				}
			}

		}

		super.onRender();
	}
}
