package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Opcodes;

public class InsnPrintProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        visitor.current().visitFieldInsn(Opcodes.GETSTATIC,
                "java/lang/System",
                "out", "Ljava/io/PrintStream;");
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
        AILType type = StackObserver.last(1)[0];

        InsnCastProvider.visitCast(visitor, type, AILType.TEXT);

        visitor.current().visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream",
                "println",
                "(Ljava/lang/String;)V", false);
    }
}
