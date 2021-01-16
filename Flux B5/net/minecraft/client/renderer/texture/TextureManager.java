package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optifine.Config;
import optifine.RandomMobs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger logger = LogManager.getLogger();
    private final Map mapTextureObjects = Maps.newHashMap();
    private final List listTickables = Lists.newArrayList();
    private final Map mapTextureCounters = Maps.newHashMap();
    private IResourceManager theResourceManager;
    private static final String __OBFID = "CL_00001064";

    public TextureManager(IResourceManager p_i1284_1_)
    {
        this.theResourceManager = p_i1284_1_;
    }

    public void bindTexture(ResourceLocation resource)
    {
        if (Config.isRandomMobs())
        {
            resource = RandomMobs.getTextureLocation(resource);
        }

        Object var2 = (ITextureObject)this.mapTextureObjects.get(resource);

        if (var2 == null)
        {
            var2 = new SimpleTexture(resource);
            this.loadTexture(resource, (ITextureObject)var2);
        }

        if (Config.isShaders())
        {
            ShadersTex.bindTexture((ITextureObject)var2);
        }
        else
        {
            TextureUtil.bindTexture(((ITextureObject)var2).getGlTextureId());
        }
    }

    public boolean loadTickableTexture(ResourceLocation p_110580_1_, ITickableTextureObject p_110580_2_)
    {
        if (this.loadTexture(p_110580_1_, p_110580_2_))
        {
            this.listTickables.add(p_110580_2_);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean loadTexture(ResourceLocation p_110579_1_, final ITextureObject p_110579_2_)
    {
        boolean var3 = true;
        Object p_110579_2_2 = p_110579_2_;

        try
        {
            p_110579_2_.loadTexture(this.theResourceManager);
        }
        catch (IOException var8)
        {
            logger.warn("Failed to load texture: " + p_110579_1_, var8);
            p_110579_2_2 = TextureUtil.missingTexture;
            this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
            var3 = false;
        }
        catch (Throwable var9)
        {
            CrashReport var5 = CrashReport.makeCrashReport(var9, "Registering texture");
            CrashReportCategory var6 = var5.makeCategory("Resource location being registered");
            var6.addCrashSection("Resource location", p_110579_1_);
            var6.addCrashSectionCallable("Texture object class", new Callable()
            {
                private static final String __OBFID = "CL_00001065";
                public String call()
                {
                    return p_110579_2_.getClass().getName();
                }
            });
            throw new ReportedException(var5);
        }

        this.mapTextureObjects.put(p_110579_1_, p_110579_2_2);
        return var3;
    }

    public ITextureObject getTexture(ResourceLocation p_110581_1_)
    {
        return (ITextureObject)this.mapTextureObjects.get(p_110581_1_);
    }

    public ResourceLocation getDynamicTextureLocation(String p_110578_1_, DynamicTexture p_110578_2_)
    {
        if (p_110578_1_.equals("logo"))
        {
            p_110578_2_ = Config.getMojangLogoTexture(p_110578_2_);
        }

        Integer var3 = (Integer)this.mapTextureCounters.get(p_110578_1_);

        if (var3 == null)
        {
            var3 = Integer.valueOf(1);
        }
        else
        {
            var3 = Integer.valueOf(var3.intValue() + 1);
        }

        this.mapTextureCounters.put(p_110578_1_, var3);
        ResourceLocation var4 = new ResourceLocation(String.format("dynamic/%s_%d", new Object[] {p_110578_1_, var3}));
        this.loadTexture(var4, p_110578_2_);
        return var4;
    }

    public void tick()
    {
        Iterator var1 = this.listTickables.iterator();

        while (var1.hasNext())
        {
            ITickable var2 = (ITickable)var1.next();
            var2.tick();
        }
    }

    public void deleteTexture(ResourceLocation p_147645_1_)
    {
        ITextureObject var2 = this.getTexture(p_147645_1_);

        if (var2 != null)
        {
            TextureUtil.deleteTexture(var2.getGlTextureId());
        }
    }

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

                if (var3 instanceof AbstractTexture)
                {
                    AbstractTexture at = (AbstractTexture)var3;
                    at.deleteGlTexture();
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
