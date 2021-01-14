package com.mentalfrostbyte.jello.fake;

import net.minecraft.client.multiplayer.*;
import net.minecraft.profiler.*;
import net.minecraft.client.network.*;
import net.minecraft.world.biome.*;
import net.minecraft.block.state.*;
import net.minecraft.init.*;
import net.minecraft.tileentity.*;
import net.minecraft.block.*;
import net.minecraft.client.audio.*;
import net.minecraft.util.*;
import net.minecraft.block.material.*;
import java.util.*;
import javax.annotation.*;
import net.minecraft.nbt.*;
import net.minecraft.entity.*;
import net.minecraft.world.chunk.*;
import net.minecraft.world.*;
import net.minecraft.entity.player.*;
import net.minecraft.client.*;
import net.minecraft.world.chunk.storage.*;
import net.minecraft.world.storage.*;
import java.io.*;

public class FakeWorld extends WorldClient
{
    public FakeWorld(final WorldSettings worldSettings, final FakeNetHandlerPlayClient netHandler) {
        super((NetHandlerPlayClient)netHandler, worldSettings, 0, EnumDifficulty.HARD, new Profiler());
        this.provider.registerWorld((World)this);
    }
    
    
    protected boolean isChunkLoaded(final int i, final int i1, final boolean b) {
        return false;
    }
    
    public BlockPos getTopSolidOrLiquidBlock(final BlockPos pos) {
        return new BlockPos(pos.getX(), 63, pos.getZ());
    }
    
    public boolean isAirBlock(final BlockPos pos) {
        return pos.getY() > 63;
    }
    
    
    public boolean setBlockState(final BlockPos pos, final IBlockState state) {
        return true;
    }
    
    public boolean setBlockToAir(final BlockPos pos) {
        return true;
    }
    
    public void markChunkDirty(final BlockPos pos, final TileEntity unusedTileEntity) {
    }
    
    public void notifyBlockUpdate(final BlockPos pos, final IBlockState oldState, final IBlockState newState, final int flags) {
    }
    
    public boolean destroyBlock(final BlockPos pos, final boolean dropBlock) {
        return this.isAirBlock(pos);
    }
    
    public void notifyNeighborsOfStateExcept(final BlockPos pos, final Block blockType, final EnumFacing skipSide) {
    }
    
    public void notifyNeighborsRespectDebug(final BlockPos pos, final Block blockType, final boolean p_175722_3_) {
    }
    
    public void markAndNotifyBlock(final BlockPos pos, final Chunk chunk, final IBlockState iblockstate, final IBlockState newState, final int flags) {
    }
    
    public void markBlocksDirtyVertical(final int par1, final int par2, final int par3, final int par4) {
    }
    
    public void markBlockRangeForRenderUpdate(final int p_147458_1_, final int p_147458_2_, final int p_147458_3_, final int p_147458_4_, final int p_147458_5_, final int p_147458_6_) {
    }
    
    public boolean isBlockTickPending(final BlockPos pos, final Block blockType) {
        return false;
    }
    
    public int getLightFromNeighbors(final BlockPos pos) {
        return 14;
    }
    
    public int getLight(final BlockPos pos, final boolean checkNeighbors) {
        return 14;
    }
    
    public int getLight(final BlockPos pos) {
        return 14;
    }
    
    public int getLightFor(final EnumSkyBlock type, final BlockPos pos) {
        return 14;
    }
    
    public int getLightFromNeighborsFor(final EnumSkyBlock type, final BlockPos pos) {
        return 14;
    }
    
    public boolean canBlockSeeSky(final BlockPos pos) {
        return pos.getY() > 62;
    }
    
    public BlockPos getHeight(final BlockPos pos) {
        return new BlockPos(pos.getX(), 63, pos.getZ());
    }
    
    public int getChunksLowestHorizon(final int x, final int z) {
        return 63;
    }
    
    protected void updateBlocks() {
    }
    
    public void markBlockRangeForRenderUpdate(final BlockPos rangeMin, final BlockPos rangeMax) {
    }
    
