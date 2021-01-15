/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S1FPacketSetExperience
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private float field_149401_a;
/*    */   private int totalExperience;
/*    */   private int level;
/*    */   
/*    */   public S1FPacketSetExperience() {}
/*    */   
/*    */   public S1FPacketSetExperience(float p_i45222_1_, int totalExperienceIn, int levelIn)
/*    */   {
/* 20 */     this.field_149401_a = p_i45222_1_;
/* 21 */     this.totalExperience = totalExperienceIn;
/* 22 */     this.level = levelIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 30 */     this.field_149401_a = buf.readFloat();
/* 31 */     this.level = buf.readVarIntFromBuffer();
/* 32 */     this.totalExperience = buf.readVarIntFromBuffer();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 40 */     buf.writeFloat(this.field_149401_a);
/* 41 */     buf.writeVarIntToBuffer(this.level);
/* 42 */     buf.writeVarIntToBuffer(this.totalExperience);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 50 */     handler.handleSetExperience(this);
/*    */   }
/*    */   
/*    */   public float func_149397_c()
/*    */   {
/* 55 */     return this.field_149401_a;
/*    */   }
/*    */   
/*    */   public int getTotalExperience()
/*    */   {
/* 60 */     return this.totalExperience;
/*    */   }
/*    */   
/*    */   public int getLevel()
/*    */   {
/* 65 */     return this.level;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S1FPacketSetExperience.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */