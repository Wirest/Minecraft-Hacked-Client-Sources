package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.RandomMobs;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private final Map mapTextureObjects = Maps.newHashMap();
    private final List listTickables = Lists.newArrayList();
    private final Map mapTextureCounters = Maps.newHashMap();
    private IResourceManager theResourceManager;

    public TextureManager(IResourceManager resourceManager)
    {
        this.theResourceManager = resourceManager;
    }

    public void bindTexture(ResourceLocation resource)
    {
        if (Config.isRandomMobs())
        {
            resource = RandomMobs.getTextureLocation(resource);
        }

        Object var2 = this.mapTextureObjects.get(resource);

        if (var2 == null)
        {
            var2 = new SimpleTexture(resource);
            this.loadTexture(resource, (ITextureObject)var2);
        }

        TextureUtil.bindTexture(((ITextureObject)var2).getGlTextureId());
    }

    public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj)
    {
        if (this.loadTexture(textureLocation, textureObj))
        {
            this.listTickables.add(textureObj);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean loadTexture(ResourceLocation textureLocation, final ITextureObject textureObj)
    {
        boolean var3 = true;
        Object textureObj2 = textureObj;

        try
        {
            textureObj.loadTexture(this.theResourceManager);
        }
        catch (IOException var8)
        {
            logger.warn("Failed to load texture: " + textureLocation, var8);
            textureObj2 = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, textureObj2);
            var3 = false;
        }
        catch (Throwable var9)
        {
            CrashReport var5 = CrashReport.makeCrashReport(var9, "Registering texture");
            CrashReportCategory var6 = var5.makeCategory("Resource location being registered");
            var6.addCrashSection("Resource location", textureLocation);
            var6.addCrashSectionCallable("Texture object class", new Callable()
            {
                @Override
				public String call()
                {
                    return textureObj.getClass().getName();
                }
            });
            throw new ReportedException(var5);
        }

        this.mapTextureObjects.put(textureLocation, textureObj2);
        return var3;
    }

    public ITextureObject getTexture(ResourceLocation textureLocation)
    {
        return (ITextureObject)this.mapTextureObjects.get(textureLocation);
    }

    public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture)
    {
        Integer var3 = (Integer)this.mapTextureCounters.get(name);

        if (var3 == null)
        {
            var3 = Integer.valueOf(1);
        }
        else
        {
            var3 = Integer.valueOf(var3.intValue() + 1);
        }

        this.mapTextureCounters.put(name, var3);
        ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] {name, var3}));
        this.loadTexture(var4, texture);
        return var4;
    }

    @Override
	public void tick()
    {
        Iterator var1 = this.listTickables.iterator();

        while (var1.hasNext())
        {
            ITickable var2 = (ITickable)var1.next();
            var2.tick();
        }
    }

    public void deleteTexture(ResourceLocation textureLocation)
    {
        ITextureObject var2 = this.getTexture(textureLocation);

        if (var2 != null)
        {
            TextureUtil.deleteTexture(var2.getGlTextureId());
        }
    }

    @Override
	public void onResourceManagerReload(IResourceManager resourceManager)
    {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        Iterator it = this.mapTextureObjects.keySet().iterator();

        while (it.hasNext())
        {
            ResourceLocation var2 = (ResourceLocation)it.next();

            if (var2.getResourcePath().startsWith("mcpatcher/"))
            {
                ITextureObject var3 = (ITextureObject)this.mapTextureObjects.get(var2);
                int glTexId = var3.getGlTextureId();

                if (glTexId > 0)
                {
                    GL11.glDeleteTextures(glTexId);
                }

                it.remove();
            }
        }

        Iterator var21 = this.mapTextureObjects.entrySet().iterator();

        while (var21.hasNext())
        {
            Entry var31 = (Entry)var21.next();
            this.loadTexture((ResourceLocation)var31.getKey(), (ITextureObject)var31.getValue());
        }
    }
}
