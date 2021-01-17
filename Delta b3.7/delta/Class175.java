/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.enchantment.EnchantmentHelper
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.EnumCreatureAttribute
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.network.Packet
 *  net.minecraft.network.play.client.C02PacketUseEntity
 *  net.minecraft.network.play.client.C02PacketUseEntity$Action
 *  net.minecraft.network.play.client.C0APacketAnimation
 */
package delta;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0APacketAnimation;

public class Class175 {
    public static Minecraft shoot$ = Minecraft.getMinecraft();

    public static float _catholic(EntityLivingBase entityLivingBase) {
        double d = entityLivingBase.field_70165_t - Class175.shoot$.thePlayer.field_70165_t;
        double d2 = entityLivingBase.field_70161_v - Class175.shoot$.thePlayer.field_70161_v;
        double d3 = Math.atan2(d, d2) * 57.0;
        d3 = -d3;
        return (float)d3;
    }

    public static boolean _suppose(EntityLivingBase entityLivingBase) {
        if (entityLivingBase == null) {
            return 235 - 345 + 232 + -122;
        }
        if (entityLivingBase == Class175.shoot$.thePlayer) {
            return 61 - 81 + 2 + 18;
        }
        if (!entityLivingBase.isEntityAlive()) {
            return 120 - 235 + 82 - 27 + 60;
        }
        return 53 - 72 + 69 - 40 + -9;
    }

    public static float _vampire(EntityLivingBase entityLivingBase) {
        double d = entityLivingBase.field_70163_u + (double)entityLivingBase.getEyeHeight() - (Class175.shoot$.thePlayer.field_70163_u + (double)Class175.shoot$.thePlayer.func_70047_e());
        double d2 = Math.asin(d /= (double)Class175.shoot$.thePlayer.func_70032_d((Entity)entityLivingBase)) * 57.0;
        d2 = -d2;
        return (float)d2;
    }

    public static EntityLivingBase _surname() {
        double d = Double.MAX_VALUE;
        EntityLivingBase entityLivingBase = null;
        for (Object e : Class175.shoot$.theWorld.field_72996_f) {
            EntityLivingBase entityLivingBase2;
            if (!(e instanceof EntityLivingBase) || !Class175._suppose(entityLivingBase2 = (EntityLivingBase)e) || !((double)Class175.shoot$.thePlayer.func_70032_d((Entity)entityLivingBase2) < d)) continue;
            entityLivingBase = entityLivingBase2;
            d = Class175.shoot$.thePlayer.func_70032_d((Entity)entityLivingBase);
        }
        return entityLivingBase;
    }

    public static boolean _heather(EntityLivingBase entityLivingBase, double d) {
        return ((double)Class175.shoot$.thePlayer.func_70032_d((Entity)entityLivingBase) <= d ? 94 - 147 + 44 - 37 + 47 : 55 - 66 + 8 + 3) != 0;
    }

    public static void _printing(EntityLivingBase entityLivingBase, boolean bl, boolean bl2, int n) {
        if (bl) {
            Class175.shoot$.thePlayer.sendQueue.addToSendQueue((Packet)new C0APacketAnimation());
        } else {
            Class175.shoot$.thePlayer.swingItem();
        }
        if (bl2) {
            Class175.shoot$.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity((Entity)entityLivingBase, C02PacketUseEntity.Action.ATTACK));
        } else {
            Class175.shoot$.thePlayer.func_70031_b(159 - 311 + 113 + 39);
            Class175.shoot$.playerController.attackEntity((EntityPlayer)Class175.shoot$.thePlayer, (Entity)entityLivingBase);
        }
        float f = EnchantmentHelper.func_152377_a((ItemStack)Class175.shoot$.thePlayer.func_70694_bm(), (EnumCreatureAttribute)entityLivingBase.getCreatureAttribute());
        if (f > 0.0f && n == 0) {
            Class175.shoot$.thePlayer.func_71047_c((Entity)entityLivingBase);
        }
        for (int i = 120 - 129 + 4 - 1 + 6; i < n; ++i) {
            Class175.shoot$.thePlayer.func_71009_b((Entity)entityLivingBase);
            Class175.shoot$.thePlayer.func_71047_c((Entity)entityLivingBase);
        }
    }
}

