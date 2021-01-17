// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

class StateStack
{
    private int[] state_stack;
    private int stack_pos;
    
    public int getState() {
        return this.state_stack[this.stack_pos];
    }
    
    public void pushState(final int new_state) {
        final int pos = ++this.stack_pos;
        if (pos == this.state_stack.length) {
            this.growState();
        }
        this.state_stack[pos] = new_state;
    }
    
    public int popState() {
        return this.state_stack[this.stack_pos--];
    }
    
    public void growState() {
        final int[] new_state_stack = new int[this.state_stack.length + 1];
        System.arraycopy(this.state_stack, 0, new_state_stack, 0, this.state_stack.length);
        this.state_stack = new_state_stack;
    }
    
    StateStack(final int initial_value) {
        this.state_stack = new int[1];
        this.stack_pos = 0;
        this.state_stack[this.stack_pos] = initial_value;
    }
}
