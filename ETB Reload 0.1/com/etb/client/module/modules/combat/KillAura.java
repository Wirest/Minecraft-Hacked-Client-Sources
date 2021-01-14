package com.etb.client.module.modules.combat;

import com.etb.client.Client;
import com.etb.client.event.events.player.UpdateEvent;
import com.etb.client.event.events.world.PacketEvent;
import com.etb.client.module.Module;
import com.etb.client.module.modules.world.Phase;
import com.etb.client.utils.*;
import com.etb.client.utils.value.impl.BooleanValue;
import com.etb.client.utils.value.impl.EnumValue;
import com.etb.client.utils.value.impl.NumberValue;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.util.*;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.Subscribe;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * made by oHare for oHareWare
 *
 * @since 5/30/2019
 **/
public class KillAura extends Module {
    public EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList();
    private TimerUtil timer = new TimerUtil();
    private TimerUtil switchtimer = new TimerUtil();
    private int switchIndex, dynamicTickCounter;
    private final Random random = new Random();
    public EnumValue<Mode> mode = new EnumValue("Mode", Mode.SWITCH);
    private EnumValue<SortMode> sortingmode = new EnumValue("SortMode", SortMode.FOV);
    private NumberValue<Integer> maxtargets = new NumberValue("MaxTargets", 3, 1, 5, 1);
    private NumberValue<Integer> speed = new NumberValue("Speed", 9, 1, 20, 1);
    private NumberValue<Float> range = new NumberValue("Range", 4.2f, 1.0f, 7.0f, 0.1f);
    private NumberValue<Float> blockrange = new NumberValue("BlockRange", 6.0f, 1.0f, 20.0f, 0.1f);
    private NumberValue<Integer> switchspeed = new NumberValue("SwitchSpeed", 300, 100, 2000, 100);
    private NumberValue<Integer> tickdelay = new NumberValue("TickDelay", 500, 100, 1000, 1);
    private NumberValue<Integer> maxangle = new NumberValue("MaxAngle", 12, 1, 50, 1);
    private BooleanValue dtd = new BooleanValue("DTD", true);
    private BooleanValue resetpos = new BooleanValue("Resetpos", true);
    private BooleanValue players = new BooleanValue("Players", true);
    private BooleanValue animals = new BooleanValue("Animals", false);
    private BooleanValue mobs = new BooleanValue("Mobs", false);
    private BooleanValue autoblock = new BooleanValue("AutoBlock", true);
    private BooleanValue criticals = new BooleanValue("Criticals", true);
    private BooleanValue passives = new BooleanValue("Passives", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", false);
    private BooleanValue fakeab = new BooleanValue("FakeAB", false);
    private long dynamicTickValue;
    private boolean nigga, canAttack;
    private double x, y, z;
    public static int stage;
    private float[] prevRotations = new float[2];
    private float[] serverAngles = new float[2];

    public KillAura() {
        super("KillAura", Category.COMBAT, new Color(190, 0, 0, 255).getRGB());
        addValues(mode, sortingmode, maxtargets, speed, range, blockrange, switchspeed, tickdelay,maxangle, resetpos, dtd, players, animals, mobs, autoblock,fakeab, criticals, passives, invisibles);
        setDescription("Auto Attacks an entity");
    }

    @Subscribe
    public void onUpdate(UpdateEvent event) {
        setSuffix(StringUtils.capitalize(mode.getValue().name().toLowerCase()));
        if (AutoApple.doingStuff || AutoHeal.healing || AutoHeal.doSoup) return;
        switch (mode.getValue()) {
            case TICK:
                if (event.isPre()) {
                    target = getBestTarget(event.getYaw());
                    if (target != null) {
                        if (!isValid(target, false)) target = null;
                        final float[] rotations = getRotations(target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                    }
                } else {
                    if (target != null) {
                        final long doDynamicTick = (dtd.isEnabled() && mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null && mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null) ? dynamicTickDelay() : tickdelay.getValue();
                        if (timer.sleep(doDynamicTick)) {
                            mc.thePlayer.swingItem();
                            attack(target, true);
                            swap(9, mc.thePlayer.inventory.currentItem);
                            attack(target, false);
                            attack(target, true);
                            swap(9, mc.thePlayer.inventory.currentItem);
                            attack(target, false);
                            attack(target, true);
                            final ItemStack[] items = target.getInventory();
                            ItemStack helm = null;
                            if (items[3] != null) {
                                helm = items[3];
                            }
                            if (helm != null) {
                                final float oldDura = helm.getMaxDamage();
                                final float newDura = helm.getItemDamage();
                                final float ey = oldDura - newDura;
                                final float damagedone = oldDura - ey;
                                Printer.print("§f" + target.getName() + "§7's Helmet Dura: §f" + ey + " (" + damagedone + ")");
                            }
                        }
                        if (autoblock.isEnabled() && !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    }
                }
                break;
            case SAFETY:
                target = getBestTarget(event.getYaw());
                if (event.isPre()) {
                    if (target != null && mc.thePlayer.canEntityBeSeen(target)) {
                        if (AutoHeal.doSoup || AutoHeal.healing) return;
                        final float[] dstAngle = getRotations(target);
                        final float[] srcAngle = new float[]{serverAngles[0], serverAngles[1]};
                        serverAngles = smoothAngle(dstAngle, srcAngle);
                        event.setYaw(serverAngles[0]);
                        event.setPitch(serverAngles[1]);
                        if (timer.reach(1000 / randomNumber(9, 13)) && getDistance(prevRotations) < 7 && mc.currentScreen == null && canAttack) {
                            float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), mc.thePlayer.getCreatureAttribute());
                            if (sharpLevel > 0) mc.thePlayer.onEnchantmentCritical(target);
                            MovingObjectPosition ray;
                            ray = rayCast(mc.thePlayer, target.posX, target.posY + target.getEyeHeight() * 0.7, target.posZ);
                            if (ray != null) {
                                Entity entityHit = ray.entityHit;
                                if (entityHit instanceof EntityLivingBase) {
                                    target = (EntityLivingBase) entityHit;
                                }
                            }
                            attack(target,false);
                            timer.reset();
                        }
                    } else if (target == null) {
                        serverAngles[0] = (mc.thePlayer.rotationYaw);
                        serverAngles[1] = (mc.thePlayer.rotationPitch);
                    }
                    if (autoblock.isEnabled() && !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                }
                break;
            case SMOOTH:
                target = getBestTarget(event.getYaw());
                if (event.isPre()) {
                    if (target != null) {
                        if (AutoHeal.doSoup || AutoHeal.healing) return;
                        final float[] dstAngle = getRotations(target);
                        final float[] srcAngle = new float[]{serverAngles[0], serverAngles[1]};
                        serverAngles = smoothAngle(dstAngle, srcAngle);
                        event.setYaw(serverAngles[0]);
                        event.setPitch(serverAngles[1]);
                        if (timer.reach(1000 / randomNumber(9, 13)) && getDistance(prevRotations) < maxangle.getValue() && mc.currentScreen == null && canAttack) {
                            float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), mc.thePlayer.getCreatureAttribute());
                            if (sharpLevel > 0) mc.thePlayer.onEnchantmentCritical(target);
                            attack(target,false);
                            timer.reset();
                        }
                    } else if (target == null) {
                        serverAngles[0] = (mc.thePlayer.rotationYaw);
                        serverAngles[1] = (mc.thePlayer.rotationPitch);
                    }
                    if (autoblock.isEnabled() && !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                }
                break;
            case DYNAMIC:
                if (event.isPre()) {
                    target = getBestTarget(event.getYaw());
                    if (target != null) {
                        if (!isValid(target, false)) target = null;
                        final float[] rots = getRotations(target);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    }
                } else {
                    final long ping = mc.getCurrentServerData() == null ? 50 : Math.max(mc.getCurrentServerData().pingToServer, 90);
                    final int pingDelay = Math.round(ping / 10);
                    if (target != null) {
                        final long doDynamicTick = (mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()) != null && mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null) ? dynamicTickDelay() : 500;
                        if (target.hurtResistantTime <= 11 + pingDelay && timer.sleep(doDynamicTick + ((long) getLagComp()))) {
                            attack(target, criticals.isEnabled());
                        }
                        if (autoblock.isEnabled()&& !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    }
                }
                break;
            case SWITCH:
                if (event.isPre()) {
                    if (targets.size() > maxtargets.getValue()) targets.clear();
                    mc.theWorld.loadedEntityList.forEach(target1 -> {
                        if (target1 instanceof EntityLivingBase) {
                            EntityLivingBase ent = (EntityLivingBase) target1;
                            if (!targets.contains(target1) && isValid(ent, false) && targets.size() < maxtargets.getValue()) {
                                targets.add(ent);
                            }
                            if (targets.contains(target1) && (!isValid(ent, false))) {
                                targets.remove(ent);
                            }
                        }
                    });
                    if (switchIndex >= targets.size()) {
                        switchIndex = 0;
                    }
                    if (!targets.isEmpty()) {
                        sortTargets(event.getYaw());
                        target = targets.get(switchIndex);
                    }
                    if (target != null) {
                        if (!isValid(target, false)) {
                            target = null;
                            return;
                        }
                        final float rotations[] = getRotations(target);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                    }
                } else {
                    if (target != null) {
                        if (timer.sleep(1000 / speed.getValue())) {
                            attack(target, criticals.isEnabled());
                        }
                        if (autoblock.isEnabled()&& !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    }
                }
                if (target != null && targets.size() > 0 && switchtimer.sleep(switchspeed.getValue())) {
                    ++switchIndex;
                }
                break;
            case SINGLE:
                if (event.isPre()) {
                    target = getBestTarget(event.getYaw());
                    if (target != null) {
                        if (!isValid(target, false)) target = null;
                        final float[] rots = getRotations(target);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    }
                } else {
                    if (target != null) {
                        if (timer.sleep(1000 / speed.getValue())) {
                            attack(target, criticals.isEnabled());
                        }
                        if (autoblock.isEnabled() && !fakeab.isEnabled() && mc.thePlayer.getHeldItem() != null && mc.currentScreen == null && mc.thePlayer.getHeldItem().getItem() instanceof ItemSword)
                            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    }
                }
                break;
            case TELEPORT:
                if (event.isPre() && !Phase.phasing) {
                    target = getBestTargetTP(event.getYaw());
                    if (target != null) {
                        mc.thePlayer.motionY = 0;
                        float[] rots = getRotations(target);
                        event.setYaw(mc.thePlayer.ticksExisted % 3 == 0 ? rots[0] + (random.nextBoolean() ? 25 : -25) : rots[0] + (MathUtils.getRandomInRange(-500, 500) * 0.01F));
                        event.setPitch(mc.thePlayer.ticksExisted % 3 == 0 ? 90 : rots[1] + (MathUtils.getRandomInRange(-500, 500) * 0.01F));
                        if (resetpos.isEnabled()) {
                            if (timer.reach(150 + random.nextInt(200))) {
                                x = mc.thePlayer.posX;
                                y = mc.thePlayer.posY;
                                z = mc.thePlayer.posZ;
                                final double tpx = RenderUtil.interpolate(target.posX, target.lastTickPosX, mc.timer.renderPartialTicks);
                                final double tpy = RenderUtil.interpolate(target.posY, target.lastTickPosY, mc.timer.renderPartialTicks);
                                final double tpz = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, mc.timer.renderPartialTicks);
                                mc.thePlayer.setPosition(tpx + (MathUtils.getRandomInRange(-150, 150) * 0.01), tpy + 2, tpz + (MathUtils.getRandomInRange(-150, 150) * 0.01));
                                mc.thePlayer.swingItem();
                                mc.playerController.attackEntity(mc.thePlayer, target);
                                nigga = true;
                            }
                            if (nigga && switchtimer.reach(250)) {
                                nigga = false;
                                mc.thePlayer.setPosition(x, y, z);
                                timer.reset();
                                switchtimer.reset();
                            }
                        } else {
                            if (timer.sleep(150 + random.nextInt(150))) {
                                final double tpx = RenderUtil.interpolate(target.posX, target.lastTickPosX, mc.timer.renderPartialTicks);
                                final double tpy = RenderUtil.interpolate(target.posY, target.lastTickPosY, mc.timer.renderPartialTicks);
                                final double tpz = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, mc.timer.renderPartialTicks);
                                mc.thePlayer.setPosition(tpx + (MathUtils.getRandomInRange(-150, 150) * 0.01), tpy + 2, tpz + (MathUtils.getRandomInRange(-150, 150) * 0.01));
                                attack(target, criticals.isEnabled());
                            }
                        }
                    }
                    if (!isValidTP(target)) target = null;
                }
                break;
        }
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        TickRate.update(event);
        canAttack = !event.isSending() || !(event.getPacket() instanceof C0DPacketCloseWindow || event.getPacket() instanceof S2DPacketOpenWindow);
        // Bypass Aura check 15 on AGC and Inventory check 2
        if (event.isSending() && (event.getPacket() instanceof C03PacketPlayer.C05PacketPlayerLook || event.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
            final C03PacketPlayer packet = (C03PacketPlayer) event.getPacket();
            prevRotations[0] = packet.getYaw();
            prevRotations[1] = packet.getPitch();
        }

    }

    private double getArmorVal(EntityLivingBase ent) {
        if (ent instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) ent;
            double armorstrength = 0;
            for (int index = 3; index >= 0; index--) {
                ItemStack stack = player.inventory.armorInventory[index];
                if (stack != null) {
                    armorstrength += getArmorStrength(stack);
                }
            }
            return armorstrength;
        }
        return 0;
    }

