package net.minecraft.network.play.server;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S08PacketPlayerPosLook implements Packet {
   private double x;
   private double y;
   private double z;
   private float yaw;
   private float pitch;
   private Set field_179835_f;

   public S08PacketPlayerPosLook() {
   }

   public S08PacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set p_i45993_9_) {
      this.x = xIn;
      this.y = yIn;
      this.z = zIn;
      this.yaw = yawIn;
      this.pitch = pitchIn;
      this.field_179835_f = p_i45993_9_;
   }

   public void readPacketData(PacketBuffer buf) throws IOException {
      this.x = buf.readDouble();
      this.y = buf.readDouble();
      this.z = buf.readDouble();
      this.yaw = buf.readFloat();
      this.pitch = buf.readFloat();
      this.field_179835_f = S08PacketPlayerPosLook.EnumFlags.func_180053_a(buf.readUnsignedByte());
   }

   public void writePacketData(PacketBuffer buf) throws IOException {
      buf.writeDouble(this.x);
      buf.writeDouble(this.y);
      buf.writeDouble(this.z);
      buf.writeFloat(this.yaw);
      buf.writeFloat(this.pitch);
      buf.writeByte(S08PacketPlayerPosLook.EnumFlags.func_180056_a(this.field_179835_f));
   }

   public void processPacket(INetHandlerPlayClient handler) {
      handler.handlePlayerPosLook(this);
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public Set func_179834_f() {
      return this.field_179835_f;
   }

   public static enum EnumFlags {
      X(0),
      Y(1),
      Z(2),
      Y_ROT(3),
      X_ROT(4);

      private final int field_180058_f;

      private EnumFlags(int p_i45992_3_) {
         this.field_180058_f = p_i45992_3_;
      }

      public static Set func_180053_a(int p_180053_0_) {
         Set set = EnumSet.noneOf(S08PacketPlayerPosLook.EnumFlags.class);
         S08PacketPlayerPosLook.EnumFlags[] var2 = values();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags = var2[var4];
            if (s08packetplayerposlook$enumflags.func_180054_b(p_180053_0_)) {
               set.add(s08packetplayerposlook$enumflags);
            }
         }

         return set;
      }

      public static int func_180056_a(Set p_180056_0_) {
         int i = 0;

         S08PacketPlayerPosLook.EnumFlags s08packetplayerposlook$enumflags;
         for(Iterator var2 = p_180056_0_.iterator(); var2.hasNext(); i |= s08packetplayerposlook$enumflags.func_180055_a()) {
            s08packetplayerposlook$enumflags = (S08PacketPlayerPosLook.EnumFlags)var2.next();
         }

         return i;
      }

      private int func_180055_a() {
         return 1 << this.field_180058_f;
      }

      private boolean func_180054_b(int p_180054_1_) {
         return (p_180054_1_ & this.func_180055_a()) == this.func_180055_a();
      }
   }
}
