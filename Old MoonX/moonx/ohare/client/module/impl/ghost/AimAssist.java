package moonx.ohare.client.module.impl.ghost;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.combat.AntiBot;
import moonx.ohare.client.utils.CombatUtil;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class AimAssist extends Module {
    private EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList<>();
    //private EnumValue<mode> Mode = new EnumValue<>("Mode", mode.NORMAL);
    private NumberValue<Float> range = new NumberValue<>("Range", 4F, 1.0F, 7.0F, 0.1F);
    private BooleanValue players = new BooleanValue("Players", "Target Players", true);
    private BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    private BooleanValue monsters = new BooleanValue("Monsters", "Target Monsters", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);

    public AimAssist() {
        super("AimAssist", Category.GHOST, new Color(168, 166, 158).getRGB());
        setDescription("Aim for you!");
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if (event.isPre()) {
            target = getTarget();
            if (target != null) {
                final float[] dstAngle = getRotationsToEnt(target, getMc().thePlayer);
                final float[] srcAngle = new float[]{getMc().thePlayer.rotationYaw, getMc().thePlayer.rotationPitch};
                float[] angles;
                angles = smoothAngle(dstAngle, srcAngle);
                getMc().thePlayer.LegitRotation(angles[0], angles[1], 1, true);
            }
        }
    }

    private EntityLivingBase getTarget() {
        targets.clear();
        double Dist = Double.MAX_VALUE;
        if (getMc().theWorld != null) {
            for (Object object : getMc().theWorld.loadedEntityList) {
                if ((object instanceof EntityLivingBase)) {
                    EntityLivingBase e = (EntityLivingBase) object;
                    if ((getMc().thePlayer.getDistanceToEntity(e) < Dist)) {
                        if (isTargetable(e, getMc().thePlayer)) {
                            targets.add(e);
                        }
                    }
                }
            }
        }
        if (targets.isEmpty()) return null;
        targets.sort(Comparator.comparingDouble(target -> CombatUtil.yawDist((EntityLivingBase) target)));
        return targets.get(0);
    }

    private boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer) {
        return entity.getUniqueID() != clientPlayer.getUniqueID() && entity != clientPlayer && entity.isEntityAlive() &&
                !AntiBot.getBots().contains(entity) && !Moonx.INSTANCE.getFriendManager().isFriend(entity.getName()) &&
                !(entity.isInvisible() && !invisibles.isEnabled()) &&
                (clientPlayer.getDistanceToEntity(entity) <= range.getValue())
                && ((entity instanceof EntityPlayer && players.isEnabled())
                || ((entity instanceof EntityMob || entity instanceof EntityGolem) && monsters.isEnabled()) || (entity instanceof IAnimals && animals.isEnabled()));
    }
    private float[] smoothAngle(float[] dst, float[] src) {
        float[] smoothedAngle = new float[2];
        smoothedAngle[0] = (src[0] - dst[0]);
        smoothedAngle[1] = (src[1] - dst[1]);
        smoothedAngle = MathUtils.constrainAngle(smoothedAngle);
        smoothedAngle[0] = (src[0] - smoothedAngle[0] / 100 * MathUtils.getRandomInRange(14, 24));
        smoothedAngle[1] = (src[1] - smoothedAngle[1] / 100 * MathUtils.getRandomInRange(3, 8));
        return smoothedAngle;
    }
    private float[] getRotationsToEnt(Entity ent, EntityPlayerSP playerSP) {
        final double differenceX = ent.posX - playerSP.posX;
        final double differenceY = (ent.posY + ent.height) - (playerSP.posY + playerSP.height);
        final double differenceZ = ent.posZ - playerSP.posZ;
        final float rotationYaw = (float) (Math.atan2(differenceZ, differenceX) * 180.0D / Math.PI) - 90.0f;
        final float rotationPitch = (float) (Math.atan2(differenceY, playerSP.getDistanceToEntity(ent)) * 180.0D / Math.PI);
        final float finishedYaw = playerSP.rotationYaw + MathHelper.wrapAngleTo180_float(rotationYaw - playerSP.rotationYaw);
        final float finishedPitch = playerSP.rotationPitch + MathHelper.wrapAngleTo180_float(rotationPitch - playerSP.rotationPitch);
        return new float[]{finishedYaw, -finishedPitch};
    }
}
