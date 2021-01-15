package net.minecraft.client.resources;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class FileResourcePack extends AbstractResourcePack implements Closeable
{
    public static final Splitter entryNameSplitter = Splitter.on('/').omitEmptyStrings().limit(3);
    private ZipFile resourcePackZipFile;

    public FileResourcePack(File resourcePackFileIn)
    {
        super(resourcePackFileIn);
    }

    private ZipFile getResourcePackZipFile() throws IOException
    {
        if (this.resourcePackZipFile == null)
        {
            this.resourcePackZipFile = new ZipFile(this.resourcePackFile);
        }

        return this.resourcePackZipFile;
    }

    @Override
	protected InputStream getInputStreamByName(String name) throws IOException
    {
        ZipFile var2 = this.getResourcePackZipFile();
        ZipEntry var3 = var2.getEntry(name);

        if (var3 == null)
        {
            throw new ResourcePackFileNotFoundException(this.resourcePackFile, name);
        }
        else
        {
            return var2.getInputStream(var3);
        }
    }

    @Override
	public boolean hasResourceName(String name)
    {
        try
        {
            return this.getResourcePackZipFile().getEntry(name) != null;
        }
        catch (IOException var3)
        {
            return false;
        }
    }

    @Override
	public Set getResourceDomains()
    {
        ZipFile var1;

        try
        {
            var1 = this.getResourcePackZipFile();
        }
        catch (IOException var8)
        {
            return Collections.emptySet();
        }

        Enumeration var2 = var1.entries();
        HashSet var3 = Sets.newHashSet();

        while (var2.hasMoreElements())
        {
            ZipEntry var4 = (ZipEntry)var2.nextElement();
            String var5 = var4.getName();

            if (var5.startsWith("assets/"))
            {
                ArrayList var6 = Lists.newArrayList(entryNameSplitter.split(var5));

                if (var6.size() > 1)
                {
                    String var7 = (String)var6.get(1);

                    if (!var7.equals(var7.toLowerCase()))
                    {
                        this.logNameNotLowercase(var7);
                    }
                    else
                    {
                        var3.add(var7);
                    }
                }
            }
        }

        return var3;
    }

    @Override
	protected void finalize() throws Throwable
    {
        this.close();
        super.finalize();
    }

    @Override
	public void close() throws IOException
    {
        if (this.resourcePackZipFile != null)
        {
            this.resourcePackZipFile.close();
            this.resourcePackZipFile = null;
        }
    }
}
