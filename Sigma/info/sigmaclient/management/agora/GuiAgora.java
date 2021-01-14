package info.sigmaclient.management.agora;

import static org.lwjgl.opengl.GL11.GL_SCISSOR_TEST;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glScissor;
import info.sigmaclient.Client;
import info.sigmaclient.gui.screen.impl.mainmenu.GuiModdedMainMenu;
import info.sigmaclient.management.agora.component.AgoraBackButton;
import info.sigmaclient.management.agora.component.AgoraChannel;
import info.sigmaclient.management.agora.component.AgoraChannelButton;
import info.sigmaclient.management.agora.component.AgoraChatMessage;
import info.sigmaclient.management.agora.component.AgoraLightButton;
import info.sigmaclient.management.agora.component.AgoraMembersButton;
import info.sigmaclient.management.agora.component.AgoraRank;
import info.sigmaclient.management.animate.Translate;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.Timer;
import info.sigmaclient.util.misc.ChatUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;

import java.awt.Color;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;


public class GuiAgora extends GuiScreen {

    protected GuiTextField inputField;
    public static AgoraChannel selectedChannel;
    public static boolean showMembers = true;
    public static boolean dark = true;
    private final ResourceLocation sigmaIcon = new ResourceLocation("textures/menu/big.png");
    private int mWheel, cWheel, aWheel;
    private Translate translate, mWheelT, cWheelT, aWheelT;
    private final GuiScreen parent;

    public GuiAgora(GuiScreen parent) {
        while (Agora.getInstance().getChannels().isEmpty()) {//TODO Faire un truc propre
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        selectedChannel = Agora.getInstance().getChannels().get(0);
        mWheel = 0;
        mWheelT = new Translate(0, 0);
        cWheelT = new Translate(0, 0);
        aWheelT = new Translate(0, 0);
        this.parent = parent;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return true;
    }

    public void initGui() {
        if (!Agora.getInstance().getChannels().contains(selectedChannel)) {
            selectedChannel = Agora.getInstance().getChannels().get(0);
        }

        translate = new Translate(showMembers ? this.width / 8 : 0, dark ? 0 : 255);
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        int y = 35;
        for (int i = 0; i < Agora.getInstance().getChannels().size(); i++) {
            this.buttonList.add(new AgoraChannelButton(i, 3, y, width / 8 - 10, 20, Agora.getInstance().getChannels().get(i).getName())); //Ajout des channels
            y += 20;
        }
        this.buttonList.add(new AgoraMembersButton(101, (int) (this.width * (7d / 8) - 30), 2, "Show Members"));
        this.buttonList.add(new AgoraLightButton(102, (int) (this.width * (7d / 8) - 55), 3, "Light Mode"));
        this.buttonList.add(new AgoraBackButton(103, 2, 2, "Back"));
        this.inputField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 8 + 20, this.height - 25, (int) (this.width * (3 / 4d)) - 25, 20);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setMaxStringLength(200);
    }


