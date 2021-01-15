package me.onlyeli.ice.values;

import me.onlyeli.ice.modules.*;
import me.onlyeli.ice.Module;
import me.onlyeli.ice.main.Ice;

public class ModeValue extends Value
{
    private String value;
    private String[] values;
    
    public ModeValue(final String name, final String commandName, final String value, final String[] values, final Module module) {
        super(name, commandName, values, module);
        this.value = value;
        this.values = values;
    }
    
    public String getStringValue() {
        return this.value;
    }
    
    public void setStringValue(final String value) {
        this.value = value;
        //Ice.getFileManager().getFileByName("valuesconfiguration").saveFile();
    }
    
    public String[] getStringValues() {
        return this.values;
    }
}
