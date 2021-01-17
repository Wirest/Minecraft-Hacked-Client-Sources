// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import net.minecraft.realms.RealmsScreen;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;

public class RealmsOptions
{
    public Boolean pvp;
    public Boolean spawnAnimals;
    public Boolean spawnMonsters;
    public Boolean spawnNPCs;
    public Integer spawnProtection;
    public Boolean commandBlocks;
    public Boolean forceGameMode;
    public Integer difficulty;
    public Integer gameMode;
    public String slotName;
    public long templateId;
    public String templateImage;
    public boolean empty;
    private static boolean forceGameModeDefault;
    private static boolean pvpDefault;
    private static boolean spawnAnimalsDefault;
    private static boolean spawnMonstersDefault;
    private static boolean spawnNPCsDefault;
    private static int spawnProtectionDefault;
    private static boolean commandBlocksDefault;
    private static int difficultyDefault;
    private static int gameModeDefault;
    private static String slotNameDefault;
    private static long templateIdDefault;
    private static String templateImageDefault;
    
    public RealmsOptions(final Boolean pvp, final Boolean spawnAnimals, final Boolean spawnMonsters, final Boolean spawnNPCs, final Integer spawnProtection, final Boolean commandBlocks, final Integer difficulty, final Integer gameMode, final Boolean forceGameMode, final String slotName) {
        this.empty = false;
        this.pvp = pvp;
        this.spawnAnimals = spawnAnimals;
        this.spawnMonsters = spawnMonsters;
        this.spawnNPCs = spawnNPCs;
        this.spawnProtection = spawnProtection;
        this.commandBlocks = commandBlocks;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.forceGameMode = forceGameMode;
        this.slotName = slotName;
    }
    
    public static RealmsOptions getDefaults() {
        return new RealmsOptions(RealmsOptions.pvpDefault, RealmsOptions.spawnAnimalsDefault, RealmsOptions.spawnMonstersDefault, RealmsOptions.spawnNPCsDefault, RealmsOptions.spawnProtectionDefault, RealmsOptions.commandBlocksDefault, RealmsOptions.difficultyDefault, RealmsOptions.gameModeDefault, RealmsOptions.forceGameModeDefault, RealmsOptions.slotNameDefault);
    }
    
    public static RealmsOptions getEmptyDefaults() {
        final RealmsOptions options = new RealmsOptions(RealmsOptions.pvpDefault, RealmsOptions.spawnAnimalsDefault, RealmsOptions.spawnMonstersDefault, RealmsOptions.spawnNPCsDefault, RealmsOptions.spawnProtectionDefault, RealmsOptions.commandBlocksDefault, RealmsOptions.difficultyDefault, RealmsOptions.gameModeDefault, RealmsOptions.forceGameModeDefault, RealmsOptions.slotNameDefault);
        options.setEmpty(true);
        return options;
    }
    
    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }
    
    public static RealmsOptions parse(final JsonObject jsonObject) {
        final RealmsOptions newOptions = new RealmsOptions(JsonUtils.getBooleanOr("pvp", jsonObject, RealmsOptions.pvpDefault), JsonUtils.getBooleanOr("spawnAnimals", jsonObject, RealmsOptions.spawnAnimalsDefault), JsonUtils.getBooleanOr("spawnMonsters", jsonObject, RealmsOptions.spawnMonstersDefault), JsonUtils.getBooleanOr("spawnNPCs", jsonObject, RealmsOptions.spawnNPCsDefault), JsonUtils.getIntOr("spawnProtection", jsonObject, RealmsOptions.spawnProtectionDefault), JsonUtils.getBooleanOr("commandBlocks", jsonObject, RealmsOptions.commandBlocksDefault), JsonUtils.getIntOr("difficulty", jsonObject, RealmsOptions.difficultyDefault), JsonUtils.getIntOr("gameMode", jsonObject, RealmsOptions.gameModeDefault), JsonUtils.getBooleanOr("forceGameMode", jsonObject, RealmsOptions.forceGameModeDefault), JsonUtils.getStringOr("slotName", jsonObject, RealmsOptions.slotNameDefault));
        newOptions.templateId = JsonUtils.getLongOr("worldTemplateId", jsonObject, RealmsOptions.templateIdDefault);
        newOptions.templateImage = JsonUtils.getStringOr("worldTemplateImage", jsonObject, RealmsOptions.templateImageDefault);
        return newOptions;
    }
    
    public String getSlotName(final int i) {
        if (this.slotName != null && !this.slotName.equals("")) {
            return this.slotName;
        }
        if (this.empty) {
            return RealmsScreen.getLocalizedString("mco.configure.world.slot.empty");
        }
        return RealmsScreen.getLocalizedString("mco.configure.world.slot", i);
    }
    
    public String getDefaultSlotName(final int i) {
        return RealmsScreen.getLocalizedString("mco.configure.world.slot", i);
    }
    
    public String toJson() {
        final JsonObject jsonObject = new JsonObject();
        if (this.pvp != RealmsOptions.pvpDefault) {
            jsonObject.addProperty("pvp", this.pvp);
        }
        if (this.spawnAnimals != RealmsOptions.spawnAnimalsDefault) {
            jsonObject.addProperty("spawnAnimals", this.spawnAnimals);
        }
        if (this.spawnMonsters != RealmsOptions.spawnMonstersDefault) {
            jsonObject.addProperty("spawnMonsters", this.spawnMonsters);
        }
        if (this.spawnNPCs != RealmsOptions.spawnNPCsDefault) {
            jsonObject.addProperty("spawnNPCs", this.spawnNPCs);
        }
        if (this.spawnProtection != RealmsOptions.spawnProtectionDefault) {
            jsonObject.addProperty("spawnProtection", this.spawnProtection);
        }
        if (this.commandBlocks != RealmsOptions.commandBlocksDefault) {
            jsonObject.addProperty("commandBlocks", this.commandBlocks);
        }
        if (this.difficulty != RealmsOptions.difficultyDefault) {
            jsonObject.addProperty("difficulty", this.difficulty);
        }
        if (this.gameMode != RealmsOptions.gameModeDefault) {
            jsonObject.addProperty("gameMode", this.gameMode);
        }
        if (this.forceGameMode != RealmsOptions.forceGameModeDefault) {
            jsonObject.addProperty("forceGameMode", this.forceGameMode);
        }
        if (!this.slotName.equals(RealmsOptions.slotNameDefault) && !this.slotName.equals("")) {
            jsonObject.addProperty("slotName", this.slotName);
        }
        return jsonObject.toString();
    }
    
    public RealmsOptions clone() {
        return new RealmsOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficulty, this.gameMode, this.forceGameMode, this.slotName);
    }
    
    static {
        RealmsOptions.forceGameModeDefault = false;
        RealmsOptions.pvpDefault = true;
        RealmsOptions.spawnAnimalsDefault = true;
        RealmsOptions.spawnMonstersDefault = true;
        RealmsOptions.spawnNPCsDefault = true;
        RealmsOptions.spawnProtectionDefault = 0;
        RealmsOptions.commandBlocksDefault = false;
        RealmsOptions.difficultyDefault = 2;
        RealmsOptions.gameModeDefault = 0;
        RealmsOptions.slotNameDefault = null;
        RealmsOptions.templateIdDefault = -1L;
        RealmsOptions.templateImageDefault = null;
    }
}
