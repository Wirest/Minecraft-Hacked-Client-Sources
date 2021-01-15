/*     */ package net.minecraft.scoreboard;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S3BPacketScoreboardObjective;
/*     */ import net.minecraft.network.play.server.S3CPacketUpdateScore;
/*     */ import net.minecraft.network.play.server.S3DPacketDisplayScoreboard;
/*     */ import net.minecraft.network.play.server.S3EPacketTeams;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ 
/*     */ public class ServerScoreboard extends Scoreboard
/*     */ {
/*     */   private final MinecraftServer scoreboardMCServer;
/*  19 */   private final Set<ScoreObjective> field_96553_b = com.google.common.collect.Sets.newHashSet();
/*     */   private ScoreboardSaveData scoreboardSaveData;
/*     */   
/*     */   public ServerScoreboard(MinecraftServer mcServer)
/*     */   {
/*  24 */     this.scoreboardMCServer = mcServer;
/*     */   }
/*     */   
/*     */   public void func_96536_a(Score p_96536_1_)
/*     */   {
/*  29 */     super.func_96536_a(p_96536_1_);
/*     */     
/*  31 */     if (this.field_96553_b.contains(p_96536_1_.getObjective()))
/*     */     {
/*  33 */       this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(p_96536_1_));
/*     */     }
/*     */     
/*  36 */     func_96551_b();
/*     */   }
/*     */   
/*     */   public void func_96516_a(String p_96516_1_)
/*     */   {
/*  41 */     super.func_96516_a(p_96516_1_);
/*  42 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(p_96516_1_));
/*  43 */     func_96551_b();
/*     */   }
/*     */   
/*     */   public void func_178820_a(String p_178820_1_, ScoreObjective p_178820_2_)
/*     */   {
/*  48 */     super.func_178820_a(p_178820_1_, p_178820_2_);
/*  49 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3CPacketUpdateScore(p_178820_1_, p_178820_2_));
/*  50 */     func_96551_b();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setObjectiveInDisplaySlot(int p_96530_1_, ScoreObjective p_96530_2_)
/*     */   {
/*  58 */     ScoreObjective scoreobjective = getObjectiveInDisplaySlot(p_96530_1_);
/*  59 */     super.setObjectiveInDisplaySlot(p_96530_1_, p_96530_2_);
/*     */     
/*  61 */     if ((scoreobjective != p_96530_2_) && (scoreobjective != null))
/*     */     {
/*  63 */       if (func_96552_h(scoreobjective) > 0)
/*     */       {
/*  65 */         this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(p_96530_1_, p_96530_2_));
/*     */       }
/*     */       else
/*     */       {
/*  69 */         getPlayerIterator(scoreobjective);
/*     */       }
/*     */     }
/*     */     
/*  73 */     if (p_96530_2_ != null)
/*     */     {
/*  75 */       if (this.field_96553_b.contains(p_96530_2_))
/*     */       {
/*  77 */         this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3DPacketDisplayScoreboard(p_96530_1_, p_96530_2_));
/*     */       }
/*     */       else
/*     */       {
/*  81 */         func_96549_e(p_96530_2_);
/*     */       }
/*     */     }
/*     */     
/*  85 */     func_96551_b();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean addPlayerToTeam(String player, String newTeam)
/*     */   {
/*  93 */     if (super.addPlayerToTeam(player, newTeam))
/*     */     {
/*  95 */       ScorePlayerTeam scoreplayerteam = getTeam(newTeam);
/*  96 */       this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(scoreplayerteam, java.util.Arrays.asList(new String[] { player }), 3));
/*  97 */       func_96551_b();
/*  98 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 102 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePlayerFromTeam(String p_96512_1_, ScorePlayerTeam p_96512_2_)
/*     */   {
/* 112 */     super.removePlayerFromTeam(p_96512_1_, p_96512_2_);
/* 113 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(p_96512_2_, java.util.Arrays.asList(new String[] { p_96512_1_ }), 4));
/* 114 */     func_96551_b();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onScoreObjectiveAdded(ScoreObjective scoreObjectiveIn)
/*     */   {
/* 122 */     super.onScoreObjectiveAdded(scoreObjectiveIn);
/* 123 */     func_96551_b();
/*     */   }
/*     */   
/*     */   public void func_96532_b(ScoreObjective p_96532_1_)
/*     */   {
/* 128 */     super.func_96532_b(p_96532_1_);
/*     */     
/* 130 */     if (this.field_96553_b.contains(p_96532_1_))
/*     */     {
/* 132 */       this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3BPacketScoreboardObjective(p_96532_1_, 2));
/*     */     }
/*     */     
/* 135 */     func_96551_b();
/*     */   }
/*     */   
/*     */   public void func_96533_c(ScoreObjective p_96533_1_)
/*     */   {
/* 140 */     super.func_96533_c(p_96533_1_);
/*     */     
/* 142 */     if (this.field_96553_b.contains(p_96533_1_))
/*     */     {
/* 144 */       getPlayerIterator(p_96533_1_);
/*     */     }
/*     */     
/* 147 */     func_96551_b();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void broadcastTeamCreated(ScorePlayerTeam playerTeam)
/*     */   {
/* 155 */     super.broadcastTeamCreated(playerTeam);
/* 156 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(playerTeam, 0));
/* 157 */     func_96551_b();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void sendTeamUpdate(ScorePlayerTeam playerTeam)
/*     */   {
/* 165 */     super.sendTeamUpdate(playerTeam);
/* 166 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(playerTeam, 2));
/* 167 */     func_96551_b();
/*     */   }
/*     */   
/*     */   public void func_96513_c(ScorePlayerTeam playerTeam)
/*     */   {
/* 172 */     super.func_96513_c(playerTeam);
/* 173 */     this.scoreboardMCServer.getConfigurationManager().sendPacketToAllPlayers(new S3EPacketTeams(playerTeam, 1));
/* 174 */     func_96551_b();
/*     */   }
/*     */   
/*     */   public void func_96547_a(ScoreboardSaveData p_96547_1_)
/*     */   {
/* 179 */     this.scoreboardSaveData = p_96547_1_;
/*     */   }
/*     */   
/*     */   protected void func_96551_b()
/*     */   {
/* 184 */     if (this.scoreboardSaveData != null)
/*     */     {
/* 186 */       this.scoreboardSaveData.markDirty();
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Packet> func_96550_d(ScoreObjective p_96550_1_)
/*     */   {
/* 192 */     List<Packet> list = com.google.common.collect.Lists.newArrayList();
/* 193 */     list.add(new S3BPacketScoreboardObjective(p_96550_1_, 0));
/*     */     
/* 195 */     for (int i = 0; i < 19; i++)
/*     */     {
/* 197 */       if (getObjectiveInDisplaySlot(i) == p_96550_1_)
/*     */       {
/* 199 */         list.add(new S3DPacketDisplayScoreboard(i, p_96550_1_));
/*     */       }
/*     */     }
/*     */     
/* 203 */     for (Score score : getSortedScores(p_96550_1_))
/*     */     {
/* 205 */       list.add(new S3CPacketUpdateScore(score));
/*     */     }
/*     */     
/* 208 */     return list;
/*     */   }
/*     */   
/*     */   public void func_96549_e(ScoreObjective p_96549_1_)
/*     */   {
/* 213 */     List<Packet> list = func_96550_d(p_96549_1_);
/*     */     Iterator localIterator2;
/* 215 */     for (Iterator localIterator1 = this.scoreboardMCServer.getConfigurationManager().func_181057_v().iterator(); localIterator1.hasNext(); 
/*     */         
/* 217 */         localIterator2.hasNext())
/*     */     {
/* 215 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)localIterator1.next();
/*     */       
/* 217 */       localIterator2 = list.iterator(); continue;Packet packet = (Packet)localIterator2.next();
/*     */       
/* 219 */       entityplayermp.playerNetServerHandler.sendPacket(packet);
/*     */     }
/*     */     
/*     */ 
/* 223 */     this.field_96553_b.add(p_96549_1_);
/*     */   }
/*     */   
/*     */   public List<Packet> func_96548_f(ScoreObjective p_96548_1_)
/*     */   {
/* 228 */     List<Packet> list = com.google.common.collect.Lists.newArrayList();
/* 229 */     list.add(new S3BPacketScoreboardObjective(p_96548_1_, 1));
/*     */     
/* 231 */     for (int i = 0; i < 19; i++)
/*     */     {
/* 233 */       if (getObjectiveInDisplaySlot(i) == p_96548_1_)
/*     */       {
/* 235 */         list.add(new S3DPacketDisplayScoreboard(i, p_96548_1_));
/*     */       }
/*     */     }
/*     */     
/* 239 */     return list;
/*     */   }
/*     */   
/*     */   public void getPlayerIterator(ScoreObjective p_96546_1_)
/*     */   {
/* 244 */     List<Packet> list = func_96548_f(p_96546_1_);
/*     */     Iterator localIterator2;
/* 246 */     for (Iterator localIterator1 = this.scoreboardMCServer.getConfigurationManager().func_181057_v().iterator(); localIterator1.hasNext(); 
/*     */         
/* 248 */         localIterator2.hasNext())
/*     */     {
/* 246 */       EntityPlayerMP entityplayermp = (EntityPlayerMP)localIterator1.next();
/*     */       
/* 248 */       localIterator2 = list.iterator(); continue;Packet packet = (Packet)localIterator2.next();
/*     */       
/* 250 */       entityplayermp.playerNetServerHandler.sendPacket(packet);
/*     */     }
/*     */     
/*     */ 
/* 254 */     this.field_96553_b.remove(p_96546_1_);
/*     */   }
/*     */   
/*     */   public int func_96552_h(ScoreObjective p_96552_1_)
/*     */   {
/* 259 */     int i = 0;
/*     */     
/* 261 */     for (int j = 0; j < 19; j++)
/*     */     {
/* 263 */       if (getObjectiveInDisplaySlot(j) == p_96552_1_)
/*     */       {
/* 265 */         i++;
/*     */       }
/*     */     }
/*     */     
/* 269 */     return i;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\scoreboard\ServerScoreboard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */