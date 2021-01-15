/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ 
/*     */ 
/*     */ public class S44PacketWorldBorder
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private Action action;
/*     */   private int size;
/*     */   private double centerX;
/*     */   private double centerZ;
/*     */   private double targetSize;
/*     */   private double diameter;
/*     */   private long timeUntilTarget;
/*     */   private int warningTime;
/*     */   private int warningDistance;
/*     */   
/*     */   public S44PacketWorldBorder() {}
/*     */   
/*     */   public S44PacketWorldBorder(WorldBorder border, Action actionIn)
/*     */   {
/*  27 */     this.action = actionIn;
/*  28 */     this.centerX = border.getCenterX();
/*  29 */     this.centerZ = border.getCenterZ();
/*  30 */     this.diameter = border.getDiameter();
/*  31 */     this.targetSize = border.getTargetSize();
/*  32 */     this.timeUntilTarget = border.getTimeUntilTarget();
/*  33 */     this.size = border.getSize();
/*  34 */     this.warningDistance = border.getWarningDistance();
/*  35 */     this.warningTime = border.getWarningTime();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  43 */     this.action = ((Action)buf.readEnumValue(Action.class));
/*     */     
/*  45 */     switch (this.action)
/*     */     {
/*     */     case INITIALIZE: 
/*  48 */       this.targetSize = buf.readDouble();
/*  49 */       break;
/*     */     
/*     */     case LERP_SIZE: 
/*  52 */       this.diameter = buf.readDouble();
/*  53 */       this.targetSize = buf.readDouble();
/*  54 */       this.timeUntilTarget = buf.readVarLong();
/*  55 */       break;
/*     */     
/*     */     case SET_CENTER: 
/*  58 */       this.centerX = buf.readDouble();
/*  59 */       this.centerZ = buf.readDouble();
/*  60 */       break;
/*     */     
/*     */     case SET_WARNING_TIME: 
/*  63 */       this.warningDistance = buf.readVarIntFromBuffer();
/*  64 */       break;
/*     */     
/*     */     case SET_WARNING_BLOCKS: 
/*  67 */       this.warningTime = buf.readVarIntFromBuffer();
/*  68 */       break;
/*     */     
/*     */     case SET_SIZE: 
/*  71 */       this.centerX = buf.readDouble();
/*  72 */       this.centerZ = buf.readDouble();
/*  73 */       this.diameter = buf.readDouble();
/*  74 */       this.targetSize = buf.readDouble();
/*  75 */       this.timeUntilTarget = buf.readVarLong();
/*  76 */       this.size = buf.readVarIntFromBuffer();
/*  77 */       this.warningDistance = buf.readVarIntFromBuffer();
/*  78 */       this.warningTime = buf.readVarIntFromBuffer();
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  87 */     buf.writeEnumValue(this.action);
/*     */     
/*  89 */     switch (this.action)
/*     */     {
/*     */     case INITIALIZE: 
/*  92 */       buf.writeDouble(this.targetSize);
/*  93 */       break;
/*     */     
/*     */     case LERP_SIZE: 
/*  96 */       buf.writeDouble(this.diameter);
/*  97 */       buf.writeDouble(this.targetSize);
/*  98 */       buf.writeVarLong(this.timeUntilTarget);
/*  99 */       break;
/*     */     
/*     */     case SET_CENTER: 
/* 102 */       buf.writeDouble(this.centerX);
/* 103 */       buf.writeDouble(this.centerZ);
/* 104 */       break;
/*     */     
/*     */     case SET_WARNING_TIME: 
/* 107 */       buf.writeVarIntToBuffer(this.warningDistance);
/* 108 */       break;
/*     */     
/*     */     case SET_WARNING_BLOCKS: 
/* 111 */       buf.writeVarIntToBuffer(this.warningTime);
/* 112 */       break;
/*     */     
/*     */     case SET_SIZE: 
/* 115 */       buf.writeDouble(this.centerX);
/* 116 */       buf.writeDouble(this.centerZ);
/* 117 */       buf.writeDouble(this.diameter);
/* 118 */       buf.writeDouble(this.targetSize);
/* 119 */       buf.writeVarLong(this.timeUntilTarget);
/* 120 */       buf.writeVarIntToBuffer(this.size);
/* 121 */       buf.writeVarIntToBuffer(this.warningDistance);
/* 122 */       buf.writeVarIntToBuffer(this.warningTime);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 131 */     handler.handleWorldBorder(this);
/*     */   }
/*     */   
/*     */   public void func_179788_a(WorldBorder border)
/*     */   {
/* 136 */     switch (this.action)
/*     */     {
/*     */     case INITIALIZE: 
/* 139 */       border.setTransition(this.targetSize);
/* 140 */       break;
/*     */     
/*     */     case LERP_SIZE: 
/* 143 */       border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
/* 144 */       break;
/*     */     
/*     */     case SET_CENTER: 
/* 147 */       border.setCenter(this.centerX, this.centerZ);
/* 148 */       break;
/*     */     
/*     */     case SET_WARNING_TIME: 
/* 151 */       border.setWarningDistance(this.warningDistance);
/* 152 */       break;
/*     */     
/*     */     case SET_WARNING_BLOCKS: 
/* 155 */       border.setWarningTime(this.warningTime);
/* 156 */       break;
/*     */     
/*     */     case SET_SIZE: 
/* 159 */       border.setCenter(this.centerX, this.centerZ);
/*     */       
/* 161 */       if (this.timeUntilTarget > 0L)
/*     */       {
/* 163 */         border.setTransition(this.diameter, this.targetSize, this.timeUntilTarget);
/*     */       }
/*     */       else
/*     */       {
/* 167 */         border.setTransition(this.targetSize);
/*     */       }
/*     */       
/* 170 */       border.setSize(this.size);
/* 171 */       border.setWarningDistance(this.warningDistance);
/* 172 */       border.setWarningTime(this.warningTime);
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum Action
/*     */   {
/* 178 */     SET_SIZE, 
/* 179 */     LERP_SIZE, 
/* 180 */     SET_CENTER, 
/* 181 */     INITIALIZE, 
/* 182 */     SET_WARNING_TIME, 
/* 183 */     SET_WARNING_BLOCKS;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S44PacketWorldBorder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */