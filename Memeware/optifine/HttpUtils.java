package optifine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;

public class HttpUtils {
    public static final String SERVER_URL = "http://s.optifine.net";
    public static final String POST_URL = "http://optifine.net";

    public static byte[] get(String urlStr) throws IOException {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection(Minecraft.getMinecraft().getProxy());
            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.connect();

            if (conn.getResponseCode() / 100 != 2) {
                if (conn.getErrorStream() != null) {
                    Config.readAll(conn.getErrorStream());
                }

                throw new IOException("HTTP response: " + conn.getResponseCode());
            } else {
                InputStream in = conn.getInputStream();
                byte[] bytes = new byte[conn.getContentLength()];
                int pos = 0;

                do {
                    int len = in.read(bytes, pos, bytes.length - pos);

                    if (len < 0) {
                        throw new IOException("Input stream closed: " + urlStr);
                    }

                    pos += len;
                }
                while (pos < bytes.length);

                byte[] len1 = bytes;
                return len1;
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String post(String urlStr, Map headers, byte[] content) throws IOException {
        HttpURLConnection conn = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection(Minecraft.getMinecraft().getProxy());
            conn.setRequestMethod("POST");

            if (headers != null) {
                Set os = headers.keySet();
                Iterator in = os.iterator();

                while (in.hasNext()) {
                    String isr = (String) in.next();
                    String br = "" + headers.get(isr);
                    conn.setRequestProperty(isr, br);
                }
            }

            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("Content-Length", "" + content.length);
            conn.setRequestProperty("Content-Language", "en-US");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            OutputStream os1 = conn.getOutputStream();
            os1.write(content);
            os1.flush();
            os1.close();
            InputStream in1 = conn.getInputStream();
            InputStreamReader isr1 = new InputStreamReader(in1, "ASCII");
            BufferedReader br1 = new BufferedReader(isr1);
            StringBuffer sb = new StringBuffer();
            String line;

            while ((line = br1.readLine()) != null) {
                sb.append(line);
                sb.append('\r');
            }

            br1.close();
            String var11 = sb.toString();
            return var11;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
