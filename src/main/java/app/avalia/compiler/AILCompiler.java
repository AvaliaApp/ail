package app.avalia.compiler;

import app.avalia.compiler.bytecode.BytecodeVisitor;
import app.avalia.compiler.bytecode.observer.LabelObserver;
import app.avalia.compiler.bytecode.observer.StackObserver;
import app.avalia.compiler.lang.*;
import app.avalia.compiler.lang.error.AILErrorLogger;
import app.avalia.compiler.pool.BaseFunctions;
import app.avalia.compiler.pool.BaseInstructions;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.ClassProvider;
import app.avalia.compiler.provider.attribute.IgnoreInnerInstructions;

import java.util.Collection;

public class AILCompiler {

    public static final String COMPILER_VERSION = "b100";

    public static byte[] compile(AILClass traversedClass) {
        BytecodeVisitor visitor = new BytecodeVisitor();

        ClassProvider classProvider = new ClassProvider();
        classProvider.begin(visitor, traversedClass);
        {
            recursiveInstructionCompile(visitor, traversedClass.getInstructions());

            for (AILFunction function : traversedClass.getFunctions()) {
                AILProvider<AILFunction> provider = BaseFunctions.getProvider(function.getName());
                provider.begin(visitor, function);
                {
                    if (!function.hasAttribute(IgnoreInnerInstructions.class))
                        recursiveInstructionCompile(visitor, function.getInstructions());
                }
                provider.end(visitor, function);

                visitor.stack().flush();

                if (!visitor.stack().isEmpty())
                    AILErrorLogger.logError(function.getLine(), "type stack is not empty "
                            + "(" + visitor.stack().getStackSize() + " > 0)");

                visitor.stack().flush();
            }
        }
        classProvider.end(visitor, traversedClass);

        return visitor.get().toByteArray();
    }

    public static void recursiveInstructionCompile(BytecodeVisitor visitor, Collection<AILInstruction> instructions) {
        if (instructions.isEmpty())
            return;

        for (AILInstruction instruction : instructions) {
            AILProvider<AILInstruction> insnProvider = BaseInstructions.getProvider(instruction.getName());
            insnProvider.begin(visitor, instruction);
            {
                if (!instruction.hasAttribute(IgnoreInnerInstructions.class))
                    recursiveInstructionCompile(visitor, instruction.getInstructions());
            }
            insnProvider.end(visitor, instruction);
        }
    }

}
