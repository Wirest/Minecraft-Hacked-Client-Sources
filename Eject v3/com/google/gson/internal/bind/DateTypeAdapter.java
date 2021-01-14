package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class DateTypeAdapter
        extends TypeAdapter<Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson paramAnonymousGson, TypeToken<T> paramAnonymousTypeToken) {
            return paramAnonymousTypeToken.getRawType() == Date.class ? new DateTypeAdapter() : null;
        }
    };
    private final DateFormat enUsFormat = DateFormat.getDateTimeInstance(2, 2, Locale.US);
    private final DateFormat localFormat = DateFormat.getDateTimeInstance(2, 2);
    private final DateFormat iso8601Format = buildIso8601Format();

    private static DateFormat buildIso8601Format() {
        SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return localSimpleDateFormat;
    }

    public Date read(JsonReader paramJsonReader)
            throws IOException {
        if (paramJsonReader.peek() == JsonToken.NULL) {
            paramJsonReader.nextNull();
            return null;
        }
        return deserializeToDate(paramJsonReader.nextString());
    }

    private synchronized Date deserializeToDate(String paramString) {
        try {
            return this.localFormat.parse(paramString);
        } catch (ParseException localParseException1) {
            try {
                return this.enUsFormat.parse(paramString);
            } catch (ParseException localParseException2) {
                try {
                    return this.iso8601Format.parse(paramString);
                } catch (ParseException localParseException3) {
                    throw new JsonSyntaxException(paramString, localParseException3);
                }
            }
        }
    }

    public synchronized void write(JsonWriter paramJsonWriter, Date paramDate)
            throws IOException {
        if (paramDate == null) {
            paramJsonWriter.nullValue();
            return;
        }
        String str = this.enUsFormat.format(paramDate);
        paramJsonWriter.value(str);
    }
}




