// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.io;

import java.io.IOException;
import com.google.common.annotations.Beta;

@Beta
public interface LineProcessor<T>
{
    boolean processLine(final String p0) throws IOException;
    
    T getResult();
}
