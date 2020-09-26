package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILTypeContent;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Opcodes;

public class InsnReturnProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        if (component.getArguments().isEmpty()) {
            visitor.current().visitInsn(Opcodes.RETURN);
            return;
        }

        AILArgument argument = component.getArguments().get(0);
        AILTypeContent content = (AILTypeContent) argument.getContent();
        AILType type = content.getType();

        visitor.current().visitInsn(type.toRetInsn());
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
