/*     */ package net.minecraft.network.play.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayServer;
/*     */ 
/*     */ 
/*     */ public class C03PacketPlayer
/*     */   implements Packet<INetHandlerPlayServer>
/*     */ {
/*     */   protected double x;
/*     */   protected double y;
/*     */   protected double z;
/*     */   protected float yaw;
/*     */   protected float pitch;
/*     */   protected boolean onGround;
/*     */   protected boolean moving;
/*     */   protected boolean rotating;
/*     */   
/*     */   public C03PacketPlayer() {}
/*     */   
/*     */   public C03PacketPlayer(boolean isOnGround)
/*     */   {
/*  25 */     this.onGround = isOnGround;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayServer handler)
/*     */   {
/*  33 */     handler.processPlayer(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  41 */     this.onGround = (buf.readUnsignedByte() != 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  49 */     buf.writeByte(this.onGround ? 1 : 0);
/*     */   }
/*     */   
/*     */   public double getPositionX()
/*     */   {
/*  54 */     return this.x;
/*     */   }
/*     */   
/*     */   public double getPositionY()
/*     */   {
/*  59 */     return this.y;
/*     */   }
/*     */   
/*     */   public double getPositionZ()
/*     */   {
/*  64 */     return this.z;
/*     */   }
/*     */   
/*     */   public float getYaw()
/*     */   {
/*  69 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public float getPitch()
/*     */   {
/*  74 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public boolean isOnGround()
/*     */   {
/*  79 */     return this.onGround;
/*     */   }
/*     */   
/*     */   public boolean isMoving()
/*     */   {
/*  84 */     return this.moving;
/*     */   }
/*     */   
/*     */   public boolean getRotating()
/*     */   {
/*  89 */     return this.rotating;
/*     */   }
/*     */   
/*     */   public void setMoving(boolean isMoving)
/*     */   {
/*  94 */     this.moving = isMoving;
/*     */   }
/*     */   
/*     */   public static class C04PacketPlayerPosition extends C03PacketPlayer
/*     */   {
/*     */     public C04PacketPlayerPosition()
/*     */     {
/* 101 */       this.moving = true;
/*     */     }
/*     */     
/*     */     public C04PacketPlayerPosition(double posX, double posY, double posZ, boolean isOnGround)
/*     */     {
/* 106 */       this.x = posX;
/* 107 */       this.y = posY;
/* 108 */       this.z = posZ;
/* 109 */       this.onGround = isOnGround;
/* 110 */       this.moving = true;
/*     */     }
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 115 */       this.x = buf.readDouble();
/* 116 */       this.y = buf.readDouble();
/* 117 */       this.z = buf.readDouble();
/* 118 */       super.readPacketData(buf);
/*     */     }
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 123 */       buf.writeDouble(this.x);
/* 124 */       buf.writeDouble(this.y);
/* 125 */       buf.writeDouble(this.z);
/* 126 */       super.writePacketData(buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C05PacketPlayerLook extends C03PacketPlayer
/*     */   {
/*     */     public C05PacketPlayerLook()
/*     */     {
/* 134 */       this.rotating = true;
/*     */     }
/*     */     
/*     */     public C05PacketPlayerLook(float playerYaw, float playerPitch, boolean isOnGround)
/*     */     {
/* 139 */       this.yaw = playerYaw;
/* 140 */       this.pitch = playerPitch;
/* 141 */       this.onGround = isOnGround;
/* 142 */       this.rotating = true;
/*     */     }
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 147 */       this.yaw = buf.readFloat();
/* 148 */       this.pitch = buf.readFloat();
/* 149 */       super.readPacketData(buf);
/*     */     }
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 154 */       buf.writeFloat(this.yaw);
/* 155 */       buf.writeFloat(this.pitch);
/* 156 */       super.writePacketData(buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class C06PacketPlayerPosLook extends C03PacketPlayer
/*     */   {
/*     */     public C06PacketPlayerPosLook()
/*     */     {
/* 164 */       this.moving = true;
/* 165 */       this.rotating = true;
/*     */     }
/*     */     
/*     */     public C06PacketPlayerPosLook(double playerX, double playerY, double playerZ, float playerYaw, float playerPitch, boolean playerIsOnGround)
/*     */     {
/* 170 */       this.x = playerX;
/* 171 */       this.y = playerY;
/* 172 */       this.z = playerZ;
/* 173 */       this.yaw = playerYaw;
/* 174 */       this.pitch = playerPitch;
/* 175 */       this.onGround = playerIsOnGround;
/* 176 */       this.rotating = true;
/* 177 */       this.moving = true;
/*     */     }
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 182 */       this.x = buf.readDouble();
/* 183 */       this.y = buf.readDouble();
/* 184 */       this.z = buf.readDouble();
/* 185 */       this.yaw = buf.readFloat();
/* 186 */       this.pitch = buf.readFloat();
/* 187 */       super.readPacketData(buf);
/*     */     }
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 192 */       buf.writeDouble(this.x);
/* 193 */       buf.writeDouble(this.y);
/* 194 */       buf.writeDouble(this.z);
/* 195 */       buf.writeFloat(this.yaw);
/* 196 */       buf.writeFloat(this.pitch);
/* 197 */       super.writePacketData(buf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C03PacketPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */