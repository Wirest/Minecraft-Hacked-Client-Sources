package net.minecraft.client.resources;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class SimpleResource implements IResource
{
    private final Map mapMetadataSections = Maps.newHashMap();
    private final String resourcePackName;
    private final ResourceLocation srResourceLocation;
    private final InputStream resourceInputStream;
    private final InputStream mcmetaInputStream;
    private final IMetadataSerializer srMetadataSerializer;
    private boolean mcmetaJsonChecked;
    private JsonObject mcmetaJson;

    public SimpleResource(String resourcePackNameIn, ResourceLocation srResourceLocationIn, InputStream resourceInputStreamIn, InputStream mcmetaInputStreamIn, IMetadataSerializer srMetadataSerializerIn)
    {
        this.resourcePackName = resourcePackNameIn;
        this.srResourceLocation = srResourceLocationIn;
        this.resourceInputStream = resourceInputStreamIn;
        this.mcmetaInputStream = mcmetaInputStreamIn;
        this.srMetadataSerializer = srMetadataSerializerIn;
    }

    @Override
	public ResourceLocation getResourceLocation()
    {
        return this.srResourceLocation;
    }

    @Override
	public InputStream getInputStream()
    {
        return this.resourceInputStream;
    }

    @Override
	public boolean hasMetadata()
    {
        return this.mcmetaInputStream != null;
    }

    @Override
	public IMetadataSection getMetadata(String p_110526_1_)
    {
        if (!this.hasMetadata())
        {
            return null;
        }
        else
        {
            if (this.mcmetaJson == null && !this.mcmetaJsonChecked)
            {
                this.mcmetaJsonChecked = true;
                BufferedReader var2 = null;

                try
                {
                    var2 = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
                    this.mcmetaJson = (new JsonParser()).parse(var2).getAsJsonObject();
                }
                finally
                {
                    IOUtils.closeQuietly(var2);
                }
            }

            IMetadataSection var6 = (IMetadataSection)this.mapMetadataSections.get(p_110526_1_);

            if (var6 == null)
            {
                var6 = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
            }

            return var6;
        }
    }

    @Override
	public String getResourcePackName()
    {
        return this.resourcePackName;
    }

    @Override
	public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else if (!(p_equals_1_ instanceof SimpleResource))
        {
            return false;
        }
        else
        {
            SimpleResource var2 = (SimpleResource)p_equals_1_;

            if (this.srResourceLocation != null)
            {
                if (!this.srResourceLocation.equals(var2.srResourceLocation))
                {
                    return false;
                }
            }
            else if (var2.srResourceLocation != null)
            {
                return false;
            }

            if (this.resourcePackName != null)
            {
                if (!this.resourcePackName.equals(var2.resourcePackName))
                {
                    return false;
                }
            }
            else if (var2.resourcePackName != null)
            {
                return false;
            }

            return true;
        }
    }

    @Override
	public int hashCode()
    {
        int var1 = this.resourcePackName != null ? this.resourcePackName.hashCode() : 0;
        var1 = 31 * var1 + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
        return var1;
    }
}
