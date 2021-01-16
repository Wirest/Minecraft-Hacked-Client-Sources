/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 */
package me.razerboy420.weepcraft.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import net.minecraft.client.util.JsonException;

public class WebUtils {
    public static JsonParser JsonParser = new JsonParser();

    public static boolean openLink(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String read(URL url) {
        String fuckniggers = "";
        try {
            String line;
            HttpURLConnection communism = (HttpURLConnection)url.openConnection();
            communism.setRequestMethod("GET");
            BufferedReader input = new BufferedReader(new InputStreamReader(communism.getInputStream()));
            StringBuilder buffer = new StringBuilder();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            input.close();
            fuckniggers = buffer.toString();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return fuckniggers;
    }

    public static String jsonGet(URL url, String item) throws IOException, JsonException {
        HttpURLConnection request = (HttpURLConnection)url.openConnection();
        request.connect();
        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse((Reader)new InputStreamReader((InputStream)request.getContent()));
        JsonObject rootobj = root.getAsJsonObject();
        return rootobj.get(item).getAsString();
    }
}

