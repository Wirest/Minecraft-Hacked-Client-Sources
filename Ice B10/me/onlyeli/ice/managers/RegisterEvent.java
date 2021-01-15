package me.onlyeli.ice.managers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import me.onlyeli.eventapi.events.Event;

@Retention(RetentionPolicy.RUNTIME)
public @interface RegisterEvent {
	public Class<? extends Event>[]events();
}
