package optifine;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import optifine.json.JSONArray;
import optifine.json.JSONObject;
import optifine.json.JSONParser;
import optifine.json.JSONWriter;
import optifine.json.ParseException;

public class Installer
{
    public static void main(String[] args)
    {
        try
        {
            File file1 = Utils.getWorkingDirectory();
            doInstall(file1);
        }
        catch (Exception exception)
        {
            String s = exception.getMessage();

            if (s != null && s.equals("QUIET"))
            {
                return;
            }

            exception.printStackTrace();
            String s1 = Utils.getExceptionStackTrace(exception);
            s1 = s1.replace("\t", "  ");
            JTextArea jtextarea = new JTextArea(s1);
            jtextarea.setEditable(false);
            Font font = jtextarea.getFont();
            Font font1 = new Font("Monospaced", font.getStyle(), font.getSize());
            jtextarea.setFont(font1);
            JScrollPane jscrollpane = new JScrollPane(jtextarea);
            jscrollpane.setPreferredSize(new Dimension(600, 400));
            JOptionPane.showMessageDialog((Component)null, jscrollpane, "Error", 0);
        }
    }

    public static void doInstall(File dirMc) throws Exception
    {
        Utils.dbg("Dir minecraft: " + dirMc);
        File file1 = new File(dirMc, "libraries");
        Utils.dbg("Dir libraries: " + file1);
        File file2 = new File(dirMc, "versions");
        Utils.dbg("Dir versions: " + file2);
        String s = getOptiFineVersion();
        Utils.dbg("OptiFine Version: " + s);
        String[] astring = Utils.tokenize(s, "_");
        String s1 = astring[1];
        Utils.dbg("Minecraft Version: " + s1);
        String s2 = getOptiFineEdition(astring);
        Utils.dbg("OptiFine Edition: " + s2);
        String s3 = s1 + "-OptiFine_" + s2;
        Utils.dbg("Minecraft_OptiFine Version: " + s3);
        copyMinecraftVersion(s1, s3, file2);
        installOptiFineLibrary(s1, s2, file1, false);
        updateJson(file2, s3, file1, s1, s2);
        updateLauncherJson(dirMc, s3);
    }

    public static boolean doExtract(File dirMc) throws Exception
    {
        Utils.dbg("Dir minecraft: " + dirMc);
        File file1 = new File(dirMc, "libraries");
        Utils.dbg("Dir libraries: " + file1);
        File file2 = new File(dirMc, "versions");
        Utils.dbg("Dir versions: " + file2);
        String s = getOptiFineVersion();
        Utils.dbg("OptiFine Version: " + s);
        String[] astring = Utils.tokenize(s, "_");
        String s1 = astring[1];
        Utils.dbg("Minecraft Version: " + s1);
        String s2 = getOptiFineEdition(astring);
        Utils.dbg("OptiFine Edition: " + s2);
        String s3 = s1 + "-OptiFine_" + s2;
        Utils.dbg("Minecraft_OptiFine Version: " + s3);
        return installOptiFineLibrary(s1, s2, file1, true);
    }

    private static void updateLauncherJson(File dirMc, String mcVerOf) throws IOException, ParseException
    {
        File file1 = new File(dirMc, "launcher_profiles.json");

        if (file1.exists() && file1.isFile())
        {
            String s = Utils.readFile(file1, "UTF-8");
            JSONParser jsonparser = new JSONParser();
            JSONObject jsonobject = (JSONObject)jsonparser.parse(s);
            JSONObject jsonobject1 = (JSONObject)jsonobject.get("profiles");
            JSONObject jsonobject2 = (JSONObject)jsonobject1.get("OptiFine");

            if (jsonobject2 == null)
            {
                jsonobject2 = new JSONObject();
                jsonobject2.put("name", "OptiFine");
                jsonobject1.put("OptiFine", jsonobject2);
            }

            jsonobject2.put("lastVersionId", mcVerOf);
            jsonobject.put("selectedProfile", "OptiFine");
            FileOutputStream fileoutputstream = new FileOutputStream(file1);
            OutputStreamWriter outputstreamwriter = new OutputStreamWriter(fileoutputstream, "UTF-8");
            JSONWriter jsonwriter = new JSONWriter(outputstreamwriter);
            jsonwriter.writeObject(jsonobject);
            outputstreamwriter.flush();
            outputstreamwriter.close();
        }
        else
        {
            Utils.showErrorMessage("File not found: " + file1);
            throw new RuntimeException("QUIET");
        }
    }

