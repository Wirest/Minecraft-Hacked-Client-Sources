/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2Connection
/*     */   implements Http2Connection
/*     */ {
/*  54 */   private final Set<Http2Connection.Listener> listeners = new HashSet(4);
/*  55 */   private final IntObjectMap<Http2Stream> streamMap = new IntObjectHashMap();
/*  56 */   private final ConnectionStream connectionStream = new ConnectionStream();
/*  57 */   private final Set<Http2Stream> activeStreams = new LinkedHashSet();
/*     */   
/*     */ 
/*     */   private final DefaultEndpoint<Http2LocalFlowController> localEndpoint;
/*     */   
/*     */   private final DefaultEndpoint<Http2RemoteFlowController> remoteEndpoint;
/*     */   
/*     */   private final Http2StreamRemovalPolicy removalPolicy;
/*     */   
/*     */ 
/*     */   public DefaultHttp2Connection(boolean server)
/*     */   {
/*  69 */     this(server, Http2CodecUtil.immediateRemovalPolicy());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultHttp2Connection(boolean server, Http2StreamRemovalPolicy removalPolicy)
/*     */   {
/*  82 */     this.removalPolicy = ((Http2StreamRemovalPolicy)ObjectUtil.checkNotNull(removalPolicy, "removalPolicy"));
/*  83 */     this.localEndpoint = new DefaultEndpoint(server);
/*  84 */     this.remoteEndpoint = new DefaultEndpoint(!server);
/*     */     
/*     */ 
/*  87 */     removalPolicy.setAction(new Http2StreamRemovalPolicy.Action()
/*     */     {
/*     */       public void removeStream(Http2Stream stream) {
/*  90 */         DefaultHttp2Connection.this.removeStream((DefaultHttp2Connection.DefaultStream)stream);
/*     */       }
/*     */       
/*     */ 
/*  94 */     });
/*  95 */     this.streamMap.put(this.connectionStream.id(), this.connectionStream);
/*     */   }
/*     */   
/*     */   public void addListener(Http2Connection.Listener listener)
/*     */   {
/* 100 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */   public void removeListener(Http2Connection.Listener listener)
/*     */   {
/* 105 */     this.listeners.remove(listener);
/*     */   }
/*     */   
/*     */   public boolean isServer()
/*     */   {
/* 110 */     return this.localEndpoint.isServer();
/*     */   }
/*     */   
/*     */   public Http2Stream connectionStream()
/*     */   {
/* 115 */     return this.connectionStream;
/*     */   }
/*     */   
/*     */   public Http2Stream requireStream(int streamId) throws Http2Exception
/*     */   {
/* 120 */     Http2Stream stream = stream(streamId);
/* 121 */     if (stream == null) {
/* 122 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream does not exist %d", new Object[] { Integer.valueOf(streamId) });
/*     */     }
/* 124 */     return stream;
/*     */   }
/*     */   
/*     */   public Http2Stream stream(int streamId)
/*     */   {
/* 129 */     return (Http2Stream)this.streamMap.get(streamId);
/*     */   }
/*     */   
/*     */   public int numActiveStreams()
/*     */   {
/* 134 */     return this.activeStreams.size();
/*     */   }
/*     */   
/*     */   public Set<Http2Stream> activeStreams()
/*     */   {
/* 139 */     return Collections.unmodifiableSet(this.activeStreams);
/*     */   }
/*     */   
/*     */   public void deactivate(Http2Stream stream)
/*     */   {
/* 144 */     deactivateInternal((DefaultStream)stream);
/*     */   }
/*     */   
/*     */   public Http2Connection.Endpoint<Http2LocalFlowController> local()
/*     */   {
/* 149 */     return this.localEndpoint;
/*     */   }
/*     */   
/*     */   public Http2Connection.Endpoint<Http2RemoteFlowController> remote()
/*     */   {
/* 154 */     return this.remoteEndpoint;
/*     */   }
/*     */   
/*     */   public boolean isGoAway()
/*     */   {
/* 159 */     return (goAwaySent()) || (goAwayReceived());
/*     */   }
/*     */   
/*     */   public Http2Stream createLocalStream(int streamId) throws Http2Exception
/*     */   {
/* 164 */     return local().createStream(streamId);
/*     */   }
/*     */   
/*     */   public Http2Stream createRemoteStream(int streamId) throws Http2Exception
/*     */   {
/* 169 */     return remote().createStream(streamId);
/*     */   }
/*     */   
/*     */   public boolean goAwayReceived()
/*     */   {
/* 174 */     return this.localEndpoint.lastKnownStream >= 0;
/*     */   }
/*     */   
/*     */   public void goAwayReceived(int lastKnownStream)
/*     */   {
/* 179 */     this.localEndpoint.lastKnownStream(lastKnownStream);
/*     */   }
/*     */   
/*     */   public boolean goAwaySent()
/*     */   {
/* 184 */     return this.remoteEndpoint.lastKnownStream >= 0;
/*     */   }
/*     */   
/*     */   public void goAwaySent(int lastKnownStream)
/*     */   {
/* 189 */     this.remoteEndpoint.lastKnownStream(lastKnownStream);
/*     */   }
/*     */   
/*     */   private void removeStream(DefaultStream stream)
/*     */   {
/* 194 */     for (Http2Connection.Listener listener : this.listeners) {
/* 195 */       listener.streamRemoved(stream);
/*     */     }
/*     */     
/*     */ 
/* 199 */     this.streamMap.remove(stream.id());
/* 200 */     stream.parent().removeChild(stream);
/*     */   }
/*     */   
/*     */   private void activateInternal(DefaultStream stream) {
/* 204 */     if (this.activeStreams.add(stream))
/*     */     {
/* 206 */       DefaultEndpoint.access$308(stream.createdBy());
/*     */       
/*     */ 
/* 209 */       for (Http2Connection.Listener listener : this.listeners) {
/* 210 */         listener.streamActive(stream);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void deactivateInternal(DefaultStream stream) {
/* 216 */     if (this.activeStreams.remove(stream))
/*     */     {
/* 218 */       DefaultEndpoint.access$310(stream.createdBy());
/*     */       
/*     */ 
/* 221 */       for (Http2Connection.Listener listener : this.listeners) {
/* 222 */         listener.streamInactive(stream);
/*     */       }
/*     */       
/*     */ 
/* 226 */       this.removalPolicy.markForRemoval(stream);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class DefaultStream
/*     */     implements Http2Stream
/*     */   {
/*     */     private final int id;
/* 235 */     private Http2Stream.State state = Http2Stream.State.IDLE;
/* 236 */     private short weight = 16;
/*     */     private DefaultStream parent;
/* 238 */     private IntObjectMap<DefaultStream> children = DefaultHttp2Connection.access$400();
/*     */     private int totalChildWeights;
/*     */     private boolean resetSent;
/*     */     private DefaultHttp2Connection.PropertyMap data;
/*     */     
/*     */     DefaultStream(int id) {
/* 244 */       this.id = id;
/* 245 */       this.data = new DefaultHttp2Connection.LazyPropertyMap(this);
/*     */     }
/*     */     
/*     */     public final int id()
/*     */     {
/* 250 */       return this.id;
/*     */     }
/*     */     
/*     */     public final Http2Stream.State state()
/*     */     {
/* 255 */       return this.state;
/*     */     }
/*     */     
/*     */     public boolean isResetSent()
/*     */     {
/* 260 */       return this.resetSent;
/*     */     }
/*     */     
/*     */     public Http2Stream resetSent()
/*     */     {
/* 265 */       this.resetSent = true;
/* 266 */       return this;
/*     */     }
/*     */     
/*     */     public Object setProperty(Object key, Object value)
/*     */     {
/* 271 */       return this.data.put(key, value);
/*     */     }
/*     */     
/*     */     public <V> V getProperty(Object key)
/*     */     {
/* 276 */       return (V)this.data.get(key);
/*     */     }
/*     */     
/*     */     public <V> V removeProperty(Object key)
/*     */     {
/* 281 */       return (V)this.data.remove(key);
/*     */     }
/*     */     
/*     */     public final boolean isRoot()
/*     */     {
/* 286 */       return this.parent == null;
/*     */     }
/*     */     
/*     */     public final short weight()
/*     */     {
/* 291 */       return this.weight;
/*     */     }
/*     */     
/*     */     public final int totalChildWeights()
/*     */     {
/* 296 */       return this.totalChildWeights;
/*     */     }
/*     */     
/*     */     public final DefaultStream parent()
/*     */     {
/* 301 */       return this.parent;
/*     */     }
/*     */     
/*     */     public final boolean isDescendantOf(Http2Stream stream)
/*     */     {
/* 306 */       Http2Stream next = parent();
/* 307 */       while (next != null) {
/* 308 */         if (next == stream) {
/* 309 */           return true;
/*     */         }
/* 311 */         next = next.parent();
/*     */       }
/* 313 */       return false;
/*     */     }
/*     */     
/*     */     public final boolean isLeaf()
/*     */     {
/* 318 */       return numChildren() == 0;
/*     */     }
/*     */     
/*     */     public final int numChildren()
/*     */     {
/* 323 */       return this.children.size();
/*     */     }
/*     */     
/*     */     public final Collection<? extends Http2Stream> children()
/*     */     {
/* 328 */       return this.children.values();
/*     */     }
/*     */     
/*     */     public final boolean hasChild(int streamId)
/*     */     {
/* 333 */       return child(streamId) != null;
/*     */     }
/*     */     
/*     */     public final Http2Stream child(int streamId)
/*     */     {
/* 338 */       return (Http2Stream)this.children.get(streamId);
/*     */     }
/*     */     
/*     */     public Http2Stream setPriority(int parentStreamId, short weight, boolean exclusive) throws Http2Exception
/*     */     {
/* 343 */       if ((weight < 1) || (weight > 256)) {
/* 344 */         throw new IllegalArgumentException(String.format("Invalid weight: %d.  Must be between %d and %d (inclusive).", new Object[] { Short.valueOf(weight), Short.valueOf(1), Short.valueOf(256) }));
/*     */       }
/*     */       
/*     */ 
/* 348 */       DefaultStream newParent = (DefaultStream)DefaultHttp2Connection.this.stream(parentStreamId);
/* 349 */       if (newParent == null)
/*     */       {
/*     */ 
/* 352 */         newParent = createdBy().createStream(parentStreamId);
/* 353 */       } else if (this == newParent) {
/* 354 */         throw new IllegalArgumentException("A stream cannot depend on itself");
/*     */       }
/*     */       
/*     */ 
/* 358 */       weight(weight);
/*     */       
/* 360 */       if ((newParent != parent()) || (exclusive)) {
/*     */         List<DefaultHttp2Connection.ParentChangedEvent> events;
/* 362 */         if (newParent.isDescendantOf(this)) {
/* 363 */           List<DefaultHttp2Connection.ParentChangedEvent> events = new ArrayList(2 + (exclusive ? newParent.numChildren() : 0));
/* 364 */           this.parent.takeChild(newParent, false, events);
/*     */         } else {
/* 366 */           events = new ArrayList(1 + (exclusive ? newParent.numChildren() : 0));
/*     */         }
/* 368 */         newParent.takeChild(this, exclusive, events);
/* 369 */         DefaultHttp2Connection.this.notifyParentChanged(events);
/*     */       }
/*     */       
/* 372 */       return this;
/*     */     }
/*     */     
/*     */     public Http2Stream open(boolean halfClosed) throws Http2Exception
/*     */     {
/* 377 */       switch (DefaultHttp2Connection.2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[this.state.ordinal()]) {
/*     */       case 1: 
/* 379 */         this.state = (halfClosed ? Http2Stream.State.HALF_CLOSED_REMOTE : isLocal() ? Http2Stream.State.HALF_CLOSED_LOCAL : Http2Stream.State.OPEN);
/* 380 */         break;
/*     */       case 2: 
/* 382 */         this.state = Http2Stream.State.HALF_CLOSED_REMOTE;
/* 383 */         break;
/*     */       case 3: 
/* 385 */         this.state = Http2Stream.State.HALF_CLOSED_LOCAL;
/* 386 */         break;
/*     */       default: 
/* 388 */         throw Http2Exception.streamError(this.id, Http2Error.PROTOCOL_ERROR, "Attempting to open a stream in an invalid state: " + this.state, new Object[0]);
/*     */       }
/*     */       
/* 391 */       DefaultHttp2Connection.this.activateInternal(this);
/* 392 */       return this;
/*     */     }
/*     */     
/*     */     public Http2Stream close()
/*     */     {
/* 397 */       if (this.state == Http2Stream.State.CLOSED) {
/* 398 */         return this;
/*     */       }
/*     */       
/* 401 */       this.state = Http2Stream.State.CLOSED;
/* 402 */       DefaultHttp2Connection.this.deactivateInternal(this);
/* 403 */       return this;
/*     */     }
/*     */     
/*     */     public Http2Stream closeLocalSide()
/*     */     {
/* 408 */       switch (DefaultHttp2Connection.2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[this.state.ordinal()]) {
/*     */       case 4: 
/* 410 */         this.state = Http2Stream.State.HALF_CLOSED_LOCAL;
/* 411 */         notifyHalfClosed(this);
/* 412 */         break;
/*     */       case 5: 
/*     */         break;
/*     */       default: 
/* 416 */         close();
/*     */       }
/*     */       
/* 419 */       return this;
/*     */     }
/*     */     
/*     */     public Http2Stream closeRemoteSide()
/*     */     {
/* 424 */       switch (DefaultHttp2Connection.2.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[this.state.ordinal()]) {
/*     */       case 4: 
/* 426 */         this.state = Http2Stream.State.HALF_CLOSED_REMOTE;
/* 427 */         notifyHalfClosed(this);
/* 428 */         break;
/*     */       case 6: 
/*     */         break;
/*     */       default: 
/* 432 */         close();
/*     */       }
/*     */       
/* 435 */       return this;
/*     */     }
/*     */     
/*     */     private void notifyHalfClosed(Http2Stream stream) {
/* 439 */       for (Http2Connection.Listener listener : DefaultHttp2Connection.this.listeners) {
/* 440 */         listener.streamHalfClosed(stream);
/*     */       }
/*     */     }
/*     */     
/*     */     public final boolean remoteSideOpen()
/*     */     {
/* 446 */       return (this.state == Http2Stream.State.HALF_CLOSED_LOCAL) || (this.state == Http2Stream.State.OPEN) || (this.state == Http2Stream.State.RESERVED_REMOTE);
/*     */     }
/*     */     
/*     */     public final boolean localSideOpen()
/*     */     {
/* 451 */       return (this.state == Http2Stream.State.HALF_CLOSED_REMOTE) || (this.state == Http2Stream.State.OPEN) || (this.state == Http2Stream.State.RESERVED_LOCAL);
/*     */     }
/*     */     
/*     */     final DefaultHttp2Connection.DefaultEndpoint<? extends Http2FlowController> createdBy() {
/* 455 */       return DefaultHttp2Connection.this.localEndpoint.createdStreamId(this.id) ? DefaultHttp2Connection.this.localEndpoint : DefaultHttp2Connection.this.remoteEndpoint;
/*     */     }
/*     */     
/*     */ 
/* 459 */     final boolean isLocal() { return DefaultHttp2Connection.this.localEndpoint.createdStreamId(this.id); }
/*     */     
/*     */     final void weight(short weight) {
/*     */       short oldWeight;
/* 463 */       if (weight != this.weight) {
/* 464 */         if (this.parent != null) {
/* 465 */           int delta = weight - this.weight;
/* 466 */           this.parent.totalChildWeights += delta;
/*     */         }
/* 468 */         oldWeight = this.weight;
/* 469 */         this.weight = weight;
/* 470 */         for (Http2Connection.Listener l : DefaultHttp2Connection.this.listeners) {
/* 471 */           l.onWeightChanged(this, oldWeight);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     final IntObjectMap<DefaultStream> removeAllChildren() {
/* 477 */       this.totalChildWeights = 0;
/* 478 */       IntObjectMap<DefaultStream> prevChildren = this.children;
/* 479 */       this.children = DefaultHttp2Connection.access$400();
/* 480 */       return prevChildren;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     final void takeChild(DefaultStream child, boolean exclusive, List<DefaultHttp2Connection.ParentChangedEvent> events)
/*     */     {
/* 488 */       DefaultStream oldParent = child.parent();
/* 489 */       events.add(new DefaultHttp2Connection.ParentChangedEvent(child, oldParent));
/* 490 */       DefaultHttp2Connection.this.notifyParentChanging(child, this);
/* 491 */       child.parent = this;
/*     */       
/* 493 */       if ((exclusive) && (!this.children.isEmpty()))
/*     */       {
/*     */ 
/*     */ 
/* 497 */         for (DefaultStream grandchild : removeAllChildren().values()) {
/* 498 */           child.takeChild(grandchild, false, events);
/*     */         }
/*     */       }
/*     */       
/* 502 */       if (this.children.put(child.id(), child) == null) {
/* 503 */         this.totalChildWeights += child.weight();
/*     */       }
/*     */       
/* 506 */       if ((oldParent != null) && (oldParent.children.remove(child.id()) != null)) {
/* 507 */         oldParent.totalChildWeights -= child.weight();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     final void removeChild(DefaultStream child)
/*     */     {
/* 515 */       if (this.children.remove(child.id()) != null) {
/* 516 */         List<DefaultHttp2Connection.ParentChangedEvent> events = new ArrayList(1 + child.children.size());
/* 517 */         events.add(new DefaultHttp2Connection.ParentChangedEvent(child, child.parent()));
/* 518 */         DefaultHttp2Connection.this.notifyParentChanging(child, null);
/* 519 */         child.parent = null;
/* 520 */         this.totalChildWeights -= child.weight();
/*     */         
/*     */ 
/* 523 */         for (DefaultStream grandchild : child.children.values()) {
/* 524 */           takeChild(grandchild, false, events);
/*     */         }
/*     */         
/* 527 */         DefaultHttp2Connection.this.notifyParentChanged(events);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static abstract interface PropertyMap
/*     */   {
/*     */     public abstract Object put(Object paramObject1, Object paramObject2);
/*     */     
/*     */ 
/*     */     public abstract <V> V get(Object paramObject);
/*     */     
/*     */     public abstract <V> V remove(Object paramObject);
/*     */   }
/*     */   
/*     */   private static final class DefaultProperyMap
/*     */     implements DefaultHttp2Connection.PropertyMap
/*     */   {
/*     */     private final Map<Object, Object> data;
/*     */     
/*     */     DefaultProperyMap(int initialSize)
/*     */     {
/* 550 */       this.data = new HashMap(initialSize);
/*     */     }
/*     */     
/*     */     public Object put(Object key, Object value)
/*     */     {
/* 555 */       return this.data.put(key, value);
/*     */     }
/*     */     
/*     */ 
/*     */     public <V> V get(Object key)
/*     */     {
/* 561 */       return (V)this.data.get(key);
/*     */     }
/*     */     
/*     */ 
/*     */     public <V> V remove(Object key)
/*     */     {
/* 567 */       return (V)this.data.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class LazyPropertyMap
/*     */     implements DefaultHttp2Connection.PropertyMap
/*     */   {
/*     */     private static final int DEFAULT_INITIAL_SIZE = 4;
/*     */     private final DefaultHttp2Connection.DefaultStream stream;
/*     */     
/*     */     LazyPropertyMap(DefaultHttp2Connection.DefaultStream stream)
/*     */     {
/* 579 */       this.stream = stream;
/*     */     }
/*     */     
/*     */     public Object put(Object key, Object value)
/*     */     {
/* 584 */       DefaultHttp2Connection.DefaultStream.access$1202(this.stream, new DefaultHttp2Connection.DefaultProperyMap(4));
/* 585 */       return DefaultHttp2Connection.DefaultStream.access$1200(this.stream).put(key, value);
/*     */     }
/*     */     
/*     */     public <V> V get(Object key)
/*     */     {
/* 590 */       DefaultHttp2Connection.DefaultStream.access$1202(this.stream, new DefaultHttp2Connection.DefaultProperyMap(4));
/* 591 */       return (V)DefaultHttp2Connection.DefaultStream.access$1200(this.stream).get(key);
/*     */     }
/*     */     
/*     */     public <V> V remove(Object key)
/*     */     {
/* 596 */       DefaultHttp2Connection.DefaultStream.access$1202(this.stream, new DefaultHttp2Connection.DefaultProperyMap(4));
/* 597 */       return (V)DefaultHttp2Connection.DefaultStream.access$1200(this.stream).remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   private static IntObjectMap<DefaultStream> newChildMap() {
/* 602 */     return new IntObjectHashMap(4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class ParentChangedEvent
/*     */   {
/*     */     private final Http2Stream stream;
/*     */     
/*     */ 
/*     */     private final Http2Stream oldParent;
/*     */     
/*     */ 
/*     */ 
/*     */     ParentChangedEvent(Http2Stream stream, Http2Stream oldParent)
/*     */     {
/* 618 */       this.stream = stream;
/* 619 */       this.oldParent = oldParent;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void notifyListener(Http2Connection.Listener l)
/*     */     {
/* 627 */       l.priorityTreeParentChanged(this.stream, this.oldParent);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void notifyParentChanged(List<ParentChangedEvent> events)
/*     */   {
/*     */     ParentChangedEvent event;
/*     */     
/* 636 */     for (int i = 0; i < events.size(); i++) {
/* 637 */       event = (ParentChangedEvent)events.get(i);
/* 638 */       for (Http2Connection.Listener l : this.listeners) {
/* 639 */         event.notifyListener(l);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void notifyParentChanging(Http2Stream stream, Http2Stream newParent) {
/* 645 */     for (Http2Connection.Listener l : this.listeners) {
/* 646 */       l.priorityTreeParentChanging(stream, newParent);
/*     */     }
/*     */   }
/*     */   
/*     */   private final class ConnectionStream
/*     */     extends DefaultHttp2Connection.DefaultStream
/*     */   {
/*     */     ConnectionStream()
/*     */     {
/* 655 */       super(0);
/*     */     }
/*     */     
/*     */     public Http2Stream setPriority(int parentStreamId, short weight, boolean exclusive)
/*     */     {
/* 660 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Http2Stream open(boolean halfClosed)
/*     */     {
/* 665 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Http2Stream close()
/*     */     {
/* 670 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Http2Stream closeLocalSide()
/*     */     {
/* 675 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public Http2Stream closeRemoteSide()
/*     */     {
/* 680 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class DefaultEndpoint<F extends Http2FlowController>
/*     */     implements Http2Connection.Endpoint<F>
/*     */   {
/*     */     private final boolean server;
/*     */     private int nextStreamId;
/*     */     private int lastStreamCreated;
/* 691 */     private int lastKnownStream = -1;
/* 692 */     private boolean pushToAllowed = true;
/*     */     
/*     */ 
/*     */     private F flowController;
/*     */     
/*     */ 
/*     */     private int maxStreams;
/*     */     
/*     */ 
/*     */     private int numActiveStreams;
/*     */     
/*     */ 
/*     */     DefaultEndpoint(boolean server)
/*     */     {
/* 706 */       this.server = server;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 712 */       this.nextStreamId = (server ? 2 : 1);
/*     */       
/*     */ 
/* 715 */       this.pushToAllowed = (!server);
/* 716 */       this.maxStreams = Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public int nextStreamId()
/*     */     {
/* 723 */       return this.nextStreamId > 1 ? this.nextStreamId : this.nextStreamId + 2;
/*     */     }
/*     */     
/*     */     public boolean createdStreamId(int streamId)
/*     */     {
/* 728 */       boolean even = (streamId & 0x1) == 0;
/* 729 */       return this.server == even;
/*     */     }
/*     */     
/*     */     public boolean acceptingNewStreams()
/*     */     {
/* 734 */       return (nextStreamId() > 0) && (this.numActiveStreams + 1 <= this.maxStreams);
/*     */     }
/*     */     
/*     */     public DefaultHttp2Connection.DefaultStream createStream(int streamId) throws Http2Exception
/*     */     {
/* 739 */       checkNewStreamAllowed(streamId);
/*     */       
/*     */ 
/* 742 */       DefaultHttp2Connection.DefaultStream stream = new DefaultHttp2Connection.DefaultStream(DefaultHttp2Connection.this, streamId);
/*     */       
/*     */ 
/* 745 */       this.nextStreamId = (streamId + 2);
/* 746 */       this.lastStreamCreated = streamId;
/*     */       
/* 748 */       addStream(stream);
/* 749 */       return stream;
/*     */     }
/*     */     
/*     */     public boolean isServer()
/*     */     {
/* 754 */       return this.server;
/*     */     }
/*     */     
/*     */     public DefaultHttp2Connection.DefaultStream reservePushStream(int streamId, Http2Stream parent) throws Http2Exception
/*     */     {
/* 759 */       if (parent == null) {
/* 760 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Parent stream missing", new Object[0]);
/*     */       }
/* 762 */       if (isLocal() ? !parent.localSideOpen() : !parent.remoteSideOpen()) {
/* 763 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d is not open for sending push promise", new Object[] { Integer.valueOf(parent.id()) });
/*     */       }
/* 765 */       if (!opposite().allowPushTo()) {
/* 766 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server push not allowed to opposite endpoint.", new Object[0]);
/*     */       }
/* 768 */       checkNewStreamAllowed(streamId);
/*     */       
/*     */ 
/* 771 */       DefaultHttp2Connection.DefaultStream stream = new DefaultHttp2Connection.DefaultStream(DefaultHttp2Connection.this, streamId);
/* 772 */       DefaultHttp2Connection.DefaultStream.access$1302(stream, isLocal() ? Http2Stream.State.RESERVED_LOCAL : Http2Stream.State.RESERVED_REMOTE);
/*     */       
/*     */ 
/* 775 */       this.nextStreamId = (streamId + 2);
/* 776 */       this.lastStreamCreated = streamId;
/*     */       
/*     */ 
/* 779 */       addStream(stream);
/* 780 */       return stream;
/*     */     }
/*     */     
/*     */     private void addStream(DefaultHttp2Connection.DefaultStream stream)
/*     */     {
/* 785 */       DefaultHttp2Connection.this.streamMap.put(stream.id(), stream);
/* 786 */       List<DefaultHttp2Connection.ParentChangedEvent> events = new ArrayList(1);
/* 787 */       DefaultHttp2Connection.this.connectionStream.takeChild(stream, false, events);
/*     */       
/*     */ 
/* 790 */       for (Http2Connection.Listener listener : DefaultHttp2Connection.this.listeners) {
/* 791 */         listener.streamAdded(stream);
/*     */       }
/*     */       
/* 794 */       DefaultHttp2Connection.this.notifyParentChanged(events);
/*     */     }
/*     */     
/*     */     public void allowPushTo(boolean allow)
/*     */     {
/* 799 */       if ((allow) && (this.server)) {
/* 800 */         throw new IllegalArgumentException("Servers do not allow push");
/*     */       }
/* 802 */       this.pushToAllowed = allow;
/*     */     }
/*     */     
/*     */     public boolean allowPushTo()
/*     */     {
/* 807 */       return this.pushToAllowed;
/*     */     }
/*     */     
/*     */     public int numActiveStreams()
/*     */     {
/* 812 */       return this.numActiveStreams;
/*     */     }
/*     */     
/*     */     public int maxStreams()
/*     */     {
/* 817 */       return this.maxStreams;
/*     */     }
/*     */     
/*     */     public void maxStreams(int maxStreams)
/*     */     {
/* 822 */       this.maxStreams = maxStreams;
/*     */     }
/*     */     
/*     */     public int lastStreamCreated()
/*     */     {
/* 827 */       return this.lastStreamCreated;
/*     */     }
/*     */     
/*     */     public int lastKnownStream()
/*     */     {
/* 832 */       return this.lastKnownStream >= 0 ? this.lastKnownStream : this.lastStreamCreated;
/*     */     }
/*     */     
/*     */     private void lastKnownStream(int lastKnownStream) {
/* 836 */       boolean alreadyNotified = DefaultHttp2Connection.this.isGoAway();
/* 837 */       this.lastKnownStream = lastKnownStream;
/* 838 */       if (!alreadyNotified) {
/* 839 */         notifyGoingAway();
/*     */       }
/*     */     }
/*     */     
/*     */     private void notifyGoingAway() {
/* 844 */       for (Http2Connection.Listener listener : DefaultHttp2Connection.this.listeners) {
/* 845 */         listener.goingAway();
/*     */       }
/*     */     }
/*     */     
/*     */     public F flowController()
/*     */     {
/* 851 */       return this.flowController;
/*     */     }
/*     */     
/*     */     public void flowController(F flowController)
/*     */     {
/* 856 */       this.flowController = ((Http2FlowController)ObjectUtil.checkNotNull(flowController, "flowController"));
/*     */     }
/*     */     
/*     */     public Http2Connection.Endpoint<? extends Http2FlowController> opposite()
/*     */     {
/* 861 */       return isLocal() ? DefaultHttp2Connection.this.remoteEndpoint : DefaultHttp2Connection.this.localEndpoint;
/*     */     }
/*     */     
/*     */     private void checkNewStreamAllowed(int streamId) throws Http2Exception {
/* 865 */       if (DefaultHttp2Connection.this.isGoAway()) {
/* 866 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Cannot create a stream since the connection is going away", new Object[0]);
/*     */       }
/* 868 */       verifyStreamId(streamId);
/* 869 */       if (!acceptingNewStreams()) {
/* 870 */         throw Http2Exception.connectionError(Http2Error.REFUSED_STREAM, "Maximum streams exceeded for this endpoint.", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*     */     private void verifyStreamId(int streamId) throws Http2Exception {
/* 875 */       if (streamId < 0) {
/* 876 */         throw new Http2NoMoreStreamIdsException();
/*     */       }
/* 878 */       if (streamId < this.nextStreamId) {
/* 879 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is behind the next expected stream %d", new Object[] { Integer.valueOf(streamId), Integer.valueOf(this.nextStreamId) });
/*     */       }
/*     */       
/* 882 */       if (!createdStreamId(streamId)) {
/* 883 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Request stream %d is not correct for %s connection", new Object[] { Integer.valueOf(streamId), this.server ? "server" : "client" });
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean isLocal()
/*     */     {
/* 889 */       return this == DefaultHttp2Connection.this.localEndpoint;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */