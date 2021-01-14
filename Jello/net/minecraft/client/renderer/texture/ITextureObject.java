package net.minecraft.client.renderer.texture;

import java.io.IOException;
import net.minecraft.client.resources.IResourceManager;
import shadersmod.client.MultiTexID;

public interface ITextureObject
{
    void func_174936_b(boolean var1, boolean var2);

    void func_174935_a();

    void loadTexture(IResourceManager var1) throws IOException;

    int getGlTextureId();

    MultiTexID getMultiTexID();
}
