package cn.kody.debug.mod.mods.MOVEMENT;

import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;

import java.math.RoundingMode;
import java.math.BigDecimal;
import com.darkmagician6.eventapi.EventTarget;

import cn.kody.debug.events.EventMove;
import cn.kody.debug.events.EventPacket;
import cn.kody.debug.events.EventStep;
import cn.kody.debug.events.EventUpdate;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.mod.ModManager;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.utils.PlayerUtil;
import cn.kody.debug.utils.Type;
import cn.kody.debug.utils.time.TimeHelper;
import cn.kody.debug.value.Value;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;

public class Speed extends Mod
{
    public Value<String> mode;
    public Value<Boolean> lagback;
    public boolean shouldslow;
    boolean collided;
    boolean lessSlow;
    private int stage;
    private int stageOG;
    double less;
    double stair;
    private double speed;
    public double slow;
    private double lastDist;
    TimeHelper timer;
    TimeHelper lastCheck;
    
    public Speed() {
        super("Speed", Category.MOVEMENT);
        this.mode = new Value<String>("Speed", "Mode", 0);
        this.lagback = new Value<Boolean>("Speed_Lagback", true);
        this.shouldslow = false;
        this.stageOG = 1;
        this.timer = new TimeHelper();
        this.lastCheck = new TimeHelper();
        this.mode.addValue("Hypixel");
        this.mode.addValue("HypixelCN");
        this.mode.addValue("HypixelCN2");
    }
    
