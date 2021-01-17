/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.atlaspluginloader.api.APLProvider
 *  me.xtrm.atlaspluginloader.api.load.transform.ITransformerManager
 *  me.xtrm.atlaspluginloader.api.load.transform.Transformer
 *  me.xtrm.atlaspluginloader.api.types.IBasePlugin
 *  me.xtrm.atlaspluginloader.api.types.PluginInfo
 *  me.xtrm.delta.api.apl.types.IDeltaPlugin
 *  net.minecraft.launchwrapper.Launch
 */
package delta;

import delta.Class160;
import delta.Class199;
import delta.Class2;
import delta.Class66;
import delta.client.DeltaClient;
import delta.hooks.DiscordRPCTransformer;
import me.xtrm.atlaspluginloader.api.APLProvider;
import me.xtrm.atlaspluginloader.api.load.transform.ITransformerManager;
import me.xtrm.atlaspluginloader.api.load.transform.Transformer;
import me.xtrm.atlaspluginloader.api.types.IBasePlugin;
import me.xtrm.atlaspluginloader.api.types.PluginInfo;
import me.xtrm.delta.api.apl.types.IDeltaPlugin;
import net.minecraft.launchwrapper.Launch;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
@PluginInfo(name="Delta", author="xTrM_", version="b3.7")
public class Class57
implements IDeltaPlugin {
    private Class199 cohen$;
    private static Class57 addition$;

    public void onShutdown() {
        DeltaClient.instance._michigan();
    }

    public Class199 9L0o() {
        return this.cohen$;
    }

    public Class57() {
        addition$ = this;
        this.cohen$ = new Class199((ClassLoader)Launch.classLoader);
        ITransformerManager iTransformerManager = APLProvider.getPrimaryAPL().getLoadController().getTransformerManager();
        iTransformerManager.registerInternalTransformer((IBasePlugin)this, (Transformer)new Class160());
        iTransformerManager.registerInternalTransformer((IBasePlugin)this, (Transformer)new Class2());
        iTransformerManager.registerInternalTransformer((IBasePlugin)this, (Transformer)new Class66());
        iTransformerManager.registerInternalTransformer((IBasePlugin)this, (Transformer)new DiscordRPCTransformer());
        new DeltaClient();
    }

    public void onPreInit() {
        DeltaClient.instance.setupClient();
    }

    public void onInit() {
    }

    public void onPostInit() {
        DeltaClient.instance.init();
    }

    public static Class57 jGwh() {
        return addition$;
    }
}

