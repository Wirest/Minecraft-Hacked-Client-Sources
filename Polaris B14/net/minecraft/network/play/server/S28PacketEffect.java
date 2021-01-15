/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class S28PacketEffect
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int soundType;
/*    */   private BlockPos soundPos;
/*    */   private int soundData;
/*    */   private boolean serverWide;
/*    */   
/*    */   public S28PacketEffect() {}
/*    */   
/*    */   public S28PacketEffect(int soundTypeIn, BlockPos soundPosIn, int soundDataIn, boolean serverWideIn)
/*    */   {
/* 26 */     this.soundType = soundTypeIn;
/* 27 */     this.soundPos = soundPosIn;
/* 28 */     this.soundData = soundDataIn;
/* 29 */     this.serverWide = serverWideIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 37 */     this.soundType = buf.readInt();
/* 38 */     this.soundPos = buf.readBlockPos();
/* 39 */     this.soundData = buf.readInt();
/* 40 */     this.serverWide = buf.readBoolean();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 48 */     buf.writeInt(this.soundType);
/* 49 */     buf.writeBlockPos(this.soundPos);
/* 50 */     buf.writeInt(this.soundData);
/* 51 */     buf.writeBoolean(this.serverWide);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 59 */     handler.handleEffect(this);
/*    */   }
/*    */   
/*    */   public boolean isSoundServerwide()
/*    */   {
/* 64 */     return this.serverWide;
/*    */   }
/*    */   
/*    */   public int getSoundType()
/*    */   {
/* 69 */     return this.soundType;
/*    */   }
/*    */   
/*    */   public int getSoundData()
/*    */   {
/* 74 */     return this.soundData;
/*    */   }
/*    */   
/*    */   public BlockPos getSoundPos()
/*    */   {
/* 79 */     return this.soundPos;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S28PacketEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */