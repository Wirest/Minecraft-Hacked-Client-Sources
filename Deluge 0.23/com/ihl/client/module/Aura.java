package com.ihl.client.module;

import com.ihl.client.Helper;
import com.ihl.client.comparator.EntityCrosshairComparator;
import com.ihl.client.comparator.EntityDistanceComparator;
import com.ihl.client.comparator.EntityHealthComparator;
import com.ihl.client.event.Event;
import com.ihl.client.event.EventHandler;
import com.ihl.client.event.EventPlayerMotion;
import com.ihl.client.event.EventRender;
import com.ihl.client.module.option.Option;
import com.ihl.client.module.option.ValueBoolean;
import com.ihl.client.module.option.ValueChoice;
import com.ihl.client.module.option.ValueDouble;
import com.ihl.client.util.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@EventHandler(events = {EventPlayerMotion.class, EventRender.class})
public class Aura extends Module {

    private EntityLivingBase target;
    private double oldYaw, oldPitch, yaw, pitch, newYaw, newPitch;
    private TimerUtil attackTimer = new TimerUtil();

    public Aura(String name, String desc, Category category, String keybind) {
        super(name, desc, category, keybind);
        options.put("mode", new Option("Mode", "Target aim mode", new ValueChoice(2, new String[]{"directional", "lock", "silent"}), Option.Type.CHOICE));
        options.put("priority", new Option("Priority", "Switch target selection mode", new ValueChoice(0, new String[] {"distance", "health", "direction"}), Option.Type.CHOICE));
        options.put("distance", new Option("Distance", "Distance to attack entities within", new ValueDouble(3.6, new double[] {0, 10}, 0.1), Option.Type.NUMBER));
        options.put("range", new Option("Range", "View range to attack entities within", new ValueDouble(180, new double[] {0, 180}, 1), Option.Type.NUMBER));
        options.put("turnspeed", new Option("Turn Speed", "Speed to aim towards the target", new ValueDouble(30, new double[] {0, 180}, 1), Option.Type.NUMBER));
        options.put("delay", new Option("Delay", "Attack delay period (s)", new ValueDouble(0.3, new double[]{0.1, 2}, 0.01), Option.Type.NUMBER));
        options.put("multi", new Option("Multi", "Attack multiple entities simultaneously", new ValueBoolean(true), Option.Type.BOOLEAN));
        initCommands(name.toLowerCase().replaceAll(" ", ""));
    }

    public void enable() {
        super.enable();
        if (!HelperUtil.inGame()) {
            return;
        }
        yaw = Helper.player().rotationYaw;
        pitch = Helper.player().rotationPitch;
        newYaw = yaw;
        newPitch = pitch;
    }

