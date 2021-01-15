package net.minecraft.world.border;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;

public class WorldBorder
{
    private final List listeners = Lists.newArrayList();
    private double centerX = 0.0D;
    private double centerZ = 0.0D;
    private double startDiameter = 6.0E7D;
    private double endDiameter;
    private long endTime;
    private long startTime;
    private int worldSize;
    private double damageAmount;
    private double damageBuffer;
    private int warningTime;
    private int warningDistance;

    public WorldBorder()
    {
        this.endDiameter = this.startDiameter;
        this.worldSize = 29999984;
        this.damageAmount = 0.2D;
        this.damageBuffer = 5.0D;
        this.warningTime = 15;
        this.warningDistance = 5;
    }

    public boolean contains(BlockPos pos)
    {
        return pos.getX() + 1 > this.minX() && pos.getX() < this.maxX() && pos.getZ() + 1 > this.minZ() && pos.getZ() < this.maxZ();
    }

    public boolean contains(ChunkCoordIntPair range)
    {
        return range.getXEnd() > this.minX() && range.getXStart() < this.maxX() && range.getZEnd() > this.minZ() && range.getZStart() < this.maxZ();
    }

    public boolean contains(AxisAlignedBB bb)
    {
        return bb.maxX > this.minX() && bb.minX < this.maxX() && bb.maxZ > this.minZ() && bb.minZ < this.maxZ();
    }

    public double getClosestDistance(Entity entityIn)
    {
        return this.getClosestDistance(entityIn.posX, entityIn.posZ);
    }

    public double getClosestDistance(double x, double z)
    {
        double var5 = z - this.minZ();
        double var7 = this.maxZ() - z;
        double var9 = x - this.minX();
        double var11 = this.maxX() - x;
        double var13 = Math.min(var9, var11);
        var13 = Math.min(var13, var5);
        return Math.min(var13, var7);
    }

    public EnumBorderStatus getStatus()
    {
        return this.endDiameter < this.startDiameter ? EnumBorderStatus.SHRINKING : (this.endDiameter > this.startDiameter ? EnumBorderStatus.GROWING : EnumBorderStatus.STATIONARY);
    }

    public double minX()
    {
        double var1 = this.getCenterX() - this.getDiameter() / 2.0D;

        if (var1 < (-this.worldSize))
        {
            var1 = (-this.worldSize);
        }

        return var1;
    }

    public double minZ()
    {
        double var1 = this.getCenterZ() - this.getDiameter() / 2.0D;

        if (var1 < (-this.worldSize))
        {
            var1 = (-this.worldSize);
        }

        return var1;
    }

    public double maxX()
    {
        double var1 = this.getCenterX() + this.getDiameter() / 2.0D;

        if (var1 > this.worldSize)
        {
            var1 = this.worldSize;
        }

        return var1;
    }

    public double maxZ()
    {
        double var1 = this.getCenterZ() + this.getDiameter() / 2.0D;

        if (var1 > this.worldSize)
        {
            var1 = this.worldSize;
        }

        return var1;
    }

    public double getCenterX()
    {
        return this.centerX;
    }

    public double getCenterZ()
    {
        return this.centerZ;
    }

    public void setCenter(double x, double z)
    {
        this.centerX = x;
        this.centerZ = z;
        Iterator var5 = this.getListeners().iterator();

        while (var5.hasNext())
        {
            IBorderListener var6 = (IBorderListener)var5.next();
            var6.onCenterChanged(this, x, z);
        }
    }

    public double getDiameter()
    {
        if (this.getStatus() != EnumBorderStatus.STATIONARY)
        {
            double var1 = (float)(System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime);

            if (var1 < 1.0D)
            {
                return this.startDiameter + (this.endDiameter - this.startDiameter) * var1;
            }

            this.setTransition(this.endDiameter);
        }

        return this.startDiameter;
    }

    public long getTimeUntilTarget()
    {
        return this.getStatus() != EnumBorderStatus.STATIONARY ? this.endTime - System.currentTimeMillis() : 0L;
    }

    public double getTargetSize()
    {
        return this.endDiameter;
    }

    public void setTransition(double newSize)
    {
        this.startDiameter = newSize;
        this.endDiameter = newSize;
        this.endTime = System.currentTimeMillis();
        this.startTime = this.endTime;
        Iterator var3 = this.getListeners().iterator();

        while (var3.hasNext())
        {
            IBorderListener var4 = (IBorderListener)var3.next();
            var4.onSizeChanged(this, newSize);
        }
    }

    public void setTransition(double oldSize, double newSize, long time)
    {
        this.startDiameter = oldSize;
        this.endDiameter = newSize;
        this.startTime = System.currentTimeMillis();
        this.endTime = this.startTime + time;
        Iterator var7 = this.getListeners().iterator();

        while (var7.hasNext())
        {
            IBorderListener var8 = (IBorderListener)var7.next();
            var8.onTransitionStarted(this, oldSize, newSize, time);
        }
    }

    protected List getListeners()
    {
        return Lists.newArrayList(this.listeners);
    }

    public void addListener(IBorderListener listener)
    {
        this.listeners.add(listener);
    }

    public void setSize(int size)
    {
        this.worldSize = size;
    }

    public int getSize()
    {
        return this.worldSize;
    }

    public double getDamageBuffer()
    {
        return this.damageBuffer;
    }

    public void setDamageBuffer(double bufferSize)
    {
        this.damageBuffer = bufferSize;
        Iterator var3 = this.getListeners().iterator();

        while (var3.hasNext())
        {
            IBorderListener var4 = (IBorderListener)var3.next();
            var4.onDamageBufferChanged(this, bufferSize);
        }
    }

    public double getDamageAmount()
    {
        return this.damageAmount;
    }

    public void setDamageAmount(double newAmount)
    {
        this.damageAmount = newAmount;
        Iterator var3 = this.getListeners().iterator();

        while (var3.hasNext())
        {
            IBorderListener var4 = (IBorderListener)var3.next();
            var4.onDamageAmountChanged(this, newAmount);
        }
    }

    public double getResizeSpeed()
    {
        return this.endTime == this.startTime ? 0.0D : Math.abs(this.startDiameter - this.endDiameter) / (this.endTime - this.startTime);
    }

    public int getWarningTime()
    {
        return this.warningTime;
    }

    public void setWarningTime(int warningTime)
    {
        this.warningTime = warningTime;
        Iterator var2 = this.getListeners().iterator();

        while (var2.hasNext())
        {
            IBorderListener var3 = (IBorderListener)var2.next();
            var3.onWarningTimeChanged(this, warningTime);
        }
    }

    public int getWarningDistance()
    {
        return this.warningDistance;
    }

    public void setWarningDistance(int warningDistance)
    {
        this.warningDistance = warningDistance;
        Iterator var2 = this.getListeners().iterator();

        while (var2.hasNext())
        {
            IBorderListener var3 = (IBorderListener)var2.next();
            var3.onWarningDistanceChanged(this, warningDistance);
        }
    }
}
