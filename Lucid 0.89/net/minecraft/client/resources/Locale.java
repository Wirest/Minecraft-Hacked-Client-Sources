package net.minecraft.client.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;

public class Locale
{
    /** Splits on "=" */
    private static final Splitter splitter = Splitter.on('=').limit(2);
    private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    Map properties = Maps.newHashMap();
    private boolean unicode;

    /**
     * par2 is a list of languages. For each language $L and domain $D, attempts to load the resource $D:lang/$L.lang
     */
    public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List p_135022_2_)
    {
        this.properties.clear();
        Iterator var3 = p_135022_2_.iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            String var5 = String.format("lang/%s.lang", new Object[] {var4});
            Iterator var6 = resourceManager.getResourceDomains().iterator();

            while (var6.hasNext())
            {
                String var7 = (String)var6.next();

                try
                {
                    this.loadLocaleData(resourceManager.getAllResources(new ResourceLocation(var7, var5)));
                }
                catch (IOException var9)
                {
                    ;
                }
            }
        }

        this.checkUnicode();
    }

    public boolean isUnicode()
    {
        return this.unicode;
    }

    private void checkUnicode()
    {
        this.unicode = false;
        int var1 = 0;
        int var2 = 0;
        Iterator var3 = this.properties.values().iterator();

        while (var3.hasNext())
        {
            String var4 = (String)var3.next();
            int var5 = var4.length();
            var2 += var5;

            for (int var6 = 0; var6 < var5; ++var6)
            {
                if (var4.charAt(var6) >= 256)
                {
                    ++var1;
                }
            }
        }

        float var7 = (float)var1 / (float)var2;
        this.unicode = var7 > 0.1D;
    }

    /**
     * par1 is a list of Resources
     */
    private void loadLocaleData(List p_135028_1_) throws IOException
    {
        Iterator var2 = p_135028_1_.iterator();

        while (var2.hasNext())
        {
            IResource var3 = (IResource)var2.next();
            InputStream var4 = var3.getInputStream();

            try
            {
                this.loadLocaleData(var4);
            }
            finally
            {
                IOUtils.closeQuietly(var4);
            }
        }
    }

    private void loadLocaleData(InputStream p_135021_1_) throws IOException
    {
        Iterator var2 = IOUtils.readLines(p_135021_1_, Charsets.UTF_8).iterator();

        while (var2.hasNext())
        {
            String var3 = (String)var2.next();

            if (!var3.isEmpty() && var3.charAt(0) != 35)
            {
                String[] var4 = Iterables.toArray(splitter.split(var3), String.class);

                if (var4 != null && var4.length == 2)
                {
                    String var5 = var4[0];
                    String var6 = pattern.matcher(var4[1]).replaceAll("%$1s");
                    this.properties.put(var5, var6);
                }
            }
        }
    }

    /**
     * Returns the translation, or the key itself if the key could not be translated.
     */
    private String translateKeyPrivate(String p_135026_1_)
    {
        String var2 = (String)this.properties.get(p_135026_1_);
        return var2 == null ? p_135026_1_ : var2;
    }

    /**
     * Calls String.format(translateKey(key), params)
     */
    public String formatMessage(String translateKey, Object[] parameters)
    {
        String var3 = this.translateKeyPrivate(translateKey);

        try
        {
            return String.format(var3, parameters);
        }
        catch (IllegalFormatException var5)
        {
            return "Format error: " + var3;
        }
    }
}
