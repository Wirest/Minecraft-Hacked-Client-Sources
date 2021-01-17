// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.message.StructuredDataId;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "LoggerFields", category = "Core", printObject = true)
public final class LoggerFields
{
    private final Map<String, String> map;
    private final String sdId;
    private final String enterpriseId;
    private final boolean discardIfAllFieldsAreEmpty;
    
    private LoggerFields(final Map<String, String> map, final String sdId, final String enterpriseId, final boolean discardIfAllFieldsAreEmpty) {
        this.sdId = sdId;
        this.enterpriseId = enterpriseId;
        this.map = Collections.unmodifiableMap((Map<? extends String, ? extends String>)map);
        this.discardIfAllFieldsAreEmpty = discardIfAllFieldsAreEmpty;
    }
    
    public Map<String, String> getMap() {
        return this.map;
    }
    
    @Override
    public String toString() {
        return this.map.toString();
    }
    
    @PluginFactory
    public static LoggerFields createLoggerFields(@PluginElement("LoggerFields") final KeyValuePair[] keyValuePairs, @PluginAttribute("sdId") final String sdId, @PluginAttribute("enterpriseId") final String enterpriseId, @PluginAttribute("discardIfAllFieldsAreEmpty") final String discardIfAllFieldsAreEmpty) {
        final Map<String, String> map = new HashMap<String, String>();
        for (final KeyValuePair keyValuePair : keyValuePairs) {
            map.put(keyValuePair.getKey(), keyValuePair.getValue());
        }
        final boolean discardIfEmpty = Booleans.parseBoolean(discardIfAllFieldsAreEmpty, false);
        return new LoggerFields(map, sdId, enterpriseId, discardIfEmpty);
    }
    
    public StructuredDataId getSdId() {
        if (this.enterpriseId == null || this.sdId == null) {
            return null;
        }
        final int eId = Integer.parseInt(this.enterpriseId);
        return new StructuredDataId(this.sdId, eId, null, null);
    }
    
    public boolean getDiscardIfAllFieldsAreEmpty() {
        return this.discardIfAllFieldsAreEmpty;
    }
}
