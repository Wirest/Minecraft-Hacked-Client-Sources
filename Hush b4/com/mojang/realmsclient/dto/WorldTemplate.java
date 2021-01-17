// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.Logger;

public class WorldTemplate extends ValueObject
{
    private static final Logger LOGGER;
    public String id;
    public String name;
    public String version;
    public String author;
    public String link;
    public boolean minigame;
    public String image;
    public String trailer;
    public String recommendedPlayers;
    
    public WorldTemplate() {
        this.minigame = false;
    }
    
    public static WorldTemplate parse(final JsonObject node) {
        final WorldTemplate template = new WorldTemplate();
        try {
            template.id = JsonUtils.getStringOr("id", node, "");
            template.name = JsonUtils.getStringOr("name", node, "");
            template.version = JsonUtils.getStringOr("version", node, "");
            template.author = JsonUtils.getStringOr("author", node, "");
            template.link = JsonUtils.getStringOr("link", node, "");
            template.image = JsonUtils.getStringOr("image", node, null);
            template.trailer = JsonUtils.getStringOr("trailer", node, "");
            template.recommendedPlayers = JsonUtils.getStringOr("recommendedPlayers", node, "");
        }
        catch (Exception e) {
            WorldTemplate.LOGGER.error("Could not parse WorldTemplate: " + e.getMessage());
        }
        return template;
    }
    
    public void setMinigame(final boolean minigame) {
        this.minigame = minigame;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
