package com.ihl.client.gui;

import com.ihl.client.Helper;
import com.ihl.client.commands.Command;
import com.ihl.client.commands.exceptions.ArgumentException;
import com.ihl.client.commands.exceptions.CommandException;
import com.ihl.client.commands.exceptions.SyntaxException;
import com.ihl.client.module.Module;
import com.ihl.client.util.ChatUtil;
import com.ihl.client.util.RenderUtil;
import com.ihl.client.util.RenderUtil2D;
import com.ihl.client.util.part.Settings;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiHandle extends GuiScreen {

    private static List<String> history = new CopyOnWriteArrayList();
    private int selected = -1;
    private GuiTextField console;

    public void initGui() {
        Gui.init();
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        console = new GuiTextField(0, fontRendererObj, width / 4, 4, width / 2, 16);
        console.setEnableBackgroundDrawing(false);
        console.setMaxStringLength(100);
        selected = -1;
    }

    public void updateScreen() {
        super.updateScreen();
        if (console != null) {
            console.setFocused(Module.get("console").active);
            console.updateCursorCounter();
            if (!console.isFocused()) {
                console.setText("");
            }
        }
    }


    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(null);
            return;
        }

        Gui.keyPress(keyCode, typedChar);
        if (Gui.components.get("ring") instanceof RingKeybind || Gui.components.get("ring") instanceof RingString) {
            return;
        }

        if (console != null) {
            int prev = selected;
            if (keyCode == Keyboard.KEY_UP) {
                selected++;
            }
            if (keyCode == Keyboard.KEY_DOWN) {
                selected--;
            }
            selected = Math.min(Math.max(-1, selected), history.size() - 1);
            if (selected != -1 && selected != prev) {
                console.setText(history.get(history.size() - 1 - selected));
            } else if (selected != prev) {
                console.setText("");
            }

            console.textboxKeyTyped(typedChar, keyCode);

            if (keyCode == 28 || keyCode == 156) {
                String message = console.getText();
                String[] args = message.split(" ");
                String base = args[0];
                args = Command.dropFirst(args);

                Command.run(base, args);
                history.add(console.getText());
                console.setText("");
                selected = -1;
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (console != null) {
            console.mouseClicked(mouseX, mouseY, mouseButton);
        }
        Gui.mouseClicked(mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        Gui.mouseReleased(mouseButton);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        double padding = 2;

        if (console != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, 0, 1);

            String text = console.getText();

            int sizeY = 0;
            if (text.length() > 0) {
                List<String> matching = new ArrayList();
                for (String key : Command.commands.keySet()) {
                    Command command = Command.commands.get(key);
                    for (String string : command.usages) {
                        if (string.startsWith(text.toLowerCase())) {
                            matching.add(string);
                        }
                    }
                    Collections.sort(matching);
                }

                if (!matching.isEmpty()) {
                    ScaledResolution scaledRes = new ScaledResolution(Helper.mc(), Helper.mc().displayWidth, Helper.mc().displayHeight);

                    sizeY += padding * 2;
                    sizeY += (fontRendererObj.FONT_HEIGHT) * matching.size();
                    sizeY = Math.min(sizeY, scaledRes.getScaledHeight() - 32);

                    RenderUtil.startClip((console.xPosition - (padding / 2)) * scaledRes.getScaleFactor(), (console.yPosition - (padding / 2)) * scaledRes.getScaleFactor(), (console.xPosition + console.getWidth() + (padding / 2)) * scaledRes.getScaleFactor(), (console.yPosition + fontRendererObj.FONT_HEIGHT + (padding / 2) + sizeY) * scaledRes.getScaleFactor());
                    for (String m : matching) {
                        int index = matching.indexOf(m);
                        fontRendererObj.drawStringWithShadow("\u00a77" + m, console.xPosition, console.yPosition + fontRendererObj.FONT_HEIGHT + ((int) padding * 2) + 1 + (index * fontRendererObj.FONT_HEIGHT), -1);
                    }
                    RenderUtil.endClip();
                }
            }

            console.setText("\u00a7a" + ChatUtil.clearFormat(console.getText()));
            console.drawTextBox();
            console.setText(text);

            GlStateManager.popMatrix();

            if (console.isFocused()) {
                RenderUtil2D.rectBordered(console.xPosition - padding, console.yPosition - padding, console.xPosition + console.getWidth() + padding, console.yPosition + fontRendererObj.FONT_HEIGHT + padding + sizeY, 0x80000000, 0xFF000000, 2f);
            }
        }
    }
}
