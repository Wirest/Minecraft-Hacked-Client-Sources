package me.onlyeli.ice.modules;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import net.minecraft.client.*;
import net.minecraft.block.material.*;
import net.minecraft.client.entity.*;

public class Glide extends Module
{
    public Glide() {
        super("Glide", Keyboard.KEY_NONE, Category.MOVEMENT);
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
        if (Minecraft.getMinecraft().thePlayer.motionY < 0.0 && Minecraft.getMinecraft().thePlayer.isAirBorne && !Minecraft.getMinecraft().thePlayer.isInWater() && !Minecraft.getMinecraft().thePlayer.isOnLadder() && !Minecraft.getMinecraft().thePlayer.isInsideOfMaterial(Material.lava)) {
            Minecraft.getMinecraft().thePlayer.motionY = -0.125;
            final EntityPlayerSP thePlayer = Minecraft.getMinecraft().thePlayer;
            thePlayer.jumpMovementFactor *= 1.21337f;
        }
        super.onUpdate();
    }
}
