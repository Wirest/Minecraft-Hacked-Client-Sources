/*
 * Decompiled with CFR 0_122.
 *
 * Could not load the following classes:
 *  com.mojang.authlib.Agent
 *  com.mojang.authlib.GameProfile
 *  com.mojang.authlib.UserAuthentication
 *  com.mojang.authlib.exceptions.AuthenticationException
 *  com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
 *  com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication
 */
package me.memewaredevs.proxymanager;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import me.memewaredevs.client.Memeware;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

public final class ProxyCheckThread
        extends Thread {
    private final String password;
    private String status;
    private final String username;
    private Minecraft mc = Minecraft.getMinecraft();
    private ProxyVersion version;

    public ProxyCheckThread(String username, String password, ProxyVersion proxyVersion) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.version = proxyVersion;
        this.status = EnumChatFormatting.GRAY + "Waiting...";
    }

    private Proxy createSession(String IP, int PORT, ProxyVersion version) {
        if (IP.toLowerCase().equals("192.168.213.164")) {
            Authenticator.setDefault(new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("meme", "client".toCharArray());
                }
            });
        } else {
            Authenticator.setDefault(null);
        }
        return new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(IP, PORT));
    }

    public String getStatus() {
        return this.status;
    }

    @Override
    public void run() {
        try {
            this.status = "\u00A7a Success! IP: " + this.username + ", Port: " + this.password;
            Memeware.INSTANCE.proxy = this.createSession(this.username, Integer.parseInt(this.password), this.version);
        } catch (Exception e) {
            this.status = "Invalid Port.";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

