// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

import java.io.IOException;
import com.google.gson.stream.JsonReader;

public abstract class JsonReaderInternalAccess
{
    public static JsonReaderInternalAccess INSTANCE;
    
    public abstract void promoteNameToValue(final JsonReader p0) throws IOException;
}
