package net.minecraft.realms;

import net.minecraft.world.storage.SaveFormatComparator;

public class RealmsLevelSummary implements Comparable {
    private SaveFormatComparator levelSummary;
    private static final String __OBFID = "CL_00001857";

    public RealmsLevelSummary(SaveFormatComparator p_i1109_1_) {
        levelSummary = p_i1109_1_;
    }

    public int getGameMode() {
        return levelSummary.getEnumGameType().getID();
    }

    public String getLevelId() {
        return levelSummary.getFileName();
    }

    public boolean hasCheats() {
        return levelSummary.getCheatsEnabled();
    }

    public boolean isHardcore() {
        return levelSummary.isHardcoreModeEnabled();
    }

    public boolean isRequiresConversion() {
        return levelSummary.requiresConversion();
    }

    public String getLevelName() {
        return levelSummary.getDisplayName();
    }

    public long getLastPlayed() {
        return levelSummary.getLastTimePlayed();
    }

    public int compareTo(SaveFormatComparator p_compareTo_1_) {
        return levelSummary.compareTo(p_compareTo_1_);
    }

    public long getSizeOnDisk() {
        return levelSummary.func_154336_c();
    }

    public int compareTo(RealmsLevelSummary p_compareTo_1_) {
        return levelSummary.getLastTimePlayed() < p_compareTo_1_.getLastPlayed() ? 1 : (levelSummary.getLastTimePlayed() > p_compareTo_1_.getLastPlayed() ? -1 : levelSummary.getFileName().compareTo(p_compareTo_1_.getLevelId()));
    }

    @Override
    public int compareTo(Object p_compareTo_1_) {
        return this.compareTo((RealmsLevelSummary) p_compareTo_1_);
    }
}
