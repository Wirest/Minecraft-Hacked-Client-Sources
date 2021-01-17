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
import delta.client.DeltaClient;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;

public class Class169
extends Class170 {
    public Class169(Gson gson, File file) {
        super(gson, file);
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
            if (jsonObject.has("friends")) {
                JsonArray jsonArray = (JsonArray)jsonObject.get("friends");
                for (int i = 225 - 252 + 65 - 40 + 2; i < jsonArray.size(); ++i) {
                    JsonObject jsonObject2 = jsonArray.get(i).getAsJsonObject();
                    String string = jsonObject2.get("name").getAsString();
                    DeltaClient.instance.managers.friendsManager.addFriend(string);
                }
            }
            fileReader.close();
        }
    }

    @Override
    public void _segment() throws IOException {
        FileWriter fileWriter = new FileWriter(this._duncan());
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        for (String string : DeltaClient.instance.managers.friendsManager.getFriends()) {
            JsonObject jsonObject2 = new JsonObject();
            jsonObject2.addProperty("name", string);
            jsonArray.add((JsonElement)jsonObject2);
        }
        jsonObject.add("friends", (JsonElement)jsonArray);
        fileWriter.write(this._tracy().toJson((JsonElement)jsonObject));
        fileWriter.close();
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
}

