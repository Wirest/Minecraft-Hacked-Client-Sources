// 
// Decompiled by Procyon v0.5.36
// 

package shadersmod.client;

import optifine.Lang;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiSlot;

class GuiSlotShaders extends GuiSlot
{
    private ArrayList shaderslist;
    private int selectedIndex;
    private long lastClickedCached;
    final GuiShaders shadersGui;
    
    public GuiSlotShaders(final GuiShaders par1GuiShaders, final int width, final int height, final int top, final int bottom, final int slotHeight) {
        super(par1GuiShaders.getMc(), width, height, top, bottom, slotHeight);
        this.lastClickedCached = 0L;
        this.shadersGui = par1GuiShaders;
        this.updateList();
        this.amountScrolled = 0.0f;
        final int i = this.selectedIndex * slotHeight;
        final int j = (bottom - top) / 2;
        if (i > j) {
            this.scrollBy(i - j);
        }
    }
    
    @Override
    public int getListWidth() {
        return this.width - 20;
    }
    
    public void updateList() {
        this.shaderslist = Shaders.listOfShaders();
        this.selectedIndex = 0;
        for (int i = 0, j = this.shaderslist.size(); i < j; ++i) {
            if (this.shaderslist.get(i).equals(Shaders.currentshadername)) {
                this.selectedIndex = i;
                break;
            }
        }
    }
    
    @Override
    protected int getSize() {
        return this.shaderslist.size();
    }
    
    @Override
    protected void elementClicked(final int index, final boolean doubleClicked, final int mouseX, final int mouseY) {
        if (index != this.selectedIndex || this.lastClicked != this.lastClickedCached) {
            this.selectedIndex = index;
            this.lastClickedCached = this.lastClicked;
            Shaders.setShaderPack(this.shaderslist.get(index));
            Shaders.uninit();
            this.shadersGui.updateButtons();
        }
    }
    
    @Override
    protected boolean isSelected(final int index) {
        return index == this.selectedIndex;
    }
    
    @Override
    protected int getScrollBarX() {
        return this.width - 6;
    }
    
    @Override
    protected int getContentHeight() {
        return this.getSize() * 18;
    }
    
    @Override
    protected void drawBackground() {
    }
    
    @Override
    protected void drawSlot(final int index, final int posX, final int posY, final int contentY, final int mouseX, final int mouseY) {
        String s = this.shaderslist.get(index);
        if (s.equals(Shaders.packNameNone)) {
            s = Lang.get("of.options.shaders.packNone");
        }
        else if (s.equals(Shaders.packNameDefault)) {
            s = Lang.get("of.options.shaders.packDefault");
        }
        this.shadersGui.drawCenteredString(s, this.width / 2, posY + 1, 16777215);
    }
    
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
}
