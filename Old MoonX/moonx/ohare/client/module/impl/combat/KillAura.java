package moonx.ohare.client.module.impl.combat;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.event.impl.render.Render2DEvent;
import moonx.ohare.client.event.impl.render.Render3DEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.*;
import moonx.ohare.client.utils.font.MCFontRenderer;
import moonx.ohare.client.utils.value.impl.BooleanValue;
import moonx.ohare.client.utils.value.impl.EnumValue;
import moonx.ohare.client.utils.value.impl.NumberValue;
import moonx.ohare.client.utils.value.impl.RangedValue;
import net.minecraft.block.*;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import net.minecraft.util.Vec3;
import net.minecraft.util.*;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class KillAura extends Module {
    public static EntityLivingBase target;
    private List<EntityLivingBase> targets = new ArrayList<>();
    private RangedValue<Integer> cps = new RangedValue<>("CPS", 1, 20, 1, 7, 11);
    private NumberValue<Float> range = new NumberValue<>("Range", 4.2F, 1.0F, 7.0F, 0.1F);
    private NumberValue<Float> blockrange = new NumberValue<>("Block Range", 7.0F, 1.0F, 15.0F, 0.1F);
    private NumberValue<Integer> maxTargets = new NumberValue<>("Max Targets", 3, 1, 5, 1);
    private NumberValue<Integer> switchSpeed = new NumberValue<>("Switch Speed", 300, 100, 1000, 50);
    private BooleanValue players = new BooleanValue("Players", "Target Players", true);
    private BooleanValue animals = new BooleanValue("Animals", "Target Animals", false);
    private BooleanValue monsters = new BooleanValue("Monsters", "Target Monsters", false);
    private BooleanValue invisibles = new BooleanValue("Invisibles", "Target Invisibles", false);
    private BooleanValue autoblock = new BooleanValue("AutoBlock", "Automatically Block", true);
    private BooleanValue dynamic = new BooleanValue("Dynamic", "Dynamic Timing", true);
    private BooleanValue targetesp = new BooleanValue("Target ESP", "Target ESP", true);
    private BooleanValue targethud = new BooleanValue("Target HUD", "Target HUD", true);
    private BooleanValue teams = new BooleanValue("Teams", "Teams Mode", false);
    private EnumValue<sortmode> sortMode = new EnumValue<>("Sort Mode", sortmode.FOV);
    private EnumValue<mode> Mode = new EnumValue<>("Mode", mode.SINGLE);
    private BooleanValue dura = new BooleanValue("Dura", "Tick Dura", false, Mode, "TICK");
    private NumberValue<Integer> multitargets = new NumberValue<>("MultiTargets", 3, 1, 10, 1, Mode, "multi");
    private NumberValue<Integer> multifov = new NumberValue<>("MultiFOV", 75, 1, 180, 1, Mode, "multi");
    private TimerUtil timerUtil = new TimerUtil();
    private TimerUtil switchTimer = new TimerUtil();
    private long time;
    private boolean groundTicks;
    private float[] serverAngles = new float[2];
    private float[] prevRotations = new float[2];
    private int switchI;
    private float oldYaw;
    private final MCFontRenderer otherfont = new MCFontRenderer(new Font("Tahoma", Font.PLAIN, 12), true, true);

    public KillAura() {
        super("KillAura", Category.COMBAT, new Color(0xAF0E00).getRGB());
        setRenderLabel("Kill Aura");
    }

    @Override
    public void onDisable() {
        target = null;
        switchI = 0;
        getMc().timer.timerSpeed = 1f;
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        final Criticals criticals = (Criticals) Moonx.INSTANCE.getModuleManager().getModule("criticals");
        setSuffix(StringUtils.capitalize(Mode.getValue().name().toLowerCase()));
        final long ping = getMc().getCurrentServerData() == null ? 0 : Math.min(50, Math.max(getMc().getCurrentServerData().pingToServer, 110));
        final int pingDelay = Math.round(ping / 10);
        if (AutoHeal.doSoup || AutoHeal.healing || AutoApple.doingStuff || getMc().thePlayer.isSpectator()) return;
        if (event.isPre()) {
            event.setPitch(getMc().thePlayer.rotationPitch);
        }
        target = null;
        switch (Mode.getValue()) {
            case EXPERIMENTAL:
                target = getTarget();
                int attackKey = getMc().gameSettings.keyBindAttack.getKeyCode();
                int blockKey = getMc().gameSettings.keyBindUseItem.getKeyCode();
                if (event.isPre()) {
                    if (target != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        float sens = CombatUtil.getSensitivityMultiplier();
                        float yaw = rots[0] + MathUtils.getRandomInRange(-0.55F, 0.55F);
                        float pitch = rots[1] + MathUtils.getRandomInRange(-1.5F, 1.5F);
                        float yawGCD = (Math.round(yaw / sens) * sens);
                        float pitchGCD = (Math.round(pitch / sens) * sens);
                        if (Math.abs(pitchGCD) > 90) {
                            pitchGCD = 90;
                        }
                        if (Math.abs(pitchGCD) == Math.round(Math.abs(pitchGCD))) {
                            pitchGCD += (Math.round(Math.random() * 2 / sens) * sens);
                        }
                        //event.setYaw(yawGCD);
                        //event.setPitch(pitchGCD);
                        getMc().thePlayer.LegitRotation(yawGCD, pitchGCD, 1, true);
                        if (!getMc().thePlayer.isBlocking()) {
                            if (getMc().thePlayer.ticksExisted % (1 + MathUtils.getRandomInRange(1, 2)) == 0 && target.hurtResistantTime <= 17) {
                                if (timerUtil.sleep(1000 / getCPS())) {
                                    KeyBinding.setKeyBindState(attackKey, true);
                                    KeyBinding.setKeyBindState(attackKey, false);
                                    KeyBinding.onTick(attackKey);
                                }
                            }
                        }
                        if (canBlock() && autoblock.isEnabled() && (getMc().thePlayer.ticksExisted % (1 + MathUtils.getRandomInRange(1, 2)) != 0 || getMc().thePlayer.hurtResistantTime <= 15) && getMc().thePlayer.getDistanceToEntity(target) < 5) {
                            KeyBinding.setKeyBindState(blockKey, true);
                            KeyBinding.setKeyBindState(blockKey, false);
                            KeyBinding.onTick(blockKey);
                        }
                    } else timerUtil.reset();
                }
                break;
            case AAC:
                target = getTarget();
                if (event.isPre()) {
                    if (target != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        float sens = CombatUtil.getSensitivityMultiplier();
                        float yaw = rots[0] + MathUtils.getRandomInRange(-0.15F, 0.15F);
                        float pitch = rots[1] + MathUtils.getRandomInRange(-0.15F, 0.15F);
                        float yawGCD = (Math.round(yaw / sens) * sens);
                        float pitchGCD = (Math.round(pitch / sens) * sens);
                        if (Math.abs(pitchGCD) == Math.round(Math.abs(pitchGCD))) {
                            pitchGCD += (Math.round(Math.random() * 2 / sens) * sens);
                        }
                        if (Math.abs(pitchGCD) > 90) {
                            pitchGCD = 90;
                        }
                        if (getMc().thePlayer.onGround && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.motionY <= 0) {
                            if (getMc().thePlayer.isSprinting() && Math.abs(getMc().thePlayer.rotationYaw - yawGCD) > 45) {
                                getMc().thePlayer.motionX /= 1.3F;
                                getMc().thePlayer.motionZ /= 1.3F;
                            }
                            getMc().thePlayer.setSprinting(false);
                        }
                        event.setYaw(yawGCD);
                        event.setPitch(pitchGCD);
                        Object[] rayTarget = CombatUtil.getEntityCustom(pitchGCD, yawGCD, range.getValue(), 0.05, 0);
                        if (rayTarget != null) {
                            if (rayTarget[0] == target) {
                                if (timerUtil.sleep(newgetCPS()) && target.hurtResistantTime < 17 && switchTimer.reach(75)) {
                                    attackEntity(target, false);
                                } else {
                                    if (canBlock() && nearbyTargets(true) && getMc().thePlayer.ticksExisted % 5 == 0) {
                                        getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
                                    }
                                }
                            }
                        }
                    } else timerUtil.reset();
                }
                break;
            case CUBECRAFT:
                target = getTarget();
                if (event.isPre()) {
                    if (target != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        float yaw = rots[0] + MathUtils.getRandomInRange(-1.8F, 1.8F);
                        float pitch = rots[1] + MathUtils.getRandomInRange(-6.8F, 6.8F);
                        float sens = CombatUtil.getSensitivityMultiplier();
                        float yawGCD = (Math.round(yaw / sens) * sens);
                        float pitchGCD = (Math.round(pitch / sens) * sens);
                        if (Math.abs(pitchGCD) > 90) {
                            pitchGCD = 90;
                        }
                        event.setYaw(yawGCD);
                        event.setPitch(pitchGCD);
                        // event.setPitch(getMc().thePlayer.ticksExisted % 3 != 0 ? 90 : -90);
                        //event.setPitch(getMc().thePlayer.ticksExisted % 3 != 0 ? 90 : -90);
                        if (dynamic.isEnabled()) {
                            if (target.hurtResistantTime == 0) {
                                if (timerUtil.sleep(ping * 3)) attackEntity(target, false);
                            } else if (target.hurtResistantTime <= 9 + pingDelay) attackEntity(target, false);
                        } else if (timerUtil.reach(1000 / getCPS())) {
                            timerUtil.reset();
                            attackEntity(target, false);
                        }
                    } else timerUtil.reset();
                }
                if (!event.isPre() && canBlock() && nearbyTargets(true)) {
                    getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
                }
                break;
            case SINGLE:
                target = getTarget();
                if (event.isPre()) {
                    if (target != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    } else timerUtil.reset();
                } else {
                    if (target != null) {
                        if (dynamic.isEnabled()) {
                            if (target.hurtResistantTime == 0) {
                                if (timerUtil.sleep(ping * 3)) attackEntity(target, false);
                            } else if (target.hurtResistantTime <= 9 + pingDelay) attackEntity(target, false);
                        } else if (timerUtil.sleep(1000 / getCPS())) attackEntity(target, false);
                    } else timerUtil.reset();
                    if (canBlock() && nearbyTargets(true))
                        getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
                }
                break;
            case TICK:
                target = getTarget();
                if (event.isPre()) {
                    lowerTicks();
                    if (target != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    }
                } else {
                    if (target != null) {
                        if (isValidTicks(target)) {
                            if (!dura.isEnabled()) {
                                getMc().thePlayer.swingItem();
                                getMc().thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                                getMc().thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                                getMc().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                                getMc().thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
                                target.auraticks = 10;
                            } else {
                                getMc().thePlayer.swingItem();
                                swap(9, getMc().thePlayer.inventory.currentItem);
                                attackEntity(target, false);
                                crit();
                                attackEntity(target, true);
                                swap(9, getMc().thePlayer.inventory.currentItem);
                                attackEntity(target, false);
                                crit();
                                attackEntity(target, true);
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
                                target.auraticks = 10;
                            }
                        }
                    }
                    if (canBlock() && nearbyTargets(true)) {
                        getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
                    }
                }
                break;
            case SWITCH:
                if (event.isPre()) {
                    if (target != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                    }
                }
                if (!event.isPre()) {
                    final ArrayList<EntityLivingBase> targs = new ArrayList<>();
                    getMc().theWorld.getLoadedEntityList().stream().filter(entity -> entity instanceof EntityLivingBase).filter(entity -> isTargetable((EntityLivingBase) entity, getMc().thePlayer, false)).forEach(potentialTarget -> {
                        if (targs.size() < maxTargets.getValue()) {
                            targs.add((EntityLivingBase) potentialTarget);
                        }
                    });
                    if (switchTimer.sleep(switchSpeed.getValue()) && !targs.isEmpty()) {
                        if (switchI + 1 > targs.size() - 1 || targs.size() < 2) {
                            switchI = 0;
                        } else {
                            switchI++;
                        }
                    }
                    if (!targs.isEmpty()) target = targs.get(Math.min(switchI, targs.size() - 1));
                    if (target != null) {
                        if (!isTargetable(target, getMc().thePlayer, false)) target = null;
                    }
                    if (target != null && getMc().thePlayer != null) {
                        final float[] rots = getRotationsToEnt(target, getMc().thePlayer);
                        event.setYaw(rots[0]);
                        event.setPitch(rots[1]);
                        if (dynamic.isEnabled()) {
                            if (target.hurtResistantTime == 0) {
                                if (timerUtil.sleep(ping * 3)) attackEntity(target, false);
                            } else if (target.hurtResistantTime <= 9 + pingDelay) attackEntity(target, false);
                        } else if (timerUtil.sleep(1000 / getCPS())) attackEntity(target, false);
                    } else {
                        timerUtil.reset();
                    }
                }
                if (!event.isPre() && canBlock() && nearbyTargets(true)) {
                    getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
                }
                break;
            case MULTI:
                target = findMostCrowdedEntity();
                if (event.isPre()) {
                    if (target != null) {
                        final float[] rotations = getRotationsToEnt(target, getMc().thePlayer);
                        event.setYaw(rotations[0]);
                        event.setPitch(rotations[1]);
                        oldYaw = event.getYaw();
                    }
                } else {
                    if (target != null) {
                        targets = getMultiTargets();
                        if (timerUtil.sleep(1000 / getCPS() + (targets.size() - 1) * pingDelay)) {
                            attackEntity(target, false);
                            targets.stream().filter(t -> t.hurtResistantTime <= 9 + pingDelay).forEach(t -> {
                                if (t != target) {
                                    final float[] rotations = getRotationsToEnt(t, getMc().thePlayer);
                                    getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C05PacketPlayerLook(rotations[0], rotations[1], event.isOnGround()));
                                    attackEntity(t, false);
                                }
                            });
                        }
                    }
                    if (canBlock() && nearbyTargets(true)) {
                        getMc().playerController.sendUseItem(getMc().thePlayer, getMc().theWorld, getMc().thePlayer.getHeldItem());
                    }
                }
                break;
            case SMOOTH:
                target = getTarget();
                if (event.isPre()) {
                    if (target != null) {
                        if (criticals.isEnabled() && getMc().thePlayer.onGround && !isInsideBlock()) {
                            event.setY(event.getY() + 0.01);
                            event.setOnGround(false);
                        }
                        final float[] dstAngle = getRotationsToEnt(target, getMc().thePlayer);
                        final float[] srcAngle = new float[]{serverAngles[0], serverAngles[1]};
                        serverAngles = smoothAngle(dstAngle, srcAngle);
                        event.setYaw(serverAngles[0]);
                        event.setPitch(serverAngles[1]);
                        if (getDistance(prevRotations) < 16 && !getMc().thePlayer.isBlocking()) {
                            if (dynamic.isEnabled()) {
                                if (target.hurtResistantTime == 0) {
                                    if (timerUtil.sleep(ping * 3)) attackEntity(target, false);
                                } else if (target.hurtResistantTime <= 9 + pingDelay) attackEntity(target, false);
                            } else if (timerUtil.sleep(1000 / getCPS())) attackEntity(target, false);
                        }
                    } else {
                        serverAngles[0] = (getMc().thePlayer.rotationYaw);
                        serverAngles[1] = (getMc().thePlayer.rotationPitch);
                        timerUtil.reset();
                    }
                }
                break;
        }
    }

    @Handler
    public void onRender2D(Render2DEvent event) {
        float x = (event.getScaledResolution().getScaledWidth() >> 1) - 5;
        float y = (event.getScaledResolution().getScaledHeight() >> 1) + 120;
        if (targethud.isEnabled() && target != null) {
            if (getMc().thePlayer != null && target instanceof EntityPlayer) {
                NetworkPlayerInfo networkPlayerInfo = getMc().getNetHandler().getPlayerInfo(target.getUniqueID());
                final String ping = "Ping: " + (Objects.isNull(networkPlayerInfo) ? "0ms" : networkPlayerInfo.getResponseTime() + "ms");
                final String playerName = "Name: " + net.minecraft.util.StringUtils.stripControlCodes(target.getName());
                RenderUtil.drawBorderedRect(x, y, 140, 45, 0.5, new Color(0, 0, 0, 255).getRGB(), new Color(0, 0, 0, 90).getRGB());
                RenderUtil.drawRect(x, y, 45, 45, new Color(0, 0, 0).getRGB());
                otherfont.drawStringWithShadow(playerName, x + 46.5, y + 4, -1);
                otherfont.drawStringWithShadow("Distance: " + MathUtils.round(getMc().thePlayer.getDistanceToEntity(target), 2), x + 46.5, y + 12, -1);
                otherfont.drawStringWithShadow(ping, x + 46.5, y + 28, new Color(0x5D5B5C).getRGB());
                otherfont.drawStringWithShadow("Health: " + MathUtils.round((Float.isNaN(target.getHealth()) ? 20 : target.getHealth()) / 2, 2), x + 46.5, y + 20, getHealthColor(target));
                drawFace(x + 0.5, y + 0.5, 8, 8, 8, 8, 44, 44, 64, 64, (AbstractClientPlayer) target);
                RenderUtil.drawBorderedRect(x + 46, y + 45 - 10, 92, 8, 0.5, new Color(0).getRGB(), new Color(35, 35, 35).getRGB());
                double inc = 91 / target.getMaxHealth();
                double end = inc * (Math.min(target.getHealth(), target.getMaxHealth()));
                RenderUtil.drawRect(x + 46.5, y + 45 - 9.5, end, 7, getHealthColor(target));
            }
        }
    }

    @Handler
    public void onRender3D(Render3DEvent event) {
        if (targetesp.isEnabled()) {
            if (Mode.getValue() == mode.MULTI) {
                targets.forEach(target2 -> {
                    if (isTargetable(target2, getMc().thePlayer, false)) {
                        final double x = RenderUtil.interpolate(target2.posX, target2.lastTickPosX, event.getPartialTicks());
                        final double y = RenderUtil.interpolate(target2.posY, target2.lastTickPosY, event.getPartialTicks());
                        final double z = RenderUtil.interpolate(target2.posZ, target2.lastTickPosZ, event.getPartialTicks());
                        drawEntityESP(x - getMc().getRenderManager().getRenderPosX(), y + target2.height + 0.1 - target2.height - getMc().getRenderManager().getRenderPosY(), z - getMc().getRenderManager().getRenderPosZ(), target2.height, 0.65, new Color(target2.hurtTime > 0 ? 0xE33726 : RenderUtil.getRainbow(4000, 0, 0.85f)));
                    }
                });
            } else if (target != null) {
                final double x = RenderUtil.interpolate(target.posX, target.lastTickPosX, event.getPartialTicks());
                final double y = RenderUtil.interpolate(target.posY, target.lastTickPosY, event.getPartialTicks());
                final double z = RenderUtil.interpolate(target.posZ, target.lastTickPosZ, event.getPartialTicks());
                drawEntityESP(x - getMc().getRenderManager().getRenderPosX(), y + target.height + 0.1 - target.height - getMc().getRenderManager().getRenderPosY(), z - getMc().getRenderManager().getRenderPosZ(), target.height, 0.65, new Color(target.hurtTime > 0 ? 0xE33726 : RenderUtil.getRainbow(4000, 0, 0.85f)));
            }
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {
        final Criticals criticals = (Criticals) Moonx.INSTANCE.getModuleManager().getModule("criticals");
        if (event.isSending() && (event.getPacket() instanceof C03PacketPlayer)) {
            if (groundTicks) {
                event.setCanceled(true);
                groundTicks = false;
            }
        }
        if (event.isSending() && event.getPacket() instanceof C03PacketPlayer) {
            prevRotations[0] = ((C03PacketPlayer) event.getPacket()).getYaw();
            prevRotations[1] = ((C03PacketPlayer) event.getPacket()).getPitch();
        }
        if (event.isSending() && (event.getPacket() instanceof C0APacketAnimation)) {
            if (criticals.isEnabled() && target != null && criticals.Mode.getValue() != Criticals.mode.AREA51 && !(Mode.getValue() == mode.TICK && dura.isEnabled()))
                crit();
        }
    }

    private List<EntityLivingBase> getMultiTargets() {
        final List<EntityLivingBase> entities = new ArrayList<>();
        int targets = 0;
        for (Entity entity : getMc().theWorld.getLoadedEntityList()) {
            if (targets >= multitargets.getValue()) {
                break;
            }
            if (entity instanceof EntityLivingBase) {
                final EntityLivingBase living = (EntityLivingBase) entity;
                if (isTargetable(living, getMc().thePlayer, false) && isWithinFOV(living, oldYaw, multifov.getValue())) {
                    entities.add(living);
                    ++targets;
                }
            }
        }
        return entities;
    }

    private EntityLivingBase findMostCrowdedEntity() {
        List<EntityLivingBase> entities = new ArrayList();
        for (Entity entity : getMc().theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                entities.add((EntityLivingBase) entity);
            }
        }
        EntityLivingBase best = null;
        int numBestEntities = -1;
        for (EntityLivingBase e : entities) {
            if (isTargetable(e, getMc().thePlayer, false)) {
                int closeEntities = 0;
                final float yaw = getRotationsToEnt(e, getMc().thePlayer)[0];
                for (EntityLivingBase e1 : entities) {
                    if (isTargetable(e1, getMc().thePlayer, false) && isWithinFOV(e1, yaw, multifov.getValue())) {
                        ++closeEntities;
                    }
                }
                if (closeEntities > numBestEntities) {
                    numBestEntities = closeEntities;
                    best = e;
                }
            }
        }
        return best;
    }

    private boolean isWithinFOV(EntityLivingBase entity, final float yaw, final double fov) {
        final float[] rotations = getRotationsToEnt(entity, getMc().thePlayer);
        final float yawDifference = getYawDifference(yaw % 360.0f, rotations[0]);
        return yawDifference < fov && yawDifference > -fov;
    }

    private float getYawDifference(final float currentYaw, final float neededYaw) {
        float yawDifference = neededYaw - currentYaw;
        if (yawDifference > 180.0f) {
            yawDifference = -(360.0f - neededYaw + currentYaw);
        } else if (yawDifference < -180.0f) {
            yawDifference = 360.0f - currentYaw + neededYaw;
        }
        return yawDifference;
    }


    public boolean isTeammate(EntityPlayer target) {
        if (!teams.isEnabled()) return false;
        boolean teamChecks = false;
        EnumChatFormatting myCol = null;
        EnumChatFormatting enemyCol = null;
        if (target != null) {
            for (EnumChatFormatting col : EnumChatFormatting.values()) {
                if (col == EnumChatFormatting.RESET)
                    continue;
                if (getMc().thePlayer.getDisplayName().getFormattedText().contains(col.toString()) && myCol == null) {
                    myCol = col;
                }
                if (target.getDisplayName().getFormattedText().contains(col.toString()) && enemyCol == null) {
                    enemyCol = col;
                }
            }
            try {
                if (myCol != null && enemyCol != null) {
                    teamChecks = myCol != enemyCol;
                } else {
                    if (getMc().thePlayer.getTeam() != null) {
                        teamChecks = !getMc().thePlayer.isOnSameTeam(target);
                    } else {
                        if (getMc().thePlayer.inventory.armorInventory[3].getItem() instanceof ItemBlock) {
                            teamChecks = !ItemStack.areItemStacksEqual(getMc().thePlayer.inventory.armorInventory[3], target.inventory.armorInventory[3]);
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        }
        return teamChecks;
    }

    private void drawFace(double x, double y, float u, float v, int uWidth, int vHeight, int width, int height, float tileWidth, float tileHeight, AbstractClientPlayer target) {
        try {
            ResourceLocation skin = target.getLocationSkin();
            getMc().getTextureManager().bindTexture(skin);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glColor4f(1, 1, 1, 1);
            Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(GL11.GL_BLEND);
        } catch (Exception ignored) {
        }
    }

    private int getHealthColor(EntityLivingBase player) {
        float f = player.getHealth();
        float f1 = player.getMaxHealth();
        float f2 = Math.max(0.0F, Math.min(f, f1) / f1);
        return Color.HSBtoRGB(f2 / 3.0F, 1.0F, 0.75F) | 0xFF000000;
    }

    private void drawEntityESP(double x, double y, double z, double height, double width, Color color) {
        GL11.glPushMatrix();
        GLUtil.setGLCap(3042, true);
        GLUtil.setGLCap(3553, false);
        GLUtil.setGLCap(2896, false);
        GLUtil.setGLCap(2929, false);
        GL11.glDepthMask(false);
        GL11.glLineWidth(1.8f);
        GL11.glBlendFunc(770, 771);
        GLUtil.setGLCap(2848, true);
        GL11.glDepthMask(true);
        RenderUtil.BB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
        RenderUtil.OutlinedBB(new AxisAlignedBB(x - width + 0.25, y, z - width + 0.25, x + width - 0.25, y + height, z + width - 0.25), 1, color.getRGB());
        GLUtil.revertAllCaps();
        GL11.glPopMatrix();
        GL11.glColor4f(1, 1, 1, 1);
    }

    private List<EntityLivingBase> loadedLivingLowTicks() {
        List<EntityLivingBase> toreturn = new ArrayList();
        for (Entity entity : getMc().theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                toreturn.add((EntityLivingBase) entity);
            }
        }
        toreturn.sort(Comparator.comparingInt(e -> e.auraticks));
        return toreturn;
    }

    private boolean nearbyTargets(boolean block) {
        for (Object e : getMc().theWorld.loadedEntityList) {
            if (e instanceof EntityLivingBase && isTargetable((EntityLivingBase) e, getMc().thePlayer, block)) {
                return true;
            }
        }
        return false;
    }

    private boolean canBlock() {
        return autoblock.isEnabled() && getMc().thePlayer.getHeldItem() != null && getMc().thePlayer.getHeldItem().getItem() instanceof ItemSword;
    }


    private void crit() {
        final Criticals criticals = (Criticals) Moonx.INSTANCE.getModuleManager().getModule("criticals");
        final double[] HYPIXELOFFSETS = {0.062f + 1.0E-5F, 0.001f + 1.0E-5F, 0.062f + 1.0E-5F, 0.051f};
        final float[] NCPOFFSETS = {0.0624f, 0.0f, 1.13E-4F, 0.0f};

        if (!(MathUtils.getBlockUnderPlayer(getMc().thePlayer, 0.06) instanceof BlockStairs) && canCrit() && !(MathUtils.getBlockUnderPlayer(getMc().thePlayer, 0.06) instanceof BlockSlab)) {
            if (criticals.Mode.getValue() == Criticals.mode.HYPIXEL) {
                double delay = 95;
                if (target.hurtResistantTime == 0) {
                    delay = 425;
                }
                if (System.currentTimeMillis() - time >= delay && target.hurtResistantTime <= 12) {
                    for (double offset : HYPIXELOFFSETS) {
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + offset, getMc().thePlayer.posZ, false));
                    }
                    groundTicks = true;
                    time = System.currentTimeMillis();
                }
            } else if (criticals.Mode.getValue() == Criticals.mode.NCP) {
                if (canCrit() && target.hurtResistantTime <= 13) {
                    for (double offset : NCPOFFSETS) {
                        getMc().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(getMc().thePlayer.posX, getMc().thePlayer.posY + offset, getMc().thePlayer.posZ, false));
                    }
                }
            }
        }
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

    private float getDistance(float[] original) {
        final float yaw = MathHelper.wrapAngleTo180_float(serverAngles[0]) - MathHelper.wrapAngleTo180_float(original[0]);
        final float pitch = MathHelper.wrapAngleTo180_float(serverAngles[1]) - MathHelper.wrapAngleTo180_float(original[1]);
        return (float) Math.sqrt(yaw * yaw + pitch * pitch);
    }

    private void attackEntity(Entity entity, boolean dura) {
        if (canBlock()) {
            getMc().playerController.syncCurrentPlayItem();
            getMc().thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        }
        if (dura) {
            getMc().thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
            getMc().thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(target, C02PacketUseEntity.Action.ATTACK));
        } else {
            getMc().thePlayer.swingItem();
            getMc().thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
        }
        float f1 = 0.0f;
        final boolean flag = getMc().thePlayer.fallDistance > 0.0f && !getMc().thePlayer.onGround && !getMc().thePlayer.isOnLadder() && !getMc().thePlayer.isInWater() && !getMc().thePlayer.isPotionActive(Potion.blindness) && getMc().thePlayer.ridingEntity == null && target instanceof EntityLivingBase;
        if (target != null) {
            f1 = EnchantmentHelper.func_152377_a(getMc().thePlayer.getHeldItem(), target.getCreatureAttribute());
        } else {
            f1 = EnchantmentHelper.func_152377_a(getMc().thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
        }
        if (flag) {
            getMc().thePlayer.onCriticalHit(target);
        }
        if (f1 > 0.0f) {
            getMc().thePlayer.onEnchantmentCritical(target);
        }
    }

    private boolean canCrit() {
        return getMc().thePlayer.onGround && !Moonx.INSTANCE.getModuleManager().getModule("speed").isEnabled() && !getMc().gameSettings.keyBindJump.isKeyDown() && getMc().thePlayer.fallDistance == 0;
    }

    private int newgetCPS() {
        double range = MathUtils.getRandomInRange(cps.getLeftVal(), cps.getRightVal());
        range = 20 / range;
        if (getMc().thePlayer.ticksExisted % 3 != 0) {
            range += Math.round(MathUtils.getRandomInRange(-1.75, 1.75));
        }
        if (getMc().thePlayer.ticksExisted % 27 == 0) {
            range += MathUtils.getRandomInRange(1, 6);
        }
        range = Math.round(Math.max(range, 1));
        int result = (int) range * 50;
        return result;
    }

    private int getCPS() {
        return MathUtils.getRandomInRange(cps.getLeftVal(), cps.getRightVal());
    }

    private void swap(final int slot, final int hotbarNum) {
        getMc().playerController.windowClick(getMc().thePlayer.inventoryContainer.windowId, slot, hotbarNum, 2, getMc().thePlayer);
    }

    private EntityLivingBase getTarget() {
        targets.clear();
        double Dist = Double.MAX_VALUE;
        if (getMc().theWorld != null) {
            for (Object object : getMc().theWorld.loadedEntityList) {
                if ((object instanceof EntityLivingBase)) {
                    EntityLivingBase e = (EntityLivingBase) object;
                    if ((getMc().thePlayer.getDistanceToEntity(e) < Dist)) {
                        if (isTargetable(e, getMc().thePlayer, false)) {
                            targets.add(e);
                        }
                    }
                }
            }
        }
        if (targets.isEmpty()) return null;
        switch (sortMode.getValue()) {
            case FOV:
                targets.sort(Comparator.comparingDouble(target -> yawDist((EntityLivingBase) target)));
                break;
            case HEALTH:
                targets.sort(Comparator.comparingDouble(target -> ((EntityLivingBase) target).getHealth()));
                break;
            case DISTANCE:
                targets.sort(Comparator.comparingDouble(target -> getMc().thePlayer.getDistanceToEntity(target)));
                break;
        }
        return targets.get(0);
    }

    private boolean isValidTicks(EntityLivingBase target) {
        return target.auraticks == 0 && isTargetable(target, getMc().thePlayer, false);
    }

    private void lowerTicks() {
        getMc().theWorld.getLoadedEntityList().forEach(e -> {
            if (e instanceof EntityLivingBase) {
                final EntityLivingBase living = (EntityLivingBase) e;
                if (living.auraticks > 0) {
                    --living.auraticks;
                }
            }
        });
    }

    private double yawDist(EntityLivingBase e) {
        if (e != null) {
            final Vec3 difference = e.getPositionVector().addVector(0.0, e.getEyeHeight() / 2.0f, 0.0).subtract(getMc().thePlayer.getPositionVector().addVector(0.0, getMc().thePlayer.getEyeHeight(), 0.0));
            final double d = Math.abs(getMc().thePlayer.rotationYaw - (Math.toDegrees(Math.atan2(difference.zCoord, difference.xCoord)) - 90.0f)) % 360.0f;
            return (d > 180.0f) ? (360.0f - d) : d;
        }
        return 0;
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

    private boolean isTargetable(EntityLivingBase entity, EntityPlayerSP clientPlayer, boolean b) {
        return entity.getUniqueID() != clientPlayer.getUniqueID() && entity.isEntityAlive() && !(entity instanceof EntityPlayer && isTeammate((EntityPlayer) entity)) && !AntiBot.getBots().contains(entity) && !Moonx.INSTANCE.getFriendManager().isFriend(entity.getName()) && !(entity.isInvisible() && !invisibles.isEnabled()) && (clientPlayer.getDistanceToEntity(entity) <= (b ? blockrange.getValue() : range.getValue()) || Mode.getValue() == mode.AAC && clientPlayer.getDistanceToEntity(entity) <= (b ? blockrange.getValue() : 6)) && (entity instanceof EntityPlayer && players.isEnabled() || (entity instanceof EntityMob || entity instanceof EntityGolem) && monsters.isEnabled() || ((entity instanceof EntityVillager || entity instanceof EntityAnimal) && animals.isEnabled()));
    }

    private boolean isInsideBlock() {
        for (int x = MathHelper.floor_double(getMc().thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(getMc().thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int y = MathHelper.floor_double(getMc().thePlayer.getEntityBoundingBox().minY); y < MathHelper.floor_double(getMc().thePlayer.getEntityBoundingBox().maxY) + 1; y++) {
                for (int z = MathHelper.floor_double(getMc().thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(getMc().thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                    Block block = getMc().theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if ((block != null) && (!(block instanceof BlockAir))) {
                        AxisAlignedBB boundingBox = block.getCollisionBoundingBox(getMc().theWorld, new BlockPos(x, y, z), getMc().theWorld.getBlockState(new BlockPos(x, y, z)));
                        if ((block instanceof BlockHopper)) {
                            boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                        }
                        if ((boundingBox != null) && (getMc().thePlayer.getEntityBoundingBox().intersectsWith(boundingBox))) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private enum sortmode {
        FOV, HEALTH, DISTANCE
    }

    private enum mode {
        SINGLE, SWITCH, TICK, SMOOTH, CUBECRAFT, AAC, EXPERIMENTAL, MULTI
    }
}
