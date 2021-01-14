package net.minecraft.client.gui.inventory;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

public class GuiEditSign extends GuiScreen {
    /**
     * Reference to the sign object.
     */
    private TileEntitySign tileSign;

    /**
     * Counts the number of screen updates.
     */
    private int updateCounter;

    /**
     * The index of the line that is being edited.
     */
    private int editLine;

    /**
     * "Done" button for the GUI.
     */
    private GuiButton doneBtn;
    private static final String __OBFID = "CL_00000764";

    public GuiEditSign(TileEntitySign p_i1097_1_) {
        this.tileSign = p_i1097_1_;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();
        Keyboard.enableRepeatEvents(true);
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0])));
        this.tileSign.setEditable(false);
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        NetHandlerPlayClient var1 = this.mc.getNetHandler();

        if (var1 != null) {
            var1.addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
        }

        this.tileSign.setEditable(true);
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        ++this.updateCounter;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.enabled) {
            if (button.id == 0) {
                this.tileSign.markDirty();
                this.mc.displayGuiScreen((GuiScreen) null);
            }
        }
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 200) {
            this.editLine = this.editLine - 1 & 3;
        }

        if (keyCode == 208 || keyCode == 28 || keyCode == 156) {
            this.editLine = this.editLine + 1 & 3;
        }

        String var3 = this.tileSign.signText[this.editLine].getUnformattedText();

        if (keyCode == 14 && var3.length() > 0) {
            var3 = var3.substring(0, var3.length() - 1);
        }

        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRendererObj.getStringWidth(var3 + typedChar) <= 90) {
            var3 = var3 + typedChar;
        }

        this.tileSign.signText[this.editLine] = new ChatComponentText(var3);

        if (keyCode == 1) {
            this.actionPerformed(this.doneBtn);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40, 16777215);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (this.width / 2), 0.0F, 50.0F);
        float var4 = 93.75F;
        GlStateManager.scale(-var4, -var4, -var4);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        Block var5 = this.tileSign.getBlockType();

        if (var5 == Blocks.standing_sign) {
            float var6 = (float) (this.tileSign.getBlockMetadata() * 360) / 16.0F;
            GlStateManager.rotate(var6, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        } else {
            int var8 = this.tileSign.getBlockMetadata();
            float var7 = 0.0F;

            if (var8 == 2) {
                var7 = 180.0F;
            }

            if (var8 == 4) {
                var7 = 90.0F;
            }

            if (var8 == 5) {
                var7 = -90.0F;
            }

            GlStateManager.rotate(var7, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.0F, -1.0625F, 0.0F);
        }

        if (this.updateCounter / 6 % 2 == 0) {
            this.tileSign.lineBeingEdited = this.editLine;
        }

        TileEntityRendererDispatcher.instance.renderTileEntityAt(this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
        this.tileSign.lineBeingEdited = -1;
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
