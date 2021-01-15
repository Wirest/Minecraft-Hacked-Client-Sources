package io.netty.handler.codec.http2;

import java.util.Collection;

public abstract interface Http2Connection
{
  public abstract void addListener(Listener paramListener);
  
  public abstract void removeListener(Listener paramListener);
  
  public abstract Http2Stream requireStream(int paramInt)
    throws Http2Exception;
  
  public abstract Http2Stream stream(int paramInt);
  
  public abstract Http2Stream connectionStream();
  
  public abstract int numActiveStreams();
  
  public abstract Collection<Http2Stream> activeStreams();
  
  public abstract void deactivate(Http2Stream paramHttp2Stream);
  
  public abstract boolean isServer();
  
  public abstract Endpoint<Http2LocalFlowController> local();
  
  public abstract Http2Stream createLocalStream(int paramInt)
    throws Http2Exception;
  
  public abstract Endpoint<Http2RemoteFlowController> remote();
  
  public abstract Http2Stream createRemoteStream(int paramInt)
    throws Http2Exception;
  
  public abstract boolean goAwayReceived();
  
  public abstract void goAwayReceived(int paramInt);
  
  public abstract boolean goAwaySent();
  
  public abstract void goAwaySent(int paramInt);
  
  public abstract boolean isGoAway();
  
  public static abstract interface Endpoint<F extends Http2FlowController>
  {
    public abstract int nextStreamId();
    
    public abstract boolean createdStreamId(int paramInt);
    
    public abstract boolean acceptingNewStreams();
    
    public abstract Http2Stream createStream(int paramInt)
      throws Http2Exception;
    
    public abstract Http2Stream reservePushStream(int paramInt, Http2Stream paramHttp2Stream)
      throws Http2Exception;
    
    public abstract boolean isServer();
    
    public abstract void allowPushTo(boolean paramBoolean);
    
    public abstract boolean allowPushTo();
    
    public abstract int numActiveStreams();
    
    public abstract int maxStreams();
    
    public abstract void maxStreams(int paramInt);
    
    public abstract int lastStreamCreated();
    
    public abstract int lastKnownStream();
    
    public abstract F flowController();
    
    public abstract void flowController(F paramF);
    
    public abstract Endpoint<? extends Http2FlowController> opposite();
  }
  
  public static abstract interface Listener
  {
    public abstract void streamAdded(Http2Stream paramHttp2Stream);
    
    public abstract void streamActive(Http2Stream paramHttp2Stream);
    
    public abstract void streamHalfClosed(Http2Stream paramHttp2Stream);
    
    public abstract void streamInactive(Http2Stream paramHttp2Stream);
    
    public abstract void streamRemoved(Http2Stream paramHttp2Stream);
    
    public abstract void priorityTreeParentChanged(Http2Stream paramHttp2Stream1, Http2Stream paramHttp2Stream2);
    
    public abstract void priorityTreeParentChanging(Http2Stream paramHttp2Stream1, Http2Stream paramHttp2Stream2);
    
    public abstract void onWeightChanged(Http2Stream paramHttp2Stream, short paramShort);
    
    public abstract void goingAway();
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2Connection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */