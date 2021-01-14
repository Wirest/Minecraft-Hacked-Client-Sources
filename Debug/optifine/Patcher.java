package optifine;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import optifine.xdelta.GDiffPatcher;
import optifine.xdelta.PatchException;

public class Patcher
{
    public static final String CONFIG_FILE = "patch.cfg";
    public static final String PREFIX_PATCH = "patch/";
    public static final String SUFFIX_DELTA = ".xdelta";
    public static final String SUFFIX_MD5 = ".md5";

    public static void main(String[] args) throws Exception
    {
        if (args.length < 3)
        {
            Utils.dbg("Usage: Patcher <base.jar> <diff.jar> <mod.jar>");
        }
        else
        {
            File file1 = new File(args[0]);
            File file2 = new File(args[1]);
            File file3 = new File(args[2]);

            if (file1.getName().equals("AUTO"))
            {
                file1 = Differ.detectBaseFile(file2);
            }

            if (file1.exists() && file1.isFile())
            {
                if (file2.exists() && file2.isFile())
                {
                    process(file1, file2, file3);
                }
                else
                {
                    throw new IOException("Diff file not found: " + file3);
                }
            }
            else
            {
                throw new IOException("Base file not found: " + file1);
            }
        }
    }

    public static void process(File baseFile, File diffFile, File modFile) throws Exception
    {
        ZipFile zipfile = new ZipFile(diffFile);
        Map<String, String> map = getConfigurationMap(zipfile);
        Pattern[] apattern = getConfigurationPatterns(map);
        ZipOutputStream zipoutputstream = new ZipOutputStream(new FileOutputStream(modFile));
        ZipFile zipfile1 = new ZipFile(baseFile);
        ZipResourceProvider zipresourceprovider = new ZipResourceProvider(zipfile1);
        Enumeration enumeration = zipfile.entries();

        while (enumeration.hasMoreElements())
        {
            ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
            InputStream inputstream = zipfile.getInputStream(zipentry);
            byte[] abyte = Utils.readAll(inputstream);
            String s = zipentry.getName();

            if (s.startsWith("patch/") && s.endsWith(".xdelta"))
            {
                s = s.substring("patch/".length());
                s = s.substring(0, s.length() - ".xdelta".length());
                byte[] abyte3 = applyPatch(s, abyte, apattern, map, zipresourceprovider);
                String s1 = "patch/" + s + ".md5";
                ZipEntry zipentry2 = zipfile.getEntry(s1);

                if (zipentry2 != null)
                {
                    byte[] abyte1 = Utils.readAll(zipfile.getInputStream(zipentry2));
                    String s2 = new String(abyte1, "ASCII");
                    byte[] abyte2 = HashUtils.getHashMd5(abyte3);
                    String s3 = HashUtils.toHexString(abyte2);

                    if (!s2.equals(s3))
                    {
                        throw new Exception("MD5 not matching, name: " + s + ", saved: " + s2 + ", patched: " + s3);
                    }
                }

                ZipEntry zipentry3 = new ZipEntry(s);
                zipoutputstream.putNextEntry(zipentry3);
                zipoutputstream.write(abyte3);
                zipoutputstream.closeEntry();
                Utils.dbg("Mod: " + s);
            }
            else if (!s.startsWith("patch/") || !s.endsWith(".md5"))
            {
                ZipEntry zipentry1 = new ZipEntry(s);
                zipoutputstream.putNextEntry(zipentry1);
                zipoutputstream.write(abyte);
                zipoutputstream.closeEntry();
                Utils.dbg("Same: " + zipentry1.getName());
            }
        }

        zipoutputstream.close();
    }

    public static byte[] applyPatch(String name, byte[] bytesDiff, Pattern[] patterns, Map<String, String> cfgMap, IResourceProvider resourceProvider) throws IOException, PatchException
    {
        name = Utils.removePrefix(name, "/");
        String s = getPatchBase(name, patterns, cfgMap);

        if (s == null)
        {
            throw new IOException("No patch base, name: " + name + ", patterns: " + Utils.arrayToCommaSeparatedString(patterns));
        }
        else
        {
            InputStream inputstream = resourceProvider.getResourceStream(s);

            if (inputstream == null)
            {
                throw new IOException("Base resource not found: " + s);
            }
            else
            {
                byte[] abyte = Utils.readAll(inputstream);
                InputStream inputstream1 = new ByteArrayInputStream(bytesDiff);
                ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
                new GDiffPatcher(abyte, inputstream1, bytearrayoutputstream);
                bytearrayoutputstream.close();
                return bytearrayoutputstream.toByteArray();
            }
        }
    }

    public static Pattern[] getConfigurationPatterns(Map<String, String> cfgMap)
    {
        String[] astring = (String[])cfgMap.keySet().toArray(new String[0]);
        Pattern[] apattern = new Pattern[astring.length];

        for (int i = 0; i < astring.length; ++i)
        {
            String s = astring[i];
            apattern[i] = Pattern.compile(s);
        }

        return apattern;
    }

    public static Map<String, String> getConfigurationMap(ZipFile modZip) throws IOException
    {
        Map<String, String> map = new LinkedHashMap();

        if (modZip == null)
        {
            return map;
        }
        else
        {
            ZipEntry zipentry = modZip.getEntry("patch.cfg");

            if (zipentry == null)
            {
                return map;
            }
            else
            {
                InputStream inputstream = modZip.getInputStream(zipentry);
                String[] astring = Utils.readLines(inputstream, "ASCII");
                inputstream.close();

                for (int i = 0; i < astring.length; ++i)
                {
                    String s = astring[i].trim();

                    if (!s.startsWith("#") && s.length() > 0)
                    {
                        String[] astring1 = Utils.tokenize(s, "=");

                        if (astring1.length != 2)
                        {
                            throw new IOException("Invalid patch configuration: " + s);
                        }

                        String s1 = astring1[0].trim();
                        String s2 = astring1[1].trim();
                        map.put(s1, s2);
                    }
                }

                return map;
            }
        }
    }

    public static String getPatchBase(String name, Pattern[] patterns, Map<String, String> cfgMap)
    {
        name = Utils.removePrefix(name, "/");

        for (int i = 0; i < patterns.length; ++i)
        {
            Pattern pattern = patterns[i];
            Matcher matcher = pattern.matcher(name);

            if (matcher.matches())
            {
                String s = (String)cfgMap.get(pattern.pattern());

                if (s != null && s.trim().equals("*"))
                {
                    return name;
                }

                return s;
            }
        }

        return null;
    }
}
