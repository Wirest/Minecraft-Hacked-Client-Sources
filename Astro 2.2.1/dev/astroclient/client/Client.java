package dev.astroclient.client;

import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.Suge;
import awfdd.wefsd.awdsaef.awdfsgjjj.awdsafe.awfdsserg.Zabef;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.thealtening.auth.TheAlteningAuthentication;
import dev.astroclient.client.command.CommandManager;
import dev.astroclient.client.configuration.ConfigurationManager;
import dev.astroclient.client.event.EventAhhhhhhh;
import dev.astroclient.client.feature.FeatureManager;
import dev.astroclient.client.feature.impl.hud.hud.component.HudManager;
import dev.astroclient.client.property.PropertyManager;
import dev.astroclient.client.ui.clickable.Clickable;
import dev.astroclient.client.ui.menu.click.GuiMenu;
import dev.astroclient.client.ui.menu.onetap.MainScreen;
import dev.astroclient.client.util.render.font.FontRenderer;
import dev.astroclient.client.util.render.font.FontUtil;
import dev.astroclient.security.auth.AuthenticationManager;
import dev.astroclient.security.controller.Controller;
import dev.astroclient.security.indirection.MethodIndirection;
import dev.astroclient.security.uuid.UUIDGenerator;
import dev.astroclient.security.uuid.store.SimpleAttributeStore;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

import java.awt.*;
import java.io.*;
import java.util.UUID;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */
public class Client extends Controller {

    public static final Client INSTANCE = new Client();

    public final String BASE_URL = "https://astroclient.dev/";

    public final String NAME = "Astro", VERSION = "v2.2.1", BUILD = "121019";

    public String USER = "User", alteningToken, username;

    public int uid = 6666;

    public UUID uuid;

    public boolean pass_1, pass_2, RPC = true;

    public File directory;

    public Zabef<Suge> bus;

    private AuthenticationManager authenticationManager;

    public FeatureManager featureManager;

    public PropertyManager propertyManager;

    public HudManager hudManager;

    public ConfigurationManager configManager;

    public CommandManager commandManager;

    public GuiMenu guiMenu;
    public Clickable clickable;
    public MainScreen mainScreen;


    public FontRenderer fontRenderer;

    public FontRenderer hudFontRenderer;
    public FontRenderer hudFontRendererVerdana;
    public FontRenderer hudFontRendererTahoma;
    public FontRenderer hudFontRendererRoboto;

    public FontRenderer smallFontRenderer;
    public FontRenderer smallBoldFontRenderer;
    public FontRenderer largeFontRenderer;
    public FontRenderer largeBoldFontRenderer;
    public FontRenderer iconRenderer;
    public FontRenderer boldFontRenderer;
    public FontRenderer verdanaFontRenderer;
    public FontRenderer buttonFontRenderer;

    public TheAlteningAuthentication theAlteningAuth;

    @Override
    public void preAuth() {
        this.uuid = new UUIDGenerator(new SimpleAttributeStore()).generateUUID();
    }



    @Override
    public void setup() {
    	USER = "Dev" ;
        directory = new File(Minecraft.getMinecraft().mcDataDir, "Astro");

        Suge baseBus = new Suge();

        baseBus.setDispatcher(EventAhhhhhhh.class);
        this.uuid = new UUIDGenerator(new SimpleAttributeStore()).generateUUID();
        this.bus = new Zabef<>(baseBus);
        this.propertyManager = new PropertyManager();
        this.configManager = new ConfigurationManager(directory);
        this.configManager.load();
        this.featureManager = new FeatureManager();
        this.hudManager = new HudManager();
        this.guiMenu = new GuiMenu();
        this.clickable = new Clickable();
        this.username = "PokeS";
        if (uuid != null)
            this.bus.register(featureManager);
        this.theAlteningAuth = TheAlteningAuthentication.mojang();
        this.fontRenderer = new FontRenderer(new Font("Tahoma", Font.PLAIN, 16), true, true);

        this.hudFontRenderer = new FontRenderer(new Font("Consolas Regular", Font.PLAIN, 17), true, true);
        this.hudFontRendererVerdana = new FontRenderer(new Font("Verdana", Font.PLAIN, 17), true, true);
        this.hudFontRendererTahoma = new FontRenderer(new Font("Tahoma", Font.PLAIN, 17), true, true);
        this.hudFontRendererRoboto = new FontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("client/fonts/roboto.ttf"), 17, 0), true, true);


        this.smallFontRenderer = new FontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("client/fonts/roboto.ttf"), 16, 0), true, true);
        this.smallBoldFontRenderer = new FontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("client/fonts/robotob.ttf"), 16, 0), true, true);
        this.largeFontRenderer = new FontRenderer(new Font("Tahoma", Font.PLAIN, 19), true, true);
        this.iconRenderer = new FontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("client/fonts/icons.ttf"), 26, 0), true, true);
        this.boldFontRenderer = new FontRenderer(new Font("Tahoma Bold", Font.PLAIN, 16), true, true);
        this.largeBoldFontRenderer = new FontRenderer(new Font("Tahoma Bold", Font.PLAIN, 19), true, true);
        this.verdanaFontRenderer = new FontRenderer(new Font("Verdana", Font.PLAIN, 10), true, true);
        this.buttonFontRenderer = new FontRenderer(FontUtil.getFontFromTTF(new ResourceLocation("client/fonts/roboto.ttf"), 16, 0), true, true);
        loadGeneralSettings();
        this.commandManager = new CommandManager();
        Display.setTitle(NAME + " " + VERSION);

        this.guiMenu.init();

        this.mainScreen = new MainScreen();
        this.bus.bind();
    }

    @Override
    public void shutdown() {
        saveGeneralSettings();
    }

    public String getAlteningToken() {
        return alteningToken;
    }

    public void setAlteningToken(String alteningToken) {
        this.alteningToken = alteningToken;
    }

    public void setRPC(boolean RPC) {
        this.RPC = RPC;
    }

    public void loadGeneralSettings() {
        File f = new File(Client.INSTANCE.directory, "client.json");

        if (f.exists()) {
            try (Reader reader = new FileReader(f)) {
                JsonElement element = new JsonParser().parse(reader);
                if (element.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("altening-token"))
                        setAlteningToken(object.get("altening-token").getAsString());
                    if (object.has("RPC"))
                        setRPC(object.get("RPC").getAsBoolean());

                }
            } catch (Exception e) {
                //
            }
        } else
            try {
                f.createNewFile();
            } catch (IOException e) {
                //
            }
    }

    public void saveGeneralSettings() {
        File f = new File(Client.INSTANCE.directory, "client.json");

        if (!f.exists())
            try {
                f.createNewFile();
            } catch (IOException e) {
                //
            }
        if (f.exists()) {
            JsonObject object = new JsonObject();
            object.addProperty("altening-token", alteningToken);
            object.addProperty("RPC", RPC);

            try (Writer writer = new FileWriter(f)) {
                writer.write(new GsonBuilder().setPrettyPrinting().create().toJson(object));
            } catch (IOException e) {
                f.delete();
            }
        }

    }



	@Override
	public boolean auth() {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public boolean postAuth() {
		// TODO Auto-generated method stub
		return false;
	}

}
