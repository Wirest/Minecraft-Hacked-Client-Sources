package info.sigmaclient.gui.screen;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.components.CategoryButton;
import info.sigmaclient.gui.screen.component.CustomGuiTextField;
import info.sigmaclient.management.MusicManager;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.util.RenderingUtil;
import info.sigmaclient.util.render.Colors;
import info.sigmaclient.util.render.TTFFontRenderer;
import javafx.scene.media.MediaPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class MusicScreen extends GuiScreen {
    private Expand expand = new Expand(0, 0, 0, 0);
    private Expand scroller = new Expand(0, 0, 0, 0);
    private List<MusicManager.Track> displayedTracks = null;
    private int yOffset = 0;
    private int yOffsetTarget = 0;
    private CustomGuiTextField search = null;
    private boolean displayingPlaylist = true;

    @Override
    public void initGui() {
        Minecraft m = Minecraft.getMinecraft();
        if (!Client.isLowEndPC) {
            m.entityRenderer.func_175069_a(EntityRenderer.shaderResourceLocations[18]);
        }

        ScaledResolution res = new ScaledResolution(m, m.displayWidth, m.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        search = new CustomGuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 90, (int) y - 115, 180, 20);
        search.setFocused(true);

        displayedTracks = MusicManager.getInstance().getPlaylist();
        //displayedTracks = MusicManager.getInstance().search("lemaitre");
    }

    @Override
    public void updateScreen() {
        scroller.interpolate(0, yOffsetTarget, 0, 2);
        yOffset = (int) scroller.getExpandY();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        TTFFontRenderer font = Client.fm.getFont("SFR 14");
        TTFFontRenderer font2 = Client.fm.getFont("Verdana");

        Minecraft m = Minecraft.getMinecraft();
        int speedx = (int) ((400 - expand.getExpandX()) * 0.22);
        if (speedx < 2 && 400 - expand.getExpandX() > 0) {
            speedx = 8;
        }
        int speedy = (int) ((350 - expand.getExpandY()) * 0.4);
        if (speedy < 3 && 350 - expand.getExpandY() > 0) {
            speedy = 6;
        }
        expand.interpolate(300, 400, speedx, speedy);
        ScaledResolution res = new ScaledResolution(m, m.displayWidth, m.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;
        int s = res.getScaleFactor();
        glPushMatrix();
        glScissor((int) (x - expand.getExpandX() / 2) * s, (int) (y - expand.getExpandY() / 2) * s, (int) (expand.getExpandX()) * s, (int) (expand.getExpandY()) * s);
        glEnable(GL_SCISSOR_TEST);

        font.drawCenteredString("Music player", x, y - 140, Colors.getColor(45));
        RenderingUtil.drawRoundedRect(x - 99, y - 149, x + 99, y + 149, Colors.getColor(232, 220), Colors.getColor(232, 220));
        font.drawCenteredString("Music player", x, y - 140, Colors.getColor(45));

        search.drawTextBox();

        //Render le la liste des tracks
        RenderingUtil.drawRoundedRect(x - 97, y - 90, x + 97, y + 147, Colors.getColor(180, 220), Colors.getColor(180, 220));

        glPushMatrix();
        glScissor((int) (x - 97) * s, (int) (y - 147) * s, 194 * s, 237 * s);
        glEnable(GL_SCISSOR_TEST);

        if (displayedTracks != null && !displayedTracks.isEmpty()) {
            int i = 0;
            for (MusicManager.Track track : displayedTracks) {
                ResourceLocation artLocation = MusicManager.getInstance().getArt(track.getId());
                if (artLocation != null) {
                    Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(artLocation);
                    drawScaledTexturedModalRect(x - 90, y - 85 + i * 50 + yOffset, 0, 0, 45, 45, 5.7f);
                } else {
                    RenderingUtil.rectangleBordered(x - 90, y - 85 + i * 50 + yOffset, x - 90 + 45, y - 85 + i * 50 + yOffset + 45, 1.0f, Colors.getColor(225, 50), Colors.getColor(160, 150));
                }
                font2.drawString(track.getName(), x - 40, y - 80 + i * 50 + yOffset, Colors.getColor(45));

                /*Minecraft.getMinecraft().getTextureManager().bindTexture(MusicManager.Icon.PLAY.getRl());
                drawScaledTexturedModalRect(x - 90, y - 85 + i * 50 + yOffset, 0, 0, 15, 15, 20f);*/

                RenderingUtil.drawRoundedRect(x - 40, y - 65 + 50 * i + yOffset, x, y - 50 + 50 * i + yOffset, Colors.getColor(150, 220), Colors.getColor(150, 220));
                font2.drawString("Play", x - 35, y - 63 + 50 * i + yOffset, Colors.getColor(45));

                RenderingUtil.drawRoundedRect(x + 5, y - 65 + 50 * i + yOffset, x + 50, y - 50 + 50 * i + yOffset, Colors.getColor(150, 220), Colors.getColor(150, 220));
                font2.drawString(displayingPlaylist ? "Remove" : "Like", x + 10, y - 63 + 50 * i + yOffset, Colors.getColor(45));

                i++;
            }

            float progress = -(float) yOffset / ((i * 50) - 244);
            int height = (int) (244 * (244f / Math.max(244, i * 50)));
            int progressPos = (int) (progress * (244 - height));
            RenderingUtil.drawRoundedRect(x + 95, y - 90 + progressPos, x + 97, y - 149 + height + progressPos, Colors.getColor(120, 220), Colors.getColor(120, 220));
        } else {
            font2.drawCenteredString("No results", x, y - 65, Colors.getColor(45));
            font2.drawCenteredString("Use the searchbar and hit enter", x, y - 50, Colors.getColor(45));
        }

        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();
        //Fin du render de la liste des tracks

        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();

        glPushMatrix();
        glScissor((int) (x - 97) * s, (int) (y + 147) * s, 194 * s, 100 * s);
        glEnable(GL_SCISSOR_TEST);
        renderMusicPlayer((int) x - 98, (int) y - 210);
        glDisable(GL_SCISSOR_TEST);
        glPopMatrix();


        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                yOffsetTarget -= 26;
            } else if (wheel > 0) {
                yOffsetTarget += 26;
                if (yOffsetTarget > 0) {
                    yOffsetTarget = 0;
                }
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1 && !Client.isLowEndPC) {
            Minecraft.getMinecraft().entityRenderer.theShaderGroup = null;
        }
        switch (keyCode) {
            case Keyboard.KEY_DOWN:
                yOffsetTarget -= 160;
                break;
            case Keyboard.KEY_UP:
                yOffsetTarget += 160;
                if (yOffsetTarget > 0) {
                    yOffsetTarget = 0;
                }
                break;
            case Keyboard.KEY_RETURN:
                if (search.isFocused()) {
                    displayedTracks = MusicManager.getInstance().search(search.getText());
                }
                displayingPlaylist = false;
                break;
        }
        search.textboxKeyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int clicX, int clicY, int button) {
        try {
            super.mouseClicked(clicX, clicY, button);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Minecraft m = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(m, m.displayWidth, m.displayHeight);
        float x = res.getScaledWidth() / 2;
        float y = res.getScaledHeight() / 2;

        if (displayedTracks != null) {
            int i = 0;
            for (MusicManager.Track track : displayedTracks) {
                if (
                        clicX >= x - 40
                                && clicY >= y - 65 + 50 * i + yOffset
                                && clicX <= x
                                && clicY <= y - 50 + 50 * i + yOffset
                        ) {
                    MusicManager.getInstance().play(track);
                    break;
                }
                if (
                        clicX >= x + 5
                                && clicY >= y - 65 + 50 * i + yOffset
                                && clicX <= x + 45
                                && clicY <= y - 50 + 50 * i + yOffset
                        ) {
                    if (displayingPlaylist) {
                        MusicManager.getInstance().removeFromPlaylist(track);
                    } else {
                        MusicManager.getInstance().addToPlaylist(track);
                    }
                    break;
                }
                i++;
            }
        }

        search.mouseClicked(clicX, clicY, button);
        clickMusicPlayer((int) x - 98, (int) y - 210, clicX, clicY);
    }

    public void renderMusicPlayer(int x, int y) {
        RenderingUtil.drawRoundedRect(x, y, x + 195, 60 + y - 5, Colors.getColor(232, 220), Colors.getColor(232, 220));
        TTFFontRenderer font = Client.fm.getFont("Verdana");

        RenderingUtil.rectangleBordered(x + 5, y + 5, x + 200 - 195 + 45, 10 + 45 + y - 5, 1.0f, Colors.getColor(225, 50), Colors.getColor(160, 150));

        if (MusicManager.getInstance().getCurrentTrack() != null) {
            ResourceLocation artLocation = MusicManager.getInstance().getArt(MusicManager.getInstance().getCurrentTrack().getId());
            if (artLocation != null) {
                Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(artLocation);
                //drawModalRectWithCustomSizedTexture(0, 0, 0, 0, 200, 200, 200, 200);
                drawScaledTexturedModalRect(x + 200 - 195, 10 + y - 5, 0, 0, 45, 45, 5.7f);
            }

            font.drawString(MusicManager.getInstance().getCurrentTrack().getName(), x + 200 - 145, 15 + y - 5, 0xff000000);
        } else {
            font.drawString("Music", x + 200 - 145, 15 + y - 5, 0xff000000);
        }

        int readedSeconds = 0;
        int totalSeconds = 0;

        if (MusicManager.getInstance().getMediaPlayer() != null) {
            readedSeconds = (int) MusicManager.getInstance().getMediaPlayer().getCurrentTime().toSeconds();
            totalSeconds = (int) MusicManager.getInstance().getMediaPlayer().getStopTime().toSeconds();
        }

        font.drawString(formatSeconds(readedSeconds), x + 200 - 145, 25 + y - 5, 0xff000000);
        font.drawString(formatSeconds(totalSeconds), x + 200 - 37, 25 + y - 5, 0xff000000);


        if (MusicManager.getInstance().getLoadingThread() != null) {
            int val = (int) ((Math.cos(System.currentTimeMillis() / 100D) + 1) / 2D * 155);
            Color color = new Color(0, 0, 100 + val);
            drawRect(x + 200 - 115, 30 + y - 5, x + 200 - 40, 32 + y - 5, color.getRGB());
        } else {
            drawRect(x + 200 - 115, 30 + y - 5, x + 200 - 40, 32 + y - 5, 0xff000000);
            drawRect(x + 200 - 115, 30 + y - 5, x + 200 - 115 + (115 - 40) * ((float) readedSeconds / Math.max(1, totalSeconds)), 32 + y - 5, 0xff777777);
        }

        RenderingUtil.drawRoundedRect(x + 200 - 145, 40 + y - 5, x + 200 - 145 + 29, 40 + 15 + y - 5, Colors.getColor(150, 220), Colors.getColor(150, 220));
        font.drawString((MusicManager.getInstance().getMediaPlayer() == null || MusicManager.getInstance().getMediaPlayer().getStatus() != MediaPlayer.Status.PLAYING) ? "Play" : "Pause", x + 200 - 143, 42 + y - 5, Colors.getColor(45));

        RenderingUtil.drawRoundedRect(x + 200 - 115, 40 + y - 5, x + 200 - 115 + 28, 40 + 15 + y - 5, Colors.getColor(150, 220), Colors.getColor(150, 220));
        font.drawString("Next", x + 200 - 113, 42 + y - 5, Colors.getColor(45));

        font.drawString("Volume", x + 200 - 60, 42 + y - 5, Colors.getColor(45));

        RenderingUtil.drawRoundedRect(x + 200 - 75, 40 + y - 5, x + 200 - 75 + 13, 40 + 15 + y - 5, Colors.getColor(150, 220), Colors.getColor(150, 220));
        font.drawString("-", x + 200 - 71, 42 + y - 5, Colors.getColor(45));
        RenderingUtil.drawRoundedRect(x + 200 - 23, 40 + y - 5, x + 200 - 23 + 13, 40 + 15 + y - 5, Colors.getColor(150, 220), Colors.getColor(150, 220));
        font.drawString("+", x + 200 - 21, 42 + y - 5, Colors.getColor(45));
    }

    public void clickMusicPlayer(int x, int y, int clicX, int clicY) {
        if (clicX > x && clicY < 60 + y - 5) {
            if (MusicManager.getInstance().getCurrentTrack() == null || MusicManager.getInstance().getMediaPlayer() == null || clicY < 30) {
                Minecraft.getMinecraft().displayGuiScreen(new MusicScreen());
            } else if (
                    clicX >= x + 200 - 145
                            && clicY >= 40 + y - 5
                            && clicX <= x + 200 - 145 + 25
                            && clicY <= 40 + 15 + y - 5
                    ) {
                if (MusicManager.getInstance().getMediaPlayer().getStatus() == MediaPlayer.Status.PLAYING) {
                    MusicManager.getInstance().getMediaPlayer().pause();
                } else {
                    MusicManager.getInstance().getMediaPlayer().play();
                }
            } else if (
                    clicX >= x + 200 - 115
                            && clicY >= 40 + y - 5
                            && clicX <= x + 200 - 115 + 28
                            && clicY <= 40 + 15 + y - 5
                    ) {
                MusicManager.getInstance().next();
            } else if (
                    clicX >= x + 200 - 75
                            && clicY >= 40 + y - 5
                            && clicX <= x + 200 - 75 + 13
                            && clicY <= 40 + 15 + y - 5
                    ) {
                MusicManager.getInstance().getMediaPlayer().setVolume(Math.max(0.1, MusicManager.getInstance().getMediaPlayer().getVolume() - 0.1));
                MusicManager.getInstance().setVolume(MusicManager.getInstance().getMediaPlayer().getVolume());
            } else if (
                    clicX >= x + 200 - 23
                            && clicY >= 40 + y - 5
                            && clicX <= x + 200 - 23 + 13
                            && clicY <= 40 + 15 + y - 5
                    ) {
                MusicManager.getInstance().getMediaPlayer().setVolume(Math.min(1, MusicManager.getInstance().getMediaPlayer().getVolume() + 0.1));
                MusicManager.getInstance().setVolume(MusicManager.getInstance().getMediaPlayer().getVolume());
            }
        }
    }

    private String formatSeconds(int seconds) {
        String rstl = "";
        int mins = seconds / 60;
        if (mins < 10) {
            rstl += "0";
        }
        rstl += mins + ":";
        seconds %= 60;
        if (seconds < 10) {
            rstl += "0";
        }
        rstl += seconds;
        return rstl;
    }

}
