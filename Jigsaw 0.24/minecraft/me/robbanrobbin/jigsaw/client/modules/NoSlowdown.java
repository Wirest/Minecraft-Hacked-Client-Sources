package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.PreMotionEvent;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {

	public NoSlowdown() {
		super("NoSlowdown", Keyboard.KEY_NONE, Category.MOVEMENT, "When using items, you don't get slowed down.");
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
	public void onPreMotion(PreMotionEvent event) {
		if (!this.currentMode.equals("NCP")) {
			return;
		}
		if (mc.thePlayer.isBlocking() && mc.thePlayer.isMovingXZ()) {
			this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(
					C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.UP));
		}
		super.onPreMotion(event);
	}

	@Override
	public void onPostMotion() {
		if (!this.currentMode.equals("NCP")) {
			return;
		}
		if (mc.thePlayer.isBlocking() && mc.thePlayer.isMovingXZ()) {
			this.mc.getNetHandler()
					.addToSendQueue(new C08PacketPlayerBlockPlacement(this.mc.thePlayer.inventory.getCurrentItem()));
		}
		super.onPostMotion();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Normal", "NCP" };
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

}
