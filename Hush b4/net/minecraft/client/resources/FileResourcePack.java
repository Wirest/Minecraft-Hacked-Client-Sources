// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.List;
import java.util.Enumeration;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.util.zip.ZipFile;
import com.google.common.base.Splitter;
import java.io.Closeable;

public class FileResourcePack extends AbstractResourcePack implements Closeable
{
    public static final Splitter entryNameSplitter;
    private ZipFile resourcePackZipFile;
    
    static {
        entryNameSplitter = Splitter.on('/').omitEmptyStrings().limit(3);
    }
    
    public FileResourcePack(final File resourcePackFileIn) {
        super(resourcePackFileIn);
    }
    
    private ZipFile getResourcePackZipFile() throws IOException {
        if (this.resourcePackZipFile == null) {
            this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
        }
        return this.resourcePackZipFile;
    }
    
    @Override
    protected InputStream getInputStreamByName(final String name) throws IOException {
        final ZipFile zipfile = this.getResourcePackZipFile();
        final ZipEntry zipentry = zipfile.getEntry(name);
        if (zipentry == null) {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, name);
        }
        return zipfile.getInputStream(zipentry);
    }
    
    public boolean hasResourceName(final String name) {
        try {
            return this.getResourcePackZipFile().getEntry(name) != null;
        }
        catch (IOException var3) {
            return false;
        }
    }
    
    @Override
    public Set<String> getResourceDomains() {
        ZipFile zipfile;
        try {
            zipfile = this.getResourcePackZipFile();
        }
        catch (IOException var8) {
            return Collections.emptySet();
        }
        final Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
        final Set<String> set = (Set<String>)Sets.newHashSet();
        while (enumeration.hasMoreElements()) {
            final ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
            final String s = zipentry.getName();
            if (s.startsWith("assets/")) {
                final List<String> list = (List<String>)Lists.newArrayList((Iterable<?>)FileResourcePack.entryNameSplitter.split(s));
                if (list.size() <= 1) {
                    continue;
                }
                final String s2 = list.get(1);
                if (!s2.equals(s2.toLowerCase())) {
                    this.logNameNotLowercase(s2);
                }
                else {
                    set.add(s2);
                }
            }
        }
        return set;
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    
    @Override
    public void close() throws IOException {
        if (this.resourcePackZipFile != null) {
            this.resourcePackZipFile.close();
            this.resourcePackZipFile = null;
        }
    }
}
