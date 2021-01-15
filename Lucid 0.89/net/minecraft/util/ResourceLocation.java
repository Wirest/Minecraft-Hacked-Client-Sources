package net.minecraft.util;

import org.apache.commons.lang3.Validate;

public class ResourceLocation
{
    protected final String resourceDomain;
    protected final String resourcePath;

    protected ResourceLocation(int p_i45928_1_, String ... resourceName)
    {
        this.resourceDomain = org.apache.commons.lang3.StringUtils.isEmpty(resourceName[0]) ? "minecraft" : resourceName[0].toLowerCase();
        this.resourcePath = resourceName[1];
        Validate.notNull(this.resourcePath);
    }

    public ResourceLocation(String resourceName)
    {
        this(0, splitObjectName(resourceName));
    }

    public ResourceLocation(String resourceDomainIn, String resourcePathIn)
    {
        this(0, new String[] {resourceDomainIn, resourcePathIn});
    }

    /**
     * Splits an object name (such as minecraft:apple) into the domain and path parts and returns these as an array of
     * length 2. If no colon is present in the passed value the returned array will contain {null, toSplit}.
     *  
     * @param toSplit The object name to split into its domain and path Strings.
     */
    protected static String[] splitObjectName(String toSplit)
    {
        String[] var1 = new String[] {null, toSplit};
        int var2 = toSplit.indexOf(58);

        if (var2 >= 0)
        {
            var1[1] = toSplit.substring(var2 + 1, toSplit.length());

            if (var2 > 1)
            {
                var1[0] = toSplit.substring(0, var2);
            }
        }

        return var1;
    }

    public String getResourcePath()
    {
        return this.resourcePath;
    }

    public String getResourceDomain()
    {
        return this.resourceDomain;
    }

    @Override
	public String toString()
    {
        return this.resourceDomain + ':' + this.resourcePath;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof ResourceLocation))
        {
            return false;
        }
        else
        {
            ResourceLocation var2 = (ResourceLocation)p_equals_1_;
            return this.resourceDomain.equals(var2.resourceDomain) && this.resourcePath.equals(var2.resourcePath);
        }
    }

    @Override
	public int hashCode()
    {
        return 31 * this.resourceDomain.hashCode() + this.resourcePath.hashCode();
    }
}
