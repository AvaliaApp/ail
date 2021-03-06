package app.avalia.compiler.provider.instruction.config;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.InvokePoolInfo;
import app.avalia.compiler.provider.AILProvider;

import java.util.ArrayList;
import java.util.List;

public class InsnInvokePoolProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        int id = component.getId();

        InvokePoolInfo invokePool = new InvokePoolInfo(id);
        List<String> arguments = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            AILValueContent content = component.asValue(i).get();

            arguments.add(content.getContent().toString());
        }

        invokePool.setInvokeType(arguments.get(0));
        invokePool.setMethodName(arguments.get(1));
        invokePool.setInstanceSig(arguments.get(2));
        invokePool.setMethodSig(arguments.get(3));

        BasePoolProvider.getInvokePool().push(id, invokePool);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
