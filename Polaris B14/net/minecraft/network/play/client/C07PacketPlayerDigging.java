/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class C07PacketPlayerDigging
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private BlockPos position;
/*    */   private EnumFacing facing;
/*    */   private Action status;
/*    */   
/*    */   public C07PacketPlayerDigging() {}
/*    */   
/*    */   public C07PacketPlayerDigging(Action statusIn, BlockPos posIn, EnumFacing facingIn)
/*    */   {
/* 24 */     this.status = statusIn;
/* 25 */     this.position = posIn;
/* 26 */     this.facing = facingIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 34 */     this.status = ((Action)buf.readEnumValue(Action.class));
/* 35 */     this.position = buf.readBlockPos();
/* 36 */     this.facing = EnumFacing.getFront(buf.readUnsignedByte());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 44 */     buf.writeEnumValue(this.status);
/* 45 */     buf.writeBlockPos(this.position);
/* 46 */     buf.writeByte(this.facing.getIndex());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 54 */     handler.processPlayerDigging(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPosition()
/*    */   {
/* 59 */     return this.position;
/*    */   }
/*    */   
/*    */   public EnumFacing getFacing()
/*    */   {
/* 64 */     return this.facing;
/*    */   }
/*    */   
/*    */   public Action getStatus()
/*    */   {
/* 69 */     return this.status;
/*    */   }
/*    */   
/*    */   public static enum Action
/*    */   {
/* 74 */     START_DESTROY_BLOCK, 
/* 75 */     ABORT_DESTROY_BLOCK, 
/* 76 */     STOP_DESTROY_BLOCK, 
/* 77 */     DROP_ALL_ITEMS, 
/* 78 */     DROP_ITEM, 
/* 79 */     RELEASE_USE_ITEM;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C07PacketPlayerDigging.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */