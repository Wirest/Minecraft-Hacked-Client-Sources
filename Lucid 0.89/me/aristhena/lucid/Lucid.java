/*
 * Decompiled with CFR 0_114.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.renderer.texture.TextureManager
 *  net.minecraft.client.resources.IReloadableResourceManager
 *  net.minecraft.client.resources.IResourceManagerReloadListener
 *  net.minecraft.client.resources.LanguageManager
 *  net.minecraft.client.settings.GameSettings
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.Display
 */
package me.aristhena.lucid;

import me.aristhena.lucid.commands.Hud;
import me.aristhena.lucid.commands.InventoryCleaner;
import me.aristhena.lucid.management.account.AccountManager;
import me.aristhena.lucid.management.command.CommandManager;
import me.aristhena.lucid.management.friend.FriendManager;
import me.aristhena.lucid.management.module.ModuleManager;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.value.ValueManager;
import me.aristhena.lucid.ui.alt.screens.account.AccountScreen;
import me.aristhena.lucid.ui.clickgui.Draw;
import me.aristhena.lucid.ui.clickgui.Gui;
import me.aristhena.lucid.util.FontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.LanguageManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;

public class Lucid {
    public static final String NAME = "Lucid";
    public static final double VERSION = 0.89;
    public static AccountScreen accountScreen = new AccountScreen();
    public static FontRenderer font;

    public static void init() throws Exception {
        Lucid.configureFont();
        ModuleManager.init();
        CommandManager.init();
        OptionManager.init();
        ValueManager.init();
        FriendManager.init();
        AccountManager.init();
        Hud.load();
        InventoryCleaner.load();
        Draw.load();
        new me.aristhena.lucid.ui.clickgui.Gui();
        Display.setTitle((String)NAME + " " + VERSION + " | Source Code");
    }

    private static void configureFont() {
        Minecraft mc = Minecraft.getMinecraft();
        font = new FontRenderer(mc.gameSettings, new ResourceLocation("lucid/font/ascii.png"), mc.renderEngine, false);
        if (mc.gameSettings.language != null) {
            mc.fontRendererObj.setUnicodeFlag(mc.isUnicode());
            mc.fontRendererObj.setBidiFlag(mc.mcLanguageManager.isCurrentLanguageBidirectional());
        }
        mc.mcResourceManager.registerReloadListener((IResourceManagerReloadListener)font);
    }
}

