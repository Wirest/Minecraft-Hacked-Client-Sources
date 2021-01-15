package ch.qos.logback.core.pattern;

public abstract interface PostCompileProcessor<E>
{
  public abstract void process(Converter<E> paramConverter);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\PostCompileProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */