// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.ListIterator;
import java.util.LinkedList;
import java.util.List;
import com.ibm.icu.lang.UCharacter;
import java.util.Iterator;

public class TextTrieMap<V>
{
    private Node _root;
    boolean _ignoreCase;
    
    public TextTrieMap(final boolean ignoreCase) {
        this._root = new Node();
        this._ignoreCase = ignoreCase;
    }
    
    public TextTrieMap<V> put(final CharSequence text, final V val) {
        final CharIterator chitr = new CharIterator(text, 0, this._ignoreCase);
        this._root.add(chitr, val);
        return this;
    }
    
    public Iterator<V> get(final String text) {
        return this.get(text, 0);
    }
    
    public Iterator<V> get(final CharSequence text, final int start) {
        return this.get(text, start, null);
    }
    
    public Iterator<V> get(final CharSequence text, final int start, final int[] matchLen) {
        final LongestMatchHandler<V> handler = new LongestMatchHandler<V>();
        this.find(text, start, handler);
        if (matchLen != null && matchLen.length > 0) {
            matchLen[0] = handler.getMatchLength();
        }
        return handler.getMatches();
    }
    
    public void find(final CharSequence text, final ResultHandler<V> handler) {
        this.find(text, 0, handler);
    }
    
    public void find(final CharSequence text, final int offset, final ResultHandler<V> handler) {
        final CharIterator chitr = new CharIterator(text, offset, this._ignoreCase);
        this.find(this._root, chitr, handler);
    }
    
    private synchronized void find(final Node node, final CharIterator chitr, final ResultHandler<V> handler) {
        final Iterator<V> values = node.values();
        if (values != null && !handler.handlePrefixMatch(chitr.processedLength(), values)) {
            return;
        }
        final Node nextMatch = node.findMatch(chitr);
        if (nextMatch != null) {
            this.find(nextMatch, chitr, handler);
        }
    }
    
    private static char[] toCharArray(final CharSequence text) {
        final char[] array = new char[text.length()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = text.charAt(i);
        }
        return array;
    }
    
    private static char[] subArray(final char[] array, final int start) {
        if (start == 0) {
            return array;
        }
        final char[] sub = new char[array.length - start];
        System.arraycopy(array, start, sub, 0, sub.length);
        return sub;
    }
    
    private static char[] subArray(final char[] array, final int start, final int limit) {
        if (start == 0 && limit == array.length) {
            return array;
        }
        final char[] sub = new char[limit - start];
        System.arraycopy(array, start, sub, 0, limit - start);
        return sub;
    }
    
    public static class CharIterator implements Iterator<Character>
    {
        private boolean _ignoreCase;
        private CharSequence _text;
        private int _nextIdx;
        private int _startIdx;
        private Character _remainingChar;
        
        CharIterator(final CharSequence text, final int offset, final boolean ignoreCase) {
            this._text = text;
            this._startIdx = offset;
            this._nextIdx = offset;
            this._ignoreCase = ignoreCase;
        }
        
        public boolean hasNext() {
            return this._nextIdx != this._text.length() || this._remainingChar != null;
        }
        
        public Character next() {
            if (this._nextIdx == this._text.length() && this._remainingChar == null) {
                return null;
            }
            Character next;
            if (this._remainingChar != null) {
                next = this._remainingChar;
                this._remainingChar = null;
            }
            else if (this._ignoreCase) {
                final int cp = UCharacter.foldCase(Character.codePointAt(this._text, this._nextIdx), true);
                this._nextIdx += Character.charCount(cp);
                final char[] chars = Character.toChars(cp);
                next = chars[0];
                if (chars.length == 2) {
                    this._remainingChar = chars[1];
                }
            }
            else {
                next = this._text.charAt(this._nextIdx);
                ++this._nextIdx;
            }
            return next;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("remove() not supproted");
        }
        
        public int nextIndex() {
            return this._nextIdx;
        }
        
        public int processedLength() {
            if (this._remainingChar != null) {
                throw new IllegalStateException("In the middle of surrogate pair");
            }
            return this._nextIdx - this._startIdx;
        }
    }
    
