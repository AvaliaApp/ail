package app.avalia.compiler.provider.instruction;

import app.avalia.compiler.AILCompiler;
import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.lang.type.AILType;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.attribute.IgnoreInnerInstructions;
import org.objectweb.asm.Opcodes;

import java.util.Collections;

@IgnoreInnerInstructions
public class InsnFormatProvider implements AILProvider<AILInstruction> {
    @Override
    public void parse(AILInstruction component) {

    }

    @Override
    public void begin(BytecodeVisitor visitor, AILInstruction component) {
        String val = component.asValue(0).get().getContent().toString();
        int innerSize = component.getInstructions().size();

        visitor.current().visitLdcInsn(val);
        visitor.visitPushInsn(AILType.INT, innerSize);
        visitor.current().visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/Object");
        visitor.current().visitInsn(Opcodes.DUP);

        int index = 0;
        for (AILInstruction instruction : component.getInstructions()) {
            int stackSize = visitor.stack().getStackSize(); // store old stack size

            AILCompiler.recursiveInstructionCompile(visitor, Collections.singletonList(instruction));

            // detect if stack size changed
            if (stackSize > visitor.stack().getStackSize()) {

                index++;
            }
        }

    }

    @Override
    public void end(BytecodeVisitor visitor, AILInstruction component) {

    }
}
