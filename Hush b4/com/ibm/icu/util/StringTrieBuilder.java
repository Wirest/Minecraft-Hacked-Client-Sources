// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StringTrieBuilder
{
    private State state;
    @Deprecated
    protected StringBuilder strings;
    private Node root;
    private HashMap<Node, Node> nodes;
    private ValueNode lookupFinalValueNode;
    
    @Deprecated
    protected StringTrieBuilder() {
        this.state = State.ADDING;
        this.strings = new StringBuilder();
        this.nodes = new HashMap<Node, Node>();
        this.lookupFinalValueNode = new ValueNode();
    }
    
    @Deprecated
    protected void addImpl(final CharSequence s, final int value) {
        if (this.state != State.ADDING) {
            throw new IllegalStateException("Cannot add (string, value) pairs after build().");
        }
        if (s.length() > 65535) {
            throw new IndexOutOfBoundsException("The maximum string length is 0xffff.");
        }
        if (this.root == null) {
            this.root = this.createSuffixNode(s, 0, value);
        }
        else {
            this.root = this.root.add(this, s, 0, value);
        }
    }
    
    @Deprecated
    protected final void buildImpl(final Option buildOption) {
        switch (this.state) {
            case ADDING: {
                if (this.root == null) {
                    throw new IndexOutOfBoundsException("No (string, value) pairs were added.");
                }
                if (buildOption == Option.FAST) {
                    this.state = State.BUILDING_FAST;
                    break;
                }
                this.state = State.BUILDING_SMALL;
                break;
            }
            case BUILDING_FAST:
            case BUILDING_SMALL: {
                throw new IllegalStateException("Builder failed and must be clear()ed.");
            }
            case BUILT: {
                return;
            }
        }
        (this.root = this.root.register(this)).markRightEdgesFirst(-1);
        this.root.write(this);
        this.state = State.BUILT;
    }
    
    @Deprecated
    protected void clearImpl() {
        this.strings.setLength(0);
        this.nodes.clear();
        this.root = null;
        this.state = State.ADDING;
    }
    
    private final Node registerNode(final Node newNode) {
        if (this.state == State.BUILDING_FAST) {
            return newNode;
        }
        Node oldNode = this.nodes.get(newNode);
        if (oldNode != null) {
            return oldNode;
        }
        oldNode = this.nodes.put(newNode, newNode);
        assert oldNode == null;
        return newNode;
    }
    
    private final ValueNode registerFinalValue(final int value) {
        this.lookupFinalValueNode.setFinalValue(value);
        Node oldNode = this.nodes.get(this.lookupFinalValueNode);
        if (oldNode != null) {
            return (ValueNode)oldNode;
        }
        final ValueNode newNode = new ValueNode(value);
        oldNode = this.nodes.put(newNode, newNode);
        assert oldNode == null;
        return newNode;
    }
    
    private ValueNode createSuffixNode(final CharSequence s, final int start, final int sValue) {
        ValueNode node = this.registerFinalValue(sValue);
        if (start < s.length()) {
            final int offset = this.strings.length();
            this.strings.append(s, start, s.length());
            node = new LinearMatchNode(this.strings, offset, s.length() - start, node);
        }
        return node;
    }
    
    @Deprecated
    protected abstract boolean matchNodesCanHaveValues();
    
    @Deprecated
    protected abstract int getMaxBranchLinearSubNodeLength();
    
    @Deprecated
    protected abstract int getMinLinearMatch();
    
    @Deprecated
    protected abstract int getMaxLinearMatchLength();
    
    @Deprecated
    protected abstract int write(final int p0);
    
    @Deprecated
    protected abstract int write(final int p0, final int p1);
    
    @Deprecated
    protected abstract int writeValueAndFinal(final int p0, final boolean p1);
    
    @Deprecated
    protected abstract int writeValueAndType(final boolean p0, final int p1, final int p2);
    
    @Deprecated
    protected abstract int writeDeltaTo(final int p0);
    
    public enum Option
    {
        FAST, 
        SMALL;
    }
    
    private abstract static class Node
    {
        protected int offset;
        
        public Node() {
            this.offset = 0;
        }
        
        @Override
        public abstract int hashCode();
        
        @Override
        public boolean equals(final Object other) {
            return this == other || this.getClass() == other.getClass();
        }
        
        public Node add(final StringTrieBuilder builder, final CharSequence s, final int start, final int sValue) {
            return this;
        }
        
        public Node register(final StringTrieBuilder builder) {
            return this;
        }
        
        public int markRightEdgesFirst(final int edgeNumber) {
            if (this.offset == 0) {
                this.offset = edgeNumber;
            }
            return edgeNumber;
        }
        
        public abstract void write(final StringTrieBuilder p0);
        
        public final void writeUnlessInsideRightEdge(final int firstRight, final int lastRight, final StringTrieBuilder builder) {
            if (this.offset < 0 && (this.offset < lastRight || firstRight < this.offset)) {
                this.write(builder);
            }
        }
        
        public final int getOffset() {
            return this.offset;
        }
    }
    
    private static class ValueNode extends Node
    {
        protected boolean hasValue;
        protected int value;
        
        public ValueNode() {
        }
        
        public ValueNode(final int v) {
            this.hasValue = true;
            this.value = v;
        }
        
        public final void setValue(final int v) {
            assert !this.hasValue;
            this.hasValue = true;
            this.value = v;
        }
        
        private void setFinalValue(final int v) {
            this.hasValue = true;
            this.value = v;
        }
        
        @Override
        public int hashCode() {
            int hash = 1118481;
            if (this.hasValue) {
                hash = hash * 37 + this.value;
            }
            return hash;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            final ValueNode o = (ValueNode)other;
            return this.hasValue == o.hasValue && (!this.hasValue || this.value == o.value);
        }
        
        @Override
        public Node add(final StringTrieBuilder builder, final CharSequence s, final int start, final int sValue) {
            if (start == s.length()) {
                throw new IllegalArgumentException("Duplicate string.");
            }
            final ValueNode node = builder.createSuffixNode(s, start, sValue);
            node.setValue(this.value);
            return node;
        }
        
        @Override
        public void write(final StringTrieBuilder builder) {
            this.offset = builder.writeValueAndFinal(this.value, true);
        }
    }
    
    private static final class IntermediateValueNode extends ValueNode
    {
        private Node next;
        
        public IntermediateValueNode(final int v, final Node nextNode) {
            this.next = nextNode;
            this.setValue(v);
        }
        
        @Override
        public int hashCode() {
            return (82767594 + this.value) * 37 + this.next.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            final IntermediateValueNode o = (IntermediateValueNode)other;
            return this.next == o.next;
        }
        
        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                edgeNumber = (this.offset = this.next.markRightEdgesFirst(edgeNumber));
            }
            return edgeNumber;
        }
        
        @Override
        public void write(final StringTrieBuilder builder) {
            this.next.write(builder);
            this.offset = builder.writeValueAndFinal(this.value, false);
        }
    }
    
    private static final class LinearMatchNode extends ValueNode
    {
        private CharSequence strings;
        private int stringOffset;
        private int length;
        private Node next;
        private int hash;
        
        public LinearMatchNode(final CharSequence builderStrings, final int sOffset, final int len, final Node nextNode) {
            this.strings = builderStrings;
            this.stringOffset = sOffset;
            this.length = len;
            this.next = nextNode;
        }
        
        @Override
        public int hashCode() {
            return this.hash;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            final LinearMatchNode o = (LinearMatchNode)other;
            if (this.length != o.length || this.next != o.next) {
                return false;
            }
            for (int i = this.stringOffset, j = o.stringOffset, limit = this.stringOffset + this.length; i < limit; ++i, ++j) {
                if (this.strings.charAt(i) != this.strings.charAt(j)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public Node add(final StringTrieBuilder builder, final CharSequence s, int start, final int sValue) {
            if (start != s.length()) {
                for (int limit = this.stringOffset + this.length, i = this.stringOffset; i < limit; ++i, ++start) {
                    if (start == s.length()) {
                        final int prefixLength = i - this.stringOffset;
                        final LinearMatchNode suffixNode = new LinearMatchNode(this.strings, i, this.length - prefixLength, this.next);
                        suffixNode.setValue(sValue);
                        this.length = prefixLength;
                        this.next = suffixNode;
                        return this;
                    }
                    final char thisChar = this.strings.charAt(i);
                    final char newChar = s.charAt(start);
                    if (thisChar != newChar) {
                        final DynamicBranchNode branchNode = new DynamicBranchNode();
                        Node thisSuffixNode;
                        Node result;
                        if (i == this.stringOffset) {
                            if (this.hasValue) {
                                branchNode.setValue(this.value);
                                this.value = 0;
                                this.hasValue = false;
                            }
                            ++this.stringOffset;
                            --this.length;
                            thisSuffixNode = ((this.length > 0) ? this : this.next);
                            result = branchNode;
                        }
                        else if (i == limit - 1) {
                            --this.length;
                            thisSuffixNode = this.next;
                            this.next = branchNode;
                            result = this;
                        }
                        else {
                            final int prefixLength2 = i - this.stringOffset;
                            ++i;
                            thisSuffixNode = new LinearMatchNode(this.strings, i, this.length - (prefixLength2 + 1), this.next);
                            this.length = prefixLength2;
                            this.next = branchNode;
                            result = this;
                        }
                        final ValueNode newSuffixNode = builder.createSuffixNode(s, start + 1, sValue);
                        branchNode.add(thisChar, thisSuffixNode);
                        branchNode.add(newChar, newSuffixNode);
                        return result;
                    }
                }
                this.next = this.next.add(builder, s, start, sValue);
                return this;
            }
            if (this.hasValue) {
                throw new IllegalArgumentException("Duplicate string.");
            }
            this.setValue(sValue);
            return this;
        }
        
        @Override
        public Node register(final StringTrieBuilder builder) {
            this.next = this.next.register(builder);
            final int maxLinearMatchLength = builder.getMaxLinearMatchLength();
            while (this.length > maxLinearMatchLength) {
                final int nextOffset = this.stringOffset + this.length - maxLinearMatchLength;
                this.length -= maxLinearMatchLength;
                final LinearMatchNode suffixNode = new LinearMatchNode(this.strings, nextOffset, maxLinearMatchLength, this.next);
                suffixNode.setHashCode();
                this.next = builder.registerNode(suffixNode);
            }
            Node result;
            if (this.hasValue && !builder.matchNodesCanHaveValues()) {
                final int intermediateValue = this.value;
                this.value = 0;
                this.hasValue = false;
                this.setHashCode();
                result = new IntermediateValueNode(intermediateValue, builder.registerNode(this));
            }
            else {
                this.setHashCode();
                result = this;
            }
            return builder.registerNode(result);
        }
        
        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                edgeNumber = (this.offset = this.next.markRightEdgesFirst(edgeNumber));
            }
            return edgeNumber;
        }
        
        @Override
        public void write(final StringTrieBuilder builder) {
            this.next.write(builder);
            builder.write(this.stringOffset, this.length);
            this.offset = builder.writeValueAndType(this.hasValue, this.value, builder.getMinLinearMatch() + this.length - 1);
        }
        
        private void setHashCode() {
            this.hash = (124151391 + this.length) * 37 + this.next.hashCode();
            if (this.hasValue) {
                this.hash = this.hash * 37 + this.value;
            }
            for (int i = this.stringOffset, limit = this.stringOffset + this.length; i < limit; ++i) {
                this.hash = this.hash * 37 + this.strings.charAt(i);
            }
        }
    }
    
    private static final class DynamicBranchNode extends ValueNode
    {
        private StringBuilder chars;
        private ArrayList<Node> equal;
        
        public DynamicBranchNode() {
            this.chars = new StringBuilder();
            this.equal = new ArrayList<Node>();
        }
        
        public void add(final char c, final Node node) {
            final int i = this.find(c);
            this.chars.insert(i, c);
            this.equal.add(i, node);
        }
        
        @Override
        public Node add(final StringTrieBuilder builder, final CharSequence s, int start, final int sValue) {
            if (start != s.length()) {
                final char c = s.charAt(start++);
                final int i = this.find(c);
                if (i < this.chars.length() && c == this.chars.charAt(i)) {
                    this.equal.set(i, this.equal.get(i).add(builder, s, start, sValue));
                }
                else {
                    this.chars.insert(i, c);
                    this.equal.add(i, builder.createSuffixNode(s, start, sValue));
                }
                return this;
            }
            if (this.hasValue) {
                throw new IllegalArgumentException("Duplicate string.");
            }
            this.setValue(sValue);
            return this;
        }
        
        @Override
        public Node register(final StringTrieBuilder builder) {
            final Node subNode = this.register(builder, 0, this.chars.length());
            Node result;
            final BranchHeadNode head = (BranchHeadNode)(result = new BranchHeadNode(this.chars.length(), subNode));
            if (this.hasValue) {
                if (builder.matchNodesCanHaveValues()) {
                    head.setValue(this.value);
                }
                else {
                    result = new IntermediateValueNode(this.value, builder.registerNode(head));
                }
            }
            return builder.registerNode(result);
        }
        
        private Node register(final StringTrieBuilder builder, int start, final int limit) {
            final int length = limit - start;
            if (length > builder.getMaxBranchLinearSubNodeLength()) {
                final int middle = start + length / 2;
                return builder.registerNode(new SplitBranchNode(this.chars.charAt(middle), this.register(builder, start, middle), this.register(builder, middle, limit)));
            }
            final ListBranchNode listNode = new ListBranchNode(length);
            do {
                final char c = this.chars.charAt(start);
                final Node node = this.equal.get(start);
                if (node.getClass() == ValueNode.class) {
                    listNode.add(c, ((ValueNode)node).value);
                }
                else {
                    listNode.add(c, node.register(builder));
                }
            } while (++start < limit);
            return builder.registerNode(listNode);
        }
        
        private int find(final char c) {
            int start = 0;
            int limit = this.chars.length();
            while (start < limit) {
                final int i = (start + limit) / 2;
                final char middleChar = this.chars.charAt(i);
                if (c < middleChar) {
                    limit = i;
                }
                else {
                    if (c == middleChar) {
                        return i;
                    }
                    start = i + 1;
                }
            }
            return start;
        }
    }
    
    private abstract static class BranchNode extends Node
    {
        protected int hash;
        protected int firstEdgeNumber;
        
        public BranchNode() {
        }
        
        @Override
        public int hashCode() {
            return this.hash;
        }
    }
    
    private static final class ListBranchNode extends BranchNode
    {
        private Node[] equal;
        private int length;
        private int[] values;
        private char[] units;
        
        public ListBranchNode(final int capacity) {
            this.hash = 165535188 + capacity;
            this.equal = new Node[capacity];
            this.values = new int[capacity];
            this.units = new char[capacity];
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            final ListBranchNode o = (ListBranchNode)other;
            for (int i = 0; i < this.length; ++i) {
                if (this.units[i] != o.units[i] || this.values[i] != o.values[i] || this.equal[i] != o.equal[i]) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        
        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.firstEdgeNumber = edgeNumber;
                int step = 0;
                int i = this.length;
                do {
                    final Node edge = this.equal[--i];
                    if (edge != null) {
                        edgeNumber = edge.markRightEdgesFirst(edgeNumber - step);
                    }
                    step = 1;
                } while (i > 0);
                this.offset = edgeNumber;
            }
            return edgeNumber;
        }
        
        @Override
        public void write(final StringTrieBuilder builder) {
            int unitNumber = this.length - 1;
            final Node rightEdge = this.equal[unitNumber];
            final int rightEdgeNumber = (rightEdge == null) ? this.firstEdgeNumber : rightEdge.getOffset();
            do {
                --unitNumber;
                if (this.equal[unitNumber] != null) {
                    this.equal[unitNumber].writeUnlessInsideRightEdge(this.firstEdgeNumber, rightEdgeNumber, builder);
                }
            } while (unitNumber > 0);
            unitNumber = this.length - 1;
            if (rightEdge == null) {
                builder.writeValueAndFinal(this.values[unitNumber], true);
            }
            else {
                rightEdge.write(builder);
            }
            this.offset = builder.write(this.units[unitNumber]);
            while (--unitNumber >= 0) {
                int value;
                boolean isFinal;
                if (this.equal[unitNumber] == null) {
                    value = this.values[unitNumber];
                    isFinal = true;
                }
                else {
                    assert this.equal[unitNumber].getOffset() > 0;
                    value = this.offset - this.equal[unitNumber].getOffset();
                    isFinal = false;
                }
                builder.writeValueAndFinal(value, isFinal);
                this.offset = builder.write(this.units[unitNumber]);
            }
        }
        
        public void add(final int c, final int value) {
            this.units[this.length] = (char)c;
            this.equal[this.length] = null;
            this.values[this.length] = value;
            ++this.length;
            this.hash = (this.hash * 37 + c) * 37 + value;
        }
        
        public void add(final int c, final Node node) {
            this.units[this.length] = (char)c;
            this.equal[this.length] = node;
            this.values[this.length] = 0;
            ++this.length;
            this.hash = (this.hash * 37 + c) * 37 + node.hashCode();
        }
    }
    
    private static final class SplitBranchNode extends BranchNode
    {
        private char unit;
        private Node lessThan;
        private Node greaterOrEqual;
        
        public SplitBranchNode(final char middleUnit, final Node lessThanNode, final Node greaterOrEqualNode) {
            this.hash = ((206918985 + middleUnit) * 37 + lessThanNode.hashCode()) * 37 + greaterOrEqualNode.hashCode();
            this.unit = middleUnit;
            this.lessThan = lessThanNode;
            this.greaterOrEqual = greaterOrEqualNode;
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            final SplitBranchNode o = (SplitBranchNode)other;
            return this.unit == o.unit && this.lessThan == o.lessThan && this.greaterOrEqual == o.greaterOrEqual;
        }
        
        @Override
        public int hashCode() {
            return super.hashCode();
        }
        
        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                this.firstEdgeNumber = edgeNumber;
                edgeNumber = this.greaterOrEqual.markRightEdgesFirst(edgeNumber);
                edgeNumber = (this.offset = this.lessThan.markRightEdgesFirst(edgeNumber - 1));
            }
            return edgeNumber;
        }
        
        @Override
        public void write(final StringTrieBuilder builder) {
            this.lessThan.writeUnlessInsideRightEdge(this.firstEdgeNumber, this.greaterOrEqual.getOffset(), builder);
            this.greaterOrEqual.write(builder);
            assert this.lessThan.getOffset() > 0;
            builder.writeDeltaTo(this.lessThan.getOffset());
            this.offset = builder.write(this.unit);
        }
    }
    
    private static final class BranchHeadNode extends ValueNode
    {
        private int length;
        private Node next;
        
        public BranchHeadNode(final int len, final Node subNode) {
            this.length = len;
            this.next = subNode;
        }
        
        @Override
        public int hashCode() {
            return (248302782 + this.length) * 37 + this.next.hashCode();
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!super.equals(other)) {
                return false;
            }
            final BranchHeadNode o = (BranchHeadNode)other;
            return this.length == o.length && this.next == o.next;
        }
        
        @Override
        public int markRightEdgesFirst(int edgeNumber) {
            if (this.offset == 0) {
                edgeNumber = (this.offset = this.next.markRightEdgesFirst(edgeNumber));
            }
            return edgeNumber;
        }
        
        @Override
        public void write(final StringTrieBuilder builder) {
            this.next.write(builder);
            if (this.length <= builder.getMinLinearMatch()) {
                this.offset = builder.writeValueAndType(this.hasValue, this.value, this.length - 1);
            }
            else {
                builder.write(this.length - 1);
                this.offset = builder.writeValueAndType(this.hasValue, this.value, 0);
            }
        }
    }
    
    private enum State
    {
        ADDING, 
        BUILDING_FAST, 
        BUILDING_SMALL, 
        BUILT;
    }
}