    private static class LongestMatchHandler<V> implements ResultHandler<V>
    {
        private Iterator<V> matches;
        private int length;
        
        private LongestMatchHandler() {
            this.matches = null;
            this.length = 0;
        }
        
        public boolean handlePrefixMatch(final int matchLength, final Iterator<V> values) {
            if (matchLength > this.length) {
                this.length = matchLength;
                this.matches = values;
            }
            return true;
        }
        
        public Iterator<V> getMatches() {
            return this.matches;
        }
        
        public int getMatchLength() {
            return this.length;
        }
    }
    
    private class Node
    {
        private char[] _text;
        private List<V> _values;
        private List<Node> _children;
        
        private Node() {
        }
        
        private Node(final char[] text, final List<V> values, final List<Node> children) {
            this._text = text;
            this._values = values;
            this._children = children;
        }
        
        public Iterator<V> values() {
            if (this._values == null) {
                return null;
            }
            return this._values.iterator();
        }
        
        public void add(final CharIterator chitr, final V value) {
            final StringBuilder buf = new StringBuilder();
            while (chitr.hasNext()) {
                buf.append(chitr.next());
            }
            this.add(toCharArray(buf), 0, value);
        }
        
        public Node findMatch(final CharIterator chitr) {
            if (this._children == null) {
                return null;
            }
            if (!chitr.hasNext()) {
                return null;
            }
            Node match = null;
            final Character ch = chitr.next();
            for (final Node child : this._children) {
                if (ch < child._text[0]) {
                    break;
                }
                if (ch != child._text[0]) {
                    continue;
                }
                if (child.matchFollowing(chitr)) {
                    match = child;
                    break;
                }
                break;
            }
            return match;
        }
        
        private void add(final char[] text, final int offset, final V value) {
            if (text.length == offset) {
                this._values = this.addValue(this._values, value);
                return;
            }
            if (this._children == null) {
                this._children = new LinkedList<Node>();
                final Node child = new Node(subArray(text, offset), this.addValue(null, value), null);
                this._children.add(child);
                return;
            }
            final ListIterator<Node> litr = this._children.listIterator();
            while (litr.hasNext()) {
                final Node next = litr.next();
                if (text[offset] < next._text[0]) {
                    litr.previous();
                    break;
                }
                if (text[offset] == next._text[0]) {
                    final int matchLen = next.lenMatches(text, offset);
                    if (matchLen == next._text.length) {
                        next.add(text, offset + matchLen, value);
                    }
                    else {
                        next.split(matchLen);
                        next.add(text, offset + matchLen, value);
                    }
                    return;
                }
            }
            litr.add(new Node(subArray(text, offset), this.addValue(null, value), null));
        }
        
        private boolean matchFollowing(final CharIterator chitr) {
            boolean matched = true;
            for (int idx = 1; idx < this._text.length; ++idx) {
                if (!chitr.hasNext()) {
                    matched = false;
                    break;
                }
                final Character ch = chitr.next();
                if (ch != this._text[idx]) {
                    matched = false;
                    break;
                }
            }
            return matched;
        }
        
        private int lenMatches(final char[] text, final int offset) {
            final int textLen = text.length - offset;
            int limit;
            int len;
            for (limit = ((this._text.length < textLen) ? this._text.length : textLen), len = 0; len < limit && this._text[len] == text[offset + len]; ++len) {}
            return len;
        }
        
        private void split(final int offset) {
            final char[] childText = subArray(this._text, offset);
            this._text = subArray(this._text, 0, offset);
            final Node child = new Node(childText, this._values, this._children);
            this._values = null;
            (this._children = new LinkedList<Node>()).add(child);
        }
        
        private List<V> addValue(List<V> list, final V value) {
            if (list == null) {
                list = new LinkedList<V>();
            }
            list.add(value);
            return list;
        }
    }
    
    public interface ResultHandler<V>
    {
        boolean handlePrefixMatch(final int p0, final Iterator<V> p1);
    }
}
