package app.avalia.compiler.pool;

import app.avalia.compiler.lang.AILInstruction;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.instruction.*;
import app.avalia.compiler.provider.instruction.config.*;

import java.util.HashMap;
import java.util.Map;

public class BaseInstructions {

    private static final Map<String, AILProvider<AILInstruction>> instructions = new HashMap<>();

    public static void load() {
        // Compile-time Instructions
        instructions.put("ailv", new InsnCompilerVersionProvider());
        instructions.put("cfgv", new InsnConfigVersionProvider());
        instructions.put("cfgn", new InsnConfigNameProvider());
        instructions.put("epool", new InsnEventPoolProvider());
        instructions.put("ipool", new InsnInvokePoolProvider());
        instructions.put("cpool", new InsnCommandPoolProvider());

        // Bytecode Instructions
        instructions.put("push", new InsnPushProvider());
        instructions.put("store", new InsnStoreProvider());
        instructions.put("load", new InsnLoadProvider());
        instructions.put("init", new InsnInitProvider());
        instructions.put("jmp", new InsnJumpProvider());
        instructions.put("label", new InsnLabelProvider());
        instructions.put("invoke", new InsnInvokeProvider());
        instructions.put("inc", new InsnIncrementProvider());
        instructions.put("if", new InsnIfProvider());
        instructions.put("nvar", new InsnNvarProvider());
        instructions.put("ret", new InsnReturnProvider());
        instructions.put("clis", new InsnCommandListenProvider());
        instructions.put("new", new InsnNewProvider());
        instructions.put("cast", new InsnCastProvider());
        instructions.put("print", new InsnPrintProvider());
    }

    public static AILProvider<AILInstruction> getProvider(String insnName) {
        return instructions.get(insnName);
    }

}
