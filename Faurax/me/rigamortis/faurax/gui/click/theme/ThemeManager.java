package me.rigamortis.faurax.gui.click.theme;

import java.util.*;
import me.rigamortis.faurax.gui.click.theme.themes.*;

public class ThemeManager
{
    public ArrayList<Theme> themes;
    
    public ThemeManager() {
        (this.themes = new ArrayList<Theme>()).add(new Faurax());
        this.themes.add(new Huzuni());
        this.themes.add(new Nodus());
        this.themes.add(new Vanity());
    }
}
