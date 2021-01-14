package optifine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class PlayerConfigurationParser
{
    private String player = null;
    public static final String CONFIG_ITEMS = "items";
    public static final String ITEM_TYPE = "type";
    public static final String ITEM_ACTIVE = "active";

    public PlayerConfigurationParser(String player)
    {
        this.player = player;
    }

    public PlayerConfiguration parsePlayerConfiguration(JsonElement je)
    {
        if (je == null)
        {
            throw new JsonParseException("JSON object is null, player: " + this.player);
        }
        else
        {
            JsonObject jo = (JsonObject)je;
            PlayerConfiguration pc = new PlayerConfiguration();
            JsonArray items = (JsonArray)jo.get("items");

            if (items != null)
            {
                for (int i = 0; i < items.size(); ++i)
                {
                    JsonObject item = (JsonObject)items.get(i);
                    boolean active = Json.getBoolean(item, "active", true);

                    if (active)
                    {
                        String type = Json.getString(item, "type");

                        if (type == null)
                        {
                            Config.warn("Item type is null, player: " + this.player);
                        }
                        else
                        {
                            String modelPath = Json.getString(item, "model");

                            if (modelPath == null)
                            {
                                modelPath = "items/" + type + "/model.cfg";
                            }

                            PlayerItemModel model = this.downloadModel(modelPath);

                            if (model != null)
                            {
                                if (!model.isUsePlayerTexture())
                                {
                                    String texturePath = Json.getString(item, "texture");

                                    if (texturePath == null)
                                    {
                                        texturePath = "items/" + type + "/users/" + this.player + ".png";
                                    }

                                    BufferedImage image = this.downloadTextureImage(texturePath);

                                    if (image == null)
                                    {
                                        continue;
                                    }

                                    model.setTextureImage(image);
                                    ResourceLocation loc = new ResourceLocation("optifine.net", texturePath);
                                    model.setTextureLocation(loc);
                                }

                                pc.addPlayerItemModel(model);
                            }
                        }
                    }
                }
            }

            return pc;
        }
    }

    private BufferedImage downloadTextureImage(String texturePath)
    {
        String textureUrl = "http://s.optifine.net/" + texturePath;

        try
        {
            byte[] e = HttpPipeline.get(textureUrl, Minecraft.getMinecraft().getProxy());
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(e));
            return image;
        }
        catch (IOException var5)
        {
            Config.warn("Error loading item texture " + texturePath + ": " + var5.getClass().getName() + ": " + var5.getMessage());
            return null;
        }
    }

    private PlayerItemModel downloadModel(String modelPath)
    {
        String modelUrl = "http://s.optifine.net/" + modelPath;

        try
        {
            byte[] e = HttpPipeline.get(modelUrl, Minecraft.getMinecraft().getProxy());
            String jsonStr = new String(e, "ASCII");
            JsonParser jp = new JsonParser();
            JsonObject jo = (JsonObject)jp.parse(jsonStr);
            PlayerItemParser pip = new PlayerItemParser();
            PlayerItemModel pim = PlayerItemParser.parseItemModel(jo);
            return pim;
        }
        catch (Exception var9)
        {
            Config.warn("Error loading item model " + modelPath + ": " + var9.getClass().getName() + ": " + var9.getMessage());
            return null;
        }
    }
}
