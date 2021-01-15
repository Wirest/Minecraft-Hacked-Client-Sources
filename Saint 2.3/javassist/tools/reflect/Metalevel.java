package javassist.tools.reflect;

public interface Metalevel {
   ClassMetaobject _getClass();

   Metaobject _getMetaobject();

   void _setMetaobject(Metaobject m);
}
