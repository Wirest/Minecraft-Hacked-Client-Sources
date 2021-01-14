package net.minecraft.client.renderer.texture;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.Map.Entry;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import optifine.BetterGrass;
import optifine.Config;
import optifine.ConnectedTextures;
import optifine.CounterInt;
import optifine.CustomItems;
import optifine.Reflector;
import optifine.ReflectorForge;
import optifine.TextureUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import shadersmod.client.ShadersTex;

/**
 * 操你妈FernFlower 为什么反编译不出来好似一个畏畏缩缩窝囊废是不是你啊
 * 
 * 你爹我废了这么大劲才修好了你这个破材质难道你一点也不感到羞愧吗？
 */
public class TextureMap extends AbstractTexture implements ITickableTextureObject
{
    private static final boolean ENABLE_SKIP = Boolean.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
    private static final Logger logger = LogManager.getLogger();
    public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
    public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
    private final List<TextureAtlasSprite> listAnimatedSprites;
    private final Map<String, TextureAtlasSprite> mapRegisteredSprites;
    private final Map<String, TextureAtlasSprite> mapUploadedSprites;
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
    private CounterInt counterIndexInMap;
    public int atlasWidth;
    public int atlasHeight;

    public TextureMap(String p_i46099_1_)
    {
        this(p_i46099_1_, (IIconCreator)null);
    }

    public TextureMap(String p_i17_1_, boolean p_i17_2_)
    {
        this(p_i17_1_, (IIconCreator)null, p_i17_2_);
    }

