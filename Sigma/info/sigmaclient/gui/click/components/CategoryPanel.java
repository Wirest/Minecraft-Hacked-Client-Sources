package info.sigmaclient.gui.click.components;

import info.sigmaclient.Client;
import info.sigmaclient.gui.click.ui.UI;
import info.sigmaclient.management.animate.Expand;
import info.sigmaclient.module.Module;

import java.util.ArrayList;

/**
 * Created by cool1 on 1/21/2017.
 */
public class CategoryPanel {

    public float x;
    public float y;
    public boolean visible;
    public CategoryButton categoryButton;
    public String headerString;
    public ArrayList<Button> buttons;
    public ArrayList<Slider> sliders;
    public ArrayList<DropdownBox> dropdownBoxes;
    public ArrayList<DropdownButton> dropdownButtons;
    public ArrayList<Checkbox> checkboxes;
    public ArrayList<ColorPreview> colorPreviews;
    public ArrayList<RGBSlider> rgbSliders;
    public Module settingModule;
    public Expand expand = new Expand(0, 0, 0, 0);

    public CategoryPanel(String name, CategoryButton categoryButton, float x, float y) {
        this.headerString = name;
        this.x = x;
        this.y = y;
        this.categoryButton = categoryButton;
        colorPreviews = new ArrayList<>();
        buttons = new ArrayList<>();
        sliders = new ArrayList<>();
        dropdownBoxes = new ArrayList<>();
        dropdownButtons = new ArrayList<>();
        checkboxes = new ArrayList<>();
        rgbSliders = new ArrayList<>();
        this.visible = false;
        categoryButton.panel.theme.categoryPanelConstructor(this, categoryButton, x, y);
    }

    public void draw(final float x, final float y) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryPanelDraw(this, x, y);
        }
    }

    public void mouseClicked(final int x, final int y, final int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryPanelMouseClicked(this, x, y, button);
        }
    }

    public void mouseReleased(final int x, final int y, final int button) {
        for (UI theme : Client.getClickGui().getThemes()) {
            theme.categoryPanelMouseMovedOrUp(this, x, y, button);
        }
    }

}
