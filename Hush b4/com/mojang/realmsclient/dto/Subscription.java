// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import org.apache.logging.log4j.Logger;

public class Subscription extends ValueObject
{
    private static final Logger LOGGER;
    public long startDate;
    public int daysLeft;
    public SubscriptionType type;
    
    public Subscription() {
        this.type = SubscriptionType.NORMAL;
    }
    
    public static Subscription parse(final String json) {
        final Subscription sub = new Subscription();
        try {
            final JsonParser parser = new JsonParser();
            final JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            sub.startDate = JsonUtils.getLongOr("startDate", jsonObject, 0L);
            sub.daysLeft = JsonUtils.getIntOr("daysLeft", jsonObject, 0);
            sub.type = typeFrom(JsonUtils.getStringOr("subscriptionType", jsonObject, SubscriptionType.NORMAL.name()));
        }
        catch (Exception e) {
            Subscription.LOGGER.error("Could not parse Subscription: " + e.getMessage());
        }
        return sub;
    }
    
    private static SubscriptionType typeFrom(final String subscriptionType) {
        try {
            return SubscriptionType.valueOf(subscriptionType);
        }
        catch (Exception e) {
            return SubscriptionType.NORMAL;
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
    
    public enum SubscriptionType
    {
        NORMAL, 
        RECURRING;
    }
}
