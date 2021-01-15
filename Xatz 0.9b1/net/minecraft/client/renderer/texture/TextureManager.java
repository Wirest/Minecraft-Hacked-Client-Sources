package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
import shadersmod.client.ShadersTex;

public class TextureManager implements ITickable, IResourceManagerReloadListener {
	private static final Logger logger = LogManager.getLogger();
	private final Map<ResourceLocation, ITextureObject> mapTextureObjects = Maps
			.<ResourceLocation, ITextureObject>newHashMap();
	private final List<ITickable> listTickables = Lists.<ITickable>newArrayList();
	private final Map<String, Integer> mapTextureCounters = Maps.<String, Integer>newHashMap();
	private IResourceManager theResourceManager;

	public TextureManager(IResourceManager resourceManager) {
		this.theResourceManager = resourceManager;
	}

	public void bindTexture(ResourceLocation resource) {
		if (Config.isRandomMobs()) {
			resource = RandomMobs.getTextureLocation(resource);
		}

		ITextureObject itextureobject = (ITextureObject) this.mapTextureObjects.get(resource);

		if (itextureobject == null) {
			itextureobject = new SimpleTexture(resource);
			this.loadTexture(resource, itextureobject);
		}

		if (Config.isShaders()) {
			ShadersTex.bindTexture(itextureobject);
		} else {
			TextureUtil.bindTexture(itextureobject.getGlTextureId());
		}
	}

	public boolean loadTickableTexture(ResourceLocation textureLocation, ITickableTextureObject textureObj) {
		if (this.loadTexture(textureLocation, textureObj)) {
			this.listTickables.add(textureObj);
			return true;
		} else {
			return false;
		}
	}

	public boolean loadTexture(ResourceLocation textureLocation, ITextureObject textureObj) {
		boolean flag = true;

		try {
			((ITextureObject) textureObj).loadTexture(this.theResourceManager);
		} catch (IOException ioexception) {
			logger.warn((String) ("Failed to load texture: " + textureLocation), (Throwable) ioexception);
			textureObj = TextureUtil.missingTexture;
			this.mapTextureObjects.put(textureLocation, (ITextureObject) textureObj);
			flag = false;
		} catch (Throwable throwable) {
			final ITextureObject textureObjf = textureObj;
			CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Registering texture");
			CrashReportCategory crashreportcategory = crashreport.makeCategory("Resource location being registered");
			crashreportcategory.addCrashSection("Resource location", textureLocation);
			crashreportcategory.addCrashSectionCallable("Texture object class", new Callable<String>() {
				public String call() throws Exception {
					return textureObjf.getClass().getName();
				}
			});
			throw new ReportedException(crashreport);
		}

		this.mapTextureObjects.put(textureLocation, (ITextureObject) textureObj);
		return flag;
	}

	public ITextureObject getTexture(ResourceLocation textureLocation) {
		return (ITextureObject) this.mapTextureObjects.get(textureLocation);
	}

	public ResourceLocation getDynamicTextureLocation(String name, DynamicTexture texture) {
		if (name.equals("logo")) {
			texture = Config.getMojangLogoTexture(texture);
		}

		Integer integer = (Integer) this.mapTextureCounters.get(name);

		if (integer == null) {
			integer = Integer.valueOf(1);
		} else {
			integer = Integer.valueOf(integer.intValue() + 1);
		}

		this.mapTextureCounters.put(name, integer);
		ResourceLocation resourcelocation = new ResourceLocation(
				String.format("dynamic/%s_%d", new Object[] { name, integer }));
		this.loadTexture(resourcelocation, texture);
		return resourcelocation;
	}

	public void tick() {
		for (ITickable itickable : this.listTickables) {
			itickable.tick();
		}
	}

	public void deleteTexture(ResourceLocation textureLocation) {
		ITextureObject itextureobject = this.getTexture(textureLocation);

		if (itextureobject != null) {
			TextureUtil.deleteTexture(itextureobject.getGlTextureId());
		}
	}

	public void onResourceManagerReload(IResourceManager resourceManager) {
		Config.dbg("*** Reloading textures ***");
		Config.log("Resource packs: " + Config.getResourcePackNames());
		Iterator iterator = this.mapTextureObjects.keySet().iterator();

		while (iterator.hasNext()) {
			ResourceLocation resourcelocation = (ResourceLocation) iterator.next();

			if (resourcelocation.getResourcePath().startsWith("mcpatcher/")) {
				ITextureObject itextureobject = (ITextureObject) this.mapTextureObjects.get(resourcelocation);

				if (itextureobject instanceof AbstractTexture) {
					AbstractTexture abstracttexture = (AbstractTexture) itextureobject;
					abstracttexture.deleteGlTexture();
				}

				iterator.remove();
			}
		}

		for (Entry<ResourceLocation, ITextureObject> entry : this.mapTextureObjects.entrySet()) {
			this.loadTexture((ResourceLocation) entry.getKey(), (ITextureObject) entry.getValue());
		}
	}
}
