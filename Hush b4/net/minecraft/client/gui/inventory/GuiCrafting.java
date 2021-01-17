// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCrafting extends GuiContainer
{
    private static final ResourceLocation craftingTableGuiTextures;
    
    static {
        craftingTableGuiTextures = new ResourceLocation("textures/gui/container/crafting_table.png");
    }
    
    public GuiCrafting(final InventoryPlayer playerInv, final World worldIn) {
        this(playerInv, worldIn, BlockPos.ORIGIN);
    }
    
    public GuiCrafting(final InventoryPlayer playerInv, final World worldIn, final BlockPos blockPosition) {
        super(new ContainerWorkbench(playerInv, worldIn, blockPosition));
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(I18n.format("container.crafting", new Object[0]), 28.0, 6.0, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiCrafting.craftingTableGuiTextures);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
