/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.api.types.PluginInfo
 *  me.xtrm.atlaspluginloader.core.event.EventTarget
 *  me.xtrm.delta.api.apl.types.IDeltaPlugin
 *  me.xtrm.delta.api.event.events.move.EventMotion
 *  net.minecraft.client.Minecraft
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package delta;

import delta.Class163;
import delta.Class179;
import delta.Class23;
import delta.Class32;
import delta.client.DeltaClient;
import delta.utils.Wrapper;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;
import me.xtrm.atlaspluginloader.core.event.EventTarget;
import me.xtrm.delta.api.apl.types.IDeltaPlugin;
import me.xtrm.delta.api.event.events.move.EventMotion;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@PluginInfo(author="xTrM_", version="2.20", name="Elf")
public class Class1
implements IDeltaPlugin {
    public static boolean insights$;
    private Logger floral$ = LogManager.getLogger((String)"ELF");
    public static boolean asylum$;

    @EventTarget
    public void ed2Z(EventMotion eventMotion) {
        if (Class32._manor()) {
            this.floral$.info("Skidada Skidoodle, your game is outdated you fucking noodle");
            this.onShutdown();
            Wrapper._occurs();
        }
        if (Class163.viewers$) {
            this.floral$.info("Yeetus yeetus, put a cactus up your anus");
            this.onShutdown();
            Wrapper._occurs();
        }
    }

    public void onShutdown() {
        if (!asylum$) {
            return;
        }
        if (!insights$) {
            return;
        }
        try {
            Class179._bookmark();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Class1() {
        try {
            asylum$ = Class179._chronic();
        }
        catch (Exception exception) {
            this.floral$.info("API Offline");
        }
        if (!asylum$) {
            return;
        }
        this.floral$.info("API Online");
        try {
            insights$ = Class179._craft();
        }
        catch (Exception exception) {
            this.floral$.info("Paarthurnax Offline");
        }
        if (insights$) {
            this.floral$.info("Paarthurnax Online");
        }
    }

    public void onInit() {
        this.floral$.info("Initializing ELF.");
    }

    public void onPreInit() {
        this.floral$.info("Pre-Initializing ELF.");
        Class23._charms();
        this.floral$.info("Starting Pixie: Odahviing");
        new Class32();
    }

    public void onPostInit() {
        this.floral$.info("Post-Initializing ELF.");
        DeltaClient.instance.managers.eventManager.register((Object)this);
        if (!asylum$) {
            return;
        }
        Class23._charms();
        if (!insights$) {
            return;
        }
        try {
            this.floral$.info("Starting IPixie: Paarthurnax");
            Class179._seeds(System.getenv("user.name"), Minecraft.getMinecraft().getSession().func_148256_e().getId().toString().replace("-", ""));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

