package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.util.ResourceLocation;

public class DefaultResourcePack implements IResourcePack
{
    public static final Set defaultResourceDomains = ImmutableSet.of("minecraft", "realms");
    private final Map mapAssets;

    public DefaultResourcePack(Map mapAssetsIn)
    {
        this.mapAssets = mapAssetsIn;
    }

    @Override
	public InputStream getInputStream(ResourceLocation location) throws IOException
    {
        InputStream var2 = this.getResourceStream(location);

        if (var2 != null)
        {
            return var2;
        }
        else
        {
            InputStream var3 = this.getInputStreamAssets(location);

            if (var3 != null)
            {
                return var3;
            }
            else
            {
                throw new FileNotFoundException(location.getResourcePath());
            }
        }
    }

    @SuppressWarnings("resource")
	public InputStream getInputStreamAssets(ResourceLocation location) throws IOException
    {
        File var2 = (File)this.mapAssets.get(location.toString());
        return var2 != null && var2.isFile() ? new FileInputStream(var2) : null;
    }

    private InputStream getResourceStream(ResourceLocation location)
    {
        return DefaultResourcePack.class.getResourceAsStream("/assets/" + location.getResourceDomain() + "/" + location.getResourcePath());
    }

    @Override
	public boolean resourceExists(ResourceLocation location)
    {
        return this.getResourceStream(location) != null || this.mapAssets.containsKey(location.toString());
    }

    @Override
	public Set getResourceDomains()
    {
        return defaultResourceDomains;
    }

    @Override
	public IMetadataSection getPackMetadata(IMetadataSerializer p_135058_1_, String p_135058_2_) throws IOException
    {
        try
        {
            FileInputStream var3 = new FileInputStream((File)this.mapAssets.get("pack.mcmeta"));
            return AbstractResourcePack.readMetadata(p_135058_1_, var3, p_135058_2_);
        }
        catch (RuntimeException var4)
        {
            return null;
        }
        catch (FileNotFoundException var5)
        {
            return null;
        }
    }

    @Override
	public BufferedImage getPackImage() throws IOException
    {
        return TextureUtil.readBufferedImage(DefaultResourcePack.class.getResourceAsStream("/" + (new ResourceLocation("pack.png")).getResourcePath()));
    }

    @Override
	public String getPackName()
    {
        return "Default";
    }
}
