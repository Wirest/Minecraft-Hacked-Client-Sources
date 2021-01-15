package net.minecraft.client.renderer.texture;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.ConnectedTextures;
import net.minecraft.src.Reflector;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    private final List listAnimatedSprites;
    private final Map mapRegisteredSprites;
    private final Map mapUploadedSprites;
    private final String basePath;
    private final IIconCreator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;

    public TextureMap(String p_i46099_1_)
    {
        this(p_i46099_1_, (IIconCreator)null);
    }

    public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn)
    {
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i46100_1_;
        this.iconCreator = iconCreatorIn;
    }

    private void initMissingImage()
    {
        int[] var1 = TextureUtil.missingTextureData;
        this.missingImage.setIconWidth(16);
        this.missingImage.setIconHeight(16);
        int[][] var2 = new int[this.mipmapLevels + 1][];
        var2[0] = var1;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] {var2}));
        this.missingImage.setIndexInMap(0);
    }

    @Override
	public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        if (this.iconCreator != null)
        {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_)
    {
        this.mapRegisteredSprites.clear();
        p_174943_2_.registerSprites(this);
        this.initMissingImage();
        this.deleteGlTexture();
        ConnectedTextures.updateIcons(this);
        this.loadTextureAtlas(resourceManager);
    }

    public void loadTextureAtlas(IResourceManager resourceManager)
    {
        Config.dbg("Multitexture: " + Config.isMultiTexture());

        if (Config.isMultiTexture())
        {
            Iterator var2 = this.mapUploadedSprites.values().iterator();

            while (var2.hasNext())
            {
                TextureAtlasSprite var3 = (TextureAtlasSprite)var2.next();
                var3.deleteSpriteTexture();
            }
        }

        int var21 = Minecraft.getGLMaximumTextureSize();
        Stitcher var31 = new Stitcher(var21, var21, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int var4 = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, new Object[] {this});
        int var5 = 1 << this.mipmapLevels;
        Iterator var6 = this.mapRegisteredSprites.entrySet().iterator();

        while (true)
        {
            Iterator it1;

            while (var6.hasNext())
            {
                Entry var25 = (Entry)var6.next();
                TextureAtlasSprite var26 = (TextureAtlasSprite)var25.getValue();
                ResourceLocation var27 = new ResourceLocation(var26.getIconName());
                ResourceLocation var28 = this.completeResourceLocation(var27, 0);

                if (!var26.hasCustomLoader(resourceManager, var27))
                {
                    try
                    {
                        IResource var30 = resourceManager.getResource(var28);
                        BufferedImage[] var311 = new BufferedImage[1 + this.mipmapLevels];
                        var311[0] = TextureUtil.readBufferedImage(var30.getInputStream());
                        TextureMetadataSection sheetWidth = (TextureMetadataSection)var30.getMetadata("texture");

                        if (sheetWidth != null)
                        {
                            List sheetHeight = sheetWidth.getListMipmaps();
                            int listSprites;

                            if (!sheetHeight.isEmpty())
                            {
                                int it = var311[0].getWidth();
                                listSprites = var311[0].getHeight();

                                if (MathHelper.roundUpToPowerOfTwo(it) != it || MathHelper.roundUpToPowerOfTwo(listSprites) != listSprites)
                                {
                                    throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                                }
                            }

                            it1 = sheetHeight.iterator();

                            while (it1.hasNext())
                            {
                                listSprites = ((Integer)it1.next()).intValue();

                                if (listSprites > 0 && listSprites < var311.length - 1 && var311[listSprites] == null)
                                {
                                    ResourceLocation tas = this.completeResourceLocation(var27, listSprites);

                                    try
                                    {
                                        var311[listSprites] = TextureUtil.readBufferedImage(resourceManager.getResource(tas).getInputStream());
                                    }
                                    catch (IOException var24)
                                    {
                                        logger.error("Unable to load miplevel {} from: {}", new Object[] {Integer.valueOf(listSprites), tas, var24});
                                    }
                                }
                            }
                        }

                        AnimationMetadataSection sheetHeight1 = (AnimationMetadataSection)var30.getMetadata("animation");
                        var26.loadSprite(var311, sheetHeight1);
                    }
                    catch (RuntimeException var251)
                    {
                        logger.error("Unable to parse metadata from " + var28, var251);
                        continue;
                    }
                    catch (IOException var261)
                    {
                        logger.error("Using missing texture, unable to load " + var28 + ", " + var261.getClass().getName());
                        continue;
                    }

                    var4 = Math.min(var4, Math.min(var26.getIconWidth(), var26.getIconHeight()));
                    int var301 = Math.min(Integer.lowestOneBit(var26.getIconWidth()), Integer.lowestOneBit(var26.getIconHeight()));

                    if (var301 < var5)
                    {
                        logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[] {var28, Integer.valueOf(var26.getIconWidth()), Integer.valueOf(var26.getIconHeight()), Integer.valueOf(MathHelper.calculateLogBaseTwo(var5)), Integer.valueOf(MathHelper.calculateLogBaseTwo(var301))});
                        var5 = var301;
                    }

                    var31.addSprite(var26);
                }
                else if (!var26.load(resourceManager, var27))
                {
                    var4 = Math.min(var4, Math.min(var26.getIconWidth(), var26.getIconHeight()));
                    var31.addSprite(var26);
                }
            }

            int var252 = Math.min(var4, var5);
            int var262 = MathHelper.calculateLogBaseTwo(var252);

            if (var262 < 0)
            {
                var262 = 0;
            }

            if (var262 < this.mipmapLevels)
            {
                logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] {this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(var262), Integer.valueOf(var252)});
                this.mipmapLevels = var262;
            }

            Iterator var271 = this.mapRegisteredSprites.values().iterator();

            while (var271.hasNext())
            {
                final TextureAtlasSprite var281 = (TextureAtlasSprite)var271.next();

                try
                {
                    var281.generateMipmaps(this.mipmapLevels);
                }
                catch (Throwable var23)
                {
                    CrashReport var311 = CrashReport.makeCrashReport(var23, "Applying mipmap");
                    CrashReportCategory sheetWidth1 = var311.makeCategory("Sprite being mipmapped");
                    sheetWidth1.addCrashSectionCallable("Sprite name", new Callable()
                    {
                        @Override
						public String call()
                        {
                            return var281.getIconName();
                        }
                    });
                    sheetWidth1.addCrashSectionCallable("Sprite size", new Callable()
                    {
                        @Override
						public String call()
                        {
                            return var281.getIconWidth() + " x " + var281.getIconHeight();
                        }
                    });
                    sheetWidth1.addCrashSectionCallable("Sprite frames", new Callable()
                    {
                        @Override
						public String call()
                        {
                            return var281.getFrameCount() + " frames";
                        }
                    });
                    sheetWidth1.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
                    throw new ReportedException(var311);
                }
            }

            this.missingImage.generateMipmaps(this.mipmapLevels);
            var31.addSprite(this.missingImage);

            try
            {
                var31.doStitch();
            }
            catch (StitcherException var22)
            {
                throw var22;
            }

            logger.info("Created: {}x{} {}-atlas", new Object[] {Integer.valueOf(var31.getCurrentWidth()), Integer.valueOf(var31.getCurrentHeight()), this.basePath});
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, var31.getCurrentWidth(), var31.getCurrentHeight());
            HashMap var282 = Maps.newHashMap(this.mapRegisteredSprites);
            Iterator var302 = var31.getStichSlots().iterator();
            TextureAtlasSprite var312;

            while (var302.hasNext())
            {
                var312 = (TextureAtlasSprite)var302.next();
                String sheetWidth2 = var312.getIconName();
                var282.remove(sheetWidth2);
                this.mapUploadedSprites.put(sheetWidth2, var312);

                try
                {
                    TextureUtil.uploadTextureMipmap(var312.getFrameTextureData(0), var312.getIconWidth(), var312.getIconHeight(), var312.getOriginX(), var312.getOriginY(), false, false);
                }
                catch (Throwable var211)
                {
                    CrashReport listSprites1 = CrashReport.makeCrashReport(var211, "Stitching texture atlas");
                    CrashReportCategory it2 = listSprites1.makeCategory("Texture being stitched together");
                    it2.addCrashSection("Atlas path", this.basePath);
                    it2.addCrashSection("Sprite", var312);
                    throw new ReportedException(listSprites1);
                }

                if (var312.hasAnimationMetadata())
                {
                    this.listAnimatedSprites.add(var312);
                }
            }

            var302 = var282.values().iterator();

            while (var302.hasNext())
            {
                var312 = (TextureAtlasSprite)var302.next();
                var312.copyFrom(this.missingImage);
            }

            if (Config.isMultiTexture())
            {
                int sheetWidth3 = var31.getCurrentWidth();
                int sheetHeight2 = var31.getCurrentHeight();
                List listSprites2 = var31.getStichSlots();
                it1 = listSprites2.iterator();

                while (it1.hasNext())
                {
                    TextureAtlasSprite tas1 = (TextureAtlasSprite)it1.next();
                    tas1.sheetWidth = sheetWidth3;
                    tas1.sheetHeight = sheetHeight2;
                    TextureAtlasSprite ss = tas1.spriteSingle;

                    if (ss != null)
                    {
                        ss.sheetWidth = sheetWidth3;
                        ss.sheetHeight = sheetHeight2;
                        tas1.bindSpriteTexture();
                        boolean texBlur = false;
                        boolean texClamp = true;
                        TextureUtil.uploadTextureMipmap(ss.getFrameTextureData(0), ss.getIconWidth(), ss.getIconHeight(), ss.getOriginX(), ss.getOriginY(), texBlur, texClamp);
                    }
                }

                Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
            }

            Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, new Object[] {this});

            if (Config.equals(System.getProperty("saveTextureMap"), "true"))
            {
                TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, var31.getCurrentWidth(), var31.getCurrentHeight());
            }

            return;
        }
    }

    private ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_)
    {
        return this.isAbsoluteLocation(location) ? (p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "mipmap" + p_147634_2_ + ".png")) : (p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath, location.getResourcePath(), ".png"})): new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] {this.basePath, location.getResourcePath(), Integer.valueOf(p_147634_2_), ".png"})));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName)
    {
        TextureAtlasSprite var2 = (TextureAtlasSprite)this.mapUploadedSprites.get(iconName);

        if (var2 == null)
        {
            var2 = this.missingImage;
        }

        return var2;
    }

    public void updateAnimations()
    {
        TextureUtil.bindTexture(this.getGlTextureId());
        Iterator var1 = this.listAnimatedSprites.iterator();

        while (var1.hasNext())
        {
            TextureAtlasSprite it = (TextureAtlasSprite)var1.next();

            if (this.isTerrainAnimationActive(it))
            {
                it.updateAnimation();
            }
        }

        if (Config.isMultiTexture())
        {
            Iterator it1 = this.listAnimatedSprites.iterator();

            while (it1.hasNext())
            {
                TextureAtlasSprite ts = (TextureAtlasSprite)it1.next();

                if (this.isTerrainAnimationActive(ts))
                {
                    TextureAtlasSprite spriteSingle = ts.spriteSingle;

                    if (spriteSingle != null)
                    {
                        ts.bindSpriteTexture();
                        spriteSingle.updateAnimation();
                    }
                }
            }

            TextureUtil.bindTexture(this.getGlTextureId());
        }
    }

    public TextureAtlasSprite registerSprite(ResourceLocation location)
    {
        if (location == null)
        {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        else
        {
            TextureAtlasSprite var2 = (TextureAtlasSprite)this.mapRegisteredSprites.get(location.toString());

            if (var2 == null && Reflector.ModLoader_getCustomAnimationLogic.exists())
            {
                var2 = (TextureAtlasSprite)Reflector.call(Reflector.ModLoader_getCustomAnimationLogic, new Object[] {location});
            }

            if (var2 == null)
            {
                var2 = TextureAtlasSprite.makeAtlasSprite(location);
                this.mapRegisteredSprites.put(location.toString(), var2);

                if (var2 instanceof TextureAtlasSprite && var2.getIndexInMap() < 0)
                {
                    var2.setIndexInMap(this.mapRegisteredSprites.size());
                }
            }

            return var2;
        }
    }

    @Override
	public void tick()
    {
        this.updateAnimations();
    }

    public void setMipmapLevels(int mipmapLevelsIn)
    {
        this.mipmapLevels = mipmapLevelsIn;
    }

    public TextureAtlasSprite getMissingSprite()
    {
        return this.missingImage;
    }

    public TextureAtlasSprite getTextureExtry(String name)
    {
        ResourceLocation loc = new ResourceLocation(name);
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(loc.toString());
    }

    public boolean setTextureEntry(String name, TextureAtlasSprite entry)
    {
        if (!this.mapRegisteredSprites.containsKey(name))
        {
            this.mapRegisteredSprites.put(name, entry);

            if (entry.getIndexInMap() < 0)
            {
                entry.setIndexInMap(this.mapRegisteredSprites.size());
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    private boolean isAbsoluteLocation(ResourceLocation loc)
    {
        String path = loc.getResourcePath();
        return this.isAbsoluteLocationPath(path);
    }

    private boolean isAbsoluteLocationPath(String resPath)
    {
        String path = resPath.toLowerCase();
        return path.startsWith("mcpatcher/") || path.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String name)
    {
        ResourceLocation loc = new ResourceLocation(name);
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(loc.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite ts)
    {
        return ts != TextureUtils.iconWaterStill && ts != TextureUtils.iconWaterFlow ? (ts != TextureUtils.iconLavaStill && ts != TextureUtils.iconLavaFlow ? (ts != TextureUtils.iconFireLayer0 && ts != TextureUtils.iconFireLayer1 ? (ts == TextureUtils.iconPortal ? Config.isAnimatedPortal() : Config.isAnimatedTerrain()) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }

    public int getCountRegisteredSprites()
    {
        return this.mapRegisteredSprites.size();
    }
}
