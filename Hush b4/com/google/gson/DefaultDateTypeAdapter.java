// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import java.text.ParseException;
import java.sql.Timestamp;
import java.lang.reflect.Type;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.Date;

final class DefaultDateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
{
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;
    private final DateFormat iso8601Format;
    
    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }
    
    DefaultDateTypeAdapter(final String datePattern) {
        this(new SimpleDateFormat(datePattern, Locale.US), new SimpleDateFormat(datePattern));
    }
    
    DefaultDateTypeAdapter(final int style) {
        this(DateFormat.getDateInstance(style, Locale.US), DateFormat.getDateInstance(style));
    }
    
    public DefaultDateTypeAdapter(final int dateStyle, final int timeStyle) {
        this(DateFormat.getDateTimeInstance(dateStyle, timeStyle, Locale.US), DateFormat.getDateTimeInstance(dateStyle, timeStyle));
    }
    
    DefaultDateTypeAdapter(final DateFormat enUsFormat, final DateFormat localFormat) {
        this.enUsFormat = enUsFormat;
        this.localFormat = localFormat;
        (this.iso8601Format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)).setTimeZone(TimeZone.getTimeZone("UTC"));
    }
    
    public JsonElement serialize(final Date src, final Type typeOfSrc, final JsonSerializationContext context) {
        synchronized (this.localFormat) {
            final String dateFormatAsString = this.enUsFormat.format(src);
            return new JsonPrimitive(dateFormatAsString);
        }
    }
    
    public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        final Date date = this.deserializeToDate(json);
        if (typeOfT == Date.class) {
            return date;
        }
        if (typeOfT == Timestamp.class) {
            return new Timestamp(date.getTime());
        }
        if (typeOfT == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        }
        throw new IllegalArgumentException(this.getClass() + " cannot deserialize to " + typeOfT);
    }
    
    private Date deserializeToDate(final JsonElement json) {
        synchronized (this.localFormat) {
            try {
                return this.localFormat.parse(json.getAsString());
            }
            catch (ParseException ignored) {
                try {
                    return this.enUsFormat.parse(json.getAsString());
                }
                catch (ParseException ignored) {
                    try {
                        // monitorexit(this.localFormat)
                        return this.iso8601Format.parse(json.getAsString());
                    }
                    catch (ParseException e) {
                        throw new JsonSyntaxException(json.getAsString(), e);
                    }
                }
            }
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DefaultDateTypeAdapter.class.getSimpleName());
        sb.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
        return sb.toString();
    }
}
