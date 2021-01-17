// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import java.text.ParsePosition;
import java.util.HashMap;

class RBBISymbolTable implements SymbolTable
{
    String fRules;
    HashMap<String, RBBISymbolTableEntry> fHashTable;
    RBBIRuleScanner fRuleScanner;
    String ffffString;
    UnicodeSet fCachedSetLookup;
    
    RBBISymbolTable(final RBBIRuleScanner rs, final String rules) {
        this.fRules = rules;
        this.fRuleScanner = rs;
        this.fHashTable = new HashMap<String, RBBISymbolTableEntry>();
        this.ffffString = "\uffff";
    }
    
    public char[] lookup(final String s) {
        final RBBISymbolTableEntry el = this.fHashTable.get(s);
        if (el == null) {
            return null;
        }
        RBBINode varRefNode;
        for (varRefNode = el.val; varRefNode.fLeftChild.fType == 2; varRefNode = varRefNode.fLeftChild) {}
        final RBBINode exprNode = varRefNode.fLeftChild;
        String retString;
        if (exprNode.fType == 0) {
            final RBBINode usetNode = exprNode.fLeftChild;
            this.fCachedSetLookup = usetNode.fInputSet;
            retString = this.ffffString;
        }
        else {
            this.fRuleScanner.error(66063);
            retString = exprNode.fText;
            this.fCachedSetLookup = null;
        }
        return retString.toCharArray();
    }
    
    public UnicodeMatcher lookupMatcher(final int ch) {
        UnicodeSet retVal = null;
        if (ch == 65535) {
            retVal = this.fCachedSetLookup;
            this.fCachedSetLookup = null;
        }
        return retVal;
    }
    
    public String parseReference(final String text, final ParsePosition pos, final int limit) {
        int i;
        final int start = i = pos.getIndex();
        String result = "";
        while (i < limit) {
            final int c = UTF16.charAt(text, i);
            if (i == start && !UCharacter.isUnicodeIdentifierStart(c)) {
                break;
            }
            if (!UCharacter.isUnicodeIdentifierPart(c)) {
                break;
            }
            i += UTF16.getCharCount(c);
        }
        if (i == start) {
            return result;
        }
        pos.setIndex(i);
        result = text.substring(start, i);
        return result;
    }
    
    RBBINode lookupNode(final String key) {
        RBBINode retNode = null;
        final RBBISymbolTableEntry el = this.fHashTable.get(key);
        if (el != null) {
            retNode = el.val;
        }
        return retNode;
    }
    
    void addEntry(final String key, final RBBINode val) {
        RBBISymbolTableEntry e = this.fHashTable.get(key);
        if (e != null) {
            this.fRuleScanner.error(66055);
            return;
        }
        e = new RBBISymbolTableEntry();
        e.key = key;
        e.val = val;
        this.fHashTable.put(e.key, e);
    }
    
    void rbbiSymtablePrint() {
        System.out.print("Variable Definitions\nName               Node Val     String Val\n----------------------------------------------------------------------\n");
        final RBBISymbolTableEntry[] syms = this.fHashTable.values().toArray(new RBBISymbolTableEntry[0]);
        for (int i = 0; i < syms.length; ++i) {
            final RBBISymbolTableEntry s = syms[i];
            System.out.print("  " + s.key + "  ");
            System.out.print("  " + s.val + "  ");
            System.out.print(s.val.fLeftChild.fText);
            System.out.print("\n");
        }
        System.out.println("\nParsed Variable Definitions\n");
        for (int i = 0; i < syms.length; ++i) {
            final RBBISymbolTableEntry s = syms[i];
            System.out.print(s.key);
            s.val.fLeftChild.printTree(true);
            System.out.print("\n");
        }
    }
    
    static class RBBISymbolTableEntry
    {
        String key;
        RBBINode val;
    }
}
