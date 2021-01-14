package cn.kody.debug.mod.mods.COMBAT;

import java.util.function.ToDoubleFunction;

import javax.vecmath.Vector2f;

import java.util.Comparator;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.potion.Potion;

import com.darkmagician6.eventapi.types.EventType;

import cn.kody.debug.Client;
import cn.kody.debug.events.EventPostMotion;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.events.EventRender2D;
import cn.kody.debug.friend.FriendsManager;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.mod.mods.MOVEMENT.Fly;
import cn.kody.debug.utils.angle.Angle;
import cn.kody.debug.utils.angle.AngleUtility;
import cn.kody.debug.utils.angle.RotationUtil;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.render.RenderUtil;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.utils.time.WaitTimer;
import cn.kody.debug.value.Value;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import com.darkmagician6.eventapi.EventTarget;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Random;
import java.text.DecimalFormat;
import java.util.List;

public class KillAura extends Mod
{
    public Value<String> priority;
    public Value<String> mode;
    public Value<String> blockmode;
    public Value<Boolean> targethud;
    public Value<Boolean> players;
    public Value<Boolean> mobs;
    public Value<Boolean> animals;
    public Value<Boolean> invis;
    public static Value<Boolean> autoBlock;
    public Value<Double> fov;
    public static Value<Double> range;
    public Value<Double> cps;
    public Value<Double> hitsBeforeSwitch;
    public Value<Boolean> walls;
    public Value<Boolean> teams;
    public Value<Boolean> particle;
    public Value<Boolean> autodisable;
    public Value turnspeed;
    public static Value<Double> blockrange;
    private Value<Double> hitchance;
    public boolean isBlocking;
    TimeHelper timer;
    int hit;
    public float[] rotation;
    private List<EntityLivingBase> loaded;
    private List<EntityLivingBase> attacktargets;
    public static EntityLivingBase target;
    public static EntityLivingBase attacktarget;
    public float[] lastRotations;
    private Vector2f lastAngles;//ex”√µƒ“°Õ∑ Utils
    private double healthanimation;
    public DecimalFormat format;
    public EntityLivingBase lastEnt;
    public float lastHealth;
    public float damageDelt;
    public float lastPlayerHealth;
    public float damageDeltToPlayer;
    public double animation;
    int attackSpeed;
    Random random;
    
    public KillAura() {
        super("Aura", "Aura", Category.COMBAT);
        this.priority = new Value<String>("Aura", "Priority", 0);
        this.mode = new Value<String>("Aura", "Mode", 0);
        this.blockmode = new Value<String>("Aura", "BlockMode", 0);
        this.targethud = new Value<Boolean>("Aura_TargetHUD", true);
        this.players = new Value<Boolean>("Aura_Players", true);
        this.mobs = new Value<Boolean>("Aura_Mobs", false);
        this.animals = new Value<Boolean>("Aura_Animals", false);
        this.invis = new Value<Boolean>("Aura_Invisible", false);
        this.fov = new Value<Double>("Aura_Fov", 180.0, 1.0, 180.0, 1.0);
        this.cps = new Value<Double>("Aura_CPS", 9.0, 1.0, 20.0, 1.0);
        this.hitsBeforeSwitch = new Value<Double>("Aura_SwitchHits", 3.0, 1.0, 20.0, 1.0);
        this.walls = new Value<Boolean>("Aura_ThroughWalls", true);
        this.teams = new Value<Boolean>("Aura_Teams", true);
        this.particle = new Value<Boolean>("Aura_Particle", false);
        this.autodisable = new Value<Boolean>("Aura_AutoDisable", true);
        this.turnspeed = new Value("Aura_TurnSpeed", 120.0,0.0,180.0, 10.0);
        this.hitchance = new Value<Double>("Aura_HitChance", 100.0, 0.0, 100.0, 5.0);
        this.timer = new TimeHelper();
        this.loaded = new CopyOnWriteArrayList<EntityLivingBase>();
        this.attacktargets = new CopyOnWriteArrayList<EntityLivingBase>();
        this.lastRotations = new float[] { 0.0f, 0.0f };
        this.lastAngles = new Vector2f(0.0f, 0.0f);
        this.healthanimation = 0.0;
        this.format = new DecimalFormat("0.0");
        this.lastHealth = -1.0f;
        this.damageDelt = 0.0f;
        this.lastPlayerHealth = -1.0f;
        this.damageDeltToPlayer = 0.0f;
        this.animation = 0.0;
        this.random = new Random();
        this.priority.mode.add("Angle");
        this.priority.mode.add("Range");
        this.priority.mode.add("Fov");
        this.priority.mode.add("Health");
        this.mode.addValue("Switch");
        this.mode.addValue("Single");
        this.blockmode.addValue("NCP");
        this.blockmode.addValue("HypixelCN");
    }
    
