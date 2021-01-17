// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import com.google.gson.internal.Streams;
import java.io.IOException;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser
{
    public JsonElement parse(final String json) throws JsonSyntaxException {
        return this.parse(new StringReader(json));
    }
    
    public JsonElement parse(final Reader json) throws JsonIOException, JsonSyntaxException {
        try {
            final JsonReader jsonReader = new JsonReader(json);
            final JsonElement element = this.parse(jsonReader);
            if (!element.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return element;
        }
        catch (MalformedJsonException e) {
            throw new JsonSyntaxException(e);
        }
        catch (IOException e2) {
            throw new JsonIOException(e2);
        }
        catch (NumberFormatException e3) {
            throw new JsonSyntaxException(e3);
        }
    }
    
    public JsonElement parse(final JsonReader json) throws JsonIOException, JsonSyntaxException {
        final boolean lenient = json.isLenient();
        json.setLenient(true);
        try {
            return Streams.parse(json);
        }
        catch (StackOverflowError e) {
            throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e);
        }
        catch (OutOfMemoryError e2) {
            throw new JsonParseException("Failed parsing JSON source: " + json + " to Json", e2);
        }
        finally {
            json.setLenient(lenient);
        }
    }
}
