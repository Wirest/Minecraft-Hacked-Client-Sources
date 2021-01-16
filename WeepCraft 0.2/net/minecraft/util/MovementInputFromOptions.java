package net.minecraft.util;

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
        this.field_192832_b = 0.0F;

        if (this.gameSettings.keyBindForward.isKeyDown())
        {
            ++this.field_192832_b;
            this.forwardKeyDown = true;
        }
        else
        {
            this.forwardKeyDown = false;
        }

        if (this.gameSettings.keyBindBack.isKeyDown())
        {
            --this.field_192832_b;
            this.backKeyDown = true;
        }
        else
        {
            this.backKeyDown = false;
        }

        if (this.gameSettings.keyBindLeft.isKeyDown())
        {
            ++this.moveStrafe;
            this.leftKeyDown = true;
        }
        else
        {
            this.leftKeyDown = false;
        }

        if (this.gameSettings.keyBindRight.isKeyDown())
        {
            --this.moveStrafe;
            this.rightKeyDown = true;
        }
        else
        {
            this.rightKeyDown = false;
        }

        this.jump = this.gameSettings.keyBindJump.isKeyDown();
        this.sneak = this.gameSettings.keyBindSneak.isKeyDown();

        if (this.sneak)
        {
            this.moveStrafe = (float)((double)this.moveStrafe * 0.3D);
            this.field_192832_b = (float)((double)this.field_192832_b * 0.3D);
        }
    }
}
