// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import net.minecraft.util.MovementInput;
import net.minecraft.util.MathHelper;
import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.utils.MoveUtils;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import me.nico.hush.utils.PlayerUtils;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class Speed extends Module
{
    TimeHelper delay;
    public static String mode;
    private double prevV;
    private int i;
    double speed;
    private int counter;
    
    static {
        Speed.mode = "Timer";
    }
    
    public Speed() {
        super("Speed", "Speed", 1701952, 23, Category.MOVEMENT);
        this.delay = new TimeHelper();
        this.prevV = 0.0;
        this.i = 0;
        this.speed = 0.0;
        this.counter = 0;
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Intave");
        mode.add("Timer");
        mode.add("Y-Port");
        mode.add("Strafe");
        mode.add("AAC");
        mode.add("MC-Central");
        mode.add("MC-CentralFast");
        mode.add("CC-Bhop");
        mode.add("CC-OnGround");
        Client.instance.settingManager.rSetting(new Setting("Mode", "SpeedMode", this, "Timer", mode));
        Client.instance.settingManager.rSetting(new Setting("Timer §l\u2192", "SpeedTimer", this, 2.3, 1.0, 3.5, false));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("Timer")) {
            this.setDisplayname("Speed");
            this.Timer();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("Y-Port")) {
            this.setDisplayname("Speed");
            this.yPort();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("Intave")) {
            this.setDisplayname("Speed");
            this.Intave();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("Strafe")) {
            this.setDisplayname("Speed");
            this.Strafe();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("AAC")) {
            this.setDisplayname("Speed");
            this.AAC();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("MC-Central")) {
            this.setDisplayname("Speed");
            this.MCCentral();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("MC-CentralFast")) {
            this.setDisplayname("Speed");
            this.MCCentralFast();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("CC-Bhop")) {
            this.setDisplayname("Speed");
            this.ccBhop();
        }
        else if (Client.instance.settingManager.getSettingByName("SpeedMode").getValString().equalsIgnoreCase("CC-OnGround")) {
            this.setDisplayname("Speed");
            this.ccOnGround();
        }
    }
    
    private void Intave() {
        if (PlayerUtils.playeriswalkingforward()) {
            final Minecraft mc = Speed.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc2 = Speed.mc;
                if (!Minecraft.thePlayer.isOnLadder()) {
                    final Minecraft mc3 = Speed.mc;
                    if (!Minecraft.thePlayer.isInWater()) {
                        final Minecraft mc4 = Speed.mc;
                        if (!Minecraft.thePlayer.isInLava()) {
                            final Minecraft mc5 = Speed.mc;
                            Minecraft.thePlayer.jump();
                            final Minecraft mc6 = Speed.mc;
                            final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                            thePlayer.motionX *= 1.0255;
                            final Minecraft mc7 = Speed.mc;
                            final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                            thePlayer2.motionZ *= 1.0255;
                            return;
                        }
                    }
                }
                final Minecraft mc8 = Speed.mc;
                final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
                thePlayer3.motionX *= 1.0;
            }
            else {
                final Minecraft mc9 = Speed.mc;
                final EntityPlayerSP thePlayer4 = Minecraft.thePlayer;
                thePlayer4.motionX *= 1.0;
            }
        }
        else {
            final Minecraft mc10 = Speed.mc;
            final EntityPlayerSP thePlayer5 = Minecraft.thePlayer;
            thePlayer5.motionY *= 1.0;
        }
    }
    
    private void Strafe() {
        if (PlayerUtils.playeriswalkingforward()) {
            Speed.mc.gameSettings.keyBindJump.pressed = true;
            final Minecraft mc = Speed.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc2 = Speed.mc;
                Minecraft.thePlayer.jump();
                final Minecraft mc3 = Speed.mc;
                Minecraft.thePlayer.motionY = 0.42;
                Speed.mc.timer.timerSpeed = 1.0f;
                this.counter = 0;
            }
            else {
                Speed.mc.timer.timerSpeed = 1.0f;
                final double speed = 1.0;
                if (this.counter < 2) {
                    if (this.counter > 0) {
                        Speed.mc.timer.timerSpeed = 1.4f;
                    }
                    else {
                        Speed.mc.timer.timerSpeed = 1.0f;
                    }
                    ++this.counter;
                }
                final Minecraft mc4 = Speed.mc;
                final double motionX = Minecraft.thePlayer.motionX;
                final Minecraft mc5 = Speed.mc;
                final double n = motionX * Minecraft.thePlayer.motionX;
                final Minecraft mc6 = Speed.mc;
                final double motionZ = Minecraft.thePlayer.motionZ;
                final Minecraft mc7 = Speed.mc;
                final double Motion = Math.sqrt(n + motionZ * Minecraft.thePlayer.motionZ);
                final Minecraft mc8 = Speed.mc;
                if (Minecraft.thePlayer.hurtTime < 5) {
                    final Minecraft mc9 = Speed.mc;
                    Minecraft.thePlayer.motionX = -Math.sin(MoveUtils.getDirection()) * speed * Motion;
                    final Minecraft mc10 = Speed.mc;
                    Minecraft.thePlayer.motionZ = Math.cos(MoveUtils.getDirection()) * speed * Motion;
                }
            }
        }
    }
    
    private void MCCentral() {
        final Minecraft mc = Speed.mc;
        if (!Minecraft.thePlayer.isOnLadder()) {
            final Minecraft mc2 = Speed.mc;
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                final Minecraft mc3 = Speed.mc;
                if (Minecraft.thePlayer.onGround) {
                    Speed.mc.gameSettings.keyBindSprint.pressed = true;
                    final Minecraft mc4 = Speed.mc;
                    Minecraft.thePlayer.setSprinting(true);
                    final Minecraft mc5 = Speed.mc;
                    Minecraft.thePlayer.jump();
                }
                else {
                    final Minecraft mc6 = Speed.mc;
                    final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                    thePlayer.motionX *= 1.0;
                }
                final Minecraft mc7 = Speed.mc;
                final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                thePlayer2.motionX *= 1.015;
                final Minecraft mc8 = Speed.mc;
                final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
                thePlayer3.motionZ *= 1.015;
            }
        }
    }
    
    private void ccOnGround() {
        Speed.mc.gameSettings.keyBindJump.pressed = false;
        final Minecraft mc = Speed.mc;
        if (Minecraft.thePlayer.onGround) {
            if (this.counter < 2) {
                if (this.counter == 1) {
                    setSpeed(0.001);
                }
                else {
                    setSpeed(2.0);
                }
                ++this.counter;
            }
            else {
                this.counter = 0;
            }
        }
    }
    
    private void AAC() {
        final Minecraft mc = Speed.mc;
        if (!Minecraft.thePlayer.isOnLadder()) {
            final Minecraft mc2 = Speed.mc;
            if (!Minecraft.thePlayer.isEating()) {
                final Minecraft mc3 = Speed.mc;
                if (!Minecraft.thePlayer.isInWater()) {
                    final Minecraft mc4 = Speed.mc;
                    if (!Minecraft.thePlayer.isInLava()) {
                        Speed.mc.gameSettings.keyBindJump.pressed = false;
                        final Minecraft mc5 = Speed.mc;
                        if (Minecraft.thePlayer.onGround) {
                            final Minecraft mc6 = Speed.mc;
                            Minecraft.thePlayer.jump();
                            this.counter = 5;
                        }
                        else if (this.counter < 7) {
                            if (this.counter == 1) {
                                Speed.mc.timer.timerSpeed = 10.0f;
                            }
                            else {
                                Speed.mc.timer.timerSpeed = 1.0f;
                            }
                            ++this.counter;
                        }
                        else {
                            this.counter = 0;
                        }
                    }
                }
            }
        }
    }
    
    private void MCCentralFast() {
        final Minecraft mc = Speed.mc;
        if (!Minecraft.thePlayer.isOnLadder()) {
            final Minecraft mc2 = Speed.mc;
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                final Minecraft mc3 = Speed.mc;
                if (Minecraft.thePlayer.onGround) {
                    Speed.mc.gameSettings.keyBindSprint.pressed = true;
                    final Minecraft mc4 = Speed.mc;
                    Minecraft.thePlayer.setSprinting(true);
                    final Minecraft mc5 = Speed.mc;
                    Minecraft.thePlayer.jump();
                }
                else {
                    final Minecraft mc6 = Speed.mc;
                    final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                    thePlayer.motionX *= 1.0;
                }
                final Minecraft mc7 = Speed.mc;
                final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                thePlayer2.motionX *= 1.022;
                final Minecraft mc8 = Speed.mc;
                final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
                thePlayer3.motionZ *= 1.022;
            }
        }
    }
    
    private void ccBhop() {
        final Minecraft mc = Speed.mc;
        if (!Minecraft.thePlayer.isOnLadder()) {
            final Minecraft mc2 = Speed.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Speed.mc;
                if (Minecraft.thePlayer.moveForward > 0.0) {
                    final Minecraft mc4 = Speed.mc;
                    Minecraft.thePlayer.jump();
                }
                else {
                    final Minecraft mc5 = Speed.mc;
                    final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                    thePlayer.motionX *= 1.0;
                }
                final Minecraft mc6 = Speed.mc;
                final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                thePlayer2.motionX *= 0.75;
                final Minecraft mc7 = Speed.mc;
                final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
                thePlayer3.motionZ *= 0.75;
                final Minecraft mc8 = Speed.mc;
                final EntityPlayerSP thePlayer4 = Minecraft.thePlayer;
                thePlayer4.motionY *= 0.35;
                Speed.mc.timer.timerSpeed = 2.15f;
            }
        }
    }
    
    public void yPort() {
        if (!this.isEnabled()) {
            return;
        }
        final Minecraft mc = Speed.mc;
        if (Minecraft.thePlayer.isCollidedHorizontally) {
            return;
        }
        if (Speed.mc.gameSettings.keyBindForward.isKeyDown() || Speed.mc.gameSettings.keyBindBack.isKeyDown() || Speed.mc.gameSettings.keyBindLeft.isKeyDown() || Speed.mc.gameSettings.keyBindRight.isKeyDown()) {
            final Minecraft mc2 = Speed.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Speed.mc;
                this.prevV = Minecraft.thePlayer.posY;
                Speed.mc.timer.timerSpeed = 1.3846154f;
                final Minecraft mc4 = Speed.mc;
                Minecraft.thePlayer.setSneaking(true);
                Speed.mc.gameSettings.keyBindSprint.pressed = true;
                final Minecraft mc5 = Speed.mc;
                Minecraft.thePlayer.setSprinting(true);
                ++this.i;
                this.i %= 3;
                final Minecraft mc6 = Speed.mc;
                final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                final Minecraft mc7 = Speed.mc;
                thePlayer.jumpMovementFactor = -(float)(Minecraft.thePlayer.jumpMovementFactor * 12.5);
                final Minecraft mc8 = Speed.mc;
                Minecraft.thePlayer.jump();
                final Minecraft mc9 = Speed.mc;
                Minecraft.thePlayer.motionY = 0.4000000149011612;
            }
            this.setSpeed(0.398743f);
            final Minecraft mc10 = Speed.mc;
            final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
            thePlayer2.motionX *= 1.0099999904632568;
            final Minecraft mc11 = Speed.mc;
            final EntityPlayerSP thePlayer3 = Minecraft.thePlayer;
            thePlayer3.motionZ *= 1.0099999904632568;
        }
        else {
            final Minecraft mc12 = Speed.mc;
            if (Minecraft.thePlayer.fallDistance < 1.0f) {
                Speed.mc.timer.timerSpeed = 1.0f;
                final Minecraft mc13 = Speed.mc;
                Minecraft.thePlayer.motionY = -1.0;
                final Minecraft mc14 = Speed.mc;
                Minecraft.thePlayer.posY = this.prevV;
            }
            else {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
        }
    }
    
    public void Timer() {
        Speed.mc.gameSettings.keyBindSprint.pressed = true;
        final Minecraft mc = Speed.mc;
        Minecraft.thePlayer.setSprinting(true);
        Speed.mc.timer.timerSpeed = (float)Client.instance.settingManager.getSettingByName("SpeedTimer").getValDouble();
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
        if (Speed.mode.equalsIgnoreCase("CC-OnGround")) {
            if (Client.instance.moduleManager.getModuleName("AutoBlock").isEnabled()) {
                Client.instance.commandManager.execute(".toggle AutoBlock");
            }
            if (Client.instance.moduleManager.getModuleName("Step").isEnabled()) {
                Client.instance.commandManager.execute(".toggle Step");
            }
        }
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        Speed.mc.gameSettings.keyBindJump.pressed = false;
        Speed.mc.timer.timerSpeed = 1.0f;
        final Minecraft mc = Speed.mc;
        this.prevV = Minecraft.thePlayer.posY;
        final Minecraft mc2 = Speed.mc;
        Minecraft.thePlayer.motionX = 0.0;
        final Minecraft mc3 = Speed.mc;
        Minecraft.thePlayer.motionY = 0.0;
        final Minecraft mc4 = Speed.mc;
        Minecraft.thePlayer.motionZ = 0.0;
        super.onDisable();
    }
    
    public void setMotion(final double paramDouble) {
        final Minecraft mc = Speed.mc;
        double d1 = Minecraft.thePlayer.movementInput.moveForward;
        final Minecraft mc2 = Speed.mc;
        double d2 = Minecraft.thePlayer.movementInput.moveStrafe;
        final Minecraft mc3 = Speed.mc;
        float f = Minecraft.thePlayer.rotationYaw;
        if (d1 == 0.0 && d2 == 0.0) {
            final Minecraft mc4 = Speed.mc;
            Minecraft.thePlayer.motionX = 0.0;
            final Minecraft mc5 = Speed.mc;
            Minecraft.thePlayer.motionZ = 0.0;
        }
        else {
            if (d1 != 0.0) {
                final int i = 20;
                if (d2 > 0.0) {
                    f += ((d1 > 0.0) ? -35 : 35);
                }
                else if (d2 < 0.0) {
                    f += ((d1 > 0.0) ? 35 : -35);
                }
                d2 = 0.0;
                if (d1 > 0.0) {
                    d1 = 1.0;
                }
                else if (d1 < 0.0) {
                    d1 = -1.0;
                }
            }
            final Minecraft mc6 = Speed.mc;
            Minecraft.thePlayer.motionX = d1 * paramDouble * Math.cos(Math.toRadians(f + 90.0f)) + d2 * paramDouble * Math.sin(Math.toRadians(f + 90.0f));
            final Minecraft mc7 = Speed.mc;
            Minecraft.thePlayer.motionZ = d1 * paramDouble * Math.sin(Math.toRadians(f + 90.0f)) - d2 * paramDouble * Math.cos(Math.toRadians(f + 90.0f));
        }
    }
    
    public void setSpeed(final float speed) {
        final Minecraft mc = Speed.mc;
        Minecraft.thePlayer.motionX = -(Math.sin(this.getPlayerDirection()) * speed);
        final Minecraft mc2 = Speed.mc;
        Minecraft.thePlayer.motionZ = Math.cos(this.getPlayerDirection()) * speed;
        final Minecraft mc3 = Speed.mc;
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        final Minecraft mc4 = Speed.mc;
        final double x = Minecraft.thePlayer.posX - MathHelper.sin(this.getPlayerDirection()) * 5.0E-4f;
        final double prevV = this.prevV;
        final Minecraft mc5 = Speed.mc;
        thePlayer.setPositionAndUpdate(x, prevV, Minecraft.thePlayer.posZ + MathHelper.cos(this.getPlayerDirection()) * 5.0E-4f);
    }
    
    public static void setSpeed(final double speed) {
        final Minecraft mc = Minecraft.getMinecraft();
        final MovementInput movementInput = Minecraft.thePlayer.movementInput;
        float forward = movementInput.moveForward;
        float strafe = movementInput.moveStrafe;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0f && strafe == 0.0f) {
            Minecraft.thePlayer.motionX = 0.0;
            Minecraft.thePlayer.motionZ = 0.0;
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
        final double motionX = forward * speed * mx + strafe * speed * mz;
        final double motionZ = forward * speed * mz - strafe * speed * mx;
        Minecraft.thePlayer.motionX = forward * speed * mx + strafe * speed * mz;
        Minecraft.thePlayer.motionZ = forward * speed * mz - strafe * speed * mz;
    }
    
    public float getPlayerDirection() {
        final Minecraft mc = Speed.mc;
        float yaw = Minecraft.thePlayer.rotationYaw;
        final Minecraft mc2 = Speed.mc;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        final Minecraft mc3 = Speed.mc;
        if (Minecraft.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        }
        else {
            final Minecraft mc4 = Speed.mc;
            if (Minecraft.thePlayer.moveForward > 0.0f) {
                forward = 0.5f;
            }
        }
        final Minecraft mc5 = Speed.mc;
        if (Minecraft.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        final Minecraft mc6 = Speed.mc;
        if (Minecraft.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }
}
