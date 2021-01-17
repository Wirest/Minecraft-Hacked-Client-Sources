// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;

public class NaturalTextures
{
    private static NaturalProperties[] propertiesByIndex;
    
    static {
        NaturalTextures.propertiesByIndex = new NaturalProperties[0];
    }
    
    public static void update() {
        NaturalTextures.propertiesByIndex = new NaturalProperties[0];
        if (Config.isNaturalTextures()) {
            final String s = "optifine/natural.properties";
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                if (!Config.hasResource(resourcelocation)) {
                    Config.dbg("NaturalTextures: configuration \"" + s + "\" not found");
                    return;
                }
                final boolean flag = Config.isFromDefaultResourcePack(resourcelocation);
                final InputStream inputstream = Config.getResourceStream(resourcelocation);
                final ArrayList arraylist = new ArrayList(256);
                final String s2 = Config.readInputStream(inputstream);
                inputstream.close();
                final String[] astring = Config.tokenize(s2, "\n\r");
                if (flag) {
                    Config.dbg("Natural Textures: Parsing default configuration \"" + s + "\"");
                    Config.dbg("Natural Textures: Valid only for textures from default resource pack");
                }
                else {
                    Config.dbg("Natural Textures: Parsing configuration \"" + s + "\"");
                }
                final TextureMap texturemap = TextureUtils.getTextureMapBlocks();
                for (int i = 0; i < astring.length; ++i) {
                    final String s3 = astring[i].trim();
                    if (!s3.startsWith("#")) {
                        final String[] astring2 = Config.tokenize(s3, "=");
                        if (astring2.length != 2) {
                            Config.warn("Natural Textures: Invalid \"" + s + "\" line: " + s3);
                        }
                        else {
                            final String s4 = astring2[0].trim();
                            final String s5 = astring2[1].trim();
                            final TextureAtlasSprite textureatlassprite = texturemap.getSpriteSafe("minecraft:blocks/" + s4);
                            if (textureatlassprite == null) {
                                Config.warn("Natural Textures: Texture not found: \"" + s + "\" line: " + s3);
                            }
                            else {
                                final int j = textureatlassprite.getIndexInMap();
                                if (j < 0) {
                                    Config.warn("Natural Textures: Invalid \"" + s + "\" line: " + s3);
                                }
                                else {
                                    if (flag && !Config.isFromDefaultResourcePack(new ResourceLocation("textures/blocks/" + s4 + ".png"))) {
                                        return;
                                    }
                                    final NaturalProperties naturalproperties = new NaturalProperties(s5);
                                    if (naturalproperties.isValid()) {
                                        while (arraylist.size() <= j) {
                                            arraylist.add(null);
                                        }
                                        arraylist.set(j, naturalproperties);
                                        Config.dbg("NaturalTextures: " + s4 + " = " + s5);
                                    }
                                }
                            }
                        }
                    }
                }
                NaturalTextures.propertiesByIndex = arraylist.toArray(new NaturalProperties[arraylist.size()]);
            }
            catch (FileNotFoundException var17) {
                Config.warn("NaturalTextures: configuration \"" + s + "\" not found");
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public static BakedQuad getNaturalTexture(final BlockPos p_getNaturalTexture_0_, final BakedQuad p_getNaturalTexture_1_) {
        final TextureAtlasSprite textureatlassprite = p_getNaturalTexture_1_.getSprite();
        if (textureatlassprite == null) {
            return p_getNaturalTexture_1_;
        }
        final NaturalProperties naturalproperties = getNaturalProperties(textureatlassprite);
        if (naturalproperties == null) {
            return p_getNaturalTexture_1_;
        }
        final int i = ConnectedTextures.getSide(p_getNaturalTexture_1_.getFace());
        final int j = Config.getRandom(p_getNaturalTexture_0_, i);
        int k = 0;
        boolean flag = false;
        if (naturalproperties.rotation > 1) {
            k = (j & 0x3);
        }
        if (naturalproperties.rotation == 2) {
            k = k / 2 * 2;
        }
        if (naturalproperties.flip) {
            flag = ((j & 0x4) != 0x0);
        }
        return naturalproperties.getQuad(p_getNaturalTexture_1_, k, flag);
    }
    
    public static NaturalProperties getNaturalProperties(final TextureAtlasSprite p_getNaturalProperties_0_) {
        if (!(p_getNaturalProperties_0_ instanceof TextureAtlasSprite)) {
            return null;
        }
        final int i = p_getNaturalProperties_0_.getIndexInMap();
        if (i >= 0 && i < NaturalTextures.propertiesByIndex.length) {
            final NaturalProperties naturalproperties = NaturalTextures.propertiesByIndex[i];
            return naturalproperties;
        }
        return null;
    }
}
