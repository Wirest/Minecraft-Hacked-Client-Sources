// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import me.CheerioFX.FusionX.GUI.clickgui.Targets;

public class ModeUtils
{
    public static String speedMode;
    public static String phaseMode;
    
    static {
        ModeUtils.speedMode = "yport";
        ModeUtils.phaseMode = "new";
    }
    
    private static boolean getAuraP() {
        return Targets.players();
    }
    
    private static boolean getAuraM() {
        return Targets.mobs();
    }
    
    private static boolean getAuraA() {
        return Targets.animals();
    }
    
    public static boolean isValidForAura(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return getAuraP();
        }
        if (entity instanceof EntityMob || entity instanceof EntitySlime) {
            return getAuraM();
        }
        return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && getAuraA();
    }
}
