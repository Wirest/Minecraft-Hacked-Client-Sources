package me.Corbis.Execution.Replay;

import io.netty.buffer.Unpooled;
import me.Corbis.Execution.Execution;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

import java.io.IOException;

public class GuiReplay extends GuiScreen {

    public GuiReplay(){}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect(0,0, width, height, 0xFF000000);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if(button.id == 0){
            Execution.instance.replayManager.init();
            Minecraft.getMinecraft().displayGuiScreen(new GuiDownloadTerrain(Minecraft.getMinecraft().getNetHandler()));
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|Brand", (new PacketBuffer(Unpooled.buffer())).writeString(ClientBrandRetriever.getClientModName())));;
            Execution.instance.isReplay = true;
        }
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        this.buttonList.add(new GuiButton(0, 0, 0, "Replay"));
        super.initGui();
    }
}
