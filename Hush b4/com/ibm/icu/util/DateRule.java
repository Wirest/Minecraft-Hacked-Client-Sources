// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Date;

public interface DateRule
{
    Date firstAfter(final Date p0);
    
    Date firstBetween(final Date p0, final Date p1);
    
    boolean isOn(final Date p0);
    
    boolean isBetween(final Date p0, final Date p1);
}
