package nivia.gui.aclickgui;


import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Apex on 8/26/2016.
 */
public abstract class Theme {
    public String name;
    private final List<Element> panelList = new ArrayList<>();
    public List<Element> getPanelList() {
        return panelList;
    }

    public Theme(String name) {
        this.name = name;
    }

    public abstract void insert();
    protected abstract void keyTyped(char typedChar, int keyCode);
    public abstract void mouseReleased(int mouseX, int mouseY, int mouseButton);
    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks);
    public abstract void mouseClicked(int mouseX, int mouseY, int button);
    public abstract void handleMouseInput();
    public abstract void updatePanel(String category, float posx, float posy, int maxh, boolean mnmzd);
    public abstract void writeSave(BufferedWriter writer) throws IOException;
}
