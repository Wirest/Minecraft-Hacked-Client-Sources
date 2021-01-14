package optifine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import optifine.xdelta.Delta;
import optifine.xdelta.DeltaException;
import optifine.xdelta.DiffWriter;
import optifine.xdelta.GDiffWriter;

public class Differ
{
    public static void main(String[] args) throws Exception
    {
        if (args.length < 3)
        {
            Utils.dbg("Usage: Differ <base.jar> <mod.jar> <diff.jar>");
        }
        else
        {
            File file1 = new File(args[0]);
            File file2 = new File(args[1]);
            File file3 = new File(args[2]);

            if (file1.getName().equals("AUTO"))
            {
                file1 = detectBaseFile(file2);
            }

            if (file1.exists() && file1.isFile())
            {
                if (file2.exists() && file2.isFile())
                {
                    process(file1, file2, file3);
                }
                else
                {
                    throw new IOException("Mod file not found: " + file2);
                }
            }
            else
            {
                throw new IOException("Base file not found: " + file1);
            }
        }
    }

    private static void process(File baseFile, File modFile, File diffFile) throws IOException, DeltaException, NoSuchAlgorithmException
    {
        ZipFile zipfile = new ZipFile(modFile);
        Map<String, String> map = Patcher.getConfigurationMap(zipfile);
        Pattern[] apattern = Patcher.getConfigurationPatterns(map);
        ZipOutputStream zipoutputstream = new ZipOutputStream(new FileOutputStream(diffFile));
        ZipFile zipfile1 = new ZipFile(baseFile);
        Enumeration enumeration = zipfile.entries();

        while (enumeration.hasMoreElements())
        {
            ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
            InputStream inputstream = zipfile.getInputStream(zipentry);
            byte[] abyte = Utils.readAll(inputstream);
            String s = zipentry.getName();
            byte[] abyte1 = makeDiff(s, abyte, apattern, map, zipfile1);

            if (abyte1 != abyte)
            {
                ZipEntry zipentry1 = new ZipEntry("patch/" + s + ".xdelta");
                zipoutputstream.putNextEntry(zipentry1);
                zipoutputstream.write(abyte1);
                zipoutputstream.closeEntry();
                Utils.dbg("Delta: " + zipentry1.getName());
                byte[] abyte2 = HashUtils.getHashMd5(abyte);
                String s1 = HashUtils.toHexString(abyte2);
                byte[] abyte3 = s1.getBytes("ASCII");
                ZipEntry zipentry2 = new ZipEntry("patch/" + s + ".md5");
                zipoutputstream.putNextEntry(zipentry2);
                zipoutputstream.write(abyte3);
                zipoutputstream.closeEntry();
            }
            else
            {
                ZipEntry zipentry3 = new ZipEntry(s);
                zipoutputstream.putNextEntry(zipentry3);
                zipoutputstream.write(abyte);
                zipoutputstream.closeEntry();
                Utils.dbg("Same: " + zipentry3.getName());
            }
        }

        zipoutputstream.close();
    }

    public static byte[] makeDiff(String name, byte[] bytesMod, Pattern[] patterns, Map<String, String> cfgMap, ZipFile zipBase) throws IOException, DeltaException
    {
        String s = Patcher.getPatchBase(name, patterns, cfgMap);

        if (s == null)
        {
            return bytesMod;
        }
        else
        {
            ZipEntry zipentry = zipBase.getEntry(s);

            if (zipentry == null)
            {
                throw new IOException("Base entry not found: " + s + " in: " + zipBase.getName());
            }
            else
            {
                InputStream inputstream = zipBase.getInputStream(zipentry);
                byte[] abyte = Utils.readAll(inputstream);
                ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(bytesMod);
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                DiffWriter diffwriter = new GDiffWriter(new DataOutputStream(bytearrayoutputstream));
                Delta.computeDelta((byte[])abyte, bytearrayinputstream, bytesMod.length, diffwriter);
                diffwriter.close();
                return bytearrayoutputstream.toByteArray();
            }
        }
    }

    public static File detectBaseFile(File modFile) throws IOException
    {
        ZipFile zipfile = new ZipFile(modFile);
        String s = Installer.getOptiFineVersion(zipfile);

        if (s == null)
        {
            throw new IOException("Version not found");
        }
        else
        {
            zipfile.close();
            String s1 = Installer.getMinecraftVersionFromOfVersion(s);

            if (s1 == null)
            {
                throw new IOException("Version not found");
            }
            else
            {
                File file1 = Utils.getWorkingDirectory();
                File file2 = new File(file1, "versions/" + s1 + "/" + s1 + ".jar");
                return file2;
            }
        }
    }
}
