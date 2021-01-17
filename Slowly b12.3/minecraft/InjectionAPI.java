/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import net.minecraft.client.main.Main;

public class InjectionAPI {
    private static /* synthetic */ int[] $SWITCH_TABLE$InjectionAPI$OS;

    public static void inject() throws Exception {
        File workingDirectory;
        String userHome = System.getProperty("user.home", ".");
        switch (InjectionAPI.$SWITCH_TABLE$InjectionAPI$OS()[InjectionAPI.getPlatform().ordinal()]) {
            case 1: {
                workingDirectory = new File(userHome, ".minecraft/");
                break;
            }
            case 3: {
                String applicationData = System.getenv("APPDATA");
                String folder = applicationData != null ? applicationData : userHome;
                workingDirectory = new File(folder, ".minecraft/");
                break;
            }
            case 4: {
                workingDirectory = new File(userHome, "Library/Application Support/minecraft");
                break;
            }
            default: {
                workingDirectory = new File(userHome, "minecraft/");
            }
        }
        try {
            Main.main(new String[]{"--version", "Slowly", "--accessToken", "0", "--assetIndex", "1.8", "--userProperties", "{}", "--gameDir", new File(workingDirectory, ".").getAbsolutePath(), "--assetsDir", new File(workingDirectory, "assets/").getAbsolutePath()});
        }
        catch (Exception e1) {
            try {
                PrintWriter writer = new PrintWriter("C:\\AntiLeak\\error.txt", "UTF-8");
                writer.println(e1);
                writer.close();
                Thread.sleep(10000L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public static OS getPlatform() {
        String s = System.getProperty("os.name").toLowerCase();
        return s.contains("win") ? OS.WINDOWS : (s.contains("mac") ? OS.MACOS : (s.contains("solaris") ? OS.SOLARIS : (s.contains("sunos") ? OS.SOLARIS : (s.contains("linux") ? OS.LINUX : (s.contains("unix") ? OS.LINUX : OS.UNKNOWN)))));
    }

    static /* synthetic */ int[] $SWITCH_TABLE$InjectionAPI$OS() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$InjectionAPI$OS;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[OS.values().length];
        try {
            arrn[OS.LINUX.ordinal()] = 1;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[OS.MACOS.ordinal()] = 4;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[OS.SOLARIS.ordinal()] = 2;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[OS.UNKNOWN.ordinal()] = 5;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        try {
            arrn[OS.WINDOWS.ordinal()] = 3;
        }
        catch (NoSuchFieldError noSuchFieldError) {}
        $SWITCH_TABLE$InjectionAPI$OS = arrn;
        return $SWITCH_TABLE$InjectionAPI$OS;
    }

    public static enum OS {
    	LINUX("LINUX", 0), 
        SOLARIS("SOLARIS", 1), 
        WINDOWS("WINDOWS", 2), 
        MACOS("MACOS", 3), 
        UNKNOWN("UNKNOWN", 4);
        

        private OS(String string2, int n2) {
        }
    }

}

