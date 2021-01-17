// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal.bind;

import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import com.google.gson.TypeAdapterFactory;
import java.sql.Time;
import com.google.gson.TypeAdapter;

public final class TimeTypeAdapter extends TypeAdapter<Time>
{
    public static final TypeAdapterFactory FACTORY;
    private final DateFormat format;
    
    public TimeTypeAdapter() {
        this.format = new SimpleDateFormat("hh:mm:ss a");
    }
    
    @Override
    public synchronized Time read(final JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        try {
            final Date date = this.format.parse(in.nextString());
            return new Time(date.getTime());
        }
        catch (ParseException e) {
            throw new JsonSyntaxException(e);
        }
    }
    
    @Override
    public synchronized void write(final JsonWriter out, final Time value) throws IOException {
        out.value((value == null) ? null : this.format.format(value));
    }
    
    static {
        FACTORY = new TypeAdapterFactory() {
            public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
                return (TypeAdapter<T>)((typeToken.getRawType() == Time.class) ? new TimeTypeAdapter() : null);
            }
        };
    }
}
