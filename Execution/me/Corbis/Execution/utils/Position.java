package me.Corbis.Execution.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class Position {

    double x, y, z;

    public Position(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getDistanceToPosition(Position entityIn)
    {
        float f = (float)(this.getX() - entityIn.getX());
        float f1 = (float)(this.getY() - entityIn.getY());
        float f2 = (float)(this.getZ() - entityIn.getZ());
        return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
    }
}
