package net.minecraft.util;

import org.lwjgl.input.Keyboard;

import info.sigmaclient.Client;
import info.sigmaclient.management.agora.GuiAgoraIngame;
import info.sigmaclient.module.data.Options;
import info.sigmaclient.module.impl.combat.AntiBot;
import info.sigmaclient.module.impl.player.InventoryWalk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.GameSettings;

public class MovementInputFromOptions extends MovementInput {
    private final GameSettings gameSettings;
    private static final String __OBFID = "CL_00000937";

    public MovementInputFromOptions(GameSettings p_i1237_1_) {
        gameSettings = p_i1237_1_;
    }

    public void updatePlayerMoveState() {
        InventoryWalk x = (InventoryWalk) Client.getModuleManager().get(InventoryWalk.class);
        if (x.isEnabled() && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)&& !(Minecraft.getMinecraft().currentScreen instanceof GuiAgoraIngame)) {
            if(((Options) x.getSetting(InventoryWalk.MODE).getValue()).getSelected().equals("Cubecraft") && Minecraft.getMinecraft().currentScreen instanceof GuiChest){

                this.moveStrafe = 0.0F;
                this.moveForward = 0.0F;
                if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
                    this.moveForward += 1.0F;
                }
                if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
                    this.moveForward -= 1.0F;
                }
                if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
                    this.moveStrafe += 1.0F;
                }
                if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
                    this.moveStrafe -= 1.0F;
                }
                this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
                this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
                if (this.sneak) {
                    this.moveStrafe = ((float) (this.moveStrafe * 0.3D));
                    this.moveForward = ((float) (this.moveForward * 0.3D));
                }
                return;
            }
        	this.moveStrafe = 0.0F;
            this.moveForward = 0.0F;
            if (Keyboard.isKeyDown(this.gameSettings.keyBindForward.getKeyCode())) {
                this.moveForward += 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindBack.getKeyCode())) {
                this.moveForward -= 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindLeft.getKeyCode())) {
                this.moveStrafe += 1.0F;
            }
            if (Keyboard.isKeyDown(this.gameSettings.keyBindRight.getKeyCode())) {
                this.moveStrafe -= 1.0F;
            }
            this.jump = Keyboard.isKeyDown(this.gameSettings.keyBindJump.getKeyCode());
            this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
            if (this.sneak) {
                this.moveStrafe = ((float) (this.moveStrafe * 0.3D));
                this.moveForward = ((float) (this.moveForward * 0.3D));
            }
        } else {
            this.moveStrafe = 0.0F;
            this.moveForward = 0.0F;
            if (this.gameSettings.keyBindForward.getIsKeyPressed()) {
                this.moveForward += 1.0F;
            }
            if (this.gameSettings.keyBindBack.getIsKeyPressed()) {
                this.moveForward -= 1.0F;
            }
            if (this.gameSettings.keyBindLeft.getIsKeyPressed()) {
                this.moveStrafe += 1.0F;
            }
            if (this.gameSettings.keyBindRight.getIsKeyPressed()) {
                this.moveStrafe -= 1.0F;
            }
            this.jump = this.gameSettings.keyBindJump.getIsKeyPressed();
            this.sneak = this.gameSettings.keyBindSneak.getIsKeyPressed();
            if (this.sneak) {
                this.moveStrafe = ((float) (this.moveStrafe * 0.3D));
                this.moveForward = ((float) (this.moveForward * 0.3D));
            }
        }
    }

}
