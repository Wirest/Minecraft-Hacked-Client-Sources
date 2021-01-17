// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CancellationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.common.collect.ImmutableCollection;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.base.Optional;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;
import com.google.common.collect.Queues;
import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.common.base.Function;
import java.lang.reflect.Constructor;
import com.google.common.collect.Ordering;
import com.google.common.annotations.Beta;

@Beta
public final class Futures
{
    private static final AsyncFunction<ListenableFuture<Object>, Object> DEREFERENCER;
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_FIRST;
    
    private Futures() {
    }
    
    public static <V, X extends Exception> CheckedFuture<V, X> makeChecked(final ListenableFuture<V> future, final Function<Exception, X> mapper) {
        return new MappingCheckedFuture<V, X>(Preconditions.checkNotNull(future), mapper);
    }
    
    public static <V> ListenableFuture<V> immediateFuture(@Nullable final V value) {
        return new ImmediateSuccessfulFuture<V>(value);
    }
    
    public static <V, X extends Exception> CheckedFuture<V, X> immediateCheckedFuture(@Nullable final V value) {
        return new ImmediateSuccessfulCheckedFuture<V, X>(value);
    }
    
    public static <V> ListenableFuture<V> immediateFailedFuture(final Throwable throwable) {
        Preconditions.checkNotNull(throwable);
        return new ImmediateFailedFuture<V>(throwable);
    }
    
    public static <V> ListenableFuture<V> immediateCancelledFuture() {
        return new ImmediateCancelledFuture<V>();
    }
    
    public static <V, X extends Exception> CheckedFuture<V, X> immediateFailedCheckedFuture(final X exception) {
        Preconditions.checkNotNull(exception);
        return new ImmediateFailedCheckedFuture<V, X>(exception);
    }
    
