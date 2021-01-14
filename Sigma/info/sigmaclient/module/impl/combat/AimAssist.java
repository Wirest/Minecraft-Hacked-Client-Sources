package info.sigmaclient.module.impl.combat;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import info.sigmaclient.event.Event;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.util.RotationUtils;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;

public class AimAssist extends Module {

    private EntityLivingBase target;

    public AimAssist(ModuleData data) {
        super(data);
        settings.put(WEAPON, new Setting<>(WEAPON, true, "Checks if you have a sword in hand."));
        settings.put(X, new Setting<>(X, 0, "Randomization on XZ axis.", 0.01, 0, 1));
        settings.put(Y, new Setting<>(Y, 0, "Randomization on Y axis.", 0.01, 0, 1));
        settings.put(RANGE, new Setting<>(RANGE, 4.5, "The distance in which an entity is valid to attack.", 0.1D, 1.0D, 10.0D));
        settings.put(HORIZONTAL, new Setting<>(HORIZONTAL, 20, "Horizontal speed.", 0.25, 0, 10));
        settings.put(VERTICAL, new Setting<>(VERTICAL, 15, "Vertical speed.", 0.25, 0, 10));
        settings.put(FOVYAW, new Setting<>(FOVYAW, 45, "Yaw FOV check.", 1, 5D, 50));
        settings.put(FOVPITCH, new Setting<>(FOVPITCH, 25, "Vertical FOV check.", 1, 5D, 50));
    }

    private String WEAPON = "WEAPON";
    private String RANGE = "RANGE";
    private String HORIZONTAL = "SPEED-H";
    private String VERTICAL = "SPEED-V";
    private String FOVYAW = "FOVYAW";
    private String FOVPITCH = "FOVPITCH";
    private String X = "RANDOM-XZ";
    private String Y = "RANDOM-Y";

    private int randomNumber() {
        return -1 + (int) (Math.random() * ((1 - (-1)) + 1));
    }

    @Override
    @RegisterEvent(events = {EventUpdate.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                target = getBestEntity();
            } else if (em.isPost() && mc.currentScreen == null) {
                if (mc.thePlayer.getHeldItem() == null || mc.thePlayer.getHeldItem().getItem() == null) {
                    return;
                }
                final Item heldItem = mc.thePlayer.getHeldItem().getItem();
                if ((Boolean) settings.get(WEAPON).getValue() && heldItem != null) {
                    if (!(heldItem instanceof ItemSword)) {
                        return;
                    }
                }
                if (target != null && !FriendManager.isFriend(target.getName()) && mc.thePlayer.isEntityAlive()) {
                    stepAngle();
                }
            }
        }
    }


    private void stepAngle() {
        float yawFactor = ((Number) settings.get(HORIZONTAL).getValue()).floatValue();
        float pitchFactor = ((Number) settings.get(VERTICAL).getValue()).floatValue();
        double xz = ((Number) settings.get(X).getValue()).doubleValue();
        double y = ((Number) settings.get(Y).getValue()).doubleValue();
        float targetYaw = RotationUtils.getYawChange(mc.thePlayer.rotationYaw, target.posX + randomNumber() * xz, target.posZ + randomNumber() * xz);

        if (targetYaw > 0 && targetYaw > yawFactor) {
            mc.thePlayer.rotationYaw += yawFactor;
        } else if (targetYaw < 0 && targetYaw < -yawFactor) {
            mc.thePlayer.rotationYaw -= yawFactor;
        } else {
            mc.thePlayer.rotationYaw += targetYaw;
        }

        float targetPitch = RotationUtils.getPitchChange(mc.thePlayer.rotationPitch, target, target.posY + randomNumber() * y);

        if (targetPitch > 0 && targetPitch > pitchFactor) {
            mc.thePlayer.rotationPitch += pitchFactor;
        } else if (targetPitch < 0 && targetPitch < -pitchFactor) {
            mc.thePlayer.rotationPitch -= pitchFactor;
        } else {
            mc.thePlayer.rotationPitch += targetPitch;
        }

    }

    private EntityLivingBase getBestEntity() {
        List<EntityLivingBase> loaded = new CopyOnWriteArrayList<>();
        for (Object o : mc.theWorld.getLoadedEntityList()) {
            if (o instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) o;
                if (ent.isEntityAlive() && ent instanceof EntityPlayer && ent
                        .getDistanceToEntity(mc.thePlayer) < ((Number) settings.get(RANGE).getValue()).floatValue()
                        && fovCheck(ent)) {
                    if (ent == Killaura.vip) {
                        return ent;
                    }
                    loaded.add(ent);
                }
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        try {
            loaded.sort((o1, o2) -> {
                float[] rot1 = RotationUtils.getRotations(o1);
                float[] rot2 = RotationUtils.getRotations(o2);
                return (int) ((RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot1[0])
                        + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot1[1]))
                        - (RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rot2[0])
                        + RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rot2[1])));
            });
        } catch (Exception e) {
            ChatUtil.printChat("Exception with TM: " + e.getMessage());
        }
        return loaded.get(0);
    }

    private boolean fovCheck(EntityLivingBase ent) {
        float[] rotations = RotationUtils.getRotations(ent);
        float dist = mc.thePlayer.getDistanceToEntity(ent);
        if (dist == 0) {
            dist = 1;
        }
        float yawDist = RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationYaw, rotations[0]);
        float pitchDist = RotationUtils.getDistanceBetweenAngles(mc.thePlayer.rotationPitch, rotations[1]);
        float fovYaw = ((Number) settings.get(FOVYAW).getValue()).floatValue() * 3 / dist;
        float fovPitch = (((Number) settings.get(FOVPITCH).getValue()).floatValue() * 3) / dist;
        return yawDist < fovYaw && pitchDist < fovPitch;
    }

}
