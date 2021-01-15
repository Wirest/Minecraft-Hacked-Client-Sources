package io.netty.util;

public abstract interface ReferenceCounted
{
  public abstract int refCnt();
  
  public abstract ReferenceCounted retain();
  
  public abstract ReferenceCounted retain(int paramInt);
  
  public abstract ReferenceCounted touch();
  
  public abstract ReferenceCounted touch(Object paramObject);
  
  public abstract boolean release();
  
  public abstract boolean release(int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\ReferenceCounted.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */