package net.minecraft.event;

import com.google.common.collect.Maps;

import java.util.Map;

public class ClickEvent {
    private final ClickEvent.Action action;
    private final String value;
    private static final String __OBFID = "CL_00001260";

    public ClickEvent(ClickEvent.Action p_i45156_1_, String p_i45156_2_) {
        action = p_i45156_1_;
        value = p_i45156_2_;
    }

    /**
     * Gets the action to perform when this event is raised.
     */
    public ClickEvent.Action getAction() {
        return action;
    }

    /**
     * Gets the value to perform the action on when this event is raised. For
     * example, if the action is "open URL", this would be the URL to open.
     */
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        } else if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            ClickEvent var2 = (ClickEvent) p_equals_1_;

            if (action != var2.action) {
                return false;
            } else {
                if (value != null) {
                    if (!value.equals(var2.value)) {
                        return false;
                    }
                } else if (var2.value != null) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "ClickEvent{action=" + action + ", value=\'" + value + '\'' + '}';
    }

    @Override
    public int hashCode() {
        int var1 = action.hashCode();
        var1 = 31 * var1 + (value != null ? value.hashCode() : 0);
        return var1;
    }

    public static enum Action {
        OPEN_URL("OPEN_URL", 0, "open_url", true), OPEN_FILE("OPEN_FILE", 1, "open_file", false), RUN_COMMAND("RUN_COMMAND", 2, "run_command", true), TWITCH_USER_INFO("TWITCH_USER_INFO", 3, "twitch_user_info", false), SUGGEST_COMMAND("SUGGEST_COMMAND", 4, "suggest_command", true), CHANGE_PAGE("CHANGE_PAGE", 5, "change_page", true);
        private static final Map nameMapping = Maps.newHashMap();
        private final boolean allowedInChat;
        private final String canonicalName;

        private static final ClickEvent.Action[] $VALUES = new ClickEvent.Action[]{OPEN_URL, OPEN_FILE, RUN_COMMAND, TWITCH_USER_INFO, SUGGEST_COMMAND, CHANGE_PAGE};
        private static final String __OBFID = "CL_00001261";

        private Action(String p_i45155_1_, int p_i45155_2_, String p_i45155_3_, boolean p_i45155_4_) {
            canonicalName = p_i45155_3_;
            allowedInChat = p_i45155_4_;
        }

        public boolean shouldAllowInChat() {
            return allowedInChat;
        }

        public String getCanonicalName() {
            return canonicalName;
        }

        public static ClickEvent.Action getValueByCanonicalName(String p_150672_0_) {
            return (ClickEvent.Action) Action.nameMapping.get(p_150672_0_);
        }

        static {
            ClickEvent.Action[] var0 = Action.values();
            int var1 = var0.length;

            for (int var2 = 0; var2 < var1; ++var2) {
                ClickEvent.Action var3 = var0[var2];
                Action.nameMapping.put(var3.getCanonicalName(), var3);
            }
        }
    }
}
