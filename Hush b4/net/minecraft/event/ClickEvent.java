// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent
{
    private final Action action;
    private final String value;
    
    public ClickEvent(final Action theAction, final String theValue) {
        this.action = theAction;
        this.value = theValue;
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public String getValue() {
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
        final ClickEvent clickevent = (ClickEvent)p_equals_1_;
        if (this.action != clickevent.action) {
            return false;
        }
        if (this.value != null) {
            if (!this.value.equals(clickevent.value)) {
                return false;
            }
        }
        else if (clickevent.value != null) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "ClickEvent{action=" + this.action + ", value='" + this.value + '\'' + '}';
    }
    
    @Override
    public int hashCode() {
        int i = this.action.hashCode();
        i = 31 * i + ((this.value != null) ? this.value.hashCode() : 0);
        return i;
    }
    
    public enum Action
    {
        OPEN_URL("OPEN_URL", 0, "open_url", true), 
        OPEN_FILE("OPEN_FILE", 1, "open_file", false), 
        RUN_COMMAND("RUN_COMMAND", 2, "run_command", true), 
        TWITCH_USER_INFO("TWITCH_USER_INFO", 3, "twitch_user_info", false), 
        SUGGEST_COMMAND("SUGGEST_COMMAND", 4, "suggest_command", true), 
        CHANGE_PAGE("CHANGE_PAGE", 5, "change_page", true);
        
        private static final Map<String, Action> nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;
        
        static {
            nameMapping = Maps.newHashMap();
            Action[] values;
            for (int length = (values = values()).length, i = 0; i < length; ++i) {
                final Action clickevent$action = values[i];
                Action.nameMapping.put(clickevent$action.getCanonicalName(), clickevent$action);
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
