// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

class VecMathI18N
{
    static String getString(final String s) {
        String string;
        try {
            string = ResourceBundle.getBundle("javax.vecmath.ExceptionStrings").getString(s);
        }
        catch (MissingResourceException ex) {
            System.err.println("VecMathI18N: Error looking up: " + s);
            string = s;
        }
        return string;
    }
}