    protected void onEvent(Event event) {
        String mode = Option.get(options, "mode").CHOICE();
        String priority = Option.get(options, "priority").CHOICE();
        double distance = Option.get(options, "distance").DOUBLE();
        int range = Option.get(options, "range").INTEGER();
        int turnSpeed = Option.get(options, "turnspeed").INTEGER();
        double delay = Option.get(options, "delay").DOUBLE();
        boolean multi = Option.get(options, "multi").BOOLEAN();

        if (event instanceof EventPlayerMotion) {
            EventPlayerMotion e = (EventPlayerMotion) event;
            if (e.type == Event.Type.PRE) {
                oldYaw = Helper.player().rotationYaw;
                oldPitch = Helper.player().rotationPitch;
                target = null;

                targetEntity(mode, priority, distance, range, delay);

                if (target != null) {
                    double[] rotationTo = EntityUtil.getRotationToEntity(target);

                    newYaw = rotationTo[0];
                    newPitch = rotationTo[1];
                } else {
                    newYaw = oldYaw;
                    newPitch = oldPitch;
                }

                double yawDifference = MathUtil.angleDifference(newYaw, yaw);
                double pitchDifference = MathUtil.angleDifference(newPitch, pitch);
                yaw+= yawDifference > turnSpeed ? turnSpeed : yawDifference < -turnSpeed ? -turnSpeed : yawDifference;
                pitch+= pitchDifference > turnSpeed ? turnSpeed : pitchDifference < -turnSpeed ? -turnSpeed : pitchDifference;

                Helper.player().rotationYaw = (float) yaw;
                Helper.player().rotationPitch = (float) pitch;
            } else if (e.type == Event.Type.POST) {
                if (target != null) {
                    if (attackTimer.isTime(delay)) {
                        double[] rotationTo = EntityUtil.getRotationToEntity(target);
                        double rotationDifference = EntityUtil.getRotationDifference(rotationTo);

                        if (rotationDifference < 35) {
                            Helper.player().swingItem();
                            Helper.controller().attackEntity(Helper.player(), target);

                            if (multi) {
                                for (Object object : Helper.world().getLoadedEntityList()) {
                                    if (object instanceof EntityLivingBase && object != Helper.player()) {
                                        EntityLivingBase entity = (EntityLivingBase) object;

                                        if (isLiable(entity, distance, 35)) {
                                            Helper.player().swingItem();
                                            Helper.controller().attackEntity(Helper.player(), entity);
                                        }
                                    }
                                }
                            }

                            attackTimer.reset();
                        }
                    }
                }

                if (!mode.equalsIgnoreCase("lock")) {
                    Helper.player().rotationYaw = (float) oldYaw;
                    Helper.player().rotationPitch = (float) oldPitch;
                }
            }
        } else if (event instanceof EventRender) {
            EventRender e = (EventRender) event;
            if (e.type == Event.Type.PRE) {
                if (target != null) {
                    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                    GlStateManager.disableDepth();

                    if (!multi) {
                        RenderUtil3D.box(target, 0x80FF0000, 1);
                    } else {
                        float tempYaw = Helper.player().rotationYaw;
                        float tempPitch = Helper.player().rotationPitch;
                        Helper.player().rotationYaw = (float) yaw;
                        Helper.player().rotationPitch = (float) pitch;
                        for (Object object : Helper.world().getLoadedEntityList()) {
                            if (object instanceof EntityLivingBase) {
                                EntityLivingBase entity = (EntityLivingBase) object;

                                if (isLiable(entity, distance, 35)) {
                                    RenderUtil3D.box(entity, 0x80FF0000, 1);
                                }
                            }
                        }
                        Helper.player().rotationYaw = tempYaw;
                        Helper.player().rotationPitch = tempPitch ;
                    }

                    GL11.glPopAttrib();
                }
            }
        }
    }

    private void targetEntity(String mode, String priority, double distance, double range, double delay) {
        List<EntityLivingBase> entities = new ArrayList();
        for(Object object : Helper.world().getLoadedEntityList()) {
            if (object instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) object;

                if (isLiable(entity, distance, range)) {
                    entities.add(entity);
                }
            }
        }

        switch(priority) {
            case "distance":
                Collections.sort(entities, new EntityDistanceComparator(Helper.player()));
                break;
            case "health":
                Collections.sort(entities, new EntityHealthComparator());
                break;
            case "direction":
                Collections.sort(entities, new EntityCrosshairComparator(Helper.player()));
                break;
        }

        if (!entities.isEmpty()) {
            target = entities.get(0);
        }
    }

    private boolean isLiable(EntityLivingBase entity, double distance, double range) {
        double[] rotationTo = EntityUtil.getRotationToEntity(entity);
        double rotationDifference = EntityUtil.getRotationDifference(rotationTo);

        return (entity != Helper.player() &&
                entity.getName() != Helper.player().getName() &&
                entity.isEntityAlive() &&
                !Friends.isFriend(entity.getName()) &&
                Helper.player().getDistanceToEntity(entity) < distance &&
                rotationDifference < range);
    }
}
