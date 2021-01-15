package net.minecraft.client.multiplayer;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Callable;

import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSoundMinecart;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EntityFireworkStarterFX;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.src.BlockPosM;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.storage.SaveDataMemoryStorage;
import net.minecraft.world.storage.SaveHandlerMP;
import net.minecraft.world.storage.WorldInfo;

public class WorldClient extends World
{
    /** The packets that need to be sent to the server. */
    private NetHandlerPlayClient sendQueue;
    
    /** The ChunkProviderClient instance */
    private ChunkProviderClient clientChunkProvider;
    
    /** Contains all entities for this client, both spawned and non-spawned. */
    private final Set entityList = Sets.newHashSet();
    
    /**
     * Contains all entities for this client that were not spawned due to a non-present chunk. The game will attempt to
     * spawn up to 10 pending entities with each subsequent tick until the spawn queue is empty.
     */
    private final Set entitySpawnQueue = Sets.newHashSet();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Set previousActiveChunkSet = Sets.newHashSet();
    
    public WorldClient(NetHandlerPlayClient p_i45063_1_, WorldSettings p_i45063_2_, int p_i45063_3_, EnumDifficulty p_i45063_4_, Profiler p_i45063_5_)
    {
	super(new SaveHandlerMP(), new WorldInfo(p_i45063_2_, "MpServer"), WorldProvider.getProviderForDimension(p_i45063_3_), p_i45063_5_, true);
	this.sendQueue = p_i45063_1_;
	this.getWorldInfo().setDifficulty(p_i45063_4_);
	this.provider.registerWorld(this);
	this.setSpawnPoint(new BlockPos(8, 64, 8));
	this.chunkProvider = this.createChunkProvider();
	this.mapStorage = new SaveDataMemoryStorage();
	this.calculateInitialSkylight();
	this.calculateInitialWeather();
    }
    
    /**
     * Runs a single tick for the world
     */
    @Override
    public void tick()
    {
	super.tick();
	this.setTotalWorldTime(this.getTotalWorldTime() + 1L);
	
	if (this.getGameRules().getGameRuleBooleanValue("doDaylightCycle"))
	{
	    this.setWorldTime(this.getWorldTime() + 1L);
	}
	
	this.theProfiler.startSection("reEntryProcessing");
	
	for (int var1 = 0; var1 < 10 && !this.entitySpawnQueue.isEmpty(); ++var1)
	{
	    Entity var2 = (Entity) this.entitySpawnQueue.iterator().next();
	    this.entitySpawnQueue.remove(var2);
	    
	    if (!this.loadedEntityList.contains(var2))
	    {
		this.spawnEntityInWorld(var2);
	    }
	}
	
	this.theProfiler.endStartSection("chunkCache");
	this.clientChunkProvider.unloadQueuedChunks();
	this.theProfiler.endStartSection("blocks");
	this.updateBlocks();
	this.theProfiler.endSection();
    }
    
    /**
     * Invalidates an AABB region of blocks from the receive queue, in the event that the block has been modified
     * client-side in the intervening 80 receive ticks.
     */
    public void invalidateBlockReceiveRegion(int p_73031_1_, int p_73031_2_, int p_73031_3_, int p_73031_4_, int p_73031_5_, int p_73031_6_)
    {
    }
    
    /**
     * Creates the chunk provider for this world. Called in the constructor. Retrieves provider from worldProvider?
     */
    @Override
    protected IChunkProvider createChunkProvider()
    {
	this.clientChunkProvider = new ChunkProviderClient(this);
	return this.clientChunkProvider;
    }
    
