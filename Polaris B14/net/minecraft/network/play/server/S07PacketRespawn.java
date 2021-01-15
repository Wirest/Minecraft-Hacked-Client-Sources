/*    */ package net.minecraft.network.play.server;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ import net.minecraft.network.play.INetHandlerPlayClient;
/*    */ import net.minecraft.world.EnumDifficulty;
/*    */ import net.minecraft.world.WorldSettings.GameType;
/*    */ import net.minecraft.world.WorldType;
/*    */ 
/*    */ 
/*    */ public class S07PacketRespawn
/*    */   implements Packet<INetHandlerPlayClient>
/*    */ {
/*    */   private int dimensionID;
/*    */   private EnumDifficulty difficulty;
/*    */   private WorldSettings.GameType gameType;
/*    */   private WorldType worldType;
/*    */   
/*    */   public S07PacketRespawn() {}
/*    */   
/*    */   public S07PacketRespawn(int dimensionIDIn, EnumDifficulty difficultyIn, WorldType worldTypeIn, WorldSettings.GameType gameTypeIn)
/*    */   {
/* 24 */     this.dimensionID = dimensionIDIn;
/* 25 */     this.difficulty = difficultyIn;
/* 26 */     this.gameType = gameTypeIn;
/* 27 */     this.worldType = worldTypeIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void processPacket(INetHandlerPlayClient handler)
/*    */   {
/* 35 */     handler.handleRespawn(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void readPacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 43 */     this.dimensionID = buf.readInt();
/* 44 */     this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
/* 45 */     this.gameType = WorldSettings.GameType.getByID(buf.readUnsignedByte());
/* 46 */     this.worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));
/*    */     
/* 48 */     if (this.worldType == null)
/*    */     {
/* 50 */       this.worldType = WorldType.DEFAULT;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void writePacketData(PacketBuffer buf)
/*    */     throws IOException
/*    */   {
/* 59 */     buf.writeInt(this.dimensionID);
/* 60 */     buf.writeByte(this.difficulty.getDifficultyId());
/* 61 */     buf.writeByte(this.gameType.getID());
/* 62 */     buf.writeString(this.worldType.getWorldTypeName());
/*    */   }
/*    */   
/*    */   public int getDimensionID()
/*    */   {
/* 67 */     return this.dimensionID;
/*    */   }
/*    */   
/*    */   public EnumDifficulty getDifficulty()
/*    */   {
/* 72 */     return this.difficulty;
/*    */   }
/*    */   
/*    */   public WorldSettings.GameType getGameType()
/*    */   {
/* 77 */     return this.gameType;
/*    */   }
/*    */   
/*    */   public WorldType getWorldType()
/*    */   {
/* 82 */     return this.worldType;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S07PacketRespawn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */