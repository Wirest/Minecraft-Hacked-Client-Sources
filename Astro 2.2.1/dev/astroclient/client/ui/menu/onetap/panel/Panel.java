package dev.astroclient.client.ui.menu.onetap.panel;

import java.io.IOException;

public interface Panel {

    void init();

    void drawScreen(int mouseX, int mouseY, float partialTicks);

    void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException;

    void mouseReleased(int mouseX, int mouseY, int state);

    void keyTyped(char typedChar, int key);


}
