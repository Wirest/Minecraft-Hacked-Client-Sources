package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.event.impl.EventRenderGui;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.management.command.Command;
import info.sigmaclient.management.notifications.Notifications;
import info.sigmaclient.module.Module;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class AntiVanish extends Module {

    private List<UUID> vanished = new CopyOnWriteArrayList<>();
    private HashMap<UUID, String> uuidNames = new HashMap<>();
    private int delay = 20 * -160;

    public AntiVanish(ModuleData data) {
        super(data);
    }

    @RegisterEvent(events = {EventUpdate.class, EventPacket.class, EventRenderGui.class})
    public void onEvent(Event event) {

        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre() && mc.getCurrentServerData() != null) {
                String server = mc.getCurrentServerData().serverIP.toLowerCase();
                if (server.contains("hypixel") || server.contains("hive") || server.contains("mineplex")) {
                    Notifications.getManager().post("Auto Config", "AntiVanish disabled for spam reasons.", 1500, Notifications.Type.WARNING);
                    toggle();
                }
                if (!vanished.isEmpty()) {
                    if (delay > 20 * 160) {
                        vanished.clear();
                        Notifications.getManager().post("Vanish Cleared", "\247fVanish List has been \2476Cleared.", 2500L,
                                Notifications.Type.NOTIFY);
                        delay = 20 * -160;
                    } else {
                        delay++;
                    }
                }
                try {
                    for (UUID uuid : vanished) {
                        if (mc.getNetHandler().func_175102_a(uuid) != null && vanished.contains(uuid)) {
                            Notifications.getManager().post("Vanish Warning",
                                    "\247b" + mc.getNetHandler().func_175102_a(uuid).getDisplayName() + "is no longer \2476Vanished",
                                    2500L, Notifications.Type.NOTIFY);
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            Date date = new Date();
                            ChatUtil.printChat(Command.chatPrefix + "\2477[" + dateFormat.format(date) + "]\247f " + mc.getNetHandler().func_175102_a(uuid).getDisplayName() + "is no longer \247cVanished");
                        }
                        vanished.remove(uuid);
                    }
                } catch (Exception e) {
                    Notifications.getManager().post("Vanish Error", "\247cSomething happened.");
                }
            }
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isIncoming() && (mc.theWorld != null) && ((ep.getPacket() instanceof S38PacketPlayerListItem))) {
            	S38PacketPlayerListItem listItem = (S38PacketPlayerListItem) ep.getPacket();
                if (listItem.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
                    for (Object o : listItem.func_179767_a()) {
                        S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData) o;
                        if ((mc.getNetHandler().func_175102_a(data.field_179964_d.getId()) == null)
                                && (!checkList(data.field_179964_d.getId()))) {
                            Notifications.getManager().post("Vanish Warning",
                                    "\247bA player is \2476Vanished!", 2500L,
                                    Notifications.Type.WARNING);
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            Date date = new Date();
                            ChatUtil.printChat(Command.chatPrefix + "\2477[" + dateFormat.format(date) + "] \247cVanish Alert.");
                            delay = 20 * -160;
                        }
                    }
                } else if (listItem.func_179768_b() == S38PacketPlayerListItem.Action.REMOVE_PLAYER) {
                    for (Object o : listItem.func_179767_a()) {
                        S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData) o;
                        UUID uuid = data.field_179964_d.getId();
                        if(mc.getNetHandler().func_175102_a(uuid) != null && mc.getNetHandler().func_175102_a(uuid).getDisplayName() != null){
                        	ChatUtil.printChat("" +  mc.getNetHandler().func_175102_a(uuid));
                        }
                    }
                }	
            }
        }

    }

    private boolean checkList(final UUID uuid) {
        if (this.vanished.contains(uuid)) {
            return true;
        }
        this.vanished.add(uuid);
        return false;
    }
}
