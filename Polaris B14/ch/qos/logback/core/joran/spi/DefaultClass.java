package ch.qos.logback.core.joran.spi;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface DefaultClass
{
  Class<?> value();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\spi\DefaultClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */