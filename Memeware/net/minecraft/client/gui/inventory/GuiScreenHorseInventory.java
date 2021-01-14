package net.minecraft.client.gui.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer {
    private static final ResourceLocation horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
    private IInventory field_147030_v;
    private IInventory field_147029_w;
    private EntityHorse field_147034_x;
    private float field_147033_y;
    private float field_147032_z;
    private static final String __OBFID = "CL_00000760";

    public GuiScreenHorseInventory(IInventory p_i1093_1_, IInventory p_i1093_2_, EntityHorse p_i1093_3_) {
        super(new ContainerHorseInventory(p_i1093_1_, p_i1093_2_, p_i1093_3_, Minecraft.getMinecraft().thePlayer));
        this.field_147030_v = p_i1093_1_;
        this.field_147029_w = p_i1093_2_;
        this.field_147034_x = p_i1093_3_;
        this.allowUserInput = false;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items). Args : mouseX, mouseY
     */
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.field_147029_w.getDisplayName().getUnformattedText(), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.field_147030_v.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }

    /**
     * Args : renderPartialTicks, mouseX, mouseY
     */
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(horseGuiTextures);
        int var4 = (this.width - this.xSize) / 2;
        int var5 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(var4, var5, 0, 0, this.xSize, this.ySize);

        if (this.field_147034_x.isChested()) {
            this.drawTexturedModalRect(var4 + 79, var5 + 17, 0, this.ySize, 90, 54);
        }

        if (this.field_147034_x.canWearArmor()) {
            this.drawTexturedModalRect(var4 + 7, var5 + 35, 0, this.ySize + 54, 18, 18);
        }

        GuiInventory.drawEntityOnScreen(var4 + 51, var5 + 60, 17, (float) (var4 + 51) - this.field_147033_y, (float) (var5 + 75 - 50) - this.field_147032_z, this.field_147034_x);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.field_147033_y = (float) mouseX;
        this.field_147032_z = (float) mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
