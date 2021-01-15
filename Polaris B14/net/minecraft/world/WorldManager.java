/*     */ package net.minecraft.world;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityTracker;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.network.play.server.S25PacketBlockBreakAnim;
/*     */ import net.minecraft.network.play.server.S28PacketEffect;
/*     */ import net.minecraft.network.play.server.S29PacketSoundEffect;
/*     */ import net.minecraft.scoreboard.Scoreboard;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.management.ServerConfigurationManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class WorldManager implements IWorldAccess
/*     */ {
/*     */   private MinecraftServer mcServer;
/*     */   private WorldServer theWorldServer;
/*     */   
/*     */   public WorldManager(MinecraftServer p_i1517_1_, WorldServer p_i1517_2_)
/*     */   {
/*  22 */     this.mcServer = p_i1517_1_;
/*  23 */     this.theWorldServer = p_i1517_2_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_180442_15_) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityAdded(Entity entityIn)
/*     */   {
/*  36 */     this.theWorldServer.getEntityTracker().trackEntity(entityIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityRemoved(Entity entityIn)
/*     */   {
/*  45 */     this.theWorldServer.getEntityTracker().untrackEntity(entityIn);
/*  46 */     this.theWorldServer.getScoreboard().func_181140_a(entityIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playSound(String soundName, double x, double y, double z, float volume, float pitch)
/*     */   {
/*  54 */     this.mcServer.getConfigurationManager().sendToAllNear(x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playSoundToNearExcept(EntityPlayer except, String soundName, double x, double y, double z, float volume, float pitch)
/*     */   {
/*  62 */     this.mcServer.getConfigurationManager().sendToAllNearExcept(except, x, y, z, volume > 1.0F ? 16.0F * volume : 16.0D, this.theWorldServer.provider.getDimensionId(), new S29PacketSoundEffect(soundName, x, y, z, volume, pitch));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void markBlockForUpdate(BlockPos pos)
/*     */   {
/*  75 */     this.theWorldServer.getPlayerManager().markBlockForUpdate(pos);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void notifyLightSet(BlockPos pos) {}
/*     */   
/*     */ 
/*     */   public void playRecord(String recordName, BlockPos blockPosIn) {}
/*     */   
/*     */ 
/*     */   public void playAuxSFX(EntityPlayer player, int sfxType, BlockPos blockPosIn, int p_180439_4_)
/*     */   {
/*  88 */     this.mcServer.getConfigurationManager().sendToAllNearExcept(player, blockPosIn.getX(), blockPosIn.getY(), blockPosIn.getZ(), 64.0D, this.theWorldServer.provider.getDimensionId(), new S28PacketEffect(sfxType, blockPosIn, p_180439_4_, false));
/*     */   }
/*     */   
/*     */   public void broadcastSound(int p_180440_1_, BlockPos p_180440_2_, int p_180440_3_)
/*     */   {
/*  93 */     this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S28PacketEffect(p_180440_1_, p_180440_2_, p_180440_3_, true));
/*     */   }
/*     */   
/*     */   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress)
/*     */   {
/*  98 */     for (EntityPlayerMP entityplayermp : this.mcServer.getConfigurationManager().func_181057_v())
/*     */     {
/* 100 */       if ((entityplayermp != null) && (entityplayermp.worldObj == this.theWorldServer) && (entityplayermp.getEntityId() != breakerId))
/*     */       {
/* 102 */         double d0 = pos.getX() - entityplayermp.posX;
/* 103 */         double d1 = pos.getY() - entityplayermp.posY;
/* 104 */         double d2 = pos.getZ() - entityplayermp.posZ;
/*     */         
/* 106 */         if (d0 * d0 + d1 * d1 + d2 * d2 < 1024.0D)
/*     */         {
/* 108 */           entityplayermp.playerNetServerHandler.sendPacket(new S25PacketBlockBreakAnim(breakerId, pos, progress));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\WorldManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */