// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.world.storage.WorldInfo;

public final class WorldSettings
{
    private final long seed;
    private final GameType theGameType;
    private final boolean mapFeaturesEnabled;
    private final boolean hardcoreEnabled;
    private final WorldType terrainType;
    private boolean commandsAllowed;
    private boolean bonusChestEnabled;
    private String worldName;
    
    public WorldSettings(final long seedIn, final GameType gameType, final boolean enableMapFeatures, final boolean hardcoreMode, final WorldType worldTypeIn) {
        this.worldName = "";
        this.seed = seedIn;
        this.theGameType = gameType;
        this.mapFeaturesEnabled = enableMapFeatures;
        this.hardcoreEnabled = hardcoreMode;
        this.terrainType = worldTypeIn;
    }
    
    public WorldSettings(final WorldInfo info) {
        this(info.getSeed(), info.getGameType(), info.isMapFeaturesEnabled(), info.isHardcoreModeEnabled(), info.getTerrainType());
    }
    
    public WorldSettings enableBonusChest() {
        this.bonusChestEnabled = true;
        return this;
    }
    
    public WorldSettings enableCommands() {
        this.commandsAllowed = true;
        return this;
    }
    
    public WorldSettings setWorldName(final String name) {
        this.worldName = name;
        return this;
    }
    
    public boolean isBonusChestEnabled() {
        return this.bonusChestEnabled;
    }
    
    public long getSeed() {
        return this.seed;
    }
    
    public GameType getGameType() {
        return this.theGameType;
    }
    
    public boolean getHardcoreEnabled() {
        return this.hardcoreEnabled;
    }
    
    public boolean isMapFeaturesEnabled() {
        return this.mapFeaturesEnabled;
    }
    
    public WorldType getTerrainType() {
        return this.terrainType;
    }
    
    public boolean areCommandsAllowed() {
        return this.commandsAllowed;
    }
    
    public static GameType getGameTypeById(final int id) {
        return GameType.getByID(id);
    }
    
    public String getWorldName() {
        return this.worldName;
    }
    
    public enum GameType
    {
        NOT_SET("NOT_SET", 0, -1, ""), 
        SURVIVAL("SURVIVAL", 1, 0, "survival"), 
        CREATIVE("CREATIVE", 2, 1, "creative"), 
        ADVENTURE("ADVENTURE", 3, 2, "adventure"), 
        SPECTATOR("SPECTATOR", 4, 3, "spectator");
        
        int id;
        String name;
        
        private GameType(final String name, final int ordinal, final int typeId, final String nameIn) {
            this.id = typeId;
            this.name = nameIn;
        }
        
        public int getID() {
            return this.id;
        }
        
        public String getName() {
            return this.name;
        }
        
        public void configurePlayerCapabilities(final PlayerCapabilities capabilities) {
            if (this == GameType.CREATIVE) {
                capabilities.allowFlying = true;
                capabilities.isCreativeMode = true;
                capabilities.disableDamage = true;
            }
            else if (this == GameType.SPECTATOR) {
                capabilities.allowFlying = true;
                capabilities.isCreativeMode = false;
                capabilities.disableDamage = true;
                capabilities.isFlying = true;
            }
            else {
                capabilities.allowFlying = false;
                capabilities.isCreativeMode = false;
                capabilities.disableDamage = false;
                capabilities.isFlying = false;
            }
            capabilities.allowEdit = !this.isAdventure();
        }
        
        public boolean isAdventure() {
            return this == GameType.ADVENTURE || this == GameType.SPECTATOR;
        }
        
        public boolean isCreative() {
            return this == GameType.CREATIVE;
        }
        
        public boolean isSurvivalOrAdventure() {
            return this == GameType.SURVIVAL || this == GameType.ADVENTURE;
        }
        
        public static GameType getByID(final int idIn) {
            GameType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final GameType worldsettings$gametype = values[i];
                if (worldsettings$gametype.id == idIn) {
                    return worldsettings$gametype;
                }
            }
            return GameType.SURVIVAL;
        }
        
        public static GameType getByName(final String p_77142_0_) {
            GameType[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final GameType worldsettings$gametype = values[i];
                if (worldsettings$gametype.name.equals(p_77142_0_)) {
                    return worldsettings$gametype;
                }
            }
            return GameType.SURVIVAL;
        }
    }
}
