// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import java.util.Iterator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonParser;
import java.util.ArrayList;
import java.util.List;

public class ServerActivityList
{
    public long periodInMillis;
    public List<ServerActivity> serverActivities;
    
    public ServerActivityList() {
        this.serverActivities = new ArrayList<ServerActivity>();
    }
    
    public static ServerActivityList parse(final String json) {
        final ServerActivityList activityList = new ServerActivityList();
        final JsonParser parser = new JsonParser();
        try {
            final JsonElement jsonElement = parser.parse(json);
            final JsonObject object = jsonElement.getAsJsonObject();
            activityList.periodInMillis = JsonUtils.getLongOr("periodInMillis", object, -1L);
            final JsonElement activityArray = object.get("playerActivityDto");
            if (activityArray != null && activityArray.isJsonArray()) {
                final JsonArray jsonArray = activityArray.getAsJsonArray();
                for (final JsonElement element : jsonArray) {
                    final ServerActivity sa = ServerActivity.parse(element.getAsJsonObject());
                    activityList.serverActivities.add(sa);
                }
            }
        }
        catch (Exception ex) {}
        return activityList;
    }
}
