// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.AltManager.GUI;

import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import me.CheerioFX.FusionX.utils.R2DUtils;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import me.CheerioFX.FusionX.AltManager.AltManager;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.util.EnumChatFormatting;
import me.CheerioFX.FusionX.AltManager.Alt;
import me.CheerioFX.FusionX.AltManager.AltLoginThread;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    private AltLoginThread loginThread;
    private int offset;
    public Alt selectedAlt;
    private String status;
    
    public GuiAltManager() {
        this.selectedAlt = null;
        this.status = EnumChatFormatting.GRAY + "Idle...";
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                if (!this.loginThread.getStatus().equals(EnumChatFormatting.YELLOW + "Logging in...") && !this.loginThread.getStatus().equals(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in...")) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus(EnumChatFormatting.RED + "Do not hit back!" + EnumChatFormatting.YELLOW + " Logging in...");
                break;
            }
            case 1: {
                final String user = this.selectedAlt.getUsername();
                final String pass = this.selectedAlt.getPassword();
                (this.loginThread = new AltLoginThread(user, pass)).start();
                try {
                    FusionX.theClient.fileManager.saveAlts();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                final AltManager altManager = FusionX.theClient.altManager;
                AltManager.registry.remove(this.selectedAlt);
                this.status = "§aRemoved.";
                try {
                    FusionX.theClient.fileManager.saveAlts();
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                this.selectedAlt = null;
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                final AltManager altManager2 = FusionX.theClient.altManager;
                final ArrayList<Alt> registry = AltManager.registry;
                if (registry.isEmpty()) {
                    this.status = "§cYou need at least one alt to randomize!";
                    return;
                }
                final Random random = new Random();
                final AltManager altManager3 = FusionX.theClient.altManager;
                final Alt randomAlt = registry.get(random.nextInt(AltManager.registry.size()));
                final String user2 = randomAlt.getUsername();
                final String pass2 = randomAlt.getPassword();
                (this.loginThread = new AltLoginThread(user2, pass2)).start();
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            }
            case 7: {
                final AltManager altManager4 = FusionX.theClient.altManager;
                final Alt lastAlt = AltManager.lastAlt;
                if (lastAlt != null) {
                    final String user3 = lastAlt.getUsername();
                    final String pass3 = lastAlt.getPassword();
                    (this.loginThread = new AltLoginThread(user3, pass3)).start();
                    break;
                }
                if (this.loginThread == null) {
                    this.status = "§cThere is no last used alt!";
                    break;
                }
                this.loginThread.setStatus("§cThere is no last used alt!");
                break;
            }
            case 8: {
                AltManager.registry.clear();
                try {
                    FusionX.theClient.fileManager.loadAlts();
                }
                catch (Exception e3) {
                    e3.printStackTrace();
                }
                this.status = "§eReloaded!";
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        this.drawDefaultBackground();
        this.drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, -7829368);
        final FontRenderer fontRendererObj = this.fontRendererObj;
        final StringBuilder sb = new StringBuilder("Account Manager - ");
        final AltManager altManager = FusionX.theClient.altManager;
        this.drawCenteredString(fontRendererObj, String.valueOf(AltManager.registry.size()) + " alts", this.width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, (this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.width / 2, 20, -1);
        R2DUtils.drawBorderedRect(50.0f, 33.0f, this.width - 50, this.height - 50, 1.0f, -16777216, Integer.MIN_VALUE);
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, this.width, this.height - 50);
        GL11.glEnable(3089);
        int y = 38;
        final AltManager altManager2 = FusionX.theClient.altManager;
        for (final Alt alt : AltManager.registry) {
            if (this.isAltInArea(y)) {
                String name;
                if (alt.getMask().equals("")) {
                    name = alt.getUsername();
                }
                else {
                    name = alt.getMask();
                }
                String pass;
                if (alt.getPassword().equals("")) {
                    pass = "§cCracked";
                }
                else {
                    pass = alt.getPassword().replaceAll(".", "*");
                }
                if (alt == this.selectedAlt) {
                    if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
                        R2DUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0f, -16777216, -2142943931);
                    }
                    else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                        R2DUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0f, -16777216, -2142088622);
                    }
                    else {
                        R2DUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0f, -16777216, -2144259791);
                    }
                }
                else if (this.isMouseOverAlt(par1, par2, y - this.offset) && Mouse.isButtonDown(0)) {
                    R2DUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0f, -16777216, -2146101995);
                }
                else if (this.isMouseOverAlt(par1, par2, y - this.offset)) {
                    R2DUtils.drawBorderedRect(52.0f, y - this.offset - 4, this.width - 52, y - this.offset + 20, 1.0f, -16777216, -2145180893);
                }
                this.drawCenteredString(this.fontRendererObj, name, this.width / 2, y - this.offset, -1);
                this.drawCenteredString(this.fontRendererObj, pass, this.width / 2, y - this.offset + 10, 5592405);
                y += 26;
            }
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
            this.rename.enabled = false;
        }
        else {
            this.login.enabled = true;
            this.remove.enabled = true;
            this.rename.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }
    
    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 24, 75, 20, "Cancel"));
        this.buttonList.add(this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login"));
        this.buttonList.add(this.remove = new GuiButton(2, this.width / 2 - 74, this.height - 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 154, this.height - 24, 70, 20, "Random"));
        this.buttonList.add(this.rename = new GuiButton(6, this.width / 2 + 4, this.height - 24, 70, 20, "Edit"));
        this.buttonList.add(new GuiButton(7, this.width / 2 - 206, this.height - 24, 50, 20, "Last Alt"));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 206, this.height - 48, 50, 20, "Reload"));
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
    }
    
    private boolean isAltInArea(final int y) {
        return y - this.offset <= this.height - 50;
    }
    
    private boolean isMouseOverAlt(final int x, final int y, final int y1) {
        return x >= 52 && y >= y1 - 4 && x <= this.width - 52 && y <= y1 + 20 && x >= 0 && y >= 33 && x <= this.width && y <= this.height - 50;
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) throws IOException {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y = 38 - this.offset;
        final AltManager altManager = FusionX.theClient.altManager;
        for (final Alt alt : AltManager.registry) {
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt;
            }
            y += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void prepareScissorBox(final float x, final float y, final float x2, final float y2) {
        final ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        final int factor = scale.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)((scale.getScaledHeight() - y2) * factor), (int)((x2 - x) * factor), (int)((y2 - y) * factor));
    }
}
