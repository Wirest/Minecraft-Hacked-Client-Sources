/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.MovingObjectPosition
 */
package me.aristhena.lucid.modules.misc;

import me.aristhena.lucid.eventapi.EventTarget;
import me.aristhena.lucid.eventapi.events.MouseEvent;
import me.aristhena.lucid.management.friend.FriendManager;
import me.aristhena.lucid.management.module.Mod;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.util.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;

@Mod
public class MiddleClickFriend
extends Module {
    @EventTarget
    private void onMouseClick(MouseEvent event) {
        if (event.key == 2 && this.mc.objectMouseOver != null && this.mc.objectMouseOver.entityHit != null && this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().objectMouseOver.entityHit;
            String name = player.getCommandSenderName();
            if (FriendManager.isFriend(name)) {
                FriendManager.removeFriend(name);
                ChatUtils.sendClientMessage("Removed: " + name);
            } else {
                FriendManager.addFriend(name, name);
                ChatUtils.sendClientMessage("Added: " + name);
            }
        }
    }
}

