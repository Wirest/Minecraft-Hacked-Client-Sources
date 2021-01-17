// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class RealmsServerList extends ValueObject
{
    private static final Logger LOGGER;
    public List<RealmsServer> servers;
    
    public static RealmsServerList parse(final String json) {
        final RealmsServerList list = new RealmsServerList();
        list.servers = new ArrayList<RealmsServer>();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(json).getAsJsonObject();
            if (object.get("servers").isJsonArray()) {
                final JsonArray jsonArray = object.get("servers").getAsJsonArray();
                final Iterator<JsonElement> it = jsonArray.iterator();
                while (it.hasNext()) {
                    list.servers.add(RealmsServer.parse(it.next().getAsJsonObject()));
                }
            }
        }
        catch (Exception e) {
            RealmsServerList.LOGGER.error("Could not parse McoServerList: " + e.getMessage());
        }
        return list;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
