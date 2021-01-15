/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ 
/*     */ public class S12PacketEntityVelocity
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityID;
/*     */   public int motionX;
/*     */   public int motionY;
/*     */   public int motionZ;
/*     */   
/*     */   public S12PacketEntityVelocity() {}
/*     */   
/*     */   public S12PacketEntityVelocity(Entity entityIn)
/*     */   {
/*  22 */     this(entityIn.getEntityId(), entityIn.motionX, entityIn.motionY, entityIn.motionZ);
/*     */   }
/*     */   
/*     */   public S12PacketEntityVelocity(int entityIDIn, double motionXIn, double motionYIn, double motionZIn)
/*     */   {
/*  27 */     this.entityID = entityIDIn;
/*  28 */     double d0 = 3.9D;
/*     */     
/*  30 */     if (motionXIn < -d0)
/*     */     {
/*  32 */       motionXIn = -d0;
/*     */     }
/*     */     
/*  35 */     if (motionYIn < -d0)
/*     */     {
/*  37 */       motionYIn = -d0;
/*     */     }
/*     */     
/*  40 */     if (motionZIn < -d0)
/*     */     {
/*  42 */       motionZIn = -d0;
/*     */     }
/*     */     
/*  45 */     if (motionXIn > d0)
/*     */     {
/*  47 */       motionXIn = d0;
/*     */     }
/*     */     
/*  50 */     if (motionYIn > d0)
/*     */     {
/*  52 */       motionYIn = d0;
/*     */     }
/*     */     
/*  55 */     if (motionZIn > d0)
/*     */     {
/*  57 */       motionZIn = d0;
/*     */     }
/*     */     
/*  60 */     this.motionX = ((int)(motionXIn * 8000.0D));
/*  61 */     this.motionY = ((int)(motionYIn * 8000.0D));
/*  62 */     this.motionZ = ((int)(motionZIn * 8000.0D));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  70 */     this.entityID = buf.readVarIntFromBuffer();
/*  71 */     this.motionX = buf.readShort();
/*  72 */     this.motionY = buf.readShort();
/*  73 */     this.motionZ = buf.readShort();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  81 */     buf.writeVarIntToBuffer(this.entityID);
/*  82 */     buf.writeShort(this.motionX);
/*  83 */     buf.writeShort(this.motionY);
/*  84 */     buf.writeShort(this.motionZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  92 */     handler.handleEntityVelocity(this);
/*     */   }
/*     */   
/*     */   public int getEntityID()
/*     */   {
/*  97 */     return this.entityID;
/*     */   }
/*     */   
/*     */   public int getMotionX()
/*     */   {
/* 102 */     return this.motionX;
/*     */   }
/*     */   
/*     */   public int getMotionY()
/*     */   {
/* 107 */     return this.motionY;
/*     */   }
/*     */   
/*     */   public int getMotionZ()
/*     */   {
/* 112 */     return this.motionZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S12PacketEntityVelocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */