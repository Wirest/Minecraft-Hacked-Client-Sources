package cedo.util.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAtomicOperationOnVolatileField")
public class FontUtil {
    public static volatile int completed;
    public static MinecraftFontRenderer regular, regular2, cleanlarge, cleankindalarge, cleanmedium, clean, cleanSmall, mediumfont, smallfont, tommysmallfont, regularSmall, large, expandedfont, tabguimodule;
    private static Font regular_, regular2_, cleanlarge_, cleankindalarge_, cleanmedium_, clean_, cleanSmall_, mediumfont_, smallfont_, tommysmallfont_, regularSmall_, large_, expandedfont_, tabguimodule_;

    private static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font = null;

        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("Fan/" + location)).getInputStream();
                font = Font.createFont(0, is);
                locationMap.put(location, font);
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }

        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }

    public static void bootstrap() {
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            regular_ = getFont(locationMap, "bold.ttf", 28);
            regular2_ = getFont(locationMap, "bold.ttf", 24);
            clean_ = getFont(locationMap, "Applefnt.ttf", 24);
            cleankindalarge_ = getFont(locationMap, "Applefnt.ttf", 28);
            cleanlarge_ = getFont(locationMap, "Applefnt.ttf", 31);
            cleanmedium_ = getFont(locationMap, "Applefnt.ttf", 20);
            cleanSmall_ = getFont(locationMap, "Applefnt.ttf", 17);
            mediumfont_ = getFont(locationMap, "bold.ttf", 20);
            smallfont_ = getFont(locationMap, "Applefnt.ttf", 18);
            tommysmallfont_ = getFont(locationMap, "bold.ttf", 18);
            regularSmall_ = getFont(locationMap, "bold.ttf", 16);
            large_ = getFont(locationMap, "Tommy3.ttf", 25);
            expandedfont_ = getFont(locationMap, "bold.ttf", 38);
            tabguimodule_ = getFont(locationMap, "title.ttf", 35);
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();
        new Thread(() ->
        {
            Map<String, Font> locationMap = new HashMap<>();
            completed++;
        }).start();

        while (!hasLoaded()) {
            try {
                //noinspection BusyWait
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        cleankindalarge = new MinecraftFontRenderer(cleankindalarge_, true, true);
        regular = new MinecraftFontRenderer(regular_, true, true);
        regular2 = new MinecraftFontRenderer(regular2_, true, true);
        cleanlarge = new MinecraftFontRenderer(cleanlarge_, true, true);
        cleanmedium = new MinecraftFontRenderer(cleanmedium_, true, true);
        clean = new MinecraftFontRenderer(clean_, true, true);
        cleanSmall = new MinecraftFontRenderer(cleanSmall_, true, true);
        mediumfont = new MinecraftFontRenderer(mediumfont_, true, true);
        smallfont = new MinecraftFontRenderer(smallfont_, true, true);
        tommysmallfont = new MinecraftFontRenderer(tommysmallfont_, true, true);
        regularSmall = new MinecraftFontRenderer(regularSmall_, true, true);
        large = new MinecraftFontRenderer(large_, true, true);
        expandedfont = new MinecraftFontRenderer(expandedfont_, true, true);
        tabguimodule = new MinecraftFontRenderer(expandedfont_, true, true);
    }
}
