package shadersmod.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import optifine.StrUtils;

public class ShaderPackFolder implements IShaderPack
{
    protected File packFile;

    public ShaderPackFolder(String name, File file)
    {
        this.packFile = file;
    }

    public void close() {}

    public InputStream getResourceAsStream(String resName)
    {
        try
        {
            String excp = StrUtils.removePrefixSuffix(resName, "/", "/");
            File resFile = new File(this.packFile, excp);
            return !resFile.exists() ? null : new BufferedInputStream(new FileInputStream(resFile));
        }
        catch (Exception var4)
        {
            return null;
        }
    }

    public boolean hasDirectory(String name)
    {
        File resFile = new File(this.packFile, name.substring(1));
        return !resFile.exists() ? false : resFile.isDirectory();
    }

    public String getName()
    {
        return this.packFile.getName();
    }
}
