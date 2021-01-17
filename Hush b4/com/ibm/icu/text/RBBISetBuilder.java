// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import com.ibm.icu.impl.Assert;
import com.ibm.icu.impl.TrieBuilder;
import java.io.OutputStream;
import java.util.Iterator;
import com.ibm.icu.impl.IntTrieBuilder;

class RBBISetBuilder
{
    RBBIRuleBuilder fRB;
    RangeDescriptor fRangeList;
    IntTrieBuilder fTrie;
    int fTrieSize;
    int fGroupCount;
    boolean fSawBOF;
    RBBIDataManipulate dm;
    
    RBBISetBuilder(final RBBIRuleBuilder rb) {
        this.dm = new RBBIDataManipulate();
        this.fRB = rb;
    }
    
    void build() {
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("usets") >= 0) {
            this.printSets();
        }
        this.fRangeList = new RangeDescriptor();
        this.fRangeList.fStartChar = 0;
        this.fRangeList.fEndChar = 1114111;
        for (final RBBINode usetNode : this.fRB.fUSetNodes) {
            final UnicodeSet inputSet = usetNode.fInputSet;
            final int inputSetRangeCount = inputSet.getRangeCount();
            int inputSetRangeIndex = 0;
            RangeDescriptor rlRange = this.fRangeList;
            while (inputSetRangeIndex < inputSetRangeCount) {
                final int inputSetRangeBegin = inputSet.getRangeStart(inputSetRangeIndex);
                final int inputSetRangeEnd = inputSet.getRangeEnd(inputSetRangeIndex);
                while (rlRange.fEndChar < inputSetRangeBegin) {
                    rlRange = rlRange.fNext;
                }
                if (rlRange.fStartChar < inputSetRangeBegin) {
                    rlRange.split(inputSetRangeBegin);
                }
                else {
                    if (rlRange.fEndChar > inputSetRangeEnd) {
                        rlRange.split(inputSetRangeEnd + 1);
                    }
                    if (rlRange.fIncludesSets.indexOf(usetNode) == -1) {
                        rlRange.fIncludesSets.add(usetNode);
                    }
                    if (inputSetRangeEnd == rlRange.fEndChar) {
                        ++inputSetRangeIndex;
                    }
                    rlRange = rlRange.fNext;
                }
            }
        }
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("range") >= 0) {
            this.printRanges();
        }
        for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
            for (RangeDescriptor rlSearchRange = this.fRangeList; rlSearchRange != rlRange; rlSearchRange = rlSearchRange.fNext) {
                if (rlRange.fIncludesSets.equals(rlSearchRange.fIncludesSets)) {
                    rlRange.fNum = rlSearchRange.fNum;
                    break;
                }
            }
            if (rlRange.fNum == 0) {
                ++this.fGroupCount;
                rlRange.fNum = this.fGroupCount + 2;
                rlRange.setDictionaryFlag();
                this.addValToSets(rlRange.fIncludesSets, this.fGroupCount + 2);
            }
        }
        final String eofString = "eof";
        final String bofString = "bof";
        for (final RBBINode usetNode2 : this.fRB.fUSetNodes) {
            final UnicodeSet inputSet2 = usetNode2.fInputSet;
            if (inputSet2.contains(eofString)) {
                this.addValToSet(usetNode2, 1);
            }
            if (inputSet2.contains(bofString)) {
                this.addValToSet(usetNode2, 2);
                this.fSawBOF = true;
            }
        }
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("rgroup") >= 0) {
            this.printRangeGroups();
        }
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("esets") >= 0) {
            this.printSets();
        }
        this.fTrie = new IntTrieBuilder(null, 100000, 0, 0, true);
        for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
            this.fTrie.setRange(rlRange.fStartChar, rlRange.fEndChar + 1, rlRange.fNum, true);
        }
    }
    
    int getTrieSize() {
        int size = 0;
        try {
            size = this.fTrie.serialize(null, true, this.dm);
        }
        catch (IOException e) {
            Assert.assrt(false);
        }
        return size;
    }
    
    void serializeTrie(final OutputStream os) throws IOException {
        this.fTrie.serialize(os, true, this.dm);
    }
    
    void addValToSets(final List<RBBINode> sets, final int val) {
        for (final RBBINode usetNode : sets) {
            this.addValToSet(usetNode, val);
        }
    }
    
    void addValToSet(final RBBINode usetNode, final int val) {
        final RBBINode leafNode = new RBBINode(3);
        leafNode.fVal = val;
        if (usetNode.fLeftChild == null) {
            usetNode.fLeftChild = leafNode;
            leafNode.fParent = usetNode;
        }
        else {
            final RBBINode orNode = new RBBINode(9);
            orNode.fLeftChild = usetNode.fLeftChild;
            orNode.fRightChild = leafNode;
            orNode.fLeftChild.fParent = orNode;
            orNode.fRightChild.fParent = orNode;
            usetNode.fLeftChild = orNode;
            orNode.fParent = usetNode;
        }
    }
    
    int getNumCharCategories() {
        return this.fGroupCount + 3;
    }
    
    boolean sawBOF() {
        return this.fSawBOF;
    }
    
    int getFirstChar(final int category) {
        int retVal = -1;
        for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
            if (rlRange.fNum == category) {
                retVal = rlRange.fStartChar;
                break;
            }
        }
        return retVal;
    }
    
    void printRanges() {
        System.out.print("\n\n Nonoverlapping Ranges ...\n");
        for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
            System.out.print(" " + rlRange.fNum + "   " + rlRange.fStartChar + "-" + rlRange.fEndChar);
            for (int i = 0; i < rlRange.fIncludesSets.size(); ++i) {
                final RBBINode usetNode = rlRange.fIncludesSets.get(i);
                String setName = "anon";
                final RBBINode setRef = usetNode.fParent;
                if (setRef != null) {
                    final RBBINode varRef = setRef.fParent;
                    if (varRef != null && varRef.fType == 2) {
                        setName = varRef.fText;
                    }
                }
                System.out.print(setName);
                System.out.print("  ");
            }
            System.out.println("");
        }
    }
    
    void printRangeGroups() {
        int lastPrintedGroupNum = 0;
        System.out.print("\nRanges grouped by Unicode Set Membership...\n");
        for (RangeDescriptor rlRange = this.fRangeList; rlRange != null; rlRange = rlRange.fNext) {
            final int groupNum = rlRange.fNum & 0xBFFF;
            if (groupNum > lastPrintedGroupNum) {
                if ((lastPrintedGroupNum = groupNum) < 10) {
                    System.out.print(" ");
                }
                System.out.print(groupNum + " ");
                if ((rlRange.fNum & 0x4000) != 0x0) {
                    System.out.print(" <DICT> ");
                }
                for (int i = 0; i < rlRange.fIncludesSets.size(); ++i) {
                    final RBBINode usetNode = rlRange.fIncludesSets.get(i);
                    String setName = "anon";
                    final RBBINode setRef = usetNode.fParent;
                    if (setRef != null) {
                        final RBBINode varRef = setRef.fParent;
                        if (varRef != null && varRef.fType == 2) {
                            setName = varRef.fText;
                        }
                    }
                    System.out.print(setName);
                    System.out.print(" ");
                }
                int i = 0;
                for (RangeDescriptor tRange = rlRange; tRange != null; tRange = tRange.fNext) {
                    if (tRange.fNum == rlRange.fNum) {
                        if (i++ % 5 == 0) {
                            System.out.print("\n    ");
                        }
                        RBBINode.printHex(tRange.fStartChar, -1);
                        System.out.print("-");
                        RBBINode.printHex(tRange.fEndChar, 0);
                    }
                }
                System.out.print("\n");
            }
        }
        System.out.print("\n");
    }
    
    void printSets() {
        System.out.print("\n\nUnicode Sets List\n------------------\n");
        for (int i = 0; i < this.fRB.fUSetNodes.size(); ++i) {
            final RBBINode usetNode = this.fRB.fUSetNodes.get(i);
            RBBINode.printInt(2, i);
            String setName = "anonymous";
            final RBBINode setRef = usetNode.fParent;
            if (setRef != null) {
                final RBBINode varRef = setRef.fParent;
                if (varRef != null && varRef.fType == 2) {
                    setName = varRef.fText;
                }
            }
            System.out.print("  " + setName);
            System.out.print("   ");
            System.out.print(usetNode.fText);
            System.out.print("\n");
            if (usetNode.fLeftChild != null) {
                usetNode.fLeftChild.printTree(true);
            }
        }
        System.out.print("\n");
    }
    
    static class RangeDescriptor
    {
        int fStartChar;
        int fEndChar;
        int fNum;
        List<RBBINode> fIncludesSets;
        RangeDescriptor fNext;
        
        RangeDescriptor() {
            this.fIncludesSets = new ArrayList<RBBINode>();
        }
        
        RangeDescriptor(final RangeDescriptor other) {
            this.fStartChar = other.fStartChar;
            this.fEndChar = other.fEndChar;
            this.fNum = other.fNum;
            this.fIncludesSets = new ArrayList<RBBINode>(other.fIncludesSets);
        }
        
        void split(final int where) {
            Assert.assrt(where > this.fStartChar && where <= this.fEndChar);
            final RangeDescriptor nr = new RangeDescriptor(this);
            nr.fStartChar = where;
            this.fEndChar = where - 1;
            nr.fNext = this.fNext;
            this.fNext = nr;
        }
        
        void setDictionaryFlag() {
            for (int i = 0; i < this.fIncludesSets.size(); ++i) {
                final RBBINode usetNode = this.fIncludesSets.get(i);
                String setName = "";
                final RBBINode setRef = usetNode.fParent;
                if (setRef != null) {
                    final RBBINode varRef = setRef.fParent;
                    if (varRef != null && varRef.fType == 2) {
                        setName = varRef.fText;
                    }
                }
                if (setName.equals("dictionary")) {
                    this.fNum |= 0x4000;
                    break;
                }
            }
        }
    }
    
    class RBBIDataManipulate implements TrieBuilder.DataManipulate
    {
        public int getFoldedValue(int start, final int offset) {
            final boolean[] inBlockZero = { false };
            final int limit = start + 1024;
            while (start < limit) {
                final int value = RBBISetBuilder.this.fTrie.getValue(start, inBlockZero);
                if (inBlockZero[0]) {
                    start += 32;
                }
                else {
                    if (value != 0) {
                        return offset | 0x8000;
                    }
                    ++start;
                }
            }
            return 0;
        }
    }
}
