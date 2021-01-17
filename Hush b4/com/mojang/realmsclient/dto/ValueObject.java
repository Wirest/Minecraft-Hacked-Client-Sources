// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import java.lang.reflect.Modifier;
import java.lang.reflect.Field;

public abstract class ValueObject
{
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        for (final Field f : this.getClass().getFields()) {
            if (!isStatic(f)) {
                try {
                    sb.append(f.getName()).append("=").append(f.get(this)).append(" ");
                }
                catch (IllegalAccessException ignore) {}
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('}');
        return sb.toString();
    }
    
    private static boolean isStatic(final Field f) {
        return Modifier.isStatic(f.getModifiers());
    }
}
