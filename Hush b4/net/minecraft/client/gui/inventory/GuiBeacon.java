// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.potion.Potion;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Iterator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.renderer.RenderHelper;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBeacon;
import net.minecraft.entity.player.InventoryPlayer;
import org.apache.logging.log4j.LogManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiBeacon extends GuiContainer
{
    private static final Logger logger;
    private static final ResourceLocation beaconGuiTextures;
    private IInventory tileBeacon;
    private ConfirmButton beaconConfirmButton;
    private boolean buttonsNotDrawn;
    
    static {
        logger = LogManager.getLogger();
        beaconGuiTextures = new ResourceLocation("textures/gui/container/beacon.png");
    }
    
    public GuiBeacon(final InventoryPlayer playerInventory, final IInventory tileBeaconIn) {
        super(new ContainerBeacon(playerInventory, tileBeaconIn));
        this.tileBeacon = tileBeaconIn;
        this.xSize = 230;
        this.ySize = 219;
    }
    
    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.add(this.beaconConfirmButton = new ConfirmButton(-1, this.guiLeft + 164, this.guiTop + 107));
        this.buttonList.add(new CancelButton(-2, this.guiLeft + 190, this.guiTop + 107));
        this.buttonsNotDrawn = true;
        this.beaconConfirmButton.enabled = false;
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final int i = this.tileBeacon.getField(0);
        final int j = this.tileBeacon.getField(1);
        final int k = this.tileBeacon.getField(2);
        if (this.buttonsNotDrawn && i >= 0) {
            this.buttonsNotDrawn = false;
            for (int l = 0; l <= 2; ++l) {
                final int i2 = TileEntityBeacon.effectsList[l].length;
                final int j2 = i2 * 22 + (i2 - 1) * 2;
                for (int k2 = 0; k2 < i2; ++k2) {
                    final int l2 = TileEntityBeacon.effectsList[l][k2].id;
                    final PowerButton guibeacon$powerbutton = new PowerButton(l << 8 | l2, this.guiLeft + 76 + k2 * 24 - j2 / 2, this.guiTop + 22 + l * 25, l2, l);
                    this.buttonList.add(guibeacon$powerbutton);
                    if (l >= i) {
                        guibeacon$powerbutton.enabled = false;
                    }
                    else if (l2 == j) {
                        guibeacon$powerbutton.func_146140_b(true);
                    }
                }
            }
            final int i3 = 3;
            final int j3 = TileEntityBeacon.effectsList[i3].length + 1;
            final int k3 = j3 * 22 + (j3 - 1) * 2;
            for (int l3 = 0; l3 < j3 - 1; ++l3) {
                final int i4 = TileEntityBeacon.effectsList[i3][l3].id;
                final PowerButton guibeacon$powerbutton2 = new PowerButton(i3 << 8 | i4, this.guiLeft + 167 + l3 * 24 - k3 / 2, this.guiTop + 47, i4, i3);
                this.buttonList.add(guibeacon$powerbutton2);
                if (i3 >= i) {
                    guibeacon$powerbutton2.enabled = false;
                }
                else if (i4 == k) {
                    guibeacon$powerbutton2.func_146140_b(true);
                }
            }
            if (j > 0) {
                final PowerButton guibeacon$powerbutton3 = new PowerButton(i3 << 8 | j, this.guiLeft + 167 + (j3 - 1) * 24 - k3 / 2, this.guiTop + 47, j, i3);
                this.buttonList.add(guibeacon$powerbutton3);
                if (i3 >= i) {
                    guibeacon$powerbutton3.enabled = false;
                }
                else if (j == k) {
                    guibeacon$powerbutton3.func_146140_b(true);
                }
            }
        }
        this.beaconConfirmButton.enabled = (this.tileBeacon.getStackInSlot(0) != null && j > 0);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == -2) {
            this.mc.displayGuiScreen(null);
        }
        else if (button.id == -1) {
            final String s = "MC|Beacon";
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.tileBeacon.getField(1));
            packetbuffer.writeInt(this.tileBeacon.getField(2));
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload(s, packetbuffer));
            this.mc.displayGuiScreen(null);
        }
        else if (button instanceof PowerButton) {
            if (((PowerButton)button).func_146141_c()) {
                return;
            }
            final int j = button.id;
            final int k = j & 0xFF;
            final int i = j >> 8;
            if (i < 3) {
                this.tileBeacon.setField(1, k);
            }
            else {
                this.tileBeacon.setField(2, k);
            }
            this.buttonList.clear();
            this.initGui();
            this.updateScreen();
        }
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        RenderHelper.disableStandardItemLighting();
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.primary", new Object[0]), 62, 10, 14737632);
        this.drawCenteredString(this.fontRendererObj, I18n.format("tile.beacon.secondary", new Object[0]), 169, 10, 14737632);
        for (final GuiButton guibutton : this.buttonList) {
            if (guibutton.isMouseOver()) {
                guibutton.drawButtonForegroundLayer(mouseX - this.guiLeft, mouseY - this.guiTop);
                break;
            }
        }
        RenderHelper.enableGUIStandardItemLighting();
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.itemRender.zLevel = 100.0f;
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.emerald), i + 42, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.diamond), i + 42 + 22, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.gold_ingot), i + 42 + 44, j + 109);
        this.itemRender.renderItemAndEffectIntoGUI(new ItemStack(Items.iron_ingot), i + 42 + 66, j + 109);
        this.itemRender.zLevel = 0.0f;
    }
    
    static class Button extends GuiButton
    {
        private final ResourceLocation field_146145_o;
        private final int field_146144_p;
        private final int field_146143_q;
        private boolean field_146142_r;
        
        protected Button(final int p_i1077_1_, final int p_i1077_2_, final int p_i1077_3_, final ResourceLocation p_i1077_4_, final int p_i1077_5_, final int p_i1077_6_) {
            super(p_i1077_1_, p_i1077_2_, p_i1077_3_, 22, 22, "");
            this.field_146145_o = p_i1077_4_;
            this.field_146144_p = p_i1077_5_;
            this.field_146143_q = p_i1077_6_;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiBeacon.beaconGuiTextures);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
                final int i = 219;
                int j = 0;
                if (!this.enabled) {
                    j += this.width * 2;
                }
                else if (this.field_146142_r) {
                    j += this.width * 1;
                }
                else if (this.hovered) {
                    j += this.width * 3;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
                if (!GuiBeacon.beaconGuiTextures.equals(this.field_146145_o)) {
                    mc.getTextureManager().bindTexture(this.field_146145_o);
                }
                this.drawTexturedModalRect(this.xPosition + 2, this.yPosition + 2, this.field_146144_p, this.field_146143_q, 18, 18);
            }
        }
        
        public boolean func_146141_c() {
            return this.field_146142_r;
        }
        
        public void func_146140_b(final boolean p_146140_1_) {
            this.field_146142_r = p_146140_1_;
        }
    }
    
    class CancelButton extends Button
    {
        public CancelButton(final int p_i1074_2_, final int p_i1074_3_, final int p_i1074_4_) {
            super(p_i1074_2_, p_i1074_3_, p_i1074_4_, GuiBeacon.beaconGuiTextures, 112, 220);
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            GuiScreen.this.drawCreativeTabHoveringText(I18n.format("gui.cancel", new Object[0]), mouseX, mouseY);
        }
    }
    
    class ConfirmButton extends Button
    {
        public ConfirmButton(final int p_i1075_2_, final int p_i1075_3_, final int p_i1075_4_) {
            super(p_i1075_2_, p_i1075_3_, p_i1075_4_, GuiBeacon.beaconGuiTextures, 90, 220);
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            GuiScreen.this.drawCreativeTabHoveringText(I18n.format("gui.done", new Object[0]), mouseX, mouseY);
        }
    }
    
    class PowerButton extends Button
    {
        private final int field_146149_p;
        private final int field_146148_q;
        
        public PowerButton(final int p_i1076_2_, final int p_i1076_3_, final int p_i1076_4_, final int p_i1076_5_, final int p_i1076_6_) {
            super(p_i1076_2_, p_i1076_3_, p_i1076_4_, GuiContainer.inventoryBackground, 0 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() % 8 * 18, 198 + Potion.potionTypes[p_i1076_5_].getStatusIconIndex() / 8 * 18);
            this.field_146149_p = p_i1076_5_;
            this.field_146148_q = p_i1076_6_;
        }
        
        @Override
        public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
            String s = I18n.format(Potion.potionTypes[this.field_146149_p].getName(), new Object[0]);
            if (this.field_146148_q >= 3 && this.field_146149_p != Potion.regeneration.id) {
                s = String.valueOf(s) + " II";
            }
            GuiScreen.this.drawCreativeTabHoveringText(s, mouseX, mouseY);
        }
    }
}
