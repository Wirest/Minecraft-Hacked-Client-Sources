package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.*;
import me.rigamortis.faurax.events.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import net.minecraft.client.entity.*;
import com.darkmagician6.eventapi.*;

public class InvMove extends Module
{
    public InvMove() {
        this.setKey("");
        this.setName("InvMove");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void preTick(final EventPreTick e) {
        if (this.isToggled() && InvMove.mc.currentScreen != null && !(InvMove.mc.currentScreen instanceof GuiChat)) {
            if (Keyboard.isKeyDown(17)) {
                InvMove.mc.gameSettings.keyBindForward.pressed = true;
            }
            else {
                InvMove.mc.gameSettings.keyBindForward.pressed = false;
            }
            if (Keyboard.isKeyDown(31)) {
                InvMove.mc.gameSettings.keyBindBack.pressed = true;
            }
            else {
                InvMove.mc.gameSettings.keyBindBack.pressed = false;
            }
            if (Keyboard.isKeyDown(32)) {
                InvMove.mc.gameSettings.keyBindRight.pressed = true;
            }
            else {
                InvMove.mc.gameSettings.keyBindRight.pressed = false;
            }
            if (Keyboard.isKeyDown(30)) {
                InvMove.mc.gameSettings.keyBindLeft.pressed = true;
            }
            else {
                InvMove.mc.gameSettings.keyBindLeft.pressed = false;
            }
            if (Keyboard.isKeyDown(203)) {
                final EntityPlayerSP thePlayer = InvMove.mc.thePlayer;
                thePlayer.rotationYaw -= 4.0f;
            }
            if (Keyboard.isKeyDown(205)) {
                final EntityPlayerSP thePlayer2 = InvMove.mc.thePlayer;
                thePlayer2.rotationYaw += 4.0f;
            }
            if (Keyboard.isKeyDown(200)) {
                final EntityPlayerSP thePlayer3 = InvMove.mc.thePlayer;
                thePlayer3.rotationPitch -= 4.0f;
            }
            if (Keyboard.isKeyDown(208)) {
                final EntityPlayerSP thePlayer4 = InvMove.mc.thePlayer;
                thePlayer4.rotationPitch += 4.0f;
            }
            if (InvMove.mc.thePlayer.rotationPitch >= 90.0f) {
                InvMove.mc.thePlayer.rotationPitch = 90.0f;
            }
            if (InvMove.mc.thePlayer.rotationPitch <= -90.0f) {
                InvMove.mc.thePlayer.rotationPitch = -90.0f;
            }
            if (Keyboard.isKeyDown(57) && InvMove.mc.thePlayer.onGround && !InvMove.mc.thePlayer.isInWater()) {
                InvMove.mc.thePlayer.jump();
            }
        }
    }
    
    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
