// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiFurnace extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures;
    private final InventoryPlayer playerInventory;
    private IInventory tileFurnace;
    
    static {
        furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
    }
    
    public GuiFurnace(final InventoryPlayer playerInv, final IInventory furnaceInv) {
        super(new ContainerFurnace(playerInv, furnaceInv));
        this.playerInventory = playerInv;
        this.tileFurnace = furnaceInv;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String s = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6.0, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiFurnace.furnaceGuiTextures);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        if (TileEntityFurnace.isBurning(this.tileFurnace)) {
            final int k = this.getBurnLeftScaled(13);
            this.drawTexturedModalRect(i + 56, j + 36 + 12 - k, 176, 12 - k, 14, k + 1);
        }
        final int l = this.getCookProgressScaled(24);
        this.drawTexturedModalRect(i + 79, j + 34, 176, 14, l + 1, 16);
    }
    
    private int getCookProgressScaled(final int pixels) {
        final int i = this.tileFurnace.getField(2);
        final int j = this.tileFurnace.getField(3);
        return (j != 0 && i != 0) ? (i * pixels / j) : 0;
    }
    
    private int getBurnLeftScaled(final int pixels) {
        int i = this.tileFurnace.getField(1);
        if (i == 0) {
            i = 200;
        }
        return this.tileFurnace.getField(0) * pixels / i;
    }
}
