// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.data;

import com.ibm.icu.impl.ICUData;
import java.util.ListResourceBundle;

public class BreakIteratorRules_th extends ListResourceBundle
{
    private static final String DATA_NAME = "data/th.brk";
    
    public Object[][] getContents() {
        final boolean exists = ICUData.exists("data/th.brk");
        if (!exists) {
            return new Object[0][0];
        }
        return new Object[][] { { "BreakIteratorClasses", { "RuleBasedBreakIterator", "DictionaryBasedBreakIterator", "DictionaryBasedBreakIterator", "RuleBasedBreakIterator" } }, { "WordBreakDictionary", "data/th.brk" }, { "LineBreakDictionary", "data/th.brk" } };
    }
}
