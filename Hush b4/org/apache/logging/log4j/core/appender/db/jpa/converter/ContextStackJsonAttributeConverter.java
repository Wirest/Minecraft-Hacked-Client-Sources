// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.util.Collection;
import org.apache.logging.log4j.spi.DefaultThreadContextStack;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import org.apache.logging.log4j.core.helpers.Strings;
import java.io.IOException;
import javax.persistence.PersistenceException;
import javax.persistence.Converter;
import org.apache.logging.log4j.ThreadContext;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class ContextStackJsonAttributeConverter implements AttributeConverter<ThreadContext.ContextStack, String>
{
    public String convertToDatabaseColumn(final ThreadContext.ContextStack contextStack) {
        if (contextStack == null) {
            return null;
        }
        try {
            return ContextMapJsonAttributeConverter.OBJECT_MAPPER.writeValueAsString((Object)contextStack.asList());
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to convert stack list to JSON string.", (Throwable)e);
        }
    }
    
    public ThreadContext.ContextStack convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        List<String> list;
        try {
            list = (List<String>)ContextMapJsonAttributeConverter.OBJECT_MAPPER.readValue(s, (TypeReference)new TypeReference<List<String>>() {});
        }
        catch (IOException e) {
            throw new PersistenceException("Failed to convert JSON string to list for stack.", (Throwable)e);
        }
        final DefaultThreadContextStack result = new DefaultThreadContextStack(true);
        result.addAll(list);
        return result;
    }
}