    private static void updateJson(File dirMcVers, String mcVerOf, File dirMcLib, String mcVer, String ofEd) throws IOException, ParseException
    {
        File file1 = new File(dirMcVers, mcVerOf);
        File file2 = new File(file1, mcVerOf + ".json");
        String s = Utils.readFile(file2, "UTF-8");
        JSONParser jsonparser = new JSONParser();
        JSONObject jsonobject = (JSONObject)jsonparser.parse(s);
        jsonobject.put("id", mcVerOf);
        JSONArray jsonarray = (JSONArray)jsonobject.get("libraries");
        jsonobject.put("inheritsFrom", mcVer);
        jsonarray = new JSONArray();
        jsonobject.put("libraries", jsonarray);
        String s1 = (String)jsonobject.get("mainClass");

        if (!s1.startsWith("net.minecraft.launchwrapper."))
        {
            s1 = "net.minecraft.launchwrapper.Launch";
            jsonobject.put("mainClass", s1);
            String s2 = (String)jsonobject.get("minecraftArguments");
            s2 = s2 + "  --tweakClass optifine.OptiFineTweaker";
            jsonobject.put("minecraftArguments", s2);
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("name", "net.minecraft:launchwrapper:1.12");
            jsonarray.add(0, jsonobject1);
        }

        JSONObject jsonobject2 = new JSONObject();
        jsonobject2.put("name", "optifine:OptiFine:" + mcVer + "_" + ofEd);
        jsonarray.add(0, jsonobject2);
        FileOutputStream fileoutputstream = new FileOutputStream(file2);
        OutputStreamWriter outputstreamwriter = new OutputStreamWriter(fileoutputstream, "UTF-8");
        JSONWriter jsonwriter = new JSONWriter(outputstreamwriter);
        jsonwriter.writeObject(jsonobject);
        outputstreamwriter.flush();
        outputstreamwriter.close();
    }

    public static String getOptiFineEdition(String[] ofVers)
    {
        if (ofVers.length <= 2)
        {
            return "";
        }
        else
        {
            String s = "";

            for (int i = 2; i < ofVers.length; ++i)
            {
                if (i > 2)
                {
                    s = s + "_";
                }

                s = s + ofVers[i];
            }

            return s;
        }
    }

    private static boolean installOptiFineLibrary(String mcVer, String ofEd, File dirMcLib, boolean selectTarget) throws Exception
    {
        File file1 = getOptiFineZipFile();
        File file2 = new File(dirMcLib, "optifine/OptiFine/" + mcVer + "_" + ofEd);
        File file3 = new File(file2, "OptiFine-" + mcVer + "_" + ofEd + ".jar");

        if (selectTarget)
        {
            file3 = new File(file1.getParentFile(), "OptiFine_" + mcVer + "_" + ofEd + "_MOD.jar");
            JFileChooser jfilechooser = new JFileChooser(file3.getParentFile());
            jfilechooser.setSelectedFile(file3);
            int i = jfilechooser.showSaveDialog((Component)null);

            if (i != 0)
            {
                return false;
            }

            file3 = jfilechooser.getSelectedFile();

            if (file3.exists())
            {
                JOptionPane.setDefaultLocale(Locale.ENGLISH);
                int j = JOptionPane.showConfirmDialog((Component)null, "The file \"" + file3.getName() + "\" already exists.\nDo you want to overwrite it?", "Save", 1);

                if (j != 0)
                {
                    return false;
                }
            }
        }

        if (file3.equals(file1))
        {
            JOptionPane.showMessageDialog((Component)null, "Source and target file are the same.", "Save", 0);
            return false;
        }
        else
        {
            Utils.dbg("Source: " + file1);
            Utils.dbg("Dest: " + file3);
            File file4 = dirMcLib.getParentFile();
            File file5 = new File(file4, "versions/" + mcVer + "/" + mcVer + ".jar");

            if (!file5.exists())
            {
                showMessageVersionNotFound(mcVer);
                throw new RuntimeException("QUIET");
            }
            else
            {
                if (file3.getParentFile() != null)
                {
                    file3.getParentFile().mkdirs();
                }

                Patcher.process(file5, file1, file3);
                return true;
            }
        }
    }

