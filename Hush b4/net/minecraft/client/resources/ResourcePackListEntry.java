// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNoCallback;
import java.util.List;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiListExtended;

public abstract class ResourcePackListEntry implements GuiListExtended.IGuiListEntry
{
    private static final ResourceLocation RESOURCE_PACKS_TEXTURE;
    private static final IChatComponent field_183020_d;
    private static final IChatComponent field_183021_e;
    private static final IChatComponent field_183022_f;
    protected final Minecraft mc;
    protected final GuiScreenResourcePacks resourcePacksGUI;
    
    static {
        RESOURCE_PACKS_TEXTURE = new ResourceLocation("textures/gui/resource_packs.png");
        field_183020_d = new ChatComponentTranslation("resourcePack.incompatible", new Object[0]);
        field_183021_e = new ChatComponentTranslation("resourcePack.incompatible.old", new Object[0]);
        field_183022_f = new ChatComponentTranslation("resourcePack.incompatible.new", new Object[0]);
    }
    
    public ResourcePackListEntry(final GuiScreenResourcePacks resourcePacksGUIIn) {
        this.resourcePacksGUI = resourcePacksGUIIn;
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void drawEntry(final int slotIndex, final int x, final int y, final int listWidth, final int slotHeight, final int mouseX, final int mouseY, final boolean isSelected) {
        final int i = this.func_183019_a();
        if (i != 1) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawRect(x - 1, y - 1, x + listWidth - 9, y + slotHeight + 1, -8978432);
        }
        this.func_148313_c();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 32.0f, 32.0f);
        String s = this.func_148312_b();
        String s2 = this.func_148311_a();
        if ((this.mc.gameSettings.touchscreen || isSelected) && this.func_148310_d()) {
            this.mc.getTextureManager().bindTexture(ResourcePackListEntry.RESOURCE_PACKS_TEXTURE);
            Gui.drawRect(x, y, x + 32, y + 32, -1601138544);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            final int j = mouseX - x;
            final int k = mouseY - y;
            if (i < 1) {
                s = ResourcePackListEntry.field_183020_d.getFormattedText();
                s2 = ResourcePackListEntry.field_183021_e.getFormattedText();
            }
            else if (i > 1) {
                s = ResourcePackListEntry.field_183020_d.getFormattedText();
                s2 = ResourcePackListEntry.field_183022_f.getFormattedText();
            }
            if (this.func_148309_e()) {
                if (j < 32) {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                }
                else {
                    Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                }
            }
            else {
                if (this.func_148308_f()) {
                    if (j < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 32.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
                if (this.func_148314_g()) {
                    if (j < 32 && j > 16 && k < 16) {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 96.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
                if (this.func_148307_h()) {
                    if (j < 32 && j > 16 && k > 16) {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 32.0f, 32, 32, 256.0f, 256.0f);
                    }
                    else {
                        Gui.drawModalRectWithCustomSizedTexture(x, y, 64.0f, 0.0f, 32, 32, 256.0f, 256.0f);
                    }
                }
            }
        }
        final int i2 = this.mc.fontRendererObj.getStringWidth(s);
        if (i2 > 157) {
            s = String.valueOf(this.mc.fontRendererObj.trimStringToWidth(s, 157 - this.mc.fontRendererObj.getStringWidth("..."))) + "...";
        }
        this.mc.fontRendererObj.drawStringWithShadow(s, (float)(x + 32 + 2), (float)(y + 1), 16777215);
        final List<String> list = (List<String>)this.mc.fontRendererObj.listFormattedStringToWidth(s2, 157);
        for (int l = 0; l < 2 && l < list.size(); ++l) {
            this.mc.fontRendererObj.drawStringWithShadow(list.get(l), (float)(x + 32 + 2), (float)(y + 12 + 10 * l), 8421504);
        }
    }
    
    protected abstract int func_183019_a();
    
    protected abstract String func_148311_a();
    
    protected abstract String func_148312_b();
    
    protected abstract void func_148313_c();
    
    protected boolean func_148310_d() {
        return true;
    }
    
    protected boolean func_148309_e() {
        return !this.resourcePacksGUI.hasResourcePackEntry(this);
    }
    
    protected boolean func_148308_f() {
        return this.resourcePacksGUI.hasResourcePackEntry(this);
    }
    
    protected boolean func_148314_g() {
        final List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        final int i = list.indexOf(this);
        return i > 0 && list.get(i - 1).func_148310_d();
    }
    
    protected boolean func_148307_h() {
        final List<ResourcePackListEntry> list = this.resourcePacksGUI.getListContaining(this);
        final int i = list.indexOf(this);
        return i >= 0 && i < list.size() - 1 && list.get(i + 1).func_148310_d();
    }
    
    @Override
    public boolean mousePressed(final int slotIndex, final int p_148278_2_, final int p_148278_3_, final int p_148278_4_, final int p_148278_5_, final int p_148278_6_) {
        if (this.func_148310_d() && p_148278_5_ <= 32) {
            if (this.func_148309_e()) {
                this.resourcePacksGUI.markChanged();
                final int j = this.func_183019_a();
                if (j != 1) {
                    final String s1 = I18n.format("resourcePack.incompatible.confirm.title", new Object[0]);
                    final String s2 = I18n.format("resourcePack.incompatible.confirm." + ((j > 1) ? "new" : "old"), new Object[0]);
                    this.mc.displayGuiScreen(new GuiYesNo(new GuiYesNoCallback() {
                        @Override
                        public void confirmClicked(final boolean result, final int id) {
                            final List<ResourcePackListEntry> list2 = ResourcePackListEntry.this.resourcePacksGUI.getListContaining(ResourcePackListEntry.this);
                            ResourcePackListEntry.this.mc.displayGuiScreen(ResourcePackListEntry.this.resourcePacksGUI);
                            if (result) {
                                list2.remove(ResourcePackListEntry.this);
                                ResourcePackListEntry.this.resourcePacksGUI.getSelectedResourcePacks().add(0, ResourcePackListEntry.this);
                            }
                        }
                    }, s1, s2, 0));
                }
                else {
                    this.resourcePacksGUI.getListContaining(this).remove(this);
                    this.resourcePacksGUI.getSelectedResourcePacks().add(0, this);
                }
                return true;
            }
            if (p_148278_5_ < 16 && this.func_148308_f()) {
                this.resourcePacksGUI.getListContaining(this).remove(this);
                this.resourcePacksGUI.getAvailableResourcePacks().add(0, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
            if (p_148278_5_ > 16 && p_148278_6_ < 16 && this.func_148314_g()) {
                final List<ResourcePackListEntry> list1 = this.resourcePacksGUI.getListContaining(this);
                final int k = list1.indexOf(this);
                list1.remove(this);
                list1.add(k - 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
            if (p_148278_5_ > 16 && p_148278_6_ > 16 && this.func_148307_h()) {
                final List<ResourcePackListEntry> list2 = this.resourcePacksGUI.getListContaining(this);
                final int i = list2.indexOf(this);
                list2.remove(this);
                list2.add(i + 1, this);
                this.resourcePacksGUI.markChanged();
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void setSelected(final int p_178011_1_, final int p_178011_2_, final int p_178011_3_) {
    }
    
    @Override
    public void mouseReleased(final int slotIndex, final int x, final int y, final int mouseEvent, final int relativeX, final int relativeY) {
    }
}
