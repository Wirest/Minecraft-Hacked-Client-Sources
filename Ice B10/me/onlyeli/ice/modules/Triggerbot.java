package me.onlyeli.ice.modules;

import me.onlyeli.ice.Module;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import net.minecraft.client.*;
import net.minecraft.util.*;
import net.minecraft.entity.player.*;
import net.minecraft.entity.*;

public class Triggerbot extends Module
{
    public Triggerbot() {
        super("Triggerbot", Keyboard.KEY_NONE, Category.COMBAT);
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
        if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityLivingBase) {
            final EntityLivingBase en = (EntityLivingBase)Minecraft.getMinecraft().objectMouseOver.entityHit;
            Minecraft.getMinecraft().thePlayer.swingItem();
            Minecraft.getMinecraft().playerController.attackEntity(Minecraft.getMinecraft().thePlayer, en);
        }
        super.onUpdate();
    }
}