    public static File getOptiFineZipFile() throws Exception
    {
        URL url = Installer.class.getProtectionDomain().getCodeSource().getLocation();
        Utils.dbg("URL: " + url);
        URI uri = url.toURI();
        File file1 = new File(uri);
        return file1;
    }

    public static boolean isPatchFile() throws Exception
    {
        File file1 = getOptiFineZipFile();
        ZipFile zipfile = new ZipFile(file1);

        try
        {
            Enumeration<ZipEntry> enumeration = (Enumeration<ZipEntry>) zipfile.entries();

            while (enumeration.hasMoreElements())
            {
                ZipEntry zipentry = (ZipEntry)enumeration.nextElement();

                if (zipentry.getName().startsWith("patch/"))
                {
                    return true;
                }
            }

            return false;
        }
        finally
        {
            zipfile.close();
        }
    }

    private static void copyMinecraftVersion(String mcVer, String mcVerOf, File dirMcVer) throws IOException
    {
        File file1 = new File(dirMcVer, mcVer);

        if (!file1.exists())
        {
            showMessageVersionNotFound(mcVer);
            throw new RuntimeException("QUIET");
        }
        else
        {
            File file2 = new File(dirMcVer, mcVerOf);
            file2.mkdirs();
            Utils.dbg("Dir version MC: " + file1);
            Utils.dbg("Dir version MC-OF: " + file2);
            File file3 = new File(file1, mcVer + ".jar");
            File file4 = new File(file2, mcVerOf + ".jar");

            if (!file3.exists())
            {
                showMessageVersionNotFound(mcVer);
                throw new RuntimeException("QUIET");
            }
            else
            {
                Utils.copyFile(file3, file4);
                File file5 = new File(file1, mcVer + ".json");
                File file6 = new File(file2, mcVerOf + ".json");
                Utils.copyFile(file5, file6);
            }
        }
    }

    private static void showMessageVersionNotFound(String mcVer)
    {
        Utils.showErrorMessage("Minecraft version " + mcVer + " not found.\nYou need to start the version " + mcVer + " manually once.");
    }

    public static String getOptiFineVersion() throws IOException
    {
        InputStream inputstream = Installer.class.getResourceAsStream("/Config.class");

        if (inputstream == null)
        {
            inputstream = Installer.class.getResourceAsStream("/VersionThread.class");
        }

        return getOptiFineVersion(inputstream);
    }

    public static String getOptiFineVersion(ZipFile zipFile) throws IOException
    {
        ZipEntry zipentry = zipFile.getEntry("Config.class");

        if (zipentry == null)
        {
            zipentry = zipFile.getEntry("VersionThread.class");
        }

        if (zipentry == null)
        {
            return null;
        }
        else
        {
            InputStream inputstream = zipFile.getInputStream(zipentry);
            String s = getOptiFineVersion(inputstream);
            inputstream.close();
            return s;
        }
    }

    public static String getOptiFineVersion(InputStream in) throws IOException
    {
        byte[] abyte = Utils.readAll(in);
        byte[] abyte1 = "OptiFine_".getBytes("ASCII");
        int i = Utils.find(abyte, abyte1);

        if (i < 0)
        {
            return null;
        }
        else
        {
            for (i = i; i < abyte.length; ++i)
            {
                byte b0 = abyte[i];

                if (b0 < 32 || b0 > 122)
                {
                    break;
                }
            }

            String s = new String(abyte, i, i - i, "ASCII");
            return s;
        }
    }

    public static String getMinecraftVersionFromOfVersion(String ofVer)
    {
        if (ofVer == null)
        {
            return null;
        }
        else
        {
            String[] astring = Utils.tokenize(ofVer, "_");

            if (astring.length < 2)
            {
                return null;
            }
            else
            {
                String s = astring[1];
                return s;
            }
        }
    }
}
