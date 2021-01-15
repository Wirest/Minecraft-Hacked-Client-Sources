package net.minecraft.util;

import me.aristhena.client.module.Module;
import me.aristhena.client.module.modules.misc.ScreenWalk;
import me.aristhena.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class MovementInputFromOptions
  extends MovementInput
{
  private final GameSettings gameSettings;
  private static final String __OBFID = "CL_00000937";
  
  public MovementInputFromOptions(GameSettings p_i1237_1_)
  {
    this.gameSettings = p_i1237_1_;
  }
  
  public void updatePlayerMoveState()
  {
    if ((new ScreenWalk().getInstance().isEnabled()) && (!(ClientUtils.mc().currentScreen instanceof GuiChat)))
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
      this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
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
      if (this.gameSettings.keyBindForward.isKeyDown()) {
        this.moveForward += 1.0F;
      }
      if (this.gameSettings.keyBindBack.isKeyDown()) {
        this.moveForward -= 1.0F;
      }
      if (this.gameSettings.keyBindLeft.isKeyDown()) {
        this.moveStrafe += 1.0F;
      }
      if (this.gameSettings.keyBindRight.isKeyDown()) {
        this.moveStrafe -= 1.0F;
      }
      this.jump = this.gameSettings.keyBindJump.isKeyDown();
      this.sneak = this.gameSettings.keyBindSneak.isKeyDown();
      if (this.sneak)
      {
        this.moveStrafe = ((float)(this.moveStrafe * 0.3D));
        this.moveForward = ((float)(this.moveForward * 0.3D));
      }
    }
  }
}
