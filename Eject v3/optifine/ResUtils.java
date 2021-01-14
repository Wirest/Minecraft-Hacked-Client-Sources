package optifine;

import net.minecraft.client.resources.AbstractResourcePack;
import net.minecraft.client.resources.DefaultResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ResUtils {
    public static String[] collectFiles(String paramString1, String paramString2) {
        return collectFiles(new String[]{paramString1}, new String[]{paramString2});
    }

    public static String[] collectFiles(String[] paramArrayOfString1, String[] paramArrayOfString2) {
        LinkedHashSet localLinkedHashSet = new LinkedHashSet();
        IResourcePack[] arrayOfIResourcePack = Config.getResourcePacks();
        for (int i = 0; i < arrayOfIResourcePack.length; i++) {
            IResourcePack localIResourcePack = arrayOfIResourcePack[i];
            String[] arrayOfString2 = collectFiles(localIResourcePack, (String[]) paramArrayOfString1, (String[]) paramArrayOfString2, (String[]) null);
            localLinkedHashSet.addAll(Arrays.asList(arrayOfString2));
        }
        String[] arrayOfString1 = (String[]) localLinkedHashSet.toArray(new String[localLinkedHashSet.size()]);
        return arrayOfString1;
    }

    public static String[] collectFiles(IResourcePack paramIResourcePack, String paramString1, String paramString2, String[] paramArrayOfString) {
        return collectFiles(paramIResourcePack, new String[]{paramString1}, new String[]{paramString2}, paramArrayOfString);
    }

    public static String[] collectFiles(IResourcePack paramIResourcePack, String[] paramArrayOfString1, String[] paramArrayOfString2) {
        return collectFiles(paramIResourcePack, (String[]) paramArrayOfString1, (String[]) paramArrayOfString2, (String[]) null);
    }

    public static String[] collectFiles(IResourcePack paramIResourcePack, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3) {
        if ((paramIResourcePack instanceof DefaultResourcePack)) {
            return collectFilesFixed(paramIResourcePack, paramArrayOfString3);
        }
        if (!(paramIResourcePack instanceof AbstractResourcePack)) {
            return new String[0];
        }
        AbstractResourcePack localAbstractResourcePack = (AbstractResourcePack) paramIResourcePack;
        File localFile = localAbstractResourcePack.resourcePackFile;
        return localFile.isFile() ? collectFilesZIP(localFile, paramArrayOfString1, paramArrayOfString2) : localFile.isDirectory() ? collectFilesFolder(localFile, "", paramArrayOfString1, paramArrayOfString2) : localFile == null ? new String[0] : new String[0];
    }

    private static String[] collectFilesFixed(IResourcePack paramIResourcePack, String[] paramArrayOfString) {
        if (paramArrayOfString == null) {
            return new String[0];
        }
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayOfString.length; i++) {
            String str = paramArrayOfString[i];
            ResourceLocation localResourceLocation = new ResourceLocation(str);
            if (paramIResourcePack.resourceExists(localResourceLocation)) {
                localArrayList.add(str);
            }
        }
        String[] arrayOfString = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        return arrayOfString;
    }

    private static String[] collectFilesFolder(File paramFile, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) {
        ArrayList localArrayList = new ArrayList();
        String str1 = "assets/minecraft/";
        File[] arrayOfFile = paramFile.listFiles();
        if (arrayOfFile == null) {
            return new String[0];
        }
        for (int i = 0; i < arrayOfFile.length; i++) {
            File localFile = arrayOfFile[i];
            String str2;
            if (localFile.isFile()) {
                str2 = paramString + localFile.getName();
                if (str2.startsWith(str1)) {
                    str2 = str2.substring(str1.length());
                    if ((StrUtils.startsWith(str2, paramArrayOfString1)) && (StrUtils.endsWith(str2, paramArrayOfString2))) {
                        localArrayList.add(str2);
                    }
                }
            } else if (localFile.isDirectory()) {
                str2 = paramString + localFile.getName() + "/";
                String[] arrayOfString2 = collectFilesFolder(localFile, str2, paramArrayOfString1, paramArrayOfString2);
                for (int j = 0; j < arrayOfString2.length; j++) {
                    String str3 = arrayOfString2[j];
                    localArrayList.add(str3);
                }
            }
        }
        String[] arrayOfString1 = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
        return arrayOfString1;
    }

    private static String[] collectFilesZIP(File paramFile, String[] paramArrayOfString1, String[] paramArrayOfString2) {
        ArrayList localArrayList = new ArrayList();
        String str1 = "assets/minecraft/";
        try {
            ZipFile localZipFile = new ZipFile(paramFile);
            Enumeration localEnumeration = localZipFile.entries();
            while (localEnumeration.hasMoreElements()) {
                localObject = (ZipEntry) localEnumeration.nextElement();
                String str2 = ((ZipEntry) localObject).getName();
                if (str2.startsWith(str1)) {
                    str2 = str2.substring(str1.length());
                    if ((StrUtils.startsWith(str2, paramArrayOfString1)) && (StrUtils.endsWith(str2, paramArrayOfString2))) {
                        localArrayList.add(str2);
                    }
                }
            }
            localZipFile.close();
            Object localObject = (String[]) (String[]) localArrayList.toArray(new String[localArrayList.size()]);
            return (String[]) localObject;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return new String[0];
    }
}




