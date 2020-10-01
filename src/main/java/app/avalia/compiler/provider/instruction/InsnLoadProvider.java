package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILArgument;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.content.AILTypeContent;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;

import java.util.Optional;

public class InsnLoadProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        Optional<AILTypeContent> content = component.asType(0);
        AILType type;
        if (content.isPresent())
            type = content.get().getType();
        else type = visitor.stack().byId(component.getId());

        if (type != null) {
            System.out.println("LOADED " + type.name() + " AT " + component.getId());
        } else System.out.println("LOADED NULL AT " + component.getId());

        visitor.current().visitVarInsn(type.toLoadInsn(), component.getId());
        visitor.stack().push(type);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
