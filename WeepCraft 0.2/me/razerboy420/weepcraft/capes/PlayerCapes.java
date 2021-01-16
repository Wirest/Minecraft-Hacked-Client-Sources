/*
 * Decompiled with CFR 0_122.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonParser
 */
package me.razerboy420.weepcraft.capes;

import com.google.gson.JsonParser;
import java.util.ArrayList;

public class PlayerCapes {
    public static ArrayList<CapeList> capes = new ArrayList();
    public static JsonParser jsonParser = new JsonParser();

    public static void load() {
    }

    public static boolean deservesCape(String name) {
        for (CapeList c : capes) {
            if (!c.name.equalsIgnoreCase(name)) continue;
            return true;
        }
        return false;
    }

    public static String getCapeURL(String name) {
        for (CapeList c : capes) {
            if (!c.name.equalsIgnoreCase(name)) continue;
            if (!c.capelink.endsWith(".png")) {
                c.capelink = String.valueOf(String.valueOf(c.capelink)) + ".png";
            }
            return c.capelink;
        }
        return null;
    }

    public static int getWidth(String name) {
        for (CapeList c : capes) {
            if (!c.name.equalsIgnoreCase(name)) continue;
            return c.width;
        }
        return 0;
    }

    public static int getHeight(String name) {
        for (CapeList c : capes) {
            if (!c.name.equalsIgnoreCase(name)) continue;
            return c.height;
        }
        return 0;
    }

    public static class CapeList {
        public String name;
        public String capelink;
        public int height;
        public int width;

        public CapeList(String name, String capelink, int width, int height) {
            this.name = name;
            this.capelink = capelink;
            this.width = width;
            this.height = height;
        }
    }

}

