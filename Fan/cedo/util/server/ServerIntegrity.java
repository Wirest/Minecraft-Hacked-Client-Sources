package cedo.util.server;

import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerIntegrity {

    static Minecraft mc = Minecraft.getMinecraft();
    static boolean redirecting;

    public static boolean isGeniuneHypixel() {
        return isConnectedHypixel() && !redirecting;
    }

    public static boolean isConnectedHypixel() {
        if (mc.getCurrentServerData() == null)
            return false;

        String server = mc.isSingleplayer() ? "" : mc.getCurrentServerData().serverIP;
        return server.endsWith(".hypixel.net") || server.equals("hypixel.net");
    }

    public static boolean isHostsRedirectingHypixel() throws IOException {
        Path value = Paths.get(System.getenv("SystemDrive") + "\\Windows\\System32\\drivers\\etc\\hosts");
        return !Files.notExists(value) && Files.lines(value).anyMatch(s -> s.toLowerCase().contains("hypixel"));
    }

    public static void updateRedirecting() throws IOException {
        redirecting = isHostsRedirectingHypixel();
    }

}
