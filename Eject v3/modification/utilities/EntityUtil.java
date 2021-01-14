package modification.utilities;

import modification.extenders.Module;
import modification.interfaces.MCHook;
import modification.main.Modification;
import modification.modules.misc.IRC;
import modification.modules.misc.NoFriends;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.MathHelper;

import java.util.Iterator;
import java.util.Objects;

public final class EntityUtil
        implements MCHook {
    public final EntityLivingBase findEntityWithFOV(float paramFloat, boolean paramBoolean) {
        Object localObject = null;
        float f1 = 180.0F;
        Iterator localIterator = MC.theWorld.loadedEntityList.iterator();
        while (localIterator.hasNext()) {
            Entity localEntity = (Entity) localIterator.next();
            if ((localEntity instanceof EntityLivingBase)) {
                EntityLivingBase localEntityLivingBase = (EntityLivingBase) localEntity;
                if (validated(localEntityLivingBase, paramFloat, paramBoolean)) {
                    float f2 = Math.abs(MathHelper.wrapAngleTo180_float(MC.thePlayer.rotationYaw - Modification.ROTATION_UTIL.rotationsToEntity(localEntityLivingBase)[0]));
                    if (f2 <= f1) {
                        f1 = f2;
                        localObject = localEntityLivingBase;
                    }
                }
            }
        }
        return (EntityLivingBase) localObject;
    }

    public boolean validated(EntityLivingBase paramEntityLivingBase, float paramFloat, boolean paramBoolean) {
        NoFriends localNoFriends = (NoFriends) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("NoFriends"));
        if ((paramEntityLivingBase == null) || ((!paramEntityLivingBase.canEntityBeSeen(MC.thePlayer)) && ((!paramBoolean) || (MC.thePlayer.getDistanceToEntity(paramEntityLivingBase) > 3.0F))) || ((Math.abs(MathHelper.wrapAngleTo180_float(paramEntityLivingBase.rotationYaw - getYaw(paramEntityLivingBase))) > 60.0F) && (MC.thePlayer.getDistanceToEntity(paramEntityLivingBase) > paramFloat - 0.5D))) {
            return false;
        }
        if (((!localNoFriends.enabled) || ((localNoFriends.playerInfoList != null) && (localNoFriends.playerInfoList.size() != MC.getNetHandler().getPlayerInfoMap().size()))) && ((Modification.FRIEND_MANAGER.containsFriend(paramEntityLivingBase.getName())) || (IRC.IRC_FRIENDS.containsKey(paramEntityLivingBase.getName())))) {
            return false;
        }
        if ((((Module) Objects.requireNonNull(Modification.MODULE_MANAGER.checkModuleForName("Teams"))).enabled) && (!paramEntityLivingBase.getDisplayName().getUnformattedText().isEmpty()) && (paramEntityLivingBase.getDisplayName().getUnformattedText().length() > 2) && (paramEntityLivingBase.getDisplayName().getUnformattedText().substring(0, 2).equals(MC.thePlayer.getDisplayName().getUnformattedText().substring(0, 2)))) {
            return false;
        }
        if ((!Modification.rank.equals("Dev")) && (((paramEntityLivingBase instanceof EntityVillager)) || (paramEntityLivingBase.getDisplayName().getUnformattedText().equals("§6Dealer")))) {
            return false;
        }
        ServerData localServerData = MC.getCurrentServerData();
        if ((localServerData != null) && (localServerData.serverIP.toLowerCase().contains("timolia")) && (paramEntityLivingBase.getDisplayName().getUnformattedText().startsWith("§f"))) {
            return false;
        }
        return (paramEntityLivingBase != MC.thePlayer) && (!paramEntityLivingBase.isInvisible()) && (MC.thePlayer.deathTime <= 0) && (paramEntityLivingBase.deathTime <= 3) && (!(MC.currentScreen instanceof GuiInventory)) && (MC.thePlayer.getDistanceToEntity(paramEntityLivingBase) <= paramFloat);
    }

    public final void portPlayer(double paramDouble1, boolean paramBoolean, double paramDouble2) {
        double d1 = -Math.sin(Math.toRadians(MC.thePlayer.rotationYaw)) * paramDouble1;
        double d2 = Math.cos(Math.toRadians(MC.thePlayer.rotationYaw)) * paramDouble1;
        if (paramBoolean) {
            MC.thePlayer.setPositionAndUpdate(MC.thePlayer.posX + d1, MC.thePlayer.posY, MC.thePlayer.posZ + d2);
            return;
        }
        MC.thePlayer.setPosition(MC.thePlayer.posX + d1, MC.thePlayer.posY + paramDouble2, MC.thePlayer.posZ + d2);
    }

    private float getYaw(Entity paramEntity) {
        if (paramEntity != null) {
            double d1 = MC.thePlayer.posX - paramEntity.posX;
            double d2 = MC.thePlayer.posZ - paramEntity.posZ;
            return (float) Math.toDegrees(Math.atan2(d2, d1)) - 90.0F;
        }
        return 0.0F;
    }
}




