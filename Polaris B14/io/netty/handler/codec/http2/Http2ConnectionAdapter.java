package io.netty.handler.codec.http2;

public class Http2ConnectionAdapter
  implements Http2Connection.Listener
{
  public void streamAdded(Http2Stream stream) {}
  
  public void streamActive(Http2Stream stream) {}
  
  public void streamHalfClosed(Http2Stream stream) {}
  
  public void streamInactive(Http2Stream stream) {}
  
  public void streamRemoved(Http2Stream stream) {}
  
  public void goingAway() {}
  
  public void priorityTreeParentChanged(Http2Stream stream, Http2Stream oldParent) {}
  
  public void priorityTreeParentChanging(Http2Stream stream, Http2Stream newParent) {}
  
  public void onWeightChanged(Http2Stream stream, short oldWeight) {}
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2ConnectionAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */