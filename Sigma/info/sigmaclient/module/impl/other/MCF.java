package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventMouse;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.friend.Friend;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Mouse;

/**
 * Created by Arithmo on 8/10/2017 at 1:53 AM.
 */
public class MCF extends Module {

    public MCF(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events = EventMouse.class)
    public void onEvent(Event event) {
        EventMouse em = (EventMouse) event;
        if (em.getButtonID() == 2 && Mouse.getEventButtonState() && mc.objectMouseOver.entityHit != null && mc.objectMouseOver.entityHit instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer) mc.objectMouseOver.entityHit;
            if (FriendManager.isFriend(entityPlayer.getName())) {
                ChatUtil.printChat(Command.chatPrefix + "\247b" + entityPlayer.getName() + "\2477 has been \247cunfriended.");
                FriendManager.removeFriend(entityPlayer.getName());
            } else {
                ChatUtil.printChat(Command.chatPrefix + "\247b" + entityPlayer.getName() + "\2477 has been \247afriended.");
                FriendManager.addFriend(entityPlayer.getName(), entityPlayer.getName());
            }
        }
    }

}
