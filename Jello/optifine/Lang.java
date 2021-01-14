package optifine;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

public class Lang
{
    private static final Splitter splitter = Splitter.on('=').limit(2);
    private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");

    public static void resourcesReloaded()
    {
        Map localeProperties = I18n.getLocaleProperties();
        ArrayList listFiles = new ArrayList();
        String PREFIX = "optifine/lang/";
        String EN_US = "en_US";
        String SUFFIX = ".lang";
        listFiles.add(PREFIX + EN_US + SUFFIX);

        if (!Config.getGameSettings().language.equals(EN_US))
        {
            listFiles.add(PREFIX + Config.getGameSettings().language + SUFFIX);
        }

        String[] files = (String[])((String[])listFiles.toArray(new String[listFiles.size()]));
        loadResources(Config.getDefaultResourcePack(), files, localeProperties);
        IResourcePack[] resourcePacks = Config.getResourcePacks();

        for (int i = 0; i < resourcePacks.length; ++i)
        {
            IResourcePack rp = resourcePacks[i];
            loadResources(rp, files, localeProperties);
        }
    }

    private static void loadResources(IResourcePack rp, String[] files, Map localeProperties)
    {
        try
        {
            for (int e = 0; e < files.length; ++e)
            {
                String file = files[e];
                ResourceLocation loc = new ResourceLocation(file);

                if (rp.resourceExists(loc))
                {
                    InputStream in = rp.getInputStream(loc);

                    if (in != null)
                    {
                        loadLocaleData(in, localeProperties);
                    }
                }
            }
        }
        catch (IOException var7)
        {
            var7.printStackTrace();
        }
    }

    public static void loadLocaleData(InputStream is, Map localeProperties) throws IOException
    {
        Iterator it = IOUtils.readLines(is, Charsets.UTF_8).iterator();

        while (it.hasNext())
        {
            String line = (String)it.next();

            if (!line.isEmpty() && line.charAt(0) != 35)
            {
                String[] parts = (String[])((String[])Iterables.toArray(splitter.split(line), String.class));

                if (parts != null && parts.length == 2)
                {
                    String key = parts[0];
                    String value = pattern.matcher(parts[1]).replaceAll("%$1s");
                    localeProperties.put(key, value);
                }
            }
        }
    }

    public static String get(String key)
    {
        return I18n.format(key, new Object[0]);
    }

    public static String get(String key, String def)
    {
        String str = I18n.format(key, new Object[0]);
        return str != null && !str.equals(key) ? str : def;
    }

    public static String getOn()
    {
        return I18n.format("options.on", new Object[0]);
    }

    public static String getOff()
    {
        return I18n.format("options.off", new Object[0]);
    }

    public static String getFast()
    {
        return I18n.format("options.graphics.fast", new Object[0]);
    }

    public static String getFancy()
    {
        return I18n.format("options.graphics.fancy", new Object[0]);
    }

    public static String getDefault()
    {
        return I18n.format("generator.default", new Object[0]);
    }
}
