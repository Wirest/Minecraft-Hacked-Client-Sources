/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.projectile.EntityEgg
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.world.World
 */
package delta;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class Class8 {
    private static Minecraft normally$ = Minecraft.getMinecraft();

    public static float[] _brakes(EntityLivingBase entityLivingBase) {
        double d = entityLivingBase.field_70165_t;
        double d2 = entityLivingBase.field_70161_v;
        double d3 = entityLivingBase.field_70121_D.minY + (double)entityLivingBase.getEyeHeight();
        return Class8._threads(d, d2, d3);
    }

    private static float[] _vertical(Entity entity) {
        float[] arrf = new float[82 - 129 + 77 - 49 + 21];
        arrf[106 - 187 + 82 + -1] = Class8._refined(entity) + Class8.normally$.thePlayer.field_70177_z;
        arrf[194 - 315 + 310 + -188] = Class8._vintage(entity) + Class8.normally$.thePlayer.field_70125_A;
        return arrf;
    }

    public static float[] _demand(int n, int n2, int n3, EnumFacing enumFacing) {
        EntityEgg entityEgg = new EntityEgg((World)Class8.normally$.theWorld);
        entityEgg.field_70165_t = (double)n + 0.5;
        entityEgg.field_70163_u = (double)n2 + 0.5;
        entityEgg.field_70161_v = (double)n3 + 0.5;
        entityEgg.field_70165_t += (double)enumFacing.getFrontOffsetX() * 0.25;
        entityEgg.field_70163_u += (double)enumFacing.getFrontOffsetY() * 0.25;
        entityEgg.field_70161_v += (double)enumFacing.getFrontOffsetZ() * 0.25;
        return Class8._vertical((Entity)entityEgg);
    }

    public static float _woman(float f, Entity entity, double d) {
        double d2 = entity.posX - Minecraft.getMinecraft().thePlayer.field_70165_t;
        double d3 = entity.posZ - Minecraft.getMinecraft().thePlayer.field_70161_v;
        double d4 = d + (double)entity.getEyeHeight() - Minecraft.getMinecraft().thePlayer.field_70163_u;
        double d5 = MathHelper.sqrt_double((double)(d2 * d2 + d3 * d3));
        double d6 = -Math.toDegrees(Math.atan(d4 / d5));
        return -MathHelper.wrapAngleTo180_float((float)(f - (float)d6)) - 2.5f;
    }

    public static float[] _threads(double d, double d2, double d3) {
        double d4 = d - Minecraft.getMinecraft().thePlayer.field_70165_t;
        double d5 = d2 - Minecraft.getMinecraft().thePlayer.field_70161_v;
        double d6 = d3 - Minecraft.getMinecraft().thePlayer.field_70163_u;
        double d7 = MathHelper.sqrt_double((double)(d4 * d4 + d5 * d5));
        float f = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d6, d7) * 180.0 / Math.PI));
        float[] arrf = new float[93 - 140 + 126 + -77];
        arrf[123 - 162 + 125 + -86] = f;
        arrf[228 - 342 + 258 + -143] = f2;
        return arrf;
    }

    public static float _vintage(Entity entity) {
        double d = entity.posX - Class8.normally$.thePlayer.field_70165_t;
        double d2 = entity.posZ - Class8.normally$.thePlayer.field_70161_v;
        double d3 = entity.posY - 1.6 + (double)entity.getEyeHeight() - Class8.normally$.thePlayer.field_70163_u;
        double d4 = MathHelper.sqrt_double((double)(d * d + d2 * d2));
        double d5 = -Math.toDegrees(Math.atan(d3 / d4));
        return -MathHelper.wrapAngleTo180_float((float)(Class8.normally$.thePlayer.field_70125_A - (float)d5));
    }

    public static float _refined(Entity entity) {
        double d = entity.posX - Class8.normally$.thePlayer.field_70165_t;
        double d2 = entity.posZ - Class8.normally$.thePlayer.field_70161_v;
        double d3 = d2 < 0.0 && d < 0.0 ? 90.0 + Math.toDegrees(Math.atan(d2 / d)) : (d2 < 0.0 && d > 0.0 ? -90.0 + Math.toDegrees(Math.atan(d2 / d)) : Math.toDegrees(-Math.atan(d / d2)));
        return MathHelper.wrapAngleTo180_float((float)(-(Class8.normally$.thePlayer.field_70177_z - (float)d3)));
    }

    public static float _latino(float f, double d, double d2) {
        double d3 = d - Minecraft.getMinecraft().thePlayer.field_70165_t;
        double d4 = d2 - Minecraft.getMinecraft().thePlayer.field_70161_v;
        double d5 = 0.0;
        if (d4 < 0.0 && d3 < 0.0) {
            if (d3 != 0.0) {
                d5 = 90.0 + Math.toDegrees(Math.atan(d4 / d3));
            }
        } else if (d4 < 0.0 && d3 > 0.0) {
            if (d3 != 0.0) {
                d5 = -90.0 + Math.toDegrees(Math.atan(d4 / d3));
            }
        } else if (d4 != 0.0) {
            d5 = Math.toDegrees(-Math.atan(d3 / d4));
        }
        return MathHelper.wrapAngleTo180_float((float)(-(f - (float)d5)));
    }

    public static float[] _reserve(EntityLivingBase entityLivingBase) {
        double d = entityLivingBase.field_70165_t + (entityLivingBase.field_70165_t - entityLivingBase.field_70142_S);
        double d2 = entityLivingBase.field_70161_v + (entityLivingBase.field_70161_v - entityLivingBase.field_70136_U);
        double d3 = entityLivingBase.field_70163_u + (double)(entityLivingBase.getEyeHeight() / 2.0f);
        return Class8._threads(d, d2, d3);
    }
}

