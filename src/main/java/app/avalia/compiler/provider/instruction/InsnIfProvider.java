package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Label;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.IFNE;

public class InsnIfProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    private Label label;

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        String val = component.asValue(0).get().getContent().toString();

        label = new Label();
        visitor.current().visitJumpInsn(getCondition(visitor, val), label);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
        visitor.current().visitLabel(label);
    }

    public static int getCondition(BytecodeVisitor visitor, String str) {
        for (IfType type : IfType.values()) {
            if (!type.str.equals(str))
                continue;
            visitor.stack().pop(type.pops);
            return type.opcode;
        }
        return -1;
    }

    public enum IfType {
        NU("==n", IFNONNULL, 1),
        NN("!=n", IFNULL, 1),
        GT(">", IF_ICMPLE, 2),
        LT("<", IF_ICMPGE, 2),
        GE(">=",  IF_ICMPLT, 2),
        LE("<=",  IF_ICMPGT, 2),
        EQ("==", IF_ICMPNE, 2),
        NE("!=", IF_ICMPEQ, 2),
        ZGT(">0", IFLE, 1),
        ZLT("<0", IFGE, 1),
        ZGE(">=0",  IFLT, 1),
        ZLE("<=0",  IFGT, 1),
        ZEQ("==0", IFNE, 1),
        ZNE("!=0", IFEQ, 1);

        private final int opcode;
        private final String str;
        private final int pops;

        IfType(String str, int opcode, int pops) {
            this.str = str;
            this.opcode = opcode;
            this.pops = pops;
        }

        public int getOpcode() {
            return opcode;
        }

        public String getStr() {
            return str;
        }

        public int getPops() {
            return pops;
        }
    }
}
