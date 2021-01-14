package net.minecraft.client.resources.data;

import java.util.Collection;

public class LanguageMetadataSection implements IMetadataSection
{
    private final Collection languages;
    

    public LanguageMetadataSection(Collection p_i1311_1_)
    {
        this.languages = p_i1311_1_;
    }

    public Collection getLanguages()
    {
        return this.languages;
    }
}
