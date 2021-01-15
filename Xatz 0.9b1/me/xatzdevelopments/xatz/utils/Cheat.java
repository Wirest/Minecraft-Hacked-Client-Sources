package me.xatzdevelopments.xatz.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;

import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author antja03
 */
public abstract class Cheat {
    protected String mode;
    private final String id, description;
    private int bind;
    private final LinkedHashMap<String, Property> propertyRegistry;
    private boolean state;
    protected static Minecraft mc = Minecraft.getMinecraft();
    public int animation;

    public Cheat(String id, String description) {
        this.id = id;
        this.description = description;
        this.bind = Keyboard.KEY_NONE;

        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }

    public Cheat(String id, String description, int bind) {
        this.id = id;
        this.description = description;
        this.bind = bind;
        this.propertyRegistry = new LinkedHashMap<>();
        this.state = false;
    }
    protected void onEnable() {
        String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + "[" + this.getMode() + "]" : "");
        this.animation = mc.fontRendererObj.getStringWidth(name) - 10;
        //NotificationManager.postInfo("Modules", "You enabled " + this.getId());
    }

    protected void onDisable() {
        String name = this.getId() + (this.getMode() != null ? " " + EnumChatFormatting.GRAY + "[" + this.getMode() + "]" : "");
        this.animation = mc.fontRendererObj.getStringWidth(name) + 10;
        //NotificationManager.postInfo("Modules", "You disabled " + this.getId());
    }

    protected void registerProperties(Property... properties) {
        for (Property property : properties) {
            propertyRegistry.put(property.getId(), property);
        }
    }

    protected Minecraft getMc() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    protected WorldClient getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    protected GameSettings getGameSettings() {
        return Minecraft.getMinecraft().gameSettings;
    }

    protected PlayerControllerMP getPlayerController() {
        return Minecraft.getMinecraft().playerController;
    }

    public void setMode(String themode) {
        mode = themode;
    }

    public String getMode() {
        return mode;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int bind) {
        this.bind = bind;
  
    }

    public HashMap<String, Property> getPropertyRegistry() {
        return propertyRegistry;
    }

    public boolean getState() {
        return state;
    }

    public int getAnimation() {
        return animation;
    }

    public void setAnimation(int animation) {
        this.animation = animation;
    }
}
