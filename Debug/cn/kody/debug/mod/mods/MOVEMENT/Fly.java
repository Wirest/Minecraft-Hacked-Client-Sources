package cn.kody.debug.mod.mods.MOVEMENT;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;

import com.darkmagician6.eventapi.types.EventType;

import cn.kody.debug.events.EventMove;
import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.utils.PlayerUtil;
import cn.kody.debug.utils.Type;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

public class Fly extends Mod
{
    int counter;
    int level;
    public static int fastFlew;
    public static double hypixel;
    public Value<Double> speed;
    public Value<Double> timer;
    public Value<Double> timerduration;
    public Value<String> mode;
    public Value<Boolean> damage;
    public Value<Boolean> timerboost;
    public Value<Boolean> boost;
    public Value<Boolean> bob;
    public Value<Boolean> lagback;
    public Value<String> hypixelmode;
    TimeHelper time;
    
    public Fly() {
        super("Fly", Category.MOVEMENT);
        this.speed = new Value<Double>("Fly_Speed", 4.5, 1.0, 7.0, 0.1);
        this.timer = new Value<Double>("Fly_Timer", 2.0, 1.0, 7.0, 0.1);
        this.timerduration = new Value<Double>("Fly_TimerDuration", 400.0, 0.0, 1000.0, 50.0);
        this.mode = new Value<String>("Fly", "Mode", 0);
        this.damage = new Value<Boolean>("Fly_Damage", true);
        this.timerboost = new Value<Boolean>("Fly_TimerBoost", true);
        this.boost = new Value<Boolean>("Fly_Boost", true);
        this.bob = new Value<Boolean>("Fly_ViewBob", false);
        this.lagback = new Value<Boolean>("Fly_Lagback", true);
        this.hypixelmode = new Value<String>("Fly", "HypixelMode", 0);
        this.time = new TimeHelper();
        this.mode.addValue("Motion");
        this.mode.addValue("Hypixel");
        this.hypixelmode.addValue("Hypixel");
        this.hypixelmode.addValue("HypixelCN");
        this.hypixelmode.addValue("UHC");
        this.hypixelmode.addValue("MW");
    }
    
    @EventTarget
    public void onLagback(final EventPacket eventPacket) {
        if (eventPacket.packet instanceof S08PacketPlayerPosLook && this.lagback.getValueState()) {
            this.set(false);
            Notification.tellPlayer("Fly LagBack Checked", Type.WARN);
        }
    }
    
    @Override
    public void onEnable() {
        if (this.mode.isCurrentMode("Hypixel")) {
            if (this.damage.getValueState()) {
                if (this.hypixelmode.isCurrentMode("Hypixel")) {
                    int i = 0;
                    while (i <= 48) {
                        this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0514865, this.mc.thePlayer.posZ, false));
                        this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0618865, this.mc.thePlayer.posZ, false));
                        this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ, false));
                        ++i;
                    }
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                }
                else if (this.hypixelmode.isCurrentMode("HypixelCN")) {
                    this.damagePlayer(1);
                }
                else if (this.hypixelmode.isCurrentMode("UHC")) {
                    int j = 0;
                    while (j <= 64) {
                        this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0514865, this.mc.thePlayer.posZ, false));
                        this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.0618865, this.mc.thePlayer.posZ, false));
                        this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ, false));
                        ++j;
                    }
                    this.mc.thePlayer.sendQueue.getNetworkManager().sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, true));
                }
                else if (this.hypixelmode.isCurrentMode("MW")) {
                    int k = 0;
                    while (k < 70) {
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.06, this.mc.thePlayer.posZ, false));
                        this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ, false));
                        ++k;
                    }
                    this.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 0.1, this.mc.thePlayer.posZ, false));
                }
            }
