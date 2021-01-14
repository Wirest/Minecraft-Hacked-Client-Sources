package com.google.gson;

import java.lang.reflect.Type;

public abstract interface InstanceCreator<T> {
    public abstract T createInstance(Type paramType);
}




