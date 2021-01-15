package viamcp.gui;

import com.github.creeper123123321.viafabric.ViaFabric;
import com.github.creeper123123321.viafabric.util.ProtocolUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import viamcp.utils.ProtocolSorter;

import java.io.IOException;

public class GuiProtocolSelector extends GuiScreen {

    public SlotList list;

    private GuiScreen parent;

    public GuiProtocolSelector(GuiScreen parent) {
        this.parent = parent;
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.add(new GuiButton(1, width / 2 - 100, height - 27, 200, 20, "Back"));
        list = new SlotList(mc, width, height, 32, height - 32, 10);
    }
    
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        list.actionPerformed(button);

        if (button.id == 1)
            mc.displayGuiScreen(parent);
    }

    @Override
    public void handleMouseInput() throws IOException {
        list.handleMouseInput();
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int drawScreen, int mouseX, float mouseY) {
    	
    	//drawGradientRect(0, 0, width, height, 0xffff0000, 0xff00ff00);
    	
        list.drawScreen(drawScreen, mouseX, mouseY);

        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        this.drawCenteredString(this.fontRendererObj, EnumChatFormatting.BOLD.toString() + "Via Version",this.width / 4, 6, 16777215);
        GL11.glPopMatrix();
        super.drawScreen(drawScreen, mouseX, mouseY);
    }
    
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    	
    	if (keyCode == Keyboard.KEY_ESCAPE) {
    		
    		mc.displayGuiScreen(parent);
    		
    	}
    	
    }
    
    public class SlotList extends GuiSlot {
    	
        public SlotList(Minecraft p_i1052_1_, int p_i1052_2_, int p_i1052_3_, int p_i1052_4_, int p_i1052_5_, int p_i1052_6_) {
            super(p_i1052_1_, p_i1052_2_, p_i1052_3_, p_i1052_4_, p_i1052_5_, p_i1052_6_);
        }

        @Override
        protected int getSize() {
            return ProtocolSorter.getProtocolVersions().size();
        }

        @Override
        protected void elementClicked(int i, boolean b, int i1, int i2) {
            ViaFabric.clientSideVersion = ProtocolSorter.getProtocolVersions().get(i).getVersion();
        }

        @Override
        protected boolean isSelected(int i) {
            return false;
        }

        @Override
        protected void drawBackground() {
            drawDefaultBackground();
        }

        @Override
        protected void drawSlot(int i, int i1, int i2, int i3, int i4, int i5) {
            drawCenteredString(mc.fontRendererObj,(ViaFabric.clientSideVersion == ProtocolSorter.getProtocolVersions().get(i).getVersion() ? EnumChatFormatting.GREEN.toString() : EnumChatFormatting.WHITE.toString()) + ProtocolUtils.getProtocolName(ProtocolSorter.getProtocolVersions().get(i).getVersion()) , width / 2, i2 + 2, -1);
        }
        
    }
    
}
