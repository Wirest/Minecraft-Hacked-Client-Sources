// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.renderer.texture;

import net.minecraft.client.renderer.GlStateManager;
import java.util.Set;
import java.awt.Dimension;
import java.io.InputStream;
import java.util.Collection;
import java.util.TreeSet;
import java.util.HashMap;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.client.resources.IResource;
import java.util.Iterator;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.util.ReportedException;
import java.util.concurrent.Callable;
import net.minecraft.crash.CrashReport;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.util.MathHelper;
import net.minecraft.client.resources.data.TextureMetadataSection;
import optifine.TextureUtils;
import java.awt.image.BufferedImage;
import optifine.Reflector;
import net.minecraft.client.Minecraft;
import optifine.ConnectedTextures;
import optifine.Config;
import java.io.IOException;
import shadersmod.client.ShadersTex;
import net.minecraft.client.resources.IResourceManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.Map;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final boolean ENABLE_SKIP;
    private static final Logger logger;
    public static final ResourceLocation LOCATION_MISSING_TEXTURE;
    public static final ResourceLocation locationBlocksTexture;
    private final List listAnimatedSprites;
    private final Map mapRegisteredSprites;
    private final Map mapUploadedSprites;
    private final String basePath;
    private final IIconCreator iconCreator;
    private int mipmapLevels;
    private final TextureAtlasSprite missingImage;
    private static final String __OBFID = "CL_00001058";
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
    
    static {
        ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
        logger = LogManager.getLogger();
        LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
        locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    }
    
    public TextureMap(final String p_i46099_1_) {
        this(p_i46099_1_, null);
    }
    
    public TextureMap(final String p_i10_1_, final boolean p_i10_2_) {
        this(p_i10_1_, null, p_i10_2_);
    }
    
    public TextureMap(final String p_i46100_1_, final IIconCreator iconCreatorIn) {
        this(p_i46100_1_, iconCreatorIn, false);
    }
    
    public TextureMap(final String p_i11_1_, final IIconCreator p_i11_2_, final boolean p_i11_3_) {
        this.skipFirst = false;
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0;
        this.iconGridSizeV = -1.0;
        this.counterIndexInMap = 0;
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i11_1_;
        this.iconCreator = p_i11_2_;
        this.skipFirst = (p_i11_3_ && TextureMap.ENABLE_SKIP);
    }
    
    private void initMissingImage() {
        final int i = this.getMinSpriteSize();
        final int[] aint = this.getMissingImageData(i);
        this.missingImage.setIconWidth(i);
        this.missingImage.setIconHeight(i);
        final int[][] aint2 = new int[this.mipmapLevels + 1][];
        aint2[0] = aint;
        this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] { aint2 }));
        this.missingImage.setIndexInMap(this.counterIndexInMap++);
    }
    
    @Override
    public void loadTexture(final IResourceManager resourceManager) throws IOException {
        ShadersTex.resManager = resourceManager;
        if (this.iconCreator != null) {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }
    
    public void loadSprites(final IResourceManager resourceManager, final IIconCreator p_174943_2_) {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap = 0;
        p_174943_2_.registerSprites(this);
        if (this.mipmapLevels >= 4) {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }
        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }
    
    public void loadTextureAtlas(final IResourceManager resourceManager) {
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
            for (final Object textureatlassprite : this.mapUploadedSprites.values()) {
                ((TextureAtlasSprite)textureatlassprite).deleteSpriteTexture();
            }
        }
        ConnectedTextures.updateIcons(this);
        final int l1 = Minecraft.getGLMaximumTextureSize();
        final Stitcher stitcher = new Stitcher(l1, l1, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int i = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, this);
        final int j = this.getMinSpriteSize();
        int k = 1 << this.mipmapLevels;
        for (final Object entry : this.mapRegisteredSprites.entrySet()) {
            final TextureAtlasSprite textureatlassprite2 = ((Map.Entry)entry).getValue();
            final ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite2.getIconName());
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, 0);
            if (!textureatlassprite2.hasCustomLoader(resourceManager, resourcelocation)) {
                try {
                    final IResource iresource = resourceManager.getResource(resourcelocation2);
                    final BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
                    abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
                    if (this.mipmapLevels > 0 && abufferedimage != null) {
                        final int m = abufferedimage[0].getWidth();
                        abufferedimage[0] = TextureUtils.scaleToPowerOfTwo(abufferedimage[0], j);
                        final int i2 = abufferedimage[0].getWidth();
                        if (!TextureUtils.isPowerOfTwo(m)) {
                            Config.log("Scaled non power of 2: " + textureatlassprite2.getIconName() + ", " + m + " -> " + i2);
                        }
                    }
                    final TextureMetadataSection texturemetadatasection = iresource.getMetadata("texture");
                    if (texturemetadatasection != null) {
                        final List list = texturemetadatasection.getListMipmaps();
                        if (!list.isEmpty()) {
                            final int k2 = abufferedimage[0].getWidth();
                            final int j2 = abufferedimage[0].getHeight();
                            if (MathHelper.roundUpToPowerOfTwo(k2) != k2 || MathHelper.roundUpToPowerOfTwo(j2) != j2) {
                                throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                            }
                        }
                        for (final int j3 : list) {
                            if (j3 > 0 && j3 < abufferedimage.length - 1 && abufferedimage[j3] == null) {
                                final ResourceLocation resourcelocation3 = this.completeResourceLocation(resourcelocation, j3);
                                try {
                                    abufferedimage[j3] = TextureUtil.readBufferedImage(resourceManager.getResource(resourcelocation3).getInputStream());
                                }
                                catch (IOException ioexception) {
                                    TextureMap.logger.error("Unable to load miplevel {} from: {}", j3, resourcelocation3, ioexception);
                                }
                            }
                        }
                    }
                    final AnimationMetadataSection animationmetadatasection = iresource.getMetadata("animation");
                    textureatlassprite2.loadSprite(abufferedimage, animationmetadatasection);
                }
                catch (RuntimeException runtimeexception) {
                    TextureMap.logger.error("Unable to parse metadata from " + resourcelocation2, runtimeexception);
                    continue;
                }
                catch (IOException ioexception2) {
                    TextureMap.logger.error("Using missing texture, unable to load " + resourcelocation2 + ", " + ioexception2.getClass().getName());
                    continue;
                }
                i = Math.min(i, Math.min(textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight()));
                final int k3 = Math.min(Integer.lowestOneBit(textureatlassprite2.getIconWidth()), Integer.lowestOneBit(textureatlassprite2.getIconHeight()));
                if (k3 < k) {
                    TextureMap.logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", resourcelocation2, textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(), MathHelper.calculateLogBaseTwo(k), MathHelper.calculateLogBaseTwo(k3));
                    k = k3;
                }
                stitcher.addSprite(textureatlassprite2);
            }
            else {
                if (textureatlassprite2.load(resourceManager, resourcelocation)) {
                    continue;
                }
                i = Math.min(i, Math.min(textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight()));
                stitcher.addSprite(textureatlassprite2);
            }
        }
        final int i3 = Math.min(i, k);
        int j4 = MathHelper.calculateLogBaseTwo(i3);
        if (j4 < 0) {
            j4 = 0;
        }
        if (j4 < this.mipmapLevels) {
            TextureMap.logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", this.basePath, this.mipmapLevels, j4, i3);
            this.mipmapLevels = j4;
        }
        for (final Object textureatlassprite3 : this.mapRegisteredSprites.values()) {
            final TextureAtlasSprite textureatlassprite4 = (TextureAtlasSprite)textureatlassprite3;
            try {
                textureatlassprite4.generateMipmaps(this.mipmapLevels);
            }
            catch (Throwable throwable1) {
                final CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
                final CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
                crashreportcategory.addCrashSectionCallable("Sprite name", new Callable() {
                    private static final String __OBFID = "CL_00001059";
                    
                    @Override
                    public String call() throws Exception {
                        return textureatlassprite4.getIconName();
                    }
                });
                crashreportcategory.addCrashSectionCallable("Sprite size", new Callable() {
                    private static final String __OBFID = "CL_00001060";
                    
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(textureatlassprite4.getIconWidth()) + " x " + textureatlassprite4.getIconHeight();
                    }
                });
                crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable() {
                    private static final String __OBFID = "CL_00001061";
                    
                    @Override
                    public String call() throws Exception {
                        return String.valueOf(textureatlassprite4.getFrameCount()) + " frames";
                    }
                });
                crashreportcategory.addCrashSection("Mipmap levels", this.mipmapLevels);
                throw new ReportedException(crashreport);
            }
        }
        this.missingImage.generateMipmaps(this.mipmapLevels);
        stitcher.addSprite(this.missingImage);
        try {
            stitcher.doStitch();
        }
        catch (StitcherException stitcherexception) {
            throw stitcherexception;
        }
        TextureMap.logger.info("Created: {}x{} {}-atlas", stitcher.getCurrentWidth(), stitcher.getCurrentHeight(), this.basePath);
        TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        final HashMap hashmap = Maps.newHashMap((Map<?, ?>)this.mapRegisteredSprites);
        for (final Object textureatlassprite5 : stitcher.getStichSlots()) {
            final TextureAtlasSprite textureatlassprite6 = (TextureAtlasSprite)textureatlassprite5;
            final String s = textureatlassprite6.getIconName();
            hashmap.remove(s);
            this.mapUploadedSprites.put(s, textureatlassprite6);
            try {
                TextureUtil.uploadTextureMipmap(textureatlassprite6.getFrameTextureData(0), textureatlassprite6.getIconWidth(), textureatlassprite6.getIconHeight(), textureatlassprite6.getOriginX(), textureatlassprite6.getOriginY(), false, false);
            }
            catch (Throwable throwable2) {
                final CrashReport crashreport2 = CrashReport.makeCrashReport(throwable2, "Stitching texture atlas");
                final CrashReportCategory crashreportcategory2 = crashreport2.makeCategory("Texture being stitched together");
                crashreportcategory2.addCrashSection("Atlas path", this.basePath);
                crashreportcategory2.addCrashSection("Sprite", textureatlassprite6);
                throw new ReportedException(crashreport2);
            }
            if (textureatlassprite6.hasAnimationMetadata()) {
                this.listAnimatedSprites.add(textureatlassprite6);
            }
        }
        for (final Object textureatlassprite7 : hashmap.values()) {
            ((TextureAtlasSprite)textureatlassprite7).copyFrom(this.missingImage);
        }
        if (Config.isMultiTexture()) {
            final int l2 = stitcher.getCurrentWidth();
            final int i4 = stitcher.getCurrentHeight();
            for (final Object textureatlassprite8 : stitcher.getStichSlots()) {
                final TextureAtlasSprite textureatlassprite9 = (TextureAtlasSprite)textureatlassprite8;
                textureatlassprite9.sheetWidth = l2;
                textureatlassprite9.sheetHeight = i4;
                textureatlassprite9.mipmapLevels = this.mipmapLevels;
                final TextureAtlasSprite textureatlassprite10 = textureatlassprite9.spriteSingle;
                if (textureatlassprite10 != null) {
                    textureatlassprite10.sheetWidth = l2;
                    textureatlassprite10.sheetHeight = i4;
                    textureatlassprite10.mipmapLevels = this.mipmapLevels;
                    textureatlassprite9.bindSpriteTexture();
                    final boolean flag = false;
                    final boolean flag2 = true;
                    TextureUtil.uploadTextureMipmap(textureatlassprite10.getFrameTextureData(0), textureatlassprite10.getIconWidth(), textureatlassprite10.getIconHeight(), textureatlassprite10.getOriginX(), textureatlassprite10.getOriginY(), flag, flag2);
                }
            }
            Config.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        }
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, this);
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
            TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(), stitcher.getCurrentHeight());
        }
    }
    
    public ResourceLocation completeResourceLocation(final ResourceLocation location, final int p_147634_2_) {
        return this.isAbsoluteLocation(location) ? ((p_147634_2_ == 0) ? new ResourceLocation(location.getResourceDomain(), String.valueOf(location.getResourcePath()) + ".png") : new ResourceLocation(location.getResourceDomain(), String.valueOf(location.getResourcePath()) + "mipmap" + p_147634_2_ + ".png")) : ((p_147634_2_ == 0) ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", this.basePath, location.getResourcePath(), ".png")) : new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", this.basePath, location.getResourcePath(), p_147634_2_, ".png")));
    }
    
    public TextureAtlasSprite getAtlasSprite(final String iconName) {
        TextureAtlasSprite textureatlassprite = this.mapUploadedSprites.get(iconName);
        if (textureatlassprite == null) {
            textureatlassprite = this.missingImage;
        }
        return textureatlassprite;
    }
    
    public void updateAnimations() {
        if (Config.isShaders()) {
            ShadersTex.updatingTex = this.getMultiTexID();
        }
        boolean flag = false;
        boolean flag2 = false;
        TextureUtil.bindTexture(this.getGlTextureId());
        for (final Object textureatlassprite0 : this.listAnimatedSprites) {
            final TextureAtlasSprite textureatlassprite2 = (TextureAtlasSprite)textureatlassprite0;
            if (this.isTerrainAnimationActive(textureatlassprite2)) {
                textureatlassprite2.updateAnimation();
                if (textureatlassprite2.spriteNormal != null) {
                    flag = true;
                }
                if (textureatlassprite2.spriteSpecular == null) {
                    continue;
                }
                flag2 = true;
            }
        }
        if (Config.isMultiTexture()) {
            for (final Object textureatlassprite3 : this.listAnimatedSprites) {
                final TextureAtlasSprite textureatlassprite4 = (TextureAtlasSprite)textureatlassprite3;
                if (this.isTerrainAnimationActive(textureatlassprite4)) {
                    final TextureAtlasSprite textureatlassprite5 = textureatlassprite4.spriteSingle;
                    if (textureatlassprite5 == null) {
                        continue;
                    }
                    if (textureatlassprite4 == TextureUtils.iconClock || textureatlassprite4 == TextureUtils.iconCompass) {
                        textureatlassprite5.frameCounter = textureatlassprite4.frameCounter;
                    }
                    textureatlassprite4.bindSpriteTexture();
                    textureatlassprite5.updateAnimation();
                }
            }
            TextureUtil.bindTexture(this.getGlTextureId());
        }
        if (Config.isShaders()) {
            if (flag) {
                TextureUtil.bindTexture(this.getMultiTexID().norm);
                for (final Object textureatlassprite6 : this.listAnimatedSprites) {
                    final TextureAtlasSprite textureatlassprite7 = (TextureAtlasSprite)textureatlassprite6;
                    if (textureatlassprite7.spriteNormal != null && this.isTerrainAnimationActive(textureatlassprite7)) {
                        if (textureatlassprite7 == TextureUtils.iconClock || textureatlassprite7 == TextureUtils.iconCompass) {
                            textureatlassprite7.spriteNormal.frameCounter = textureatlassprite7.frameCounter;
                        }
                        textureatlassprite7.spriteNormal.updateAnimation();
                    }
                }
            }
            if (flag2) {
                TextureUtil.bindTexture(this.getMultiTexID().spec);
                for (final Object textureatlassprite8 : this.listAnimatedSprites) {
                    final TextureAtlasSprite textureatlassprite9 = (TextureAtlasSprite)textureatlassprite8;
                    if (textureatlassprite9.spriteSpecular != null && this.isTerrainAnimationActive(textureatlassprite9)) {
                        if (textureatlassprite9 == TextureUtils.iconClock || textureatlassprite9 == TextureUtils.iconCompass) {
                            textureatlassprite9.spriteNormal.frameCounter = textureatlassprite9.frameCounter;
                        }
                        textureatlassprite9.spriteSpecular.updateAnimation();
                    }
                }
            }
            if (flag || flag2) {
                TextureUtil.bindTexture(this.getGlTextureId());
            }
        }
        if (Config.isShaders()) {
            ShadersTex.updatingTex = null;
        }
    }
    
    public TextureAtlasSprite registerSprite(final ResourceLocation location) {
        if (location == null) {
            throw new IllegalArgumentException("Location cannot be null!");
        }
        TextureAtlasSprite textureatlassprite = this.mapRegisteredSprites.get(location.toString());
        if (textureatlassprite == null) {
            textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
            this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
            if (textureatlassprite.getIndexInMap() < 0) {
                textureatlassprite.setIndexInMap(this.counterIndexInMap++);
            }
        }
        return textureatlassprite;
    }
    
    @Override
    public void tick() {
        this.updateAnimations();
    }
    
    public void setMipmapLevels(final int mipmapLevelsIn) {
        this.mipmapLevels = mipmapLevelsIn;
    }
    
    public TextureAtlasSprite getMissingSprite() {
        return this.missingImage;
    }
    
    public TextureAtlasSprite getTextureExtry(final String p_getTextureExtry_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }
    
    public boolean setTextureEntry(final String p_setTextureEntry_1_, final TextureAtlasSprite p_setTextureEntry_2_) {
        if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_)) {
            this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
            if (p_setTextureEntry_2_.getIndexInMap() < 0) {
                p_setTextureEntry_2_.setIndexInMap(this.counterIndexInMap++);
            }
            return true;
        }
        return false;
    }
    
    public boolean setTextureEntry(final TextureAtlasSprite p_setTextureEntry_1_) {
        return this.setTextureEntry(p_setTextureEntry_1_.getIconName(), p_setTextureEntry_1_);
    }
    
    public String getBasePath() {
        return this.basePath;
    }
    
    public int getMipmapLevels() {
        return this.mipmapLevels;
    }
    
    private boolean isAbsoluteLocation(final ResourceLocation p_isAbsoluteLocation_1_) {
        final String s = p_isAbsoluteLocation_1_.getResourcePath();
        return this.isAbsoluteLocationPath(s);
    }
    
    private boolean isAbsoluteLocationPath(final String p_isAbsoluteLocationPath_1_) {
        final String s = p_isAbsoluteLocationPath_1_.toLowerCase();
        return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
    }
    
    public TextureAtlasSprite getSpriteSafe(final String p_getSpriteSafe_1_) {
        final ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
        return this.mapRegisteredSprites.get(resourcelocation.toString());
    }
    
    private boolean isTerrainAnimationActive(final TextureAtlasSprite p_isTerrainAnimationActive_1_) {
        return (p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow) ? ((p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow) ? ((p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1) ? ((p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal) ? Config.isAnimatedPortal() : (p_isTerrainAnimationActive_1_ == TextureUtils.iconClock || p_isTerrainAnimationActive_1_ == TextureUtils.iconCompass || Config.isAnimatedTerrain())) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }
    
    public int getCountRegisteredSprites() {
        return this.counterIndexInMap;
    }
    
    private int detectMaxMipmapLevel(final Map p_detectMaxMipmapLevel_1_, final IResourceManager p_detectMaxMipmapLevel_2_) {
        int i = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);
        if (i < 16) {
            i = 16;
        }
        i = MathHelper.roundUpToPowerOfTwo(i);
        if (i > 16) {
            Config.log("Sprite size: " + i);
        }
        int j = MathHelper.calculateLogBaseTwo(i);
        if (j < 4) {
            j = 4;
        }
        return j;
    }
    
    private int detectMinimumSpriteSize(final Map p_detectMinimumSpriteSize_1_, final IResourceManager p_detectMinimumSpriteSize_2_, final int p_detectMinimumSpriteSize_3_) {
        final Map map = new HashMap();
        for (final Object entry : p_detectMinimumSpriteSize_1_.entrySet()) {
            final TextureAtlasSprite textureatlassprite = ((Map.Entry)entry).getValue();
            final ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            final ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, 0);
            if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) {
                try {
                    final IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation2);
                    if (iresource == null) {
                        continue;
                    }
                    final InputStream inputstream = iresource.getInputStream();
                    if (inputstream == null) {
                        continue;
                    }
                    final Dimension dimension = TextureUtils.getImageSize(inputstream, "png");
                    if (dimension == null) {
                        continue;
                    }
                    final int i = dimension.width;
                    final int j = MathHelper.roundUpToPowerOfTwo(i);
                    if (!map.containsKey(j)) {
                        map.put(j, 1);
                    }
                    else {
                        final int k = map.get(j);
                        map.put(j, k + 1);
                    }
                }
                catch (Exception ex) {}
            }
        }
        int l = 0;
        final Set set = map.keySet();
        final Set set2 = new TreeSet(set);
        for (final int j2 : set2) {
            final int l2 = map.get(j2);
            l += l2;
        }
        int i2 = 16;
        int k2 = 0;
        final int l2 = l * p_detectMinimumSpriteSize_3_ / 100;
        for (final int i3 : set2) {
            final int j3 = map.get(i3);
            k2 += j3;
            if (i3 > i2) {
                i2 = i3;
            }
            if (k2 > l2) {
                return i2;
            }
        }
        return i2;
    }
    
    private int getMinSpriteSize() {
        int i = 1 << this.mipmapLevels;
        if (i < 8) {
            i = 8;
        }
        return i;
    }
    
    private int[] getMissingImageData(final int p_getMissingImageData_1_) {
        final BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
        final BufferedImage bufferedimage2 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
        final int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
        bufferedimage2.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0, p_getMissingImageData_1_);
        return aint;
    }
    
    public boolean isTextureBound() {
        final int i = GlStateManager.getBoundTexture();
        final int j = this.getGlTextureId();
        return i == j;
    }
    
    private void updateIconGrid(final int p_updateIconGrid_1_, final int p_updateIconGrid_2_) {
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGrid = null;
        if (this.iconGridSize > 0) {
            this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
            this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
            this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
            this.iconGridSizeU = 1.0 / this.iconGridCountX;
            this.iconGridSizeV = 1.0 / this.iconGridCountY;
            for (final Object textureatlassprite0 : this.mapUploadedSprites.values()) {
                final TextureAtlasSprite textureatlassprite2 = (TextureAtlasSprite)textureatlassprite0;
                final double d0 = 0.5 / p_updateIconGrid_1_;
                final double d2 = 0.5 / p_updateIconGrid_2_;
                final double d3 = Math.min(textureatlassprite2.getMinU(), textureatlassprite2.getMaxU()) + d0;
                final double d4 = Math.min(textureatlassprite2.getMinV(), textureatlassprite2.getMaxV()) + d2;
                final double d5 = Math.max(textureatlassprite2.getMinU(), textureatlassprite2.getMaxU()) - d0;
                final double d6 = Math.max(textureatlassprite2.getMinV(), textureatlassprite2.getMaxV()) - d2;
                final int i = (int)(d3 / this.iconGridSizeU);
                final int j = (int)(d4 / this.iconGridSizeV);
                final int k = (int)(d5 / this.iconGridSizeU);
                final int l = (int)(d6 / this.iconGridSizeV);
                for (int i2 = i; i2 <= k; ++i2) {
                    if (i2 >= 0 && i2 < this.iconGridCountX) {
                        for (int j2 = j; j2 <= l; ++j2) {
                            if (j2 >= 0 && j2 < this.iconGridCountX) {
                                final int k2 = j2 * this.iconGridCountX + i2;
                                this.iconGrid[k2] = textureatlassprite2;
                            }
                            else {
                                Config.warn("Invalid grid V: " + j2 + ", icon: " + textureatlassprite2.getIconName());
                            }
                        }
                    }
                    else {
                        Config.warn("Invalid grid U: " + i2 + ", icon: " + textureatlassprite2.getIconName());
                    }
                }
            }
        }
    }
    
    public TextureAtlasSprite getIconByUV(final double p_getIconByUV_1_, final double p_getIconByUV_3_) {
        if (this.iconGrid == null) {
            return null;
        }
        final int i = (int)(p_getIconByUV_1_ / this.iconGridSizeU);
        final int j = (int)(p_getIconByUV_3_ / this.iconGridSizeV);
        final int k = j * this.iconGridCountX + i;
        return (k >= 0 && k <= this.iconGrid.length) ? this.iconGrid[k] : null;
    }
}
