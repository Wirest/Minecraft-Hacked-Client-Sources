// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.Comparator;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.ArrayList;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import java.util.Set;
import com.google.common.base.Supplier;
import java.util.EnumMap;
import java.util.List;
import com.google.common.base.Stopwatch;
import java.util.Map;
import com.google.common.collect.Multiset;
import javax.annotation.concurrent.GuardedBy;
import com.google.common.collect.SetMultimap;
import java.util.Collection;
import com.google.common.collect.Collections2;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.Iterator;
import java.util.concurrent.Executor;
import com.google.common.base.Preconditions;
import java.lang.ref.WeakReference;
import com.google.common.collect.ImmutableCollection;
import java.util.logging.Level;
import com.google.common.collect.ImmutableList;
import java.util.logging.Logger;
import com.google.common.annotations.Beta;

@Beta
public final class ServiceManager
{
    private static final Logger logger;
    private static final ListenerCallQueue.Callback<Listener> HEALTHY_CALLBACK;
    private static final ListenerCallQueue.Callback<Listener> STOPPED_CALLBACK;
    private final ServiceManagerState state;
    private final ImmutableList<Service> services;
    
    public ServiceManager(final Iterable<? extends Service> services) {
        ImmutableList<Service> copy = ImmutableList.copyOf(services);
        if (copy.isEmpty()) {
            ServiceManager.logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning());
            copy = (ImmutableList<Service>)ImmutableList.of(new NoOpService());
        }
        this.state = new ServiceManagerState(copy);
        this.services = copy;
        final WeakReference<ServiceManagerState> stateReference = new WeakReference<ServiceManagerState>(this.state);
        final Executor sameThreadExecutor = MoreExecutors.sameThreadExecutor();
        for (final Service service : copy) {
            service.addListener(new ServiceListener(service, stateReference), sameThreadExecutor);
            Preconditions.checkArgument(service.state() == Service.State.NEW, "Can only manage NEW services, %s", service);
        }
        this.state.markReady();
    }
    
    public void addListener(final Listener listener, final Executor executor) {
        this.state.addListener(listener, executor);
    }
    
    public void addListener(final Listener listener) {
        this.state.addListener(listener, MoreExecutors.sameThreadExecutor());
    }
    
    public ServiceManager startAsync() {
        for (final Service service : this.services) {
            final Service.State state = service.state();
            Preconditions.checkState(state == Service.State.NEW, "Service %s is %s, cannot start it.", service, state);
        }
        for (final Service service : this.services) {
            try {
                service.startAsync();
            }
            catch (IllegalStateException e) {
                ServiceManager.logger.log(Level.WARNING, "Unable to start Service " + service, e);
            }
        }
        return this;
    }
    
    public void awaitHealthy() {
        this.state.awaitHealthy();
    }
    
    public void awaitHealthy(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.state.awaitHealthy(timeout, unit);
    }
    
    public ServiceManager stopAsync() {
        for (final Service service : this.services) {
            service.stopAsync();
        }
        return this;
    }
    
    public void awaitStopped() {
        this.state.awaitStopped();
    }
    
    public void awaitStopped(final long timeout, final TimeUnit unit) throws TimeoutException {
        this.state.awaitStopped(timeout, unit);
    }
    
    public boolean isHealthy() {
        for (final Service service : this.services) {
            if (!service.isRunning()) {
                return false;
            }
        }
        return true;
    }
    
    public ImmutableMultimap<Service.State, Service> servicesByState() {
        return this.state.servicesByState();
    }
    
    public ImmutableMap<Service, Long> startupTimes() {
        return this.state.startupTimes();
    }
    
    @Override
    public String toString() {
        return Objects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, (Predicate<? super Service>)Predicates.not((Predicate<? super E>)Predicates.instanceOf(NoOpService.class)))).toString();
    }
    
    static {
        logger = Logger.getLogger(ServiceManager.class.getName());
        HEALTHY_CALLBACK = new ListenerCallQueue.Callback<Listener>("healthy()") {
            @Override
            void call(final Listener listener) {
                listener.healthy();
            }
        };
        STOPPED_CALLBACK = new ListenerCallQueue.Callback<Listener>("stopped()") {
            @Override
            void call(final Listener listener) {
                listener.stopped();
            }
        };
    }
    
    @Beta
    public abstract static class Listener
    {
        public void healthy() {
        }
        
        public void stopped() {
        }
        
        public void failure(final Service service) {
        }
    }
    
    private static final class ServiceManagerState
    {
        final Monitor monitor;
        @GuardedBy("monitor")
        final SetMultimap<Service.State, Service> servicesByState;
        @GuardedBy("monitor")
        final Multiset<Service.State> states;
        @GuardedBy("monitor")
        final Map<Service, Stopwatch> startupTimers;
        @GuardedBy("monitor")
        boolean ready;
        @GuardedBy("monitor")
        boolean transitioned;
        final int numberOfServices;
        final Monitor.Guard awaitHealthGuard;
        final Monitor.Guard stoppedGuard;
        @GuardedBy("monitor")
        final List<ListenerCallQueue<Listener>> listeners;
        
        ServiceManagerState(final ImmutableCollection<Service> services) {
            this.monitor = new Monitor();
            this.servicesByState = Multimaps.newSetMultimap(new EnumMap<Service.State, Collection<Service>>(Service.State.class), new Supplier<Set<Service>>() {
                @Override
                public Set<Service> get() {
                    return (Set<Service>)Sets.newLinkedHashSet();
                }
            });
            this.states = this.servicesByState.keys();
            this.startupTimers = (Map<Service, Stopwatch>)Maps.newIdentityHashMap();
            this.awaitHealthGuard = new Monitor.Guard(this.monitor) {
                @Override
                public boolean isSatisfied() {
                    return ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManagerState.this.numberOfServices || ServiceManagerState.this.states.contains(Service.State.STOPPING) || ServiceManagerState.this.states.contains(Service.State.TERMINATED) || ServiceManagerState.this.states.contains(Service.State.FAILED);
                }
            };
            this.stoppedGuard = new Monitor.Guard(this.monitor) {
                @Override
                public boolean isSatisfied() {
                    return ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManagerState.this.numberOfServices;
                }
            };
            this.listeners = Collections.synchronizedList(new ArrayList<ListenerCallQueue<Listener>>());
            this.numberOfServices = services.size();
            this.servicesByState.putAll(Service.State.NEW, (Iterable<?>)services);
            for (final Service service : services) {
                this.startupTimers.put(service, Stopwatch.createUnstarted());
            }
        }
        
        void markReady() {
            this.monitor.enter();
            try {
                if (this.transitioned) {
                    final List<Service> servicesInBadStates = (List<Service>)Lists.newArrayList();
                    for (final Service service : this.servicesByState().values()) {
                        if (service.state() != Service.State.NEW) {
                            servicesInBadStates.add(service);
                        }
                    }
                    throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
                }
                this.ready = true;
            }
            finally {
                this.monitor.leave();
            }
        }
        
        void addListener(final Listener listener, final Executor executor) {
            Preconditions.checkNotNull(listener, (Object)"listener");
            Preconditions.checkNotNull(executor, (Object)"executor");
            this.monitor.enter();
            try {
                if (!this.stoppedGuard.isSatisfied()) {
                    this.listeners.add(new ListenerCallQueue<Listener>(listener, executor));
                }
            }
            finally {
                this.monitor.leave();
            }
        }
        
        void awaitHealthy() {
            this.monitor.enterWhenUninterruptibly(this.awaitHealthGuard);
            try {
                this.checkHealthy();
            }
            finally {
                this.monitor.leave();
            }
        }
        
        void awaitHealthy(final long timeout, final TimeUnit unit) throws TimeoutException {
            this.monitor.enter();
            try {
                if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit)) {
                    throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(this.servicesByState, Predicates.in((Collection<? extends Service.State>)ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
                }
                this.checkHealthy();
            }
            finally {
                this.monitor.leave();
            }
        }
        
        void awaitStopped() {
            this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
            this.monitor.leave();
        }
        
        void awaitStopped(final long timeout, final TimeUnit unit) throws TimeoutException {
            this.monitor.enter();
            try {
                if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit)) {
                    throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(this.servicesByState, (Predicate<? super Service.State>)Predicates.not(Predicates.in((Collection<? extends K>)ImmutableSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
                }
            }
            finally {
                this.monitor.leave();
            }
        }
        
        ImmutableMultimap<Service.State, Service> servicesByState() {
            final ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
            this.monitor.enter();
            try {
                for (final Map.Entry<Service.State, Service> entry : this.servicesByState.entries()) {
                    if (!(entry.getValue() instanceof NoOpService)) {
                        builder.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            finally {
                this.monitor.leave();
            }
            return builder.build();
        }
        
        ImmutableMap<Service, Long> startupTimes() {
            this.monitor.enter();
            List<Map.Entry<Service, Long>> loadTimes;
            try {
                loadTimes = (List<Map.Entry<Service, Long>>)Lists.newArrayListWithCapacity(this.states.size() - this.states.count(Service.State.NEW) + this.states.count(Service.State.STARTING));
                for (final Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
                    final Service service = entry.getKey();
                    final Stopwatch stopWatch = entry.getValue();
                    if (!stopWatch.isRunning() && !this.servicesByState.containsEntry(Service.State.NEW, service) && !(service instanceof NoOpService)) {
                        loadTimes.add(Maps.immutableEntry(service, stopWatch.elapsed(TimeUnit.MILLISECONDS)));
                    }
                }
            }
            finally {
                this.monitor.leave();
            }
            Collections.sort(loadTimes, Ordering.natural().onResultOf((Function<Object, ? extends Comparable>)new Function<Map.Entry<Service, Long>, Long>() {
                @Override
                public Long apply(final Map.Entry<Service, Long> input) {
                    return input.getValue();
                }
            }));
            final ImmutableMap.Builder<Service, Long> builder = ImmutableMap.builder();
            for (final Map.Entry<Service, Long> entry2 : loadTimes) {
                builder.put(entry2);
            }
            return builder.build();
        }
        
        void transitionService(final Service service, final Service.State from, final Service.State to) {
            Preconditions.checkNotNull(service);
            Preconditions.checkArgument(from != to);
            this.monitor.enter();
            try {
                this.transitioned = true;
                if (!this.ready) {
                    return;
                }
                Preconditions.checkState(this.servicesByState.remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
                Preconditions.checkState(this.servicesByState.put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
                final Stopwatch stopwatch = this.startupTimers.get(service);
                if (from == Service.State.NEW) {
                    stopwatch.start();
                }
                if (to.compareTo(Service.State.RUNNING) >= 0 && stopwatch.isRunning()) {
                    stopwatch.stop();
                    if (!(service instanceof NoOpService)) {
                        ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
                    }
                }
                if (to == Service.State.FAILED) {
                    this.fireFailedListeners(service);
                }
                if (this.states.count(Service.State.RUNNING) == this.numberOfServices) {
                    this.fireHealthyListeners();
                }
                else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
                    this.fireStoppedListeners();
                }
            }
            finally {
                this.monitor.leave();
                this.executeListeners();
            }
        }
        
        @GuardedBy("monitor")
        void fireStoppedListeners() {
            ServiceManager.STOPPED_CALLBACK.enqueueOn(this.listeners);
        }
        
        @GuardedBy("monitor")
        void fireHealthyListeners() {
            ServiceManager.HEALTHY_CALLBACK.enqueueOn(this.listeners);
        }
        
        @GuardedBy("monitor")
        void fireFailedListeners(final Service service) {
            new ListenerCallQueue.Callback<Listener>("failed({service=" + service + "})") {
                @Override
                void call(final Listener listener) {
                    listener.failure(service);
                }
            }.enqueueOn(this.listeners);
        }
        
        void executeListeners() {
            Preconditions.checkState(!this.monitor.isOccupiedByCurrentThread(), (Object)"It is incorrect to execute listeners with the monitor held.");
            for (int i = 0; i < this.listeners.size(); ++i) {
                this.listeners.get(i).execute();
            }
        }
        
        @GuardedBy("monitor")
        void checkHealthy() {
            if (this.states.count(Service.State.RUNNING) != this.numberOfServices) {
                throw new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, (Predicate<? super Service.State>)Predicates.not((Predicate<? super K>)Predicates.equalTo((T)Service.State.RUNNING))));
            }
        }
    }
    
    private static final class ServiceListener extends Service.Listener
    {
        final Service service;
        final WeakReference<ServiceManagerState> state;
        
        ServiceListener(final Service service, final WeakReference<ServiceManagerState> state) {
            this.service = service;
            this.state = state;
        }
        
        @Override
        public void starting() {
            final ServiceManagerState state = this.state.get();
            if (state != null) {
                state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
                }
            }
        }
        
        @Override
        public void running() {
            final ServiceManagerState state = this.state.get();
            if (state != null) {
                state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
            }
        }
        
        @Override
        public void stopping(final Service.State from) {
            final ServiceManagerState state = this.state.get();
            if (state != null) {
                state.transitionService(this.service, from, Service.State.STOPPING);
            }
        }
        
        @Override
        public void terminated(final Service.State from) {
            final ServiceManagerState state = this.state.get();
            if (state != null) {
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
                }
                state.transitionService(this.service, from, Service.State.TERMINATED);
            }
        }
        
        @Override
        public void failed(final Service.State from, final Throwable failure) {
            final ServiceManagerState state = this.state.get();
            if (state != null) {
                if (!(this.service instanceof NoOpService)) {
                    ServiceManager.logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
                }
                state.transitionService(this.service, from, Service.State.FAILED);
            }
        }
    }
    
    private static final class NoOpService extends AbstractService
    {
        @Override
        protected void doStart() {
            this.notifyStarted();
        }
        
        @Override
        protected void doStop() {
            this.notifyStopped();
        }
    }
    
    private static final class EmptyServiceManagerWarning extends Throwable
    {
    }
}
