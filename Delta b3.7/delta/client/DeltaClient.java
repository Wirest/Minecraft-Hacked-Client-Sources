/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraftforge.common.MinecraftForge
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.lwjgl.opengl.Display
 */
package delta.client;

import delta.Class115;
import delta.Class55;
import delta.client.Managers;
import delta.utils.RenderUtils;
import delta.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.Display;

public class DeltaClient {
    public String displayTitle;
    public static DeltaClient instance;
    private Class115 pencil$;
    public static String regarded$;
    public Managers managers;

    public Class115 _resort() {
        return this.pencil$;
    }

    public DeltaClient() {
        instance = this;
        regarded$ = instance.toString().concat("1337");
        String string = System.getProperty("delta.loader.env", "TWEAKER");
        for (Class115 class115 : Class115._reader()) {
            if (!class115._realty().equalsIgnoreCase(string)) continue;
            this.pencil$ = class115;
            break;
        }
    }

    private static void setTitle() {
        Display.setTitle((String)"Delta b3.7");
    }

    public String toString() {
        return "DeltaBYxTrM_";
    }

    public void _michigan() {
        this.managers._minds();
    }

    public void setupClient() {
        this.managers = new Managers();
        this.managers._tagged();
        this.displayTitle = Display.getTitle().replace(Wrapper.mc.getSession().getUsername(), "%PLAYER_USERNAME%");
        Minecraft.getMinecraft().func_152344_a(DeltaClient::setTitle);
    }

    public void init() {
        Logger logger = LogManager.getLogger((String)"Delta");
        logger.info("Initializing...");
        logger.info("Delta b3.7 Starting on {MCENV=" + Class55._party() + ", LOADENV=" + this.pencil$._catering() + "}");
        this.managers._getting();
        if (!Display.getTitle().equalsIgnoreCase("Delta b3.7")) {
            this.displayTitle = Display.getTitle().replace(Wrapper.mc.getSession().getUsername(), "%PLAYER_USERNAME%");
            Minecraft.getMinecraft().func_152344_a(DeltaClient::setTitle);
        }
        logger.info("Registering FMLHookManager...");
        try {
            this.managers.fmlBusHook._candle(MinecraftForge.EVENT_BUS);
        }
        catch (ReflectiveOperationException reflectiveOperationException) {
            reflectiveOperationException.printStackTrace();
        }
        RenderUtils._heard();
        logger.info("Delta was loaded successfully!");
    }
}

