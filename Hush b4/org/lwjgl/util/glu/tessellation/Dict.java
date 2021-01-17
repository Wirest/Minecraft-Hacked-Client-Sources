// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class Dict
{
    DictNode head;
    Object frame;
    DictLeq leq;
    
    private Dict() {
    }
    
    static Dict dictNewDict(final Object frame, final DictLeq leq) {
        final Dict dict = new Dict();
        dict.head = new DictNode();
        dict.head.key = null;
        dict.head.next = dict.head;
        dict.head.prev = dict.head;
        dict.frame = frame;
        dict.leq = leq;
        return dict;
    }
    
    static void dictDeleteDict(final Dict dict) {
        dict.head = null;
        dict.frame = null;
        dict.leq = null;
    }
    
    static DictNode dictInsert(final Dict dict, final Object key) {
        return dictInsertBefore(dict, dict.head, key);
    }
    
    static DictNode dictInsertBefore(final Dict dict, DictNode node, final Object key) {
        do {
            node = node.prev;
        } while (node.key != null && !dict.leq.leq(dict.frame, node.key, key));
        final DictNode newNode = new DictNode();
        newNode.key = key;
        newNode.next = node.next;
        node.next.prev = newNode;
        newNode.prev = node;
        return node.next = newNode;
    }
    
    static Object dictKey(final DictNode aNode) {
        return aNode.key;
    }
    
    static DictNode dictSucc(final DictNode aNode) {
        return aNode.next;
    }
    
    static DictNode dictPred(final DictNode aNode) {
        return aNode.prev;
    }
    
    static DictNode dictMin(final Dict aDict) {
        return aDict.head.next;
    }
    
    static DictNode dictMax(final Dict aDict) {
        return aDict.head.prev;
    }
    
    static void dictDelete(final Dict dict, final DictNode node) {
        node.next.prev = node.prev;
        node.prev.next = node.next;
    }
    
    static DictNode dictSearch(final Dict dict, final Object key) {
        DictNode node = dict.head;
        do {
            node = node.next;
        } while (node.key != null && !dict.leq.leq(dict.frame, key, node.key));
        return node;
    }
    
    public interface DictLeq
    {
        boolean leq(final Object p0, final Object p1, final Object p2);
    }
}
