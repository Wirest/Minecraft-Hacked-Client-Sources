package cedo.altmanager;

import cedo.ui.animations.Animation;
import cedo.ui.animations.Direction;
import cedo.ui.animations.impl.SmoothStepAnimation;
import cedo.ui.elements.Rectangle;
import cedo.util.Timer;
import cedo.util.font.FontUtil;
import cedo.util.font.MinecraftFontRenderer;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.SSLController;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;

public class AltManager extends GuiScreen {
    private final GuiScreen back;
    SmoothStepAnimation barAnimation = new SmoothStepAnimation(1000, 10, Direction.BACKWARDS);
    ArrayList<ButtonPackage> buttonPackages = new ArrayList<>();
    double barHeight = 30;
    String[] strings = {"New Alt", "Delete Alt", "Login", "Back"}; // Add a new string to automatically
    // create a new button
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;
    private String error = "";
    private Timer timer;

    public AltManager(GuiScreen back) {
        this.back = back;
    }

    @Override
    public void initGui() {
        int var3 = height / 4 + 24;
        this.username = new GuiTextField(var3, FontUtil.clean, width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(null, 0, width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);

        for (String string : strings) { // For every string, make a ButtonPackage which stores an invisible button, the
            // text, and the text's animation
            buttonPackages
                    .add(new ButtonPackage(string, new Rectangle(0, 0, 0, 0, new Color(0, 0, 0, 0)),
                            new SmoothStepAnimation(250, 10, Direction.BACKWARDS)));
        }
    }

    @Override
    protected void keyTyped(char character, int key) {
        try {
            super.keyTyped(character, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            } else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            login();
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }

    private void login() {
        String usernameS;
        String passwordS;
        if (username.getText().contains(":")) {
            String[] combo = username.getText().split(":");
            usernameS = combo[0];
            passwordS = combo[1];
        } else {
            usernameS = username.getText();
            passwordS = password.getText();
        }
        thread = new AltLoginThread(usernameS, passwordS);
        thread.start();
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.getTextureManager().bindTexture(new ResourceLocation("Fan/BackgroundAltmanager.png"));
        drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, this.width, this.height);
        this.username.drawTextBox();
        this.password.drawPasswordBox();

        MinecraftFontRenderer font1 = FontUtil.regular;
        MinecraftFontRenderer font2 = FontUtil.regularSmall;

        font1.drawCenteredString("Alt Manager Working Placeholder :flushed: how long can I make this", width / 2, 20, -1);
        font2.drawCenteredString(this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(),
                width / 2, 38, -1);
        if (this.username.getText().isEmpty()) {
            this.drawString("email / email:pass", width / 2 - 96, 66, -7829368);
        }
        if (this.password.getText().isEmpty()) {
            this.drawString("pass", width / 2 - 96, 106, -7829368);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);

        int stringSize = 0;
        for (String string : strings) {
            stringSize += FontUtil.regular.getStringWidth(string);
        }
        int distBetweenStrings = (width - stringSize) / (strings.length * 2);
        int currentX = 0;
        for (ButtonPackage buttonPackage : buttonPackages) { // Sets button coords.
            buttonPackage.getButton().setX(currentX);
            buttonPackage.getButton()
                    .setWidth(distBetweenStrings * 2 + FontUtil.regular.getStringWidth(buttonPackage.getString()));
            buttonPackage.getButton().setY(height - barHeight);
            buttonPackage.getButton().setHeight(barHeight);

            currentX += (distBetweenStrings * 2 + FontUtil.regular.getStringWidth(buttonPackage.getString()));
        }

        boolean barHovered = false;
        for (ButtonPackage buttonPackage : buttonPackages) { // Checks if any button is hovered over in order to get
            // barHovered
            if (buttonPackage.getButton().isInside(mouseX, mouseY)) {
                barHovered = true;
                buttonPackage.setHovered(true);
                buttonPackage.getAnimation().setDirection(Direction.FORWARDS);
            } else {
                buttonPackage.setHovered(false);
                buttonPackage.getAnimation().setDirection(Direction.BACKWARDS);
            }
        }

        if (barHovered) {
            barAnimation.setDuration(250);
            barAnimation.setDirection(Direction.FORWARDS);
        } else {
            barAnimation.setDirection(Direction.BACKWARDS);
        }
        barHeight = barAnimation != null ? 30 + barAnimation.getOutput() : 30; // If animation is null, set height to 30
        drawRect(0, height - barHeight, width, height, 0xffBB40C2); // Draw bar

        for (ButtonPackage buttonPackage : buttonPackages) {
            double[] coords = buttonPackage.getButton().getCenter(); // Gets the center coords of corresponding
            // rectangle
            MinecraftFontRenderer font = buttonPackage.isHovered() ? FontUtil.expandedfont : FontUtil.regular;
            font.drawCenteredStringWithShadow(buttonPackage.getString(), (float) coords[0],
                    (float) (height - 24 - buttonPackage.getAnimation().getOutput()),
                    buttonPackage.isHovered() ? 0xffff87c7 : -1); // Draw string
        }

        //Stuff below is placeholder and to be removed
        if (!error.equals("") && timer == null) {
            timer = new Timer();
        } else if (timer != null && timer.hasTimeElapsed(5000, true)) {
            error = "";
            timer = null;
        }

        FontUtil.regular.drawCenteredStringWithShadow(EnumChatFormatting.RED + error, width / 2, height - ((int) barHeight + FontUtil.regular.getHeight() + 4), -1);
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (ButtonPackage buttonPackage : buttonPackages) {
            if (buttonPackage.isHovered()) {
                switch (buttonPackage.getString()) {
                    case "New Alt":
                        error = "\"New Alt\" doesn't do anything yet";
                        if (timer != null)
                            timer.reset();
                        break;

                    case "Delete Alt":
                        error = "\"Delete Alt\" doesn't do anything yet";
                        if (timer != null)
                            timer.reset();
                        break;

                    case "Login":
                        login();
                        break;

                    case "Back":
                        mc.displayGuiScreen(back);
                        break;
                }
            }
        }
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(mouseX, mouseY, mouseButton);
        this.password.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private static class ButtonPackage {
        String string;
        Rectangle button;
        SmoothStepAnimation animation;
        boolean hovered;

        public ButtonPackage(String string, Rectangle button, SmoothStepAnimation animation) {
            this.string = string;
            this.button = button;
            this.animation = animation;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }

        public Rectangle getButton() {
            return button;
        }

        public void setButton(Rectangle button) {
            this.button = button;
        }

        public Animation getAnimation() {
            return animation;
        }

        public void setAnimation(SmoothStepAnimation animation) {
            this.animation = animation;
        }

        public boolean isHovered() {
            return hovered;
        }

        public void setHovered(boolean hovered) {
            this.hovered = hovered;
        }
    }

}

class PasswordField extends Gui {

    private final int id;
    /**
     * The width of this text field.
     */
    private final int width;
    private final int height;
    public int bottomBarColor = 0xffaaaaaa,
            textColor = 0xffaaaaaa,
            cursorColor = 0xffaaaaaa;
    public int xPosition;
    public int yPosition;
    public String placeholder;
    /**
     * Has the current text being edited on the textbox.
     */
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    private boolean enableBackgroundDrawing = true;
    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    private boolean canLoseFocus = true;
    /**
     * If this value is true along with isEnabled, keyTyped will process the keys.
     */
    private boolean isFocused;
    /**
     * If this value is true along with isFocused, keyTyped will process the keys.
     */
    private boolean isEnabled = true;
    /**
     * The current character index that should be used as start of the rendered text.
     */
    private int lineScrollOffset;
    private int cursorPosition;
    /**
     * other selection position, maybe the same as the cursor
     */
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;
    /**
     * True if this textbox is visible
     */
    private boolean visible = true;
    private GuiPageButtonList.GuiResponder field_175210_x;
    private Predicate<String> field_175209_y = Predicates.alwaysTrue();

    public PasswordField(String placeholder, int componentId, int x, int y, int par5Width, int par6Height) {
        this.placeholder = placeholder;
        this.id = componentId;
        this.xPosition = x;
        this.yPosition = y;
        this.width = par5Width;
        this.height = par6Height;
    }

    public void func_175207_a(GuiPageButtonList.GuiResponder p_175207_1_) {
        this.field_175210_x = p_175207_1_;
    }

    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter() {
        ++this.cursorCounter;
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText() {
        return this.text;
    }

    /**
     * Sets the text of the textbox
     */
    public void setText(String p_146180_1_) {
        if (this.field_175209_y.apply(p_146180_1_)) {
            if (p_146180_1_.length() > this.maxStringLength) {
                this.text = p_146180_1_.substring(0, this.maxStringLength);
            } else {
                this.text = p_146180_1_;
            }

            this.setCursorPositionEnd();
        }
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText() {
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }

    public void func_175205_a(Predicate<String> p_175205_1_) {
        this.field_175209_y = p_175205_1_;
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String p_146191_1_) {
        String s = "";
        String s1 = ChatAllowedCharacters.filterAllowedCharacters(p_146191_1_);
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - (i - j);
        int l = 0;

        if (this.text.length() > 0) {
            s = s + this.text.substring(0, i);
        }

        if (k < s1.length()) {
            s = s + s1.substring(0, k);
            l = k;
        } else {
            s = s + s1;
            l = s1.length();
        }

        if (this.text.length() > 0 && j < this.text.length()) {
            s = s + this.text.substring(j);
        }

        if (this.field_175209_y.apply(s)) {
            this.text = s;
            this.moveCursorBy(i - this.selectionEnd + l);

            if (this.field_175210_x != null) {
                this.field_175210_x.func_175319_a(this.id, this.text);
            }
        }
    }

    /**
     * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
     * the cursor.
     */
    public void deleteWords(int p_146177_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
        }
    }

    public void drawTextBox() {
        drawTextBox(text, false);
    }

    public void drawPasswordBox() {
        drawTextBox(text, true);
    }

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int p_146175_1_) {
        if (this.text.length() != 0) {
            if (this.selectionEnd != this.cursorPosition) {
                this.writeText("");
            } else {
                boolean flag = p_146175_1_ < 0;
                int i = flag ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
                int j = flag ? this.cursorPosition : this.cursorPosition + p_146175_1_;
                String s = "";

                if (i >= 0) {
                    s = this.text.substring(0, i);
                }

                if (j < this.text.length()) {
                    s = s + this.text.substring(j);
                }

                if (this.field_175209_y.apply(s)) {
                    this.text = s;

                    if (flag) {
                        this.moveCursorBy(p_146175_1_);
                    }

                    if (this.field_175210_x != null) {
                        this.field_175210_x.func_175319_a(this.id, this.text);
                    }
                }
            }
        }
    }

    public int getId() {
        return this.id;
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int p_146187_1_) {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int p_146183_1_, int p_146183_2_) {
        return this.func_146197_a(p_146183_1_, p_146183_2_, true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_) {
        int i = p_146197_2_;
        boolean flag = p_146197_1_ < 0;
        int j = Math.abs(p_146197_1_);

        for (int k = 0; k < j; ++k) {
            if (!flag) {
                int l = this.text.length();
                i = this.text.indexOf(32, i);

                if (i == -1) {
                    i = l;
                } else {
                    while (p_146197_3_ && i < l && this.text.charAt(i) == 32) {
                        ++i;
                    }
                }
            } else {
                while (p_146197_3_ && i > 0 && this.text.charAt(i - 1) == 32) {
                    --i;
                }

                while (i > 0 && this.text.charAt(i - 1) != 32) {
                    --i;
                }
            }
        }

        return i;
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int p_146182_1_) {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero() {
        this.setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd() {
        this.setCursorPosition(this.text.length());
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    public boolean textboxKeyTyped(char p_146201_1_, int p_146201_2_) {
        if (!this.isFocused) {
            return false;
        } else if (GuiScreen.isKeyComboCtrlA(p_146201_2_)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (GuiScreen.isKeyComboCtrlC(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (GuiScreen.isKeyComboCtrlV(p_146201_2_)) {
            if (this.isEnabled) {
                this.writeText(GuiScreen.getClipboardString());
            }

            return true;
        } else if (GuiScreen.isKeyComboCtrlX(p_146201_2_)) {
            GuiScreen.setClipboardString(this.getSelectedText());

            if (this.isEnabled) {
                this.writeText("");
            }

            return true;
        } else {
            switch (p_146201_2_) {
                case 14:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled) {
                            this.deleteWords(-1);
                        }
                    } else if (this.isEnabled) {
                        this.deleteFromCursor(-1);
                    }

                    return true;

                case 199:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(0);
                    } else {
                        this.setCursorPositionZero();
                    }

                    return true;

                case 203:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        } else {
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    } else {
                        this.moveCursorBy(-1);
                    }

                    return true;

                case 205:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown()) {
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        } else {
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                        }
                    } else if (GuiScreen.isCtrlKeyDown()) {
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    } else {
                        this.moveCursorBy(1);
                    }

                    return true;

                case 207:
                    if (GuiScreen.isShiftKeyDown()) {
                        this.setSelectionPos(this.text.length());
                    } else {
                        this.setCursorPositionEnd();
                    }

                    return true;

                case 211:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled) {
                            this.deleteWords(1);
                        }
                    } else if (this.isEnabled) {
                        this.deleteFromCursor(1);
                    }

                    return true;

                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_)) {
                        if (this.isEnabled) {
                            this.writeText(Character.toString(p_146201_1_));
                        }

                        return true;
                    } else {
                        return false;
                    }
            }
        }
    }

    /**
     * Args: x, y, buttonClicked
     */
    public void mouseClicked(int p_146192_1_, int p_146192_2_, int p_146192_3_) {
        boolean flag = p_146192_1_ >= this.xPosition && p_146192_1_ < this.xPosition + this.width && p_146192_2_ >= this.yPosition && p_146192_2_ < this.yPosition + this.height;

        if (this.canLoseFocus) {
            this.setFocused(flag);
        }

        if (this.isFocused && flag && p_146192_3_ == 0) {
            int i = p_146192_1_ - this.xPosition;

            if (this.enableBackgroundDrawing) {
                i -= 4;
            }

            String s = FontUtil.clean.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
            this.setCursorPosition(FontUtil.clean.trimStringToWidth(s, i).length() + this.lineScrollOffset);
        }
    }

    /**
     * Draws the textbox
     */
    public void drawTextBox(String text, boolean password) {
        if (password)
            text = text.replaceAll(".", "*");

        if (this.getVisible()) {
            if (this.getEnableBackgroundDrawing()) {
                drawRect(this.xPosition, this.yPosition + this.height, this.xPosition + this.width, this.yPosition + this.height + 2, bottomBarColor);
            }

            GlStateManager.color(1, 1, 1, 1);
            int i = textColor;
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = FontUtil.clean.trimStringToWidth(text.substring(this.lineScrollOffset), this.getWidth());
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.enableBackgroundDrawing ? this.xPosition + 4 : this.xPosition;
            int i1 = this.enableBackgroundDrawing ? this.yPosition + (this.height - 8) / 4 : this.yPosition;
            int j1 = l;

            if (!isFocused && placeholder != null && (text == null || text.isEmpty())) {
                FontUtil.clean.drawString(placeholder, l, i1, textColor);
            }

            if (k > s.length()) {
                k = s.length();
            }

            if (s.length() > 0) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = FontUtil.clean.drawString(s1, (float) l, (float) i1, i);
            }

            boolean flag2 = this.cursorPosition < text.length() || text.length() >= this.getMaxStringLength();
            int k1 = j1;

            if (!flag) {
                k1 = j > 0 ? l + this.width : l;
            } else if (flag2) {
                k1 = j1 - 1;
                --j1;
            }

            if (s.length() > 0 && flag && j < s.length()) {
                GlStateManager.color(1, 1, 1, 1);
                j1 = FontUtil.clean.drawString(s.substring(j), (float) j1 + 6, (float) i1, i);
            }

            if (flag1) {
                if (flag2) {
                    GlStateManager.color(1, 1, 1, 1);
                    Gui.drawRect(k1 + 3.5f, i1 - 1, k1 + 5, i1 + 1 + FontUtil.clean.getHeight(), this.cursorColor);
                } else {
                    GlStateManager.color(1, 1, 1, 1);
                    FontUtil.clean.drawString("_", (float) k1 + 4, (float) i1, textColor);
                }
            }

            if (k != j) {
                int l1 = (int) (l + FontUtil.clean.getStringWidth(s.substring(0, k)));
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + FontUtil.clean.getHeight());
            }

            GlStateManager.color(1, 1, 1, 1);
        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_) {
        if (p_146188_1_ < p_146188_3_) {
            int i = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i;
        }

        if (p_146188_2_ < p_146188_4_) {
            int j = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = j;
        }

        if (p_146188_3_ > this.xPosition + this.width) {
            p_146188_3_ = this.xPosition + this.width;
        }

        if (p_146188_1_ > this.xPosition + this.width) {
            p_146188_1_ = this.xPosition + this.width;
        }

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(5387);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(p_146188_1_, p_146188_4_, 0.0D).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_4_, 0.0D).endVertex();
        worldrenderer.pos(p_146188_3_, p_146188_2_, 0.0D).endVertex();
        worldrenderer.pos(p_146188_1_, p_146188_2_, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxStringLength() {
        return this.maxStringLength;
    }

    public void setMaxStringLength(int p_146203_1_) {
        this.maxStringLength = p_146203_1_;

        if (this.text.length() > p_146203_1_) {
            this.text = this.text.substring(0, p_146203_1_);
        }
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition() {
        return this.cursorPosition;
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int p_146190_1_) {
        this.cursorPosition = p_146190_1_;
        int i = this.text.length();
        this.cursorPosition = MathHelper.clamp_int(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    /**
     * get enable drawing background and outline
     */
    public boolean getEnableBackgroundDrawing() {
        return this.enableBackgroundDrawing;
    }

    /**
     * enable drawing background and outline
     */
    public void setEnableBackgroundDrawing(boolean p_146185_1_) {
        this.enableBackgroundDrawing = p_146185_1_;
    }

    /**
     * Sets the text colour for this textbox (disabled text will not use this colour)
     */
    public void setTextColor(int p_146193_1_) {
        this.enabledColor = p_146193_1_;
    }

    public void setDisabledTextColour(int p_146204_1_) {
        this.disabledColor = p_146204_1_;
    }

    /**
     * Getter for the focused field
     */
    public boolean isFocused() {
        return this.isFocused;
    }

    /**
     * Sets focus to this gui element
     */
    public void setFocused(boolean p_146195_1_) {
        if (p_146195_1_ && !this.isFocused) {
            this.cursorCounter = 0;
        }

        this.isFocused = p_146195_1_;
    }

    public void setEnabled(boolean p_146184_1_) {
        this.isEnabled = p_146184_1_;
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidth() {
        return this.getEnableBackgroundDrawing() ? this.width - 8 : this.width;
    }

    /**
     * Sets the position of the selection anchor (i.e. position the selection was started at)
     */
    public void setSelectionPos(int p_146199_1_) {
        int i = this.text.length();

        if (p_146199_1_ > i) {
            p_146199_1_ = i;
        }

        if (p_146199_1_ < 0) {
            p_146199_1_ = 0;
        }

        this.selectionEnd = p_146199_1_;

        if (FontUtil.clean != null) {
            if (this.lineScrollOffset > i) {
                this.lineScrollOffset = i;
            }

            int j = this.getWidth();
            String s = FontUtil.clean.trimStringToWidth(this.text.substring(this.lineScrollOffset), j);
            int k = s.length() + this.lineScrollOffset;

            if (p_146199_1_ == this.lineScrollOffset) {
                this.lineScrollOffset -= FontUtil.clean.trimStringToWidth(this.text, j, true).length();
            }

            if (p_146199_1_ > k) {
                this.lineScrollOffset += p_146199_1_ - k;
            } else if (p_146199_1_ <= this.lineScrollOffset) {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }

            this.lineScrollOffset = MathHelper.clamp_int(this.lineScrollOffset, 0, i);
        }
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean p_146205_1_) {
        this.canLoseFocus = p_146205_1_;
    }

    /**
     * returns true if this textbox is visible
     */
    public boolean getVisible() {
        return this.visible;
    }

    /**
     * Sets whether or not this textbox is visible
     */
    public void setVisible(boolean p_146189_1_) {
        this.visible = p_146189_1_;
    }
}

final class AltLoginThread extends Thread {
    private final String password;
    private final String username;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final SSLController ssl = new SSLController();
    private final TheAlteningAuthentication alteningAuthentication = TheAlteningAuthentication.mojang();
    private String status;

    public AltLoginThread(String username, String password) {
        super("Alt Login Thread");
        this.username = username;
        this.password = password;
        this.status = EnumChatFormatting.GRAY + "Waiting...";
    }

    private Session createSession(String username, String password) {
        YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) service
                .createUserAuthentication(Agent.MINECRAFT);
        auth.setUsername(username);
        auth.setPassword(password);
        try {
            if (username.endsWith("@alt.com")) {
                ssl.disableCertificateValidation();
                alteningAuthentication.updateService(AlteningServiceType.THEALTENING);
            } else if (alteningAuthentication.getService() == AlteningServiceType.THEALTENING) {
                ssl.enableCertificateValidation();
                alteningAuthentication.updateService(AlteningServiceType.MOJANG);
            }
            auth.logIn();
            return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(),
                    auth.getAuthenticatedToken(), "mojang");
        } catch (AuthenticationException localAuthenticationException) {
            localAuthenticationException.printStackTrace();
            return null;
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public void run() {
        if (this.password.equals("")) {
            this.mc.session = new Session(this.username, "", "", "mojang");
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + this.username
                    + " - offline name)";
            return;
        }
        this.status = EnumChatFormatting.YELLOW + "Logging in...";
        Session auth = this.createSession(this.username, this.password);
        if (auth == null) {
            this.status = EnumChatFormatting.RED + "Login failed!";
        } else {
            // AltManager altManager = Client.instance.altManager;
            // AltManager.lastAlt = new Alt(this.username, this.password);
            this.status = EnumChatFormatting.GREEN + "Logged in. (" + auth.getUsername() + ")";
            this.mc.session = auth;
        }
    }
}
