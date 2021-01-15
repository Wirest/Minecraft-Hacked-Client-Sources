/*    */ package com.jagrosh.discordipc.entities;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import org.json.JSONObject;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Packet
/*    */ {
/*    */   private final OpCode op;
/*    */   private final JSONObject data;
/*    */   
/*    */   public Packet(OpCode op, JSONObject data)
/*    */   {
/* 40 */     this.op = op;
/* 41 */     this.data = data;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte[] toBytes()
/*    */   {
/* 51 */     byte[] d = this.data.toString().getBytes();
/* 52 */     ByteBuffer packet = ByteBuffer.allocate(d.length + 8);
/* 53 */     packet.putInt(Integer.reverseBytes(this.op.ordinal()));
/* 54 */     packet.putInt(Integer.reverseBytes(d.length));
/* 55 */     packet.put(d);
/* 56 */     return packet.array();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public OpCode getOp()
/*    */   {
/* 66 */     return this.op;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public JSONObject getJson()
/*    */   {
/* 76 */     return this.data;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 82 */     return "Pkt:" + getOp() + getJson().toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static enum OpCode
/*    */   {
/* 93 */     HANDSHAKE,  FRAME,  CLOSE,  PING,  PONG;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\com\jagrosh\discordipc\entities\Packet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */