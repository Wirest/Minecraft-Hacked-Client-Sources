package com.etb.client.gui.alt;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import com.etb.client.Client;
import com.etb.client.gui.alt.account.Account;
import com.etb.client.gui.alt.impl.*;
import com.etb.client.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class GuiAccountManager extends GuiScreen {
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    private GuiButton random;
    private AuthThread loginThread;
    private int offset;
    public Account selectedAlt = null;
    private String status = ChatFormatting.GRAY + "Idle...";

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                if (loginThread == null) mc.displayGuiScreen(null);
                else if ((!loginThread.getStatus().equals(ChatFormatting.AQUA + "Logging in...")) && (!loginThread.getStatus().equals(ChatFormatting.RED + "Do not hit back!" + ChatFormatting.AQUA + " Logging in..."))) {
                    mc.displayGuiScreen(null);
                } else {
                    loginThread.setStatus(ChatFormatting.RED + "Do not hit back!" + ChatFormatting.AQUA + " Logging in...");
                }
                break;
            case 1:
                String user = selectedAlt.getUsername();
                String pass = selectedAlt.getPassword();
                loginThread = new AuthThread(user, pass);
                loginThread.start();
                break;
            case 2:
                if (loginThread != null) {
                    loginThread = null;
                }
                Client.INSTANCE.getAccountManager().getAccounts().remove(selectedAlt);
                status = "\247aRemoved.";
                Client.INSTANCE.getAccountManager().getAltSaving().saveFile();

                selectedAlt = null;
                break;
            case 3:
                mc.displayGuiScreen(new GuiAddAccount(this));
                break;
            case 4:
                mc.displayGuiScreen(new GuiAccountLogin(this));
                break;
            case 5:
                Account randomAlt = Client.INSTANCE.getAccountManager().getAccounts().get(new java.util.Random().nextInt(Client.INSTANCE.getAccountManager().getAccounts().size()));
                String user1 = randomAlt.getUsername();
                String pass1 = randomAlt.getPassword();
                loginThread = new AuthThread(user1, pass1);
                loginThread.start();
                break;
            case 6:
                mc.displayGuiScreen(new GuiRenameAccount(this));
                break;
            case 7:
                Account lastAlt = Client.INSTANCE.getAccountManager().getLastAlt();
                if (lastAlt == null) {
                    status = "\247cThere is no last used alt!";
                } else {
                    String user2 = lastAlt.getUsername();
                    String pass2 = lastAlt.getPassword();
                    loginThread = new AuthThread(user2, pass2);
                    loginThread.start();
                }
                break;
            case 8:
                Client.INSTANCE.getAccountManager().getAccounts().clear();
                Client.INSTANCE.getAccountManager().getAltSaving().loadFile();
                status = "\247bReloaded!";
                break;
            case 9:
                JFrame frame = new JFrame("Import");
                frame.setAlwaysOnTop(true);
                AccountImport accountImport = new AccountImport();
                frame.setContentPane(accountImport);
                new Thread(() -> accountImport.openButton.doClick()).start();
                break;
            case 10:
                if (!Client.INSTANCE.getAccountManager().getAccounts().isEmpty()) {
                    Client.INSTANCE.getAccountManager().getAccounts().clear();
                    Client.INSTANCE.getAccountManager().getAltSaving().saveFile();
                }
                break;
            case 11:
                mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 12:
                mc.displayGuiScreen(new GuiAlteningLogin(this));
                break;
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {

        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                offset += 26;
                if (offset < 0) {
                    offset = 0;
                }
            } else if (wheel > 0) {
                offset -= 26;
                if (offset < 0) {
                    offset = 0;
                }
            }
        }
        drawDefaultBackground();
        Minecraft.getMinecraft().fontRendererObj.drawString(mc.session.getUsername(), 10, 10, 0xDDDDDD);
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString("Account Manager - " + Client.INSTANCE.getAccountManager().getAccounts().size() + " alts", width / 2, 10, -1);
        Minecraft.getMinecraft().fontRendererObj.drawCenteredString(loginThread == null ? status : loginThread.getStatus(), width / 2, 20, -1);
        RenderUtil.drawBorderedRect(50.0F, 33.0F, width - 100, height - 90, 1.0F, new Color(25, 25, 25, 255).getRGB(), new Color(15, 15, 15, 255).getRGB());
        GL11.glPushMatrix();
        prepareScissorBox(0.0F, 33.0F, width, height - 50);
        GL11.glEnable(3089);
        int y = 38;

        for (Account alt : Client.INSTANCE.getAccountManager().getAccounts()) {
            if (isAltInArea(y)) {
                String name;
                if (alt.getMask().equals("")) {
                    name = alt.getUsername();
                } else name = alt.getMask();
                String pass;
                if (alt.getPassword().equals("")) {
                    pass = "\247cCracked";
                } else {
                    pass = alt.getPassword().replaceAll(".", "*");
                }
                if (alt == selectedAlt) {
                    if ((isMouseOverAlt(par1, par2, y - offset)) && (Mouse.isButtonDown(0))) {
                        RenderUtil.drawBorderedRect(52.0F, y - offset - 4, width - 104,   20, 1.0F, new Color(45, 45, 45, 255).getRGB(), -2142943931);
                    } else if (isMouseOverAlt(par1, par2, y - offset)) {
                        RenderUtil.drawBorderedRect(52.0F, y - offset - 4, width - 104,   20, 1.0F, new Color(45, 45, 45, 255).getRGB(), -2142088622);
                    } else {
                        RenderUtil.drawBorderedRect(52.0F, y - offset - 4, width - 104,   20, 1.0F, new Color(45, 45, 45, 255).getRGB(), -2144259791);
                    }
                } else if ((isMouseOverAlt(par1, par2, y - offset)) && (Mouse.isButtonDown(0))) {
                    RenderUtil.drawBorderedRect(52.0F, y - offset - 4, width - 104,   20, 1.0F, -new Color(45, 45, 45, 255).getRGB(), -2146101995);
                } else if (isMouseOverAlt(par1, par2, y - offset)) {
                    RenderUtil.drawBorderedRect(52.0F, y - offset - 4, width - 104,  20, 1.0F, new Color(45, 45, 45, 255).getRGB(), -2145180893);
                }
                Minecraft.getMinecraft().fontRendererObj.drawCenteredString(name, width / 2, y - offset, -1);
                Minecraft.getMinecraft().fontRendererObj.drawCenteredString(pass, width / 2, y - offset + 10, 5592405);
                y += 26;
            }
        }

        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (selectedAlt == null) {
            login.enabled = false;
            remove.enabled = false;
            rename.enabled = false;
        } else {
            login.enabled = true;
            remove.enabled = true;
            rename.enabled = true;
        }
        if (Client.INSTANCE.getAccountManager().getAccounts().isEmpty()) {
            random.enabled = false;
        } else {
            random.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            offset -= 26;
            if (offset < 0) {
                offset = 0;
            }
        } else if (Keyboard.isKeyDown(208)) {
            offset += 26;
            if (offset < 0) {
                offset = 0;
            }
        }
    }

    @Override
    public void initGui() {
        buttonList.add(new GuiButton(0, width / 2 + 116, height - 24, 75, 20, "Cancel"));
        buttonList.add(login = new GuiButton(1, width / 2 - 122, height - 48, 100, 20, "Login"));
        buttonList.add(remove = new GuiButton(2, width / 2 - 40, height - 24, 70, 20, "Remove"));
        buttonList.add(new GuiButton(3, width / 2 + 4 + 86, height - 48, 100, 20, "Add"));
        buttonList.add(new GuiButton(4, width / 2 - 16, height - 48, 100, 20, "Direct Login"));
        buttonList.add(random = new GuiButton(5, width / 2 - 122, height - 24, 78, 20, "Random"));
        buttonList.add(rename = new GuiButton(6, width / 2 + 38, height - 24, 70, 20, "Edit"));
        buttonList.add(new GuiButton(7, width / 2 - 190, height - 24, 60, 20, "Last Alt"));
        buttonList.add(new GuiButton(8, width / 2 - 190, height - 48, 60, 20, "Reload"));
        buttonList.add(new GuiButton(9, width / 2 - 247, height - 24, 50, 20, "Import"));
        buttonList.add(new GuiButton(10, width / 2 - 247, height - 48, 50, 20, "Clear"));
        buttonList.add(new GuiButton(11, width / 2 + 198, height - 24, 75, 20, "MultiPlayer"));
        buttonList.add(new GuiButton(12, width / 2 + 198, height - 48, 75, 20, "TheAltening"));
        login.enabled = false;
        remove.enabled = false;
        rename.enabled = false;
    }

    private boolean isAltInArea(int y) {
        return y - offset <= height - 60;
    }

    private boolean isMouseOverAlt(int x, int y, int y1) {
        return (x >= 52) && (y >= y1 - 4) && (x <= width - 52) && (y <= y1 + 20) && (x >= 0) && (y >= 33) && (x <= width) && (y <= height - 60);
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (offset < 0) {
            offset = 0;
        }
        int y = 38 - offset;
        for (Account alt : Client.INSTANCE.getAccountManager().getAccounts()) {
            if (isMouseOverAlt(par1, par2, y)) {
                if (alt == selectedAlt) {
                    actionPerformed(buttonList.get(1));
                    return;
                }
                selectedAlt = alt;
            }
            y += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
        }
    }

    private void prepareScissorBox(float x, float y, float x2, float y2) {
        ScaledResolution scale = new ScaledResolution(mc);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) (x * factor), (int) ((scale.getScaledHeight() - y2) * factor), (int) ((x2 - x) * factor), (int) ((y2 - y) * factor));
    }

}
