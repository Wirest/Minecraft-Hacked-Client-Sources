/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ 
/*     */ 
/*     */ public class S08PacketPlayerPosLook
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private double x;
/*     */   private double y;
/*     */   private double z;
/*     */   public float yaw;
/*     */   public float pitch;
/*     */   private Set<EnumFlags> field_179835_f;
/*     */   
/*     */   public S08PacketPlayerPosLook() {}
/*     */   
/*     */   public S08PacketPlayerPosLook(double xIn, double yIn, double zIn, float yawIn, float pitchIn, Set<EnumFlags> p_i45993_9_)
/*     */   {
/*  25 */     this.x = xIn;
/*  26 */     this.y = yIn;
/*  27 */     this.z = zIn;
/*  28 */     this.yaw = yawIn;
/*  29 */     this.pitch = pitchIn;
/*  30 */     this.field_179835_f = p_i45993_9_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  38 */     this.x = buf.readDouble();
/*  39 */     this.y = buf.readDouble();
/*  40 */     this.z = buf.readDouble();
/*  41 */     this.yaw = buf.readFloat();
/*  42 */     this.pitch = buf.readFloat();
/*  43 */     this.field_179835_f = EnumFlags.func_180053_a(buf.readUnsignedByte());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  51 */     buf.writeDouble(this.x);
/*  52 */     buf.writeDouble(this.y);
/*  53 */     buf.writeDouble(this.z);
/*  54 */     buf.writeFloat(this.yaw);
/*  55 */     buf.writeFloat(this.pitch);
/*  56 */     buf.writeByte(EnumFlags.func_180056_a(this.field_179835_f));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  64 */     handler.handlePlayerPosLook(this);
/*     */   }
/*     */   
/*     */   public double getX()
/*     */   {
/*  69 */     return this.x;
/*     */   }
/*     */   
/*     */   public double getY()
/*     */   {
/*  74 */     return this.y;
/*     */   }
/*     */   
/*     */   public double getZ()
/*     */   {
/*  79 */     return this.z;
/*     */   }
/*     */   
/*     */   public float getYaw()
/*     */   {
/*  84 */     return this.yaw;
/*     */   }
/*     */   
/*     */   public float getPitch()
/*     */   {
/*  89 */     return this.pitch;
/*     */   }
/*     */   
/*     */   public Set<EnumFlags> func_179834_f()
/*     */   {
/*  94 */     return this.field_179835_f;
/*     */   }
/*     */   
/*     */   public static enum EnumFlags
/*     */   {
/*  99 */     X(0), 
/* 100 */     Y(1), 
/* 101 */     Z(2), 
/* 102 */     Y_ROT(3), 
/* 103 */     X_ROT(4);
/*     */     
/*     */     private int field_180058_f;
/*     */     
/*     */     private EnumFlags(int p_i45992_3_)
/*     */     {
/* 109 */       this.field_180058_f = p_i45992_3_;
/*     */     }
/*     */     
/*     */     private int func_180055_a()
/*     */     {
/* 114 */       return 1 << this.field_180058_f;
/*     */     }
/*     */     
/*     */     private boolean func_180054_b(int p_180054_1_)
/*     */     {
/* 119 */       return (p_180054_1_ & func_180055_a()) == func_180055_a();
/*     */     }
/*     */     
/*     */     public static Set<EnumFlags> func_180053_a(int p_180053_0_)
/*     */     {
/* 124 */       Set<EnumFlags> set = EnumSet.noneOf(EnumFlags.class);
/*     */       EnumFlags[] arrayOfEnumFlags;
/* 126 */       int j = (arrayOfEnumFlags = values()).length; for (int i = 0; i < j; i++) { EnumFlags s08packetplayerposlook$enumflags = arrayOfEnumFlags[i];
/*     */         
/* 128 */         if (s08packetplayerposlook$enumflags.func_180054_b(p_180053_0_))
/*     */         {
/* 130 */           set.add(s08packetplayerposlook$enumflags);
/*     */         }
/*     */       }
/*     */       
/* 134 */       return set;
/*     */     }
/*     */     
/*     */     public static int func_180056_a(Set<EnumFlags> p_180056_0_)
/*     */     {
/* 139 */       int i = 0;
/*     */       
/* 141 */       for (EnumFlags s08packetplayerposlook$enumflags : p_180056_0_)
/*     */       {
/* 143 */         i |= s08packetplayerposlook$enumflags.func_180055_a();
/*     */       }
/*     */       
/* 146 */       return i;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S08PacketPlayerPosLook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */