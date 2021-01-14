package moonx.ohare.client.module.impl.other;

import moonx.ohare.client.event.bus.Handler;
import moonx.ohare.client.event.impl.game.PacketEvent;
import moonx.ohare.client.event.impl.game.TickEvent;
import moonx.ohare.client.event.impl.player.UpdateEvent;
import moonx.ohare.client.module.Module;
import moonx.ohare.client.utils.Printer;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

import java.awt.*;
import java.util.*;

public class VanishDetector extends Module {
    private Set<UUID> vanished = new HashSet<>();
    private HashMap<UUID, String> uuids = new HashMap<>();

    public VanishDetector() {
        super("VanishDetector", Category.OTHER, new Color(0xB5FFEE).getRGB());
        setDescription("no vanish niggas");
        setRenderLabel("Vanish Detector");
    }

    @Handler
    public void onTick(TickEvent event) {
        if (getMc().getNetHandler() != null) {
            getMc().getNetHandler().getRealPlayerInfoMap().values().forEach(info -> {
                if (info.getGameProfile().getName() != null) uuids.put(info.getGameProfile().getId(), info.getGameProfile().getName());
            });
        }
    }

    @Handler
    public void onPacket(PacketEvent event) {
        if (event.getPacket() instanceof S38PacketPlayerListItem) {
            S38PacketPlayerListItem packet = (S38PacketPlayerListItem) event.getPacket();
            if (packet.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
                packet.func_179767_a().forEach(data -> {
                    if (getMc().getNetHandler().getPlayerInfo(data.getProfile().getId()) == null) {
                        if (!vanished.contains(data.getProfile().getId()))
                            Printer.print(getName(data.getProfile().getId()) + " is now vanished.");
                        vanished.add(data.getProfile().getId());
                    }
                });
            }
        }
    }

    @Handler
    public void onUpdate(UpdateEvent event) {
        if(vanished != null) {
            vanished.forEach(uuid -> {
                if (getMc().getNetHandler().getPlayerInfo(uuid) != null)
                    Printer.print(getName(uuid) + " is no longer vanished.");
                    vanished.remove(uuid);
            });
        }
    }

    public String getName(UUID uuid) {
        if (uuids.containsKey(uuid)) {
            return uuids.get(uuid);
        }
        return "undefined - " + uuid.toString();
    }


}
