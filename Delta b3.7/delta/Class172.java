/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 */
package delta;

import delta.Class192;
import delta.Class43;
import delta.utils.RenderUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;

public class Class172 {
    private File viking$;
    private URL builders$;

    public Class172(String string) {
        try {
            this.builders$ = new URL(string);
        }
        catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        }
        this.viking$ = new File(Class43._although(), Class192._perry(string.toString()));
        if (!this.viking$.exists()) {
            this.viking$.getParentFile().mkdirs();
            LogManager.getLogger((String)"CachedResource").info("Downloading " + string);
            URL uRL = this.builders$;
            HttpURLConnection httpURLConnection = (HttpURLConnection)uRL.openConnection();
            Map<String, List<String>> map = httpURLConnection.getHeaderFields();
            while (this._colored(map)) {
                uRL = new URL(map.get("Location").get(186 - 290 + 122 + -18));
                httpURLConnection = (HttpURLConnection)uRL.openConnection();
                map = httpURLConnection.getHeaderFields();
            }
            try {
                InputStream inputStream = httpURLConnection.getInputStream();
                CopyOption[] arrcopyOption = new CopyOption[271 - 517 + 85 + 162];
                arrcopyOption[197 - 242 + 95 + -50] = StandardCopyOption.REPLACE_EXISTING;
                Files.copy(inputStream, this.viking$.toPath(), arrcopyOption);
                httpURLConnection.disconnect();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
        if (string.toString().endsWith(".png") || string.toString().endsWith(".jpg")) {
            RenderUtils._being(this);
        }
    }

    public File _horror() {
        return this.viking$;
    }

    public URL _portion() {
        return this.builders$;
    }

    private boolean _colored(Map<String, List<String>> map) {
        for (String string : map.get(null)) {
            if (!string.contains("301") && !string.contains("302")) continue;
            return 79 - 105 + 72 + -45;
        }
        return 198 - 289 + 280 + -189;
    }
}

