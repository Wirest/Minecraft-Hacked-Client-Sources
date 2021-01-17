// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.io.FileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import com.google.common.collect.Sets;
import java.util.Set;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

public class FolderResourcePack extends AbstractResourcePack
{
    public FolderResourcePack(final File resourcePackFileIn) {
        super(resourcePackFileIn);
    }
    
    @Override
    protected InputStream getInputStreamByName(final String name) throws IOException {
        return new BufferedInputStream(new FileInputStream(new File(this.resourcePackFile, name)));
    }
    
    @Override
    protected boolean hasResourceName(final String name) {
        return new File(this.resourcePackFile, name).isFile();
    }
    
    @Override
    public Set<String> getResourceDomains() {
        final Set<String> set = (Set<String>)Sets.newHashSet();
        final File file1 = new File(this.resourcePackFile, "assets/");
        if (file1.isDirectory()) {
            File[] listFiles;
            for (int length = (listFiles = file1.listFiles((FileFilter)DirectoryFileFilter.DIRECTORY)).length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                final String s = AbstractResourcePack.getRelativeName(file1, file2);
                if (!s.equals(s.toLowerCase())) {
                    this.logNameNotLowercase(s);
                }
                else {
                    set.add(s.substring(0, s.length() - 1));
                }
            }
        }
        return set;
    }
}
