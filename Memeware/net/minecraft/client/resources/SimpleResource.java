package net.minecraft.client.resources;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

public class SimpleResource implements IResource {
    private final Map mapMetadataSections = Maps.newHashMap();
    private final String field_177242_b;
    private final ResourceLocation srResourceLocation;
    private final InputStream resourceInputStream;
    private final InputStream mcmetaInputStream;
    private final IMetadataSerializer srMetadataSerializer;
    private boolean mcmetaJsonChecked;
    private JsonObject mcmetaJson;
    private static final String __OBFID = "CL_00001093";

    public SimpleResource(String p_i46090_1_, ResourceLocation p_i46090_2_, InputStream p_i46090_3_, InputStream p_i46090_4_, IMetadataSerializer p_i46090_5_) {
        this.field_177242_b = p_i46090_1_;
        this.srResourceLocation = p_i46090_2_;
        this.resourceInputStream = p_i46090_3_;
        this.mcmetaInputStream = p_i46090_4_;
        this.srMetadataSerializer = p_i46090_5_;
    }

    public ResourceLocation func_177241_a() {
        return this.srResourceLocation;
    }

    public InputStream getInputStream() {
        return this.resourceInputStream;
    }

    public boolean hasMetadata() {
        return this.mcmetaInputStream != null;
    }

    public IMetadataSection getMetadata(String p_110526_1_) {
        if (!this.hasMetadata()) {
            return null;
        } else {
            if (this.mcmetaJson == null && !this.mcmetaJsonChecked) {
                this.mcmetaJsonChecked = true;
                BufferedReader var2 = null;

                try {
                    var2 = new BufferedReader(new InputStreamReader(this.mcmetaInputStream));
                    this.mcmetaJson = (new JsonParser()).parse(var2).getAsJsonObject();
                } finally {
                    IOUtils.closeQuietly(var2);
                }
            }

            IMetadataSection var6 = (IMetadataSection) this.mapMetadataSections.get(p_110526_1_);

            if (var6 == null) {
                var6 = this.srMetadataSerializer.parseMetadataSection(p_110526_1_, this.mcmetaJson);
            }

            return var6;
        }
    }

    public String func_177240_d() {
        return this.field_177242_b;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (!(p_equals_1_ instanceof SimpleResource)) {
            return false;
        } else {
            SimpleResource var2 = (SimpleResource) p_equals_1_;

            if (this.srResourceLocation != null) {
                if (!this.srResourceLocation.equals(var2.srResourceLocation)) {
                    return false;
                }
            } else if (var2.srResourceLocation != null) {
                return false;
            }

            if (this.field_177242_b != null) {
                if (!this.field_177242_b.equals(var2.field_177242_b)) {
                    return false;
                }
            } else if (var2.field_177242_b != null) {
                return false;
            }

            return true;
        }
    }

    public int hashCode() {
        int var1 = this.field_177242_b != null ? this.field_177242_b.hashCode() : 0;
        var1 = 31 * var1 + (this.srResourceLocation != null ? this.srResourceLocation.hashCode() : 0);
        return var1;
    }
}
