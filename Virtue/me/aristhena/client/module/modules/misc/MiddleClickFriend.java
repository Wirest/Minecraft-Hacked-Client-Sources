// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.event.EventTarget;
import me.aristhena.client.friend.FriendManager;
import net.minecraft.entity.player.EntityPlayer;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.MouseEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Middle Click Friend")
public class MiddleClickFriend extends Module
{
    @EventTarget
    private void onMouseClick(final MouseEvent event) {
        if (event.getKey() == 2 && ClientUtils.mc().objectMouseOver != null && ClientUtils.mc().objectMouseOver.entityHit != null && ClientUtils.mc().objectMouseOver.entityHit instanceof EntityPlayer) {
            final EntityPlayer player = (EntityPlayer)ClientUtils.mc().objectMouseOver.entityHit;
            final String name = player.getName();
            if (FriendManager.isFriend(name)) {
                FriendManager.removeFriend(name);
                ClientUtils.sendMessage("Removed " + name);
            }
            else {
                FriendManager.addFriend(name, name);
                ClientUtils.sendMessage("Added " + name);
            }
        }
    }
}
