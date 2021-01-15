/*     */ package net.minecraft.world.border;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ 
/*     */ public class WorldBorder
/*     */ {
/*  12 */   private final List<IBorderListener> listeners = Lists.newArrayList();
/*  13 */   private double centerX = 0.0D;
/*  14 */   private double centerZ = 0.0D;
/*  15 */   private double startDiameter = 6.0E7D;
/*     */   private double endDiameter;
/*     */   private long endTime;
/*     */   private long startTime;
/*     */   private int worldSize;
/*     */   private double damageAmount;
/*     */   private double damageBuffer;
/*     */   private int warningTime;
/*     */   private int warningDistance;
/*     */   
/*     */   public WorldBorder()
/*     */   {
/*  27 */     this.endDiameter = this.startDiameter;
/*  28 */     this.worldSize = 29999984;
/*  29 */     this.damageAmount = 0.2D;
/*  30 */     this.damageBuffer = 5.0D;
/*  31 */     this.warningTime = 15;
/*  32 */     this.warningDistance = 5;
/*     */   }
/*     */   
/*     */   public boolean contains(BlockPos pos)
/*     */   {
/*  37 */     return (pos.getX() + 1 > minX()) && (pos.getX() < maxX()) && (pos.getZ() + 1 > minZ()) && (pos.getZ() < maxZ());
/*     */   }
/*     */   
/*     */   public boolean contains(ChunkCoordIntPair range)
/*     */   {
/*  42 */     return (range.getXEnd() > minX()) && (range.getXStart() < maxX()) && (range.getZEnd() > minZ()) && (range.getZStart() < maxZ());
/*     */   }
/*     */   
/*     */   public boolean contains(AxisAlignedBB bb)
/*     */   {
/*  47 */     return (bb.maxX > minX()) && (bb.minX < maxX()) && (bb.maxZ > minZ()) && (bb.minZ < maxZ());
/*     */   }
/*     */   
/*     */   public double getClosestDistance(Entity entityIn)
/*     */   {
/*  52 */     return getClosestDistance(entityIn.posX, entityIn.posZ);
/*     */   }
/*     */   
/*     */   public double getClosestDistance(double x, double z)
/*     */   {
/*  57 */     double d0 = z - minZ();
/*  58 */     double d1 = maxZ() - z;
/*  59 */     double d2 = x - minX();
/*  60 */     double d3 = maxX() - x;
/*  61 */     double d4 = Math.min(d2, d3);
/*  62 */     d4 = Math.min(d4, d0);
/*  63 */     return Math.min(d4, d1);
/*     */   }
/*     */   
/*     */   public EnumBorderStatus getStatus()
/*     */   {
/*  68 */     return this.endDiameter > this.startDiameter ? EnumBorderStatus.GROWING : this.endDiameter < this.startDiameter ? EnumBorderStatus.SHRINKING : EnumBorderStatus.STATIONARY;
/*     */   }
/*     */   
/*     */   public double minX()
/*     */   {
/*  73 */     double d0 = getCenterX() - getDiameter() / 2.0D;
/*     */     
/*  75 */     if (d0 < -this.worldSize)
/*     */     {
/*  77 */       d0 = -this.worldSize;
/*     */     }
/*     */     
/*  80 */     return d0;
/*     */   }
/*     */   
/*     */   public double minZ()
/*     */   {
/*  85 */     double d0 = getCenterZ() - getDiameter() / 2.0D;
/*     */     
/*  87 */     if (d0 < -this.worldSize)
/*     */     {
/*  89 */       d0 = -this.worldSize;
/*     */     }
/*     */     
/*  92 */     return d0;
/*     */   }
/*     */   
/*     */   public double maxX()
/*     */   {
/*  97 */     double d0 = getCenterX() + getDiameter() / 2.0D;
/*     */     
/*  99 */     if (d0 > this.worldSize)
/*     */     {
/* 101 */       d0 = this.worldSize;
/*     */     }
/*     */     
/* 104 */     return d0;
/*     */   }
/*     */   
/*     */   public double maxZ()
/*     */   {
/* 109 */     double d0 = getCenterZ() + getDiameter() / 2.0D;
/*     */     
/* 111 */     if (d0 > this.worldSize)
/*     */     {
/* 113 */       d0 = this.worldSize;
/*     */     }
/*     */     
/* 116 */     return d0;
/*     */   }
/*     */   
/*     */   public double getCenterX()
/*     */   {
/* 121 */     return this.centerX;
/*     */   }
/*     */   
/*     */   public double getCenterZ()
/*     */   {
/* 126 */     return this.centerZ;
/*     */   }
/*     */   
/*     */   public void setCenter(double x, double z)
/*     */   {
/* 131 */     this.centerX = x;
/* 132 */     this.centerZ = z;
/*     */     
/* 134 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 136 */       iborderlistener.onCenterChanged(this, x, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getDiameter()
/*     */   {
/* 142 */     if (getStatus() != EnumBorderStatus.STATIONARY)
/*     */     {
/* 144 */       double d0 = (float)(System.currentTimeMillis() - this.startTime) / (float)(this.endTime - this.startTime);
/*     */       
/* 146 */       if (d0 < 1.0D)
/*     */       {
/* 148 */         return this.startDiameter + (this.endDiameter - this.startDiameter) * d0;
/*     */       }
/*     */       
/* 151 */       setTransition(this.endDiameter);
/*     */     }
/*     */     
/* 154 */     return this.startDiameter;
/*     */   }
/*     */   
/*     */   public long getTimeUntilTarget()
/*     */   {
/* 159 */     return getStatus() != EnumBorderStatus.STATIONARY ? this.endTime - System.currentTimeMillis() : 0L;
/*     */   }
/*     */   
/*     */   public double getTargetSize()
/*     */   {
/* 164 */     return this.endDiameter;
/*     */   }
/*     */   
/*     */   public void setTransition(double newSize)
/*     */   {
/* 169 */     this.startDiameter = newSize;
/* 170 */     this.endDiameter = newSize;
/* 171 */     this.endTime = System.currentTimeMillis();
/* 172 */     this.startTime = this.endTime;
/*     */     
/* 174 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 176 */       iborderlistener.onSizeChanged(this, newSize);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTransition(double oldSize, double newSize, long time)
/*     */   {
/* 182 */     this.startDiameter = oldSize;
/* 183 */     this.endDiameter = newSize;
/* 184 */     this.startTime = System.currentTimeMillis();
/* 185 */     this.endTime = (this.startTime + time);
/*     */     
/* 187 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 189 */       iborderlistener.onTransitionStarted(this, oldSize, newSize, time);
/*     */     }
/*     */   }
/*     */   
/*     */   protected List<IBorderListener> getListeners()
/*     */   {
/* 195 */     return Lists.newArrayList(this.listeners);
/*     */   }
/*     */   
/*     */   public void addListener(IBorderListener listener)
/*     */   {
/* 200 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void setSize(int size)
/*     */   {
/* 205 */     this.worldSize = size;
/*     */   }
/*     */   
/*     */   public int getSize()
/*     */   {
/* 210 */     return this.worldSize;
/*     */   }
/*     */   
/*     */   public double getDamageBuffer()
/*     */   {
/* 215 */     return this.damageBuffer;
/*     */   }
/*     */   
/*     */   public void setDamageBuffer(double bufferSize)
/*     */   {
/* 220 */     this.damageBuffer = bufferSize;
/*     */     
/* 222 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 224 */       iborderlistener.onDamageBufferChanged(this, bufferSize);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getDamageAmount()
/*     */   {
/* 230 */     return this.damageAmount;
/*     */   }
/*     */   
/*     */   public void setDamageAmount(double newAmount)
/*     */   {
/* 235 */     this.damageAmount = newAmount;
/*     */     
/* 237 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 239 */       iborderlistener.onDamageAmountChanged(this, newAmount);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getResizeSpeed()
/*     */   {
/* 245 */     return this.endTime == this.startTime ? 0.0D : Math.abs(this.startDiameter - this.endDiameter) / (this.endTime - this.startTime);
/*     */   }
/*     */   
/*     */   public int getWarningTime()
/*     */   {
/* 250 */     return this.warningTime;
/*     */   }
/*     */   
/*     */   public void setWarningTime(int warningTime)
/*     */   {
/* 255 */     this.warningTime = warningTime;
/*     */     
/* 257 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 259 */       iborderlistener.onWarningTimeChanged(this, warningTime);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getWarningDistance()
/*     */   {
/* 265 */     return this.warningDistance;
/*     */   }
/*     */   
/*     */   public void setWarningDistance(int warningDistance)
/*     */   {
/* 270 */     this.warningDistance = warningDistance;
/*     */     
/* 272 */     for (IBorderListener iborderlistener : getListeners())
/*     */     {
/* 274 */       iborderlistener.onWarningDistanceChanged(this, warningDistance);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\border\WorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */