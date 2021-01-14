package moonx.ohare.client.module.impl.visuals;

import com.google.common.collect.Maps;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.module.impl.combat.AntiBot;
import moonx.ohare.client.utils.MathUtils;
import moonx.ohare.client.utils.RenderUtil;
import moonx.ohare.client.utils.Vec3;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.NumberValue;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

public class OffscreenESP extends Module {
    private int alpha;
    private boolean plus_or_minus;
    private NumberValue<Float> size = new NumberValue<>("Size", 10f, 5f, 25f, 0.1f);
    private NumberValue<Integer> radius = new NumberValue<>("Radius", 45, 10, 200, 1);
    private BooleanValue fade = new BooleanValue("Fade", false);
    private EntityListener entityListener = new EntityListener();
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", true);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue passives = new BooleanValue("Passives", true);

    public OffscreenESP() {
        super("OffScreenESP", Category.VISUALS, new Color(0x76DBFF).getRGB());
        setRenderLabel("OffScreen ESP");
        setDescription("OffScreen ESP.");
    }

    @Override
    public void onEnable() {
        alpha = 0;
        plus_or_minus = false;
    }

    @Handler
    public void onRender3D(Render3DEvent event) {
        entityListener.render3d(event);
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        if (fade.isEnabled()) {
            final float speed = 195 / Math.max(Minecraft.getDebugFPS() / 2,1);
            if (alpha <= 60.f || alpha >= 255.f) plus_or_minus = !plus_or_minus;
            if (plus_or_minus) alpha+=speed;
            else alpha-= speed;
            alpha = (int) MathUtils.clamp(alpha, 255, 60);
        } else {
            alpha = 255;
        }
        getMc().theWorld.loadedEntityList.forEach(o -> {
            if (o instanceof EntityLivingBase && isValid((EntityLivingBase) o)) {
                EntityLivingBase entity = (EntityLivingBase) o;
                Vec3 pos = entityListener.getEntityLowerBounds().get(entity);

                if (pos != null) {
                    if (!isOnScreen(pos)) {
                        int x = (Display.getWidth() / 2) / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale);
                        int y = (Display.getHeight() / 2) / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale);
                        float yaw = getRotations(entity) - getMc().thePlayer.rotationYaw;
                        GL11.glTranslatef(x, y, 0);
                        GL11.glRotatef(yaw, 0, 0, 1);
                        GL11.glTranslatef(-x, -y, 0);
                        RenderUtil.drawTracerPointer(x, y - radius.getValue(), size.getValue(), 2, 1, getColor(entity, alpha).getRGB());
                        GL11.glTranslatef(x, y, 0);
                        GL11.glRotatef(-yaw, 0, 0, 1);
                        GL11.glTranslatef(-x, -y, 0);
                    }
                }
            }
        });
    }

    private boolean isOnScreen(Vec3 pos) {
        if (pos.xCoord > -1 && pos.zCoord < 1)
            return pos.xCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) >= 0 && pos.xCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) <= Display.getWidth() && pos.yCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) >= 0 && pos.yCoord / (getMc().gameSettings.guiScale == 0 ? 1 : getMc().gameSettings.guiScale) <= Display.getHeight();

        return false;
    }

    private boolean isValid(EntityLivingBase entity) {
        return !AntiBot.getBots().contains(entity) && entity != getMc().thePlayer && isValidType(entity) && entity.getEntityId() != -1488 && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.isEnabled());
    }

    private boolean isValidType(EntityLivingBase entity) {
        return (players.isEnabled() && entity instanceof EntityPlayer) || (mobs.isEnabled() && (entity instanceof EntityMob || entity instanceof EntitySlime) || (passives.isEnabled() && (entity instanceof EntityVillager || entity instanceof EntityGolem)) || (animals.isEnabled() && entity instanceof EntityAnimal));
    }

    private float getRotations(EntityLivingBase ent) {
        final double x = ent.posX - getMc().thePlayer.posX;
        final double z = ent.posZ - getMc().thePlayer.posZ;
        final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
        return yaw;
    }

    private Color getColor(EntityLivingBase player, int alpha) {
        float f = getMc().thePlayer.getDistanceToEntity(player);
        float f1 = 40;
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        final Color clr = new Color(Color.HSBtoRGB(f2 / 3.0F, 1.0F, 1.0F) | 0xFF000000);
        return new Color(clr.getRed(), clr.getGreen(), clr.getBlue(), alpha);
    }

    public class EntityListener {
        private Map<Entity, Vec3> entityUpperBounds = Maps.newHashMap();
        private Map<Entity, Vec3> entityLowerBounds = Maps.newHashMap();

        private void render3d(Render3DEvent event) {
            if (!entityUpperBounds.isEmpty()) {
                entityUpperBounds.clear();
            }
            if (!entityLowerBounds.isEmpty()) {
                entityLowerBounds.clear();
            }
            for (Entity e : getMc().theWorld.loadedEntityList) {
                Vec3 bound = getEntityRenderPosition(e);
                bound.add(new Vec3(0, e.height + 0.2, 0));
                Vec3 upperBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord, bound.zCoord), lowerBounds = RenderUtil.to2D(bound.xCoord, bound.yCoord - 2, bound.zCoord);
                if (upperBounds != null && lowerBounds != null) {
                    entityUpperBounds.put(e, upperBounds);
                    entityLowerBounds.put(e, lowerBounds);
                }
            }
        }

        private Vec3 getEntityRenderPosition(Entity entity) {
            double partial = getMc().timer.renderPartialTicks;

            double x = entity.lastTickPosX + ((entity.posX - entity.lastTickPosX) * partial) - getMc().getRenderManager().viewerPosX;
            double y = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * partial) - getMc().getRenderManager().viewerPosY;
            double z = entity.lastTickPosZ + ((entity.posZ - entity.lastTickPosZ) * partial) - getMc().getRenderManager().viewerPosZ;

            return new Vec3(x, y, z);
        }

        public Map<Entity, Vec3> getEntityLowerBounds() {
            return entityLowerBounds;
        }
    }
}
