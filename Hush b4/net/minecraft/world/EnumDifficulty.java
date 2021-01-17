// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public enum EnumDifficulty
{
    PEACEFUL("PEACEFUL", 0, 0, "options.difficulty.peaceful"), 
    EASY("EASY", 1, 1, "options.difficulty.easy"), 
    NORMAL("NORMAL", 2, 2, "options.difficulty.normal"), 
    HARD("HARD", 3, 3, "options.difficulty.hard");
    
    private static final EnumDifficulty[] difficultyEnums;
    private final int difficultyId;
    private final String difficultyResourceKey;
    
    static {
        difficultyEnums = new EnumDifficulty[values().length];
        EnumDifficulty[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final EnumDifficulty enumdifficulty = values[i];
            EnumDifficulty.difficultyEnums[enumdifficulty.difficultyId] = enumdifficulty;
        }
    }
    
    private EnumDifficulty(final String name, final int ordinal, final int difficultyIdIn, final String difficultyResourceKeyIn) {
        this.difficultyId = difficultyIdIn;
        this.difficultyResourceKey = difficultyResourceKeyIn;
    }
    
    public int getDifficultyId() {
        return this.difficultyId;
    }
    
    public static EnumDifficulty getDifficultyEnum(final int p_151523_0_) {
        return EnumDifficulty.difficultyEnums[p_151523_0_ % EnumDifficulty.difficultyEnums.length];
    }
    
    public String getDifficultyResourceKey() {
        return this.difficultyResourceKey;
    }
}
