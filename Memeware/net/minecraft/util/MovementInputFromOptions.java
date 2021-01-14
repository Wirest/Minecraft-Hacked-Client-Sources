package net.minecraft.util;

import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";

    public MovementInputFromOptions(GameSettings p_i1237_1_) {
        this.gameSettings = p_i1237_1_;
    }

    public void updatePlayerMoveState() {
        this.setStrafe(0.0F);
        this.setForward(0.0F);

        if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
            this.setForward(this.getForward() + 1);
        }

        if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
            this.setForward(this.getForward() - 1);
        }

        if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
            this.setStrafe(this.getStrafe() + 1);
        }

        if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
            this.setStrafe(this.getStrafe() - 1);
        }

        this.setJumping(this.gameSettings.keyBindJump.getIsKeyPressed());
        this.setSneaking(this.gameSettings.keyBindSneak.getIsKeyPressed());

        if (this.isSneaking()) {
            this.setStrafe((float) ((double) this.getStrafe() * 0.3D));
            this.setForward((float) ((double) this.getForward() * 0.3D));
        }
    }
}
