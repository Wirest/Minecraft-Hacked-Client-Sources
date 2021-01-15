package nivia.security;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stringer.annotations.HideAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@InvokeDynamics
@StringEncryption
@HideAccess
public class ConnectionUtils {
	
	public static void authorize(String HWID) {
		try {
		URL url = new URL("http://www.niviaclient.com/api/v1/client/auth/hwid/" + a().toString());
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setReadTimeout(60 * 1000);
		connection.setConnectTimeout(60 * 1000);
		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
		connection.setUseCaches(false);
		connection.connect();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuilder str = new StringBuilder();
		while ((inputLine = reader.readLine()) != null)
			str.append(inputLine);
		Gson gson = new Gson();
		JsonObject ar = gson.fromJson(str.toString(), JsonObject.class);
		if (ar.get("error").toString().equalsIgnoreCase("true") && ar.get("error_code").toString().equalsIgnoreCase("hwid_not_valid"))
			crashMinecraft();
		} catch (Exception e) {
			System.out.println("hi");
		}
		
	}
	
	public static void crashMinecraft() {
		try
        {
            //this.stream.shutdownStream();
           // logger.info("Stopping!");

            try
            {
            	Minecraft.getMinecraft().theWorld = (WorldClient)null;
                //this.loadWorld((WorldClient)null);
            }
            catch (Throwable var5)
            {
                ;
            }

           // this.mcSoundHandler.unloadSounds();
        }
        finally
        {
            Display.destroy();

            if (true)
            {
                System.exit(0);
            }
        }

        System.gc();
	}
	
    private static String a() throws Exception {
        String hwid = g(String.valueOf(System.getenv("PROCESSOR_IDENTIFIER")) + System.getenv("COMPUTERNAME") + System.getProperty("user.name"));
        StringSelection stringSelection = new StringSelection(hwid);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return hwid;
    }

    private static String g(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] sha1hash;
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
        return z(sha1hash);
    }

    private static String z(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte aData : data) {
            int halfbyte = aData >>> 4 & 0xF;
            int two_halfs = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char) (48 + halfbyte));
                } else {
                    buf.append((char) (97 + (halfbyte - 5)));
                }
                halfbyte = (aData & 0xF);
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
	

}
