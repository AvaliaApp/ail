package app.avalia.compiler.pool;

import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.instruction.*;
import app.avalia.compiler.provider.instruction.config.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores information about the instruction set
 */
public class BaseInstructions {

    private static final Map<String, AILProvider<AILInstruction>> instructions = new HashMap<>();

    static {
        // Compile-time Instructions
        push("ailv", new InsnCompilerVersionProvider());
        push("cfgv", new InsnConfigVersionProvider());
        push("cfgn", new InsnConfigNameProvider());
        push("epool", new InsnEventPoolProvider());
        push("ipool", new InsnInvokePoolProvider());
        push("cpool", new InsnCommandPoolProvider());

        // Bytecode Instructions
        push("push", new InsnPushProvider());
        push("store", new InsnStoreProvider());
        push("load", new InsnLoadProvider());
        push("init", new InsnInitProvider());
        push("jmp", new InsnJumpProvider());
        push("label", new InsnLabelProvider());
        push("invoke", new InsnInvokeProvider());
        push("inc", new InsnIncrementProvider());
        push("if", new InsnIfProvider());
        push("nvar", new InsnNvarProvider());
        push("ret", new InsnReturnProvider());
        push("clis", new InsnCommandListenProvider());
        push("new", new InsnNewProvider());
        push("cast", new InsnCastProvider());
        push("print", new InsnPrintProvider());
    }

    public static void push(String name, AILProvider<AILInstruction> provider) {
        instructions.put(name, provider);
    }

    public static AILProvider<AILInstruction> getProvider(String insnName) {
        return instructions.get(insnName);
    }

}
