/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class C0CPacketInput
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private float strafeSpeed;
/*    */   private float forwardSpeed;
/*    */   private boolean jumping;
/*    */   private boolean sneaking;
/*    */   
/*    */   public C0CPacketInput() {}
/*    */   
/*    */   public C0CPacketInput(float strafeSpeed, float forwardSpeed, boolean jumping, boolean sneaking)
/*    */   {
/* 24 */     this.strafeSpeed = strafeSpeed;
/* 25 */     this.forwardSpeed = forwardSpeed;
/* 26 */     this.jumping = jumping;
/* 27 */     this.sneaking = sneaking;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 35 */     this.strafeSpeed = buf.readFloat();
/* 36 */     this.forwardSpeed = buf.readFloat();
/* 37 */     byte b0 = buf.readByte();
/* 38 */     this.jumping = ((b0 & 0x1) > 0);
/* 39 */     this.sneaking = ((b0 & 0x2) > 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 47 */     buf.writeFloat(this.strafeSpeed);
/* 48 */     buf.writeFloat(this.forwardSpeed);
/* 49 */     byte b0 = 0;
/*    */     
/* 51 */     if (this.jumping)
/*    */     {
/* 53 */       b0 = (byte)(b0 | 0x1);
/*    */     }
/*    */     
/* 56 */     if (this.sneaking)
/*    */     {
/* 58 */       b0 = (byte)(b0 | 0x2);
/*    */     }
/*    */     
/* 61 */     buf.writeByte(b0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 69 */     handler.processInput(this);
/*    */   }
/*    */   
/*    */   public float getStrafeSpeed()
/*    */   {
/* 74 */     return this.strafeSpeed;
/*    */   }
/*    */   
/*    */   public float getForwardSpeed()
/*    */   {
/* 79 */     return this.forwardSpeed;
/*    */   }
/*    */   
/*    */   public boolean isJumping()
/*    */   {
/* 84 */     return this.jumping;
/*    */   }
/*    */   
/*    */   public boolean isSneaking()
/*    */   {
/* 89 */     return this.sneaking;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C0CPacketInput.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */