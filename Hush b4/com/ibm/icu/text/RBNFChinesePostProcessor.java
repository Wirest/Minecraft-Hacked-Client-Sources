// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

final class RBNFChinesePostProcessor implements RBNFPostProcessor
{
    private boolean longForm;
    private int format;
    private static final String[] rulesetNames;
    
    public void init(final RuleBasedNumberFormat formatter, final String rules) {
    }
    
    public void process(final StringBuffer buf, final NFRuleSet ruleSet) {
        final String name = ruleSet.getName();
        for (int i = 0; i < RBNFChinesePostProcessor.rulesetNames.length; ++i) {
            if (RBNFChinesePostProcessor.rulesetNames[i].equals(name)) {
                this.format = i;
                this.longForm = (i == 1 || i == 3);
                break;
            }
        }
        if (this.longForm) {
            for (int i = buf.indexOf("*"); i != -1; i = buf.indexOf("*", i)) {
                buf.delete(i, i + 1);
            }
            return;
        }
        final String DIAN = "\u9ede";
        final String[][] markers = { { "\u842c", "\u5104", "\u5146", "\u3007" }, { "\u4e07", "\u4ebf", "\u5146", "\u3007" }, { "\u842c", "\u5104", "\u5146", "\u96f6" } };
        final String[] m = markers[this.format];
        for (int j = 0; j < m.length - 1; ++j) {
            final int n = buf.indexOf(m[j]);
            if (n != -1) {
                buf.insert(n + m[j].length(), '|');
            }
        }
        int x = buf.indexOf("\u9ede");
        if (x == -1) {
            x = buf.length();
        }
        int s = 0;
        int n = -1;
        final String ling = markers[this.format][3];
        while (x >= 0) {
            final int k = buf.lastIndexOf("|", x);
            final int nn = buf.lastIndexOf(ling, x);
            int ns = 0;
            if (nn > k) {
                ns = ((nn > 0 && buf.charAt(nn - 1) != '*') ? 2 : 1);
            }
            x = k - 1;
            switch (s * 3 + ns) {
                case 0: {
                    s = ns;
                    n = -1;
                    continue;
                }
                case 1: {
                    s = ns;
                    n = nn;
                    continue;
                }
                case 2: {
                    s = ns;
                    n = -1;
                    continue;
                }
                case 3: {
                    s = ns;
                    n = -1;
                    continue;
                }
                case 4: {
                    buf.delete(nn - 1, nn + ling.length());
                    s = 0;
                    n = -1;
                    continue;
                }
                case 5: {
                    buf.delete(n - 1, n + ling.length());
                    s = ns;
                    n = -1;
                    continue;
                }
                case 6: {
                    s = ns;
                    n = -1;
                    continue;
                }
                case 7: {
                    buf.delete(nn - 1, nn + ling.length());
                    s = 0;
                    n = -1;
                    continue;
                }
                case 8: {
                    s = ns;
                    n = -1;
                    continue;
                }
                default: {
                    throw new IllegalStateException();
                }
            }
        }
        int l = buf.length();
        while (--l >= 0) {
            final char c = buf.charAt(l);
            if (c == '*' || c == '|') {
                buf.delete(l, l + 1);
            }
        }
    }
    
    static {
        rulesetNames = new String[] { "%traditional", "%simplified", "%accounting", "%time" };
    }
}
