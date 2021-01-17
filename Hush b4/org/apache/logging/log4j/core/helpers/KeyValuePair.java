// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "KeyValuePair", category = "Core", printObject = true)
public class KeyValuePair
{
    private final String key;
    private final String value;
    
    public KeyValuePair(final String key, final String value) {
        this.key = key;
        this.value = value;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public String toString() {
        return this.key + "=" + this.value;
    }
    
    @PluginFactory
    public static KeyValuePair createPair(@PluginAttribute("key") final String key, @PluginAttribute("value") final String value) {
        return new KeyValuePair(key, value);
    }
}
