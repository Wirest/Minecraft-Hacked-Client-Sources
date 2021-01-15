package com.ihl.client.accounts;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Account {

    public static List<Account> accounts = new CopyOnWriteArrayList();

    public String email, password;
    public String username;
    public ResourceLocation head;
    public boolean authenticating;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;

        if (!email.contains("@")) {
            loadHead(email);
        }
    }

    public void loadHead(String username) {
        this.username = username;

        if (head == null) {
            head = new ResourceLocation("heads/" + username);
            ThreadDownloadImageData textureHead = new ThreadDownloadImageData((File) null, String.format("https://minotar.net/helm/%s/64.png", username), (ResourceLocation) null, null);
            Minecraft.getMinecraft().getTextureManager().loadTexture(head, textureHead);
        }
    }
}
