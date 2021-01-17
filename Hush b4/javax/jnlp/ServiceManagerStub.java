// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

public interface ServiceManagerStub
{
    String[] getServiceNames();
    
    Object lookup(final String p0) throws UnavailableServiceException;
}
