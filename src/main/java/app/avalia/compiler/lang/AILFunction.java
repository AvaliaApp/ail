package app.avalia.compiler.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles information about a single function and it's inner instructions
 */
public class AILFunction extends AILComponent {

    private final List<AILInstruction> instructions = new ArrayList<>();

    private String name;

    public List<AILInstruction> getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
