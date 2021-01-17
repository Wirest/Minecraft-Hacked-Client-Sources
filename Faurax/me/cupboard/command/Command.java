package me.cupboard.command;

import me.cupboard.command.argument.factory.*;
import java.util.*;

public class Command extends ArgumentFactory
{
    private final List<String> list;
    private String handle;
    private String[] handles;
    
    public Command(final String handle, final String... handles) {
        this.list = new ArrayList<String>();
        this.handle = handle;
        this.handles = handles;
        this.load();
    }
    
    public List<String> getHandles() {
        if (this.list.isEmpty()) {
            this.list.add(this.handle);
            Collections.addAll(this.list, this.handles);
        }
        return this.list;
    }
}
