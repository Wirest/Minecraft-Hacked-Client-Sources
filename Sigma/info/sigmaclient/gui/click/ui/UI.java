package info.sigmaclient.gui.click.ui;

import info.sigmaclient.gui.click.ClickGui;
import info.sigmaclient.gui.click.components.*;
import net.minecraft.client.Minecraft;

/**
 * Created by cool1 on 1/21/2017.
 */
public abstract class UI {

    public Minecraft g = Minecraft.getMinecraft();

    public abstract void mainConstructor(ClickGui p0, MainPanel panel);

    public abstract void onClose();

    public abstract void mainPanelDraw(MainPanel panel, int p0, int p1);

    public abstract void mainPanelKeyPress(MainPanel panel, int key);

    public abstract void panelConstructor(MainPanel mainPanel, float x, float y);

    public abstract void panelMouseClicked(MainPanel mainPanel, int p1, int p2, int p3);

    public abstract void panelMouseMovedOrUp(MainPanel mainPanel, int p1, int p2, int p3);

    public abstract void categoryButtonConstructor(final CategoryButton p0, final MainPanel p1);

    public abstract void categoryButtonMouseClicked(final CategoryButton p0, final MainPanel p1, final int p2, final int p3, final int p4);

    public abstract void categoryButtonDraw(final CategoryButton p0, final float p2, final float p3);

    public abstract void categoryPanelConstructor(final CategoryPanel categoryPanel, final CategoryButton categoryButton, final float x, final float y);

    public abstract void categoryPanelMouseClicked(final CategoryPanel categoryPanel, int p1, int p2, int p3);

    public abstract void categoryPanelDraw(final CategoryPanel categoryPanel, final float x, final float y);

    public abstract void categoryPanelMouseMovedOrUp(final CategoryPanel categoryPanel, final int x, final int y, final int button);

    public abstract void buttonContructor(final Button p0, final CategoryPanel panel);

    public abstract void buttonMouseClicked(final Button p0, final int p2, final int p3, final int p4, final CategoryPanel panel);

    public abstract void buttonDraw(final Button p0, final float p2, final float p3, final CategoryPanel panel);

    public abstract void buttonKeyPressed(final Button button, final int key);

    public abstract void checkBoxMouseClicked(final Checkbox p0, final int p2, final int p3, final int p4, final CategoryPanel panel);

    public abstract void checkBoxDraw(final Checkbox p0, final float p2, final float p3, final CategoryPanel panel);

    public abstract void dropDownContructor(final DropdownBox p0, final float x, final float u, final CategoryPanel panel);

    public abstract void dropDownMouseClicked(final DropdownBox p0, final int x, final int u, final int mouse, final CategoryPanel panel);

    public abstract void dropDownDraw(final DropdownBox p0, final float x, final float y, final CategoryPanel panel);

    public abstract void dropDownButtonMouseClicked(final DropdownButton p0, final DropdownBox p1, final int x, final int y, final int mouse);

    public abstract void dropDownButtonDraw(final DropdownButton p0, final DropdownBox p1, final float x, final float y);

    public abstract void SliderContructor(final Slider p0, final CategoryPanel panel);

    public abstract void SliderMouseClicked(final Slider p0, final int p2, final int p3, final int p4, final CategoryPanel panel);

    public abstract void SliderMouseMovedOrUp(final Slider p0, final int p2, final int p3, final int p4, final CategoryPanel panel);

    public abstract void SliderDraw(final Slider p0, final float p2, final float p3, final CategoryPanel panel);

    public abstract void categoryButtonMouseReleased(CategoryButton categoryButton, int x, int y, int button);

    public abstract void slButtonDraw(SLButton slButton, float x, float y, MainPanel panel);

    public abstract void slButtonMouseClicked(SLButton slButton, float x, float y, int button, MainPanel panel);

    public abstract void colorConstructor(ColorPreview colorPreview, float x, float y);

    public abstract void colorPrewviewDraw(ColorPreview colorPreview, float x, float y);

    public abstract void rgbSliderDraw(RGBSlider slider, float x, float y);

    public abstract void rgbSliderClick(RGBSlider slider, float x, float y, int mouse);

    public abstract void rgbSliderMovedOrUp(RGBSlider slider, float x, float y, int mouse);

}

