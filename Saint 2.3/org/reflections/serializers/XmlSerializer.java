package org.reflections.serializers;

import com.google.common.collect.Multimap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.Utils;

public class XmlSerializer implements Serializer {
   public Reflections read(InputStream inputStream) {
      Reflections reflections;
      try {
         Constructor constructor = Reflections.class.getDeclaredConstructor();
         constructor.setAccessible(true);
         reflections = (Reflections)constructor.newInstance();
      } catch (Exception var16) {
         reflections = new Reflections(new ConfigurationBuilder());
      }

      Document document;
      try {
         document = (new SAXReader()).read(inputStream);
      } catch (DocumentException var15) {
         throw new RuntimeException(var15);
      }

      Iterator i$ = document.getRootElement().elements().iterator();

      while(i$.hasNext()) {
         Object e1 = i$.next();
         Element index = (Element)e1;
         Iterator i$ = index.elements().iterator();

         while(i$.hasNext()) {
            Object e2 = i$.next();
            Element entry = (Element)e2;
            Element key = entry.element("key");
            Element values = entry.element("values");
            Iterator i$ = values.elements().iterator();

            while(i$.hasNext()) {
               Object o3 = i$.next();
               Element value = (Element)o3;
               reflections.getStore().getOrCreate(index.getName()).put(key.getText(), value.getText());
            }
         }
      }

      return reflections;
   }

   public File save(Reflections reflections, String filename) {
      File file = Utils.prepareFile(filename);
      Document document = this.createDocument(reflections);

      try {
         XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
         xmlWriter.write(document);
         xmlWriter.close();
         return file;
      } catch (IOException var6) {
         throw new ReflectionsException("could not save to file " + filename, var6);
      }
   }

   public String toString(Reflections reflections) {
      Document document = this.createDocument(reflections);

      try {
         StringWriter writer = new StringWriter();
         XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
         xmlWriter.write(document);
         xmlWriter.close();
         return writer.toString();
      } catch (IOException var5) {
         throw new RuntimeException();
      }
   }

   private Document createDocument(Reflections reflections) {
      Map map = reflections.getStore().getStoreMap();
      Document document = DocumentFactory.getInstance().createDocument();
      Element root = document.addElement("Reflections");
      Iterator i$ = map.keySet().iterator();

      while(i$.hasNext()) {
         String indexName = (String)i$.next();
         Element indexElement = root.addElement(indexName);
         Iterator i$ = ((Multimap)map.get(indexName)).keySet().iterator();

         while(i$.hasNext()) {
            String key = (String)i$.next();
            Element entryElement = indexElement.addElement("entry");
            entryElement.addElement("key").setText(key);
            Element valuesElement = entryElement.addElement("values");
            Iterator i$ = ((Multimap)map.get(indexName)).get(key).iterator();

            while(i$.hasNext()) {
               String value = (String)i$.next();
               valuesElement.addElement("value").setText(value);
            }
         }
      }

      return document;
   }
}
