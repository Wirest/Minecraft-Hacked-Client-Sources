package moonx.ohare.client.gui.account.gui;

import moonx.ohare.client.Moonx;
import moonx.ohare.client.gui.account.gui.components.GuiAccountList;
import moonx.ohare.client.gui.account.gui.impl.GuiAddAlt;
import moonx.ohare.client.gui.account.gui.impl.GuiAltLogin;
import moonx.ohare.client.gui.account.gui.impl.GuiAlteningLogin;
import moonx.ohare.client.gui.account.gui.thread.AccountLoginThread;
import moonx.ohare.client.gui.account.system.Account;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Xen for OhareWare
 * @since 8/6/2019
 **/
public class GuiAltManager extends GuiScreen {
    public static GuiAltManager INSTANCE;
    private GuiAccountList accountList;
    private Account selectAccount = null;
    public static Account currentAccount;
    public static AccountLoginThread loginThread;
    private final Random random = new Random();

    public GuiAltManager() {
        INSTANCE = this;
    }

    public void initGui() {
        accountList = new GuiAccountList(this);
        accountList.registerScrollButtons(7, 8);
        accountList.elementClicked(-1, false, 0, 0);

        this.buttonList.add(new GuiButton(0, this.width / 2 + 158, this.height - 24, 100, 20, "Cancel"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 50, this.height - 24, 100, 20, "Remove"));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Import"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 154, this.height - 24, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 258, this.height - 24, 100, 20, "TheAltening"));
        this.buttonList.add(new GuiButton(7, this.width / 2 + 54, this.height - 24, 100, 20, "Random"));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 258, this.height - 48, 100, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(9, this.width / 2 + 158, this.height - 48, 100, 20, "Clear"));
    }

    @Override
    public void drawScreen(int p_drawScreen_1_, int p_drawScreen_2_, float p_drawScreen_3_){
        ScaledResolution scaledResolution = new ScaledResolution(mc);

        drawDefaultBackground();
        accountList.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);
        super.drawScreen(p_drawScreen_1_, p_drawScreen_2_, p_drawScreen_3_);

        String status = "Waiting for login...";

        if(loginThread != null)
            status = loginThread.getStatus();

        drawCenteredString(mc.fontRendererObj, status, scaledResolution.getScaledWidth()/2, 5, 0xFFCFCFCF);
        drawCenteredString(mc.fontRendererObj, "Accounts: " + Moonx.INSTANCE.getAccountManager().getAccounts().size(), width / 2, 15, 0xFFFFFFFF);
    }

    @Override
    public void handleMouseInput() throws IOException{
        super.handleMouseInput();
        accountList.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException{
        switch (button.id) {
            case 0:
                if (loginThread == null || !loginThread.getStatus().contains("Logging in")) {
                    mc.displayGuiScreen(new GuiMainMenu());
                }
                break;
            case 1:
                if(accountList.selected == -1)
                    return;

                loginThread = new AccountLoginThread(accountList.getSelectedAccount().getEmail(),accountList.getSelectedAccount().getPassword());
                loginThread.start();
                break;
            case 2:
                accountList.removeSelected();
                break;
            case 3:
                if (loginThread != null)
                    loginThread = null;

                mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            case 4:
                if (loginThread != null)
                    loginThread = null;

                mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            case 6:
                mc.displayGuiScreen(new GuiAlteningLogin(this));
                break;
            case 7:
                ArrayList<Account> registry = Moonx.INSTANCE.getAccountManager().getAccounts();
                if (registry.isEmpty()) return;
                Random random = new Random();
                Account randomAlt = registry.get(random.nextInt(Moonx.INSTANCE.getAccountManager().getAccounts().size()));
                if(randomAlt.isBanned())
                    return;

                currentAccount = randomAlt;
                login(randomAlt);
                break;
            case 5:
                JFrame frame = new JFrame("Import alts");
                JFileChooser chooser = new JFileChooser();
                frame.add(chooser);
                frame.pack();
                int returnVal = chooser.showOpenDialog(frame);
                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));

                    for(String line : Files.readAllLines(Paths.get(chooser.getSelectedFile().getPath()))){
                        if(!line.contains(":")) {
                            break;
                        }

                        String[] parts = line.split(":");

                        Account account = new Account(parts[0], parts[1], parts[0]);
                        Moonx.INSTANCE.getAccountManager().getAccounts().add(account);
                    }
                }

                Moonx.INSTANCE.getAccountManager().save();
                break;
            case 8:
                if(Moonx.INSTANCE.getAccountManager().getLastAlt() == null)
                    return;
                loginThread = new AccountLoginThread(Moonx.INSTANCE.getAccountManager().getLastAlt().getEmail(),Moonx.INSTANCE.getAccountManager().getLastAlt().getPassword());
                loginThread.start();
                break;
            case 9:
                if (Moonx.INSTANCE.getAccountManager().getAccounts().isEmpty()) return;
                Moonx.INSTANCE.getAccountManager().getAccounts().clear();
                break;
        }
    }

    public void login(Account account){
        loginThread = new AccountLoginThread(account.getEmail(),account.getPassword());
        loginThread.start();
    }
}
