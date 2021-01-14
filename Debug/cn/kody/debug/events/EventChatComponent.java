package cn.kody.debug.events;

import java.util.List;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

import cn.kody.debug.utils.NewChatUtil;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.util.IChatComponent;

public class EventChatComponent extends EventCancellable
{
    private IChatComponent component;
    private List<ChatLine> chatLines;
    
    public EventChatComponent(IChatComponent p_i1496_1_, List<ChatLine> p_i1496_2_) {
        this.component = p_i1496_1_;
        this.chatLines = p_i1496_2_;
    }
    
    public IChatComponent getComponent() {
        return this.component;
    }
    
    public void setComponent(IChatComponent p_setComponent_1_) {
        this.component = p_setComponent_1_;
    }
    
    public List<ChatLine> getChatLines() {
        return this.chatLines;
    }
}
