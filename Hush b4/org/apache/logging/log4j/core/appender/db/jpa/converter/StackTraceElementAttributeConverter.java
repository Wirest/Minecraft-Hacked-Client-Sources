// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import org.apache.logging.log4j.core.helpers.Strings;
import javax.persistence.Converter;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class StackTraceElementAttributeConverter implements AttributeConverter<StackTraceElement, String>
{
    private static final int UNKNOWN_SOURCE = -1;
    private static final int NATIVE_METHOD = -2;
    
    public String convertToDatabaseColumn(final StackTraceElement element) {
        if (element == null) {
            return null;
        }
        return element.toString();
    }
    
    public StackTraceElement convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        return convertString(s);
    }
    
    static StackTraceElement convertString(final String s) {
        final int open = s.indexOf("(");
        final String classMethod = s.substring(0, open);
        final String className = classMethod.substring(0, classMethod.lastIndexOf("."));
        final String methodName = classMethod.substring(classMethod.lastIndexOf(".") + 1);
        final String parenthesisContents = s.substring(open + 1, s.indexOf(")"));
        String fileName = null;
        int lineNumber = -1;
        if ("Native Method".equals(parenthesisContents)) {
            lineNumber = -2;
        }
        else if (!"Unknown Source".equals(parenthesisContents)) {
            final int colon = parenthesisContents.indexOf(":");
            if (colon > -1) {
                fileName = parenthesisContents.substring(0, colon);
                try {
                    lineNumber = Integer.parseInt(parenthesisContents.substring(colon + 1));
                }
                catch (NumberFormatException ignore) {}
            }
            else {
                fileName = parenthesisContents.substring(0);
            }
        }
        return new StackTraceElement(className, methodName, fileName, lineNumber);
    }
}
