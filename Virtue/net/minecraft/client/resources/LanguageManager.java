package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.StringTranslate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private final IMetadataSerializer theMetadataSerializer;
    private String currentLanguage;
    protected static final Locale currentLocale = new Locale();
    private Map languageMap = Maps.newHashMap();
    private static final String __OBFID = "CL_00001096";

    public LanguageManager(IMetadataSerializer p_i1304_1_, String p_i1304_2_)
    {
        this.theMetadataSerializer = p_i1304_1_;
        this.currentLanguage = p_i1304_2_;
        I18n.setLocale(currentLocale);
    }

    public void parseLanguageMetadata(List p_135043_1_)
    {
        this.languageMap.clear();
        Iterator var2 = p_135043_1_.iterator();

        while (var2.hasNext())
        {
            IResourcePack var3 = (IResourcePack)var2.next();

            try
            {
                LanguageMetadataSection var4 = (LanguageMetadataSection)var3.getPackMetadata(this.theMetadataSerializer, "language");

                if (var4 != null)
                {
                    Iterator var5 = var4.getLanguages().iterator();

                    while (var5.hasNext())
                    {
                        Language var6 = (Language)var5.next();

                        if (!this.languageMap.containsKey(var6.getLanguageCode()))
                        {
                            this.languageMap.put(var6.getLanguageCode(), var6);
                        }
                    }
                }
            }
            catch (RuntimeException var7)
            {
                logger.warn("Unable to parse metadata section of resourcepack: " + var3.getPackName(), var7);
            }
            catch (IOException var8)
            {
                logger.warn("Unable to parse metadata section of resourcepack: " + var3.getPackName(), var8);
            }
        }
    }

    public void onResourceManagerReload(IResourceManager p_110549_1_)
    {
        ArrayList var2 = Lists.newArrayList(new String[] {"en_US"});

        if (!"en_US".equals(this.currentLanguage))
        {
            var2.add(this.currentLanguage);
        }

        currentLocale.loadLocaleDataFiles(p_110549_1_, var2);
        StringTranslate.replaceWith(currentLocale.field_135032_a);
    }

    public boolean isCurrentLocaleUnicode()
    {
        return currentLocale.isUnicode();
    }

    public boolean isCurrentLanguageBidirectional()
    {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }

    public void setCurrentLanguage(Language p_135045_1_)
    {
        this.currentLanguage = p_135045_1_.getLanguageCode();
    }

    public Language getCurrentLanguage()
    {
        return this.languageMap.containsKey(this.currentLanguage) ? (Language)this.languageMap.get(this.currentLanguage) : (Language)this.languageMap.get("en_US");
    }

    public SortedSet getLanguages()
    {
        return Sets.newTreeSet(this.languageMap.values());
    }
}
