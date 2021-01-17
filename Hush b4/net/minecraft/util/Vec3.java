// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class Vec3
{
    public final double xCoord;
    public final double yCoord;
    public final double zCoord;
    
    public Vec3(double x, double y, double z) {
        if (x == -0.0) {
            x = 0.0;
        }
        if (y == -0.0) {
            y = 0.0;
        }
        if (z == -0.0) {
            z = 0.0;
        }
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }
    
    public Vec3(final Vec3i p_i46377_1_) {
        this(p_i46377_1_.getX(), p_i46377_1_.getY(), p_i46377_1_.getZ());
    }
    
    public Vec3 subtractReverse(final Vec3 vec) {
        return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }
    
    public Vec3 normalize() {
        final double d0 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        return (d0 < 1.0E-4) ? new Vec3(0.0, 0.0, 0.0) : new Vec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
    }
    
    public double dotProduct(final Vec3 vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }
    
    public Vec3 crossProduct(final Vec3 vec) {
        return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }
    
    public Vec3 subtract(final Vec3 vec) {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public Vec3 subtract(final double x, final double y, final double z) {
        return this.addVector(-x, -y, -z);
    }
    
    public Vec3 add(final Vec3 vec) {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }
    
    public Vec3 addVector(final double x, final double y, final double z) {
        return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }
    
    public double distanceTo(final Vec3 vec) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }
    
    public double squareDistanceTo(final Vec3 vec) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double lengthVector() {
        return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }
    
    public Vec3 getIntermediateWithXValue(final Vec3 vec, final double x) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        if (d0 * d0 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (x - this.xCoord) / d0;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
    }
    
    public Vec3 getIntermediateWithYValue(final Vec3 vec, final double y) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        if (d2 * d2 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (y - this.yCoord) / d2;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
    }
    
    public Vec3 getIntermediateWithZValue(final Vec3 vec, final double z) {
        final double d0 = vec.xCoord - this.xCoord;
        final double d2 = vec.yCoord - this.yCoord;
        final double d3 = vec.zCoord - this.zCoord;
        if (d3 * d3 < 1.0000000116860974E-7) {
            return null;
        }
        final double d4 = (z - this.zCoord) / d3;
        return (d4 >= 0.0 && d4 <= 1.0) ? new Vec3(this.xCoord + d0 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
    }
    
    @Override
    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }
    
    public Vec3 rotatePitch(final float pitch) {
        final float f = MathHelper.cos(pitch);
        final float f2 = MathHelper.sin(pitch);
        final double d0 = this.xCoord;
        final double d2 = this.yCoord * f + this.zCoord * f2;
        final double d3 = this.zCoord * f - this.yCoord * f2;
        return new Vec3(d0, d2, d3);
    }
    
    public Vec3 rotateYaw(final float yaw) {
        final float f = MathHelper.cos(yaw);
        final float f2 = MathHelper.sin(yaw);
        final double d0 = this.xCoord * f + this.zCoord * f2;
        final double d2 = this.yCoord;
        final double d3 = this.zCoord * f - this.xCoord * f2;
        return new Vec3(d0, d2, d3);
    }
}