    @Override
    protected void updateBlocks()
    {
	super.updateBlocks();
	this.previousActiveChunkSet.retainAll(this.activeChunkSet);
	
	if (this.previousActiveChunkSet.size() == this.activeChunkSet.size())
	{
	    this.previousActiveChunkSet.clear();
	}
	
	int var1 = 0;
	Iterator var2 = this.activeChunkSet.iterator();
	
	while (var2.hasNext())
	{
	    ChunkCoordIntPair var3 = (ChunkCoordIntPair) var2.next();
	    
	    if (!this.previousActiveChunkSet.contains(var3))
	    {
		int var4 = var3.chunkXPos * 16;
		int var5 = var3.chunkZPos * 16;
		this.theProfiler.startSection("getChunk");
		Chunk var6 = this.getChunkFromChunkCoords(var3.chunkXPos, var3.chunkZPos);
		this.playMoodSoundAndCheckLight(var4, var5, var6);
		this.theProfiler.endSection();
		this.previousActiveChunkSet.add(var3);
		++var1;
		
		if (var1 >= 10)
		{
		    return;
		}
	    }
	}
    }
    
    public void doPreChunk(int p_73025_1_, int p_73025_2_, boolean p_73025_3_)
    {
	if (p_73025_3_)
	{
	    this.clientChunkProvider.loadChunk(p_73025_1_, p_73025_2_);
	}
	else
	{
	    this.clientChunkProvider.unloadChunk(p_73025_1_, p_73025_2_);
	}
	
	if (!p_73025_3_)
	{
	    this.markBlockRangeForRenderUpdate(p_73025_1_ * 16, 0, p_73025_2_ * 16, p_73025_1_ * 16 + 15, 256, p_73025_2_ * 16 + 15);
	}
    }
    
    /**
     * Called when an entity is spawned in the world. This includes players.
     */
    @Override
    public boolean spawnEntityInWorld(Entity entityIn)
    {
	boolean var2 = super.spawnEntityInWorld(entityIn);
	this.entityList.add(entityIn);
	
	if (!var2)
	{
	    this.entitySpawnQueue.add(entityIn);
	}
	else if (entityIn instanceof EntityMinecart)
	{
	    this.mc.getSoundHandler().playSound(new MovingSoundMinecart((EntityMinecart) entityIn));
	}
	
	return var2;
    }
    
    /**
     * Schedule the entity for removal during the next tick. Marks the entity dead in anticipation.
     */
    @Override
    public void removeEntity(Entity entityIn)
    {
	super.removeEntity(entityIn);
	this.entityList.remove(entityIn);
    }
    
    @Override
    protected void onEntityAdded(Entity entityIn)
    {
	super.onEntityAdded(entityIn);
	
	if (this.entitySpawnQueue.contains(entityIn))
	{
	    this.entitySpawnQueue.remove(entityIn);
	}
    }
    
    @Override
    protected void onEntityRemoved(Entity entityIn)
    {
	super.onEntityRemoved(entityIn);
	if (this.entityList.contains(entityIn))
	{
	    if (entityIn.isEntityAlive())
	    {
		this.entitySpawnQueue.add(entityIn);
	    }
	    else
	    {
		this.entityList.remove(entityIn);
	    }
	}
    }
    
    /**
     * Add an ID to Entity mapping to entityHashSet
     */
    public void addEntityToWorld(int p_73027_1_, Entity p_73027_2_)
    {
	Entity var3 = this.getEntityByID(p_73027_1_);
	
	if (var3 != null)
	{
	    this.removeEntity(var3);
	}
	
	this.entityList.add(p_73027_2_);
	p_73027_2_.setEntityId(p_73027_1_);
	
	if (!this.spawnEntityInWorld(p_73027_2_))
	{
	    this.entitySpawnQueue.add(p_73027_2_);
	}
	
	this.entitiesById.addKey(p_73027_1_, p_73027_2_);
    }
    
    /**
     * Returns the Entity with the given ID, or null if it doesn't exist in this World.
     */
    @Override
    public Entity getEntityByID(int id)
    {
	return id == this.mc.thePlayer.getEntityId() ? this.mc.thePlayer : super.getEntityByID(id);
    }
    
    public Entity removeEntityFromWorld(int p_73028_1_)
    {
	Entity var2 = (Entity) this.entitiesById.removeObject(p_73028_1_);
	
	if (var2 != null)
	{
	    this.entityList.remove(var2);
	    this.removeEntity(var2);
	}
	
	return var2;
    }
    
