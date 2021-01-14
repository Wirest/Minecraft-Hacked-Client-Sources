/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  org.lwjgl.input.Keyboard
 *  org.lwjgl.input.Mouse
 *  org.lwjgl.opengl.GL11
 */
package me.memewaredevs.altmanager;

import me.memewaredevs.proxymanager.GuiProxyManager;
import net.minecraft.client.gui.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GuiAltManager
        extends GuiScreen {
    private GuiButton login;
    private GuiButton remove;
    private GuiButton rename;
    private AltLoginThread loginThread;
    private int offset;
    public Alt selectedAlt = null;
    private String status = (Object) ((Object) EnumChatFormatting.GRAY) + "Idle...";

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0: {
                if (this.loginThread == null) {
                    this.mc.displayGuiScreen(new GuiMainMenu());
                    break;
                }
                if (!this.loginThread.getStatus().equals((Object) ((Object) EnumChatFormatting.AQUA) + "Logging in...") && !this.loginThread.getStatus().equals((Object) ((Object) EnumChatFormatting.RED) + "Do not hit back!" + (Object) ((Object) EnumChatFormatting.AQUA) + " Logging in...")) {
                    this.mc.displayGuiScreen(null);
                    break;
                }
                this.loginThread.setStatus((Object) ((Object) EnumChatFormatting.RED) + "Do not hit back!" + (Object) ((Object) EnumChatFormatting.AQUA) + " Logging in...");
                break;
            }
            case 1: {
                String user = this.selectedAlt.getUsername();
                String pass = this.selectedAlt.getPassword();
                this.loginThread = new AltLoginThread(user, pass);
                this.loginThread.start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.registry.remove(this.selectedAlt);
                this.status = "\u00a7aRemoved.";
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
                ArrayList<Alt> registry = AltManager.registry;
                Random random = new Random();
                Alt randomAlt = registry.get(random.nextInt(AltManager.registry.size()));
                String user2 = randomAlt.getUsername();
                String pass2 = randomAlt.getPassword();
                this.loginThread = new AltLoginThread(user2, pass2);
                this.loginThread.start();
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiProxyManager());
                break;
            }
            case 7: {
                this.mc.displayGuiScreen(new GuiMultiplayer(null));
                break;
            }
            case 8: {
                AltManager.registry.clear();
                this.status = "\u00a7bReloaded!";
            }
        }
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        if (Mouse.hasWheel()) {
            int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            } else if (wheel > 0) {
                this.offset -= 26;
                if (this.offset < 0) {
                    this.offset = 0;
                }
            }
        }
        ScaledResolution res = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        RenderUtil.rectangle(0.0, 0.0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(0));
        this.drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, 14540253);
        FontRenderer fontRendererObj = this.fontRendererObj;
        StringBuilder sb = new StringBuilder("Account Manager - ");
        this.drawCenteredString(fontRendererObj, sb.append(AltManager.registry.size()).append(" alts").toString(), this.width / 2, 10, -1);
        this.drawCenteredString(this.fontRendererObj, this.loginThread == null ? this.status : this.loginThread.getStatus(), this.width / 2, 20, -1);
        RenderUtil.rectangleBordered(50.0, 33.0, this.width - 50, this.height - 50, 1.0, Colors.getColor(25, 25, 25, 255), Colors.getColor(5, 5, 5, 255));
        GL11.glPushMatrix();
        this.prepareScissorBox(0.0f, 33.0f, this.width, this.height - 50);
        GL11.glEnable((int) 3089);
        int y2 = 38;
        for (Alt alt : AltManager.registry) {
            String pass;
            if (!this.isAltInArea(y2)) continue;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            String string = pass = alt.getPassword().equals("") ? "\u00a7cCracked" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                if (this.isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown((int) 0)) {
                    RenderUtil.rectangleBordered(52.0, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1.0, Colors.getColor(45, 45, 45, 255), -2142943931);
                } else if (this.isMouseOverAlt(par1, par2, y2 - this.offset)) {
                    RenderUtil.rectangleBordered(52.0, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1.0, Colors.getColor(45, 45, 45, 255), -2142088622);
                } else {
                    RenderUtil.rectangleBordered(52.0, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1.0, Colors.getColor(45, 45, 45, 255), -2144259791);
                }
            } else if (this.isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown((int) 0)) {
                RenderUtil.rectangleBordered(52.0, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1.0, -Colors.getColor(45, 45, 45, 255), -2146101995);
            } else if (this.isMouseOverAlt(par1, par2, y2 - this.offset)) {
                RenderUtil.rectangleBordered(52.0, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1.0, Colors.getColor(45, 45, 45, 255), -2145180893);
            }
            this.drawCenteredString(this.fontRendererObj, name, this.width / 2, y2 - this.offset, -1);
            this.drawCenteredString(this.fontRendererObj, pass, this.width / 2, y2 - this.offset + 10, 5592405);
            y2 += 26;
        }
        GL11.glDisable((int) 3089);
        GL11.glPopMatrix();
        super.drawScreen(par1, par2, par3);
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
            this.rename.enabled = true;
        } else {
            this.login.enabled = true;
            this.remove.enabled = true;
            this.rename.enabled = true;
        }
        if (Keyboard.isKeyDown((int) 200)) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        } else if (Keyboard.isKeyDown((int) 208)) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, this.width / 2 + 116, this.height - 24, 75, 20, "Cancel"));
        this.login = new GuiButton(1, this.width / 2 - 122, this.height - 48, 100, 20, "Login");
        this.buttonList.add(this.login);
        this.remove = new GuiButton(2, this.width / 2 - 40, this.height - 24, 70, 20, "Remove");
        this.buttonList.add(this.remove);
        this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 86, this.height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 16, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 122, this.height - 24, 78, 20, "Random"));
        this.rename = new GuiButton(6, this.width / 2 + 38, this.height - 24, 70, 20, "Proxies");
        this.buttonList.add(this.rename);
        this.buttonList.add(new GuiButton(7, this.width / 2 - 190, this.height - 24, 60, 20, "Play MP"));
        this.buttonList.add(new GuiButton(8, this.width / 2 - 190, this.height - 48, 60, 20, "Reload"));
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = true;
    }

    private boolean isAltInArea(int y2) {
        return y2 - this.offset <= this.height - 50;
    }

    private boolean isMouseOverAlt(int x, int y2, int y1) {
        return x >= 52 && y2 >= y1 - 4 && x <= this.width - 52 && y2 <= y1 + 20 && x >= 0 && y2 >= 33 && x <= this.width && y2 <= this.height - 50;
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3) {
        if (this.offset < 0) {
            this.offset = 0;
        }
        int y2 = 38 - this.offset;
        for (Alt alt : AltManager.registry) {
            if (this.isMouseOverAlt(par1, par2, y2)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed((GuiButton) this.buttonList.get(1));
                    return;
                }
                this.selectedAlt = alt;
            }
            y2 += 26;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void prepareScissorBox(float x, float y2, float x2, float y22) {
        ScaledResolution scale = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int factor = scale.getScaleFactor();
        GL11.glScissor((int) ((int) (x * (float) factor)), (int) ((int) (((float) scale.getScaledHeight() - y22) * (float) factor)), (int) ((int) ((x2 - x) * (float) factor)), (int) ((int) ((y22 - y2) * (float) factor)));
    }
}

