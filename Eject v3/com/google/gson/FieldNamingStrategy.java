package com.google.gson;

import java.lang.reflect.Field;

public abstract interface FieldNamingStrategy {
    public abstract String translateName(Field paramField);
}




