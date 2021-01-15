package io.netty.handler.codec.http2;

public abstract interface Http2HeaderTable
{
  public abstract void maxHeaderTableSize(int paramInt)
    throws Http2Exception;
  
  public abstract int maxHeaderTableSize();
  
  public abstract void maxHeaderListSize(int paramInt)
    throws Http2Exception;
  
  public abstract int maxHeaderListSize();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2HeaderTable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */