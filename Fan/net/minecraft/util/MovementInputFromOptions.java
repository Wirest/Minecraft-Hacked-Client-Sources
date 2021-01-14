package net.minecraft.util;

import cedo.Fan;
import cedo.modules.movement.Scaffold;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput
{
    private final GameSettings gameSettings;

    public MovementInputFromOptions(GameSettings gameSettingsIn)
    {
        this.gameSettings = gameSettingsIn;
    }

    public void updatePlayerMoveState()
    {
        this.moveStrafe = 0.0F;
        this.moveForward = 0.0F;

        if (this.gameSettings.keyBindForward.isKeyDown())
        {
            ++this.moveForward;
        }

        if (this.gameSettings.keyBindBack.isKeyDown())
        {
            --this.moveForward;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown())
        {
            ++this.moveStrafe;
        }

        if (this.gameSettings.keyBindRight.isKeyDown())
        {
            --this.moveStrafe;
        }

        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();

        if (this.sneak)
        {
        	boolean scaffoldDownward = Fan.getModule(Scaffold.class).isToggled();
            this.moveStrafe = (float)((double)this.moveStrafe * (scaffoldDownward ? 0.7D : 0.3D));
            this.moveForward = (float)((double)this.moveForward * (scaffoldDownward ? 0.7D : 0.3D));
            //this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            //this.moveForward = (float)((double)this.moveForward * 0.3D);
        }
    }
}
