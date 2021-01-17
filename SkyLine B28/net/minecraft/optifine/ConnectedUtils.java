package net.minecraft.optifine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

public class ConnectedUtils
{
    public static String[] collectFiles(IResourcePack rp, String prefix, String suffix, String[] defaultPaths)
    {
        if (rp instanceof DefaultResourcePack)
        {
            return collectFilesFixed(rp, defaultPaths);
        }
        else if (!(rp instanceof AbstractResourcePack))
        {
            return new String[0];
        }
        else
        {
            AbstractResourcePack arp = (AbstractResourcePack)rp;
            File tpFile = ResourceUtils.getResourcePackFile(arp);
            return tpFile == null ? new String[0] : (tpFile.isDirectory() ? collectFilesFolder(tpFile, "", prefix, suffix) : (tpFile.isFile() ? collectFilesZIP(tpFile, prefix, suffix) : new String[0]));
        }
    }

    private static String[] collectFilesFixed(IResourcePack rp, String[] paths)
    {
        if (paths == null)
        {
            return new String[0];
        }
        else
        {
            ArrayList list = new ArrayList();

            for (int pathArr = 0; pathArr < paths.length; ++pathArr)
            {
                String path = paths[pathArr];
                ResourceLocation loc = new ResourceLocation(path);

                if (rp.resourceExists(loc))
                {
                    list.add(path);
                }
            }

            String[] var6 = (String[])((String[])list.toArray(new String[list.size()]));
            return var6;
        }
    }

    private static String[] collectFilesFolder(File tpFile, String basePath, String prefix, String suffix)
    {
        ArrayList list = new ArrayList();
        String prefixAssets = "assets/minecraft/";
        File[] files = tpFile.listFiles();

        if (files == null)
        {
            return new String[0];
        }
        else
        {
            for (int names = 0; names < files.length; ++names)
            {
                File file = files[names];
                String dirPath;

                if (file.isFile())
                {
                    dirPath = basePath + file.getName();

                    if (dirPath.startsWith(prefixAssets))
                    {
                        dirPath = dirPath.substring(prefixAssets.length());

                        if (dirPath.startsWith(prefix) && dirPath.endsWith(suffix))
                        {
                            list.add(dirPath);
                        }
                    }
                }
                else if (file.isDirectory())
                {
                    dirPath = basePath + file.getName() + "/";
                    String[] names1 = collectFilesFolder(file, dirPath, prefix, suffix);

                    for (int n = 0; n < names1.length; ++n)
                    {
                        String name = names1[n];
                        list.add(name);
                    }
                }
            }

            String[] var13 = (String[])((String[])list.toArray(new String[list.size()]));
            return var13;
        }
    }

    private static String[] collectFilesZIP(File tpFile, String prefix, String suffix)
    {
        ArrayList list = new ArrayList();
        String prefixAssets = "assets/minecraft/";

        try
        {
            ZipFile e = new ZipFile(tpFile);
            Enumeration en = e.entries();

            while (en.hasMoreElements())
            {
                ZipEntry names = (ZipEntry)en.nextElement();
                String name = names.getName();

                if (name.startsWith(prefixAssets))
                {
                    name = name.substring(prefixAssets.length());

                    if (name.startsWith(prefix) && name.endsWith(suffix))
                    {
                        list.add(name);
                    }
                }
            }

            e.close();
            String[] names1 = (String[])((String[])list.toArray(new String[list.size()]));
            return names1;
        }
        catch (IOException var9)
        {
            var9.printStackTrace();
            return new String[0];
        }
    }

    public static int getAverage(int[] vals)
    {
        if (vals.length <= 0)
        {
            return 0;
        }
        else
        {
            int sum = 0;
            int avg;

            for (avg = 0; avg < vals.length; ++avg)
            {
                int val = vals[avg];
                sum += val;
            }

            avg = sum / vals.length;
            return avg;
        }
    }
}
