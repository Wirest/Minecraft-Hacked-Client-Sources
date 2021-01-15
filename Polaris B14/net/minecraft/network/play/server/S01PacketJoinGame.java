/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.WorldSettings.GameType;
/*     */ import net.minecraft.world.WorldType;
/*     */ 
/*     */ 
/*     */ public class S01PacketJoinGame
/*     */   implements Packet<INetHandlerPlayClient>
/*     */ {
/*     */   private int entityId;
/*     */   private boolean hardcoreMode;
/*     */   private WorldSettings.GameType gameType;
/*     */   private int dimension;
/*     */   private EnumDifficulty difficulty;
/*     */   private int maxPlayers;
/*     */   private WorldType worldType;
/*     */   private boolean reducedDebugInfo;
/*     */   
/*     */   public S01PacketJoinGame() {}
/*     */   
/*     */   public S01PacketJoinGame(int entityIdIn, WorldSettings.GameType gameTypeIn, boolean hardcoreModeIn, int dimensionIn, EnumDifficulty difficultyIn, int maxPlayersIn, WorldType worldTypeIn, boolean reducedDebugInfoIn)
/*     */   {
/*  28 */     this.entityId = entityIdIn;
/*  29 */     this.dimension = dimensionIn;
/*  30 */     this.difficulty = difficultyIn;
/*  31 */     this.gameType = gameTypeIn;
/*  32 */     this.maxPlayers = maxPlayersIn;
/*  33 */     this.hardcoreMode = hardcoreModeIn;
/*  34 */     this.worldType = worldTypeIn;
/*  35 */     this.reducedDebugInfo = reducedDebugInfoIn;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  43 */     this.entityId = buf.readInt();
/*  44 */     int i = buf.readUnsignedByte();
/*  45 */     this.hardcoreMode = ((i & 0x8) == 8);
/*  46 */     i &= 0xFFFFFFF7;
/*  47 */     this.gameType = WorldSettings.GameType.getByID(i);
/*  48 */     this.dimension = buf.readByte();
/*  49 */     this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
/*  50 */     this.maxPlayers = buf.readUnsignedByte();
/*  51 */     this.worldType = WorldType.parseWorldType(buf.readStringFromBuffer(16));
/*     */     
/*  53 */     if (this.worldType == null)
/*     */     {
/*  55 */       this.worldType = WorldType.DEFAULT;
/*     */     }
/*     */     
/*  58 */     this.reducedDebugInfo = buf.readBoolean();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  66 */     buf.writeInt(this.entityId);
/*  67 */     int i = this.gameType.getID();
/*     */     
/*  69 */     if (this.hardcoreMode)
/*     */     {
/*  71 */       i |= 0x8;
/*     */     }
/*     */     
/*  74 */     buf.writeByte(i);
/*  75 */     buf.writeByte(this.dimension);
/*  76 */     buf.writeByte(this.difficulty.getDifficultyId());
/*  77 */     buf.writeByte(this.maxPlayers);
/*  78 */     buf.writeString(this.worldType.getWorldTypeName());
/*  79 */     buf.writeBoolean(this.reducedDebugInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  87 */     handler.handleJoinGame(this);
/*     */   }
/*     */   
/*     */   public int getEntityId()
/*     */   {
/*  92 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public boolean isHardcoreMode()
/*     */   {
/*  97 */     return this.hardcoreMode;
/*     */   }
/*     */   
/*     */   public WorldSettings.GameType getGameType()
/*     */   {
/* 102 */     return this.gameType;
/*     */   }
/*     */   
/*     */   public int getDimension()
/*     */   {
/* 107 */     return this.dimension;
/*     */   }
/*     */   
/*     */   public EnumDifficulty getDifficulty()
/*     */   {
/* 112 */     return this.difficulty;
/*     */   }
/*     */   
/*     */   public int getMaxPlayers()
/*     */   {
/* 117 */     return this.maxPlayers;
/*     */   }
/*     */   
/*     */   public WorldType getWorldType()
/*     */   {
/* 122 */     return this.worldType;
/*     */   }
/*     */   
/*     */   public boolean isReducedDebugInfo()
/*     */   {
/* 127 */     return this.reducedDebugInfo;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S01PacketJoinGame.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */