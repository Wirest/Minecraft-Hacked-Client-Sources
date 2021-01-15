package net.minecraft.client.renderer.texture;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.StitcherException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.AnimationMetadataSection;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.src.Config;
import net.minecraft.src.TextureUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import shadersmod.client.ShadersTex;

public class TextureMap extends AbstractTexture implements ITickableTextureObject {
	private static final Logger logger = LogManager.getLogger();
	public static final ResourceLocation LOCATION_MISSING_TEXTURE = new ResourceLocation("missingno");
	public static final ResourceLocation locationBlocksTexture = new ResourceLocation("textures/atlas/blocks.png");
	private final List<TextureAtlasSprite> listAnimatedSprites;
	private final Map mapRegisteredSprites;
	private final Map mapUploadedSprites;
	private final String basePath;
	private final IIconCreator iconCreator;
	private int mipmapLevels;
	private final TextureAtlasSprite missingImage;
	private static final String __OBFID = "CL_00001058";
	private TextureAtlasSprite[] iconGrid;
	private int iconGridSize;
	private int iconGridCountX;
	private int iconGridCountY;
	private double iconGridSizeU;
	private double iconGridSizeV;
	private static final boolean ENABLE_SKIP = Boolean
			.parseBoolean(System.getProperty("fml.skipFirstTextureLoad", "true"));
	private boolean skipFirst;
	public int atlasWidth;
	public int atlasHeight;

	public TextureMap(String p_i46099_1_) {
		this(p_i46099_1_, (IIconCreator) null);
	}

	public TextureMap(String p_i10_1_, boolean p_i10_2_) {
		this(p_i10_1_, (IIconCreator) null, p_i10_2_);
	}

	public TextureMap(String p_i46100_1_, IIconCreator iconCreatorIn) {
		this(p_i46100_1_, iconCreatorIn, false);
	}

	public TextureMap(String p_i11_1_, IIconCreator p_i11_2_, boolean p_i11_3_) {
		this.iconGrid = null;
		this.iconGridSize = -1;
		this.iconGridCountX = -1;
		this.iconGridCountY = -1;
		this.iconGridSizeU = -1.0D;
		this.iconGridSizeV = -1.0D;
		this.skipFirst = false;
		this.atlasWidth = 0;
		this.atlasHeight = 0;
		this.listAnimatedSprites = Lists.newArrayList();
		this.mapRegisteredSprites = Maps.newHashMap();
		this.mapUploadedSprites = Maps.newHashMap();
		this.missingImage = new TextureAtlasSprite("missingno");
		this.basePath = p_i11_1_;
		this.iconCreator = p_i11_2_;
		this.skipFirst = p_i11_3_ && ENABLE_SKIP;
	}

	private void initMissingImage() {
		int i = this.getMinSpriteSize();
		int[] aint = this.getMissingImageData(i);
		this.missingImage.setIconWidth(i);
		this.missingImage.setIconHeight(i);
		int[][] aint1 = new int[this.mipmapLevels + 1][];
		aint1[0] = aint;
		this.missingImage.setFramesTextureData(Lists.newArrayList(new int[][][] { aint1 }));
		this.missingImage.setIndexInMap(0);
	}

	public void loadTexture(IResourceManager resourceManager) throws IOException {
		ShadersTex.resManager = resourceManager;

		if (this.iconCreator != null) {
			this.loadSprites(resourceManager, this.iconCreator);
		}
	}

	public void loadSprites(IResourceManager resourceManager, IIconCreator p_174943_2_) {
		this.mapRegisteredSprites.clear();
		p_174943_2_.registerSprites(this);

		if (this.mipmapLevels >= 4) {
			this.mipmapLevels = this.detectMaxMipmapLevel(this.mapRegisteredSprites, resourceManager);
			Config.log("Mipmap levels: " + this.mipmapLevels);
		}

		this.initMissingImage();
		this.deleteGlTexture();
		this.loadTextureAtlas(resourceManager);
	}

