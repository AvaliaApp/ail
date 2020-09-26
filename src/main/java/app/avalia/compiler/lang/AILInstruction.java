package app.avalia.compiler.lang;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AILInstruction extends AILComponent {

    private String name;

    private boolean hasId;
    private int id;

    private boolean hasInnerInstructions;
    private final List<AILInstruction> instructions = new ArrayList<>();

    private final List<AILArgument> arguments = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasId() {
        return hasId;
    }

    public void setHasId(boolean hasId) {
        this.hasId = hasId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean hasInnerInstructions() {
        return hasInnerInstructions;
    }

    public void setHasInnerInstructions(boolean hasInnerInstructions) {
        this.hasInnerInstructions = hasInnerInstructions;
    }

    public List<AILInstruction> getInstructions() {
        return instructions;
    }

    public List<AILArgument> getArguments() {
        return arguments;
    }
}
