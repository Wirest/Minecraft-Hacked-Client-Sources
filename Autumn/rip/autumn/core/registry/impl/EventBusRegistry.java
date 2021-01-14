package rip.autumn.core.registry.impl;

import me.zane.basicbus.api.bus.Bus;
import me.zane.basicbus.api.bus.impl.EventBusImpl;
import me.zane.basicbus.api.invocation.impl.ReflectionInvoker;
import rip.autumn.core.registry.Registry;

public final class EventBusRegistry implements Registry {
   public final Bus eventBus = new EventBusImpl(new ReflectionInvoker());
}
