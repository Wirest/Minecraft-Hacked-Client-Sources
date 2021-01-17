// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

public class ClassPathUtils
{
    public static String toFullyQualifiedName(final Class<?> context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context");
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        return toFullyQualifiedName(context.getPackage(), resourceName);
    }
    
    public static String toFullyQualifiedName(final Package context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context");
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getName());
        sb.append(".");
        sb.append(resourceName);
        return sb.toString();
    }
    
    public static String toFullyQualifiedPath(final Class<?> context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context");
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        return toFullyQualifiedPath(context.getPackage(), resourceName);
    }
    
    public static String toFullyQualifiedPath(final Package context, final String resourceName) {
        Validate.notNull(context, "Parameter '%s' must not be null!", "context");
        Validate.notNull(resourceName, "Parameter '%s' must not be null!", "resourceName");
        final StringBuilder sb = new StringBuilder();
        sb.append(context.getName().replace('.', '/'));
        sb.append("/");
        sb.append(resourceName);
        return sb.toString();
    }
}
