package com.etb.client.friend;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.etb.client.Client;


public class FriendSaving {
    private static File friendFile;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public FriendSaving(File dir) {
        friendFile = new File(dir + File.separator + "friends.json");
    }

    public void setup() {
        try {
            if (!friendFile.exists()) {
                friendFile.createNewFile();
                return;
            }
            loadFile();
        } catch (IOException exception) {
        }
    }
    

    public void loadFile() {
        if (!friendFile.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(friendFile)) {
            Client.INSTANCE.getFriendManager().setFriends(GSON.fromJson(inFile, new TypeToken<ArrayList<Friend>>() {
            }.getType()));

            if (Client.INSTANCE.getFriendManager().getFriends() == null)
                Client.INSTANCE.getFriendManager().setFriends(new ArrayList<>());

        } catch (Exception e) {
        }
    }

    public void saveFile() {
        if (friendFile.exists()) {
            try (PrintWriter writer = new PrintWriter(friendFile)) {
                writer.print(GSON.toJson(Client.INSTANCE.getFriendManager().getFriends()));
            } catch (Exception e) {
            }
        }
    }
}
