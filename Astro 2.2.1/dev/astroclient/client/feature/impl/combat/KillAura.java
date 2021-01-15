package dev.astroclient.client.feature.impl.combat;

import awfdd.ksksk.zabejhf.rgsd.xasnfkj.zajjebf.Subscribe;
import dev.astroclient.client.Client;
import dev.astroclient.client.event.impl.packet.EventReceivePacket;
import dev.astroclient.client.event.impl.player.EventMotion;
import dev.astroclient.client.event.impl.render.EventRender2D;
import dev.astroclient.client.feature.Category;
import dev.astroclient.client.feature.ToggleableFeature;
import dev.astroclient.client.feature.annotation.Toggleable;
import dev.astroclient.client.property.impl.BooleanProperty;
import dev.astroclient.client.property.impl.MultiSelectableProperty;
import dev.astroclient.client.property.impl.StringProperty;
import dev.astroclient.client.property.impl.number.NumberProperty;
import dev.astroclient.client.property.impl.number.Type;
import dev.astroclient.client.util.EntityUtil;
import dev.astroclient.client.util.Timer;
import dev.astroclient.client.util.math.RotationUtil;
import dev.astroclient.client.util.render.ColorHelper;
import dev.astroclient.client.util.render.Render2DUtil;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Zane for PublicBase
 * @since 10/24/19
 */

@Toggleable(label = "KillAura", description = "Attacks entities.", category = Category.COMBAT)
public class KillAura extends ToggleableFeature {

    public StringProperty mode = new StringProperty("Mode", true, "Switch", new String[]{"Priority", "Switch"});
    public StringProperty sortingMode = new StringProperty("Sorting Mode", true, "Health", new String[]{"Health", "Angle", "Distance"});
    public MultiSelectableProperty entities = new MultiSelectableProperty("Entities", true, new String[]{"Players"}, new String[]{"Players", "Mobs", "Animals", "Villagers"});

    public NumberProperty<Integer> aps = new NumberProperty<>("APS", true, 11, 1, 1, 20);
    public NumberProperty<Float> range = new NumberProperty<>("Range", true, 4.45F, 0.05F, 3F, 6F);
    public NumberProperty<Float> blockRange = new NumberProperty<>("Block Range", true, 6F, 0.05F, 3F, 8F);
    public NumberProperty<Float> aimSpeed = new NumberProperty<>("Aim Speed", true, 10F, 0.2F, 1.0F, 10F);
    public NumberProperty<Integer> switchDelay = new NumberProperty<>("Switch Delay", true, 250, 1, 0, 1000, Type.MILLISECONDS);

    public BooleanProperty autoblock = new BooleanProperty("Auto Block", true, true);
    public BooleanProperty lockView = new BooleanProperty("Lock View", true, false);
    public BooleanProperty teams = new BooleanProperty("Teams", true, false);
    public BooleanProperty walls = new BooleanProperty("Through Walls", true, false);
    public BooleanProperty targetHUD = new BooleanProperty("Target HUD", true, true);
    public BooleanProperty disableOnDeath = new BooleanProperty("Disable", true, true);

    public boolean blocking;

    private int targetIndex;

    public EntityLivingBase target;

    private Timer attackTimer = new Timer();
    private Timer switchTimer = new Timer();

    private List<EntityLivingBase> targetList = new ArrayList<>();

    private final String[] strings = new String[]{"1st Killer - ", "1st Place - ", "You died! Want to play again? Click here!", " - Damage Dealt - ", "1st - ", "Winning Team - ", "Winners: ", "Winner: ", "Winning Team: ", " win the game!", "1st Place: ", "Last team standing!", "Winner #1 (", "Top Survivors", "Winners - "};

    @Override
    public void onEnable() {
        targetList.clear();
        target = null;
        blocking = false;
    }

    @Override
    public void onDisable() {
        targetList.clear();
        target = null;
        blocking = false;
    }

    @Subscribe
    public void onEvent(EventReceivePacket eventReceivePacket) {
        if (eventReceivePacket.getPacket() instanceof S02PacketChat &&
                disableOnDeath.getValue()) {
            for (String string : strings) {
                if (((S02PacketChat) eventReceivePacket.getPacket()).getChatComponent().getUnformattedText().contains(string))
                    if (this.getState())
                        this.toggle();
            }
        }
    }

