package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILTypeContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;

import static org.objectweb.asm.Opcodes.*;

public class InsnCastProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        AILType origin = component.asType(0).get().getType();
        AILType target = component.asType(1).get().getType();

        visitCast(visitor, origin, target);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
    }

    public static void visitCast(BytecodeVisitor visitor, AILType origin, AILType target) {
        System.out.println("CASTING " + origin.name() + " TO " + target.name());
        if (origin == AILType.TEXT || target == AILType.TEXT) {
            ParseInvoker invoker = ParseProcess.get(origin, target);
            if (invoker == null)
                return;

            if (invoker == ParseInvoker.CHAR_AT)
                visitor.visitPushInsn(AILType.INT, 0);

            invoker.invoke(visitor);
            return;
        }

        int[] opcodes = CastProcess.get(origin, target);
        for (int opcode : opcodes) {
            visitor.current().visitInsn(opcode);
        }
    }

    public enum ParseInvoker {
        INT_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(I)Ljava/lang/String;"),
        DOUBLE_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(D)Ljava/lang/String;"),
        BYTE_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(B)Ljava/lang/String;"),
        FLOAT_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(F)Ljava/lang/String;"),
        BOOL_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(Z)Ljava/lang/String;"),
        SHORT_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(S)Ljava/lang/String;"),
        CHAR_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(C)Ljava/lang/String;"),
        LONG_VALUEOF(INVOKESTATIC, "java/lang/String", "valueOf", "(J)Ljava/lang/String;"),

        INT_PARSE(INVOKESTATIC, "java/lang/Integer", "parseInt", "(Ljava/lang/String;)I"),
        DOUBLE_PARSE(INVOKESTATIC, "java/lang/Double", "parseDouble", "(Ljava/lang/String;)D"),
        BYTE_PARSE(INVOKESTATIC, "java/lang/Byte", "parseByte", "(Ljava/lang/String;)B"),
        FLOAT_PARSE(INVOKESTATIC, "java/lang/Float", "parseFloat", "(Ljava/lang/String;)F"),
        BOOL_PARSE(INVOKESTATIC, "java/lang/Boolean", "parseBoolean", "(Ljava/lang/String;)Z"),
        SHORT_PARSE(INVOKESTATIC, "java/lang/Short", "parseShort", "(Ljava/lang/String;)S"),
        LONG_PARSE(INVOKESTATIC, "java/lang/Long", "parseLong", "(Ljava/lang/String;)J"),

        CHAR_AT(INVOKEVIRTUAL, "java/lang/String", "charAt", "(I)C");

        public void invoke(BytecodeVisitor visitor) {
            visitor.current().visitMethodInsn(
                    this.getOpcode(),
                    this.getOwner(),
                    this.getName(),
                    this.getDescriptor(),
                    false);
        }

        private final int opcode;
        private final String owner;
        private final String name;
        private final String descriptor;

        ParseInvoker(int opcode, String owner, String name, String descriptor) {
            this.opcode = opcode;
            this.owner = owner;
            this.name = name;
            this.descriptor = descriptor;
        }

        public int getOpcode() {
            return opcode;
        }

        public String getOwner() {
            return owner;
        }

        public String getName() {
            return name;
        }

        public String getDescriptor() {
            return descriptor;
        }
    }

    public enum ParseProcess {
        INT_TO_TEXT(AILType.INT, AILType.TEXT, ParseInvoker.INT_VALUEOF),
        DOUBLE_TO_TEXT(AILType.DOUBLE, AILType.TEXT, ParseInvoker.DOUBLE_VALUEOF),
        BYTE_TO_TEXT(AILType.BYTE, AILType.TEXT, ParseInvoker.BYTE_VALUEOF),
        FLOAT_TO_TEXT(AILType.FLOAT, AILType.TEXT, ParseInvoker.FLOAT_VALUEOF),
        SHORT_TO_TEXT(AILType.SHORT, AILType.TEXT, ParseInvoker.SHORT_VALUEOF),
        CHAR_TO_TEXT(AILType.CHAR, AILType.TEXT, ParseInvoker.CHAR_VALUEOF),
        LONG_TO_TEXT(AILType.LONG, AILType.TEXT, ParseInvoker.LONG_VALUEOF),
        BOOL_TO_TEXT(AILType.BOOL, AILType.TEXT, ParseInvoker.BOOL_VALUEOF),

        TEXT_TO_INT(AILType.TEXT, AILType.INT, ParseInvoker.INT_PARSE),
        TEXT_TO_DOUBLE(AILType.TEXT, AILType.DOUBLE, ParseInvoker.DOUBLE_PARSE),
        TEXT_TO_BYTE(AILType.TEXT, AILType.BYTE, ParseInvoker.BYTE_PARSE),
        TEXT_TO_FLOAT(AILType.TEXT, AILType.FLOAT, ParseInvoker.FLOAT_PARSE),
        TEXT_TO_SHORT(AILType.TEXT, AILType.SHORT, ParseInvoker.SHORT_PARSE),
        TEXT_TO_CHAR(AILType.TEXT, AILType.CHAR, ParseInvoker.CHAR_AT),
        TEXT_TO_LONG(AILType.TEXT, AILType.LONG, ParseInvoker.LONG_PARSE),
        TEXT_TO_BOOL(AILType.TEXT, AILType.BOOL, ParseInvoker.BOOL_PARSE);

        public static ParseInvoker get(AILType origin, AILType target) {
            for (ParseProcess process : ParseProcess.values()) {
                if (process.getOrigin() == origin
                        && process.getTarget() == target)
                    return process.getInvoker();
            }
            return null;
        }

        private final AILType origin;
        private final AILType target;
        private final ParseInvoker invoker;

        ParseProcess(AILType origin, AILType target, ParseInvoker invoker) {
            this.origin = origin;
            this.target = target;
            this.invoker = invoker;
        }

        public AILType getOrigin() {
            return origin;
        }

        public AILType getTarget() {
            return target;
        }

        public ParseInvoker getInvoker() {
            return invoker;
        }
    }

    public enum CastProcess {
        INT_TO_DOUBLE(AILType.INT, AILType.DOUBLE, I2D),
        INT_TO_BYTE(AILType.INT, AILType.BYTE, I2B),
        INT_TO_FLOAT(AILType.INT, AILType.FLOAT, I2F),
        INT_TO_SHORT(AILType.INT, AILType.SHORT, I2S),
        INT_TO_CHAR(AILType.INT, AILType.CHAR, I2C),
        INT_TO_LONG(AILType.INT, AILType.LONG, I2L),

        DOUBLE_TO_INT(AILType.DOUBLE, AILType.INT, D2I),
        DOUBLE_TO_BYTE(AILType.DOUBLE, AILType.BYTE, D2I, I2B),
        DOUBLE_TO_FLOAT(AILType.DOUBLE, AILType.FLOAT, D2F),
        DOUBLE_TO_SHORT(AILType.DOUBLE, AILType.SHORT, D2I, I2S),
        DOUBLE_TO_CHAR(AILType.DOUBLE, AILType.CHAR, D2I, I2C),
        DOUBLE_TO_LONG(AILType.DOUBLE, AILType.LONG, D2L),

        BOOL_TO_DOUBLE(AILType.BOOL, AILType.DOUBLE, I2D),
        BOOL_TO_FLOAT(AILType.BOOL, AILType.FLOAT, I2F),
        BOOL_TO_SHORT(AILType.BOOL, AILType.SHORT, I2S),
        BOOL_TO_CHAR(AILType.BOOL, AILType.CHAR, I2C),
        BOOL_TO_LONG(AILType.BOOL, AILType.LONG, I2L),

        FLOAT_TO_INT(AILType.FLOAT, AILType.INT, F2I),
        FLOAT_TO_BYTE(AILType.FLOAT, AILType.BYTE, F2I, I2B),
        FLOAT_TO_DOUBLE(AILType.FLOAT, AILType.DOUBLE, F2D),
        FLOAT_TO_SHORT(AILType.FLOAT, AILType.SHORT, F2I, I2S),
        FLOAT_TO_CHAR(AILType.FLOAT, AILType.CHAR, F2I, I2C),
        FLOAT_TO_LONG(AILType.FLOAT, AILType.LONG, F2L),

        LONG_TO_INT(AILType.LONG, AILType.INT, L2I),
        LONG_TO_BYTE(AILType.LONG, AILType.BYTE, L2I, I2B),
        LONG_TO_DOUBLE(AILType.LONG, AILType.DOUBLE, L2D),
        LONG_TO_SHORT(AILType.LONG, AILType.SHORT, L2I, I2S),
        LONG_TO_CHAR(AILType.LONG, AILType.CHAR, L2I, I2C),
        LONG_TO_FLOAT(AILType.LONG, AILType.FLOAT, L2F);

        public static int[] get(AILType origin, AILType target) {
            for (CastProcess process : CastProcess.values()) {
                if (process.getOrigin() == origin
                        && process.getTarget() == target)
                    return process.getOpcodes();
            }
            return new int[]{};
        }

        private final AILType origin;
        private final AILType target;
        private final int[] opcodes;

        CastProcess(AILType origin, AILType target, int... opcodes) {
            this.origin = origin;
            this.target = target;
            this.opcodes = opcodes;
        }

        public AILType getOrigin() {
            return origin;
        }

        public AILType getTarget() {
            return target;
        }

        public int[] getOpcodes() {
            return opcodes;
        }
    }
}
