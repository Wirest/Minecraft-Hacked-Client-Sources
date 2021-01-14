package optifine;

import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class CustomSky {
    private static CustomSkyLayer[][] worldSkyLayers = (CustomSkyLayer[][]) null;

    public static void reset() {
        worldSkyLayers = (CustomSkyLayer[][]) null;
    }

    public static void update() {

        if (Config.isCustomSky()) {
            worldSkyLayers = readCustomSkies();
        }
    }

    private static CustomSkyLayer[][] readCustomSkies() {
        CustomSkyLayer[][] arrayOfCustomSkyLayer = new CustomSkyLayer[10][0];
        String str1 = "mcpatcher/sky/world";
        int i = -1;
        for (int j = 0; j < arrayOfCustomSkyLayer.length; j++) {
            localObject = str1 + j + "/sky";
            ArrayList localArrayList = new ArrayList();
            for (int m = 1; m < 1000; m++) {
                String str2 = (String) localObject + m + ".properties";
                try {
                    ResourceLocation localResourceLocation1 = new ResourceLocation(str2);
                    InputStream localInputStream = Config.getResourceStream(localResourceLocation1);
                    if (localInputStream == null) {
                        break;
                    }
                    Properties localProperties = new Properties();
                    localProperties.load(localInputStream);
                    localInputStream.close();
                    Config.dbg("CustomSky properties: " + str2);
                    String str3 = (String) localObject + m + ".png";
                    CustomSkyLayer localCustomSkyLayer = new CustomSkyLayer(localProperties, str3);
                    if (localCustomSkyLayer.isValid(str2)) {
                        ResourceLocation localResourceLocation2 = new ResourceLocation(localCustomSkyLayer.source);
                        ITextureObject localITextureObject = TextureUtils.getTexture(localResourceLocation2);
                        if (localITextureObject == null) {
                            Config.log("CustomSky: Texture not found: " + localResourceLocation2);
                        } else {
                            localCustomSkyLayer.textureId = localITextureObject.getGlTextureId();
                            localArrayList.add(localCustomSkyLayer);
                            localInputStream.close();
                        }
                    }
                } catch (FileNotFoundException localFileNotFoundException) {
                    break;
                } catch (IOException localIOException) {
                    localIOException.printStackTrace();
                }
            }
            if (localArrayList.size() > 0) {
                CustomSkyLayer[] arrayOfCustomSkyLayer1 = (CustomSkyLayer[]) (CustomSkyLayer[]) localArrayList.toArray(new CustomSkyLayer[localArrayList.size()]);
                arrayOfCustomSkyLayer[j] = arrayOfCustomSkyLayer1;
                i = j;
            }
        }
        if (i < 0) {
            return (CustomSkyLayer[][]) null;
        }
        j = i | 0x1;
        Object localObject = new CustomSkyLayer[j][0];
        for (int k = 0; k < localObject.length; k++) {
            localObject[k] = arrayOfCustomSkyLayer[k];
        }
        return (CustomSkyLayer[][]) localObject;
    }

    public static void renderSky(World paramWorld, TextureManager paramTextureManager, float paramFloat1, float paramFloat2) {
        if ((worldSkyLayers != null) && (Config.getGameSettings().renderDistanceChunks >= 8)) {
            int i = paramWorld.provider.getDimensionId();
            if ((i >= 0) && (i < worldSkyLayers.length)) {
                CustomSkyLayer[] arrayOfCustomSkyLayer = worldSkyLayers[i];
                if (arrayOfCustomSkyLayer != null) {
                    long l = paramWorld.getWorldTime();
                    int j = (int) (l % 24000L);
                    for (int k = 0; k < arrayOfCustomSkyLayer.length; k++) {
                        CustomSkyLayer localCustomSkyLayer = arrayOfCustomSkyLayer[k];
                        if (localCustomSkyLayer.isActive(paramWorld, j)) {
                            localCustomSkyLayer.render(j, paramFloat1, paramFloat2);
                        }
                    }
                    Blender.clearBlend(paramFloat2);
                }
            }
        }
    }

    public static boolean hasSkyLayers(World paramWorld) {
        if (worldSkyLayers == null) {
            return false;
        }
        if (Config.getGameSettings().renderDistanceChunks < 8) {
            return false;
        }
        int i = paramWorld.provider.getDimensionId();
        if ((i >= 0) && (i < worldSkyLayers.length)) {
            CustomSkyLayer[] arrayOfCustomSkyLayer = worldSkyLayers[i];
            return arrayOfCustomSkyLayer != null;
        }
        return false;
    }
}




