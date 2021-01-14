package com.google.gson;

public enum LongSerializationPolicy {
    DEFAULT, STRING;

    private LongSerializationPolicy() {
    }

    public abstract JsonElement serialize(Long paramLong);
}




