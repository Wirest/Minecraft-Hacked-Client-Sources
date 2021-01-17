// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson;

import com.google.gson.reflect.TypeToken;

public interface TypeAdapterFactory
{
     <T> TypeAdapter<T> create(final Gson p0, final TypeToken<T> p1);
}
