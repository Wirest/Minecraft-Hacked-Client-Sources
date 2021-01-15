// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Desktop;
import java.net.URI;

public class BrowserUtil
{
    protected static void openWebpage(final URI uri) {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void openWebpage(final URL url) {
        try {
            openWebpage(url.toURI());
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
