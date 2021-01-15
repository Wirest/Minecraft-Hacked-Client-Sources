/*     */ package net.minecraft.network.play.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class C08PacketPlayerBlockPlacement implements Packet<INetHandlerPlayServer>
/*     */ {
/*  12 */   private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
/*     */   
/*     */   private BlockPos position;
/*     */   
/*     */   private int placedBlockDirection;
/*     */   private ItemStack stack;
/*     */   private float facingX;
/*     */   private float facingY;
/*     */   private float facingZ;
/*     */   
/*     */   public C08PacketPlayerBlockPlacement() {}
/*     */   
/*     */   public C08PacketPlayerBlockPlacement(ItemStack stackIn)
/*     */   {
/*  26 */     this(field_179726_a, 255, stackIn, 0.0F, 0.0F, 0.0F);
/*     */   }
/*     */   
/*     */   public C08PacketPlayerBlockPlacement(BlockPos positionIn, int placedBlockDirectionIn, ItemStack stackIn, float facingXIn, float facingYIn, float facingZIn)
/*     */   {
/*  31 */     this.position = positionIn;
/*  32 */     this.placedBlockDirection = placedBlockDirectionIn;
/*  33 */     this.stack = (stackIn != null ? stackIn.copy() : null);
/*  34 */     this.facingX = facingXIn;
/*  35 */     this.facingY = facingYIn;
/*  36 */     this.facingZ = facingZIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  44 */     this.position = buf.readBlockPos();
/*  45 */     this.placedBlockDirection = buf.readUnsignedByte();
/*  46 */     this.stack = buf.readItemStackFromBuffer();
/*  47 */     this.facingX = (buf.readUnsignedByte() / 16.0F);
/*  48 */     this.facingY = (buf.readUnsignedByte() / 16.0F);
/*  49 */     this.facingZ = (buf.readUnsignedByte() / 16.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  57 */     buf.writeBlockPos(this.position);
/*  58 */     buf.writeByte(this.placedBlockDirection);
/*  59 */     buf.writeItemStackToBuffer(this.stack);
/*  60 */     buf.writeByte((int)(this.facingX * 16.0F));
/*  61 */     buf.writeByte((int)(this.facingY * 16.0F));
/*  62 */     buf.writeByte((int)(this.facingZ * 16.0F));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayServer handler)
/*     */   {
/*  70 */     handler.processPlayerBlockPlacement(this);
/*     */   }
/*     */   
/*     */   public BlockPos getPosition()
/*     */   {
/*  75 */     return this.position;
/*     */   }
/*     */   
/*     */   public int getPlacedBlockDirection()
/*     */   {
/*  80 */     return this.placedBlockDirection;
/*     */   }
/*     */   
/*     */   public ItemStack getStack()
/*     */   {
/*  85 */     return this.stack;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getPlacedBlockOffsetX()
/*     */   {
/*  93 */     return this.facingX;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getPlacedBlockOffsetY()
/*     */   {
/* 101 */     return this.facingY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public float getPlacedBlockOffsetZ()
/*     */   {
/* 109 */     return this.facingZ;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C08PacketPlayerBlockPlacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */