package me.onlyeli.ice.events;

import java.lang.reflect.InvocationTargetException;

import me.onlyeli.eventapi.EventManager;
import me.onlyeli.ice.utils.FlexibleArray;

public interface Event2 {
    public enum State
    {
        PRE("PRE", 0), 
        POST("POST", 1);
        
        private State(final String s, final int n) {
        }
    }
}
