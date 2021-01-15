// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client;

import me.aristhena.utils.MCStencil;
import me.aristhena.client.gui.account.AccountScreen;
import me.aristhena.client.gui.click.ClickGui;
import me.aristhena.client.friend.FriendManager;
import me.aristhena.client.option.OptionManager;
import me.aristhena.client.command.CommandManager;
import me.aristhena.client.module.ModuleManager;
import me.aristhena.utils.ClientUtils;

public final class Client
{
    public static String clientName;
    public static final double VERSION = 5.5;
    public static AccountScreen accountScreen;
    
    static {
        Client.clientName = "Client";
    }
    
    public static void start() {
        ClientUtils.loadClientFont();
        ModuleManager.start();
        CommandManager.start();
        OptionManager.start();
        FriendManager.start();
        ClickGui.start();
        MCStencil.checkSetupFBO();
    }
}
