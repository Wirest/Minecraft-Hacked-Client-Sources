// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.combat;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.entity.EntityPlayerSP;
import me.nico.hush.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.PreUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.modules.Module;

public class Velocity extends Module
{
    public Velocity() {
        super("Velocity", "Velocity", 16777215, 0, Category.COMBAT);
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("Intave");
        mode.add("IntaveOther");
        mode.add("AAC");
        mode.add("Push");
        mode.add("Reverse");
        mode.add("AAC3.3.13");
        mode.add("IntaveOld");
        mode.add("Test");
        Client.instance.settingManager.rSetting(new Setting("Mode", "VelocityMode", this, "AAC", mode));
    }
    
    @EventTarget
    public void onUpdate(final PreUpdate event) {
        if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("IntaveOld")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[IntaveOld]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.IntaveOld();
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("AAC")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[AAC]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.AAC(event);
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("Push")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[Push]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.Push(event);
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("Reverse")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[Reverse]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.Reverse();
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("AAC3.3.13")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[AACOld]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.AAC2(event);
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("Intave2")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[Intave2]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.IntaveNew(event);
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("Test")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[Test]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.Test(event);
        }
        else if (Client.instance.settingManager.getSettingByName("VelocityMode").getValString().equalsIgnoreCase("Intave")) {
            if (Client.instance.settingManager.getSettingByName("ArraySettings").getValBoolean()) {
                this.setDisplayname("Velocity[Intave]");
            }
            else {
                this.setDisplayname("Velocity");
            }
            this.Intave(event);
        }
    }
    
    private void Test(final PreUpdate event) {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime > 0.0) {
            final Minecraft mc2 = Velocity.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Velocity.mc;
                double velocity = Minecraft.thePlayer.rotationYawHead;
                velocity = Math.toRadians(velocity);
                double motionX = -Math.sin(velocity) * 0.053;
                double motionZ = -Math.sin(velocity) * 0.039;
                final Minecraft mc4 = Velocity.mc;
                motionX = Minecraft.thePlayer.motionX;
                final Minecraft mc5 = Velocity.mc;
                motionZ = Minecraft.thePlayer.motionZ;
                final Minecraft mc6 = Velocity.mc;
                final double x = Minecraft.thePlayer.posX + motionX;
                final Minecraft mc7 = Velocity.mc;
                final double posY = Minecraft.thePlayer.posY;
                final Minecraft mc8 = Velocity.mc;
                PlayerUtils.sendPosition(x, posY, Minecraft.thePlayer.posZ + motionZ, false);
            }
        }
    }
    
    private void IntaveNew(final PreUpdate event) {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime == 9) {
            final Minecraft mc2 = Velocity.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Velocity.mc;
                Minecraft.thePlayer.capabilities.allowFlying = true;
                final Minecraft mc4 = Velocity.mc;
                double velocity = Minecraft.thePlayer.rotationYawHead;
                velocity = Math.toRadians(velocity);
                double motionX = Math.sin(velocity) * 0.053;
                double motionZ = Math.sin(velocity) * 0.039;
                final double motionY = -Math.sin(velocity) * 0.1;
                final Minecraft mc5 = Velocity.mc;
                motionX = Minecraft.thePlayer.motionX;
                final Minecraft mc6 = Velocity.mc;
                motionZ = Minecraft.thePlayer.motionZ;
            }
            else {
                final Minecraft mc7 = Velocity.mc;
                Minecraft.thePlayer.motionX = 0.0;
                final Minecraft mc8 = Velocity.mc;
                Minecraft.thePlayer.motionY = 0.0;
                final Minecraft mc9 = Velocity.mc;
                Minecraft.thePlayer.motionZ = 0.0;
                Velocity.mc.timer.timerSpeed = 1.0f;
            }
        }
        else {
            final Minecraft mc10 = Velocity.mc;
            if (Minecraft.thePlayer.hurtTime == 8) {
                final Minecraft mc11 = Velocity.mc;
                if (Minecraft.thePlayer.onGround) {
                    final double motionX2 = 0.048;
                    final double motionZ2 = 0.036;
                    final Minecraft mc12 = Velocity.mc;
                    Minecraft.thePlayer.motionX = -motionX2 * 0.11;
                    final Minecraft mc13 = Velocity.mc;
                    Minecraft.thePlayer.motionZ = -motionZ2 * 0.11;
                    Velocity.mc.timer.timerSpeed = 0.9f;
                }
                else {
                    final Minecraft mc14 = Velocity.mc;
                    Minecraft.thePlayer.motionX = 0.0;
                    final Minecraft mc15 = Velocity.mc;
                    Minecraft.thePlayer.motionY = 0.0;
                    final Minecraft mc16 = Velocity.mc;
                    Minecraft.thePlayer.motionZ = 0.0;
                    Velocity.mc.timer.timerSpeed = 1.0f;
                }
            }
        }
    }
    
    private void Intave(final PreUpdate event) {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime > 0.85) {
            final Minecraft mc2 = Velocity.mc;
            Minecraft.thePlayer.moveForward = 0.0f;
            final Minecraft mc3 = Velocity.mc;
            Minecraft.thePlayer.capabilities.allowFlying = true;
            final Minecraft mc4 = Velocity.mc;
            double velocity = Minecraft.thePlayer.getRotationYawHead();
            velocity = -Math.toRadians(velocity);
            final double dX = -Math.sin(velocity) * 0.075;
            final Minecraft mc5 = Velocity.mc;
            Minecraft.thePlayer.motionX = dX;
        }
        else {
            final Minecraft mc6 = Velocity.mc;
            Minecraft.thePlayer.capabilities.allowFlying = false;
        }
    }
    
    private void IntaveOld() {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime > 0.0) {
            final Minecraft mc2 = Velocity.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Velocity.mc;
                double yaw = Minecraft.thePlayer.rotationYawHead;
                yaw = Math.toRadians(yaw);
                final double dX = Math.sin(yaw) * 0.0;
                final double dZ = -Math.sin(yaw) * 0.0;
                final Minecraft mc4 = Velocity.mc;
                Minecraft.thePlayer.motionX = dZ;
                final Minecraft mc5 = Velocity.mc;
                Minecraft.thePlayer.motionZ = dZ;
            }
        }
    }
    
    private void AAC(final PreUpdate event) {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime > 0.0) {
            final Minecraft mc2 = Velocity.mc;
            if (Minecraft.thePlayer.onGround) {
                final Minecraft mc3 = Velocity.mc;
                double yaw = Minecraft.thePlayer.rotationYawHead;
                yaw = Math.toRadians(yaw);
                final double dX = -Math.sin(yaw) * 0.045;
                final double dZ = -Math.sin(yaw) * 0.045;
                final Minecraft mc4 = Velocity.mc;
                Minecraft.thePlayer.motionX = dZ;
                final Minecraft mc5 = Velocity.mc;
                Minecraft.thePlayer.motionZ = dZ;
            }
        }
    }
    
    private void Reverse() {
        final Minecraft mc = Velocity.mc;
        if (!Minecraft.thePlayer.onGround) {
            final Minecraft mc2 = Velocity.mc;
            if (Minecraft.thePlayer.hurtTime > 0.0) {
                final Minecraft mc3 = Velocity.mc;
                Minecraft.thePlayer.onGround = true;
            }
        }
    }
    
    public void Push(final PreUpdate event) {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime > 0.0) {
            final Minecraft mc2 = Velocity.mc;
            double yaw = Minecraft.thePlayer.rotationYawHead;
            yaw = Math.toRadians(yaw);
            final double dX = -Math.sin(yaw) * 0.06;
            final double dZ = -Math.sin(yaw) * 0.06;
            final Minecraft mc3 = Velocity.mc;
            Minecraft.thePlayer.motionX = dZ;
            final Minecraft mc4 = Velocity.mc;
            Minecraft.thePlayer.motionZ = dZ;
        }
    }
    
    public void AAC2(final PreUpdate event) {
        final Minecraft mc = Velocity.mc;
        if (Minecraft.thePlayer.hurtTime > 0.0) {
            final Minecraft mc2 = Velocity.mc;
            final EntityPlayerSP thePlayer = Minecraft.thePlayer;
            final Minecraft mc3 = Velocity.mc;
            final double posX = Minecraft.thePlayer.posX;
            final Minecraft mc4 = Velocity.mc;
            final double y = Minecraft.thePlayer.posY - 0.26;
            final Minecraft mc5 = Velocity.mc;
            thePlayer.setPosition(posX, y, Minecraft.thePlayer.posZ);
            final Minecraft mc6 = Velocity.mc;
            final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
            final Minecraft mc7 = Velocity.mc;
            final double posX2 = Minecraft.thePlayer.posX;
            final Minecraft mc8 = Velocity.mc;
            final double y2 = Minecraft.thePlayer.posY + 0.3;
            final Minecraft mc9 = Velocity.mc;
            thePlayer2.setPosition(posX2, y2, Minecraft.thePlayer.posZ);
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        final Minecraft mc = Velocity.mc;
        Minecraft.thePlayer.capabilities.isFlying = false;
        final Minecraft mc2 = Velocity.mc;
        Minecraft.thePlayer.capabilities.allowFlying = false;
        Velocity.mc.timer.timerSpeed = 1.0f;
        final Minecraft mc3 = Velocity.mc;
        Minecraft.thePlayer.motionX = 0.0;
        final Minecraft mc4 = Velocity.mc;
        Minecraft.thePlayer.motionY = 0.0;
        final Minecraft mc5 = Velocity.mc;
        Minecraft.thePlayer.motionZ = 0.0;
        final Minecraft mc6 = Velocity.mc;
        Minecraft.thePlayer.onGround = false;
    }
}
