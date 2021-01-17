// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.potion;

import java.util.ArrayList;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.IntegerCache;
import java.util.Iterator;
import optifine.CustomColors;
import optifine.Config;
import java.util.Collection;
import com.google.common.collect.Maps;
import java.util.Map;

public class PotionHelper
{
    public static final String field_77924_a;
    public static final String sugarEffect = "-0+1-2-3&4-4+13";
    public static final String ghastTearEffect = "+0-1-2-3&4-4+13";
    public static final String spiderEyeEffect = "-0-1+2-3&4-4+13";
    public static final String fermentedSpiderEyeEffect = "-0+3-4+13";
    public static final String speckledMelonEffect = "+0-1+2-3&4-4+13";
    public static final String blazePowderEffect = "+0-1-2+3&4-4+13";
    public static final String magmaCreamEffect = "+0+1-2-3&4-4+13";
    public static final String redstoneEffect = "-5+6-7";
    public static final String glowstoneEffect = "+5-6-7";
    public static final String gunpowderEffect = "+14&13-13";
    public static final String goldenCarrotEffect = "-0+1+2-3+13&4-4";
    public static final String pufferfishEffect = "+0-1+2+3+13&4-4";
    public static final String rabbitFootEffect = "+0+1-2+3&4-4+13";
    private static final Map potionRequirements;
    private static final Map potionAmplifiers;
    private static final Map DATAVALUE_COLORS;
    private static final String[] potionPrefixes;
    private static final String __OBFID = "CL_00000078";
    
    static {
        field_77924_a = null;
        potionRequirements = Maps.newHashMap();
        potionAmplifiers = Maps.newHashMap();
        DATAVALUE_COLORS = Maps.newHashMap();
        potionPrefixes = new String[] { "potion.prefix.mundane", "potion.prefix.uninteresting", "potion.prefix.bland", "potion.prefix.clear", "potion.prefix.milky", "potion.prefix.diffuse", "potion.prefix.artless", "potion.prefix.thin", "potion.prefix.awkward", "potion.prefix.flat", "potion.prefix.bulky", "potion.prefix.bungling", "potion.prefix.buttered", "potion.prefix.smooth", "potion.prefix.suave", "potion.prefix.debonair", "potion.prefix.thick", "potion.prefix.elegant", "potion.prefix.fancy", "potion.prefix.charming", "potion.prefix.dashing", "potion.prefix.refined", "potion.prefix.cordial", "potion.prefix.sparkling", "potion.prefix.potent", "potion.prefix.foul", "potion.prefix.odorless", "potion.prefix.rank", "potion.prefix.harsh", "potion.prefix.acrid", "potion.prefix.gross", "potion.prefix.stinky" };
        PotionHelper.potionRequirements.put(Potion.regeneration.getId(), "0 & !1 & !2 & !3 & 0+6");
        PotionHelper.potionRequirements.put(Potion.moveSpeed.getId(), "!0 & 1 & !2 & !3 & 1+6");
        PotionHelper.potionRequirements.put(Potion.fireResistance.getId(), "0 & 1 & !2 & !3 & 0+6");
        PotionHelper.potionRequirements.put(Potion.heal.getId(), "0 & !1 & 2 & !3");
        PotionHelper.potionRequirements.put(Potion.poison.getId(), "!0 & !1 & 2 & !3 & 2+6");
        PotionHelper.potionRequirements.put(Potion.weakness.getId(), "!0 & !1 & !2 & 3 & 3+6");
        PotionHelper.potionRequirements.put(Potion.harm.getId(), "!0 & !1 & 2 & 3");
        PotionHelper.potionRequirements.put(Potion.moveSlowdown.getId(), "!0 & 1 & !2 & 3 & 3+6");
        PotionHelper.potionRequirements.put(Potion.damageBoost.getId(), "0 & !1 & !2 & 3 & 3+6");
        PotionHelper.potionRequirements.put(Potion.nightVision.getId(), "!0 & 1 & 2 & !3 & 2+6");
        PotionHelper.potionRequirements.put(Potion.invisibility.getId(), "!0 & 1 & 2 & 3 & 2+6");
        PotionHelper.potionRequirements.put(Potion.waterBreathing.getId(), "0 & !1 & 2 & 3 & 2+6");
        PotionHelper.potionRequirements.put(Potion.jump.getId(), "0 & 1 & !2 & 3 & 3+6");
        PotionHelper.potionAmplifiers.put(Potion.moveSpeed.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.digSpeed.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.damageBoost.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.regeneration.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.harm.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.heal.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.resistance.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.poison.getId(), "5");
        PotionHelper.potionAmplifiers.put(Potion.jump.getId(), "5");
    }
    
