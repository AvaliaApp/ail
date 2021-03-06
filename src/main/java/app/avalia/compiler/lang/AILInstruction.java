package app.avalia.compiler.lang;

import app.avalia.compiler.lang.content.AILDelegateContent;
import app.avalia.compiler.lang.content.AILTypeContent;
import app.avalia.compiler.lang.content.AILValueContent;
import app.avalia.compiler.lang.type.AILType;

import java.util.*;

/**
 * Handles information about a single instruction, it's inner instructions and it's arguments
 */
public class AILInstruction extends AILComponent {

    private String name;

    private boolean hasId;
    private int id;

    private boolean compileInnerInstructions;
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

    public boolean isCompileInnerInstructions() {
        return compileInnerInstructions;
    }

    public void setCompileInnerInstructions(boolean compileInnerInstructions) {
        this.compileInnerInstructions = compileInnerInstructions;
    }

    public List<AILInstruction> getInstructions() {
        return instructions;
    }

    public List<AILArgument> getArguments() {
        return arguments;
    }

    /**
     * Gets an argument at position as value type
     * @param position Argument index
     * @return {@link AILValueContent} if it exists
     */
    public Optional<AILValueContent> asValue(int position) {
        if (position >= this.getArguments().size())
            return Optional.empty();

        AILArgument argument = this.getArguments()
                .get(position);

        return Optional.of((AILValueContent)argument.getContent());
    }

    /**
     * Gets an argument at position as type-type
     * @param position Argument index
     * @return {@link AILTypeContent} if it exists
     */
    public Optional<AILTypeContent> asType(int position) {
        if (position >= this.getArguments().size())
            return Optional.empty();

        AILArgument argument = this.getArguments()
                .get(position);

        return Optional.of((AILTypeContent)argument.getContent());
    }

    /**
     * Gets an argument at position as delegate type
     * @param position Argument index
     * @return {@link AILDelegateContent} if it exists
     */
    public Optional<AILDelegateContent> asDelegate(int position) {
        if (position >= this.getArguments().size())
            return Optional.empty();

        AILArgument argument = this.getArguments()
                .get(position);

        return Optional.of((AILDelegateContent)argument.getContent());
    }
}
