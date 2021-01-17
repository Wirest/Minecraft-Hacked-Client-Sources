package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import net.minecraft.potion.*;
import net.minecraft.entity.*;
import me.rigamortis.faurax.utils.*;
import me.rigamortis.faurax.module.modules.player.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.util.*;
import net.minecraft.block.*;

public class Speed extends Module implements MovementHelper, CombatHelper
{
    private int state;
    private double moveSpeed;
    private double lastDist;
    private boolean changedtimer;
    private int ticks;
    private boolean stop;
    private boolean stopMotionUntilNext;
    private boolean spedUp;
    private double yOffset;
    private boolean boost;
    private boolean canStep;
    private boolean speedTick;
    private int ticksOnground;
    public static float speed;
    private double stage;
    private boolean disabling;
    private boolean cancel;
    private int motionDelay;
    private int groundDelay;
    public static Value mode;
    public static Value speedValue;
    
    static {
        Speed.mode = new Value("Speed", String.class, "Mode", "New Bhop", new String[] { "Bhop", "YPort", "Timer", "Vanilla", "New Bhop", "Slow", "Boost" });
        Speed.speedValue = new Value("Speed", Float.TYPE, "Speed", 0.8f, 0.1f, 10.0f);
    }
    
    public Speed() {
        this.state = 1;
        this.changedtimer = false;
        this.setName("Speed");
        this.setKey("V");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Speed.mode);
        Client.getValues();
        ValueManager.values.add(Speed.speedValue);
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Speed.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = Speed.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @EventTarget
    public void moveEntity(final EventMove event) {
        if (this.isToggled()) {
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Bhop")) {
                if (Speed.movementUtils.isInLiquid()) {
                    return;
                }
                Speed.mc.timer.timerSpeed = 1.0f;
                Speed.mc.thePlayer.isAirBorne = true;
                if (!Speed.mc.gameSettings.keyBindForward.pressed && !Speed.mc.gameSettings.keyBindLeft.pressed && !Speed.mc.gameSettings.keyBindRight.pressed && !Speed.mc.gameSettings.keyBindBack.pressed && (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0 || Speed.mc.thePlayer.isCollidedVertically)) {
                    this.state = 1;
                }
                final MovementUtils movementUtils = Speed.movementUtils;
                final double round = MovementUtils.round(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3);
                final MovementUtils movementUtils2 = Speed.movementUtils;
                if (round == MovementUtils.round(0.138, 3)) {
                    final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                    thePlayer.motionY -= 0.08;
                    event.motionY -= 0.09316090325960147;
                    final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                    thePlayer2.posY -= 0.09316090325960147;
                }
                if (this.state == 1 && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                    this.state = 2;
                    this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
                }
                else if (this.state == 2) {
                    this.state = 3;
                    if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() < 1) {
                        return;
                    }
                    Speed.mc.thePlayer.motionY = 0.4;
                    if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(Speed.mc.thePlayer.motionX, Speed.mc.thePlayer.motionY, Speed.mc.thePlayer.motionZ)).size() > 0) {
                        return;
                    }
                    event.motionY = 0.4;
                    this.moveSpeed *= 2.149;
                }
                else if (this.state == 3) {
                    this.state = 4;
                    final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                }
                else {
                    if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0 || Speed.mc.thePlayer.isCollidedVertically) {
                        this.state = 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                final MovementInput movementInput = Speed.mc.thePlayer.movementInput;
                float forward = movementInput.moveForward;
                float strafe = movementInput.moveStrafe;
                float yaw = Speed.mc.thePlayer.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.motionX = 1.0;
                    event.motionZ = 1.0;
                }
                else if (forward != 0.0f) {
                    if (strafe >= 1.0f) {
                        yaw += ((forward > 0.0f) ? -45 : 45);
                        strafe = 0.0f;
                    }
                    else if (strafe <= -1.0f) {
                        yaw += ((forward > 0.0f) ? 45 : -45);
                        strafe = 0.0f;
                    }
                    if (forward > 0.0f) {
                        forward = 1.0f;
                    }
                    else if (forward < 0.0f) {
                        forward = -1.0f;
                    }
                }
                final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
                final double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
                final double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
                event.motionX = motionX;
                event.motionZ = motionZ;
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("YPort")) {
                if (Speed.mc.thePlayer.fallDistance >= 1.0f || Speed.mc.thePlayer.isCollidedHorizontally || Speed.movementUtils.isInLiquid()) {
                    return;
                }
                Speed.mc.timer.timerSpeed = 1.0f;
                if (Speed.mc.thePlayer.onGround) {
                    this.ticks = 2;
                }
                final MovementUtils movementUtils3 = Speed.movementUtils;
                final double round2 = MovementUtils.round(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3);
                final MovementUtils movementUtils4 = Speed.movementUtils;
                if (round2 == MovementUtils.round(0.138, 3)) {
                    final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                    thePlayer3.motionY -= 5.0;
                    event.motionY -= 5.0;
                    final EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
                    thePlayer4.posY -= 0.09316090325960147;
                }
                if (this.ticks == 1 && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                    this.ticks = 2;
                    final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
                    thePlayer5.motionY -= 5.0;
                    event.motionY -= 5.0;
                    this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
                }
                else if (this.ticks == 2) {
                    this.moveSpeed *= 0.9;
                    this.ticks = 3;
                    if ((Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f) && !Speed.mc.thePlayer.isCollidedHorizontally) {
                        Speed.mc.thePlayer.motionY = 0.399399995003033;
                        event.motionY = 0.399399995003033;
                        this.moveSpeed *= 2.385;
                        Speed.mc.thePlayer.motionY = -5.0;
                        this.speedTick = false;
                    }
                }
                else if (this.ticks == 3) {
                    this.ticks = 4;
                    final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                }
                else if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.expand(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0 || Speed.mc.thePlayer.isCollidedVertically) {
                    this.ticks = 1;
                }
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                final MovementInput movementInput = Speed.mc.thePlayer.movementInput;
                float forward = movementInput.moveForward;
                float strafe = movementInput.moveStrafe;
                float yaw = Speed.mc.thePlayer.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.motionX = 0.0;
                    event.motionZ = 0.0;
                }
                else if (forward != 0.0f) {
                    if (strafe >= 1.0f) {
                        yaw += ((forward > 0.0f) ? -45 : 45);
                        strafe = 0.0f;
                    }
                    else if (strafe <= -1.0f) {
                        yaw += ((forward > 0.0f) ? 45 : -45);
                        strafe = 0.0f;
                    }
                    if (forward > 0.0f) {
                        forward = 1.0f;
                    }
                    else if (forward < 0.0f) {
                        forward = -1.0f;
                    }
                }
                final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
                final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
                final double motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
                final double motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
                event.motionX = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
                event.motionZ = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
                this.canStep = true;
                Speed.mc.thePlayer.stepHeight = 0.6f;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.motionX = 0.0;
                    event.motionZ = 0.0;
                }
                else {
                    boolean collideCheck = false;
                    if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.expand(0.5, 0.0, 0.5)).size() > 0) {
                        collideCheck = true;
                    }
                    if (forward != 0.0f) {
                        if (strafe >= 1.0f) {
                            yaw += ((forward > 0.0f) ? -45 : 45);
                            strafe = 0.0f;
                        }
                        else if (strafe <= -1.0f) {
                            yaw += ((forward > 0.0f) ? 45 : -45);
                            strafe = 0.0f;
                        }
                        if (forward > 0.0f) {
                            forward = 1.0f;
                        }
                        else if (forward < 0.0f) {
                            forward = -1.0f;
                        }
                    }
                }
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Vanilla")) {
                Speed.mc.timer.timerSpeed = 1.0f;
                this.setMoveSpeed(event, Speed.speedValue.getFloatValue());
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Slow")) {
                Speed.mc.timer.timerSpeed = 1.0f;
                this.setMoveSpeed(event, 0.009999999776482582);
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("New Bhop")) {
                if (Speed.movementUtils.isInLiquid() || Flight.enabled || FreeCam.enabled) {
                    return;
                }
                Speed.mc.timer.timerSpeed = 1.0f;
                final MovementUtils movementUtils5 = Speed.movementUtils;
                final double round3 = MovementUtils.round(Speed.mc.thePlayer.posY - (int)Speed.mc.thePlayer.posY, 3);
                final MovementUtils movementUtils6 = Speed.movementUtils;
                if (round3 == MovementUtils.round(0.138, 3)) {
                    final EntityPlayerSP thePlayer6 = Speed.mc.thePlayer;
                    --thePlayer6.motionY;
                    event.motionY -= 0.0931;
                    final EntityPlayerSP thePlayer7 = Speed.mc.thePlayer;
                    thePlayer7.posY -= 0.0931;
                }
                if (this.stage == 2.0 && (Speed.mc.thePlayer.moveForward != 0.0f || Speed.mc.thePlayer.moveStrafing != 0.0f)) {
                    final EntityPlayerSP thePlayer8 = Speed.mc.thePlayer;
                    final double n = 0.39936;
                    thePlayer8.motionY = n;
                    event.motionY = n;
                    this.moveSpeed *= 2.1499999999;
                }
                else if (this.stage == 3.0) {
                    final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                }
                else {
                    if (Speed.mc.theWorld.getCollidingBoundingBoxes(Speed.mc.thePlayer, Speed.mc.thePlayer.boundingBox.offset(0.0, Speed.mc.thePlayer.motionY, 0.0)).size() > 0 || Speed.mc.thePlayer.isCollidedVertically) {
                        this.stage = 1.0;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                float forward2 = Speed.mc.thePlayer.movementInput.moveForward;
                float strafe2 = Speed.mc.thePlayer.movementInput.moveStrafe;
                float yaw2 = Minecraft.getMinecraft().thePlayer.rotationYaw;
                if (forward2 == 0.0f && strafe2 == 0.0f) {
                    event.motionX = 0.0;
                    event.motionZ = 0.0;
                }
                else if (forward2 != 0.0f) {
                    if (strafe2 >= 1.0f) {
                        yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                        strafe2 = 0.0f;
                    }
                    else if (strafe2 <= -1.0f) {
                        yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                        strafe2 = 0.0f;
                    }
                    if (forward2 > 0.0f) {
                        forward2 = 1.0f;
                    }
                    else if (forward2 < 0.0f) {
                        forward2 = -1.0f;
                    }
                }
                final double mx2 = Math.cos(Math.toRadians(yaw2 + 90.0f));
                final double mz2 = Math.sin(Math.toRadians(yaw2 + 90.0f));
                event.motionX = forward2 * this.moveSpeed * mx2 + strafe2 * this.moveSpeed * mz2;
                event.motionZ = forward2 * this.moveSpeed * mz2 - strafe2 * this.moveSpeed * mx2;
                Speed.mc.thePlayer.stepHeight = 0.6f;
                if (forward2 == 0.0f && strafe2 == 0.0f) {
                    event.motionX = 0.0;
                    event.motionZ = 0.0;
                }
                else if (forward2 != 0.0f) {
                    if (strafe2 >= 1.0f) {
                        yaw2 += ((forward2 > 0.0f) ? -45 : 45);
                        strafe2 = 0.0f;
                    }
                    else if (strafe2 <= -1.0f) {
                        yaw2 += ((forward2 > 0.0f) ? 45 : -45);
                        strafe2 = 0.0f;
                    }
                    if (forward2 > 0.0f) {
                        forward2 = 1.0f;
                    }
                    else if (forward2 < 0.0f) {
                        forward2 = -1.0f;
                    }
                }
                ++this.stage;
            }
        }
    }
    
    public void setMoveSpeed(final EventMove event, final double speed) {
        final MovementInput movementInput = Speed.mc.thePlayer.movementInput;
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = Speed.mc.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setMotionX(0.0);
            event.setMotionZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setMotionX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setMotionZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick event) {
        if (this.isToggled()) {
            final double xDist = Speed.mc.thePlayer.posX - Speed.mc.thePlayer.prevPosX;
            final double zDist = Speed.mc.thePlayer.posZ - Speed.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Timer")) {
                Speed.mc.timer.timerSpeed = 3.0f;
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Bhop")) {
                this.setModInfo(" §7Bhop");
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("YPort")) {
                this.setModInfo(" §7YPort");
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Vanilla")) {
                this.setModInfo(" §7Vanilla");
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Boost")) {
                this.setModInfo(" §7Boost");
                Speed.mc.timer.timerSpeed = 1.0f;
                if (!Speed.mc.thePlayer.isCollidedHorizontally) {
                    final boolean limit = Speed.mc.thePlayer.motionX <= 0.25 && Speed.mc.thePlayer.motionX >= -0.25 && Speed.mc.thePlayer.motionZ <= 0.25 && Speed.mc.thePlayer.motionZ >= -0.2;
                    if ((this.getBlock(2) instanceof BlockIce || this.getBlock(2) instanceof BlockPackedIce) && limit && !this.isStandingStill()) {
                        final EntityPlayerSP thePlayer = Speed.mc.thePlayer;
                        thePlayer.jumpMovementFactor *= 2.0f;
                        final EntityPlayerSP thePlayer2 = Speed.mc.thePlayer;
                        thePlayer2.motionX *= 2.0;
                        final EntityPlayerSP thePlayer3 = Speed.mc.thePlayer;
                        thePlayer3.motionZ *= 2.0;
                    }
                    final boolean strafe = Speed.mc.gameSettings.keyBindForward.pressed && (Speed.mc.gameSettings.keyBindLeft.pressed || Speed.mc.gameSettings.keyBindRight.pressed);
                    final double offsetX = strafe ? 4.5f : 5.0f;
                    final double offsetZ = strafe ? 4.5f : 5.0f;
                    final Block block = Speed.mc.theWorld.getBlockState(new BlockPos((int)Math.round(Speed.mc.thePlayer.posX + offsetX * 32.0), (int)Math.round(Speed.mc.thePlayer.boundingBox.minY), (int)Math.round(Speed.mc.thePlayer.posZ + offsetZ * 32.0))).getBlock();
                    if (!block.getMaterial().isSolid() || !(block instanceof BlockGlass)) {
                        ++this.motionDelay;
                        if (Speed.mc.thePlayer.isCollidedVertically) {
                            ++this.groundDelay;
                        }
                        else {
                            this.groundDelay = 0;
                        }
                        final boolean check = Speed.mc.thePlayer.isInWater() || !Speed.mc.thePlayer.onGround;
                        final boolean jesusCheck = !(this.getBlock(2) instanceof BlockLiquid);
                        if (this.motionDelay >= 2 && !this.isStandingStill() && jesusCheck && !check && this.groundDelay >= 6 && !Speed.mc.gameSettings.keyBindJump.pressed) {
                            final EntityPlayerSP thePlayer4 = Speed.mc.thePlayer;
                            thePlayer4.motionX *= offsetX;
                            final EntityPlayerSP thePlayer5 = Speed.mc.thePlayer;
                            thePlayer5.motionZ *= offsetZ;
                            Speed.mc.timer.timerSpeed = 1.25f;
                            final EntityPlayerSP thePlayer6 = Speed.mc.thePlayer;
                            thePlayer6.motionY += 0.19900000095367432;
                        }
                        if (this.motionDelay >= 3 && !this.isStandingStill() && jesusCheck && !check && this.groundDelay >= 6 && !Speed.mc.gameSettings.keyBindJump.pressed) {
                            final EntityPlayerSP thePlayer7 = Speed.mc.thePlayer;
                            thePlayer7.motionX /= 0.9990000128746033;
                            final EntityPlayerSP thePlayer8 = Speed.mc.thePlayer;
                            thePlayer8.motionZ /= 0.9990000128746033;
                        }
                        if (this.motionDelay >= 4 && !this.isStandingStill() && jesusCheck && !check && this.groundDelay >= 6 && !Speed.mc.gameSettings.keyBindJump.pressed) {
                            final EntityPlayerSP thePlayer9 = Speed.mc.thePlayer;
                            thePlayer9.motionX /= 1.4989999532699585;
                            final EntityPlayerSP thePlayer10 = Speed.mc.thePlayer;
                            thePlayer10.motionZ /= 1.4989999532699585;
                            this.motionDelay = 0;
                        }
                    }
                }
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Slow")) {
                this.setModInfo(" §7Slow");
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("New Bhop")) {
                this.setModInfo(" §7New Bhop");
            }
            if (Speed.mode.getSelectedOption().equalsIgnoreCase("Timer")) {
                this.setModInfo(" §7Timer");
            }
        }
    }
    
    private Block getBlock(final int offset) {
        final int x = (int)Math.round(Speed.mc.thePlayer.posX - 0.5);
        final int y = (int)Math.round(Speed.mc.thePlayer.posY - 0.5);
        final int z = (int)Math.round(Speed.mc.thePlayer.posZ - 0.5);
        final Block block = Speed.mc.theWorld.getBlockState(new BlockPos(x, y - offset, z)).getBlock();
        return block;
    }
    
    private boolean isStandingStill() {
        return Math.abs(Speed.mc.thePlayer.motionX) <= 0.01 && Math.abs(Speed.mc.thePlayer.motionZ) <= 0.01;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Speed.mc.timer.timerSpeed = 1.0f;
        if (Speed.mode.getSelectedOption().equalsIgnoreCase("New Bhop")) {
            this.moveSpeed = this.getBaseMoveSpeed();
            this.lastDist = 0.0;
            this.stage = 4.0;
        }
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        this.cancel = false;
        this.stage = 1.0;
        this.moveSpeed = ((Speed.mc.thePlayer == null) ? 0.2873 : this.getBaseMoveSpeed());
        this.moveSpeed = this.getBaseMoveSpeed();
        this.lastDist = 0.0;
        this.stage = 4.0;
    }
}
