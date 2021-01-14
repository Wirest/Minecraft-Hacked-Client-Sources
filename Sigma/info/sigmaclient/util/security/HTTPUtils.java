package info.sigmaclient.util.security;

import com.sun.net.ssl.internal.ssl.Provider;
import info.sigmaclient.management.Authentication;
import info.sigmaclient.util.FileUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.Security;

/**
 * Created by Arithmo on 5/29/2017 at 3:43 PM.
 */
public class HTTPUtils {

    public static String doConnection(String link) throws Exception {
        URL configURL = new URL(link);
        final BufferedReader in = new BufferedReader(new InputStreamReader(configURL.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            if (str.equalsIgnoreCase(PlayerControllerMP.getHwid())) {
                return "{\"HWID\": \"" + str + "\" }";
            }
        }
        in.close();
        return "Unset HWID XD?";
    }

    public static String doConnection(URL url) throws Exception {
        final BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String str;
        while ((str = in.readLine()) != null) {
            if (str.getBytes() == (PlayerControllerMP.getHwid().getBytes())) {
                return "{\"HWID\": \"" + str + "\" }";
            }
        }
        in.close();
        return "Unset HWID XD?";
    }

    public static String getBullshit() {
        return System.getenv("COMPUTERNAME");
    }

}
