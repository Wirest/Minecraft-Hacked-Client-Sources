// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import java.util.Iterator;
import net.minecraft.event.ClickEvent;
import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import net.minecraft.item.ItemEditableBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ChatAllowedCharacters;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.init.Items;
import net.minecraft.util.ChatComponentText;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.IChatComponent;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiScreenBook extends GuiScreen
{
    private static final Logger logger;
    private static final ResourceLocation bookGuiTextures;
    private final EntityPlayer editingPlayer;
    private final ItemStack bookObj;
    private final boolean bookIsUnsigned;
    private boolean bookIsModified;
    private boolean bookGettingSigned;
    private int updateCount;
    private int bookImageWidth;
    private int bookImageHeight;
    private int bookTotalPages;
    private int currPage;
    private NBTTagList bookPages;
    private String bookTitle;
    private List<IChatComponent> field_175386_A;
    private int field_175387_B;
    private NextPageButton buttonNextPage;
    private NextPageButton buttonPreviousPage;
    private GuiButton buttonDone;
    private GuiButton buttonSign;
    private GuiButton buttonFinalize;
    private GuiButton buttonCancel;
    
    static {
        logger = LogManager.getLogger();
        bookGuiTextures = new ResourceLocation("textures/gui/book.png");
    }
    
    public GuiScreenBook(final EntityPlayer player, final ItemStack book, final boolean isUnsigned) {
        this.bookImageWidth = 192;
        this.bookImageHeight = 192;
        this.bookTotalPages = 1;
        this.bookTitle = "";
        this.field_175387_B = -1;
        this.editingPlayer = player;
        this.bookObj = book;
        this.bookIsUnsigned = isUnsigned;
        if (book.hasTagCompound()) {
            final NBTTagCompound nbttagcompound = book.getTagCompound();
            this.bookPages = nbttagcompound.getTagList("pages", 8);
            if (this.bookPages != null) {
                this.bookPages = (NBTTagList)this.bookPages.copy();
                this.bookTotalPages = this.bookPages.tagCount();
                if (this.bookTotalPages < 1) {
                    this.bookTotalPages = 1;
                }
            }
        }
        if (this.bookPages == null && isUnsigned) {
            (this.bookPages = new NBTTagList()).appendTag(new NBTTagString(""));
            this.bookTotalPages = 1;
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        ++this.updateCount;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        if (this.bookIsUnsigned) {
            this.buttonList.add(this.buttonSign = new GuiButton(3, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.signButton", new Object[0])));
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.done", new Object[0])));
            this.buttonList.add(this.buttonFinalize = new GuiButton(5, this.width / 2 - 100, 4 + this.bookImageHeight, 98, 20, I18n.format("book.finalizeButton", new Object[0])));
            this.buttonList.add(this.buttonCancel = new GuiButton(4, this.width / 2 + 2, 4 + this.bookImageHeight, 98, 20, I18n.format("gui.cancel", new Object[0])));
        }
        else {
            this.buttonList.add(this.buttonDone = new GuiButton(0, this.width / 2 - 100, 4 + this.bookImageHeight, 200, 20, I18n.format("gui.done", new Object[0])));
        }
        final int i = (this.width - this.bookImageWidth) / 2;
        final int j = 2;
        this.buttonList.add(this.buttonNextPage = new NextPageButton(1, i + 120, j + 154, true));
        this.buttonList.add(this.buttonPreviousPage = new NextPageButton(2, i + 38, j + 154, false));
        this.updateButtons();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    private void updateButtons() {
        this.buttonNextPage.visible = (!this.bookGettingSigned && (this.currPage < this.bookTotalPages - 1 || this.bookIsUnsigned));
        this.buttonPreviousPage.visible = (!this.bookGettingSigned && this.currPage > 0);
        this.buttonDone.visible = (!this.bookIsUnsigned || !this.bookGettingSigned);
        if (this.bookIsUnsigned) {
            this.buttonSign.visible = !this.bookGettingSigned;
            this.buttonCancel.visible = this.bookGettingSigned;
            this.buttonFinalize.visible = this.bookGettingSigned;
            this.buttonFinalize.enabled = (this.bookTitle.trim().length() > 0);
        }
    }
    
    private void sendBookToServer(final boolean publish) throws IOException {
        if (this.bookIsUnsigned && this.bookIsModified && this.bookPages != null) {
            while (this.bookPages.tagCount() > 1) {
                final String s = this.bookPages.getStringTagAt(this.bookPages.tagCount() - 1);
                if (s.length() != 0) {
                    break;
                }
                this.bookPages.removeTag(this.bookPages.tagCount() - 1);
            }
            if (this.bookObj.hasTagCompound()) {
                final NBTTagCompound nbttagcompound = this.bookObj.getTagCompound();
                nbttagcompound.setTag("pages", this.bookPages);
            }
            else {
                this.bookObj.setTagInfo("pages", this.bookPages);
            }
            String s2 = "MC|BEdit";
            if (publish) {
                s2 = "MC|BSign";
                this.bookObj.setTagInfo("author", new NBTTagString(this.editingPlayer.getName()));
                this.bookObj.setTagInfo("title", new NBTTagString(this.bookTitle.trim()));
                for (int i = 0; i < this.bookPages.tagCount(); ++i) {
                    String s3 = this.bookPages.getStringTagAt(i);
                    final IChatComponent ichatcomponent = new ChatComponentText(s3);
                    s3 = IChatComponent.Serializer.componentToJson(ichatcomponent);
                    this.bookPages.set(i, new NBTTagString(s3));
                }
                this.bookObj.setItem(Items.written_book);
            }
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeItemStackToBuffer(this.bookObj);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s2, packetbuffer));
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.mc.displayGuiScreen(null);
                this.sendBookToServer(false);
            }
            else if (button.id == 3 && this.bookIsUnsigned) {
                this.bookGettingSigned = true;
            }
            else if (button.id == 1) {
                if (this.currPage < this.bookTotalPages - 1) {
                    ++this.currPage;
                }
                else if (this.bookIsUnsigned) {
                    this.addNewPage();
                    if (this.currPage < this.bookTotalPages - 1) {
                        ++this.currPage;
                    }
                }
            }
            else if (button.id == 2) {
                if (this.currPage > 0) {
                    --this.currPage;
                }
            }
            else if (button.id == 5 && this.bookGettingSigned) {
                this.sendBookToServer(true);
                this.mc.displayGuiScreen(null);
            }
            else if (button.id == 4 && this.bookGettingSigned) {
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
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (this.bookIsUnsigned) {
            if (this.bookGettingSigned) {
                this.keyTypedInTitle(typedChar, keyCode);
            }
            else {
                this.keyTypedInBook(typedChar, keyCode);
            }
        }
    }
    
    private void keyTypedInBook(final char typedChar, final int keyCode) {
        if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
        }
        else {
            switch (keyCode) {
                case 14: {
                    final String s = this.pageGetCurrent();
                    if (s.length() > 0) {
                        this.pageSetCurrent(s.substring(0, s.length() - 1));
                    }
                }
                case 28:
                case 156: {
                    this.pageInsertIntoCurrent("\n");
                }
                default: {
                    if (ChatAllowedCharacters.isAllowedCharacter(typedChar)) {
                        this.pageInsertIntoCurrent(Character.toString(typedChar));
                        break;
                    }
                    break;
                }
            }
        }
    }
    
    private void keyTypedInTitle(final char p_146460_1_, final int p_146460_2_) throws IOException {
        switch (p_146460_2_) {
            case 14: {
                if (!this.bookTitle.isEmpty()) {
                    this.bookTitle = this.bookTitle.substring(0, this.bookTitle.length() - 1);
                    this.updateButtons();
                }
            }
            case 28:
            case 156: {
                if (!this.bookTitle.isEmpty()) {
                    this.sendBookToServer(true);
                    this.mc.displayGuiScreen(null);
                }
            }
            default: {
                if (this.bookTitle.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(p_146460_1_)) {
                    this.bookTitle = String.valueOf(this.bookTitle) + Character.toString(p_146460_1_);
                    this.updateButtons();
                    this.bookIsModified = true;
                }
            }
        }
    }
    
    private String pageGetCurrent() {
        return (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) ? this.bookPages.getStringTagAt(this.currPage) : "";
    }
    
    private void pageSetCurrent(final String p_146457_1_) {
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
            this.bookPages.set(this.currPage, new NBTTagString(p_146457_1_));
            this.bookIsModified = true;
        }
    }
    
    private void pageInsertIntoCurrent(final String p_146459_1_) {
        final String s = this.pageGetCurrent();
        final String s2 = String.valueOf(s) + p_146459_1_;
        final int i = this.fontRendererObj.splitStringWidth(String.valueOf(s2) + EnumChatFormatting.BLACK + "_", 118);
        if (i <= 128 && s2.length() < 256) {
            this.pageSetCurrent(s2);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiScreenBook.bookGuiTextures);
        final int i = (this.width - this.bookImageWidth) / 2;
        final int j = 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);
        if (this.bookGettingSigned) {
            String s = this.bookTitle;
            if (this.bookIsUnsigned) {
                if (this.updateCount / 6 % 2 == 0) {
                    s = String.valueOf(s) + EnumChatFormatting.BLACK + "_";
                }
                else {
                    s = String.valueOf(s) + EnumChatFormatting.GRAY + "_";
                }
            }
            final String s2 = I18n.format("book.editTitle", new Object[0]);
            final int k = this.fontRendererObj.getStringWidth(s2);
            this.fontRendererObj.drawString(s2, i + 36 + (116 - k) / 2, j + 16 + 16, 0);
            final int l = this.fontRendererObj.getStringWidth(s);
            this.fontRendererObj.drawString(s, i + 36 + (116 - l) / 2, j + 48, 0);
            final String s3 = I18n.format("book.byAuthor", this.editingPlayer.getName());
            final int i2 = this.fontRendererObj.getStringWidth(s3);
            this.fontRendererObj.drawString(EnumChatFormatting.DARK_GRAY + s3, i + 36 + (116 - i2) / 2, j + 48 + 10, 0);
            final String s4 = I18n.format("book.finalizeWarning", new Object[0]);
            this.fontRendererObj.drawSplitString(s4, i + 36, j + 80, 116, 0);
        }
        else {
            final String s5 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
            String s6 = "";
            if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.tagCount()) {
                s6 = this.bookPages.getStringTagAt(this.currPage);
            }
            if (this.bookIsUnsigned) {
                if (this.fontRendererObj.getBidiFlag()) {
                    s6 = String.valueOf(s6) + "_";
                }
                else if (this.updateCount / 6 % 2 == 0) {
                    s6 = String.valueOf(s6) + EnumChatFormatting.BLACK + "_";
                }
                else {
                    s6 = String.valueOf(s6) + EnumChatFormatting.GRAY + "_";
                }
            }
            else if (this.field_175387_B != this.currPage) {
                if (ItemEditableBook.validBookTagContents(this.bookObj.getTagCompound())) {
                    try {
                        final IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s6);
                        this.field_175386_A = ((ichatcomponent != null) ? GuiUtilRenderComponents.func_178908_a(ichatcomponent, 116, this.fontRendererObj, true, true) : null);
                    }
                    catch (JsonParseException var13) {
                        this.field_175386_A = null;
                    }
                }
                else {
                    final ChatComponentText chatcomponenttext = new ChatComponentText(String.valueOf(EnumChatFormatting.DARK_RED.toString()) + "* Invalid book tag *");
                    this.field_175386_A = (List<IChatComponent>)Lists.newArrayList((Iterable<?>)chatcomponenttext);
                }
                this.field_175387_B = this.currPage;
            }
            final int j2 = this.fontRendererObj.getStringWidth(s5);
            this.fontRendererObj.drawString(s5, i - j2 + this.bookImageWidth - 44, j + 16, 0);
            if (this.field_175386_A == null) {
                this.fontRendererObj.drawSplitString(s6, i + 36, j + 16 + 16, 116, 0);
            }
            else {
                for (int k2 = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size()), l2 = 0; l2 < k2; ++l2) {
                    final IChatComponent ichatcomponent2 = this.field_175386_A.get(l2);
                    this.fontRendererObj.drawString(ichatcomponent2.getUnformattedText(), i + 36, j + 16 + 16 + l2 * this.fontRendererObj.FONT_HEIGHT, 0);
                }
                final IChatComponent ichatcomponent3 = this.func_175385_b(mouseX, mouseY);
                if (ichatcomponent3 != null) {
                    this.handleComponentHover(ichatcomponent3, mouseX, mouseY);
                }
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        if (mouseButton == 0) {
            final IChatComponent ichatcomponent = this.func_175385_b(mouseX, mouseY);
            if (this.handleComponentClick(ichatcomponent)) {
                return;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected boolean handleComponentClick(final IChatComponent p_175276_1_) {
        final ClickEvent clickevent = (p_175276_1_ == null) ? null : p_175276_1_.getChatStyle().getChatClickEvent();
        if (clickevent == null) {
            return false;
        }
        if (clickevent.getAction() == ClickEvent.Action.CHANGE_PAGE) {
            final String s = clickevent.getValue();
            try {
                final int i = Integer.parseInt(s) - 1;
                if (i >= 0 && i < this.bookTotalPages && i != this.currPage) {
                    this.currPage = i;
                    this.updateButtons();
                    return true;
                }
            }
            catch (Throwable t) {}
            return false;
        }
        final boolean flag = super.handleComponentClick(p_175276_1_);
        if (flag && clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
            this.mc.displayGuiScreen(null);
        }
        return flag;
    }
    
    public IChatComponent func_175385_b(final int p_175385_1_, final int p_175385_2_) {
        if (this.field_175386_A == null) {
            return null;
        }
        final int i = p_175385_1_ - (this.width - this.bookImageWidth) / 2 - 36;
        final int j = p_175385_2_ - 2 - 16 - 16;
        if (i < 0 || j < 0) {
            return null;
        }
        final int k = Math.min(128 / this.fontRendererObj.FONT_HEIGHT, this.field_175386_A.size());
        if (i <= 116 && j < this.mc.fontRendererObj.FONT_HEIGHT * k + k) {
            final int l = j / this.mc.fontRendererObj.FONT_HEIGHT;
            if (l >= 0 && l < this.field_175386_A.size()) {
                final IChatComponent ichatcomponent = this.field_175386_A.get(l);
                int i2 = 0;
                for (final IChatComponent ichatcomponent2 : ichatcomponent) {
                    if (ichatcomponent2 instanceof ChatComponentText) {
                        i2 += this.mc.fontRendererObj.getStringWidth(((ChatComponentText)ichatcomponent2).getChatComponentText_TextValue());
                        if (i2 > i) {
                            return ichatcomponent2;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    static class NextPageButton extends GuiButton
    {
        private final boolean field_146151_o;
        
        public NextPageButton(final int p_i46316_1_, final int p_i46316_2_, final int p_i46316_3_, final boolean p_i46316_4_) {
            super(p_i46316_1_, p_i46316_2_, p_i46316_3_, 23, 13, "");
            this.field_146151_o = p_i46316_4_;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
            if (this.visible) {
                final boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                mc.getTextureManager().bindTexture(GuiScreenBook.bookGuiTextures);
                int i = 0;
                int j = 192;
                if (flag) {
                    i += 23;
                }
                if (!this.field_146151_o) {
                    j += 13;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, i, j, 23, 13);
            }
        }
    }
}
