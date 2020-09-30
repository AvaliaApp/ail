package app.avalia.compiler.pool;

import app.avalia.compiler.lang.AILFunction;
import app.avalia.compiler.provider.AILProvider;
import app.avalia.compiler.provider.function.FuncEnableEventProvider;

import java.util.HashMap;
import java.util.Map;

public class BaseFunctions {

    private static final Map<String, AILProvider<AILFunction>> functions = new HashMap<>();

    static {
        push("OnEnable", new FuncEnableEventProvider());
    }

    public static void push(String name, AILProvider<AILFunction> provider) {
        functions.put(name, provider);
    }

    public static AILProvider<AILFunction> getProvider(String funcName) {
        return functions.get(funcName);
    }

}
