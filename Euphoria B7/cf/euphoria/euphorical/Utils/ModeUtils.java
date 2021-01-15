// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Utils;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;

public class ModeUtils
{
    public static String speedMode;
    public static String phaseMode;
    public static boolean espP;
    public static boolean espM;
    public static boolean espA;
    public static boolean tracerP;
    public static boolean tracerM;
    public static boolean tracerA;
    public static boolean bHit;
    public static boolean auraP;
    public static boolean auraM;
    public static boolean auraA;
    
    static {
        ModeUtils.speedMode = "yport";
        ModeUtils.phaseMode = "new";
        ModeUtils.espP = true;
        ModeUtils.espM = false;
        ModeUtils.espA = false;
        ModeUtils.tracerP = true;
        ModeUtils.tracerM = false;
        ModeUtils.tracerA = false;
        ModeUtils.bHit = true;
        ModeUtils.auraP = true;
        ModeUtils.auraM = false;
        ModeUtils.auraA = false;
    }
    
    public static boolean isValidForESP(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return ModeUtils.espP;
        }
        if (entity instanceof EntityMob || entity instanceof EntitySlime) {
            return ModeUtils.espM;
        }
        return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && ModeUtils.espA;
    }
    
    public static boolean isValidForTracers(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return ModeUtils.tracerP;
        }
        if (entity instanceof EntityMob || entity instanceof EntitySlime) {
            return ModeUtils.tracerM;
        }
        return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && ModeUtils.tracerA;
    }
    
    public static boolean isValidForAura(final EntityLivingBase entity) {
        if (entity instanceof EntityPlayer) {
            return ModeUtils.auraP;
        }
        if (entity instanceof EntityMob || entity instanceof EntitySlime) {
            return ModeUtils.auraM;
        }
        return (entity instanceof EntityCreature || entity instanceof EntitySquid || entity instanceof EntityBat || entity instanceof EntityVillager) && ModeUtils.auraA;
    }
}
