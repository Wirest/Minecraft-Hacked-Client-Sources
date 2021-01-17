package net.minecraft.realms;

import java.net.Proxy;

import com.mojang.authlib.GameProfile;
import com.mojang.util.UUIDTypeAdapter;

import net.minecraft.client.Mineman;
import net.minecraft.util.Session;
import net.minecraft.world.WorldSettings;

public class Realms
{

    public static boolean isTouchScreen()
    {
        return Mineman.getMinecraft().gameSettings.touchscreen;
    }

    public static Proxy getProxy()
    {
        return Mineman.getMinecraft().getProxy();
    }

    public static String sessionId()
    {
        Session var0 = Mineman.getMinecraft().getSession();
        return var0 == null ? null : var0.getSessionID();
    }

    public static String userName()
    {
        Session var0 = Mineman.getMinecraft().getSession();
        return var0 == null ? null : var0.getUsername();
    }

    public static long currentTimeMillis()
    {
        return Mineman.getSystemTime();
    }

    public static String getSessionId()
    {
        return Mineman.getMinecraft().getSession().getSessionID();
    }

    public static String getName()
    {
        return Mineman.getMinecraft().getSession().getUsername();
    }

    public static String uuidToName(String p_uuidToName_0_)
    {
        return Mineman.getMinecraft().getSessionService().fillProfileProperties(new GameProfile(UUIDTypeAdapter.fromString(p_uuidToName_0_), (String)null), false).getName();
    }

    public static void setScreen(RealmsScreen p_setScreen_0_)
    {
        Mineman.getMinecraft().displayGuiScreen(p_setScreen_0_.getProxy());
    }

    public static String getGameDirectoryPath()
    {
        return Mineman.getMinecraft().mcDataDir.getAbsolutePath();
    }

    public static int survivalId()
    {
        return WorldSettings.GameType.SURVIVAL.getID();
    }

    public static int creativeId()
    {
        return WorldSettings.GameType.CREATIVE.getID();
    }

    public static int adventureId()
    {
        return WorldSettings.GameType.ADVENTURE.getID();
    }
}
