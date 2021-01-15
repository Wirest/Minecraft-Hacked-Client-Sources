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
/*     */ public class S18PacketEntityTeleport
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private int posX;
/*     */   private int posY;
/*     */   private int posZ;
/*     */   private byte yaw;
/*     */   private byte pitch;
/*     */   private boolean onGround;
/*     */   
/*     */   public S18PacketEntityTeleport() {}
/*     */   
/*     */   public S18PacketEntityTeleport(Entity entityIn)
/*     */   {
/*  26 */     this.entityId = entityIn.getEntityId();
/*  27 */     this.posX = MathHelper.floor_double(entityIn.posX * 32.0D);
/*  28 */     this.posY = MathHelper.floor_double(entityIn.posY * 32.0D);
/*  29 */     this.posZ = MathHelper.floor_double(entityIn.posZ * 32.0D);
/*  30 */     this.yaw = ((byte)(int)(entityIn.rotationYaw * 256.0F / 360.0F));
/*  31 */     this.pitch = ((byte)(int)(entityIn.rotationPitch * 256.0F / 360.0F));
/*  32 */     this.onGround = entityIn.onGround;
/*     */   }
/*     */   
/*     */   public S18PacketEntityTeleport(int entityIdIn, int posXIn, int posYIn, int posZIn, byte yawIn, byte pitchIn, boolean onGroundIn)
/*     */   {
/*  37 */     this.entityId = entityIdIn;
/*  38 */     this.posX = posXIn;
/*  39 */     this.posY = posYIn;
/*  40 */     this.posZ = posZIn;
/*  41 */     this.yaw = yawIn;
/*  42 */     this.pitch = pitchIn;
/*  43 */     this.onGround = onGroundIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  51 */     this.entityId = buf.readVarIntFromBuffer();
/*  52 */     this.posX = buf.readInt();
/*  53 */     this.posY = buf.readInt();
/*  54 */     this.posZ = buf.readInt();
/*  55 */     this.yaw = buf.readByte();
/*  56 */     this.pitch = buf.readByte();
/*  57 */     this.onGround = buf.readBoolean();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  65 */     buf.writeVarIntToBuffer(this.entityId);
/*  66 */     buf.writeInt(this.posX);
/*  67 */     buf.writeInt(this.posY);
/*  68 */     buf.writeInt(this.posZ);
/*  69 */     buf.writeByte(this.yaw);
/*  70 */     buf.writeByte(this.pitch);
/*  71 */     buf.writeBoolean(this.onGround);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  79 */     handler.handleEntityTeleport(this);
/*     */   }
/*     */   
/*     */   public int getEntityId()
/*     */   {
/*  84 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public int getX()
/*     */   {
/*  89 */     return this.posX;
/*     */   }
/*     */   
/*     */   public int getY()
/*     */   {
/*  94 */     return this.posY;
/*     */   }
/*     */   
/*     */   public int getZ()
/*     */   {
/*  99 */     return this.posZ;
/*     */   }
/*     */   
/*     */   public byte getYaw()
/*     */   {
/* 104 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public void setYaw(byte yaw)
/*     */   {
/* 109 */     this.yaw = yaw;
/*     */   }
/*     */   
/*     */   public void setPitch(byte pitch)
/*     */   {
/* 114 */     this.pitch = pitch;
/*     */   }
/*     */   
/*     */   public byte getPitch()
/*     */   {
/* 119 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public boolean getOnGround()
/*     */   {
/* 124 */     return this.onGround;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S18PacketEntityTeleport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */