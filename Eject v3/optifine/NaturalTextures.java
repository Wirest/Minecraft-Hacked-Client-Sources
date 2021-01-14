package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class NaturalTextures {
    private static NaturalProperties[] propertiesByIndex = new NaturalProperties[0];

    public static void update() {
        propertiesByIndex = new NaturalProperties[0];
        if (Config.isNaturalTextures()) {
            String str1 = "optifine/natural.properties";
            try {
                ResourceLocation localResourceLocation = new ResourceLocation(str1);
                if (!Config.hasResource(localResourceLocation)) {
                    Config.dbg("NaturalTextures: configuration \"" + str1 + "\" not found");
                    return;
                }
                boolean bool = Config.isFromDefaultResourcePack(localResourceLocation);
                InputStream localInputStream = Config.getResourceStream(localResourceLocation);
                ArrayList localArrayList = new ArrayList(256);
                String str2 = Config.readInputStream(localInputStream);
                localInputStream.close();
                String[] arrayOfString1 = Config.tokenize(str2, "\n\r");
                if (bool) {
                    Config.dbg("Natural Textures: Parsing default configuration \"" + str1 + "\"");
                    Config.dbg("Natural Textures: Valid only for textures from default resource pack");
                } else {
                    Config.dbg("Natural Textures: Parsing configuration \"" + str1 + "\"");
                }
                TextureMap localTextureMap = TextureUtils.getTextureMapBlocks();
                for (int i = 0; i < arrayOfString1.length; i++) {
                    String str3 = arrayOfString1[i].trim();
                    if (!str3.startsWith("#")) {
                        String[] arrayOfString2 = Config.tokenize(str3, "=");
                        if (arrayOfString2.length != 2) {
                            Config.warn("Natural Textures: Invalid \"" + str1 + "\" line: " + str3);
                        } else {
                            String str4 = arrayOfString2[0].trim();
                            String str5 = arrayOfString2[1].trim();
                            TextureAtlasSprite localTextureAtlasSprite = localTextureMap.getSpriteSafe("minecraft:blocks/" + str4);
                            if (localTextureAtlasSprite == null) {
                                Config.warn("Natural Textures: Texture not found: \"" + str1 + "\" line: " + str3);
                            } else {
                                int j = localTextureAtlasSprite.getIndexInMap();
                                if (j < 0) {
                                    Config.warn("Natural Textures: Invalid \"" + str1 + "\" line: " + str3);
                                } else {
                                    if ((bool) && (!Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + str4 + ".png")))) {
                                        return;
                                    }
                                    NaturalProperties localNaturalProperties = new NaturalProperties(str5);
                                    if (localNaturalProperties.isValid()) {
                                        while (localArrayList.size() <= j) {
                                            localArrayList.add(null);
                                        }
                                        localArrayList.set(j, localNaturalProperties);
                                        Config.dbg("NaturalTextures: " + str4 + " = " + str5);
                                    }
                                }
                            }
                        }
                    }
                }
                propertiesByIndex = (NaturalProperties[]) (NaturalProperties[]) localArrayList.toArray(new NaturalProperties[localArrayList.size()]);
            } catch (FileNotFoundException localFileNotFoundException) {
                Config.warn("NaturalTextures: configuration \"" + str1 + "\" not found");
                return;
            } catch (Exception localException) {
                localException.printStackTrace();
            }
        }
    }

    public static BakedQuad getNaturalTexture(BlockPos paramBlockPos, BakedQuad paramBakedQuad) {
        TextureAtlasSprite localTextureAtlasSprite = paramBakedQuad.getSprite();
        if (localTextureAtlasSprite == null) {
            return paramBakedQuad;
        }
        NaturalProperties localNaturalProperties = getNaturalProperties(localTextureAtlasSprite);
        if (localNaturalProperties == null) {
            return paramBakedQuad;
        }
        int i = ConnectedTextures.getSide(paramBakedQuad.getFace());
        int j = Config.getRandom(paramBlockPos, i);
        int k = 0;
        boolean bool = false;
        if (localNaturalProperties.rotation > 1) {
            k = j >> 3;
        }
        if (localNaturalProperties.rotation == 2) {
            k = -2 * 2;
        }
        if (localNaturalProperties.flip) {
            bool = j >> 4 != 0;
        }
        return localNaturalProperties.getQuad(paramBakedQuad, k, bool);
    }

    public static NaturalProperties getNaturalProperties(TextureAtlasSprite paramTextureAtlasSprite) {
        if (!(paramTextureAtlasSprite instanceof TextureAtlasSprite)) {
            return null;
        }
        int i = paramTextureAtlasSprite.getIndexInMap();
        if ((i >= 0) && (i < propertiesByIndex.length)) {
            NaturalProperties localNaturalProperties = propertiesByIndex[i];
            return localNaturalProperties;
        }
        return null;
    }
}