    @EventTarget
    public void onRender2D(EventRender2D class112) {
        if (this.targethud.getValueState() && KillAura.target != null) {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            if (KillAura.target != null) {
                EntityLivingBase target = KillAura.target;
                if (target != this.lastEnt) {
                    this.lastEnt = target;
                    this.lastHealth = target.getHealth();
                    this.damageDelt = 0.0f;
                    this.damageDeltToPlayer = 0.0f;
                }
                if (this.lastHealth != target.getHealth() && target.getHealth() - this.lastHealth < 1.0f) {
                    this.damageDelt = target.getHealth() - this.lastHealth;
                    this.lastHealth = target.getHealth();
                }
                if (!this.mc.thePlayer.isEntityAlive()) {
                    this.lastPlayerHealth = -1.0f;
                }
                if (this.lastPlayerHealth == -1.0f && this.mc.thePlayer.isEntityAlive()) {
                    this.lastPlayerHealth = this.mc.thePlayer.getHealth();
                }
                if (this.lastPlayerHealth != this.mc.thePlayer.getHealth()) {
                    this.damageDeltToPlayer = this.mc.thePlayer.getHealth() - this.lastPlayerHealth;
                    this.lastPlayerHealth = this.mc.thePlayer.getHealth();
                }
                String replaceAll = target.getName().replaceAll("ß.", "");
                String string = "HP: " + String.valueOf(this.format.format(target.getHealth()));
                String string2 = "In: " + this.format.format(this.damageDeltToPlayer).replace("-", "") + "/Out: " + this.format.format(this.damageDelt).replace("-", "");
                GL11.glPushMatrix();
                GL11.glTranslatef((float)(scaledResolution.getScaledWidth() / 2), (float)(scaledResolution.getScaledHeight() / 2), 0.0f);
                if (!target.isDead) {
                    float n = KillAura.target.getHealth() / KillAura.target.getMaxHealth() * 100.0f;
                    this.animation = RenderUtil.getAnimationState(this.animation, n, Math.max(10.0, Math.abs(this.animation - n) * 30.0) * 0.3);
                    RenderUtil.drawArc(1.0f, 1.0f, 15.0, Colors.RED.c, 180, 180.0 + 3.5999999046325684 * this.animation, 5);
                    RenderUtil.drawArc(1.0f, 1.0f, 16.0, Colors.BLUE.c, 180, 180.0f + 3.6f * (KillAura.target.getTotalArmorValue() * 4), 3);
                    Gui.drawCenteredString(this.mc.fontRendererObj, replaceAll, 0, -30, Colors.WHITE.c);
                    Client.instance.fontMgr.tahoma16.drawCenteredString(string2, 0.0f, 20.0f, Colors.WHITE.c);
                    Client.instance.fontMgr.tahoma16.drawCenteredString(string, 0.0f, 30.0f, Colors.WHITE.c);
                }
                GL11.glPopMatrix();
            }
        }
    }
    
    @Override
    public void onEnable() {
        this.lastAngles.x = this.mc.thePlayer.rotationYaw;
        this.rotation = new float[] { this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch };
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.mc.getItemRenderer().block = false;
        if (this.isBlocking) {
            NetworkManager networkManager = this.mc.thePlayer.sendQueue.getNetworkManager();
            C07PacketPlayerDigging.Action release_USE_ITEM = C07PacketPlayerDigging.Action.RELEASE_USE_ITEM;
            BlockPos origin;
            if (this.blockmode.isCurrentMode("NCP")) {
                origin = new BlockPos(-1, -1, -1);
            }
            else {
                origin = BlockPos.ORIGIN;
            }
            networkManager.sendPacketNoEvent(new C07PacketPlayerDigging(release_USE_ITEM, origin, EnumFacing.DOWN));
            this.mc.thePlayer.itemInUseCount = 0;
            this.isBlocking = false;
        }
        this.lastAngles.x = this.mc.thePlayer.rotationYaw;
        super.onDisable();
    }
    
