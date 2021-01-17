// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.text.ParsePosition;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.impl.Assert;
import java.util.HashMap;

class RBBIRuleScanner
{
    private static final int kStackSize = 100;
    RBBIRuleBuilder fRB;
    int fScanIndex;
    int fNextIndex;
    boolean fQuoteMode;
    int fLineNum;
    int fCharNum;
    int fLastChar;
    RBBIRuleChar fC;
    String fVarName;
    short[] fStack;
    int fStackPtr;
    RBBINode[] fNodeStack;
    int fNodeStackPtr;
    boolean fReverseRule;
    boolean fLookAheadRule;
    RBBISymbolTable fSymbolTable;
    HashMap<String, RBBISetTableEl> fSetTable;
    UnicodeSet[] fRuleSets;
    int fRuleNum;
    int fOptionStart;
    private static String gRuleSet_rule_char_pattern;
    private static String gRuleSet_name_char_pattern;
    private static String gRuleSet_digit_char_pattern;
    private static String gRuleSet_name_start_char_pattern;
    private static String gRuleSet_white_space_pattern;
    private static String kAny;
    static final int chNEL = 133;
    static final int chLS = 8232;
    
    RBBIRuleScanner(final RBBIRuleBuilder rb) {
        this.fC = new RBBIRuleChar();
        this.fStack = new short[100];
        this.fNodeStack = new RBBINode[100];
        this.fSetTable = new HashMap<String, RBBISetTableEl>();
        this.fRuleSets = new UnicodeSet[10];
        this.fRB = rb;
        this.fLineNum = 1;
        this.fRuleSets[3] = new UnicodeSet(RBBIRuleScanner.gRuleSet_rule_char_pattern);
        this.fRuleSets[4] = new UnicodeSet(RBBIRuleScanner.gRuleSet_white_space_pattern);
        this.fRuleSets[1] = new UnicodeSet(RBBIRuleScanner.gRuleSet_name_char_pattern);
        this.fRuleSets[2] = new UnicodeSet(RBBIRuleScanner.gRuleSet_name_start_char_pattern);
        this.fRuleSets[0] = new UnicodeSet(RBBIRuleScanner.gRuleSet_digit_char_pattern);
        this.fSymbolTable = new RBBISymbolTable(this, rb.fRules);
    }
    
