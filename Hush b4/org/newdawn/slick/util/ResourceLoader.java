// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.util;

import java.net.URL;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;

public class ResourceLoader
{
    private static ArrayList locations;
    
    static {
        (ResourceLoader.locations = new ArrayList()).add(new ClasspathLocation());
        ResourceLoader.locations.add(new FileSystemLocation(new File(".")));
    }
    
    public static void addResourceLocation(final ResourceLocation location) {
        ResourceLoader.locations.add(location);
    }
    
    public static void removeResourceLocation(final ResourceLocation location) {
        ResourceLoader.locations.remove(location);
    }
    
    public static void removeAllResourceLocations() {
        ResourceLoader.locations.clear();
    }
    
    public static InputStream getResourceAsStream(final String ref) {
        InputStream in = null;
        for (int i = 0; i < ResourceLoader.locations.size(); ++i) {
            final ResourceLocation location = ResourceLoader.locations.get(i);
            in = location.getResourceAsStream(ref);
            if (in != null) {
                break;
            }
        }
        if (in == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }
        return new BufferedInputStream(in);
    }
    
    public static boolean resourceExists(final String ref) {
        URL url = null;
        for (int i = 0; i < ResourceLoader.locations.size(); ++i) {
            final ResourceLocation location = ResourceLoader.locations.get(i);
            url = location.getResource(ref);
            if (url != null) {
                return true;
            }
        }
        return false;
    }
    
    public static URL getResource(final String ref) {
        URL url = null;
        for (int i = 0; i < ResourceLoader.locations.size(); ++i) {
            final ResourceLocation location = ResourceLoader.locations.get(i);
            url = location.getResource(ref);
            if (url != null) {
                break;
            }
        }
        if (url == null) {
            throw new RuntimeException("Resource not found: " + ref);
        }
        return url;
    }
}
