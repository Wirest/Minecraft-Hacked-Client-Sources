package awfdd.ksksk;

import java.lang.reflect.Method;

import awfdd.ksksk.ap.zajkb.rgds.Event;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class dabehfb {
	public static String getClassType(Class<?> clazz) {
		return clazz.getName().replace(".", "/");
	}

	public static String getClassObjectType(Class<?> clazz) {
		return "L" + getClassType(clazz) + ";";
	}

	public static String getArrayObjectType(Class<?> clazz) {
		return "[" + getClassObjectType(clazz);
	}

	public static String getListenerMethodType(String eventClassName) {
		return "(L" + eventClassName + ";)V";
	}

	public static String getFilterMethodType() {
		return "(L" + getClassType(Event.class) + ";)Z";
	}

	public static String getMethodType(Method method) {
		return Type.getMethodDescriptor(method);
	}

	public static AbstractInsnNode getOptimizedIndex(int index) {
		if(index <= 5) {
			switch(index) {
			case 0:
				return new InsnNode(Opcodes.ICONST_0);
			case 1:
				return new InsnNode(Opcodes.ICONST_1);
			case 2:
				return new InsnNode(Opcodes.ICONST_2);
			case 3:
				return new InsnNode(Opcodes.ICONST_3);
			case 4:
				return new InsnNode(Opcodes.ICONST_4);
			case 5:
				return new InsnNode(Opcodes.ICONST_5);
			}
		} else if(index <= Byte.MAX_VALUE) {
			return new IntInsnNode(Opcodes.BIPUSH, index);
		} else if(index <= Short.MAX_VALUE) {
			return new IntInsnNode(Opcodes.SIPUSH, index);
		} else {
			return new LdcInsnNode(index);
		}
		return null;
	}

	public static AbstractInsnNode[] getObject2PrimitiveConverter(Class<?> type) {
		String classType = null;
		String convertMethod = null;
		String convertMethodType = null;
		if(type == int.class) {
			classType = dabehfb.getClassType(Integer.class);
			convertMethod = "intValue";
			convertMethodType = "()I";
		} else if(type == boolean.class) {
			classType = dabehfb.getClassType(Boolean.class);
			convertMethod = "booleanValue";
			convertMethodType = "()Z";
		} else if(type == byte.class) {
			classType = dabehfb.getClassType(Byte.class);
			convertMethod = "byteValue";
			convertMethodType = "()B";
		} else if(type == char.class) {
			classType = dabehfb.getClassType(Character.class);
			convertMethod = "charValue";
			convertMethodType = "()C";
		} else if(type == double.class) {
			classType = dabehfb.getClassType(Double.class);
			convertMethod = "doubleValue";
			convertMethodType = "()D";
		} else if(type == float.class) {
			classType = dabehfb.getClassType(Float.class);
			convertMethod = "floatValue";
			convertMethodType = "()F";
		} else if(type == long.class) {
			classType = dabehfb.getClassType(Long.class);
			convertMethod = "longValue";
			convertMethodType = "()J";
		} else if(type == short.class) {
			classType = dabehfb.getClassType(Short.class);
			convertMethod = "shortValue";
			convertMethodType = "()S";
		}
		if(classType != null) {
			return new AbstractInsnNode[]{new TypeInsnNode(Opcodes.CHECKCAST, classType), new MethodInsnNode(Opcodes.INVOKEVIRTUAL, classType, convertMethod, convertMethodType)};
		}
		return null;
	}

	public static AbstractInsnNode getPrimitive2ObjectConverter(Class<?> type) {
		String classType = null;
		String convertMethod = null;
		String convertMethodType = null;
		if(type == int.class) {
			classType = dabehfb.getClassType(Integer.class);
			convertMethod = "valueOf";
			convertMethodType = "(I)Ljava/lang/Integer;";
		} else if(type == boolean.class) {
			classType = dabehfb.getClassType(Boolean.class);
			convertMethod = "valueOf";
			convertMethodType = "(Z)Ljava/lang/Boolean;";
		} else if(type == byte.class) {
			classType = dabehfb.getClassType(Byte.class);
			convertMethod = "valueOf";
			convertMethodType = "(B)Ljava/lang/Byte;";
		} else if(type == char.class) {
			classType = dabehfb.getClassType(Character.class);
			convertMethod = "valueOf";
			convertMethodType = "(C)Ljava/lang/Character;";
		} else if(type == double.class) {
			classType = dabehfb.getClassType(Double.class);
			convertMethod = "valueOf";
			convertMethodType = "(D)Ljava/lang/Double;";
		} else if(type == float.class) {
			classType = dabehfb.getClassType(Float.class);
			convertMethod = "valueOf";
			convertMethodType = "(F)Ljava/lang/Float;";
		} else if(type == long.class) {
			classType = dabehfb.getClassType(Long.class);
			convertMethod = "valueOf";
			convertMethodType = "(J)Ljava/lang/Long;";
		} else if(type == short.class) {
			classType = dabehfb.getClassType(Short.class);
			convertMethod = "valueOf";
			convertMethodType = "(S)Ljava/lang/Short;";
		}
		if(classType != null) {
			return new MethodInsnNode(Opcodes.INVOKESTATIC, classType, convertMethod, convertMethodType);
		}
		return null;
	}
}