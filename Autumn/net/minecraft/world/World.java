package net.minecraft.world;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHopper;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.IntHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ReportedException;
import net.minecraft.util.Vec3;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;
import rip.autumn.core.Autumn;
import rip.autumn.module.impl.world.TimeChangerMod;

public abstract class World implements IBlockAccess {
   public final List loadedEntityList = Lists.newArrayList();
   public final List loadedTileEntityList = Lists.newArrayList();
   public final List tickableTileEntities = Lists.newArrayList();
   public final List playerEntities = Lists.newArrayList();
   public final List weatherEffects = Lists.newArrayList();
   public final Random rand = new Random();
   public final WorldProvider provider;
   public final Profiler theProfiler;
   public final boolean isRemote;
   protected final List unloadedEntityList = Lists.newArrayList();
   protected final IntHashMap entitiesById = new IntHashMap();
   protected final int DIST_HASH_MAGIC = 1013904223;
   protected final ISaveHandler saveHandler;
   private final List addedTileEntityList = Lists.newArrayList();
   private final List tileEntitiesToBeRemoved = Lists.newArrayList();
   private final Calendar theCalendar = Calendar.getInstance();
   private final WorldBorder worldBorder;
   protected boolean scheduledUpdatesAreImmediate;
   protected int updateLCG = (new Random()).nextInt();
   protected float prevRainingStrength;
   protected float rainingStrength;
   protected float prevThunderingStrength;
   protected float thunderingStrength;
   protected List worldAccesses = Lists.newArrayList();
   protected IChunkProvider chunkProvider;
   protected WorldInfo worldInfo;
   protected boolean findingSpawnPoint;
   protected MapStorage mapStorage;
   protected VillageCollection villageCollectionObj;
   protected Scoreboard worldScoreboard = new Scoreboard();
   protected Set activeChunkSet = Sets.newHashSet();
   protected boolean spawnHostileMobs;
   protected boolean spawnPeacefulMobs;
   int[] lightUpdateBlockList;
   private int field_181546_a = 63;
   private final long cloudColour = 16777215L;
   private int skylightSubtracted;
   private int lastLightningBolt;
   private int ambientTickCountdown;
   private boolean processingLoadedTiles;

   protected World(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
      this.ambientTickCountdown = this.rand.nextInt(12000);
      this.spawnHostileMobs = true;
      this.spawnPeacefulMobs = true;
      this.lightUpdateBlockList = new int['耀'];
      this.saveHandler = saveHandlerIn;
      this.theProfiler = profilerIn;
      this.worldInfo = info;
      this.provider = providerIn;
      this.isRemote = client;
      this.worldBorder = providerIn.getWorldBorder();
   }

   public static boolean doesBlockHaveSolidTopSurface(IBlockAccess blockAccess, BlockPos pos) {
      IBlockState iblockstate = blockAccess.getBlockState(pos);
      Block block = iblockstate.getBlock();
      boolean var10000;
      if (!block.getMaterial().isOpaque() || !block.isFullCube()) {
         label41: {
            if (block instanceof BlockStairs) {
               if (iblockstate.getValue(BlockStairs.HALF) == BlockStairs.EnumHalf.TOP) {
                  break label41;
               }
            } else if (block instanceof BlockSlab) {
               if (iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP) {
                  break label41;
               }
            } else if (block instanceof BlockHopper || block instanceof BlockSnow && (Integer)iblockstate.getValue(BlockSnow.LAYERS) == 7) {
               break label41;
            }

            var10000 = false;
            return var10000;
         }
      }

      var10000 = true;
      return var10000;
   }

   public World init() {
      return this;
   }

   public BiomeGenBase getBiomeGenForCoords(final BlockPos pos) {
      if (this.isBlockLoaded(pos)) {
         Chunk chunk = this.getChunkFromBlockCoords(pos);

         try {
            return chunk.getBiome(pos, this.provider.getWorldChunkManager());
         } catch (Throwable var6) {
            CrashReport crashreport = CrashReport.makeCrashReport(var6, "Getting biome");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Coordinates of biome request");
            crashreportcategory.addCrashSectionCallable("Location", new Callable() {
               public String call() throws Exception {
                  return CrashReportCategory.getCoordinateInfo(pos);
               }
            });
            throw new ReportedException(crashreport);
         }
      } else {
         return this.provider.getWorldChunkManager().getBiomeGenerator(pos, BiomeGenBase.plains);
      }
   }

   public WorldChunkManager getWorldChunkManager() {
      return this.provider.getWorldChunkManager();
   }

   protected abstract IChunkProvider createChunkProvider();

   public void initialize(WorldSettings settings) {
      this.worldInfo.setServerInitialized(true);
   }

   public void setInitialSpawnLocation() {
      this.setSpawnPoint(new BlockPos(8, 64, 8));
   }

   public Block getGroundAboveSeaLevel(BlockPos pos) {
      BlockPos blockpos;
      for(blockpos = new BlockPos(pos.getX(), this.func_181545_F(), pos.getZ()); !this.isAirBlock(blockpos.up()); blockpos = blockpos.up()) {
      }

      return this.getBlockState(blockpos).getBlock();
   }

   private boolean isValid(BlockPos pos) {
      return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256;
   }

   public boolean isAirBlock(BlockPos pos) {
      return this.getBlockState(pos).getBlock().getMaterial() == Material.air;
   }

   public boolean isBlockLoaded(BlockPos pos) {
      return this.isBlockLoaded(pos, true);
   }

