package optifine;

import net.minecraft.util.ResourceLocation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Properties;

public class FontUtils {
    public static Properties readFontProperties(ResourceLocation paramResourceLocation) {
        String str1 = paramResourceLocation.getResourcePath();
        Properties localProperties = new Properties();
        String str2 = ".png";
        if (!str1.endsWith(str2)) {
            return localProperties;
        }
        String str3 = str1.substring(0, str1.length() - str2.length()) + ".properties";
        try {
            ResourceLocation localResourceLocation = new ResourceLocation(paramResourceLocation.getResourceDomain(), str3);
            InputStream localInputStream = Config.getResourceStream(Config.getResourceManager(), localResourceLocation);
            if (localInputStream == null) {
                return localProperties;
            }
            Config.log("Loading " + str3);
            localProperties.load(localInputStream);
        } catch (FileNotFoundException localFileNotFoundException) {
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return localProperties;
    }

    public static void readCustomCharWidths(Properties paramProperties, float[] paramArrayOfFloat) {
        Iterator localIterator = paramProperties.keySet().iterator();
        while (localIterator.hasNext()) {
            Object localObject = localIterator.next();
            String str1 = (String) localObject;
            String str2 = "width.";
            if (str1.startsWith(str2)) {
                String str3 = str1.substring(str2.length());
                int i = Config.parseInt(str3, -1);
                if ((i >= 0) && (i < paramArrayOfFloat.length)) {
                    String str4 = paramProperties.getProperty(str1);
                    float f = Config.parseFloat(str4, -1.0F);
                    if (f >= 0.0F) {
                        paramArrayOfFloat[i] = f;
                    }
                }
            }
        }
    }

    public static float readFloat(Properties paramProperties, String paramString, float paramFloat) {
        String str = paramProperties.getProperty(paramString);
        if (str == null) {
            return paramFloat;
        }
        float f = Config.parseFloat(str, Float.MIN_VALUE);
        if (f == Float.MIN_VALUE) {
            Config.warn("Invalid value for " + paramString + ": " + str);
            return paramFloat;
        }
        return f;
    }

    public static ResourceLocation getHdFontLocation(ResourceLocation paramResourceLocation) {
        if (!Config.isCustomFonts()) {
            return paramResourceLocation;
        }
        if (paramResourceLocation == null) {
            return paramResourceLocation;
        }
        String str1 = paramResourceLocation.getResourcePath();
        String str2 = "textures/";
        String str3 = "mcpatcher/";
        if (!str1.startsWith(str2)) {
            return paramResourceLocation;
        }
        str1 = str1.substring(str2.length());
        str1 = str3 + str1;
        ResourceLocation localResourceLocation = new ResourceLocation(paramResourceLocation.getResourceDomain(), str1);
        return Config.hasResource(Config.getResourceManager(), localResourceLocation) ? localResourceLocation : paramResourceLocation;
    }
}




