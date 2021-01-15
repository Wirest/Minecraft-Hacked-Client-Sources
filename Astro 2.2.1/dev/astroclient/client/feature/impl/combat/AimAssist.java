package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.util.MouseUtil;
import dev.astroclient.client.util.math.MathUtil;
import dev.astroclient.client.util.math.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Toggleable(label = "AimAssist", category = Category.COMBAT)
public class AimAssist extends ToggleableFeature {

    public BooleanProperty clickAim = new BooleanProperty("Click Aim", true, true);
    public BooleanProperty yaw = new BooleanProperty("Yaw Aim", true, true);
    public BooleanProperty pitch = new BooleanProperty("Pitch Aim", true, false);
    public NumberProperty<Float> aimSpeed = new NumberProperty<>("Aim Speed", true, 1F, 0.05F, 0.1F, 2F);
    public NumberProperty<Float> randomisation = new NumberProperty<>("Randomisation", true, 2F, .01F, 0F, 4F);
    public NumberProperty<Float> range = new NumberProperty<>("Range", true, 4F, 0.1F, 3F, 7F);

    private List<EntityLivingBase> targets = new ArrayList<>();

    private EntityLivingBase target;

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        collectTargets();
        sortTargets();
        target = targets.isEmpty() ? null : targets.get(0);
        float random = randomisation.getValue() != 0 ? (float) MathUtil.getRandomInRange(-randomisation.getValue(), randomisation.getValue()) : 0;
        if (!clickAim.getValue() || Mouse.isButtonDown(0))
            if (target != null && mc.currentScreen == null) {
                if (yaw.getValue())
                    mc.thePlayer.rotationYaw = (RotationUtil.getRotations(target, (float) (aimSpeed.getValue() * 3D) + random)[0]) + random;
                if (pitch.getValue())
                    mc.thePlayer.rotationPitch = (RotationUtil.getRotations(target, (float) (aimSpeed.getValue() * 3D) + random)[1]) + random;
            }

    }

    private void sortTargets() {
        targets.sort(Comparator.comparingDouble(RotationUtil::getAngleChange));
    }

    private void collectTargets() {
        targets.clear();
        for (Entity entity : mc.thePlayer.getEntityWorld().loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (isValid(entityLivingBase))
                    targets.add(entityLivingBase);
            }
        }
    }

    private boolean isValid(EntityLivingBase entityLivingBase) {
        return entityLivingBase != mc.thePlayer && !entityLivingBase.isDead && !(entityLivingBase.getDistanceToEntity(mc.thePlayer) > range.getValue()) && entityLivingBase.getHealth() != 0 && !entityLivingBase.isInvisible() && !(entityLivingBase instanceof EntityArmorStand);
    }
}
