// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import org.apache.commons.io.IOUtils;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.data.IMetadataSerializer;
import java.io.InputStream;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.data.IMetadataSection;
import java.util.Map;

public class SimpleResource implements IResource
{
    private final Map<String, IMetadataSection> mapMetadataSections;
    private final String resourcePackName;
    private final ResourceLocation srResourceLocation;
    private final InputStream resourceInputStream;
    private final InputStream mcmetaInputStream;
    private final IMetadataSerializer srMetadataSerializer;
    private boolean mcmetaJsonChecked;
    private JsonObject mcmetaJson;
    
    public SimpleResource(final String resourcePackNameIn, final ResourceLocation srResourceLocationIn, final InputStream resourceInputStreamIn, final InputStream mcmetaInputStreamIn, final IMetadataSerializer srMetadataSerializerIn) {
        this.mapMetadataSections = (Map<String, IMetadataSection>)Maps.newHashMap();
        this.resourcePackName = resourcePackNameIn;
        this.srResourceLocation = srResourceLocationIn;
        this.resourceInputStream = resourceInputStreamIn;
        this.mcmetaInputStream = mcmetaInputStreamIn;
        this.srMetadataSerializer = srMetadataSerializerIn;
    }
    
    @Override
    public ResourceLocation getResourceLocation() {
        return this.srResourceLocation;
    }
    
    @Override
    public InputStream getInputStream() {
        return this.resourceInputStream;
    }
    
    @Override
    public boolean hasMetadata() {
        return this.mcmetaInputStream != null;
    }
    
    @Override
    public <T extends IMetadataSection> T getMetadata(final String p_110526_1_) {
        if (!this.hasMetadata()) {
            return null;
        }
        if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
            this.mcmetaJsonChecked = true;
            BufferedReader bufferedreader = null;
            try {
                bufferedreader = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
                this.mcmetaJson = new JsonParser().parse(bufferedreader).getAsJsonObject();
            }
            finally {
                IOUtils.closeQuietly(bufferedreader);
            }
            IOUtils.closeQuietly(bufferedreader);
        }
        T t = (T)this.mapMetadataSections.get(p_110526_1_);
        if (t == null) {
            t = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
        }
        return t;
    }
    
    @Override
    public String getResourcePackName() {
        return this.resourcePackName;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (!(p_equals_1_ instanceof SimpleResource)) {
            return false;
        }
        final SimpleResource simpleresource = (SimpleResource)p_equals_1_;
        if (this.srResourceLocation != null) {
            if (!this.srResourceLocation.equals(simpleresource.srResourceLocation)) {
                return false;
            }
        }
        else if (simpleresource.srResourceLocation != null) {
            return false;
        }
        if (this.resourcePackName != null) {
            if (!this.resourcePackName.equals(simpleresource.resourcePackName)) {
                return false;
            }
        }
        else if (simpleresource.resourcePackName != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int i = (this.resourcePackName != null) ? this.resourcePackName.hashCode() : 0;
        i = 31 * i + ((this.srResourceLocation != null) ? this.srResourceLocation.hashCode() : 0);
        return i;
    }
}
