// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.module.modules;

import net.minecraft.util.MathHelper;
import org.hero.settings.Setting;
import me.CheerioFX.FusionX.FusionX;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.utils.TimeHelper2;
import me.CheerioFX.FusionX.module.Module;

public class Longjump extends Module
{
    private double m;
    private double n;
    public static int o;
    double watchthis;
    private TimeHelper2 timer;
    
    public Longjump() {
        super("Highjump", Category.MOVEMENT);
    }
    
    @Override
    public void setup() {
        this.timer = new TimeHelper2();
        FusionX.theClient.setmgr.rSetting(new Setting("var1", this, 17.92452830188679, 0.0, 100.0, false));
        FusionX.theClient.setmgr.rSetting(new Setting("var2", this, 0.0, 0.0, 10000.0, false));
        FusionX.theClient.setmgr.rSetting(new Setting("varHeight", this, 4.06562453, 0.0, 10.0, false));
        FusionX.theClient.setmgr.rSetting(new Setting("CoolDown(Sec)", this, 7.0, 5.0, 10.0, true));
        FusionX.theClient.setmgr.rSetting(new Setting("Further", this, true));
    }
    
    public boolean isFurther() {
        return FusionX.theClient.setmgr.getSetting(this, "Further").getValBoolean();
    }
    
    public double getVar1() {
        return FusionX.theClient.setmgr.getSetting(this, "var1").getValDouble();
    }
    
    public double getVar2() {
        return FusionX.theClient.setmgr.getSetting(this, "var2").getValDouble();
    }
    
    public double getVarH() {
        return FusionX.theClient.setmgr.getSetting(this, "varHeight").getValDouble();
    }
    
    public double getCoolDown() {
        return FusionX.theClient.setmgr.getSetting(this, "CoolDown(Sec)").getValDouble();
    }
    
    public static boolean b() {
        return Longjump.mc.gameSettings.keyBindForward.pressed || Longjump.mc.gameSettings.keyBindBack.pressed || Longjump.mc.gameSettings.keyBindLeft.pressed || Longjump.mc.gameSettings.keyBindRight.pressed;
    }
    
    public static float getDirection() {
        float yaw = Longjump.mc.thePlayer.rotationYaw;
        final float forward = Longjump.mc.thePlayer.moveForward;
        final float strafe = Longjump.mc.thePlayer.moveStrafing;
        yaw += ((forward < 0.0f) ? 180 : 0);
        if (strafe < 0.0f) {
            yaw += ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        if (strafe > 0.0f) {
            yaw -= ((forward < 0.0f) ? -45 : ((forward == 0.0f) ? 90 : 45));
        }
        return yaw * 0.017453292f;
    }
    
    @Override
    public void onUpdate() {
        if (!this.getState()) {
            return;
        }
        if (FusionX.theClient.moduleManager.getModule(AutoAreaClick.class).getState()) {
            FusionX.theClient.moduleManager.getModule(AutoAreaClick.class).setState(false);
        }
        if (FusionX.theClient.moduleManager.getModule(Speed.class).getState()) {
            FusionX.theClient.moduleManager.getModule(Speed.class).setState(false);
        }
        if (FusionX.theClient.moduleManager.getModule(KillAura.class).getState()) {
            FusionX.theClient.moduleManager.getModule(KillAura.class).setState(false);
        }
        Longjump.mc.thePlayer.setSprinting(true);
        final double dspeed = Math.sqrt(Longjump.mc.thePlayer.motionX * Longjump.mc.thePlayer.motionX + Longjump.mc.thePlayer.motionZ * Longjump.mc.thePlayer.motionZ);
        Longjump.mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * dspeed;
        Longjump.mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * dspeed;
        if (!this.isFurther()) {
            this.watchthis = 0.0;
        }
        if (b() && Longjump.mc.thePlayer.onGround) {
            if (!this.timer.hasPassed(this.getCoolDown() * 1000.0)) {
                FusionX.addChatMessage("Please Wait " + this.getCoolDown() + " seconds before using this Hack Again.");
                this.setState(false);
                return;
            }
            Longjump.mc.thePlayer.jump();
            this.timer.reset();
            Longjump.mc.thePlayer.motionY *= 0.94356256 + this.getVarH();
        }
        else if (Longjump.mc.thePlayer.isAirBorne && !Longjump.mc.thePlayer.onGround) {
            final double speed = Math.sqrt(Longjump.mc.thePlayer.motionX * Longjump.mc.thePlayer.motionX + Longjump.mc.thePlayer.motionZ * Longjump.mc.thePlayer.motionZ + this.getVar1() / 400.0) + this.getVar2() / 10000.0 + this.watchthis;
            if (this.isFurther()) {
                this.watchthis += 0.001;
            }
            Longjump.mc.thePlayer.motionX = -MathHelper.sin(getDirection()) * speed;
            Longjump.mc.thePlayer.motionZ = MathHelper.cos(getDirection()) * speed;
        }
    }
    
    @Override
    public void onEnable() {
        this.watchthis = 1.0E-4;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
