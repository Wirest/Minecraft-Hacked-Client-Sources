// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http;

import java.util.Iterator;

public interface HeaderElementIterator extends Iterator<Object>
{
    boolean hasNext();
    
    HeaderElement nextElement();
}
