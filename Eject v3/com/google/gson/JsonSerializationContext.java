package com.google.gson;

import java.lang.reflect.Type;

public abstract interface JsonSerializationContext {
    public abstract JsonElement serialize(Object paramObject);

    public abstract JsonElement serialize(Object paramObject, Type paramType);
}




