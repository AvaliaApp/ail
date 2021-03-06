package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.LabelObserver;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;

public class InsnJumpProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        Label label = visitor.label().markOrGet(component.getId());
        visitor.current().visitJumpInsn(Opcodes.GOTO, label);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
