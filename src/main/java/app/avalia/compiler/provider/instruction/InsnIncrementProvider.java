package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;

public class InsnIncrementProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        int value = (int) component.asValue(0).get().getContent();

        visitor.current().visitIincInsn(component.getId(), value);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
