// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware;

public interface HardwareAbstractionLayer
{
    Processor[] getProcessors();
    
    Memory getMemory();
}
