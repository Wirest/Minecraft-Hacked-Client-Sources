package cn.kody.debug.utils;

import net.minecraft.util.IChatComponent;

public class NewChatUtil
{
    private int updateCounterCreated;
    private IChatComponent lineString;
    private int chatLineID;
    
    public NewChatUtil(int p_i1826_1_, IChatComponent p_i1826_2_, int p_i1826_3_) {
        super();
        this.lineString = p_i1826_2_;
        this.updateCounterCreated = p_i1826_1_;
        this.chatLineID = p_i1826_3_;
    }
    
    public IChatComponent getChatComponent() {
        return this.lineString;
    }
    
    public int getUpdatedCounter() {
        return this.updateCounterCreated;
    }
    
    public int getChatLineID() {
        return this.chatLineID;
    }
}
