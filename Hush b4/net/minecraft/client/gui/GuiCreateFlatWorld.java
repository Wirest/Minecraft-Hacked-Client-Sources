// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.world.gen.FlatLayerInfo;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.gen.FlatGeneratorInfo;

public class GuiCreateFlatWorld extends GuiScreen
{
    private final GuiCreateWorld createWorldGui;
    private FlatGeneratorInfo theFlatGeneratorInfo;
    private String flatWorldTitle;
    private String field_146394_i;
    private String field_146391_r;
    private Details createFlatWorldListSlotGui;
    private GuiButton field_146389_t;
    private GuiButton field_146388_u;
    private GuiButton field_146386_v;
    
    public GuiCreateFlatWorld(final GuiCreateWorld createWorldGuiIn, final String p_i1029_2_) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
        this.createWorldGui = createWorldGuiIn;
        this.func_146383_a(p_i1029_2_);
    }
    
    public String func_146384_e() {
        return this.theFlatGeneratorInfo.toString();
    }
    
    public void func_146383_a(final String p_146383_1_) {
        this.theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(p_146383_1_);
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        this.flatWorldTitle = I18n.format("createWorld.customize.flat.title", new Object[0]);
        this.field_146394_i = I18n.format("createWorld.customize.flat.tile", new Object[0]);
        this.field_146391_r = I18n.format("createWorld.customize.flat.height", new Object[0]);
        this.createFlatWorldListSlotGui = new Details();
        this.buttonList.add(this.field_146389_t = new GuiButton(2, this.width / 2 - 154, this.height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.addLayer", new Object[0])) + " (NYI)"));
        this.buttonList.add(this.field_146388_u = new GuiButton(3, this.width / 2 - 50, this.height - 52, 100, 20, String.valueOf(I18n.format("createWorld.customize.flat.editLayer", new Object[0])) + " (NYI)"));
        this.buttonList.add(this.field_146386_v = new GuiButton(4, this.width / 2 - 155, this.height - 52, 150, 20, I18n.format("createWorld.customize.flat.removeLayer", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 + 5, this.height - 52, 150, 20, I18n.format("createWorld.customize.presets", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        final GuiButton field_146389_t = this.field_146389_t;
        final GuiButton field_146388_u = this.field_146388_u;
        final boolean b = false;
        field_146388_u.visible = b;
        field_146389_t.visible = b;
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.createFlatWorldListSlotGui.handleMouseInput();
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        final int i = this.theFlatGeneratorInfo.getFlatLayers().size() - this.createFlatWorldListSlotGui.field_148228_k - 1;
        if (button.id == 1) {
            this.mc.displayGuiScreen(this.createWorldGui);
        }
        else if (button.id == 0) {
            this.createWorldGui.chunkProviderSettingsJson = this.func_146384_e();
            this.mc.displayGuiScreen(this.createWorldGui);
        }
        else if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (button.id == 4 && this.func_146382_i()) {
            this.theFlatGeneratorInfo.getFlatLayers().remove(i);
            this.createFlatWorldListSlotGui.field_148228_k = Math.min(this.createFlatWorldListSlotGui.field_148228_k, this.theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }
        this.theFlatGeneratorInfo.func_82645_d();
        this.func_146375_g();
    }
    
    public void func_146375_g() {
        final boolean flag = this.func_146382_i();
        this.field_146386_v.enabled = flag;
        this.field_146388_u.enabled = flag;
        this.field_146388_u.enabled = false;
        this.field_146389_t.enabled = false;
    }
    
    private boolean func_146382_i() {
        return this.createFlatWorldListSlotGui.field_148228_k > -1 && this.createFlatWorldListSlotGui.field_148228_k < this.theFlatGeneratorInfo.getFlatLayers().size();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.createFlatWorldListSlotGui.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.flatWorldTitle, this.width / 2, 8, 16777215);
        final int i = this.width / 2 - 92 - 16;
        this.drawString(this.fontRendererObj, this.field_146394_i, i, 32, 16777215);
        this.drawString(this.fontRendererObj, this.field_146391_r, i + 2 + 213 - this.fontRendererObj.getStringWidth(this.field_146391_r), 32, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    class Details extends GuiSlot
    {
        public int field_148228_k;
        
        public Details() {
            super(GuiCreateFlatWorld.this.mc, GuiCreateFlatWorld.this.width, GuiCreateFlatWorld.this.height, 43, GuiCreateFlatWorld.this.height - 60, 24);
            this.field_148228_k = -1;
        }
        
        private void func_148225_a(final int p_148225_1_, final int p_148225_2_, final ItemStack p_148225_3_) {
            this.func_148226_e(p_148225_1_ + 1, p_148225_2_ + 1);
            GlStateManager.enableRescaleNormal();
            if (p_148225_3_ != null && p_148225_3_.getItem() != null) {
                RenderHelper.enableGUIStandardItemLighting();
                GuiCreateFlatWorld.this.itemRender.renderItemIntoGUI(p_148225_3_, p_148225_1_ + 2, p_148225_2_ + 2);
                RenderHelper.disableStandardItemLighting();
            }
            GlStateManager.disableRescaleNormal();
        }
        
        private void func_148226_e(final int p_148226_1_, final int p_148226_2_) {
            this.func_148224_c(p_148226_1_, p_148226_2_, 0, 0);
        }
        
        private void func_148224_c(final int p_148224_1_, final int p_148224_2_, final int p_148224_3_, final int p_148224_4_) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.statIcons);
            final float f = 0.0078125f;
            final float f2 = 0.0078125f;
            final int i = 18;
            final int j = 18;
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(p_148224_1_ + 0, p_148224_2_ + 18, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 0) * 0.0078125f, (p_148224_4_ + 18) * 0.0078125f).endVertex();
            worldrenderer.pos(p_148224_1_ + 18, p_148224_2_ + 18, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 18) * 0.0078125f, (p_148224_4_ + 18) * 0.0078125f).endVertex();
            worldrenderer.pos(p_148224_1_ + 18, p_148224_2_ + 0, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 18) * 0.0078125f, (p_148224_4_ + 0) * 0.0078125f).endVertex();
            worldrenderer.pos(p_148224_1_ + 0, p_148224_2_ + 0, GuiCreateFlatWorld.this.zLevel).tex((p_148224_3_ + 0) * 0.0078125f, (p_148224_4_ + 0) * 0.0078125f).endVertex();
            tessellator.draw();
        }
        
        @Override
        protected int getSize() {
            return GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            this.field_148228_k = slotIndex;
            GuiCreateFlatWorld.this.func_146375_g();
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == this.field_148228_k;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final FlatLayerInfo flatlayerinfo = GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().get(GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - entryID - 1);
            final IBlockState iblockstate = flatlayerinfo.func_175900_c();
            final Block block = iblockstate.getBlock();
            Item item = Item.getItemFromBlock(block);
            ItemStack itemstack = (block != Blocks.air && item != null) ? new ItemStack(item, 1, block.getMetaFromState(iblockstate)) : null;
            String s = (itemstack == null) ? "Air" : item.getItemStackDisplayName(itemstack);
            if (item == null) {
                if (block != Blocks.water && block != Blocks.flowing_water) {
                    if (block == Blocks.lava || block == Blocks.flowing_lava) {
                        item = Items.lava_bucket;
                    }
                }
                else {
                    item = Items.water_bucket;
                }
                if (item != null) {
                    itemstack = new ItemStack(item, 1, block.getMetaFromState(iblockstate));
                    s = block.getLocalizedName();
                }
            }
            this.func_148225_a(p_180791_2_, p_180791_3_, itemstack);
            GuiCreateFlatWorld.this.fontRendererObj.drawString(s, p_180791_2_ + 18 + 5, p_180791_3_ + 3, 16777215);
            String s2;
            if (entryID == 0) {
                s2 = I18n.format("createWorld.customize.flat.layer.top", flatlayerinfo.getLayerCount());
            }
            else if (entryID == GuiCreateFlatWorld.this.theFlatGeneratorInfo.getFlatLayers().size() - 1) {
                s2 = I18n.format("createWorld.customize.flat.layer.bottom", flatlayerinfo.getLayerCount());
            }
            else {
                s2 = I18n.format("createWorld.customize.flat.layer", flatlayerinfo.getLayerCount());
            }
            GuiCreateFlatWorld.this.fontRendererObj.drawString(s2, p_180791_2_ + 2 + 213 - GuiCreateFlatWorld.this.fontRendererObj.getStringWidth(s2), p_180791_3_ + 3, 16777215);
        }
        
        @Override
        protected int getScrollBarX() {
            return this.width - 70;
        }
    }
}
