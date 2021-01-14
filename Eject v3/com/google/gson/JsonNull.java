package com.google.gson;

public final class JsonNull
        extends JsonElement {
    public static final JsonNull INSTANCE = new JsonNull();

    JsonNull deepCopy() {
        return INSTANCE;
    }

    public int hashCode() {
        return JsonNull.class.hashCode();
    }

    public boolean equals(Object paramObject) {
        return (this == paramObject) || ((paramObject instanceof JsonNull));
    }
}




