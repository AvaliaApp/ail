package app.avalia.compiler.provider.instruction.setup;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILDelegateContent;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.pool.PoolProvider;
import app.avalia.compiler.pool.data.CommandPoolInfo;
import app.avalia.compiler.pool.set.AILFunctionSet;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.function.FuncBukkitCommandProvider;

import java.util.ArrayList;
import java.util.List;

public class InsnCommandPoolProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        CommandPoolInfo info = new CommandPoolInfo();

        AILArgument argument = component.getArguments().get(0);
        AILDelegateContent content = (AILDelegateContent) argument.getContent();
        String target = content.getDelegateTarget();

        AILArgument argument1 = component.getArguments().get(1);
        AILValueContent content1 = (AILValueContent) argument1.getContent();
        String name = content1.getContent().toString();

        info.setTarget(target);
        info.setName(name);

        AILFunctionSet.push(target, new FuncBukkitCommandProvider());

        PoolProvider.getCommandPool().push(info.getTarget(), info);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
