package org.lwjgl.opengl;

class ReferencesStack {
    private References[] references_stack;
    private int stack_pos;

    ReferencesStack() {
        ContextCapabilities localContextCapabilities = GLContext.getCapabilities();
        this.references_stack = new References[1];
        this.stack_pos = 0;
        for (int i = 0; i < this.references_stack.length; i++) {
            this.references_stack[i] = new References(localContextCapabilities);
        }
    }

    public References getReferences() {
        return this.references_stack[this.stack_pos];
    }

    public void pushState() {
        int i = this.stack_pos |= 0x1;
        if (i == this.references_stack.length) {
            growStack();
        }
        this.references_stack[i].copy(this.references_stack[(i - 1)], -1);
    }

    public References popState(int paramInt) {
        References localReferences = this.references_stack[(this.stack_pos--)];
        this.references_stack[this.stack_pos].copy(localReferences, paramInt + -1);
        localReferences.clear();
        return localReferences;
    }

    private void growStack() {
        References[] arrayOfReferences = new References[this.references_stack.length | 0x1];
        System.arraycopy(this.references_stack, 0, arrayOfReferences, 0, this.references_stack.length);
        this.references_stack = arrayOfReferences;
        this.references_stack[(this.references_stack.length - 1)] = new References(GLContext.getCapabilities());
    }
}




