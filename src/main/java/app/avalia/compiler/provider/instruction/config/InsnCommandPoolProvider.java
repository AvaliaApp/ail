package app.avalia.compiler.provider.instruction.config;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILDelegateContent;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.CommandPoolInfo;
import app.avalia.compiler.pool.BaseFunctions;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.function.FuncBukkitCommandProvider;

public class InsnCommandPoolProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        CommandPoolInfo info = new CommandPoolInfo();

        String target = component.asDelegate(0).get().getDelegateTarget();
        String name = component.asValue(1).get().getContent().toString();

        info.setTarget(target);
        info.setName(name);

        BaseFunctions.push(target, new FuncBukkitCommandProvider());

        BasePoolProvider.getCommandPool().push(info.getTarget(), info);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
