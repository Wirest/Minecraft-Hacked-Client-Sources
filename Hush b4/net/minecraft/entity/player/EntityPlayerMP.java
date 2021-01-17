// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.player;

import net.minecraft.network.play.server.S43PacketCamera;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.network.play.client.C15PacketClientSettings;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraft.network.play.server.S39PacketPlayerAbilities;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.potion.PotionEffect;
import net.minecraft.item.EnumAction;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S2EPacketCloseWindow;
import net.minecraft.network.play.server.S31PacketWindowProperty;
import net.minecraft.network.play.server.S30PacketWindowItems;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.entity.IMerchant;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.network.play.server.S29PacketSoundEffect;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.inventory.IInventory;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.world.IInteractionObject;
import net.minecraft.network.play.server.S36PacketSignEditorOpen;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.network.play.server.S1BPacketEntityAttach;
import net.minecraft.network.play.server.S0BPacketAnimation;
import net.minecraft.network.play.server.S0APacketUseBed;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.scoreboard.Score;
import net.minecraft.stats.StatList;
import net.minecraft.entity.EntityList;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.stats.StatBase;
import net.minecraft.util.JsonSerializableSet;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.crash.CrashReport;
import net.minecraft.stats.AchievementList;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import java.util.Arrays;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.item.ItemMapBase;
import java.util.Iterator;
import net.minecraft.network.play.server.S26PacketMapChunkBulk;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.tileentity.TileEntity;
import java.util.Collection;
import net.minecraft.network.play.server.S13PacketDestroyEntities;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S42PacketCombatEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldSettings;
import com.google.common.collect.Lists;
import net.minecraft.world.World;
import com.mojang.authlib.GameProfile;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.Entity;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.world.ChunkCoordIntPair;
import java.util.List;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.NetHandlerPlayServer;
import org.apache.logging.log4j.Logger;
import net.minecraft.inventory.ICrafting;

