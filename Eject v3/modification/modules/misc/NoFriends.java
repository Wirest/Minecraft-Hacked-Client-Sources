package modification.modules.misc;

import com.google.common.collect.Lists;
import modification.enummerates.Category;
import modification.events.EventTick;
import modification.extenders.Module;
import modification.interfaces.Event;
import modification.main.Modification;
import net.minecraft.client.network.NetworkPlayerInfo;

import java.util.Iterator;
import java.util.List;

public final class NoFriends
        extends Module {
    public List<NetworkPlayerInfo> playerInfoList;

    public NoFriends(String paramString, Category paramCategory) {
        super(paramString, paramCategory);
    }

    protected void onActivated() {
    }

    public void onEvent(Event paramEvent) {
        if ((paramEvent instanceof EventTick)) {
            this.playerInfoList = Lists.newArrayList();
            Iterator localIterator = MC.getNetHandler().getPlayerInfoMap().iterator();
            while (localIterator.hasNext()) {
                NetworkPlayerInfo localNetworkPlayerInfo = (NetworkPlayerInfo) localIterator.next();
                if ((Modification.FRIEND_MANAGER.containsFriend(localNetworkPlayerInfo.getGameProfile().getName())) || (IRC.IRC_FRIENDS.containsKey(localNetworkPlayerInfo.getGameProfile().getName()))) {
                    this.playerInfoList.add(localNetworkPlayerInfo);
                }
            }
        }
    }

    protected void onDeactivated() {
    }
}




