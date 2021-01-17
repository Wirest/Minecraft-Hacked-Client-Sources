/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 */
package delta;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import delta.Class170;
import delta.module.modules.Xray;
import delta.utils.XrayBlocks;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Class155
extends Class170 {
    @Override
    public void _segment() throws IOException {
        FileWriter fileWriter = new FileWriter(this._duncan());
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (XrayBlocks xrayBlocks : Xray.xrayBlocks.grave$) {
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("id", (Number)xrayBlocks.gentle$);
            jsonObject2.addProperty("meta", (Number)xrayBlocks.rapidly$);
            jsonArray.add((JsonElement)jsonObject2);
        }
        jsonObject.add("blacklist", (JsonElement)jsonArray);
        fileWriter.write(this._tracy().toJson((JsonElement)jsonObject));
        fileWriter.close();
    }

    public Class155(Gson gson, File file) {
        super(gson, file);
    }

    @Override
    public void _region() {
        if (this._duncan() != null && !this._duncan().exists()) {
            try {
                this._duncan().createNewFile();
            }
            catch (IOException iOException) {
                iOException.printStackTrace();
            }
        }
    }

    @Override
    public void _redhead() throws IOException {
        if (this._duncan().exists()) {
            FileReader fileReader = new FileReader(this._duncan());
            JsonObject jsonObject = (JsonObject)this._tracy().fromJson((Reader)fileReader, JsonObject.class);
            if (jsonObject == null) {
                fileReader.close();
                return;
            }
            if (jsonObject.has("blacklist")) {
                JsonArray jsonArray = (JsonArray)jsonObject.get("blacklist");
                XrayBlocks xrayBlocks = new XrayBlocks();
                for (int i = 118 - 213 + 203 - 41 + -67; i < jsonArray.size(); ++i) {
                    JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
                    int n = jsonObject2.get("id").getAsInt();
                    int n2 = jsonObject2.get("meta").getAsInt();
                    xrayBlocks.addBlock(n, n2);
                }
                if (!xrayBlocks.grave$.isEmpty()) {
                    Xray.xrayBlocks = xrayBlocks;
                }
            }
            fileReader.close();
        }
    }
}

