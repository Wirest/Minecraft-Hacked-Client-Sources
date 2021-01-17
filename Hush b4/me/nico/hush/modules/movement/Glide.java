// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.modules.movement;

import com.darkmagician6.eventapi.EventManager;
import me.nico.hush.utils.MoveUtils;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.client.Minecraft;
import com.darkmagician6.eventapi.EventTarget;
import me.nico.hush.events.EventUpdate;
import de.Hero.settings.Setting;
import me.nico.hush.Client;
import java.util.ArrayList;
import me.nico.hush.modules.Category;
import me.nico.hush.utils.TimeHelper;
import me.nico.hush.modules.Module;

public class Glide extends Module
{
    TimeHelper time;
    private int counter;
    private double prevV;
    
    public Glide() {
        super("Glide", "Glide", 1572112, 44, Category.MOVEMENT);
        this.time = new TimeHelper();
        this.counter = 0;
        this.prevV = 0.0;
        final ArrayList<String> mode = new ArrayList<String>();
        mode.add("MC-Central");
        mode.add("Hypixel");
        mode.add("NoDown");
        mode.add("Intave");
        mode.add("CC");
        Client.instance.settingManager.rSetting(new Setting("Mode", "GlideMode", this, "NoDown", mode));
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (Client.instance.settingManager.getSettingByName("GlideMode").getValString().equalsIgnoreCase("NoDown")) {
            this.setDisplayname("Glide");
            this.NoDown();
        }
        else if (Client.instance.settingManager.getSettingByName("GlideMode").getValString().equalsIgnoreCase("Intave")) {
            this.setDisplayname("Glide");
            this.Intave();
        }
        else if (Client.instance.settingManager.getSettingByName("GlideMode").getValString().equalsIgnoreCase("MC-Central")) {
            this.setDisplayname("Glide");
            this.MCCentral();
        }
        else if (Client.instance.settingManager.getSettingByName("GlideMode").getValString().equalsIgnoreCase("Hypixel")) {
            this.setDisplayname("Glide");
            this.Hypixel();
        }
        else if (Client.instance.settingManager.getSettingByName("GlideMode").getValString().equalsIgnoreCase("CC")) {
            this.setDisplayname("Glide");
            this.CC();
        }
    }
    
    private void CC() {
        final Minecraft mc = Glide.mc;
        if (Minecraft.thePlayer.onGround) {
            Glide.mc.timer.timerSpeed = 1.0f;
        }
        else {
            final Minecraft mc2 = Glide.mc;
            Minecraft.thePlayer.motionY = -0.001;
            Glide.mc.timer.timerSpeed = 0.8f;
        }
    }
    
