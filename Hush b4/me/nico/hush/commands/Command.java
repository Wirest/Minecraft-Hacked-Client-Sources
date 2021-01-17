// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.commands;

import me.nico.hush.Client;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.Minecraft;

public abstract class Command
{
    private String name;
    private String decription;
    private Minecraft mc;
    
    public Command(final String name, final String description) {
        this.name = name;
        this.decription = description;
    }
    
    public abstract void execute(final String[] p0);
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDecription() {
        return this.decription;
    }
    
    public void setDecription(final String decription) {
        this.decription = decription;
    }
    
    public static void messageWithoutPrefix(final String msg) {
        final Object chat = new ChatComponentText(msg);
        if (msg != null) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage((IChatComponent)chat);
        }
    }
    
    public static void messageWithPrefix(final String msg) {
        messageWithoutPrefix(String.valueOf(Client.instance.ClientPrefix) + msg);
    }
}
