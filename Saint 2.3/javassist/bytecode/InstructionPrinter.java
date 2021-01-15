package javassist.bytecode;

import java.io.PrintStream;
import javassist.CtMethod;

public class InstructionPrinter implements Opcode {
   private static final String[] opcodes;
   private final PrintStream stream;

   public InstructionPrinter(PrintStream stream) {
      this.stream = stream;
   }

   public static void print(CtMethod method, PrintStream stream) {
      (new InstructionPrinter(stream)).print(method);
   }

   public void print(CtMethod method) {
      MethodInfo info = method.getMethodInfo2();
      ConstPool pool = info.getConstPool();
      CodeAttribute code = info.getCodeAttribute();
      if (code != null) {
         int pos;
         for(CodeIterator iterator = code.iterator(); iterator.hasNext(); this.stream.println(pos + ": " + instructionString(iterator, pos, pool))) {
            try {
               pos = iterator.next();
            } catch (BadBytecode var8) {
               throw new RuntimeException(var8);
            }
         }

      }
   }

   public static String instructionString(CodeIterator iter, int pos, ConstPool pool) {
      int opcode = iter.byteAt(pos);
      if (opcode <= opcodes.length && opcode >= 0) {
         String opstring = opcodes[opcode];
         switch(opcode) {
         case 16:
            return opstring + " " + iter.byteAt(pos + 1);
         case 17:
            return opstring + " " + iter.s16bitAt(pos + 1);
         case 18:
            return opstring + " " + ldc(pool, iter.byteAt(pos + 1));
         case 19:
         case 20:
            return opstring + " " + ldc(pool, iter.u16bitAt(pos + 1));
         case 21:
         case 22:
         case 23:
         case 24:
         case 25:
         case 54:
         case 55:
         case 56:
         case 57:
         case 58:
            return opstring + " " + iter.byteAt(pos + 1);
         case 26:
         case 27:
         case 28:
         case 29:
         case 30:
         case 31:
         case 32:
         case 33:
         case 34:
         case 35:
         case 36:
         case 37:
         case 38:
         case 39:
         case 40:
         case 41:
         case 42:
         case 43:
         case 44:
         case 45:
         case 46:
         case 47:
         case 48:
         case 49:
         case 50:
         case 51:
         case 52:
         case 53:
         case 59:
         case 60:
         case 61:
         case 62:
         case 63:
         case 64:
         case 65:
         case 66:
         case 67:
         case 68:
         case 69:
         case 70:
         case 71:
         case 72:
         case 73:
         case 74:
         case 75:
         case 76:
         case 77:
         case 78:
         case 79:
         case 80:
         case 81:
         case 82:
         case 83:
         case 84:
         case 85:
         case 86:
         case 87:
         case 88:
         case 89:
         case 90:
         case 91:
         case 92:
         case 93:
         case 94:
         case 95:
         case 96:
         case 97:
         case 98:
         case 99:
         case 100:
         case 101:
         case 102:
         case 103:
         case 104:
         case 105:
         case 106:
         case 107:
         case 108:
         case 109:
         case 110:
         case 111:
         case 112:
         case 113:
         case 114:
         case 115:
         case 116:
         case 117:
         case 118:
         case 119:
         case 120:
         case 121:
         case 122:
         case 123:
         case 124:
         case 125:
         case 126:
         case 127:
         case 128:
         case 129:
         case 130:
         case 131:
         case 133:
         case 134:
         case 135:
         case 136:
         case 137:
         case 138:
         case 139:
         case 140:
         case 141:
         case 142:
         case 143:
         case 144:
         case 145:
         case 146:
         case 147:
         case 148:
         case 149:
         case 150:
         case 151:
         case 152:
         case 172:
         case 173:
         case 174:
         case 175:
         case 176:
         case 177:
         case 190:
         case 191:
         case 193:
         case 194:
         case 195:
         default:
            return opstring;
         case 132:
            return opstring + " " + iter.byteAt(pos + 1) + ", " + iter.signedByteAt(pos + 2);
         case 153:
         case 154:
         case 155:
         case 156:
         case 157:
         case 158:
         case 159:
         case 160:
         case 161:
         case 162:
         case 163:
         case 164:
         case 165:
         case 166:
         case 198:
         case 199:
            return opstring + " " + (iter.s16bitAt(pos + 1) + pos);
         case 167:
         case 168:
            return opstring + " " + (iter.s16bitAt(pos + 1) + pos);
         case 169:
            return opstring + " " + iter.byteAt(pos + 1);
         case 170:
            return tableSwitch(iter, pos);
         case 171:
            return lookupSwitch(iter, pos);
         case 178:
         case 179:
         case 180:
         case 181:
            return opstring + " " + fieldInfo(pool, iter.u16bitAt(pos + 1));
         case 182:
         case 183:
         case 184:
            return opstring + " " + methodInfo(pool, iter.u16bitAt(pos + 1));
         case 185:
            return opstring + " " + interfaceMethodInfo(pool, iter.u16bitAt(pos + 1));
         case 186:
            return opstring + " " + iter.u16bitAt(pos + 1);
         case 187:
            return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
         case 188:
            return opstring + " " + arrayInfo(iter.byteAt(pos + 1));
         case 189:
         case 192:
            return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
         case 196:
            return wide(iter, pos);
         case 197:
            return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
         case 200:
         case 201:
            return opstring + " " + (iter.s32bitAt(pos + 1) + pos);
         }
      } else {
         throw new IllegalArgumentException("Invalid opcode, opcode: " + opcode + " pos: " + pos);
      }
   }

