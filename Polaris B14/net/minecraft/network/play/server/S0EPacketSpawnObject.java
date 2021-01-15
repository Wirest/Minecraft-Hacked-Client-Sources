/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ public class S0EPacketSpawnObject
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private int x;
/*     */   private int y;
/*     */   private int z;
/*     */   private int speedX;
/*     */   private int speedY;
/*     */   private int speedZ;
/*     */   private int pitch;
/*     */   private int yaw;
/*     */   private int type;
/*     */   private int field_149020_k;
/*     */   
/*     */   public S0EPacketSpawnObject() {}
/*     */   
/*     */   public S0EPacketSpawnObject(Entity entityIn, int typeIn)
/*     */   {
/*  30 */     this(entityIn, typeIn, 0);
/*     */   }
/*     */   
/*     */   public S0EPacketSpawnObject(Entity entityIn, int typeIn, int p_i45166_3_)
/*     */   {
/*  35 */     this.entityId = entityIn.getEntityId();
/*  36 */     this.x = MathHelper.floor_double(entityIn.posX * 32.0D);
/*  37 */     this.y = MathHelper.floor_double(entityIn.posY * 32.0D);
/*  38 */     this.z = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*  39 */     this.pitch = MathHelper.floor_float(entityIn.rotationPitch * 256.0F / 360.0F);
/*  40 */     this.yaw = MathHelper.floor_float(entityIn.rotationYaw * 256.0F / 360.0F);
/*  41 */     this.type = typeIn;
/*  42 */     this.field_149020_k = p_i45166_3_;
/*     */     
/*  44 */     if (p_i45166_3_ > 0)
/*     */     {
/*  46 */       double d0 = entityIn.motionX;
/*  47 */       double d1 = entityIn.motionY;
/*  48 */       double d2 = entityIn.motionZ;
/*  49 */       double d3 = 3.9D;
/*     */       
/*  51 */       if (d0 < -d3)
/*     */       {
/*  53 */         d0 = -d3;
/*     */       }
/*     */       
/*  56 */       if (d1 < -d3)
/*     */       {
/*  58 */         d1 = -d3;
/*     */       }
/*     */       
/*  61 */       if (d2 < -d3)
/*     */       {
/*  63 */         d2 = -d3;
/*     */       }
/*     */       
/*  66 */       if (d0 > d3)
/*     */       {
/*  68 */         d0 = d3;
/*     */       }
/*     */       
/*  71 */       if (d1 > d3)
/*     */       {
/*  73 */         d1 = d3;
/*     */       }
/*     */       
/*  76 */       if (d2 > d3)
/*     */       {
/*  78 */         d2 = d3;
/*     */       }
/*     */       
/*  81 */       this.speedX = ((int)(d0 * 8000.0D));
/*  82 */       this.speedY = ((int)(d1 * 8000.0D));
/*  83 */       this.speedZ = ((int)(d2 * 8000.0D));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  92 */     this.entityId = buf.readVarIntFromBuffer();
/*  93 */     this.type = buf.readByte();
/*  94 */     this.x = buf.readInt();
/*  95 */     this.y = buf.readInt();
/*  96 */     this.z = buf.readInt();
/*  97 */     this.pitch = buf.readByte();
/*  98 */     this.yaw = buf.readByte();
/*  99 */     this.field_149020_k = buf.readInt();
/*     */     
/* 101 */     if (this.field_149020_k > 0)
/*     */     {
/* 103 */       this.speedX = buf.readShort();
/* 104 */       this.speedY = buf.readShort();
/* 105 */       this.speedZ = buf.readShort();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/* 114 */     buf.writeVarIntToBuffer(this.entityId);
/* 115 */     buf.writeByte(this.type);
/* 116 */     buf.writeInt(this.x);
/* 117 */     buf.writeInt(this.y);
/* 118 */     buf.writeInt(this.z);
/* 119 */     buf.writeByte(this.pitch);
/* 120 */     buf.writeByte(this.yaw);
/* 121 */     buf.writeInt(this.field_149020_k);
/*     */     
/* 123 */     if (this.field_149020_k > 0)
/*     */     {
/* 125 */       buf.writeShort(this.speedX);
/* 126 */       buf.writeShort(this.speedY);
/* 127 */       buf.writeShort(this.speedZ);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 136 */     handler.handleSpawnObject(this);
/*     */   }
/*     */   
/*     */   public int getEntityID()
/*     */   {
/* 141 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public int getX()
/*     */   {
/* 146 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY()
/*     */   {
/* 151 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getZ()
/*     */   {
/* 156 */     return this.z;
/*     */   }
/*     */   
/*     */   public int getSpeedX()
/*     */   {
/* 161 */     return this.speedX;
/*     */   }
/*     */   
/*     */   public int getSpeedY()
/*     */   {
/* 166 */     return this.speedY;
/*     */   }
/*     */   
/*     */   public int getSpeedZ()
/*     */   {
/* 171 */     return this.speedZ;
/*     */   }
/*     */   
/*     */   public int getPitch()
/*     */   {
/* 176 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public int getYaw()
/*     */   {
/* 181 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public int getType()
/*     */   {
/* 186 */     return this.type;
/*     */   }
/*     */   
/*     */   public int func_149009_m()
/*     */   {
/* 191 */     return this.field_149020_k;
/*     */   }
/*     */   
/*     */   public void setX(int newX)
/*     */   {
/* 196 */     this.x = newX;
/*     */   }
/*     */   
/*     */   public void setY(int newY)
/*     */   {
/* 201 */     this.y = newY;
/*     */   }
/*     */   
/*     */   public void setZ(int newZ)
/*     */   {
/* 206 */     this.z = newZ;
/*     */   }
/*     */   
/*     */   public void setSpeedX(int newSpeedX)
/*     */   {
/* 211 */     this.speedX = newSpeedX;
/*     */   }
/*     */   
/*     */   public void setSpeedY(int newSpeedY)
/*     */   {
/* 216 */     this.speedY = newSpeedY;
/*     */   }
/*     */   
/*     */   public void setSpeedZ(int newSpeedZ)
/*     */   {
/* 221 */     this.speedZ = newSpeedZ;
/*     */   }
/*     */   
/*     */   public void func_149002_g(int p_149002_1_)
/*     */   {
/* 226 */     this.field_149020_k = p_149002_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S0EPacketSpawnObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */