// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.io.OutputStream;
import java.util.Iterator;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.io.InputStream;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils
{
    public static final String SERVER_URL = "http://s.optifine.net";
    public static final String POST_URL = "http://optifine.net";
    
    public static byte[] get(final String p_get_0_) throws IOException {
        HttpURLConnection httpurlconnection = null;
        byte[] abyte2;
        try {
            final URL url = new URL(p_get_0_);
            httpurlconnection = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(false);
            httpurlconnection.connect();
            if (httpurlconnection.getResponseCode() / 100 != 2) {
                if (httpurlconnection.getErrorStream() != null) {
                    Config.readAll(httpurlconnection.getErrorStream());
                }
                throw new IOException("HTTP response: " + httpurlconnection.getResponseCode());
            }
            final InputStream inputstream = httpurlconnection.getInputStream();
            final byte[] abyte = new byte[httpurlconnection.getContentLength()];
            int i = 0;
            do {
                final int j = inputstream.read(abyte, i, abyte.length - i);
                if (j < 0) {
                    throw new IOException("Input stream closed: " + p_get_0_);
                }
                i += j;
            } while (i < abyte.length);
            abyte2 = abyte;
        }
        finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
        if (httpurlconnection != null) {
            httpurlconnection.disconnect();
        }
        return abyte2;
    }
    
    public static String post(final String p_post_0_, final Map p_post_1_, final byte[] p_post_2_) throws IOException {
        HttpURLConnection httpurlconnection = null;
        String s4;
        try {
            final URL url = new URL(p_post_0_);
            httpurlconnection = (HttpURLConnection)url.openConnection(Minecraft.getMinecraft().getProxy());
            httpurlconnection.setRequestMethod("POST");
            if (p_post_1_ != null) {
                for (final Object s : p_post_1_.keySet()) {
                    final String s2 = new StringBuilder().append(p_post_1_.get(s)).toString();
                    httpurlconnection.setRequestProperty((String)s, s2);
                }
            }
            httpurlconnection.setRequestProperty("Content-Type", "text/plain");
            httpurlconnection.setRequestProperty("Content-Length", new StringBuilder().append(p_post_2_.length).toString());
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            final OutputStream outputstream = httpurlconnection.getOutputStream();
            outputstream.write(p_post_2_);
            outputstream.flush();
            outputstream.close();
            final InputStream inputstream = httpurlconnection.getInputStream();
            final InputStreamReader inputstreamreader = new InputStreamReader(inputstream, "ASCII");
            final BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
            final StringBuffer stringbuffer = new StringBuffer();
            String s3;
            while ((s3 = bufferedreader.readLine()) != null) {
                stringbuffer.append(s3);
                stringbuffer.append('\r');
            }
            bufferedreader.close();
            s4 = stringbuffer.toString();
        }
        finally {
            if (httpurlconnection != null) {
                httpurlconnection.disconnect();
            }
        }
        if (httpurlconnection != null) {
            httpurlconnection.disconnect();
        }
        return s4;
    }
}
