package com.etb.client.gui.alt.account;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.etb.client.Client;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AltSaving {
    private static File altFile;
    private static File lastAltFile;
    private static File alteningFile;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public AltSaving(File dir) {
        altFile = new File(dir + File.separator + "alts.json");
        lastAltFile = new File(dir + File.separator + "lastalt.json");
        alteningFile = new File(dir + File.separator + "alteningkey.json");
    }

    public void setup() {
        // Tries to create the module's file.
        try {
            // Creates the module file if it doesn't exist.
            if (!altFile.exists()) {

                // Creates the module's file.
                altFile.createNewFile();
                // Returns because there is no need to load.
                return;
            }

            // Loads the file.
            loadFile();
        } catch (IOException exception) {
        }
    }

    public void loadLastAltFile() {
        if (!lastAltFile.exists()) {
            try {
                lastAltFile.createNewFile();
            } catch (IOException e) {
            }
            return;
        }
        try (FileReader inFile = new FileReader(lastAltFile)) {
            Client.INSTANCE.getAccountManager().setLastAlt(GSON.fromJson(inFile, new TypeToken<Account>() {
            }.getType()));
        } catch (Exception e) {
        }
    }

    public void loadAlteningTokenFile() {
        if (!alteningFile.exists()) {
            try {
                alteningFile.createNewFile();
            } catch (IOException e) {
            }
            return;
        }
        try (FileReader inFile = new FileReader(alteningFile)) {
            Client.INSTANCE.getAccountManager().setAlteningToken(GSON.fromJson(inFile, new TypeToken<String>() {
            }.getType()));
        } catch (Exception e) {
        }
    }

    public void loadFile() {
        if (!altFile.exists()) {
            return;
        }
        try (FileReader inFile = new FileReader(altFile)) {
            Client.INSTANCE.getAccountManager().setAccounts(GSON.fromJson(inFile, new TypeToken<ArrayList<Account>>() {
            }.getType()));

            if (Client.INSTANCE.getAccountManager().getAccounts() == null)
                Client.INSTANCE.getAccountManager().setAccounts(new ArrayList<Account>());

        } catch (Exception e) {
        }
    }

    public void saveFile() {
        if (altFile.exists()) {
            try (PrintWriter writer = new PrintWriter(altFile)) {
                writer.print(GSON.toJson(Client.INSTANCE.getAccountManager().getAccounts()));
            } catch (Exception e) {
            }
        }
    }

    public void saveLastAltFile() {
        if (Client.INSTANCE.getAccountManager().getLastAlt() != null) {
            try (PrintWriter writer = new PrintWriter(lastAltFile)) {
                writer.print(GSON.toJson(Client.INSTANCE.getAccountManager().getLastAlt()));
            } catch (Exception e) {
            }
        }
    }
    public void saveAlteningTokenFile() {
        if (Client.INSTANCE.getAccountManager().getAlteningToken() != null) {
            try (PrintWriter writer = new PrintWriter(alteningFile)) {
                writer.print(GSON.toJson(Client.INSTANCE.getAccountManager().getAlteningToken()));
            } catch (Exception e) {
            }
        }
    }
}
