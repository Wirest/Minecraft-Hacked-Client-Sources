/*      */ package net.minecraft.entity.player;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import java.util.Arrays;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Random;
/*      */ import java.util.Set;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.DataWatcher;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.EntityList.EntityEggInfo;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.EntityTracker;
/*      */ import net.minecraft.entity.IMerchant;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.ContainerChest;
/*      */ import net.minecraft.inventory.ContainerMerchant;
/*      */ import net.minecraft.inventory.ICrafting;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.item.EnumAction;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemMapBase;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.network.NetHandlerPlayServer;
/*      */ import net.minecraft.network.NetworkManager;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.PacketBuffer;
/*      */ import net.minecraft.network.play.client.C15PacketClientSettings;
/*      */ import net.minecraft.network.play.server.S02PacketChat;
/*      */ import net.minecraft.network.play.server.S0APacketUseBed;
/*      */ import net.minecraft.network.play.server.S0BPacketAnimation;
/*      */ import net.minecraft.network.play.server.S13PacketDestroyEntities;
/*      */ import net.minecraft.network.play.server.S1BPacketEntityAttach;
/*      */ import net.minecraft.network.play.server.S1DPacketEntityEffect;
/*      */ import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
/*      */ import net.minecraft.network.play.server.S21PacketChunkData;
/*      */ import net.minecraft.network.play.server.S26PacketMapChunkBulk;
/*      */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*      */ import net.minecraft.network.play.server.S2DPacketOpenWindow;
/*      */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*      */ import net.minecraft.network.play.server.S30PacketWindowItems;
/*      */ import net.minecraft.network.play.server.S31PacketWindowProperty;
/*      */ import net.minecraft.network.play.server.S36PacketSignEditorOpen;
/*      */ import net.minecraft.network.play.server.S3FPacketCustomPayload;
/*      */ import net.minecraft.network.play.server.S42PacketCombatEvent;
/*      */ import net.minecraft.network.play.server.S42PacketCombatEvent.Event;
/*      */ import net.minecraft.potion.PotionEffect;
/*      */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.scoreboard.Team.EnumVisible;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.server.management.ItemInWorldManager;
/*      */ import net.minecraft.server.management.ServerConfigurationManager;
/*      */ import net.minecraft.server.management.UserListOpsEntry;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.stats.StatList;
/*      */ import net.minecraft.stats.StatisticsFile;
/*      */ import net.minecraft.tileentity.TileEntity;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentTranslation;
/*      */ import net.minecraft.util.CombatTracker;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.FoodStats;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.JsonSerializableSet;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.village.MerchantRecipeList;
/*      */ import net.minecraft.world.ChunkCoordIntPair;
/*      */ import net.minecraft.world.GameRules;
/*      */ import net.minecraft.world.IInteractionObject;
/*      */ import net.minecraft.world.ILockableContainer;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldServer;
/*      */ import net.minecraft.world.WorldSettings.GameType;
/*      */ import net.minecraft.world.biome.BiomeGenBase;
/*      */ import net.minecraft.world.border.WorldBorder;
/*      */ import net.minecraft.world.chunk.Chunk;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ 
/*      */ public class EntityPlayerMP extends EntityPlayer implements ICrafting
/*      */ {
/*  101 */   private static final Logger logger = ;
/*  102 */   private String translator = "en_US";
/*      */   
/*      */ 
/*      */   public NetHandlerPlayServer playerNetServerHandler;
/*      */   
/*      */ 
/*      */   public final MinecraftServer mcServer;
/*      */   
/*      */ 
/*      */   public final ItemInWorldManager theItemInWorldManager;
/*      */   
/*      */ 
/*      */   public double managedPosX;
/*      */   
/*      */ 
/*      */   public double managedPosZ;
/*      */   
/*      */ 
/*  120 */   public final List<ChunkCoordIntPair> loadedChunks = Lists.newLinkedList();
/*  121 */   private final List<Integer> destroyedItemsNetCache = Lists.newLinkedList();
/*      */   
/*      */ 
/*      */   private final StatisticsFile statsFile;
/*      */   
/*      */ 
/*  127 */   private float combinedHealth = Float.MIN_VALUE;
/*      */   
/*      */ 
/*  130 */   private float lastHealth = -1.0E8F;
/*      */   
/*      */ 
/*  133 */   private int lastFoodLevel = -99999999;
/*      */   
/*      */ 
/*  136 */   private boolean wasHungry = true;
/*      */   
/*      */ 
/*  139 */   private int lastExperience = -99999999;
/*  140 */   private int respawnInvulnerabilityTicks = 60;
/*      */   private EntityPlayer.EnumChatVisibility chatVisibility;
/*  142 */   private boolean chatColours = true;
/*  143 */   private long playerLastActiveTime = System.currentTimeMillis();
/*      */   
/*      */ 
/*  146 */   private Entity spectatingEntity = null;
/*      */   
/*      */ 
/*      */ 
/*      */   private int currentWindowId;
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isChangingQuantityOnly;
/*      */   
/*      */ 
/*      */ 
/*      */   public int ping;
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean playerConqueredTheEnd;
/*      */   
/*      */ 
/*      */ 
/*      */   public EntityPlayerMP(MinecraftServer server, WorldServer worldIn, GameProfile profile, ItemInWorldManager interactionManager)
/*      */   {
/*  168 */     super(worldIn, profile);
/*  169 */     interactionManager.thisPlayerMP = this;
/*  170 */     this.theItemInWorldManager = interactionManager;
/*  171 */     BlockPos blockpos = worldIn.getSpawnPoint();
/*      */     
/*  173 */     if ((!worldIn.provider.getHasNoSky()) && (worldIn.getWorldInfo().getGameType() != WorldSettings.GameType.ADVENTURE))
/*      */     {
/*  175 */       int i = Math.max(5, server.getSpawnProtectionSize() - 6);
/*  176 */       int j = MathHelper.floor_double(worldIn.getWorldBorder().getClosestDistance(blockpos.getX(), blockpos.getZ()));
/*      */       
/*  178 */       if (j < i)
/*      */       {
/*  180 */         i = j;
/*      */       }
/*      */       
/*  183 */       if (j <= 1)
/*      */       {
/*  185 */         i = 1;
/*      */       }
/*      */       
/*  188 */       blockpos = worldIn.getTopSolidOrLiquidBlock(blockpos.add(this.rand.nextInt(i * 2) - i, 0, this.rand.nextInt(i * 2) - i));
/*      */     }
/*      */     
/*  191 */     this.mcServer = server;
/*  192 */     this.statsFile = server.getConfigurationManager().getPlayerStatsFile(this);
/*  193 */     this.stepHeight = 0.0F;
/*  194 */     moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
/*      */     
/*  196 */     while ((!worldIn.getCollidingBoundingBoxes(this, getEntityBoundingBox()).isEmpty()) && (this.posY < 255.0D))
/*      */     {
/*  198 */       setPosition(this.posX, this.posY + 1.0D, this.posZ);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*      */   {
/*  207 */     super.readEntityFromNBT(tagCompund);
/*      */     
/*  209 */     if (tagCompund.hasKey("playerGameType", 99))
/*      */     {
/*  211 */       if (MinecraftServer.getServer().getForceGamemode())
/*      */       {
/*  213 */         this.theItemInWorldManager.setGameType(MinecraftServer.getServer().getGameType());
/*      */       }
/*      */       else
/*      */       {
/*  217 */         this.theItemInWorldManager.setGameType(WorldSettings.GameType.getByID(tagCompund.getInteger("playerGameType")));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*      */   {
/*  227 */     super.writeEntityToNBT(tagCompound);
/*  228 */     tagCompound.setInteger("playerGameType", this.theItemInWorldManager.getGameType().getID());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addExperienceLevel(int levels)
/*      */   {
/*  236 */     super.addExperienceLevel(levels);
/*  237 */     this.lastExperience = -1;
/*      */   }
/*      */   
/*      */   public void removeExperienceLevel(int levels)
/*      */   {
/*  242 */     super.removeExperienceLevel(levels);
/*  243 */     this.lastExperience = -1;
/*      */   }
/*      */   
/*      */   public void addSelfToInternalCraftingInventory()
/*      */   {
/*  248 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendEnterCombat()
/*      */   {
/*  256 */     super.sendEnterCombat();
/*  257 */     this.playerNetServerHandler.sendPacket(new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.ENTER_COMBAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendEndCombat()
/*      */   {
/*  265 */     super.sendEndCombat();
/*  266 */     this.playerNetServerHandler.sendPacket(new S42PacketCombatEvent(getCombatTracker(), S42PacketCombatEvent.Event.END_COMBAT));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onUpdate()
/*      */   {
/*  274 */     this.theItemInWorldManager.updateBlockRemoving();
/*  275 */     this.respawnInvulnerabilityTicks -= 1;
/*      */     
/*  277 */     if (this.hurtResistantTime > 0)
/*      */     {
/*  279 */       this.hurtResistantTime -= 1;
/*      */     }
/*      */     
/*  282 */     this.openContainer.detectAndSendChanges();
/*      */     
/*  284 */     if ((!this.worldObj.isRemote) && (!this.openContainer.canInteractWith(this)))
/*      */     {
/*  286 */       closeScreen();
/*  287 */       this.openContainer = this.inventoryContainer;
/*      */     }
/*      */     
/*  290 */     while (!this.destroyedItemsNetCache.isEmpty())
/*      */     {
/*  292 */       int i = Math.min(this.destroyedItemsNetCache.size(), Integer.MAX_VALUE);
/*  293 */       int[] aint = new int[i];
/*  294 */       Iterator<Integer> iterator = this.destroyedItemsNetCache.iterator();
/*  295 */       int j = 0;
/*      */       
/*  297 */       while ((iterator.hasNext()) && (j < i))
/*      */       {
/*  299 */         aint[(j++)] = ((Integer)iterator.next()).intValue();
/*  300 */         iterator.remove();
/*      */       }
/*      */       
/*  303 */       this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(aint));
/*      */     }
/*      */     
/*  306 */     if (!this.loadedChunks.isEmpty())
/*      */     {
/*  308 */       List<Chunk> list = Lists.newArrayList();
/*  309 */       Iterator<ChunkCoordIntPair> iterator1 = this.loadedChunks.iterator();
/*  310 */       List<TileEntity> list1 = Lists.newArrayList();
/*      */       Chunk chunk;
/*  312 */       while ((iterator1.hasNext()) && (list.size() < 10))
/*      */       {
/*  314 */         ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)iterator1.next();
/*      */         
/*  316 */         if (chunkcoordintpair != null)
/*      */         {
/*  318 */           if (this.worldObj.isBlockLoaded(new BlockPos(chunkcoordintpair.chunkXPos << 4, 0, chunkcoordintpair.chunkZPos << 4)))
/*      */           {
/*  320 */             chunk = this.worldObj.getChunkFromChunkCoords(chunkcoordintpair.chunkXPos, chunkcoordintpair.chunkZPos);
/*      */             
/*  322 */             if (chunk.isPopulated())
/*      */             {
/*  324 */               list.add(chunk);
/*  325 */               list1.addAll(((WorldServer)this.worldObj).getTileEntitiesIn(chunkcoordintpair.chunkXPos * 16, 0, chunkcoordintpair.chunkZPos * 16, chunkcoordintpair.chunkXPos * 16 + 16, 256, chunkcoordintpair.chunkZPos * 16 + 16));
/*  326 */               iterator1.remove();
/*      */             }
/*      */             
/*      */           }
/*      */         }
/*      */         else {
/*  332 */           iterator1.remove();
/*      */         }
/*      */       }
/*      */       
/*  336 */       if (!list.isEmpty())
/*      */       {
/*  338 */         if (list.size() == 1)
/*      */         {
/*  340 */           this.playerNetServerHandler.sendPacket(new S21PacketChunkData((Chunk)list.get(0), true, 65535));
/*      */         }
/*      */         else
/*      */         {
/*  344 */           this.playerNetServerHandler.sendPacket(new S26PacketMapChunkBulk(list));
/*      */         }
/*      */         
/*  347 */         for (TileEntity tileentity : list1)
/*      */         {
/*  349 */           sendTileEntityUpdate(tileentity);
/*      */         }
/*      */         
/*  352 */         for (Chunk chunk1 : list)
/*      */         {
/*  354 */           getServerForPlayer().getEntityTracker().func_85172_a(this, chunk1);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  359 */     Entity entity = getSpectatingEntity();
/*      */     
/*  361 */     if (entity != this)
/*      */     {
/*  363 */       if (!entity.isEntityAlive())
/*      */       {
/*  365 */         setSpectatingEntity(this);
/*      */       }
/*      */       else
/*      */       {
/*  369 */         setPositionAndRotation(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
/*  370 */         this.mcServer.getConfigurationManager().serverUpdateMountedMovingPlayer(this);
/*      */         
/*  372 */         if (isSneaking())
/*      */         {
/*  374 */           setSpectatingEntity(this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void onUpdateEntity()
/*      */   {
/*      */     try
/*      */     {
/*  384 */       super.onUpdate();
/*      */       ItemStack itemstack;
/*  386 */       for (int i = 0; i < this.inventory.getSizeInventory(); i++)
/*      */       {
/*  388 */         itemstack = this.inventory.getStackInSlot(i);
/*      */         
/*  390 */         if ((itemstack != null) && (itemstack.getItem().isMap()))
/*      */         {
/*  392 */           Packet packet = ((ItemMapBase)itemstack.getItem()).createMapDataPacket(itemstack, this.worldObj, this);
/*      */           
/*  394 */           if (packet != null)
/*      */           {
/*  396 */             this.playerNetServerHandler.sendPacket(packet);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  401 */       if ((getHealth() == this.lastHealth) && (this.lastFoodLevel == this.foodStats.getFoodLevel())) { if ((this.foodStats.getSaturationLevel() == 0.0F) == this.wasHungry) {}
/*      */       } else {
/*  403 */         this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S06PacketUpdateHealth(getHealth(), this.foodStats.getFoodLevel(), this.foodStats.getSaturationLevel()));
/*  404 */         this.lastHealth = getHealth();
/*  405 */         this.lastFoodLevel = this.foodStats.getFoodLevel();
/*  406 */         this.wasHungry = (this.foodStats.getSaturationLevel() == 0.0F);
/*      */       }
/*      */       
/*  409 */       if (getHealth() + getAbsorptionAmount() != this.combinedHealth)
/*      */       {
/*  411 */         this.combinedHealth = (getHealth() + getAbsorptionAmount());
/*      */         
/*  413 */         for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.health))
/*      */         {
/*  415 */           getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).func_96651_a(Arrays.asList(new EntityPlayer[] { this }));
/*      */         }
/*      */       }
/*      */       
/*  419 */       if (this.experienceTotal != this.lastExperience)
/*      */       {
/*  421 */         this.lastExperience = this.experienceTotal;
/*  422 */         this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S1FPacketSetExperience(this.experience, this.experienceTotal, this.experienceLevel));
/*      */       }
/*      */       
/*  425 */       if ((this.ticksExisted % 20 * 5 == 0) && (!getStatFile().hasAchievementUnlocked(AchievementList.exploreAllBiomes)))
/*      */       {
/*  427 */         updateBiomesExplored();
/*      */       }
/*      */     }
/*      */     catch (Throwable throwable)
/*      */     {
/*  432 */       CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Ticking player");
/*  433 */       CrashReportCategory crashreportcategory = crashreport.makeCategory("Player being ticked");
/*  434 */       addEntityCrashInfo(crashreportcategory);
/*  435 */       throw new net.minecraft.util.ReportedException(crashreport);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateBiomesExplored()
/*      */   {
/*  444 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));
/*  445 */     String s = biomegenbase.biomeName;
/*  446 */     JsonSerializableSet jsonserializableset = (JsonSerializableSet)getStatFile().func_150870_b(AchievementList.exploreAllBiomes);
/*      */     
/*  448 */     if (jsonserializableset == null)
/*      */     {
/*  450 */       jsonserializableset = (JsonSerializableSet)getStatFile().func_150872_a(AchievementList.exploreAllBiomes, new JsonSerializableSet());
/*      */     }
/*      */     
/*  453 */     jsonserializableset.add(s);
/*      */     
/*  455 */     if ((getStatFile().canUnlockAchievement(AchievementList.exploreAllBiomes)) && (jsonserializableset.size() >= BiomeGenBase.explorationBiomesList.size()))
/*      */     {
/*  457 */       Set<BiomeGenBase> set = Sets.newHashSet(BiomeGenBase.explorationBiomesList);
/*      */       
/*  459 */       for (String s1 : jsonserializableset)
/*      */       {
/*  461 */         Iterator<BiomeGenBase> iterator = set.iterator();
/*      */         
/*  463 */         while (iterator.hasNext())
/*      */         {
/*  465 */           BiomeGenBase biomegenbase1 = (BiomeGenBase)iterator.next();
/*      */           
/*  467 */           if (biomegenbase1.biomeName.equals(s1))
/*      */           {
/*  469 */             iterator.remove();
/*      */           }
/*      */         }
/*      */         
/*  473 */         if (set.isEmpty()) {
/*      */           break;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  479 */       if (set.isEmpty())
/*      */       {
/*  481 */         triggerAchievement(AchievementList.exploreAllBiomes);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onDeath(DamageSource cause)
/*      */   {
/*  491 */     if (this.worldObj.getGameRules().getBoolean("showDeathMessages"))
/*      */     {
/*  493 */       Team team = getTeam();
/*      */       
/*  495 */       if ((team != null) && (team.getDeathMessageVisibility() != Team.EnumVisible.ALWAYS))
/*      */       {
/*  497 */         if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OTHER_TEAMS)
/*      */         {
/*  499 */           this.mcServer.getConfigurationManager().sendMessageToAllTeamMembers(this, getCombatTracker().getDeathMessage());
/*      */         }
/*  501 */         else if (team.getDeathMessageVisibility() == Team.EnumVisible.HIDE_FOR_OWN_TEAM)
/*      */         {
/*  503 */           this.mcServer.getConfigurationManager().sendMessageToTeamOrEvryPlayer(this, getCombatTracker().getDeathMessage());
/*      */         }
/*      */         
/*      */       }
/*      */       else {
/*  508 */         this.mcServer.getConfigurationManager().sendChatMsg(getCombatTracker().getDeathMessage());
/*      */       }
/*      */     }
/*      */     
/*  512 */     if (!this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/*  514 */       this.inventory.dropAllItems();
/*      */     }
/*      */     
/*  517 */     for (ScoreObjective scoreobjective : this.worldObj.getScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.deathCount))
/*      */     {
/*  519 */       Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
/*  520 */       score.func_96648_a();
/*      */     }
/*      */     
/*  523 */     EntityLivingBase entitylivingbase = func_94060_bK();
/*      */     
/*  525 */     if (entitylivingbase != null)
/*      */     {
/*  527 */       EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID(entitylivingbase)));
/*      */       
/*  529 */       if (entitylist$entityegginfo != null)
/*      */       {
/*  531 */         triggerAchievement(entitylist$entityegginfo.field_151513_e);
/*      */       }
/*      */       
/*  534 */       entitylivingbase.addToPlayerScore(this, this.scoreValue);
/*      */     }
/*      */     
/*  537 */     triggerAchievement(StatList.deathsStat);
/*  538 */     func_175145_a(StatList.timeSinceDeathStat);
/*  539 */     getCombatTracker().reset();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean attackEntityFrom(DamageSource source, float amount)
/*      */   {
/*  547 */     if (isEntityInvulnerable(source))
/*      */     {
/*  549 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  553 */     boolean flag = (this.mcServer.isDedicatedServer()) && (canPlayersAttack()) && ("fall".equals(source.damageType));
/*      */     
/*  555 */     if ((!flag) && (this.respawnInvulnerabilityTicks > 0) && (source != DamageSource.outOfWorld))
/*      */     {
/*  557 */       return false;
/*      */     }
/*      */     
/*      */ 
/*  561 */     if ((source instanceof net.minecraft.util.EntityDamageSource))
/*      */     {
/*  563 */       Entity entity = source.getEntity();
/*      */       
/*  565 */       if (((entity instanceof EntityPlayer)) && (!canAttackPlayer((EntityPlayer)entity)))
/*      */       {
/*  567 */         return false;
/*      */       }
/*      */       
/*  570 */       if ((entity instanceof EntityArrow))
/*      */       {
/*  572 */         EntityArrow entityarrow = (EntityArrow)entity;
/*      */         
/*  574 */         if (((entityarrow.shootingEntity instanceof EntityPlayer)) && (!canAttackPlayer((EntityPlayer)entityarrow.shootingEntity)))
/*      */         {
/*  576 */           return false;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  581 */     return super.attackEntityFrom(source, amount);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean canAttackPlayer(EntityPlayer other)
/*      */   {
/*  588 */     return !canPlayersAttack() ? false : super.canAttackPlayer(other);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean canPlayersAttack()
/*      */   {
/*  596 */     return this.mcServer.isPVPEnabled();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void travelToDimension(int dimensionId)
/*      */   {
/*  604 */     if ((this.dimension == 1) && (dimensionId == 1))
/*      */     {
/*  606 */       triggerAchievement(AchievementList.theEnd2);
/*  607 */       this.worldObj.removeEntity(this);
/*  608 */       this.playerConqueredTheEnd = true;
/*  609 */       this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(4, 0.0F));
/*      */     }
/*      */     else
/*      */     {
/*  613 */       if ((this.dimension == 0) && (dimensionId == 1))
/*      */       {
/*  615 */         triggerAchievement(AchievementList.theEnd);
/*  616 */         BlockPos blockpos = this.mcServer.worldServerForDimension(dimensionId).getSpawnCoordinate();
/*      */         
/*  618 */         if (blockpos != null)
/*      */         {
/*  620 */           this.playerNetServerHandler.setPlayerLocation(blockpos.getX(), blockpos.getY(), blockpos.getZ(), 0.0F, 0.0F);
/*      */         }
/*      */         
/*  623 */         dimensionId = 1;
/*      */       }
/*      */       else
/*      */       {
/*  627 */         triggerAchievement(AchievementList.portal);
/*      */       }
/*      */       
/*  630 */       this.mcServer.getConfigurationManager().transferPlayerToDimension(this, dimensionId);
/*  631 */       this.lastExperience = -1;
/*  632 */       this.lastHealth = -1.0F;
/*  633 */       this.lastFoodLevel = -1;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean isSpectatedByPlayer(EntityPlayerMP player)
/*      */   {
/*  639 */     return isSpectator() ? false : player.isSpectator() ? false : getSpectatingEntity() == this ? true : super.isSpectatedByPlayer(player);
/*      */   }
/*      */   
/*      */   private void sendTileEntityUpdate(TileEntity p_147097_1_)
/*      */   {
/*  644 */     if (p_147097_1_ != null)
/*      */     {
/*  646 */       Packet packet = p_147097_1_.getDescriptionPacket();
/*      */       
/*  648 */       if (packet != null)
/*      */       {
/*  650 */         this.playerNetServerHandler.sendPacket(packet);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onItemPickup(Entity p_71001_1_, int p_71001_2_)
/*      */   {
/*  660 */     super.onItemPickup(p_71001_1_, p_71001_2_);
/*  661 */     this.openContainer.detectAndSendChanges();
/*      */   }
/*      */   
/*      */   public EntityPlayer.EnumStatus trySleep(BlockPos bedLocation)
/*      */   {
/*  666 */     EntityPlayer.EnumStatus entityplayer$enumstatus = super.trySleep(bedLocation);
/*      */     
/*  668 */     if (entityplayer$enumstatus == EntityPlayer.EnumStatus.OK)
/*      */     {
/*  670 */       Packet packet = new S0APacketUseBed(this, bedLocation);
/*  671 */       getServerForPlayer().getEntityTracker().sendToAllTrackingEntity(this, packet);
/*  672 */       this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*  673 */       this.playerNetServerHandler.sendPacket(packet);
/*      */     }
/*      */     
/*  676 */     return entityplayer$enumstatus;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void wakeUpPlayer(boolean p_70999_1_, boolean updateWorldFlag, boolean setSpawn)
/*      */   {
/*  684 */     if (isPlayerSleeping())
/*      */     {
/*  686 */       getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 2));
/*      */     }
/*      */     
/*  689 */     super.wakeUpPlayer(p_70999_1_, updateWorldFlag, setSpawn);
/*      */     
/*  691 */     if (this.playerNetServerHandler != null)
/*      */     {
/*  693 */       this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void mountEntity(Entity entityIn)
/*      */   {
/*  702 */     Entity entity = this.ridingEntity;
/*  703 */     super.mountEntity(entityIn);
/*      */     
/*  705 */     if (entityIn != entity)
/*      */     {
/*  707 */       this.playerNetServerHandler.sendPacket(new S1BPacketEntityAttach(0, this, this.ridingEntity));
/*  708 */       this.playerNetServerHandler.setPlayerLocation(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public void handleFalling(double p_71122_1_, boolean p_71122_3_)
/*      */   {
/*  721 */     int i = MathHelper.floor_double(this.posX);
/*  722 */     int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/*  723 */     int k = MathHelper.floor_double(this.posZ);
/*  724 */     BlockPos blockpos = new BlockPos(i, j, k);
/*  725 */     Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*      */     
/*  727 */     if (block.getMaterial() == net.minecraft.block.material.Material.air)
/*      */     {
/*  729 */       Block block1 = this.worldObj.getBlockState(blockpos.down()).getBlock();
/*      */       
/*  731 */       if (((block1 instanceof net.minecraft.block.BlockFence)) || ((block1 instanceof net.minecraft.block.BlockWall)) || ((block1 instanceof net.minecraft.block.BlockFenceGate)))
/*      */       {
/*  733 */         blockpos = blockpos.down();
/*  734 */         block = this.worldObj.getBlockState(blockpos).getBlock();
/*      */       }
/*      */     }
/*      */     
/*  738 */     super.updateFallState(p_71122_1_, p_71122_3_, block, blockpos);
/*      */   }
/*      */   
/*      */   public void openEditSign(TileEntitySign signTile)
/*      */   {
/*  743 */     signTile.setPlayer(this);
/*  744 */     this.playerNetServerHandler.sendPacket(new S36PacketSignEditorOpen(signTile.getPos()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void getNextWindowId()
/*      */   {
/*  752 */     this.currentWindowId = (this.currentWindowId % 100 + 1);
/*      */   }
/*      */   
/*      */   public void displayGui(IInteractionObject guiOwner)
/*      */   {
/*  757 */     getNextWindowId();
/*  758 */     this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, guiOwner.getGuiID(), guiOwner.getDisplayName()));
/*  759 */     this.openContainer = guiOwner.createContainer(this.inventory, this);
/*  760 */     this.openContainer.windowId = this.currentWindowId;
/*  761 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayGUIChest(IInventory chestInventory)
/*      */   {
/*  769 */     if (this.openContainer != this.inventoryContainer)
/*      */     {
/*  771 */       closeScreen();
/*      */     }
/*      */     
/*  774 */     if ((chestInventory instanceof ILockableContainer))
/*      */     {
/*  776 */       ILockableContainer ilockablecontainer = (ILockableContainer)chestInventory;
/*      */       
/*  778 */       if ((ilockablecontainer.isLocked()) && (!canOpen(ilockablecontainer.getLockCode())) && (!isSpectator()))
/*      */       {
/*  780 */         this.playerNetServerHandler.sendPacket(new S02PacketChat(new ChatComponentTranslation("container.isLocked", new Object[] { chestInventory.getDisplayName() }), (byte)2));
/*  781 */         this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S29PacketSoundEffect("random.door_close", this.posX, this.posY, this.posZ, 1.0F, 1.0F));
/*  782 */         return;
/*      */       }
/*      */     }
/*      */     
/*  786 */     getNextWindowId();
/*      */     
/*  788 */     if ((chestInventory instanceof IInteractionObject))
/*      */     {
/*  790 */       this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, ((IInteractionObject)chestInventory).getGuiID(), chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
/*  791 */       this.openContainer = ((IInteractionObject)chestInventory).createContainer(this.inventory, this);
/*      */     }
/*      */     else
/*      */     {
/*  795 */       this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "minecraft:container", chestInventory.getDisplayName(), chestInventory.getSizeInventory()));
/*  796 */       this.openContainer = new ContainerChest(this.inventory, chestInventory, this);
/*      */     }
/*      */     
/*  799 */     this.openContainer.windowId = this.currentWindowId;
/*  800 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */   
/*      */   public void displayVillagerTradeGui(IMerchant villager)
/*      */   {
/*  805 */     getNextWindowId();
/*  806 */     this.openContainer = new ContainerMerchant(this.inventory, villager, this.worldObj);
/*  807 */     this.openContainer.windowId = this.currentWindowId;
/*  808 */     this.openContainer.onCraftGuiOpened(this);
/*  809 */     IInventory iinventory = ((ContainerMerchant)this.openContainer).getMerchantInventory();
/*  810 */     IChatComponent ichatcomponent = villager.getDisplayName();
/*  811 */     this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "minecraft:villager", ichatcomponent, iinventory.getSizeInventory()));
/*  812 */     MerchantRecipeList merchantrecipelist = villager.getRecipes(this);
/*      */     
/*  814 */     if (merchantrecipelist != null)
/*      */     {
/*  816 */       PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
/*  817 */       packetbuffer.writeInt(this.currentWindowId);
/*  818 */       merchantrecipelist.writeToBuf(packetbuffer);
/*  819 */       this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|TrList", packetbuffer));
/*      */     }
/*      */   }
/*      */   
/*      */   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory)
/*      */   {
/*  825 */     if (this.openContainer != this.inventoryContainer)
/*      */     {
/*  827 */       closeScreen();
/*      */     }
/*      */     
/*  830 */     getNextWindowId();
/*  831 */     this.playerNetServerHandler.sendPacket(new S2DPacketOpenWindow(this.currentWindowId, "EntityHorse", horseInventory.getDisplayName(), horseInventory.getSizeInventory(), horse.getEntityId()));
/*  832 */     this.openContainer = new net.minecraft.inventory.ContainerHorseInventory(this.inventory, horseInventory, horse, this);
/*  833 */     this.openContainer.windowId = this.currentWindowId;
/*  834 */     this.openContainer.onCraftGuiOpened(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void displayGUIBook(ItemStack bookStack)
/*      */   {
/*  842 */     Item item = bookStack.getItem();
/*      */     
/*  844 */     if (item == Items.written_book)
/*      */     {
/*  846 */       this.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("MC|BOpen", new PacketBuffer(Unpooled.buffer())));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
/*      */   {
/*  856 */     if (!(containerToSend.getSlot(slotInd) instanceof net.minecraft.inventory.SlotCrafting))
/*      */     {
/*  858 */       if (!this.isChangingQuantityOnly)
/*      */       {
/*  860 */         this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(containerToSend.windowId, slotInd, stack));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendContainerToPlayer(Container p_71120_1_)
/*      */   {
/*  867 */     updateCraftingInventory(p_71120_1_, p_71120_1_.getInventory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateCraftingInventory(Container containerToSend, List<ItemStack> itemsList)
/*      */   {
/*  875 */     this.playerNetServerHandler.sendPacket(new S30PacketWindowItems(containerToSend.windowId, itemsList));
/*  876 */     this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendProgressBarUpdate(Container containerIn, int varToUpdate, int newValue)
/*      */   {
/*  886 */     this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(containerIn.windowId, varToUpdate, newValue));
/*      */   }
/*      */   
/*      */   public void func_175173_a(Container p_175173_1_, IInventory p_175173_2_)
/*      */   {
/*  891 */     for (int i = 0; i < p_175173_2_.getFieldCount(); i++)
/*      */     {
/*  893 */       this.playerNetServerHandler.sendPacket(new S31PacketWindowProperty(p_175173_1_.windowId, i, p_175173_2_.getField(i)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void closeScreen()
/*      */   {
/*  902 */     this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S2EPacketCloseWindow(this.openContainer.windowId));
/*  903 */     closeContainer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void updateHeldItem()
/*      */   {
/*  911 */     if (!this.isChangingQuantityOnly)
/*      */     {
/*  913 */       this.playerNetServerHandler.sendPacket(new S2FPacketSetSlot(-1, -1, this.inventory.getItemStack()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void closeContainer()
/*      */   {
/*  922 */     this.openContainer.onContainerClosed(this);
/*  923 */     this.openContainer = this.inventoryContainer;
/*      */   }
/*      */   
/*      */   public void setEntityActionState(float p_110430_1_, float p_110430_2_, boolean p_110430_3_, boolean sneaking)
/*      */   {
/*  928 */     if (this.ridingEntity != null)
/*      */     {
/*  930 */       if ((p_110430_1_ >= -1.0F) && (p_110430_1_ <= 1.0F))
/*      */       {
/*  932 */         this.moveStrafing = p_110430_1_;
/*      */       }
/*      */       
/*  935 */       if ((p_110430_2_ >= -1.0F) && (p_110430_2_ <= 1.0F))
/*      */       {
/*  937 */         this.moveForward = p_110430_2_;
/*      */       }
/*      */       
/*  940 */       this.isJumping = p_110430_3_;
/*  941 */       setSneaking(sneaking);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addStat(StatBase stat, int amount)
/*      */   {
/*  950 */     if (stat != null)
/*      */     {
/*  952 */       this.statsFile.increaseStat(this, stat, amount);
/*      */       
/*  954 */       for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(stat.func_150952_k()))
/*      */       {
/*  956 */         getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).increseScore(amount);
/*      */       }
/*      */       
/*  959 */       if (this.statsFile.func_150879_e())
/*      */       {
/*  961 */         this.statsFile.func_150876_a(this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void func_175145_a(StatBase p_175145_1_)
/*      */   {
/*  968 */     if (p_175145_1_ != null)
/*      */     {
/*  970 */       this.statsFile.unlockAchievement(this, p_175145_1_, 0);
/*      */       
/*  972 */       for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(p_175145_1_.func_150952_k()))
/*      */       {
/*  974 */         getWorldScoreboard().getValueFromObjective(getName(), scoreobjective).setScorePoints(0);
/*      */       }
/*      */       
/*  977 */       if (this.statsFile.func_150879_e())
/*      */       {
/*  979 */         this.statsFile.func_150876_a(this);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void mountEntityAndWakeUp()
/*      */   {
/*  986 */     if (this.riddenByEntity != null)
/*      */     {
/*  988 */       this.riddenByEntity.mountEntity(this);
/*      */     }
/*      */     
/*  991 */     if (this.sleeping)
/*      */     {
/*  993 */       wakeUpPlayer(true, false, false);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPlayerHealthUpdated()
/*      */   {
/* 1003 */     this.lastHealth = -1.0E8F;
/*      */   }
/*      */   
/*      */   public void addChatComponentMessage(IChatComponent chatComponent)
/*      */   {
/* 1008 */     this.playerNetServerHandler.sendPacket(new S02PacketChat(chatComponent));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void onItemUseFinish()
/*      */   {
/* 1016 */     this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S19PacketEntityStatus(this, (byte)9));
/* 1017 */     super.onItemUseFinish();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setItemInUse(ItemStack stack, int duration)
/*      */   {
/* 1025 */     super.setItemInUse(stack, duration);
/*      */     
/* 1027 */     if ((stack != null) && (stack.getItem() != null) && (stack.getItem().getItemUseAction(stack) == EnumAction.EAT))
/*      */     {
/* 1029 */       getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(this, 3));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd)
/*      */   {
/* 1039 */     super.clonePlayer(oldPlayer, respawnFromEnd);
/* 1040 */     this.lastExperience = -1;
/* 1041 */     this.lastHealth = -1.0F;
/* 1042 */     this.lastFoodLevel = -1;
/* 1043 */     this.destroyedItemsNetCache.addAll(((EntityPlayerMP)oldPlayer).destroyedItemsNetCache);
/*      */   }
/*      */   
/*      */   protected void onNewPotionEffect(PotionEffect id)
/*      */   {
/* 1048 */     super.onNewPotionEffect(id);
/* 1049 */     this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(getEntityId(), id));
/*      */   }
/*      */   
/*      */   protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_)
/*      */   {
/* 1054 */     super.onChangedPotionEffect(id, p_70695_2_);
/* 1055 */     this.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(getEntityId(), id));
/*      */   }
/*      */   
/*      */   protected void onFinishedPotionEffect(PotionEffect p_70688_1_)
/*      */   {
/* 1060 */     super.onFinishedPotionEffect(p_70688_1_);
/* 1061 */     this.playerNetServerHandler.sendPacket(new S1EPacketRemoveEntityEffect(getEntityId(), p_70688_1_));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPositionAndUpdate(double x, double y, double z)
/*      */   {
/* 1069 */     this.playerNetServerHandler.setPlayerLocation(x, y, z, this.rotationYaw, this.rotationPitch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void onCriticalHit(Entity entityHit)
/*      */   {
/* 1077 */     getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 4));
/*      */   }
/*      */   
/*      */   public void onEnchantmentCritical(Entity entityHit)
/*      */   {
/* 1082 */     getServerForPlayer().getEntityTracker().func_151248_b(this, new S0BPacketAnimation(entityHit, 5));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void sendPlayerAbilities()
/*      */   {
/* 1090 */     if (this.playerNetServerHandler != null)
/*      */     {
/* 1092 */       this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S39PacketPlayerAbilities(this.capabilities));
/* 1093 */       updatePotionMetadata();
/*      */     }
/*      */   }
/*      */   
/*      */   public WorldServer getServerForPlayer()
/*      */   {
/* 1099 */     return (WorldServer)this.worldObj;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setGameType(WorldSettings.GameType gameType)
/*      */   {
/* 1107 */     this.theItemInWorldManager.setGameType(gameType);
/* 1108 */     this.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(3, gameType.getID()));
/*      */     
/* 1110 */     if (gameType == WorldSettings.GameType.SPECTATOR)
/*      */     {
/* 1112 */       mountEntity(null);
/*      */     }
/*      */     else
/*      */     {
/* 1116 */       setSpectatingEntity(this);
/*      */     }
/*      */     
/* 1119 */     sendPlayerAbilities();
/* 1120 */     markPotionsDirty();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isSpectator()
/*      */   {
/* 1128 */     return this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addChatMessage(IChatComponent component)
/*      */   {
/* 1136 */     this.playerNetServerHandler.sendPacket(new S02PacketChat(component));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canCommandSenderUseCommand(int permLevel, String commandName)
/*      */   {
/* 1144 */     if (("seed".equals(commandName)) && (!this.mcServer.isDedicatedServer()))
/*      */     {
/* 1146 */       return true;
/*      */     }
/* 1148 */     if ((!"tell".equals(commandName)) && (!"help".equals(commandName)) && (!"me".equals(commandName)) && (!"trigger".equals(commandName)))
/*      */     {
/* 1150 */       if (this.mcServer.getConfigurationManager().canSendCommands(getGameProfile()))
/*      */       {
/* 1152 */         UserListOpsEntry userlistopsentry = (UserListOpsEntry)this.mcServer.getConfigurationManager().getOppedPlayers().getEntry(getGameProfile());
/* 1153 */         return userlistopsentry.getPermissionLevel() >= permLevel;
/*      */       }
/*      */       
/*      */ 
/* 1157 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1162 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getPlayerIP()
/*      */   {
/* 1171 */     String s = this.playerNetServerHandler.netManager.getRemoteAddress().toString();
/* 1172 */     s = s.substring(s.indexOf("/") + 1);
/* 1173 */     s = s.substring(0, s.indexOf(":"));
/* 1174 */     return s;
/*      */   }
/*      */   
/*      */   public void handleClientSettings(C15PacketClientSettings packetIn)
/*      */   {
/* 1179 */     this.translator = packetIn.getLang();
/* 1180 */     this.chatVisibility = packetIn.getChatVisibility();
/* 1181 */     this.chatColours = packetIn.isColorsEnabled();
/* 1182 */     getDataWatcher().updateObject(10, Byte.valueOf((byte)packetIn.getModelPartFlags()));
/*      */   }
/*      */   
/*      */   public EntityPlayer.EnumChatVisibility getChatVisibility()
/*      */   {
/* 1187 */     return this.chatVisibility;
/*      */   }
/*      */   
/*      */   public void loadResourcePack(String url, String hash)
/*      */   {
/* 1192 */     this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S48PacketResourcePackSend(url, hash));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BlockPos getPosition()
/*      */   {
/* 1201 */     return new BlockPos(this.posX, this.posY + 0.5D, this.posZ);
/*      */   }
/*      */   
/*      */   public void markPlayerActive()
/*      */   {
/* 1206 */     this.playerLastActiveTime = MinecraftServer.getCurrentTimeMillis();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public StatisticsFile getStatFile()
/*      */   {
/* 1214 */     return this.statsFile;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEntity(Entity p_152339_1_)
/*      */   {
/* 1222 */     if ((p_152339_1_ instanceof EntityPlayer))
/*      */     {
/* 1224 */       this.playerNetServerHandler.sendPacket(new S13PacketDestroyEntities(new int[] { p_152339_1_.getEntityId() }));
/*      */     }
/*      */     else
/*      */     {
/* 1228 */       this.destroyedItemsNetCache.add(Integer.valueOf(p_152339_1_.getEntityId()));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void updatePotionMetadata()
/*      */   {
/* 1238 */     if (isSpectator())
/*      */     {
/* 1240 */       resetPotionEffectMetadata();
/* 1241 */       setInvisible(true);
/*      */     }
/*      */     else
/*      */     {
/* 1245 */       super.updatePotionMetadata();
/*      */     }
/*      */     
/* 1248 */     getServerForPlayer().getEntityTracker().func_180245_a(this);
/*      */   }
/*      */   
/*      */   public Entity getSpectatingEntity()
/*      */   {
/* 1253 */     return this.spectatingEntity == null ? this : this.spectatingEntity;
/*      */   }
/*      */   
/*      */   public void setSpectatingEntity(Entity entityToSpectate)
/*      */   {
/* 1258 */     Entity entity = getSpectatingEntity();
/* 1259 */     this.spectatingEntity = (entityToSpectate == null ? this : entityToSpectate);
/*      */     
/* 1261 */     if (entity != this.spectatingEntity)
/*      */     {
/* 1263 */       this.playerNetServerHandler.sendPacket(new net.minecraft.network.play.server.S43PacketCamera(this.spectatingEntity));
/* 1264 */       setPositionAndUpdate(this.spectatingEntity.posX, this.spectatingEntity.posY, this.spectatingEntity.posZ);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void attackTargetEntityWithCurrentItem(Entity targetEntity)
/*      */   {
/* 1274 */     if (this.theItemInWorldManager.getGameType() == WorldSettings.GameType.SPECTATOR)
/*      */     {
/* 1276 */       setSpectatingEntity(targetEntity);
/*      */     }
/*      */     else
/*      */     {
/* 1280 */       super.attackTargetEntityWithCurrentItem(targetEntity);
/*      */     }
/*      */   }
/*      */   
/*      */   public long getLastActiveTime()
/*      */   {
/* 1286 */     return this.playerLastActiveTime;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public IChatComponent getTabListDisplayName()
/*      */   {
/* 1295 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\player\EntityPlayerMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */