/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.utils.Wrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Class23 {
    private static boolean dealtime$;
    private static String glance$;
    private static String trans$;

    public static String _mustang() {
        if (trans$ == null || dealtime$) {
            try {
                trans$ = Class23._diamond(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/discord"));
                dealtime$ = 223 - 351 + 320 + -192;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                dealtime$ = 258 - 388 + 120 - 58 + 69;
            }
        }
        return trans$;
    }

    public static String _diamond(URL uRL) throws IOException {
        String string;
        uRL.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRL.openStream()));
        StringBuilder stringBuilder = new StringBuilder();
        while ((string = bufferedReader.readLine()) != null) {
            stringBuilder.append(string + "\n");
        }
        String string2 = stringBuilder.toString();
        string2 = string2.substring(243 - 250 + 249 - 53 + -189, string2.lastIndexOf("\n"));
        return string2;
    }

    public static String _bunch() {
        if (glance$ == null) {
            try {
                glance$ = Class23._diamond(new URL("https://raw.githubusercontent.com/nkosmos/xdelta/master/website"));
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        return glance$;
    }

    public static boolean _dozen() {
        try {
            URL uRL = new URL("https://www.google.com");
            URLConnection uRLConnection = uRL.openConnection();
            uRLConnection.addRequestProperty("User-Agent", "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
            uRLConnection.setConnectTimeout(27 - 46 + 8 - 2 + 15013);
            uRLConnection.connect();
            uRLConnection.getInputStream().close();
            return 130 - 194 + 177 + -112;
        }
        catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        catch (IOException iOException) {
            iOException.printStackTrace();
        }
        return 23 - 42 + 40 - 23 + 2;
    }

    public static void _charms() {
        if (!Class23._dozen()) {
            Wrapper._occurs();
        }
    }
}

