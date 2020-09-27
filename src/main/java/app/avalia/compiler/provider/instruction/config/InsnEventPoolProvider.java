package app.avalia.compiler.provider.instruction.config;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILDelegateContent;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.pool.BaseFunctions;
import app.avalia.compiler.pool.info.EventPoolInfo;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.function.FuncBukkitEventProvider;

public class InsnEventPoolProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        int id = component.getId();

        EventPoolInfo eventPool = new EventPoolInfo();

        AILArgument argument = component.getArguments().get(0);
        AILDelegateContent content = (AILDelegateContent) argument.getContent();
        String target = content.getDelegateTarget();

        eventPool.setName(target);

        AILArgument argument1 = component.getArguments().get(1);
        AILValueContent content1 = (AILValueContent) argument1.getContent();
        String value = content1.getContent().toString();

        eventPool.setDescriptor(value);

        FuncBukkitEventProvider provider = new FuncBukkitEventProvider();
        provider.setDescriptor(value);
        BaseFunctions.push(target, provider);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
