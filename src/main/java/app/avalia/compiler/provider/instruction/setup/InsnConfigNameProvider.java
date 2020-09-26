package app.avalia.compiler.provider.instruction.setup;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.pool.PoolProvider;
import app.avalia.compiler.provider.AILProvider;

public class InsnConfigNameProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        AILArgument argument = component.getArguments().get(0);
        AILValueContent content = (AILValueContent) argument.getContent();
        AILType type = content.getType();
        String val = content.getContent().toString();

        PoolProvider.setPluginName(val);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
