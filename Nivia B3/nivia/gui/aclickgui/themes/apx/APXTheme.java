package nivia.gui.aclickgui.themes.apx;

import org.lwjgl.input.Mouse;
import nivia.Pandora;
import nivia.gui.aclickgui.Element;
import nivia.gui.aclickgui.GuiAPX;
import nivia.gui.aclickgui.Theme;
import nivia.gui.aclickgui.themes.apx.elements.ModButton;
import nivia.gui.aclickgui.themes.apx.elements.Panel;
import nivia.modules.Module;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * Created by Apex on 8/26/2016.
 */
public class APXTheme extends Theme {

    public APXTheme() {
        super("APX");
    }

    @Override
    public void insert() {
        if (getPanelList().isEmpty()) {
        
            int i = 0;
            for (Module.Category category : Module.Category.values()) {
                if (category == Module.Category.NONE) continue;
                Panel modPanel = new Panel(category.name());
                
                modPanel.setPosX(20);
                modPanel.setPosY(20 + (i * 20));
                modPanel.setWidth(115);
                modPanel.minimized = true;
                int pheight = 0;
                modPanel.totalHeight = 0;
                
                for (Module m : Pandora.getModManager().mods) {
                    if (m.getCategory() != null && m.getCategory().getName().equalsIgnoreCase(modPanel.category)) {
                    
                        ModButton modButton = new ModButton(modPanel, m);

                        pheight += 15;
                        modButton.setWidth(modPanel.getWidth());

                        modPanel.elements.add(modButton);
                    }
                }
              
                modPanel.setHeight(16 + pheight);
                getPanelList().add(modPanel);
                ++i;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        for (Element e : getPanelList()) {
            e.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
        for (Element e : getPanelList()) {
            Panel p = (Panel) e;
            p.mouseReleased(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (Element e : getPanelList()) {
            Panel p = (Panel) e;
            p.update();
            p.drawElement(0, 0, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        for (int i = getPanelList().size()-1; i >= 0; i--) {
            Panel p = (Panel) getPanelList().get(i);
            if (p.isOverPanel(mouseX, mouseY)) {
                Panel toMove = (Panel) getPanelList().get(getPanelList().size() - 1);
                getPanelList().set(getPanelList().indexOf(p), toMove);
                getPanelList().set(getPanelList().size() - 1, p);
                p.mouseClicked(mouseX, mouseY, button);
                break;
            }
        }
    }

    @Override
    public void handleMouseInput() {
        int direction = Mouse.getEventDWheel();
        for (Element p : getPanelList()) {
            ((Panel) p).handleMouseInput(direction);
        }
    }

    @Override
    public void updatePanel(String category, float posx, float posy, int maxh, boolean mnmzd) {
        for (Element e : getPanelList()) {
            Panel p = (Panel) e;

            if (p.category.toLowerCase().replaceAll(" ", "").equalsIgnoreCase(category.toLowerCase().replaceAll(" ", ""))) {
                p.setPosX(posx);
                p.setPosY(posy);
                p.maxHeight = maxh;
                p.minimized = mnmzd;
            }
        }
    }

    @Override
    public void writeSave(BufferedWriter writer) throws IOException {
        for (Element e : GuiAPX.getTheme().getPanelList()) {
            Panel p = (Panel) e;
            writer.write(p.category + " = ");
            writer.write(p.getPosX() + ", " + p.getPosY() + ", " + p.maxHeight + ", " + p.minimized);
            writer.newLine();
        }
    }
}
