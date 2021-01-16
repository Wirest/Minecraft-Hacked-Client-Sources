package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.src.BetterGrass;
import net.minecraft.src.Config;
import net.minecraft.src.ConnectedTextures;
import net.minecraft.src.CustomItems;
import net.minecraft.src.Reflector;
import net.minecraft.src.ReflectorForge;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation LOCATION_BLOCKS_TEXTURE = new ResourceLocation("textures/atlas/blocks.png");
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
    private final String basePath;
    private final ITextureMapPopulator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private boolean skipFirst;
    private TextureAtlasSprite[] iconGrid;
    private int iconGridSize;
    private int iconGridCountX;
    private int iconGridCountY;
    private double iconGridSizeU;
    private double iconGridSizeV;
    private int counterIndexInMap;
    public int atlasWidth;
    public int atlasHeight;

    public TextureMap(String basePathIn)
    {
        this(basePathIn, (ITextureMapPopulator)null);
    }

    public TextureMap(String p_i5_1_, boolean p_i5_2_)
    {
        this(p_i5_1_, (ITextureMapPopulator)null, p_i5_2_);
    }

    public TextureMap(String basePathIn, @Nullable ITextureMapPopulator iconCreatorIn)
    {
        this(basePathIn, iconCreatorIn, false);
    }

    public TextureMap(String p_i6_1_, ITextureMapPopulator p_i6_2_, boolean p_i6_3_)
    {
        this.skipFirst = false;
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0D;
        this.iconGridSizeV = -1.0D;
        this.counterIndexInMap = 0;
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = Lists.<TextureAtlasSprite>newArrayList();
        this.mapRegisteredSprites = Maps.<String, TextureAtlasSprite>newHashMap();
        this.mapUploadedSprites = Maps.<String, TextureAtlasSprite>newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i6_1_;
        this.iconCreator = p_i6_2_;
        this.skipFirst = p_i6_3_ && ENABLE_SKIP;
    }

    private void initMissingImage()
    {
        int i = this.getMinSpriteSize();
        int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        int[][] aint1 = new int[this.mipmapLevels + 1][];
        aint1[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] {aint1}));
        this.missingImage.setIndexInMap(this.counterIndexInMap++);
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        ShadersTex.resManager = resourceManager;

        if (this.iconCreator != null)
        {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, ITextureMapPopulator iconCreatorIn)
    {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap = 0;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        iconCreatorIn.registerSprites(this);

        if (this.mipmapLevels >= 4)
        {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }

        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    public void loadTextureAtlas(IResourceManager resourceManager)
    {
        ShadersTex.resManager = resourceManager;
        Config.dbg("Multitexture: " + Config.isMultiTexture());

        if (Config.isMultiTexture())
        {
            for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values())
            {
                textureatlassprite.deleteSpriteTexture();
            }
        }

        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        BetterGrass.updateIcons(this);
        int j1 = TextureUtils.getGLMaximumTextureSize();
        Stitcher stitcher = new Stitcher(j1, j1, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i = Integer.MAX_VALUE;
        int j = this.getMinSpriteSize();
        this.iconGridSize = j;
        int k = 1 << this.mipmapLevels;

        for (Entry<String, TextureAtlasSprite> entry : this.mapRegisteredSprites.entrySet())
        {
            if (this.skipFirst)
            {
                break;
            }

            TextureAtlasSprite textureatlassprite1 = entry.getValue();
            ResourceLocation resourcelocation = this.getResourceLocation(textureatlassprite1);
            IResource iresource = null;

            if (textureatlassprite1.getIndexInMap() < 0)
            {
                textureatlassprite1.setIndexInMap(this.counterIndexInMap++);
            }

            if (textureatlassprite1.hasCustomLoader(resourceManager, resourcelocation))
            {
                if (textureatlassprite1.load(resourceManager, resourcelocation))
                {
                    Config.dbg("Custom loader (skipped): " + textureatlassprite1);
                    continue;
                }

                Config.dbg("Custom loader: " + textureatlassprite1);
            }
            else
            {
                try
                {
                    PngSizeInfo pngsizeinfo = PngSizeInfo.makeFromResource(resourceManager.getResource(resourcelocation));

                    if (Config.isShaders())
                    {
                        iresource = ShadersTex.loadResource(resourceManager, resourcelocation);
                    }
                    else
                    {
                        iresource = resourceManager.getResource(resourcelocation);
                    }

                    boolean flag = iresource.getMetadata("animation") != null;
                    textureatlassprite1.loadSprite(pngsizeinfo, flag);
                }
                catch (RuntimeException runtimeexception)
                {
                    LOGGER.error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    ReflectorForge.FMLClientHandler_trackBrokenTexture(resourcelocation, runtimeexception.getMessage());
                    continue;
                }
                catch (IOException ioexception)
                {
                    LOGGER.error("Using missing texture, unable to load " + resourcelocation + ", " + ioexception.getClass().getName());
                    ReflectorForge.FMLClientHandler_trackMissingTexture(resourcelocation);
                    continue;
                }
                finally
                {
                    IOUtils.closeQuietly((Closeable)iresource);
                }
            }

            int k2 = textureatlassprite1.getIconWidth();
            int l2 = textureatlassprite1.getIconHeight();

            if (k2 >= 1 && l2 >= 1)
            {
                if (k2 < j || this.mipmapLevels > 0)
                {
                    int l = this.mipmapLevels > 0 ? TextureUtils.scaleToPowerOfTwo(k2, j) : TextureUtils.scaleMinTo(k2, j);

                    if (l != k2)
                    {
                        if (!TextureUtils.isPowerOfTwo(k2))
                        {
                            Config.log("Scaled non power of 2: " + textureatlassprite1.getIconName() + ", " + k2 + " -> " + l);
                        }
                        else
                        {
                            Config.log("Scaled too small texture: " + textureatlassprite1.getIconName() + ", " + k2 + " -> " + l);
                        }

                        int i1 = l2 * l / k2;
                        textureatlassprite1.setIconWidth(l);
                        textureatlassprite1.setIconHeight(i1);
                    }
                }

                i = Math.min(i, Math.min(textureatlassprite1.getIconWidth(), textureatlassprite1.getIconHeight()));
                int i3 = Math.min(Integer.lowestOneBit(textureatlassprite1.getIconWidth()), Integer.lowestOneBit(textureatlassprite1.getIconHeight()));

                if (i3 < k)
                {
                    LOGGER.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation, Integer.valueOf(textureatlassprite1.getIconWidth()), Integer.valueOf(textureatlassprite1.getIconHeight()), Integer.valueOf(MathHelper.log2(k)), Integer.valueOf(MathHelper.log2(i3)));
                    k = i3;
                }

                stitcher.addSprite(textureatlassprite1);
            }
            else
            {
                Config.warn("Invalid sprite size: " + textureatlassprite1);
            }
        }

        int k1 = Math.min(i, k);
        int l1 = MathHelper.log2(k1);

        if (l1 < 0)
        {
            l1 = 0;
        }

        if (l1 < this.mipmapLevels)
        {
            LOGGER.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(l1), Integer.valueOf(k1));
            this.mipmapLevels = l1;
        }

        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        this.skipFirst = false;

        try
        {
            stitcher.doStitch();
        }
        catch (StitcherException stitcherexception)
        {
            throw stitcherexception;
        }

        LOGGER.info("Created: {}x{} {}-atlas", Integer.valueOf(stitcher.getCurrentWidth()), Integer.valueOf(stitcher.getCurrentHeight()), this.basePath);

        if (Config.isShaders())
        {
            ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), stitcher, this);
        }
        else
        {
            TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }

        Map<String, TextureAtlasSprite> map = Maps.<String, TextureAtlasSprite>newHashMap(this.mapRegisteredSprites);

        for (TextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots())
        {
            if (Config.isShaders())
            {
                ShadersTex.setIconName(ShadersTex.setSprite(textureatlassprite2).getIconName());
            }

            if (textureatlassprite2 == this.missingImage || this.generateMipmaps(resourceManager, textureatlassprite2))
            {
                String s = textureatlassprite2.getIconName();
                map.remove(s);
                this.mapUploadedSprites.put(s, textureatlassprite2);

                try
                {
                    if (Config.isShaders())
                    {
                        ShadersTex.uploadTexSubForLoadAtlas(textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
                    }
                    else
                    {
                        TextureUtil.uploadTextureMipmap(textureatlassprite2.getFrameTextureData(0), textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
                    }
                }
                catch (Throwable throwable)
                {
                    CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
                    CrashReportCategory crashreportcategory = crashreport.makeCategory("Texture being stitched together");
                    crashreportcategory.addCrashSection("Atlas path", this.basePath);
                    crashreportcategory.addCrashSection("Sprite", textureatlassprite2);
                    throw new ReportedException(crashreport);
                }

                if (textureatlassprite2.hasAnimationMetadata())
                {
                    this.listAnimatedSprites.add(textureatlassprite2);
                }
            }
        }

        for (TextureAtlasSprite textureatlassprite3 : map.values())
        {
            textureatlassprite3.copyFrom(this.missingImage);
        }

        if (Config.isMultiTexture())
        {
            int i2 = stitcher.getCurrentWidth();
            int j2 = stitcher.getCurrentHeight();

            for (TextureAtlasSprite textureatlassprite4 : stitcher.getStichSlots())
            {
                textureatlassprite4.sheetWidth = i2;
                textureatlassprite4.sheetHeight = j2;
                textureatlassprite4.mipmapLevels = this.mipmapLevels;
                TextureAtlasSprite textureatlassprite5 = textureatlassprite4.spriteSingle;

                if (textureatlassprite5 != null)
                {
                    if (textureatlassprite5.getIconWidth() <= 0)
                    {
                        textureatlassprite5.setIconWidth(textureatlassprite4.getIconWidth());
                        textureatlassprite5.setIconHeight(textureatlassprite4.getIconHeight());
                        textureatlassprite5.initSprite(textureatlassprite4.getIconWidth(), textureatlassprite4.getIconHeight(), 0, 0, false);
                        textureatlassprite5.clearFramesTextureData();
                        List<int[][]> list = textureatlassprite4.getFramesTextureData();
                        textureatlassprite5.setFramesTextureData(list);
                        textureatlassprite5.setAnimationMetadata(textureatlassprite4.getAnimationMetadata());
                    }

                    textureatlassprite5.sheetWidth = i2;
                    textureatlassprite5.sheetHeight = j2;
                    textureatlassprite5.mipmapLevels = this.mipmapLevels;
                    textureatlassprite4.bindSpriteTexture();
                    boolean flag2 = false;
                    boolean flag1 = true;

                    try
                    {
                        TextureUtil.uploadTextureMipmap(textureatlassprite5.getFrameTextureData(0), textureatlassprite5.getIconWidth(), textureatlassprite5.getIconHeight(), textureatlassprite5.getOriginX(), textureatlassprite5.getOriginY(), flag2, flag1);
                    }
                    catch (Exception exception)
                    {
                        Config.dbg("Error uploading sprite single: " + textureatlassprite5 + ", parent: " + textureatlassprite4);
                        exception.printStackTrace();
                    }
                }
            }

            Config.getMinecraft().getTextureManager().bindTexture(LOCATION_BLOCKS_TEXTURE);
        }

        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        this.updateIconGrid(stitcher.getCurrentWidth(), stitcher.getCurrentHeight());

        if (Config.equals(System.getProperty("saveTextureMap"), "true"))
        {
            Config.dbg("Exporting texture map: " + this.basePath);
            TextureUtils.saveGlTexture("debug/" + this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }

    public boolean generateMipmaps(IResourceManager resourceManager, final TextureAtlasSprite texture)
    {
        ResourceLocation resourcelocation = this.getResourceLocation(texture);
        IResource iresource = null;

        if (texture.hasCustomLoader(resourceManager, resourcelocation))
        {
            TextureUtils.generateCustomMipmaps(texture, this.mipmapLevels);
        }
        else
        {
            label60:
            {
                boolean flag1;

                try
                {
                    iresource = resourceManager.getResource(resourcelocation);
                    texture.loadSpriteFrames(iresource, this.mipmapLevels + 1);
                    break label60;
                }
                catch (RuntimeException runtimeexception)
                {
                    LOGGER.error("Unable to parse metadata from {}", resourcelocation, runtimeexception);
                    flag1 = false;
                }
                catch (IOException ioexception)
                {
                    LOGGER.error("Using missing texture, unable to load {}", resourcelocation, ioexception);
                    flag1 = false;
                    boolean crashreportcategory = flag1;
                    return crashreportcategory;
                }
                finally
                {
                    IOUtils.closeQuietly((Closeable)iresource);
                }

                return flag1;
            }
        }

        try
        {
            texture.generateMipmaps(this.mipmapLevels);
            return true;
        }
        catch (Throwable throwable1)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
            crashreportcategory.setDetail("Sprite name", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return texture.getIconName();
                }
            });
            crashreportcategory.setDetail("Sprite size", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return texture.getIconWidth() + " x " + texture.getIconHeight();
                }
            });
            crashreportcategory.setDetail("Sprite frames", new ICrashReportDetail<String>()
            {
                public String call() throws Exception
                {
                    return texture.getFrameCount() + " frames";
                }
            });
            crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
            throw new ReportedException(crashreport);
        }
    }

    public ResourceLocation getResourceLocation(TextureAtlasSprite p_184396_1_)
    {
        ResourceLocation resourcelocation = new ResourceLocation(p_184396_1_.getIconName());
        return this.completeResourceLocation(resourcelocation);
    }

    public ResourceLocation completeResourceLocation(ResourceLocation p_completeResourceLocation_1_)
    {
        return this.isAbsoluteLocation(p_completeResourceLocation_1_) ? new ResourceLocation(p_completeResourceLocation_1_.getResourceDomain(), p_completeResourceLocation_1_.getResourcePath() + ".png") : new ResourceLocation(p_completeResourceLocation_1_.getResourceDomain(), String.format("%s/%s%s", this.basePath, p_completeResourceLocation_1_.getResourcePath(), ".png"));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName)
    {
        TextureAtlasSprite textureatlassprite = this.mapUploadedSprites.get(iconName);

        if (textureatlassprite == null)
        {
            textureatlassprite = this.missingImage;
        }

        return textureatlassprite;
    }

    public void updateAnimations()
    {
        if (Config.isShaders())
        {
            ShadersTex.updatingTex = this.getMultiTexID();
        }

        boolean flag = false;
        boolean flag1 = false;
        TextureUtil.bindTexture(this.getGlTextureId());

        for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites)
        {
            if (this.isTerrainAnimationActive(textureatlassprite))
            {
                textureatlassprite.updateAnimation();

                if (textureatlassprite.spriteNormal != null)
                {
                    flag = true;
                }

                if (textureatlassprite.spriteSpecular != null)
                {
                    flag1 = true;
                }
            }
        }

        if (Config.isMultiTexture())
        {
            for (TextureAtlasSprite textureatlassprite2 : this.listAnimatedSprites)
            {
                if (this.isTerrainAnimationActive(textureatlassprite2))
                {
                    TextureAtlasSprite textureatlassprite1 = textureatlassprite2.spriteSingle;

                    if (textureatlassprite1 != null)
                    {
                        if (textureatlassprite2 == TextureUtils.iconClock || textureatlassprite2 == TextureUtils.iconCompass)
                        {
                            textureatlassprite1.frameCounter = textureatlassprite2.frameCounter;
                        }

                        textureatlassprite2.bindSpriteTexture();
                        textureatlassprite1.updateAnimation();
                    }
                }
            }

            TextureUtil.bindTexture(this.getGlTextureId());
        }

        if (Config.isShaders())
        {
            if (flag)
            {
                TextureUtil.bindTexture(this.getMultiTexID().norm);

                for (TextureAtlasSprite textureatlassprite3 : this.listAnimatedSprites)
                {
                    if (textureatlassprite3.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite3))
                    {
                        if (textureatlassprite3 == TextureUtils.iconClock || textureatlassprite3 == TextureUtils.iconCompass)
                        {
                            textureatlassprite3.spriteNormal.frameCounter = textureatlassprite3.frameCounter;
                        }

                        textureatlassprite3.spriteNormal.updateAnimation();
                    }
                }
            }

            if (flag1)
            {
                TextureUtil.bindTexture(this.getMultiTexID().spec);

                for (TextureAtlasSprite textureatlassprite4 : this.listAnimatedSprites)
                {
                    if (textureatlassprite4.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite4))
                    {
                        if (textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass)
                        {
                            textureatlassprite4.spriteNormal.frameCounter = textureatlassprite4.frameCounter;
                        }

                        textureatlassprite4.spriteSpecular.updateAnimation();
                    }
                }
            }

            if (flag || flag1)
            {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }

        if (Config.isShaders())
        {
            ShadersTex.updatingTex = null;
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
            TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());

            if (textureatlassprite == null)
            {
                textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
                this.mapRegisteredSprites.put(location.toString(), textureatlassprite);

                if (textureatlassprite.getIndexInMap() < 0)
                {
                    textureatlassprite.setIndexInMap(this.counterIndexInMap++);
                }
            }

            return textureatlassprite;
        }
    }

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

    @Nullable
    public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_)
    {
        return this.mapRegisteredSprites.get(p_getTextureExtry_1_);
    }

    public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_)
    {
        String s = p_setTextureEntry_1_.getIconName();

        if (!this.mapRegisteredSprites.containsKey(s))
        {
            this.mapRegisteredSprites.put(s, p_setTextureEntry_1_);

            if (p_setTextureEntry_1_.getIndexInMap() < 0)
            {
                p_setTextureEntry_1_.setIndexInMap(this.counterIndexInMap++);
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public String getBasePath()
    {
        return this.basePath;
    }

    public int getMipmapLevels()
    {
        return this.mipmapLevels;
    }

    private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_)
    {
        String s = p_isAbsoluteLocation_1_.getResourcePath();
        return this.isAbsoluteLocationPath(s);
    }

    private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_)
    {
        String s = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
    }

    public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_)
    {
        ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_)
    {
        if (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow)
        {
            if (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow)
            {
                if (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1)
                {
                    if (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal)
                    {
                        return Config.isAnimatedPortal();
                    }
                    else
                    {
                        return p_isTerrainAnimationActive_1_ != TextureUtils.iconClock && p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true;
                    }
                }
                else
                {
                    return Config.isAnimatedFire();
                }
            }
            else
            {
                return Config.isAnimatedLava();
            }
        }
        else
        {
            return Config.isAnimatedWater();
        }
    }

    public int getCountRegisteredSprites()
    {
        return this.counterIndexInMap;
    }

    private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_)
    {
        int i = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);

        if (i < 16)
        {
            i = 16;
        }

        i = MathHelper.smallestEncompassingPowerOfTwo(i);

        if (i > 16)
        {
            Config.log("Sprite size: " + i);
        }

        int j = MathHelper.log2(i);

        if (j < 4)
        {
            j = 4;
        }

        return j;
    }

    private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_)
    {
        Map map = new HashMap();

        for (Object entry : p_detectMinimumSpriteSize_1_.entrySet())
        {
            TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)((Entry<String, TextureAtlasSprite>) entry).getValue();
            ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation);

            if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation))
            {
                try
                {
                    IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation1);

                    if (iresource != null)
                    {
                        InputStream inputstream = iresource.getInputStream();

                        if (inputstream != null)
                        {
                            Dimension dimension = TextureUtils.getImageSize(inputstream, "png");

                            if (dimension != null)
                            {
                                int i = dimension.width;
                                int j = MathHelper.smallestEncompassingPowerOfTwo(i);

                                if (!map.containsKey(Integer.valueOf(j)))
                                {
                                    map.put(Integer.valueOf(j), Integer.valueOf(1));
                                }
                                else
                                {
                                    int k = ((Integer)map.get(Integer.valueOf(j))).intValue();
                                    map.put(Integer.valueOf(j), Integer.valueOf(k + 1));
                                }
                            }
                        }
                    }
                }
                catch (Exception var17)
                {
                    ;
                }
            }
        }

        int l = 0;
        Set set = map.keySet();
        Set set1 = new TreeSet(set);
        int l1;

        for (Iterator iterator = set1.iterator(); iterator.hasNext(); l += l1)
        {
            int j1 = ((Integer)iterator.next()).intValue();
            l1 = ((Integer)map.get(Integer.valueOf(j1))).intValue();
        }

        int i1 = 16;
        int k1 = 0;
        l1 = l * p_detectMinimumSpriteSize_3_ / 100;
        Iterator iterator1 = set1.iterator();

        while (iterator1.hasNext())
        {
            int i2 = ((Integer)iterator1.next()).intValue();
            int j2 = ((Integer)map.get(Integer.valueOf(i2))).intValue();
            k1 += j2;

            if (i2 > i1)
            {
                i1 = i2;
            }

            if (k1 > l1)
            {
                return i1;
            }
        }

        return i1;
    }

    private int getMinSpriteSize()
    {
        int i = 1 << this.mipmapLevels;

        if (i < 8)
        {
            i = 8;
        }

        return i;
    }

    private int[] getMissingImageData(int p_getMissingImageData_1_)
    {
        BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.MISSING_TEXTURE_DATA, 0, 16);
        BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
        int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }

    public boolean isTextureBound()
    {
        int i = GlStateManager.getBoundTexture();
        int j = this.getGlTextureId();
        return i == j;
    }

    private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_)
    {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;

        if (this.iconGridSize > 0)
        {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0D / (double)this.iconGridCountX;
            this.iconGridSizeV = 1.0D / (double)this.iconGridCountY;

            for (TextureAtlasSprite textureatlassprite : this.mapUploadedSprites.values())
            {
                double d0 = 0.5D / (double)p_updateIconGrid_1_;
                double d1 = 0.5D / (double)p_updateIconGrid_2_;
                double d2 = (double)Math.min(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) + d0;
                double d3 = (double)Math.min(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) + d1;
                double d4 = (double)Math.max(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) - d0;
                double d5 = (double)Math.max(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) - d1;
                int i = (int)(d2 / this.iconGridSizeU);
                int j = (int)(d3 / this.iconGridSizeV);
                int k = (int)(d4 / this.iconGridSizeU);
                int l = (int)(d5 / this.iconGridSizeV);

                for (int i1 = i; i1 <= k; ++i1)
                {
                    if (i1 >= 0 && i1 < this.iconGridCountX)
                    {
                        for (int j1 = j; j1 <= l; ++j1)
                        {
                            if (j1 >= 0 && j1 < this.iconGridCountX)
                            {
                                int k1 = j1 * this.iconGridCountX + i1;
                                this.iconGrid[k1] = textureatlassprite;
                            }
                            else
                            {
                                Config.warn("Invalid grid V: " + j1 + ", icon: " + textureatlassprite.getIconName());
                            }
                        }
                    }
                    else
                    {
                        Config.warn("Invalid grid U: " + i1 + ", icon: " + textureatlassprite.getIconName());
                    }
                }
            }
        }
    }

    public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_)
    {
        if (this.iconGrid == null)
        {
            return null;
        }
        else
        {
            int i = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
            int j = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
            int k = j * this.iconGridCountX + i;
            return k >= 0 && k <= this.iconGrid.length ? this.iconGrid[k] : null;
        }
    }
}
