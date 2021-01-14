package shadersmod.client;

import java.io.InputStream;

public abstract interface IShaderPack {
    public abstract String getName();

    public abstract InputStream getResourceAsStream(String paramString);

    public abstract boolean hasDirectory(String paramString);

    public abstract void close();
}