    public static boolean checkFlag(final int p_77914_0_, final int p_77914_1_) {
        return (p_77914_0_ & 1 << p_77914_1_) != 0x0;
    }
    
    private static int isFlagSet(final int p_77910_0_, final int p_77910_1_) {
        return checkFlag(p_77910_0_, p_77910_1_) ? 1 : 0;
    }
    
    private static int isFlagUnset(final int p_77916_0_, final int p_77916_1_) {
        return checkFlag(p_77916_0_, p_77916_1_) ? 0 : 1;
    }
    
    public static int getPotionPrefixIndex(final int dataValue) {
        return func_77908_a(dataValue, 5, 4, 3, 2, 1);
    }
    
    public static int calcPotionLiquidColor(final Collection p_77911_0_) {
        int i = 3694022;
        if (p_77911_0_ == null || p_77911_0_.isEmpty()) {
            if (Config.isCustomColors()) {
                i = CustomColors.getPotionColor(0, i);
            }
            return i;
        }
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        for (final Object potioneffect0 : p_77911_0_) {
            final PotionEffect potioneffect2 = (PotionEffect)potioneffect0;
            if (potioneffect2.getIsShowParticles()) {
                int j = Potion.potionTypes[potioneffect2.getPotionID()].getLiquidColor();
                if (Config.isCustomColors()) {
                    j = CustomColors.getPotionColor(potioneffect2.getPotionID(), j);
                }
                for (int k = 0; k <= potioneffect2.getAmplifier(); ++k) {
                    f += (j >> 16 & 0xFF) / 255.0f;
                    f2 += (j >> 8 & 0xFF) / 255.0f;
                    f3 += (j >> 0 & 0xFF) / 255.0f;
                    ++f4;
                }
            }
        }
        if (f4 == 0.0f) {
            return 0;
        }
        f = f / f4 * 255.0f;
        f2 = f2 / f4 * 255.0f;
        f3 = f3 / f4 * 255.0f;
        return (int)f << 16 | (int)f2 << 8 | (int)f3;
    }
    
    public static boolean getAreAmbient(final Collection potionEffects) {
        for (final Object potioneffect : potionEffects) {
            if (!((PotionEffect)potioneffect).getIsAmbient()) {
                return false;
            }
        }
        return true;
    }
    
    public static int getLiquidColor(final int dataValue, final boolean bypassCache) {
        final Integer integer = IntegerCache.func_181756_a(dataValue);
        if (bypassCache) {
            return calcPotionLiquidColor(getPotionEffects(integer, true));
        }
        if (PotionHelper.DATAVALUE_COLORS.containsKey(integer)) {
            return PotionHelper.DATAVALUE_COLORS.get(integer);
        }
        final int i = calcPotionLiquidColor(getPotionEffects(integer, false));
        PotionHelper.DATAVALUE_COLORS.put(integer, i);
        return i;
    }
    
    public static String getPotionPrefix(final int dataValue) {
        final int i = getPotionPrefixIndex(dataValue);
        return PotionHelper.potionPrefixes[i];
    }
    
    private static int func_77904_a(final boolean p_77904_0_, final boolean p_77904_1_, final boolean p_77904_2_, final int p_77904_3_, final int p_77904_4_, final int p_77904_5_, final int p_77904_6_) {
        int i = 0;
        if (p_77904_0_) {
            i = isFlagUnset(p_77904_6_, p_77904_4_);
        }
        else if (p_77904_3_ != -1) {
            if (p_77904_3_ == 0 && countSetFlags(p_77904_6_) == p_77904_4_) {
                i = 1;
            }
            else if (p_77904_3_ == 1 && countSetFlags(p_77904_6_) > p_77904_4_) {
                i = 1;
            }
            else if (p_77904_3_ == 2 && countSetFlags(p_77904_6_) < p_77904_4_) {
                i = 1;
            }
        }
        else {
            i = isFlagSet(p_77904_6_, p_77904_4_);
        }
        if (p_77904_1_) {
            i *= p_77904_5_;
        }
        if (p_77904_2_) {
            i *= -1;
        }
        return i;
    }
    
    private static int countSetFlags(int p_77907_0_) {
        int i;
        for (i = 0; p_77907_0_ > 0; p_77907_0_ &= p_77907_0_ - 1, ++i) {}
        return i;
    }
    
