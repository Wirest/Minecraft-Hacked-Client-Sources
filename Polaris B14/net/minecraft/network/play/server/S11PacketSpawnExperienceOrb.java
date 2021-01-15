/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.entity.item.EntityXPOrb;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ public class S11PacketSpawnExperienceOrb
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int entityID;
/*    */   private int posX;
/*    */   private int posY;
/*    */   private int posZ;
/*    */   private int xpValue;
/*    */   
/*    */   public S11PacketSpawnExperienceOrb() {}
/*    */   
/*    */   public S11PacketSpawnExperienceOrb(EntityXPOrb xpOrb)
/*    */   {
/* 24 */     this.entityID = xpOrb.getEntityId();
/* 25 */     this.posX = MathHelper.floor_double(xpOrb.posX * 32.0D);
/* 26 */     this.posY = MathHelper.floor_double(xpOrb.posY * 32.0D);
/* 27 */     this.posZ = MathHelper.floor_double(xpOrb.posZ * 32.0D);
/* 28 */     this.xpValue = xpOrb.getXpValue();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 36 */     this.entityID = buf.readVarIntFromBuffer();
/* 37 */     this.posX = buf.readInt();
/* 38 */     this.posY = buf.readInt();
/* 39 */     this.posZ = buf.readInt();
/* 40 */     this.xpValue = buf.readShort();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 48 */     buf.writeVarIntToBuffer(this.entityID);
/* 49 */     buf.writeInt(this.posX);
/* 50 */     buf.writeInt(this.posY);
/* 51 */     buf.writeInt(this.posZ);
/* 52 */     buf.writeShort(this.xpValue);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 60 */     handler.handleSpawnExperienceOrb(this);
/*    */   }
/*    */   
/*    */   public int getEntityID()
/*    */   {
/* 65 */     return this.entityID;
/*    */   }
/*    */   
/*    */   public int getX()
/*    */   {
/* 70 */     return this.posX;
/*    */   }
/*    */   
/*    */   public int getY()
/*    */   {
/* 75 */     return this.posY;
/*    */   }
/*    */   
/*    */   public int getZ()
/*    */   {
/* 80 */     return this.posZ;
/*    */   }
/*    */   
/*    */   public int getXPValue()
/*    */   {
/* 85 */     return this.xpValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S11PacketSpawnExperienceOrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */