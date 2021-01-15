package me.onlyeli.ice.modules;

import net.minecraft.entity.player.*;
import net.minecraft.client.entity.*;
import net.minecraft.entity.*;
import java.util.*;

import org.lwjgl.input.Keyboard;

import me.onlyeli.ice.Category;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.utils.Wrapper;

public class SafeAura extends Module
{
    public SafeAura() {
        super("SafeAura", Keyboard.KEY_NONE, Category.COMBAT);
    }
    
    public void onUpdate() {
        if (!this.isToggled()) {
            return;
        }
        for (final Object theObject : Wrapper.mc.theWorld.loadedEntityList) {
            if (theObject instanceof EntityPlayer) {
                final EntityPlayer entity = (EntityPlayer)theObject;
                if (entity instanceof EntityPlayerSP) {
                    continue;
                }
                if (Wrapper.mc.thePlayer.getDistanceToEntity(entity) > 6.2173615f || !entity.isEntityAlive()) {
                    continue;
                }
                Wrapper.mc.playerController.attackEntity(Wrapper.mc.thePlayer, entity);
                Wrapper.mc.thePlayer.swingItem();
            }
        }
        super.onUpdate();
    }
}
