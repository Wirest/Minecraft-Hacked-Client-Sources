// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.util.List;
import java.util.Collection;
import java.util.HashSet;
import com.ibm.icu.impl.Assert;
import java.util.Set;

class RBBINode
{
    static final int setRef = 0;
    static final int uset = 1;
    static final int varRef = 2;
    static final int leafChar = 3;
    static final int lookAhead = 4;
    static final int tag = 5;
    static final int endMark = 6;
    static final int opStart = 7;
    static final int opCat = 8;
    static final int opOr = 9;
    static final int opStar = 10;
    static final int opPlus = 11;
    static final int opQuestion = 12;
    static final int opBreak = 13;
    static final int opReverse = 14;
    static final int opLParen = 15;
    static final int nodeTypeLimit = 16;
    static final String[] nodeTypeNames;
    static final int precZero = 0;
    static final int precStart = 1;
    static final int precLParen = 2;
    static final int precOpOr = 3;
    static final int precOpCat = 4;
    int fType;
    RBBINode fParent;
    RBBINode fLeftChild;
    RBBINode fRightChild;
    UnicodeSet fInputSet;
    int fPrecedence;
    String fText;
    int fFirstPos;
    int fLastPos;
    boolean fNullable;
    int fVal;
    boolean fLookAheadEnd;
    Set<RBBINode> fFirstPosSet;
    Set<RBBINode> fLastPosSet;
    Set<RBBINode> fFollowPos;
    int fSerialNum;
    static int gLastSerial;
    
    RBBINode(final int t) {
        this.fPrecedence = 0;
        Assert.assrt(t < 16);
        this.fSerialNum = ++RBBINode.gLastSerial;
        this.fType = t;
        this.fFirstPosSet = new HashSet<RBBINode>();
        this.fLastPosSet = new HashSet<RBBINode>();
        this.fFollowPos = new HashSet<RBBINode>();
        if (t == 8) {
            this.fPrecedence = 4;
        }
        else if (t == 9) {
            this.fPrecedence = 3;
        }
        else if (t == 7) {
            this.fPrecedence = 1;
        }
        else if (t == 15) {
            this.fPrecedence = 2;
        }
        else {
            this.fPrecedence = 0;
        }
    }
    
    RBBINode(final RBBINode other) {
        this.fPrecedence = 0;
        this.fSerialNum = ++RBBINode.gLastSerial;
        this.fType = other.fType;
        this.fInputSet = other.fInputSet;
        this.fPrecedence = other.fPrecedence;
        this.fText = other.fText;
        this.fFirstPos = other.fFirstPos;
        this.fLastPos = other.fLastPos;
        this.fNullable = other.fNullable;
        this.fVal = other.fVal;
        this.fFirstPosSet = new HashSet<RBBINode>(other.fFirstPosSet);
        this.fLastPosSet = new HashSet<RBBINode>(other.fLastPosSet);
        this.fFollowPos = new HashSet<RBBINode>(other.fFollowPos);
    }
    
    RBBINode cloneTree() {
        RBBINode n;
        if (this.fType == 2) {
            n = this.fLeftChild.cloneTree();
        }
        else if (this.fType == 1) {
            n = this;
        }
        else {
            n = new RBBINode(this);
            if (this.fLeftChild != null) {
                n.fLeftChild = this.fLeftChild.cloneTree();
                n.fLeftChild.fParent = n;
            }
            if (this.fRightChild != null) {
                n.fRightChild = this.fRightChild.cloneTree();
                n.fRightChild.fParent = n;
            }
        }
        return n;
    }
    
    RBBINode flattenVariables() {
        if (this.fType == 2) {
            final RBBINode retNode = this.fLeftChild.cloneTree();
            return retNode;
        }
        if (this.fLeftChild != null) {
            this.fLeftChild = this.fLeftChild.flattenVariables();
            this.fLeftChild.fParent = this;
        }
        if (this.fRightChild != null) {
            this.fRightChild = this.fRightChild.flattenVariables();
            this.fRightChild.fParent = this;
        }
        return this;
    }
    
    void flattenSets() {
        Assert.assrt(this.fType != 0);
        if (this.fLeftChild != null) {
            if (this.fLeftChild.fType == 0) {
                final RBBINode setRefNode = this.fLeftChild;
                final RBBINode usetNode = setRefNode.fLeftChild;
                final RBBINode replTree = usetNode.fLeftChild;
                this.fLeftChild = replTree.cloneTree();
                this.fLeftChild.fParent = this;
            }
            else {
                this.fLeftChild.flattenSets();
            }
        }
        if (this.fRightChild != null) {
            if (this.fRightChild.fType == 0) {
                final RBBINode setRefNode = this.fRightChild;
                final RBBINode usetNode = setRefNode.fLeftChild;
                final RBBINode replTree = usetNode.fLeftChild;
                this.fRightChild = replTree.cloneTree();
                this.fRightChild.fParent = this;
            }
            else {
                this.fRightChild.flattenSets();
            }
        }
    }
    
    void findNodes(final List<RBBINode> dest, final int kind) {
        if (this.fType == kind) {
            dest.add(this);
        }
        if (this.fLeftChild != null) {
            this.fLeftChild.findNodes(dest, kind);
        }
        if (this.fRightChild != null) {
            this.fRightChild.findNodes(dest, kind);
        }
    }
    
    static void printNode(final RBBINode n) {
        if (n == null) {
            System.out.print(" -- null --\n");
        }
        else {
            printInt(n.fSerialNum, 10);
            printString(RBBINode.nodeTypeNames[n.fType], 11);
            printInt((n.fParent == null) ? 0 : n.fParent.fSerialNum, 11);
            printInt((n.fLeftChild == null) ? 0 : n.fLeftChild.fSerialNum, 11);
            printInt((n.fRightChild == null) ? 0 : n.fRightChild.fSerialNum, 12);
            printInt(n.fFirstPos, 12);
            printInt(n.fVal, 7);
            if (n.fType == 2) {
                System.out.print(" " + n.fText);
            }
        }
        System.out.println("");
    }
    
    static void printString(final String s, final int minWidth) {
        for (int i = minWidth; i < 0; ++i) {
            System.out.print(' ');
        }
        for (int i = s.length(); i < minWidth; ++i) {
            System.out.print(' ');
        }
        System.out.print(s);
    }
    
    static void printInt(final int i, final int minWidth) {
        final String s = Integer.toString(i);
        printString(s, Math.max(minWidth, s.length() + 1));
    }
    
    static void printHex(final int i, final int minWidth) {
        String s = Integer.toString(i, 16);
        final String leadingZeroes = "00000".substring(0, Math.max(0, 5 - s.length()));
        s = leadingZeroes + s;
        printString(s, minWidth);
    }
    
    void printTree(final boolean printHeading) {
        if (printHeading) {
            System.out.println("-------------------------------------------------------------------");
            System.out.println("    Serial       type     Parent  LeftChild  RightChild    position  value");
        }
        printNode(this);
        if (this.fType != 2) {
            if (this.fLeftChild != null) {
                this.fLeftChild.printTree(false);
            }
            if (this.fRightChild != null) {
                this.fRightChild.printTree(false);
            }
        }
    }
    
    static {
        nodeTypeNames = new String[] { "setRef", "uset", "varRef", "leafChar", "lookAhead", "tag", "endMark", "opStart", "opCat", "opOr", "opStar", "opPlus", "opQuestion", "opBreak", "opReverse", "opLParen" };
    }
}
