package com.google.gson;

import java.lang.reflect.Type;

public abstract interface JsonDeserializationContext {
    public abstract <T> T deserialize(JsonElement paramJsonElement, Type paramType)
            throws JsonParseException;
}




