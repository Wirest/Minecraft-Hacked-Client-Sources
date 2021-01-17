// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

public class Language implements Comparable<Language>
{
    private final String languageCode;
    private final String region;
    private final String name;
    private final boolean bidirectional;
    
    public Language(final String languageCodeIn, final String regionIn, final String nameIn, final boolean bidirectionalIn) {
        this.languageCode = languageCodeIn;
        this.region = regionIn;
        this.name = nameIn;
        this.bidirectional = bidirectionalIn;
    }
    
    public String getLanguageCode() {
        return this.languageCode;
    }
    
    public boolean isBidirectional() {
        return this.bidirectional;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s)", this.name, this.region);
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        return this == p_equals_1_ || (p_equals_1_ instanceof Language && this.languageCode.equals(((Language)p_equals_1_).languageCode));
    }
    
    @Override
    public int hashCode() {
        return this.languageCode.hashCode();
    }
    
    @Override
    public int compareTo(final Language p_compareTo_1_) {
        return this.languageCode.compareTo(p_compareTo_1_.languageCode);
    }
}
