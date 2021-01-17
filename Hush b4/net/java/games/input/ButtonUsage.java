// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.util.HashMap;
import java.util.Map;

final class ButtonUsage implements Usage
{
    private static final Map map;
    private final int button_id;
    
    public static final ButtonUsage map(final int button_id) {
        final Integer button_id_obj = new Integer(button_id);
        final ButtonUsage existing = ButtonUsage.map.get(button_id_obj);
        if (existing != null) {
            return existing;
        }
        final ButtonUsage new_button = new ButtonUsage(button_id);
        ButtonUsage.map.put(button_id_obj, new_button);
        return new_button;
    }
    
    private ButtonUsage(final int button_id) {
        this.button_id = button_id;
    }
    
    public final Component.Identifier.Button getIdentifier() {
        switch (this.button_id) {
            case 1: {
                return Component.Identifier.Button._0;
            }
            case 2: {
                return Component.Identifier.Button._1;
            }
            case 3: {
                return Component.Identifier.Button._2;
            }
            case 4: {
                return Component.Identifier.Button._3;
            }
            case 5: {
                return Component.Identifier.Button._4;
            }
            case 6: {
                return Component.Identifier.Button._5;
            }
            case 7: {
                return Component.Identifier.Button._6;
            }
            case 8: {
                return Component.Identifier.Button._7;
            }
            case 9: {
                return Component.Identifier.Button._8;
            }
            case 10: {
                return Component.Identifier.Button._9;
            }
            case 11: {
                return Component.Identifier.Button._10;
            }
            case 12: {
                return Component.Identifier.Button._11;
            }
            case 13: {
                return Component.Identifier.Button._12;
            }
            case 14: {
                return Component.Identifier.Button._13;
            }
            case 15: {
                return Component.Identifier.Button._14;
            }
            case 16: {
                return Component.Identifier.Button._15;
            }
            case 17: {
                return Component.Identifier.Button._16;
            }
            case 18: {
                return Component.Identifier.Button._17;
            }
            case 19: {
                return Component.Identifier.Button._18;
            }
            case 20: {
                return Component.Identifier.Button._19;
            }
            case 21: {
                return Component.Identifier.Button._20;
            }
            case 22: {
                return Component.Identifier.Button._21;
            }
            case 23: {
                return Component.Identifier.Button._22;
            }
            case 24: {
                return Component.Identifier.Button._23;
            }
            case 25: {
                return Component.Identifier.Button._24;
            }
            case 26: {
                return Component.Identifier.Button._25;
            }
            case 27: {
                return Component.Identifier.Button._26;
            }
            case 28: {
                return Component.Identifier.Button._27;
            }
            case 29: {
                return Component.Identifier.Button._28;
            }
            case 30: {
                return Component.Identifier.Button._29;
            }
            case 31: {
                return Component.Identifier.Button._30;
            }
            case 32: {
                return Component.Identifier.Button._31;
            }
            default: {
                return null;
            }
        }
    }
    
    public final String toString() {
        return "ButtonUsage (" + this.button_id + ")";
    }
    
    static {
        map = new HashMap();
    }
}
