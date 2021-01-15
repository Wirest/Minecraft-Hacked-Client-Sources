/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.Vec3;
/*     */ 
/*     */ 
/*     */ public class S27PacketExplosion
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private double posX;
/*     */   private double posY;
/*     */   private double posZ;
/*     */   private float strength;
/*     */   private List<BlockPos> affectedBlockPositions;
/*     */   public float field_149152_f;
/*     */   public float field_149153_g;
/*     */   public float field_149159_h;
/*     */   
/*     */   public S27PacketExplosion() {}
/*     */   
/*     */   public S27PacketExplosion(double p_i45193_1_, double y, double z, float strengthIn, List<BlockPos> affectedBlocksIn, Vec3 p_i45193_9_)
/*     */   {
/*  29 */     this.posX = p_i45193_1_;
/*  30 */     this.posY = y;
/*  31 */     this.posZ = z;
/*  32 */     this.strength = strengthIn;
/*  33 */     this.affectedBlockPositions = Lists.newArrayList(affectedBlocksIn);
/*     */     
/*  35 */     if (p_i45193_9_ != null)
/*     */     {
/*  37 */       this.field_149152_f = ((float)p_i45193_9_.xCoord);
/*  38 */       this.field_149153_g = ((float)p_i45193_9_.yCoord);
/*  39 */       this.field_149159_h = ((float)p_i45193_9_.zCoord);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  48 */     this.posX = buf.readFloat();
/*  49 */     this.posY = buf.readFloat();
/*  50 */     this.posZ = buf.readFloat();
/*  51 */     this.strength = buf.readFloat();
/*  52 */     int i = buf.readInt();
/*  53 */     this.affectedBlockPositions = Lists.newArrayListWithCapacity(i);
/*  54 */     int j = (int)this.posX;
/*  55 */     int k = (int)this.posY;
/*  56 */     int l = (int)this.posZ;
/*     */     
/*  58 */     for (int i1 = 0; i1 < i; i1++)
/*     */     {
/*  60 */       int j1 = buf.readByte() + j;
/*  61 */       int k1 = buf.readByte() + k;
/*  62 */       int l1 = buf.readByte() + l;
/*  63 */       this.affectedBlockPositions.add(new BlockPos(j1, k1, l1));
/*     */     }
/*     */     
/*  66 */     this.field_149152_f = buf.readFloat();
/*  67 */     this.field_149153_g = buf.readFloat();
/*  68 */     this.field_149159_h = buf.readFloat();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  76 */     buf.writeFloat((float)this.posX);
/*  77 */     buf.writeFloat((float)this.posY);
/*  78 */     buf.writeFloat((float)this.posZ);
/*  79 */     buf.writeFloat(this.strength);
/*  80 */     buf.writeInt(this.affectedBlockPositions.size());
/*  81 */     int i = (int)this.posX;
/*  82 */     int j = (int)this.posY;
/*  83 */     int k = (int)this.posZ;
/*     */     
/*  85 */     for (BlockPos blockpos : this.affectedBlockPositions)
/*     */     {
/*  87 */       int l = blockpos.getX() - i;
/*  88 */       int i1 = blockpos.getY() - j;
/*  89 */       int j1 = blockpos.getZ() - k;
/*  90 */       buf.writeByte(l);
/*  91 */       buf.writeByte(i1);
/*  92 */       buf.writeByte(j1);
/*     */     }
/*     */     
/*  95 */     buf.writeFloat(this.field_149152_f);
/*  96 */     buf.writeFloat(this.field_149153_g);
/*  97 */     buf.writeFloat(this.field_149159_h);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/* 105 */     handler.handleExplosion(this);
/*     */   }
/*     */   
/*     */   public float func_149149_c()
/*     */   {
/* 110 */     return this.field_149152_f;
/*     */   }
/*     */   
/*     */   public float func_149144_d()
/*     */   {
/* 115 */     return this.field_149153_g;
/*     */   }
/*     */   
/*     */   public float func_149147_e()
/*     */   {
/* 120 */     return this.field_149159_h;
/*     */   }
/*     */   
/*     */   public double getX()
/*     */   {
/* 125 */     return this.posX;
/*     */   }
/*     */   
/*     */   public double getY()
/*     */   {
/* 130 */     return this.posY;
/*     */   }
/*     */   
/*     */   public double getZ()
/*     */   {
/* 135 */     return this.posZ;
/*     */   }
/*     */   
/*     */   public float getStrength()
/*     */   {
/* 140 */     return this.strength;
/*     */   }
/*     */   
/*     */   public List<BlockPos> getAffectedBlockPositions()
/*     */   {
/* 145 */     return this.affectedBlockPositions;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S27PacketExplosion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */