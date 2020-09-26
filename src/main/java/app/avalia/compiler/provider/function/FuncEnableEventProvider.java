package app.avalia.compiler.provider.function;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.provider.AILProvider;

public class FuncEnableEventProvider implements AILProvider<AILFunction> {
    @Override
    public void parse(AILFunction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILFunction component) {
        visitor.visitMethod("OnEnable", "()V");
    }

    @Override
    public void end(BytecodeVisitor visitor, AILFunction component) {
        visitor.current().visitMaxs(0, 0);
        visitor.current().visitEnd();
    }
}