    // TODO: Clean up code
    @Subscribe
    public void onEvent(EventRender2D eventRender2D) {
        if (target != null &&
                target instanceof EntityPlayer &&
                targetHUD.getValue()) {
            double hpPercentage = target.getHealth() / target.getMaxHealth();
            float scaledWidth = eventRender2D.getScaledResolution().getScaledWidth();
            float scaledHeight = eventRender2D.getScaledResolution().getScaledHeight();
            if (hpPercentage > 1)
                hpPercentage = 1;
            else if (hpPercentage < 0)
                hpPercentage = 0;
            Render2DUtil.drawRect(scaledWidth / 2 - 200, scaledHeight / 2 - 42, scaledWidth / 2 - 200 + 40 + (mc.fontRendererObj.getStringWidth(target.getName()) > 105 ? mc.fontRendererObj.getStringWidth(target.getName()) - 10 : 105), scaledHeight / 2 - 2, new Color(0, 0, 0, 150).getRGB());
            Render2DUtil.drawFace((int) scaledWidth / 2 - 196, (int) (scaledHeight / 2 - 38), 8, 8, 8, 8, 32, 32, 64, 64, (AbstractClientPlayer) target);
            mc.fontRendererObj.drawStringWithShadow(target.getName(), scaledWidth / 2 - 196 + 40, scaledHeight / 2 - 36, -1);
            Render2DUtil.drawRect(scaledWidth / 2 - 196 + 40, scaledHeight / 2 - 26, scaledWidth / 2 - 196 + 40 + (70 * 1.25), scaledHeight / 2 - 14, new Color(0, 0, 0).getRGB());
            Render2DUtil.drawRect(scaledWidth / 2 - 196 + 40, scaledHeight / 2 - 26, scaledWidth / 2 - 196 + 40 + (hpPercentage * 1.25) * 70, scaledHeight / 2 - 14, ColorHelper.getHealthColor(target).getRGB());
            mc.fontRendererObj.drawStringWithShadow(String.format("%.1f", target.getHealth()), scaledWidth / 2 - 196 + 40 + 36, scaledHeight / 2 - 23, ColorHelper.getHealthColor(target).getRGB());
            mc.fontRendererObj.drawStringWithShadow("Distance: \2477" + (int) mc.thePlayer.getDistanceToEntity(target) + "m", scaledWidth / 2 - 196 + 40, scaledHeight / 2 - 12, -1);
        }
    }

    @Subscribe
    public void onEvent(EventMotion eventMotion) {
        this.setSuffix(mode.getValue());

        collectTargets();

        sortTargets();

        if (switchTimer.hasReached(switchDelay.getValue()) && mode.getValue().equals("Switch")) {
            targetIndex++;
            switchTimer.reset();
        }

        if (targetIndex >= targetList.size())
            targetIndex = 0;

        target = !targetList.isEmpty() &&
                targetIndex < targetList.size() ?
                targetList.get(targetIndex) :
                null;

        if (!isHoldingSword())
            blocking = false;

        if (target == null) {
            if (blocking)
                unblock();
            return;
        }

        switch (eventMotion.getEventType()) {
            case PRE:
                float yaw = RotationUtil.getRotations(target, (aimSpeed.getValue() * 5F))[0];
                float pitch = RotationUtil.getRotations(target, (aimSpeed.getValue() * 5F))[1];
                if (lockView.getValue()) {
                    mc.thePlayer.rotationYaw = yaw;
                    mc.thePlayer.rotationPitch = pitch;
                } else {
                    eventMotion.setYaw(yaw);
                    eventMotion.setPitch(pitch);
                }

                if (attackTimer.hasReached(1000 / aps.getValue())) {
                    if (isValid(target, false)) {

                        MovingObjectPosition ray = EntityUtil.rayCast(
                                mc.thePlayer,
                                target.posX,
                                target.posY + target.getEyeHeight(),
                                target.posZ);

                        if (ray != null) {
                            Entity entityHit = ray.entityHit;
                            if (entityHit instanceof EntityLivingBase)
                                if (isValid((EntityLivingBase) entityHit, false))
                                    target = (EntityLivingBase) entityHit;
                        }

                        if (isHoldingSword())
                            unblock();

                        mc.thePlayer.swingItem();
                        attack(target);
                        attackTimer.reset();
                    }
                }
                break;
            case POST:
                if (isHoldingSword())
                    block();
                break;
        }
    }