    private double getHypixelCNSpeed(final int n) {
        double n2 = defaultSpeed() + 0.028 * this.getSpeedEffect() + this.getSpeedEffect() / 15.0;
        final double n3 = 0.4145 + this.getSpeedEffect() / 12.5;
        final double n4 = n / 500.0 * 2.0;
        if (n == 0) {
            if (this.timer.delay(300.0)) {
                this.timer.reset();
            }
            if (!this.lastCheck.delay(500.0)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            }
            else if (this.shouldslow) {
                this.shouldslow = false;
            }
            n2 = 0.64 + (this.getSpeedEffect() + 0.028 * this.getSpeedEffect()) * 0.134;
        }
        else if (n == 1) {
            if (this.mc.timer.timerSpeed == 0.1f) {}
            n2 = n3;
        }
        else if (n >= 2) {
            if (this.mc.timer.timerSpeed == 0.1f) {}
            n2 = n3 - n4;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0) || this.collided) {
            n2 = 0.2;
            if (n == 0) {
                n2 = 0.0;
            }
        }
        final double n5 = n2;
        double n6;
        if (this.shouldslow) {
            n6 = n2;
        }
        else {
            n6 = defaultSpeed() + 0.028 * this.getSpeedEffect();
        }
        return Math.max(n5, n6);
    }
    
    @EventTarget
    public void onPacket(final EventPacket eventPacket) {
        if (eventPacket.packet instanceof S08PacketPlayerPosLook) {
            final S08PacketPlayerPosLook s08PacketPlayerPosLook = (S08PacketPlayerPosLook)eventPacket.packet;
            if (this.lastCheck.delay(300.0)) {
                s08PacketPlayerPosLook.yaw = this.mc.thePlayer.rotationYaw;
                s08PacketPlayerPosLook.pitch = this.mc.thePlayer.rotationPitch;
            }
            this.lastCheck.reset();
            this.stage = -6;
            if (this.lagback.getValueState()) {
                this.set(false);
                Notification.tellPlayer("Speed LagBack Checked", Type.WARN);
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        this.lastDist = Math.sqrt((this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) * (this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX) + (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ) * (this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ));
    }
    
    private double getHypixelSpeed(final int n) {
        double n2 = defaultSpeed() + 0.028 * this.getSpeedEffect() + this.getSpeedEffect() / 15.0;
        final double n3 = 0.4145 + this.getSpeedEffect() / 12.5;
        final double n4 = 0.3945 + this.getSpeedEffect() / 12.5;
        final double n5 = n / 500.0 * 2.5;
        if (n == 0) {
            if (this.timer.delay(300.0)) {
                this.timer.reset();
            }
            if (!this.lastCheck.delay(500.0)) {
                if (!this.shouldslow) {
                    this.shouldslow = true;
                }
            }
            else if (this.shouldslow) {
                this.shouldslow = false;
            }
            n2 = 0.64 + (this.getSpeedEffect() + 0.028 * this.getSpeedEffect()) * 0.134;
        }
        else if (n == 1) {
            n2 = n3;
        }
        else if (n == 2) {
            if (this.mc.timer.timerSpeed == 1.354f) {}
            n2 = n4;
        }
        else if (n >= 3) {
            if (this.mc.timer.timerSpeed == 1.254f) {}
            n2 = n4 - n5;
        }
        if (this.shouldslow || !this.lastCheck.delay(500.0) || this.collided) {
            n2 = 0.2;
            if (n == 0) {
                n2 = 0.0;
            }
        }
        final double n6 = n2;
        double n7;
        if (this.shouldslow) {
            n7 = n2;
        }
        else {
            n7 = defaultSpeed() + 0.028 * this.getSpeedEffect();
        }
        return Math.max(n6, n7);
    }
    
    @EventTarget
    public void onStep(EventStep e) {
        final double n = this.mc.thePlayer.getEntityBoundingBox().minY - this.mc.thePlayer.posY;
        if (n > 0.7) {
            this.less = 0.0;
        }
        if (n == 0.5) {
            this.stair = 0.75;
        }
    }
    
    @EventTarget
    public void onMove(final EventMove class1201) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            if (this.mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                this.mc.timer.timerSpeed = 1.0f;
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            final double less = this.less;
            double n;
            if (this.less > 1.0) {
                n = 0.12;
            }
            else {
                n = 0.11;
            }
            this.less = less - n;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!this.isInLiquid() && this.isOnGround(0.01) && this.isMoving2()) {
                this.collided = this.mc.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
                    final double motionY = 0.4086666 + this.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        this.slow = randomNumber(-10000, 0) / 1.0E8;
                        this.mc.thePlayer.jump();
                        class1201.setY(this.mc.thePlayer.motionY = motionY);
                    }
                    ++this.less;
                    if (this.less > 1.0 && !this.lessSlow) {
                        this.lessSlow = true;
                    }
                    else {
                        this.lessSlow = false;
                    }
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(this.stage) + 0.0331;
            this.speed *= 0.91;
            this.speed += this.slow;
            if (this.stair > 0.0) {
                this.speed *= 0.7 - this.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                this.speed = defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.95;
                this.speed *= 0.95;
            }
            if (this.isInLiquid()) {
                if (!ModManager.getModByName("Jesus").isEnabled()) {
                    this.speed = 0.12;
                }
                else {
                    this.speed = defaultSpeed();
                }
            }
            if (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(class1201, this.speed);
                ++this.stage;
            }
        }
        else if (this.mode.isCurrentMode("HypixelCN")) {
            this.setDisplayName("HypixelCN");
            if (!this.mc.thePlayer.isInWater()) {
                if (this.canZoom()) {
                    this.speed = PlayerUtil.getBaseMoveSpeed();
                }
                if (this.stage == 1 && this.mc.thePlayer.isCollidedVertically && this.canZoom()) {
                    this.speed = 1.5 + PlayerUtil.getBaseMoveSpeed() - 0.01;
                }
                if (this.canZoom() && this.stage == 2) {
                    double motionY2 = 0.418;
                    if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
                        motionY2 += (this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1f;
                    }
                    class1201.setY(this.mc.thePlayer.motionY = motionY2);
                    this.speed *= 1.9;
                }
                else if (this.stage == 3) {
                    this.speed = this.lastDist - 0.66 * (this.lastDist - PlayerUtil.getBaseMoveSpeed());
                }
                else {
                    if ((this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0)).size() > 0 || this.mc.thePlayer.isCollidedVertically) && this.stage > 0) {
                        int stage;
                        if (this.isMoving2()) {
                            stage = 1;
                        }
                        else {
                            stage = 0;
                        }
                        this.stage = stage;
                    }
                    this.speed = PlayerUtil.getBaseMoveSpeed() * 1.00000011920929;
                    this.speed = this.lastDist - this.lastDist / 159.0;
                }
                this.setMotion(class1201, this.speed = Math.max(this.speed, PlayerUtil.getBaseMoveSpeed()));
                if (this.stage > 0) {
                    this.setMotion(class1201, this.speed);
                }
                final MovementInput movementInput = this.mc.thePlayer.movementInput;
                float moveForward = movementInput.moveForward;
                final MovementInput movementInput2 = this.mc.thePlayer.movementInput;
                float moveStrafe = movementInput.moveStrafe;
                float rotationYaw = this.mc.thePlayer.rotationYaw;
                if (moveForward == 0.0f && moveStrafe == 0.0f) {
                    final EntityPlayerSP thePlayer = this.mc.thePlayer;
                    final double motionX = thePlayer.motionX;
                    final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                    final double motionZ = thePlayer2.motionZ * 0.0;
                    thePlayer2.motionZ = motionZ;
                    thePlayer.motionX = motionX * motionZ;
                    class1201.x = 0.0;
                    class1201.z = 0.0;
                }
                else if (moveForward != 0.0f) {
                    if (moveStrafe >= 1.0f) {
                        final float n2 = rotationYaw;
                        float n3;
                        if (moveForward > 0.0f) {
                            n3 = -45.0f;
                        }
                        else {
                            n3 = 45.0f;
                        }
                        rotationYaw = n2 + n3;
                        moveStrafe = 0.0f;
                    }
                    else if (moveStrafe <= -1.0f) {
                        final float n4 = rotationYaw;
                        float n5;
                        if (moveForward > 0.0f) {
                            n5 = 45.0f;
                        }
                        else {
                            n5 = -45.0f;
                        }
                        rotationYaw = n4 + n5;
                        moveStrafe = 0.0f;
                    }
                    if (moveForward > 0.0f) {
                        moveForward = 1.0f;
                    }
                    else if (moveForward < 0.0f) {
                        moveForward = -1.0f;
                    }
                }
                final double cos = Math.cos(Math.toRadians(rotationYaw + 90.0f));
                final double sin = Math.sin(Math.toRadians(rotationYaw + 90.0f));
                class1201.x = moveForward * this.speed * cos + moveStrafe * this.speed * sin;
                class1201.z = moveForward * this.speed * sin - moveStrafe * this.speed * cos;
                if (moveForward == 0.0f && moveStrafe == 0.0f) {
                    class1201.x = 0.0;
                    class1201.z = 0.0;
                    final EntityPlayerSP thePlayer3 = this.mc.thePlayer;
                    final double motionX2 = thePlayer3.motionX;
                    final EntityPlayerSP thePlayer4 = this.mc.thePlayer;
                    final double motionZ2 = thePlayer4.motionZ * 0.0;
                    thePlayer4.motionZ = motionZ2;
                    thePlayer3.motionX = motionX2 * motionZ2;
                }
                else if (moveForward != 0.0f) {
                    if (moveStrafe >= 1.0f) {
                        if (moveForward > 0.0f) {
                        }
                    }
                    else if (moveStrafe <= -1.0f) {
                        if (moveForward > 0.0f) {
                        }
                    }
                    if (moveForward > 0.0f) {
                    }
                    else if (moveForward < 0.0f) {}
                }
                ++this.stage;
            }
            PlayerUtil.setSpeed(PlayerUtil.getSpeed());
        }
        else if (this.mode.isCurrentMode("HypixelCN2")) {
            if (this.mc.thePlayer.isCollidedHorizontally) {
                this.collided = true;
            }
            if (this.collided) {
                this.mc.timer.timerSpeed = 1.0f;
                this.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.25;
            }
            final double less2 = this.less;
            double n6;
            if (this.less > 1.0) {
                n6 = 0.12;
            }
            else {
                n6 = 0.11;
            }
            this.less = less2 - n6;
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!this.isInLiquid() && this.isOnGround(0.01) && this.isMoving2()) {
                this.collided = this.mc.thePlayer.isCollidedHorizontally;
                if (this.stage >= 0 || this.collided) {
                    this.stage = 0;
                    double n7;
                    if (ModManager.getModByName("Scaffold").isEnabled()) {
                        n7 = 0.407;
                    }
                    else {
                        n7 = 0.41999742;
                    }
                    final double motionY3 = n7 + this.getJumpEffect() * 0.1;
                    if (this.stair == 0.0) {
                        this.mc.thePlayer.jump();
                        class1201.setY(this.mc.thePlayer.motionY = motionY3);
                    }
                    ++this.less;
                    if (this.less > 1.0 && !this.lessSlow) {
                        this.lessSlow = true;
                    }
                    else {
                        this.lessSlow = false;
                    }
                    if (this.less > 1.12) {
                        this.less = 1.12;
                    }
                }
            }
            this.speed = this.getHypixelSpeed(this.stage) + 0.01 + Math.random() / 500.0;
            this.speed *= 0.87;
            if (this.stair > 0.0) {
                this.speed *= 0.7 - this.getSpeedEffect() * 0.1;
            }
            if (this.stage < 0) {
                this.speed = defaultSpeed();
            }
            if (this.lessSlow) {
                this.speed *= 0.95;
            }
            if (this.isInLiquid()) {
                this.speed = 0.12;
            }
            if (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) {
                this.setMotion(class1201, this.speed);
                ++this.stage;
            }
        }
    }
    
    public double round(final double n, final int n2) {
        if (n2 < 0) {
            throw new IllegalArgumentException();
        }
        return new BigDecimal(n).setScale(n2, RoundingMode.HALF_UP).doubleValue();
    }
    
    private double random(final double n, final double n2) {
        return Math.random() * (n - n2) + n2;
    }
    
    public static void GetSpeed(EventMove e, double d) {
        Minecraft.getMinecraft();
        double v3 = Minecraft.thePlayer.movementInput.moveForward;
        Minecraft.getMinecraft();
        double v5 = Minecraft.thePlayer.movementInput.moveStrafe;
        Minecraft.getMinecraft();
        double v7 = Minecraft.thePlayer.rotationYaw;
        if (v3 != 0.0 || v5 != 0.0) {
            if (v3 != 0.0) {
                if (v5 > 0.0) {
                    v7 += (double)(v3 > 0.0 ? -45 : 45);
                } else if (v5 < 0.0) {
                    v7 += (double)(v3 > 0.0 ? 45 : -45);
                }
                v5 = 0.0;
                if (v3 > 0.0) {
                    v3 = 1.0;
                } else if (v3 < 0.0) {
                    v3 = -1.0;
                }
            }
            e.setX(v3 * d * Math.cos(Math.toRadians(v7 + 88.0)) + v5 * d * Math.sin(Math.toRadians(v7 + 87.9000015258789)));
            e.setZ(v3 * d * Math.sin(Math.toRadians(v7 + 88.0)) - v5 * d * Math.cos(Math.toRadians(v7 + 87.9000015258789)));
        }
    }
    
    private boolean canZoom() {
        boolean b;
        if (this.isMoving2() && this.mc.thePlayer.onGround) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    private void setMotion(EventMove em, double speed) {
        double forward = Minecraft.thePlayer.movementInput.moveForward;
        double strafe = Minecraft.thePlayer.movementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            em.setX(0.0);
            em.setZ(0.0);
        } else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += (float)(forward > 0.0 ? -45 : 45);
                } else if (strafe < 0.0) {
                    yaw += (float)(forward > 0.0 ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                } else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            em.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            em.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public boolean isMoving2() {
        boolean b;
        if (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) {
            b = true;
        }
        else {
            b = false;
        }
        return b;
    }
    
    public boolean isOnGround(final double n) {
        return !this.mc.theWorld.getCollidingBoundingBoxes(this.mc.thePlayer, this.mc.thePlayer.getEntityBoundingBox().offset(0.0, -n, 0.0)).isEmpty();
    }
    
    public int getJumpEffect() {
        if (this.mc.thePlayer.isPotionActive(Potion.jump)) {
            return this.mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier() + 1;
        }
        return 0;
    }
    
    public int getSpeedEffect() {
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            return this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1;
        }
        return 0;
    }
    
    public boolean isInLiquid() {
        if (Minecraft.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        int y = (int)Minecraft.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double((double)Minecraft.thePlayer.getEntityBoundingBox().minX); x < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double((double)Minecraft.thePlayer.getEntityBoundingBox().minZ); z < MathHelper.floor_double(Minecraft.thePlayer.getEntityBoundingBox().maxZ) + 1; ++z) {
                Block block = this.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block == null || block.getMaterial() == Material.air) continue;
                if (!(block instanceof BlockLiquid)) {
                    return false;
                }
                inLiquid = true;
            }
        }
        return inLiquid;
    }
    
    @Override
    public void onEnable() {
        boolean b;
        if (this.mc.thePlayer == null) {
            b = true;
        }
        else {
            b = false;
        }
        boolean isCollidedHorizontally;
        if (b) {
            isCollidedHorizontally = false;
        }
        else {
            isCollidedHorizontally = this.mc.thePlayer.isCollidedHorizontally;
        }
        this.collided = isCollidedHorizontally;
        this.lessSlow = false;
        if (this.mc.thePlayer != null) {
            this.speed = defaultSpeed();
        }
        this.slow = randomNumber(-10000, 0) / 1.0E7;
        this.less = 0.0;
        this.stage = 2;
        if (this.mode.isCurrentMode("HypixelCN")) {
            this.stage = 1;
        }
        this.mc.timer.timerSpeed = 1.0f;
        super.onEnable();
    }
    
    public static int randomNumber(final int n, final int n2) {
        return Math.round(n2 + (float)Math.random() * (n - n2));
    }
    
    public static double defaultSpeed() {
        double n = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            n *= 1.0 + 0.2 * (Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
        }
        return n;
    }
    
    @Override
    public void onDisable() {
        this.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
