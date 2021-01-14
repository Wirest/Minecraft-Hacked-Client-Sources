package cn.kody.debug.mod.mods.PLAYER;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.types.EventType;

import cn.kody.debug.Client;
import cn.kody.debug.events.EventPreMotion;
import cn.kody.debug.friend.FriendsManager;
import cn.kody.debug.mod.Category;
import cn.kody.debug.mod.Mod;
import cn.kody.debug.utils.Friend;
import cn.kody.debug.utils.handler.MouseInputHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class MidClickFriend extends Mod
{
    private MouseInputHandler handler;
    
    public MidClickFriend() {
        super("MCF", "Mid Click Friend", Category.PLAYER);
        this.handler = new MouseInputHandler(2);
    }
    
    @EventTarget
    public void onMotion(final EventPreMotion eventMotion) {
        if (eventMotion.getEventType() == EventType.PRE 
        	&& this.mc.objectMouseOver != null 
        	&& this.mc.objectMouseOver.entityHit != null 
        	&& this.mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            final String name = this.mc.objectMouseOver.entityHit.getName();
            if (this.handler.canExcecute()) {
                if (FriendsManager.isFriend(name)) {
                    int i = 0;
                    while (i < FriendsManager.getFriends().size()) {
                        if (((Friend)FriendsManager.getFriends().get(i)).getName().equalsIgnoreCase(name)) {
                            FriendsManager.getFriends().remove(i);
                        }
                        ++i;
                    }
                }
                else {
                    FriendsManager.getFriends().add(new Friend(name));
                }
                Client.instance.fileMgr.saveFriends();
            }
        }
    }
}
