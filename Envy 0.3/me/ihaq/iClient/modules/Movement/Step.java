package me.ihaq.iClient.modules.Movement;

import org.lwjgl.input.Keyboard;

import me.ihaq.iClient.event.EventTarget;
import me.ihaq.iClient.event.events.EventEntityStep;
import me.ihaq.iClient.event.events.EventTick;
import me.ihaq.iClient.modules.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.Timer;

public class Step extends Module {

	private boolean resetNextTick;
	private double preY;

	public Step() {
		super("Step", Keyboard.KEY_NONE, Category.MOVEMENT, "");
	}

	@EventTarget
	public void onStep(EventEntityStep event) {

		if (!this.isToggled()) {
			return;
		}

		if (event.getEntity() != this.mc.thePlayer) {
			return;
		}
		this.preY = this.mc.thePlayer.posY;
		if (canStep()) {
			event.setStepHeight(1.0F);
		}
		double offset = this.mc.thePlayer.boundingBox.minY - this.preY;
		if (offset > 0.6D) {
			double[] offsets = { 0.42D, (offset < 1.0D) && (offset > 0.8D) ? 0.73D : 0.75D, 1.0D, 1.16D, 1.23D, 1.2D };
			for (int i = 0; i < (offset > 1.0D ? offsets.length : 2); i++) {
				this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
						this.mc.thePlayer.posX, this.mc.thePlayer.posY + offsets[i], this.mc.thePlayer.posZ, false));
			}
			Timer.timerSpeed = offset > 1.0D ? 0.15F : 0.37F;
			this.resetNextTick = true;
		}
	}

	@EventTarget
	public void onTick(EventTick event) {
		if (!this.isToggled()) {
			return;
		}

		if (this.resetNextTick) {
			Timer.timerSpeed = 1.0F;
			this.resetNextTick = false;
		}

	}

private boolean canStep() {
        return (this.mc.thePlayer.isCollidedVertically) && (this.mc.thePlayer.onGround)
                && (this.mc.thePlayer.motionY < 0.0D) && (!this.mc.thePlayer.movementInput.jump);
    }

}