	public void loadTextureAtlas(IResourceManager resourceManager) {
		int i = Minecraft.getGLMaximumTextureSize();
		Stitcher stitcher = new Stitcher(i, i, true, 0, this.mipmapLevels);
		this.mapUploadedSprites.clear();
		this.listAnimatedSprites.clear();
		int j = Integer.MAX_VALUE;
		int k = 1 << this.mipmapLevels;

		for (Object o : this.mapRegisteredSprites.entrySet()) {
			Entry<String, TextureAtlasSprite> entry = (Entry<String, TextureAtlasSprite>) o;
			TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) entry.getValue();
			ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
			ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

			try {
				IResource iresource = resourceManager.getResource(resourcelocation1);
				BufferedImage[] abufferedimage = new BufferedImage[1 + this.mipmapLevels];
				abufferedimage[0] = TextureUtil.readBufferedImage(iresource.getInputStream());
				TextureMetadataSection texturemetadatasection = (TextureMetadataSection) iresource
						.getMetadata("texture");

				if (texturemetadatasection != null) {
					List<Integer> list = texturemetadatasection.getListMipmaps();

					if (!list.isEmpty()) {
						int l = abufferedimage[0].getWidth();
						int i1 = abufferedimage[0].getHeight();

						if (MathHelper.roundUpToPowerOfTwo(l) != l || MathHelper.roundUpToPowerOfTwo(i1) != i1) {
							throw new RuntimeException(
									"Unable to load extra miplevels, source-texture is not power of two");
						}
					}

					Iterator iterator = list.iterator();

					while (iterator.hasNext()) {
						int i2 = ((Integer) iterator.next()).intValue();

						if (i2 > 0 && i2 < abufferedimage.length - 1 && abufferedimage[i2] == null) {
							ResourceLocation resourcelocation2 = this.completeResourceLocation(resourcelocation, i2);

							try {
								abufferedimage[i2] = TextureUtil.readBufferedImage(
										resourceManager.getResource(resourcelocation2).getInputStream());
							} catch (IOException ioexception) {
								logger.error("Unable to load miplevel {} from: {}",
										new Object[] { Integer.valueOf(i2), resourcelocation2, ioexception });
							}
						}
					}
				}

				AnimationMetadataSection animationmetadatasection = (AnimationMetadataSection) iresource
						.getMetadata("animation");
				textureatlassprite.loadSprite(abufferedimage, animationmetadatasection);
			} catch (RuntimeException runtimeexception) {
				logger.error((String) ("Unable to parse metadata from " + resourcelocation1),
						(Throwable) runtimeexception);
				continue;
			} catch (IOException ioexception1) {
				logger.error((String) ("Using missing texture, unable to load " + resourcelocation1),
						(Throwable) ioexception1);
				continue;
			}

			j = Math.min(j, Math.min(textureatlassprite.getIconWidth(), textureatlassprite.getIconHeight()));
			int l1 = Math.min(Integer.lowestOneBit(textureatlassprite.getIconWidth()),
					Integer.lowestOneBit(textureatlassprite.getIconHeight()));

			if (l1 < k) {
				logger.warn("Texture {} with size {}x{} limits mip level from {} to {}",
						new Object[] { resourcelocation1, Integer.valueOf(textureatlassprite.getIconWidth()),
								Integer.valueOf(textureatlassprite.getIconHeight()),
								Integer.valueOf(MathHelper.calculateLogBaseTwo(k)),
								Integer.valueOf(MathHelper.calculateLogBaseTwo(l1)) });
				k = l1;
			}

			stitcher.addSprite(textureatlassprite);
		}

		int j1 = Math.min(j, k);
		int k1 = MathHelper.calculateLogBaseTwo(j1);

		if (k1 < this.mipmapLevels) {
			logger.warn("{}: dropping miplevel from {} to {}, because of minimum power of two: {}", new Object[] {
					this.basePath, Integer.valueOf(this.mipmapLevels), Integer.valueOf(k1), Integer.valueOf(j1) });
			this.mipmapLevels = k1;
		}

