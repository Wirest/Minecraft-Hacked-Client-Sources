// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

public class Registry extends Util
{
    private static final String versionString = "1.3";
    private static final String extensionString = "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess ";
    
    public static String gluGetString(final int name) {
        if (name == 100800) {
            return "1.3";
        }
        if (name == 100801) {
            return "GLU_EXT_nurbs_tessellator GLU_EXT_object_space_tess ";
        }
        return null;
    }
    
    public static boolean gluCheckExtension(final String extName, final String extString) {
        return extString != null && extName != null && extString.indexOf(extName) != -1;
    }
}