    public void setLightFor(final EnumSkyBlock type, final BlockPos pos, final int lightValue) {
    }
    
    public float getLightBrightness(final BlockPos pos) {
        return 1.0f;
    }
    
    public float getSunBrightnessFactor(final float p_72967_1_) {
        return 1.0f;
    }
    
    
    public float getSunBrightness(final float p_72971_1_) {
        return 1.0f;
    }
    
    
    public float getSunBrightnessBody(final float p_72971_1_) {
        return 1.0f;
    }
    
    public boolean isDaytime() {
        return true;
    }
    
    
    
    
    public boolean addWeatherEffect(final Entity par1Entity) {
        return false;
    }
    
    public boolean spawnEntityInWorld(final Entity par1Entity) {
        return false;
    }
    
    public void onEntityAdded(final Entity par1Entity) {
    }
    
    public void onEntityRemoved(final Entity par1Entity) {
    }
    
    public void removeEntity(final Entity par1Entity) {
    }
    
    public void removeEntityDangerously(final Entity entityIn) {
    }
    
    public int calculateSkylightSubtracted(final float par1) {
        return 6;
    }
    
    public void scheduleBlockUpdate(final BlockPos pos, final Block blockIn, final int delay, final int priority) {
    }
    
    public void updateEntities() {
    }
    
    public void updateEntityWithOptionalForce(final Entity entityIn, final boolean forceUpdate) {
        if (forceUpdate) {
            ++entityIn.ticksExisted;
        }
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB bb) {
        return true;
    }
    
    public boolean checkNoEntityCollision(final AxisAlignedBB bb, final Entity entityIn) {
        return true;
    }
    
    public boolean checkBlockCollision(final AxisAlignedBB bb) {
        return false;
    }
    
    public boolean containsAnyLiquid(final AxisAlignedBB bb) {
        return false;
    }
    
    public boolean handleMaterialAcceleration(final AxisAlignedBB par1AxisAlignedBB, final Material par2Material, final Entity par3Entity) {
        return false;
    }
    
    public boolean isMaterialInBB(final AxisAlignedBB par1AxisAlignedBB, final Material par2Material) {
        return false;
    }
    
    public TileEntity getTileEntity(final BlockPos pos) {
        return null;
    }
    
