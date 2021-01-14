package org.lwjgl.opengl;

class StateStack {
    private int[] state_stack = new int[1];
    private int stack_pos = 0;

    StateStack(int paramInt) {
        this.state_stack[this.stack_pos] = paramInt;
    }

    public int getState() {
        return this.state_stack[this.stack_pos];
    }

    public void pushState(int paramInt) {
        int i = this.stack_pos |= 0x1;
        if (i == this.state_stack.length) {
            growState();
        }
        this.state_stack[i] = paramInt;
    }

    public int popState() {
        return this.state_stack[(this.stack_pos--)];
    }

    public void growState() {
        int[] arrayOfInt = new int[this.state_stack.length | 0x1];
        System.arraycopy(this.state_stack, 0, arrayOfInt, 0, this.state_stack.length);
        this.state_stack = arrayOfInt;
    }
}




