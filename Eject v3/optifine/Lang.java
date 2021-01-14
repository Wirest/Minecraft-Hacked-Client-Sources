package optifine;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

public class Lang {
    private static final Splitter splitter = Splitter.on('=').limit(2);
    private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

    public static void resourcesReloaded() {
        Map localMap = I18n.getLocaleProperties();
        ArrayList localArrayList = new ArrayList();
        String str1 = "optifine/lang/";
        String str2 = "en_US";
        String str3 = ".lang";
        localArrayList.add(str1 + str2 + str3);
        if (!Config.getGameSettings().language.equals(str2)) {
            localArrayList.add(str1 + Config.getGameSettings().language + str3);
        }
        String[] arrayOfString = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        loadResources(Config.getDefaultResourcePack(), arrayOfString, localMap);
        IResourcePack[] arrayOfIResourcePack = Config.getResourcePacks();
        for (int i = 0; i < arrayOfIResourcePack.length; i++) {
            IResourcePack localIResourcePack = arrayOfIResourcePack[i];
            loadResources(localIResourcePack, arrayOfString, localMap);
        }
    }

    private static void loadResources(IResourcePack paramIResourcePack, String[] paramArrayOfString, Map paramMap) {
        try {
            for (int i = 0; i < paramArrayOfString.length; i++) {
                String str = paramArrayOfString[i];
                ResourceLocation localResourceLocation = new ResourceLocation(str);
                if (paramIResourcePack.resourceExists(localResourceLocation)) {
                    InputStream localInputStream = paramIResourcePack.getInputStream(localResourceLocation);
                    if (localInputStream != null) {
                        loadLocaleData(localInputStream, paramMap);
                    }
                }
            }
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
    }

    public static void loadLocaleData(InputStream paramInputStream, Map paramMap)
            throws IOException {
        Iterator localIterator = IOUtils.readLines(paramInputStream, Charsets.UTF_8).iterator();
        while (localIterator.hasNext()) {
            String str1 = (String) localIterator.next();
            if ((!str1.isEmpty()) && (str1.charAt(0) != '#')) {
                String[] arrayOfString = (String[]) (String[]) Iterables.toArray(splitter.split(str1), String.class);
                if ((arrayOfString != null) && (arrayOfString.length == 2)) {
                    String str2 = arrayOfString[0];
                    String str3 = pattern.matcher(arrayOfString[1]).replaceAll("%$1s");
                    paramMap.put(str2, str3);
                }
            }
        }
    }

    public static String get(String paramString) {
        return I18n.format(paramString, new Object[0]);
    }

    public static String get(String paramString1, String paramString2) {
        String str = I18n.format(paramString1, new Object[0]);
        return (str != null) && (!str.equals(paramString1)) ? str : paramString2;
    }

    public static String getOn() {
        return I18n.format("options.on", new Object[0]);
    }

    public static String getOff() {
        return I18n.format("options.off", new Object[0]);
    }

    public static String getFast() {
        return I18n.format("options.graphics.fast", new Object[0]);
    }

    public static String getFancy() {
        return I18n.format("options.graphics.fancy", new Object[0]);
    }

    public static String getDefault() {
        return I18n.format("generator.default", new Object[0]);
    }
}




