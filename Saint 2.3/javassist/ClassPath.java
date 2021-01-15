package javassist;

import java.io.InputStream;
import java.net.URL;

public interface ClassPath {
   InputStream openClassfile(String classname) throws NotFoundException;

   URL find(String classname);

   void close();
}
