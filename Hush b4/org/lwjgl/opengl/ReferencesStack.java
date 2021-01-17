// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

class ReferencesStack
{
    private References[] references_stack;
    private int stack_pos;
    
    public References getReferences() {
        return this.references_stack[this.stack_pos];
    }
    
    public void pushState() {
        final int pos = ++this.stack_pos;
        if (pos == this.references_stack.length) {
            this.growStack();
        }
        this.references_stack[pos].copy(this.references_stack[pos - 1], -1);
    }
    
    public References popState(final int mask) {
        final References result = this.references_stack[this.stack_pos--];
        this.references_stack[this.stack_pos].copy(result, ~mask);
        result.clear();
        return result;
    }
    
    private void growStack() {
        final References[] new_references_stack = new References[this.references_stack.length + 1];
        System.arraycopy(this.references_stack, 0, new_references_stack, 0, this.references_stack.length);
        (this.references_stack = new_references_stack)[this.references_stack.length - 1] = new References(GLContext.getCapabilities());
    }
    
    ReferencesStack() {
        final ContextCapabilities caps = GLContext.getCapabilities();
        this.references_stack = new References[1];
        this.stack_pos = 0;
        for (int i = 0; i < this.references_stack.length; ++i) {
            this.references_stack[i] = new References(caps);
        }
    }
}
