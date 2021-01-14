package optifine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import net.minecraft.launchwrapper.IClassTransformer;

public class OptiFineClassTransformer implements IClassTransformer, IResourceProvider
{
    private ZipFile ofZipFile = null;
    private Map<String, String> patchMap = null;
    private Pattern[] patterns = null;
    public static OptiFineClassTransformer instance = null;

    public OptiFineClassTransformer()
    {
        instance = this;

        try
        {
            dbg("OptiFine ClassTransformer");
            URL url = OptiFineClassTransformer.class.getProtectionDomain().getCodeSource().getLocation();
            URI uri = url.toURI();
            File file1 = new File(uri);
            this.ofZipFile = new ZipFile(file1);
            dbg("OptiFine ZIP file: " + file1);
            this.patchMap = Patcher.getConfigurationMap(this.ofZipFile);
            this.patterns = Patcher.getConfigurationPatterns(this.patchMap);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if (this.ofZipFile == null)
        {
            dbg("*** Can not find the OptiFine JAR in the classpath ***");
            dbg("*** OptiFine will not be loaded! ***");
        }
    }

    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        String s = name + ".class";
        byte[] abyte = this.getOptiFineResource(s);
        return abyte != null ? abyte : bytes;
    }

    public InputStream getResourceStream(String path)
    {
        path = Utils.ensurePrefix(path, "/");
        return OptiFineClassTransformer.class.getResourceAsStream(path);
    }

    public synchronized byte[] getOptiFineResource(String name)
    {
        name = Utils.removePrefix(name, "/");
        byte[] abyte = this.getOptiFineResourceZip(name);

        if (abyte != null)
        {
            return abyte;
        }
        else
        {
            abyte = this.getOptiFineResourcePatched(name, this);
            return abyte != null ? abyte : null;
        }
    }

    public synchronized byte[] getOptiFineResourceZip(String name)
    {
        if (this.ofZipFile == null)
        {
            return null;
        }
        else
        {
            name = Utils.removePrefix(name, "/");
            ZipEntry zipentry = this.ofZipFile.getEntry(name);

            if (zipentry == null)
            {
                return null;
            }
            else
            {
                try
                {
                    InputStream inputstream = this.ofZipFile.getInputStream(zipentry);
                    byte[] abyte = readAll(inputstream);
                    inputstream.close();

                    if ((long)abyte.length != zipentry.getSize())
                    {
                        dbg("Invalid size, name: " + name + ", zip: " + zipentry.getSize() + ", stream: " + abyte.length);
                        return null;
                    }
                    else
                    {
                        return abyte;
                    }
                }
                catch (IOException ioexception)
                {
                    ioexception.printStackTrace();
                    return null;
                }
            }
        }
    }

    public synchronized byte[] getOptiFineResourcePatched(String name, IResourceProvider resourceProvider)
    {
        if (this.patterns != null && this.patchMap != null && resourceProvider != null)
        {
            name = Utils.removePrefix(name, "/");
            String s = "patch/" + name + ".xdelta";
            byte[] abyte = this.getOptiFineResourceZip(s);

            if (abyte == null)
            {
                return null;
            }
            else
            {
                try
                {
                    byte[] abyte1 = Patcher.applyPatch(name, abyte, this.patterns, this.patchMap, resourceProvider);
                    String s1 = "patch/" + name + ".md5";
                    byte[] abyte2 = this.getOptiFineResourceZip(s1);

                    if (abyte2 != null)
                    {
                        String s2 = new String(abyte2, "ASCII");
                        byte[] abyte3 = HashUtils.getHashMd5(abyte1);
                        String s3 = HashUtils.toHexString(abyte3);

                        if (!s2.equals(s3))
                        {
                            throw new IOException("MD5 not matching, name: " + name + ", saved: " + s2 + ", patched: " + s3);
                        }
                    }

                    return abyte1;
                }
                catch (Exception exception)
                {
                    exception.printStackTrace();
                    return null;
                }
            }
        }
        else
        {
            return null;
        }
    }

    public static byte[] readAll(InputStream is) throws IOException
    {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        byte[] abyte = new byte[1024];

        while (true)
        {
            int i = is.read(abyte);

            if (i < 0)
            {
                is.close();
                byte[] abyte1 = bytearrayoutputstream.toByteArray();
                return abyte1;
            }

            bytearrayoutputstream.write(abyte, 0, i);
        }
    }

    private static void dbg(String str)
    {
        System.out.println(str);
    }
}
