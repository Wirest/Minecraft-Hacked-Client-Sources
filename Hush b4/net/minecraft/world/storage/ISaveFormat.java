// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import net.minecraft.util.IProgressUpdate;
import net.minecraft.client.AnvilConverterException;
import java.util.List;

public interface ISaveFormat
{
    String getName();
    
    ISaveHandler getSaveLoader(final String p0, final boolean p1);
    
    List<SaveFormatComparator> getSaveList() throws AnvilConverterException;
    
    void flushCache();
    
    WorldInfo getWorldInfo(final String p0);
    
    boolean func_154335_d(final String p0);
    
    boolean deleteWorldDirectory(final String p0);
    
    void renameWorld(final String p0, final String p1);
    
    boolean func_154334_a(final String p0);
    
    boolean isOldMapFormat(final String p0);
    
    boolean convertMapFormat(final String p0, final IProgressUpdate p1);
    
    boolean canLoadWorld(final String p0);
}
