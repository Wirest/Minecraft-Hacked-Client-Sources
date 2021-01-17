// 
// Decompiled by Procyon v0.5.36
// 

package oshi.software.os;

public interface OperatingSystem
{
    String getFamily();
    
    String getManufacturer();
    
    OperatingSystemVersion getVersion();
}
