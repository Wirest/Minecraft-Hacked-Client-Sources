package info.sigmaclient.module.impl.other;

import info.sigmaclient.event.Event;
import info.sigmaclient.event.impl.EventUpdate;
import info.sigmaclient.event.impl.EventPacket;
import info.sigmaclient.module.data.ModuleData;
import info.sigmaclient.module.data.Setting;
import info.sigmaclient.event.RegisterEvent;
import info.sigmaclient.module.Module;
import info.sigmaclient.util.misc.ChatUtil;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S02PacketChat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arithmo on 5/15/2017 at 12:07 AM.
 */
public class StreamerMode extends Module {

    private String NAMEPROTECT = "PROTECT";
    private String SCRAMBLE = "SCRAMBLE";
    private String HIDESCORE = "HIDESCORE";
    private String HIDETAB = "HIDETAB";
    private String SPOOFSKINS = "SPOOFSKINS";

    public static boolean scrambleNames;
    public static boolean hideTab;
    public static boolean hideScore;
    public static boolean spoofSkins;

    public StreamerMode(ModuleData data) {
        super(data);
        settings.put(NAMEPROTECT, new Setting<>(NAMEPROTECT, true, "Protects your name."));
        settings.put(SCRAMBLE, new Setting<>(SCRAMBLE, true, "Scrambles other player names."));
        settings.put(HIDESCORE, new Setting<>(HIDESCORE, false, "Hides scoreboard."));
        settings.put(SPOOFSKINS, new Setting<>(SPOOFSKINS, false, "Spoofs player skins."));
        settings.put(HIDETAB, new Setting<>(HIDETAB, false, "Hides tablist/player list."));
    }

    public static List<String> strings = new ArrayList<>();

    @Override
    public void onDisable() {
        super.onDisable();
        scrambleNames = false;
        hideScore = false;
        hideTab = false;
        spoofSkins = false;
        strings.clear();
    }

    @RegisterEvent(events = {EventUpdate.class, EventPacket.class})
    public void onEvent(Event event) {
        if (event instanceof EventUpdate) {
            EventUpdate em = (EventUpdate) event;
            if (em.isPre()) {
                scrambleNames = (Boolean) settings.get(SCRAMBLE).getValue();
                spoofSkins = (Boolean) settings.get(SPOOFSKINS).getValue();
                hideScore = (Boolean) settings.get(HIDESCORE).getValue();
                hideTab = (Boolean) settings.get(HIDETAB).getValue();
                final NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
                final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.func_175106_d());
                for (final Object o : players) {
                    final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
                    if (info == null) {
                        break;
                    }
                    if (!strings.contains(info.getGameProfile().getName())) {
                        strings.add(info.getGameProfile().getName());
                    }
                }
                for (Object o : mc.theWorld.getLoadedEntityList()) {
                    if (o instanceof EntityPlayer) {
                        if (!strings.contains(((EntityPlayer) o).getName())) {
                            strings.add(((EntityPlayer) o).getName());
                        }
                    }
                }
            }
        }
        if (event instanceof EventPacket) {
            EventPacket ep = (EventPacket) event;
            if (ep.isIncoming() && ep.getPacket() instanceof S02PacketChat && (Boolean) settings.get(NAMEPROTECT).getValue()) {
                S02PacketChat packet = (S02PacketChat) ep.getPacket();
                if (packet.func_148915_c().getUnformattedText().contains(mc.thePlayer.getName())) {
                    String temp = packet.func_148915_c().getFormattedText();
                    ChatUtil.printChat(temp.replaceAll(mc.thePlayer.getName(), "\2479You\247r"));
                    ep.setCancelled(true);
                } else {
                    String[] list = new String[]{"join", "left", "leave", "leaving", "lobby", "server", "fell", "died", "slain", "burn", "void", "disconnect", "kill", "by", "was", "quit", "blood", "game"};
                    for (String str : list) {
                        if (packet.func_148915_c().getUnformattedText().toLowerCase().contains(str)) {
                            ep.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        }
    }

}