    public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn)
    {
        this(p_i46100_1_, iconCreatorIn, false);
    }

    public TextureMap(String p_i18_1_, IIconCreator p_i18_2_, boolean p_i18_3_)
    {
        this.skipFirst = false;
        this.iconGrid = null;
        this.iconGridSize = -1;
        this.iconGridCountX = -1;
        this.iconGridCountY = -1;
        this.iconGridSizeU = -1.0D;
        this.iconGridSizeV = -1.0D;
        this.counterIndexInMap = new CounterInt(0);
        this.atlasWidth = 0;
        this.atlasHeight = 0;
        this.listAnimatedSprites = Lists.newArrayList();
        this.mapRegisteredSprites = Maps.newHashMap();
        this.mapUploadedSprites = Maps.newHashMap();
        this.missingImage = new TextureAtlasSprite("missingno");
        this.basePath = p_i18_1_;
        this.iconCreator = p_i18_2_;
        this.skipFirst = p_i18_3_ && ENABLE_SKIP;
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
        this.missingImage.setIndexInMap(this.counterIndexInMap.nextValue());
    }

    public void loadTexture(IResourceManager resourceManager) throws IOException
    {
        ShadersTex.resManager = resourceManager;

        if (this.iconCreator != null)
        {
            this.loadSprites(resourceManager, this.iconCreator);
        }
    }

    public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_)
    {
        this.mapRegisteredSprites.clear();
        this.counterIndexInMap.reset();
        p_174943_2_.registerSprites(this);

        if (this.mipmapLevels >= 4)
        {
            this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
            Config.log("Mipmap levels: " + this.mipmapLevels);
        }

        this.initMissingImage();
        this.deleteGlTexture();
        this.loadTextureAtlas(resourceManager);
    }

    public void loadTextureAtlas(IResourceManager p_110571_1_) {
        ShadersTex.resManager = p_110571_1_;
        Config.dbg("Multitexture: " + Config.isMultiTexture());
        if (Config.isMultiTexture()) {
           Iterator it = this.mapUploadedSprites.values().iterator();

           while(it.hasNext()) {
              TextureAtlasSprite ts = (TextureAtlasSprite)it.next();
              ts.deleteSpriteTexture();
           }
        }

        ConnectedTextures.updateIcons(this);
        CustomItems.updateIcons(this);
        BetterGrass.updateIcons(this);
        int var2 = TextureUtils.getGLMaximumTextureSize();
        Stitcher var3 = new Stitcher(var2, var2, true, 0, this.mipmapLevels);
        this.mapUploadedSprites.clear();
        this.listAnimatedSprites.clear();
        int var4 = Integer.MAX_VALUE;
        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPre, new Object[]{this});
        int minSpriteSize = this.getMinSpriteSize();
        this.iconGridSize = minSpriteSize;
        int var5 = 1 << this.mipmapLevels;
        Iterator var6 = this.mapRegisteredSprites.entrySet().iterator();

        int sheetHeight;
        List listSprites;
        while(var6.hasNext() && !this.skipFirst) {
           Entry var7 = (Entry)var6.next();
           TextureAtlasSprite var8 = (TextureAtlasSprite)var7.getValue();
           ResourceLocation var9 = new ResourceLocation(var8.getIconName());
           ResourceLocation var10 = this.completeResourceLocation(var9, 0);
           var8.updateIndexInMap(this.counterIndexInMap);
           if (var8.hasCustomLoader(p_110571_1_, var9)) {
              if (!var8.load(p_110571_1_, var9)) {
                 var4 = Math.min(var4, Math.min(var8.getIconWidth(), var8.getIconHeight()));
                 var3.addSprite(var8);
              }

              Config.dbg("Custom loader: " + var8);
           } else {
              try {
                 IResource var11 = ShadersTex.loadResource(p_110571_1_, var10);
                 BufferedImage[] var12 = new BufferedImage[1 + this.mipmapLevels];
                 var12[0] = TextureUtil.readBufferedImage(var11.getInputStream());
                 if (var12 != null) {
                    sheetHeight = var12[0].getWidth();
                    if (sheetHeight < minSpriteSize || this.mipmapLevels > 0) {
                       var12[0] = this.mipmapLevels > 0 ? TextureUtils.scaleToPowerOfTwo(var12[0], minSpriteSize) : TextureUtils.scaleMinTo(var12[0], minSpriteSize);
                       int ws2 = var12[0].getWidth();
                       if (ws2 != sheetHeight) {
                          if (!TextureUtils.isPowerOfTwo(sheetHeight)) {
                             Config.log("Scaled non power of 2: " + var8.getIconName() + ", " + sheetHeight + " -> " + ws2);
                          } else {
                             Config.log("Scaled too small texture: " + var8.getIconName() + ", " + sheetHeight + " -> " + ws2);
                          }
                       }
                    }
                 }

                 TextureMetadataSection var13 = (TextureMetadataSection)var11.getMetadata("texture");
                 if (var13 != null) {
                    listSprites = var13.getListMipmaps();
                    int var16;
                    if (!listSprites.isEmpty()) {
                       int var15 = var12[0].getWidth();
                       var16 = var12[0].getHeight();
                       if (MathHelper.roundUpToPowerOfTwo(var15) != var15 || MathHelper.roundUpToPowerOfTwo(var16) != var16) {
                          throw new RuntimeException("Unable to load extra miplevels, source-texture is not power of two");
                       }
                    }

                    Iterator var39 = listSprites.iterator();

                    while(var39.hasNext()) {
                       var16 = ((Integer)var39.next()).intValue();
                       if (var16 > 0 && var16 < var12.length - 1 && var12[var16] == null) {
                          ResourceLocation var17 = this.completeResourceLocation(var9, var16);

                          try {
                             var12[var16] = TextureUtil.readBufferedImage(ShadersTex.loadResource(p_110571_1_, var17).getInputStream());
                          } catch (IOException var25) {
                             logger.error("Unable to load miplevel {} from: {}", new Object[]{var16, var17, var25});
                          }
                       }
                    }
                 }

                 AnimationMetadataSection var37 = (AnimationMetadataSection)var11.getMetadata("animation");
                 var8.loadSprite(var12, var37);
              } catch (RuntimeException var26) {
                 logger.error("Unable to parse metadata from " + var10, var26);
                 ReflectorForge.FMLClientHandler_trackBrokenTexture(var10, var26.getMessage());
                 continue;
              } catch (IOException var27) {
                 logger.error("Using missing texture, unable to load " + var10 + ", " + var27.getClass().getName());
                 ReflectorForge.FMLClientHandler_trackMissingTexture(var10);
                 continue;
              }

              var4 = Math.min(var4, Math.min(var8.getIconWidth(), var8.getIconHeight()));
              int var32 = Math.min(Integer.lowestOneBit(var8.getIconWidth()), Integer.lowestOneBit(var8.getIconHeight()));
              if (var32 < var5) {
                 logger.warn("Texture {} with size {}x{} limits mip level from {} to {}", new Object[]{var10, var8.getIconWidth(), var8.getIconHeight(), MathHelper.calculateLogBaseTwo(var5), MathHelper.calculateLogBaseTwo(var32)});
                 var5 = var32;
              }

              var3.addSprite(var8);
           }
        }

        int var25 = Math.min(var4, var5);
        int var26 = MathHelper.calculateLogBaseTwo(var25);
        if (var26 < 0) {
           var26 = 0;
        }

        if (var26 < this.mipmapLevels) {
           logger.info("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[]{this.basePath, this.mipmapLevels, var26, var25});
           this.mipmapLevels = var26;
        }

        Iterator var27 = this.mapRegisteredSprites.values().iterator();

        while(var27.hasNext() && !this.skipFirst) {
           final TextureAtlasSprite var29 = (TextureAtlasSprite)var27.next();

           try {
              var29.generateMipmaps(this.mipmapLevels);
           } catch (Throwable throwable1) {
        	   CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
               CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
               crashreportcategory.addCrashSectionCallable("Sprite name", new Callable<String>()
               {
                   private static final String __OBFID = "CL_00001059";
                   public String call() throws Exception
                   {
                       return var29.getIconName();
                   }
               });
               crashreportcategory.addCrashSectionCallable("Sprite size", new Callable()
               {
                   private static final String __OBFID = "CL_00001060";
                   public String call() throws Exception
                   {
                       return var29.getIconWidth() + " x " + var29.getIconHeight();
                   }
               });
               crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable()
               {
                   private static final String __OBFID = "CL_00001061";
                   public String call() throws Exception
                   {
                       return var29.getFrameCount() + " frames";
                   }
               });
               crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
               throw new ReportedException(crashreport);
           }
        }

        this.missingImage.generateMipmaps(this.mipmapLevels);
        var3.addSprite(this.missingImage);
        this.skipFirst = false;

        try {
           var3.doStitch();
        } catch (StitcherException var23) {
           throw var23;
        }

        logger.info("Created: {}x{} {}-atlas", new Object[]{var3.getCurrentWidth(), var3.getCurrentHeight(), this.basePath});
        if (Config.isShaders()) {
           ShadersTex.allocateTextureMap(this.getGlTextureId(), this.mipmapLevels, var3.getCurrentWidth(), var3.getCurrentHeight(), var3, this);
        } else {
           TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, var3.getCurrentWidth(), var3.getCurrentHeight());
        }

        HashMap var28 = Maps.newHashMap(this.mapRegisteredSprites);
        Iterator var30 = var3.getStichSlots().iterator();

        TextureAtlasSprite var31;
        while(var30.hasNext()) {
           var31 = (TextureAtlasSprite)var30.next();
           if (Config.isShaders()) {
              ShadersTex.setIconName(ShadersTex.setSprite(var31).getIconName());
           }

           String var34 = var31.getIconName();
           var28.remove(var34);
           this.mapUploadedSprites.put(var34, var31);

           try {
              if (Config.isShaders()) {
                 ShadersTex.uploadTexSubForLoadAtlas(var31.getFrameTextureData(0), var31.getIconWidth(), var31.getIconHeight(), var31.getOriginX(), var31.getOriginY(), false, false);
              } else {
                 TextureUtil.uploadTextureMipmap(var31.getFrameTextureData(0), var31.getIconWidth(), var31.getIconHeight(), var31.getOriginX(), var31.getOriginY(), false, false);
              }
           } catch (Throwable var22) {
              CrashReport var36 = CrashReport.makeCrashReport(var22, "Stitching texture atlas");
              CrashReportCategory var38 = var36.makeCategory("Texture being stitched together");
              var38.addCrashSection("Atlas path", this.basePath);
              var38.addCrashSection("Sprite", var31);
              throw new ReportedException(var36);
           }

           if (var31.hasAnimationMetadata()) {
              this.listAnimatedSprites.add(var31);
           }
        }

        var30 = var28.values().iterator();

        while(var30.hasNext()) {
           var31 = (TextureAtlasSprite)var30.next();
           var31.copyFrom(this.missingImage);
        }

        if (Config.isMultiTexture()) {
           int sheetWidth = var3.getCurrentWidth();
           sheetHeight = var3.getCurrentHeight();
           listSprites = var3.getStichSlots();
           Iterator it = listSprites.iterator();

           while(it.hasNext()) {
              TextureAtlasSprite tas = (TextureAtlasSprite)it.next();
              tas.sheetWidth = sheetWidth;
              tas.sheetHeight = sheetHeight;
              tas.mipmapLevels = this.mipmapLevels;
              TextureAtlasSprite ss = tas.spriteSingle;
              if (ss != null) {
                 ss.sheetWidth = sheetWidth;
                 ss.sheetHeight = sheetHeight;
                 ss.mipmapLevels = this.mipmapLevels;
                 tas.bindSpriteTexture();
                 boolean texBlur = false;
                 boolean texClamp = true;
                 TextureUtil.uploadTextureMipmap(ss.getFrameTextureData(0), ss.getIconWidth(), ss.getIconHeight(), ss.getOriginX(), ss.getOriginY(), texBlur, texClamp);
              }
           }

           Config.getMinecraft().getTextureManager().bindTexture(locationBlocksTexture);
        }

        Reflector.callVoid(Reflector.ForgeHooksClient_onTextureStitchedPost, new Object[]{this});
        this.updateIconGrid(var3.getCurrentWidth(), var3.getCurrentHeight());
        if (Config.equals(System.getProperty("saveTextureMap"), "true")) {
           Config.dbg("Exporting texture map to: " + this.basePath + "_x.png");
           TextureUtil.saveGlTexture(this.basePath.replaceAll("/", "_"), this.getGlTextureId(), this.mipmapLevels, var3.getCurrentWidth(), var3.getCurrentHeight());
        }

     }



    public ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_)
    {
        return this.isAbsoluteLocation(location) ? (p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png") : new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + "mipmap" + p_147634_2_ + ".png")) : (p_147634_2_ == 0 ? new ResourceLocation(location.getResourceDomain(), String.format("%s/%s%s", new Object[] {this.basePath, location.getResourcePath(), ".png"})): new ResourceLocation(location.getResourceDomain(), String.format("%s/mipmaps/%s.%d%s", new Object[] {this.basePath, location.getResourcePath(), Integer.valueOf(p_147634_2_), ".png"})));
    }

    public TextureAtlasSprite getAtlasSprite(String iconName)
    {
        TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapUploadedSprites.get(iconName);

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
            for (TextureAtlasSprite textureatlassprite1 : this.listAnimatedSprites)
            {
                if (this.isTerrainAnimationActive(textureatlassprite1))
                {
                    TextureAtlasSprite textureatlassprite2 = textureatlassprite1.spriteSingle;

                    if (textureatlassprite2 != null)
                    {
                        if (textureatlassprite1 == TextureUtils.iconClock || textureatlassprite1 == TextureUtils.iconCompass)
                        {
                            textureatlassprite2.frameCounter = textureatlassprite1.frameCounter;
                        }

                        textureatlassprite1.bindSpriteTexture();
                        textureatlassprite2.updateAnimation();
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
            TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)this.mapRegisteredSprites.get(location.toString());

            if (textureatlassprite == null)
            {
                textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
                this.mapRegisteredSprites.put(location.toString(), textureatlassprite);
                textureatlassprite.updateIndexInMap(this.counterIndexInMap);
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

    public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_)
    {
        ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_)
    {
        if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_))
        {
            this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);
            p_setTextureEntry_2_.updateIndexInMap(this.counterIndexInMap);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean setTextureEntry(TextureAtlasSprite p_setTextureEntry_1_)
    {
        return this.setTextureEntry(p_setTextureEntry_1_.getIconName(), p_setTextureEntry_1_);
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
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(resourcelocation.toString());
    }

    public TextureAtlasSprite getRegisteredSprite(ResourceLocation p_getRegisteredSprite_1_)
    {
        return (TextureAtlasSprite)this.mapRegisteredSprites.get(p_getRegisteredSprite_1_.toString());
    }

    private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_)
    {
        return p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow ? (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill && p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow ? (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0 && p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1 ? (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal ? Config.isAnimatedPortal() : (p_isTerrainAnimationActive_1_ != TextureUtils.iconClock && p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass ? Config.isAnimatedTerrain() : true)) : Config.isAnimatedFire()) : Config.isAnimatedLava()) : Config.isAnimatedWater();
    }

    public int getCountRegisteredSprites()
    {
        return this.counterIndexInMap.getValue();
    }

    private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_)
    {
        int i = this.detectMinimumSpriteSize(p_detectMaxMipmapLevel_1_, p_detectMaxMipmapLevel_2_, 20);

        if (i < 16)
        {
            i = 16;
        }

        i = MathHelper.roundUpToPowerOfTwo(i);

        if (i > 16)
        {
            Config.log("Sprite size: " + i);
        }

        int j = MathHelper.calculateLogBaseTwo(i);

        if (j < 4)
        {
            j = 4;
        }

        return j;
    }

    private int detectMinimumSpriteSize(Map<String, TextureAtlasSprite> p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_, int p_detectMinimumSpriteSize_3_)
    {
        Map<Integer , Integer> map = new HashMap<Integer , Integer>();

        for (Entry<String, TextureAtlasSprite> entry : p_detectMinimumSpriteSize_1_.entrySet())
        {
            TextureAtlasSprite textureatlassprite = (TextureAtlasSprite)entry.getValue();
            ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
            ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

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
                                int j = MathHelper.roundUpToPowerOfTwo(i);

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
        bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
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
