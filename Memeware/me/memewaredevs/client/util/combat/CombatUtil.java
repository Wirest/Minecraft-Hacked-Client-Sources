
package me.memewaredevs.client.util.combat;

import me.memewaredevs.client.util.misc.MinecraftUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

public class CombatUtil implements MinecraftUtil {
    public static List botList = new ArrayList();

    public static Entity getTarget(boolean teams, final boolean mobs, final boolean players, final double range, final boolean invis) {
        for (final Object obj : mc.theWorld.loadedEntityList) {
            EntityLivingBase o;
            if (!(obj instanceof EntityLivingBase)
                    || (o = (EntityLivingBase) obj).getDistanceToEntity(mc.thePlayer) > range
                    || o.isInvisible() && !invis || o.isDead() || o.getHealth() == 0.0f || o == mc.thePlayer
                    || botList.contains(o))
                continue;
            return o;
        }
        return null;
    }

    public static boolean canBlock(boolean teams, final boolean mobs, final boolean players, final double range, final boolean invis) {
        for (final Object obj : mc.theWorld.loadedEntityList) {
            EntityLivingBase o;
            if (!(obj instanceof EntityLivingBase)
                    || (o = (EntityLivingBase) obj).getDistanceToEntity(mc.thePlayer) > range
                    || o.isInvisible() && !invis || o.isDead()
                    || o == mc.thePlayer || botList.contains(o) || !(o instanceof EntityPlayer) && players) {
                continue;
            }

            return true;
        }
        return false;
    }

    public static List<EntityLivingBase> getTargets(boolean teams, final int maxTargets, final boolean mobs, final boolean players, final double range,
                                                    final boolean invis) {
        final ArrayList<EntityLivingBase> list = new ArrayList<>();
        for (final Object obj : mc.theWorld.loadedEntityList) {
            EntityLivingBase o;
            if (!(obj instanceof EntityLivingBase)
                    || (o = (EntityLivingBase) obj).getDistanceToEntity(mc.thePlayer) > (((EntityLivingBase) obj).posY > mc.thePlayer.posY + 1 ? range + 2 : range)
                    || o.isInvisible() && !invis || o.isDead() || o.getHealth() == 0.0f || o == mc.thePlayer
                    || botList.contains(o)) continue;
            if (!(obj instanceof EntityPlayer)) continue;
            if (list.size() >= maxTargets) continue;
            list.add(o);
        }
        return list;
    }

}