   public boolean isBlockLoaded(BlockPos pos, boolean allowEmpty) {
      return this.isValid(pos) && this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, allowEmpty);
   }

   public boolean isAreaLoaded(BlockPos center, int radius) {
      return this.isAreaLoaded(center, radius, true);
   }

   public boolean isAreaLoaded(BlockPos center, int radius, boolean allowEmpty) {
      return this.isAreaLoaded(center.getX() - radius, center.getY() - radius, center.getZ() - radius, center.getX() + radius, center.getY() + radius, center.getZ() + radius, allowEmpty);
   }

   public boolean isAreaLoaded(BlockPos from, BlockPos to) {
      return this.isAreaLoaded(from, to, true);
   }

   public boolean isAreaLoaded(BlockPos from, BlockPos to, boolean allowEmpty) {
      return this.isAreaLoaded(from.getX(), from.getY(), from.getZ(), to.getX(), to.getY(), to.getZ(), allowEmpty);
   }

   public boolean isAreaLoaded(StructureBoundingBox box) {
      return this.isAreaLoaded(box, true);
   }

   public boolean isAreaLoaded(StructureBoundingBox box, boolean allowEmpty) {
      return this.isAreaLoaded(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ, allowEmpty);
   }

   private boolean isAreaLoaded(int xStart, int yStart, int zStart, int xEnd, int yEnd, int zEnd, boolean allowEmpty) {
      if (yEnd >= 0 && yStart < 256) {
         xStart >>= 4;
         zStart >>= 4;
         xEnd >>= 4;
         zEnd >>= 4;

         for(int i = xStart; i <= xEnd; ++i) {
            for(int j = zStart; j <= zEnd; ++j) {
               if (!this.isChunkLoaded(i, j, allowEmpty)) {
                  return false;
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   protected boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
      return this.chunkProvider.chunkExists(x, z) && (allowEmpty || !this.chunkProvider.provideChunk(x, z).isEmpty());
   }

   public Chunk getChunkFromBlockCoords(BlockPos pos) {
      return this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4);
   }

   public Chunk getChunkFromChunkCoords(int chunkX, int chunkZ) {
      return this.chunkProvider.provideChunk(chunkX, chunkZ);
   }

   public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
      if (!this.isValid(pos)) {
         return false;
      } else if (!this.isRemote && this.worldInfo.getTerrainType() == WorldType.DEBUG_WORLD) {
         return false;
      } else {
         Chunk chunk = this.getChunkFromBlockCoords(pos);
         Block block = newState.getBlock();
         IBlockState iblockstate = chunk.setBlockState(pos, newState);
         if (iblockstate == null) {
            return false;
         } else {
            Block block1 = iblockstate.getBlock();
            if (block.getLightOpacity() != block1.getLightOpacity() || block.getLightValue() != block1.getLightValue()) {
               this.theProfiler.startSection("checkLight");
               this.checkLight(pos);
               this.theProfiler.endSection();
            }

            if ((flags & 2) != 0 && (!this.isRemote || (flags & 4) == 0) && chunk.isPopulated()) {
               this.markBlockForUpdate(pos);
            }

            if (!this.isRemote && (flags & 1) != 0) {
               this.notifyNeighborsRespectDebug(pos, iblockstate.getBlock());
               if (block.hasComparatorInputOverride()) {
                  this.updateComparatorOutputLevel(pos, block);
               }
            }

            return true;
         }
      }
   }

   public boolean setBlockToAir(BlockPos pos) {
      return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
   }

   public boolean destroyBlock(BlockPos pos, boolean dropBlock) {
      IBlockState iblockstate = this.getBlockState(pos);
      Block block = iblockstate.getBlock();
      if (block.getMaterial() == Material.air) {
         return false;
      } else {
         this.playAuxSFX(2001, pos, Block.getStateId(iblockstate));
         if (dropBlock) {
            block.dropBlockAsItem(this, pos, iblockstate, 0);
         }

         return this.setBlockState(pos, Blocks.air.getDefaultState(), 3);
      }
   }

   public boolean setBlockState(BlockPos pos, IBlockState state) {
      return this.setBlockState(pos, state, 3);
   }

   public void markBlockForUpdate(BlockPos pos) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).markBlockForUpdate(pos);
      }

   }

   public void notifyNeighborsRespectDebug(BlockPos pos, Block blockType) {
      if (this.worldInfo.getTerrainType() != WorldType.DEBUG_WORLD) {
         this.notifyNeighborsOfStateChange(pos, blockType);
      }

   }

   public void markBlocksDirtyVertical(int x1, int z1, int x2, int z2) {
      int j;
      if (x2 > z2) {
         j = z2;
         z2 = x2;
         x2 = j;
      }

      if (!this.provider.getHasNoSky()) {
         for(j = x2; j <= z2; ++j) {
            this.checkLightFor(EnumSkyBlock.SKY, new BlockPos(x1, j, z1));
         }
      }

      this.markBlockRangeForRenderUpdate(x1, x2, z1, x1, z2, z1);
   }

   public void markBlockRangeForRenderUpdate(BlockPos rangeMin, BlockPos rangeMax) {
      this.markBlockRangeForRenderUpdate(rangeMin.getX(), rangeMin.getY(), rangeMin.getZ(), rangeMax.getX(), rangeMax.getY(), rangeMax.getZ());
   }

   public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).markBlockRangeForRenderUpdate(x1, y1, z1, x2, y2, z2);
      }

   }

   public void notifyNeighborsOfStateChange(BlockPos pos, Block blockType) {
      this.notifyBlockOfStateChange(pos.west(), blockType);
      this.notifyBlockOfStateChange(pos.east(), blockType);
      this.notifyBlockOfStateChange(pos.down(), blockType);
      this.notifyBlockOfStateChange(pos.up(), blockType);
      this.notifyBlockOfStateChange(pos.north(), blockType);
      this.notifyBlockOfStateChange(pos.south(), blockType);
   }

   public void notifyNeighborsOfStateExcept(BlockPos pos, Block blockType, EnumFacing skipSide) {
      if (skipSide != EnumFacing.WEST) {
         this.notifyBlockOfStateChange(pos.west(), blockType);
      }

      if (skipSide != EnumFacing.EAST) {
         this.notifyBlockOfStateChange(pos.east(), blockType);
      }

      if (skipSide != EnumFacing.DOWN) {
         this.notifyBlockOfStateChange(pos.down(), blockType);
      }

      if (skipSide != EnumFacing.UP) {
         this.notifyBlockOfStateChange(pos.up(), blockType);
      }

      if (skipSide != EnumFacing.NORTH) {
         this.notifyBlockOfStateChange(pos.north(), blockType);
      }

      if (skipSide != EnumFacing.SOUTH) {
         this.notifyBlockOfStateChange(pos.south(), blockType);
      }

   }

   public void notifyBlockOfStateChange(BlockPos pos, final Block blockIn) {
      if (!this.isRemote) {
         IBlockState iblockstate = this.getBlockState(pos);

         try {
            iblockstate.getBlock().onNeighborBlockChange(this, pos, iblockstate, blockIn);
         } catch (Throwable var7) {
            CrashReport crashreport = CrashReport.makeCrashReport(var7, "Exception while updating neighbours");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being updated");
            crashreportcategory.addCrashSectionCallable("Source block type", new Callable() {
               public String call() throws Exception {
                  try {
                     return String.format("ID #%d (%s // %s)", Block.getIdFromBlock(blockIn), blockIn.getUnlocalizedName(), blockIn.getClass().getCanonicalName());
                  } catch (Throwable var2) {
                     return "ID #" + Block.getIdFromBlock(blockIn);
                  }
               }
            });
            CrashReportCategory.addBlockInfo(crashreportcategory, pos, iblockstate);
            throw new ReportedException(crashreport);
         }
      }

   }

   public boolean isBlockTickPending(BlockPos pos, Block blockType) {
      return false;
   }

   public boolean canSeeSky(BlockPos pos) {
      return this.getChunkFromBlockCoords(pos).canSeeSky(pos);
   }

   public boolean canBlockSeeSky(BlockPos pos) {
      if (pos.getY() >= this.func_181545_F()) {
         return this.canSeeSky(pos);
      } else {
         BlockPos blockpos = new BlockPos(pos.getX(), this.func_181545_F(), pos.getZ());
         if (!this.canSeeSky(blockpos)) {
            return false;
         } else {
            for(blockpos = blockpos.down(); blockpos.getY() > pos.getY(); blockpos = blockpos.down()) {
               Block block = this.getBlockState(blockpos).getBlock();
               if (block.getLightOpacity() > 0 && !block.getMaterial().isLiquid()) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public int getLight(BlockPos pos) {
      if (pos.getY() < 0) {
         return 0;
      } else {
         if (pos.getY() >= 256) {
            pos = new BlockPos(pos.getX(), 255, pos.getZ());
         }

         return this.getChunkFromBlockCoords(pos).getLightSubtracted(pos, 0);
      }
   }

   public int getLightFromNeighbors(BlockPos pos) {
      return this.getLight(pos, true);
   }

   public int getLight(BlockPos pos, boolean checkNeighbors) {
      if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
         if (checkNeighbors && this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int i1 = this.getLight(pos.up(), false);
            int i = this.getLight(pos.east(), false);
            int j = this.getLight(pos.west(), false);
            int k = this.getLight(pos.south(), false);
            int l = this.getLight(pos.north(), false);
            if (i > i1) {
               i1 = i;
            }

            if (j > i1) {
               i1 = j;
            }

            if (k > i1) {
               i1 = k;
            }

            if (l > i1) {
               i1 = l;
            }

            return i1;
         } else if (pos.getY() < 0) {
            return 0;
         } else {
            if (pos.getY() >= 256) {
               pos = new BlockPos(pos.getX(), 255, pos.getZ());
            }

            Chunk chunk = this.getChunkFromBlockCoords(pos);
            return chunk.getLightSubtracted(pos, this.skylightSubtracted);
         }
      } else {
         return 15;
      }
   }

   public BlockPos getHeight(BlockPos pos) {
      int i;
      if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
         if (this.isChunkLoaded(pos.getX() >> 4, pos.getZ() >> 4, true)) {
            i = this.getChunkFromChunkCoords(pos.getX() >> 4, pos.getZ() >> 4).getHeightValue(pos.getX() & 15, pos.getZ() & 15);
         } else {
            i = 0;
         }
      } else {
         i = this.func_181545_F() + 1;
      }

      return new BlockPos(pos.getX(), i, pos.getZ());
   }

   public int getChunksLowestHorizon(int x, int z) {
      if (x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000) {
         if (!this.isChunkLoaded(x >> 4, z >> 4, true)) {
            return 0;
         } else {
            Chunk chunk = this.getChunkFromChunkCoords(x >> 4, z >> 4);
            return chunk.getLowestHeight();
         }
      } else {
         return this.func_181545_F() + 1;
      }
   }

   public int getLightFromNeighborsFor(EnumSkyBlock type, BlockPos pos) {
      if (this.provider.getHasNoSky() && type == EnumSkyBlock.SKY) {
         return 0;
      } else {
         if (pos.getY() < 0) {
            pos = new BlockPos(pos.getX(), 0, pos.getZ());
         }

         if (!this.isValid(pos)) {
            return type.defaultLightValue;
         } else if (!this.isBlockLoaded(pos)) {
            return type.defaultLightValue;
         } else if (this.getBlockState(pos).getBlock().getUseNeighborBrightness()) {
            int i1 = this.getLightFor(type, pos.up());
            int i = this.getLightFor(type, pos.east());
            int j = this.getLightFor(type, pos.west());
            int k = this.getLightFor(type, pos.south());
            int l = this.getLightFor(type, pos.north());
            if (i > i1) {
               i1 = i;
            }

            if (j > i1) {
               i1 = j;
            }

            if (k > i1) {
               i1 = k;
            }

            if (l > i1) {
               i1 = l;
            }

            return i1;
         } else {
            Chunk chunk = this.getChunkFromBlockCoords(pos);
            return chunk.getLightFor(type, pos);
         }
      }
   }

   public int getLightFor(EnumSkyBlock type, BlockPos pos) {
      if (pos.getY() < 0) {
         pos = new BlockPos(pos.getX(), 0, pos.getZ());
      }

      if (!this.isValid(pos)) {
         return type.defaultLightValue;
      } else if (!this.isBlockLoaded(pos)) {
         return type.defaultLightValue;
      } else {
         Chunk chunk = this.getChunkFromBlockCoords(pos);
         return chunk.getLightFor(type, pos);
      }
   }

   public void setLightFor(EnumSkyBlock type, BlockPos pos, int lightValue) {
      if (this.isValid(pos) && this.isBlockLoaded(pos)) {
         Chunk chunk = this.getChunkFromBlockCoords(pos);
         chunk.setLightFor(type, pos, lightValue);
         this.notifyLightSet(pos);
      }

   }

   public void notifyLightSet(BlockPos pos) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).notifyLightSet(pos);
      }

   }

   public int getCombinedLight(BlockPos pos, int lightValue) {
      int i = this.getLightFromNeighborsFor(EnumSkyBlock.SKY, pos);
      int j = this.getLightFromNeighborsFor(EnumSkyBlock.BLOCK, pos);
      if (j < lightValue) {
         j = lightValue;
      }

      return i << 20 | j << 4;
   }

   public float getLightBrightness(BlockPos pos) {
      return this.provider.getLightBrightnessTable()[this.getLightFromNeighbors(pos)];
   }

   public IBlockState getBlockState(BlockPos pos) {
      if (!this.isValid(pos)) {
         return Blocks.air.getDefaultState();
      } else {
         Chunk chunk = this.getChunkFromBlockCoords(pos);
         return chunk.getBlockState(pos);
      }
   }

   public boolean isDaytime() {
      return this.skylightSubtracted < 4;
   }

   public MovingObjectPosition rayTraceBlocks(Vec3 p_72933_1_, Vec3 p_72933_2_) {
      return this.rayTraceBlocks(p_72933_1_, p_72933_2_, false, false, false);
   }

   public MovingObjectPosition rayTraceBlocks(Vec3 start, Vec3 end, boolean stopOnLiquid) {
      return this.rayTraceBlocks(start, end, stopOnLiquid, false, false);
   }

   public MovingObjectPosition rayTraceBlocks(Vec3 vec31, Vec3 vec32, boolean stopOnLiquid, boolean ignoreBlockWithoutBoundingBox, boolean returnLastUncollidableBlock) {
      if (!Double.isNaN(vec31.xCoord) && !Double.isNaN(vec31.yCoord) && !Double.isNaN(vec31.zCoord)) {
         if (!Double.isNaN(vec32.xCoord) && !Double.isNaN(vec32.yCoord) && !Double.isNaN(vec32.zCoord)) {
            int i = MathHelper.floor_double(vec32.xCoord);
            int j = MathHelper.floor_double(vec32.yCoord);
            int k = MathHelper.floor_double(vec32.zCoord);
            int l = MathHelper.floor_double(vec31.xCoord);
            int i1 = MathHelper.floor_double(vec31.yCoord);
            int j1 = MathHelper.floor_double(vec31.zCoord);
            BlockPos blockpos = new BlockPos(l, i1, j1);
            IBlockState iblockstate = this.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            MovingObjectPosition movingobjectposition2;
            if ((!ignoreBlockWithoutBoundingBox || block.getCollisionBoundingBox(this, blockpos, iblockstate) != null) && block.canCollideCheck(iblockstate, stopOnLiquid)) {
               movingobjectposition2 = block.collisionRayTrace(this, blockpos, vec31, vec32);
               if (movingobjectposition2 != null) {
                  return movingobjectposition2;
               }
            }

            movingobjectposition2 = null;
            int var16 = 200;

            while(var16-- >= 0) {
               if (Double.isNaN(vec31.xCoord) || Double.isNaN(vec31.yCoord) || Double.isNaN(vec31.zCoord)) {
                  return null;
               }

               if (l == i && i1 == j && j1 == k) {
                  return returnLastUncollidableBlock ? movingobjectposition2 : null;
               }

               boolean flag2 = true;
               boolean flag = true;
               boolean flag1 = true;
               double d0 = 999.0D;
               double d1 = 999.0D;
               double d2 = 999.0D;
               if (i > l) {
                  d0 = (double)l + 1.0D;
               } else if (i < l) {
                  d0 = (double)l + 0.0D;
               } else {
                  flag2 = false;
               }

               if (j > i1) {
                  d1 = (double)i1 + 1.0D;
               } else if (j < i1) {
                  d1 = (double)i1 + 0.0D;
               } else {
                  flag = false;
               }

               if (k > j1) {
                  d2 = (double)j1 + 1.0D;
               } else if (k < j1) {
                  d2 = (double)j1 + 0.0D;
               } else {
                  flag1 = false;
               }

               double d3 = 999.0D;
               double d4 = 999.0D;
               double d5 = 999.0D;
               double d6 = vec32.xCoord - vec31.xCoord;
               double d7 = vec32.yCoord - vec31.yCoord;
               double d8 = vec32.zCoord - vec31.zCoord;
               if (flag2) {
                  d3 = (d0 - vec31.xCoord) / d6;
               }

               if (flag) {
                  d4 = (d1 - vec31.yCoord) / d7;
               }

               if (flag1) {
                  d5 = (d2 - vec31.zCoord) / d8;
               }

               if (d3 == -0.0D) {
                  d3 = -1.0E-4D;
               }

               if (d4 == -0.0D) {
                  d4 = -1.0E-4D;
               }

               if (d5 == -0.0D) {
                  d5 = -1.0E-4D;
               }

               EnumFacing enumfacing;
               if (d3 < d4 && d3 < d5) {
                  enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
                  vec31 = new Vec3(d0, vec31.yCoord + d7 * d3, vec31.zCoord + d8 * d3);
               } else if (d4 < d5) {
                  enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
                  vec31 = new Vec3(vec31.xCoord + d6 * d4, d1, vec31.zCoord + d8 * d4);
               } else {
                  enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                  vec31 = new Vec3(vec31.xCoord + d6 * d5, vec31.yCoord + d7 * d5, d2);
               }

               l = MathHelper.floor_double(vec31.xCoord) - (enumfacing == EnumFacing.EAST ? 1 : 0);
               i1 = MathHelper.floor_double(vec31.yCoord) - (enumfacing == EnumFacing.UP ? 1 : 0);
               j1 = MathHelper.floor_double(vec31.zCoord) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
               blockpos = new BlockPos(l, i1, j1);
               IBlockState iblockstate1 = this.getBlockState(blockpos);
               Block block1 = iblockstate1.getBlock();
               if (!ignoreBlockWithoutBoundingBox || block1.getCollisionBoundingBox(this, blockpos, iblockstate1) != null) {
                  if (block1.canCollideCheck(iblockstate1, stopOnLiquid)) {
                     MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(this, blockpos, vec31, vec32);
                     if (movingobjectposition1 != null) {
                        return movingobjectposition1;
                     }
                  } else {
                     movingobjectposition2 = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.MISS, vec31, enumfacing, blockpos);
                  }
               }
            }

            return returnLastUncollidableBlock ? movingobjectposition2 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public void playSoundAtEntity(Entity entityIn, String name, float volume, float pitch) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).playSound(name, entityIn.posX, entityIn.posY, entityIn.posZ, volume, pitch);
      }

   }

   public void playSoundToNearExcept(EntityPlayer player, String name, float volume, float pitch) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).playSoundToNearExcept(player, name, player.posX, player.posY, player.posZ, volume, pitch);
      }

   }

   public void playSoundEffect(double x, double y, double z, String soundName, float volume, float pitch) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).playSound(soundName, x, y, z, volume, pitch);
      }

   }

   public void playSound(double x, double y, double z, String soundName, float volume, float pitch, boolean distanceDelay) {
   }

   public void playRecord(BlockPos pos, String name) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).playRecord(name, pos);
      }

   }

   public void spawnParticle(EnumParticleTypes particleType, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175688_14_) {
      this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange(), xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175688_14_);
   }

   public void spawnParticle(EnumParticleTypes particleType, boolean p_175682_2_, double xCoord, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175682_15_) {
      this.spawnParticle(particleType.getParticleID(), particleType.getShouldIgnoreRange() | p_175682_2_, xCoord, yCoord, zCoord, xOffset, yOffset, zOffset, p_175682_15_);
   }

   private void spawnParticle(int particleID, boolean p_175720_2_, double xCood, double yCoord, double zCoord, double xOffset, double yOffset, double zOffset, int... p_175720_15_) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).spawnParticle(particleID, p_175720_2_, xCood, yCoord, zCoord, xOffset, yOffset, zOffset, p_175720_15_);
      }

   }

   public boolean addWeatherEffect(Entity entityIn) {
      this.weatherEffects.add(entityIn);
      return true;
   }

   public boolean spawnEntityInWorld(Entity entityIn) {
      int i = MathHelper.floor_double(entityIn.posX / 16.0D);
      int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
      boolean flag = entityIn.forceSpawn;
      if (entityIn instanceof EntityPlayer) {
         flag = true;
      }

      if (!flag && !this.isChunkLoaded(i, j, true)) {
         return false;
      } else {
         if (entityIn instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer)entityIn;
            this.playerEntities.add(entityplayer);
            this.updateAllPlayersSleepingFlag();
         }

         this.getChunkFromChunkCoords(i, j).addEntity(entityIn);
         this.loadedEntityList.add(entityIn);
         this.onEntityAdded(entityIn);
         return true;
      }
   }

   protected void onEntityAdded(Entity entityIn) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).onEntityAdded(entityIn);
      }

   }

   protected void onEntityRemoved(Entity entityIn) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).onEntityRemoved(entityIn);
      }

   }

   public void removeEntity(Entity entityIn) {
      if (entityIn.riddenByEntity != null) {
         entityIn.riddenByEntity.mountEntity((Entity)null);
      }

      if (entityIn.ridingEntity != null) {
         entityIn.mountEntity((Entity)null);
      }

      entityIn.setDead();
      if (entityIn instanceof EntityPlayer) {
         this.playerEntities.remove(entityIn);
         this.updateAllPlayersSleepingFlag();
         this.onEntityRemoved(entityIn);
      }

   }

   public void removePlayerEntityDangerously(Entity entityIn) {
      entityIn.setDead();
      if (entityIn instanceof EntityPlayer) {
         this.playerEntities.remove(entityIn);
         this.updateAllPlayersSleepingFlag();
      }

      int i = entityIn.chunkCoordX;
      int j = entityIn.chunkCoordZ;
      if (entityIn.addedToChunk && this.isChunkLoaded(i, j, true)) {
         this.getChunkFromChunkCoords(i, j).removeEntity(entityIn);
      }

      this.loadedEntityList.remove(entityIn);
      this.onEntityRemoved(entityIn);
   }

   public void addWorldAccess(IWorldAccess worldAccess) {
      this.worldAccesses.add(worldAccess);
   }

   public void removeWorldAccess(IWorldAccess worldAccess) {
      this.worldAccesses.remove(worldAccess);
   }

   public List getCollidingBoundingBoxes(Entity entityIn, AxisAlignedBB bb) {
      List list = Lists.newArrayList();
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX + 1.0D);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY + 1.0D);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
      WorldBorder worldborder = this.getWorldBorder();
      boolean flag = entityIn.isOutsideBorder();
      boolean flag1 = this.isInsideBorder(worldborder, entityIn);
      IBlockState iblockstate = Blocks.stone.getDefaultState();
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k1 = i; k1 < j; ++k1) {
         for(int l1 = i1; l1 < j1; ++l1) {
            if (this.isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k1, 64, l1))) {
               for(int i2 = k - 1; i2 < l; ++i2) {
                  blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
                  if (flag && flag1) {
                     entityIn.setOutsideBorder(false);
                  } else if (!flag && !flag1) {
                     entityIn.setOutsideBorder(true);
                  }

                  IBlockState iblockstate1 = iblockstate;
                  if (worldborder.contains((BlockPos)blockpos$mutableblockpos) || !flag1) {
                     iblockstate1 = this.getBlockState(blockpos$mutableblockpos);
                  }

                  iblockstate1.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate1, bb, list, entityIn);
               }
            }
         }
      }

      double d0 = 0.25D;
      List list1 = this.getEntitiesWithinAABBExcludingEntity(entityIn, bb.expand(d0, d0, d0));

      for(int j2 = 0; j2 < list1.size(); ++j2) {
         if (entityIn.riddenByEntity != list1 && entityIn.ridingEntity != list1) {
            AxisAlignedBB axisalignedbb = ((Entity)list1.get(j2)).getCollisionBoundingBox();
            if (axisalignedbb != null && axisalignedbb.intersectsWith(bb)) {
               list.add(axisalignedbb);
            }

            axisalignedbb = entityIn.getCollisionBox((Entity)list1.get(j2));
            if (axisalignedbb != null && axisalignedbb.intersectsWith(bb)) {
               list.add(axisalignedbb);
            }
         }
      }

      return list;
   }

   public boolean isInsideBorder(WorldBorder worldBorderIn, Entity entityIn) {
      double d0 = worldBorderIn.minX();
      double d1 = worldBorderIn.minZ();
      double d2 = worldBorderIn.maxX();
      double d3 = worldBorderIn.maxZ();
      if (entityIn.isOutsideBorder()) {
         ++d0;
         ++d1;
         --d2;
         --d3;
      } else {
         --d0;
         --d1;
         ++d2;
         ++d3;
      }

      return entityIn.posX > d0 && entityIn.posX < d2 && entityIn.posZ > d1 && entityIn.posZ < d3;
   }

   public List func_147461_a(AxisAlignedBB bb) {
      List list = Lists.newArrayList();
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX + 1.0D);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY + 1.0D);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k1 = i; k1 < j; ++k1) {
         for(int l1 = i1; l1 < j1; ++l1) {
            if (this.isBlockLoaded(blockpos$mutableblockpos.func_181079_c(k1, 64, l1))) {
               for(int i2 = k - 1; i2 < l; ++i2) {
                  blockpos$mutableblockpos.func_181079_c(k1, i2, l1);
                  IBlockState iblockstate;
                  if (k1 >= -30000000 && k1 < 30000000 && l1 >= -30000000 && l1 < 30000000) {
                     iblockstate = this.getBlockState(blockpos$mutableblockpos);
                  } else {
                     iblockstate = Blocks.bedrock.getDefaultState();
                  }

                  iblockstate.getBlock().addCollisionBoxesToList(this, blockpos$mutableblockpos, iblockstate, bb, list, (Entity)null);
               }
            }
         }
      }

      return list;
   }

   public int calculateSkylightSubtracted(float p_72967_1_) {
      float f = this.getCelestialAngle(p_72967_1_);
      float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F);
      f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
      f1 = 1.0F - f1;
      f1 = (float)((double)f1 * (1.0D - (double)(this.getRainStrength(p_72967_1_) * 5.0F) / 16.0D));
      f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderStrength(p_72967_1_) * 5.0F) / 16.0D));
      f1 = 1.0F - f1;
      return (int)(f1 * 11.0F);
   }

   public float getSunBrightness(float p_72971_1_) {
      float f = this.getCelestialAngle(p_72971_1_);
      float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.2F);
      f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
      f1 = 1.0F - f1;
      f1 = (float)((double)f1 * (1.0D - (double)(this.getRainStrength(p_72971_1_) * 5.0F) / 16.0D));
      f1 = (float)((double)f1 * (1.0D - (double)(this.getThunderStrength(p_72971_1_) * 5.0F) / 16.0D));
      return f1 * 0.8F + 0.2F;
   }

   public Vec3 getSkyColor(Entity entityIn, float partialTicks) {
      float f = this.getCelestialAngle(partialTicks);
      float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
      int i = MathHelper.floor_double(entityIn.posX);
      int j = MathHelper.floor_double(entityIn.posY);
      int k = MathHelper.floor_double(entityIn.posZ);
      BlockPos blockpos = new BlockPos(i, j, k);
      BiomeGenBase biomegenbase = this.getBiomeGenForCoords(blockpos);
      float f2 = biomegenbase.getFloatTemperature(blockpos);
      int l = biomegenbase.getSkyColorByTemp(f2);
      float f3 = (float)(l >> 16 & 255) / 255.0F;
      float f4 = (float)(l >> 8 & 255) / 255.0F;
      float f5 = (float)(l & 255) / 255.0F;
      f3 *= f1;
      f4 *= f1;
      f5 *= f1;
      float f6 = this.getRainStrength(partialTicks);
      float f10;
      float f12;
      if (f6 > 0.0F) {
         f10 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.6F;
         f12 = 1.0F - f6 * 0.75F;
         f3 = f3 * f12 + f10 * (1.0F - f12);
         f4 = f4 * f12 + f10 * (1.0F - f12);
         f5 = f5 * f12 + f10 * (1.0F - f12);
      }

      f10 = this.getThunderStrength(partialTicks);
      if (f10 > 0.0F) {
         f12 = (f3 * 0.3F + f4 * 0.59F + f5 * 0.11F) * 0.2F;
         float f9 = 1.0F - f10 * 0.75F;
         f3 = f3 * f9 + f12 * (1.0F - f9);
         f4 = f4 * f9 + f12 * (1.0F - f9);
         f5 = f5 * f9 + f12 * (1.0F - f9);
      }

      if (this.lastLightningBolt > 0) {
         f12 = (float)this.lastLightningBolt - partialTicks;
         if (f12 > 1.0F) {
            f12 = 1.0F;
         }

         f12 *= 0.45F;
         f3 = f3 * (1.0F - f12) + 0.8F * f12;
         f4 = f4 * (1.0F - f12) + 0.8F * f12;
         f5 = f5 * (1.0F - f12) + 1.0F * f12;
      }

      return new Vec3((double)f3, (double)f4, (double)f5);
   }

   public float getCelestialAngle(float partialTicks) {
      return this.provider.calculateCelestialAngle(this.worldInfo.getWorldTime(), partialTicks);
   }

   public int getMoonPhase() {
      return this.provider.getMoonPhase(this.worldInfo.getWorldTime());
   }

   public float getCurrentMoonPhaseFactor() {
      return WorldProvider.moonPhaseFactors[this.provider.getMoonPhase(this.worldInfo.getWorldTime())];
   }

   public float getCelestialAngleRadians(float partialTicks) {
      float f = this.getCelestialAngle(partialTicks);
      return f * 3.1415927F * 2.0F;
   }

   public Vec3 getCloudColour(float partialTicks) {
      float f = this.getCelestialAngle(partialTicks);
      float f1 = MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.5F;
      f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
      this.getClass();
      float f2 = (float)(16777215L >> 16 & 255L) / 255.0F;
      this.getClass();
      float f3 = (float)(16777215L >> 8 & 255L) / 255.0F;
      this.getClass();
      float f4 = (float)(16777215L & 255L) / 255.0F;
      float f5 = this.getRainStrength(partialTicks);
      float f9;
      float f10;
      if (f5 > 0.0F) {
         f9 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.6F;
         f10 = 1.0F - f5 * 0.95F;
         f2 = f2 * f10 + f9 * (1.0F - f10);
         f3 = f3 * f10 + f9 * (1.0F - f10);
         f4 = f4 * f10 + f9 * (1.0F - f10);
      }

      f2 *= f1 * 0.9F + 0.1F;
      f3 *= f1 * 0.9F + 0.1F;
      f4 *= f1 * 0.85F + 0.15F;
      f9 = this.getThunderStrength(partialTicks);
      if (f9 > 0.0F) {
         f10 = (f2 * 0.3F + f3 * 0.59F + f4 * 0.11F) * 0.2F;
         float f8 = 1.0F - f9 * 0.95F;
         f2 = f2 * f8 + f10 * (1.0F - f8);
         f3 = f3 * f8 + f10 * (1.0F - f8);
         f4 = f4 * f8 + f10 * (1.0F - f8);
      }

      return new Vec3((double)f2, (double)f3, (double)f4);
   }

   public Vec3 getFogColor(float partialTicks) {
      float f = this.getCelestialAngle(partialTicks);
      return this.provider.getFogColor(f, partialTicks);
   }

   public BlockPos getPrecipitationHeight(BlockPos pos) {
      return this.getChunkFromBlockCoords(pos).getPrecipitationHeight(pos);
   }

   public BlockPos getTopSolidOrLiquidBlock(BlockPos pos) {
      Chunk chunk = this.getChunkFromBlockCoords(pos);

      BlockPos blockpos;
      BlockPos blockpos1;
      for(blockpos = new BlockPos(pos.getX(), chunk.getTopFilledSegment() + 16, pos.getZ()); blockpos.getY() >= 0; blockpos = blockpos1) {
         blockpos1 = blockpos.down();
         Material material = chunk.getBlock(blockpos1).getMaterial();
         if (material.blocksMovement() && material != Material.leaves) {
            break;
         }
      }

      return blockpos;
   }

   public float getStarBrightness(float partialTicks) {
      float f = this.getCelestialAngle(partialTicks);
      float f1 = 1.0F - (MathHelper.cos(f * 3.1415927F * 2.0F) * 2.0F + 0.25F);
      f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
      return f1 * f1 * 0.5F;
   }

   public void scheduleUpdate(BlockPos pos, Block blockIn, int delay) {
   }

   public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {
   }

   public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {
   }

   public void updateEntities() {
      this.theProfiler.startSection("entities");
      this.theProfiler.startSection("global");

      int i1;
      Entity entity2;
      CrashReport crashreport1;
      CrashReportCategory crashreportcategory2;
      for(i1 = 0; i1 < this.weatherEffects.size(); ++i1) {
         entity2 = (Entity)this.weatherEffects.get(i1);

         try {
            ++entity2.ticksExisted;
            entity2.onUpdate();
         } catch (Throwable var9) {
            crashreport1 = CrashReport.makeCrashReport(var9, "Ticking entity");
            crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
            if (entity2 == null) {
               crashreportcategory2.addCrashSection("Entity", "~~NULL~~");
            } else {
               entity2.addEntityCrashInfo(crashreportcategory2);
            }

            throw new ReportedException(crashreport1);
         }

         if (entity2.isDead) {
            this.weatherEffects.remove(i1--);
         }
      }

      this.theProfiler.endStartSection("remove");
      this.loadedEntityList.removeAll(this.unloadedEntityList);

      int k1;
      int i2;
      for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
         entity2 = (Entity)this.unloadedEntityList.get(i1);
         k1 = entity2.chunkCoordX;
         i2 = entity2.chunkCoordZ;
         if (entity2.addedToChunk && this.isChunkLoaded(k1, i2, true)) {
            this.getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
         }
      }

      for(i1 = 0; i1 < this.unloadedEntityList.size(); ++i1) {
         this.onEntityRemoved((Entity)this.unloadedEntityList.get(i1));
      }

      this.unloadedEntityList.clear();
      this.theProfiler.endStartSection("regular");

      for(i1 = 0; i1 < this.loadedEntityList.size(); ++i1) {
         entity2 = (Entity)this.loadedEntityList.get(i1);
         if (entity2.ridingEntity != null) {
            if (!entity2.ridingEntity.isDead && entity2.ridingEntity.riddenByEntity == entity2) {
               continue;
            }

            entity2.ridingEntity.riddenByEntity = null;
            entity2.ridingEntity = null;
         }

         this.theProfiler.startSection("tick");
         if (!entity2.isDead) {
            try {
               this.updateEntity(entity2);
            } catch (Throwable var8) {
               crashreport1 = CrashReport.makeCrashReport(var8, "Ticking entity");
               crashreportcategory2 = crashreport1.makeCategory("Entity being ticked");
               entity2.addEntityCrashInfo(crashreportcategory2);
               throw new ReportedException(crashreport1);
            }
         }

         this.theProfiler.endSection();
         this.theProfiler.startSection("remove");
         if (entity2.isDead) {
            k1 = entity2.chunkCoordX;
            i2 = entity2.chunkCoordZ;
            if (entity2.addedToChunk && this.isChunkLoaded(k1, i2, true)) {
               this.getChunkFromChunkCoords(k1, i2).removeEntity(entity2);
            }

            this.loadedEntityList.remove(i1--);
            this.onEntityRemoved(entity2);
         }

         this.theProfiler.endSection();
      }

      this.theProfiler.endStartSection("blockEntities");
      this.processingLoadedTiles = true;
      Iterator iterator = this.tickableTileEntities.iterator();

      while(iterator.hasNext()) {
         TileEntity tileentity = (TileEntity)iterator.next();
         if (!tileentity.isInvalid() && tileentity.hasWorldObj()) {
            BlockPos blockpos = tileentity.getPos();
            if (this.isBlockLoaded(blockpos) && this.worldBorder.contains(blockpos)) {
               try {
                  ((ITickable)tileentity).update();
               } catch (Throwable var7) {
                  CrashReport crashreport2 = CrashReport.makeCrashReport(var7, "Ticking block entity");
                  CrashReportCategory crashreportcategory1 = crashreport2.makeCategory("Block entity being ticked");
                  tileentity.addInfoToCrashReport(crashreportcategory1);
                  throw new ReportedException(crashreport2);
               }
            }
         }

         if (tileentity.isInvalid()) {
            iterator.remove();
            this.loadedTileEntityList.remove(tileentity);
            if (this.isBlockLoaded(tileentity.getPos())) {
               this.getChunkFromBlockCoords(tileentity.getPos()).removeTileEntity(tileentity.getPos());
            }
         }
      }

      this.processingLoadedTiles = false;
      if (!this.tileEntitiesToBeRemoved.isEmpty()) {
         this.tickableTileEntities.removeAll(this.tileEntitiesToBeRemoved);
         this.loadedTileEntityList.removeAll(this.tileEntitiesToBeRemoved);
         this.tileEntitiesToBeRemoved.clear();
      }

      this.theProfiler.endStartSection("pendingBlockEntities");
      if (!this.addedTileEntityList.isEmpty()) {
         for(int j1 = 0; j1 < this.addedTileEntityList.size(); ++j1) {
            TileEntity tileentity1 = (TileEntity)this.addedTileEntityList.get(j1);
            if (!tileentity1.isInvalid()) {
               if (!this.loadedTileEntityList.contains(tileentity1)) {
                  this.addTileEntity(tileentity1);
               }

               if (this.isBlockLoaded(tileentity1.getPos())) {
                  this.getChunkFromBlockCoords(tileentity1.getPos()).addTileEntity(tileentity1.getPos(), tileentity1);
               }

               this.markBlockForUpdate(tileentity1.getPos());
            }
         }

         this.addedTileEntityList.clear();
      }

      this.theProfiler.endSection();
      this.theProfiler.endSection();
   }

   public boolean addTileEntity(TileEntity tile) {
      boolean flag = this.loadedTileEntityList.add(tile);
      if (flag && tile instanceof ITickable) {
         this.tickableTileEntities.add(tile);
      }

      return flag;
   }

   public void addTileEntities(Collection tileEntityCollection) {
      if (this.processingLoadedTiles) {
         this.addedTileEntityList.addAll(tileEntityCollection);
      } else {
         Iterator var2 = tileEntityCollection.iterator();

         while(var2.hasNext()) {
            TileEntity tileentity = (TileEntity)var2.next();
            this.loadedTileEntityList.add(tileentity);
            if (tileentity instanceof ITickable) {
               this.tickableTileEntities.add(tileentity);
            }
         }
      }

   }

   public void updateEntity(Entity ent) {
      this.updateEntityWithOptionalForce(ent, true);
   }

   public void updateEntityWithOptionalForce(Entity entityIn, boolean forceUpdate) {
      int i = MathHelper.floor_double(entityIn.posX);
      int j = MathHelper.floor_double(entityIn.posZ);
      int k = 32;
      if (!forceUpdate || this.isAreaLoaded(i - k, 0, j - k, i + k, 0, j + k, true)) {
         entityIn.lastTickPosX = entityIn.posX;
         entityIn.lastTickPosY = entityIn.posY;
         entityIn.lastTickPosZ = entityIn.posZ;
         entityIn.prevRotationYaw = entityIn.rotationYaw;
         entityIn.prevRotationPitch = entityIn.rotationPitch;
         if (forceUpdate && entityIn.addedToChunk) {
            ++entityIn.ticksExisted;
            if (entityIn.ridingEntity != null) {
               entityIn.updateRidden();
            } else {
               entityIn.onUpdate();
            }
         }

         this.theProfiler.startSection("chunkCheck");
         if (Double.isNaN(entityIn.posX) || Double.isInfinite(entityIn.posX)) {
            entityIn.posX = entityIn.lastTickPosX;
         }

         if (Double.isNaN(entityIn.posY) || Double.isInfinite(entityIn.posY)) {
            entityIn.posY = entityIn.lastTickPosY;
         }

         if (Double.isNaN(entityIn.posZ) || Double.isInfinite(entityIn.posZ)) {
            entityIn.posZ = entityIn.lastTickPosZ;
         }

         if (Double.isNaN((double)entityIn.rotationPitch) || Double.isInfinite((double)entityIn.rotationPitch)) {
            entityIn.rotationPitch = entityIn.prevRotationPitch;
         }

         if (Double.isNaN((double)entityIn.rotationYaw) || Double.isInfinite((double)entityIn.rotationYaw)) {
            entityIn.rotationYaw = entityIn.prevRotationYaw;
         }

         int l = MathHelper.floor_double(entityIn.posX / 16.0D);
         int i1 = MathHelper.floor_double(entityIn.posY / 16.0D);
         int j1 = MathHelper.floor_double(entityIn.posZ / 16.0D);
         if (!entityIn.addedToChunk || entityIn.chunkCoordX != l || entityIn.chunkCoordY != i1 || entityIn.chunkCoordZ != j1) {
            if (entityIn.addedToChunk && this.isChunkLoaded(entityIn.chunkCoordX, entityIn.chunkCoordZ, true)) {
               this.getChunkFromChunkCoords(entityIn.chunkCoordX, entityIn.chunkCoordZ).removeEntityAtIndex(entityIn, entityIn.chunkCoordY);
            }

            if (this.isChunkLoaded(l, j1, true)) {
               entityIn.addedToChunk = true;
               this.getChunkFromChunkCoords(l, j1).addEntity(entityIn);
            } else {
               entityIn.addedToChunk = false;
            }
         }

         this.theProfiler.endSection();
         if (forceUpdate && entityIn.addedToChunk && entityIn.riddenByEntity != null) {
            if (!entityIn.riddenByEntity.isDead && entityIn.riddenByEntity.ridingEntity == entityIn) {
               this.updateEntity(entityIn.riddenByEntity);
            } else {
               entityIn.riddenByEntity.ridingEntity = null;
               entityIn.riddenByEntity = null;
            }
         }
      }

   }

   public boolean checkNoEntityCollision(AxisAlignedBB bb) {
      return this.checkNoEntityCollision(bb, (Entity)null);
   }

   public boolean checkNoEntityCollision(AxisAlignedBB bb, Entity entityIn) {
      List list = this.getEntitiesWithinAABBExcludingEntity((Entity)null, bb);

      for(int i = 0; i < list.size(); ++i) {
         Entity entity = (Entity)list.get(i);
         if (!entity.isDead && entity.preventEntitySpawning && entity != entityIn && (entityIn == null || entityIn.ridingEntity != entity && entityIn.riddenByEntity != entity)) {
            return false;
         }
      }

      return true;
   }

   public boolean checkBlockCollision(AxisAlignedBB bb) {
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k1 = i; k1 <= j; ++k1) {
         for(int l1 = k; l1 <= l; ++l1) {
            for(int i2 = i1; i2 <= j1; ++i2) {
               Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
               if (block.getMaterial() != Material.air) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean isAnyLiquid(AxisAlignedBB bb) {
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k1 = i; k1 <= j; ++k1) {
         for(int l1 = k; l1 <= l; ++l1) {
            for(int i2 = i1; i2 <= j1; ++i2) {
               Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
               if (block.getMaterial().isLiquid()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean isFlammableWithin(AxisAlignedBB bb) {
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX + 1.0D);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY + 1.0D);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
      if (this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

         for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
               for(int i2 = i1; i2 < j1; ++i2) {
                  Block block = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock();
                  if (block == Blocks.fire || block == Blocks.flowing_lava || block == Blocks.lava) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public boolean handleMaterialAcceleration(AxisAlignedBB bb, Material materialIn, Entity entityIn) {
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX + 1.0D);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY + 1.0D);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
      if (!this.isAreaLoaded(i, k, i1, j, l, j1, true)) {
         return false;
      } else {
         boolean flag = false;
         Vec3 vec3 = new Vec3(0.0D, 0.0D, 0.0D);
         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

         for(int k1 = i; k1 < j; ++k1) {
            for(int l1 = k; l1 < l; ++l1) {
               for(int i2 = i1; i2 < j1; ++i2) {
                  blockpos$mutableblockpos.func_181079_c(k1, l1, i2);
                  IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos);
                  Block block = iblockstate.getBlock();
                  if (block.getMaterial() == materialIn) {
                     double d0 = (double)((float)(l1 + 1) - BlockLiquid.getLiquidHeightPercent((Integer)iblockstate.getValue(BlockLiquid.LEVEL)));
                     if ((double)l >= d0) {
                        flag = true;
                        vec3 = block.modifyAcceleration(this, blockpos$mutableblockpos, entityIn, vec3);
                     }
                  }
               }
            }
         }

         if (vec3.lengthVector() > 0.0D && entityIn.isPushedByWater()) {
            vec3 = vec3.normalize();
            double d1 = 0.014D;
            entityIn.motionX += vec3.xCoord * d1;
            entityIn.motionY += vec3.yCoord * d1;
            entityIn.motionZ += vec3.zCoord * d1;
         }

         return flag;
      }
   }

   public boolean isMaterialInBB(AxisAlignedBB bb, Material materialIn) {
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX + 1.0D);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY + 1.0D);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k1 = i; k1 < j; ++k1) {
         for(int l1 = k; l1 < l; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
               if (this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2)).getBlock().getMaterial() == materialIn) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean isAABBInMaterial(AxisAlignedBB bb, Material materialIn) {
      int i = MathHelper.floor_double(bb.minX);
      int j = MathHelper.floor_double(bb.maxX + 1.0D);
      int k = MathHelper.floor_double(bb.minY);
      int l = MathHelper.floor_double(bb.maxY + 1.0D);
      int i1 = MathHelper.floor_double(bb.minZ);
      int j1 = MathHelper.floor_double(bb.maxZ + 1.0D);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      for(int k1 = i; k1 < j; ++k1) {
         for(int l1 = k; l1 < l; ++l1) {
            for(int i2 = i1; i2 < j1; ++i2) {
               IBlockState iblockstate = this.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, l1, i2));
               Block block = iblockstate.getBlock();
               if (block.getMaterial() == materialIn) {
                  int j2 = (Integer)iblockstate.getValue(BlockLiquid.LEVEL);
                  double d0 = (double)(l1 + 1);
                  if (j2 < 8) {
                     d0 = (double)(l1 + 1) - (double)j2 / 8.0D;
                  }

                  if (d0 >= bb.minY) {
                     return true;
                  }
               }
            }
         }
      }

      return false;
   }

   public Explosion createExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isSmoking) {
      return this.newExplosion(entityIn, x, y, z, strength, false, isSmoking);
   }

   public Explosion newExplosion(Entity entityIn, double x, double y, double z, float strength, boolean isFlaming, boolean isSmoking) {
      Explosion explosion = new Explosion(this, entityIn, x, y, z, strength, isFlaming, isSmoking);
      explosion.doExplosionA();
      explosion.doExplosionB(true);
      return explosion;
   }

   public float getBlockDensity(Vec3 vec, AxisAlignedBB bb) {
      double d0 = 1.0D / ((bb.maxX - bb.minX) * 2.0D + 1.0D);
      double d1 = 1.0D / ((bb.maxY - bb.minY) * 2.0D + 1.0D);
      double d2 = 1.0D / ((bb.maxZ - bb.minZ) * 2.0D + 1.0D);
      double d3 = (1.0D - Math.floor(1.0D / d0) * d0) / 2.0D;
      double d4 = (1.0D - Math.floor(1.0D / d2) * d2) / 2.0D;
      if (d0 >= 0.0D && d1 >= 0.0D && d2 >= 0.0D) {
         int i = 0;
         int j = 0;

         for(float f = 0.0F; f <= 1.0F; f = (float)((double)f + d0)) {
            for(float f1 = 0.0F; f1 <= 1.0F; f1 = (float)((double)f1 + d1)) {
               for(float f2 = 0.0F; f2 <= 1.0F; f2 = (float)((double)f2 + d2)) {
                  double d5 = bb.minX + (bb.maxX - bb.minX) * (double)f;
                  double d6 = bb.minY + (bb.maxY - bb.minY) * (double)f1;
                  double d7 = bb.minZ + (bb.maxZ - bb.minZ) * (double)f2;
                  if (this.rayTraceBlocks(new Vec3(d5 + d3, d6, d7 + d4), vec) == null) {
                     ++i;
                  }

                  ++j;
               }
            }
         }

         return (float)i / (float)j;
      } else {
         return 0.0F;
      }
   }

   public boolean extinguishFire(EntityPlayer player, BlockPos pos, EnumFacing side) {
      pos = pos.offset(side);
      if (this.getBlockState(pos).getBlock() == Blocks.fire) {
         this.playAuxSFXAtEntity(player, 1004, pos, 0);
         this.setBlockToAir(pos);
         return true;
      } else {
         return false;
      }
   }

   public String getDebugLoadedEntities() {
      return "All: " + this.loadedEntityList.size();
   }

   public String getProviderName() {
      return this.chunkProvider.makeString();
   }

   public TileEntity getTileEntity(BlockPos pos) {
      if (!this.isValid(pos)) {
         return null;
      } else {
         TileEntity tileentity = null;
         int j;
         TileEntity tileentity2;
         if (this.processingLoadedTiles) {
            for(j = 0; j < this.addedTileEntityList.size(); ++j) {
               tileentity2 = (TileEntity)this.addedTileEntityList.get(j);
               if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos)) {
                  tileentity = tileentity2;
                  break;
               }
            }
         }

         if (tileentity == null) {
            tileentity = this.getChunkFromBlockCoords(pos).getTileEntity(pos, Chunk.EnumCreateEntityType.IMMEDIATE);
         }

         if (tileentity == null) {
            for(j = 0; j < this.addedTileEntityList.size(); ++j) {
               tileentity2 = (TileEntity)this.addedTileEntityList.get(j);
               if (!tileentity2.isInvalid() && tileentity2.getPos().equals(pos)) {
                  tileentity = tileentity2;
                  break;
               }
            }
         }

         return tileentity;
      }
   }

   public void setTileEntity(BlockPos pos, TileEntity tileEntityIn) {
      if (tileEntityIn != null && !tileEntityIn.isInvalid()) {
         if (this.processingLoadedTiles) {
            tileEntityIn.setPos(pos);
            Iterator iterator = this.addedTileEntityList.iterator();

            while(iterator.hasNext()) {
               TileEntity tileentity = (TileEntity)iterator.next();
               if (tileentity.getPos().equals(pos)) {
                  tileentity.invalidate();
                  iterator.remove();
               }
            }

            this.addedTileEntityList.add(tileEntityIn);
         } else {
            this.addTileEntity(tileEntityIn);
            this.getChunkFromBlockCoords(pos).addTileEntity(pos, tileEntityIn);
         }
      }

   }

   public void removeTileEntity(BlockPos pos) {
      TileEntity tileentity = this.getTileEntity(pos);
      if (tileentity != null && this.processingLoadedTiles) {
         tileentity.invalidate();
         this.addedTileEntityList.remove(tileentity);
      } else {
         if (tileentity != null) {
            this.addedTileEntityList.remove(tileentity);
            this.loadedTileEntityList.remove(tileentity);
            this.tickableTileEntities.remove(tileentity);
         }

         this.getChunkFromBlockCoords(pos).removeTileEntity(pos);
      }

   }

   public void markTileEntityForRemoval(TileEntity tileEntityIn) {
      this.tileEntitiesToBeRemoved.add(tileEntityIn);
   }

   public boolean isBlockFullCube(BlockPos pos) {
      IBlockState iblockstate = this.getBlockState(pos);
      AxisAlignedBB axisalignedbb = iblockstate.getBlock().getCollisionBoundingBox(this, pos, iblockstate);
      return axisalignedbb != null && axisalignedbb.getAverageEdgeLength() >= 1.0D;
   }

   public boolean isBlockNormalCube(BlockPos pos, boolean _default) {
      if (!this.isValid(pos)) {
         return _default;
      } else {
         Chunk chunk = this.chunkProvider.provideChunk(pos);
         if (chunk.isEmpty()) {
            return _default;
         } else {
            Block block = this.getBlockState(pos).getBlock();
            return block.getMaterial().isOpaque() && block.isFullCube();
         }
      }
   }

   public void calculateInitialSkylight() {
      int i = this.calculateSkylightSubtracted(1.0F);
      if (i != this.skylightSubtracted) {
         this.skylightSubtracted = i;
      }

   }

   public void setAllowedSpawnTypes(boolean hostile, boolean peaceful) {
      this.spawnHostileMobs = hostile;
      this.spawnPeacefulMobs = peaceful;
   }

   public void tick() {
      this.updateWeather();
   }

   protected void calculateInitialWeather() {
      if (this.worldInfo.isRaining()) {
         this.rainingStrength = 1.0F;
         if (this.worldInfo.isThundering()) {
            this.thunderingStrength = 1.0F;
         }
      }

   }

   protected void updateWeather() {
      if (!this.provider.getHasNoSky() && !this.isRemote) {
         int i = this.worldInfo.getCleanWeatherTime();
         if (i > 0) {
            --i;
            this.worldInfo.setCleanWeatherTime(i);
            this.worldInfo.setThunderTime(this.worldInfo.isThundering() ? 1 : 2);
            this.worldInfo.setRainTime(this.worldInfo.isRaining() ? 1 : 2);
         }

         int j = this.worldInfo.getThunderTime();
         if (j <= 0) {
            if (this.worldInfo.isThundering()) {
               this.worldInfo.setThunderTime(this.rand.nextInt(12000) + 3600);
            } else {
               this.worldInfo.setThunderTime(this.rand.nextInt(168000) + 12000);
            }
         } else {
            --j;
            this.worldInfo.setThunderTime(j);
            if (j <= 0) {
               this.worldInfo.setThundering(!this.worldInfo.isThundering());
            }
         }

         this.prevThunderingStrength = this.thunderingStrength;
         if (this.worldInfo.isThundering()) {
            this.thunderingStrength = (float)((double)this.thunderingStrength + 0.01D);
         } else {
            this.thunderingStrength = (float)((double)this.thunderingStrength - 0.01D);
         }

         this.thunderingStrength = MathHelper.clamp_float(this.thunderingStrength, 0.0F, 1.0F);
         int k = this.worldInfo.getRainTime();
         if (k <= 0) {
            if (this.worldInfo.isRaining()) {
               this.worldInfo.setRainTime(this.rand.nextInt(12000) + 12000);
            } else {
               this.worldInfo.setRainTime(this.rand.nextInt(168000) + 12000);
            }
         } else {
            --k;
            this.worldInfo.setRainTime(k);
            if (k <= 0) {
               this.worldInfo.setRaining(!this.worldInfo.isRaining());
            }
         }

         this.prevRainingStrength = this.rainingStrength;
         if (this.worldInfo.isRaining()) {
            this.rainingStrength = (float)((double)this.rainingStrength + 0.01D);
         } else {
            this.rainingStrength = (float)((double)this.rainingStrength - 0.01D);
         }

         this.rainingStrength = MathHelper.clamp_float(this.rainingStrength, 0.0F, 1.0F);
      }

   }

   protected void setActivePlayerChunksAndCheckLight() {
      this.activeChunkSet.clear();
      this.theProfiler.startSection("buildList");

      int k1;
      EntityPlayer entityplayer1;
      int j;
      int k;
      int j2;
      for(k1 = 0; k1 < this.playerEntities.size(); ++k1) {
         entityplayer1 = (EntityPlayer)this.playerEntities.get(k1);
         j = MathHelper.floor_double(entityplayer1.posX / 16.0D);
         k = MathHelper.floor_double(entityplayer1.posZ / 16.0D);
         j2 = this.getRenderDistanceChunks();

         for(int i1 = -j2; i1 <= j2; ++i1) {
            for(int j1 = -j2; j1 <= j2; ++j1) {
               this.activeChunkSet.add(new ChunkCoordIntPair(i1 + j, j1 + k));
            }
         }
      }

      this.theProfiler.endSection();
      if (this.ambientTickCountdown > 0) {
         --this.ambientTickCountdown;
      }

      this.theProfiler.startSection("playerCheckLight");
      if (!this.playerEntities.isEmpty()) {
         k1 = this.rand.nextInt(this.playerEntities.size());
         entityplayer1 = (EntityPlayer)this.playerEntities.get(k1);
         j = MathHelper.floor_double(entityplayer1.posX) + this.rand.nextInt(11) - 5;
         k = MathHelper.floor_double(entityplayer1.posY) + this.rand.nextInt(11) - 5;
         j2 = MathHelper.floor_double(entityplayer1.posZ) + this.rand.nextInt(11) - 5;
         this.checkLight(new BlockPos(j, k, j2));
      }

      this.theProfiler.endSection();
   }

   protected abstract int getRenderDistanceChunks();

   protected void playMoodSoundAndCheckLight(int p_147467_1_, int p_147467_2_, Chunk chunkIn) {
      this.theProfiler.endStartSection("moodSound");
      if (this.ambientTickCountdown == 0 && !this.isRemote) {
         this.updateLCG = this.updateLCG * 3 + 1013904223;
         int i = this.updateLCG >> 2;
         int j = i & 15;
         int k = i >> 8 & 15;
         int l = i >> 16 & 255;
         BlockPos blockpos = new BlockPos(j, l, k);
         Block block = chunkIn.getBlock(blockpos);
         j += p_147467_1_;
         k += p_147467_2_;
         if (block.getMaterial() == Material.air && this.getLight(blockpos) <= this.rand.nextInt(8) && this.getLightFor(EnumSkyBlock.SKY, blockpos) <= 0) {
            EntityPlayer entityplayer = this.getClosestPlayer((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D, 8.0D);
            if (entityplayer != null && entityplayer.getDistanceSq((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D) > 4.0D) {
               this.playSoundEffect((double)j + 0.5D, (double)l + 0.5D, (double)k + 0.5D, "ambient.cave.cave", 0.7F, 0.8F + this.rand.nextFloat() * 0.2F);
               this.ambientTickCountdown = this.rand.nextInt(12000) + 6000;
            }
         }
      }

      this.theProfiler.endStartSection("checkLight");
      chunkIn.enqueueRelightChecks();
   }

   protected void updateBlocks() {
      this.setActivePlayerChunksAndCheckLight();
   }

   public void forceBlockUpdateTick(Block blockType, BlockPos pos, Random random) {
      this.scheduledUpdatesAreImmediate = true;
      blockType.updateTick(this, pos, this.getBlockState(pos), random);
      this.scheduledUpdatesAreImmediate = false;
   }

   public boolean canBlockFreezeWater(BlockPos pos) {
      return this.canBlockFreeze(pos, false);
   }

   public boolean canBlockFreezeNoWater(BlockPos pos) {
      return this.canBlockFreeze(pos, true);
   }

   public boolean canBlockFreeze(BlockPos pos, boolean noWaterAdj) {
      BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
      float f = biomegenbase.getFloatTemperature(pos);
      if (f > 0.15F) {
         return false;
      } else {
         if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
            IBlockState iblockstate = this.getBlockState(pos);
            Block block = iblockstate.getBlock();
            if ((block == Blocks.water || block == Blocks.flowing_water) && (Integer)iblockstate.getValue(BlockLiquid.LEVEL) == 0) {
               if (!noWaterAdj) {
                  return true;
               }

               boolean flag = this.isWater(pos.west()) && this.isWater(pos.east()) && this.isWater(pos.north()) && this.isWater(pos.south());
               return !flag;
            }
         }

         return false;
      }
   }

   private boolean isWater(BlockPos pos) {
      return this.getBlockState(pos).getBlock().getMaterial() == Material.water;
   }

   public boolean canSnowAt(BlockPos pos, boolean checkLight) {
      BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
      float f = biomegenbase.getFloatTemperature(pos);
      if (f > 0.15F) {
         return false;
      } else if (!checkLight) {
         return true;
      } else if (pos.getY() >= 0 && pos.getY() < 256 && this.getLightFor(EnumSkyBlock.BLOCK, pos) < 10) {
         Block block = this.getBlockState(pos).getBlock();
         return block.getMaterial() == Material.air && Blocks.snow_layer.canPlaceBlockAt(this, pos);
      } else {
         return false;
      }
   }

   public boolean checkLight(BlockPos pos) {
      boolean flag = false;
      if (!this.provider.getHasNoSky()) {
         flag |= this.checkLightFor(EnumSkyBlock.SKY, pos);
      }

      flag |= this.checkLightFor(EnumSkyBlock.BLOCK, pos);
      return flag;
   }

   private int getRawLight(BlockPos pos, EnumSkyBlock lightType) {
      if (lightType == EnumSkyBlock.SKY && this.canSeeSky(pos)) {
         return 15;
      } else {
         Block block = this.getBlockState(pos).getBlock();
         int i = lightType == EnumSkyBlock.SKY ? 0 : block.getLightValue();
         int j = block.getLightOpacity();
         if (j >= 15 && block.getLightValue() > 0) {
            j = 1;
         }

         if (j < 1) {
            j = 1;
         }

         if (j >= 15) {
            return 0;
         } else if (i >= 14) {
            return i;
         } else {
            EnumFacing[] var6 = EnumFacing.values();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               EnumFacing enumfacing = var6[var8];
               BlockPos blockpos = pos.offset(enumfacing);
               int k = this.getLightFor(lightType, blockpos) - j;
               if (k > i) {
                  i = k;
               }

               if (i >= 14) {
                  return i;
               }
            }

            return i;
         }
      }
   }

   public boolean checkLightFor(EnumSkyBlock lightType, BlockPos pos) {
      if (!this.isAreaLoaded(pos, 17, false)) {
         return false;
      } else {
         int i = 0;
         int j = 0;
         this.theProfiler.startSection("getBrightness");
         int k = this.getLightFor(lightType, pos);
         int l = this.getRawLight(pos, lightType);
         int i1 = pos.getX();
         int j1 = pos.getY();
         int k1 = pos.getZ();
         int l1;
         int j5;
         int k5;
         int l5;
         int j6;
         int j3;
         int k3;
         int l3;
         if (l > k) {
            this.lightUpdateBlockList[j++] = 133152;
         } else if (l < k) {
            this.lightUpdateBlockList[j++] = 133152 | k << 18;

            label90:
            while(true) {
               int l2;
               do {
                  do {
                     BlockPos blockpos;
                     do {
                        if (i >= j) {
                           i = 0;
                           break label90;
                        }

                        l1 = this.lightUpdateBlockList[i++];
                        j5 = (l1 & 63) - 32 + i1;
                        k5 = (l1 >> 6 & 63) - 32 + j1;
                        l5 = (l1 >> 12 & 63) - 32 + k1;
                        l2 = l1 >> 18 & 15;
                        blockpos = new BlockPos(j5, k5, l5);
                        j6 = this.getLightFor(lightType, blockpos);
                     } while(j6 != l2);

                     this.setLightFor(lightType, blockpos, 0);
                  } while(l2 <= 0);

                  j3 = MathHelper.abs_int(j5 - i1);
                  k3 = MathHelper.abs_int(k5 - j1);
                  l3 = MathHelper.abs_int(l5 - k1);
               } while(j3 + k3 + l3 >= 17);

               BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
               EnumFacing[] var21 = EnumFacing.values();
               int var22 = var21.length;

               for(int var23 = 0; var23 < var22; ++var23) {
                  EnumFacing enumfacing = var21[var23];
                  int i4 = j5 + enumfacing.getFrontOffsetX();
                  int j4 = k5 + enumfacing.getFrontOffsetY();
                  int k4 = l5 + enumfacing.getFrontOffsetZ();
                  blockpos$mutableblockpos.func_181079_c(i4, j4, k4);
                  int l4 = Math.max(1, this.getBlockState(blockpos$mutableblockpos).getBlock().getLightOpacity());
                  j6 = this.getLightFor(lightType, blockpos$mutableblockpos);
                  if (j6 == l2 - l4 && j < this.lightUpdateBlockList.length) {
                     this.lightUpdateBlockList[j++] = i4 - i1 + 32 | j4 - j1 + 32 << 6 | k4 - k1 + 32 << 12 | l2 - l4 << 18;
                  }
               }
            }
         }

         this.theProfiler.endSection();
         this.theProfiler.startSection("checkedPosition < toCheckCount");

         while(i < j) {
            l1 = this.lightUpdateBlockList[i++];
            j5 = (l1 & 63) - 32 + i1;
            k5 = (l1 >> 6 & 63) - 32 + j1;
            l5 = (l1 >> 12 & 63) - 32 + k1;
            BlockPos blockpos1 = new BlockPos(j5, k5, l5);
            int i6 = this.getLightFor(lightType, blockpos1);
            j6 = this.getRawLight(blockpos1, lightType);
            if (j6 != i6) {
               this.setLightFor(lightType, blockpos1, j6);
               if (j6 > i6) {
                  j3 = Math.abs(j5 - i1);
                  k3 = Math.abs(k5 - j1);
                  l3 = Math.abs(l5 - k1);
                  boolean flag = j < this.lightUpdateBlockList.length - 6;
                  if (j3 + k3 + l3 < 17 && flag) {
                     if (this.getLightFor(lightType, blockpos1.west()) < j6) {
                        this.lightUpdateBlockList[j++] = j5 - 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
                     }

                     if (this.getLightFor(lightType, blockpos1.east()) < j6) {
                        this.lightUpdateBlockList[j++] = j5 + 1 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
                     }

                     if (this.getLightFor(lightType, blockpos1.down()) < j6) {
                        this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
                     }

                     if (this.getLightFor(lightType, blockpos1.up()) < j6) {
                        this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 + 1 - j1 + 32 << 6) + (l5 - k1 + 32 << 12);
                     }

                     if (this.getLightFor(lightType, blockpos1.north()) < j6) {
                        this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 - 1 - k1 + 32 << 12);
                     }

                     if (this.getLightFor(lightType, blockpos1.south()) < j6) {
                        this.lightUpdateBlockList[j++] = j5 - i1 + 32 + (k5 - j1 + 32 << 6) + (l5 + 1 - k1 + 32 << 12);
                     }
                  }
               }
            }
         }

         this.theProfiler.endSection();
         return true;
      }
   }

   public boolean tickUpdates(boolean p_72955_1_) {
      return false;
   }

   public List getPendingBlockUpdates(Chunk chunkIn, boolean p_72920_2_) {
      return null;
   }

   public List func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_) {
      return null;
   }

   public List getEntitiesWithinAABBExcludingEntity(Entity entityIn, AxisAlignedBB bb) {
      return this.getEntitiesInAABBexcluding(entityIn, bb, EntitySelectors.NOT_SPECTATING);
   }

   public List getEntitiesInAABBexcluding(Entity entityIn, AxisAlignedBB boundingBox, Predicate predicate) {
      List list = Lists.newArrayList();
      int i = MathHelper.floor_double((boundingBox.minX - 2.0D) / 16.0D);
      int j = MathHelper.floor_double((boundingBox.maxX + 2.0D) / 16.0D);
      int k = MathHelper.floor_double((boundingBox.minZ - 2.0D) / 16.0D);
      int l = MathHelper.floor_double((boundingBox.maxZ + 2.0D) / 16.0D);

      for(int i1 = i; i1 <= j; ++i1) {
         for(int j1 = k; j1 <= l; ++j1) {
            if (this.isChunkLoaded(i1, j1, true)) {
               this.getChunkFromChunkCoords(i1, j1).getEntitiesWithinAABBForEntity(entityIn, boundingBox, list, predicate);
            }
         }
      }

      return list;
   }

   public List getEntities(Class entityType, Predicate filter) {
      List list = Lists.newArrayList();
      Iterator var4 = this.loadedEntityList.iterator();

      while(var4.hasNext()) {
         Entity entity = (Entity)var4.next();
         if (entityType.isAssignableFrom(entity.getClass()) && filter.apply(entity)) {
            list.add(entity);
         }
      }

      return list;
   }

   public List getPlayers(Class playerType, Predicate filter) {
      List list = Lists.newArrayList();
      Iterator var4 = this.playerEntities.iterator();

      while(var4.hasNext()) {
         Entity entity = (Entity)var4.next();
         if (playerType.isAssignableFrom(entity.getClass()) && filter.apply(entity)) {
            list.add(entity);
         }
      }

      return list;
   }

   public List getEntitiesWithinAABB(Class classEntity, AxisAlignedBB bb) {
      return this.getEntitiesWithinAABB(classEntity, bb, EntitySelectors.NOT_SPECTATING);
   }

   public List getEntitiesWithinAABB(Class clazz, AxisAlignedBB aabb, Predicate filter) {
      int i = MathHelper.floor_double((aabb.minX - 2.0D) / 16.0D);
      int j = MathHelper.floor_double((aabb.maxX + 2.0D) / 16.0D);
      int k = MathHelper.floor_double((aabb.minZ - 2.0D) / 16.0D);
      int l = MathHelper.floor_double((aabb.maxZ + 2.0D) / 16.0D);
      List list = Lists.newArrayList();

      for(int i1 = i; i1 <= j; ++i1) {
         for(int j1 = k; j1 <= l; ++j1) {
            if (this.isChunkLoaded(i1, j1, true)) {
               this.getChunkFromChunkCoords(i1, j1).getEntitiesOfTypeWithinAAAB(clazz, aabb, list, filter);
            }
         }
      }

      return list;
   }

   public Entity findNearestEntityWithinAABB(Class entityType, AxisAlignedBB aabb, Entity closestTo) {
      List list = this.getEntitiesWithinAABB(entityType, aabb);
      Entity t = null;
      double d0 = Double.MAX_VALUE;

      for(int i = 0; i < list.size(); ++i) {
         Entity t1 = (Entity)list.get(i);
         if (t1 != closestTo && EntitySelectors.NOT_SPECTATING.apply(t1)) {
            double d1 = closestTo.getDistanceSqToEntity(t1);
            if (d1 <= d0) {
               t = t1;
               d0 = d1;
            }
         }
      }

      return t;
   }

   public Entity getEntityByID(int id) {
      return (Entity)this.entitiesById.lookup(id);
   }

   public List getLoadedEntityList() {
      return this.loadedEntityList;
   }

   public void markChunkDirty(BlockPos pos, TileEntity unusedTileEntity) {
      if (this.isBlockLoaded(pos)) {
         this.getChunkFromBlockCoords(pos).setChunkModified();
      }

   }

   public int countEntities(Class entityType) {
      int i = 0;
      Iterator var3 = this.loadedEntityList.iterator();

      while(true) {
         Entity entity;
         do {
            if (!var3.hasNext()) {
               return i;
            }

            entity = (Entity)var3.next();
         } while(entity instanceof EntityLiving && ((EntityLiving)entity).isNoDespawnRequired());

         if (entityType.isAssignableFrom(entity.getClass())) {
            ++i;
         }
      }
   }

   public void loadEntities(Collection entityCollection) {
      this.loadedEntityList.addAll(entityCollection);
      Iterator var2 = entityCollection.iterator();

      while(var2.hasNext()) {
         Entity entity = (Entity)var2.next();
         this.onEntityAdded(entity);
      }

   }

   public void unloadEntities(Collection entityCollection) {
      this.unloadedEntityList.addAll(entityCollection);
   }

   public boolean canBlockBePlaced(Block blockIn, BlockPos pos, boolean p_175716_3_, EnumFacing side, Entity entityIn, ItemStack itemStackIn) {
      Block block = this.getBlockState(pos).getBlock();
      AxisAlignedBB axisalignedbb = p_175716_3_ ? null : blockIn.getCollisionBoundingBox(this, pos, blockIn.getDefaultState());
      return (axisalignedbb == null || this.checkNoEntityCollision(axisalignedbb, entityIn)) && (block.getMaterial() == Material.circuits && blockIn == Blocks.anvil || block.getMaterial().isReplaceable() && blockIn.canReplace(this, pos, side, itemStackIn));
   }

   public int func_181545_F() {
      return this.field_181546_a;
   }

   public void func_181544_b(int p_181544_1_) {
      this.field_181546_a = p_181544_1_;
   }

   public int getStrongPower(BlockPos pos, EnumFacing direction) {
      IBlockState iblockstate = this.getBlockState(pos);
      return iblockstate.getBlock().getStrongPower(this, pos, iblockstate, direction);
   }

   public WorldType getWorldType() {
      return this.worldInfo.getTerrainType();
   }

   public int getStrongPower(BlockPos pos) {
      int i = 0;
      int i = Math.max(i, this.getStrongPower(pos.down(), EnumFacing.DOWN));
      if (i >= 15) {
         return i;
      } else {
         i = Math.max(i, this.getStrongPower(pos.up(), EnumFacing.UP));
         if (i >= 15) {
            return i;
         } else {
            i = Math.max(i, this.getStrongPower(pos.north(), EnumFacing.NORTH));
            if (i >= 15) {
               return i;
            } else {
               i = Math.max(i, this.getStrongPower(pos.south(), EnumFacing.SOUTH));
               if (i >= 15) {
                  return i;
               } else {
                  i = Math.max(i, this.getStrongPower(pos.west(), EnumFacing.WEST));
                  if (i >= 15) {
                     return i;
                  } else {
                     i = Math.max(i, this.getStrongPower(pos.east(), EnumFacing.EAST));
                     return i >= 15 ? i : i;
                  }
               }
            }
         }
      }
   }

   public boolean isSidePowered(BlockPos pos, EnumFacing side) {
      return this.getRedstonePower(pos, side) > 0;
   }

   public int getRedstonePower(BlockPos pos, EnumFacing facing) {
      IBlockState iblockstate = this.getBlockState(pos);
      Block block = iblockstate.getBlock();
      return block.isNormalCube() ? this.getStrongPower(pos) : block.getWeakPower(this, pos, iblockstate, facing);
   }

   public boolean isBlockPowered(BlockPos pos) {
      return this.getRedstonePower(pos.down(), EnumFacing.DOWN) > 0 || this.getRedstonePower(pos.up(), EnumFacing.UP) > 0 || this.getRedstonePower(pos.north(), EnumFacing.NORTH) > 0 || this.getRedstonePower(pos.south(), EnumFacing.SOUTH) > 0 || this.getRedstonePower(pos.west(), EnumFacing.WEST) > 0 || this.getRedstonePower(pos.east(), EnumFacing.EAST) > 0;
   }

   public int isBlockIndirectlyGettingPowered(BlockPos pos) {
      int i = 0;
      EnumFacing[] var3 = EnumFacing.values();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         EnumFacing enumfacing = var3[var5];
         int j = this.getRedstonePower(pos.offset(enumfacing), enumfacing);
         if (j >= 15) {
            return 15;
         }

         if (j > i) {
            i = j;
         }
      }

      return i;
   }

   public EntityPlayer getClosestPlayerToEntity(Entity entityIn, double distance) {
      return this.getClosestPlayer(entityIn.posX, entityIn.posY, entityIn.posZ, distance);
   }

   public EntityPlayer getClosestPlayer(double x, double y, double z, double distance) {
      double d0 = -1.0D;
      EntityPlayer entityplayer = null;

      for(int i = 0; i < this.playerEntities.size(); ++i) {
         EntityPlayer entityplayer1 = (EntityPlayer)this.playerEntities.get(i);
         if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1)) {
            double d1 = entityplayer1.getDistanceSq(x, y, z);
            if ((distance < 0.0D || d1 < distance * distance) && (d0 == -1.0D || d1 < d0)) {
               d0 = d1;
               entityplayer = entityplayer1;
            }
         }
      }

      return entityplayer;
   }

   public boolean isAnyPlayerWithinRangeAt(double x, double y, double z, double range) {
      for(int i = 0; i < this.playerEntities.size(); ++i) {
         EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
         if (EntitySelectors.NOT_SPECTATING.apply(entityplayer)) {
            double d0 = entityplayer.getDistanceSq(x, y, z);
            if (range < 0.0D || d0 < range * range) {
               return true;
            }
         }
      }

      return false;
   }

   public EntityPlayer getPlayerEntityByName(String name) {
      for(int i = 0; i < this.playerEntities.size(); ++i) {
         EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
         if (name.equals(entityplayer.getName())) {
            return entityplayer;
         }
      }

      return null;
   }

   public EntityPlayer getPlayerEntityByUUID(UUID uuid) {
      for(int i = 0; i < this.playerEntities.size(); ++i) {
         EntityPlayer entityplayer = (EntityPlayer)this.playerEntities.get(i);
         if (uuid.equals(entityplayer.getUniqueID())) {
            return entityplayer;
         }
      }

      return null;
   }

   public void sendQuittingDisconnectingPacket() {
   }

   public void checkSessionLock() throws MinecraftException {
      this.saveHandler.checkSessionLock();
   }

   public long getSeed() {
      return this.worldInfo.getSeed();
   }

   public long getTotalWorldTime() {
      return this.worldInfo.getWorldTotalTime();
   }

   public void setTotalWorldTime(long worldTime) {
      this.worldInfo.setWorldTotalTime(worldTime);
   }

   public long getWorldTime() {
      TimeChangerMod timeChanger = (TimeChangerMod)Autumn.MANAGER_REGISTRY.moduleManager.getModuleOrNull(TimeChangerMod.class);
      return timeChanger.isEnabled() ? ((Double)timeChanger.time.getValue()).longValue() : this.worldInfo.getWorldTime();
   }

   public void setWorldTime(long time) {
      this.worldInfo.setWorldTime(time);
   }

   public BlockPos getSpawnPoint() {
      BlockPos blockpos = new BlockPos(this.worldInfo.getSpawnX(), this.worldInfo.getSpawnY(), this.worldInfo.getSpawnZ());
      if (!this.getWorldBorder().contains(blockpos)) {
         blockpos = this.getHeight(new BlockPos(this.getWorldBorder().getCenterX(), 0.0D, this.getWorldBorder().getCenterZ()));
      }

      return blockpos;
   }

   public void setSpawnPoint(BlockPos pos) {
      this.worldInfo.setSpawn(pos);
   }

   public void joinEntityInSurroundings(Entity entityIn) {
      int i = MathHelper.floor_double(entityIn.posX / 16.0D);
      int j = MathHelper.floor_double(entityIn.posZ / 16.0D);
      int k = 2;

      for(int l = i - k; l <= i + k; ++l) {
         for(int i1 = j - k; i1 <= j + k; ++i1) {
            this.getChunkFromChunkCoords(l, i1);
         }
      }

      if (!this.loadedEntityList.contains(entityIn)) {
         this.loadedEntityList.add(entityIn);
      }

   }

   public boolean isBlockModifiable(EntityPlayer player, BlockPos pos) {
      return true;
   }

   public void setEntityState(Entity entityIn, byte state) {
   }

   public IChunkProvider getChunkProvider() {
      return this.chunkProvider;
   }

   public void addBlockEvent(BlockPos pos, Block blockIn, int eventID, int eventParam) {
      blockIn.onBlockEventReceived(this, pos, this.getBlockState(pos), eventID, eventParam);
   }

   public ISaveHandler getSaveHandler() {
      return this.saveHandler;
   }

   public WorldInfo getWorldInfo() {
      return this.worldInfo;
   }

   public GameRules getGameRules() {
      return this.worldInfo.getGameRulesInstance();
   }

   public void updateAllPlayersSleepingFlag() {
   }

   public float getThunderStrength(float delta) {
      return (this.prevThunderingStrength + (this.thunderingStrength - this.prevThunderingStrength) * delta) * this.getRainStrength(delta);
   }

   public void setThunderStrength(float strength) {
      this.prevThunderingStrength = strength;
      this.thunderingStrength = strength;
   }

   public float getRainStrength(float delta) {
      return this.prevRainingStrength + (this.rainingStrength - this.prevRainingStrength) * delta;
   }

   public void setRainStrength(float strength) {
      this.prevRainingStrength = strength;
      this.rainingStrength = strength;
   }

   public boolean isThundering() {
      return (double)this.getThunderStrength(1.0F) > 0.9D;
   }

   public boolean isRaining() {
      return (double)this.getRainStrength(1.0F) > 0.2D;
   }

   public boolean canLightningStrike(BlockPos strikePosition) {
      if (!this.isRaining()) {
         return false;
      } else if (!this.canSeeSky(strikePosition)) {
         return false;
      } else if (this.getPrecipitationHeight(strikePosition).getY() > strikePosition.getY()) {
         return false;
      } else {
         BiomeGenBase biomegenbase = this.getBiomeGenForCoords(strikePosition);
         return !biomegenbase.getEnableSnow() && !this.canSnowAt(strikePosition, false) && biomegenbase.canSpawnLightningBolt();
      }
   }

   public boolean isBlockinHighHumidity(BlockPos pos) {
      BiomeGenBase biomegenbase = this.getBiomeGenForCoords(pos);
      return biomegenbase.isHighHumidity();
   }

   public MapStorage getMapStorage() {
      return this.mapStorage;
   }

   public void setItemData(String dataID, WorldSavedData worldSavedDataIn) {
      this.mapStorage.setData(dataID, worldSavedDataIn);
   }

   public WorldSavedData loadItemData(Class clazz, String dataID) {
      return this.mapStorage.loadData(clazz, dataID);
   }

   public int getUniqueDataId(String key) {
      return this.mapStorage.getUniqueDataId(key);
   }

   public void playBroadcastSound(int p_175669_1_, BlockPos pos, int p_175669_3_) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         ((IWorldAccess)this.worldAccesses.get(i)).broadcastSound(p_175669_1_, pos, p_175669_3_);
      }

   }

   public void playAuxSFX(int p_175718_1_, BlockPos pos, int p_175718_3_) {
      this.playAuxSFXAtEntity((EntityPlayer)null, p_175718_1_, pos, p_175718_3_);
   }

   public void playAuxSFXAtEntity(EntityPlayer player, int sfxType, BlockPos pos, int p_180498_4_) {
      try {
         for(int i = 0; i < this.worldAccesses.size(); ++i) {
            ((IWorldAccess)this.worldAccesses.get(i)).playAuxSFX(player, sfxType, pos, p_180498_4_);
         }

      } catch (Throwable var8) {
         CrashReport crashreport = CrashReport.makeCrashReport(var8, "Playing level event");
         CrashReportCategory crashreportcategory = crashreport.makeCategory("Level event being played");
         crashreportcategory.addCrashSection("Block coordinates", CrashReportCategory.getCoordinateInfo(pos));
         crashreportcategory.addCrashSection("Event source", player);
         crashreportcategory.addCrashSection("Event type", sfxType);
         crashreportcategory.addCrashSection("Event data", p_180498_4_);
         throw new ReportedException(crashreport);
      }
   }

   public int getHeight() {
      return 256;
   }

   public int getActualHeight() {
      return this.provider.getHasNoSky() ? 128 : 256;
   }

   public Random setRandomSeed(int p_72843_1_, int p_72843_2_, int p_72843_3_) {
      long i = (long)p_72843_1_ * 341873128712L + (long)p_72843_2_ * 132897987541L + this.getWorldInfo().getSeed() + (long)p_72843_3_;
      this.rand.setSeed(i);
      return this.rand;
   }

   public BlockPos getStrongholdPos(String name, BlockPos pos) {
      return this.getChunkProvider().getStrongholdGen(this, name, pos);
   }

   public boolean extendedLevelsInChunkCache() {
      return false;
   }

   public double getHorizon() {
      return this.worldInfo.getTerrainType() == WorldType.FLAT ? 0.0D : 63.0D;
   }

   public CrashReportCategory addWorldInfoToCrashReport(CrashReport report) {
      CrashReportCategory crashreportcategory = report.makeCategoryDepth("Affected level", 1);
      crashreportcategory.addCrashSection("Level name", this.worldInfo == null ? "????" : this.worldInfo.getWorldName());
      crashreportcategory.addCrashSectionCallable("All players", new Callable() {
         public String call() {
            return World.this.playerEntities.size() + " total; " + World.this.playerEntities.toString();
         }
      });
      crashreportcategory.addCrashSectionCallable("Chunk stats", new Callable() {
         public String call() {
            return World.this.chunkProvider.makeString();
         }
      });

      try {
         this.worldInfo.addToCrashReport(crashreportcategory);
      } catch (Throwable var4) {
         crashreportcategory.addCrashSectionThrowable("Level Data Unobtainable", var4);
      }

      return crashreportcategory;
   }

   public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {
      for(int i = 0; i < this.worldAccesses.size(); ++i) {
         IWorldAccess iworldaccess = (IWorldAccess)this.worldAccesses.get(i);
         iworldaccess.sendBlockBreakProgress(breakerId, pos, progress);
      }

   }

   public Calendar getCurrentDate() {
      if (this.getTotalWorldTime() % 600L == 0L) {
         this.theCalendar.setTimeInMillis(MinecraftServer.getCurrentTimeMillis());
      }

      return this.theCalendar;
   }

   public void makeFireworks(double x, double y, double z, double motionX, double motionY, double motionZ, NBTTagCompound compund) {
   }

   public Scoreboard getScoreboard() {
      return this.worldScoreboard;
   }

   public void updateComparatorOutputLevel(BlockPos pos, Block blockIn) {
      Iterator var3 = EnumFacing.Plane.HORIZONTAL.iterator();

      while(var3.hasNext()) {
         Object enumfacing = var3.next();
         BlockPos blockpos = pos.offset((EnumFacing)enumfacing);
         if (this.isBlockLoaded(blockpos)) {
            IBlockState iblockstate = this.getBlockState(blockpos);
            if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
               iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
            } else if (iblockstate.getBlock().isNormalCube()) {
               blockpos = blockpos.offset((EnumFacing)enumfacing);
               iblockstate = this.getBlockState(blockpos);
               if (Blocks.unpowered_comparator.isAssociated(iblockstate.getBlock())) {
                  iblockstate.getBlock().onNeighborBlockChange(this, blockpos, iblockstate, blockIn);
               }
            }
         }
      }

   }

   public DifficultyInstance getDifficultyForLocation(BlockPos pos) {
      long i = 0L;
      float f = 0.0F;
      if (this.isBlockLoaded(pos)) {
         f = this.getCurrentMoonPhaseFactor();
         i = this.getChunkFromBlockCoords(pos).getInhabitedTime();
      }

      return new DifficultyInstance(this.getDifficulty(), this.getWorldTime(), i, f);
   }

   public EnumDifficulty getDifficulty() {
      return this.getWorldInfo().getDifficulty();
   }

   public int getSkylightSubtracted() {
      return this.skylightSubtracted;
   }

   public void setSkylightSubtracted(int newSkylightSubtracted) {
      this.skylightSubtracted = newSkylightSubtracted;
   }

   public int getLastLightningBolt() {
      return this.lastLightningBolt;
   }

   public void setLastLightningBolt(int lastLightningBoltIn) {
      this.lastLightningBolt = lastLightningBoltIn;
   }

   public boolean isFindingSpawnPoint() {
      return this.findingSpawnPoint;
   }

   public VillageCollection getVillageCollection() {
      return this.villageCollectionObj;
   }

   public WorldBorder getWorldBorder() {
      return this.worldBorder;
   }

   public boolean isSpawnChunk(int x, int z) {
      BlockPos blockpos = this.getSpawnPoint();
      int i = x * 16 + 8 - blockpos.getX();
      int j = z * 16 + 8 - blockpos.getZ();
      int k = 128;
      return i >= -k && i <= k && j >= -k && j <= k;
   }
}