    @EventTarget
    public void onPreMotion(EventPreMotion eventMotion) {
        if (eventMotion.getEventType() == EventType.PRE) {
            if (!this.mc.thePlayer.isEntityAlive() && this.autodisable.getValueState()) {
                this.set(false);
            }
            if (this.mode.isCurrentMode("Switch")) {
                this.setDisplayName("Switch");
            }
            if (this.mode.isCurrentMode("Single")) {
                this.setDisplayName("Single");
            }
            if (KillAura.autoBlock.getValueState() && this.canBlock() && this.isBlocking) {
                NetworkManager networkManager = this.mc.thePlayer.sendQueue.getNetworkManager();
                C07PacketPlayerDigging.Action release_USE_ITEM = C07PacketPlayerDigging.Action.RELEASE_USE_ITEM;
                BlockPos origin;
                if (this.blockmode.isCurrentMode("NCP")) {
                    origin = new BlockPos(-1, -1, -1);
                }
                else {
                    origin = BlockPos.ORIGIN;
                }
                networkManager.sendPacketNoEvent(new C07PacketPlayerDigging(release_USE_ITEM, origin, EnumFacing.DOWN));
                this.mc.thePlayer.itemInUseCount = 0;
                this.isBlocking = false;
            }
            List<EntityLivingBase> sortList = this.sortList(this.getTargets(KillAura.range.getValueState(), this.fov.getValueState().floatValue()));
            if (sortList.isEmpty() && !this.attacktargets.isEmpty()) {
                this.attacktargets.clear();
            }
            this.loaded = this.sortList(this.getTargets(KillAura.range.getValueState(), this.fov.getValueState().floatValue()));
            if (this.loaded.isEmpty()) {
                KillAura.target = null;
                this.attackSpeed = 0;
                if (this.isBlocking) {
                    NetworkManager networkManager2 = this.mc.thePlayer.sendQueue.getNetworkManager();
                    C07PacketPlayerDigging.Action release_USE_ITEM2 = C07PacketPlayerDigging.Action.RELEASE_USE_ITEM;
                    BlockPos origin2;
                    if (this.blockmode.isCurrentMode("NCP")) {
                        origin2 = new BlockPos(-1, -1, -1);
                    }else {
                        origin2 = BlockPos.ORIGIN;
                    }
                    networkManager2.sendPacketNoEvent(new C07PacketPlayerDigging(release_USE_ITEM2, origin2, EnumFacing.DOWN));
                    this.mc.thePlayer.itemInUseCount = 0;
                    this.isBlocking = false;
                }
                this.rotation = new float[] { this.mc.thePlayer.rotationYaw, this.mc.thePlayer.rotationPitch };
                this.lastAngles.x = this.mc.thePlayer.rotationYaw;
            }else {
                EntityLivingBase target;
                if (this.attacktargets == sortList.get(0) && this.loaded.size() > 1) {
                    target = this.loaded.get(1);
                }
                else {
                    target = this.loaded.get(0);
                }
                KillAura.target = target;
                float[] array = RotationUtil.getRotationsForAura(KillAura.target, KillAura.range.getValueState() + 1.0);
                if (array == null) {
                    return;
                }
                eventMotion.yaw = array[0];
                eventMotion.pitch = array[1];
            }
            this.lastRotations = new float[] { eventMotion.yaw, eventMotion.pitch };
        }else {
            if (KillAura.target != null && this.shouldattack()) {
                this.attack();
            }
            boolean b;
            if (!this.getTargets(KillAura.blockrange.getValueState(), 360.0f).isEmpty()) {
                b = true;
            }
            else {
                b = false;
            }
            if (b && this.canBlock() && !this.isBlocking && KillAura.autoBlock.getValueState()) {
                NetworkManager networkManager3 = this.mc.thePlayer.sendQueue.getNetworkManager();
                BlockPos origin3;
                if (this.blockmode.isCurrentMode("NCP")) {
                    origin3 = new BlockPos(-1, -1, -1);
                }else {
                    origin3 = BlockPos.ORIGIN;
                }
                networkManager3.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(origin3, 255, this.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
                this.mc.thePlayer.itemInUseCount = this.mc.thePlayer.getHeldItem().getMaxItemUseDuration();
                this.isBlocking = true;
            }
        }
    }
    //Exhibition
    public static float getYawChangeGiven(double n, double n2, float n3) {
        double n4 = n - Minecraft.getMinecraft().thePlayer.posX;
        double n5 = n2 - Minecraft.getMinecraft().thePlayer.posZ;
        double degrees;
        if (n5 < 0.0 && n4 < 0.0) {
            degrees = 90.0 + Math.toDegrees(Math.atan(n5 / n4));
        }
        else if (n5 < 0.0 && n4 > 0.0) {
            degrees = -90.0 + Math.toDegrees(Math.atan(n5 / n4));
        }
        else {
            degrees = Math.toDegrees(-Math.atan(n4 / n5));
        }
        return MathHelper.wrapAngleTo180_float(-(n3 - (float)degrees));
    }
    
    public boolean shouldattack() {
        if (this.isValidEntity(KillAura.target, KillAura.range.getValueState(), this.fov.getValueState().floatValue())) {
            return this.timer.isDelayComplete(1000L / Math.max(this.cps.getValueState().longValue(), 1L));
        }
        KillAura.target = null;
        return false;
    }
    
    public void attack() {
        float modifierForCreature = EnchantmentHelper.getModifierForCreature(this.mc.thePlayer.getHeldItem(), EnumCreatureAttribute.UNDEFINED);
        boolean b;
        if (this.mc.thePlayer.fallDistance > 0.0f && !this.mc.thePlayer.onGround && !this.mc.thePlayer.isOnLadder() && !this.mc.thePlayer.isInWater() && !this.mc.thePlayer.isPotionActive(Potion.blindness) && this.mc.thePlayer.ridingEntity == null) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2 = b;
        if ((ModManager.getMod(Criticals.class).isEnabled() || b2) && this.particle.getValueState()) {
            this.mc.thePlayer.onCriticalHit(KillAura.target);
        }
        if (modifierForCreature > 0.0f && this.particle.getValueState()) {
            this.mc.thePlayer.onEnchantmentCritical(KillAura.target);
        }
        if (this.isBlocking && this.canBlock()) {
            NetworkManager networkManager = this.mc.thePlayer.sendQueue.getNetworkManager();
            C07PacketPlayerDigging.Action release_USE_ITEM = C07PacketPlayerDigging.Action.RELEASE_USE_ITEM;
            BlockPos origin;
            if (this.blockmode.isCurrentMode("NCP")) {
                origin = new BlockPos(-1, -1, -1);
            }
            else {
                origin = BlockPos.ORIGIN;
            }
            networkManager.sendPacketNoEvent(new C07PacketPlayerDigging(release_USE_ITEM, origin, EnumFacing.DOWN));
            this.mc.thePlayer.itemInUseCount = 0;
            this.isBlocking = false;
        }
        if (RandomUtils.nextFloat(0.0f, 100.0f) <= this.hitchance.getValueState().floatValue()) {
            if (this.mc.thePlayer.onGround && !ModManager.getMod(Fly.class).isEnabled() && this.mc.thePlayer.isCollidedVertically && !this.mc.thePlayer.isInWater() && Criticals.mode.isCurrentMode("Hypixel") && KillAura.target.hurtResistantTime <= Criticals.hurttime.getValueState() && this.attackSpeed >= Criticals.delay.getValueState().intValue() && ModManager.getMod(Criticals.class).isEnabled()) {
                this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.052 * RandomUtils.nextFloat(1.08f, 1.1f), this.mc.thePlayer.posZ, false));
                this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0125 * RandomUtils.nextFloat(1.01f, 1.07f), this.mc.thePlayer.posZ, false));
                this.attackSpeed = 0;
            }
            ++this.attackSpeed;
            this.mc.thePlayer.swingItem();
            this.mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(KillAura.target, C02PacketUseEntity.Action.ATTACK));
        }
        else {
            this.mc.thePlayer.swingItem();
        }
        if (this.mode.isCurrentMode("Switch")) {
            ++this.hit;
            if (this.hit >= this.hitsBeforeSwitch.getValueState()) {
                this.attacktargets.add(KillAura.target);
                KillAura.attacktarget = KillAura.target;
                this.hit = 0;
            }
        }
        if (this.canBlock() && !this.isBlocking && KillAura.autoBlock.getValueState()) {
            NetworkManager networkManager2 = this.mc.thePlayer.sendQueue.getNetworkManager();
            BlockPos origin2;
            if (this.blockmode.isCurrentMode("NCP")) {
                origin2 = new BlockPos(-1, -1, -1);
            }
            else {
                origin2 = BlockPos.ORIGIN;
            }
            networkManager2.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(origin2, 255, this.mc.thePlayer.getHeldItem(), 0.0f, 0.0f, 0.0f));
            this.mc.thePlayer.itemInUseCount = this.mc.thePlayer.getHeldItem().getMaxItemUseDuration();
            this.isBlocking = true;
        }
    }
    
    public boolean isOnGround(double n) {
        return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty();
    }
    
    private List<EntityLivingBase> getTargets(double n, float n2) {
        ArrayList<EntityLivingBase> list = new ArrayList<EntityLivingBase>();
        for (Object next : this.mc.theWorld.getLoadedEntityList()) {
            if (next instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase) next;
                if (this.isValidEntity(entity, n, n2) && !this.attacktargets.contains(entity)) {
                    list.add(entity);
                }
            }
        }
        return list;
    }
    
    public boolean canBlock() {
        boolean b;
        if (this.mc.thePlayer.getHeldItem() != null && this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private boolean isValidEntity(EntityLivingBase entity, double n, float n2) {
        if (entity == null) {
            return false;
        }
        if (entity == this.mc.thePlayer) {
            return false;
        }
        if (!entity.isEntityAlive() && entity.getHealth() == 0.0) {
            return false;
        }
        if (entity instanceof EntityPlayer && !this.players.getValueState()) {
            return false;
        }
        if ((entity instanceof EntityAnimal || entity instanceof EntityVillager || entity instanceof EntitySquid || entity instanceof EntityArmorStand) && !this.animals.getValueState()) {
            return false;
        }
        if ((entity instanceof EntityMob || entity instanceof EntityBat || entity instanceof EntityDragon || entity instanceof EntityGolem) && !this.mobs.getValueState()) {
            return false;
        }
        if (this.mc.thePlayer.getDistanceToEntity(entity) > n) {
            return false;
        }
        if (entity.isInvisible() && !this.invis.getValueState()) {
            return false;
        }
        if (entity instanceof EntityPlayer && isOnSameTeam(entity) && this.teams.getValueState()) {
            return false;
        }
        if (FriendsManager.isFriend(entity.getName()) && entity instanceof EntityPlayer) {
            return false;
        }
        boolean b;
        if (!this.mc.thePlayer.canEntityBeSeen(entity)) {
            b = true;
        }
        else {
            b = false;
        }
        boolean b2;
        if (!this.walls.getValueState()) {
            b2 = true;
        }
        else {
            b2 = false;
        }
        return !(b & b2) && AngleUtility.angleDifference(RotationUtil.getEntityRotations(entity, new float[] { n2, 90.0f }, 100)[0], n2) <= n2 && (!(entity instanceof EntityPlayer) || !AntiBot.isBot(entity));
    }
    
    public static boolean isOnSameTeam(EntityLivingBase entityPlayer) {
        if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().startsWith("ß")) {
            Minecraft.getMinecraft();
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().length() <= 2 || entityPlayer.getDisplayName().getUnformattedText().length() <= 2) {
                return false;
            }
            Minecraft.getMinecraft();
            if (Minecraft.getMinecraft().thePlayer.getDisplayName().getUnformattedText().substring(0, 2).equals(entityPlayer.getDisplayName().getUnformattedText().substring(0, 2))) {
                return true;
            }
        }
        return false;
    }
    
    private List<EntityLivingBase> sortList(List<EntityLivingBase> list) {
        if (this.priority.isCurrentMode("Range")) {
            list.sort(this::lambda$sortList$0);
        }
        if (this.priority.isCurrentMode("Fov")) {
            list.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)this::lambda$sortList$1));
        }
        if (this.priority.isCurrentMode("Angle")) {
            list.sort(this::lambda$sortList$2);
        }
        if (this.priority.isCurrentMode("Health")) {
            list.sort(KillAura::lambda$sortList$3);
        }
        return list;
    }
    
    public static float getDistanceBetweenAngles(float n, float n2) {
        float n3 = Math.abs(n - n2) % 360.0f;
        if (n3 > 180.0f) {
            n3 = 360.0f - n3;
        }
        return n3;
    }
    
    private static int lambda$sortList$3(EntityLivingBase entity, EntityLivingBase entity2) {
        return (int)(entity.getHealth() - entity2.getHealth());
    }
    
    private int lambda$sortList$2(EntityLivingBase entity, EntityLivingBase entity2) {
        return Float.compare(AngleUtility.angleDifference(RotationUtil.getEntityRotations(entity, this.lastRotations, 0)[0], this.lastRotations[0]), AngleUtility.angleDifference(RotationUtil.getEntityRotations(entity2, this.lastRotations, 0)[0], this.lastRotations[0]));
    }
    
    private double lambda$sortList$1(EntityLivingBase entity) {
        return getDistanceBetweenAngles(this.mc.thePlayer.rotationPitch, RotationUtil.getEntityRotations(entity, this.lastRotations, 0)[0]);
    }
    
    private int lambda$sortList$0(EntityLivingBase entity, EntityLivingBase entity2) {
        return (int)(entity.getDistanceToEntity(this.mc.thePlayer) - entity2.getDistanceToEntity(this.mc.thePlayer));
    }
    
    static {
        KillAura.autoBlock = new Value<Boolean>("Aura_AutoBlock", true);
        KillAura.range = new Value<Double>("Aura_Range", 4.2, 3.5, 7.0, 0.1);
        KillAura.blockrange = new Value<Double>("Aura_BlockRange", 8.0, 3.5, 8.0, 0.1);
    }
}