    private static int parsePotionEffects(final String p_77912_0_, final int p_77912_1_, final int p_77912_2_, final int p_77912_3_) {
        if (p_77912_1_ >= p_77912_0_.length() || p_77912_2_ < 0 || p_77912_1_ >= p_77912_2_) {
            return 0;
        }
        final int i = p_77912_0_.indexOf(124, p_77912_1_);
        if (i >= 0 && i < p_77912_2_) {
            final int l1 = parsePotionEffects(p_77912_0_, p_77912_1_, i - 1, p_77912_3_);
            if (l1 > 0) {
                return l1;
            }
            final int i2 = parsePotionEffects(p_77912_0_, i + 1, p_77912_2_, p_77912_3_);
            return (i2 > 0) ? i2 : 0;
        }
        else {
            final int j = p_77912_0_.indexOf(38, p_77912_1_);
            if (j < 0 || j >= p_77912_2_) {
                boolean flag = false;
                boolean flag2 = false;
                boolean flag3 = false;
                boolean flag4 = false;
                boolean flag5 = false;
                byte b0 = -1;
                int k = 0;
                int i3 = 0;
                int j2 = 0;
                for (int k2 = p_77912_1_; k2 < p_77912_2_; ++k2) {
                    final char c0 = p_77912_0_.charAt(k2);
                    if (c0 >= '0' && c0 <= '9') {
                        if (flag) {
                            i3 = c0 - '0';
                            flag2 = true;
                        }
                        else {
                            k *= 10;
                            k += c0 - '0';
                            flag3 = true;
                        }
                    }
                    else if (c0 == '*') {
                        flag = true;
                    }
                    else if (c0 == '!') {
                        if (flag3) {
                            j2 += func_77904_a(flag4, flag2, flag5, b0, k, i3, p_77912_3_);
                            flag4 = false;
                            flag5 = false;
                            flag = false;
                            flag2 = false;
                            flag3 = false;
                            i3 = 0;
                            k = 0;
                            b0 = -1;
                        }
                        flag4 = true;
                    }
                    else if (c0 == '-') {
                        if (flag3) {
                            j2 += func_77904_a(flag4, flag2, flag5, b0, k, i3, p_77912_3_);
                            flag4 = false;
                            flag5 = false;
                            flag = false;
                            flag2 = false;
                            flag3 = false;
                            i3 = 0;
                            k = 0;
                            b0 = -1;
                        }
                        flag5 = true;
                    }
                    else if (c0 != '=' && c0 != '<' && c0 != '>') {
                        if (c0 == '+' && flag3) {
                            j2 += func_77904_a(flag4, flag2, flag5, b0, k, i3, p_77912_3_);
                            flag4 = false;
                            flag5 = false;
                            flag = false;
                            flag2 = false;
                            flag3 = false;
                            i3 = 0;
                            k = 0;
                            b0 = -1;
                        }
                    }
                    else {
                        if (flag3) {
                            j2 += func_77904_a(flag4, flag2, flag5, b0, k, i3, p_77912_3_);
                            flag4 = false;
                            flag5 = false;
                            flag = false;
                            flag2 = false;
                            flag3 = false;
                            i3 = 0;
                            k = 0;
                            b0 = -1;
                        }
                        if (c0 == '=') {
                            b0 = 0;
                        }
                        else if (c0 == '<') {
                            b0 = 2;
                        }
                        else if (c0 == '>') {
                            b0 = 1;
                        }
                    }
                }
                if (flag3) {
                    j2 += func_77904_a(flag4, flag2, flag5, b0, k, i3, p_77912_3_);
                }
                return j2;
            }
            final int m = parsePotionEffects(p_77912_0_, p_77912_1_, j - 1, p_77912_3_);
            if (m <= 0) {
                return 0;
            }
            final int j3 = parsePotionEffects(p_77912_0_, j + 1, p_77912_2_, p_77912_3_);
            return (j3 <= 0) ? 0 : ((m > j3) ? m : j3);
        }
    }
    
