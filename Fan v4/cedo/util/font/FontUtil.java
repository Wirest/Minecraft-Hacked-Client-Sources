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
    public static MinecraftFontRenderer moonfontbig, moonfontboldmedium, fanboldmedium, moonfontsmall, moonfontmedium,
            moonfontregular, cleanlarge, cleankindalarge, cleanmedium, clean, cleanSmall, expandedfont;
    private static Font moonfontbig_, moonfontboldmedium_, fanboldmedium_, moonfontsmall_, moonfontmedium_, moonfontregular_, cleanlarge_,
            cleankindalarge_, cleanmedium_, clean_, cleanSmall_, expandedfont_;

    private static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;

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

            //Fan Fonts
            clean_ = getFont(locationMap, "Applefnt.ttf", 22);
            cleankindalarge_ = getFont(locationMap, "Applefnt.ttf", 26);
            cleanlarge_ = getFont(locationMap, "Applefnt.ttf", 31);
            cleanmedium_ = getFont(locationMap, "Applefnt.ttf", 20);
            cleanSmall_ = getFont(locationMap, "Applefnt.ttf", 17);
            expandedfont_ = getFont(locationMap, "bold.ttf", 38);
            fanboldmedium_ = getFont(locationMap, "bold.ttf", 18);

            //Moon Fonts
            moonfontsmall_ = getFont(locationMap, "moonfont.ttf", 17);
            moonfontregular_ = getFont(locationMap, "moonfont.ttf", 22);
            moonfontmedium_ = getFont(locationMap, "moonfont.ttf", 20);
            moonfontboldmedium_ = getFont(locationMap, "boldmoonfont.ttf", 18);
            moonfontbig_ = getFont(locationMap, "moonfont.ttf", 26);


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
        //Fan fonts
        cleankindalarge = new MinecraftFontRenderer(cleankindalarge_, true, true);
        cleanlarge = new MinecraftFontRenderer(cleanlarge_, true, true);
        cleanmedium = new MinecraftFontRenderer(cleanmedium_, true, true);
        clean = new MinecraftFontRenderer(clean_, true, true);
        cleanSmall = new MinecraftFontRenderer(cleanSmall_, true, true);
        fanboldmedium = new MinecraftFontRenderer(fanboldmedium_, true, true);
        expandedfont = new MinecraftFontRenderer(expandedfont_, true, true);

        //moon fonts
        moonfontregular = new MinecraftFontRenderer(moonfontregular_, true, true);
        moonfontmedium = new MinecraftFontRenderer(moonfontmedium_, true, true);
        moonfontsmall = new MinecraftFontRenderer(moonfontsmall_, true, true);
        moonfontboldmedium = new MinecraftFontRenderer(moonfontboldmedium_, true, true);
        moonfontbig = new MinecraftFontRenderer(moonfontbig_, true, true);

    }
}
