package shadersmod.client;

import net.minecraft.client.gui.GuiSlot;
import optifine.Lang;

import java.util.ArrayList;

class GuiSlotShaders
        extends GuiSlot {
    final GuiShaders shadersGui;
    private ArrayList shaderslist;
    private int selectedIndex;
    private long lastClickedCached = 0L;

    public GuiSlotShaders(GuiShaders paramGuiShaders, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5) {
        super(paramGuiShaders.getMc(), paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
        this.shadersGui = paramGuiShaders;
        updateList();
        this.amountScrolled = 0.0F;
        int i = this.selectedIndex * paramInt5;
        int j = -2;
        if (i > j) {
            scrollBy(i - j);
        }
    }

    public int getListWidth() {
        return this.width - 20;
    }

    public void updateList() {
        this.shaderslist = Shaders.listOfShaders();
        this.selectedIndex = 0;
        int i = 0;
        int j = this.shaderslist.size();
        while (i < j) {
            if (((String) this.shaderslist.get(i)).equals(Shaders.currentshadername)) {
                this.selectedIndex = i;
                break;
            }
            i++;
        }
    }

    protected int getSize() {
        return this.shaderslist.size();
    }

    protected void elementClicked(int paramInt1, boolean paramBoolean, int paramInt2, int paramInt3) {
        if ((paramInt1 != this.selectedIndex) || (this.lastClicked != this.lastClickedCached)) {
            this.selectedIndex = paramInt1;
            this.lastClickedCached = this.lastClicked;
            Shaders.setShaderPack((String) this.shaderslist.get(paramInt1));
            Shaders.uninit();
            this.shadersGui.updateButtons();
        }
    }

    protected boolean isSelected(int paramInt) {
        return paramInt == this.selectedIndex;
    }

    protected int getScrollBarX() {
        return this.width - 6;
    }

    protected int getContentHeight() {
        return getSize() * 18;
    }

    protected void drawBackground() {
    }

    protected void drawSlot(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
        String str = (String) this.shaderslist.get(paramInt1);
        if (str.equals(Shaders.packNameNone)) {
            str = Lang.get("of.options.shaders.packNone");
        } else if (str.equals(Shaders.packNameDefault)) {
            str = Lang.get("of.options.shaders.packDefault");
        }
        str.drawCenteredString(this.width, -2, paramInt3 | 0x1, 16777215);
    }

    public int getSelectedIndex() {
        return this.selectedIndex;
    }
}




