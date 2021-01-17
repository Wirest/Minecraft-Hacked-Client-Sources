// 
// Decompiled by Procyon v0.5.36
// 

package com.mojang.realmsclient.dto;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import com.google.gson.JsonElement;
import java.util.ArrayList;
import com.google.gson.JsonParser;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class BackupList
{
    private static final Logger LOGGER;
    public List<Backup> backups;
    
    public static BackupList parse(final String json) {
        final JsonParser jsonParser = new JsonParser();
        final BackupList backupList = new BackupList();
        backupList.backups = new ArrayList<Backup>();
        try {
            final JsonElement node = jsonParser.parse(json).getAsJsonObject().get("backups");
            if (node.isJsonArray()) {
                final Iterator<JsonElement> iterator = node.getAsJsonArray().iterator();
                while (iterator.hasNext()) {
                    backupList.backups.add(Backup.parse(iterator.next()));
                }
            }
        }
        catch (Exception e) {
            BackupList.LOGGER.error("Could not parse BackupList: " + e.getMessage());
        }
        return backupList;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
