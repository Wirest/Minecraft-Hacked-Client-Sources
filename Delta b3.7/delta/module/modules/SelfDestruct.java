/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.xtrm.delta.api.module.Category
 *  me.xtrm.delta.api.module.IModule
 *  me.xtrm.delta.api.module.Module
 *  net.minecraft.client.Minecraft
 *  net.minecraft.util.ChatComponentText
 *  net.minecraft.util.IChatComponent
 *  org.lwjgl.opengl.Display
 */
package delta.module.modules;

import delta.client.DeltaClient;
import me.xtrm.delta.api.module.Category;
import me.xtrm.delta.api.module.IModule;
import me.xtrm.delta.api.module.Module;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.lwjgl.opengl.Display;

public class SelfDestruct
extends Module {
    public SelfDestruct() {
        super("SelfDestruct", Category.Misc);
    }

    public void onEnable() {
        this.toggle();
        super.onEnable();
        Minecraft.getMinecraft().func_152344_a(this::destruct);
    }

    private void destruct() {
        DeltaClient.instance.managers.filesManager._medicine();
        DeltaClient.instance.managers.fmlBusHook._perfect();
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            iModule.setEnabled(false);
            iModule.onDisable();
            iModule.setKey(0);
        }
        for (IModule iModule : DeltaClient.instance.managers.modulesManager.getModules()) {
            if (!iModule.isEnabled()) continue;
            iModule.toggle();
        }
        Display.setTitle((String)DeltaClient.instance.displayTitle.replace("%PLAYER_USERNAME%", this.mc.getSession().getUsername()));
        for (int i = 0; i < 100; ++i) {
            this.mc.thePlayer.addChatMessage((IChatComponent)new ChatComponentText(" "));
        }
        DeltaClient.instance.managers.eventManager.unregister((Object)DeltaClient.instance.managers.modulesManager);
    }
}

