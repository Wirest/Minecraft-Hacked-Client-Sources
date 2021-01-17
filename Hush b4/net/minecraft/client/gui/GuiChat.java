// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import org.apache.commons.lang3.StringUtils;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.client.Minecraft;
import net.minecraft.util.MovingObjectPosition;
import java.util.Iterator;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.lwjgl.input.Mouse;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import org.apache.logging.log4j.Logger;

public class GuiChat extends GuiScreen
{
    private static final Logger logger;
    private String historyBuffer;
    private int sentHistoryCursor;
    private boolean playerNamesFound;
    private boolean waitingOnAutocomplete;
    private int autocompleteIndex;
    private List<String> foundPlayerNames;
    protected GuiTextField inputField;
    private String defaultInputFieldText;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GuiChat() {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.foundPlayerNames = (List<String>)Lists.newArrayList();
        this.defaultInputFieldText = "";
    }
    
    public GuiChat(final String defaultText) {
        this.historyBuffer = "";
        this.sentHistoryCursor = -1;
        this.foundPlayerNames = (List<String>)Lists.newArrayList();
        this.defaultInputFieldText = "";
        this.defaultInputFieldText = defaultText;
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.sentHistoryCursor = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        (this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12)).setMaxStringLength(100);
        this.inputField.setEnableBackgroundDrawing(false);
        this.inputField.setFocused(true);
        this.inputField.setText(this.defaultInputFieldText);
        this.inputField.setCanLoseFocus(false);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        this.mc.ingameGUI.getChatGUI().resetScroll();
    }
    
    @Override
    public void updateScreen() {
        this.inputField.updateCursorCounter();
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.waitingOnAutocomplete = false;
        if (keyCode == 15) {
            this.autocompletePlayerNames();
        }
        else {
            this.playerNamesFound = false;
        }
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
        }
        else if (keyCode != 28 && keyCode != 156) {
            if (keyCode == 200) {
                this.getSentHistory(-1);
            }
            else if (keyCode == 208) {
                this.getSentHistory(1);
            }
            else if (keyCode == 201) {
                this.mc.ingameGUI.getChatGUI().scroll(this.mc.ingameGUI.getChatGUI().getLineCount() - 1);
            }
            else if (keyCode == 209) {
                this.mc.ingameGUI.getChatGUI().scroll(-this.mc.ingameGUI.getChatGUI().getLineCount() + 1);
            }
            else {
                this.inputField.textboxKeyTyped(typedChar, keyCode);
            }
        }
        else {
            final String s = this.inputField.getText().trim();
            if (s.length() > 0) {
                this.sendChatMessage(s);
            }
            this.mc.displayGuiScreen(null);
        }
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }
            if (i < -1) {
                i = -1;
            }
            if (!GuiScreen.isShiftKeyDown()) {
                i *= 7;
            }
            this.mc.ingameGUI.getChatGUI().scroll(i);
        }
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
            if (this.handleComponentClick(ichatcomponent)) {
                return;
            }
        }
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void setText(final String newChatText, final boolean shouldOverwrite) {
        if (shouldOverwrite) {
            this.inputField.setText(newChatText);
        }
        else {
            this.inputField.writeText(newChatText);
        }
    }
    
    public void autocompletePlayerNames() {
        if (this.playerNamesFound) {
            this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
            if (this.autocompleteIndex >= this.foundPlayerNames.size()) {
                this.autocompleteIndex = 0;
            }
        }
        else {
            final int i = this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false);
            this.foundPlayerNames.clear();
            this.autocompleteIndex = 0;
            final String s = this.inputField.getText().substring(i).toLowerCase();
            final String s2 = this.inputField.getText().substring(0, this.inputField.getCursorPosition());
            this.sendAutocompleteRequest(s2, s);
            if (this.foundPlayerNames.isEmpty()) {
                return;
            }
            this.playerNamesFound = true;
            this.inputField.deleteFromCursor(i - this.inputField.getCursorPosition());
        }
        if (this.foundPlayerNames.size() > 1) {
            final StringBuilder stringbuilder = new StringBuilder();
            for (final String s3 : this.foundPlayerNames) {
                if (stringbuilder.length() > 0) {
                    stringbuilder.append(", ");
                }
                stringbuilder.append(s3);
            }
            this.mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new ChatComponentText(stringbuilder.toString()), 1);
        }
        this.inputField.writeText(this.foundPlayerNames.get(this.autocompleteIndex++));
    }
    
    private void sendAutocompleteRequest(final String p_146405_1_, final String p_146405_2_) {
        if (p_146405_1_.length() >= 1) {
            BlockPos blockpos = null;
            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                blockpos = this.mc.objectMouseOver.getBlockPos();
            }
            Minecraft.thePlayer.sendQueue.addToSendQueue(new C14PacketTabComplete(p_146405_1_, blockpos));
            this.waitingOnAutocomplete = true;
        }
    }
    
    public void getSentHistory(final int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        final int j = this.mc.ingameGUI.getChatGUI().getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);
        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            }
            else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }
                this.inputField.setText(this.mc.ingameGUI.getChatGUI().getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        Gui.drawRect(2.0, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
        this.inputField.drawTextBox();
        final IChatComponent ichatcomponent = this.mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void onAutocompleteResponse(final String[] p_146406_1_) {
        if (this.waitingOnAutocomplete) {
            this.playerNamesFound = false;
            this.foundPlayerNames.clear();
            for (final String s : p_146406_1_) {
                if (s.length() > 0) {
                    this.foundPlayerNames.add(s);
                }
            }
            final String s2 = this.inputField.getText().substring(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false));
            final String s3 = StringUtils.getCommonPrefix(p_146406_1_);
            if (s3.length() > 0 && !s2.equalsIgnoreCase(s3)) {
                this.inputField.deleteFromCursor(this.inputField.func_146197_a(-1, this.inputField.getCursorPosition(), false) - this.inputField.getCursorPosition());
                this.inputField.writeText(s3);
            }
            else if (this.foundPlayerNames.size() > 0) {
                this.playerNamesFound = true;
                this.autocompletePlayerNames();
            }
        }
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
