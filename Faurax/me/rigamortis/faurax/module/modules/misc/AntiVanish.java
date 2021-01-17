package me.rigamortis.faurax.module.modules.misc;

import me.rigamortis.faurax.module.helpers.*;
import me.rigamortis.faurax.module.*;
import net.minecraft.network.play.server.*;
import net.minecraft.client.*;
import net.minecraft.util.*;
import java.util.*;
import com.darkmagician6.eventapi.*;
import me.rigamortis.faurax.events.*;
import java.io.*;
import java.net.*;

public class AntiVanish extends Module implements PlayerHelper
{
    private ArrayList<UUID> vanished;
    private int delay;
    
    public AntiVanish() {
        this.vanished = new ArrayList<UUID>();
        this.setName("AntiVanish");
        this.setKey("");
        this.setType(ModuleType.MISC);
        this.setColor(-2836728);
        this.setModInfo("");
        this.setVisible(true);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.isToggled() && AntiVanish.mc.theWorld != null && e.getPacket() instanceof S38PacketPlayerListItem) {
            final S38PacketPlayerListItem listItem = (S38PacketPlayerListItem)e.getPacket();
            if (listItem.func_179768_b() == S38PacketPlayerListItem.Action.UPDATE_LATENCY) {
                for (final Object o : listItem.func_179767_a()) {
                	S38PacketPlayerListItem.AddPlayerData data = (S38PacketPlayerListItem.AddPlayerData)o;
                    if (AntiVanish.mc.getNetHandler().func_175102_a(data.field_179964_d.getId()) == null && !this.checkList(data.field_179964_d.getId())) {
                        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§f" + this.getName(data.field_179964_d.getId()) + "is vanished."));
                    }
                }
            }
        }
    }
    
    @EventTarget(0)
    public void preTick(final EventPreTick e) {
        if (this.isToggled()) {
            this.setModInfo(" §7" + AntiVanish.mc.thePlayer.sendQueue.playerInfoMap.size());
            for (final UUID uuid : this.vanished) {
                if (AntiVanish.mc.getNetHandler().func_175102_a(uuid) != null) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§9Faurax§7]:§f" + this.getName(uuid) + "is no longer vanished."));
                    this.vanished.remove(uuid);
                }
            }
        }
    }
    
    public String getName(final UUID uuid) {
        try {
            final URL url = new URL("https://namemc.com/profile/" + uuid.toString());
            final URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.7; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String name = null;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("<title>")) {
                    name = line.split("§")[0].trim().replaceAll("<title>", "").replaceAll("</title>", "").replaceAll("\u2013 Minecraft Profile \u2013 NameMC", "").replaceAll("\u00e2\u20ac\u201c Minecraft Profile \u00e2\u20ac\u201c NameMC", "");
                }
            }
            reader.close();
            return name;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "(Failed) " + uuid;
        }
    }
    
    private boolean checkList(final UUID uuid) {
        if (this.vanished.contains(uuid)) {
            this.vanished.remove(uuid);
            return true;
        }
        this.vanished.add(uuid);
        return false;
    }
}
