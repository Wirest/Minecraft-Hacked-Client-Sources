package com.google.gson;

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
    public JsonElement parse(String paramString)
            throws JsonSyntaxException {
        return parse(new StringReader(paramString));
    }

    public JsonElement parse(Reader paramReader)
            throws JsonIOException, JsonSyntaxException {
        try {
            JsonReader localJsonReader = new JsonReader(paramReader);
            JsonElement localJsonElement = parse(localJsonReader);
            if ((!localJsonElement.isJsonNull()) && (localJsonReader.peek() != JsonToken.END_DOCUMENT)) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return localJsonElement;
        } catch (MalformedJsonException localMalformedJsonException) {
            throw new JsonSyntaxException(localMalformedJsonException);
        } catch (IOException localIOException) {
            throw new JsonIOException(localIOException);
        } catch (NumberFormatException localNumberFormatException) {
            throw new JsonSyntaxException(localNumberFormatException);
        }
    }

    public JsonElement parse(JsonReader paramJsonReader)
            throws JsonIOException, JsonSyntaxException {
        boolean bool = paramJsonReader.isLenient();
        paramJsonReader.setLenient(true);
        try {
            JsonElement localJsonElement = Streams.parse(paramJsonReader);
            return localJsonElement;
        } catch (StackOverflowError localStackOverflowError) {
            throw new JsonParseException("Failed parsing JSON source: " + paramJsonReader + " to Json", localStackOverflowError);
        } catch (OutOfMemoryError localOutOfMemoryError) {
            throw new JsonParseException("Failed parsing JSON source: " + paramJsonReader + " to Json", localOutOfMemoryError);
        } finally {
            paramJsonReader.setLenient(bool);
        }
    }
}




