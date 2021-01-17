// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import net.minecraft.village.MerchantRecipeList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerMerchant;
import net.minecraft.world.World;
import net.minecraft.entity.player.InventoryPlayer;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.IChatComponent;
import net.minecraft.entity.IMerchant;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiMerchant extends GuiContainer
{
    private static final Logger logger;
    private static final ResourceLocation MERCHANT_GUI_TEXTURE;
    private IMerchant merchant;
    private MerchantButton nextButton;
    private MerchantButton previousButton;
    private int selectedMerchantRecipe;
    private IChatComponent chatComponent;
    
    static {
        logger = LogManager.getLogger();
        MERCHANT_GUI_TEXTURE = new ResourceLocation("textures/gui/container/villager.png");
    }
    
    public GuiMerchant(final InventoryPlayer p_i45500_1_, final IMerchant p_i45500_2_, final World worldIn) {
        super(new ContainerMerchant(p_i45500_1_, p_i45500_2_, worldIn));
        this.merchant = p_i45500_2_;
        this.chatComponent = p_i45500_2_.getDisplayName();
    }
    
    @Override
    public void initGui() {
        super.initGui();
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.buttonList.add(this.nextButton = new MerchantButton(1, i + 120 + 27, j + 24 - 1, true));
        this.buttonList.add(this.previousButton = new MerchantButton(2, i + 36 - 19, j + 24 - 1, false));
        this.nextButton.enabled = false;
        this.previousButton.enabled = false;
    }
    
    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String s = this.chatComponent.getUnformattedText();
        this.fontRendererObj.drawString(s, this.xSize / 2 - this.fontRendererObj.getStringWidth(s) / 2, 6.0, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory", new Object[0]), 8.0, this.ySize - 96 + 2, 4210752);
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
        final MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(Minecraft.thePlayer);
        if (merchantrecipelist != null) {
            this.nextButton.enabled = (this.selectedMerchantRecipe < merchantrecipelist.size() - 1);
            this.previousButton.enabled = (this.selectedMerchantRecipe > 0);
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        boolean flag = false;
        if (button == this.nextButton) {
            ++this.selectedMerchantRecipe;
            final MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(Minecraft.thePlayer);
            if (merchantrecipelist != null && this.selectedMerchantRecipe >= merchantrecipelist.size()) {
                this.selectedMerchantRecipe = merchantrecipelist.size() - 1;
            }
            flag = true;
        }
        else if (button == this.previousButton) {
            --this.selectedMerchantRecipe;
            if (this.selectedMerchantRecipe < 0) {
                this.selectedMerchantRecipe = 0;
            }
            flag = true;
        }
        if (flag) {
            ((ContainerMerchant)this.inventorySlots).setCurrentRecipeIndex(this.selectedMerchantRecipe);
            final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
            packetbuffer.writeInt(this.selectedMerchantRecipe);
            this.mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|TrSel", packetbuffer));
        }
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
        final int i = (this.width - this.xSize) / 2;
        final int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        final MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(Minecraft.thePlayer);
        if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
            final int k = this.selectedMerchantRecipe;
            if (k < 0 || k >= merchantrecipelist.size()) {
                return;
            }
            final MerchantRecipe merchantrecipe = merchantrecipelist.get(k);
            if (merchantrecipe.isRecipeDisabled()) {
                this.mc.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                GlStateManager.disableLighting();
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 21, 212, 0, 28, 21);
                this.drawTexturedModalRect(this.guiLeft + 83, this.guiTop + 51, 212, 0, 28, 21);
            }
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        final MerchantRecipeList merchantrecipelist = this.merchant.getRecipes(Minecraft.thePlayer);
        if (merchantrecipelist != null && !merchantrecipelist.isEmpty()) {
            final int i = (this.width - this.xSize) / 2;
            final int j = (this.height - this.ySize) / 2;
            final int k = this.selectedMerchantRecipe;
            final MerchantRecipe merchantrecipe = merchantrecipelist.get(k);
            final ItemStack itemstack = merchantrecipe.getItemToBuy();
            final ItemStack itemstack2 = merchantrecipe.getSecondItemToBuy();
            final ItemStack itemstack3 = merchantrecipe.getItemToSell();
            GlStateManager.pushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.enableLighting();
            this.itemRender.zLevel = 100.0f;
            this.itemRender.renderItemAndEffectIntoGUI(itemstack, i + 36, j + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack, i + 36, j + 24);
            if (itemstack2 != null) {
                this.itemRender.renderItemAndEffectIntoGUI(itemstack2, i + 62, j + 24);
                this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack2, i + 62, j + 24);
            }
            this.itemRender.renderItemAndEffectIntoGUI(itemstack3, i + 120, j + 24);
            this.itemRender.renderItemOverlays(this.fontRendererObj, itemstack3, i + 120, j + 24);
            this.itemRender.zLevel = 0.0f;
            GlStateManager.disableLighting();
            if (this.isPointInRegion(36, 24, 16, 16, mouseX, mouseY) && itemstack != null) {
                this.renderToolTip(itemstack, mouseX, mouseY);
            }
            else if (itemstack2 != null && this.isPointInRegion(62, 24, 16, 16, mouseX, mouseY) && itemstack2 != null) {
                this.renderToolTip(itemstack2, mouseX, mouseY);
            }
            else if (itemstack3 != null && this.isPointInRegion(120, 24, 16, 16, mouseX, mouseY) && itemstack3 != null) {
                this.renderToolTip(itemstack3, mouseX, mouseY);
            }
            else if (merchantrecipe.isRecipeDisabled() && (this.isPointInRegion(83, 21, 28, 21, mouseX, mouseY) || this.isPointInRegion(83, 51, 28, 21, mouseX, mouseY))) {
                this.drawCreativeTabHoveringText(I18n.format("merchant.deprecated", new Object[0]), mouseX, mouseY);
            }
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
        }
    }
    
    public IMerchant getMerchant() {
        return this.merchant;
    }
    
    static class MerchantButton extends GuiButton
    {
        private final boolean field_146157_o;
        
        public MerchantButton(final int buttonID, final int x, final int y, final boolean p_i1095_4_) {
            super(buttonID, x, y, 12, 19, "");
            this.field_146157_o = p_i1095_4_;
        }
        
        @Override
        public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
            if (this.visible) {
                mc.getTextureManager().bindTexture(GuiMerchant.MERCHANT_GUI_TEXTURE);
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                int i = 0;
                int j = 176;
                if (!this.enabled) {
                    j += this.width * 2;
                }
                else if (flag) {
                    j += this.width;
                }
                if (!this.field_146157_o) {
                    i += this.height;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, j, i, this.width, this.height);
            }
        }
    }
}
