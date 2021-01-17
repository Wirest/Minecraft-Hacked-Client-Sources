// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.util.IChatComponent;

public class HoverEvent
{
    private final Action action;
    private final IChatComponent value;
    
    public HoverEvent(final Action actionIn, final IChatComponent valueIn) {
        this.action = actionIn;
        this.value = valueIn;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public IChatComponent getValue() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ == null || this.getClass() != p_equals_1_.getClass()) {
            return false;
        }
        final HoverEvent hoverevent = (HoverEvent)p_equals_1_;
        if (this.action != hoverevent.action) {
            return false;
        }
        if (this.value != null) {
            if (!this.value.equals(hoverevent.value)) {
                return false;
            }
        }
        else if (hoverevent.value != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "HoverEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
    }
    
    @Override
    public int hashCode() {
        int i = this.action.hashCode();
        i = 31 * i + ((this.value != null) ? this.value.hashCode() : 0);
        return i;
    }
    
    public enum Action
    {
        SHOW_TEXT("SHOW_TEXT", 0, "show_text", true), 
        SHOW_ACHIEVEMENT("SHOW_ACHIEVEMENT", 1, "show_achievement", true), 
        SHOW_ITEM("SHOW_ITEM", 2, "show_item", true), 
        SHOW_ENTITY("SHOW_ENTITY", 3, "show_entity", true);
        
        private static final Map<String, Action> nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;
        
        static {
            nameMapping = Maps.newHashMap();
            Action[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Action hoverevent$action = values[i];
                Action.nameMapping.put(hoverevent$action.getCanonicalName(), hoverevent$action);
            }
        }
        
        private Action(final String name, final int ordinal, final String canonicalNameIn, final boolean allowedInChatIn) {
            this.canonicalName = canonicalNameIn;
            this.allowedInChat = allowedInChatIn;
        }
        
        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }
        
        public String getCanonicalName() {
            return this.canonicalName;
        }
        
        public static Action getValueByCanonicalName(final String canonicalNameIn) {
            return Action.nameMapping.get(canonicalNameIn);
        }
    }
}
