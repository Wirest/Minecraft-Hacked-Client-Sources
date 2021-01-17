// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import java.util.Iterator;
import net.minecraft.realms.RealmsButton;
import java.io.IOException;
import net.minecraft.item.ItemStack;
import java.util.List;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.realms.RealmsScreen;

public class GuiScreenRealmsProxy extends GuiScreen
{
    private RealmsScreen field_154330_a;
    
    public GuiScreenRealmsProxy(final RealmsScreen p_i1087_1_) {
        this.field_154330_a = p_i1087_1_;
        super.buttonList = Collections.synchronizedList((List<GuiButton>)Lists.newArrayList());
    }
    
    public RealmsScreen func_154321_a() {
        return this.field_154330_a;
    }
    
    @Override
    public void initGui() {
        this.field_154330_a.init();
        super.initGui();
    }
    
    public void func_154325_a(final String p_154325_1_, final int p_154325_2_, final int p_154325_3_, final int p_154325_4_) {
        super.drawCenteredString(this.fontRendererObj, p_154325_1_, p_154325_2_, p_154325_3_, p_154325_4_);
    }
    
    public void func_154322_b(final String p_154322_1_, final int p_154322_2_, final int p_154322_3_, final int p_154322_4_) {
        super.drawString(this.fontRendererObj, p_154322_1_, p_154322_2_, p_154322_3_, p_154322_4_);
    }
    
    @Override
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        this.field_154330_a.blit(x, y, textureX, textureY, width, height);
        super.drawTexturedModalRect(x, y, textureX, textureY, width, height);
    }
    
    public void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
        super.drawGradientRect(left, top, right, bottom, startColor, endColor);
    }
    
    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
    }
    
    @Override
    public boolean doesGuiPauseGame() {
        return super.doesGuiPauseGame();
    }
    
    @Override
    public void drawWorldBackground(final int tint) {
        super.drawWorldBackground(tint);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.field_154330_a.render(mouseX, mouseY, partialTicks);
    }
    
    public void renderToolTip(final ItemStack stack, final int x, final int y) {
        super.renderToolTip(stack, x, y);
    }
    
    public void drawCreativeTabHoveringText(final String tabName, final int mouseX, final int mouseY) {
        super.drawCreativeTabHoveringText(tabName, mouseX, mouseY);
    }
    
    public void drawHoveringText(final List<String> textLines, final int x, final int y) {
        super.drawHoveringText(textLines, x, y);
    }
    
    @Override
    public void updateScreen() {
        this.field_154330_a.tick();
        super.updateScreen();
    }
    
    public int func_154329_h() {
        return this.fontRendererObj.FONT_HEIGHT;
    }
    
    public int func_154326_c(final String p_154326_1_) {
        return this.fontRendererObj.getStringWidth(p_154326_1_);
    }
    
    public void func_154319_c(final String p_154319_1_, final int p_154319_2_, final int p_154319_3_, final int p_154319_4_) {
        this.fontRendererObj.drawStringWithShadow(p_154319_1_, (float)p_154319_2_, (float)p_154319_3_, p_154319_4_);
    }
    
    public List<String> func_154323_a(final String p_154323_1_, final int p_154323_2_) {
        return (List<String>)this.fontRendererObj.listFormattedStringToWidth(p_154323_1_, p_154323_2_);
    }
    
    public final void actionPerformed(final GuiButton button) throws IOException {
        this.field_154330_a.buttonClicked(((GuiButtonRealmsProxy)button).getRealmsButton());
    }
    
    public void func_154324_i() {
        super.buttonList.clear();
    }
    
    public void func_154327_a(final RealmsButton p_154327_1_) {
        super.buttonList.add(p_154327_1_.getProxy());
    }
    
    public List<RealmsButton> func_154320_j() {
        final List<RealmsButton> list = (List<RealmsButton>)Lists.newArrayListWithExpectedSize(super.buttonList.size());
        for (final GuiButton guibutton : super.buttonList) {
            list.add(((GuiButtonRealmsProxy)guibutton).getRealmsButton());
        }
        return list;
    }
    
    public void func_154328_b(final RealmsButton p_154328_1_) {
        super.buttonList.remove(p_154328_1_);
    }
    
    public void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.field_154330_a.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        this.field_154330_a.mouseEvent();
        super.handleMouseInput();
    }
    
    @Override
    public void handleKeyboardInput() throws IOException {
        this.field_154330_a.keyboardEvent();
        super.handleKeyboardInput();
    }
    
    public void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.field_154330_a.mouseReleased(mouseX, mouseY, state);
    }
    
    public void mouseClickMove(final int mouseX, final int mouseY, final int clickedMouseButton, final long timeSinceLastClick) {
        this.field_154330_a.mouseDragged(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }
    
    public void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.field_154330_a.keyPressed(typedChar, keyCode);
    }
    
    @Override
    public void confirmClicked(final boolean result, final int id) {
        this.field_154330_a.confirmResult(result, id);
    }
    
    @Override
    public void onGuiClosed() {
        this.field_154330_a.removed();
        super.onGuiClosed();
    }
}