   private static String wide(CodeIterator iter, int pos) {
      int opcode = iter.byteAt(pos + 1);
      int index = iter.u16bitAt(pos + 2);
      switch(opcode) {
      case 21:
      case 22:
      case 23:
      case 24:
      case 25:
      case 54:
      case 55:
      case 56:
      case 57:
      case 58:
      case 132:
      case 169:
         return opcodes[opcode] + " " + index;
      default:
         throw new RuntimeException("Invalid WIDE operand");
      }
   }

   private static String arrayInfo(int type) {
      switch(type) {
      case 4:
         return "boolean";
      case 5:
         return "char";
      case 6:
         return "float";
      case 7:
         return "double";
      case 8:
         return "byte";
      case 9:
         return "short";
      case 10:
         return "int";
      case 11:
         return "long";
      default:
         throw new RuntimeException("Invalid array type");
      }
   }

   private static String classInfo(ConstPool pool, int index) {
      return "#" + index + " = Class " + pool.getClassInfo(index);
   }

   private static String interfaceMethodInfo(ConstPool pool, int index) {
      return "#" + index + " = Method " + pool.getInterfaceMethodrefClassName(index) + "." + pool.getInterfaceMethodrefName(index) + "(" + pool.getInterfaceMethodrefType(index) + ")";
   }

   private static String methodInfo(ConstPool pool, int index) {
      return "#" + index + " = Method " + pool.getMethodrefClassName(index) + "." + pool.getMethodrefName(index) + "(" + pool.getMethodrefType(index) + ")";
   }

   private static String fieldInfo(ConstPool pool, int index) {
      return "#" + index + " = Field " + pool.getFieldrefClassName(index) + "." + pool.getFieldrefName(index) + "(" + pool.getFieldrefType(index) + ")";
   }

   private static String lookupSwitch(CodeIterator iter, int pos) {
      StringBuffer buffer = new StringBuffer("lookupswitch {\n");
      int index = (pos & -4) + 4;
      buffer.append("\t\tdefault: ").append(pos + iter.s32bitAt(index)).append("\n");
      index += 4;
      int npairs = iter.s32bitAt(index);
      int var10000 = npairs * 8;
      index += 4;

      for(int end = var10000 + index; index < end; index += 8) {
         int match = iter.s32bitAt(index);
         int target = iter.s32bitAt(index + 4) + pos;
         buffer.append("\t\t").append(match).append(": ").append(target).append("\n");
      }

      buffer.setCharAt(buffer.length() - 1, '}');
      return buffer.toString();
   }

   private static String tableSwitch(CodeIterator iter, int pos) {
      StringBuffer buffer = new StringBuffer("tableswitch {\n");
      int index = (pos & -4) + 4;
      buffer.append("\t\tdefault: ").append(pos + iter.s32bitAt(index)).append("\n");
      index += 4;
      int low = iter.s32bitAt(index);
      index += 4;
      int high = iter.s32bitAt(index);
      int var10000 = (high - low + 1) * 4;
      index += 4;
      int end = var10000 + index;

      for(int key = low; index < end; ++key) {
         int target = iter.s32bitAt(index) + pos;
         buffer.append("\t\t").append(key).append(": ").append(target).append("\n");
         index += 4;
      }

      buffer.setCharAt(buffer.length() - 1, '}');
      return buffer.toString();
   }

   private static String ldc(ConstPool pool, int index) {
      int tag = pool.getTag(index);
      switch(tag) {
      case 3:
         return "#" + index + " = int " + pool.getIntegerInfo(index);
      case 4:
         return "#" + index + " = float " + pool.getFloatInfo(index);
      case 5:
         return "#" + index + " = long " + pool.getLongInfo(index);
      case 6:
         return "#" + index + " = int " + pool.getDoubleInfo(index);
      case 7:
         return classInfo(pool, index);
      case 8:
         return "#" + index + " = \"" + pool.getStringInfo(index) + "\"";
      default:
         throw new RuntimeException("bad LDC: " + tag);
      }
   }

   static {
      opcodes = Mnemonic.OPCODE;
   }
}
