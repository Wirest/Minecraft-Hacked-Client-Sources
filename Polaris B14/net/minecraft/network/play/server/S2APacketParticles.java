/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class S2APacketParticles
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private EnumParticleTypes particleType;
/*     */   private float xCoord;
/*     */   private float yCoord;
/*     */   private float zCoord;
/*     */   private float xOffset;
/*     */   private float yOffset;
/*     */   private float zOffset;
/*     */   private float particleSpeed;
/*     */   private int particleCount;
/*     */   private boolean longDistance;
/*     */   private int[] particleArguments;
/*     */   
/*     */   public S2APacketParticles() {}
/*     */   
/*     */   public S2APacketParticles(EnumParticleTypes particleTypeIn, boolean longDistanceIn, float x, float y, float z, float xOffsetIn, float yOffset, float zOffset, float particleSpeedIn, int particleCountIn, int... particleArgumentsIn)
/*     */   {
/*  33 */     this.particleType = particleTypeIn;
/*  34 */     this.longDistance = longDistanceIn;
/*  35 */     this.xCoord = x;
/*  36 */     this.yCoord = y;
/*  37 */     this.zCoord = z;
/*  38 */     this.xOffset = xOffsetIn;
/*  39 */     this.yOffset = yOffset;
/*  40 */     this.zOffset = zOffset;
/*  41 */     this.particleSpeed = particleSpeedIn;
/*  42 */     this.particleCount = particleCountIn;
/*  43 */     this.particleArguments = particleArgumentsIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  51 */     this.particleType = EnumParticleTypes.getParticleFromId(buf.readInt());
/*     */     
/*  53 */     if (this.particleType == null)
/*     */     {
/*  55 */       this.particleType = EnumParticleTypes.BARRIER;
/*     */     }
/*     */     
/*  58 */     this.longDistance = buf.readBoolean();
/*  59 */     this.xCoord = buf.readFloat();
/*  60 */     this.yCoord = buf.readFloat();
/*  61 */     this.zCoord = buf.readFloat();
/*  62 */     this.xOffset = buf.readFloat();
/*  63 */     this.yOffset = buf.readFloat();
/*  64 */     this.zOffset = buf.readFloat();
/*  65 */     this.particleSpeed = buf.readFloat();
/*  66 */     this.particleCount = buf.readInt();
/*  67 */     int i = this.particleType.getArgumentCount();
/*  68 */     this.particleArguments = new int[i];
/*     */     
/*  70 */     for (int j = 0; j < i; j++)
/*     */     {
/*  72 */       this.particleArguments[j] = buf.readVarIntFromBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  81 */     buf.writeInt(this.particleType.getParticleID());
/*  82 */     buf.writeBoolean(this.longDistance);
/*  83 */     buf.writeFloat(this.xCoord);
/*  84 */     buf.writeFloat(this.yCoord);
/*  85 */     buf.writeFloat(this.zCoord);
/*  86 */     buf.writeFloat(this.xOffset);
/*  87 */     buf.writeFloat(this.yOffset);
/*  88 */     buf.writeFloat(this.zOffset);
/*  89 */     buf.writeFloat(this.particleSpeed);
/*  90 */     buf.writeInt(this.particleCount);
/*  91 */     int i = this.particleType.getArgumentCount();
/*     */     
/*  93 */     for (int j = 0; j < i; j++)
/*     */     {
/*  95 */       buf.writeVarIntToBuffer(this.particleArguments[j]);
/*     */     }
/*     */   }
/*     */   
/*     */   public EnumParticleTypes getParticleType()
/*     */   {
/* 101 */     return this.particleType;
/*     */   }
/*     */   
/*     */   public boolean isLongDistance()
/*     */   {
/* 106 */     return this.longDistance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getXCoordinate()
/*     */   {
/* 114 */     return this.xCoord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getYCoordinate()
/*     */   {
/* 122 */     return this.yCoord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getZCoordinate()
/*     */   {
/* 130 */     return this.zCoord;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getXOffset()
/*     */   {
/* 138 */     return this.xOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getYOffset()
/*     */   {
/* 146 */     return this.yOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getZOffset()
/*     */   {
/* 154 */     return this.zOffset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getParticleSpeed()
/*     */   {
/* 162 */     return this.particleSpeed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getParticleCount()
/*     */   {
/* 170 */     return this.particleCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getParticleArgs()
/*     */   {
/* 179 */     return this.particleArguments;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 187 */     handler.handleParticles(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S2APacketParticles.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */