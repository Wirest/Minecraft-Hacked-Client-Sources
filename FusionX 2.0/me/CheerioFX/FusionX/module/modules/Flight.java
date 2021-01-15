// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import me.CheerioFX.FusionX.events.EventClick;
import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.MovementInput;
import net.minecraft.entity.Entity;
import me.CheerioFX.FusionX.utils.EntityUtils2;
import net.minecraft.util.Timer;
import me.CheerioFX.FusionX.events.EventPreMotionUpdates;
import net.minecraft.util.MathHelper;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import java.util.ArrayList;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;

public class Flight extends Module
{
    double var1;
    boolean boo1;
    
    public Flight() {
        super("Flight", 33, Category.MOVEMENT);
        this.boo1 = false;
    }
    
    @Override
    public void setup() {
        final ArrayList<String> modes = new ArrayList<String>();
        modes.add("Hypixel");
        modes.add("Cubecraft");
        FusionX.theClient.setmgr.rSetting(new Setting("Flight Modes", this, "Hypixel", modes));
    }
    
    public static String getMode() {
        return FusionX.theClient.setmgr.getSetting("Flight Modes").getValString();
    }
    
    @Override
    public void onUpdate() {
        if (this.getState()) {
            this.extraInfo = getMode();
            if (getMode() == "Hypixel" || getMode() == "Cubecraft" || getMode() == "Hypixel-Fast") {
                if (this.boo1) {
                    this.boo1 = false;
                }
                Flight.mc.thePlayer.onGround = true;
                this.var1 = Flight.mc.thePlayer.posY + 10.0;
                this.boo1 = true;
            }
        }
    }
    
    public static double getSpeed() {
        return Math.sqrt(Flight.mc.thePlayer.motionX * Flight.mc.thePlayer.motionX + Flight.mc.thePlayer.motionZ * Flight.mc.thePlayer.motionZ);
    }
    
    public static float getDirection() {
        float yaw = Flight.mc.thePlayer.rotationYaw;
        final float forward = Flight.mc.thePlayer.moveForward;
        final float strafe = Flight.mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }
    
    public static void setSpeed(final double speed) {
        Flight.mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
        Flight.mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
    }
    
    @EventTarget
    public void onPreMotionUpdates(final EventPreMotionUpdates event) {
        if (getMode() == "Hypixel" || getMode() == "Hypixel-Fast") {
            Flight.mc.thePlayer.motionY = 0.0;
            final Timer timer = Flight.mc.timer;
            Timer.timerSpeed = 1.0f;
            if (getMode() == "Hypixel-Fast") {
                Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY + 0.005, Flight.mc.thePlayer.posZ);
                Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY - 0.005, Flight.mc.thePlayer.posZ);
            }
            if (Flight.mc.thePlayer.ticksExisted % 3 == 0 && !EntityUtils2.func2(Flight.mc.thePlayer)) {
                Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY + 0.001, Flight.mc.thePlayer.posZ);
                Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY + 1.0E-9, Flight.mc.thePlayer.posZ);
            }
            event.onGround = true;
        }
        else if (getMode() == "Cubecraft-Fast") {
            Flight.mc.thePlayer.speedInAir = 0.059017234f;
            final Timer timer2 = Flight.mc.timer;
            Timer.timerSpeed = 0.27234367f;
            Flight.mc.thePlayer.motionY = 0.0;
            Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY + 1.0E-9, Flight.mc.thePlayer.posZ);
            final MovementInput movementInput = Flight.mc.thePlayer.movementInput;
            final double lol = 1.0;
            float forward = movementInput.moveForward;
            float strafe = movementInput.moveStrafe;
            final boolean up = movementInput.jump;
            float yaw = Flight.mc.thePlayer.rotationYaw;
            if ((forward != 0.0f || strafe != 0.0f) && forward != 0.0f) {
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
            Flight.mc.gameSettings.keyBindJump.getIsKeyPressed();
            Flight.mc.gameSettings.keyBindSneak.getIsKeyPressed();
            final double mx = Math.cos(Math.toRadians(yaw + 90.0f));
            final double mz = Math.sin(Math.toRadians(yaw + 90.0f));
            if (Flight.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                event.y = 0.5 * lol;
                System.out.println(event.y);
            }
            if (Flight.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                event.y = 0.5 * -lol;
            }
        }
        else if (getMode() == "Cubecraft") {
            Flight.mc.thePlayer.motionY = 0.0;
            final Timer timer3 = Flight.mc.timer;
            Timer.timerSpeed = 1.0f;
            setSpeed(getSpeed());
            final Timer timer4 = Flight.mc.timer;
            Timer.timerSpeed = 0.27234367f;
            setSpeed(getSpeed() + 1.0105112341234);
            if (Flight.mc.thePlayer.ticksExisted % 3 == 0) {
                EntityUtils2.func2(Flight.mc.thePlayer);
            }
            event.onGround = true;
            if (Flight.mc.gameSettings.keyBindJump.getIsKeyPressed()) {
                Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY + 1.0, Flight.mc.thePlayer.posZ);
            }
            if (Flight.mc.gameSettings.keyBindSneak.getIsKeyPressed()) {
                Flight.mc.thePlayer.setPosition(Flight.mc.thePlayer.posX, Flight.mc.thePlayer.posY - 1.0, Flight.mc.thePlayer.posZ);
            }
        }
    }
    
    @EventTarget
    public void onClick(final EventClick e) {
        if (getMode() == "Cubecraft-Fast" || getMode() == "Cubecraft") {
            e.setCancelled(true);
        }
    }
    
    @Override
    public void onDisable() {
        Flight.mc.thePlayer.motionX = 0.0;
        Flight.mc.thePlayer.motionZ = 0.0;
        final Timer timer = Flight.mc.timer;
        Timer.timerSpeed = 1.0f;
        Flight.mc.thePlayer.speedInAir = 0.02f;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
}