    public static <V> ListenableFuture<V> withFallback(final ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback) {
        return withFallback(input, fallback, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    public static <V> ListenableFuture<V> withFallback(final ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, final Executor executor) {
        Preconditions.checkNotNull(fallback);
        return new FallbackFuture<V>(input, fallback, executor);
    }
    
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function) {
        return transform(input, function, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final AsyncFunction<? super I, ? extends O> function, final Executor executor) {
        final ChainingListenableFuture<I, O> output = new ChainingListenableFuture<I, O>((AsyncFunction)function, (ListenableFuture)input);
        input.addListener(output, executor);
        return (ListenableFuture<O>)output;
    }
    
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final Function<? super I, ? extends O> function) {
        return transform(input, function, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    public static <I, O> ListenableFuture<O> transform(final ListenableFuture<I> input, final Function<? super I, ? extends O> function, final Executor executor) {
        Preconditions.checkNotNull(function);
        final AsyncFunction<I, O> wrapperFunction = new AsyncFunction<I, O>() {
            @Override
            public ListenableFuture<O> apply(final I input) {
                final O output = function.apply(input);
                return Futures.immediateFuture(output);
            }
        };
        return transform(input, (AsyncFunction<? super I, ? extends O>)wrapperFunction, executor);
    }
    
    public static <I, O> Future<O> lazyTransform(final Future<I> input, final Function<? super I, ? extends O> function) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(function);
        return new Future<O>() {
            @Override
            public boolean cancel(final boolean mayInterruptIfRunning) {
                return input.cancel(mayInterruptIfRunning);
            }
            
            @Override
            public boolean isCancelled() {
                return input.isCancelled();
            }
            
            @Override
            public boolean isDone() {
                return input.isDone();
            }
            
            @Override
            public O get() throws InterruptedException, ExecutionException {
                return this.applyTransformation(input.get());
            }
            
            @Override
            public O get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                return this.applyTransformation(input.get(timeout, unit));
            }
            
            private O applyTransformation(final I input) throws ExecutionException {
                try {
                    return function.apply(input);
                }
                catch (Throwable t) {
                    throw new ExecutionException(t);
                }
            }
        };
    }
    
    public static <V> ListenableFuture<V> dereference(final ListenableFuture<? extends ListenableFuture<? extends V>> nested) {
        return transform(nested, (AsyncFunction<? super ListenableFuture<? extends V>, ? extends V>)Futures.DEREFERENCER);
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(final ListenableFuture<? extends V>... futures) {
        return listFuture((ImmutableList<ListenableFuture<? extends V>>)ImmutableList.copyOf((ListenableFuture<? extends V>[])futures), true, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> allAsList(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return listFuture(ImmutableList.copyOf(futures), true, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    public static <V> ListenableFuture<V> nonCancellationPropagating(final ListenableFuture<V> future) {
        return new NonCancellationPropagatingFuture<V>(future);
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(final ListenableFuture<? extends V>... futures) {
        return listFuture((ImmutableList<ListenableFuture<? extends V>>)ImmutableList.copyOf((ListenableFuture<? extends V>[])futures), false, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    @Beta
    public static <V> ListenableFuture<List<V>> successfulAsList(final Iterable<? extends ListenableFuture<? extends V>> futures) {
        return listFuture(ImmutableList.copyOf(futures), false, (Executor)MoreExecutors.sameThreadExecutor());
    }
    
    @Beta
    public static <T> ImmutableList<ListenableFuture<T>> inCompletionOrder(final Iterable<? extends ListenableFuture<? extends T>> futures) {
        final ConcurrentLinkedQueue<AsyncSettableFuture<T>> delegates = Queues.newConcurrentLinkedQueue();
        final ImmutableList.Builder<ListenableFuture<T>> listBuilder = ImmutableList.builder();
        final SerializingExecutor executor = new SerializingExecutor(MoreExecutors.sameThreadExecutor());
        for (final ListenableFuture<? extends T> future : futures) {
            final AsyncSettableFuture<T> delegate = AsyncSettableFuture.create();
            delegates.add(delegate);
            future.addListener(new Runnable() {
                @Override
                public void run() {
                    ((AsyncSettableFuture)delegates.remove()).setFuture(future);
                }
            }, executor);
            listBuilder.add(delegate);
        }
        return listBuilder.build();
    }
    
    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback) {
        addCallback(future, callback, MoreExecutors.sameThreadExecutor());
    }
    
    public static <V> void addCallback(final ListenableFuture<V> future, final FutureCallback<? super V> callback, final Executor executor) {
        Preconditions.checkNotNull(callback);
        final Runnable callbackListener = new Runnable() {
            @Override
            public void run() {
                V value;
                try {
                    value = Uninterruptibles.getUninterruptibly(future);
                }
                catch (ExecutionException e) {
                    callback.onFailure(e.getCause());
                    return;
                }
                catch (RuntimeException e2) {
                    callback.onFailure(e2);
                    return;
                }
                catch (Error e3) {
                    callback.onFailure(e3);
                    return;
                }
                callback.onSuccess(value);
            }
        };
        future.addListener(callbackListener, executor);
    }
    
    public static <V, X extends Exception> V get(final Future<V> future, final Class<X> exceptionClass) throws X, Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);
        try {
            return future.get();
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        }
        catch (ExecutionException e2) {
            wrapAndThrowExceptionOrError(e2.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }
    
    public static <V, X extends Exception> V get(final Future<V> future, final long timeout, final TimeUnit unit, final Class<X> exceptionClass) throws X, Exception {
        Preconditions.checkNotNull(future);
        Preconditions.checkNotNull(unit);
        Preconditions.checkArgument(!RuntimeException.class.isAssignableFrom(exceptionClass), "Futures.get exception type (%s) must not be a RuntimeException", exceptionClass);
        try {
            return future.get(timeout, unit);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw newWithCause(exceptionClass, e);
        }
        catch (TimeoutException e2) {
            throw newWithCause(exceptionClass, e2);
        }
        catch (ExecutionException e3) {
            wrapAndThrowExceptionOrError(e3.getCause(), exceptionClass);
            throw new AssertionError();
        }
    }
    
    private static <X extends Exception> void wrapAndThrowExceptionOrError(final Throwable cause, final Class<X> exceptionClass) throws X, Exception {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        if (cause instanceof RuntimeException) {
            throw new UncheckedExecutionException(cause);
        }
        throw newWithCause(exceptionClass, cause);
    }
    
    public static <V> V getUnchecked(final Future<V> future) {
        Preconditions.checkNotNull(future);
        try {
            return Uninterruptibles.getUninterruptibly(future);
        }
        catch (ExecutionException e) {
            wrapAndThrowUnchecked(e.getCause());
            throw new AssertionError();
        }
    }
    
    private static void wrapAndThrowUnchecked(final Throwable cause) {
        if (cause instanceof Error) {
            throw new ExecutionError((Error)cause);
        }
        throw new UncheckedExecutionException(cause);
    }
    
    private static <X extends Exception> X newWithCause(final Class<X> exceptionClass, final Throwable cause) {
        final List<Constructor<X>> constructors = Arrays.asList((Constructor<X>[])exceptionClass.getConstructors());
        for (final Constructor<X> constructor : preferringStrings(constructors)) {
            final X instance = newFromConstructor(constructor, cause);
            if (instance != null) {
                if (instance.getCause() == null) {
                    instance.initCause(cause);
                }
                return instance;
            }
        }
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + exceptionClass + " in response to chained exception", cause);
    }
    
    private static <X extends Exception> List<Constructor<X>> preferringStrings(final List<Constructor<X>> constructors) {
        return Futures.WITH_STRING_PARAM_FIRST.sortedCopy(constructors);
    }
    
    @Nullable
    private static <X> X newFromConstructor(final Constructor<X> constructor, final Throwable cause) {
        final Class<?>[] paramTypes = constructor.getParameterTypes();
        final Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; ++i) {
            final Class<?> paramType = paramTypes[i];
            if (paramType.equals(String.class)) {
                params[i] = cause.toString();
            }
            else {
                if (!paramType.equals(Throwable.class)) {
                    return null;
                }
                params[i] = cause;
            }
        }
        try {
            return constructor.newInstance(params);
        }
        catch (IllegalArgumentException e) {
            return null;
        }
        catch (InstantiationException e2) {
            return null;
        }
        catch (IllegalAccessException e3) {
            return null;
        }
        catch (InvocationTargetException e4) {
            return null;
        }
    }
    
    private static <V> ListenableFuture<List<V>> listFuture(final ImmutableList<ListenableFuture<? extends V>> futures, final boolean allMustSucceed, final Executor listenerExecutor) {
        return (ListenableFuture<List<V>>)new CombinedFuture(futures, allMustSucceed, listenerExecutor, (FutureCombiner<Object, Object>)new FutureCombiner<V, List<V>>() {
            @Override
            public List<V> combine(final List<Optional<V>> values) {
                final List<V> result = (List<V>)Lists.newArrayList();
                for (final Optional<V> element : values) {
                    result.add((element != null) ? element.orNull() : null);
                }
                return Collections.unmodifiableList((List<? extends V>)result);
            }
        });
    }
    
    static {
        DEREFERENCER = new AsyncFunction<ListenableFuture<Object>, Object>() {
            @Override
            public ListenableFuture<Object> apply(final ListenableFuture<Object> input) {
                return input;
            }
        };
        WITH_STRING_PARAM_FIRST = Ordering.natural().onResultOf((Function<Object, ? extends Comparable>)new Function<Constructor<?>, Boolean>() {
            @Override
            public Boolean apply(final Constructor<?> input) {
                return Arrays.asList(input.getParameterTypes()).contains(String.class);
            }
        }).reverse();
    }
    
    private abstract static class ImmediateFuture<V> implements ListenableFuture<V>
    {
        private static final Logger log;
        
        @Override
        public void addListener(final Runnable listener, final Executor executor) {
            Preconditions.checkNotNull(listener, (Object)"Runnable was null.");
            Preconditions.checkNotNull(executor, (Object)"Executor was null.");
            try {
                executor.execute(listener);
            }
            catch (RuntimeException e) {
                ImmediateFuture.log.log(Level.SEVERE, "RuntimeException while executing runnable " + listener + " with executor " + executor, e);
            }
        }
        
        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            return false;
        }
        
        @Override
        public abstract V get() throws ExecutionException;
        
        @Override
        public V get(final long timeout, final TimeUnit unit) throws ExecutionException {
            Preconditions.checkNotNull(unit);
            return this.get();
        }
        
        @Override
        public boolean isCancelled() {
            return false;
        }
        
        @Override
        public boolean isDone() {
            return true;
        }
        
        static {
            log = Logger.getLogger(ImmediateFuture.class.getName());
        }
    }
    
    private static class ImmediateSuccessfulFuture<V> extends ImmediateFuture<V>
    {
        @Nullable
        private final V value;
        
        ImmediateSuccessfulFuture(@Nullable final V value) {
            this.value = value;
        }
        
        @Override
        public V get() {
            return this.value;
        }
    }
    
    private static class ImmediateSuccessfulCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X>
    {
        @Nullable
        private final V value;
        
        ImmediateSuccessfulCheckedFuture(@Nullable final V value) {
            this.value = value;
        }
        
        @Override
        public V get() {
            return this.value;
        }
        
        @Override
        public V checkedGet() {
            return this.value;
        }
        
        @Override
        public V checkedGet(final long timeout, final TimeUnit unit) {
            Preconditions.checkNotNull(unit);
            return this.value;
        }
    }
    
    private static class ImmediateFailedFuture<V> extends ImmediateFuture<V>
    {
        private final Throwable thrown;
        
        ImmediateFailedFuture(final Throwable thrown) {
            this.thrown = thrown;
        }
        
        @Override
        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }
    }
    
    private static class ImmediateCancelledFuture<V> extends ImmediateFuture<V>
    {
        private final CancellationException thrown;
        
        ImmediateCancelledFuture() {
            this.thrown = new CancellationException("Immediate cancelled future.");
        }
        
        @Override
        public boolean isCancelled() {
            return true;
        }
        
        @Override
        public V get() {
            throw AbstractFuture.cancellationExceptionWithCause("Task was cancelled.", this.thrown);
        }
    }
    
    private static class ImmediateFailedCheckedFuture<V, X extends Exception> extends ImmediateFuture<V> implements CheckedFuture<V, X>
    {
        private final X thrown;
        
        ImmediateFailedCheckedFuture(final X thrown) {
            this.thrown = thrown;
        }
        
        @Override
        public V get() throws ExecutionException {
            throw new ExecutionException(this.thrown);
        }
        
        @Override
        public V checkedGet() throws X, Exception {
            throw this.thrown;
        }
        
        @Override
        public V checkedGet(final long timeout, final TimeUnit unit) throws X, Exception {
            Preconditions.checkNotNull(unit);
            throw this.thrown;
        }
    }
    
    private static class FallbackFuture<V> extends AbstractFuture<V>
    {
        private volatile ListenableFuture<? extends V> running;
        
        FallbackFuture(final ListenableFuture<? extends V> input, final FutureFallback<? extends V> fallback, final Executor executor) {
            Futures.addCallback((ListenableFuture<Object>)(this.running = input), (FutureCallback<? super Object>)new FutureCallback<V>() {
                @Override
                public void onSuccess(final V value) {
                    FallbackFuture.this.set(value);
                }
                
                @Override
                public void onFailure(final Throwable t) {
                    if (FallbackFuture.this.isCancelled()) {
                        return;
                    }
                    try {
                        FallbackFuture.this.running = (ListenableFuture<? extends V>)fallback.create(t);
                        if (FallbackFuture.this.isCancelled()) {
                            FallbackFuture.this.running.cancel(FallbackFuture.this.wasInterrupted());
                            return;
                        }
                        Futures.addCallback((ListenableFuture<Object>)FallbackFuture.this.running, (FutureCallback<? super Object>)new FutureCallback<V>() {
                            @Override
                            public void onSuccess(final V value) {
                                FallbackFuture.this.set(value);
                            }
                            
                            @Override
                            public void onFailure(final Throwable t) {
                                if (FallbackFuture.this.running.isCancelled()) {
                                    FallbackFuture.this.cancel(false);
                                }
                                else {
                                    FallbackFuture.this.setException(t);
                                }
                            }
                        }, MoreExecutors.sameThreadExecutor());
                    }
                    catch (Throwable e) {
                        FallbackFuture.this.setException(e);
                    }
                }
            }, executor);
        }
        
        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {
                this.running.cancel(mayInterruptIfRunning);
                return true;
            }
            return false;
        }
    }
    
    private static class ChainingListenableFuture<I, O> extends AbstractFuture<O> implements Runnable
    {
        private AsyncFunction<? super I, ? extends O> function;
        private ListenableFuture<? extends I> inputFuture;
        private volatile ListenableFuture<? extends O> outputFuture;
        private final CountDownLatch outputCreated;
        
        private ChainingListenableFuture(final AsyncFunction<? super I, ? extends O> function, final ListenableFuture<? extends I> inputFuture) {
            this.outputCreated = new CountDownLatch(1);
            this.function = Preconditions.checkNotNull(function);
            this.inputFuture = Preconditions.checkNotNull(inputFuture);
        }
        
        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            if (super.cancel(mayInterruptIfRunning)) {
                this.cancel(this.inputFuture, mayInterruptIfRunning);
                this.cancel(this.outputFuture, mayInterruptIfRunning);
                return true;
            }
            return false;
        }
        
        private void cancel(@Nullable final Future<?> future, final boolean mayInterruptIfRunning) {
            if (future != null) {
                future.cancel(mayInterruptIfRunning);
            }
        }
        
        @Override
        public void run() {
            try {
                I sourceResult;
                try {
                    sourceResult = Uninterruptibles.getUninterruptibly((Future<I>)this.inputFuture);
                }
                catch (CancellationException e3) {
                    this.cancel(false);
                    return;
                }
                catch (ExecutionException e) {
                    this.setException(e.getCause());
                    return;
                }
                final ListenableFuture<? extends O> outputFuture2 = Preconditions.checkNotNull(this.function.apply((Object)sourceResult), (Object)"AsyncFunction may not return null.");
                this.outputFuture = outputFuture2;
                final ListenableFuture<? extends O> outputFuture = outputFuture2;
                if (this.isCancelled()) {
                    outputFuture.cancel(this.wasInterrupted());
                    this.outputFuture = null;
                    return;
                }
                outputFuture.addListener(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ChainingListenableFuture.this.set(Uninterruptibles.getUninterruptibly((Future<V>)outputFuture));
                        }
                        catch (CancellationException e2) {
                            ChainingListenableFuture.this.cancel(false);
                        }
                        catch (ExecutionException e) {
                            ChainingListenableFuture.this.setException(e.getCause());
                        }
                        finally {
                            ChainingListenableFuture.this.outputFuture = null;
                        }
                    }
                }, MoreExecutors.sameThreadExecutor());
            }
            catch (UndeclaredThrowableException e2) {
                this.setException(e2.getCause());
            }
            catch (Throwable t) {
                this.setException(t);
            }
            finally {
                this.function = null;
                this.inputFuture = null;
                this.outputCreated.countDown();
            }
        }
    }
    
    private static class NonCancellationPropagatingFuture<V> extends AbstractFuture<V>
    {
        NonCancellationPropagatingFuture(final ListenableFuture<V> delegate) {
            Preconditions.checkNotNull(delegate);
            Futures.addCallback(delegate, new FutureCallback<V>() {
                @Override
                public void onSuccess(final V result) {
                    NonCancellationPropagatingFuture.this.set(result);
                }
                
                @Override
                public void onFailure(final Throwable t) {
                    if (delegate.isCancelled()) {
                        NonCancellationPropagatingFuture.this.cancel(false);
                    }
                    else {
                        NonCancellationPropagatingFuture.this.setException(t);
                    }
                }
            }, MoreExecutors.sameThreadExecutor());
        }
    }
    
    private static class CombinedFuture<V, C> extends AbstractFuture<C>
    {
        private static final Logger logger;
        ImmutableCollection<? extends ListenableFuture<? extends V>> futures;
        final boolean allMustSucceed;
        final AtomicInteger remaining;
        FutureCombiner<V, C> combiner;
        List<Optional<V>> values;
        final Object seenExceptionsLock;
        Set<Throwable> seenExceptions;
        
        CombinedFuture(final ImmutableCollection<? extends ListenableFuture<? extends V>> futures, final boolean allMustSucceed, final Executor listenerExecutor, final FutureCombiner<V, C> combiner) {
            this.seenExceptionsLock = new Object();
            this.futures = futures;
            this.allMustSucceed = allMustSucceed;
            this.remaining = new AtomicInteger(futures.size());
            this.combiner = combiner;
            this.values = (List<Optional<V>>)Lists.newArrayListWithCapacity(futures.size());
            this.init(listenerExecutor);
        }
        
        protected void init(final Executor listenerExecutor) {
            this.addListener(new Runnable() {
                @Override
                public void run() {
                    if (CombinedFuture.this.isCancelled()) {
                        for (final ListenableFuture<?> future : CombinedFuture.this.futures) {
                            future.cancel(CombinedFuture.this.wasInterrupted());
                        }
                    }
                    CombinedFuture.this.futures = null;
                    CombinedFuture.this.values = null;
                    CombinedFuture.this.combiner = null;
                }
            }, MoreExecutors.sameThreadExecutor());
            if (this.futures.isEmpty()) {
                this.set(this.combiner.combine((List<Optional<V>>)ImmutableList.of()));
                return;
            }
            for (int i = 0; i < this.futures.size(); ++i) {
                this.values.add(null);
            }
            int i = 0;
            for (final ListenableFuture<? extends V> listenable : this.futures) {
                final int index = i++;
                listenable.addListener(new Runnable() {
                    @Override
                    public void run() {
                        CombinedFuture.this.setOneValue(index, listenable);
                    }
                }, listenerExecutor);
            }
        }
        
        private void setExceptionAndMaybeLog(final Throwable throwable) {
            boolean visibleFromOutputFuture = false;
            boolean firstTimeSeeingThisException = true;
            if (this.allMustSucceed) {
                visibleFromOutputFuture = super.setException(throwable);
                synchronized (this.seenExceptionsLock) {
                    if (this.seenExceptions == null) {
                        this.seenExceptions = (Set<Throwable>)Sets.newHashSet();
                    }
                    firstTimeSeeingThisException = this.seenExceptions.add(throwable);
                }
            }
            if (throwable instanceof Error || (this.allMustSucceed && !visibleFromOutputFuture && firstTimeSeeingThisException)) {
                CombinedFuture.logger.log(Level.SEVERE, "input future failed.", throwable);
            }
        }
        
        private void setOneValue(final int index, final Future<? extends V> future) {
            final List<Optional<V>> localValues = this.values;
            while (true) {
                if (this.isDone() || localValues == null) {
                    Preconditions.checkState(this.allMustSucceed || this.isCancelled(), (Object)"Future was done before all dependencies completed");
                    try {
                        Preconditions.checkState(future.isDone(), (Object)"Tried to set value from future which is not done");
                        final V returnValue = Uninterruptibles.getUninterruptibly((Future<V>)future);
                        if (localValues != null) {
                            localValues.set(index, Optional.fromNullable(returnValue));
                        }
                    }
                    catch (CancellationException e2) {
                        if (this.allMustSucceed) {
                            this.cancel(false);
                        }
                    }
                    catch (ExecutionException e) {
                        this.setExceptionAndMaybeLog(e.getCause());
                    }
                    catch (Throwable t) {
                        this.setExceptionAndMaybeLog(t);
                    }
                    finally {
                        final int newRemaining = this.remaining.decrementAndGet();
                        Preconditions.checkState(newRemaining >= 0, (Object)"Less than 0 remaining futures");
                        if (newRemaining == 0) {
                            final FutureCombiner<V, C> localCombiner = this.combiner;
                            if (localCombiner != null && localValues != null) {
                                this.set(localCombiner.combine(localValues));
                            }
                            else {
                                Preconditions.checkState(this.isDone());
                            }
                        }
                    }
                    return;
                }
                continue;
            }
        }
        
        static {
            logger = Logger.getLogger(CombinedFuture.class.getName());
        }
    }
    
    private static class MappingCheckedFuture<V, X extends Exception> extends AbstractCheckedFuture<V, X>
    {
        final Function<Exception, X> mapper;
        
        MappingCheckedFuture(final ListenableFuture<V> delegate, final Function<Exception, X> mapper) {
            super(delegate);
            this.mapper = Preconditions.checkNotNull(mapper);
        }
        
        @Override
        protected X mapException(final Exception e) {
            return this.mapper.apply(e);
        }
    }
    
    private interface FutureCombiner<V, C>
    {
        C combine(final List<Optional<V>> p0);
    }
}
