/*     */ package net.minecraft.network.play.server;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.INetHandlerPlayClient;
/*     */ import net.minecraft.scoreboard.Score;
/*     */ import net.minecraft.scoreboard.ScoreObjective;
/*     */ 
/*     */ public class S3CPacketUpdateScore implements Packet<INetHandlerPlayClient>
/*     */ {
/*  12 */   private String name = "";
/*  13 */   private String objective = "";
/*     */   
/*     */   private int value;
/*     */   
/*     */   private Action action;
/*     */   
/*     */   public S3CPacketUpdateScore() {}
/*     */   
/*     */   public S3CPacketUpdateScore(Score scoreIn)
/*     */   {
/*  23 */     this.name = scoreIn.getPlayerName();
/*  24 */     this.objective = scoreIn.getObjective().getName();
/*  25 */     this.value = scoreIn.getScorePoints();
/*  26 */     this.action = Action.CHANGE;
/*     */   }
/*     */   
/*     */   public S3CPacketUpdateScore(String nameIn)
/*     */   {
/*  31 */     this.name = nameIn;
/*  32 */     this.objective = "";
/*  33 */     this.value = 0;
/*  34 */     this.action = Action.REMOVE;
/*     */   }
/*     */   
/*     */   public S3CPacketUpdateScore(String nameIn, ScoreObjective objectiveIn)
/*     */   {
/*  39 */     this.name = nameIn;
/*  40 */     this.objective = objectiveIn.getName();
/*  41 */     this.value = 0;
/*  42 */     this.action = Action.REMOVE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void readPacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  50 */     this.name = buf.readStringFromBuffer(40);
/*  51 */     this.action = ((Action)buf.readEnumValue(Action.class));
/*  52 */     this.objective = buf.readStringFromBuffer(16);
/*     */     
/*  54 */     if (this.action != Action.REMOVE)
/*     */     {
/*  56 */       this.value = buf.readVarIntFromBuffer();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void writePacketData(PacketBuffer buf)
/*     */     throws IOException
/*     */   {
/*  65 */     buf.writeString(this.name);
/*  66 */     buf.writeEnumValue(this.action);
/*  67 */     buf.writeString(this.objective);
/*     */     
/*  69 */     if (this.action != Action.REMOVE)
/*     */     {
/*  71 */       buf.writeVarIntToBuffer(this.value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void processPacket(INetHandlerPlayClient handler)
/*     */   {
/*  80 */     handler.handleUpdateScore(this);
/*     */   }
/*     */   
/*     */   public String getPlayerName()
/*     */   {
/*  85 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getObjectiveName()
/*     */   {
/*  90 */     return this.objective;
/*     */   }
/*     */   
/*     */   public int getScoreValue()
/*     */   {
/*  95 */     return this.value;
/*     */   }
/*     */   
/*     */   public Action getScoreAction()
/*     */   {
/* 100 */     return this.action;
/*     */   }
/*     */   
/*     */   public static enum Action
/*     */   {
/* 105 */     CHANGE, 
/* 106 */     REMOVE;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\play\server\S3CPacketUpdateScore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */