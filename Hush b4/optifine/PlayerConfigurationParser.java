// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import net.minecraft.client.Minecraft;
import java.awt.image.BufferedImage;
import net.minecraft.util.ResourceLocation;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;

public class PlayerConfigurationParser
{
    private String player;
    public static final String CONFIG_ITEMS = "items";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_ACTIVE = "active";
    
    public PlayerConfigurationParser(final String p_i71_1_) {
        this.player = null;
        this.player = p_i71_1_;
    }
    
    public PlayerConfiguration parsePlayerConfiguration(final JsonElement p_parsePlayerConfiguration_1_) {
        if (p_parsePlayerConfiguration_1_ == null) {
            throw new JsonParseException("JSON object is null, player: " + this.player);
        }
        final JsonObject jsonobject = (JsonObject)p_parsePlayerConfiguration_1_;
        final PlayerConfiguration playerconfiguration = new PlayerConfiguration();
        final JsonArray jsonarray = (JsonArray)jsonobject.get("items");
        if (jsonarray != null) {
            for (int i = 0; i < jsonarray.size(); ++i) {
                final JsonObject jsonobject2 = (JsonObject)jsonarray.get(i);
                final boolean flag = Json.getBoolean(jsonobject2, "active", true);
                if (flag) {
                    final String s = Json.getString(jsonobject2, "type");
                    if (s == null) {
                        Config.warn("Item type is null, player: " + this.player);
                    }
                    else {
                        String s2 = Json.getString(jsonobject2, "model");
                        if (s2 == null) {
                            s2 = "items/" + s + "/model.cfg";
                        }
                        final PlayerItemModel playeritemmodel = this.downloadModel(s2);
                        if (playeritemmodel != null) {
                            if (!playeritemmodel.isUsePlayerTexture()) {
                                String s3 = Json.getString(jsonobject2, "texture");
                                if (s3 == null) {
                                    s3 = "items/" + s + "/users/" + this.player + ".png";
                                }
                                final BufferedImage bufferedimage = this.downloadTextureImage(s3);
                                if (bufferedimage == null) {
                                    continue;
                                }
                                playeritemmodel.setTextureImage(bufferedimage);
                                final ResourceLocation resourcelocation = new ResourceLocation("optifine.net", s3);
                                playeritemmodel.setTextureLocation(resourcelocation);
                            }
                            playerconfiguration.addPlayerItemModel(playeritemmodel);
                        }
                    }
                }
            }
        }
        return playerconfiguration;
    }
    
    private BufferedImage downloadTextureImage(final String p_downloadTextureImage_1_) {
        final String s = "http://s.optifine.net/" + p_downloadTextureImage_1_;
        try {
            final byte[] abyte = HttpPipeline.get(s, Minecraft.getMinecraft().getProxy());
            final BufferedImage bufferedimage = ImageIO.read(new ByteArrayInputStream(abyte));
            return bufferedimage;
        }
        catch (IOException ioexception) {
            Config.warn("Error loading item texture " + p_downloadTextureImage_1_ + ": " + ioexception.getClass().getName() + ": " + ioexception.getMessage());
            return null;
        }
    }
    
    private PlayerItemModel downloadModel(final String p_downloadModel_1_) {
        final String s = "http://s.optifine.net/" + p_downloadModel_1_;
        try {
            final byte[] abyte = HttpPipeline.get(s, Minecraft.getMinecraft().getProxy());
            final String s2 = new String(abyte, "ASCII");
            final JsonParser jsonparser = new JsonParser();
            final JsonObject jsonobject = (JsonObject)jsonparser.parse(s2);
            final PlayerItemParser playeritemparser = new PlayerItemParser();
            final PlayerItemModel playeritemmodel = PlayerItemParser.parseItemModel(jsonobject);
            return playeritemmodel;
        }
        catch (Exception exception) {
            Config.warn("Error loading item model " + p_downloadModel_1_ + ": " + exception.getClass().getName() + ": " + exception.getMessage());
            return null;
        }
    }
}
