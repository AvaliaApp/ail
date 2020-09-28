package app.avalia.compiler.provider.function;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Opcodes;

public class FuncEnableEventProvider implements AILProvider<AILFunction> {
    @Override
    public void parse(AILFunction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILFunction component) {
        visitor.visitMethod("onEnable", "()V");
        StackObserver.store(0, AILType.REF); // reference to self
    }

    @Override
    public void end(BytecodeVisitor visitor, AILFunction component) {
        visitor.current().visitInsn(Opcodes.RETURN);
        visitor.current().visitMaxs(0, 0);
        visitor.current().visitEnd();
    }
}
