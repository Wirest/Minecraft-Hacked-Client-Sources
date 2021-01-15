package javassist.tools.reflect;

public class Sample {
   private Metaobject _metaobject;
   private static ClassMetaobject _classobject;

   public Object trap(Object[] args, int identifier) throws Throwable {
      Metaobject mobj = this._metaobject;
      return mobj == null ? ClassMetaobject.invoke(this, identifier, args) : mobj.trapMethodcall(identifier, args);
   }

   public static Object trapStatic(Object[] args, int identifier) throws Throwable {
      return _classobject.trapMethodcall(identifier, args);
   }

   public static Object trapRead(Object[] args, String name) {
      return args[0] == null ? _classobject.trapFieldRead(name) : ((Metalevel)args[0])._getMetaobject().trapFieldRead(name);
   }

   public static Object trapWrite(Object[] args, String name) {
      Metalevel base = (Metalevel)args[0];
      if (base == null) {
         _classobject.trapFieldWrite(name, args[1]);
      } else {
         base._getMetaobject().trapFieldWrite(name, args[1]);
      }

      return null;
   }
}
