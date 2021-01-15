package net.minecraft.client.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.InsecureTextureException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class SkinManager
{
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1L, TimeUnit.MINUTES, new LinkedBlockingQueue());
    private final TextureManager textureManager;
    private final File skinCacheDir;
    private final MinecraftSessionService sessionService;
    private final LoadingCache skinCacheLoader;

    public SkinManager(TextureManager textureManagerInstance, File skinCacheDirectory, MinecraftSessionService sessionService)
    {
        this.textureManager = textureManagerInstance;
        this.skinCacheDir = skinCacheDirectory;
        this.sessionService = sessionService;
        this.skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).build(new CacheLoader()
        {
            public Map func_152786_a(GameProfile profile)
            {
                return Minecraft.getMinecraft().getSessionService().getTextures(profile, false);
            }
            @Override
			public Object load(Object p_load_1_)
            {
                return this.func_152786_a((GameProfile)p_load_1_);
            }
        });
    }

    /**
     * Used in the Skull renderer to fetch a skin. May download the skin if it's not in the cache
     */
    public ResourceLocation loadSkin(MinecraftProfileTexture profileTexture, Type p_152792_2_)
    {
        return this.loadSkin(profileTexture, p_152792_2_, (SkinManager.SkinAvailableCallback)null);
    }

    /**
     * May download the skin if its not in the cache, can be passed a SkinManager#SkinAvailableCallback for handling
     */
    public ResourceLocation loadSkin(final MinecraftProfileTexture profileTexture, final Type p_152789_2_, final SkinManager.SkinAvailableCallback skinAvailableCallback)
    {
        final ResourceLocation var4 = new ResourceLocation("skins/" + profileTexture.getHash());
        ITextureObject var5 = this.textureManager.getTexture(var4);

        if (var5 != null)
        {
            if (skinAvailableCallback != null)
            {
                skinAvailableCallback.skinAvailable(p_152789_2_, var4, profileTexture);
            }
        }
        else
        {
            File var6 = new File(this.skinCacheDir, profileTexture.getHash().substring(0, 2));
            File var7 = new File(var6, profileTexture.getHash());
            final ImageBufferDownload var8 = p_152789_2_ == Type.SKIN ? new ImageBufferDownload() : null;
            ThreadDownloadImageData var9 = new ThreadDownloadImageData(var7, profileTexture.getUrl(), DefaultPlayerSkin.getDefaultSkinLegacy(), new IImageBuffer()
            {
                @Override
				public BufferedImage parseUserSkin(BufferedImage image)
                {
                    if (var8 != null)
                    {
                        image = var8.parseUserSkin(image);
                    }

                    return image;
                }
                @Override
				public void skinAvailable()
                {
                    if (var8 != null)
                    {
                        var8.skinAvailable();
                    }

                    if (skinAvailableCallback != null)
                    {
                        skinAvailableCallback.skinAvailable(p_152789_2_, var4, profileTexture);
                    }
                }
            });
            this.textureManager.loadTexture(var4, var9);
        }

        return var4;
    }

    public void loadProfileTextures(final GameProfile profile, final SkinManager.SkinAvailableCallback skinAvailableCallback, final boolean p_152790_3_)
    {
        THREAD_POOL.submit(new Runnable()
        {
            @Override
			public void run()
            {
                final HashMap var1 = Maps.newHashMap();

                try
                {
                    var1.putAll(SkinManager.this.sessionService.getTextures(profile, p_152790_3_));
                }
                catch (InsecureTextureException var3)
                {
                    ;
                }

                if (var1.isEmpty() && profile.getId().equals(Minecraft.getMinecraft().getSession().getProfile().getId()))
                {
                    var1.putAll(SkinManager.this.sessionService.getTextures(SkinManager.this.sessionService.fillProfileProperties(profile, false), false));
                }

                Minecraft.getMinecraft().addScheduledTask(new Runnable()
                {
                    @Override
					public void run()
                    {
                        if (var1.containsKey(Type.SKIN))
                        {
                            SkinManager.this.loadSkin((MinecraftProfileTexture)var1.get(Type.SKIN), Type.SKIN, skinAvailableCallback);
                        }

                        if (var1.containsKey(Type.CAPE))
                        {
                            SkinManager.this.loadSkin((MinecraftProfileTexture)var1.get(Type.CAPE), Type.CAPE, skinAvailableCallback);
                        }
                    }
                });
            }
        });
    }

    public Map loadSkinFromCache(GameProfile profile)
    {
        return (Map)this.skinCacheLoader.getUnchecked(profile);
    }

    public interface SkinAvailableCallback
    {
        void skinAvailable(Type var1, ResourceLocation var2, MinecraftProfileTexture var3);
    }
}
