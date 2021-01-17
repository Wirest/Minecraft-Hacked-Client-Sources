// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources.data;

import net.minecraft.client.resources.Language;
import java.util.Collection;

public class LanguageMetadataSection implements IMetadataSection
{
    private final Collection<Language> languages;
    
    public LanguageMetadataSection(final Collection<Language> p_i1311_1_) {
        this.languages = p_i1311_1_;
    }
    
    public Collection<Language> getLanguages() {
        return this.languages;
    }
}