    public boolean extinguishFire(final EntityPlayer player, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    
    public String getDebugLoadedEntities() {
        return "";
    }
    
    
    public String getProviderName() {
        return "";
    }
    
    public void setTileEntity(final BlockPos pos, final TileEntity tileEntityIn) {
    }
    
    public void removeTileEntity(final BlockPos pos) {
    }
    
    public void markTileEntityForRemoval(final TileEntity p_147457_1_) {
    }
    
    public boolean isBlockNormalCube(final BlockPos pos, final boolean _default) {
        return true;
    }
    
    public void tick() {
    }
    
    protected void updateWeather() {
    }
    
    public void updateWeatherBody() {
    }
    
    public boolean canBlockFreezeWater(final BlockPos pos) {
        return false;
    }
    
    public boolean canBlockFreezeNoWater(final BlockPos pos) {
        return false;
    }
    
    public boolean canBlockFreeze(final BlockPos pos, final boolean noWaterAdj) {
        return false;
    }
    
    public boolean canBlockFreezeBody(final BlockPos pos, final boolean noWaterAdj) {
        return false;
    }
    
    public boolean canSnowAt(final BlockPos pos, final boolean checkLight) {
        return false;
    }
    
    public boolean canSnowAtBody(final BlockPos pos, final boolean checkLight) {
        return false;
    }
    
    public boolean tickUpdates(final boolean par1) {
        return false;
    }
    
    public List getPendingBlockUpdates(final Chunk par1Chunk, final boolean par2) {
        return null;
    }
    
    public Entity findNearestEntityWithinAABB(final Class par1Class, final AxisAlignedBB par2AxisAlignedBB, final Entity par3Entity) {
        return null;
    }
    
    
    public int countEntities(final Class par1Class) {
        return 0;
    }
    
    public int getStrongPower(final BlockPos pos) {
        return 0;
    }
    
    public int getStrongPower(final BlockPos pos, final EnumFacing direction) {
        return 0;
    }
    
    public boolean isSidePowered(final BlockPos pos, final EnumFacing side) {
        return false;
    }
    
    public int getRedstonePower(final BlockPos pos, final EnumFacing facing) {
        return 0;
    }
    
    public boolean isBlockPowered(final BlockPos pos) {
        return false;
    }
    
    public int isBlockIndirectlyGettingPowered(final BlockPos pos) {
        return 0;
    }
    
    public void checkSessionLock() throws MinecraftException {
    }
    
    public long getSeed() {
        return 1L;
    }
    
    public long getTotalWorldTime() {
        return 1L;
    }
    
    public long getWorldTime() {
        return 1L;
    }
    
    public void setWorldTime(final long par1) {
    }
    
    public BlockPos getSpawnPoint() {
        return new BlockPos(0, 64, 0);
    }
    
    
    public void joinEntityInSurroundings(final Entity par1Entity) {
    }
    
    public boolean canSeeSky(final BlockPos pos) {
        return pos.getY() > 62;
    }
    
    public boolean canMineBlockBody(final EntityPlayer player, final BlockPos pos) {
        return false;
    }
    
    public void setEntityState(final Entity par1Entity, final byte par2) {
    }
    
    public float getThunderStrength(final float delta) {
        return 0.0f;
    }
    
    public void addBlockEvent(final BlockPos pos, final Block blockIn, final int eventID, final int eventParam) {
    }
    
    public void updateAllPlayersSleepingFlag() {
    }
    
    public boolean isRainingAt(final BlockPos strikePosition) {
        return false;
    }
    
    
    public void setThunderStrength(final float p_147442_1_) {
    }
    
    public float getRainStrength(final float par1) {
        return 0.0f;
    }
    
    
    public void setRainStrength(final float par1) {
    }
    
    public boolean isThundering() {
        return false;
    }
    
    public boolean isRaining() {
        return false;
    }
    
    public boolean isBlockinHighHumidity(final BlockPos pos) {
        return false;
    }
    
    public void setItemData(final String dataID, final WorldSavedData worldSavedDataIn) {
    }
    
    public void playBroadcastSound(final int p_175669_1_, final BlockPos pos, final int p_175669_3_) {
    }
    
    public void playEvent( final EntityPlayer player, final int type, final BlockPos pos, final int data) {
    }
    
    public void playEvent(final int type, final BlockPos pos, final int data) {
    }
    
    public int getHeight() {
        return 256;
    }
    
    public int getActualHeight() {
        return 256;
    }
    
    
    public void makeFireworks(final double par1, final double par3, final double par5, final double par7, final double par9, final double par11, final NBTTagCompound par13nbtTagCompound) {
    }
    
    public boolean addTileEntity(final TileEntity tile) {
        return true;
    }
    
    
    public boolean isSideSolid(final BlockPos pos, final EnumFacing side) {
        return pos.getY() <= 63;
    }
    
    public boolean isSideSolid(final BlockPos pos, final EnumFacing side, final boolean _default) {
        return pos.getY() <= 63;
    }
    
    public int countEntities(final EnumCreatureType type, final boolean forSpawnCount) {
        return 0;
    }
    
    protected IChunkProvider createChunkProvider() {
        return (IChunkProvider)new FakeChunkProvider();
    }
    
    public Chunk getChunkFromChunkCoords(final int par1, final int par2) {
        return null;
    }
    
    protected static class FakeWorldProvider extends WorldProvider
    {
        
        public boolean isSurfaceWorld() {
            return true;
        }
        
        public boolean canRespawnHere() {
            return true;
        }
        
        public int getAverageGroundLevel() {
            return 63;
        }
        
        
        public boolean doesXZShowFog(final int par1, final int par2) {
            return false;
        }
        
        public void setDimension(final int dim) {
        }
        
        public String getSaveFolder() {
            return null;
        }
        
        public BlockPos getRandomizedSpawnPoint() {
            return new BlockPos(0, 64, 0);
        }
        
        public boolean shouldMapSpin(final String entity, final double x, final double y, final double z) {
            return false;
        }
        
        public int getRespawnDimension(final EntityPlayerMP player) {
            return 0;
        }
        
        public BiomeGenBase getBiomeForCoords(final BlockPos pos) {
            return new BiomeGenPlains(0);
        }
        
        public boolean isDaytime() {
            return true;
        }
        
        public void setAllowedSpawnTypes(final boolean allowHostile, final boolean allowPeaceful) {
        }
        
        public void calculateInitialWeather() {
        }
        
        public void updateWeather() {
        }
        
        public boolean canBlockFreeze(final BlockPos pos, final boolean byWater) {
            return false;
        }
        
        public boolean canSnowAt(final BlockPos pos, final boolean checkLight) {
            return false;
        }
        
        public long getSeed() {
            return 1L;
        }
        
        public long getWorldTime() {
            return 1L;
        }
        
        public void setWorldTime(final long time) {
        }
        
        public boolean canMineBlock(final EntityPlayer player, final BlockPos pos) {
            return false;
        }
        
        public boolean isBlockHighHumidity(final BlockPos pos) {
            return false;
        }
        
        public int getHeight() {
            return 256;
        }
        
        public int getActualHeight() {
            return 256;
        }
        
        public void resetRainAndThunder() {
        }
        
        public boolean canDoLightning(final Chunk chunk) {
            return false;
        }
        
        public boolean canDoRainSnowIce(final Chunk chunk) {
            return false;
        }
        
        public BlockPos getSpawnPoint() {
            return new BlockPos(0, 64, 0);
        }
        
        public boolean canCoordinateBeSpawn(final int par1, final int par2) {
            return true;
        }

		@Override
		public String getDimensionName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getInternalNameSuffix() {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    protected static class FakeSaveHandler implements ISaveHandler
    {
        public WorldInfo loadWorldInfo() {
            return null;
        }
        
        public void checkSessionLock() {
        }
        
        public IChunkLoader getChunkLoader(final WorldProvider var1) {
            return null;
        }
        
        public void saveWorldInfoWithPlayer(final WorldInfo var1, final NBTTagCompound var2) {
        }
        
        public void saveWorldInfo(final WorldInfo var1) {
        }
        
        public IPlayerFileData getPlayerNBTManager() {
            return null;
        }
        
        public void flush() {
        }
        
        public File getWorldDirectory() {
            return null;
        }
        
        public File getMapFileFromName(final String var1) {
            return null;
        }

		@Override
		public String getWorldDirectoryName() {
			// TODO Auto-generated method stub
			return null;
		}
    }
    
    protected static class FakeChunkProvider implements IChunkProvider
    {
        public Chunk getLoadedChunk(final int x, final int z) {
            return null;
        }
        
        public Chunk provideChunk(final int var1, final int var2) {
            return null;
        }
        
        public String makeString() {
            return null;
        }
        
        public boolean unloadQueuedChunks() {
            return false;
        }
        
        public boolean func_191062_e(final int p_191062_1_, final int p_191062_2_) {
            return true;
        }

		@Override
		public boolean chunkExists(int var1, int var2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Chunk func_177459_a(BlockPos var1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void populate(IChunkProvider var1, int var2, int var3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean func_177460_a(IChunkProvider var1, Chunk var2, int var3, int var4) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean saveChunks(boolean var1, IProgressUpdate var2) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean canSave() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public List func_177458_a(EnumCreatureType var1, BlockPos var2) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public BlockPos func_180513_a(World var1, String var2, BlockPos var3) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLoadedChunkCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void func_180514_a(Chunk var1, int var2, int var3) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void saveExtraData() {
			// TODO Auto-generated method stub
			
		}
    }
}
