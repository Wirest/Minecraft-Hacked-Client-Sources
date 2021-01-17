// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

public interface Processor
{
    String getVendor();
    
    void setVendor(final String p0);
    
    String getName();
    
    void setName(final String p0);
    
    String getIdentifier();
    
    void setIdentifier(final String p0);
    
    boolean isCpu64bit();
    
    void setCpu64(final boolean p0);
    
    String getStepping();
    
    void setStepping(final String p0);
    
    String getModel();
    
    void setModel(final String p0);
    
    String getFamily();
    
    void setFamily(final String p0);
    
    float getLoad();
}
