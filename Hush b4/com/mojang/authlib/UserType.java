// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.authlib;

import java.util.HashMap;
import java.util.Map;

public enum UserType
{
    LEGACY("legacy"), 
    MOJANG("mojang");
    
    private static final Map<String, UserType> BY_NAME;
    private final String name;
    
    private UserType(final String name) {
        this.name = name;
    }
    
    public static UserType byName(final String name) {
        return UserType.BY_NAME.get(name.toLowerCase());
    }
    
    public String getName() {
        return this.name;
    }
    
    static {
        BY_NAME = new HashMap<String, UserType>();
        for (final UserType type : values()) {
            UserType.BY_NAME.put(type.name, type);
        }
    }
}
