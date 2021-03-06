package app.avalia.compiler.provider.function;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;

public class FuncBukkitCommandProvider implements AILProvider<AILFunction> {
    @Override
    public void parse(AILFunction component) {
    }

    @Override
    public void begin(BytecodeVisitor visitor, AILFunction component) {
        visitor.visitMethod(decapitalize(component.getName()),
                "(Lorg/bukkit/command/CommandSender;" +
                        "Lorg/bukkit/command/Command;Ljava/lang/String;" +
                        "[Ljava/lang/String;)Z");
        visitor.stack().store(0, AILType.REF); // reference to self
        visitor.stack().store(1, AILType.REF);
        visitor.stack().store(2, AILType.REF);
        visitor.stack().store(3, AILType.TEXT);
        visitor.stack().store(4, AILType.REF); // todo should be text array instead of ref
        visitor.current().visitCode();

    }

    @Override
    public void end(BytecodeVisitor visitor, AILFunction component) {
        visitor.visitPushInsn(AILType.BOOL, true);
        visitor.current().visitInsn(AILType.BOOL.toRetInsn());
        visitor.current().visitMaxs(0, 0);
        visitor.current().visitEnd();
    }

    private String decapitalize(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }
}
