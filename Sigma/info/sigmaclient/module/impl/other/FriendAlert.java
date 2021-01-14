/**
 * Time: 8:22:36 PM
 * Date: Jan 5, 2017
 * Creator: cool1
 */
package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.management.friend.FriendManager;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.management.notifications.Notifications.Type;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.Timer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

/**
 * @author cool1
 */
public class FriendAlert extends Module {

    private boolean connect;
    private String name;
    private int currentY, targetY;
    Timer timer = new Timer();

    public FriendAlert(ModuleData data) {
        super(data);
        // TODO Auto-generated constructor stub
    }

    @Override
    @RegisterEvent(events = {EventPacket.class})
    public void onEvent(Event event) {
        EventPacket ep = (EventPacket) event;
        if (ep.isIncoming() && ep.getPacket() instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem packet = (S38PacketPlayerListItem) ep.getPacket();
            if (packet.func_179768_b() == S38PacketPlayerListItem.Action.ADD_PLAYER) {
                for (Object o : packet.func_179767_a()) {
                    S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData) o;
                    if (FriendManager.isFriend(data.field_179964_d.getName())) {
                        Notifications.getManager().post("Friend Alert", "\247b" + data.field_179964_d.getName() + " has joined!", 2500L, Type.INFO);
                    }
                }
            }
        }
    }
}