    public void drawScreen(final int mouseX, final int mouseY, final float f) {
        translate.interpolate(showMembers ? 0 : this.width / 8, dark ? 0 : 255, 10);
        inputField.width = (int) (this.width * (3d / 4d) - 32 + translate.getX());
        mWheelT.interpolate(0, mWheel, 10);
        cWheelT.interpolate(0, cWheel, 6);
        aWheelT.interpolate(0, aWheel, 10);
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), res.getScaledHeight(), Colors.getColor(getBrightness(80))); //fond
        RenderingUtil.rectangle(0, 0, res.getScaledWidth(), 30, Colors.getColor(getBrightness(dark ? 68 : -190))); //barre du haut

        RenderingUtil.drawRoundedRect(inputField.xPosition - 7, inputField.yPosition - 5, inputField.width + 7, inputField.height, Colors.getColor(dark ? 120 : 190));
        TTFFontRenderer font = Client.fm.getFont("SFM 8");

        TTFFontRenderer title = Client.fm.getFont("SFL 17");
        TTFFontRenderer head = Client.fm.getFont("SFM 11");

        if (!selectedChannel.canTalk()) {
            if (this.inputField.isFocused())
                this.inputField.setFocused(false);
            if (this.inputField.isEnabled())
                this.inputField.setEnabled(false);
            if (!this.inputField.getText().isEmpty())
                this.inputField.setText("");
            font.drawString("You are not allowed to talk in #" + selectedChannel.getName(), inputField.xPosition, inputField.yPosition + 3, Colors.getColor(180, 50, 50));
        } else {
            if (!this.inputField.isEnabled())
                this.inputField.setEnabled(true);

            if (inputField.getText().isEmpty() && !inputField.isFocused()) {
                font.drawString("Send a message in #" + selectedChannel.getName(), inputField.xPosition, inputField.yPosition + 3, Colors.getColor(dark ? 200 : 100));
            }
        }
        inputField.drawTextBox();
        RenderingUtil.rectangle(0, 0, this.width / 8, this.height, Colors.getColor(getBrightness(dark ? 60 : -290)));
        final int wheelR = Mouse.getDWheel();
        drawMembers(translate.getX(), mouseX, mouseY, res, wheelR);

        RenderingUtil.drawHLine(0, 30, res.getScaledWidth(), 30, 1, Colors.getColor(getBrightness(dark ? 50 : -50)));
        title.drawString("#", this.width / 8 + 14, 10, Colors.getColor(dark ? 180 : 130));
        head.drawString(selectedChannel.getName(), this.width / 8 + 29, 12, Colors.getColor(255));
        GL11.glPushMatrix();
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(sigmaIcon);
        drawModalRectWithCustomSizedTexture((int) (width * (7d / 8)) + 2, 0, 0, 0, (int) (this.width / 8.1d), 30, (int) (this.width / 8.1d), 30);
        GlStateManager.disableBlend();
        GL11.glPopMatrix();

        drawChat(mouseX, mouseY, res, wheelR);
        drawChannels(mouseX, mouseY, res, wheelR);
        //super.drawScreen(mouseX, mouseY, f);
    }

    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }

    public void drawChannels(final int mouseX, final int mouseY, ScaledResolution res, int wheelR) {
        int i, y = 0, s = res.getScaleFactor();
        for (i = 0; i < this.buttonList.size(); ++i) {
            GuiButton button = (GuiButton) this.buttonList.get(i);
            if (button instanceof AgoraChannelButton) {
                GL11.glPushMatrix();
                GL11.glScissor(0, 0, (int) (this.width / 8D) * s, (int) (this.height - 31) * s);
                GL11.glEnable(GL_SCISSOR_TEST);
                y += button.getButtonHeight();
                ((AgoraChannelButton) button).setYOff(aWheel);
                button.drawButton(this.mc, mouseX, mouseY);
                GL11.glDisable(GL_SCISSOR_TEST);
                GL11.glPopMatrix();
            } else {
                button.drawButton(this.mc, mouseX, mouseY);
            }

        }
        boolean hovered = mouseX > 0 && mouseX < this.width * (1D / 8) && (mouseY > 30);
        if (Mouse.hasWheel()) {

            if (hovered) {
                if (wheelR < 0 && y > this.height) {
                    aWheel -= 18;

                } else if (wheelR > 0) {
                    aWheel += 18;

                }
                if (aWheel + y + 40 < this.height)
                    aWheel = this.height - y - 40;
                if (aWheel > 0)
                    aWheel = 0;
            }
        }
    }

    public void drawChat(final int mouseX, final int mouseY, ScaledResolution res, int wheelR) {
        TTFFontRenderer fontb = Client.fm.getFont("SFB 9");
        TTFFontRenderer fontl = Client.fm.getFont("SFR 7");
        TTFFontRenderer font = Client.fm.getFont("SFM 8");
        Queue<AgoraChatMessage> messages = selectedChannel.getMessages();
        float y = this.height - 50;
        ArrayList<AgoraChatMessage> list = new ArrayList<>(messages);
        Collections.reverse(list);
        float x = this.width / 8f + 12;
        float x2 = this.width * (7f / 8) + translate.getX();
        AgoraChatMessage checkPoint = null;
        int s = res.getScaleFactor();
        Format format = new SimpleDateFormat("HH:mm:ss (MM dd yyyy)");
     
        RenderingUtil.drawHLine(x, this.height - 40, this.width * (7D / 8) - 12 + translate.getX(), this.height - 40, 1, Colors.getColor(100));
        GL11.glPushMatrix();
        GL11.glScissor((int) (x - 12) * s, 40 * s, (int) (this.width * (6 / 8d) + translate.getX()) * s, (this.height - 71) * s);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glTranslated(0, cWheelT.getY(), 0);
        for (int i = 0; i < list.size(); i++) {
            AgoraChatMessage mess = list.get(i);
            AgoraChatMessage last = i == list.size() - 1 ? null : list.get(i + 1);
            Date date = new Date(mess.getTimestamp());      
            String[] splt = mess.getMessage().split(" ");
            String[] lines = new String[20];

            do {
                mess = list.get(i);
                last = i == list.size() - 1 ? null : list.get(i + 1);
                splt = mess.getMessage().split(" ");
                lines = new String[20];
                int off = 0;
                float xOff = this.width / 8f + 12;
                for (int j = 0; j < splt.length; j++) {
                    xOff += font.getWidth(splt[j] + " ");
                    if (xOff > x2 && j != 0) {
                        off++;
                        xOff = x;
                        j--;
                    } else {
                        String add = lines[off] == null ? splt[j] + " " : lines[off] + splt[j] + " ";
                        lines[off] = add;
                    }
                }
                for (int j = lines.length - 2; j >= 0; j--) {
                    if (lines[j] != null) {
                        font.drawString(lines[j], x + 5, y, Colors.getColor(dark ? -1 : 120));
                   
                        y -= 10;
                    }
                }
                i++;


            } while (last != null && last.getUsername().equals(mess.getUsername()));
            i--;
            int c = -1;
            if (mess.getRank() != null)
                c = mess.getRank().getColor();
            fontb.drawStringWithShadow(mess.getUsername(), x, y - 2, 0xff000000 | c);
            fontl.drawString(format.format(date), x + fontb.getWidth(mess.getUsername()) + 2, y - 2, Colors.getColor(170));
            RenderingUtil.drawHLine(x, y - 7, this.width * (7D / 8) - 12 + translate.getX(), y - 7, 1, Colors.getColor(90));
            y -= 15;
        }
        GL11.glTranslated(0, -cWheelT.getY(), 0);
        boolean hovered = mouseX > this.width * (1D / 8) && mouseX < this.width * (7D / 8) && (mouseY > 30);
        if (Mouse.hasWheel()) {
            if (hovered) {
                if (wheelR < 0) {
                    cWheel -= 18 * (-wheelR / 120d);

                } else if (wheelR > 0 && y < 30) {
                    cWheel += 18 * (wheelR / 120d);

                }
                if (cWheel < 0)
                    cWheel = 0;
                if (y + cWheel > 30 && y < 30)
                    cWheel = 30 - (int) y;
            }
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GL11.glPopMatrix();
    }

    public void drawMembers(double offset, final int mouseX, final int mouseY, ScaledResolution res, int wheelR) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(offset, 0, 0);
        RenderingUtil.rectangle(this.width * (7d / 8d), 30, this.width, this.height, Colors.getColor(getBrightness(dark ? 60 : -190)));
        GlStateManager.translate(0, mWheel, 0);
        int y = 40;

        TTFFontRenderer font = Client.fm.getFont("SFB 9");
        TTFFontRenderer font2 = Client.fm.getFont("SFB 8");
        int x = (int) (this.width * (7d / 8d) + 10);
        CopyOnWriteArrayList<AgoraRank> ranks = Agora.getInstance().getRanks();

        glPushMatrix();
        int s = res.getScaleFactor();
        glScissor((int) (this.width * (7d / 8d)) * s, 0, (int) (this.width / 8d) * s, (this.height - 31) * s);
        glEnable(GL_SCISSOR_TEST);
        for (AgoraRank rank : ranks) {
            List<String> players = rank.getOnlineMembers();
            int size = players.size();
            font.drawString(rank.getName(), x, y, Colors.getColor(new Color(rank.getColor())));
            font.drawString(" - " + size, x + font.getWidth(rank.getName()), y, Colors.getColor(dark ? 180 : 130));
            RenderingUtil.drawHLine(x - 3, y + 8, this.width - 6, y + 8, 1, Colors.getColor(dark ? 100 : 150));
            y += 12;
            for (String str : players) {
                font2.drawString(str, x + 4, y, Colors.getColor(dark ? 180 : 130));
                y += 10;
            }
        }

        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
        boolean depass = y > this.height;
        GlStateManager.translate(0, -mWheel, 0);
        GlStateManager.translate(-offset, 0, 0);
        GlStateManager.popMatrix();
        boolean hovered = mouseX > this.width * (7D / 8) && (mouseY > 30);
        if (Mouse.hasWheel()) {
            if (hovered) {
                if (wheelR < 0) {
                    if (mWheel + y > this.height)
                        mWheel -= 18;
                    else
                        mWheel = this.height - y;

                } else if (wheelR > 0) {
                    mWheel += 18;

                }
                if (mWheel > 0)
                    mWheel = 0;
            }
        }
    }


    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.inputField.textboxKeyTyped(par1, par2);
        if (par2 == 200) {
            int nn = Agora.getInstance().getChannels().indexOf(selectedChannel) - 1;
            if (nn < 0)
                nn = Agora.getInstance().getChannels().size() - 1;
            selectedChannel = Agora.getInstance().getChannels().get(nn);
        } else if (par2 == 208) {
            int nn = Agora.getInstance().getChannels().indexOf(selectedChannel) + 1;
            if (nn > Agora.getInstance().getChannels().size() - 1)
                nn = 0;
            selectedChannel = Agora.getInstance().getChannels().get(nn);
        }
        if (par1 == '\r') {
            if (!inputField.getText().isEmpty()) {
                Agora.getInstance().sendMessage(selectedChannel, inputField.getText());
                this.inputField.setText("");
            }
        } else if (par2 == 1) {
            if (this.inputField.isFocused()) {
                this.inputField.setFocused(false);
            } else {
                this.mc.displayGuiScreen(this.parent);
            }
        }
    }

    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.inputField.mouseClicked(par1, par2, par3);
    }

    int getBrightness(float dark) {
        int ret = (int) (dark + translate.getY() / 255 * (255 - dark) * 0.9);
        return ret;
    }

    @Override
    protected void actionPerformed(final GuiButton button) {
        if (button.id >= 0 && button.id <= Agora.getInstance().getChannels().size() - 1) {
            this.selectedChannel = Agora.getInstance().getChannelsFromName().get(button.displayString);
            cWheel = 0;
        }
        switch (button.id) {
            case 101:
                showMembers = !showMembers;
                break;
            case 102:
                dark = !dark;
                break;
            case 103:
                this.mc.displayGuiScreen(this.parent);
                break;
        }

    }

}
