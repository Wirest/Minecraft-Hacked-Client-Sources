// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

@Deprecated
public interface HttpParams
{
    Object getParameter(final String p0);
    
    HttpParams setParameter(final String p0, final Object p1);
    
    HttpParams copy();
    
    boolean removeParameter(final String p0);
    
    long getLongParameter(final String p0, final long p1);
    
    HttpParams setLongParameter(final String p0, final long p1);
    
    int getIntParameter(final String p0, final int p1);
    
    HttpParams setIntParameter(final String p0, final int p1);
    
    double getDoubleParameter(final String p0, final double p1);
    
    HttpParams setDoubleParameter(final String p0, final double p1);
    
    boolean getBooleanParameter(final String p0, final boolean p1);
    
    HttpParams setBooleanParameter(final String p0, final boolean p1);
    
    boolean isParameterTrue(final String p0);
    
    boolean isParameterFalse(final String p0);
}
