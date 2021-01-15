package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Module;
import me.onlyeli.ice.Category;
import net.minecraft.client.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

public class Regen extends Module
{
    public Regen() {
        super("Regen", Keyboard.KEY_NONE, Category.COMBAT);
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
    
    @Override
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        if (!Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode && Minecraft.getMinecraft().thePlayer.getFoodStats().getFoodLevel() > 17 && Minecraft.getMinecraft().thePlayer.getHealth() < 20.0f && Minecraft.getMinecraft().thePlayer.getHealth() != 0.0f && Minecraft.getMinecraft().thePlayer.onGround) {
            for (int i = 0; i < 1000; ++i) {
                Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
        super.onUpdate();
    }
}