    public boolean func_180503_b(BlockPos p_180503_1_, IBlockState p_180503_2_)
    {
	int var3 = p_180503_1_.getX();
	int var4 = p_180503_1_.getY();
	int var5 = p_180503_1_.getZ();
	this.invalidateBlockReceiveRegion(var3, var4, var5, var3, var4, var5);
	return super.setBlockState(p_180503_1_, p_180503_2_, 3);
    }
    
    /**
     * If on MP, sends a quitting packet.
     */
    @Override
    public void sendQuittingDisconnectingPacket()
    {
	this.sendQueue.getNetworkManager().closeChannel(new ChatComponentText("Quitting"));
    }
    
    /**
     * Updates all weather states.
     */
    @Override
    protected void updateWeather()
    {
    }
    
    @Override
    protected int getRenderDistanceChunks()
    {
	return this.mc.gameSettings.renderDistanceChunks;
    }
    
    public void doVoidFogParticles(int p_73029_1_, int p_73029_2_, int p_73029_3_)
    {
	byte var4 = 16;
	Random var5 = new Random();
	ItemStack var6 = this.mc.thePlayer.getHeldItem();
	boolean var7 = this.mc.playerController.getCurrentGameType() == WorldSettings.GameType.CREATIVE && var6 != null && Block.getBlockFromItem(var6.getItem()) == Blocks.barrier;
	BlockPosM blockPosM = new BlockPosM(0, 0, 0, 3);
	
	for (int var8 = 0; var8 < 1000; ++var8)
	{
	    int var9 = p_73029_1_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
	    int var10 = p_73029_2_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
	    int var11 = p_73029_3_ + this.rand.nextInt(var4) - this.rand.nextInt(var4);
	    blockPosM.setXyz(var9, var10, var11);
	    IBlockState var13 = this.getBlockState(blockPosM);
	    var13.getBlock().randomDisplayTick(this, blockPosM, var13, var5);
	    
	    if (var7 && var13.getBlock() == Blocks.barrier)
	    {
		this.spawnParticle(EnumParticleTypes.BARRIER, var9 + 0.5F, var10 + 0.5F, var11 + 0.5F, 0.0D, 0.0D, 0.0D, new int[0]);
	    }
	}
    }
    
    /**
     * also releases skins.
     */
    public void removeAllEntities()
    {
	this.loadedEntityList.removeAll(this.unloadedEntityList);
	int var1;
	Entity var2;
	int var3;
	int var4;
	
	for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1)
	{
	    var2 = (Entity) this.unloadedEntityList.get(var1);
	    var3 = var2.chunkCoordX;
	    var4 = var2.chunkCoordZ;
	    
	    if (var2.addedToChunk && this.isChunkLoaded(var3, var4, true))
	    {
		this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
	    }
	}
	
	for (var1 = 0; var1 < this.unloadedEntityList.size(); ++var1)
	{
	    this.onEntityRemoved((Entity) this.unloadedEntityList.get(var1));
	}
	
	this.unloadedEntityList.clear();
	