    private void setMotion(final double speed) {
        final Minecraft mc = Glide.mc;
        double forward = Minecraft.thePlayer.movementInput.moveForward;
        final Minecraft mc2 = Glide.mc;
        double strafe = Minecraft.thePlayer.movementInput.moveStrafe;
        final Minecraft mc3 = Glide.mc;
        float yaw = Minecraft.thePlayer.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            final Minecraft mc4 = Glide.mc;
            Minecraft.thePlayer.motionX = 0.0;
            final Minecraft mc5 = Glide.mc;
            Minecraft.thePlayer.motionZ = 0.0;
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
            final Minecraft mc6 = Glide.mc;
            Minecraft.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f));
            final Minecraft mc7 = Glide.mc;
            Minecraft.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f));
        }
    }
    
    private void Hypixel() {
        final Minecraft mc = Glide.mc;
        Minecraft.thePlayer.motionY = 0.0;
        final Minecraft mc2 = Glide.mc;
        if (Minecraft.thePlayer.ticksExisted % 3 == 0) {
            final Minecraft mc3 = Glide.mc;
            final double y = Minecraft.thePlayer.posY - 1.0E-10;
            final Minecraft mc4 = Glide.mc;
            final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
            final Minecraft mc5 = Glide.mc;
            final double posX = Minecraft.thePlayer.posX;
            final Minecraft mc6 = Glide.mc;
            final double posY = Minecraft.thePlayer.posY;
            final Minecraft mc7 = Glide.mc;
            sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, Minecraft.thePlayer.posZ, true));
        }
        final Minecraft mc8 = Glide.mc;
        final double y2 = Minecraft.thePlayer.posY + 1.0E-10;
        final Minecraft mc9 = Glide.mc;
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        final Minecraft mc10 = Glide.mc;
        final double posX2 = Minecraft.thePlayer.posX;
        final double y3 = y2;
        final Minecraft mc11 = Glide.mc;
        thePlayer.setPosition(posX2, y3, Minecraft.thePlayer.posZ);
    }
    
    private void Intave() {
        final Minecraft mc = Glide.mc;
        if (Minecraft.thePlayer.ticksExisted % 1 == 0) {
            final Minecraft mc2 = Glide.mc;
            Minecraft.thePlayer.onGround = true;
            final Minecraft mc3 = Glide.mc;
            Minecraft.thePlayer.capabilities.isFlying = true;
            final Minecraft mc4 = Glide.mc;
            double yaw = Minecraft.thePlayer.posY;
            yaw = Math.toRadians(yaw);
            final double dY = Math.sin(yaw) * 0.28;
            final Minecraft mc5 = Glide.mc;
            Minecraft.thePlayer.motionY = dY;
            final Minecraft mc6 = Glide.mc;
            if (!Minecraft.thePlayer.isInWater()) {
                final Minecraft mc7 = Glide.mc;
                Minecraft.thePlayer.ticksExisted = 15;
                final Minecraft mc8 = Glide.mc;
                Minecraft.thePlayer.isInWater();
                final Minecraft mc9 = Glide.mc;
                Minecraft.thePlayer.motionY = 0.0;
            }
        }
    }
    
    private void NoDown() {
        final Minecraft mc = Glide.mc;
        Minecraft.thePlayer.motionY = 0.0;
        final Minecraft mc2 = Glide.mc;
        if (Minecraft.thePlayer.ticksExisted % 5 == 0) {
            final Minecraft mc3 = Glide.mc;
            final double y = Minecraft.thePlayer.posY - 1.0E-10;
            final Minecraft mc4 = Glide.mc;
            final NetHandlerPlayClient sendQueue = Minecraft.thePlayer.sendQueue;
            final Minecraft mc5 = Glide.mc;
            final double posX = Minecraft.thePlayer.posX;
            final Minecraft mc6 = Glide.mc;
            final double posY = Minecraft.thePlayer.posY;
            final Minecraft mc7 = Glide.mc;
            sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(posX, posY, Minecraft.thePlayer.posZ, true));
        }
        final Minecraft mc8 = Glide.mc;
        final double y2 = Minecraft.thePlayer.posY + 1.0E-10;
        final Minecraft mc9 = Glide.mc;
        final EntityPlayerSP thePlayer = Minecraft.thePlayer;
        final Minecraft mc10 = Glide.mc;
        final double posX2 = Minecraft.thePlayer.posX;
        final double y3 = y2;
        final Minecraft mc11 = Glide.mc;
        thePlayer.setPosition(posX2, y3, Minecraft.thePlayer.posZ);
    }
    
    public static void messageWithoutPrefix(final String msg) {
        final Object chat = new ChatComponentText(msg);
        if (msg != null) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((IChatComponent)chat);
        }
    }
    
    public static void messageWithPrefix(final String msg) {
        messageWithoutPrefix(String.valueOf(Client.instance.ClientPrefix) + msg);
    }
    
    public void MCCentral() {
        final Minecraft mc = Glide.mc;
        if (Minecraft.thePlayer.onGround) {
            final Minecraft mc2 = Glide.mc;
            Minecraft.thePlayer.jump();
            Glide.mc.timer.timerSpeed = 1.0f;
        }
        else {
            final Minecraft mc3 = Glide.mc;
            if (Minecraft.thePlayer.fallDistance > 0.1f) {
                final MoveUtils m = new MoveUtils();
                boolean boost = false;
                if (boost) {
                    Glide.mc.timer.timerSpeed = 0.15f;
                    final double x = MoveUtils.getPosForSetPosX(3.05);
                    final double z = MoveUtils.getPosForSetPosZ(3.05);
                    final Minecraft mc4 = Glide.mc;
                    final EntityPlayerSP thePlayer = Minecraft.thePlayer;
                    final Minecraft mc5 = Glide.mc;
                    final double x2 = Minecraft.thePlayer.posX + x;
                    final Minecraft mc6 = Glide.mc;
                    final double y = Minecraft.thePlayer.posY - 0.20000000298023224;
                    final Minecraft mc7 = Glide.mc;
                    thePlayer.setPosition(x2, y, Minecraft.thePlayer.posZ + z);
                    boost = false;
                }
                Glide.mc.timer.timerSpeed = 1.85f;
                final Minecraft mc8 = Glide.mc;
                Minecraft.thePlayer.motionY = 0.25;
                final double x = MoveUtils.getPosForSetPosX(0.75);
                final double z = MoveUtils.getPosForSetPosZ(0.75);
                final Minecraft mc9 = Glide.mc;
                final EntityPlayerSP thePlayer2 = Minecraft.thePlayer;
                final Minecraft mc10 = Glide.mc;
                final double x3 = Minecraft.thePlayer.posX + x;
                final Minecraft mc11 = Glide.mc;
                final double y2 = Minecraft.thePlayer.posY - 0.006000000052154064;
                final Minecraft mc12 = Glide.mc;
                thePlayer2.setPosition(x3, y2, Minecraft.thePlayer.posZ + z);
            }
        }
    }
    
    @Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
        final Minecraft mc = Glide.mc;
        this.prevV = Minecraft.thePlayer.posY;
        Glide.mc.timer.timerSpeed = 1.0f;
        final Minecraft mc2 = Glide.mc;
        Minecraft.thePlayer.speedInAir = 0.02f;
        final Minecraft mc3 = Glide.mc;
        Minecraft.thePlayer.motionX = 0.0;
        final Minecraft mc4 = Glide.mc;
        Minecraft.thePlayer.motionZ = 0.0;
        final Minecraft mc5 = Glide.mc;
        Minecraft.thePlayer.motionY = 0.0;
        final Minecraft mc6 = Glide.mc;
        Minecraft.thePlayer.onGround = false;
        final Minecraft mc7 = Glide.mc;
        Minecraft.thePlayer.capabilities.isFlying = false;
        final Minecraft mc8 = Glide.mc;
        Minecraft.thePlayer.capabilities.allowFlying = false;
    }
}