    boolean doParseActions(final int action) {
        RBBINode n = null;
        boolean returnVal = true;
        switch (action) {
            case 11: {
                this.pushNewNode(7);
                ++this.fRuleNum;
                break;
            }
            case 9: {
                this.fixOpStack(4);
                final RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                final RBBINode orNode = this.pushNewNode(9);
                orNode.fLeftChild = operandNode;
                operandNode.fParent = orNode;
                break;
            }
            case 7: {
                this.fixOpStack(4);
                final RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                final RBBINode catNode = this.pushNewNode(8);
                catNode.fLeftChild = operandNode;
                operandNode.fParent = catNode;
                break;
            }
            case 12: {
                this.pushNewNode(15);
                break;
            }
            case 10: {
                this.fixOpStack(2);
                break;
            }
            case 13: {
                break;
            }
            case 22: {
                n = this.fNodeStack[this.fNodeStackPtr - 1];
                n.fFirstPos = this.fNextIndex;
                this.pushNewNode(7);
                break;
            }
            case 3: {
                this.fixOpStack(1);
                final RBBINode startExprNode = this.fNodeStack[this.fNodeStackPtr - 2];
                final RBBINode varRefNode = this.fNodeStack[this.fNodeStackPtr - 1];
                final RBBINode RHSExprNode = this.fNodeStack[this.fNodeStackPtr];
                RHSExprNode.fFirstPos = startExprNode.fFirstPos;
                RHSExprNode.fLastPos = this.fScanIndex;
                RHSExprNode.fText = this.fRB.fRules.substring(RHSExprNode.fFirstPos, RHSExprNode.fLastPos);
                varRefNode.fLeftChild = RHSExprNode;
                RHSExprNode.fParent = varRefNode;
                this.fSymbolTable.addEntry(varRefNode.fText, varRefNode);
                this.fNodeStackPtr -= 3;
                break;
            }
            case 4: {
                this.fixOpStack(1);
                if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("rtree") >= 0) {
                    this.printNodeStack("end of rule");
                }
                Assert.assrt(this.fNodeStackPtr == 1);
                if (this.fLookAheadRule) {
                    final RBBINode thisRule = this.fNodeStack[this.fNodeStackPtr];
                    final RBBINode endNode = this.pushNewNode(6);
                    final RBBINode catNode2 = this.pushNewNode(8);
                    this.fNodeStackPtr -= 2;
                    catNode2.fLeftChild = thisRule;
                    catNode2.fRightChild = endNode;
                    this.fNodeStack[this.fNodeStackPtr] = catNode2;
                    endNode.fVal = this.fRuleNum;
                    endNode.fLookAheadEnd = true;
                }
                final int destRules = this.fReverseRule ? 1 : this.fRB.fDefaultTree;
                if (this.fRB.fTreeRoots[destRules] != null) {
                    final RBBINode thisRule2 = this.fNodeStack[this.fNodeStackPtr];
                    final RBBINode prevRules = this.fRB.fTreeRoots[destRules];
                    final RBBINode orNode2 = this.pushNewNode(9);
                    orNode2.fLeftChild = prevRules;
                    prevRules.fParent = orNode2;
                    orNode2.fRightChild = thisRule2;
                    thisRule2.fParent = orNode2;
                    this.fRB.fTreeRoots[destRules] = orNode2;
                }
                else {
                    this.fRB.fTreeRoots[destRules] = this.fNodeStack[this.fNodeStackPtr];
                }
                this.fReverseRule = false;
                this.fLookAheadRule = false;
                this.fNodeStackPtr = 0;
                break;
            }
            case 18: {
                this.error(66052);
                returnVal = false;
                break;
            }
            case 31: {
                this.error(66052);
                break;
            }
            case 28: {
                final RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                final RBBINode plusNode = this.pushNewNode(11);
                plusNode.fLeftChild = operandNode;
                operandNode.fParent = plusNode;
                break;
            }
            case 29: {
                final RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                final RBBINode qNode = this.pushNewNode(12);
                qNode.fLeftChild = operandNode;
                operandNode.fParent = qNode;
                break;
            }
            case 30: {
                final RBBINode operandNode = this.fNodeStack[this.fNodeStackPtr--];
                final RBBINode starNode = this.pushNewNode(10);
                starNode.fLeftChild = operandNode;
                operandNode.fParent = starNode;
                break;
            }
            case 17: {
                n = this.pushNewNode(0);
                final String s = String.valueOf((char)this.fC.fChar);
                this.findSetFor(s, n, null);
                n.fFirstPos = this.fScanIndex;
                n.fLastPos = this.fNextIndex;
                n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
                break;
            }
            case 2: {
                n = this.pushNewNode(0);
                this.findSetFor(RBBIRuleScanner.kAny, n, null);
                n.fFirstPos = this.fScanIndex;
                n.fLastPos = this.fNextIndex;
                n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
                break;
            }
            case 21: {
                n = this.pushNewNode(4);
                n.fVal = this.fRuleNum;
                n.fFirstPos = this.fScanIndex;
                n.fLastPos = this.fNextIndex;
                n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
                this.fLookAheadRule = true;
                break;
            }
            case 23: {
                n = this.pushNewNode(5);
                n.fVal = 0;
                n.fFirstPos = this.fScanIndex;
                n.fLastPos = this.fNextIndex;
                break;
            }
            case 25: {
                n = this.fNodeStack[this.fNodeStackPtr];
                final int v = UCharacter.digit((char)this.fC.fChar, 10);
                n.fVal = n.fVal * 10 + v;
                break;
            }
            case 27: {
                n = this.fNodeStack[this.fNodeStackPtr];
                n.fLastPos = this.fNextIndex;
                n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos);
                break;
            }
            case 26: {
                this.error(66062);
                returnVal = false;
                break;
            }
            case 15: {
                this.fOptionStart = this.fScanIndex;
                break;
            }
            case 14: {
                final String opt = this.fRB.fRules.substring(this.fOptionStart, this.fScanIndex);
                if (opt.equals("chain")) {
                    this.fRB.fChainRules = true;
                    break;
                }
                if (opt.equals("LBCMNoChain")) {
                    this.fRB.fLBCMNoChain = true;
                    break;
                }
                if (opt.equals("forward")) {
                    this.fRB.fDefaultTree = 0;
                    break;
                }
                if (opt.equals("reverse")) {
                    this.fRB.fDefaultTree = 1;
                    break;
                }
                if (opt.equals("safe_forward")) {
                    this.fRB.fDefaultTree = 2;
                    break;
                }
                if (opt.equals("safe_reverse")) {
                    this.fRB.fDefaultTree = 3;
                    break;
                }
                if (opt.equals("lookAheadHardBreak")) {
                    this.fRB.fLookAheadHardBreak = true;
                    break;
                }
                this.error(66061);
                break;
            }
            case 16: {
                this.fReverseRule = true;
                break;
            }
            case 24: {
                n = this.pushNewNode(2);
                n.fFirstPos = this.fScanIndex;
                break;
            }
            case 5: {
                n = this.fNodeStack[this.fNodeStackPtr];
                if (n == null || n.fType != 2) {
                    this.error(66049);
                    break;
                }
                n.fLastPos = this.fScanIndex;
                n.fText = this.fRB.fRules.substring(n.fFirstPos + 1, n.fLastPos);
                n.fLeftChild = this.fSymbolTable.lookupNode(n.fText);
                break;
            }
            case 1: {
                n = this.fNodeStack[this.fNodeStackPtr];
                if (n.fLeftChild == null) {
                    this.error(66058);
                    returnVal = false;
                    break;
                }
                break;
            }
            case 8: {
                break;
            }
            case 19: {
                this.error(66054);
                returnVal = false;
                break;
            }
            case 6: {
                returnVal = false;
                break;
            }
            case 20: {
                this.scanSet();
                break;
            }
            default: {
                this.error(66049);
                returnVal = false;
                break;
            }
        }
        return returnVal;
    }
    
    void error(final int e) {
        final String s = "Error " + e + " at line " + this.fLineNum + " column " + this.fCharNum;
        final IllegalArgumentException ex = new IllegalArgumentException(s);
        throw ex;
    }
    
    void fixOpStack(final int p) {
        while (true) {
            final RBBINode n = this.fNodeStack[this.fNodeStackPtr - 1];
            if (n.fPrecedence == 0) {
                System.out.print("RBBIRuleScanner.fixOpStack, bad operator node");
                this.error(66049);
                return;
            }
            if (n.fPrecedence < p || n.fPrecedence <= 2) {
                if (p <= 2) {
                    if (n.fPrecedence != p) {
                        this.error(66056);
                    }
                    this.fNodeStack[this.fNodeStackPtr - 1] = this.fNodeStack[this.fNodeStackPtr];
                    --this.fNodeStackPtr;
                }
                return;
            }
            n.fRightChild = this.fNodeStack[this.fNodeStackPtr];
            this.fNodeStack[this.fNodeStackPtr].fParent = n;
            --this.fNodeStackPtr;
        }
    }
    
    void findSetFor(final String s, final RBBINode node, UnicodeSet setToAdopt) {
        RBBISetTableEl el = this.fSetTable.get(s);
        if (el != null) {
            node.fLeftChild = el.val;
            Assert.assrt(node.fLeftChild.fType == 1);
            return;
        }
        if (setToAdopt == null) {
            if (s.equals(RBBIRuleScanner.kAny)) {
                setToAdopt = new UnicodeSet(0, 1114111);
            }
            else {
                final int c = UTF16.charAt(s, 0);
                setToAdopt = new UnicodeSet(c, c);
            }
        }
        final RBBINode usetNode = new RBBINode(1);
        usetNode.fInputSet = setToAdopt;
        usetNode.fParent = node;
        node.fLeftChild = usetNode;
        usetNode.fText = s;
        this.fRB.fUSetNodes.add(usetNode);
        el = new RBBISetTableEl();
        el.key = s;
        el.val = usetNode;
        this.fSetTable.put(el.key, el);
    }
    
    static String stripRules(final String rules) {
        final StringBuilder strippedRules = new StringBuilder();
        final int rulesLength = rules.length();
        int idx = 0;
        while (idx < rulesLength) {
            char ch = rules.charAt(idx++);
            if (ch == '#') {
                while (idx < rulesLength && ch != '\r' && ch != '\n' && ch != '\u0085') {
                    ch = rules.charAt(idx++);
                }
            }
            if (!UCharacter.isISOControl(ch)) {
                strippedRules.append(ch);
            }
        }
        return strippedRules.toString();
    }
    
    int nextCharLL() {
        if (this.fNextIndex >= this.fRB.fRules.length()) {
            return -1;
        }
        final int ch = UTF16.charAt(this.fRB.fRules, this.fNextIndex);
        this.fNextIndex = UTF16.moveCodePointOffset(this.fRB.fRules, this.fNextIndex, 1);
        if (ch == 13 || ch == 133 || ch == 8232 || (ch == 10 && this.fLastChar != 13)) {
            ++this.fLineNum;
            this.fCharNum = 0;
            if (this.fQuoteMode) {
                this.error(66057);
                this.fQuoteMode = false;
            }
        }
        else if (ch != 10) {
            ++this.fCharNum;
        }
        return this.fLastChar = ch;
    }
    
    void nextChar(final RBBIRuleChar c) {
        this.fScanIndex = this.fNextIndex;
        c.fChar = this.nextCharLL();
        c.fEscaped = false;
        if (c.fChar == 39) {
            if (UTF16.charAt(this.fRB.fRules, this.fNextIndex) != 39) {
                this.fQuoteMode = !this.fQuoteMode;
                if (this.fQuoteMode) {
                    c.fChar = 40;
                }
                else {
                    c.fChar = 41;
                }
                c.fEscaped = false;
                return;
            }
            c.fChar = this.nextCharLL();
            c.fEscaped = true;
        }
        if (this.fQuoteMode) {
            c.fEscaped = true;
        }
        else {
            if (c.fChar == 35) {
                do {
                    c.fChar = this.nextCharLL();
                } while (c.fChar != -1 && c.fChar != 13 && c.fChar != 10 && c.fChar != 133 && c.fChar != 8232);
            }
            if (c.fChar == -1) {
                return;
            }
            if (c.fChar == 92) {
                c.fEscaped = true;
                final int[] unescapeIndex = { this.fNextIndex };
                c.fChar = Utility.unescapeAt(this.fRB.fRules, unescapeIndex);
                if (unescapeIndex[0] == this.fNextIndex) {
                    this.error(66050);
                }
                this.fCharNum += unescapeIndex[0] - this.fNextIndex;
                this.fNextIndex = unescapeIndex[0];
            }
        }
    }
    
    void parse() {
        int state = 1;
        this.nextChar(this.fC);
        while (true) {
            while (state != 0) {
                RBBIRuleParseTable.RBBIRuleTableElement tableEl = RBBIRuleParseTable.gRuleParseStateTable[state];
                if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
                    System.out.println("char, line, col = ('" + (char)this.fC.fChar + "', " + this.fLineNum + ", " + this.fCharNum + "    state = " + tableEl.fStateName);
                }
                int tableRow = state;
                while (true) {
                    tableEl = RBBIRuleParseTable.gRuleParseStateTable[tableRow];
                    if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
                        System.out.print(".");
                    }
                    if (tableEl.fCharClass < 127 && !this.fC.fEscaped && tableEl.fCharClass == this.fC.fChar) {
                        break;
                    }
                    if (tableEl.fCharClass == 255) {
                        break;
                    }
                    if (tableEl.fCharClass == 254 && this.fC.fEscaped) {
                        break;
                    }
                    if (tableEl.fCharClass == 253 && this.fC.fEscaped) {
                        if (this.fC.fChar == 80) {
                            break;
                        }
                        if (this.fC.fChar == 112) {
                            break;
                        }
                    }
                    if (tableEl.fCharClass == 252 && this.fC.fChar == -1) {
                        break;
                    }
                    if (tableEl.fCharClass >= 128 && tableEl.fCharClass < 240 && !this.fC.fEscaped && this.fC.fChar != -1) {
                        final UnicodeSet uniset = this.fRuleSets[tableEl.fCharClass - 128];
                        if (uniset.contains(this.fC.fChar)) {
                            break;
                        }
                    }
                    ++tableRow;
                }
                if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("scan") >= 0) {
                    System.out.println("");
                }
                if (!this.doParseActions(tableEl.fAction)) {
                    if (this.fRB.fTreeRoots[1] == null) {
                        this.fRB.fTreeRoots[1] = this.pushNewNode(10);
                        final RBBINode operand = this.pushNewNode(0);
                        this.findSetFor(RBBIRuleScanner.kAny, operand, null);
                        this.fRB.fTreeRoots[1].fLeftChild = operand;
                        operand.fParent = this.fRB.fTreeRoots[1];
                        this.fNodeStackPtr -= 2;
                    }
                    if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("symbols") >= 0) {
                        this.fSymbolTable.rbbiSymtablePrint();
                    }
                    if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("ptree") >= 0) {
                        System.out.println("Completed Forward Rules Parse Tree...");
                        this.fRB.fTreeRoots[0].printTree(true);
                        System.out.println("\nCompleted Reverse Rules Parse Tree...");
                        this.fRB.fTreeRoots[1].printTree(true);
                        System.out.println("\nCompleted Safe Point Forward Rules Parse Tree...");
                        if (this.fRB.fTreeRoots[2] == null) {
                            System.out.println("  -- null -- ");
                        }
                        else {
                            this.fRB.fTreeRoots[2].printTree(true);
                        }
                        System.out.println("\nCompleted Safe Point Reverse Rules Parse Tree...");
                        if (this.fRB.fTreeRoots[3] == null) {
                            System.out.println("  -- null -- ");
                        }
                        else {
                            this.fRB.fTreeRoots[3].printTree(true);
                        }
                    }
                    return;
                }
                if (tableEl.fPushState != 0) {
                    ++this.fStackPtr;
                    if (this.fStackPtr >= 100) {
                        System.out.println("RBBIRuleScanner.parse() - state stack overflow.");
                        this.error(66049);
                    }
                    this.fStack[this.fStackPtr] = tableEl.fPushState;
                }
                if (tableEl.fNextChar) {
                    this.nextChar(this.fC);
                }
                if (tableEl.fNextState != 255) {
                    state = tableEl.fNextState;
                }
                else {
                    state = this.fStack[this.fStackPtr];
                    --this.fStackPtr;
                    if (this.fStackPtr >= 0) {
                        continue;
                    }
                    System.out.println("RBBIRuleScanner.parse() - state stack underflow.");
                    this.error(66049);
                }
            }
            continue;
        }
    }
    
    void printNodeStack(final String title) {
        System.out.println(title + ".  Dumping node stack...\n");
        for (int i = this.fNodeStackPtr; i > 0; --i) {
            this.fNodeStack[i].printTree(true);
        }
    }
    
    RBBINode pushNewNode(final int nodeType) {
        ++this.fNodeStackPtr;
        if (this.fNodeStackPtr >= 100) {
            System.out.println("RBBIRuleScanner.pushNewNode - stack overflow.");
            this.error(66049);
        }
        return this.fNodeStack[this.fNodeStackPtr] = new RBBINode(nodeType);
    }
    
    void scanSet() {
        UnicodeSet uset = null;
        final ParsePosition pos = new ParsePosition(this.fScanIndex);
        final int startPos = this.fScanIndex;
        try {
            uset = new UnicodeSet(this.fRB.fRules, pos, this.fSymbolTable, 1);
        }
        catch (Exception e) {
            this.error(66063);
        }
        if (uset.isEmpty()) {
            this.error(66060);
        }
        final int i = pos.getIndex();
        while (this.fNextIndex < i) {
            this.nextCharLL();
        }
        final RBBINode n = this.pushNewNode(0);
        n.fFirstPos = startPos;
        n.fLastPos = this.fNextIndex;
        this.findSetFor(n.fText = this.fRB.fRules.substring(n.fFirstPos, n.fLastPos), n, uset);
    }
    
    static {
        RBBIRuleScanner.gRuleSet_rule_char_pattern = "[^[\\p{Z}\\u0020-\\u007f]-[\\p{L}]-[\\p{N}]]";
        RBBIRuleScanner.gRuleSet_name_char_pattern = "[_\\p{L}\\p{N}]";
        RBBIRuleScanner.gRuleSet_digit_char_pattern = "[0-9]";
        RBBIRuleScanner.gRuleSet_name_start_char_pattern = "[_\\p{L}]";
        RBBIRuleScanner.gRuleSet_white_space_pattern = "[\\p{Pattern_White_Space}]";
        RBBIRuleScanner.kAny = "any";
    }
    
    static class RBBIRuleChar
    {
        int fChar;
        boolean fEscaped;
    }
    
    static class RBBISetTableEl
    {
        String key;
        RBBINode val;
    }
}
