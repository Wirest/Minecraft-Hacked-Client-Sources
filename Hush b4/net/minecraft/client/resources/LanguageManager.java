// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import com.google.common.collect.Sets;
import java.util.SortedSet;
import net.minecraft.util.StringTranslate;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import java.util.List;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSerializer;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener
{
    private static final Logger logger;
    private final IMetadataSerializer theMetadataSerializer;
    private String currentLanguage;
    protected static final Locale currentLocale;
    private Map<String, Language> languageMap;
    
    static {
        logger = LogManager.getLogger();
        currentLocale = new Locale();
    }
    
    public LanguageManager(final IMetadataSerializer theMetadataSerializerIn, final String currentLanguageIn) {
        this.languageMap = (Map<String, Language>)Maps.newHashMap();
        this.theMetadataSerializer = theMetadataSerializerIn;
        this.currentLanguage = currentLanguageIn;
        I18n.setLocale(LanguageManager.currentLocale);
    }
    
    public void parseLanguageMetadata(final List<IResourcePack> p_135043_1_) {
        this.languageMap.clear();
        for (final IResourcePack iresourcepack : p_135043_1_) {
            try {
                final LanguageMetadataSection languagemetadatasection = iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");
                if (languagemetadatasection == null) {
                    continue;
                }
                for (final Language language : languagemetadatasection.getLanguages()) {
                    if (!this.languageMap.containsKey(language.getLanguageCode())) {
                        this.languageMap.put(language.getLanguageCode(), language);
                    }
                }
            }
            catch (RuntimeException runtimeexception) {
                LanguageManager.logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), runtimeexception);
            }
            catch (IOException ioexception) {
                LanguageManager.logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), ioexception);
            }
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        final List<String> list = Lists.newArrayList("en_US");
        if (!"en_US".equals(this.currentLanguage)) {
            list.add(this.currentLanguage);
        }
        LanguageManager.currentLocale.loadLocaleDataFiles(resourceManager, list);
        StringTranslate.replaceWith(LanguageManager.currentLocale.properties);
    }
    
    public boolean isCurrentLocaleUnicode() {
        return LanguageManager.currentLocale.isUnicode();
    }
    
    public boolean isCurrentLanguageBidirectional() {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }
    
    public void setCurrentLanguage(final Language currentLanguageIn) {
        this.currentLanguage = currentLanguageIn.getLanguageCode();
    }
    
    public Language getCurrentLanguage() {
        return this.languageMap.containsKey(this.currentLanguage) ? this.languageMap.get(this.currentLanguage) : this.languageMap.get("en_US");
    }
    
    public SortedSet<Language> getLanguages() {
        return (SortedSet<Language>)Sets.newTreeSet((Iterable<? extends Comparable>)this.languageMap.values());
    }
}
