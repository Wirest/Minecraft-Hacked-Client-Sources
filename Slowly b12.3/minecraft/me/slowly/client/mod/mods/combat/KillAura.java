package me.slowly.client.mod.mods.combat;

import com.darkmagician6.eventapi.EventTarget;
import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;	
import me.slowly.client.events.EventPostMotion;
import me.slowly.client.events.EventPreMotion;
import me.slowly.client.events.EventRender;
import me.slowly.client.mod.Mod;
import me.slowly.client.mod.ModManager;
import me.slowly.client.mod.mods.misc.Teams;
import me.slowly.client.mod.mods.world.AntiBots;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.Colors;
import me.slowly.client.util.CombatUtil;
import me.slowly.client.util.FlatColors;
import me.slowly.client.util.RayTraceUtil;
import me.slowly.client.util.RenderUtil;
import me.slowly.client.util.TimeHelper;
import me.slowly.client.util.friendmanager.FriendManager;
import me.slowly.client.value.Value;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.Timer;
import org.lwjgl.opengl.GL11;

public class KillAura
extends Mod {
    TimeHelper kms = new TimeHelper();
    public ArrayList<EntityLivingBase> targets = new ArrayList();
    public ArrayList<EntityLivingBase> attackedTargets = new ArrayList();
    public static EntityLivingBase curTarget = null;
    public static EntityLivingBase curBot = null;
    public static EntityLivingBase lastTarget = null;
    public static Value<Boolean> autoBlock = new Value<Boolean>("KillAura_AutoBlock", true);
    public static Value<Double> reach = new Value<Double>("KillAura_Reach", 3.85, 0.01, 7.00,0.01);
    public Value<Double> attackDelay = new Value<Double>("KillAura_HurtDelay", 120.0, 0.0, 300.0, 1.0);
    public Value<Double> cracksize = new Value<Double>("KillAura_CrackSize", 1.0, 1.0, 10.0, 1.0);
    public static Value<Boolean> gomme = new Value<Boolean>("KillAura_Gomme", true);
    public static Value<Boolean> slow = new Value<Boolean>("KillAura_SlowDown", false);
    public Value<Boolean> attackRandomDelay = new Value<Boolean>("KillAura_Random", true);
    public Value<Boolean> attackPlayers = new Value<Boolean>("KillAura_Players", true);
    public Value<Boolean> attackAnimals = new Value<Boolean>("KillAura_Animals", true);
    public Value<Boolean> attackMobs = new Value<Boolean>("KillAura_Mobs", true);
    public Value<Boolean> aRayTrace = new Value<Boolean>("KillAura_RayCast", true);
    public Value<Boolean> blockRayTrace = new Value<Boolean>("KillAura_BlockRayTrace", true);
    public Value<Boolean> rotations = new Value<Boolean>("KillAura_HeadRotations", true);
    public Value<Double> switchDelay = new Value<Double>("KillAura_SwitchDelay", 2.0, 0.0, 10.0, 1.0);
    public Value<Double> maxTargets = new Value<Double>("KillAura_MaxTargets", 2.0, 1.0, 20.0, 1.0);
    public Value<Boolean> noswing = new Value<Boolean>("Killaura_NoSwing", false);
    public Value<Boolean> startDelay = new Value<Boolean>("KillAura_StartDelay", true);
    public Value<Boolean> openInv = new Value<Boolean>("KillAura_InventoryHit", true);
    public Value<Boolean> invisible = new Value<Boolean>("KillAura_Invisibles", false);
    public Value<String> espMode = new Value("KillAura", "ESP", 0);
    private TimeHelper test = new TimeHelper();
    private boolean doBlock = false;
    private boolean unBlock = false;
    private Random random = new Random();
    private long lastMs;
    private int delay = 0;
    private float curYaw = 0.0f;
    private float curPitch = 0.0f;
    private int tick = 0;
    private float lastYaw;

    public KillAura() {
        super("KillAura", Mod.Category.COMBAT, Colors.DARKRED.c);
        this.showValue = reach;
        this.espMode.addValue("None");
        this.espMode.addValue("Box");
        this.espMode.addValue("Flat Box");
    }
    

    @EventTarget
    public void onRender(final EventRender render) {
        this.setColor(-65536);
        if (KillAura.curTarget == null || this.espMode.isCurrentMode("None")) {
            return;
        }
        Color color = new Color(Colors.BLUE.c);
        if (KillAura.curTarget.hurtTime > 0) {
            color = new Color(FlatColors.RED.c);
        }
        if (this.espMode.isCurrentMode("Box")) {
            this.mc.getRenderManager();
            double x = KillAura.curTarget.lastTickPosX + (KillAura.curTarget.posX - KillAura.curTarget.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = KillAura.curTarget.lastTickPosY + (KillAura.curTarget.posY - KillAura.curTarget.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = KillAura.curTarget.lastTickPosZ + (KillAura.curTarget.posZ - KillAura.curTarget.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if (KillAura.curTarget instanceof EntityPlayer) {
                x -= 0.275;
                z -= 0.275;
                y += KillAura.curTarget.getEyeHeight() - 0.225 - (KillAura.curTarget.isSneaking() ? 0.25 : 0.0);
                final double mid = 0.275;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                final double rotAdd = -0.25 * (Math.abs(KillAura.curTarget.rotationPitch) / 90.0f);
                GL11.glTranslated(0.0, rotAdd, 0.0);
                GL11.glTranslated(x + mid, y + mid, z + mid);
                GL11.glRotated((double)(-KillAura.curTarget.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
                GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
                GL11.glTranslated(x + mid, y + mid, z + mid);
                GL11.glRotated((double)KillAura.curTarget.rotationPitch, 1.0, 0.0, 0.0);
                GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                GL11.glLineWidth(1.0f);
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x - 0.0025, y - 0.0025, z - 0.0025, x + 0.55 + 0.0025, y + 0.55 + 0.0025, z + 0.55 + 0.0025));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
            else {
                final double width = KillAura.curTarget.getEntityBoundingBox().maxX - KillAura.curTarget.getEntityBoundingBox().minX;
                final double height = KillAura.curTarget.getEntityBoundingBox().maxY - KillAura.curTarget.getEntityBoundingBox().minY + 0.25;
                final float red = 0.0f;
                final float green = 0.5f;
                final float blue = 1.0f;
                final float alpha = 0.5f;
                final float lineRed = 0.0f;
                final float lineGreen = 0.5f;
                final float lineBlue = 1.0f;
                final float lineAlpha = 1.0f;
                final float lineWdith = 2.0f;
                RenderUtil.drawEntityESP(x, y, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
            }
        }
        else {
            this.mc.getRenderManager();
            double x = KillAura.curTarget.lastTickPosX + (KillAura.curTarget.posX - KillAura.curTarget.lastTickPosX) * this.mc.timer.renderPartialTicks - RenderManager.renderPosX;
            this.mc.getRenderManager();
            double y = KillAura.curTarget.lastTickPosY + (KillAura.curTarget.posY - KillAura.curTarget.lastTickPosY) * this.mc.timer.renderPartialTicks - RenderManager.renderPosY;
            this.mc.getRenderManager();
            double z = KillAura.curTarget.lastTickPosZ + (KillAura.curTarget.posZ - KillAura.curTarget.lastTickPosZ) * this.mc.timer.renderPartialTicks - RenderManager.renderPosZ;
            if (KillAura.curTarget instanceof EntityPlayer) {
                x -= 0.5;
                z -= 0.5;
                y += KillAura.curTarget.getEyeHeight() + 0.35 - (KillAura.curTarget.isSneaking() ? 0.25 : 0.0);
                final double mid = 0.5;
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, 771);
                final double rotAdd = -0.25 * (Math.abs(KillAura.curTarget.rotationPitch) / 90.0f);
                GL11.glTranslated(x + mid, y + mid, z + mid);
                GL11.glRotated((double)(-KillAura.curTarget.rotationYaw % 360.0f), 0.0, 1.0, 0.0);
                GL11.glTranslated(-(x + mid), -(y + mid), -(z + mid));
                GL11.glDisable(3553);
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDepthMask(false);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 1.0f);
                GL11.glLineWidth(2.0f);
                RenderUtil.drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.5f);
                RenderUtil.drawBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 0.05, z + 1.0));
                GL11.glDisable(2848);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDepthMask(true);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
            else {
                final double width = KillAura.curTarget.getEntityBoundingBox().maxZ - KillAura.curTarget.getEntityBoundingBox().minZ;
                final double height = 0.1;
                final float red = 0.0f;
                final float green = 0.5f;
                final float blue = 1.0f;
                final float alpha = 0.5f;
                final float lineRed = 0.0f;
                final float lineGreen = 0.5f;
                final float lineBlue = 1.0f;
                final float lineAlpha = 1.0f;
                final float lineWdith = 2.0f;
                RenderUtil.drawEntityESP(x, y + KillAura.curTarget.getEyeHeight() + 0.25, z, width, height, red, green, blue, alpha, lineRed, lineGreen, lineBlue, lineAlpha, lineWdith);
            }
        }
    }

    @EventTarget
    public void onPre(EventPreMotion event) {
        RayTraceUtil rayCastUtil;
        for (EntityLivingBase e : this.targets) {
            if (!FriendManager.isFriend(e.getName())) continue;
            this.targets.remove(e);
        }
        if (!this.openInv.getValueState().booleanValue() && this.mc.currentScreen != null) {
            this.lastMs = System.currentTimeMillis() + 1000L;
            this.test.setLastMs(1000);
            return;
        }
        this.doBlock = false;
        this.clear();
        this.findTargets(event);
        this.setCurTarget();
        if (this.aRayTrace.getValueState().booleanValue() && curTarget != null && (rayCastUtil = new RayTraceUtil(curTarget)).getEntity() != curTarget) {
            curBot = rayCastUtil.getEntity();
        }
        if (curTarget != null) {
            this.switchDelay();
            Random rand = new Random();
            if (this.rotations.getValueState().booleanValue()) {
                if (this.tick == 0) {
                    this.doAttack();
                    lastTarget = curTarget;
                    event.pitch = this.curPitch;
                    event.yaw = this.curYaw + (float)rand.nextInt(12) - 5.0f;
                    if ((double)this.mc.thePlayer.getDistanceToEntity(curTarget) < reach.getValueState()) {
                        int i = 0;
                        while ((double)i < this.cracksize.getValueState()) {
                            this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
                            this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
                            ++i;
                        }
                    }
                    this.mc.thePlayer.rotationYawHead = this.curYaw;
                } else {
                    event.yaw = this.mc.thePlayer.rotationYaw + (this.curYaw + (float)rand.nextInt(9) - 5.0f - this.mc.thePlayer.rotationYaw) / 2.0f;
                }
            } else if (this.tick == 0) {
                this.doAttack();
                lastTarget = curTarget;
                if ((double)this.mc.thePlayer.getDistanceToEntity(curTarget) < reach.getValueState()) {
                    int i = 0;
                    while ((double)i < this.cracksize.getValueState()) {
                        this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT_MAGIC);
                        this.mc.effectRenderer.emitParticleAtEntity(curTarget, EnumParticleTypes.CRIT);
                        ++i;
                    }
                }
            }
            if (gomme.getValueState().booleanValue() && this.mc.thePlayer.getDistanceSqToEntity(curTarget) > reach.getValueState() + 1.5) {
                this.mc.gameSettings.keyBindSprint.pressed = false;
                this.mc.thePlayer.setSprinting(false);
            }
        } else {
            this.targets.clear();
            this.attackedTargets.clear();
            this.lastMs = System.currentTimeMillis();
            if (this.unBlock) {
                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.mc.thePlayer.itemInUseCount = 0;
                this.unBlock = false;
            }
        }
    }

    private void switchDelay() {
        this.tick = lastTarget != null && lastTarget != curTarget ? ++this.tick : 0;
        if ((double)this.tick > this.switchDelay.getValueState() + (double)(this.attackRandomDelay.getValueState() != false ? this.random.nextInt(3) : 0)) {
            this.tick = 0;
        }
    }

    private void setRotation() {
        float[] rotations = CombatUtil.getRotations(curTarget);
        this.curYaw = rotations[0];
        this.curPitch = rotations[1] + (float)this.random.nextInt(12) - 5.0f;
        if (this.curPitch > 90.0f) {
            this.curPitch = 90.0f;
        } else if (this.curPitch < -90.0f) {
            this.curPitch = -90.0f;
        }
    }

    private void doAttack() {
        this.setRotation();
        int ticks = 1;
        int MAX_TICK = 100;
        if ((double)this.mc.thePlayer.getDistanceToEntity(curTarget) <= reach.getValueState() && this.tick == 0 && this.test.isDelayComplete(this.attackDelay.getValueState().intValue() - 20 + (this.attackRandomDelay.getValueState() != false ? this.random.nextInt(50) : 0))) {
            boolean miss;
            this.test.reset();
            boolean bl = miss = this.random.nextInt(50) + 1 == 38;
            if (this.mc.thePlayer.isBlocking() || this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.getValueState().booleanValue()) {
                this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.unBlock = false;
            }
            if (!this.mc.thePlayer.isBlocking() && !autoBlock.getValueState().booleanValue() && this.mc.thePlayer.itemInUseCount > 0) {
                this.mc.thePlayer.itemInUseCount = 0;
            }
            this.attack(miss);
            this.doBlock = true;
            if (!miss) {
                this.attackedTargets.add(curTarget);
            }
        }
        if (System.currentTimeMillis() - this.lastMs > (long)(this.delay + ticks * MAX_TICK)) {
            this.lastMs = System.currentTimeMillis();
            this.delay = (int)((double)this.attackDelay.getValueState().intValue() + (this.attackRandomDelay.getValueState() != false ? (double)this.random.nextInt(100) : 0.0)) - ticks * MAX_TICK;
            if (this.delay < 0) {
                this.delay = 0;
            }
        }
    }

    @EventTarget
    public void onPost(EventPostMotion event) {
        if (curTarget != null && (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && autoBlock.getValueState().booleanValue() || this.mc.thePlayer.isBlocking()) && this.doBlock) {
            this.mc.thePlayer.itemInUseCount = this.mc.thePlayer.getHeldItem().getMaxItemUseDuration();
            this.mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, this.mc.thePlayer.inventory.getCurrentItem(), 0.0f, 0.0f, 0.0f));
            this.unBlock = true;
        }
    }

    private void attack(final boolean fake) {
        float sharpLevel = EnchantmentHelper.func_152377_a(mc.thePlayer.getHeldItem(), ((EntityLivingBase)KillAura.curTarget).getCreatureAttribute());
        if (sharpLevel > 0.0f) {
            mc.thePlayer.onEnchantmentCritical(KillAura.curTarget);
        }
        this.mc.thePlayer.onCriticalHit(KillAura.curTarget);
        if (!this.noswing.getValueState()) {
            this.mc.thePlayer.swingItem();
        }
        if (!fake) {
            this.doBlock = true;
            double eX = KillAura.curTarget.posX + (KillAura.curTarget.posX - KillAura.curTarget.lastTickPosX);
            eX += ((eX > KillAura.curTarget.posX) ? 0.5 : ((eX == KillAura.curTarget.posX) ? 0.0 : -0.5));
            double eZ = KillAura.curTarget.posZ + (KillAura.curTarget.posZ - KillAura.curTarget.lastTickPosZ);
            eZ += ((eZ > KillAura.curTarget.posZ) ? 0.5 : ((eZ == KillAura.curTarget.posZ) ? 0.0 : -0.5));
            this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity((KillAura.curBot != null) ? KillAura.curBot : KillAura.curTarget, C02PacketUseEntity.Action.ATTACK));
            System.out.println((KillAura.curBot != null) ? KillAura.curBot : KillAura.curTarget);
            KillAura.curBot = null;
        }
    }

    private void setCurTarget() {
        for (EntityLivingBase ent : this.targets) {
            if (!this.attackedTargets.contains(ent)) {
                curTarget = ent;
                break;
            }
            if (this.attackedTargets.size() != this.targets.size()) continue;
            if (this.attackedTargets.size() > 0) {
                this.attackedTargets.clear();
            }
            this.setCurTarget();
        }
    }

    private void autoblock() {
    }

    private void clear() {
        curTarget = null;
        curBot = null;
        for (EntityLivingBase ent : this.targets) {
            if (this.isValidEntity(ent)) continue;
            this.targets.remove(ent);
            if (!this.attackedTargets.contains(ent)) continue;
            this.attackedTargets.remove(ent);
        }
    }

    private void findTargets(EventPreMotion event) {
        int maxSize = (int)(this.maxTargets.disabled ? 1.0 : this.maxTargets.getValueState());
        for (Object o : this.mc.theWorld.loadedEntityList) {
            EntityLivingBase curEnt;
            if (o instanceof EntityLivingBase && this.isValidEntity(curEnt = (EntityLivingBase)o) && !this.targets.contains(curEnt)) {
                if (FriendManager.isFriend(curEnt.getName()) && !ModManager.getModByName("NoFriends").isEnabled()) continue;
                this.targets.add(curEnt);
            }
            if (this.targets.size() >= maxSize) break;
        }
        this.targets.sort((ent1, ent2) -> {
            float e2;
            float e1 = CombatUtil.getRotations(ent1)[0];
            return e1 < (e2 = CombatUtil.getRotations(ent2)[0]) ? 1 : (e1 == e2 ? 0 : -1);
        });
    }

    private boolean isValidEntity(EntityLivingBase ent) {
        if (ent == null) {
            return false;
        }
        if (ent == this.mc.thePlayer) {
            return false;
        }
        if (ent.getName().equalsIgnoreCase("?6Dealer")) {
            return false;
        }
        if (ent instanceof EntityPlayer && !this.attackPlayers.getValueState().booleanValue()) {
            return false;
        }
        if ((ent instanceof EntityAnimal || ent instanceof EntitySquid || ent instanceof EntityArmorStand) && !this.attackAnimals.getValueState().booleanValue()) {
            return false;
        }
        if ((ent instanceof EntityMob || ent instanceof EntityVillager || ent instanceof EntityBat) && !this.attackMobs.getValueState().booleanValue()) {
            return false;
        }
        if ((double)this.mc.thePlayer.getDistanceToEntity(ent) > reach.getValueState() + 1.0) {
            return false;
        }
        if (ent.isDead || ent.getHealth() <= 0.0f) {
            return false;
        }
        if (ent.isInvisible() && !this.invisible.getValueState().booleanValue()) {
            return false;
        }
        if (ent instanceof EntityPlayer && AntiBots.isBot((EntityPlayer)ent)) {
            return false;
        }
        if (this.mc.thePlayer.isDead) {
            return false;
        }
        if (ent instanceof EntityPlayer && Teams.isEnemy((EntityPlayer)ent)) {
            return false;
        }
        if (this.blockRayTrace.getValueState().booleanValue() && ClientUtil.isBlockBetween(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY + (double)this.mc.thePlayer.getEyeHeight(), this.mc.thePlayer.posZ), new BlockPos(ent.posX, ent.posY + (double)ent.getEyeHeight(), ent.posZ))) {
            return false;
        }
        return true;
    }

    @Override
    public void onEnable() {
        this.curYaw = this.mc.thePlayer.rotationYaw;
        this.curPitch = this.mc.thePlayer.rotationPitch;
        if (this.startDelay.getValueState().booleanValue()) {
            this.test.setLastMs(100);
        }
        super.onEnable();
        ClientUtil.sendClientMessage("KillAura Enable", ClientNotification.Type.SUCCESS);
        
    }

    @Override
    public void onDisable() {
        this.targets.clear();
        this.attackedTargets.clear();
        curTarget = null;
        this.mc.thePlayer.itemInUseCount = 0;
        this.mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
        super.onDisable();
        ClientUtil.sendClientMessage("KillAura Disable", ClientNotification.Type.ERROR);
    }

    private float getYawDifference(float yaw, EntityLivingBase target) {
        return CombatUtil.getYawDifference(yaw, CombatUtil.getRotations(target)[0]);
    }

}

