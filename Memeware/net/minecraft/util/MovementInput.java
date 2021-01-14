package net.minecraft.util;

public abstract class MovementInput {

    private float moveStrafe;
    private float moveForward;
    private boolean jump;
    private boolean sneak;

    public abstract void updatePlayerMoveState();

    public float getStrafe() {
        return moveStrafe;
    }

    public void setStrafe(float moveStrafe) {
        this.moveStrafe = moveStrafe;
    }

    public float getForward() {
        return moveForward;
    }

    public void setForward(float moveForward) {
        this.moveForward = moveForward;
    }

    public boolean isJumping() {
        return jump;
    }

    public void setJumping(boolean jump) {
        this.jump = jump;
    }

    public boolean isSneaking() {
        return sneak;
    }

    public void setSneaking(boolean sneak) {
        this.sneak = sneak;
    }
}
