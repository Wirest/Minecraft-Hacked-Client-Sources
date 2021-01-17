// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.jar;

import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipExtraField;
import org.apache.commons.compress.archivers.zip.JarMarker;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

public class JarArchiveOutputStream extends ZipArchiveOutputStream
{
    private boolean jarMarkerAdded;
    
    public JarArchiveOutputStream(final OutputStream out) {
        super(out);
        this.jarMarkerAdded = false;
    }
    
    @Override
    public void putArchiveEntry(final ArchiveEntry ze) throws IOException {
        if (!this.jarMarkerAdded) {
            ((ZipArchiveEntry)ze).addAsFirstExtraField(JarMarker.getInstance());
            this.jarMarkerAdded = true;
        }
        super.putArchiveEntry(ze);
    }
}
