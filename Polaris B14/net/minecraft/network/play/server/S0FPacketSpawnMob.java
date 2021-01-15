/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.DataWatcher.WatchableObject;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ public class S0FPacketSpawnMob
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private int type;
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private int velocityX;
/*     */   private int velocityY;
/*     */   private int velocityZ;
/*     */   private byte yaw;
/*     */   private byte pitch;
/*     */   private byte headPitch;
/*     */   private DataWatcher field_149043_l;
/*     */   private List<DataWatcher.WatchableObject> watcher;
/*     */   
/*     */   public S0FPacketSpawnMob() {}
/*     */   
/*     */   public S0FPacketSpawnMob(EntityLivingBase entityIn)
/*     */   {
/*  35 */     this.entityId = entityIn.getEntityId();
/*  36 */     this.type = ((byte)EntityList.getEntityID(entityIn));
/*  37 */     this.x = MathHelper.floor_double(entityIn.posX * 32.0D);
/*  38 */     this.y = MathHelper.floor_double(entityIn.posY * 32.0D);
/*  39 */     this.z = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*  40 */     this.yaw = ((byte)(int)(entityIn.rotationYaw * 256.0F / 360.0F));
/*  41 */     this.pitch = ((byte)(int)(entityIn.rotationPitch * 256.0F / 360.0F));
/*  42 */     this.headPitch = ((byte)(int)(entityIn.rotationYawHead * 256.0F / 360.0F));
/*  43 */     double d0 = 3.9D;
/*  44 */     double d1 = entityIn.motionX;
/*  45 */     double d2 = entityIn.motionY;
/*  46 */     double d3 = entityIn.motionZ;
/*     */     
/*  48 */     if (d1 < -d0)
/*     */     {
/*  50 */       d1 = -d0;
/*     */     }
/*     */     
/*  53 */     if (d2 < -d0)
/*     */     {
/*  55 */       d2 = -d0;
/*     */     }
/*     */     
/*  58 */     if (d3 < -d0)
/*     */     {
/*  60 */       d3 = -d0;
/*     */     }
/*     */     
/*  63 */     if (d1 > d0)
/*     */     {
/*  65 */       d1 = d0;
/*     */     }
/*     */     
/*  68 */     if (d2 > d0)
/*     */     {
/*  70 */       d2 = d0;
/*     */     }
/*     */     
/*  73 */     if (d3 > d0)
/*     */     {
/*  75 */       d3 = d0;
/*     */     }
/*     */     
/*  78 */     this.velocityX = ((int)(d1 * 8000.0D));
/*  79 */     this.velocityY = ((int)(d2 * 8000.0D));
/*  80 */     this.velocityZ = ((int)(d3 * 8000.0D));
/*  81 */     this.field_149043_l = entityIn.getDataWatcher();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  89 */     this.entityId = buf.readVarIntFromBuffer();
/*  90 */     this.type = (buf.readByte() & 0xFF);
/*  91 */     this.x = buf.readInt();
/*  92 */     this.y = buf.readInt();
/*  93 */     this.z = buf.readInt();
/*  94 */     this.yaw = buf.readByte();
/*  95 */     this.pitch = buf.readByte();
/*  96 */     this.headPitch = buf.readByte();
/*  97 */     this.velocityX = buf.readShort();
/*  98 */     this.velocityY = buf.readShort();
/*  99 */     this.velocityZ = buf.readShort();
/* 100 */     this.watcher = DataWatcher.readWatchedListFromPacketBuffer(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/* 108 */     buf.writeVarIntToBuffer(this.entityId);
/* 109 */     buf.writeByte(this.type & 0xFF);
/* 110 */     buf.writeInt(this.x);
/* 111 */     buf.writeInt(this.y);
/* 112 */     buf.writeInt(this.z);
/* 113 */     buf.writeByte(this.yaw);
/* 114 */     buf.writeByte(this.pitch);
/* 115 */     buf.writeByte(this.headPitch);
/* 116 */     buf.writeShort(this.velocityX);
/* 117 */     buf.writeShort(this.velocityY);
/* 118 */     buf.writeShort(this.velocityZ);
/* 119 */     this.field_149043_l.writeTo(buf);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 127 */     handler.handleSpawnMob(this);
/*     */   }
/*     */   
/*     */   public List<DataWatcher.WatchableObject> func_149027_c()
/*     */   {
/* 132 */     if (this.watcher == null)
/*     */     {
/* 134 */       this.watcher = this.field_149043_l.getAllWatched();
/*     */     }
/*     */     
/* 137 */     return this.watcher;
/*     */   }
/*     */   
/*     */   public int getEntityID()
/*     */   {
/* 142 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public int getEntityType()
/*     */   {
/* 147 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getX()
/*     */   {
/* 152 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY()
/*     */   {
/* 157 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ()
/*     */   {
/* 162 */     return this.z;
/*     */   }
/*     */   
/*     */   public int getVelocityX()
/*     */   {
/* 167 */     return this.velocityX;
/*     */   }
/*     */   
/*     */   public int getVelocityY()
/*     */   {
/* 172 */     return this.velocityY;
/*     */   }
/*     */   
/*     */   public int getVelocityZ()
/*     */   {
/* 177 */     return this.velocityZ;
/*     */   }
/*     */   
/*     */   public byte getYaw()
/*     */   {
/* 182 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public byte getPitch()
/*     */   {
/* 187 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public byte getHeadPitch()
/*     */   {
/* 192 */     return this.headPitch;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S0FPacketSpawnMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */