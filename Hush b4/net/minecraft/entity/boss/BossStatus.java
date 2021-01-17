// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss;

public final class BossStatus
{
    public static float healthScale;
    public static int statusBarTime;
    public static String bossName;
    public static boolean hasColorModifier;
    
    public static void setBossStatus(final IBossDisplayData displayData, final boolean hasColorModifierIn) {
        BossStatus.healthScale = displayData.getHealth() / displayData.getMaxHealth();
        BossStatus.statusBarTime = 100;
        BossStatus.bossName = displayData.getDisplayName().getFormattedText();
        BossStatus.hasColorModifier = hasColorModifierIn;
    }
}
