/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.Timer
 */
package me.aristhena.lucid.modules.movement;

import me.aristhena.lucid.eventapi.Event;
import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.MoveEvent;
import me.aristhena.lucid.eventapi.events.TickEvent;
import me.aristhena.lucid.eventapi.events.UpdateEvent;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.option.options.speed.Basic;
import me.aristhena.lucid.management.option.options.speed.Bhop;
import me.aristhena.lucid.management.option.options.speed.Boost;
import me.aristhena.lucid.management.option.options.speed.MineZ;
import me.aristhena.lucid.management.option.options.speed.SpeedOption;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.modules.render.HUD;
import me.aristhena.lucid.util.LiquidUtils;
import me.aristhena.lucid.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Timer;

@Mod
public class Speed extends Module
{
    @Val(min = 0.1, max = 9.9, increment = 0.1)
    private double speed;
    private int stage;
    private boolean disabling;
    private boolean stopMotionUntilNext;
    private double moveSpeed;
    private boolean spedUp;
    public static boolean canStep;
    private double lastDist;
    public static double yOffset;
    private boolean cancel;
    
    public Speed() {
        this.speed = 6.0;
        this.stage = 1;
        this.moveSpeed = 0.2873;
    }
    
    @Override
    public void onEnable() {
        Timer.timerSpeed = 1.0f;
        this.cancel = false;
        this.stage = 1;
        this.moveSpeed = ((this.mc.thePlayer == null) ? 0.2873 : this.getBaseMoveSpeed());
        if (!this.disabling || !OptionManager.getOption("Boost", this).value || !OptionManager.getOption("Bhop", this).value) {
            super.onEnable();
        }
    }
    
    @Override
    public void preInit() {
        OptionManager.optionList.add(new Basic("Basic", true, this));
        OptionManager.optionList.add(new Boost("Boost", false, this));
        OptionManager.optionList.add(new Bhop("Bhop", false, this));
        OptionManager.optionList.add(new MineZ("MineZ", false, this));
    }
    
