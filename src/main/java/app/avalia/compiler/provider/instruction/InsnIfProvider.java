package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
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
        AILArgument argument = component.getArguments().get(0);
        AILValueContent content = (AILValueContent) argument.getContent();
        AILType type = content.getType();
        String val = content.getContent().toString();

        label = new Label();
        visitor.current().visitJumpInsn(getCondition(val), label);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
        visitor.current().visitLabel(label);
    }

    public static int getCondition(String str) {
        for (IfType type : IfType.values()) {
            if (!type.str.equals(str))
                continue;
            return type.opcode;
        }
        return -1;
    }

    public enum IfType {
        NU("=n", IFNONNULL),
        NN("!n", IFNULL),
        GT(">", IF_ICMPLE),
        LT("<", IF_ICMPGE),
        GE(">=",  IF_ICMPLT),
        LE("<=",  IF_ICMPGT),
        EQ("==", IF_ICMPNE),
        NE("!=", IF_ICMPEQ),
        ZGT(">0", IFLE),
        ZLT("<0", IFGE),
        ZGE(">=0",  IFLT),
        ZLE("<=0",  IFGT),
        ZEQ("==0", IFNE),
        ZNE("!=0", IFEQ);

        int opcode;
        String str;

        IfType(String str, int opcode) {
            this.str = str;
            this.opcode = opcode;
        }
    }
}
