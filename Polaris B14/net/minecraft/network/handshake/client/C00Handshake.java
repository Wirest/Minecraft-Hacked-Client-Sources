/*    */ package net.minecraft.network.handshake.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.EnumConnectionState;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.handshake.INetHandlerHandshakeServer;
/*    */ 
/*    */ 
/*    */ public class C00Handshake
/*    */   implements Packet<INetHandlerHandshakeServer>
/*    */ {
/*    */   private int protocolVersion;
/*    */   private String ip;
/*    */   private int port;
/*    */   private EnumConnectionState requestedState;
/*    */   
/*    */   public C00Handshake() {}
/*    */   
/*    */   public C00Handshake(int version, String ip, int port, EnumConnectionState requestedState)
/*    */   {
/* 22 */     this.protocolVersion = version;
/* 23 */     this.ip = ip;
/* 24 */     this.port = port;
/* 25 */     this.requestedState = requestedState;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 33 */     this.protocolVersion = buf.readVarIntFromBuffer();
/* 34 */     this.ip = buf.readStringFromBuffer(255);
/* 35 */     this.port = buf.readUnsignedShort();
/* 36 */     this.requestedState = EnumConnectionState.getById(buf.readVarIntFromBuffer());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 44 */     buf.writeVarIntToBuffer(this.protocolVersion);
/* 45 */     buf.writeString(this.ip);
/* 46 */     buf.writeShort(this.port);
/* 47 */     buf.writeVarIntToBuffer(this.requestedState.getId());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerHandshakeServer handler)
/*    */   {
/* 55 */     handler.processHandshake(this);
/*    */   }
/*    */   
/*    */   public EnumConnectionState getRequestedState()
/*    */   {
/* 60 */     return this.requestedState;
/*    */   }
/*    */   
/*    */   public int getProtocolVersion()
/*    */   {
/* 65 */     return this.protocolVersion;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\handshake\client\C00Handshake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */