// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class WorldTemplateList extends ValueObject
{
    private static final Logger LOGGER;
    public List<WorldTemplate> templates;
    
    public static WorldTemplateList parse(final String json) {
        final WorldTemplateList list = new WorldTemplateList();
        list.templates = new ArrayList<WorldTemplate>();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            if (object.get("templates").isJsonArray()) {
                final Iterator<JsonElement> it = object.get("templates").getAsJsonArray().iterator();
                while (it.hasNext()) {
                    list.templates.add(WorldTemplate.parse(it.next().getAsJsonObject()));
                }
            }
        }
        catch (Exception e) {
            WorldTemplateList.LOGGER.error("Could not parse WorldTemplateList: " + e.getMessage());
        }
        return list;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
