package app.avalia.compiler.pool.set;

import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.function.FuncEnableEventProvider;

import java.util.HashMap;
import java.util.Map;

public class AILFunctionSet {

    private static final Map<String, AILProvider<AILFunction>> functions = new HashMap<>();

    public static void load() {
        push("OnEnable", new FuncEnableEventProvider());
    }

    public static void push(String name, AILProvider<AILFunction> provider) {
        functions.put(name, provider);
    }

    public static AILProvider<AILFunction> getProvider(String funcName) {
        return functions.get(funcName);
    }

}
