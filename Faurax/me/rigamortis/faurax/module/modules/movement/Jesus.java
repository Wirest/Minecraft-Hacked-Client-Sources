package me.rigamortis.faurax.module.modules.movement;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.*;
import me.rigamortis.faurax.values.*;
import me.rigamortis.faurax.module.modules.player.*;
import com.darkmagician6.eventapi.*;
import net.minecraft.network.play.client.*;
import me.rigamortis.faurax.events.*;
import org.lwjgl.input.*;
import net.minecraft.block.material.*;
import net.minecraft.client.entity.*;

public class Jesus extends Module implements MovementHelper
{
    private int delay;
    private boolean shouldCollide;
    private boolean isJumping;
    private boolean isSneaking;
    private boolean changePacket;
    public static Value mode;
    
    static {
        Jesus.mode = new Value("Jesus", Boolean.TYPE, "Mode", "New", new String[] { "Vanilla", "Old", "New", "Dolphin" });
    }
    
    public Jesus() {
        this.setName("Jesus");
        this.setKey("J");
        this.setType(ModuleType.MOVEMENT);
        this.setColor(-13448726);
        this.setModInfo("");
        this.setVisible(true);
        Client.getValues();
        ValueManager.values.add(Jesus.mode);
    }
    
    @Override
    public void onEnabled() {
        super.onEnabled();
        this.shouldCollide = true;
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
        this.shouldCollide = false;
        this.delay = 0;
    }
    
    @EventTarget
    public void getCollisionBoundingBox(final EventLiquidBoundingBox e) {
        if (this.isToggled()) {
            if (FreeCam.enabled) {
                return;
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("New") && this.shouldCollide) {
                e.setHeight(e.getHeight() + 0.9f);
                e.setCancelled(true);
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Old") && this.shouldCollide) {
                e.setHeight(e.getHeight() + 1.0f);
                e.setCancelled(true);
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Vanilla") && this.shouldCollide) {
                e.setHeight(e.getHeight() + 1.0f);
                e.setCancelled(true);
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Dolphin")) {
                e.setCancelled(false);
            }
        }
    }
    
    @EventTarget
    public void sendPacket(final EventSendPacket e) {
        if (this.isToggled()) {
            if (FreeCam.enabled) {
                return;
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Old") && this.shouldCollide && !this.isSneaking && e.getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer packet = (C03PacketPlayer)e.getPacket();
                if (this.changePacket) {
                    packet.y -= 0.009999999776482582;
                    this.changePacket = false;
                }
            }
        }
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            if (FreeCam.enabled) {
                return;
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("New")) {
                this.setModInfo(" §7New");
                if (Jesus.mc.thePlayer.fallDistance >= 2.0f) {
                    this.shouldCollide = false;
                }
                else {
                    this.shouldCollide = true;
                }
                if (Keyboard.isKeyDown(Jesus.mc.gameSettings.keyBindSneak.keyCode) && Jesus.mc.currentScreen == null) {
                    this.isSneaking = true;
                    this.shouldCollide = false;
                }
                else {
                    this.isSneaking = false;
                }
                if (!this.isSneaking && Jesus.movementUtils.isInLiquid() && !Jesus.movementUtils.isOnAir() && !Keyboard.isKeyDown(Jesus.mc.gameSettings.keyBindSneak.keyCode)) {
                    Jesus.mc.thePlayer.motionY = 0.10000000149011612;
                }
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Old")) {
                this.setModInfo(" §7Old");
                if (Jesus.mc.thePlayer.fallDistance >= 2.0f) {
                    this.shouldCollide = false;
                }
                else {
                    this.shouldCollide = true;
                }
                if (Keyboard.isKeyDown(Jesus.mc.gameSettings.keyBindSneak.keyCode) && Jesus.mc.currentScreen == null) {
                    this.isSneaking = true;
                    this.shouldCollide = false;
                }
                else {
                    this.isSneaking = false;
                }
                if (!this.isSneaking && Jesus.movementUtils.isInLiquid() && !Jesus.movementUtils.isOnAir() && !Keyboard.isKeyDown(Jesus.mc.gameSettings.keyBindSneak.keyCode)) {
                    Jesus.mc.thePlayer.motionY = 0.10000000149011612;
                }
                if (!this.isSneaking && Jesus.movementUtils.isOnLiquidOld() && !Jesus.movementUtils.isInLiquid()) {
                    ++this.delay;
                    if (this.delay >= 4) {
                        this.changePacket = true;
                        this.delay = 0;
                    }
                }
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Vanilla")) {
                this.setModInfo(" §7Vanilla");
                if (Jesus.mc.thePlayer.fallDistance >= 2.0f) {
                    this.shouldCollide = false;
                }
                else {
                    this.shouldCollide = true;
                }
                if (Keyboard.isKeyDown(Jesus.mc.gameSettings.keyBindSneak.keyCode) && Jesus.mc.currentScreen == null) {
                    this.isSneaking = true;
                    this.shouldCollide = false;
                }
                else {
                    this.isSneaking = false;
                }
                if (!this.isSneaking && Jesus.movementUtils.isInLiquid() && !Jesus.movementUtils.isOnAir() && !Keyboard.isKeyDown(Jesus.mc.gameSettings.keyBindSneak.keyCode)) {
                    Jesus.mc.thePlayer.motionY = 0.10000000149011612;
                }
            }
            if (Jesus.mode.getSelectedOption().equalsIgnoreCase("Dolphin")) {
                this.setModInfo(" §7Dolphin");
                if ((Jesus.mc.thePlayer.isInWater() || Jesus.mc.thePlayer.isInsideOfMaterial(Material.lava)) && !Jesus.mc.gameSettings.keyBindSneak.pressed && !Jesus.mc.gameSettings.keyBindJump.pressed) {
                    final EntityPlayerSP thePlayer = Jesus.mc.thePlayer;
                    thePlayer.motionY += 0.039;
                }
            }
        }
    }
}
