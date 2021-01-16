/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.module.modules.misc;

import darkmagician6.EventTarget;
import darkmagician6.events.EventMiddleClick;
import me.razerboy420.weepcraft.files.FriendsFile;
import me.razerboy420.weepcraft.friend.FriendManager;
import me.razerboy420.weepcraft.module.Module;
import me.razerboy420.weepcraft.util.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;

@Module.Mod(category=Module.Category.MISC, description="Middle click friends", key=0, name="MCF")
public class MiddleClickFriends
extends Module {
    @EventTarget
    public void onMiddleClick(EventMiddleClick event) {
        if (Minecraft.getMinecraft().objectMouseOver != null && Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer) {
            String name = Minecraft.getMinecraft().objectMouseOver.entityHit.getName();
            if (!FriendManager.isFriend(name)) {
                FriendManager.addFriend(name, name);
                Wrapper.tellPlayer("Added " + name + " as a friend.");
                FriendsFile.save();
                return;
            }
            FriendManager.removeFriend(name);
            Wrapper.tellPlayer("Removed " + name + ".");
            FriendsFile.save();
        }
    }
}

