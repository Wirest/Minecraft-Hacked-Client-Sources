// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import java.util.Map;
import com.google.common.collect.Maps;
import net.minecraft.world.gen.FlatGeneratorInfo;
import java.io.IOException;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.init.Items;
import net.minecraft.world.gen.FlatLayerInfo;
import java.util.Arrays;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import com.google.common.collect.Lists;
import java.util.List;

public class GuiFlatPresets extends GuiScreen
{
    private static final List<LayerItem> FLAT_WORLD_PRESETS;
    private final GuiCreateFlatWorld parentScreen;
    private String presetsTitle;
    private String presetsShare;
    private String field_146436_r;
    private ListSlot field_146435_s;
    private GuiButton field_146434_t;
    private GuiTextField field_146433_u;
    
    static {
        FLAT_WORLD_PRESETS = Lists.newArrayList();
        func_146421_a("Classic Flat", Item.getItemFromBlock(Blocks.grass), BiomeGenBase.plains, Arrays.asList("village"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(2, Blocks.dirt), new FlatLayerInfo(1, Blocks.bedrock));
        func_146421_a("Tunnelers' Dream", Item.getItemFromBlock(Blocks.stone), BiomeGenBase.extremeHills, Arrays.asList("biome_1", "dungeon", "decoration", "stronghold", "mineshaft"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(230, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        func_146421_a("Water World", Items.water_bucket, BiomeGenBase.deepOcean, Arrays.asList("biome_1", "oceanmonument"), new FlatLayerInfo(90, Blocks.water), new FlatLayerInfo(5, Blocks.sand), new FlatLayerInfo(5, Blocks.dirt), new FlatLayerInfo(5, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        func_175354_a("Overworld", Item.getItemFromBlock(Blocks.tallgrass), BlockTallGrass.EnumType.GRASS.getMeta(), BiomeGenBase.plains, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon", "lake", "lava_lake"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        func_146421_a("Snowy Kingdom", Item.getItemFromBlock(Blocks.snow_layer), BiomeGenBase.icePlains, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.snow_layer), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(59, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        func_146421_a("Bottomless Pit", Items.feather, BiomeGenBase.plains, Arrays.asList("village", "biome_1"), new FlatLayerInfo(1, Blocks.grass), new FlatLayerInfo(3, Blocks.dirt), new FlatLayerInfo(2, Blocks.cobblestone));
        func_146421_a("Desert", Item.getItemFromBlock(Blocks.sand), BiomeGenBase.desert, Arrays.asList("village", "biome_1", "decoration", "stronghold", "mineshaft", "dungeon"), new FlatLayerInfo(8, Blocks.sand), new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
        func_146425_a("Redstone Ready", Items.redstone, BiomeGenBase.desert, new FlatLayerInfo(52, Blocks.sandstone), new FlatLayerInfo(3, Blocks.stone), new FlatLayerInfo(1, Blocks.bedrock));
    }
    
    public GuiFlatPresets(final GuiCreateFlatWorld p_i46318_1_) {
        this.parentScreen = p_i46318_1_;
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.presetsTitle = I18n.format("createWorld.customize.presets.title", new Object[0]);
        this.presetsShare = I18n.format("createWorld.customize.presets.share", new Object[0]);
        this.field_146436_r = I18n.format("createWorld.customize.presets.list", new Object[0]);
        this.field_146433_u = new GuiTextField(2, this.fontRendererObj, 50, 40, this.width - 100, 20);
        this.field_146435_s = new ListSlot();
        this.field_146433_u.setMaxStringLength(1230);
        this.field_146433_u.setText(this.parentScreen.func_146384_e());
        this.buttonList.add(this.field_146434_t = new GuiButton(0, this.width / 2 - 155, this.height - 28, 150, 20, I18n.format("createWorld.customize.presets.select", new Object[0])));
        this.buttonList.add(new GuiButton(1, this.width / 2 + 5, this.height - 28, 150, 20, I18n.format("gui.cancel", new Object[0])));
        this.func_146426_g();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.field_146435_s.handleMouseInput();
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        this.field_146433_u.mouseClicked(mouseX, mouseY, mouseButton);
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (!this.field_146433_u.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0 && this.func_146430_p()) {
            this.parentScreen.func_146383_a(this.field_146433_u.getText());
            this.mc.displayGuiScreen(this.parentScreen);
        }
        else if (button.id == 1) {
            this.mc.displayGuiScreen(this.parentScreen);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.field_146435_s.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, this.presetsTitle, this.width / 2, 8, 16777215);
        this.drawString(this.fontRendererObj, this.presetsShare, 50, 30, 10526880);
        this.drawString(this.fontRendererObj, this.field_146436_r, 50, 70, 10526880);
        this.field_146433_u.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    @Override
    public void updateScreen() {
        this.field_146433_u.updateCursorCounter();
        super.updateScreen();
    }
    
    public void func_146426_g() {
        final boolean flag = this.func_146430_p();
        this.field_146434_t.enabled = flag;
    }
    
    private boolean func_146430_p() {
        return (this.field_146435_s.field_148175_k > -1 && this.field_146435_s.field_148175_k < GuiFlatPresets.FLAT_WORLD_PRESETS.size()) || this.field_146433_u.getText().length() > 1;
    }
    
    private static void func_146425_a(final String p_146425_0_, final Item p_146425_1_, final BiomeGenBase p_146425_2_, final FlatLayerInfo... p_146425_3_) {
        func_175354_a(p_146425_0_, p_146425_1_, 0, p_146425_2_, null, p_146425_3_);
    }
    
    private static void func_146421_a(final String p_146421_0_, final Item p_146421_1_, final BiomeGenBase p_146421_2_, final List<String> p_146421_3_, final FlatLayerInfo... p_146421_4_) {
        func_175354_a(p_146421_0_, p_146421_1_, 0, p_146421_2_, p_146421_3_, p_146421_4_);
    }
    
    private static void func_175354_a(final String p_175354_0_, final Item p_175354_1_, final int p_175354_2_, final BiomeGenBase p_175354_3_, final List<String> p_175354_4_, final FlatLayerInfo... p_175354_5_) {
        final FlatGeneratorInfo flatgeneratorinfo = new FlatGeneratorInfo();
        for (int i = p_175354_5_.length - 1; i >= 0; --i) {
            flatgeneratorinfo.getFlatLayers().add(p_175354_5_[i]);
        }
        flatgeneratorinfo.setBiome(p_175354_3_.biomeID);
        flatgeneratorinfo.func_82645_d();
        if (p_175354_4_ != null) {
            for (final String s : p_175354_4_) {
                flatgeneratorinfo.getWorldFeatures().put(s, (Map<String, String>)Maps.newHashMap());
            }
        }
        GuiFlatPresets.FLAT_WORLD_PRESETS.add(new LayerItem(p_175354_1_, p_175354_2_, p_175354_0_, flatgeneratorinfo.toString()));
    }
    
    static class LayerItem
    {
        public Item field_148234_a;
        public int field_179037_b;
        public String field_148232_b;
        public String field_148233_c;
        
        public LayerItem(final Item p_i45518_1_, final int p_i45518_2_, final String p_i45518_3_, final String p_i45518_4_) {
            this.field_148234_a = p_i45518_1_;
            this.field_179037_b = p_i45518_2_;
            this.field_148232_b = p_i45518_3_;
            this.field_148233_c = p_i45518_4_;
        }
    }
    
    class ListSlot extends GuiSlot
    {
        public int field_148175_k;
        
        public ListSlot() {
            super(GuiFlatPresets.this.mc, GuiFlatPresets.this.width, GuiFlatPresets.this.height, 80, GuiFlatPresets.this.height - 37, 24);
            this.field_148175_k = -1;
        }
        
        private void func_178054_a(final int p_178054_1_, final int p_178054_2_, final Item p_178054_3_, final int p_178054_4_) {
            this.func_148173_e(p_178054_1_ + 1, p_178054_2_ + 1);
            GlStateManager.enableRescaleNormal();
            RenderHelper.enableGUIStandardItemLighting();
            GuiFlatPresets.this.itemRender.renderItemIntoGUI(new ItemStack(p_178054_3_, 1, p_178054_4_), p_178054_1_ + 2, p_178054_2_ + 2);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
        }
        
        private void func_148173_e(final int p_148173_1_, final int p_148173_2_) {
            this.func_148171_c(p_148173_1_, p_148173_2_, 0, 0);
        }
        
        private void func_148171_c(final int p_148171_1_, final int p_148171_2_, final int p_148171_3_, final int p_148171_4_) {
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.mc.getTextureManager().bindTexture(Gui.statIcons);
            final float f = 0.0078125f;
            final float f2 = 0.0078125f;
            final int i = 18;
            final int j = 18;
            final Tessellator tessellator = Tessellator.getInstance();
            final WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
            worldrenderer.pos(p_148171_1_ + 0, p_148171_2_ + 18, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 0) * 0.0078125f, (p_148171_4_ + 18) * 0.0078125f).endVertex();
            worldrenderer.pos(p_148171_1_ + 18, p_148171_2_ + 18, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 18) * 0.0078125f, (p_148171_4_ + 18) * 0.0078125f).endVertex();
            worldrenderer.pos(p_148171_1_ + 18, p_148171_2_ + 0, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 18) * 0.0078125f, (p_148171_4_ + 0) * 0.0078125f).endVertex();
            worldrenderer.pos(p_148171_1_ + 0, p_148171_2_ + 0, GuiFlatPresets.this.zLevel).tex((p_148171_3_ + 0) * 0.0078125f, (p_148171_4_ + 0) * 0.0078125f).endVertex();
            tessellator.draw();
        }
        
        @Override
        protected int getSize() {
            return GuiFlatPresets.FLAT_WORLD_PRESETS.size();
        }
        
        @Override
        protected void elementClicked(final int slotIndex, final boolean isDoubleClick, final int mouseX, final int mouseY) {
            this.field_148175_k = slotIndex;
            GuiFlatPresets.this.func_146426_g();
            GuiFlatPresets.this.field_146433_u.setText(GuiFlatPresets.FLAT_WORLD_PRESETS.get(GuiFlatPresets.this.field_146435_s.field_148175_k).field_148233_c);
        }
        
        @Override
        protected boolean isSelected(final int slotIndex) {
            return slotIndex == this.field_148175_k;
        }
        
        @Override
        protected void drawBackground() {
        }
        
        @Override
        protected void drawSlot(final int entryID, final int p_180791_2_, final int p_180791_3_, final int p_180791_4_, final int mouseXIn, final int mouseYIn) {
            final LayerItem guiflatpresets$layeritem = GuiFlatPresets.FLAT_WORLD_PRESETS.get(entryID);
            this.func_178054_a(p_180791_2_, p_180791_3_, guiflatpresets$layeritem.field_148234_a, guiflatpresets$layeritem.field_179037_b);
            GuiFlatPresets.this.fontRendererObj.drawString(guiflatpresets$layeritem.field_148232_b, p_180791_2_ + 18 + 5, p_180791_3_ + 6, 16777215);
        }
    }
}
