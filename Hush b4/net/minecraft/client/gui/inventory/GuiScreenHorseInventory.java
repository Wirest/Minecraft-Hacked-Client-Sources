// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui.inventory;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.Container;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerHorseInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;

public class GuiScreenHorseInventory extends GuiContainer
{
    private static final ResourceLocation horseGuiTextures;
    private IInventory playerInventory;
    private IInventory horseInventory;
    private EntityHorse horseEntity;
    private float mousePosx;
    private float mousePosY;
    
    static {
        horseGuiTextures = new ResourceLocation("textures/gui/container/horse.png");
    }
    
    public GuiScreenHorseInventory(final IInventory playerInv, final IInventory horseInv, final EntityHorse horse) {
        Minecraft.getMinecraft();
        super(new ContainerHorseInventory(playerInv, horseInv, horse, Minecraft.thePlayer));
        this.playerInventory = playerInv;
        this.horseInventory = horseInv;
        this.horseEntity = horse;
        this.allowUserInput = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        this.fontRendererObj.drawString(this.horseInventory.getDisplayName().getUnformattedText(), 8.0, 6.0, 4210752);
        this.fontRendererObj.drawString(this.playerInventory.getDisplayName().getUnformattedText(), 8.0, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiScreenHorseInventory.horseGuiTextures);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        if (this.horseEntity.isChested()) {
            this.drawTexturedModalRect(i + 79, j + 17, 0, this.ySize, 90, 54);
        }
        if (this.horseEntity.canWearArmor()) {
            this.drawTexturedModalRect(i + 7, j + 35, 0, this.ySize + 54, 18, 18);
        }
        GuiInventory.drawEntityOnScreen(i + 51, j + 60, 17, i + 51 - this.mousePosx, j + 75 - 50 - this.mousePosY, this.horseEntity);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.mousePosx = (float)mouseX;
        this.mousePosY = (float)mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
