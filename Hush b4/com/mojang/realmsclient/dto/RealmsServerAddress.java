// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsServerAddress extends ValueObject
{
    private static final Logger LOGGER;
    public String address;
    
    public static RealmsServerAddress parse(final String json) {
        final JsonParser parser = new JsonParser();
        final RealmsServerAddress serverAddress = new RealmsServerAddress();
        try {
            final JsonObject object = parser.parse(json).getAsJsonObject();
            serverAddress.address = JsonUtils.getStringOr("address", object, null);
        }
        catch (Exception e) {
            RealmsServerAddress.LOGGER.error("Could not parse McoServerAddress: " + e.getMessage());
        }
        return serverAddress;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
