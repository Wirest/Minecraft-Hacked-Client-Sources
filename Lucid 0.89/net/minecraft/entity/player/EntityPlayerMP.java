package net.minecraft.entity.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;

import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.potion.PotionEffect;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntityPlayerMP extends EntityPlayer implements ICrafting
{
    private static final Logger logger = LogManager.getLogger();
    private String translator = "en_US";
    
    /**
     * The NetServerHandler assigned to this player by the ServerConfigurationManager.
     */
    public NetHandlerPlayServer playerNetServerHandler;
    
    /** Reference to the MinecraftServer object. */
    public final MinecraftServer mcServer;
    
    /** The ItemInWorldManager belonging to this player */
    public final ItemInWorldManager theItemInWorldManager;
    
    /** player X position as seen by PlayerManager */
    public double managedPosX;
    
    /** player Z position as seen by PlayerManager */
    public double managedPosZ;
    
    /** LinkedList that holds the loaded chunks. */
    public final List loadedChunks = Lists.newLinkedList();
    
    /** entities added to this list will  be packet29'd to the player */
    private final List destroyedItemsNetCache = Lists.newLinkedList();
    private final StatisticsFile statsFile;
    
    /**
     * the total health of the player, includes actual health and absorption health. Updated every tick.
     */
    private float combinedHealth = Float.MIN_VALUE;
    
    /** amount of health the client was last set to */
    private float lastHealth = -1.0E8F;
    
    /** set to foodStats.GetFoodLevel */
    private int lastFoodLevel = -99999999;
    
    /** set to foodStats.getSaturationLevel() == 0.0F each tick */
    private boolean wasHungry = true;
    
    /** Amount of experience the client was last set to */
    private int lastExperience = -99999999;
    private int respawnInvulnerabilityTicks = 60;
    private EntityPlayer.EnumChatVisibility chatVisibility;
    private boolean chatColours = true;
    private long playerLastActiveTime = System.currentTimeMillis();
    
    /** The entity the player is currently spectating through. */
    private Entity spectatingEntity = null;
    
    /**
     * The currently in use window ID. Incremented every time a window is opened.
     */
    private int currentWindowId;
    
    /**
     * set to true when player is moving quantity of items from one inventory to another(crafting) but item in either
     * slot is not changed
     */
    public boolean isChangingQuantityOnly;
    public int ping;
    
    /**
     * Set when a player beats the ender dragon, used to respawn the player at the spawn point while retaining inventory
     * and XP
     */
    public boolean playerConqueredTheEnd;
    
    public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager)
    {
	super(worldIn, profile);
	interactionManager.thisPlayerMP = this;
	this.theItemInWorldManager = interactionManager;
	BlockPos var5 = worldIn.getSpawnPoint();
	
	if (!worldIn.provider.getHasNoSky() && worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE)
	{
	    int var6 = Math.max(5, server.getSpawnProtectionSize() - 6);
	    int var7 = MathHelper.floor_double(worldIn.getWorldBorder().getClosestDistance(var5.getX(), var5.getZ()));
	    
	    if (var7 < var6)
	    {
		var6 = var7;
	    }
	    
	    if (var7 <= 1)
	    {
		var6 = 1;
	    }
	    
	    var5 = worldIn.getTopSolidOrLiquidBlock(var5.add(this.rand.nextInt(var6 * 2) - var6, 0, this.rand.nextInt(var6 * 2) - var6));
	}
	
	this.mcServer = server;
	this.statsFile = server.getConfigurationManager().getPlayerStatsFile(this);
	this.stepHeight = 0.0F;
	this.moveToBlockPosAndAngles(var5, 0.0F, 0.0F);
	
	while (!worldIn.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0D)
	{
	    this.setPosition(this.posX, this.posY + 1.0D, this.posZ);
	}
    }
    
    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
	super.readEntityFromNBT(tagCompund);
	
	if (tagCompund.hasKey("playerGameType", 99))
	{
	    if (MinecraftServer.getServer().getForceGamemode())
	    {
		this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
	    }
	    else
	    {
		this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
	    }
	}
    }
    
    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
	super.writeEntityToNBT(tagCompound);
	tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
    }
    
    /**
     * Add experience levels to this player.
     */
    @Override
    public void addExperienceLevel(int levels)
    {
	super.addExperienceLevel(levels);
	this.lastExperience = -1;
    }
    
    @Override
    public void removeExperienceLevel(int levels)
    {
	super.removeExperienceLevel(levels);
	this.lastExperience = -1;
    }
    
    public void addSelfToInternalCraftingInventory()
    {
	this.openContainer.onCraftGuiOpened(this);
    }
    
    /**
     * Sends an ENTER_COMBAT packet to the client
     */
    @Override
    public void sendEnterCombat()
    {
	super.sendEnterCombat();
	this.playerNetServerHandler.sendPacket(new S42PacketCombatEvent(this.getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
    }
    
    /**
     * Sends an END_COMBAT packet to the client
     */
    @Override
    public void sendEndCombat()
    {
	super.sendEndCombat();
	this.playerNetServerHandler.sendPacket(new S42PacketCombatEvent(this.getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
    }
    
    /**
     * Called to update the entity's position/logic.
     */
    @Override
    public void onUpdate()
    {
	this.theItemInWorldManager.updateBlockRemoving();
	--this.respawnInvulnerabilityTicks;
	
	if (this.hurtResistantTime > 0)
	{
	    --this.hurtResistantTime;
	}
	
	this.openContainer.detectAndSendChanges();
	
	if (!this.worldObj.isRemote && !this.openContainer.canInteractWith(this))
	{
	    this.closeScreen();
	    this.openContainer = this.inventoryContainer;
	}
	
	while (!this.destroyedItemsNetCache.isEmpty())
	{
	    int var1 = Math.min(this.destroyedItemsNetCache.size(), Integer.MAX_VALUE);
	    int[] var2 = new int[var1];
	    Iterator var3 = this.destroyedItemsNetCache.iterator();
	    int var4 = 0;
	    
	    while (var3.hasNext() && var4 < var1)
	    {
		var2[var4++] = ((Integer) var3.next()).intValue();
		var3.remove();
	    }
	    
	    this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(var2));
	}
	
	if (!this.loadedChunks.isEmpty())
	{
	    ArrayList var6 = Lists.newArrayList();
	    Iterator var8 = this.loadedChunks.iterator();
	    ArrayList var9 = Lists.newArrayList();
	    Chunk var5;
	    
	    while (var8.hasNext() && var6.size() < 10)
	    {
		ChunkCoordIntPair var10 = (ChunkCoordIntPair) var8.next();
		
		if (var10 != null)
		{
		    if (this.worldObj.isBlockLoaded(new BlockPos(var10.chunkXPos << 4, 0, var10.chunkZPos << 4)))
		    {
			var5 = this.worldObj.getChunkFromChunkCoords(var10.chunkXPos, var10.chunkZPos);
			
			if (var5.isPopulated())
			{
			    var6.add(var5);
			    var9.addAll(((WorldServer) this.worldObj).func_147486_a(var10.chunkXPos * 16, 0, var10.chunkZPos * 16, var10.chunkXPos * 16 + 16, 256, var10.chunkZPos * 16 + 16));
			    var8.remove();
			}
		    }
		}
		else
		{
		    var8.remove();
		}
	    }
	    
	    if (!var6.isEmpty())
	    {
		if (var6.size() == 1)
		{
		    this.playerNetServerHandler.sendPacket(new S21PacketChunkData((Chunk) var6.get(0), true, 65535));
		}
		else
		{
		    this.playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(var6));
		}
		
		Iterator var11 = var9.iterator();
		
		while (var11.hasNext())
		{
		    TileEntity var12 = (TileEntity) var11.next();
		    this.sendTileEntityUpdate(var12);
		}
		
		var11 = var6.iterator();
		
		while (var11.hasNext())
		{
		    var5 = (Chunk) var11.next();
		    this.getServerForPlayer().getEntityTracker().func_85172_a(this, var5);
		}
	    }
	}
	
	Entity var7 = this.getSpectatingEntity();
	
	if (var7 != this)
	{
	    if (!var7.isEntityAlive())
	    {
		this.setSpectatingEntity(this);
	    }
	    else
	    {
		this.setPositionAndRotation(var7.posX, var7.posY, var7.posZ, var7.rotationYaw, var7.rotationPitch);
		this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
		
		if (this.isSneaking())
		{
		    this.setSpectatingEntity(this);
		}
	    }
	}
    }
    
    public void onUpdateEntity()
    {
	try
	{
	    super.onUpdate();
	    
	    for (int var1 = 0; var1 < this.inventory.getSizeInventory(); ++var1)
	    {
		ItemStack var6 = this.inventory.getStackInSlot(var1);
		
		if (var6 != null && var6.getItem().isMap())
		{
		    Packet var8 = ((ItemMapBase) var6.getItem()).createMapDataPacket(var6, this.worldObj, this);
		    
		    if (var8 != null)
		    {
			this.playerNetServerHandler.sendPacket(var8);
		    }
		}
	    }
	    
	    if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0F != this.wasHungry)
	    {
		this.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
		this.lastHealth = this.getHealth();
		this.lastFoodLevel = this.foodStats.getFoodLevel();
		this.wasHungry = this.foodStats.getSaturationLevel() == 0.0F;
	    }
	    
	    if (this.getHealth() + this.getAbsorptionAmount() != this.combinedHealth)
	    {
		this.combinedHealth = this.getHealth() + this.getAbsorptionAmount();
		Collection var5 = this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health);
		Iterator var7 = var5.iterator();
		
		while (var7.hasNext())
		{
		    ScoreObjective var9 = (ScoreObjective) var7.next();
		    this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var9).func_96651_a(Arrays.asList(new EntityPlayer[] { this }));
		}
	    }
	    
	    if (this.experienceTotal != this.lastExperience)
	    {
		this.lastExperience = this.experienceTotal;
		this.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
	    }
	    
	    if (this.ticksExisted % 20 * 5 == 0 && !this.getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes))
	    {
		this.updateBiomesExplored();
	    }
	}
	catch (Throwable var4)
	{
	    CrashReport var2 = CrashReport.makeCrashReport(var4, "Ticking player");
	    CrashReportCategory var3 = var2.makeCategory("Player being ticked");
	    this.addEntityCrashInfo(var3);
	    throw new ReportedException(var2);
	}
    }
    
    /**
     * Updates all biomes that have been explored by this player and triggers Adventuring Time if player qualifies.
     */
    protected void updateBiomesExplored()
    {
	BiomeGenBase var1 = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));
	String var2 = var1.biomeName;
	JsonSerializableSet var3 = (JsonSerializableSet) this.getStatFile().func_150870_b(AchievementList.exploreAllBiomes);
	
	if (var3 == null)
	{
	    var3 = (JsonSerializableSet) this.getStatFile().func_150872_a(AchievementList.exploreAllBiomes, new JsonSerializableSet());
	}
	
	var3.add(var2);
	
	if (this.getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && var3.size() >= BiomeGenBase.explorationBiomesList.size())
	{
	    HashSet var4 = Sets.newHashSet(BiomeGenBase.explorationBiomesList);
	    Iterator var5 = var3.iterator();
	    
	    while (var5.hasNext())
	    {
		String var6 = (String) var5.next();
		Iterator var7 = var4.iterator();
		
		while (var7.hasNext())
		{
		    BiomeGenBase var8 = (BiomeGenBase) var7.next();
		    
		    if (var8.biomeName.equals(var6))
		    {
			var7.remove();
		    }
		}
		
		if (var4.isEmpty())
		{
		    break;
		}
	    }
	    
	    if (var4.isEmpty())
	    {
		this.triggerAchievement(AchievementList.exploreAllBiomes);
	    }
	}
    }
    
    /**
     * Called when the mob's health reaches 0.
     */
    @Override
    public void onDeath(DamageSource cause)
    {
	if (this.worldObj.getGameRules().getGameRuleBooleanValue("showDeathMessages"))
	{
	    Team var2 = this.getTeam();
	    
	    if (var2 != null && var2.func_178771_j() != Team.EnumVisible.ALWAYS)
	    {
		if (var2.func_178771_j() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
		{
		    this.mcServer.getConfigurationManager().sendMessageToAllTeamMembers(this, this.getCombatTracker().getDeathMessage());
		}
		else if (var2.func_178771_j() == Team.EnumVisible.HIDE_FOR_OWN_TEAM)
		{
		    this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer(this, this.getCombatTracker().getDeathMessage());
		}
	    }
	    else
	    {
		this.mcServer.getConfigurationManager().sendChatMsg(this.getCombatTracker().getDeathMessage());
	    }
	}
	
	if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory"))
	{
	    this.inventory.dropAllItems();
	}
	
	Collection var6 = this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount);
	Iterator var3 = var6.iterator();
	
	while (var3.hasNext())
	{
	    ScoreObjective var4 = (ScoreObjective) var3.next();
	    Score var5 = this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var4);
	    var5.func_96648_a();
	}
	
	EntityLivingBase var7 = this.func_94060_bK();
	
	if (var7 != null)
	{
	    EntityList.EntityEggInfo var8 = (EntityList.EntityEggInfo) EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID(var7)));
	    
	    if (var8 != null)
	    {
		this.triggerAchievement(var8.field_151513_e);
	    }
	    
	    var7.addToPlayerScore(this, this.scoreValue);
	}
	
	this.triggerAchievement(StatList.deathsStat);
	this.func_175145_a(StatList.timeSinceDeathStat);
	this.getCombatTracker().func_94549_h();
    }
    
    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
	if (this.isEntityInvulnerable(source))
	{
	    return false;
	}
	else
	{
	    boolean var3 = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(source.damageType);
	    
	    if (!var3 && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld)
	    {
		return false;
	    }
	    else
	    {
		if (source instanceof EntityDamageSource)
		{
		    Entity var4 = source.getEntity();
		    
		    if (var4 instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer) var4))
		    {
			return false;
		    }
		    
		    if (var4 instanceof EntityArrow)
		    {
			EntityArrow var5 = (EntityArrow) var4;
			
			if (var5.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer) var5.shootingEntity))
			{
			    return false;
			}
		    }
		}
		
		return super.attackEntityFrom(source, amount);
	    }
	}
    }
    
    @Override
    public boolean canAttackPlayer(EntityPlayer other)
    {
	return !this.canPlayersAttack() ? false : super.canAttackPlayer(other);
    }
    
    /**
     * Returns if other players can attack this player
     */
    private boolean canPlayersAttack()
    {
	return this.mcServer.isPVPEnabled();
    }
    
    /**
     * Teleports the entity to another dimension. Params: Dimension number to teleport to
     */
    @Override
    public void travelToDimension(int dimensionId)
    {
	if (this.dimension == 1 && dimensionId == 1)
	{
	    this.triggerAchievement(AchievementList.theEnd2);
	    this.worldObj.removeEntity(this);
	    this.playerConqueredTheEnd = true;
	    this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
	}
	else
	{
	    if (this.dimension == 0 && dimensionId == 1)
	    {
		this.triggerAchievement(AchievementList.theEnd);
		BlockPos var2 = this.mcServer.worldServerForDimension(dimensionId).func_180504_m();
		
		if (var2 != null)
		{
		    this.playerNetServerHandler.setPlayerLocation(var2.getX(), var2.getY(), var2.getZ(), 0.0F, 0.0F);
		}
		
		dimensionId = 1;
	    }
	    else
	    {
		this.triggerAchievement(AchievementList.portal);
	    }
	    
	    this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
	    this.lastExperience = -1;
	    this.lastHealth = -1.0F;
	    this.lastFoodLevel = -1;
	}
    }
    
    @Override
    public boolean func_174827_a(EntityPlayerMP player)
    {
	return player.isSpectator() ? this.getSpectatingEntity() == this : (this.isSpectator() ? false : super.func_174827_a(player));
    }
    
    private void sendTileEntityUpdate(TileEntity p_147097_1_)
    {
	if (p_147097_1_ != null)
	{
	    Packet var2 = p_147097_1_.getDescriptionPacket();
	    
	    if (var2 != null)
	    {
		this.playerNetServerHandler.sendPacket(var2);
	    }
	}
    }
    
    /**
     * Called whenever an item is picked up from walking over it. Args: pickedUpEntity, stackSize
     */
    @Override
    public void onItemPickup(Entity p_71001_1_, int p_71001_2_)
    {
	super.onItemPickup(p_71001_1_, p_71001_2_);
	this.openContainer.detectAndSendChanges();
    }
    
    @Override
    public EntityPlayer.EnumStatus trySleep(BlockPos bedLocation)
    {
	EntityPlayer.EnumStatus var2 = super.trySleep(bedLocation);
	
	if (var2 == EntityPlayer.EnumStatus.OK)
	{
	    S0APacketUseBed var3 = new S0APacketUseBed(this, bedLocation);
	    this.getServerForPlayer().getEntityTracker().sendToAllTrackingEntity(this, var3);
	    this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	    this.playerNetServerHandler.sendPacket(var3);
	}
	
	return var2;
    }
    
    /**
     * Wake up the player if they're sleeping.
     */
    @Override
    public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn)
    {
	if (this.isPlayerSleeping())
	{
	    this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 2));
	}
	
	super.wakeUpPlayer(p_70999_1_, updateWorldFlag, setSpawn);
	
	if (this.playerNetServerHandler != null)
	{
	    this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	}
    }
    
    /**
     * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
     */
    @Override
    public void mountEntity(Entity entityIn)
    {
	Entity var2 = this.ridingEntity;
	super.mountEntity(entityIn);
	
	if (entityIn != var2)
	{
	    this.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this, this.ridingEntity));
	    this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
	}
    }
    
    @Override
    protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos)
    {
    }
    
    /**
     * process player falling based on movement packet
     */
    public void handleFalling(double p_71122_1_, boolean p_71122_3_)
    {
	int var4 = MathHelper.floor_double(this.posX);
	int var5 = MathHelper.floor_double(this.posY - 0.20000000298023224D);
	int var6 = MathHelper.floor_double(this.posZ);
	BlockPos var7 = new BlockPos(var4, var5, var6);
	Block var8 = this.worldObj.getBlockState(var7).getBlock();
	
	if (var8.getMaterial() == Material.air)
	{
	    Block var9 = this.worldObj.getBlockState(var7.down()).getBlock();
	    
	    if (var9 instanceof BlockFence || var9 instanceof BlockWall || var9 instanceof BlockFenceGate)
	    {
		var7 = var7.down();
		var8 = this.worldObj.getBlockState(var7).getBlock();
	    }
	}
	
	super.updateFallState(p_71122_1_, p_71122_3_, var8, var7);
    }
    
    @Override
    public void openEditSign(TileEntitySign signTile)
    {
	signTile.setPlayer(this);
	this.playerNetServerHandler.sendPacket(new S36PacketSignEditorOpen(signTile.getPos()));
    }
    
    /**
     * get the next window id to use
     */
    private void getNextWindowId()
    {
	this.currentWindowId = this.currentWindowId % 100 + 1;
    }
    
    @Override
    public void displayGui(IInteractionObject guiOwner)
    {
	this.getNextWindowId();
	this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
	this.openContainer = guiOwner.createContainer(this.inventory, this);
	this.openContainer.windowId = this.currentWindowId;
	this.openContainer.onCraftGuiOpened(this);
    }
    
    /**
     * Displays the GUI for interacting with a chest inventory. Args: chestInventory
     */
    @Override
    public void displayGUIChest(IInventory chestInventory)
    {
	if (this.openContainer != this.inventoryContainer)
	{
	    this.closeScreen();
	}
	
	if (chestInventory instanceof ILockableContainer)
	{
	    ILockableContainer var2 = (ILockableContainer) chestInventory;
	    
	    if (var2.isLocked() && !this.canOpen(var2.getLockCode()) && !this.isSpectator())
	    {
		this.playerNetServerHandler.sendPacket(new S02PacketChat(new ChatComponentTranslation("container.isLocked", new Object[] { chestInventory.getDisplayName() }), (byte) 2));
		this.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("random.door_close", this.posX, this.posY, this.posZ, 1.0F, 1.0F));
		return;
	    }
	}
	
	this.getNextWindowId();
	
	if (chestInventory instanceof IInteractionObject)
	{
	    this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, ((IInteractionObject) chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
	    this.openContainer = ((IInteractionObject) chestInventory).createContainer(this.inventory, this);
	}
	else
	{
	    this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
	    this.openContainer = new ContainerChest(this.inventory, chestInventory, this);
	}
	
	this.openContainer.windowId = this.currentWindowId;
	this.openContainer.onCraftGuiOpened(this);
    }
    
    @Override
    public void displayVillagerTradeGui(IMerchant villager)
    {
	this.getNextWindowId();
	this.openContainer = new ContainerMerchant(this.inventory, villager, this.worldObj);
	this.openContainer.windowId = this.currentWindowId;
	this.openContainer.onCraftGuiOpened(this);
	InventoryMerchant var2 = ((ContainerMerchant) this.openContainer).getMerchantInventory();
	IChatComponent var3 = villager.getDisplayName();
	this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "minecraft:villager", var3, var2.getSizeInventory()));
	MerchantRecipeList var4 = villager.getRecipes(this);
	
	if (var4 != null)
	{
	    PacketBuffer var5 = new PacketBuffer(Unpooled.buffer());
	    var5.writeInt(this.currentWindowId);
	    var4.writeToBuf(var5);
	    this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|TrList", var5));
	}
    }
    
    @Override
    public void displayGUIHorse(EntityHorse horse, IInventory horseInventory)
    {
	if (this.openContainer != this.inventoryContainer)
	{
	    this.closeScreen();
	}
	
	this.getNextWindowId();
	this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
	this.openContainer = new ContainerHorseInventory(this.inventory, horseInventory, horse, this);
	this.openContainer.windowId = this.currentWindowId;
	this.openContainer.onCraftGuiOpened(this);
    }
    
    /**
     * Displays the GUI for interacting with a book.
     */
    @Override
    public void displayGUIBook(ItemStack bookStack)
    {
	Item var2 = bookStack.getItem();
	
	if (var2 == Items.written_book)
	{
	    this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer())));
	}
    }
    
    /**
     * Sends the contents of an inventory slot to the client-side Container. This doesn't have to match the actual
     * contents of that slot. Args: Container, slot number, slot contents
     *  
     * @param containerToSend The container that is to be updated on the client.
     * @param slotInd The slot index that is to be updated.
     * @param stack The itemstack to be updated in the selected slot.
     */
    @Override
    public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
    {
	if (!(containerToSend.getSlot(slotInd) instanceof SlotCrafting))
	{
	    if (!this.isChangingQuantityOnly)
	    {
		this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));
	    }
	}
    }
    
    public void sendContainerToPlayer(Container p_71120_1_)
    {
	this.updateCraftingInventory(p_71120_1_, p_71120_1_.getInventory());
    }
    
    /**
     * update the crafting window inventory with the items in the list
     *  
     * @param containerToSend The container whose contents are to be sent to the player.
     * @param itemsList The items to be sent to the player.
     */
    @Override
    public void updateCraftingInventory(Container containerToSend, List itemsList)
    {
	this.playerNetServerHandler.sendPacket(new S30PacketWindowItems(containerToSend.windowId, itemsList));
	this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
    }
    
    /**
     * Sends two ints to the client-side Container. Used for furnace burning time, smelting progress, brewing progress,
     * and enchanting level. Normally the first int identifies which variable to update, and the second contains the new
     * value. Both are truncated to shorts in non-local SMP.
     *  
     * @param containerIn The container sending a progress bar update.
     * @param varToUpdate The integer corresponding the variable to be updated.
     * @param newValue The value the variable is to be updated with.
     */
    @Override
    public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue)
    {
	this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }
    
    @Override
    public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_)
    {
	for (int var3 = 0; var3 < p_175173_2_.getFieldCount(); ++var3)
	{
	    this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(p_175173_1_.windowId, var3, p_175173_2_.getField(var3)));
	}
    }
    
    /**
     * set current crafting inventory back to the 2x2 square
     */
    @Override
    public void closeScreen()
    {
	this.playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(this.openContainer.windowId));
	this.closeContainer();
    }
    
    /**
     * updates item held by mouse
     */
    public void updateHeldItem()
    {
	if (!this.isChangingQuantityOnly)
	{
	    this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
	}
    }
    
    /**
     * Closes the container the player currently has open.
     */
    public void closeContainer()
    {
	this.openContainer.onContainerClosed(this);
	this.openContainer = this.inventoryContainer;
    }
    
    public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean sneaking)
    {
	if (this.ridingEntity != null)
	{
	    if (p_110430_1_ >= -1.0F && p_110430_1_ <= 1.0F)
	    {
		this.moveStrafing = p_110430_1_;
	    }
	    
	    if (p_110430_2_ >= -1.0F && p_110430_2_ <= 1.0F)
	    {
		this.moveForward = p_110430_2_;
	    }
	    
	    this.isJumping = p_110430_3_;
	    this.setSneaking(sneaking);
	}
    }
    
    /**
     * Adds a value to a statistic field.
     */
    @Override
    public void addStat(StatBase stat, int amount)
    {
	if (stat != null)
	{
	    this.statsFile.func_150871_b(this, stat, amount);
	    Iterator var3 = this.getWorldScoreboard().getObjectivesFromCriteria(stat.func_150952_k()).iterator();
	    
	    while (var3.hasNext())
	    {
		ScoreObjective var4 = (ScoreObjective) var3.next();
		this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var4).increseScore(amount);
	    }
	    
	    if (this.statsFile.func_150879_e())
	    {
		this.statsFile.func_150876_a(this);
	    }
	}
    }
    
    @Override
    public void func_175145_a(StatBase p_175145_1_)
    {
	if (p_175145_1_ != null)
	{
	    this.statsFile.func_150873_a(this, p_175145_1_, 0);
	    Iterator var2 = this.getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.func_150952_k()).iterator();
	    
	    while (var2.hasNext())
	    {
		ScoreObjective var3 = (ScoreObjective) var2.next();
		this.getWorldScoreboard().getValueFromObjective(this.getCommandSenderName(), var3).setScorePoints(0);
	    }
	    
	    if (this.statsFile.func_150879_e())
	    {
		this.statsFile.func_150876_a(this);
	    }
	}
    }
    
    public void mountEntityAndWakeUp()
    {
	if (this.riddenByEntity != null)
	{
	    this.riddenByEntity.mountEntity(this);
	}
	
	if (this.sleeping)
	{
	    this.wakeUpPlayer(true, false, false);
	}
    }
    
    /**
     * this function is called when a players inventory is sent to him, lastHealth is updated on any dimension
     * transitions, then reset.
     */
    public void setPlayerHealthUpdated()
    {
	this.lastHealth = -1.0E8F;
    }
    
    @Override
    public void addChatComponentMessage(IChatComponent chatComponent)
    {
	this.playerNetServerHandler.sendPacket(new S02PacketChat(chatComponent));
    }
    
    /**
     * Used for when item use count runs out, ie: eating completed
     */
    @Override
    protected void onItemUseFinish()
    {
	this.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(this, (byte) 9));
	super.onItemUseFinish();
    }
    
    /**
     * sets the itemInUse when the use item button is clicked. Args: itemstack, int maxItemUseDuration
     */
    @Override
    public void setItemInUse(ItemStack stack, int duration)
    {
	super.setItemInUse(stack, duration);
	
	if (stack != null && stack.getItem() != null && stack.getItem().getItemUseAction(stack) == EnumAction.EAT)
	{
	    this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 3));
	}
    }
    
    /**
     * Copies the values from the given player into this player if boolean par2 is true. Always clones Ender Chest
     * Inventory.
     */
    @Override
    public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd)
    {
	super.clonePlayer(oldPlayer, respawnFromEnd);
	this.lastExperience = -1;
	this.lastHealth = -1.0F;
	this.lastFoodLevel = -1;
	this.destroyedItemsNetCache.addAll(((EntityPlayerMP) oldPlayer).destroyedItemsNetCache);
    }
    
    @Override
    protected void onNewPotionEffect(PotionEffect p_70670_1_)
    {
	super.onNewPotionEffect(p_70670_1_);
	this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.getEntityId(), p_70670_1_));
    }
    
    @Override
    protected void onChangedPotionEffect(PotionEffect p_70695_1_, boolean p_70695_2_)
    {
	super.onChangedPotionEffect(p_70695_1_, p_70695_2_);
	this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.getEntityId(), p_70695_1_));
    }
    
    @Override
    protected void onFinishedPotionEffect(PotionEffect p_70688_1_)
    {
	super.onFinishedPotionEffect(p_70688_1_);
	this.playerNetServerHandler.sendPacket(new S1EPacketRemoveEntityEffect(this.getEntityId(), p_70688_1_));
    }
    
    /**
     * Sets the position of the entity and updates the 'last' variables
     */
    @Override
    public void setPositionAndUpdate(double x, double y, double z)
    {
	this.playerNetServerHandler.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
    }
    
    /**
     * Called when the player performs a critical hit on the Entity. Args: entity that was hit critically
     */
    @Override
    public void onCriticalHit(Entity entityHit)
    {
	this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 4));
    }
    
    @Override
    public void onEnchantmentCritical(Entity entityHit)
    {
	this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 5));
    }
    
    /**
     * Sends the player's abilities to the server (if there is one).
     */
    @Override
    public void sendPlayerAbilities()
    {
	if (this.playerNetServerHandler != null)
	{
	    this.playerNetServerHandler.sendPacket(new S39PacketPlayerAbilities(this.capabilities));
	    this.updatePotionMetadata();
	}
    }
    
    public WorldServer getServerForPlayer()
    {
	return (WorldServer) this.worldObj;
    }
    
    /**
     * Sets the player's game mode and sends it to them.
     */
    @Override
    public void setGameType(WorldSettings.GameType gameType)
    {
	this.theItemInWorldManager.setGameType(gameType);
	this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, gameType.getID()));
	
	if (gameType == WorldSettings.GameType.SPECTATOR)
	{
	    this.mountEntity((Entity) null);
	}
	else
	{
	    this.setSpectatingEntity(this);
	}
	
	this.sendPlayerAbilities();
	this.markPotionsDirty();
    }
    
    /**
     * Returns true if the player is in spectator mode.
     */
    @Override
    public boolean isSpectator()
    {
	return this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
    
    /**
     * Send a chat message to the CommandSender
     *  
     * @param component The ChatComponent to send
     */
    @Override
    public void addChatMessage(IChatComponent component)
    {
	this.playerNetServerHandler.sendPacket(new S02PacketChat(component));
    }
    
    /**
     * Returns {@code true} if the CommandSender is allowed to execute the command, {@code false} if not
     *  
     * @param permLevel The permission level required to execute the command
     * @param commandName The name of the command
     */
    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName)
    {
	if ("seed".equals(commandName) && !this.mcServer.isDedicatedServer())
	{
	    return true;
	}
	else if (!"tell".equals(commandName) && !"help".equals(commandName) && !"me".equals(commandName) && !"trigger".equals(commandName))
	{
	    if (this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile()))
	    {
		UserListOpsEntry var3 = (UserListOpsEntry) this.mcServer.getConfigurationManager().getOppedPlayers().getEntry(this.getGameProfile());
		return var3 != null ? var3.getPermissionLevel() >= permLevel : this.mcServer.getOpPermissionLevel() >= permLevel;
	    }
	    else
	    {
		return false;
	    }
	}
	else
	{
	    return true;
	}
    }
    
    /**
     * Gets the player's IP address. Used in /banip.
     */
    public String getPlayerIP()
    {
	String var1 = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
	var1 = var1.substring(var1.indexOf("/") + 1);
	var1 = var1.substring(0, var1.indexOf(":"));
	return var1;
    }
    
    public void handleClientSettings(C15PacketClientSettings packetIn)
    {
	this.translator = packetIn.getLang();
	this.chatVisibility = packetIn.getChatVisibility();
	this.chatColours = packetIn.isColorsEnabled();
	this.getDataWatcher().updateObject(10, Byte.valueOf((byte) packetIn.getModelPartFlags()));
    }
    
    public EntityPlayer.EnumChatVisibility getChatVisibility()
    {
	return this.chatVisibility;
    }
    
    public void loadResourcePack(String url, String hash)
    {
	this.playerNetServerHandler.sendPacket(new S48PacketResourcePackSend(url, hash));
    }
    
    /**
     * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
     * the coordinates 0, 0, 0
     */
    @Override
    public BlockPos getPosition()
    {
	return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
    }
    
    public void markPlayerActive()
    {
	this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }
    
    /**
     * Gets the stats file for reading achievements
     */
    public StatisticsFile getStatFile()
    {
	return this.statsFile;
    }
    
    public void func_152339_d(Entity p_152339_1_)
    {
	if (p_152339_1_ instanceof EntityPlayer)
	{
	    this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(new int[] { p_152339_1_.getEntityId() }));
	}
	else
	{
	    this.destroyedItemsNetCache.add(Integer.valueOf(p_152339_1_.getEntityId()));
	}
    }
    
    /**
     * Clears potion metadata values if the entity has no potion effects. Otherwise, updates potion effect color,
     * ambience, and invisibility metadata values
     */
    @Override
    protected void updatePotionMetadata()
    {
	if (this.isSpectator())
	{
	    this.resetPotionEffectMetadata();
	    this.setInvisible(true);
	}
	else
	{
	    super.updatePotionMetadata();
	}
	
	this.getServerForPlayer().getEntityTracker().func_180245_a(this);
    }
    
    public Entity getSpectatingEntity()
    {
	return this.spectatingEntity == null ? this : this.spectatingEntity;
    }
    
    public void setSpectatingEntity(Entity entityToSpectate)
    {
	Entity var2 = this.getSpectatingEntity();
	this.spectatingEntity = entityToSpectate == null ? this : entityToSpectate;
	
	if (var2 != this.spectatingEntity)
	{
	    this.playerNetServerHandler.sendPacket(new S43PacketCamera(this.spectatingEntity));
	    this.setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
	}
    }
    
    /**
     * Attacks for the player the targeted entity with the currently equipped item.  The equipped item has hitEntity
     * called on it. Args: targetEntity
     */
    @Override
    public void attackTargetEntityWithCurrentItem(Entity targetEntity)
    {
	if (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR)
	{
	    this.setSpectatingEntity(targetEntity);
	}
	else
	{
	    super.attackTargetEntityWithCurrentItem(targetEntity);
	}
    }
    
    public long getLastActiveTime()
    {
	return this.playerLastActiveTime;
    }
    
    /**
     * Returns null which indicates the tab list should just display the player's name, return a different value to
     * display the specified text instead of the player's name
     */
    public IChatComponent getTabListDisplayName()
    {
	return null;
    }
}
