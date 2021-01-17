// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.StringUtils;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;

public class GameProfile
{
    private final UUID id;
    private final String name;
    private final PropertyMap properties;
    private boolean legacy;
    
    public GameProfile(final UUID id, final String name) {
        this.properties = new PropertyMap();
        if (id == null && StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("Name and ID cannot both be blank");
        }
        this.id = id;
        this.name = name;
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public PropertyMap getProperties() {
        return this.properties;
    }
    
    public boolean isComplete() {
        return this.id != null && StringUtils.isNotBlank(this.getName());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final GameProfile that = (GameProfile)o;
        Label_0062: {
            if (this.id != null) {
                if (this.id.equals(that.id)) {
                    break Label_0062;
                }
            }
            else if (that.id == null) {
                break Label_0062;
            }
            return false;
        }
        if (this.name != null) {
            if (this.name.equals(that.name)) {
                return true;
            }
        }
        else if (that.name == null) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.id != null) ? this.id.hashCode() : 0;
        result = 31 * result + ((this.name != null) ? this.name.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("properties", this.properties).append("legacy", this.legacy).toString();
    }
    
    public boolean isLegacy() {
        return this.legacy;
    }
}
