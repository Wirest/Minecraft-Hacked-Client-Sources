// 
// Decompiled by Procyon v0.5.36
// 

package javax.jnlp;

import java.awt.print.Printable;
import java.awt.print.Pageable;
import java.awt.print.PageFormat;

public interface PrintService
{
    PageFormat getDefaultPage();
    
    boolean print(final Pageable p0);
    
    boolean print(final Printable p0);
    
    PageFormat showPageFormatDialog(final PageFormat p0);
}