    @EventTarget
    private void onTick(final TickEvent event) {
        final Character colorFormatCharacter = new Character('§');
        for (final Option option : OptionManager.optionList) {
            if (option instanceof SpeedOption && option.value) {
                this.suffix = colorFormatCharacter + "7 " + (OptionManager.getOption("Hyphen", ModuleManager.getModule(HUD.class)).value ? "- " : "") + option.name;
            }
        }
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        String currentMode = "";
        for (final Option option : OptionManager.optionList) {
            if (option instanceof SpeedOption && option.value) {
                currentMode = option.name;
            }
        }
        Timer.timerSpeed = 2f;
        final String s;
        switch (s = currentMode) {
            case "Bhop": {
                Timer.timerSpeed = 1.0f;
                if (MathUtils.round(this.mc.thePlayer.posY - (int)this.mc.thePlayer.posY, 3) == MathUtils.round(0.138, 3)) {
                    final EntityPlayerSP thePlayer = this.mc.thePlayer;
                    thePlayer.motionY -= 0.08;
                    event.y -= 0.09316090325960147;
                    final EntityPlayerSP thePlayer2 = this.mc.thePlayer;
                    thePlayer2.posY -= 0.09316090325960147;
                }
                if (this.stage == 1 && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f)) {
                    this.stage = 2;
                    this.moveSpeed = 1.35 * this.getBaseMoveSpeed() - 0.01;
                }
                else if (this.stage == 2) {
                    this.stage = 3;
                    this.mc.thePlayer.motionY = 0.4;
                    event.y = 0.4;
                    this.moveSpeed *= 2.149;
                }
                else if (this.stage == 3) {
                    this.stage = 4;
                    final double difference = 0.66 * (this.lastDist - this.getBaseMoveSpeed());
                    this.moveSpeed = this.lastDist - difference;
                }
                else {
                    if (this.mc.theWorld.getCollidingBoundingBoxes((Entity)this.mc.thePlayer, this.mc.thePlayer.boundingBox.offset(0.0, this.mc.thePlayer.motionY, 0.0), true).size() > 0 || this.mc.thePlayer.isCollidedVertically) {
                        this.stage = 1;
                    }
                    this.moveSpeed = this.lastDist - this.lastDist / 159.0;
                }
                this.moveSpeed = Math.max(this.moveSpeed, this.getBaseMoveSpeed());
                final MovementInput movementInput = this.mc.thePlayer.movementInput;
                float forward = movementInput.moveForward;
                float strafe = movementInput.moveStrafe;
                float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
                if (forward == 0.0f && strafe == 0.0f) {
                    event.x = 0.0;
                    event.z = 0.0;
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
                event.x = forward * this.moveSpeed * mx + strafe * this.moveSpeed * mz;
                event.z = forward * this.moveSpeed * mz - strafe * this.moveSpeed * mx;
            }
        }
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.state == Event.State.PRE) {
            String currentMode = "";
            for (final Option option : OptionManager.optionList) {
                if (option instanceof SpeedOption && option.value) {
                    currentMode = option.name;
                }
            }
            final double xDist = this.mc.thePlayer.posX - this.mc.thePlayer.prevPosX;
            final double zDist = this.mc.thePlayer.posZ - this.mc.thePlayer.prevPosZ;
            this.lastDist = Math.sqrt(xDist * xDist + zDist * zDist);
            final String s;
            switch (s = currentMode) {
                case "Boost": {
                    Label_0483: {
                        switch (this.stage) {
                            case 1: {
                                ++this.stage;
                                event.y += 0.18;
                                break Label_0483;
                            }
                            case 2: {
                                ++this.stage;
                                event.y += 0.278;
                                break Label_0483;
                            }
                            case 3: {
                                ++this.stage;
                                event.y += 0.295;
                                break Label_0483;
                            }
                            case 4: {
                                ++this.stage;
                                event.y += 0.23;
                                break Label_0483;
                            }
                            case 5: {
                                ++this.stage;
                                event.y += 0.01;
                                break Label_0483;
                            }
                            case 6: {
                                if (!this.mc.thePlayer.isSneaking() && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && !this.mc.gameSettings.keyBindJump.isPressed() && !LiquidUtils.isOnLiquid() && !LiquidUtils.isInLiquid()) {
                                    this.stage = 1;
                                    break;
                                }
                                this.moveSpeed = this.getBaseMoveSpeed();
                                break;
                            }
                        }
                        if (this.disabling) {
                            this.mc.thePlayer.stepHeight = 0.6f;
                            Speed.canStep = true;
                            Speed.yOffset = 0.0;
                            this.stage = 1;
                            this.disabling = false;
                            super.onDisable();
                            return;
                        }
                    }
                    Speed.yOffset = event.y - this.mc.thePlayer.posY;
                }
                case "MineZ": {
                    switch (this.stage) {
                        case 1: {
                            event.y += 1.0E-4;
                            ++this.stage;
                            break;
                        }
                        case 2: {
                            event.y += 2.0E-4;
                            ++this.stage;
                            break;
                        }
                        default: {
                            this.stage = 1;
                            if (!this.mc.thePlayer.isSneaking() && (this.mc.thePlayer.moveForward != 0.0f || this.mc.thePlayer.moveStrafing != 0.0f) && !this.mc.gameSettings.keyBindJump.isPressed() && !LiquidUtils.isOnLiquid() && !LiquidUtils.isInLiquid()) {
                                this.stage = 1;
                                break;
                            }
                            this.moveSpeed = this.getBaseMoveSpeed();
                            break;
                        }
                    }
                    Speed.yOffset = event.y - this.mc.thePlayer.posY;
                }
                default:
                    break;
            }
        }
    }
    
    private double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (this.mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            final int amplifier = this.mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    @Override
    public void onDisable() {
        Timer.timerSpeed = 1.0f;
        this.moveSpeed = this.getBaseMoveSpeed();
        if (OptionManager.getOption("Boost", this).value) {
            this.spedUp = false;
            this.disabling = true;
        }
        else {
            Speed.yOffset = 0.0;
            this.stage = 0;
            this.disabling = false;
            super.onDisable();
        }
    }
}
