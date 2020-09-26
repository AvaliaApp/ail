package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.provider.AILProvider;

public class InsnInitProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        visitor.visitInit();
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
