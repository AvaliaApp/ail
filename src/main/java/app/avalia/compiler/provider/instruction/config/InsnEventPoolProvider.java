package app.avalia.compiler.provider.instruction.config;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.pool.BaseFunctions;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.function.FuncBukkitEventProvider;

public class InsnEventPoolProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        int id = component.getId();

        String target = component.asDelegate(0).get().getDelegateTarget();
        String value = component.asValue(1).get().getContent().toString();

        FuncBukkitEventProvider provider = new FuncBukkitEventProvider();
        provider.setDescriptor(value);
        BaseFunctions.push(target, provider);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