public class EntityPlayerMP extends EntityPlayer implements ICrafting
{
    private static final Logger logger;
    private String translator;
    public NetHandlerPlayServer playerNetServerHandler;
    public final MinecraftServer mcServer;
    public final ItemInWorldManager theItemInWorldManager;
    public double managedPosX;
    public double managedPosZ;
    public final List<ChunkCoordIntPair> loadedChunks;
    private final List<Integer> destroyedItemsNetCache;
    private final StatisticsFile statsFile;
    private float combinedHealth;
    private float lastHealth;
    private int lastFoodLevel;
    private boolean wasHungry;
    private int lastExperience;
    private int respawnInvulnerabilityTicks;
    private EnumChatVisibility chatVisibility;
    private boolean chatColours;
    private long playerLastActiveTime;
    private Entity spectatingEntity;
    private int currentWindowId;
    public boolean isChangingQuantityOnly;
    public int ping;
    public boolean playerConqueredTheEnd;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public EntityPlayerMP(final MinecraftServer server, final WorldServer worldIn, final GameProfile profile, final ItemInWorldManager interactionManager) {
        super(worldIn, profile);
        this.translator = "en_US";
        this.loadedChunks = (List<ChunkCoordIntPair>)Lists.newLinkedList();
        this.destroyedItemsNetCache = (List<Integer>)Lists.newLinkedList();
        this.combinedHealth = Float.MIN_VALUE;
        this.lastHealth = -1.0E8f;
        this.lastFoodLevel = -99999999;
        this.wasHungry = true;
        this.lastExperience = -99999999;
        this.respawnInvulnerabilityTicks = 60;
        this.chatColours = true;
        this.playerLastActiveTime = System.currentTimeMillis();
        this.spectatingEntity = null;
        interactionManager.thisPlayerMP = this;
        this.theItemInWorldManager = interactionManager;
        BlockPos blockpos = worldIn.getSpawnPoint();
        if (!worldIn.provider.getHasNoSky() && worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE) {
            int i = Math.max(5, server.getSpawnProtectionSize() - 6);
            final int j = MathHelper.floor_double(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
            if (j < i) {
                i = j;
            }
            if (j <= 1) {
                i = 1;
            }
            blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2) - i, 0, this.rand.nextInt(i * 2) - i));
        }
        this.mcServer = server;
        this.statsFile = server.getConfigurationManager().getPlayerStatsFile(this);
        this.moveToBlockPosAndAngles(blockpos, this.stepHeight = 0.0f, 0.0f);
        while (!worldIn.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && this.posY < 255.0) {
            this.setPosition(this.posX, this.posY + 1.0, this.posZ);
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("playerGameType", 99)) {
            if (MinecraftServer.getServer().getForceGamemode()) {
                this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
            }
            else {
                this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
            }
        }
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
    }
    
    @Override
    public void addExperienceLevel(final int levels) {
        super.addExperienceLevel(levels);
        this.lastExperience = -1;
    }
    
    @Override
    public void removeExperienceLevel(final int levels) {
        super.removeExperienceLevel(levels);
        this.lastExperience = -1;
    }
    
    public void addSelfToInternalCraftingInventory() {
        this.openContainer.onCraftGuiOpened(this);
    }
    
    @Override
    public void sendEnterCombat() {
        super.sendEnterCombat();
        this.playerNetServerHandler.sendPacket(new S42PacketCombatEvent(this.getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
    }
    
    @Override
    public void sendEndCombat() {
        super.sendEndCombat();
        this.playerNetServerHandler.sendPacket(new S42PacketCombatEvent(this.getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
    }
    
    @Override
    public void onUpdate() {
        this.theItemInWorldManager.updateBlockRemoving();
        --this.respawnInvulnerabilityTicks;
        if (this.hurtResistantTime > 0) {
            --this.hurtResistantTime;
        }
        this.openContainer.detectAndSendChanges();
        if (!this.worldObj.isRemote && !this.openContainer.canInteractWith(this)) {
            this.closeScreen();
            this.openContainer = this.inventoryContainer;
        }
        while (!this.destroyedItemsNetCache.isEmpty()) {
            final int i = Math.min(this.destroyedItemsNetCache.size(), Integer.MAX_VALUE);
            final int[] aint = new int[i];
            final Iterator<Integer> iterator = this.destroyedItemsNetCache.iterator();
            int j = 0;
            while (iterator.hasNext() && j < i) {
                aint[j++] = iterator.next();
                iterator.remove();
            }
            this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(aint));
        }
        if (!this.loadedChunks.isEmpty()) {
            final List<Chunk> list = (List<Chunk>)Lists.newArrayList();
            final Iterator<ChunkCoordIntPair> iterator2 = this.loadedChunks.iterator();
            final List<TileEntity> list2 = (List<TileEntity>)Lists.newArrayList();
            while (iterator2.hasNext() && list.size() < 10) {
                final ChunkCoordIntPair chunkcoordintpair = iterator2.next();
                if (chunkcoordintpair != null) {
                    if (!this.worldObj.isBlockLoaded(new BlockPos(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4))) {
                        continue;
                    }
                    final Chunk chunk = this.worldObj.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
                    if (!chunk.isPopulated()) {
                        continue;
                    }
                    list.add(chunk);
                    list2.addAll(((WorldServer)this.worldObj).getTileEntitiesIn(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16));
                    iterator2.remove();
                }
                else {
                    iterator2.remove();
                }
            }
            if (!list.isEmpty()) {
                if (list.size() == 1) {
                    this.playerNetServerHandler.sendPacket(new S21PacketChunkData(list.get(0), true, 65535));
                }
                else {
                    this.playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(list));
                }
                for (final TileEntity tileentity : list2) {
                    this.sendTileEntityUpdate(tileentity);
                }
                for (final Chunk chunk2 : list) {
                    this.getServerForPlayer().getEntityTracker().func_85172_a(this, chunk2);
                }
            }
        }
        final Entity entity = this.getSpectatingEntity();
        if (entity != this) {
            if (!entity.isEntityAlive()) {
                this.setSpectatingEntity(this);
            }
            else {
                this.setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
                this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
                if (this.isSneaking()) {
                    this.setSpectatingEntity(this);
                }
            }
        }
    }
    
    public void onUpdateEntity() {
        try {
            super.onUpdate();
            for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
                final ItemStack itemstack = this.inventory.getStackInSlot(i);
                if (itemstack != null && itemstack.getItem().isMap()) {
                    final Packet packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.worldObj, this);
                    if (packet != null) {
                        this.playerNetServerHandler.sendPacket(packet);
                    }
                }
            }
            if (this.getHealth() != this.lastHealth || this.lastFoodLevel != this.foodStats.getFoodLevel() || this.foodStats.getSaturationLevel() == 0.0f != this.wasHungry) {
                this.playerNetServerHandler.sendPacket(new S06PacketUpdateHealth(this.getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
                this.lastHealth = this.getHealth();
                this.lastFoodLevel = this.foodStats.getFoodLevel();
                this.wasHungry = (this.foodStats.getSaturationLevel() == 0.0f);
            }
            if (this.getHealth() + this.getAbsorptionAmount() != this.combinedHealth) {
                this.combinedHealth = this.getHealth() + this.getAbsorptionAmount();
                for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health)) {
                    this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective).func_96651_a(Arrays.asList(this));
                }
            }
            if (this.experienceTotal != this.lastExperience) {
                this.lastExperience = this.experienceTotal;
                this.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
            }
            if (this.ticksExisted % 20 * 5 == 0 && !this.getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes)) {
                this.updateBiomesExplored();
            }
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
            this.addEntityCrashInfo(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }
    
    protected void updateBiomesExplored() {
        final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));
        final String s = biomegenbase.biomeName;
        JsonSerializableSet jsonserializableset = this.getStatFile().func_150870_b(AchievementList.exploreAllBiomes);
        if (jsonserializableset == null) {
            jsonserializableset = this.getStatFile().func_150872_a(AchievementList.exploreAllBiomes, new JsonSerializableSet());
        }
        jsonserializableset.add(s);
        if (this.getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes) && jsonserializableset.size() >= BiomeGenBase.explorationBiomesList.size()) {
            final Set<BiomeGenBase> set = (Set<BiomeGenBase>)Sets.newHashSet((Iterable<?>)BiomeGenBase.explorationBiomesList);
            for (final String s2 : jsonserializableset) {
                final Iterator<BiomeGenBase> iterator = set.iterator();
                while (iterator.hasNext()) {
                    final BiomeGenBase biomegenbase2 = iterator.next();
                    if (biomegenbase2.biomeName.equals(s2)) {
                        iterator.remove();
                    }
                }
                if (set.isEmpty()) {
                    break;
                }
            }
            if (set.isEmpty()) {
                this.triggerAchievement(AchievementList.exploreAllBiomes);
            }
        }
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (this.worldObj.getGameRules().getBoolean("showDeathMessages")) {
            final Team team = this.getTeam();
            if (team != null && team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS) {
                if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS) {
                    this.mcServer.getConfigurationManager().sendMessageToAllTeamMembers(this, this.getCombatTracker().getDeathMessage());
                }
                else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM) {
                    this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer(this, this.getCombatTracker().getDeathMessage());
                }
            }
            else {
                this.mcServer.getConfigurationManager().sendChatMsg(this.getCombatTracker().getDeathMessage());
            }
        }
        if (!this.worldObj.getGameRules().getBoolean("keepInventory")) {
            this.inventory.dropAllItems();
        }
        for (final ScoreObjective scoreobjective : this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount)) {
            final Score score = this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective);
            score.func_96648_a();
        }
        final EntityLivingBase entitylivingbase = this.func_94060_bK();
        if (entitylivingbase != null) {
            final EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.entityEggs.get(EntityList.getEntityID(entitylivingbase));
            if (entitylist$entityegginfo != null) {
                this.triggerAchievement(entitylist$entityegginfo.field_151513_e);
            }
            entitylivingbase.addToPlayerScore(this, this.scoreValue);
        }
        this.triggerAchievement(StatList.deathsStat);
        this.func_175145_a(StatList.timeSinceDeathStat);
        this.getCombatTracker().reset();
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        final boolean flag = this.mcServer.isDedicatedServer() && this.canPlayersAttack() && "fall".equals(source.damageType);
        if (!flag && this.respawnInvulnerabilityTicks > 0 && source != DamageSource.outOfWorld) {
            return false;
        }
        if (source instanceof EntityDamageSource) {
            final Entity entity = source.getEntity();
            if (entity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entity)) {
                return false;
            }
            if (entity instanceof EntityArrow) {
                final EntityArrow entityarrow = (EntityArrow)entity;
                if (entityarrow.shootingEntity instanceof EntityPlayer && !this.canAttackPlayer((EntityPlayer)entityarrow.shootingEntity)) {
                    return false;
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public boolean canAttackPlayer(final EntityPlayer other) {
        return this.canPlayersAttack() && super.canAttackPlayer(other);
    }
    
    private boolean canPlayersAttack() {
        return this.mcServer.isPVPEnabled();
    }
    
    @Override
    public void travelToDimension(int dimensionId) {
        if (this.dimension == 1 && dimensionId == 1) {
            this.triggerAchievement(AchievementList.theEnd2);
            this.worldObj.removeEntity(this);
            this.playerConqueredTheEnd = true;
            this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0f));
        }
        else {
            if (this.dimension == 0 && dimensionId == 1) {
                this.triggerAchievement(AchievementList.theEnd);
                final BlockPos blockpos = this.mcServer.worldServerForDimension(dimensionId).getSpawnCoordinate();
                if (blockpos != null) {
                    this.playerNetServerHandler.setPlayerLocation(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0f, 0.0f);
                }
                dimensionId = 1;
            }
            else {
                this.triggerAchievement(AchievementList.portal);
            }
            this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
            this.lastExperience = -1;
            this.lastHealth = -1.0f;
            this.lastFoodLevel = -1;
        }
    }
    
    @Override
    public boolean isSpectatedByPlayer(final EntityPlayerMP player) {
        return player.isSpectator() ? (this.getSpectatingEntity() == this) : (!this.isSpectator() && super.isSpectatedByPlayer(player));
    }
    
    private void sendTileEntityUpdate(final TileEntity p_147097_1_) {
        if (p_147097_1_ != null) {
            final Packet packet = p_147097_1_.getDescriptionPacket();
            if (packet != null) {
                this.playerNetServerHandler.sendPacket(packet);
            }
        }
    }
    
    @Override
    public void onItemPickup(final Entity p_71001_1_, final int p_71001_2_) {
        super.onItemPickup(p_71001_1_, p_71001_2_);
        this.openContainer.detectAndSendChanges();
    }
    
    @Override
    public EnumStatus trySleep(final BlockPos bedLocation) {
        final EnumStatus entityplayer$enumstatus = super.trySleep(bedLocation);
        if (entityplayer$enumstatus == EnumStatus.OK) {
            final Packet packet = new S0APacketUseBed(this, bedLocation);
            this.getServerForPlayer().getEntityTracker().sendToAllTrackingEntity(this, packet);
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.playerNetServerHandler.sendPacket(packet);
        }
        return entityplayer$enumstatus;
    }
    
    @Override
    public void wakeUpPlayer(final boolean p_70999_1_, final boolean updateWorldFlag, final boolean setSpawn) {
        if (this.isPlayerSleeping()) {
            this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 2));
        }
        super.wakeUpPlayer(p_70999_1_, updateWorldFlag, setSpawn);
        if (this.playerNetServerHandler != null) {
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
    @Override
    public void mountEntity(final Entity entityIn) {
        final Entity entity = this.ridingEntity;
        super.mountEntity(entityIn);
        if (entityIn != entity) {
            this.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this, this.ridingEntity));
            this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final Block blockIn, final BlockPos pos) {
    }
    
    public void handleFalling(final double p_71122_1_, final boolean p_71122_3_) {
        final int i = MathHelper.floor_double(this.posX);
        final int j = MathHelper.floor_double(this.posY - 0.20000000298023224);
        final int k = MathHelper.floor_double(this.posZ);
        BlockPos blockpos = new BlockPos(i, j, k);
        Block block = this.worldObj.getBlockState(blockpos).getBlock();
        if (block.getMaterial() == Material.air) {
            final Block block2 = this.worldObj.getBlockState(blockpos.down()).getBlock();
            if (block2 instanceof BlockFence || block2 instanceof BlockWall || block2 instanceof BlockFenceGate) {
                blockpos = blockpos.down();
                block = this.worldObj.getBlockState(blockpos).getBlock();
            }
        }
        super.updateFallState(p_71122_1_, p_71122_3_, block, blockpos);
    }
    
    @Override
    public void openEditSign(final TileEntitySign signTile) {
        signTile.setPlayer(this);
        this.playerNetServerHandler.sendPacket(new S36PacketSignEditorOpen(signTile.getPos()));
    }
    
    private void getNextWindowId() {
        this.currentWindowId = this.currentWindowId % 100 + 1;
    }
    
    @Override
    public void displayGui(final IInteractionObject guiOwner) {
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
        this.openContainer = guiOwner.createContainer(this.inventory, this);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }
    
    @Override
    public void displayGUIChest(final IInventory chestInventory) {
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }
        if (chestInventory instanceof ILockableContainer) {
            final ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;
            if (ilockablecontainer.isLocked() && !this.canOpen(ilockablecontainer.getLockCode()) && !this.isSpectator()) {
                this.playerNetServerHandler.sendPacket(new S02PacketChat(new ChatComponentTranslation("container.isLocked", new Object[] { chestInventory.getDisplayName() }), (byte)2));
                this.playerNetServerHandler.sendPacket(new S29PacketSoundEffect("random.door_close", this.posX, this.posY, this.posZ, 1.0f, 1.0f));
                return;
            }
        }
        this.getNextWindowId();
        if (chestInventory instanceof IInteractionObject) {
            this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
            this.openContainer = ((IInteractionObject)chestInventory).createContainer(this.inventory, this);
        }
        else {
            this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
            this.openContainer = new ContainerChest(this.inventory, chestInventory, this);
        }
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }
    
    @Override
    public void displayVillagerTradeGui(final IMerchant villager) {
        this.getNextWindowId();
        this.openContainer = new ContainerMerchant(this.inventory, villager, this.worldObj);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
        final IInventory iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
        final IChatComponent ichatcomponent = villager.getDisplayName();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "minecraft:villager", ichatcomponent, iinventory.getSizeInventory()));
        final MerchantRecipeList merchantrecipelist = villager.getRecipes(this);
        if (merchantrecipelist != null) {
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.currentWindowId);
            merchantrecipelist.writeToBuf(packetbuffer);
            this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|TrList", packetbuffer));
        }
    }
    
    @Override
    public void displayGUIHorse(final EntityHorse horse, final IInventory horseInventory) {
        if (this.openContainer != this.inventoryContainer) {
            this.closeScreen();
        }
        this.getNextWindowId();
        this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
        this.openContainer = new ContainerHorseInventory(this.inventory, horseInventory, horse, this);
        this.openContainer.windowId = this.currentWindowId;
        this.openContainer.onCraftGuiOpened(this);
    }
    
    @Override
    public void displayGUIBook(final ItemStack bookStack) {
        final Item item = bookStack.getItem();
        if (item == Items.written_book) {
            this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer())));
        }
    }
    
    @Override
    public void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {
        if (!(containerToSend.getSlot(slotInd) instanceof SlotCrafting) && !this.isChangingQuantityOnly) {
            this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));
        }
    }
    
    public void sendContainerToPlayer(final Container p_71120_1_) {
        this.updateCraftingInventory(p_71120_1_, p_71120_1_.getInventory());
    }
    
    @Override
    public void updateCraftingInventory(final Container containerToSend, final List<ItemStack> itemsList) {
        this.playerNetServerHandler.sendPacket(new S30PacketWindowItems(containerToSend.windowId, itemsList));
        this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
    }
    
    @Override
    public void sendProgressBarUpdate(final Container containerIn, final int varToUpdate, final int newValue) {
        this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
    }
    
    @Override
    public void func_175173_a(final Container p_175173_1_, final IInventory p_175173_2_) {
        for (int i = 0; i < p_175173_2_.getFieldCount(); ++i) {
            this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(p_175173_1_.windowId, i, p_175173_2_.getField(i)));
        }
    }
    
    public void closeScreen() {
        this.playerNetServerHandler.sendPacket(new S2EPacketCloseWindow(this.openContainer.windowId));
        this.closeContainer();
    }
    
    public void updateHeldItem() {
        if (!this.isChangingQuantityOnly) {
            this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
        }
    }
    
    public void closeContainer() {
        this.openContainer.onContainerClosed(this);
        this.openContainer = this.inventoryContainer;
    }
    
    public void setEntityActionState(final float p_110430_1_, final float p_110430_2_, final boolean p_110430_3_, final boolean sneaking) {
        if (this.ridingEntity != null) {
            if (p_110430_1_ >= -1.0f && p_110430_1_ <= 1.0f) {
                this.moveStrafing = p_110430_1_;
            }
            if (p_110430_2_ >= -1.0f && p_110430_2_ <= 1.0f) {
                this.moveForward = p_110430_2_;
            }
            this.isJumping = p_110430_3_;
            this.setSneaking(sneaking);
        }
    }
    
    @Override
    public void addStat(final StatBase stat, final int amount) {
        if (stat != null) {
            this.statsFile.increaseStat(this, stat, amount);
            for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(stat.func_150952_k())) {
                this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective).increseScore(amount);
            }
            if (this.statsFile.func_150879_e()) {
                this.statsFile.func_150876_a(this);
            }
        }
    }
    
    @Override
    public void func_175145_a(final StatBase p_175145_1_) {
        if (p_175145_1_ != null) {
            this.statsFile.unlockAchievement(this, p_175145_1_, 0);
            for (final ScoreObjective scoreobjective : this.getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.func_150952_k())) {
                this.getWorldScoreboard().getValueFromObjective(this.getName(), scoreobjective).setScorePoints(0);
            }
            if (this.statsFile.func_150879_e()) {
                this.statsFile.func_150876_a(this);
            }
        }
    }
    
    public void mountEntityAndWakeUp() {
        if (this.riddenByEntity != null) {
            this.riddenByEntity.mountEntity(this);
        }
        if (this.sleeping) {
            this.wakeUpPlayer(true, false, false);
        }
    }
    
    public void setPlayerHealthUpdated() {
        this.lastHealth = -1.0E8f;
    }
    
    @Override
    public void addChatComponentMessage(final IChatComponent chatComponent) {
        this.playerNetServerHandler.sendPacket(new S02PacketChat(chatComponent));
    }
    
    @Override
    protected void onItemUseFinish() {
        this.playerNetServerHandler.sendPacket(new S19PacketEntityStatus(this, (byte)9));
        super.onItemUseFinish();
    }
    
    @Override
    public void setItemInUse(final ItemStack stack, final int duration) {
        super.setItemInUse(stack, duration);
        if (stack != null && stack.getItem() != null && stack.getItem().getItemUseAction(stack) == EnumAction.EAT) {
            this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 3));
        }
    }
    
    @Override
    public void clonePlayer(final EntityPlayer oldPlayer, final boolean respawnFromEnd) {
        super.clonePlayer(oldPlayer, respawnFromEnd);
        this.lastExperience = -1;
        this.lastHealth = -1.0f;
        this.lastFoodLevel = -1;
        this.destroyedItemsNetCache.addAll(((EntityPlayerMP)oldPlayer).destroyedItemsNetCache);
    }
    
    @Override
    protected void onNewPotionEffect(final PotionEffect id) {
        super.onNewPotionEffect(id);
        this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.getEntityId(), id));
    }
    
    @Override
    protected void onChangedPotionEffect(final PotionEffect id, final boolean p_70695_2_) {
        super.onChangedPotionEffect(id, p_70695_2_);
        this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(this.getEntityId(), id));
    }
    
    @Override
    protected void onFinishedPotionEffect(final PotionEffect p_70688_1_) {
        super.onFinishedPotionEffect(p_70688_1_);
        this.playerNetServerHandler.sendPacket(new S1EPacketRemoveEntityEffect(this.getEntityId(), p_70688_1_));
    }
    
    @Override
    public void setPositionAndUpdate(final double x, final double y, final double z) {
        this.playerNetServerHandler.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
    }
    
    @Override
    public void onCriticalHit(final Entity entityHit) {
        this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 4));
    }
    
    @Override
    public void onEnchantmentCritical(final Entity entityHit) {
        this.getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 5));
    }
    
    @Override
    public void sendPlayerAbilities() {
        if (this.playerNetServerHandler != null) {
            this.playerNetServerHandler.sendPacket(new S39PacketPlayerAbilities(this.capabilities));
            this.updatePotionMetadata();
        }
    }
    
    public WorldServer getServerForPlayer() {
        return (WorldServer)this.worldObj;
    }
    
    @Override
    public void setGameType(final WorldSettings.GameType gameType) {
        this.theItemInWorldManager.setGameType(gameType);
        this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, (float)gameType.getID()));
        if (gameType == WorldSettings.GameType.SPECTATOR) {
            this.mountEntity(null);
        }
        else {
            this.setSpectatingEntity(this);
        }
        this.sendPlayerAbilities();
        this.markPotionsDirty();
    }
    
    @Override
    public boolean isSpectator() {
        return this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR;
    }
    
    @Override
    public void addChatMessage(final IChatComponent component) {
        this.playerNetServerHandler.sendPacket(new S02PacketChat(component));
    }
    
    @Override
    public boolean canCommandSenderUseCommand(final int permLevel, final String commandName) {
        if ("seed".equals(commandName) && !this.mcServer.isDedicatedServer()) {
            return true;
        }
        if ("tell".equals(commandName) || "help".equals(commandName) || "me".equals(commandName) || "trigger".equals(commandName)) {
            return true;
        }
        if (this.mcServer.getConfigurationManager().canSendCommands(this.getGameProfile())) {
            final UserListOpsEntry userlistopsentry = this.mcServer.getConfigurationManager().getOppedPlayers().getEntry(this.getGameProfile());
            return (userlistopsentry != null) ? (userlistopsentry.getPermissionLevel() >= permLevel) : (this.mcServer.getOpPermissionLevel() >= permLevel);
        }
        return false;
    }
    
    public String getPlayerIP() {
        String s = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
        s = s.substring(s.indexOf("/") + 1);
        s = s.substring(0, s.indexOf(":"));
        return s;
    }
    
    public void handleClientSettings(final C15PacketClientSettings packetIn) {
        this.translator = packetIn.getLang();
        this.chatVisibility = packetIn.getChatVisibility();
        this.chatColours = packetIn.isColorsEnabled();
        this.getDataWatcher().updateObject(10, (byte)packetIn.getModelPartFlags());
    }
    
    public EnumChatVisibility getChatVisibility() {
        return this.chatVisibility;
    }
    
    public void loadResourcePack(final String url, final String hash) {
        this.playerNetServerHandler.sendPacket(new S48PacketResourcePackSend(url, hash));
    }
    
    @Override
    public BlockPos getPosition() {
        return new BlockPos(this.posX, this.posY + 0.5, this.posZ);
    }
    
    public void markPlayerActive() {
        this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
    }
    
    public StatisticsFile getStatFile() {
        return this.statsFile;
    }
    
    public void removeEntity(final Entity p_152339_1_) {
        if (p_152339_1_ instanceof EntityPlayer) {
            this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(new int[] { p_152339_1_.getEntityId() }));
        }
        else {
            this.destroyedItemsNetCache.add(p_152339_1_.getEntityId());
        }
    }
    
    @Override
    protected void updatePotionMetadata() {
        if (this.isSpectator()) {
            this.resetPotionEffectMetadata();
            this.setInvisible(true);
        }
        else {
            super.updatePotionMetadata();
        }
        this.getServerForPlayer().getEntityTracker().func_180245_a(this);
    }
    
    public Entity getSpectatingEntity() {
        return (this.spectatingEntity == null) ? this : this.spectatingEntity;
    }
    
    public void setSpectatingEntity(final Entity entityToSpectate) {
        final Entity entity = this.getSpectatingEntity();
        this.spectatingEntity = ((entityToSpectate == null) ? this : entityToSpectate);
        if (entity != this.spectatingEntity) {
            this.playerNetServerHandler.sendPacket(new S43PacketCamera(this.spectatingEntity));
            this.setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
        }
    }
    
    @Override
    public void attackTargetEntityWithCurrentItem(final Entity targetEntity) {
        if (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR) {
            this.setSpectatingEntity(targetEntity);
        }
        else {
            super.attackTargetEntityWithCurrentItem(targetEntity);
        }
    }
    
    public long getLastActiveTime() {
        return this.playerLastActiveTime;
    }
    
    public IChatComponent getTabListDisplayName() {
        return null;
    }
}
