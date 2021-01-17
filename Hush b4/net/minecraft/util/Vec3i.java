// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import com.google.common.base.Objects;

public class Vec3i implements Comparable<Vec3i>
{
    public static final Vec3i NULL_VECTOR;
    private final int x;
    private final int y;
    private final int z;
    
    static {
        NULL_VECTOR = new Vec3i(0, 0, 0);
    }
    
    public Vec3i(final int xIn, final int yIn, final int zIn) {
        this.x = xIn;
        this.y = yIn;
        this.z = zIn;
    }
    
    public Vec3i(final double xIn, final double yIn, final double zIn) {
        this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof Vec3i)) {
            return false;
        }
        final Vec3i vec3i = (Vec3i)p_equals_1_;
        return this.getX() == vec3i.getX() && this.getY() == vec3i.getY() && this.getZ() == vec3i.getZ();
    }
    
    @Override
    public int hashCode() {
        return (this.getY() + this.getZ() * 31) * 31 + this.getX();
    }
    
    @Override
    public int compareTo(final Vec3i p_compareTo_1_) {
        return (this.getY() == p_compareTo_1_.getY()) ? ((this.getZ() == p_compareTo_1_.getZ()) ? (this.getX() - p_compareTo_1_.getX()) : (this.getZ() - p_compareTo_1_.getZ())) : (this.getY() - p_compareTo_1_.getY());
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getZ() {
        return this.z;
    }
    
    public Vec3i crossProduct(final Vec3i vec) {
        return new Vec3i(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }
    
    public double distanceSq(final double toX, final double toY, final double toZ) {
        final double d0 = this.getX() - toX;
        final double d2 = this.getY() - toY;
        final double d3 = this.getZ() - toZ;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double distanceSqToCenter(final double xIn, final double yIn, final double zIn) {
        final double d0 = this.getX() + 0.5 - xIn;
        final double d2 = this.getY() + 0.5 - yIn;
        final double d3 = this.getZ() + 0.5 - zIn;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double distanceSq(final Vec3i to) {
        return this.distanceSq(to.getX(), to.getY(), to.getZ());
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).toString();
    }
}
