package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.tools.RenderTools;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class ClickTeleport extends Module {

	private BlockPos hover;
	private boolean tp = false;

	public ClickTeleport() {
		super("ClickTeleport", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleports you forward when you right-click.");
	}

	@Override
	public void onUpdate(UpdateEvent event) {
		hover = mc.thePlayer.rayTrace(20, mc.timer.renderPartialTicks).getBlockPos();
		if(tp) {
			event.cancel();
			tp = false;
		}
		super.onUpdate(event);
	}

	@Override
	public void onRightClick() {
		MovingObjectPosition rayTrace = mc.thePlayer.rayTrace(20, mc.timer.renderPartialTicks);
		BlockPos blockPos = rayTrace.getBlockPos();
		if (blockPos == null) {
			return;
		}
		Utils.blinkToPosFromPos(new Vec3(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ), Utils.getVec3(rayTrace.getBlockPos().up()), 0.4);
		mc.thePlayer.setPosition(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
		tp = true;
		super.onRightClick();
	}

	@Override
	public void onRender() {
		if (hover == null) {
			return;
		}
		double x = hover.getX() + 0.5 - mc.getRenderManager().renderPosX;
		double y = hover.getY() + 1 - mc.getRenderManager().renderPosY;
		double z = hover.getZ() + 0.5 - mc.getRenderManager().renderPosZ;
		
		RenderTools.drawSolidEntityESP(x, y, z, 1 * 0.65, 0.1, 0.6f, 0.2f, 0.2f, 0.3f);
		RenderTools.drawOutlinedEntityESP(x, y, z, 1 * 0.65, 0.1, 1f, 1f, 1f, 1f);
		super.onRender();
	}

}
