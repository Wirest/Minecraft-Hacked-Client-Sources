// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public interface IProgressUpdate
{
    void displaySavingString(final String p0);
    
    void resetProgressAndMessage(final String p0);
    
    void displayLoadingString(final String p0);
    
    void setLoadingProgress(final int p0);
    
    void setDoneWorking();
}
