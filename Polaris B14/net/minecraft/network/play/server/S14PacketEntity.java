/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class S14PacketEntity
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   protected int entityId;
/*     */   protected byte posX;
/*     */   protected byte posY;
/*     */   protected byte posZ;
/*     */   protected byte yaw;
/*     */   protected byte pitch;
/*     */   protected boolean onGround;
/*     */   protected boolean field_149069_g;
/*     */   
/*     */   public S14PacketEntity() {}
/*     */   
/*     */   public S14PacketEntity(int entityIdIn)
/*     */   {
/*  27 */     this.entityId = entityIdIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  35 */     this.entityId = buf.readVarIntFromBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  43 */     buf.writeVarIntToBuffer(this.entityId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  51 */     handler.handleEntityMovement(this);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  56 */     return "Entity_" + super.toString();
/*     */   }
/*     */   
/*     */   public Entity getEntity(World worldIn)
/*     */   {
/*  61 */     return worldIn.getEntityByID(this.entityId);
/*     */   }
/*     */   
/*     */   public byte func_149062_c()
/*     */   {
/*  66 */     return this.posX;
/*     */   }
/*     */   
/*     */   public byte func_149061_d()
/*     */   {
/*  71 */     return this.posY;
/*     */   }
/*     */   
/*     */   public byte func_149064_e()
/*     */   {
/*  76 */     return this.posZ;
/*     */   }
/*     */   
/*     */   public byte func_149066_f()
/*     */   {
/*  81 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public byte func_149063_g()
/*     */   {
/*  86 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public boolean func_149060_h()
/*     */   {
/*  91 */     return this.field_149069_g;
/*     */   }
/*     */   
/*     */   public boolean getOnGround()
/*     */   {
/*  96 */     return this.onGround;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class S15PacketEntityRelMove
/*     */     extends S14PacketEntity
/*     */   {
/*     */     public S15PacketEntityRelMove() {}
/*     */     
/*     */     public S15PacketEntityRelMove(int entityIdIn, byte x, byte y, byte z, boolean onGroundIn)
/*     */     {
/* 107 */       super();
/* 108 */       this.posX = x;
/* 109 */       this.posY = y;
/* 110 */       this.posZ = z;
/* 111 */       this.onGround = onGroundIn;
/*     */     }
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 116 */       super.readPacketData(buf);
/* 117 */       this.posX = buf.readByte();
/* 118 */       this.posY = buf.readByte();
/* 119 */       this.posZ = buf.readByte();
/* 120 */       this.onGround = buf.readBoolean();
/*     */     }
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 125 */       super.writePacketData(buf);
/* 126 */       buf.writeByte(this.posX);
/* 127 */       buf.writeByte(this.posY);
/* 128 */       buf.writeByte(this.posZ);
/* 129 */       buf.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class S16PacketEntityLook extends S14PacketEntity
/*     */   {
/*     */     public S16PacketEntityLook()
/*     */     {
/* 137 */       this.field_149069_g = true;
/*     */     }
/*     */     
/*     */     public S16PacketEntityLook(int entityIdIn, byte yawIn, byte pitchIn, boolean onGroundIn)
/*     */     {
/* 142 */       super();
/* 143 */       this.yaw = yawIn;
/* 144 */       this.pitch = pitchIn;
/* 145 */       this.field_149069_g = true;
/* 146 */       this.onGround = onGroundIn;
/*     */     }
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 151 */       super.readPacketData(buf);
/* 152 */       this.yaw = buf.readByte();
/* 153 */       this.pitch = buf.readByte();
/* 154 */       this.onGround = buf.readBoolean();
/*     */     }
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 159 */       super.writePacketData(buf);
/* 160 */       buf.writeByte(this.yaw);
/* 161 */       buf.writeByte(this.pitch);
/* 162 */       buf.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class S17PacketEntityLookMove extends S14PacketEntity
/*     */   {
/*     */     public S17PacketEntityLookMove()
/*     */     {
/* 170 */       this.field_149069_g = true;
/*     */     }
/*     */     
/*     */     public S17PacketEntityLookMove(int p_i45973_1_, byte p_i45973_2_, byte p_i45973_3_, byte p_i45973_4_, byte p_i45973_5_, byte p_i45973_6_, boolean p_i45973_7_)
/*     */     {
/* 175 */       super();
/* 176 */       this.posX = p_i45973_2_;
/* 177 */       this.posY = p_i45973_3_;
/* 178 */       this.posZ = p_i45973_4_;
/* 179 */       this.yaw = p_i45973_5_;
/* 180 */       this.pitch = p_i45973_6_;
/* 181 */       this.onGround = p_i45973_7_;
/* 182 */       this.field_149069_g = true;
/*     */     }
/*     */     
/*     */     public void readPacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 187 */       super.readPacketData(buf);
/* 188 */       this.posX = buf.readByte();
/* 189 */       this.posY = buf.readByte();
/* 190 */       this.posZ = buf.readByte();
/* 191 */       this.yaw = buf.readByte();
/* 192 */       this.pitch = buf.readByte();
/* 193 */       this.onGround = buf.readBoolean();
/*     */     }
/*     */     
/*     */     public void writePacketData(PacketBuffer buf) throws IOException
/*     */     {
/* 198 */       super.writePacketData(buf);
/* 199 */       buf.writeByte(this.posX);
/* 200 */       buf.writeByte(this.posY);
/* 201 */       buf.writeByte(this.posZ);
/* 202 */       buf.writeByte(this.yaw);
/* 203 */       buf.writeByte(this.pitch);
/* 204 */       buf.writeBoolean(this.onGround);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S14PacketEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */