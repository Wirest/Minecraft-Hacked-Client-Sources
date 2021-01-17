// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.Set;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.impl.Assert;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

class RBBITableBuilder
{
    private RBBIRuleBuilder fRB;
    private int fRootIx;
    private List<RBBIStateDescriptor> fDStates;
    
    RBBITableBuilder(final RBBIRuleBuilder rb, final int rootNodeIx) {
        this.fRootIx = rootNodeIx;
        this.fRB = rb;
        this.fDStates = new ArrayList<RBBIStateDescriptor>();
    }
    
    void build() {
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return;
        }
        this.fRB.fTreeRoots[this.fRootIx] = this.fRB.fTreeRoots[this.fRootIx].flattenVariables();
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("ftree") >= 0) {
            System.out.println("Parse tree after flattening variable references.");
            this.fRB.fTreeRoots[this.fRootIx].printTree(true);
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            final RBBINode bofTop = new RBBINode(8);
            final RBBINode bofLeaf = new RBBINode(3);
            bofTop.fLeftChild = bofLeaf;
            bofTop.fRightChild = this.fRB.fTreeRoots[this.fRootIx];
            bofLeaf.fParent = bofTop;
            bofLeaf.fVal = 2;
            this.fRB.fTreeRoots[this.fRootIx] = bofTop;
        }
        final RBBINode cn = new RBBINode(8);
        cn.fLeftChild = this.fRB.fTreeRoots[this.fRootIx];
        this.fRB.fTreeRoots[this.fRootIx].fParent = cn;
        cn.fRightChild = new RBBINode(6);
        cn.fRightChild.fParent = cn;
        (this.fRB.fTreeRoots[this.fRootIx] = cn).flattenSets();
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("stree") >= 0) {
            System.out.println("Parse tree after flattening Unicode Set references.");
            this.fRB.fTreeRoots[this.fRootIx].printTree(true);
        }
        this.calcNullable(this.fRB.fTreeRoots[this.fRootIx]);
        this.calcFirstPos(this.fRB.fTreeRoots[this.fRootIx]);
        this.calcLastPos(this.fRB.fTreeRoots[this.fRootIx]);
        this.calcFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("pos") >= 0) {
            System.out.print("\n");
            this.printPosSets(this.fRB.fTreeRoots[this.fRootIx]);
        }
        if (this.fRB.fChainRules) {
            this.calcChainedFollowPos(this.fRB.fTreeRoots[this.fRootIx]);
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            this.bofFixup();
        }
        this.buildStateTable();
        this.flagAcceptingStates();
        this.flagLookAheadStates();
        this.flagTaggedStates();
        this.mergeRuleStatusVals();
        if (this.fRB.fDebugEnv != null && this.fRB.fDebugEnv.indexOf("states") >= 0) {
            this.printStates();
        }
    }
    
    void calcNullable(final RBBINode n) {
        if (n == null) {
            return;
        }
        if (n.fType == 0 || n.fType == 6) {
            n.fNullable = false;
            return;
        }
        if (n.fType == 4 || n.fType == 5) {
            n.fNullable = true;
            return;
        }
        this.calcNullable(n.fLeftChild);
        this.calcNullable(n.fRightChild);
        if (n.fType == 9) {
            n.fNullable = (n.fLeftChild.fNullable || n.fRightChild.fNullable);
        }
        else if (n.fType == 8) {
            n.fNullable = (n.fLeftChild.fNullable && n.fRightChild.fNullable);
        }
        else if (n.fType == 10 || n.fType == 12) {
            n.fNullable = true;
        }
        else {
            n.fNullable = false;
        }
    }
    
    void calcFirstPos(final RBBINode n) {
        if (n == null) {
            return;
        }
        if (n.fType == 3 || n.fType == 6 || n.fType == 4 || n.fType == 5) {
            n.fFirstPosSet.add(n);
            return;
        }
        this.calcFirstPos(n.fLeftChild);
        this.calcFirstPos(n.fRightChild);
        if (n.fType == 9) {
            n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
            n.fFirstPosSet.addAll(n.fRightChild.fFirstPosSet);
        }
        else if (n.fType == 8) {
            n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
            if (n.fLeftChild.fNullable) {
                n.fFirstPosSet.addAll(n.fRightChild.fFirstPosSet);
            }
        }
        else if (n.fType == 10 || n.fType == 12 || n.fType == 11) {
            n.fFirstPosSet.addAll(n.fLeftChild.fFirstPosSet);
        }
    }
    
    void calcLastPos(final RBBINode n) {
        if (n == null) {
            return;
        }
        if (n.fType == 3 || n.fType == 6 || n.fType == 4 || n.fType == 5) {
            n.fLastPosSet.add(n);
            return;
        }
        this.calcLastPos(n.fLeftChild);
        this.calcLastPos(n.fRightChild);
        if (n.fType == 9) {
            n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
            n.fLastPosSet.addAll(n.fRightChild.fLastPosSet);
        }
        else if (n.fType == 8) {
            n.fLastPosSet.addAll(n.fRightChild.fLastPosSet);
            if (n.fRightChild.fNullable) {
                n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
            }
        }
        else if (n.fType == 10 || n.fType == 12 || n.fType == 11) {
            n.fLastPosSet.addAll(n.fLeftChild.fLastPosSet);
        }
    }
    
    void calcFollowPos(final RBBINode n) {
        if (n == null || n.fType == 3 || n.fType == 6) {
            return;
        }
        this.calcFollowPos(n.fLeftChild);
        this.calcFollowPos(n.fRightChild);
        if (n.fType == 8) {
            for (final RBBINode i : n.fLeftChild.fLastPosSet) {
                i.fFollowPos.addAll(n.fRightChild.fFirstPosSet);
            }
        }
        if (n.fType == 10 || n.fType == 11) {
            for (final RBBINode i : n.fLastPosSet) {
                i.fFollowPos.addAll(n.fFirstPosSet);
            }
        }
    }
    
    void calcChainedFollowPos(final RBBINode tree) {
        final List<RBBINode> endMarkerNodes = new ArrayList<RBBINode>();
        final List<RBBINode> leafNodes = new ArrayList<RBBINode>();
        tree.findNodes(endMarkerNodes, 6);
        tree.findNodes(leafNodes, 3);
        RBBINode userRuleRoot = tree;
        if (this.fRB.fSetBuilder.sawBOF()) {
            userRuleRoot = tree.fLeftChild.fRightChild;
        }
        Assert.assrt(userRuleRoot != null);
        final Set<RBBINode> matchStartNodes = userRuleRoot.fFirstPosSet;
        for (final RBBINode tNode : leafNodes) {
            RBBINode endNode = null;
            for (final RBBINode endMarkerNode : endMarkerNodes) {
                if (tNode.fFollowPos.contains(endMarkerNode)) {
                    endNode = tNode;
                    break;
                }
            }
            if (endNode == null) {
                continue;
            }
            if (this.fRB.fLBCMNoChain) {
                final int c = this.fRB.fSetBuilder.getFirstChar(endNode.fVal);
                if (c != -1) {
                    final int cLBProp = UCharacter.getIntPropertyValue(c, 4104);
                    if (cLBProp == 9) {
                        continue;
                    }
                }
            }
            for (final RBBINode startNode : matchStartNodes) {
                if (startNode.fType != 3) {
                    continue;
                }
                if (endNode.fVal != startNode.fVal) {
                    continue;
                }
                endNode.fFollowPos.addAll(startNode.fFollowPos);
            }
        }
    }
    
    void bofFixup() {
        final RBBINode bofNode = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fLeftChild;
        Assert.assrt(bofNode.fType == 3);
        Assert.assrt(bofNode.fVal == 2);
        final Set<RBBINode> matchStartNodes = this.fRB.fTreeRoots[this.fRootIx].fLeftChild.fRightChild.fFirstPosSet;
        for (final RBBINode startNode : matchStartNodes) {
            if (startNode.fType != 3) {
                continue;
            }
            if (startNode.fVal != bofNode.fVal) {
                continue;
            }
            bofNode.fFollowPos.addAll(startNode.fFollowPos);
        }
    }
    
    void buildStateTable() {
        final int lastInputSymbol = this.fRB.fSetBuilder.getNumCharCategories() - 1;
        final RBBIStateDescriptor failState = new RBBIStateDescriptor(lastInputSymbol);
        this.fDStates.add(failState);
        final RBBIStateDescriptor initialState = new RBBIStateDescriptor(lastInputSymbol);
        initialState.fPositions.addAll(this.fRB.fTreeRoots[this.fRootIx].fFirstPosSet);
        this.fDStates.add(initialState);
        while (true) {
            RBBIStateDescriptor T = null;
            for (int tx = 1; tx < this.fDStates.size(); ++tx) {
                final RBBIStateDescriptor temp = this.fDStates.get(tx);
                if (!temp.fMarked) {
                    T = temp;
                    break;
                }
            }
            if (T == null) {
                break;
            }
            T.fMarked = true;
            for (int a = 1; a <= lastInputSymbol; ++a) {
                Set<RBBINode> U = null;
                for (final RBBINode p : T.fPositions) {
                    if (p.fType == 3 && p.fVal == a) {
                        if (U == null) {
                            U = new HashSet<RBBINode>();
                        }
                        U.addAll(p.fFollowPos);
                    }
                }
                int ux = 0;
                boolean UinDstates = false;
                if (U != null) {
                    Assert.assrt(U.size() > 0);
                    for (int ix = 0; ix < this.fDStates.size(); ++ix) {
                        final RBBIStateDescriptor temp2 = this.fDStates.get(ix);
                        if (U.equals(temp2.fPositions)) {
                            U = temp2.fPositions;
                            ux = ix;
                            UinDstates = true;
                            break;
                        }
                    }
                    if (!UinDstates) {
                        final RBBIStateDescriptor newState = new RBBIStateDescriptor(lastInputSymbol);
                        newState.fPositions = U;
                        this.fDStates.add(newState);
                        ux = this.fDStates.size() - 1;
                    }
                    T.fDtran[a] = ux;
                }
            }
        }
    }
    
    void flagAcceptingStates() {
        final List<RBBINode> endMarkerNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(endMarkerNodes, 6);
        for (int i = 0; i < endMarkerNodes.size(); ++i) {
            final RBBINode endMarker = endMarkerNodes.get(i);
            for (int n = 0; n < this.fDStates.size(); ++n) {
                final RBBIStateDescriptor sd = this.fDStates.get(n);
                if (sd.fPositions.contains(endMarker)) {
                    if (sd.fAccepting == 0) {
                        sd.fAccepting = endMarker.fVal;
                        if (sd.fAccepting == 0) {
                            sd.fAccepting = -1;
                        }
                    }
                    if (sd.fAccepting == -1 && endMarker.fVal != 0) {
                        sd.fAccepting = endMarker.fVal;
                    }
                    if (endMarker.fLookAheadEnd) {
                        sd.fLookAhead = sd.fAccepting;
                    }
                }
            }
        }
    }
    
    void flagLookAheadStates() {
        final List<RBBINode> lookAheadNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(lookAheadNodes, 4);
        for (int i = 0; i < lookAheadNodes.size(); ++i) {
            final RBBINode lookAheadNode = lookAheadNodes.get(i);
            for (int n = 0; n < this.fDStates.size(); ++n) {
                final RBBIStateDescriptor sd = this.fDStates.get(n);
                if (sd.fPositions.contains(lookAheadNode)) {
                    sd.fLookAhead = lookAheadNode.fVal;
                }
            }
        }
    }
    
    void flagTaggedStates() {
        final List<RBBINode> tagNodes = new ArrayList<RBBINode>();
        this.fRB.fTreeRoots[this.fRootIx].findNodes(tagNodes, 5);
        for (int i = 0; i < tagNodes.size(); ++i) {
            final RBBINode tagNode = tagNodes.get(i);
            for (int n = 0; n < this.fDStates.size(); ++n) {
                final RBBIStateDescriptor sd = this.fDStates.get(n);
                if (sd.fPositions.contains(tagNode)) {
                    sd.fTagVals.add(tagNode.fVal);
                }
            }
        }
    }
    
    void mergeRuleStatusVals() {
        if (this.fRB.fRuleStatusVals.size() == 0) {
            this.fRB.fRuleStatusVals.add(1);
            this.fRB.fRuleStatusVals.add(0);
            final SortedSet<Integer> s0 = new TreeSet<Integer>();
            final Integer izero = 0;
            this.fRB.fStatusSets.put(s0, izero);
            final SortedSet<Integer> s2 = new TreeSet<Integer>();
            s2.add(izero);
            this.fRB.fStatusSets.put(s0, izero);
        }
        for (int n = 0; n < this.fDStates.size(); ++n) {
            final RBBIStateDescriptor sd = this.fDStates.get(n);
            final Set<Integer> statusVals = sd.fTagVals;
            Integer arrayIndexI = this.fRB.fStatusSets.get(statusVals);
            if (arrayIndexI == null) {
                arrayIndexI = this.fRB.fRuleStatusVals.size();
                this.fRB.fStatusSets.put(statusVals, arrayIndexI);
                this.fRB.fRuleStatusVals.add(statusVals.size());
                this.fRB.fRuleStatusVals.addAll(statusVals);
            }
            sd.fTagsIdx = arrayIndexI;
        }
    }
    
    void printPosSets(final RBBINode n) {
        if (n == null) {
            return;
        }
        RBBINode.printNode(n);
        System.out.print("         Nullable:  " + n.fNullable);
        System.out.print("         firstpos:  ");
        this.printSet(n.fFirstPosSet);
        System.out.print("         lastpos:   ");
        this.printSet(n.fLastPosSet);
        System.out.print("         followpos: ");
        this.printSet(n.fFollowPos);
        this.printPosSets(n.fLeftChild);
        this.printPosSets(n.fRightChild);
    }
    
    int getTableSize() {
        int size = 0;
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return 0;
        }
        size = 16;
        final int numRows = this.fDStates.size();
        final int numCols = this.fRB.fSetBuilder.getNumCharCategories();
        final int rowSize = 8 + 2 * numCols;
        for (size += numRows * rowSize; size % 8 > 0; ++size) {}
        return size;
    }
    
    short[] exportTable() {
        if (this.fRB.fTreeRoots[this.fRootIx] == null) {
            return new short[0];
        }
        Assert.assrt(this.fRB.fSetBuilder.getNumCharCategories() < 32767 && this.fDStates.size() < 32767);
        final int numStates = this.fDStates.size();
        final int rowLen = 4 + this.fRB.fSetBuilder.getNumCharCategories();
        final int tableSize = this.getTableSize() / 2;
        final short[] table = new short[tableSize];
        table[0] = (short)(numStates >>> 16);
        table[1] = (short)(numStates & 0xFFFF);
        table[2] = (short)(rowLen >>> 16);
        table[3] = (short)(rowLen & 0xFFFF);
        int flags = 0;
        if (this.fRB.fLookAheadHardBreak) {
            flags |= 0x1;
        }
        if (this.fRB.fSetBuilder.sawBOF()) {
            flags |= 0x2;
        }
        table[4] = (short)(flags >>> 16);
        table[5] = (short)(flags & 0xFFFF);
        final int numCharCategories = this.fRB.fSetBuilder.getNumCharCategories();
        for (int state = 0; state < numStates; ++state) {
            final RBBIStateDescriptor sd = this.fDStates.get(state);
            final int row = 8 + state * rowLen;
            Assert.assrt(-32768 < sd.fAccepting && sd.fAccepting <= 32767);
            Assert.assrt(-32768 < sd.fLookAhead && sd.fLookAhead <= 32767);
            table[row + 0] = (short)sd.fAccepting;
            table[row + 1] = (short)sd.fLookAhead;
            table[row + 2] = (short)sd.fTagsIdx;
            for (int col = 0; col < numCharCategories; ++col) {
                table[row + 4 + col] = (short)sd.fDtran[col];
            }
        }
        return table;
    }
    
    void printSet(final Collection<RBBINode> s) {
        for (final RBBINode n : s) {
            RBBINode.printInt(n.fSerialNum, 8);
        }
        System.out.println();
    }
    
    void printStates() {
        System.out.print("state |           i n p u t     s y m b o l s \n");
        System.out.print("      | Acc  LA    Tag");
        for (int c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); ++c) {
            RBBINode.printInt(c, 3);
        }
        System.out.print("\n");
        System.out.print("      |---------------");
        for (int c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); ++c) {
            System.out.print("---");
        }
        System.out.print("\n");
        for (int n = 0; n < this.fDStates.size(); ++n) {
            final RBBIStateDescriptor sd = this.fDStates.get(n);
            RBBINode.printInt(n, 5);
            System.out.print(" | ");
            RBBINode.printInt(sd.fAccepting, 3);
            RBBINode.printInt(sd.fLookAhead, 4);
            RBBINode.printInt(sd.fTagsIdx, 6);
            System.out.print(" ");
            for (int c = 0; c < this.fRB.fSetBuilder.getNumCharCategories(); ++c) {
                RBBINode.printInt(sd.fDtran[c], 3);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }
    
    void printRuleStatusTable() {
        int thisRecord = 0;
        int nextRecord = 0;
        final List<Integer> tbl = this.fRB.fRuleStatusVals;
        System.out.print("index |  tags \n");
        System.out.print("-------------------\n");
        while (nextRecord < tbl.size()) {
            thisRecord = nextRecord;
            nextRecord = thisRecord + tbl.get(thisRecord) + 1;
            RBBINode.printInt(thisRecord, 7);
            for (int i = thisRecord + 1; i < nextRecord; ++i) {
                final int val = tbl.get(i);
                RBBINode.printInt(val, 7);
            }
            System.out.print("\n");
        }
        System.out.print("\n\n");
    }
    
    static class RBBIStateDescriptor
    {
        boolean fMarked;
        int fAccepting;
        int fLookAhead;
        SortedSet<Integer> fTagVals;
        int fTagsIdx;
        Set<RBBINode> fPositions;
        int[] fDtran;
        
        RBBIStateDescriptor(final int maxInputSymbol) {
            this.fTagVals = new TreeSet<Integer>();
            this.fPositions = new HashSet<RBBINode>();
            this.fDtran = new int[maxInputSymbol + 1];
        }
    }
}