    private void attack(EntityLivingBase entityLivingBase) {
        final float sharpLevel = EnchantmentHelper.func_152377_a(
                mc.thePlayer.getHeldItem(),
                EnumCreatureAttribute.UNDEFINED);

        if (sharpLevel > 0.0F)
            mc.thePlayer.onEnchantmentCritical(entityLivingBase);

        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(
                new C02PacketUseEntity(entityLivingBase,
                        C02PacketUseEntity.Action.ATTACK));
    }

    private void block() {
        if (autoblock.getValue() &&
                !mc.gameSettings.keyBindUseItem.isPressed() &&
                !blocking) {
            mc.getNetHandler().getNetworkManager().sendPacket(
                    new C08PacketPlayerBlockPlacement(
                            new BlockPos(-.8, -.8, -.8),
                            255,
                            mc.thePlayer.getCurrentEquippedItem(),
                            0,
                            0,
                            0));
            blocking = true;
        }
    }

    private void unblock() {
        if (autoblock.getValue() && blocking) {
            mc.getNetHandler().getNetworkManager().sendPacket((
                    new C07PacketPlayerDigging(
                            C07PacketPlayerDigging.Action.RELEASE_USE_ITEM,
                            new BlockPos(-.8, -.8, -.8),
                            EnumFacing.DOWN)));
            blocking = false;
        }
    }

    private void sortTargets() {
        switch (sortingMode.getValue()) {
            case "Angle":
                targetList.sort(
                        Comparator.comparingDouble(
                                RotationUtil::getAngleChange));
                break;
            case "Distance":
                targetList.sort(
                        Comparator.comparingDouble(
                                EntityUtil::getDistanceToEntity));
                break;
            case "Health":
                targetList.sort(
                        Comparator.comparingDouble(
                                EntityLivingBase::getHealth));
                break;
        }
    }

    private void collectTargets() {
        targetList.clear();

        for (Entity entity : mc.thePlayer.getEntityWorld().loadedEntityList) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entityLivingBase = (EntityLivingBase) entity;
                if (isValid(entityLivingBase, true))
                    targetList.add(entityLivingBase);
            }
        }
    }

    private boolean isHoldingSword() {
        return mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    private boolean isValid(EntityLivingBase entityLivingBase, boolean blocking) {
        if (entityLivingBase == mc.thePlayer || entityLivingBase.isDead || entityLivingBase.getDistanceToEntity(mc.thePlayer) > (blocking ? blockRange.getValue() : range.getValue()) || entityLivingBase.getHealth() == 0 || entityLivingBase.isInvisible() || entityLivingBase instanceof EntityArmorStand || (Client.INSTANCE.featureManager.antiBot.getState() && Client.INSTANCE.featureManager.antiBot.bots.contains(entityLivingBase)))
            return false;

        if (!entityLivingBase.canEntityBeSeen(mc.thePlayer) && !walls.getValue())
            return false;

        if (entityLivingBase instanceof EntityPlayer && !entities.getSelectedObjects().contains("Players"))
            return false;

        if (EntityUtil.isOnSameTeam(entityLivingBase) && teams.getValue())
            return false;

        if ((entityLivingBase instanceof EntityMob || entityLivingBase instanceof EntityAmbientCreature || entityLivingBase instanceof EntityWaterMob) && !entities.getSelectedObjects().contains("Mobs"))
            return false;

        if (entityLivingBase instanceof EntityAnimal && !entities.getSelectedObjects().contains("Animals"))
            return false;

        if (entityLivingBase instanceof EntityGolem && !entities.getSelectedObjects().contains("Golems"))
            return false;

        return !(entityLivingBase instanceof EntityVillager) || entities.getSelectedObjects().contains("Villagers");
    }
}
