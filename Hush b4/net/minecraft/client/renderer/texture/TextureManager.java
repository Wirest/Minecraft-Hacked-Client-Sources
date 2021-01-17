// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import java.util.Iterator;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import java.io.IOException;
import shadersmod.client.ShadersTex;
import optifine.RandomMobs;
import optifine.Config;
import net.minecraft.util.ResourceLocation;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.resources.IResourceManager;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class TextureManager implements ITickable, IResourceManagerReloadListener
{
    private static final Logger logger;
    private final Map mapTextureObjects;
    private final List listTickables;
    private final Map mapTextureCounters;
    private IResourceManager theResourceManager;
    private static final String __OBFID = "CL_00001064";
    
    static {
        logger = LogManager.getLogger();
    }
    
    public TextureManager(final IResourceManager resourceManager) {
        this.mapTextureObjects = Maps.newHashMap();
        this.listTickables = Lists.newArrayList();
        this.mapTextureCounters = Maps.newHashMap();
        this.theResourceManager = resourceManager;
    }
    
    public void bindTexture(ResourceLocation resource) {
        if (Config.isRandomMobs()) {
            resource = RandomMobs.getTextureLocation(resource);
        }
        Object object = this.mapTextureObjects.get(resource);
        if (object == null) {
            object = new SimpleTexture(resource);
            this.loadTexture(resource, (ITextureObject)object);
        }
        if (Config.isShaders()) {
            ShadersTex.bindTexture((ITextureObject)object);
        }
        else {
            TextureUtil.bindTexture(((ITextureObject)object).getGlTextureId());
        }
    }
    
    public boolean loadTickableTexture(final ResourceLocation textureLocation, final ITickableTextureObject textureObj) {
        if (this.loadTexture(textureLocation, textureObj)) {
            this.listTickables.add(textureObj);
            return true;
        }
        return false;
    }
    
    public boolean loadTexture(final ResourceLocation textureLocation, final ITextureObject textureObj) {
        boolean flag = true;
        ITextureObject itextureobject = textureObj;
        try {
            textureObj.loadTexture(this.theResourceManager);
        }
        catch (IOException ioexception) {
            TextureManager.logger.warn("Failed to load texture: " + textureLocation, ioexception);
            itextureobject = TextureUtil.missingTexture;
            this.mapTextureObjects.put(textureLocation, itextureobject);
            flag = false;
        }
        catch (Throwable throwable) {
            final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
            final CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
            crashreportcategory.addCrashSection("Resource location", textureLocation);
            crashreportcategory.addCrashSectionCallable("Texture object class", new Callable() {
                private static final String __OBFID = "CL_00001065";
                
                @Override
                public String call() throws Exception {
                    return textureObj.getClass().getName();
                }
            });
            throw new ReportedException(crashreport);
        }
        this.mapTextureObjects.put(textureLocation, itextureobject);
        return flag;
    }
    
    public ITextureObject getTexture(final ResourceLocation textureLocation) {
        return this.mapTextureObjects.get(textureLocation);
    }
    
    public ResourceLocation getDynamicTextureLocation(final String name, DynamicTexture texture) {
        if (name.equals("logo")) {
            texture = Config.getMojangLogoTexture(texture);
        }
        Integer integer = this.mapTextureCounters.get(name);
        if (integer == null) {
            integer = 1;
        }
        else {
            ++integer;
        }
        this.mapTextureCounters.put(name, integer);
        final ResourceLocation resourcelocation = new ResourceLocation(String.format("dynamic/%s_%d", name, integer));
        this.loadTexture(resourcelocation, texture);
        return resourcelocation;
    }
    
    @Override
    public void tick() {
        for (final Object itickable : this.listTickables) {
            ((ITickable)itickable).tick();
        }
    }
    
    public void deleteTexture(final ResourceLocation textureLocation) {
        final ITextureObject itextureobject = this.getTexture(textureLocation);
        if (itextureobject != null) {
            this.mapTextureObjects.remove(textureLocation);
            TextureUtil.deleteTexture(itextureobject.getGlTextureId());
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        Config.dbg("*** Reloading textures ***");
        Config.log("Resource packs: " + Config.getResourcePackNames());
        final Iterator iterator = this.mapTextureObjects.keySet().iterator();
        while (iterator.hasNext()) {
            final ResourceLocation resourcelocation = iterator.next();
            final String s = resourcelocation.getResourcePath();
            if (s.startsWith("mcpatcher/") || s.startsWith("optifine/")) {
                final ITextureObject itextureobject = this.mapTextureObjects.get(resourcelocation);
                if (itextureobject instanceof AbstractTexture) {
                    final AbstractTexture abstracttexture = (AbstractTexture)itextureobject;
                    abstracttexture.deleteGlTexture();
                }
                iterator.remove();
            }
        }
        for (final Object entry : this.mapTextureObjects.entrySet()) {
            this.loadTexture(((Map.Entry)entry).getKey(), ((Map.Entry)entry).getValue());
        }
    }
    
    public void reloadBannerTextures() {
        for (final Object entry : this.mapTextureObjects.entrySet()) {
            final ResourceLocation resourcelocation = ((Map.Entry)entry).getKey();
            final ITextureObject itextureobject = ((Map.Entry)entry).getValue();
            if (itextureobject instanceof LayeredColorMaskTexture) {
                this.loadTexture(resourcelocation, itextureobject);
            }
        }
    }
}
