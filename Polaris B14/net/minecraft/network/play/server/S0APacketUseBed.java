/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class S0APacketUseBed
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int playerID;
/*    */   private BlockPos bedPos;
/*    */   
/*    */   public S0APacketUseBed() {}
/*    */   
/*    */   public S0APacketUseBed(EntityPlayer player, BlockPos bedPosIn)
/*    */   {
/* 24 */     this.playerID = player.getEntityId();
/* 25 */     this.bedPos = bedPosIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 33 */     this.playerID = buf.readVarIntFromBuffer();
/* 34 */     this.bedPos = buf.readBlockPos();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 42 */     buf.writeVarIntToBuffer(this.playerID);
/* 43 */     buf.writeBlockPos(this.bedPos);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 51 */     handler.handleUseBed(this);
/*    */   }
/*    */   
/*    */   public EntityPlayer getPlayer(World worldIn)
/*    */   {
/* 56 */     return (EntityPlayer)worldIn.getEntityByID(this.playerID);
/*    */   }
/*    */   
/*    */   public BlockPos getBedPosition()
/*    */   {
/* 61 */     return this.bedPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S0APacketUseBed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */