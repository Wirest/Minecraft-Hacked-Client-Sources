// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import javax.persistence.Converter;
import java.util.Map;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class ContextMapAttributeConverter implements AttributeConverter<Map<String, String>, String>
{
    public String convertToDatabaseColumn(final Map<String, String> contextMap) {
        if (contextMap == null) {
            return null;
        }
        return contextMap.toString();
    }
    
    public Map<String, String> convertToEntityAttribute(final String s) {
        throw new UnsupportedOperationException("Log events can only be persisted, not extracted.");
    }
}
