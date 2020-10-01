package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.LabelObserver;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.provider.AILProvider;

public class InsnLabelProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        visitor.current().visitLabel(
                visitor.label().visit(visitor.current(), component.getId()));
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
    }
}
