// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiBrewingStand extends GuiContainer
{
    private static final ResourceLocation brewingStandGuiTextures;
    private final InventoryPlayer playerInventory;
    private IInventory tileBrewingStand;
    
    static {
        brewingStandGuiTextures = new ResourceLocation("textures/gui/container/brewing_stand.png");
    }
    
    public GuiBrewingStand(final InventoryPlayer playerInv, final IInventory p_i45506_2_) {
        super(new ContainerBrewingStand(playerInv, p_i45506_2_));
        this.playerInventory = playerInv;
        this.tileBrewingStand = p_i45506_2_;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String s = this.tileBrewingStand.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6.0, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiBrewingStand.brewingStandGuiTextures);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        final int k = this.tileBrewingStand.getField(0);
        if (k > 0) {
            int l = (int)(28.0f * (1.0f - k / 400.0f));
            if (l > 0) {
                this.drawTexturedModalRect(i + 97, j + 16, 176, 0, 9, l);
            }
            final int i2 = k / 2 % 7;
            switch (i2) {
                case 0: {
                    l = 29;
                    break;
                }
                case 1: {
                    l = 24;
                    break;
                }
                case 2: {
                    l = 20;
                    break;
                }
                case 3: {
                    l = 16;
                    break;
                }
                case 4: {
                    l = 11;
                    break;
                }
                case 5: {
                    l = 6;
                    break;
                }
                case 6: {
                    l = 0;
                    break;
                }
            }
            if (l > 0) {
                this.drawTexturedModalRect(i + 65, j + 14 + 29 - l, 185, 29 - l, 12, l);
            }
        }
    }
}
