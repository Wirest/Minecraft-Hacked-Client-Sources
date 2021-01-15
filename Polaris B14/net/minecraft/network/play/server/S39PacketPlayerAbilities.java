/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ 
/*     */ public class S39PacketPlayerAbilities
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private boolean invulnerable;
/*     */   private boolean flying;
/*     */   private boolean allowFlying;
/*     */   private boolean creativeMode;
/*     */   private float flySpeed;
/*     */   private float walkSpeed;
/*     */   
/*     */   public S39PacketPlayerAbilities() {}
/*     */   
/*     */   public S39PacketPlayerAbilities(PlayerCapabilities capabilities)
/*     */   {
/*  24 */     setInvulnerable(capabilities.disableDamage);
/*  25 */     setFlying(capabilities.isFlying);
/*  26 */     setAllowFlying(capabilities.allowFlying);
/*  27 */     setCreativeMode(capabilities.isCreativeMode);
/*  28 */     setFlySpeed(capabilities.getFlySpeed());
/*  29 */     setWalkSpeed(capabilities.getWalkSpeed());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  37 */     byte b0 = buf.readByte();
/*  38 */     setInvulnerable((b0 & 0x1) > 0);
/*  39 */     setFlying((b0 & 0x2) > 0);
/*  40 */     setAllowFlying((b0 & 0x4) > 0);
/*  41 */     setCreativeMode((b0 & 0x8) > 0);
/*  42 */     setFlySpeed(buf.readFloat());
/*  43 */     setWalkSpeed(buf.readFloat());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  51 */     byte b0 = 0;
/*     */     
/*  53 */     if (isInvulnerable())
/*     */     {
/*  55 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     
/*  58 */     if (isFlying())
/*     */     {
/*  60 */       b0 = (byte)(b0 | 0x2);
/*     */     }
/*     */     
/*  63 */     if (isAllowFlying())
/*     */     {
/*  65 */       b0 = (byte)(b0 | 0x4);
/*     */     }
/*     */     
/*  68 */     if (isCreativeMode())
/*     */     {
/*  70 */       b0 = (byte)(b0 | 0x8);
/*     */     }
/*     */     
/*  73 */     buf.writeByte(b0);
/*  74 */     buf.writeFloat(this.flySpeed);
/*  75 */     buf.writeFloat(this.walkSpeed);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  83 */     handler.handlePlayerAbilities(this);
/*     */   }
/*     */   
/*     */   public boolean isInvulnerable()
/*     */   {
/*  88 */     return this.invulnerable;
/*     */   }
/*     */   
/*     */   public void setInvulnerable(boolean isInvulnerable)
/*     */   {
/*  93 */     this.invulnerable = isInvulnerable;
/*     */   }
/*     */   
/*     */   public boolean isFlying()
/*     */   {
/*  98 */     return this.flying;
/*     */   }
/*     */   
/*     */   public void setFlying(boolean isFlying)
/*     */   {
/* 103 */     this.flying = isFlying;
/*     */   }
/*     */   
/*     */   public boolean isAllowFlying()
/*     */   {
/* 108 */     return this.allowFlying;
/*     */   }
/*     */   
/*     */   public void setAllowFlying(boolean isAllowFlying)
/*     */   {
/* 113 */     this.allowFlying = isAllowFlying;
/*     */   }
/*     */   
/*     */   public boolean isCreativeMode()
/*     */   {
/* 118 */     return this.creativeMode;
/*     */   }
/*     */   
/*     */   public void setCreativeMode(boolean isCreativeMode)
/*     */   {
/* 123 */     this.creativeMode = isCreativeMode;
/*     */   }
/*     */   
/*     */   public float getFlySpeed()
/*     */   {
/* 128 */     return this.flySpeed;
/*     */   }
/*     */   
/*     */   public void setFlySpeed(float flySpeedIn)
/*     */   {
/* 133 */     this.flySpeed = flySpeedIn;
/*     */   }
/*     */   
/*     */   public float getWalkSpeed()
/*     */   {
/* 138 */     return this.walkSpeed;
/*     */   }
/*     */   
/*     */   public void setWalkSpeed(float walkSpeedIn)
/*     */   {
/* 143 */     this.walkSpeed = walkSpeedIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S39PacketPlayerAbilities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */