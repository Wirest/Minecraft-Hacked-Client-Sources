package shadersmod.client;

import java.io.InputStream;

public class ShaderPackNone implements IShaderPack
{
    @Override
	public void close()
    {
    }

    @Override
	public InputStream getResourceAsStream(String resName)
    {
        return null;
    }

    @Override
	public boolean hasDirectory(String name)
    {
        return false;
    }

    @Override
	public String getName()
    {
        return Shaders.packNameNone;
    }
}
