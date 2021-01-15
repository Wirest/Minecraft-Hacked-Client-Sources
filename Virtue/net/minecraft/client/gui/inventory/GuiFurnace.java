package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerFurnace;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ResourceLocation;

public class GuiFurnace extends GuiContainer
{
    private static final ResourceLocation furnaceGuiTextures = new ResourceLocation("textures/gui/container/furnace.png");
    private final InventoryPlayer field_175383_v;
    private IInventory tileFurnace;
    private static final String __OBFID = "CL_00000758";

    public GuiFurnace(InventoryPlayer p_i45501_1_, IInventory p_i45501_2_)
    {
        super(new ContainerFurnace(p_i45501_1_, p_i45501_2_));
        this.field_175383_v = p_i45501_1_;
        this.tileFurnace = p_i45501_2_;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String var3 = this.tileFurnace.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.field_175383_v.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        int var6;

        if (TileEntityFurnace.func_174903_a(this.tileFurnace))
        {
            var6 = this.func_175382_i(13);
            this.drawTexturedModalRect(var4 + 56, var5 + 36 + 12 - var6, 176, 12 - var6, 14, var6 + 1);
        }

        var6 = this.func_175381_h(24);
        this.drawTexturedModalRect(var4 + 79, var5 + 34, 176, 14, var6 + 1, 16);
    }

    private int func_175381_h(int p_175381_1_)
    {
        int var2 = this.tileFurnace.getField(2);
        int var3 = this.tileFurnace.getField(3);
        return var3 != 0 && var2 != 0 ? var2 * p_175381_1_ / var3 : 0;
    }

    private int func_175382_i(int p_175382_1_)
    {
        int var2 = this.tileFurnace.getField(1);

        if (var2 == 0)
        {
            var2 = 200;
        }

        return this.tileFurnace.getField(0) * p_175382_1_ / var2;
    }
}
