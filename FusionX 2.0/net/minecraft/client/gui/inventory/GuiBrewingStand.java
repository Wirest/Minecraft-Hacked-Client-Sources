package net.minecraft.client.gui.inventory;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ContainerBrewingStand;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiBrewingStand extends GuiContainer
{
    private static final ResourceLocation brewingStandGuiTextures = new ResourceLocation("textures/gui/container/brewing_stand.png");
    private final InventoryPlayer field_175384_v;
    private IInventory tileBrewingStand;
    private static final String __OBFID = "CL_00000746";

    public GuiBrewingStand(InventoryPlayer p_i45506_1_, IInventory p_i45506_2_)
    {
        super(new ContainerBrewingStand(p_i45506_1_, p_i45506_2_));
        this.field_175384_v = p_i45506_1_;
        this.tileBrewingStand = p_i45506_2_;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
    {
        String var3 = this.tileBrewingStand.getDisplayName().getUnformattedText();
        this.fontRendererObj.drawString(var3, this.xSize / 2 - this.fontRendererObj.getStringWidth(var3) / 2, 6, 4210752);
        this.fontRendererObj.drawString(this.field_175384_v.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(brewingStandGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);
        int var6 = this.tileBrewingStand.getField(0);

        if (var6 > 0)
        {
            int var7 = (int)(28.0F * (1.0F - (float)var6 / 400.0F));

            if (var7 > 0)
            {
                this.drawTexturedModalRect(var4 + 97, var5 + 16, 176, 0, 9, var7);
            }

            int var8 = var6 / 2 % 7;

            switch (var8)
            {
                case 0:
                    var7 = 29;
                    break;

                case 1:
                    var7 = 24;
                    break;

                case 2:
                    var7 = 20;
                    break;

                case 3:
                    var7 = 16;
                    break;

                case 4:
                    var7 = 11;
                    break;

                case 5:
                    var7 = 6;
                    break;

                case 6:
                    var7 = 0;
            }

            if (var7 > 0)
            {
                this.drawTexturedModalRect(var4 + 65, var5 + 14 + 29 - var7, 185, 29 - var7, 12, var7);
            }
        }
    }
}
