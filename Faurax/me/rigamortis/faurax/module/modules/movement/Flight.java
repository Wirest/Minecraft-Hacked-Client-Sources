package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;
import net.minecraft.block.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.entity.*;

public class Flight extends Module implements PlayerHelper, WorldHelper
{
    private double flyHeight;
    private double startY;
    private int delay;
    public static boolean enabled;
    public static Value types;
    public static Value speed;
    public static Value antiKick;
    
    static {
        Flight.types = new Value("Flight", Boolean.TYPE, "Type", "Normal", new String[] { "Normal", "AAC", "MotionY" });
        Flight.speed = new Value("Flight", Float.TYPE, "Speed", 0.3f, 0.1f, 2.0f);
        Flight.antiKick = new Value("Flight", Boolean.TYPE, "AntiKick", false);
    }
    
    public Flight() {
        this.setName("Flight");
        this.setKey("F");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Flight.types);
        Client.getValues();
        ValueManager.values.add(Flight.speed);
        Client.getValues();
        ValueManager.values.add(Flight.antiKick);
    }
    
    @EventTarget
    public void addAirBB(final EventAddAirBB e) {
        if (this.isToggled() && Flight.types.getSelectedOption().equalsIgnoreCase("Normal")) {
            e.setCancelled(true);
            if (Flight.mc.gameSettings.keyBindJump.pressed || Flight.mc.gameSettings.keyBindSneak.pressed) {
                e.setCancelled(false);
            }
        }
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        this.startY = Flight.mc.thePlayer.posY;
        if (!Flight.types.getSelectedOption().equalsIgnoreCase("Normal") && Flight.mc.thePlayer.onGround && Flight.mc.currentScreen == null) {
            Client.getClientHelper().damage();
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        Flight.enabled = false;
    }
    
    public void updateFlyHeight() {
        double h = 1.0;
        final AxisAlignedBB box = Flight.mc.thePlayer.getEntityBoundingBox().expand(0.0625, 0.0625, 0.0625);
        this.flyHeight = 0.0;
        while (this.flyHeight < Flight.mc.thePlayer.posY) {
            final AxisAlignedBB nextBox = box.offset(0.0, -this.flyHeight, 0.0);
            if (Flight.mc.theWorld.checkBlockCollision(nextBox)) {
                if (h < 0.0625) {
                    break;
                }
                this.flyHeight -= h;
                h /= 2.0;
            }
            this.flyHeight += h;
        }
    }
    
    public void goToGround() {
        if (this.flyHeight > 300.0) {
            return;
        }
        final double minY = Flight.mc.thePlayer.posY - this.flyHeight;
        if (minY <= 0.0) {
            return;
        }
        double y = Flight.mc.thePlayer.posY;
        while (y > minY) {
            y -= 8.0;
            if (y < minY) {
                y = minY;
            }
            final C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(Flight.mc.thePlayer.posX, y, Flight.mc.thePlayer.posZ, true);
            Flight.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
        y = minY;
        while (y < Flight.mc.thePlayer.posY) {
            y += 8.0;
            if (y > Flight.mc.thePlayer.posY) {
                y = Flight.mc.thePlayer.posY;
            }
            final C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(Flight.mc.thePlayer.posX, y, Flight.mc.thePlayer.posZ, true);
            Flight.mc.thePlayer.sendQueue.addToSendQueue(packet);
        }
    }
    
    public double unitUntilFloor() {
        double lowestY = Flight.mc.thePlayer.posY;
        for (double i = Flight.mc.thePlayer.posY; i > 0.0; i -= 0.1) {
            if (!(Client.getClientHelper().getBlock(Flight.mc.thePlayer.posX, i, Flight.mc.thePlayer.posZ) instanceof BlockAir)) {
                lowestY = i;
                break;
            }
        }
        return Flight.mc.thePlayer.posY - lowestY;
    }
    
    public void setMoveSpeed(final EventMove event, final double speed) {
        final MovementInput movementInput = Flight.mc.thePlayer.movementInput;
        double forward = movementInput.moveForward;
        double strafe = movementInput.moveStrafe;
        float yaw = Flight.mc.thePlayer.rotationYaw;
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
    public void moveEntity(final EventMove event) {
        if (this.isToggled() && Flight.types.getSelectedOption().equalsIgnoreCase("Normal")) {
            this.setMoveSpeed(event, Flight.speed.getFloatValue());
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (Flight.types.getSelectedOption().equalsIgnoreCase("Normal")) {
                Flight.enabled = true;
                this.setModInfo(" §7Normal");
                if (Flight.antiKick.getBooleanValue()) {
                    this.updateMS();
                }
                Flight.mc.thePlayer.capabilities.isFlying = false;
                if (Flight.mc.gameSettings.keyBindJump.pressed) {
                    final EntityPlayerSP thePlayer = Flight.mc.thePlayer;
                    thePlayer.motionY += Flight.speed.getFloatValue();
                }
                if (Flight.mc.gameSettings.keyBindSneak.pressed) {
                    final EntityPlayerSP thePlayer2 = Flight.mc.thePlayer;
                    thePlayer2.motionY -= Flight.speed.getFloatValue();
                }
                if (Flight.antiKick.getBooleanValue()) {
                    this.updateFlyHeight();
                    Flight.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                    if ((this.flyHeight <= 290.0 && this.hasTimePassedM(500L)) || (this.flyHeight > 290.0 && this.hasTimePassedM(100L))) {
                        this.goToGround();
                        this.updateLastMS();
                    }
                }
            }
            if (Flight.types.getSelectedOption().equalsIgnoreCase("MotionY")) {
                this.setModInfo(" §7MotionY");
                Flight.mc.thePlayer.motionY = 0.0;
                if (Flight.mc.gameSettings.keyBindJump.pressed) {
                    final EntityPlayerSP thePlayer3 = Flight.mc.thePlayer;
                    thePlayer3.motionY += Flight.speed.getFloatValue();
                }
                if (Flight.mc.gameSettings.keyBindSneak.pressed) {
                    final EntityPlayerSP thePlayer4 = Flight.mc.thePlayer;
                    thePlayer4.motionY -= Flight.speed.getFloatValue();
                }
            }
            if (Flight.types.getSelectedOption().equalsIgnoreCase("AAC")) {
                this.setModInfo(" §7AAC");
                Flight.mc.thePlayer.motionY = -0.03999999910593033;
                if (Flight.mc.gameSettings.keyBindJump.pressed) {
                    final EntityPlayerSP thePlayer5 = Flight.mc.thePlayer;
                    thePlayer5.motionY += Flight.speed.getFloatValue();
                }
                if (Flight.mc.gameSettings.keyBindSneak.pressed) {
                    final EntityPlayerSP thePlayer6 = Flight.mc.thePlayer;
                    thePlayer6.motionY -= Flight.speed.getFloatValue();
                }
            }
            if (Flight.types.getSelectedOption().equalsIgnoreCase("Current")) {
                this.setModInfo(" §7Current");
                if (Flight.mc.gameSettings.keyBindJump.pressed) {
                    final EntityPlayerSP thePlayer7 = Flight.mc.thePlayer;
                    thePlayer7.motionY += Flight.speed.getFloatValue();
                }
                if (Flight.mc.gameSettings.keyBindSneak.pressed) {
                    final EntityPlayerSP thePlayer8 = Flight.mc.thePlayer;
                    thePlayer8.motionY -= Flight.speed.getFloatValue();
                }
            }
        }
    }
}
