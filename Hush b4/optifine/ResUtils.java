// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Enumeration;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import java.util.ArrayList;
import java.io.File;
import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import java.util.Set;
import java.util.Collection;
import java.util.Arrays;
import java.util.LinkedHashSet;

public class ResUtils
{
    public static String[] collectFiles(final String p_collectFiles_0_, final String p_collectFiles_1_) {
        return collectFiles(new String[] { p_collectFiles_0_ }, new String[] { p_collectFiles_1_ });
    }
    
    public static String[] collectFiles(final String[] p_collectFiles_0_, final String[] p_collectFiles_1_) {
        final Set<String> set = new LinkedHashSet<String>();
        final IResourcePack[] airesourcepack = Config.getResourcePacks();
        for (int i = 0; i < airesourcepack.length; ++i) {
            final IResourcePack iresourcepack = airesourcepack[i];
            final String[] astring = collectFiles(iresourcepack, p_collectFiles_0_, p_collectFiles_1_, null);
            set.addAll(Arrays.asList(astring));
        }
        final String[] astring2 = set.toArray(new String[set.size()]);
        return astring2;
    }
    
    public static String[] collectFiles(final IResourcePack p_collectFiles_0_, final String p_collectFiles_1_, final String p_collectFiles_2_, final String[] p_collectFiles_3_) {
        return collectFiles(p_collectFiles_0_, new String[] { p_collectFiles_1_ }, new String[] { p_collectFiles_2_ }, p_collectFiles_3_);
    }
    
    public static String[] collectFiles(final IResourcePack p_collectFiles_0_, final String[] p_collectFiles_1_, final String[] p_collectFiles_2_) {
        return collectFiles(p_collectFiles_0_, p_collectFiles_1_, p_collectFiles_2_, null);
    }
    
    public static String[] collectFiles(final IResourcePack p_collectFiles_0_, final String[] p_collectFiles_1_, final String[] p_collectFiles_2_, final String[] p_collectFiles_3_) {
        if (p_collectFiles_0_ instanceof DefaultResourcePack) {
            return collectFilesFixed(p_collectFiles_0_, p_collectFiles_3_);
        }
        if (!(p_collectFiles_0_ instanceof AbstractResourcePack)) {
            return new String[0];
        }
        final AbstractResourcePack abstractresourcepack = (AbstractResourcePack)p_collectFiles_0_;
        final File file1 = abstractresourcepack.resourcePackFile;
        return (file1 == null) ? new String[0] : (file1.isDirectory() ? collectFilesFolder(file1, "", p_collectFiles_1_, p_collectFiles_2_) : (file1.isFile() ? collectFilesZIP(file1, p_collectFiles_1_, p_collectFiles_2_) : new String[0]));
    }
    
    private static String[] collectFilesFixed(final IResourcePack p_collectFilesFixed_0_, final String[] p_collectFilesFixed_1_) {
        if (p_collectFilesFixed_1_ == null) {
            return new String[0];
        }
        final List list = new ArrayList();
        for (int i = 0; i < p_collectFilesFixed_1_.length; ++i) {
            final String s = p_collectFilesFixed_1_[i];
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            if (p_collectFilesFixed_0_.resourceExists(resourcelocation)) {
                list.add(s);
            }
        }
        final String[] astring = list.toArray(new String[list.size()]);
        return astring;
    }
    
    private static String[] collectFilesFolder(final File p_collectFilesFolder_0_, final String p_collectFilesFolder_1_, final String[] p_collectFilesFolder_2_, final String[] p_collectFilesFolder_3_) {
        final List list = new ArrayList();
        final String s = "assets/minecraft/";
        final File[] afile = p_collectFilesFolder_0_.listFiles();
        if (afile == null) {
            return new String[0];
        }
        for (int i = 0; i < afile.length; ++i) {
            final File file1 = afile[i];
            if (file1.isFile()) {
                String s2 = String.valueOf(p_collectFilesFolder_1_) + file1.getName();
                if (s2.startsWith(s)) {
                    s2 = s2.substring(s.length());
                    if (StrUtils.startsWith(s2, p_collectFilesFolder_2_) && StrUtils.endsWith(s2, p_collectFilesFolder_3_)) {
                        list.add(s2);
                    }
                }
            }
            else if (file1.isDirectory()) {
                final String s3 = String.valueOf(p_collectFilesFolder_1_) + file1.getName() + "/";
                final String[] astring = collectFilesFolder(file1, s3, p_collectFilesFolder_2_, p_collectFilesFolder_3_);
                for (int j = 0; j < astring.length; ++j) {
                    final String s4 = astring[j];
                    list.add(s4);
                }
            }
        }
        final String[] astring2 = list.toArray(new String[list.size()]);
        return astring2;
    }
    
    private static String[] collectFilesZIP(final File p_collectFilesZIP_0_, final String[] p_collectFilesZIP_1_, final String[] p_collectFilesZIP_2_) {
        final List list = new ArrayList();
        final String s = "assets/minecraft/";
        try {
            final ZipFile zipfile = new ZipFile(p_collectFilesZIP_0_);
            final Enumeration enumeration = zipfile.entries();
            while (enumeration.hasMoreElements()) {
                final ZipEntry zipentry = enumeration.nextElement();
                String s2 = zipentry.getName();
                if (s2.startsWith(s)) {
                    s2 = s2.substring(s.length());
                    if (!StrUtils.startsWith(s2, p_collectFilesZIP_1_) || !StrUtils.endsWith(s2, p_collectFilesZIP_2_)) {
                        continue;
                    }
                    list.add(s2);
                }
            }
            zipfile.close();
            final String[] astring = list.toArray(new String[list.size()]);
            return astring;
        }
        catch (IOException ioexception) {
            ioexception.printStackTrace();
            return new String[0];
        }
    }
}
