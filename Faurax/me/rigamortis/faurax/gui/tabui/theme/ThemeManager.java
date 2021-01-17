package me.rigamortis.faurax.gui.tabui.theme;

import java.util.*;
import me.rigamortis.faurax.gui.tabui.theme.themes.*;

public class ThemeManager
{
    public ArrayList<Theme> themes;
    
    public ThemeManager() {
        (this.themes = new ArrayList<Theme>()).add(new Faurax());
    }
}
