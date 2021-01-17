// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.auth;

public interface NTLMEngine
{
    String generateType1Msg(final String p0, final String p1) throws NTLMEngineException;
    
    String generateType3Msg(final String p0, final String p1, final String p2, final String p3, final String p4) throws NTLMEngineException;
}
