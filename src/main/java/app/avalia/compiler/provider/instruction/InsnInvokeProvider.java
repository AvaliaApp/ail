package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeDescriptors;
import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.pool.BasePoolProvider;
import app.avalia.compiler.pool.info.InvokePoolInfo;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Opcodes;

public class InsnInvokeProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        int id = component.getId();

        InvokePoolInfo pool = BasePoolProvider.getInvokePool().get(id);

        BytecodeDescriptors.BytecodeMethodInfo info =
                BytecodeDescriptors.parse(pool.getMethodSig());

        StackObserver.pop(info.getArguments().size());
        if (pool.getInvokeType() != Opcodes.INVOKESTATIC)
            StackObserver.pop(1);

//        AILType[] lastArr = StackObserver.last(info.getArguments().size());
//        for (int i = 0; i < lastArr.length; i++) {
//            AILType last = lastArr[i];
//            AILType arg = info.getArguments().get(i);
//
//            if (last != arg)
//                InsnCastProvider.visitCast(visitor, last, arg);
//        }

        visitor.current().visitMethodInsn(
                pool.getInvokeType(),
                pool.getInstanceSig(),
                pool.getMethodName(),
                pool.getMethodSig(), pool.getInvokeType() == Opcodes.INVOKEINTERFACE);

        if (!info.isVoid())
            StackObserver.push(info.getReturnType());
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
