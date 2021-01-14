package net.minecraft.client.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

public class GuiScreenBook extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation bookGuiTextures = new ResourceLocation("textures/gui/book.png");

    /**
     * The player editing the book
     */
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;

    /**
     * Whether the book is signed or can still be edited
     */
    private final boolean bookIsUnsigned;

    /**
     * Whether the book's title or contents has been modified since being opened
     */
    private boolean bookIsModified;

    /**
     * Determines if the signing screen is open
     */
    private boolean bookGettingSigned;

    /**
     * Update ticks since the gui was opened
     */
    private int updateCount;
    private int bookImageWidth = 192;
    private int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle = "";
    private List field_175386_A;
    private int field_175387_B = -1;
    private GuiScreenBook.NextPageButton buttonNextPage;
    private GuiScreenBook.NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;

    /**
     * The GuiButton to sign this book.
     */
    private GuiButton buttonSign;
    private GuiButton buttonFinalize;
    private GuiButton buttonCancel;
    private static final String __OBFID = "CL_00000744";

    public GuiScreenBook(EntityPlayer p_i1080_1_, ItemStack p_i1080_2_, boolean p_i1080_3_) {
        this.editingPlayer = p_i1080_1_;
        this.bookObj = p_i1080_2_;
        this.bookIsUnsigned = p_i1080_3_;

        if (p_i1080_2_.hasTagCompound()) {
            NBTTagCompound var4 = p_i1080_2_.getTagCompound();
            this.bookPages = var4.getTagList("pages", 8);

            if (this.bookPages != null) {
                this.bookPages = (NBTTagList) this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();

                if (this.bookTotalPages < 1) {
                    this.bookTotalPages = 1;
                }
            }
        }

        if (this.bookPages == null && p_i1080_3_) {
            this.bookPages = new NBTTagList();
            this.bookPages.appendTag(new NBTTagString(""));
            this.bookTotalPages = 1;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        ++this.updateCount;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);

        if (this.bookIsUnsigned) {
            this.buttonList.add(this.buttonSign = new GuiButton(3, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.signButton", new Object[0])));
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0])));
            this.buttonList.add(this.buttonFinalize = new GuiButton(5, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
            this.buttonList.add(this.buttonCancel = new GuiButton(4, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0])));
        } else {
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0])));
        }

        int var1 = (this.width - this.bookImageWidth) / 2;
        byte var2 = 2;
        this.buttonList.add(this.buttonNextPage = new GuiScreenBook.NextPageButton(1, var1 + 120, var2 + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new GuiScreenBook.NextPageButton(2, var1 + 38, var2 + 154, false));
        this.updateButtons();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    private void updateButtons() {
        this.buttonNextPage.visible = !this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned);
        this.buttonPreviousPage.visible = !this.bookGettingSigned && this.currPage > 0;
        this.buttonDone.visible = !this.bookIsUnsigned || !this.bookGettingSigned;

        if (this.bookIsUnsigned) {
            this.buttonSign.visible = !this.bookGettingSigned;
            this.buttonCancel.visible = this.bookGettingSigned;
            this.buttonFinalize.visible = this.bookGettingSigned;
            this.buttonFinalize.enabled = this.bookTitle.trim().length() > 0;
        }
    }

    private void sendBookToServer(boolean p_146462_1_) throws IOException {
        if (this.bookIsUnsigned && this.bookIsModified) {
            if (this.bookPages != null) {
                String var2;

                while (this.bookPages.tagCount() > 1) {
                    var2 = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);

                    if (var2.length() != 0) {
                        break;
                    }

                    this.bookPages.removeTag(this.bookPages.tagCount() - 1);
                }

                if (this.bookObj.hasTagCompound()) {
                    NBTTagCompound var6 = this.bookObj.getTagCompound();
                    var6.setTag("pages", this.bookPages);
                } else {
                    this.bookObj.setTagInfo("pages", this.bookPages);
                }

                var2 = "MC|BEdit";

                if (p_146462_1_) {
                    var2 = "MC|BSign";
                    this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
                    this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));

                    for (int var3 = 0; var3 < this.bookPages.tagCount(); ++var3) {
                        String var4 = this.bookPages.getStringTagAt(var3);
                        ChatComponentText var5 = new ChatComponentText(var4);
                        var4 = IChatComponent.Serializer.componentToJson(var5);
                        this.bookPages.set(var3, new NBTTagString(var4));
                    }

                    this.bookObj.setItem(Items.written_book);
                }

                PacketBuffer var7 = new PacketBuffer(Unpooled.buffer());
                var7.writeItemStackToBuffer(this.bookObj);
                this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(var2, var7));
            }
        }
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen((GuiScreen) null);
                this.sendBookToServer(false);
            } else if (button.id == 3 && this.bookIsUnsigned) {
                this.bookGettingSigned = true;
            } else if (button.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                } else if (this.bookIsUnsigned) {
                    this.addNewPage();

                    if (this.currPage < this.bookTotalPages - 1) {
                        ++this.currPage;
                    }
                }
            } else if (button.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            } else if (button.id == 5 && this.bookGettingSigned) {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen((GuiScreen) null);
            } else if (button.id == 4 && this.bookGettingSigned) {
                this.bookGettingSigned = false;
            }

            this.updateButtons();
        }
    }

    private void addNewPage() {
        if (this.bookPages != null && this.bookPages.tagCount() < 50) {
            this.bookPages.appendTag(new NBTTagString(""));
            ++this.bookTotalPages;
            this.bookIsModified = true;
        }
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (this.bookIsUnsigned) {
            if (this.bookGettingSigned) {
                this.keyTypedInTitle(typedChar, keyCode);
            } else {
                this.keyTypedInBook(typedChar, keyCode);
            }
        }
    }

    /**
     * Processes keystrokes when editing the text of a book
     */
    private void keyTypedInBook(char p_146463_1_, int p_146463_2_) {
        if (GuiScreen.func_175279_e(p_146463_2_)) {
            this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
        } else {
            switch (p_146463_2_) {
                case 14:
                    String var3 = this.pageGetCurrent();

                    if (var3.length() > 0) {
                        this.pageSetCurrent(var3.substring(0, var3.length() - 1));
                    }

                    return;

                case 28:
                case 156:
                    this.pageInsertIntoCurrent("\n");
                    return;

                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(p_146463_1_)) {
                        this.pageInsertIntoCurrent(Character.toString(p_146463_1_));
                    }
            }
        }
    }

    /**
     * Processes keystrokes when editing the title of a book
     */
    private void keyTypedInTitle(char p_146460_1_, int p_146460_2_) throws IOException {
        switch (p_146460_2_) {
            case 14:
                if (!this.bookTitle.isEmpty()) {
                    this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
                    this.updateButtons();
                }

                return;

            case 28:
            case 156:
                if (!this.bookTitle.isEmpty()) {
                    this.sendBookToServer(true);
                    this.mc.displayGuiScreen((GuiScreen) null);
                }

                return;

            default:
                if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
                    this.bookTitle = this.bookTitle + Character.toString(p_146460_1_);
                    this.updateButtons();
                    this.bookIsModified = true;
                }
        }
    }

    /**
     * Returns the entire text of the current page as determined by currPage
     */
    private String pageGetCurrent() {
        return this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount() ? this.bookPages.getStringTagAt(this.currPage) : "";
    }

    /**
     * Sets the text of the current page as determined by currPage
     */
    private void pageSetCurrent(String p_146457_1_) {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            this.bookPages.set(this.currPage, new NBTTagString(p_146457_1_));
            this.bookIsModified = true;
        }
    }

    /**
     * Processes any text getting inserted into the current page, enforcing the page size limit
     */
    private void pageInsertIntoCurrent(String p_146459_1_) {
        String var2 = this.pageGetCurrent();
        String var3 = var2 + p_146459_1_;
        int var4 = this.fontRendererObj.splitStringWidth(var3 + "" + EnumChatFormatting.BLACK + "_", 118);

        if (var4 <= 128 && var3.length() < 256) {
            this.pageSetCurrent(var3);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(bookGuiTextures);
        int var4 = (this.width - this.bookImageWidth) / 2;
        byte var5 = 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.bookImageWidth, this.bookImageHeight);
        String var6;
        String var7;
        int var8;
        int var9;

        if (this.bookGettingSigned) {
            var6 = this.bookTitle;

            if (this.bookIsUnsigned) {
                if (this.updateCount / 6 % 2 == 0) {
                    var6 = var6 + "" + EnumChatFormatting.BLACK + "_";
                } else {
                    var6 = var6 + "" + EnumChatFormatting.GRAY + "_";
                }
            }

            var7 = I18n.format("book.editTitle", new Object[0]);
            var8 = this.fontRendererObj.getStringWidth(var7);
            this.fontRendererObj.drawString(var7, var4 + 36 + (116 - var8) / 2, var5 + 16 + 16, 0);
            var9 = this.fontRendererObj.getStringWidth(var6);
            this.fontRendererObj.drawString(var6, var4 + 36 + (116 - var9) / 2, var5 + 48, 0);
            String var10 = I18n.format("book.byAuthor", new Object[]{this.editingPlayer.getName()});
            int var11 = this.fontRendererObj.getStringWidth(var10);
            this.fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + var10, var4 + 36 + (116 - var11) / 2, var5 + 48 + 10, 0);
            String var12 = I18n.format("book.finalizeWarning", new Object[0]);
            this.fontRendererObj.drawSplitString(var12, var4 + 36, var5 + 80, 116, 0);
        } else {
            var6 = I18n.format("book.pageIndicator", new Object[]{Integer.valueOf(this.currPage + 1), Integer.valueOf(this.bookTotalPages)});
            var7 = "";

            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
                var7 = this.bookPages.getStringTagAt(this.currPage);
            }

            if (this.bookIsUnsigned) {
                if (this.fontRendererObj.getBidiFlag()) {
                    var7 = var7 + "_";
                } else if (this.updateCount / 6 % 2 == 0) {
                    var7 = var7 + "" + EnumChatFormatting.BLACK + "_";
                } else {
                    var7 = var7 + "" + EnumChatFormatting.GRAY + "_";
                }
            } else if (this.field_175387_B != this.currPage) {
                if (ItemEditableBook.validBookTagContents(this.bookObj.getTagCompound())) {
                    try {
                        IChatComponent var14 = IChatComponent.Serializer.jsonToComponent(var7);
                        this.field_175386_A = var14 != null ? GuiUtilRenderComponents.func_178908_a(var14, 116, this.fontRendererObj, true, true) : null;
                    } catch (JsonParseException var13) {
                        this.field_175386_A = null;
                    }
                } else {
                    ChatComponentText var15 = new ChatComponentText(EnumChatFormatting.DARK_RED.toString() + "* Invalid book tag *");
                    this.field_175386_A = Lists.newArrayList(var15);
                }

                this.field_175387_B = this.currPage;
            }

            var8 = this.fontRendererObj.getStringWidth(var6);
            this.fontRendererObj.drawString(var6, var4 - var8 + this.bookImageWidth - 44, var5 + 16, 0);

            if (this.field_175386_A == null) {
                this.fontRendererObj.drawSplitString(var7, var4 + 36, var5 + 16 + 16, 116, 0);
            } else {
                var9 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());

                for (int var16 = 0; var16 < var9; ++var16) {
                    IChatComponent var18 = (IChatComponent) this.field_175386_A.get(var16);
                    this.fontRendererObj.drawString(var18.getUnformattedText(), var4 + 36, var5 + 16 + 16 + var16 * this.fontRendererObj.FONT_HEIGHT, 0);
                }

                IChatComponent var17 = this.func_175385_b(mouseX, mouseY);

                if (var17 != null) {
                    this.func_175272_a(var17, mouseX, mouseY);
                }
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            IChatComponent var4 = this.func_175385_b(mouseX, mouseY);

            if (this.func_175276_a(var4)) {
                return;
            }
        }

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected boolean func_175276_a(IChatComponent p_175276_1_) {
        ClickEvent var2 = p_175276_1_ == null ? null : p_175276_1_.getChatStyle().getChatClickEvent();

        if (var2 == null) {
            return false;
        } else if (var2.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            String var6 = var2.getValue();

            try {
                int var4 = Integer.parseInt(var6) - 1;

                if (var4 >= 0 && var4 < this.bookTotalPages && var4 != this.currPage) {
                    this.currPage = var4;
                    this.updateButtons();
                    return true;
                }
            } catch (Throwable var5) {
                ;
            }

            return false;
        } else {
            boolean var3 = super.func_175276_a(p_175276_1_);

            if (var3 && var2.getAction() == ClickEvent.Action.RUN_COMMAND) {
                this.mc.displayGuiScreen((GuiScreen) null);
            }

            return var3;
        }
    }

    public IChatComponent func_175385_b(int p_175385_1_, int p_175385_2_) {
        if (this.field_175386_A == null) {
            return null;
        } else {
            int var3 = p_175385_1_ - (this.width - this.bookImageWidth) / 2 - 36;
            int var4 = p_175385_2_ - 2 - 16 - 16;

            if (var3 >= 0 && var4 >= 0) {
                int var5 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());

                if (var3 <= 116 && var4 < this.mc.fontRendererObj.FONT_HEIGHT * var5 + var5) {
                    int var6 = var4 / this.mc.fontRendererObj.FONT_HEIGHT;

                    if (var6 >= 0 && var6 < this.field_175386_A.size()) {
                        IChatComponent var7 = (IChatComponent) this.field_175386_A.get(var6);
                        int var8 = 0;
                        Iterator var9 = var7.iterator();

                        while (var9.hasNext()) {
                            IChatComponent var10 = (IChatComponent) var9.next();

                            if (var10 instanceof ChatComponentText) {
                                var8 += this.mc.fontRendererObj.getStringWidth(((ChatComponentText) var10).getChatComponentText_TextValue());

                                if (var8 > var3) {
                                    return var10;
                                }
                            }
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    static class NextPageButton extends GuiButton {
        private final boolean field_146151_o;
        private static final String __OBFID = "CL_00000745";

        public NextPageButton(int p_i46316_1_, int p_i46316_2_, int p_i46316_3_, boolean p_i46316_4_) {
            super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
            this.field_146151_o = p_i46316_4_;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean var4 = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                mc.getTextureManager().bindTexture(GuiScreenBook.bookGuiTextures);
                int var5 = 0;
                int var6 = 192;

                if (var4) {
                    var5 += 23;
                }

                if (!this.field_146151_o) {
                    var6 += 13;
                }

                this.drawTexturedModalRect(this.xPosition, this.yPosition, var5, var6, 23, 13);
            }
        }
    }
}
