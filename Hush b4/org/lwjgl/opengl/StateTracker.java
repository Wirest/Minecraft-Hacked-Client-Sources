// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.IntBuffer;

final class StateTracker
{
    private ReferencesStack references_stack;
    private final StateStack attrib_stack;
    private boolean insideBeginEnd;
    private final FastIntMap<VAOState> vaoMap;
    
    StateTracker() {
        this.vaoMap = new FastIntMap<VAOState>();
        this.attrib_stack = new StateStack(0);
    }
    
    void init() {
        this.references_stack = new ReferencesStack();
    }
    
    static void setBeginEnd(final ContextCapabilities caps, final boolean inside) {
        caps.tracker.insideBeginEnd = inside;
    }
    
    boolean isBeginEnd() {
        return this.insideBeginEnd;
    }
    
    static void popAttrib(final ContextCapabilities caps) {
        caps.tracker.doPopAttrib();
    }
    
    private void doPopAttrib() {
        this.references_stack.popState(this.attrib_stack.popState());
    }
    
    static void pushAttrib(final ContextCapabilities caps, final int mask) {
        caps.tracker.doPushAttrib(mask);
    }
    
    private void doPushAttrib(final int mask) {
        this.attrib_stack.pushState(mask);
        this.references_stack.pushState();
    }
    
    static References getReferences(final ContextCapabilities caps) {
        return caps.tracker.references_stack.getReferences();
    }
    
    static void bindBuffer(final ContextCapabilities caps, final int target, final int buffer) {
        final BaseReferences references = getReferences(caps);
        switch (target) {
            case 34962: {
                references.arrayBuffer = buffer;
                break;
            }
            case 34963: {
                if (references.vertexArrayObject != 0) {
                    caps.tracker.vaoMap.get(references.vertexArrayObject).elementArrayBuffer = buffer;
                    break;
                }
                references.elementArrayBuffer = buffer;
                break;
            }
            case 35051: {
                references.pixelPackBuffer = buffer;
                break;
            }
            case 35052: {
                references.pixelUnpackBuffer = buffer;
                break;
            }
            case 36671: {
                references.indirectBuffer = buffer;
                break;
            }
        }
    }
    
    static void bindVAO(final ContextCapabilities caps, final int array) {
        final FastIntMap<VAOState> vaoMap = caps.tracker.vaoMap;
        if (!vaoMap.containsKey(array)) {
            vaoMap.put(array, new VAOState());
        }
        getReferences(caps).vertexArrayObject = array;
    }
    
    static void deleteVAO(final ContextCapabilities caps, final IntBuffer arrays) {
        for (int i = arrays.position(); i < arrays.limit(); ++i) {
            deleteVAO(caps, arrays.get(i));
        }
    }
    
    static void deleteVAO(final ContextCapabilities caps, final int array) {
        caps.tracker.vaoMap.remove(array);
        final BaseReferences references = getReferences(caps);
        if (references.vertexArrayObject == array) {
            references.vertexArrayObject = 0;
        }
    }
    
    static int getElementArrayBufferBound(final ContextCapabilities caps) {
        final BaseReferences references = getReferences(caps);
        if (references.vertexArrayObject == 0) {
            return references.elementArrayBuffer;
        }
        return caps.tracker.vaoMap.get(references.vertexArrayObject).elementArrayBuffer;
    }
    
    private static class VAOState
    {
        int elementArrayBuffer;
    }
}
