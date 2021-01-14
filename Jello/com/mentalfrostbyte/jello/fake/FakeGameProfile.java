package com.mentalfrostbyte.jello.fake;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class FakeGameProfile extends GameProfile {
    private final UUID id;
    private final String name;
    private final PropertyMap properties = new PropertyMap();
    private boolean legacy;

    public FakeGameProfile(UUID id, String name) {
    	super(id, name);
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

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        GameProfile that = (GameProfile)o;
        if (this.id != null ? !this.id.equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (this.name != null ? !this.name.equals(that.getName()) : that.getName() != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = this.id != null ? this.id.hashCode() : 0;
        result = 31 * result + (this.name != null ? this.name.hashCode() : 0);
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this).append("id", this.id).append("name", this.name).append("properties", this.properties).append("legacy", this.legacy).toString();
    }

    public boolean isLegacy() {
        return this.legacy;
    }
}