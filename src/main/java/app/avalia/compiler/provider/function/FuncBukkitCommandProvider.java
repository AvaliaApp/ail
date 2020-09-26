package app.avalia.compiler.provider.function;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.AILInstruction;
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
        visitor.current().visitCode();

    }

    @Override
    public void end(BytecodeVisitor visitor, AILFunction component) {
        visitor.visitPushInsn(AILType.BOOL, visitor.current(), 1);
        visitor.current().visitInsn(AILType.BOOL.toRetInsn());
        visitor.current().visitMaxs(0, 0);
        visitor.current().visitEnd();
    }

    private String decapitalize(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }
}
