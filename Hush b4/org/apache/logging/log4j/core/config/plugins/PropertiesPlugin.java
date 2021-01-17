// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config.plugins;

import org.apache.logging.log4j.core.lookup.MapLookup;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.lookup.Interpolator;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;

@Plugin(name = "properties", category = "Core", printObject = true)
public final class PropertiesPlugin
{
    private PropertiesPlugin() {
    }
    
    @PluginFactory
    public static StrLookup configureSubstitutor(@PluginElement("Properties") final Property[] properties, @PluginConfiguration final Configuration config) {
        if (properties == null) {
            return new Interpolator(null);
        }
        final Map<String, String> map = new HashMap<String, String>(config.getProperties());
        for (final Property prop : properties) {
            map.put(prop.getName(), prop.getValue());
        }
        return new Interpolator(new MapLookup(map));
    }
}
