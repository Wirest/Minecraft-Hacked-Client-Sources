// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.util.ChatComponentText;
import java.util.Iterator;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class GuiNewChat extends Gui
{
    private static final Logger logger;
    private final Minecraft mc;
    private final List<String> sentMessages;
    private final List<ChatLine> chatLines;
    private final List<ChatLine> field_146253_i;
    private int scrollPos;
    private boolean isScrolled;
    
    static {
        logger = LogManager.getLogger();
    }
    
    public GuiNewChat(final Minecraft mcIn) {
        this.sentMessages = (List<String>)Lists.newArrayList();
        this.chatLines = (List<ChatLine>)Lists.newArrayList();
        this.field_146253_i = (List<ChatLine>)Lists.newArrayList();
        this.mc = mcIn;
    }
    
    public void drawChat(final int p_146230_1_) {
        if (this.mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            final int i = this.getLineCount();
            boolean flag = false;
            int j = 0;
            final int k = this.field_146253_i.size();
            final float f = this.mc.gameSettings.chatOpacity * 0.9f + 0.1f;
            if (k > 0) {
                if (this.getChatOpen()) {
                    flag = true;
                }
                final float f2 = this.getChatScale();
                final int l = MathHelper.ceiling_float_int(this.getChatWidth() / f2);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0f, 20.0f, 0.0f);
                GlStateManager.scale(f2, f2, 1.0f);
                for (int i2 = 0; i2 + this.scrollPos < this.field_146253_i.size() && i2 < i; ++i2) {
                    final ChatLine chatline = this.field_146253_i.get(i2 + this.scrollPos);
                    if (chatline != null) {
                        final int j2 = p_146230_1_ - chatline.getUpdatedCounter();
                        if (j2 < 200 || flag) {
                            double d0 = j2 / 200.0;
                            d0 = 1.0 - d0;
                            d0 *= 10.0;
                            d0 = MathHelper.clamp_double(d0, 0.0, 1.0);
                            d0 *= d0;
                            int l2 = (int)(255.0 * d0);
                            if (flag) {
                                l2 = 255;
                            }
                            l2 *= (int)f;
                            ++j;
                            if (l2 > 3) {
                                final int i3 = 0;
                                final int j3 = -i2 * 9;
                                Gui.drawRect(i3, j3 - 9, i3 + l + 4, j3, l2 / 2 << 24);
                                final String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                this.mc.fontRendererObj.drawStringWithShadow(s, (float)i3, (float)(j3 - 8), 16777215 + (l2 << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }
                if (flag) {
                    final int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0f, 0.0f, 0.0f);
                    final int l3 = k * k2 + k;
                    final int i4 = j * k2 + j;
                    final int j4 = this.scrollPos * i4 / k;
                    final int k3 = i4 * i4 / l3;
                    if (l3 != i4) {
                        final int k4 = (j4 > 0) ? 170 : 96;
                        final int l4 = this.isScrolled ? 13382451 : 3355562;
                        Gui.drawRect(0.0, -j4, 2.0, -j4 - k3, l4 + (k4 << 24));
                        Gui.drawRect(2.0, -j4, 1.0, -j4 - k3, 13421772 + (k4 << 24));
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
    
    public void clearChatMessages() {
        this.field_146253_i.clear();
        this.chatLines.clear();
        this.sentMessages.clear();
    }
    
    public void printChatMessage(final IChatComponent p_146227_1_) {
        this.printChatMessageWithOptionalDeletion(p_146227_1_, 0);
    }
    
    public void printChatMessageWithOptionalDeletion(final IChatComponent p_146234_1_, final int p_146234_2_) {
        this.setChatLine(p_146234_1_, p_146234_2_, this.mc.ingameGUI.getUpdateCounter(), false);
        GuiNewChat.logger.info("[CHAT] " + p_146234_1_.getUnformattedText());
    }
    
    private void setChatLine(final IChatComponent p_146237_1_, final int p_146237_2_, final int p_146237_3_, final boolean p_146237_4_) {
        if (p_146237_2_ != 0) {
            this.deleteChatLine(p_146237_2_);
        }
        final int i = MathHelper.floor_float(this.getChatWidth() / this.getChatScale());
        final List<IChatComponent> list = GuiUtilRenderComponents.func_178908_a(p_146237_1_, i, this.mc.fontRendererObj, false, false);
        final boolean flag = this.getChatOpen();
        for (final IChatComponent ichatcomponent : list) {
            if (flag && this.scrollPos > 0) {
                this.isScrolled = true;
                this.scroll(1);
            }
            this.field_146253_i.add(0, new ChatLine(p_146237_3_, ichatcomponent, p_146237_2_));
        }
        while (this.field_146253_i.size() > 100) {
            this.field_146253_i.remove(this.field_146253_i.size() - 1);
        }
        if (!p_146237_4_) {
            this.chatLines.add(0, new ChatLine(p_146237_3_, p_146237_1_, p_146237_2_));
            while (this.chatLines.size() > 100) {
                this.chatLines.remove(this.chatLines.size() - 1);
            }
        }
    }
    
    public void refreshChat() {
        this.field_146253_i.clear();
        this.resetScroll();
        for (int i = this.chatLines.size() - 1; i >= 0; --i) {
            final ChatLine chatline = this.chatLines.get(i);
            this.setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
        }
    }
    
    public List<String> getSentMessages() {
        return this.sentMessages;
    }
    
    public void addToSentMessages(final String p_146239_1_) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(p_146239_1_)) {
            this.sentMessages.add(p_146239_1_);
        }
    }
    
    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }
    
    public void scroll(final int p_146229_1_) {
        this.scrollPos += p_146229_1_;
        final int i = this.field_146253_i.size();
        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }
        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }
    }
    
    public IChatComponent getChatComponent(final int p_146236_1_, final int p_146236_2_) {
        if (!this.getChatOpen()) {
            return null;
        }
        final ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        final int i = scaledresolution.getScaleFactor();
        final float f = this.getChatScale();
        int j = p_146236_1_ / i - 3;
        int k = p_146236_2_ / i - 27;
        j = MathHelper.floor_float(j / f);
        k = MathHelper.floor_float(k / f);
        if (j < 0 || k < 0) {
            return null;
        }
        final int l = Math.min(this.getLineCount(), this.field_146253_i.size());
        if (j <= MathHelper.floor_float(this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
            final int i2 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
            if (i2 >= 0 && i2 < this.field_146253_i.size()) {
                final ChatLine chatline = this.field_146253_i.get(i2);
                int j2 = 0;
                for (final IChatComponent ichatcomponent : chatline.getChatComponent()) {
                    if (ichatcomponent instanceof ChatComponentText) {
                        j2 += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText)ichatcomponent).getChatComponentText_TextValue(), false));
                        if (j2 > j) {
                            return ichatcomponent;
                        }
                        continue;
                    }
                }
            }
            return null;
        }
        return null;
    }
    
    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GuiChat;
    }
    
    public void deleteChatLine(final int p_146242_1_) {
        Iterator<ChatLine> iterator = this.field_146253_i.iterator();
        while (iterator.hasNext()) {
            final ChatLine chatline = iterator.next();
            if (chatline.getChatLineID() == p_146242_1_) {
                iterator.remove();
            }
        }
        iterator = this.chatLines.iterator();
        while (iterator.hasNext()) {
            final ChatLine chatline2 = iterator.next();
            if (chatline2.getChatLineID() == p_146242_1_) {
                iterator.remove();
                break;
            }
        }
    }
    
    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }
    
    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }
    
    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }
    
    public static int calculateChatboxWidth(final float p_146233_0_) {
        final int i = 320;
        final int j = 40;
        return MathHelper.floor_float(p_146233_0_ * (i - j) + j);
    }
    
    public static int calculateChatboxHeight(final float p_146243_0_) {
        final int i = 180;
        final int j = 20;
        return MathHelper.floor_float(p_146243_0_ * (i - j) + j);
    }
    
    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