//            if (GUILogin.usernames == null || GUILogin.passwords == null) {
//                this.damagePlayer(20);
//            }
            PlayerUtil.setMotion(defaultSpeed() + this.getSpeedEffect() * 0.05f);
            this.mc.thePlayer.motionY = 0.42;
            Fly.fastFlew = 25;
            Fly.hypixel = 14.0 + this.speed.getValueState();
            if (this.timerboost.getValueState()) {
                this.mc.timer.timerSpeed = this.timer.getValueState().floatValue();
            }
            this.time.reset();
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        final EntityPlayerSP thePlayer = this.mc.thePlayer;
        thePlayer.motionX *= 0.0;
        final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
        thePlayer2.motionZ *= 0.0;
        super.onDisable();
    }
    
    @EventTarget
    public void Move(EventMove event) {
        if (this.mode.isCurrentMode("Motion")) {
            final double doubleValue = this.speed.getValueState();
            final MovementInput movementInput = this.mc.thePlayer.movementInput;
            double n = MovementInput.moveForward;
            final MovementInput movementInput2 = this.mc.thePlayer.movementInput;
            double n2 = MovementInput.moveStrafe;
            float rotationYaw = this.mc.thePlayer.rotationYaw;
            if (n == 0.0 && n2 == 0.0) {
                event.setX(0.0);
                event.setZ(0.0);
            }
            else {
                if (n != 0.0) {
                    if (n2 > 0.0) {
                        final float n3 = rotationYaw;
                        int n4;
                        if (n > 0.0) {
                            n4 = -45;
                        }
                        else {
                            n4 = 45;
                        }
                        rotationYaw = n3 + n4;
                    }
                    else if (n2 < 0.0) {
                        final float n5 = rotationYaw;
                        int n6;
                        if (n > 0.0) {
                            n6 = 45;
                        }
                        else {
                            n6 = -45;
                        }
                        rotationYaw = n5 + n6;
                    }
                    n2 = 0.0;
                    if (n > 0.0) {
                        n = 1.0;
                    }
                    else if (n < 0.0) {
                        n = -1.0;
                    }
                }
                event.setX(n * doubleValue * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + n2 * doubleValue * Math.sin(Math.toRadians(rotationYaw + 90.0f)));
                event.setZ(n * doubleValue * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - n2 * doubleValue * Math.cos(Math.toRadians(rotationYaw + 90.0f)));
            }
        }
    }
    
    @EventTarget
    public void onMotion(final EventPreMotion eventMotion) {
        this.setDisplayName(this.mode.getModeAt(this.mode.getCurrentMode()));
        if (eventMotion.getEventType() == EventType.PRE) {
            if (this.mode.isCurrentMode("Motion")) {
                this.mc.thePlayer.onGround = false;
                boolean onGround;
                if (this.isOnGround(0.001) || ModManager.getModByName("NoFall").isEnabled()) {
                    onGround = true;
                }
                else {
                    onGround = false;
                }
                eventMotion.setOnGround(onGround);
                if (this.mc.thePlayer.movementInput.jump) {
                    this.mc.thePlayer.motionY = this.speed.getValueState() * 0.6;
                }
                else if (this.mc.thePlayer.movementInput.sneak) {
                    this.mc.thePlayer.motionY = -this.speed.getValueState() * 0.6;
                }
                else {
                    this.mc.thePlayer.motionY = 0.0;
                }
            }
            if (this.mode.isCurrentMode("Hypixel")) {
                ++Fly.fastFlew;
                final Block blockUnderPlayer = getBlockUnderPlayer(this.mc.thePlayer, 0.2);
                if (!this.isOnGround(1.0E-7) && !blockUnderPlayer.isFullBlock() && !(blockUnderPlayer instanceof BlockGlass)) {
                    this.mc.thePlayer.motionY = 0.0;
                    this.mc.thePlayer.motionX = 0.0;
                    this.mc.thePlayer.motionZ = 0.0;
                    if (this.bob.getValueState()) {
                        this.mc.thePlayer.cameraYaw = 0.1f;
                    }
                    float n = 0.29f + this.getSpeedEffect() * 0.06f;
                    if (Fly.hypixel > 0.0) {
                        if ((this.mc.thePlayer.moveForward == 0.0f && this.mc.thePlayer.moveStrafing == 0.0f) || this.mc.thePlayer.isCollidedHorizontally) {
                            Fly.hypixel = 0.0;
                        }
                        n += (float)(Fly.hypixel / 18.0);
                        Fly.hypixel -= 0.165 + this.getSpeedEffect() * 0.006;
                    }
                    if (this.boost.getValueState()) {
                        PlayerUtil.setMotion(n);
                    }
                    else {
                        PlayerUtil.setMotion(defaultSpeed());
                    }
                    this.mc.thePlayer.jumpMovementFactor = 0.0f;
                    this.mc.thePlayer.onGround = false;
                    switch (++this.counter) {
                        case 1: {
                            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ);
                            break;
                        }
                        case 2: {
                            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0E-12, this.mc.thePlayer.posZ);
                            break;
                        }
                        case 3: {
                            this.mc.thePlayer.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + 1.0E-12, this.mc.thePlayer.posZ);
                            this.counter = 0;
                            break;
                        }
                    }
                    if (this.time.delay(this.timerduration.getValueState().intValue())) {
                        this.mc.timer.timerSpeed = 1.0f;
                    }
                }
            }
        }
    }
    
    public static Block getBlockUnderPlayer(final EntityPlayerSP entityPlayerMP, final double n) {
        return Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(entityPlayerMP.posX, entityPlayerMP.posY - n, entityPlayerMP.posZ)).getBlock();
    }
    
    public boolean isOnGround(final double n) {
        return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty();
    }
    
    public void damagePlayer(int floor_double) {
        if (floor_double < 1) {
            floor_double = 1;
        }
        if (floor_double > MathHelper.floor_double(this.mc.thePlayer.getMaxHealth())) {
            floor_double = MathHelper.floor_double(this.mc.thePlayer.getMaxHealth());
        }
        final double n = 0.0625;
        if (this.mc.thePlayer != null && this.mc.getNetHandler() != null && this.mc.thePlayer.onGround) {
            int n2 = 0;
            while (n2 <= (3 + floor_double) / n) {
                this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY + n, this.mc.thePlayer.posZ, false));
                final NetHandlerPlayClient netHandler = this.mc.getNetHandler();
                final double posX = this.mc.thePlayer.posX;
                final double posY = this.mc.thePlayer.posY;
                final double posZ = this.mc.thePlayer.posZ;
                boolean p_i45942_7_;
                if (n2 == (3 + floor_double) / n) {
                    p_i45942_7_ = true;
                }
                else {
                    p_i45942_7_ = false;
                }
                netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, posZ, p_i45942_7_));
                ++n2;
            }
        }
    }
    
    public static double defaultSpeed() {
        double n = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }
    
    public int getSpeedEffect() {
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    static {
        Fly.hypixel = 0.0;
    }
}
