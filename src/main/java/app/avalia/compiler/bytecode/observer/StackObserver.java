package app.avalia.compiler.bytecode.observer;

import app.avalia.compiler.lang.type.AILType;

import java.util.*;

/**
 * Observes the stack changes and it's balance throughout the bytecode generation
 * It also stores variable (and it's corresponding id) type information for auto type matching/casting
 */
public class StackObserver {

    private final Queue<AILType> typeQueue = new LinkedList<>();
    private final Map<Integer, AILType> typeContainer = new HashMap<>();

    /**
     * Pushes a type on stack
     * @param type {@link AILType}
     */
    public void push(AILType type) {
        typeQueue.offer(type);
    }

    /**
     * Stores a variable type information
     * @param id Variable id
     * @param type {@link AILType}
     */
    public void store(int id, AILType type) {
        typeContainer.put(id, type);
    }

    /**
     * Gets the variable type
     * @param id Variable id
     * @return {@link AILType}
     */
    public AILType byId(int id) {
        return typeContainer.get(id);
    }

    /**
     * Pops a type off the stack queue
     * @param amount Amount of pops
     */
    public void pop(int amount) {
        for (int i = 0; i < amount; i++)
            typeQueue.poll();
    }

    /**
     * Polls types off the stack queue
     * @param amount Poll depth
     * @return Array of {@link AILType}
     */
    public AILType[] last(int amount) {
        AILType[] types = new AILType[amount];
        for (int i = 0; i < amount; i++) {
            types[i] = typeQueue.poll();
        }
        return types;
    }

    /**
     * Clears the local variable type information and the stack queue
     */
    public void flush() {
        typeQueue.clear();
        typeContainer.clear();
    }

    /**
     * @return True if the stack queue is empty
     */
    public boolean isEmpty() {
        return typeQueue.isEmpty();
    }

    /**
     * @return Stack queue size
     */
    public int getStackSize() {
        return typeQueue.size();
    }

}
