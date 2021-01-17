package me.rigamortis.faurax.gui.click.theme;

import me.rigamortis.faurax.gui.click.*;
import me.rigamortis.faurax.gui.click.components.*;

public abstract class Theme
{
    private String name;
    private boolean visible;
    
    public abstract void mainConstructor(final ClickUI p0);
    
    public abstract void panelContructor(final Panel p0, final float p1, final float p2);
    
    public abstract void panelMouseClicked(final Panel p0, final int p1, final int p2, final int p3);
    
    public abstract void panelMouseMovedOrUp(final Panel p0, final int p1, final int p2, final int p3);
    
    public abstract void panelDraw(final Panel p0, final int p1, final int p2);
    
    public abstract void buttonContructor(final Button p0, final Panel p1);
    
    public abstract void buttonMouseClicked(final Button p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void buttonDraw(final Button p0, final Panel p1, final float p2, final float p3);
    
    public abstract void checkBoxContructor(final CheckBox p0, final Panel p1);
    
    public abstract void checkBoxMouseClicked(final CheckBox p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void checkBoxDraw(final CheckBox p0, final Panel p1, final float p2, final float p3);
    
    public abstract void dropDownContructor(final DropDown p0, final Panel p1, final float p2, final float p3);
    
    public abstract void dropDownMouseClicked(final DropDown p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void dropDownDraw(final DropDown p0, final Panel p1, final float p2, final float p3);
    
    public abstract void dropDownButtonContructor(final DropDownButton p0, final DropDown p1, final Panel p2);
    
    public abstract void dropDownButtonMouseClicked(final DropDownButton p0, final DropDown p1, final Panel p2, final int p3, final int p4, final int p5);
    
    public abstract void dropDownButtonDraw(final DropDownButton p0, final DropDown p1, final Panel p2, final float p3, final float p4);
    
    public abstract void ScrollBarContructor(final ScrollBar p0, final Panel p1, final float p2, final float p3);
    
    public abstract void ScrollBarMouseClicked(final ScrollBar p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void ScrollBarMouseMovedOrUp(final ScrollBar p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void ScrollBarDraw(final ScrollBar p0, final Panel p1, final float p2, final float p3);
    
    public abstract void ScrollBarButtonContructor(final ScrollBarButton p0, final ScrollBar p1, final Panel p2);
    
    public abstract void ScrollBarButtonMouseClicked(final ScrollBarButton p0, final ScrollBar p1, final Panel p2, final int p3, final int p4, final int p5);
    
    public abstract void ScrollBarButtonDraw(final ScrollBarButton p0, final ScrollBar p1, final Panel p2, final float p3, final float p4);
    
    public abstract void SliderContructor(final Slider p0, final Panel p1);
    
    public abstract void SliderMouseClicked(final Slider p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void SliderMouseMovedOrUp(final Slider p0, final Panel p1, final int p2, final int p3, final int p4);
    
    public abstract void SliderDraw(final Slider p0, final Panel p1, final float p2, final float p3);
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
}
