// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.util;

import joptsimple.ValueConversionException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import joptsimple.ValueConverter;

public class DateConverter implements ValueConverter<Date>
{
    private final DateFormat formatter;
    
    public DateConverter(final DateFormat formatter) {
        if (formatter == null) {
            throw new NullPointerException("illegal null formatter");
        }
        this.formatter = formatter;
    }
    
    public static DateConverter datePattern(final String pattern) {
        final SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        formatter.setLenient(false);
        return new DateConverter(formatter);
    }
    
    public Date convert(final String value) {
        final ParsePosition position = new ParsePosition(0);
        final Date date = this.formatter.parse(value, position);
        if (position.getIndex() != value.length()) {
            throw new ValueConversionException(this.message(value));
        }
        return date;
    }
    
    public Class<Date> valueType() {
        return Date.class;
    }
    
    public String valuePattern() {
        return (this.formatter instanceof SimpleDateFormat) ? ((SimpleDateFormat)this.formatter).toPattern() : "";
    }
    
    private String message(final String value) {
        String message = "Value [" + value + "] does not match date/time pattern";
        if (this.formatter instanceof SimpleDateFormat) {
            message = message + " [" + ((SimpleDateFormat)this.formatter).toPattern() + ']';
        }
        return message;
    }
}
