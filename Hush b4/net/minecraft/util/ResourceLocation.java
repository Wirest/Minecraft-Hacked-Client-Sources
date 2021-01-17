// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.StringUtils;

public class ResourceLocation
{
    protected final String resourceDomain;
    protected final String resourcePath;
    
    protected ResourceLocation(final int p_i45928_1_, final String... resourceName) {
        this.resourceDomain = (StringUtils.isEmpty(resourceName[0]) ? "minecraft" : resourceName[0].toLowerCase());
        Validate.notNull(this.resourcePath = resourceName[1]);
    }
    
    public ResourceLocation(final String resourceName) {
        this(0, splitObjectName(resourceName));
    }
    
    public ResourceLocation(final String resourceDomainIn, final String resourcePathIn) {
        this(0, new String[] { resourceDomainIn, resourcePathIn });
    }
    
    protected static String[] splitObjectName(final String toSplit) {
        final String[] astring = { null, toSplit };
        final int i = toSplit.indexOf(58);
        if (i >= 0) {
            astring[1] = toSplit.substring(i + 1, toSplit.length());
            if (i > 1) {
                astring[0] = toSplit.substring(0, i);
            }
        }
        return astring;
    }
    
    public String getResourcePath() {
        return this.resourcePath;
    }
    
    public String getResourceDomain() {
        return this.resourceDomain;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.resourceDomain) + ':' + this.resourcePath;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof ResourceLocation)) {
            return false;
        }
        final ResourceLocation resourcelocation = (ResourceLocation)p_equals_1_;
        return this.resourceDomain.equals(resourcelocation.resourceDomain) && this.resourcePath.equals(resourcelocation.resourcePath);
    }
    
    @Override
    public int hashCode() {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}
