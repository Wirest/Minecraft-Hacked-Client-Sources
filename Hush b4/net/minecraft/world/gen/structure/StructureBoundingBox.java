// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.gen.structure;

import net.minecraft.nbt.NBTTagIntArray;
import com.google.common.base.Objects;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3i;
import net.minecraft.util.EnumFacing;

public class StructureBoundingBox
{
    public int minX;
    public int minY;
    public int minZ;
    public int maxX;
    public int maxY;
    public int maxZ;
    
    public StructureBoundingBox() {
    }
    
    public StructureBoundingBox(final int[] coords) {
        if (coords.length == 6) {
            this.minX = coords[0];
            this.minY = coords[1];
            this.minZ = coords[2];
            this.maxX = coords[3];
            this.maxY = coords[4];
            this.maxZ = coords[5];
        }
    }
    
    public static StructureBoundingBox getNewBoundingBox() {
        return new StructureBoundingBox(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    
    public static StructureBoundingBox getComponentToAddBoundingBox(final int p_175897_0_, final int p_175897_1_, final int p_175897_2_, final int p_175897_3_, final int p_175897_4_, final int p_175897_5_, final int p_175897_6_, final int p_175897_7_, final int p_175897_8_, final EnumFacing p_175897_9_) {
        switch (p_175897_9_) {
            case NORTH: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ - p_175897_8_ + 1 + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_5_);
            }
            case SOUTH: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
            }
            case WEST: {
                return new StructureBoundingBox(p_175897_0_ - p_175897_8_ + 1 + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
            }
            case EAST: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_8_ - 1 + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
            }
            default: {
                return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
            }
        }
    }
    
    public static StructureBoundingBox func_175899_a(final int p_175899_0_, final int p_175899_1_, final int p_175899_2_, final int p_175899_3_, final int p_175899_4_, final int p_175899_5_) {
        return new StructureBoundingBox(Math.min(p_175899_0_, p_175899_3_), Math.min(p_175899_1_, p_175899_4_), Math.min(p_175899_2_, p_175899_5_), Math.max(p_175899_0_, p_175899_3_), Math.max(p_175899_1_, p_175899_4_), Math.max(p_175899_2_, p_175899_5_));
    }
    
    public StructureBoundingBox(final StructureBoundingBox structurebb) {
        this.minX = structurebb.minX;
        this.minY = structurebb.minY;
        this.minZ = structurebb.minZ;
        this.maxX = structurebb.maxX;
        this.maxY = structurebb.maxY;
        this.maxZ = structurebb.maxZ;
    }
    
    public StructureBoundingBox(final int xMin, final int yMin, final int zMin, final int xMax, final int yMax, final int zMax) {
        this.minX = xMin;
        this.minY = yMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxY = yMax;
        this.maxZ = zMax;
    }
    
    public StructureBoundingBox(final Vec3i vec1, final Vec3i vec2) {
        this.minX = Math.min(vec1.getX(), vec2.getX());
        this.minY = Math.min(vec1.getY(), vec2.getY());
        this.minZ = Math.min(vec1.getZ(), vec2.getZ());
        this.maxX = Math.max(vec1.getX(), vec2.getX());
        this.maxY = Math.max(vec1.getY(), vec2.getY());
        this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
    }
    
    public StructureBoundingBox(final int xMin, final int zMin, final int xMax, final int zMax) {
        this.minX = xMin;
        this.minZ = zMin;
        this.maxX = xMax;
        this.maxZ = zMax;
        this.minY = 1;
        this.maxY = 512;
    }
    
    public boolean intersectsWith(final StructureBoundingBox structurebb) {
        return this.maxX >= structurebb.minX && this.minX <= structurebb.maxX && this.maxZ >= structurebb.minZ && this.minZ <= structurebb.maxZ && this.maxY >= structurebb.minY && this.minY <= structurebb.maxY;
    }
    
    public boolean intersectsWith(final int minXIn, final int minZIn, final int maxXIn, final int maxZIn) {
        return this.maxX >= minXIn && this.minX <= maxXIn && this.maxZ >= minZIn && this.minZ <= maxZIn;
    }
    
    public void expandTo(final StructureBoundingBox sbb) {
        this.minX = Math.min(this.minX, sbb.minX);
        this.minY = Math.min(this.minY, sbb.minY);
        this.minZ = Math.min(this.minZ, sbb.minZ);
        this.maxX = Math.max(this.maxX, sbb.maxX);
        this.maxY = Math.max(this.maxY, sbb.maxY);
        this.maxZ = Math.max(this.maxZ, sbb.maxZ);
    }
    
    public void offset(final int x, final int y, final int z) {
        this.minX += x;
        this.minY += y;
        this.minZ += z;
        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
    }
    
    public boolean isVecInside(final Vec3i vec) {
        return vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ && vec.getY() >= this.minY && vec.getY() <= this.maxY;
    }
    
    public Vec3i func_175896_b() {
        return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
    }
    
    public int getXSize() {
        return this.maxX - this.minX + 1;
    }
    
    public int getYSize() {
        return this.maxY - this.minY + 1;
    }
    
    public int getZSize() {
        return this.maxZ - this.minZ + 1;
    }
    
    public Vec3i getCenter() {
        return new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
    }
    
    public NBTTagIntArray toNBTTagIntArray() {
        return new NBTTagIntArray(new int[] { this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ });
    }
}