    public static List getPotionEffects(final int p_77917_0_, final boolean p_77917_1_) {
        ArrayList arraylist = null;
        Potion[] potionTypes;
        for (int length = (potionTypes = Potion.potionTypes).length, k = 0; k < length; ++k) {
            final Potion potion = potionTypes[k];
            if (potion != null && (!potion.isUsable() || p_77917_1_)) {
                final String s = PotionHelper.potionRequirements.get(potion.getId());
                if (s != null) {
                    int i = parsePotionEffects(s, 0, s.length(), p_77917_0_);
                    if (i > 0) {
                        int j = 0;
                        final String s2 = PotionHelper.potionAmplifiers.get(potion.getId());
                        if (s2 != null) {
                            j = parsePotionEffects(s2, 0, s2.length(), p_77917_0_);
                            if (j < 0) {
                                j = 0;
                            }
                        }
                        if (potion.isInstant()) {
                            i = 1;
                        }
                        else {
                            i = 1200 * (i * 3 + (i - 1) * 2);
                            i >>= j;
                            i = (int)Math.round(i * potion.getEffectiveness());
                            if ((p_77917_0_ & 0x4000) != 0x0) {
                                i = (int)Math.round(i * 0.75 + 0.5);
                            }
                        }
                        if (arraylist == null) {
                            arraylist = Lists.newArrayList();
                        }
                        final PotionEffect potioneffect = new PotionEffect(potion.getId(), i, j);
                        if ((p_77917_0_ & 0x4000) != 0x0) {
                            potioneffect.setSplashPotion(true);
                        }
                        arraylist.add(potioneffect);
                    }
                }
            }
        }
        return arraylist;
    }
    
    private static int brewBitOperations(int p_77906_0_, final int p_77906_1_, final boolean p_77906_2_, final boolean p_77906_3_, final boolean p_77906_4_) {
        if (p_77906_4_) {
            if (!checkFlag(p_77906_0_, p_77906_1_)) {
                return 0;
            }
        }
        else if (p_77906_2_) {
            p_77906_0_ &= ~(1 << p_77906_1_);
        }
        else if (p_77906_3_) {
            if ((p_77906_0_ & 1 << p_77906_1_) == 0x0) {
                p_77906_0_ |= 1 << p_77906_1_;
            }
            else {
                p_77906_0_ &= ~(1 << p_77906_1_);
            }
        }
        else {
            p_77906_0_ |= 1 << p_77906_1_;
        }
        return p_77906_0_;
    }
    
    public static int applyIngredient(int p_77913_0_, final String p_77913_1_) {
        final byte b0 = 0;
        final int i = p_77913_1_.length();
        boolean flag = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;
        int j = 0;
        for (int k = b0; k < i; ++k) {
            final char c0 = p_77913_1_.charAt(k);
            if (c0 >= '0' && c0 <= '9') {
                j *= 10;
                j += c0 - '0';
                flag = true;
            }
            else if (c0 == '!') {
                if (flag) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, j, flag3, flag2, flag4);
                    flag4 = false;
                    flag2 = false;
                    flag3 = false;
                    flag = false;
                    j = 0;
                }
                flag2 = true;
            }
            else if (c0 == '-') {
                if (flag) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, j, flag3, flag2, flag4);
                    flag4 = false;
                    flag2 = false;
                    flag3 = false;
                    flag = false;
                    j = 0;
                }
                flag3 = true;
            }
            else if (c0 == '+') {
                if (flag) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, j, flag3, flag2, flag4);
                    flag4 = false;
                    flag2 = false;
                    flag3 = false;
                    flag = false;
                    j = 0;
                }
            }
            else if (c0 == '&') {
                if (flag) {
                    p_77913_0_ = brewBitOperations(p_77913_0_, j, flag3, flag2, flag4);
                    flag4 = false;
                    flag2 = false;
                    flag3 = false;
                    flag = false;
                    j = 0;
                }
                flag4 = true;
            }
        }
        if (flag) {
            p_77913_0_ = brewBitOperations(p_77913_0_, j, flag3, flag2, flag4);
        }
        return p_77913_0_ & 0x7FFF;
    }
    
    public static int func_77908_a(final int p_77908_0_, final int p_77908_1_, final int p_77908_2_, final int p_77908_3_, final int p_77908_4_, final int p_77908_5_) {
        return (checkFlag(p_77908_0_, p_77908_1_) ? 16 : 0) | (checkFlag(p_77908_0_, p_77908_2_) ? 8 : 0) | (checkFlag(p_77908_0_, p_77908_3_) ? 4 : 0) | (checkFlag(p_77908_0_, p_77908_4_) ? 2 : 0) | (checkFlag(p_77908_0_, p_77908_5_) ? 1 : 0);
    }
    
    public static void clearPotionColorCache() {
        PotionHelper.DATAVALUE_COLORS.clear();
    }
}
