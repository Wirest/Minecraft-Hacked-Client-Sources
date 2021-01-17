// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib.minecraft;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.io.FilenameUtils;
import javax.annotation.Nullable;
import java.util.Map;

public class MinecraftProfileTexture
{
    private final String url;
    private final Map<String, String> metadata;
    
    public MinecraftProfileTexture(final String url, final Map<String, String> metadata) {
        this.url = url;
        this.metadata = metadata;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    @Nullable
    public String getMetadata(final String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }
    
    public String getHash() {
        return FilenameUtils.getBaseName(this.url);
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("url", this.url).append("hash", this.getHash()).toString();
    }
    
    public enum Type
    {
        SKIN, 
        CAPE;
    }
}
