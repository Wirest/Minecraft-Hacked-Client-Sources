// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.db.jpa.converter;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.List;
import java.util.Arrays;
import org.apache.logging.log4j.core.helpers.Strings;
import java.lang.reflect.Field;
import javax.persistence.Converter;
import javax.persistence.AttributeConverter;

@Converter(autoApply = false)
public class ThrowableAttributeConverter implements AttributeConverter<Throwable, String>
{
    private static final int CAUSED_BY_STRING_LENGTH = 10;
    private static final Field THROWABLE_CAUSE;
    private static final Field THROWABLE_MESSAGE;
    
    public String convertToDatabaseColumn(final Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        final StringBuilder builder = new StringBuilder();
        this.convertThrowable(builder, throwable);
        return builder.toString();
    }
    
    private void convertThrowable(final StringBuilder builder, final Throwable throwable) {
        builder.append(throwable.toString()).append('\n');
        for (final StackTraceElement element : throwable.getStackTrace()) {
            builder.append("\tat ").append(element).append('\n');
        }
        if (throwable.getCause() != null) {
            builder.append("Caused by ");
            this.convertThrowable(builder, throwable.getCause());
        }
    }
    
    public Throwable convertToEntityAttribute(final String s) {
        if (Strings.isEmpty(s)) {
            return null;
        }
        final List<String> lines = Arrays.asList(s.split("(\n|\r\n)"));
        return this.convertString(lines.listIterator(), false);
    }
    
    private Throwable convertString(final ListIterator<String> lines, final boolean removeCausedBy) {
        String firstLine = lines.next();
        if (removeCausedBy) {
            firstLine = firstLine.substring(10);
        }
        final int colon = firstLine.indexOf(":");
        String message = null;
        String throwableClassName;
        if (colon > 1) {
            throwableClassName = firstLine.substring(0, colon);
            if (firstLine.length() > colon + 1) {
                message = firstLine.substring(colon + 1).trim();
            }
        }
        else {
            throwableClassName = firstLine;
        }
        final List<StackTraceElement> stackTrace = new ArrayList<StackTraceElement>();
        Throwable cause = null;
        while (lines.hasNext()) {
            final String line = lines.next();
            if (line.startsWith("Caused by ")) {
                lines.previous();
                cause = this.convertString(lines, true);
                break;
            }
            stackTrace.add(StackTraceElementAttributeConverter.convertString(line.trim().substring(3).trim()));
        }
        return this.getThrowable(throwableClassName, message, cause, stackTrace.toArray(new StackTraceElement[stackTrace.size()]));
    }
    
    private Throwable getThrowable(final String throwableClassName, final String message, final Throwable cause, final StackTraceElement[] stackTrace) {
        try {
            final Class<Throwable> throwableClass = (Class<Throwable>)Class.forName(throwableClassName);
            if (!Throwable.class.isAssignableFrom(throwableClass)) {
                return null;
            }
            Throwable throwable;
            if (message != null && cause != null) {
                throwable = this.getThrowable(throwableClass, message, cause);
                if (throwable == null) {
                    throwable = this.getThrowable(throwableClass, cause);
                    if (throwable == null) {
                        throwable = this.getThrowable(throwableClass, message);
                        if (throwable == null) {
                            throwable = this.getThrowable(throwableClass);
                            if (throwable != null) {
                                ThrowableAttributeConverter.THROWABLE_MESSAGE.set(throwable, message);
                                ThrowableAttributeConverter.THROWABLE_CAUSE.set(throwable, cause);
                            }
                        }
                        else {
                            ThrowableAttributeConverter.THROWABLE_CAUSE.set(throwable, cause);
                        }
                    }
                    else {
                        ThrowableAttributeConverter.THROWABLE_MESSAGE.set(throwable, message);
                    }
                }
            }
            else if (cause != null) {
                throwable = this.getThrowable(throwableClass, cause);
                if (throwable == null) {
                    throwable = this.getThrowable(throwableClass);
                    if (throwable != null) {
                        ThrowableAttributeConverter.THROWABLE_CAUSE.set(throwable, cause);
                    }
                }
            }
            else if (message != null) {
                throwable = this.getThrowable(throwableClass, message);
                if (throwable == null) {
                    throwable = this.getThrowable(throwableClass);
                    if (throwable != null) {
                        ThrowableAttributeConverter.THROWABLE_MESSAGE.set(throwable, cause);
                    }
                }
            }
            else {
                throwable = this.getThrowable(throwableClass);
            }
            if (throwable == null) {
                return null;
            }
            throwable.setStackTrace(stackTrace);
            return throwable;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private Throwable getThrowable(final Class<Throwable> throwableClass, final String message, final Throwable cause) {
        try {
            final Constructor[] arr$;
            final Constructor<Throwable>[] constructors = (Constructor<Throwable>[])(arr$ = throwableClass.getConstructors());
            for (final Constructor<Throwable> constructor : arr$) {
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 2) {
                    if (String.class == parameterTypes[0] && Throwable.class.isAssignableFrom(parameterTypes[1])) {
                        return constructor.newInstance(message, cause);
                    }
                    if (String.class == parameterTypes[1] && Throwable.class.isAssignableFrom(parameterTypes[0])) {
                        return constructor.newInstance(cause, message);
                    }
                }
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private Throwable getThrowable(final Class<Throwable> throwableClass, final Throwable cause) {
        try {
            final Constructor[] arr$;
            final Constructor<Throwable>[] constructors = (Constructor<Throwable>[])(arr$ = throwableClass.getConstructors());
            for (final Constructor<Throwable> constructor : arr$) {
                final Class<?>[] parameterTypes = constructor.getParameterTypes();
                if (parameterTypes.length == 1 && Throwable.class.isAssignableFrom(parameterTypes[0])) {
                    return constructor.newInstance(cause);
                }
            }
            return null;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private Throwable getThrowable(final Class<Throwable> throwableClass, final String message) {
        try {
            return throwableClass.getConstructor(String.class).newInstance(message);
        }
        catch (Exception e) {
            return null;
        }
    }
    
    private Throwable getThrowable(final Class<Throwable> throwableClass) {
        try {
            return throwableClass.newInstance();
        }
        catch (Exception e) {
            return null;
        }
    }
    
    static {
        try {
            (THROWABLE_CAUSE = Throwable.class.getDeclaredField("cause")).setAccessible(true);
            (THROWABLE_MESSAGE = Throwable.class.getDeclaredField("detailMessage")).setAccessible(true);
        }
        catch (NoSuchFieldException e) {
            throw new IllegalStateException("Something is wrong with java.lang.Throwable.", e);
        }
    }
}
