package net.minecraft.world.storage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.GameRules;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class DerivedWorldInfo extends WorldInfo
{
    /** Instance of WorldInfo. */
    private final WorldInfo theWorldInfo;

    public DerivedWorldInfo(WorldInfo p_i2145_1_)
    {
        this.theWorldInfo = p_i2145_1_;
    }

    /**
     * Gets the NBTTagCompound for the worldInfo
     */
    @Override
	public NBTTagCompound getNBTTagCompound()
    {
        return this.theWorldInfo.getNBTTagCompound();
    }

    /**
     * Creates a new NBTTagCompound for the world, with the given NBTTag as the "Player"
     */
    @Override
	public NBTTagCompound cloneNBTCompound(NBTTagCompound nbt)
    {
        return this.theWorldInfo.cloneNBTCompound(nbt);
    }

    /**
     * Returns the seed of current world.
     */
    @Override
	public long getSeed()
    {
        return this.theWorldInfo.getSeed();
    }

    /**
     * Returns the x spawn position
     */
    @Override
	public int getSpawnX()
    {
        return this.theWorldInfo.getSpawnX();
    }

    /**
     * Return the Y axis spawning point of the player.
     */
    @Override
	public int getSpawnY()
    {
        return this.theWorldInfo.getSpawnY();
    }

    /**
     * Returns the z spawn position
     */
    @Override
	public int getSpawnZ()
    {
        return this.theWorldInfo.getSpawnZ();
    }

    @Override
	public long getWorldTotalTime()
    {
        return this.theWorldInfo.getWorldTotalTime();
    }

    /**
     * Get current world time
     */
    @Override
	public long getWorldTime()
    {
        return this.theWorldInfo.getWorldTime();
    }

    @Override
	public long getSizeOnDisk()
    {
        return this.theWorldInfo.getSizeOnDisk();
    }

    /**
     * Returns the player's NBTTagCompound to be loaded
     */
    @Override
	public NBTTagCompound getPlayerNBTTagCompound()
    {
        return this.theWorldInfo.getPlayerNBTTagCompound();
    }

    /**
     * Get current world name
     */
    @Override
	public String getWorldName()
    {
        return this.theWorldInfo.getWorldName();
    }

    /**
     * Returns the save version of this world
     */
    @Override
	public int getSaveVersion()
    {
        return this.theWorldInfo.getSaveVersion();
    }

    /**
     * Return the last time the player was in this world.
     */
    @Override
	public long getLastTimePlayed()
    {
        return this.theWorldInfo.getLastTimePlayed();
    }

    /**
     * Returns true if it is thundering, false otherwise.
     */
    @Override
	public boolean isThundering()
    {
        return this.theWorldInfo.isThundering();
    }

    /**
     * Returns the number of ticks until next thunderbolt.
     */
    @Override
	public int getThunderTime()
    {
        return this.theWorldInfo.getThunderTime();
    }

    /**
     * Returns true if it is raining, false otherwise.
     */
    @Override
	public boolean isRaining()
    {
        return this.theWorldInfo.isRaining();
    }

    /**
     * Return the number of ticks until rain.
     */
    @Override
	public int getRainTime()
    {
        return this.theWorldInfo.getRainTime();
    }

    /**
     * Gets the GameType.
     */
    @Override
	public WorldSettings.GameType getGameType()
    {
        return this.theWorldInfo.getGameType();
    }

    /**
     * Set the x spawn position to the passed in value
     */
    @Override
	public void setSpawnX(int x) {}

    /**
     * Sets the y spawn position
     */
    @Override
	public void setSpawnY(int y) {}

    /**
     * Set the z spawn position to the passed in value
     */
    @Override
	public void setSpawnZ(int z) {}

    @Override
	public void incrementTotalWorldTime(long time) {}

    /**
     * Set current world time
     */
    @Override
	public void setWorldTime(long time) {}

    @Override
	public void setSpawn(BlockPos spawnPoint) {}

    @Override
	public void setWorldName(String worldName) {}

    /**
     * Sets the save version of the world
     */
    @Override
	public void setSaveVersion(int version) {}

    /**
     * Sets whether it is thundering or not.
     */
    @Override
	public void setThundering(boolean thunderingIn) {}

    /**
     * Defines the number of ticks until next thunderbolt.
     */
    @Override
	public void setThunderTime(int time) {}

    /**
     * Sets whether it is raining or not.
     */
    @Override
	public void setRaining(boolean isRaining) {}

    /**
     * Sets the number of ticks until rain.
     */
    @Override
	public void setRainTime(int time) {}

    /**
     * Get whether the map features (e.g. strongholds) generation is enabled or disabled.
     */
    @Override
	public boolean isMapFeaturesEnabled()
    {
        return this.theWorldInfo.isMapFeaturesEnabled();
    }

    /**
     * Returns true if hardcore mode is enabled, otherwise false
     */
    @Override
	public boolean isHardcoreModeEnabled()
    {
        return this.theWorldInfo.isHardcoreModeEnabled();
    }

    @Override
	public WorldType getTerrainType()
    {
        return this.theWorldInfo.getTerrainType();
    }

    @Override
	public void setTerrainType(WorldType type) {}

    /**
     * Returns true if commands are allowed on this World.
     */
    @Override
	public boolean areCommandsAllowed()
    {
        return this.theWorldInfo.areCommandsAllowed();
    }

    @Override
	public void setAllowCommands(boolean allow) {}

    /**
     * Returns true if the World is initialized.
     */
    @Override
	public boolean isInitialized()
    {
        return this.theWorldInfo.isInitialized();
    }

    /**
     * Sets the initialization status of the World.
     */
    @Override
	public void setServerInitialized(boolean initializedIn) {}

    /**
     * Gets the GameRules class Instance.
     */
    @Override
	public GameRules getGameRulesInstance()
    {
        return this.theWorldInfo.getGameRulesInstance();
    }

    @Override
	public EnumDifficulty getDifficulty()
    {
        return this.theWorldInfo.getDifficulty();
    }

    @Override
	public void setDifficulty(EnumDifficulty newDifficulty) {}

    @Override
	public boolean isDifficultyLocked()
    {
        return this.theWorldInfo.isDifficultyLocked();
    }

    @Override
	public void setDifficultyLocked(boolean locked) {}
}
