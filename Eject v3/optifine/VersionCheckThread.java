package optifine;

import net.minecraft.client.ClientBrandRetriever;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class VersionCheckThread
        extends Thread {
    public void run() {
        HttpURLConnection localHttpURLConnection = null;
        try {
            Config.dbg("Checking for new version");
            URL localURL = new URL("http://optifine.net/version/1.8.8/HD_U.txt");
            localHttpURLConnection = (HttpURLConnection) localURL.openConnection();
            if (Config.getGameSettings().snooperEnabled) {
                localHttpURLConnection.setRequestProperty("OF-MC-Version", "1.8.8");
                localHttpURLConnection.setRequestProperty("OF-MC-Brand", "" + ClientBrandRetriever.getClientModName());
                localHttpURLConnection.setRequestProperty("OF-Edition", "HD_U");
                localHttpURLConnection.setRequestProperty("OF-Release", "H8");
                localHttpURLConnection.setRequestProperty("OF-Java-Version", "" + System.getProperty("java.version"));
                localHttpURLConnection.setRequestProperty("OF-CpuCount", "" + Config.getAvailableProcessors());
                localHttpURLConnection.setRequestProperty("OF-OpenGL-Version", "" + Config.openGlVersion);
                localHttpURLConnection.setRequestProperty("OF-OpenGL-Vendor", "" + Config.openGlVendor);
            }
            localHttpURLConnection.setDoInput(true);
            localHttpURLConnection.setDoOutput(false);
            localHttpURLConnection.connect();
            try {
                InputStream localInputStream = localHttpURLConnection.getInputStream();
                String str1 = Config.readInputStream(localInputStream);
                localInputStream.close();
                String[] arrayOfString = Config.tokenize(str1, "\n\r");
                if (arrayOfString.length >= 1) {
                    String str2 = arrayOfString[0].trim();
                    Config.dbg("Version found: " + str2);
                    if (Config.compareRelease(str2, "H8") <= 0) {
                        return;
                    }
                    Config.setNewRelease(str2);
                    return;
                }
            } finally {
                if (localHttpURLConnection != null) {
                    localHttpURLConnection.disconnect();
                }
            }
        } catch (Exception localException) {
            Config.dbg(localException.getClass().getName() + ": " + localException.getMessage());
        }
    }
}




