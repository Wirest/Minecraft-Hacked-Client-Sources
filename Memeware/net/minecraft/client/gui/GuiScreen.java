package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.awt.Toolkit;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.stream.GuiTwitchUserMode;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import tv.twitch.chat.ChatUserInfo;

public abstract class GuiScreen extends Gui implements GuiYesNoCallback {
    private static final Logger field_175287_a = LogManager.getLogger();
    private static final Set field_175284_f = Sets.newHashSet(new String[]{"http", "https"});
    private static final Splitter field_175285_g = Splitter.on('\n');

    /**
     * Reference to the Minecraft object.
     */
    protected Minecraft mc;

    /**
     * Holds a instance of RenderItem, used to draw the achievement icons on screen (is based on ItemStack)
     */
    protected RenderItem itemRender;

    /**
     * The width of the screen object.
     */
    public int width;

    /**
     * The height of the screen object.
     */
    public int height;

    /**
     * A list of all the buttons in this container.
     */
    protected List buttonList = Lists.newArrayList();

    /**
     * A list of all the labels in this container.
     */
    protected List labelList = Lists.newArrayList();
    public boolean allowUserInput;

    /**
     * The FontRenderer used by GuiScreen
     */
    protected FontRenderer fontRendererObj;

    /**
     * The button that was just pressed.
     */
    private GuiButton selectedButton;
    protected int eventButton;
    private long lastMouseEvent;

    /**
     * Incremented when the game is in touchscreen mode and the screen is tapped, decremented if the screen isn't
     * tapped. Does not appear to be used.
     */
    private int touchValue;
    private URI field_175286_t;
    private static final String __OBFID = "CL_00000710";

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int var4;

        for (var4 = 0; var4 < this.buttonList.size(); ++var4) {
            ((GuiButton) this.buttonList.get(var4)).drawButton(this.mc, mouseX, mouseY);
        }

        for (var4 = 0; var4 < this.labelList.size(); ++var4) {
            ((GuiLabel) this.labelList.get(var4)).drawLabel(this.mc, mouseX, mouseY);
        }
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen((GuiScreen) null);

            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    /**
     * Returns a string stored in the system clipboard.
     */
    public static String getClipboardString() {
        try {
            Transferable var0 = Toolkit.getDefaultToolkit().getSystemClipboard().getContents((Object) null);

            if (var0 != null && var0.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String) var0.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception var1) {
            ;
        }

        return "";
    }

    /**
     * Stores the given string in the system clipboard
     */
    public static void setClipboardString(String copyText) {
        if (!StringUtils.isEmpty(copyText)) {
            try {
                StringSelection var1 = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(var1, (ClipboardOwner) null);
            } catch (Exception var2) {
                ;
            }
        }
    }

    protected void renderToolTip(ItemStack itemIn, int x, int y) {
        List var4 = itemIn.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);

        for (int var5 = 0; var5 < var4.size(); ++var5) {
            if (var5 == 0) {
                var4.set(var5, itemIn.getRarity().rarityColor + (String) var4.get(var5));
            } else {
                var4.set(var5, EnumChatFormatting.GRAY + (String) var4.get(var5));
            }
        }

