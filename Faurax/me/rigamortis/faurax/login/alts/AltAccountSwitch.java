package me.rigamortis.faurax.login.alts;

import org.lwjgl.input.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import javax.swing.*;
import net.minecraft.util.*;
import me.rigamortis.faurax.login.*;
import java.util.*;
import java.net.*;
import java.io.*;

public class AltAccountSwitch extends GuiScreen
{
    private GuiScreen parentScreen;
    private AltAccountSlot accountSlotContainer;
    private GuiButton buttonEdit;
    private GuiButton buttonSelect;
    private GuiButton buttonDelete;
    private List<String> accountList;
    private List<String> currentAccountList;
    private String prevSearchTerm;
    private int selectedAccount;
    private File altsFile;
    public GuiTextField searchField;
    private boolean isOpen;
    
    public AltAccountSwitch(final GuiScreen gui) {
        this.accountList = new ArrayList<String>();
        this.currentAccountList = this.accountList;
        this.prevSearchTerm = "";
        this.selectedAccount = 0;
        this.altsFile = new File("Faurax 3.6", "alts.faurax");
        this.parentScreen = gui;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.loadAccountList();
        this.accountSlotContainer = new AltAccountSlot(this);
        this.buttonList.clear();
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, "Back"));
        this.buttonList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, "Login"));
        this.buttonList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, "Delete"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, "Direct Login"));
        this.buttonList.add(this.buttonEdit = new GuiButton(5, this.width / 2 - 154, this.height - 28, 70, 20, "Edit"));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 4, this.height - 28, 70, 20, "From File"));
        this.searchField = new GuiTextField(1, Minecraft.getMinecraft().fontRendererObj, 5, this.height - 28, 75, 20);
        final boolean flag = this.selectedAccount >= 0 && this.selectedAccount < this.accountSlotContainer.getSize();
        this.buttonSelect.enabled = flag;
        this.buttonEdit.enabled = flag;
        this.buttonDelete.enabled = flag;
        this.isOpen = true;
    }
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        this.drawDefaultBackground();
        this.accountSlotContainer.drawScreen(i, j, f);
        this.drawCenteredString(Minecraft.getMinecraft().fontRendererObj, "Account Manager", this.width / 2, 20, 16777215);
        this.mc.fontRendererObj.drawString("Current Account: " + this.mc.session.getUsername(), 4, 4, 16777215);
        this.mc.fontRendererObj.drawString("Search:", 5, this.height - 45, 16777215);
        this.searchField.drawTextBox();
        if (this.searchField.getText() != this.prevSearchTerm) {
            this.currentAccountList = this.getSearchedList(this.searchField.getText());
            this.prevSearchTerm = this.searchField.getText();
        }
        super.drawScreen(i, j, f);
    }
    
    private List<String> getSearchedList(final String search) {
        final List<String> accounts = new ArrayList<String>();
        for (final String account : this.accountList) {
            if (account.split(":")[0].toLowerCase().contains(search.toLowerCase())) {
                accounts.add(account);
            }
        }
        return accounts;
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.isOpen = false;
    }
    
    @Override
    protected void actionPerformed(final GuiButton guibutton) {
        if (guibutton.enabled) {
            if (guibutton.id == 0) {
                this.mc.displayGuiScreen(this.parentScreen);
            }
            if (guibutton.id == 1) {
                this.login(this.selectedAccount);
            }
            if (guibutton.id == 2) {
                final String name = this.accountList.get(this.selectedAccount).split(":")[0];
                final String s1 = "Are you sure you want to remove this Account?";
                final String s2 = "";
                this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                    @Override
                    public void confirmClicked(final boolean delete, final int index) {
                        if (delete) {
                            AltAccountSwitch.this.getAltManager().accountList.remove(AltAccountSwitch.this.selectedAccount);
                            AltAccountSwitch.this.getAltManager().saveAccountList();
                        }
                        AltAccountSwitch.this.mc.displayGuiScreen(AltAccountSwitch.this.getAltManager());
                    }
                }, s1, s2, this.selectedAccount));
            }
            if (guibutton.id == 3) {
                this.mc.displayGuiScreen(new AltAccountEdit(this, -1));
            }
            if (guibutton.id == 4) {
                this.mc.displayGuiScreen(new GuiLogin(this));
            }
            if (guibutton.id == 5) {
                this.mc.displayGuiScreen(new AltAccountEdit(this, this.selectedAccount));
            }
            if (guibutton.id == 6) {
                final JFileChooser fc = new JFileChooser();
                final int returnVal = fc.showOpenDialog(fc);
                if (returnVal == 0) {
                    final StringBuilder data = new StringBuilder();
                    try {
                        String[] file;
                        for (int length = (file = readFile(fc.getSelectedFile().getAbsolutePath())).length, i = 0; i < length; ++i) {
                            final String s3 = file[i];
                            data.append(String.valueOf(s3) + "\n");
                            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.altsFile));
                            writer.write(data.toString().trim());
                            writer.close();
                            this.loadAccountList();
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            final int id = guibutton.id;
        }
    }
    
    protected AltAccountSwitch getAltManager() {
        return this;
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.searchField.textboxKeyTyped(par1, par2);
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        this.searchField.mouseClicked(par1, par2, par3);
        super.mouseClicked(par1, par2, par3);
    }
    
    public static String[] readFile(final String s) {
        try {
            final BufferedReader br = new BufferedReader(new FileReader(s));
            String list = "";
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                list = String.valueOf(list) + "," + sCurrentLine;
            }
            br.close();
            return list.split(",");
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void login(final int i) {
        final String[] info = this.currentAccountList.get(i).split(":");
        if (info.length == 1) {
            this.mc.session = new Session(info[0], "123", "123", "mojang");
            this.mc.displayGuiScreen(this.parentScreen);
            return;
        }
        final Login login = new Login("", "", false);
    }
    
    @Override
    public void confirmClicked(final boolean flag, final int i) {
        if (flag && i >= 0) {
            this.accountList.remove(i);
            this.currentAccountList = this.getSearchedList(this.searchField.getText());
            this.saveAccountList();
        }
        this.mc.displayGuiScreen(this);
    }
    
    private void loadAccountList() {
        try {
            if (!this.altsFile.exists()) {
                this.altsFile.createNewFile();
            }
            this.accountList.clear();
            final Scanner scanner = new Scanner(new FileReader(this.altsFile));
            scanner.useDelimiter("\n");
            while (scanner.hasNext()) {
                this.accountList.add(scanner.next().trim());
            }
            scanner.close();
        }
        catch (Exception ex) {}
    }
    
    private void saveAccountList() {
        try {
            final StringBuilder data = new StringBuilder();
            for (final String s : this.accountList) {
                data.append(String.valueOf(s) + "\n");
            }
            final BufferedWriter writer = new BufferedWriter(new FileWriter(this.altsFile));
            writer.write(data.toString());
            writer.close();
        }
        catch (Exception ex) {}
    }
    
    public void editAccount(final String account, final int i) {
        if (i < 0) {
            this.accountList.add(account);
        }
        else {
            this.accountList.set(i, account);
        }
        this.currentAccountList = this.getSearchedList(this.searchField.getText());
        this.saveAccountList();
    }
    
    public List<String> getAccountList() {
        return this.currentAccountList;
    }
    
    public int setSelectedAccount(final int var0) {
        return this.selectedAccount = var0;
    }
    
    public int getSelectedAccount() {
        return this.selectedAccount;
    }
    
    public GuiButton getButtonSelect() {
        return this.buttonSelect;
    }
    
    public GuiButton getButtonEdit() {
        return this.buttonEdit;
    }
    
    public GuiButton getButtonDelete() {
        return this.buttonDelete;
    }
    
    public String Login(final String username, final String password) {
        String resultText = "";
        final String loginpage = "http://login.minecraft.net/?user=" + username + "&password=" + password + "&version=13";
        try {
            final BufferedReader pageReader = this.read(loginpage);
            String s = "";
            while ((s = pageReader.readLine()) != null) {
                resultText = s;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resultText;
    }
    
    private BufferedReader read(final String url) throws Exception, FileNotFoundException {
        return new BufferedReader(new InputStreamReader(new URL(url).openStream()));
    }
    
    public List<String> getCurrentAccountList() {
		return currentAccountList;
	}
}
