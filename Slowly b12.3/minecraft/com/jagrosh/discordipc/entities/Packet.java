package com.jagrosh.discordipc.entities;

import java.nio.ByteBuffer;
import org.json.JSONObject;

public class Packet {
   private final Packet.OpCode op;
   private final JSONObject data;

   public Packet(Packet.OpCode op, JSONObject data) {
      this.op = op;
      this.data = data;
   }

   public byte[] toBytes() {
      byte[] d = this.data.toString().getBytes();
      ByteBuffer packet = ByteBuffer.allocate(d.length + 8);
      packet.putInt(Integer.reverseBytes(this.op.ordinal()));
      packet.putInt(Integer.reverseBytes(d.length));
      packet.put(d);
      return packet.array();
   }

   public Packet.OpCode getOp() {
      return this.op;
   }

   public JSONObject getJson() {
      return this.data;
   }

   public String toString() {
      return "Pkt:" + this.getOp() + this.getJson().toString();
   }

   public static enum OpCode {
      HANDSHAKE,
      FRAME,
      CLOSE,
      PING,
      PONG;
   }
}
