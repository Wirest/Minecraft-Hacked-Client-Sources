package info.sigmaclient.gui.screen.impl.mainmenu;

import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.GuiLogin;
import info.sigmaclient.gui.screen.PanoramaScreen;
import info.sigmaclient.management.SubFolder;
import info.sigmaclient.management.users.UserManager;
import net.minecraft.client.Minecraft;

import org.apache.commons.io.FileUtils;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientMainMenu extends PanoramaScreen {
    private static int key = Keyboard.KEY_GRAVE;
    private static final GuiVanillaMainMenu menuVanilla = new GuiVanillaMainMenu();
    private static final GuiModdedMainMenu menuModded = new GuiModdedMainMenu();

    public void initGui() {
        load();
        if (getClass().equals(ClientMainMenu.class)) {
            display();
        }
    }

    private void load() {
        String file = "";
        try {
            file = FileUtils.readFileToString(getFile());
        } catch (IOException e) {
            return;
        }
        Client.hasSetup = false;
        for (String line : file.split("\n")) {
            if (line.contains("key")) {
                String[] split = line.split(":");
                if (split.length > 1) {
                    try {
                        key = Integer.parseInt(split[1]);
                    } catch (NumberFormatException n) {
                    }
                }
            }
            if (line.contains("setup")) {
                String[] split = line.split(":");
                if (split.length > 1) {
                    Client.hasSetup = Boolean.parseBoolean(split[1]);
                }
            }
            if (line.contains("lowend")) {
                String[] split = line.split(":");
                if (split.length > 1) {
                	if(split[1].contains("true")){
                		Client.isLowEndPC = true;
                	}else{
                		Client.isLowEndPC = false;
                	}
                    
                }
            }
/*            if (line.contains("notifications")) {
                String[] split = line.split(":");
                if (split.length > 1) {
                    Client.isLowEndPC = Boolean.parseBoolean(split[1]);
                }
            }*/
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == key) {
            toggleVanilla();
            display();
        }
    }

    private void display() {
        if (Client.isHidden()) {
            Minecraft.getMinecraft().displayGuiScreen(menuVanilla);
        } else {//menuModded
            Minecraft.getMinecraft().displayGuiScreen(Client.um.isFinishedLoginSequence() ? new GuiModdedMainMenu() : new GuiLoginMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void toggleVanilla() {
        Client.setHidden(!Client.isHidden());
        save();
    }

    public static void save() {
        List<String> fileContent = new ArrayList<>();
        fileContent.add(String.format("%s:%s", "key", key));
        fileContent.add(String.format("%s:%s", "setup", Client.hasSetup));
        fileContent.add(String.format("%s:%s", "lowend", Client.isLowEndPC));
        info.sigmaclient.util.FileUtils.write(getFile(), fileContent, true);
    }

    public static File getFile() {
        File file = new File(getFolder().getAbsolutePath() + File.separator + "MainMenu.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static File getFolder() {
        File folder = new File(Client.getDataDir().getAbsolutePath() + File.separator + SubFolder.Other.getFolderName());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        return folder;
    }
}
