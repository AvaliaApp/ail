package app.avalia.compiler.provider.instruction.config;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.provider.AILProvider;

public class InsnConfigVersionProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        String val = component.asValue(0).get().getContent().toString();

        BasePoolProvider.setPluginVersion(val);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