	for (var1 = 0; var1 < this.loadedEntityList.size(); ++var1)
	{
	    var2 = (Entity) this.loadedEntityList.get(var1);
	    
	    if (var2.ridingEntity != null)
	    {
		if (!var2.ridingEntity.isDead && var2.ridingEntity.riddenByEntity == var2)
		{
		    continue;
		}
		
		var2.ridingEntity.riddenByEntity = null;
		var2.ridingEntity = null;
	    }
	    
	    if (var2.isDead)
	    {
		var3 = var2.chunkCoordX;
		var4 = var2.chunkCoordZ;
		
		if (var2.addedToChunk && this.isChunkLoaded(var3, var4, true))
		{
		    this.getChunkFromChunkCoords(var3, var4).removeEntity(var2);
		}
		
		this.loadedEntityList.remove(var1--);
		this.onEntityRemoved(var2);
	    }
	}
    }
    
    /**
     * Adds some basic stats of the world to the given crash report.
     */
    @Override
    public CrashReportCategory addWorldInfoToCrashReport(CrashReport report)
    {
	CrashReportCategory var2 = super.addWorldInfoToCrashReport(report);
	var2.addCrashSectionCallable("Forced entities", new Callable()
	{
	    @Override
	    public String call()
	    {
		return WorldClient.this.entityList.size() + " total; " + WorldClient.this.entityList.toString();
	    }
	});
	var2.addCrashSectionCallable("Retry entities", new Callable()
	{
	    @Override
	    public String call()
	    {
		return WorldClient.this.entitySpawnQueue.size() + " total; " + WorldClient.this.entitySpawnQueue.toString();
	    }
	});
	var2.addCrashSectionCallable("Server brand", new Callable()
	{
	    @Override
	    public String call()
	    {
		return WorldClient.this.mc.thePlayer.getClientBrand();
	    }
	});
	var2.addCrashSectionCallable("Server type", new Callable()
	{
	    @Override
	    public String call()
	    {
		return WorldClient.this.mc.getIntegratedServer() == null ? "Non-integrated multiplayer server" : "Integrated singleplayer server";
	    }
	});
	return var2;
    }
    
    /**
     * Plays a sound at the specified position.
     */
    public void playSoundAtPos(BlockPos p_175731_1_, String p_175731_2_, float p_175731_3_, float p_175731_4_, boolean p_175731_5_)
    {
	this.playSound(p_175731_1_.getX() + 0.5D, p_175731_1_.getY() + 0.5D, p_175731_1_.getZ() + 0.5D, p_175731_2_, p_175731_3_, p_175731_4_, p_175731_5_);
    }
    
    /**
     * par8 is loudness, all pars passed to minecraftInstance.sndManager.playSound
     */
    @Override
    public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay)
    {
	double var11 = this.mc.getRenderViewEntity().getDistanceSq(x, y, z);
	PositionedSoundRecord var13 = new PositionedSoundRecord(new ResourceLocation(soundName), volume, pitch, (float) x, (float) y, (float) z);
	
	if (distanceDelay && var11 > 100.0D)
	{
	    double var14 = Math.sqrt(var11) / 40.0D;
	    this.mc.getSoundHandler().playDelayedSound(var13, (int) (var14 * 20.0D));
	}
	else
	{
	    this.mc.getSoundHandler().playSound(var13);
	}
    }
    
    @Override
    public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund)
    {
	this.mc.effectRenderer.addEffect(new EntityFireworkStarterFX(this, x, y, z, motionX, motionY, motionZ, this.mc.effectRenderer, compund));
    }
    
    public void setWorldScoreboard(Scoreboard p_96443_1_)
    {
	this.worldScoreboard = p_96443_1_;
    }
    
    /**
     * Sets the world time.
     */
    @Override
    public void setWorldTime(long time)
    {
	if (time < 0L)
	{
	    time = -time;
	    this.getGameRules().setOrCreateGameRule("doDaylightCycle", "false");
	}
	else
	{
	    this.getGameRules().setOrCreateGameRule("doDaylightCycle", "true");
	}
	
	super.setWorldTime(time);
    }
    
    public Block getBlock(int p_147439_1_, int p_147439_2_, int p_147439_3_)
    {
	if (p_147439_1_ >= -30000000 && p_147439_3_ >= -30000000 && p_147439_1_ < 30000000 && p_147439_3_ < 30000000 && p_147439_2_ >= 0 && p_147439_2_ < 256)
	{
	    Chunk var4 = null;
	    
	    try
	    {
		var4 = this.getChunkFromChunkCoords(p_147439_1_ >> 4, p_147439_3_ >> 4);
		return var4.getBlock0(p_147439_1_ & 15, p_147439_2_, p_147439_3_ & 15);
	    }
	    catch (Throwable var8)
	    {
		CrashReport var6 = CrashReport.makeCrashReport(var8, "Exception getting block type in world");
		CrashReportCategory var7 = var6.makeCategory("Requested block coordinates");
		var7.addCrashSection("Found chunk", Boolean.valueOf(var4 == null));
		throw new ReportedException(var6);
	    }
	}
	else
	{
	    return Blocks.air;
	}
    }
}
