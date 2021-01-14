package modification.modules.misc;

import modification.enummerates.Category;
import modification.events.EventMouseClicked;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;
import modification.managers.FriendManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;

public final class MCF
        extends Module {
    public MCF(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventMouseClicked)) && (((EventMouseClicked) paramEvent).button == 2) && (MC.objectMouseOver != null) && (MC.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) && ((MC.objectMouseOver.entityHit instanceof EntityPlayer))) {
            EntityPlayer localEntityPlayer = (EntityPlayer) MC.objectMouseOver.entityHit;
            if (Modification.FRIEND_MANAGER.containsFriend(localEntityPlayer.getName())) {
                Modification.FRIEND_MANAGER.remove(localEntityPlayer.getName());
                Modification.LOG_UTIL.sendChatMessage("Removed §f".concat(localEntityPlayer.getName()));
                return;
            }
            Modification.FRIEND_MANAGER.add(localEntityPlayer.getName(), "Bot".concat(Integer.toString(FriendManager.FRIENDS.size() | 0x1)));
            Modification.LOG_UTIL.sendChatMessage("Added §f".concat(localEntityPlayer.getName()));
        }
    }

    protected void onDeactivated() {
    }
}




