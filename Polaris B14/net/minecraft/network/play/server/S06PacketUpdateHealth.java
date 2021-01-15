/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ 
/*    */ 
/*    */ public class S06PacketUpdateHealth
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private float health;
/*    */   private int foodLevel;
/*    */   private float saturationLevel;
/*    */   
/*    */   public S06PacketUpdateHealth() {}
/*    */   
/*    */   public S06PacketUpdateHealth(float healthIn, int foodLevelIn, float saturationIn)
/*    */   {
/* 20 */     this.health = healthIn;
/* 21 */     this.foodLevel = foodLevelIn;
/* 22 */     this.saturationLevel = saturationIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 30 */     this.health = buf.readFloat();
/* 31 */     this.foodLevel = buf.readVarIntFromBuffer();
/* 32 */     this.saturationLevel = buf.readFloat();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 40 */     buf.writeFloat(this.health);
/* 41 */     buf.writeVarIntToBuffer(this.foodLevel);
/* 42 */     buf.writeFloat(this.saturationLevel);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 50 */     handler.handleUpdateHealth(this);
/*    */   }
/*    */   
/*    */   public float getHealth()
/*    */   {
/* 55 */     return this.health;
/*    */   }
/*    */   
/*    */   public int getFoodLevel()
/*    */   {
/* 60 */     return this.foodLevel;
/*    */   }
/*    */   
/*    */   public float getSaturationLevel()
/*    */   {
/* 65 */     return this.saturationLevel;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S06PacketUpdateHealth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */