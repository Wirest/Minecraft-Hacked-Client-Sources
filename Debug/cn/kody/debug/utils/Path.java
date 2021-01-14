package cn.kody.debug.utils;

import net.minecraft.util.*;

public class Path
{
    private double x;
    private double y;
    private double z;
    
    public Path(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public double getX() {
        return this.x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public double getZ() {
        return this.z;
    }
    
    public Path addVector(final double n, final double n2, final double n3) {
        return new Path(this.x + n, this.y + n2, this.z + n3);
    }
    
    public Path floor() {
        return new Path(Math.floor(this.x), Math.floor(this.y), Math.floor(this.z));
    }
    
    public double squareDistanceTo(final Path class148) {
        return Math.pow(class148.x - this.x, 2.0) + Math.pow(class148.y - this.y, 2.0) + Math.pow(class148.z - this.z, 2.0);
    }
    
    public Path add(final Path class148) {
        return this.addVector(class148.getX(), class148.getY(), class148.getZ());
    }
    
    public Vec3 mc() {
        return new Vec3(this.x, this.y, this.z);
    }
    
    @Override
    public String toString() {
        return "[" + this.x + ";" + this.y + ";" + this.z + "]";
    }
}
