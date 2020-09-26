package app.avalia.compiler.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AILClass extends AILComponent {

    private final List<AILFunction> functions = new ArrayList<>();
    private final List<AILInstruction> instructions = new ArrayList<>();

    public List<AILFunction> getFunctions() {
        return functions;
    }

    public List<AILInstruction> getInstructions() {
        return instructions;
    }
}
