// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import java.util.Set;
import com.google.gson.JsonObject;
import com.mojang.realmsclient.util.JsonUtils;
import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import org.apache.logging.log4j.Logger;

public class Backup extends ValueObject
{
    private static final Logger LOGGER;
    public String backupId;
    public Date lastModifiedDate;
    public long size;
    private boolean uploadedVersion;
    public Map<String, String> metadata;
    public Map<String, String> changeList;
    
    public Backup() {
        this.uploadedVersion = false;
        this.metadata = new HashMap<String, String>();
        this.changeList = new HashMap<String, String>();
    }
    
    public static Backup parse(final JsonElement node) {
        final JsonObject object = node.getAsJsonObject();
        final Backup backup = new Backup();
        try {
            backup.backupId = JsonUtils.getStringOr("backupId", object, "");
            backup.lastModifiedDate = JsonUtils.getDateOr("lastModifiedDate", object);
            backup.size = JsonUtils.getLongOr("size", object, 0L);
            if (object.has("metadata")) {
                final JsonObject metadataObject = object.getAsJsonObject("metadata");
                final Set<Map.Entry<String, JsonElement>> jsonElementSet = metadataObject.entrySet();
                for (final Map.Entry<String, JsonElement> elem : jsonElementSet) {
                    if (!elem.getValue().isJsonNull()) {
                        backup.metadata.put(format(elem.getKey()), elem.getValue().getAsString());
                    }
                }
            }
        }
        catch (Exception e) {
            Backup.LOGGER.error("Could not parse Backup: " + e.getMessage());
        }
        return backup;
    }
    
    private static String format(final String key) {
        final String[] worlds = key.split("_");
        final StringBuilder sb = new StringBuilder();
        for (final String world : worlds) {
            if (world != null && world.length() >= 1) {
                if (world.equals("of")) {
                    sb.append(world).append(" ");
                }
                else {
                    final char firstCharacter = Character.toUpperCase(world.charAt(0));
                    sb.append(firstCharacter).append(world.substring(1, world.length())).append(" ");
                }
            }
        }
        return sb.toString();
    }
    
    public boolean isUploadedVersion() {
        return this.uploadedVersion;
    }
    
    public void setUploadedVersion(final boolean uploadedVersion) {
        this.uploadedVersion = uploadedVersion;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
