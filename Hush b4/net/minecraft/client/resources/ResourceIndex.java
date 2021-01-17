// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import java.io.FileNotFoundException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import java.io.Reader;
import com.google.gson.JsonParser;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import java.io.File;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class ResourceIndex
{
    private static final Logger logger;
    private final Map<String, File> resourceMap;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public ResourceIndex(final File p_i1047_1_, final String p_i1047_2_) {
        this.resourceMap = (Map<String, File>)Maps.newHashMap();
        if (p_i1047_2_ != null) {
            final File file1 = new File(p_i1047_1_, "objects");
            final File file2 = new File(p_i1047_1_, "indexes/" + p_i1047_2_ + ".json");
            BufferedReader bufferedreader = null;
            try {
                bufferedreader = Files.newReader(file2, Charsets.UTF_8);
                final JsonObject jsonobject = new JsonParser().parse(bufferedreader).getAsJsonObject();
                final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonobject, "objects", null);
                if (jsonobject2 != null) {
                    for (final Map.Entry<String, JsonElement> entry : jsonobject2.entrySet()) {
                        final JsonObject jsonobject3 = entry.getValue();
                        final String s = entry.getKey();
                        final String[] astring = s.split("/", 2);
                        final String s2 = (astring.length == 1) ? astring[0] : (String.valueOf(astring[0]) + ":" + astring[1]);
                        final String s3 = JsonUtils.getString(jsonobject3, "hash");
                        final File file3 = new File(file1, String.valueOf(s3.substring(0, 2)) + "/" + s3);
                        this.resourceMap.put(s2, file3);
                    }
                }
            }
            catch (JsonParseException var20) {
                ResourceIndex.logger.error("Unable to parse resource index file: " + file2);
            }
            catch (FileNotFoundException var21) {
                ResourceIndex.logger.error("Can't find the resource index file: " + file2);
            }
            finally {
                IOUtils.closeQuietly(bufferedreader);
            }
            IOUtils.closeQuietly(bufferedreader);
        }
    }
    
    public Map<String, File> getResourceMap() {
        return this.resourceMap;
    }
}