    private boolean nearbyTargets() {
        for (Object e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase && isValid((EntityLivingBase) e, true)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFakeAutoBlocking() {
        return autoblock.isEnabled() && mc.thePlayer.getCurrentEquippedItem() != null && fakeab.isEnabled() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword && nearbyTargets();
    }

    private EntityLivingBase getBestTargetTP(float yaw) {
        targets.clear();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (isValidTP(ent)) {
                    targets.add(ent);
                }
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        sortTargets(yaw);
        return targets.get(0);
    }

    private EntityLivingBase getBestTarget(float yaw) {
        targets.clear();
        for (Entity e : mc.theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (isValid(ent, false)) {
                    targets.add(ent);
                }
            }
        }
        if (targets.isEmpty()) {
            return null;
        }
        sortTargets(yaw);
        return targets.get(0);
    }

    private long dynamicTickDelay() {
        final double[] nigger = {367, 374, 382};
        final double[] nigger2 = {398, 400, 405};
        if (target instanceof EntityPlayer) {
            if (target.hurtTime < 1 && mc.thePlayer.isSwingInProgress && mc.thePlayer.onGround) {
                if (mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() < mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getResponseTime()) {
                    dynamicTickCounter += 1;
                    if (dynamicTickCounter == 3 || dynamicTickCounter == 6 || dynamicTickCounter == 9) {
                        dynamicTickValue = (long) nigger[dynamicTickCounter / 3 - 1];
                    } else if (mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime() > mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getResponseTime()) {
                        dynamicTickCounter += 1;
                        if (dynamicTickCounter == 3 || dynamicTickCounter == 6 || dynamicTickCounter == 9) {
                            dynamicTickValue = (long) nigger2[dynamicTickCounter / 3 - 1];
                        }
                        if (dynamicTickCounter > 9) {
                            dynamicTickCounter = 0;
                        }
                    }
                }
            }
        }
        return dynamicTickValue;
    }

    @Override
    public void onEnable() {
        switchIndex = 0;
        nigga = false;
        if (mc.thePlayer == null) return;
        serverAngles[0] = mc.thePlayer.rotationYaw;
        serverAngles[1] = mc.thePlayer.rotationPitch;
    }

    @Override
    public void onDisable() {
        targets.clear();
        nigga = false;
        x = y = z = 0;
        target = null;
    }

    private double yawDist(EntityLivingBase e) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(mc.thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f)) % 360.0f;
        return (d > 180.0f) ? (360.0f - d) : d;
    }

    private double yawDistCycle(EntityLivingBase e, float yaw) {
        final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(mc.thePlayer.getPositionVector().addVector(0.0, mc.thePlayer.getEyeHeight(), 0.0));
        final double d = Math.abs(yaw - Math.atan2(difference.zCoord, difference.xCoord)) % 90.0f;
        return d;
    }

    private void sortTargets(float yaw) {
        switch (sortingmode.getValue()) {
            case DISTANCE:
                targets.sort(Comparator.comparingDouble(player -> mc.thePlayer.getDistanceToEntity(player)));
                break;
            case HEALTH:
                targets.sort(Comparator.comparingDouble(EntityLivingBase::getHealth));
                break;
            case FOV:
                targets.sort(Comparator.comparingDouble(this::yawDist));
                break;
            case CYCLE:
                targets.sort(Comparator.comparingDouble(player -> yawDistCycle(player, yaw)));
                break;
            case ARMOR:
                targets.sort(Comparator.comparingDouble(player -> getArmorVal(player)));
                break;
        }
    }

    private enum SortMode {
        FOV, DISTANCE, HEALTH, CYCLE, ARMOR
    }

    private float getLagComp() {
        return TickRate.TPS;
    }

    private boolean isValid(EntityLivingBase entity, boolean block) {
        final double d = (block && blockrange.getValue() > range.getValue()) ? blockrange.getValue() : range.getValue();
        return !AntiBot.getBots().contains(entity) && entity != null && mc.thePlayer != entity && ((entity instanceof EntityPlayer && players.getValue()) || (entity instanceof EntityAnimal && animals.isEnabled()) || ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue())) && entity.getDistanceSqToEntity(mc.thePlayer) <= d * d && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue()) && !Client.INSTANCE.getFriendManager().isFriend(entity.getName());
    }

    private boolean isValidTP(EntityLivingBase entity) {
        final double d = 7;
        return !AntiBot.getBots().contains(entity) && entity != null && mc.thePlayer != entity && ((entity instanceof EntityPlayer && players.getValue()) || (entity instanceof EntityAnimal && animals.isEnabled()) || ((entity instanceof EntityMob || entity instanceof EntitySlime) && mobs.getValue())) && entity.getDistanceSqToEntity(mc.thePlayer) <= d * d && entity.isEntityAlive() && (!entity.isInvisible() || invisibles.getValue()) && !Client.INSTANCE.getFriendManager().isFriend(entity.getName());
    }


    private void attack(final EntityLivingBase target, boolean crit) {
        mc.thePlayer.swingItem();
        if (crit && canCrit()) {
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.05, z, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.012511, z, false));
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
        }
        mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
    }


    private boolean canCrit() {
        return !mc.gameSettings.keyBindJump.isKeyDown() && mc.thePlayer.onGround && !Client.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !Client.INSTANCE.getModuleManager().getModule("flight").isEnabled();
    }


    private void swap(final int slot, final int hotbarNum) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, mc.thePlayer);
    }

    private float[] getRotations(EntityLivingBase ent) {
        final double x = ent.posX - mc.thePlayer.posX;
        double y = ent.posY - mc.thePlayer.posY;
        final double z = ent.posZ - mc.thePlayer.posZ;
        y /= mc.thePlayer.getDistanceToEntity(ent);
        final float yaw = (float) (-(Math.atan2(x, z) * 57.29577951308232));
        final float pitch = (float) (-(Math.asin(y) * 57.29577951308232));
        return new float[]{yaw, pitch};
    }

    private double getArmorStrength(final ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ItemArmor)) return -1;
        float damageReduction = ((ItemArmor) itemStack.getItem()).damageReduceAmount;
        Map enchantments = EnchantmentHelper.getEnchantments(itemStack);
        if (enchantments.containsKey(Enchantment.protection.effectId)) {
            int level = (int) enchantments.get(Enchantment.protection.effectId);
            damageReduction += Enchantment.protection.calcModifierDamage(level, DamageSource.generic);
        }
        return damageReduction;
    }


    private MovingObjectPosition tracePath(final World world, final float x, final float y, final float z, final float tx, final float ty, final float tz, final float borderSize, final HashSet<Entity> excluded) {
        Vec3 startVec = new Vec3(x, y, z);
        Vec3 endVec = new Vec3(tx, ty, tz);
        final float minX = (x < tx) ? x : tx;
        final float minY = (y < ty) ? y : ty;
        final float minZ = (z < tz) ? z : tz;
        final float maxX = (x > tx) ? x : tx;
        final float maxY = (y > ty) ? y : ty;
        final float maxZ = (z > tz) ? z : tz;
        final AxisAlignedBB bb = new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ).expand(borderSize, borderSize, borderSize);
        final List<Entity> allEntities = (List<Entity>) world.getEntitiesWithinAABBExcludingEntity(null, bb);
        MovingObjectPosition blockHit = world.rayTraceBlocks(startVec, endVec);
        startVec = new Vec3(x, y, z);
        endVec = new Vec3(tx, ty, tz);
        Entity closestHitEntity = null;
        float closestHit = Float.POSITIVE_INFINITY;
        float currentHit;
        for (final Entity ent : allEntities) {
            if (ent.canBeCollidedWith() && !excluded.contains(ent)) {
                final float entBorder = ent.getCollisionBorderSize();
                AxisAlignedBB entityBb = ent.getEntityBoundingBox();
                if (entityBb == null) {
                    continue;
                }
                entityBb = entityBb.expand(entBorder, entBorder, entBorder);
                final MovingObjectPosition intercept = entityBb.calculateIntercept(startVec, endVec);
                if (intercept == null) {
                    continue;
                }
                currentHit = (float) intercept.hitVec.distanceTo(startVec);
                if (currentHit >= closestHit && currentHit != 0.0f) {
                    continue;
                }
                closestHit = currentHit;
                closestHitEntity = ent;
            }
        }
        if (closestHitEntity != null) {
            blockHit = new MovingObjectPosition(closestHitEntity);
        }
        return blockHit;
    }

    private MovingObjectPosition tracePathD(final World w, final double posX, final double posY, final double posZ, final double v, final double v1, final double v2, final float borderSize, final HashSet<Entity> exclude) {
        return tracePath(w, (float) posX, (float) posY, (float) posZ, (float) v, (float) v1, (float) v2, borderSize, exclude);
    }

    private MovingObjectPosition rayCast(final EntityPlayerSP player, final double x, final double y, final double z) {
        final HashSet<Entity> excluded = new HashSet<>();
        excluded.add(player);
        return tracePathD(player.worldObj, player.posX, player.posY + player.getEyeHeight(), player.posZ, x, y, z, 1.0f, excluded);
    }


    private float getDistance(float[] original) {
        final float yaw = MathHelper.wrapAngleTo180_float(serverAngles[0]) - MathHelper.wrapAngleTo180_float(original[0]);
        final float pitch = MathHelper.wrapAngleTo180_float(serverAngles[1]) - MathHelper.wrapAngleTo180_float(original[1]);
        return (float) Math.sqrt(yaw * yaw + pitch * pitch);
    }

    private long randomNumber(long min, long max) {
        return (long) ((Math.random() * (max - min)) + min);
    }

    public float randomFloat(float min, float max) {
        return min + (this.random.nextFloat() * (max - min));
    }

    public float[] smoothAngle(float[] dst, float[] src) {
        float[] smoothedAngle = new float[2];
        smoothedAngle[0] = (src[0] - dst[0]);
        smoothedAngle[1] = (src[1] - dst[1]);
        smoothedAngle = MathUtils.constrainAngle(smoothedAngle);
        smoothedAngle[0] = (src[0] - smoothedAngle[0] / 100 * randomFloat(8, 20));
        smoothedAngle[1] = (src[1] - smoothedAngle[1] / 100 * randomFloat(0, 8));
        return smoothedAngle;
    }

    public enum Mode {
        SINGLE, SWITCH, DYNAMIC, TICK, SMOOTH,SAFETY, TELEPORT
    }
}
