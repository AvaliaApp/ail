package app.avalia.compiler;

import app.avalia.compiler.asm.BytecodeVisitor;
import app.avalia.compiler.asm.LabelStack;
import app.avalia.compiler.jar.JarPackager;
import app.avalia.compiler.lang.*;
import app.avalia.compiler.pool.set.AILFunctionSet;
import app.avalia.compiler.pool.set.AILInstructionSet;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.ClassProvider;

import java.io.IOException;
import java.util.Collection;

public class AILCompiler {

    public static final String COMPILER_VERSION = "b100";

    public static void compile(AILClass traversedClass) {
        BytecodeVisitor visitor = new BytecodeVisitor();

        ClassProvider classProvider = new ClassProvider();
        classProvider.begin(visitor, traversedClass);
        {
            recursiveInstructionCompile(visitor, traversedClass.getInstructions());

            for (AILFunction function : traversedClass.getFunctions()) {
                AILProvider<AILFunction> provider = AILFunctionSet.getProvider(function.getName());
                provider.begin(visitor, function);
                {
                    recursiveInstructionCompile(visitor, function.getInstructions());
                }
                provider.end(visitor, function);

                LabelStack.clear();
            }
        }
        classProvider.end(visitor, traversedClass);

        byte[] bytecode = visitor.get().toByteArray();
        try {
            JarPackager.pack(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void recursiveInstructionCompile(BytecodeVisitor visitor, Collection<AILInstruction> instructions) {
        if (instructions.isEmpty())
            return;

        for (AILInstruction instruction : instructions) {
            AILProvider<AILInstruction> insnProvider = AILInstructionSet.getProvider(instruction.getName());
            insnProvider.begin(visitor, instruction);
            {
                recursiveInstructionCompile(visitor, instruction.getInstructions());
            }
            insnProvider.end(visitor, instruction);
        }
    }

}