        this.drawHoveringText(var4, x, y);
    }

    /**
     * Draws the text when mouse is over creative inventory tab. Params: current creative tab to be checked, current
     * mouse x position, current mouse y position.
     */
    protected void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
        this.drawHoveringText(Arrays.asList(new String[]{tabName}), mouseX, mouseY);
    }

    protected void drawHoveringText(List textLines, int x, int y) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int var4 = 0;
            Iterator var5 = textLines.iterator();

            while (var5.hasNext()) {
                String var6 = (String) var5.next();
                int var7 = this.fontRendererObj.getStringWidth(var6);

                if (var7 > var4) {
                    var4 = var7;
                }
            }

            int var14 = x + 12;
            int var15 = y - 12;
            int var8 = 8;

            if (textLines.size() > 1) {
                var8 += 2 + (textLines.size() - 1) * 10;
            }

            if (var14 + var4 > this.width) {
                var14 -= 28 + var4;
            }

            if (var15 + var8 + 6 > this.height) {
                var15 = this.height - var8 - 6;
            }

            this.zLevel = 300.0F;
            this.itemRender.zLevel = 300.0F;
            int var9 = -267386864;
            this.drawGradientRect(var14 - 3, var15 - 4, var14 + var4 + 3, var15 - 3, var9, var9);
            this.drawGradientRect(var14 - 3, var15 + var8 + 3, var14 + var4 + 3, var15 + var8 + 4, var9, var9);
            this.drawGradientRect(var14 - 3, var15 - 3, var14 + var4 + 3, var15 + var8 + 3, var9, var9);
            this.drawGradientRect(var14 - 4, var15 - 3, var14 - 3, var15 + var8 + 3, var9, var9);
            this.drawGradientRect(var14 + var4 + 3, var15 - 3, var14 + var4 + 4, var15 + var8 + 3, var9, var9);
            int var10 = 1347420415;
            int var11 = (var10 & 16711422) >> 1 | var10 & -16777216;
            this.drawGradientRect(var14 - 3, var15 - 3 + 1, var14 - 3 + 1, var15 + var8 + 3 - 1, var10, var11);
            this.drawGradientRect(var14 + var4 + 2, var15 - 3 + 1, var14 + var4 + 3, var15 + var8 + 3 - 1, var10, var11);
            this.drawGradientRect(var14 - 3, var15 - 3, var14 + var4 + 3, var15 - 3 + 1, var10, var10);
            this.drawGradientRect(var14 - 3, var15 + var8 + 2, var14 + var4 + 3, var15 + var8 + 3, var11, var11);

            for (int var12 = 0; var12 < textLines.size(); ++var12) {
                String var13 = (String) textLines.get(var12);
                this.fontRendererObj.func_175063_a(var13, (float) var14, (float) var15, -1);

                if (var12 == 0) {
                    var15 += 2;
                }

                var15 += 10;
            }

            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    protected void func_175272_a(IChatComponent p_175272_1_, int p_175272_2_, int p_175272_3_) {
        if (p_175272_1_ != null && p_175272_1_.getChatStyle().getChatHoverEvent() != null) {
            HoverEvent var4 = p_175272_1_.getChatStyle().getChatHoverEvent();

            if (var4.getAction() == HoverEvent.Action.SHOW_ITEM) {
                ItemStack var5 = null;

                try {
                    NBTTagCompound var6 = JsonToNBT.func_180713_a(var4.getValue().getUnformattedText());

                    if (var6 instanceof NBTTagCompound) {
                        var5 = ItemStack.loadItemStackFromNBT((NBTTagCompound) var6);
                    }
                } catch (NBTException var11) {
                    ;
                }

                if (var5 != null) {
                    this.renderToolTip(var5, p_175272_2_, p_175272_3_);
                } else {
                    this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Item!", p_175272_2_, p_175272_3_);
                }
            } else {
                String var8;

                if (var4.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                    if (this.mc.gameSettings.advancedItemTooltips) {
                        try {
                            NBTTagCompound var12 = JsonToNBT.func_180713_a(var4.getValue().getUnformattedText());

                            if (var12 instanceof NBTTagCompound) {
                                ArrayList var14 = Lists.newArrayList();
                                NBTTagCompound var7 = (NBTTagCompound) var12;
                                var14.add(var7.getString("name"));

                                if (var7.hasKey("type", 8)) {
                                    var8 = var7.getString("type");
                                    var14.add("Type: " + var8 + " (" + EntityList.func_180122_a(var8) + ")");
                                }

                                var14.add(var7.getString("id"));
                                this.drawHoveringText(var14, p_175272_2_, p_175272_3_);
                            } else {
                                this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", p_175272_2_, p_175272_3_);
                            }
                        } catch (NBTException var10) {
                            this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid Entity!", p_175272_2_, p_175272_3_);
                        }
                    }
                } else if (var4.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    this.drawHoveringText(field_175285_g.splitToList(var4.getValue().getFormattedText()), p_175272_2_, p_175272_3_);
                } else if (var4.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                    StatBase var13 = StatList.getOneShotStat(var4.getValue().getUnformattedText());

                    if (var13 != null) {
                        IChatComponent var15 = var13.getStatName();
                        ChatComponentTranslation var16 = new ChatComponentTranslation("stats.tooltip.type." + (var13.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                        var16.getChatStyle().setItalic(Boolean.valueOf(true));
                        var8 = var13 instanceof Achievement ? ((Achievement) var13).getDescription() : null;
                        ArrayList var9 = Lists.newArrayList(new String[]{var15.getFormattedText(), var16.getFormattedText()});

                        if (var8 != null) {
                            var9.addAll(this.fontRendererObj.listFormattedStringToWidth(var8, 150));
                        }

                        this.drawHoveringText(var9, p_175272_2_, p_175272_3_);
                    } else {
                        this.drawCreativeTabHoveringText(EnumChatFormatting.RED + "Invalid statistic/achievement!", p_175272_2_, p_175272_3_);
                    }
                }
            }

            GlStateManager.disableLighting();
        }
    }

    protected void func_175274_a(String p_175274_1_, boolean p_175274_2_) {
    }

    protected boolean func_175276_a(IChatComponent p_175276_1_) {
        if (p_175276_1_ == null) {
            return false;
        } else {
            ClickEvent var2 = p_175276_1_.getChatStyle().getChatClickEvent();

            if (isShiftKeyDown()) {
                if (p_175276_1_.getChatStyle().getInsertion() != null) {
                    this.func_175274_a(p_175276_1_.getChatStyle().getInsertion(), false);
                }
            } else if (var2 != null) {
                URI var3;

                if (var2.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.mc.gameSettings.chatLinks) {
                        return false;
                    }

                    try {
                        var3 = new URI(var2.getValue());

                        if (!field_175284_f.contains(var3.getScheme().toLowerCase())) {
                            throw new URISyntaxException(var2.getValue(), "Unsupported protocol: " + var3.getScheme().toLowerCase());
                        }

                        if (this.mc.gameSettings.chatLinksPrompt) {
                            this.field_175286_t = var3;
                            this.mc.displayGuiScreen(new GuiConfirmOpenLink(this, var2.getValue(), 31102009, false));
                        } else {
                            this.func_175282_a(var3);
                        }
                    } catch (URISyntaxException var4) {
                        field_175287_a.error("Can\'t open url for " + var2, var4);
                    }
                } else if (var2.getAction() == ClickEvent.Action.OPEN_FILE) {
                    var3 = (new File(var2.getValue())).toURI();
                    this.func_175282_a(var3);
                } else if (var2.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.func_175274_a(var2.getValue(), true);
                } else if (var2.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    this.func_175281_b(var2.getValue(), false);
                } else if (var2.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                    ChatUserInfo var5 = this.mc.getTwitchStream().func_152926_a(var2.getValue());

                    if (var5 != null) {
                        this.mc.displayGuiScreen(new GuiTwitchUserMode(this.mc.getTwitchStream(), var5));
                    } else {
                        field_175287_a.error("Tried to handle twitch user but couldn\'t find them!");
                    }
                } else {
                    field_175287_a.error("Don\'t know how to handle " + var2);
                }

                return true;
            }

            return false;
        }
    }

    public void func_175275_f(String p_175275_1_) {
        this.func_175281_b(p_175275_1_, true);
    }

    public void func_175281_b(String p_175281_1_, boolean p_175281_2_) {
        if (p_175281_2_) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(p_175281_1_);
        }

        this.mc.thePlayer.sendChatMessage(p_175281_1_);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int var4 = 0; var4 < this.buttonList.size(); ++var4) {
                GuiButton var5 = (GuiButton) this.buttonList.get(var4);

                if (var5.mousePressed(this.mc, mouseX, mouseY)) {
                    this.selectedButton = var5;
                    var5.playPressSound(this.mc.getSoundHandler());
                    this.actionPerformed(var5);
                }
            }
        }
    }

    /**
     * Called when a mouse button is released.  Args : mouseX, mouseY, releaseButton
     */
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }

    /**
     * Called when a mouse button is pressed and the mouse is moved around. Parameters are : mouseX, mouseY,
     * lastButtonClicked & timeSinceMouseClick.
     */
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    /**
     * Causes the screen to lay out its subcomponents again. This is the equivalent of the Java call
     * Container.validate()
     */
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.initGui();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
    }

    /**
     * Delegates mouse and keyboard input.
     */
    public void handleInput() throws IOException {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.handleMouseInput();
            }
        }

        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.handleKeyboardInput();
            }
        }
    }

    /**
     * Handles mouse input.
     */
    public void handleMouseInput() throws IOException {
        int var1 = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int var2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int var3 = Mouse.getEventButton();

        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }

            this.eventButton = var3;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(var1, var2, this.eventButton);
        } else if (var3 != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }

            this.eventButton = -1;
            this.mouseReleased(var1, var2, var3);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long var4 = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(var1, var2, this.eventButton, var4);
        }
    }

    /**
     * Handles keyboard input.
     */
    public void handleKeyboardInput() throws IOException {
        if (Keyboard.getEventKeyState()) {
            this.keyTyped(Keyboard.getEventCharacter(), Keyboard.getEventKey());
        }

        this.mc.dispatchKeypresses();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int tint) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(tint);
        }
    }

    /**
     * Draws the background (i is always 0 as of 1.2.2)
     */
    public void drawBackground(int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator var2 = Tessellator.getInstance();
        WorldRenderer var3 = var2.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float var4 = 32.0F;
        var3.startDrawingQuads();
        var3.func_178991_c(4210752);
        var3.addVertexWithUV(0.0D, (double) this.height, 0.0D, 0.0D, (double) ((float) this.height / var4 + (float) tint));
        var3.addVertexWithUV((double) this.width, (double) this.height, 0.0D, (double) ((float) this.width / var4), (double) ((float) this.height / var4 + (float) tint));
        var3.addVertexWithUV((double) this.width, 0.0D, 0.0D, (double) ((float) this.width / var4), (double) tint);
        var3.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, (double) tint);
        var2.draw();
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return true;
    }

    public void confirmClicked(boolean result, int id) {
        if (id == 31102009) {
            if (result) {
                this.func_175282_a(this.field_175286_t);
            }

            this.field_175286_t = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void func_175282_a(URI p_175282_1_) {
        try {
            Class var2 = Class.forName("java.awt.Desktop");
            Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
            var2.getMethod("browse", new Class[]{URI.class}).invoke(var3, new Object[]{p_175282_1_});
        } catch (Throwable var4) {
            field_175287_a.error("Couldn\'t open link", var4);
        }
    }

    /**
     * Returns true if either windows ctrl key is down or if either mac meta key is down
     */
    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220) : Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
    }

    /**
     * Returns true if either shift key is down
     */
    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    public static boolean func_175283_s() {
        return Keyboard.isKeyDown(56) || Keyboard.isKeyDown(184);
    }

    public static boolean func_175277_d(int p_175277_0_) {
        return p_175277_0_ == 45 && isCtrlKeyDown();
    }

    public static boolean func_175279_e(int p_175279_0_) {
        return p_175279_0_ == 47 && isCtrlKeyDown();
    }

    public static boolean func_175280_f(int p_175280_0_) {
        return p_175280_0_ == 46 && isCtrlKeyDown();
    }

    public static boolean func_175278_g(int p_175278_0_) {
        return p_175278_0_ == 30 && isCtrlKeyDown();
    }

    public void func_175273_b(Minecraft mcIn, int p_175273_2_, int p_175273_3_) {
        this.setWorldAndResolution(mcIn, p_175273_2_, p_175273_3_);
    }
}
