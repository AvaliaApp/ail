package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILTypeContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;

import java.util.Optional;

public class InsnStoreProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        Optional<AILTypeContent> content = component.asType(0);
        if (content.isPresent()) {
            AILType type = content.get().getType();
            AILType[] lastArr = StackObserver.last(1);
            if (lastArr.length == 0)
                return; // todo error handling
            AILType last = lastArr[0];

            System.out.println(type.name() + ", " + last.name());

            // todo check if auto-casting is enabled
            if (type != last)
                InsnCastProvider.visitCast(visitor, last, type);

            visitor.current().visitVarInsn(type.toStoreInsn(),
                    component.getId());
            StackObserver.store(component.getId(), type);
            return;
        }

        AILType[] last = StackObserver.last(1);
        if (last.length == 0)
            return; // todo error handling

        visitor.current().visitVarInsn(last[0].toStoreInsn(), component.getId());
        StackObserver.store(component.getId(), last[0]);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {
    }
}
