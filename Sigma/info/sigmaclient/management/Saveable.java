package info.sigmaclient.management;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import info.sigmaclient.Client;
import info.sigmaclient.module.Module;

/**
 * A class that allows for the easy saving and loading of field data.
 */

public abstract class Saveable {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
    private SubFolder folderType;
    private File folder;
    private File file;

    /**
     * Saves the object to its settings file.
     */

    public boolean save() {
        String data = gson.toJson(this);
        try {
            checkFile();
            FileWriter writer = new FileWriter(getFile());
            writer.write(data);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        Module.saveStatus();
        Module.saveSettings();
        return false;
    }

    /**
     * Returns and object
     */
    public Saveable load() {
        try {
            checkFile();
            BufferedReader br = new BufferedReader(new FileReader(getFile()));
            return gson.fromJson(br, getClass());
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Checks if the object's settings file exists. If it does not, it will
     * create it.
     *
     * @throws IOException
     */
    protected void checkFile() throws IOException {
        File file = getFile();
        if (!file.exists()) {
            File folder = getFolder();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            file.createNewFile();
        }
    }

    /**
     * Sets up the object's internal folder object.
     */
    public void setupFolder() {
        if (folderType == null) {
            folderType = SubFolder.Other;
        }
        String basePath = Client.getDataDir().getAbsolutePath();
        String newPath = basePath + ((basePath.endsWith(File.separator)) ? folderType.getFolderName() : File.separator + folderType.getFolderName());
        folder = new File(newPath);
    }

    /**
     * Sets up the object's internal file object.
     */
    public void setupFile() {
        if (folder == null) {
            setupFolder();
        }
        String fileName = getFileName();
        String basePath = folder.getAbsolutePath();
        String newPath = basePath + ((basePath.endsWith(File.separator)) ? fileName : File.separator + fileName);
        file = new File(newPath);
    }

    /**
     * Gets the folder that the object's settings belong in.
     */
    public File getFolder() {
        if (folder == null) {
            setupFolder();
        }
        return folder;
    }

    /**
     * Gets the file of the object's settings.
     */
    public File getFile() {
        if (file == null) {
            setupFile();
        }
        return file;
    }

    public SubFolder getFolderType() {
        return folderType;
    }

    public void setFolderType(SubFolder folderType) {
        this.folderType = folderType;
    }

    public String getFileName() {
        return getClass().getSimpleName() + ".json";
    }
}
