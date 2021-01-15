/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class S35PacketUpdateTileEntity
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private BlockPos blockPos;
/*    */   private int metadata;
/*    */   private NBTTagCompound nbt;
/*    */   
/*    */   public S35PacketUpdateTileEntity() {}
/*    */   
/*    */   public S35PacketUpdateTileEntity(BlockPos blockPosIn, int metadataIn, NBTTagCompound nbtIn)
/*    */   {
/* 24 */     this.blockPos = blockPosIn;
/* 25 */     this.metadata = metadataIn;
/* 26 */     this.nbt = nbtIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     this.blockPos = buf.readBlockPos();
/* 35 */     this.metadata = buf.readUnsignedByte();
/* 36 */     this.nbt = buf.readNBTTagCompoundFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 44 */     buf.writeBlockPos(this.blockPos);
/* 45 */     buf.writeByte((byte)this.metadata);
/* 46 */     buf.writeNBTTagCompoundToBuffer(this.nbt);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 54 */     handler.handleUpdateTileEntity(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos()
/*    */   {
/* 59 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public int getTileEntityType()
/*    */   {
/* 64 */     return this.metadata;
/*    */   }
/*    */   
/*    */   public NBTTagCompound getNbtCompound()
/*    */   {
/* 69 */     return this.nbt;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S35PacketUpdateTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */