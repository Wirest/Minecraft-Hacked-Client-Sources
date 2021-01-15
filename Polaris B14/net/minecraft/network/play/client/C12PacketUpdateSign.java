/*    */ package net.minecraft.network.play.client;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.util.IChatComponent.Serializer;
/*    */ 
/*    */ public class C12PacketUpdateSign
/*    */   implements Packet<INetHandlerPlayServer>
/*    */ {
/*    */   private BlockPos pos;
/*    */   private IChatComponent[] lines;
/*    */   
/*    */   public C12PacketUpdateSign() {}
/*    */   
/*    */   public C12PacketUpdateSign(BlockPos pos, IChatComponent[] lines)
/*    */   {
/* 21 */     this.pos = pos;
/* 22 */     this.lines = new IChatComponent[] { lines[0], lines[1], lines[2], lines[3] };
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 30 */     this.pos = buf.readBlockPos();
/* 31 */     this.lines = new IChatComponent[4];
/*    */     
/* 33 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 35 */       String s = buf.readStringFromBuffer(384);
/* 36 */       IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
/* 37 */       this.lines[i] = ichatcomponent;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 46 */     buf.writeBlockPos(this.pos);
/*    */     
/* 48 */     for (int i = 0; i < 4; i++)
/*    */     {
/* 50 */       IChatComponent ichatcomponent = this.lines[i];
/* 51 */       String s = IChatComponent.Serializer.componentToJson(ichatcomponent);
/* 52 */       buf.writeString(s);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayServer handler)
/*    */   {
/* 61 */     handler.processUpdateSign(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPosition()
/*    */   {
/* 66 */     return this.pos;
/*    */   }
/*    */   
/*    */   public IChatComponent[] getLines()
/*    */   {
/* 71 */     return this.lines;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\client\C12PacketUpdateSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */