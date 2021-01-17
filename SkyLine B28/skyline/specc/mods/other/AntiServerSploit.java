package skyline.specc.mods.other;
import net.minecraft.client.Mineman;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.C19PacketResourcePackStatus;
import net.minecraft.network.play.server.S48PacketResourcePackSend;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.skyline.event.eventtypes.EventPacket;

import java.net.URI;
import java.net.URISyntaxException;
/*
 * @author Sk1er
 * @credits : Specc, Making Sk1er's mod compatable
 * to prevent Server exploit.
 * 
 * */
public class AntiServerSploit {
    private NetworkManager netManager;
    private void handle(S48PacketResourcePackSend packetIn, EventPacket event) {
        if (!validateResourcePackUrl(packetIn.url, packetIn.hash))
            event.setCancelled(true);
    }
private boolean validateResourcePackUrl(String url, String hash) {
try {
URI uri = new URI(url);
String scheme = uri.getScheme();
boolean isLevelProtocol = "level".equals(scheme);
if (!"http".equals(scheme) && !"https".equals(scheme) && !isLevelProtocol) {
netManager.sendPacket(new C19PacketResourcePackStatus
(hash, C19PacketResourcePackStatus.Action.FAILED_DOWNLOAD));
throw new URISyntaxException(url, "Wrong protocol");
}
if (isLevelProtocol && (url.contains("..") || !url.endsWith("/resources.zip"))) {
EntityPlayerSP thePlayer = Mineman.getMinecraft().thePlayer;
if (thePlayer != null) {
thePlayer.addChatMessage
(new ChatComponentText
(EnumChatFormatting.RED + "SkyLine AntiZeroDay"
+ EnumChatFormatting.DARK_RED + ">>"
+ EnumChatFormatting.BOLD.toString() + EnumChatFormatting.GOLD +
"Server attempted to download files on this PC! Location:" + url));
}
throw new URISyntaxException(url, "Invalid levelstorage resourcepack path");
}
return true;
} catch (URISyntaxException e) {
return false;
}
}
}
