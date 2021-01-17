/*
 * Decompiled with CFR 0.150.
 */
package delta;

import delta.Class35;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class Class148
extends ConcurrentHashMap<String, Class<?>> {
    private String premises$;
    private List<String> steel$ = Class35.zXeo().qG8F();

    @Override
    public String toString() {
        if (this.premises$ == null || this.premises$ == "") {
            String string = super.toString();
            String string2 = string.replace("{", "").replace("}", "");
            String[] arrstring = string2.split(Pattern.quote(", "));
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add("net.minecraft.client.multiplayer.ServerList_class");
            arrayList.add("net.minecraft.client.network.LanServerDetector_class net.minecraft.client.network.LanServerDetector");
            arrayList.add("net.minecraft.client.gui.GuiScreenServerList");
            arrayList.add("net.minecraft.client.network.LanServerDetector$LanServerList");
            arrayList.add("net.minecraft.client.gui.GuiListExtended_class net.minecraft.client.gui.GuiListExtended");
            arrayList.add("net.minecraft.client.gui.GuiScreenAddServer_class net.minecraft.client.gui.GuiScreenAddServer");
            arrayList.add("net.minecraft.client.gui.ServerListEntryLanScan_class net.minecraft.client.gui.ServerListEntryLanScan");
            arrayList.add("net.minecraft.client.gui.GuiTextField_class net.minecraft.client.gui.GuiTextField");
            arrayList.add("net.minecraft.client.gui.inventory.GuiContainerCreative_class net.minecraft.client.gui.inventory.GuiContainerCreative");
            arrayList.add("net.minecraft.client.gui.ServerSelectionList_class net.minecraft.client.gui.ServerSelectionList");
            arrayList.add("net.minecraft.util.ChatAllowedCharacters_class net.minecraft.util.ChatAllowedCharacters");
            arrayList.add("com.google.gson.internal.UnsafeAllocator");
            arrayList.add("com.google.gson.internal.ConstructorConstructor$12");
            arrayList.add("com.sun.jna.win32.DLLCallback_interface com.sun.jna.win32.DLLCallback");
            arrayList.add("com.sun.jna.CallbackReference$DefaultCallbackProxy_class com.sun.jna.CallbackReference$DefaultCallbackProxy");
            arrayList.add("com.sun.jna.CallbackResultContext_class com.sun.jna.CallbackResultContext");
            arrayList.add("com.sun.jna.CallbackParameterContext_class com.sun.jna.CallbackParameterContext");
            arrayList.add("club.minnced.discord.rpc.DiscordUser_class club.minnced.discord.rpc.DiscordUser");
            arrayList.add("com.mojang.authlib.Agent_class com.mojang.authlib.Agent");
            arrayList.add("com.mojang.authlib.BaseUserAuthentication_class com.mojang.authlib.BaseUserAuthentication");
            arrayList.add("com.mojang.authlib.yggdrasil.request.AuthenticationRequest_class com.mojang.authlib.yggdrasil.request.AuthenticationRequest");
            arrayList.add("com.mojang.authlib.yggdrasil.response.User_class com.mojang.authlib.yggdrasil.response.User");
            arrayList.add("com.mojang.authlib.UserType_class com.mojang.authlib.UserType");
            arrayList.add("com.mojang.authlib.HttpUserAuthentication_class com.mojang.authlib.HttpUserAuthentication");
            arrayList.add("com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication_class com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication");
            arrayList.add("com.mojang.authlib.yggdrasil.response.AuthenticationResponse_class com.mojang.authlib.yggdrasil.response.AuthenticationResponse");
            StringBuilder stringBuilder = new StringBuilder();
            Object object = arrstring;
            int n = ((String[])object).length;
            block0: for (int i = 246 - 359 + 217 + -104; i < n; ++i) {
                String string3 = object[i];
                if (string3.contains(".")) {
                    for (String string4 : this.steel$) {
                        if (!string3.startsWith(string4)) continue;
                        continue block0;
                    }
                    for (String string4 : arrayList) {
                        if (!string3.startsWith(string4)) continue;
                        continue block0;
                    }
                }
                stringBuilder.append(string3);
                stringBuilder.append(", ");
            }
            object = stringBuilder.toString();
            object = ((String)object).substring(74 - 81 + 13 - 6 + 0, ((String)object).length() - (25 - 28 + 14 + -9));
            this.premises$ = object = "{" + (String)object + "}";
        }
        return this.premises$;
    }

    public Class148(Map<String, Class<?>> map) {
        super(map);
    }
}