		for (Object o : this.mapRegisteredSprites.values()) {
			final TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite) o;
			try {
				textureatlassprite1.generateMipmaps(this.mipmapLevels);
			} catch (Throwable throwable1) {
				CrashReport crashreport = CrashReport.makeCrashReport(throwable1, "Applying mipmap");
				CrashReportCategory crashreportcategory = crashreport.makeCategory("Sprite being mipmapped");
				crashreportcategory.addCrashSectionCallable("Sprite name", new Callable<String>() {
					public String call() throws Exception {
						return textureatlassprite1.getIconName();
					}
				});
				crashreportcategory.addCrashSectionCallable("Sprite size", new Callable<String>() {
					public String call() throws Exception {
						return textureatlassprite1.getIconWidth() + " x " + textureatlassprite1.getIconHeight();
					}
				});
				crashreportcategory.addCrashSectionCallable("Sprite frames", new Callable<String>() {
					public String call() throws Exception {
						return textureatlassprite1.getFrameCount() + " frames";
					}
				});
				crashreportcategory.addCrashSection("Mipmap levels", Integer.valueOf(this.mipmapLevels));
				throw new ReportedException(crashreport);
			}
		}

		this.missingImage.generateMipmaps(this.mipmapLevels);
		stitcher.addSprite(this.missingImage);

		try {
			stitcher.doStitch();
		} catch (StitcherException stitcherexception) {
			throw stitcherexception;
		}

		logger.info("Created: {}x{} {}-atlas", new Object[] { Integer.valueOf(stitcher.getCurrentWidth()),
				Integer.valueOf(stitcher.getCurrentHeight()), this.basePath });
		TextureUtil.allocateTextureImpl(this.getGlTextureId(), this.mipmapLevels, stitcher.getCurrentWidth(),
				stitcher.getCurrentHeight());
		Map<String, TextureAtlasSprite> map = Maps.<String, TextureAtlasSprite>newHashMap(this.mapRegisteredSprites);

		for (TextureAtlasSprite textureatlassprite2 : stitcher.getStichSlots()) {
			String s = textureatlassprite2.getIconName();
			map.remove(s);
			this.mapUploadedSprites.put(s, textureatlassprite2);

			try {
				TextureUtil.uploadTextureMipmap(textureatlassprite2.getFrameTextureData(0),
						textureatlassprite2.getIconWidth(), textureatlassprite2.getIconHeight(),
						textureatlassprite2.getOriginX(), textureatlassprite2.getOriginY(), false, false);
			} catch (Throwable throwable) {
				CrashReport crashreport1 = CrashReport.makeCrashReport(throwable, "Stitching texture atlas");
				CrashReportCategory crashreportcategory1 = crashreport1.makeCategory("Texture being stitched together");
				crashreportcategory1.addCrashSection("Atlas path", this.basePath);
				crashreportcategory1.addCrashSection("Sprite", textureatlassprite2);
				throw new ReportedException(crashreport1);
			}

			if (textureatlassprite2.hasAnimationMetadata()) {
				this.listAnimatedSprites.add(textureatlassprite2);
			}
		}

		for (TextureAtlasSprite textureatlassprite3 : map.values()) {
			textureatlassprite3.copyFrom(this.missingImage);
		}
	}

	public ResourceLocation completeResourceLocation(ResourceLocation location, int p_147634_2_) {
		return this.isAbsoluteLocation(location)
				? (p_147634_2_ == 0
						? new ResourceLocation(location.getResourceDomain(), location.getResourcePath() + ".png")
						: new ResourceLocation(location.getResourceDomain(),
								location.getResourcePath() + "mipmap" + p_147634_2_ + ".png"))
				: (p_147634_2_ == 0
						? new ResourceLocation(location.getResourceDomain(),
								String.format("%s/%s%s",
										new Object[] { this.basePath, location.getResourcePath(), ".png" }))
						: new ResourceLocation(location.getResourceDomain(),
								String.format("%s/mipmaps/%s.%d%s", new Object[] { this.basePath,
										location.getResourcePath(), Integer.valueOf(p_147634_2_), ".png" })));
	}

	public TextureAtlasSprite getAtlasSprite(String iconName) {
		TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) this.mapUploadedSprites.get(iconName);

		if (textureatlassprite == null) {
			textureatlassprite = this.missingImage;
		}

		return textureatlassprite;
	}

	public void updateAnimations() {
		if (Config.isShaders()) {
			ShadersTex.updatingTex = this.getMultiTexID();
		}

		TextureUtil.bindTexture(this.getGlTextureId());

		for (TextureAtlasSprite textureatlassprite : this.listAnimatedSprites) {
			if (this.isTerrainAnimationActive(textureatlassprite)) {
				textureatlassprite.updateAnimation();
			}
		}

		if (Config.isMultiTexture()) {
			for (TextureAtlasSprite textureatlassprite1 : this.listAnimatedSprites) {
				if (this.isTerrainAnimationActive(textureatlassprite1)) {
					TextureAtlasSprite textureatlassprite2 = textureatlassprite1.spriteSingle;

					if (textureatlassprite2 != null) {
						if (textureatlassprite1 == TextureUtils.iconClock
								|| textureatlassprite1 == TextureUtils.iconCompass) {
							textureatlassprite2.frameCounter = textureatlassprite1.frameCounter;
						}

						textureatlassprite1.bindSpriteTexture();
						textureatlassprite2.updateAnimation();
					}
				}
			}

			TextureUtil.bindTexture(this.getGlTextureId());
		}

		if (Config.isShaders()) {
			ShadersTex.updatingTex = null;
		}
	}

	public TextureAtlasSprite registerSprite(ResourceLocation location) {
		if (location == null) {
			throw new IllegalArgumentException("Location cannot be null!");
		} else {
			TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) this.mapRegisteredSprites
					.get(location.toString());

			if (textureatlassprite == null) {
				textureatlassprite = TextureAtlasSprite.makeAtlasSprite(location);
				this.mapRegisteredSprites.put(location.toString(), textureatlassprite);

				if (textureatlassprite instanceof TextureAtlasSprite && textureatlassprite.getIndexInMap() < 0) {
					textureatlassprite.setIndexInMap(this.mapRegisteredSprites.size());
				}
			}

			return textureatlassprite;
		}
	}

	public void tick() {
		this.updateAnimations();
	}

	public void setMipmapLevels(int mipmapLevelsIn) {
		this.mipmapLevels = mipmapLevelsIn;
	}

	public TextureAtlasSprite getMissingSprite() {
		return this.missingImage;
	}

	public TextureAtlasSprite getTextureExtry(String p_getTextureExtry_1_) {
		ResourceLocation resourcelocation = new ResourceLocation(p_getTextureExtry_1_);
		return (TextureAtlasSprite) this.mapRegisteredSprites.get(resourcelocation.toString());
	}

	public boolean setTextureEntry(String p_setTextureEntry_1_, TextureAtlasSprite p_setTextureEntry_2_) {
		if (!this.mapRegisteredSprites.containsKey(p_setTextureEntry_1_)) {
			this.mapRegisteredSprites.put(p_setTextureEntry_1_, p_setTextureEntry_2_);

			if (p_setTextureEntry_2_.getIndexInMap() < 0) {
				p_setTextureEntry_2_.setIndexInMap(this.mapRegisteredSprites.size());
			}

			return true;
		} else {
			return false;
		}
	}

	private boolean isAbsoluteLocation(ResourceLocation p_isAbsoluteLocation_1_) {
		String s = p_isAbsoluteLocation_1_.getResourcePath();
		return this.isAbsoluteLocationPath(s);
	}

	private boolean isAbsoluteLocationPath(String p_isAbsoluteLocationPath_1_) {
		String s = p_isAbsoluteLocationPath_1_.toLowerCase();
		return s.startsWith("mcpatcher/") || s.startsWith("optifine/");
	}

	public TextureAtlasSprite getSpriteSafe(String p_getSpriteSafe_1_) {
		ResourceLocation resourcelocation = new ResourceLocation(p_getSpriteSafe_1_);
		return (TextureAtlasSprite) this.mapRegisteredSprites.get(resourcelocation.toString());
	}

	private boolean isTerrainAnimationActive(TextureAtlasSprite p_isTerrainAnimationActive_1_) {
		return p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterStill
				&& p_isTerrainAnimationActive_1_ != TextureUtils.iconWaterFlow
						? (p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaStill
								&& p_isTerrainAnimationActive_1_ != TextureUtils.iconLavaFlow
										? (p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer0
												&& p_isTerrainAnimationActive_1_ != TextureUtils.iconFireLayer1
														? (p_isTerrainAnimationActive_1_ == TextureUtils.iconPortal
																? Config.isAnimatedPortal()
																: (p_isTerrainAnimationActive_1_ != TextureUtils.iconClock
																		&& p_isTerrainAnimationActive_1_ != TextureUtils.iconCompass
																				? Config.isAnimatedTerrain() : true))
														: Config.isAnimatedFire())
										: Config.isAnimatedLava())
						: Config.isAnimatedWater();
	}

	public int getCountRegisteredSprites() {
		return this.mapRegisteredSprites.size();
	}

	private int detectMaxMipmapLevel(Map p_detectMaxMipmapLevel_1_, IResourceManager p_detectMaxMipmapLevel_2_) {
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

	private int detectMinimumSpriteSize(Map p_detectMinimumSpriteSize_1_, IResourceManager p_detectMinimumSpriteSize_2_,
			int p_detectMinimumSpriteSize_3_) {
		Map map = new HashMap();

		for (Object o : p_detectMinimumSpriteSize_1_.entrySet()) {
			Entry entry = (Entry) o;
			TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) entry.getValue();
			ResourceLocation resourcelocation = new ResourceLocation(textureatlassprite.getIconName());
			ResourceLocation resourcelocation1 = this.completeResourceLocation(resourcelocation, 0);

			if (!textureatlassprite.hasCustomLoader(p_detectMinimumSpriteSize_2_, resourcelocation)) {
				try {
					IResource iresource = p_detectMinimumSpriteSize_2_.getResource(resourcelocation1);

					if (iresource != null) {
						InputStream inputstream = iresource.getInputStream();

						if (inputstream != null) {
							Dimension dimension = TextureUtils.getImageSize(inputstream, "png");

							if (dimension != null) {
								int i = dimension.width;
								int j = MathHelper.roundUpToPowerOfTwo(i);

								if (!map.containsKey(Integer.valueOf(j))) {
									map.put(Integer.valueOf(j), Integer.valueOf(1));
								} else {
									int k = ((Integer) map.get(Integer.valueOf(j))).intValue();
									map.put(Integer.valueOf(j), Integer.valueOf(k + 1));
								}
							}
						}
					}
				} catch (Exception var17) {
					;
				}
			}
		}

		int l = 0;
		Set set = map.keySet();
		Set set1 = new TreeSet(set);
		int l1;

		for (Iterator iterator = set1.iterator(); iterator.hasNext(); l += l1) {
			int j1 = ((Integer) iterator.next()).intValue();
			l1 = ((Integer) map.get(Integer.valueOf(j1))).intValue();
		}

		int i1 = 16;
		int k1 = 0;
		l1 = l * p_detectMinimumSpriteSize_3_ / 100;
		Iterator iterator1 = set1.iterator();

		while (iterator1.hasNext()) {
			int i2 = ((Integer) iterator1.next()).intValue();
			int j2 = ((Integer) map.get(Integer.valueOf(i2))).intValue();
			k1 += j2;

			if (i2 > i1) {
				i1 = i2;
			}

			if (k1 > l1) {
				return i1;
			}
		}

		return i1;
	}

	private int getMinSpriteSize() {
		int i = 1 << this.mipmapLevels;

		if (i < 8) {
			i = 8;
		}

		return i;
	}

	private int[] getMissingImageData(int p_getMissingImageData_1_) {
		BufferedImage bufferedimage = new BufferedImage(16, 16, 2);
		bufferedimage.setRGB(0, 0, 16, 16, TextureUtil.missingTextureData, 0, 16);
		BufferedImage bufferedimage1 = TextureUtils.scaleToPowerOfTwo(bufferedimage, p_getMissingImageData_1_);
		int[] aint = new int[p_getMissingImageData_1_ * p_getMissingImageData_1_];
		bufferedimage1.getRGB(0, 0, p_getMissingImageData_1_, p_getMissingImageData_1_, aint, 0,
				p_getMissingImageData_1_);
		return aint;
	}

	public boolean isTextureBound() {
		int i = GlStateManager.getBoundTexture();
		int j = this.getGlTextureId();
		return i == j;
	}

	private void updateIconGrid(int p_updateIconGrid_1_, int p_updateIconGrid_2_) {
		this.iconGridCountX = -1;
		this.iconGridCountY = -1;
		this.iconGrid = null;

		if (this.iconGridSize > 0) {
			this.iconGridCountX = p_updateIconGrid_1_ / this.iconGridSize;
			this.iconGridCountY = p_updateIconGrid_2_ / this.iconGridSize;
			this.iconGrid = new TextureAtlasSprite[this.iconGridCountX * this.iconGridCountY];
			this.iconGridSizeU = 1.0D / (double) this.iconGridCountX;
			this.iconGridSizeV = 1.0D / (double) this.iconGridCountY;

			for (Object o : this.mapUploadedSprites.values()) {
				TextureAtlasSprite textureatlassprite = (TextureAtlasSprite) o;
				double d0 = 0.5D / (double) p_updateIconGrid_1_;
				double d1 = 0.5D / (double) p_updateIconGrid_2_;
				double d2 = (double) Math.min(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) + d0;
				double d3 = (double) Math.min(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) + d1;
				double d4 = (double) Math.max(textureatlassprite.getMinU(), textureatlassprite.getMaxU()) - d0;
				double d5 = (double) Math.max(textureatlassprite.getMinV(), textureatlassprite.getMaxV()) - d1;
				int i = (int) (d2 / this.iconGridSizeU);
				int j = (int) (d3 / this.iconGridSizeV);
				int k = (int) (d4 / this.iconGridSizeU);
				int l = (int) (d5 / this.iconGridSizeV);

				for (int i1 = i; i1 <= k; ++i1) {
					if (i1 >= 0 && i1 < this.iconGridCountX) {
						for (int j1 = j; j1 <= l; ++j1) {
							if (j1 >= 0 && j1 < this.iconGridCountX) {
								int k1 = j1 * this.iconGridCountX + i1;
								this.iconGrid[k1] = textureatlassprite;
							} else {
								Config.warn("Invalid grid V: " + j1 + ", icon: " + textureatlassprite.getIconName());
							}
						}
					} else {
						Config.warn("Invalid grid U: " + i1 + ", icon: " + textureatlassprite.getIconName());
					}
				}
			}
		}
	}

	public TextureAtlasSprite getIconByUV(double p_getIconByUV_1_, double p_getIconByUV_3_) {
		if (this.iconGrid == null) {
			return null;
		} else {
			int i = (int) (p_getIconByUV_1_ / this.iconGridSizeU);
			int j = (int) (p_getIconByUV_3_ / this.iconGridSizeV);
			int k = j * this.iconGridCountX + i;
			return k >= 0 && k <= this.iconGrid.length ? this.iconGrid[k] : null;
		}
	}
}
