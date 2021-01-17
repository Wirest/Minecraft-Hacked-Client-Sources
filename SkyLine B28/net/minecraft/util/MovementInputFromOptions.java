package net.minecraft.util;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Mineman;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import skyline.specc.SkyLine;
import skyline.specc.mods.other.InvMove;

public class MovementInputFromOptions extends MovementInput
{
	private final GameSettings gameSettings;

	public MovementInputFromOptions(GameSettings p_i1237_1_)
	{
		this.gameSettings = p_i1237_1_;
	}

	public void updatePlayerMoveState()
	{
		if ((SkyLine.getManagers().getModuleManager().getModuleFromClass(InvMove.class).getState()) && (!(Mineman.getMinecraft().currentScreen instanceof GuiChat)))
		{
			this.moveStrafe = 0.0F;
			this.moveForward = 0.0F;
			if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
				this.moveForward += 1.0F;
			}
			if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
				this.moveForward -= 1.0F;
			}
			if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
				this.moveStrafe += 1.0F;
			}
			if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
				this.moveStrafe -= 1.0F;
			}
			this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
			this.sneak = this.gameSettings.keyBindSneak.pressed;
			if (this.sneak)
			{
				this.moveStrafe = ((float)(this.moveStrafe * 0.3D));
				this.moveForward = ((float)(this.moveForward * 0.3D));
			}
		}
		else
		{
			this.moveStrafe = 0.0F;
			this.moveForward = 0.0F;
			if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
				this.moveForward += 1.0F;
			}
			if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
				this.moveForward -= 1.0F;
			}
			if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
				this.moveStrafe += 1.0F;
			}
			if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
				this.moveStrafe -= 1.0F;
			}
			this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
			this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
			if (this.sneak)
			{
				this.moveStrafe = ((float)(this.moveStrafe * 0.3D));
				this.moveForward = ((float)(this.moveForward * 0.3D));
			}
		}
	}
}
