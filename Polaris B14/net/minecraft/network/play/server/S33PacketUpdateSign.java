/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ public class S33PacketUpdateSign
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private World world;
/*    */   private BlockPos blockPos;
/*    */   private IChatComponent[] lines;
/*    */   
/*    */   public S33PacketUpdateSign() {}
/*    */   
/*    */   public S33PacketUpdateSign(World worldIn, BlockPos blockPosIn, IChatComponent[] linesIn)
/*    */   {
/* 23 */     this.world = worldIn;
/* 24 */     this.blockPos = blockPosIn;
/* 25 */     this.lines = new IChatComponent[] { linesIn[0], linesIn[1], linesIn[2], linesIn[3] };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 33 */     this.blockPos = buf.readBlockPos();
/* 34 */     this.lines = new IChatComponent[4];
/*    */     
/* 36 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 38 */       this.lines[i] = buf.readChatComponent();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 47 */     buf.writeBlockPos(this.blockPos);
/*    */     
/* 49 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 51 */       buf.writeChatComponent(this.lines[i]);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 60 */     handler.handleUpdateSign(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos()
/*    */   {
/* 65 */     return this.blockPos;
/*    */   }
/*    */   
/*    */   public IChatComponent[] getLines()
/*    */   {
/* 70 */     return this.lines;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S33PacketUpdateSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */