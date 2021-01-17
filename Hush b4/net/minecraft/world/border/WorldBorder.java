// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.border;

import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.util.BlockPos;
import com.google.common.collect.Lists;
import java.util.List;

public class WorldBorder
{
    private final List<IBorderListener> listeners;
    private double centerX;
    private double centerZ;
    private double startDiameter;
    private double endDiameter;
    private long endTime;
    private long startTime;
    private int worldSize;
    private double damageAmount;
    private double damageBuffer;
    private int warningTime;
    private int warningDistance;
    
    public WorldBorder() {
        this.listeners = (List<IBorderListener>)Lists.newArrayList();
        this.centerX = 0.0;
        this.centerZ = 0.0;
        this.startDiameter = 6.0E7;
        this.endDiameter = this.startDiameter;
        this.worldSize = 29999984;
        this.damageAmount = 0.2;
        this.damageBuffer = 5.0;
        this.warningTime = 15;
        this.warningDistance = 5;
    }
    
    public boolean contains(final BlockPos pos) {
        return pos.getX() + 1 > this.minX() && pos.getX() < this.maxX() && pos.getZ() + 1 > this.minZ() && pos.getZ() < this.maxZ();
    }
    
    public boolean contains(final ChunkCoordIntPair range) {
        return range.getXEnd() > this.minX() && range.getXStart() < this.maxX() && range.getZEnd() > this.minZ() && range.getZStart() < this.maxZ();
    }
    
    public boolean contains(final AxisAlignedBB bb) {
        return bb.maxX > this.minX() && bb.minX < this.maxX() && bb.maxZ > this.minZ() && bb.minZ < this.maxZ();
    }
    
    public double getClosestDistance(final Entity entityIn) {
        return this.getClosestDistance(entityIn.posX, entityIn.posZ);
    }
    
    public double getClosestDistance(final double x, final double z) {
        final double d0 = z - this.minZ();
        final double d2 = this.maxZ() - z;
        final double d3 = x - this.minX();
        final double d4 = this.maxX() - x;
        double d5 = Math.min(d3, d4);
        d5 = Math.min(d5, d0);
        return Math.min(d5, d2);
    }
    
    public EnumBorderStatus getStatus() {
        return (this.endDiameter < this.startDiameter) ? EnumBorderStatus.SHRINKING : ((this.endDiameter > this.startDiameter) ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
    }
    
    public double minX() {
        double d0 = this.getCenterX() - this.getDiameter() / 2.0;
        if (d0 < -this.worldSize) {
            d0 = -this.worldSize;
        }
        return d0;
    }
    
    public double minZ() {
        double d0 = this.getCenterZ() - this.getDiameter() / 2.0;
        if (d0 < -this.worldSize) {
            d0 = -this.worldSize;
        }
        return d0;
    }
    
    public double maxX() {
        double d0 = this.getCenterX() + this.getDiameter() / 2.0;
        if (d0 > this.worldSize) {
            d0 = this.worldSize;
        }
        return d0;
    }
    
    public double maxZ() {
        double d0 = this.getCenterZ() + this.getDiameter() / 2.0;
        if (d0 > this.worldSize) {
            d0 = this.worldSize;
        }
        return d0;
    }
    
    public double getCenterX() {
        return this.centerX;
    }
    
    public double getCenterZ() {
        return this.centerZ;
    }
    
    public void setCenter(final double x, final double z) {
        this.centerX = x;
        this.centerZ = z;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onCenterChanged(this, x, z);
        }
    }
    
    public double getDiameter() {
        if (this.getStatus() != EnumBorderStatus.STATIONARY) {
            final double d0 = (System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime);
            if (d0 < 1.0) {
                return this.startDiameter + (this.endDiameter - this.startDiameter) * d0;
            }
            this.setTransition(this.endDiameter);
        }
        return this.startDiameter;
    }
    
    public long getTimeUntilTarget() {
        return (this.getStatus() != EnumBorderStatus.STATIONARY) ? (this.endTime - System.currentTimeMillis()) : 0L;
    }
    
    public double getTargetSize() {
        return this.endDiameter;
    }
    
    public void setTransition(final double newSize) {
        this.startDiameter = newSize;
        this.endDiameter = newSize;
        this.endTime = System.currentTimeMillis();
        this.startTime = this.endTime;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onSizeChanged(this, newSize);
        }
    }
    
    public void setTransition(final double oldSize, final double newSize, final long time) {
        this.startDiameter = oldSize;
        this.endDiameter = newSize;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + time;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onTransitionStarted(this, oldSize, newSize, time);
        }
    }
    
    protected List<IBorderListener> getListeners() {
        return (List<IBorderListener>)Lists.newArrayList((Iterable<?>)this.listeners);
    }
    
    public void addListener(final IBorderListener listener) {
        this.listeners.add(listener);
    }
    
    public void setSize(final int size) {
        this.worldSize = size;
    }
    
    public int getSize() {
        return this.worldSize;
    }
    
    public double getDamageBuffer() {
        return this.damageBuffer;
    }
    
    public void setDamageBuffer(final double bufferSize) {
        this.damageBuffer = bufferSize;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onDamageBufferChanged(this, bufferSize);
        }
    }
    
    public double getDamageAmount() {
        return this.damageAmount;
    }
    
    public void setDamageAmount(final double newAmount) {
        this.damageAmount = newAmount;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onDamageAmountChanged(this, newAmount);
        }
    }
    
    public double getResizeSpeed() {
        return (this.endTime == this.startTime) ? 0.0 : (Math.abs(this.startDiameter - this.endDiameter) / (this.endTime - this.startTime));
    }
    
    public int getWarningTime() {
        return this.warningTime;
    }
    
    public void setWarningTime(final int warningTime) {
        this.warningTime = warningTime;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onWarningTimeChanged(this, warningTime);
        }
    }
    
    public int getWarningDistance() {
        return this.warningDistance;
    }
    
    public void setWarningDistance(final int warningDistance) {
        this.warningDistance = warningDistance;
        for (final IBorderListener iborderlistener : this.getListeners()) {
            iborderlistener.onWarningDistanceChanged(this, warningDistance);
        }
    }
}
