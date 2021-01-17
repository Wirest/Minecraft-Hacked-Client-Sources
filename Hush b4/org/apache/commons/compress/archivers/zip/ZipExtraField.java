// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.util.zip.ZipException;

public interface ZipExtraField
{
    ZipShort getHeaderId();
    
    ZipShort getLocalFileDataLength();
    
    ZipShort getCentralDirectoryLength();
    
    byte[] getLocalFileDataData();
    
    byte[] getCentralDirectoryData();
    
    void parseFromLocalFileData(final byte[] p0, final int p1, final int p2) throws ZipException;
    
    void parseFromCentralDirectoryData(final byte[] p0, final int p1, final int p2) throws ZipException;
}
