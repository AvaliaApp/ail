package app.avalia.compiler.provider.function;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.provider.AILProvider;
import org.objectweb.asm.Opcodes;

public class FuncBukkitEventProvider implements AILProvider<AILFunction> {

    private String descriptor;

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public void parse(AILFunction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILFunction component) {
        visitor.visitEvent(decapitalize(component.getName()), descriptor);
    }

    @Override
    public void end(BytecodeVisitor visitor, AILFunction component) {
        visitor.current().visitInsn(Opcodes.RETURN);
        visitor.current().visitMaxs(0, 0);
        visitor.current().visitEnd();
    }

    private String decapitalize(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }
}
