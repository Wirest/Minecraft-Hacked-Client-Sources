package net.optifine.shaders;

import java.util.ArrayDeque;
import java.util.Deque;

public class ProgramStack
{
    private Deque<Program> stack = new ArrayDeque();

    public void push(Program p)
    {
        this.stack.addLast(p);
    }

    public Program pop()
    {
        if (this.stack.isEmpty())
        {
            return Shaders.ProgramNone;
        }
        else
        {
            Program program = this.stack.pollLast();
            return program;
        }
    }
}
