// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.client;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class RealmsError
{
    private static final Logger LOGGER;
    private String errorMessage;
    private int errorCode;
    
    public RealmsError(final String error) {
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject object = parser.parse(error).getAsJsonObject();
            this.errorMessage = JsonUtils.getStringOr("errorMsg", object, "");
            this.errorCode = JsonUtils.getIntOr("errorCode", object, -1);
        }
        catch (Exception e) {
            RealmsError.LOGGER.error("Could not parse RealmsError: " + e.getMessage());
        }
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
    
    public int getErrorCode() {
        return this.errorCode;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
