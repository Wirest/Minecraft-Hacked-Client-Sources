/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.MathHelper
 */
package delta.utils;

import delta.utils.Wrapper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;

public class BoundingBox {
    private double blair$;
    private double counts$;
    private double scanning$;

    public BoundingBox(AxisAlignedBB axisAlignedBB) {
        this(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0);
    }

    public String toString() {
        return "Location[" + this._involve() + "];";
    }

    public BoundingBox(double d, double d2, double d3) {
        this.scanning$ = d;
        this.counts$ = d2;
        this.blair$ = d3;
    }

    public float _company(Entity entity) {
        return MathHelper.sqrt_float((float)((float)(this.scanning$ - entity.posX) * (float)(this.scanning$ - entity.posX) + (float)(this.counts$ - entity.posY) * (float)(this.counts$ - entity.posY) + (float)(this.blair$ - entity.posZ) * (float)(this.blair$ - entity.posZ)));
    }

    public double _talented() {
        return this.scanning$;
    }

    public BoundingBox(Entity entity) {
        this(entity.posX, entity.posY, entity.posZ);
    }

    public double _adelaide() {
        return this.counts$;
    }

    public double _produce() {
        return this.blair$;
    }

    public BoundingBox _exclude() {
        return new BoundingBox(this);
    }

    public Block _maria() {
        return Wrapper.mc.theWorld.getBlock((int)this.scanning$, (int)this.counts$, (int)this.blair$);
    }

    public BoundingBox(BoundingBox blockPos) {
        this(blockPos._talented(), blockPos._adelaide(), blockPos._produce());
    }

    public BoundingBox offset(double d, double d2, double d3) {
        this.scanning$ += d;
        this.counts$ += d2;
        this.blair$ += d3;
        return this;
    }

    public Object clone() throws CloneNotSupportedException {
        return this._exclude();
    }

    public AxisAlignedBB _wrist() {
        return AxisAlignedBB.getBoundingBox((double)this.scanning$, (double)this.counts$, (double)this.blair$, (double)(this.scanning$ + 1.0), (double)(this.counts$ + 1.0), (double)(this.blair$ + 1.0));
    }

    public BoundingBox _blind(AxisAlignedBB axisAlignedBB) {
        return this.offset(axisAlignedBB.minX + (axisAlignedBB.maxX - axisAlignedBB.minX) / 2.0, axisAlignedBB.minY + (axisAlignedBB.maxY - axisAlignedBB.minY) / 2.0, axisAlignedBB.minZ + (axisAlignedBB.maxZ - axisAlignedBB.minZ) / 2.0);
    }

    public BoundingBox _glucose(Entity entity) {
        return this.offset(entity.posX, entity.posY, entity.posZ);
    }

    public String _involve() {
        return this.scanning$ + ", " + this.counts$ + ", " + this.blair$;
    }

    public BoundingBox _talking(BoundingBox blockPos) {
        return this.offset(blockPos._talented(), blockPos._adelaide(), blockPos._produce());
    }
}

