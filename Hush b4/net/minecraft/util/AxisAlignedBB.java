// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class AxisAlignedBB
{
    public final double minX;
    public final double minY;
    public final double minZ;
    public final double maxX;
    public final double maxY;
    public final double maxZ;
    
    public AxisAlignedBB(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        this.minX = Math.min(x1, x2);
        this.minY = Math.min(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxX = Math.max(x1, x2);
        this.maxY = Math.max(y1, y2);
        this.maxZ = Math.max(z1, z2);
    }
    
    public AxisAlignedBB(final BlockPos pos1, final BlockPos pos2) {
        this.minX = pos1.getX();
        this.minY = pos1.getY();
        this.minZ = pos1.getZ();
        this.maxX = pos2.getX();
        this.maxY = pos2.getY();
        this.maxZ = pos2.getZ();
    }
    
    public AxisAlignedBB addCoord(final double x, final double y, final double z) {
        double d0 = this.minX;
        double d2 = this.minY;
        double d3 = this.minZ;
        double d4 = this.maxX;
        double d5 = this.maxY;
        double d6 = this.maxZ;
        if (x < 0.0) {
            d0 += x;
        }
        else if (x > 0.0) {
            d4 += x;
        }
        if (y < 0.0) {
            d2 += y;
        }
        else if (y > 0.0) {
            d5 += y;
        }
        if (z < 0.0) {
            d3 += z;
        }
        else if (z > 0.0) {
            d6 += z;
        }
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB expand(final double x, final double y, final double z) {
        final double d0 = this.minX - x;
        final double d2 = this.minY - y;
        final double d3 = this.minZ - z;
        final double d4 = this.maxX + x;
        final double d5 = this.maxY + y;
        final double d6 = this.maxZ + z;
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB union(final AxisAlignedBB other) {
        final double d0 = Math.min(this.minX, other.minX);
        final double d2 = Math.min(this.minY, other.minY);
        final double d3 = Math.min(this.minZ, other.minZ);
        final double d4 = Math.max(this.maxX, other.maxX);
        final double d5 = Math.max(this.maxY, other.maxY);
        final double d6 = Math.max(this.maxZ, other.maxZ);
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public static AxisAlignedBB fromBounds(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        final double d0 = Math.min(x1, x2);
        final double d2 = Math.min(y1, y2);
        final double d3 = Math.min(z1, z2);
        final double d4 = Math.max(x1, x2);
        final double d5 = Math.max(y1, y2);
        final double d6 = Math.max(z1, z2);
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public AxisAlignedBB offset(final double x, final double y, final double z) {
        return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
    }
    
    public double calculateXOffset(final AxisAlignedBB other, double offsetX) {
        if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetX > 0.0 && other.maxX <= this.minX) {
                final double d1 = this.minX - other.maxX;
                if (d1 < offsetX) {
                    offsetX = d1;
                }
            }
            else if (offsetX < 0.0 && other.minX >= this.maxX) {
                final double d2 = this.maxX - other.minX;
                if (d2 > offsetX) {
                    offsetX = d2;
                }
            }
            return offsetX;
        }
        return offsetX;
    }
    
    public double calculateYOffset(final AxisAlignedBB other, double offsetY) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
            if (offsetY > 0.0 && other.maxY <= this.minY) {
                final double d1 = this.minY - other.maxY;
                if (d1 < offsetY) {
                    offsetY = d1;
                }
            }
            else if (offsetY < 0.0 && other.minY >= this.maxY) {
                final double d2 = this.maxY - other.minY;
                if (d2 > offsetY) {
                    offsetY = d2;
                }
            }
            return offsetY;
        }
        return offsetY;
    }
    
    public double calculateZOffset(final AxisAlignedBB other, double offsetZ) {
        if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
            if (offsetZ > 0.0 && other.maxZ <= this.minZ) {
                final double d1 = this.minZ - other.maxZ;
                if (d1 < offsetZ) {
                    offsetZ = d1;
                }
            }
            else if (offsetZ < 0.0 && other.minZ >= this.maxZ) {
                final double d2 = this.maxZ - other.minZ;
                if (d2 > offsetZ) {
                    offsetZ = d2;
                }
            }
            return offsetZ;
        }
        return offsetZ;
    }
    
    public boolean intersectsWith(final AxisAlignedBB other) {
        return other.maxX > this.minX && other.minX < this.maxX && (other.maxY > this.minY && other.minY < this.maxY) && (other.maxZ > this.minZ && other.minZ < this.maxZ);
    }
    
    public boolean isVecInside(final Vec3 vec) {
        return vec.xCoord > this.minX && vec.xCoord < this.maxX && (vec.yCoord > this.minY && vec.yCoord < this.maxY) && (vec.zCoord > this.minZ && vec.zCoord < this.maxZ);
    }
    
    public double getAverageEdgeLength() {
        final double d0 = this.maxX - this.minX;
        final double d2 = this.maxY - this.minY;
        final double d3 = this.maxZ - this.minZ;
        return (d0 + d2 + d3) / 3.0;
    }
    
    public AxisAlignedBB contract(final double x, final double y, final double z) {
        final double d0 = this.minX + x;
        final double d2 = this.minY + y;
        final double d3 = this.minZ + z;
        final double d4 = this.maxX - x;
        final double d5 = this.maxY - y;
        final double d6 = this.maxZ - z;
        return new AxisAlignedBB(d0, d2, d3, d4, d5, d6);
    }
    
    public MovingObjectPosition calculateIntercept(final Vec3 vecA, final Vec3 vecB) {
        Vec3 vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
        Vec3 vec4 = vecA.getIntermediateWithXValue(vecB, this.maxX);
        Vec3 vec5 = vecA.getIntermediateWithYValue(vecB, this.minY);
        Vec3 vec6 = vecA.getIntermediateWithYValue(vecB, this.maxY);
        Vec3 vec7 = vecA.getIntermediateWithZValue(vecB, this.minZ);
        Vec3 vec8 = vecA.getIntermediateWithZValue(vecB, this.maxZ);
        if (!this.isVecInYZ(vec3)) {
            vec3 = null;
        }
        if (!this.isVecInYZ(vec4)) {
            vec4 = null;
        }
        if (!this.isVecInXZ(vec5)) {
            vec5 = null;
        }
        if (!this.isVecInXZ(vec6)) {
            vec6 = null;
        }
        if (!this.isVecInXY(vec7)) {
            vec7 = null;
        }
        if (!this.isVecInXY(vec8)) {
            vec8 = null;
        }
        Vec3 vec9 = null;
        if (vec3 != null) {
            vec9 = vec3;
        }
        if (vec4 != null && (vec9 == null || vecA.squareDistanceTo(vec4) < vecA.squareDistanceTo(vec9))) {
            vec9 = vec4;
        }
        if (vec5 != null && (vec9 == null || vecA.squareDistanceTo(vec5) < vecA.squareDistanceTo(vec9))) {
            vec9 = vec5;
        }
        if (vec6 != null && (vec9 == null || vecA.squareDistanceTo(vec6) < vecA.squareDistanceTo(vec9))) {
            vec9 = vec6;
        }
        if (vec7 != null && (vec9 == null || vecA.squareDistanceTo(vec7) < vecA.squareDistanceTo(vec9))) {
            vec9 = vec7;
        }
        if (vec8 != null && (vec9 == null || vecA.squareDistanceTo(vec8) < vecA.squareDistanceTo(vec9))) {
            vec9 = vec8;
        }
        if (vec9 == null) {
            return null;
        }
        EnumFacing enumfacing = null;
        if (vec9 == vec3) {
            enumfacing = EnumFacing.WEST;
        }
        else if (vec9 == vec4) {
            enumfacing = EnumFacing.EAST;
        }
        else if (vec9 == vec5) {
            enumfacing = EnumFacing.DOWN;
        }
        else if (vec9 == vec6) {
            enumfacing = EnumFacing.UP;
        }
        else if (vec9 == vec7) {
            enumfacing = EnumFacing.NORTH;
        }
        else {
            enumfacing = EnumFacing.SOUTH;
        }
        return new MovingObjectPosition(vec9, enumfacing);
    }
    
    private boolean isVecInYZ(final Vec3 vec) {
        return vec != null && (vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
    }
    
    private boolean isVecInXZ(final Vec3 vec) {
        return vec != null && (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ);
    }
    
    private boolean isVecInXY(final Vec3 vec) {
        return vec != null && (vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY);
    }
    
    @Override
    public String toString() {
        return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
    }
    
    public boolean func_181656_b() {
        return Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ);
    }
}
